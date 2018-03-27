-- =============================================
-- Procedure to handle errors in the Officer file(s)
-- =============================================
-- creating the store procedure
IF EXISTS (SELECT name 
	   FROM   sysobjects 
	   WHERE  name = N'sp_errorOfficer' 
	   AND 	  type = 'P')
    DROP PROCEDURE sp_errorOfficer
GO

CREATE PROCEDURE sp_errorOfficer 
AS
DECLARE @aff_pk int,
	@user_pk integer, 
	@Administrator int,
	@ExecDirector int, 
	@FRO int
DECLARE @errorCode int

SET @user_pk = (SELECT person_pk FROM users WHERE user_id = 'migration')

SET @Administrator = (SELECT com_cd_pk FROM DM_Code_Mapping_view 
		       WHERE com_cd_type_key = 'AFSCMETitle' 
		         AND com_cd_desc = 'Administrator')
SET @ExecDirector = (SELECT com_cd_pk FROM DM_Code_Mapping_view 
		      WHERE com_cd_type_key = 'AFSCMETitle' 
		        AND com_cd_desc = 'Executive Director')
SET @FRO = (SELECT com_cd_pk FROM DM_Code_Mapping_view 
	     WHERE com_cd_type_key = 'AFSCMETitle' 
	       AND com_cd_desc = 'Financial Reporting Officer')

BEGIN TRAN sp_errorOfficer

-- DELETE FROM DM_migrateOfficer_ERROR
-- Move everything to DM_migrateOfficer table and do initial prep
-- (remove leading zeros)
	INSERT INTO DM_migrateOfficer
		(leg_person_pk, aff_pk, MemberId, Affiliate_Type, 
		Affiliate_State, Affiliate_Council, Affiliate_Local, Affiliate_Seq, 
		SubLocal, AffiliateId, UnitCode, TitleCodeId, TitleCodeSeq, Gender, 
		Salutation, LastName, FirstName, MiddleName, Suffix, Home_Addr1, 
		Home_Addr2, Home_City, Home_State, Home_ZipCode, Home_Zip4, Home_Province, 
		Home_Country, Work_Addr1, Work_Addr2, Work_City, Work_State, Work_ZipCode, 
		Work_Zip4, Work_Province, Work_Country, Email, Phone_Number, 
		Office_Mail_Code, Office_Exp_Date, Last_Filled_Dt, Last_Update_Dt)
	 SELECT NULL, NULL, MemberId, Affiliate_Type, Affiliate_State, 
		CASE CAST(Affiliate_Council AS int) 
		WHEN 0 THEN '' 
			ELSE CAST(CAST(Affiliate_Council AS int) AS varchar(4)) END,
		CASE CAST(Affiliate_Local AS int) 
			WHEN 0 THEN '' 
			ELSE CAST(CAST(Affiliate_Local AS int) AS varchar(4)) END,
		COALESCE(Affiliate_Seq, ''), 
		CASE WHEN Substring(SubLocal, 1, 4) = '0000' THEN '' 
		     WHEN Substring(SubLocal, 1, 3) = '000' THEN Substring(SubLocal, 4, 1 )
		     WHEN Substring(SubLocal, 1, 2) = '00' THEN Substring(SubLocal, 3, 2 )
		     WHEN Substring(SubLocal, 1, 1) = '0' THEN Substring(SubLocal, 2, 3 )
		     ELSE SubLocal END,
		AffiliateId, UnitCode, 		
		(SELECT com_cd_pk FROM DM_Code_Mapping_view 
		  WHERE com_cd_type_key = 'AFSCMETitle' 
		    AND Legacy_Code = TitleCodeId),
		TitleCodeSeq, Gender, 
		Salutation, LastName, FirstName, MiddleName, Suffix, 
		Home_Addr1, Home_Addr2, Home_City, Home_State, Home_ZipCode, 
		Home_Zip4, Home_Province, Home_Country, Work_Addr1, Work_Addr2, 
		Work_City, Work_State, Work_ZipCode, Work_Zip4, Work_Province, 
		Work_Country, Email, Phone_Number, Office_Mail_Code, 
		CASE WHEN Office_Exp_Date = '000000' OR Office_Exp_Date = '999999' THEN NULL
		     ELSE Office_Exp_Date END,
		CASE WHEN Last_Filled_Dt = '000000' OR Last_Filled_Dt = '00000000' THEN NULL
		     ELSE Last_Filled_Dt END, 
		CASE WHEN Last_Update_Dt = '000000' OR Last_Update_Dt = '00000000' THEN NULL
		     ELSE Last_Update_Dt END 
	   FROM LEG_Officer
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

