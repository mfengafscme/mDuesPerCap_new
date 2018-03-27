-- =============================================
-- Procedure to handle the Person/Member file(s)
-- =============================================
-- creating the store procedure
IF EXISTS (SELECT name 
	   FROM   sysobjects 
	   WHERE  name = N'sp_migratePersonMember' 
	   AND 	  type = 'P')
    DROP PROCEDURE sp_migratePersonMember
GO

CREATE PROCEDURE sp_migratePersonMember 
AS
DECLARE @aff_pk int,
	@user_pk integer, 
	@membership_dept_cd varchar(8),
	@dept_type_key varchar(20)
DECLARE @errorCode int

SET @user_pk = (SELECT person_pk FROM users WHERE user_id = 'migration')
SET @membership_dept_cd = 'MD'
SET @dept_type_key = 'Department'

BEGIN TRAN sp_migratePersonMember

--> Remove from DMAL the List Codes values that fail
--EXECUTE sp_errorDMAL

--> Call the stored procedure to migrate the Organizations into the system
--EXECUTE sp_migrateOrganization
	-- Remove these records from the PersonMember table to avoid confusion
	DELETE FROM LEG_PersonMember WHERE Suffix = '*' --AND UnitCode = 'L6000A'
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

--> See if Person already is in system (DMAL)
	-- Find the Person pk for those with a MemberID
	 UPDATE d
	    SET d.leg_person_pk = m.leg_person_pk
	   FROM DM_migratePerson_DMAL d
	   JOIN LEG_PersonMember m ON m.MemberId = d.MemberId
	  WHERE d.MemberId IS NOT NULL
	    AND d.List_Code IS NOT NULL
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- From remaining records, check for SSN and confirm with First Name, Last Name 
	 UPDATE d
	    SET d.leg_person_pk = p.leg_person_pk
	   FROM DM_migratePerson_DMAL d
	   JOIN LEG_PersonMember p ON p.Social_Security = d.Social_Security
	    AND p.FirstName = d.FirstName
	    AND p.LastName = d.LastName
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Update List Code for matching Person
	 UPDATE p
	    SET p.List_Code = d.List_Code
	   FROM DM_migratePerson_DMAL d
	   JOIN LEG_PersonMember p ON p.leg_person_pk = d.leg_person_pk
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

--> These people exist only in the DMAL and need to be added to the Person load
	-- Copy over the Persons in DMAL that do not have a Person_pk
	INSERT INTO LEG_PersonMember 
		(MemberId, Affiliate_Type, Affiliate_State, 
		Council, Affiliate_Local, SubLocal, List_Code, Social_Security, 
		Gender, Salutation, LastName, FirstName, MiddleName, Suffix, 
		MailingAddress1, MailingAddress2, City, State, ZipCode, Zip_4, 
		Province, Country, AddressType, Mailable_Add_Flag, No_Mail_Flag, 
		Email, PhoneNumber, StatusCode, DateJoined, Register_Voter, Political_Party, 
		Information_Source, AffiliateId, UnitCode, AddressChange_Dt, AlternateMailName)
	 SELECT MemberId, Affiliate_Type, Affiliate_State, 
		Council, Affiliate_Local, SubLocal, List_Code, Social_Security, 
		Gender, Salutation, LastName, FirstName, MiddleName, Suffix, 
		MailingAddress1, MailingAddress2, City, State, ZipCode, Zip_4, 
		Province, Country, AddressType, Mailable_Add_Flag, No_Mail_Flag, 
		Email, PhoneNumber, StatusCode, DateJoined, Register_Voter, Political_Party, 
		Information_Source, AffiliateId, UnitCode, AddressChange_Dt, AlternateMailName 
	   FROM DM_migratePerson_DMAL
	  WHERE leg_person_pk IS NULL
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

--> Check for errors.  If found, move to DM_migratePerson_ERROR
EXECUTE sp_errorPerson

