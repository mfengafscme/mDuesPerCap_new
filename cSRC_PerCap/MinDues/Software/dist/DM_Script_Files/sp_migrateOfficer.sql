-- =============================================
-- Procedure to handle the Officer file(s)
-- =============================================
-- creating the store procedure
IF EXISTS (SELECT name 
	   FROM   sysobjects 
	   WHERE  name = N'sp_migrateOfficer' 
	   AND 	  type = 'P')
    DROP PROCEDURE sp_migrateOfficer
GO

CREATE PROCEDURE sp_migrateOfficer 
AS
DECLARE @user_pk integer, 
	@PrimaryEmail int,
	@WorkAddressType int,
	@MembershipDept int
DECLARE @errorCode int

SET @user_pk = (SELECT person_pk FROM users WHERE user_id = 'migration')
SET @PrimaryEmail = (SELECT com_cd_pk FROM common_codes 
		      WHERE com_cd_type_key = 'EmailType' AND com_cd_desc = 'Primary')
SET @WorkAddressType = (SELECT com_cd_pk FROM Common_Codes 
			 WHERE com_cd_desc = 'Work' AND com_cd_type_key = 'PersonAddressType')
SET @MembershipDept = (SELECT com_cd_pk FROM Common_Codes 
			WHERE com_cd_cd = 'MD' AND com_cd_type_key = 'Department')
BEGIN TRAN sp_migrateOfficer

-- DELETE FROM DM_migrateOfficer
-- DELETE FROM DM_migrateOfficer_ERROR

-- Get the Person's Affiliate Relationship
	-- If NOT a Member and AFSCME_Offices.afscme_title_nm in ('Administrator', 'Executive Director', 'Financial Reporting Officer')
		-- If AFSCME Staff, update User table for their AFSCME Department
		-- If Affiliate Staff, get Affiliate relationship
		-- If neither AFSCME Staff or Affiliate Staff, then ERROR
	-- If NOT a Member and AFSCME_Offices.afscme_title_nm NOT in ('Administrator', 'Executive Director', 'Financial Reporting Officer')
		-- ERROR
--> Add Officer Information to Officer_History
	-- Update new Mail Code
	-- If a Member, get the Affiliate Association using person_pk and AFSCME Member ID
	INSERT INTO Officer_History
		(person_pk, pos_start_dt, pos_expiration_dt, aff_pk, 
		pos_addr_from_person_pk, pos_addr_from_org_pk, office_group_id, 
		afscme_office_pk, position_mbr_affiliation, suspended_fg,
		created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt) 
	 SELECT leg_person_pk, DateAdd(Day, 1, CONVERT(DateTime, Last_Filled_Dt)) pos_start_dt,
		CONVERT(DateTime, SubString(Office_Exp_Date, 1, 2)+'/01/'+SubString(Office_Exp_Date, 3, 4)) pos_expiration_dt, 
		o.aff_pk, 
		CASE Office_Mail_Code WHEN 0 THEN 
--			(SELECT address_pk FROM Person_Address s
--			  WHERE s.person_pk = o.leg_person_pk) END pos_addr_from_person_pk,
			(SELECT address_pk FROM Person_SMA s
			  WHERE s.person_pk = o.leg_person_pk AND s.current_fg = 1) END pos_addr_from_person_pk,
		CASE Office_Mail_Code WHEN 1 THEN 
			(SELECT org_addr_pk 
			   FROM Org_Address a 
			   JOIN Org_Locations l ON a.org_locations_pk = l.org_locations_pk
			    AND l.org_pk = o.aff_pk ) END pos_addr_from_org_pk,
		g.office_group_id, g.afscme_office_pk,
		(SELECT aff_pk FROM Aff_Members p WHERE p.person_pk = o.leg_person_pk) position_mbr_affiliation,
		0,
	 	@user_pk, GetDate(), @user_pk, CONVERT(DateTime, Last_Update_Dt)
	--select *
	   FROM DM_migrateOfficer o
	   JOIN Aff_Officer_Groups g ON g.office_group_id = o.TitleCodeSeq
	  WHERE EXISTS (SELECT aff_pk FROM Aff_Members p WHERE p.person_pk = o.leg_person_pk) --Member
	    OR EXISTS (SELECT aff_pk FROM Aff_Staff s WHERE s.person_pk = o.leg_person_pk 
			  AND g.afscme_office_pk in (4, 6, 32)) --Aff Staff
	    OR EXISTS (SELECT * FROM Users u WHERE u.person_pk = o.leg_person_pk AND dept IS NOT NULL
			  AND g.afscme_office_pk in (4, 6, 32)) --AFSCME Staff
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

/*	--  Unqualified person holding office for Administrator, Executive Director, Financial Reporting Officer
	-- (must be member, AFSCME Staff or Affiliate Staff)
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
	 SELECT leg_officer_pk, leg_person_pk, aff_pk, 'Unqualified person Administrator, Executive Director, Financial Reporting Officer',
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
	  WHERE NOT EXISTS (SELECT * FROM Officer_History WHERE person_pk = leg_person_pk) 
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	--> Remove those that caused an error
	DELETE o
	  FROM DM_migrateOfficer o
	  JOIN DM_migrateOfficer_ERROR e ON e.leg_officer_pk = o.leg_officer_pk
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR
*/
-- Add work address
	-- Insert Person_Address
	UPDATE DM_migrateOfficer SET Work_Country = NULLIF(Work_Country, '')
	INSERT INTO Person_Address(person_pk, addr1, addr2, city, state, 
		zipcode, zip_plus, province, country, dept, addr_type, 
		addr_source, addr_prmry_fg, eff_dt, 
		created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
	 SELECT leg_person_pk, Work_Addr1, Work_Addr2, Work_City, Work_State, 
		Work_ZipCode, Work_Zip4, Work_Province, 
		(SELECT com_cd_pk FROM Common_Codes 
		  WHERE com_cd_type_key = 'CountryCitizenship' 
		    AND com_cd_cd = Work_Country), 
		@MembershipDept,
		@WorkAddressType,
		'U', 0, GetDate(), @user_pk, GetDate(), @user_pk, CONVERT(DateTime, Last_Update_Dt)
	   FROM DM_migrateOfficer
	  WHERE Work_Addr1+Work_Addr2+Work_City+Work_State+Work_ZipCode <> ''
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

--Add Email Address
	UPDATE e
	   SET e.person_email_addr = o.Email,
	       e.lst_mod_user_pk = @user_pk,
	       e.lst_mod_dt = CONVERT(DateTime, o.Last_Update_Dt)
	-- select *
	  FROM Person_Email e
	  JOIN DM_migrateOfficer o ON e.person_pk = o.leg_person_pk
	  WHERE email_type = @PrimaryEmail
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

COMMIT TRAN sp_migrateOfficer
PRINT 'sp_migrateOfficer has completed'

-->> Display the error
E_ERROR:
	IF @errorCode <> 0
	BEGIN
		PRINT 'Error occurred in sp_migrateOfficer() with error code = ' + CONVERT(VARCHAR, @errorCode)
		ROLLBACK TRAN sp_migrateOfficer 
	END

RETURN @errorCode
	
GO

-- =============================================
-- example to execute the store procedure
-- =============================================
--EXECUTE sp_migrateOfficer 