-- Clean up data for dates
	-- Not needed with latest data provided.  Latest data has DateTime format
/*	 UPDATE DM_migrateOfficer
	    SET Office_Exp_Date = NULLIF(Office_Exp_Date, '')
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR
	
	 UPDATE DM_migrateOfficer
	    SET Last_Filled_Dt = SubString(Last_Filled_Dt, 1, 2)+'/'+
		SubString(Last_Filled_Dt, 3, 2)+'/'+SubString(Last_Filled_Dt, 5, 4)	
	  WHERE Len(Last_Filled_Dt) = 8
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	 UPDATE DM_migrateOfficer
	    SET Last_Filled_Dt = NULLIF(Last_Filled_Dt, ''),
		Office_Exp_Date = NULLIF(Office_Exp_Date, ''), 
		Last_Update_Dt = NULLIF(Last_Update_Dt, '')
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR
	
	 UPDATE DM_migrateOfficer
	    SET Last_Filled_Dt = CASE WHEN SubString(Last_Filled_Dt, 4, 2) = '00' 
			THEN SubString(Last_Filled_Dt, 1, 3)+'01'+SubString(Last_Filled_Dt, 6, 5) 
			ELSE Last_Filled_Dt END,
		Last_Update_Dt = CASE WHEN SubString(Last_Update_Dt, 4, 2) = '00' 
			THEN SubString(Last_Update_Dt, 1, 3)+'01'+SubString(Last_Update_Dt, 6, 5) 
			ELSE Last_Update_Dt END
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR
*/
-->> Get Primary Keys
	-- person_pk (requirements only call to use Member ID)
	UPDATE o
	   SET o.leg_person_pk = p.leg_person_pk
	  FROM DM_migratePerson p
	  JOIN DM_migrateOfficer o ON p.MemberId = o.MemberId
--	 WHERE p.MemberId IS NOT NULL
--	   AND o.MemberId IS NOT NULL 
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Affiliate Identifier
	UPDATE o
	   SET o.aff_pk = a.aff_pk
	  FROM DM_migrateOfficer o
	  JOIN Aff_Organizations a ON Affiliate_Type = aff_type
	   AND Affiliate_Local = aff_localSubChapter
	   AND Affiliate_State = aff_stateNat_type
	   AND Affiliate_Council = aff_councilRetiree_chap
	   AND SubLocal = aff_subUnit
	   AND Affiliate_Seq = aff_code
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

