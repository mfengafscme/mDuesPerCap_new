-- =============================================
-- Procedure to handle the Person data
-- =============================================
-- creating the store procedure
IF EXISTS (SELECT name 
	   FROM   sysobjects 
	   WHERE  name = N'sp_migratePerson' 
	   AND 	  type = 'P')
    DROP PROCEDURE sp_migratePerson
GO

CREATE PROCEDURE sp_migratePerson 
AS
DECLARE @membershipDept int,
	@PersonAddressType int,
	@PhoneTypeHome int,
	@errorCode int

SET @membershipDept = (SELECT com_cd_pk FROM Common_Codes 
			WHERE com_cd_cd = 'MD' 
			  AND com_cd_type_key = 'Department')
SET @PersonAddressType = (SELECT com_cd_pk 
			       FROM Common_Codes 
			      WHERE com_cd_desc = 'Home' 
			        AND com_cd_type_key = 'PersonAddressType')
SET @PhoneTypeHome = (SELECT com_cd_pk 
			FROM Common_Codes 
		       WHERE com_cd_desc = 'Home' 
		 	 AND com_cd_type_key = 'PhoneType')
BEGIN TRAN sp_migratePerson

-- Final Prep
	 UPDATE DM_migratePerson 
	    SET Country = NULLIF(Country, ''),
		ZipCode = CASE ZipCode WHEN '00000' THEN NullIf(ZipCode, '00000') 
			     WHEN '0' THEN NullIf(ZipCode, '0') ELSE ZipCode END,
		Zip_4 = CASE Zip_4 WHEN '0000' THEN NullIf(Zip_4, '0000') 
			   WHEN '0' THEN NullIf(Zip_4, '0') ELSE Zip_4 END


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
		0,
		created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt, lst_mod_user_pk, lst_mod_dt
	   FROM DM_migratePerson
	SET IDENTITY_INSERT Person OFF
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Insert Person_Address
	INSERT INTO Person_Address
		(person_pk, addr1, addr2, city, state, zipcode, zip_plus, province, 
		country, dept, addr_type, addr_bad_fg, addr_marked_bad_dt, 
		addr_source, addr_prmry_fg, eff_dt, created_user_pk, created_dt, 
		lst_mod_user_pk, lst_mod_dt)
	 SELECT leg_person_pk, MailingAddress1, MailingAddress2, City, 
		CASE WHEN State = 'ZZ' THEN NULL ELSE State END, 
		ZipCode, Zip_4,	Province, 
		(SELECT com_cd_pk FROM Common_Codes 
		  WHERE com_cd_type_key = 'Country' 
		    AND com_cd_cd = Country), 
		@membershipDept, @PersonAddressType,
		CASE Mailable_Add_Flag
			WHEN 'Y' THEN 0
			WHEN 'N' THEN 1 ELSE NULL END, 
		CASE Mailable_Add_Flag
			WHEN 'N' THEN GetDate() ELSE NULL END, 
		'U', 1, GetDate(), created_user_pk, created_dt, lst_mod_user_pk, COALESCE(SubString(AddressChange_Dt,1,10), GetDate())
	   FROM DM_migratePerson
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Insert Person SMA
	INSERT INTO Person_SMA 
		(address_pk, person_pk, sequence, determined_dt, current_fg)
	 SELECT address_pk, person_pk, 1, GetDate(), 1 
	   FROM Person_Address a
	  WHERE addr_type = @PersonAddressType
	    AND NOT EXISTS(SELECT person_pk FROM Person_SMA WHERE person_pk = a.person_pk)
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Insert Person_Phone
	INSERT INTO Person_Phone
		(person_pk, country_cd, area_code, phone_no, phone_prmry_fg, 
		phone_type, dept, created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
	 SELECT leg_person_pk, '1', 
		SubString(PhoneNumber, 1, 3),
		SubString(PhoneNumber, 4, 7),
		1, @PhoneTypeHome, @membershipDept,
		created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt
	   FROM DM_migratePerson
	  WHERE PhoneNumber IS NOT NULL
	     OR PhoneNumber <> ''
	     OR PhoneNumber <> '0000000000' 
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
		created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt
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
		created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt
	   FROM DM_migratePerson
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Create stub in Person_Email for all 'EmailType' in Common_Codes (Primary and Alternate)
	INSERT INTO Person_Email
		(person_pk, email_type, person_email_addr, 
		created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
	 SELECT leg_person_pk, com_cd_pk, 
		CASE WHEN com_cd_cd = 1 AND Len(email) > 0 THEN email ELSE '' END, 
		l.created_user_pk, l.created_dt, l.lst_mod_user_pk, l.lst_mod_dt
	   FROM DM_migratePerson l, Common_Codes
	  WHERE com_cd_type_key = 'EmailType' 
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

COMMIT TRAN sp_migratePerson
PRINT 'sp_migratePerson has completed'

-->> Display the error
E_ERROR:
	IF @errorCode <> 0
	BEGIN
		PRINT 'Error occurred in sp_migratePerson() with error code = ' + CONVERT(VARCHAR, @errorCode)
		ROLLBACK TRAN sp_migratePerson 
	END

RETURN @errorCode

GO

-- =============================================
-- example to execute the store procedure
-- =============================================
--EXECUTE sp_migratePerson

