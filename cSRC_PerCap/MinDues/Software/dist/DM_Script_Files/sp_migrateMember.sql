-- =============================================
-- Procedure to handle the Member data
-- =============================================
-- creating the store procedure
IF EXISTS (SELECT name 
	   FROM   sysobjects 
	   WHERE  name = N'sp_migrateMember' 
	   AND 	  type = 'P')
    DROP PROCEDURE sp_migrateMember
GO

CREATE PROCEDURE sp_migrateMember 
AS
DECLARE @errorCode int

BEGIN TRAN sp_migrateMember

--> Check for Persons that are also Members 
	-- Set Members with an invalid affiliate identifier into the error table
	UPDATE DM_migratePerson SET List_Code = NULLIF(List_Code, '')
	INSERT INTO DM_migratePerson_ERROR
		(leg_person_pk, ERROR, MemberId, Affiliate_Type, Affiliate_State, 
		Council, Affiliate_Local, SubLocal, List_Code, Social_Security, Gender, 
		Salutation, LastName, FirstName, MiddleName, Suffix, MailingAddress1, 
		MailingAddress2, City, State, ZipCode, Zip_4, Province, Country, 
		AddressType, Mailable_Add_Flag, No_Mail_Flag, Email, PhoneNumber, 
		StatusCode, DateJoined, Register_Voter, Political_Party, 
		Information_Source, AffiliateId, UnitCode, AddressChange_Dt, 
		AlternateMailName)
	 SELECT leg_person_pk, 'Invalid Affiliate Identifier - ' + 
		COALESCE(Affiliate_Type+'-'+Affiliate_Local+'-'+SubString(UnitCode, 6, 1)+'-'+Affiliate_State+'-'+SubLocal+'-'+Council, ''),  
		MemberId, Affiliate_Type, Affiliate_State, Council, 
		Affiliate_Local, SubLocal, List_Code, Social_Security, 
		Gender, Salutation, LastName, FirstName, MiddleName, Suffix, 
		MailingAddress1, MailingAddress2, City, State, ZipCode, 
		Zip_4, Province, Country, AddressType, Mailable_Add_Flag, 
		No_Mail_Flag, Email, PhoneNumber, StatusCode, DateJoined, 
		Register_Voter, Political_Party, Information_Source, AffiliateId, 
		UnitCode, AddressChange_Dt, AlternateMailName 
	   FROM DM_migratePerson
	  WHERE NOT EXISTS (SELECT aff_pk FROM aff_Organizations 
		  WHERE Affiliate_Type = aff_type
		    AND Affiliate_Local = aff_localSubChapter
		    AND Affiliate_State = aff_stateNat_type
		    AND Council = aff_councilRetiree_chap
		    AND SubLocal = aff_subUnit
		    AND SubString(UnitCode, 6, 1) = aff_code) 
	    AND List_Code IS NULL
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Length for mbr_no_local is too large 
	INSERT INTO DM_migratePerson_ERROR
		(leg_person_pk, ERROR, MemberId, Affiliate_Type, Affiliate_State, 
		Council, Affiliate_Local, SubLocal, List_Code, Social_Security, Gender, 
		Salutation, LastName, FirstName, MiddleName, Suffix, MailingAddress1, 
		MailingAddress2, City, State, ZipCode, Zip_4, Province, Country, 
		AddressType, Mailable_Add_Flag, No_Mail_Flag, Email, PhoneNumber, 
		StatusCode, DateJoined, Register_Voter, Political_Party, 
		Information_Source, AffiliateId, UnitCode, AddressChange_Dt, 
		AlternateMailName)
	 SELECT leg_person_pk, 'Length for AffiliateId/mbr_no_local is too large - ' + 
		CONVERT(varchar(3), LEN(AffiliateId)),  
		MemberId, Affiliate_Type, Affiliate_State, Council, 
		Affiliate_Local, SubLocal, List_Code, Social_Security, 
		Gender, Salutation, LastName, FirstName, MiddleName, Suffix, 
		MailingAddress1, MailingAddress2, City, State, ZipCode, 
		Zip_4, Province, Country, AddressType, Mailable_Add_Flag, 
		No_Mail_Flag, Email, PhoneNumber, StatusCode, DateJoined, 
		Register_Voter, Political_Party, Information_Source, AffiliateId, 
		UnitCode, AddressChange_Dt, AlternateMailName 
	   FROM DM_migratePerson
	  WHERE LEN(AffiliateId) > 15
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Create temporary to determine and process the members for aff_pk and join date
	 SELECT leg_person_pk, 
		(SELECT aff_pk FROM aff_Organizations 
		  WHERE Affiliate_Type = aff_type
		    AND Affiliate_Local = aff_localSubChapter
		    AND Affiliate_State = aff_stateNat_type
		    AND Council = aff_councilRetiree_chap
		    AND SubLocal = aff_subUnit
		    AND SubString(UnitCode, 6, 1) = aff_code) aff_pk,
		0 mbr_status, 0 mbr_type, Information_Source primary_information_source,
		No_Mail_Flag no_mail_fg, No_Mail_Flag no_cards_fg, 
		No_Mail_Flag no_public_emp_fg, No_Mail_Flag no_legislative_mail_fg,
		CONVERT(DateTime, SubString(DateJoined,3, 4)+SubString(DateJoined,1, 2)+'01') mbr_join_dt, 
		MemberId mbr_no_old_afscme, AffiliateId mbr_no_local,
		created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt,
		Affiliate_Type, StatusCode, List_Code
	   INTO #tmpAff_Member
	   FROM DM_migratePerson
	  WHERE leg_person_pk NOT IN (SELECT leg_person_pk FROM DM_migratePerson_ERROR)
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Remove those that are AFSCME or Affiliate Staff
	DELETE t FROM #tmpAff_Member t 
	 WHERE EXISTS (SELECT * FROM DM_migratePerson d 
			WHERE d.leg_person_pk = t.leg_person_pk
	   AND List_Code IN ('07','31','03','32'))
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	DECLARE @Active int,
		@Temporary int,
		@Regular int,
		@Retiree int,
		@AgencyFeePayer int,
		@UnionShop int
	SET	@Active = (SELECT com_cd_pk FROM Common_Codes 
			    WHERE com_cd_cd = 'A' AND com_cd_type_key = 'MemberStatus')
	SET	@Temporary = (SELECT com_cd_pk FROM Common_Codes 
			       WHERE com_cd_cd = 'T' AND com_cd_type_key = 'MemberStatus')
	SET	@Regular = (SELECT com_cd_pk FROM Common_Codes 
			     WHERE com_cd_cd = 'R' AND com_cd_type_key = 'MemberType')
	SET	@Retiree = (SELECT com_cd_pk FROM Common_Codes 
			     WHERE com_cd_cd = 'T' AND com_cd_type_key = 'MemberType')
	SET	@AgencyFeePayer = (SELECT com_cd_pk FROM Common_Codes 
				    WHERE com_cd_cd = 'A' AND com_cd_type_key = 'MemberType')
	SET	@UnionShop = (SELECT com_cd_pk FROM Common_Codes 
			       WHERE com_cd_cd = 'U' AND com_cd_type_key = 'MemberType')	-- Update for the Mail Flags, Primary Information Source, Member Status and Type
	 UPDATE #tmpAff_Member
	    SET no_mail_fg =  CASE no_mail_fg WHEN '9' THEN 1 ELSE 0 END,
		no_cards_fg = CASE no_cards_fg
				WHEN '0' THEN 0
				WHEN '1' THEN 1
				WHEN '2' THEN 0
				WHEN '3' THEN 1
				WHEN '9' THEN 1 ELSE 0 END,
		no_public_emp_fg = 
			      CASE no_public_emp_fg
				WHEN '0' THEN 0
				WHEN '1' THEN 0
				WHEN '2' THEN 1
				WHEN '3' THEN 1
				WHEN '9' THEN 1 ELSE 0 END,
		no_legislative_mail_fg = 
			      CASE no_legislative_mail_fg WHEN '9' THEN 1 ELSE 0 END,
		primary_information_source = 
			(SELECT com_cd_pk FROM DM_Code_Mapping_view 
			  WHERE com_cd_type_key = 'InformationSource' 
			    AND Legacy_Code = primary_information_source),
		mbr_status = CASE
			WHEN Affiliate_Type IN ('L', 'U', 'R', 'S') AND StatusCode IN ('A','O') 
				THEN @Active
			WHEN Affiliate_Type IN ('L', 'U', 'R', 'S') AND StatusCode = 'T' THEN @Temporary
			WHEN StatusCode = 'N' AND List_Code NOT IN ('03','07','72') THEN @Active
			WHEN StatusCode = 'C' THEN @Active
			ELSE 0 END,
		mbr_type = CASE
			WHEN Affiliate_Type IN ('L', 'U') AND StatusCode IN ('A','O') THEN @Regular
			WHEN Affiliate_Type IN ('R', 'S') AND StatusCode IN ('A','O') THEN @Retiree
			WHEN Affiliate_Type IN ('L', 'U') AND StatusCode = 'T' THEN @Regular
			WHEN Affiliate_Type IN ('R', 'S') AND StatusCode = 'T' THEN @Retiree
			WHEN StatusCode = 'N' AND (List_Code NOT IN ('03','07','72') OR List_Code IS NULL)
				THEN @AgencyFeePayer
			WHEN StatusCode = 'C' THEN @UnionShop
		ELSE 0 END
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Remove those that are not members due to Member Status and Member Type
	DELETE t FROM #tmpAff_Member t 
	 WHERE mbr_status = 0 OR mbr_type = 0
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Insert Members to Aff_Members table
	INSERT INTO Aff_Members
		(person_pk, aff_pk, mbr_status, mbr_type, 
		primary_information_source, no_mail_fg, no_cards_fg, 
		no_public_emp_fg, no_legislative_mail_fg, mbr_join_dt, 
		mbr_no_old_afscme, mbr_no_local, 
		created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
	 SELECT leg_person_pk, aff_pk, mbr_status, mbr_type, 
		primary_information_source, no_mail_fg, no_cards_fg, 
		no_public_emp_fg, no_legislative_mail_fg, mbr_join_dt, 
		mbr_no_old_afscme, mbr_no_local, 
		created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt
	   FROM #tmpAff_Member
	  WHERE aff_pk IS NOT NULL
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Update the Person table for the member last user and last mod date
	UPDATE p
	   SET mbr_mst_lst_mod_user_pk = person_mst_lst_mod_user_pk,
	       mbr_mst_lst_mod_dt = person_mst_lst_mod_dt
	  FROM Person p
	  JOIN Aff_Members m ON p.person_pk = m.person_pk
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Remove temporary table
	DROP TABLE #tmpAff_Member
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Set the Member Flag in Person for those in the Aff_Members table
	UPDATE p
	   SET member_fg = 1
	  FROM Person p
	WHERE EXISTS (SELECT m.person_pk FROM Aff_Members m WHERE m.person_pk = p.person_pk)
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR


COMMIT TRAN sp_migrateMember
PRINT 'sp_migrateMember has completed'

-->> Display the error
E_ERROR:
	IF @errorCode <> 0
	BEGIN
		PRINT 'Error occurred in sp_migrateMember() with error code = ' + CONVERT(VARCHAR, @errorCode)
		ROLLBACK TRAN sp_migrateMember 
	END

RETURN @errorCode

GO

-- =============================================
-- example to execute the store procedure
-- =============================================
--EXECUTE sp_migrateMember