-->> Check for ERRORs
	-- Invalid Affiliates (SubLocal provided an Affiliate Type other than 'U') 
	INSERT INTO DM_migrateOfficer_ERROR
	 	(leg_officer_pk, leg_person_pk, aff_pk, ERROR,
		MemberId, Affiliate_Type, Affiliate_State, 
		Affiliate_Council, Affiliate_Local, Affiliate_Seq, SubLocal, 
		AffiliateId, UnitCode, TitleCodeId, TitleCodeSeq, Gender, 
		Salutation, LastName, FirstName, MiddleName, Suffix, 
		Home_Addr1, Home_Addr2, Home_City, Home_State, Home_ZipCode, 
		Home_Zip4, Home_Province, Home_Country, Work_Addr1, Work_Addr2, 
		Work_City, Work_State, Work_ZipCode, Work_Zip4, Work_Province, 
		Work_Country, Email, Phone_Number, Office_Mail_Code, 
		Office_Exp_Date, Last_Filled_Dt, Last_Update_Dt)
	 SELECT leg_officer_pk, leg_person_pk, aff_pk, 
		'Invalid Affiliate - SubLocal provided an Affiliate Type other than U',
		MemberId, Affiliate_Type, Affiliate_State, 
		Affiliate_Council, Affiliate_Local, Affiliate_Seq, SubLocal, 
		AffiliateId, UnitCode, TitleCodeId, TitleCodeSeq, Gender, 
		Salutation, LastName, FirstName, MiddleName, Suffix, 
		Home_Addr1, Home_Addr2, Home_City, Home_State, Home_ZipCode, 
		Home_Zip4, Home_Province, Home_Country, Work_Addr1, Work_Addr2, 
		Work_City, Work_State, Work_ZipCode, Work_Zip4, Work_Province, 
		Work_Country, Email, Phone_Number, Office_Mail_Code, 
		Office_Exp_Date, Last_Filled_Dt, Last_Update_Dt
	   FROM DM_migrateOfficer
	  WHERE Affiliate_Type <> 'U' AND SubLocal <> ''

	-- Invalid Affiliates (Local/SubChapter provided an Affiliate Type other than 'L' or 'S') 
	INSERT INTO DM_migrateOfficer_ERROR
	 	(leg_officer_pk, leg_person_pk, aff_pk, ERROR,
		MemberId, Affiliate_Type, Affiliate_State, 
		Affiliate_Council, Affiliate_Local, Affiliate_Seq, SubLocal, 
		AffiliateId, UnitCode, TitleCodeId, TitleCodeSeq, Gender, 
		Salutation, LastName, FirstName, MiddleName, Suffix, 
		Home_Addr1, Home_Addr2, Home_City, Home_State, Home_ZipCode, 
		Home_Zip4, Home_Province, Home_Country, Work_Addr1, Work_Addr2, 
		Work_City, Work_State, Work_ZipCode, Work_Zip4, Work_Province, 
		Work_Country, Email, Phone_Number, Office_Mail_Code, 
		Office_Exp_Date, Last_Filled_Dt, Last_Update_Dt)
	 SELECT leg_officer_pk, leg_person_pk, aff_pk, 
		'Invalid Affiliate - Local/SubChapter provided an Affiliate Type other than L or S',
		MemberId, Affiliate_Type, Affiliate_State, 
		Affiliate_Council, Affiliate_Local, Affiliate_Seq, SubLocal, 
		AffiliateId, UnitCode, TitleCodeId, TitleCodeSeq, Gender, 
		Salutation, LastName, FirstName, MiddleName, Suffix, 
		Home_Addr1, Home_Addr2, Home_City, Home_State, Home_ZipCode, 
		Home_Zip4, Home_Province, Home_Country, Work_Addr1, Work_Addr2, 
		Work_City, Work_State, Work_ZipCode, Work_Zip4, Work_Province, 
		Work_Country, Email, Phone_Number, Office_Mail_Code, 
		Office_Exp_Date, Last_Filled_Dt, Last_Update_Dt
	   FROM DM_migrateOfficer
	  WHERE Affiliate_Type NOT IN ('L','S') AND Affiliate_Local <> ''

	-- Invalid Affiliate PK
	INSERT INTO DM_migrateOfficer_ERROR
	 	(leg_officer_pk, leg_person_pk, aff_pk, ERROR,
		MemberId, Affiliate_Type, Affiliate_State, 
		Affiliate_Council, Affiliate_Local, Affiliate_Seq, SubLocal, 
		AffiliateId, UnitCode, TitleCodeId, TitleCodeSeq, Gender, 
		Salutation, LastName, FirstName, MiddleName, Suffix, 
		Home_Addr1, Home_Addr2, Home_City, Home_State, Home_ZipCode, 
		Home_Zip4, Home_Province, Home_Country, Work_Addr1, Work_Addr2, 
		Work_City, Work_State, Work_ZipCode, Work_Zip4, Work_Province, 
		Work_Country, Email, Phone_Number, Office_Mail_Code, 
		Office_Exp_Date, Last_Filled_Dt, Last_Update_Dt)
	 SELECT leg_officer_pk, leg_person_pk, aff_pk, 'Invalid Affiliate (not in Aff_Organizations)',  
		MemberId, Affiliate_Type, Affiliate_State, 
		Affiliate_Council, Affiliate_Local, Affiliate_Seq, SubLocal, 
		AffiliateId, UnitCode, TitleCodeId, TitleCodeSeq, Gender, 
		Salutation, LastName, FirstName, MiddleName, Suffix, 
		Home_Addr1, Home_Addr2, Home_City, Home_State, Home_ZipCode, 
		Home_Zip4, Home_Province, Home_Country, Work_Addr1, Work_Addr2, 
		Work_City, Work_State, Work_ZipCode, Work_Zip4, Work_Province, 
		Work_Country, Email, Phone_Number, Office_Mail_Code, 
		Office_Exp_Date, Last_Filled_Dt, Last_Update_Dt
	   FROM DM_migrateOfficer o
	  WHERE aff_pk IS NULL 
	    AND NOT EXISTS (SELECT * FROM DM_migrateOfficer_ERROR e WHERE o.leg_officer_pk=e.leg_officer_pk)
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Person does not exist in the System
	INSERT INTO DM_migrateOfficer_ERROR
	 	(leg_officer_pk, leg_person_pk, aff_pk, ERROR,
		MemberId, Affiliate_Type, Affiliate_State, 
		Affiliate_Council, Affiliate_Local, Affiliate_Seq, SubLocal, 
		AffiliateId, UnitCode, TitleCodeId, TitleCodeSeq, Gender, 
		Salutation, LastName, FirstName, MiddleName, Suffix, 
		Home_Addr1, Home_Addr2, Home_City, Home_State, Home_ZipCode, 
		Home_Zip4, Home_Province, Home_Country, Work_Addr1, Work_Addr2, 
		Work_City, Work_State, Work_ZipCode, Work_Zip4, Work_Province, 
		Work_Country, Email, Phone_Number, Office_Mail_Code, 
		Office_Exp_Date, Last_Filled_Dt, Last_Update_Dt)
	 SELECT leg_officer_pk, leg_person_pk, aff_pk, 
		'Person does not exist (match performed on MemberId) - Person not in load files or failed in Person migration checks',
		MemberId, Affiliate_Type, Affiliate_State, 
		Affiliate_Council, Affiliate_Local, Affiliate_Seq, SubLocal, 
		AffiliateId, UnitCode, TitleCodeId, TitleCodeSeq, Gender, 
		Salutation, LastName, FirstName, MiddleName, Suffix, 
		Home_Addr1, Home_Addr2, Home_City, Home_State, Home_ZipCode, 
		Home_Zip4, Home_Province, Home_Country, Work_Addr1, Work_Addr2, 
		Work_City, Work_State, Work_ZipCode, Work_Zip4, Work_Province, 
		Work_Country, Email, Phone_Number, Office_Mail_Code, 
		Office_Exp_Date, Last_Filled_Dt, Last_Update_Dt
	   FROM DM_migrateOfficer
	  WHERE leg_person_pk IS NULL
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Invalid Last_Filled_Dt
/*	INSERT INTO DM_migrateOfficer_ERROR
	 	(leg_officer_pk, leg_person_pk, aff_pk, ERROR,
		MemberId, Affiliate_Type, Affiliate_State, 
		Affiliate_Council, Affiliate_Local, Affiliate_Seq, SubLocal, 
		AffiliateId, UnitCode, TitleCodeId, TitleCodeSeq, Gender, 
		Salutation, LastName, FirstName, MiddleName, Suffix, 
		Home_Addr1, Home_Addr2, Home_City, Home_State, Home_ZipCode, 
		Home_Zip4, Home_Province, Home_Country, Work_Addr1, Work_Addr2, 
		Work_City, Work_State, Work_ZipCode, Work_Zip4, Work_Province, 
		Work_Country, Email, Phone_Number, Office_Mail_Code, 
		Office_Exp_Date, Last_Filled_Dt, Last_Update_Dt)
	 SELECT leg_officer_pk, leg_person_pk, aff_pk, 'Invalid Last_Filled_Dt - '+Last_Filled_Dt,
		MemberId, Affiliate_Type, Affiliate_State, 
		Affiliate_Council, Affiliate_Local, Affiliate_Seq, SubLocal, 
		AffiliateId, UnitCode, TitleCodeId, TitleCodeSeq, Gender, 
		Salutation, LastName, FirstName, MiddleName, Suffix, 
		Home_Addr1, Home_Addr2, Home_City, Home_State, Home_ZipCode, 
		Home_Zip4, Home_Province, Home_Country, Work_Addr1, Work_Addr2, 
		Work_City, Work_State, Work_ZipCode, Work_Zip4, Work_Province, 
		Work_Country, Email, Phone_Number, Office_Mail_Code, 
		Office_Exp_Date, Last_Filled_Dt, Last_Update_Dt
	   FROM DM_migrateOfficer
	  WHERE Len(Last_Filled_Dt) <> 10
	     OR CAST(Substring(Last_Filled_Dt, 1, 2) AS INTEGER) NOT BETWEEN 1 AND 12
	     OR CAST(Substring(Last_Filled_Dt, 4, 2) AS INTEGER) NOT BETWEEN 1 AND 31
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR
*/
	-- Invalid Last_Update_Dt