-- Perform check for duplicates.  
	-- Copy records with a MemberId to the processing table (DM_Person_Migrated)
	-- and remove leading zeros
	INSERT INTO DM_migratePerson
		(leg_person_pk, MemberId, Affiliate_Type, Affiliate_State, Council, 
		Affiliate_Local, SubLocal, List_Code, Social_Security, Gender, 
		Salutation, LastName, FirstName, MiddleName, Suffix, MailingAddress1, 
		MailingAddress2, City, State, ZipCode, Zip_4, Province, Country, 
		AddressType, Mailable_Add_Flag, No_Mail_Flag, Email, PhoneNumber, 
		StatusCode, DateJoined, Register_Voter, Political_Party, 
		Information_Source, AffiliateId, UnitCode, AddressChange_Dt, 
		AlternateMailName)
	 SELECT leg_person_pk, MemberId, Affiliate_Type, Affiliate_State, 
		CASE CAST(Council AS int) 
			WHEN 0 THEN '' ELSE CAST(CAST(Council AS int) AS varchar(4)) END,
		CASE CAST(Affiliate_Local AS int) 
			WHEN 0 THEN '' ELSE CAST(CAST(Affiliate_Local AS int) AS varchar(4)) END,
		CASE CAST(SubLocal AS int) 
			WHEN 0 THEN '' ELSE CAST(CAST(SubLocal AS int) AS varchar(4)) END,
		List_Code, Social_Security, 
		Gender, Salutation, LastName, FirstName, MiddleName, Suffix, 
		MailingAddress1, MailingAddress2, City, State, ZipCode, 
		Zip_4, Province, Country, AddressType, Mailable_Add_Flag, 
		No_Mail_Flag, Email, PhoneNumber, StatusCode, DateJoined, 
		Register_Voter, Political_Party, Information_Source, AffiliateId, 
		UnitCode, AddressChange_Dt, AlternateMailName 
	   FROM dbo.LEG_PersonMember
	  WHERE MemberID IS NOT NULL
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR
--CREATE INDEX i_person_pk ON DM_migratePerson (leg_person_pk)
--CREATE INDEX i_leg_person_pk ON LEG_PersonMember (leg_person_pk)

	-- Create a temporary table for records without a MemberId
	CREATE TABLE #tempTable(
		leg_person_pk int NOT NULL ,
		LastName varchar (255) NULL ,
		FirstName varchar (255) NULL ,
		MiddleName varchar (255) NULL ,
		Suffix varchar (255) NULL ,
		City varchar (255) NULL ,
		State varchar (255) NULL ,
		ZipCode varchar (255) NULL ,
		Zip_4 varchar (255) NULL )
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Add the records without MemberId to the temporary table
	INSERT INTO #tempTable
		(leg_person_pk, LastName, FirstName, MiddleName, Suffix, City, 
		State, ZipCode, Zip_4)
	 SELECT leg_person_pk, leg.LastName, leg.FirstName, leg.MiddleName, 
		leg.Suffix, leg.City, leg.State, leg.ZipCode, leg.Zip_4 
	   FROM LEG_PersonMember leg
	  WHERE leg.MemberID IS NULL
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- If match, remove the duplicate records from the temporary table
	-- Use First Name, Middle Initial, Last Name, Suffix, City, State, Zip/Postal Code
	  DELETE leg 
	    FROM #tempTable leg
	    JOIN DM_migratePerson dm ON leg.FirstName = dm.FirstName AND 
					leg.MiddleName = dm.MiddleName AND 
					leg.LastName = dm.LastName AND 
					leg.Suffix = dm.Suffix AND 
					leg.City = dm.City AND 
					leg.State = dm.State AND 
					leg.ZipCode = dm.ZipCode AND 
					leg.Zip_4 = dm.Zip_4
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR
	-- If no match, move the records to DM_Person_Duplicate
	-- Any records that remain in the temporary table are unique Persons and need to be
	-- added to DM_migratedPerson
	INSERT INTO DM_migratePerson
		(leg_person_pk, MemberId, Affiliate_Type, Affiliate_State, Council, 
		Affiliate_Local, SubLocal, List_Code, Social_Security, Gender, 
		Salutation, LastName, FirstName, MiddleName, Suffix, MailingAddress1, 
		MailingAddress2, City, State, ZipCode, Zip_4, Province, Country, 
		AddressType, Mailable_Add_Flag, No_Mail_Flag, Email, PhoneNumber, 
		StatusCode, DateJoined, Register_Voter, Political_Party, 
		Information_Source, AffiliateId, UnitCode, AddressChange_Dt, 
		AlternateMailName)
	 SELECT leg_person_pk, MemberId, Affiliate_Type, Affiliate_State, 
		CASE CAST(Council AS int) 
			WHEN 0 THEN '' ELSE CAST(CAST(Council AS int) AS varchar(4)) END,
		CASE CAST(Affiliate_Local AS int) 
			WHEN 0 THEN '' ELSE CAST(CAST(Affiliate_Local AS int) AS varchar(4)) END,
		CASE WHEN Substring(SubLocal, 1, 4) = '0000' THEN ''
		     WHEN Substring(SubLocal, 1, 3) = '000' THEN Substring(SubLocal, 4, 1 )
	     WHEN Substring(SubLocal, 1, 2) = '00' THEN Substring(SubLocal, 3, 2 )
		     WHEN Substring(SubLocal, 1, 1) = '0' THEN Substring(SubLocal, 2, 3 )
		     ELSE SubLocal END,
		List_Code, Social_Security, 
		Gender, Salutation, LastName, FirstName, MiddleName, Suffix, 
		MailingAddress1, MailingAddress2, City, State, ZipCode, 
		Zip_4, Province, Country, AddressType, Mailable_Add_Flag, 
		No_Mail_Flag, Email, PhoneNumber, StatusCode, DateJoined, 
		Register_Voter, Political_Party, Information_Source, AffiliateId, 
		UnitCode, AddressChange_Dt, AlternateMailName 
	   FROM LEG_PersonMember
	  WHERE leg_person_pk IN (SELECT leg_person_pk FROM #tempTable)
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Drop the temporary table
	DROP TABLE #tempTable
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

-- Process records as Persons in the DM_migratePerson
	-- Insert into Person table
	SET IDENTITY_INSERT Person ON
	INSERT INTO Person
		(person_pk, prefix_nm, first_nm, middle_nm, last_nm,
		suffix_nm, ssn, alternate_mailing_nm,  
		mbr_barred_fg, marked_for_deletion_fg, member_fg, 
		created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt,
		person_mst_lst_mod_user_pk, person_mst_lst_mod_dt)
	 SELECT leg_person_pk, 
		(SELECT com_cd_pk FROM DM_Code_Mapping_view 
		  WHERE com_cd_type_key = 'Prefix' 
		    AND Legacy_Code = Salutation),
		FirstName, MiddleName, LastName,
		(SELECT com_cd_pk FROM DM_Code_Mapping_view 
		  WHERE com_cd_type_key = 'Suffix' 
		    AND Legacy_Code = Suffix), 
		Social_Security, AlternateMailName, 0, 0, 
		CASE StatusCode 
			WHEN 'X' THEN 0 
			WHEN 'R' THEN 0
			WHEN NULL THEN 0
			ELSE 1 END,
		@user_pk, GetDate(), @user_pk, GetDate(), @user_pk, GetDate()
	   FROM DM_migratePerson
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR
	SET IDENTITY_INSERT Person OFF

	-- Insert Person_Address
	UPDATE DM_migratePerson SET Country = NULL WHERE Country = ''
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR
	INSERT INTO Person_Address
		(person_pk, addr1, addr2, city, state, zipcode, zip_plus, province, 
		country, dept, addr_type, addr_bad_fg, addr_marked_bad_dt, 
		addr_source, addr_prmry_fg, eff_dt, created_user_pk, created_dt, 
		lst_mod_user_pk, lst_mod_dt)
	 SELECT leg_person_pk, MailingAddress1, MailingAddress2, City, State, 
		ZipCode, Zip_4, Province, 
		(SELECT com_cd_pk FROM Common_Codes 
		  WHERE com_cd_type_key = 'CountryCitizenship' 
		    AND com_cd_cd = Country), 
		(SELECT com_cd_pk FROM Common_Codes 
		  WHERE com_cd_cd = @membership_dept_cd 
		    AND com_cd_type_key = @dept_type_key),
		(SELECT com_cd_pk 
		   FROM Common_Codes 
		  WHERE com_cd_desc = 'Home' 
		    AND com_cd_type_key = 'PersonAddressType'),
		CASE Mailable_Add_Flag
			WHEN 'Y' THEN 1
			WHEN 'N' THEN 0 ELSE NULL END, 
		CASE Mailable_Add_Flag
			WHEN 'Y' THEN GetDate() ELSE NULL END, 
		'U', 1, GetDate(), @user_pk, GetDate(), @user_pk, COALESCE(AddressChange_Dt, GetDate())
	   FROM DM_migratePerson
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Insert Person SMA
	INSERT INTO Person_SMA 
		(address_pk, person_pk, sequence, determined_dt, current_fg)
	 SELECT address_pk, person_pk, 1, GetDate(), 1 
	   FROM Person_Address a
	  WHERE addr_type = (SELECT com_cd_pk 
			       FROM Common_Codes 
			      WHERE com_cd_desc = 'Home' 
			        AND com_cd_type_key = 'PersonAddressType')
	    AND NOT EXISTS(SELECT person_pk FROM Person_SMA WHERE person_pk = a.person_pk)
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Insert Person_Phone
	INSERT INTO Person_Phone
		(person_pk, country_cd, area_code, phone_no, phone_prmry_fg, 
		phone_type, dept, created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
	 SELECT leg_person_pk, '1', SubString(PhoneNumber, 1, 3),
		SubString(PhoneNumber, 4, 7), 1,  
		(SELECT com_cd_pk 
		   FROM Common_Codes 
		  WHERE com_cd_desc = 'Home' 
		    AND com_cd_type_key = 'PhoneType'),
		(SELECT com_cd_pk FROM Common_Codes 
		  WHERE com_cd_cd = @membership_dept_cd 
		    AND com_cd_type_key = @dept_type_key),
		@user_pk, GetDate(), @user_pk, GetDate()
	   FROM DM_migratePerson
	  WHERE SubString(PhoneNumber, 4, 7) IS NOT NULL 
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Insert Person_Demographics information
	INSERT INTO Person_Demographics(person_pk, gender,
		created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
	 SELECT leg_person_pk, 
		(SELECT com_cd_pk FROM DM_Code_Mapping_view 
		  WHERE com_cd_type_key = 'Gender'
		    AND Legacy_Code = Gender),
		@user_pk, GetDate(), @user_pk, GetDate()
	   FROM DM_migratePerson
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

/*	-- Insert in the COM_AFL_CIO_COPE
	INSERT INTO COM_AFL_CIO_COPE(person_pk, political_party, political_registered_voter)
	 SELECT leg_person_pk,  
		(SELECT com_cd_pk FROM DM_Code_Mapping_view 
		  WHERE com_cd_type_key = 'PoliticalParty' 
		    AND Legacy_Code = Political_Party),
		(SELECT com_cd_pk FROM DM_Code_Mapping_view 
		  WHERE com_cd_type_key = 'RegisteredVoter' 
		    AND Legacy_Code = Register_Voter)
	   FROM DM_migratePerson
*/
	-- Create stub for Person_Political_Legislative
	INSERT INTO Person_Political_Legislative
		(person_pk, created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
	 SELECT leg_person_pk,  
		@user_pk, GetDate(), @user_pk, GetDate()
	   FROM DM_migratePerson
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Create stub in Person_Email for all 'EmailType' in Common_Codes (Primary and Alternate)
	INSERT INTO Person_Email
		(person_pk, email_type, person_email_addr, 
		created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
	 SELECT leg_person_pk, com_cd_pk, '', 
		@user_pk, GetDate(), @user_pk, GetDate()
	   FROM DM_migratePerson, Common_Codes
	  WHERE com_cd_type_key = 'EmailType' 
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

--> Check for Persons that are also Members 
	-- Set Members with an invalid affiliate identifier into the error table
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
/*
DECLARE @aff_pk int,
	@user_pk integer, 
	@membership_dept_cd varchar(8),
	@dept_type_key varchar(20)
DECLARE @errorCode int

SET @user_pk = (SELECT person_pk FROM users WHERE user_id = 'migration')
SET @membership_dept_cd = 'MD'
SET @dept_type_key = 'Department'
*/
--BEGIN TRAN sp_migratePersonMember
	-- Create temporary to determine and process the members for aff_pk and join date
	 SELECT leg_person_pk, 
		(SELECT aff_pk FROM aff_Organizations 
		  WHERE Affiliate_Type = aff_type
		    AND Affiliate_Local = aff_localSubChapter
		    AND Affiliate_State = aff_stateNat_type
		    AND Council = aff_councilRetiree_chap) aff_pk,
		0 mbr_status, 0 mbr_type, Information_Source primary_information_source,
		No_Mail_Flag no_mail_fg, No_Mail_Flag no_cards_fg, 
		No_Mail_Flag no_public_emp_fg, No_Mail_Flag no_legislative_mail_fg,
		CONVERT(DateTime, SubString(DateJoined,3, 4)+SubString(DateJoined,1, 2)+'01') mbr_join_dt, 
		MemberId mbr_no_old_afscme, AffiliateId mbr_no_local,
		@user_pk created_user_pk, GetDate() created_dt, 
		@user_pk lst_mod_user_pk, GetDate() lst_mod_dt,
		Affiliate_Type, StatusCode
	   INTO #tmpAff_Member
	   FROM DM_migratePerson
	  WHERE leg_person_pk NOT IN (SELECT leg_person_pk FROM DM_migratePerson_ERROR)
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR
/*	  WHERE (SELECT aff_pk FROM aff_Organizations 
			  WHERE Affiliate_Type = aff_type
			    AND Affiliate_Local = aff_localSubChapter
			    AND Affiliate_State = aff_stateNat_type
			    AND Council = aff_councilRetiree_chap) IS NOT NULL 
*/	
	-- Remove those that are AFSCME or Affiliate Staff
/*	DELETE t FROM #tmpAff_Member t 
	 WHERE EXISTS (SELECT * FROM DM_migratePerson d 
			WHERE d.leg_person_pk = t.leg_person_pk
	   AND List_Code IN ('07','31','03','32'))
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Update for the Mail Flags, Primary Information Source, Member Status and Type
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
			WHEN Affiliate_Type IN ('L', 'S') AND StatusCode IN ('A','O') 
				THEN (SELECT com_cd_pk 
					FROM Common_Codes 
				       WHERE com_cd_cd = 'A' 
				         AND com_cd_type_key = 'MemberStatus')
			WHEN Affiliate_Type IN ('L', 'S') AND StatusCode = 'T' 
				THEN (SELECT com_cd_pk 
					FROM Common_Codes 
				       WHERE com_cd_cd = 'T' 
				         AND com_cd_type_key = 'MemberStatus')
			WHEN StatusCode = 'N' --AND List_Code NOT IN ('03','07','72') 
				THEN (SELECT com_cd_pk 
					FROM Common_Codes 
				       WHERE com_cd_cd = 'A' 
				         AND com_cd_type_key = 'MemberStatus')
			WHEN StatusCode = 'C' 
				THEN (SELECT com_cd_pk 
					FROM Common_Codes 
				       WHERE com_cd_cd = 'A' 
				         AND com_cd_type_key = 'MemberStatus')
		ELSE 0 END,
		mbr_type = CASE
			WHEN Affiliate_Type = 'L' AND StatusCode IN ('A','O') 
				THEN (SELECT com_cd_pk 
					FROM Common_Codes 
				       WHERE com_cd_cd = 'R' 
				         AND com_cd_type_key = 'MemberType')
			WHEN Affiliate_Type = 'S' AND StatusCode IN ('A','O') 
				THEN (SELECT com_cd_pk 
					FROM Common_Codes 
				       WHERE com_cd_cd = 'T' 
				         AND com_cd_type_key = 'MemberType')
			WHEN Affiliate_Type = 'L' AND StatusCode = 'T' 
				THEN (SELECT com_cd_pk 
					FROM Common_Codes 
				       WHERE com_cd_cd = 'R' 
				         AND com_cd_type_key = 'MemberType')
			WHEN Affiliate_Type = 'S' AND StatusCode = 'T' 
				THEN (SELECT com_cd_pk 
					FROM Common_Codes 
				       WHERE com_cd_cd = 'T' 
				         AND com_cd_type_key = 'MemberType')
			WHEN StatusCode = 'N' --AND List_Code NOT IN ('03','07','72') 
				THEN (SELECT com_cd_pk 
					FROM Common_Codes 
				       WHERE com_cd_cd = 'A' 
				         AND com_cd_type_key = 'MemberType')
			WHEN StatusCode = 'C' 
				THEN (SELECT com_cd_pk 
					FROM Common_Codes 
				       WHERE com_cd_cd = 'U' 
				         AND com_cd_type_key = 'MemberType')
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
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Update the Person table for the member last user and last mod date
	UPDATE p
	   SET mbr_mst_lst_mod_user_pk = @user_pk,
	       mbr_mst_lst_mod_dt = GetDate()
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
*/	

COMMIT TRAN sp_migratePersonMember
PRINT 'sp_migratePersonMember has completed'

-->> Display the error
E_ERROR:
	IF @errorCode <> 0
	BEGIN
		PRINT 'Error occurred in sp_migratePersonMember() with error code = ' + CONVERT(VARCHAR, @errorCode)
		ROLLBACK TRAN sp_migratePersonMember 
	END

RETURN @errorCode

GO

-- =============================================
-- example to execute the store procedure
-- =============================================
--EXECUTE sp_migratePersonMember