/*	INSERT INTO DM_migrateOfficer_ERROR
	 	(leg_officer_pk, leg_person_pk, aff_pk, ERROR,
		MemberId, Affiliate_Type, Affiliate_State, 
		Affiliate_Council, Affiliate_Local, Affiliate_Seq, SubLocal, 
		AffiliateId, UnitCode, TitleCodeId, TitleCodeSeq, Gender, 
		Salutation, LastName, FirstName, MiddleName, Suffix, 
		Home_Addr1, Home_Addr2, Home_City, Home_State, Home_ZipCode, 
		Home_Zip4, Home_Province, Home_Country, Work_Addr1, Work_Addr2, 
		Work_City, Work_State, Work_ZipCode, Work_Zip4, Work_Province, 
		Work_Country, Email, Phone_Number, Office_Mail_Code, 
		Office_Exp_Date, Last_Filled_Dt, Last_Update_Dt)
	 SELECT leg_officer_pk, leg_person_pk, aff_pk, 'Invalid Last_Update_Dt - '+Last_Update_Dt,
		MemberId, Affiliate_Type, Affiliate_State, 
		Affiliate_Council, Affiliate_Local, Affiliate_Seq, SubLocal, 
		AffiliateId, UnitCode, TitleCodeId, TitleCodeSeq, Gender, 
		Salutation, LastName, FirstName, MiddleName, Suffix, 
		Home_Addr1, Home_Addr2, Home_City, Home_State, Home_ZipCode, 
		Home_Zip4, Home_Province, Home_Country, Work_Addr1, Work_Addr2, 
		Work_City, Work_State, Work_ZipCode, Work_Zip4, Work_Province, 
		Work_Country, Email, Phone_Number, Office_Mail_Code, 
		Office_Exp_Date, Last_Filled_Dt, Last_Update_Dt
	   FROM DM_migrateOfficer
	  WHERE Len(Last_Update_Dt) <> 10
	     OR CAST(Substring(Last_Update_Dt, 1, 2) AS INTEGER) NOT BETWEEN 1 AND 12
	     OR CAST(Substring(Last_Update_Dt, 4, 2) AS INTEGER) NOT BETWEEN 1 AND 31
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR
*/
	-- Invalid Office_Exp_Date
	INSERT INTO DM_migrateOfficer_ERROR
	 	(leg_officer_pk, leg_person_pk, aff_pk, ERROR,
		MemberId, Affiliate_Type, Affiliate_State, 
		Affiliate_Council, Affiliate_Local, Affiliate_Seq, SubLocal, 
		AffiliateId, UnitCode, TitleCodeId, TitleCodeSeq, Gender, 
		Salutation, LastName, FirstName, MiddleName, Suffix, 
		Home_Addr1, Home_Addr2, Home_City, Home_State, Home_ZipCode, 
		Home_Zip4, Home_Province, Home_Country, Work_Addr1, Work_Addr2, 
		Work_City, Work_State, Work_ZipCode, Work_Zip4, Work_Province, 
		Work_Country, Email, Phone_Number, Office_Mail_Code, 
		Office_Exp_Date, Last_Filled_Dt, Last_Update_Dt)
	 SELECT leg_officer_pk, leg_person_pk, aff_pk, 'Invalid Office_Exp_Date - '+Office_Exp_Date,
		MemberId, Affiliate_Type, Affiliate_State, 
		Affiliate_Council, Affiliate_Local, Affiliate_Seq, SubLocal, 
		AffiliateId, UnitCode, TitleCodeId, TitleCodeSeq, Gender, 
		Salutation, LastName, FirstName, MiddleName, Suffix, 
		Home_Addr1, Home_Addr2, Home_City, Home_State, Home_ZipCode, 
		Home_Zip4, Home_Province, Home_Country, Work_Addr1, Work_Addr2, 
		Work_City, Work_State, Work_ZipCode, Work_Zip4, Work_Province, 
		Work_Country, Email, Phone_Number, Office_Mail_Code, 
		Office_Exp_Date, Last_Filled_Dt, Last_Update_Dt
	   FROM DM_migrateOfficer
	  WHERE SubString(Office_Exp_Date, 3, 2) NOT IN ('19', '20')
	     OR Len(Office_Exp_Date) <> 6
	     OR CAST(Substring(Office_Exp_Date, 1, 2) AS INTEGER) NOT BETWEEN 1 AND 12
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Invalid Office_Mail_Code
	INSERT INTO DM_migrateOfficer_ERROR
	 	(leg_officer_pk, leg_person_pk, aff_pk, ERROR,
		MemberId, Affiliate_Type, Affiliate_State, 
		Affiliate_Council, Affiliate_Local, Affiliate_Seq, SubLocal, 
		AffiliateId, UnitCode, TitleCodeId, TitleCodeSeq, Gender, 
		Salutation, LastName, FirstName, MiddleName, Suffix, 
		Home_Addr1, Home_Addr2, Home_City, Home_State, Home_ZipCode, 
		Home_Zip4, Home_Province, Home_Country, Work_Addr1, Work_Addr2, 
		Work_City, Work_State, Work_ZipCode, Work_Zip4, Work_Province, 
		Work_Country, Email, Phone_Number, Office_Mail_Code, 
		Office_Exp_Date, Last_Filled_Dt, Last_Update_Dt)
	 SELECT leg_officer_pk, leg_person_pk, aff_pk, 'Invalid Office_Mail_Code - '+Office_Mail_Code,
		MemberId, Affiliate_Type, Affiliate_State, 
		Affiliate_Council, Affiliate_Local, Affiliate_Seq, SubLocal, 
		AffiliateId, UnitCode, TitleCodeId, TitleCodeSeq, Gender, 
		Salutation, LastName, FirstName, MiddleName, Suffix, 
		Home_Addr1, Home_Addr2, Home_City, Home_State, Home_ZipCode, 
		Home_Zip4, Home_Province, Home_Country, Work_Addr1, Work_Addr2, 
		Work_City, Work_State, Work_ZipCode, Work_Zip4, Work_Province, 
		Work_Country, Email, Phone_Number, Office_Mail_Code, 
		Office_Exp_Date, Last_Filled_Dt, Last_Update_Dt
	   FROM DM_migrateOfficer
	  WHERE Office_Mail_Code NOT IN ('1','0')
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	--  Unqualified person holding office (must be member, AFSCME Staff or Affiliate Staff)
	--  Create a temporary table to hold the valid officers
	CREATE TABLE #ValidOfficers (
		leg_officer_pk int null, 
		leg_person_pk int null, 
		aff_pk int null )
	-- Populate the temporary table with matches for member, AFSCME Staff and Affiliate Staff
	INSERT INTO #ValidOfficers (leg_officer_pk, leg_person_pk, aff_pk)
	 SELECT leg_officer_pk, leg_person_pk, aff_pk
	   FROM DM_migrateOfficer
	  WHERE EXISTS (SELECT * FROM afscme_oltp.dbo.Users 
			 WHERE person_pk = leg_person_pk AND dept IS NOT NULL )
	     AND TitleCodeId IN (@Administrator, @ExecDirector, @FRO) --AFSCME Staff
	UNION ALL
	 SELECT leg_officer_pk, leg_person_pk, aff_pk
	   FROM DM_migrateOfficer
	  WHERE EXISTS (SELECT * FROM afscme_oltp.dbo.Aff_Members WHERE person_pk = leg_person_pk) --member
	UNION ALL
	 SELECT leg_officer_pk, leg_person_pk, aff_pk
	   FROM DM_migrateOfficer
	  WHERE EXISTS (SELECT * FROM afscme_oltp.dbo.Aff_Staff WHERE person_pk = leg_person_pk) 
	     AND TitleCodeId IN (@Administrator, @ExecDirector, @FRO) --Aff Staff
	-- Those not in the temporary table are copied to the Error table
	INSERT INTO DM_migrateOfficer_ERROR
	 	(leg_officer_pk, leg_person_pk, aff_pk, ERROR,
		MemberId, Affiliate_Type, Affiliate_State, 
		Affiliate_Council, Affiliate_Local, Affiliate_Seq, SubLocal, 
		AffiliateId, UnitCode, TitleCodeId, TitleCodeSeq, Gender, 
		Salutation, LastName, FirstName, MiddleName, Suffix, 
		Home_Addr1, Home_Addr2, Home_City, Home_State, Home_ZipCode, 
		Home_Zip4, Home_Province, Home_Country, Work_Addr1, Work_Addr2, 
		Work_City, Work_State, Work_ZipCode, Work_Zip4, Work_Province, 
		Work_Country, Email, Phone_Number, Office_Mail_Code, 
		Office_Exp_Date, Last_Filled_Dt, Last_Update_Dt)
	 SELECT leg_officer_pk, leg_person_pk, aff_pk, 'Unqualified person holding office - must be member, AFSCME Staff or Affiliate Staff',
		MemberId, Affiliate_Type, Affiliate_State, 
		Affiliate_Council, Affiliate_Local, Affiliate_Seq, SubLocal, 
		AffiliateId, UnitCode, TitleCodeId, TitleCodeSeq, Gender, 
		Salutation, LastName, FirstName, MiddleName, Suffix, 
		Home_Addr1, Home_Addr2, Home_City, Home_State, Home_ZipCode, 
		Home_Zip4, Home_Province, Home_Country, Work_Addr1, Work_Addr2, 
		Work_City, Work_State, Work_ZipCode, Work_Zip4, Work_Province, 
		Work_Country, Email, Phone_Number, Office_Mail_Code, 
		Office_Exp_Date, Last_Filled_Dt, Last_Update_Dt
	   FROM DM_migrateOfficer o
	  WHERE NOT EXISTS (SELECT * from #ValidOfficers v WHERE v.leg_person_pk = o.leg_person_pk)
	  --WHERE NOT EXISTS (SELECT * FROM Aff_Members WHERE person_pk = leg_person_pk) --member
	  --  AND NOT EXISTS (SELECT * FROM Aff_Staff WHERE person_pk = leg_person_pk) --Aff Staff
	  --  AND NOT EXISTS (SELECT * FROM Users WHERE dept IS NOT NULL) --AFSCME Staff
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR
	-- Drop the temporary table
	DROP TABLE #ValidOfficers

--> Remove those that caused an error
	DELETE o
	--SELECT *
	  FROM DM_migrateOfficer o
	  JOIN DM_migrateOfficer_ERROR e ON e.leg_officer_pk = o.leg_officer_pk
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

COMMIT TRAN sp_errorOfficer
PRINT 'sp_errorOfficer has completed'

-->> Display the error
E_ERROR:
	IF @errorCode <> 0
	BEGIN
		PRINT 'Error occurred in sp_errorOfficer() with error code = ' + CONVERT(VARCHAR, @errorCode)
		ROLLBACK TRAN sp_errorOfficer 
	END

RETURN @errorCode
	
GO

-- =============================================
-- example to execute the store procedure
-- =============================================
--EXECUTE sp_errorOfficer 
