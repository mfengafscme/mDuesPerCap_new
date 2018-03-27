-- =============================================
-- Process the PersonMember File for Organizations into the Enterprise System
-- =============================================
-- creating the store procedure
IF EXISTS (SELECT name 
	   FROM   sysobjects 
	   WHERE  name = N'sp_migrateOrganization' 
	   AND 	  type = 'P')
    DROP PROCEDURE sp_migrateOrganization
GO

CREATE PROCEDURE sp_migrateOrganization 
AS
DECLARE @org_pk int,
	@leg_person_pk int
DECLARE @errorCode int

BEGIN TRAN sp_migrateOrganization

--> Find the Organizations and move the records to DM_migrateOrg
	-- Where Suffix = '*'
	SET IDENTITY_INSERT DM_migrateOrg ON
	INSERT INTO DM_migrateOrg
		(leg_person_pk, org_pk, MemberId, Affiliate_Type, Affiliate_State, 
		Council, Affiliate_Local, SubLocal, List_Code, Social_Security, 
		Gender, Salutation, LastName, FirstName, MiddleName, Suffix, 
		MailingAddress1, MailingAddress2, City, State, ZipCode, Zip_4, 
		Province, Country, AddressType, Mailable_Add_Flag, No_Mail_Flag, 
		Email, PhoneNumber, StatusCode, DateJoined, Register_Voter, 
		Political_Party, Information_Source, AffiliateId, UnitCode, 
		AddressChange_Dt, AlternateMailName)
	 SELECT leg_person_pk, null, MemberId, Affiliate_Type, 
		Affiliate_State, Council, Affiliate_Local, SubLocal, 
		List_Code, Social_Security, Gender, Salutation, 
		LastName, FirstName, MiddleName, Suffix, 
		MailingAddress1, MailingAddress2, City, State, ZipCode, 
		Zip_4, Province, Country, AddressType, 
		Mailable_Add_Flag, No_Mail_Flag, Email, PhoneNumber, 
		StatusCode, DateJoined, Register_Voter, Political_Party, 
		Information_Source, AffiliateId, UnitCode, 
		AddressChange_Dt, AlternateMailName
	   FROM LEG_PersonMember
	  WHERE Suffix = '*'
	    AND UnitCode = 'L6000A'
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR
	SET IDENTITY_INSERT DM_migrateOrg OFF

	-- For Organizations found in DMAL, update the list code
	 UPDATE m
	    SET m.List_Code = d.List_Code
	   FROM DM_migratePerson_DMAL d
	   JOIN DM_migrateOrg m ON m.LastName = d.LastName
		AND m.FirstName = d.FirstName
		AND m.Suffix = d.Suffix
	  WHERE d.List_Code IN (SELECT mailing_list_legacy_cd FROM Mailing_Lists_by_Orgs)
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- For remaining DMAL Organizations, add them to the DM_migrateOrg table
	INSERT INTO DM_migrateOrg
		(org_pk, MemberId, Affiliate_Type, Affiliate_State, 
		Council, Affiliate_Local, SubLocal, List_Code, Social_Security, 
		Gender, Salutation, LastName, FirstName, MiddleName, Suffix, 
		MailingAddress1, MailingAddress2, City, State, ZipCode, Zip_4, 
		Province, Country, AddressType, Mailable_Add_Flag, No_Mail_Flag, 
		Email, PhoneNumber, StatusCode, DateJoined, Register_Voter, 
		Political_Party, Information_Source, AffiliateId, UnitCode, 
		AddressChange_Dt, AlternateMailName)
	 SELECT null, MemberId, Affiliate_Type, 
		Affiliate_State, Council, Affiliate_Local, SubLocal, 
		List_Code, Social_Security, Gender, Salutation, 
		LastName, FirstName, MiddleName, Suffix, 
		MailingAddress1, MailingAddress2, City, State, ZipCode, 
		Zip_4, Province, Country, AddressType, 
		Mailable_Add_Flag, No_Mail_Flag, Email, PhoneNumber, 
		StatusCode, DateJoined, Register_Voter, Political_Party, 
		Information_Source, AffiliateId, UnitCode, 
		AddressChange_Dt, AlternateMailName
	   FROM DM_migratePerson_DMAL d
	  WHERE List_Code IN (SELECT mailing_list_legacy_cd FROM Mailing_Lists_by_Orgs)
	    AND NOT EXISTS ( SELECT * FROM DM_migrateOrg m 
			      WHERE m.LastName = d.LastName
				AND m.FirstName = d.FirstName
				AND m.Suffix = d.Suffix)
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

--Remove the DMAL Organizations, otherwise it will error in the Person load
	DELETE FROM DM_migratePerson_DMAL
	 WHERE List_Code IN (SELECT mailing_list_legacy_cd FROM Mailing_Lists_by_Orgs)

-->> Populate Org_Parent	
	-- All should be Organizations (subtype = 24002), put all in Org_Parent
	INSERT INTO Org_Parent (org_subtype)
	 SELECT '24002'
	  FROM DM_migrateOrg
	 WHERE org_pk IS NULL
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Get first org_pk
	SET @org_pk = (SELECT MIN(org_pk) FROM Org_Parent WHERE org_subtype = '24002')

	-- Set up a cursor for those records that do not have an org_pk
	DECLARE cur_migrateOrg CURSOR
	 FOR SELECT leg_person_pk
	       FROM DM_migrateOrg
	      WHERE org_pk IS NULL
	OPEN cur_migrateOrg
	FETCH NEXT FROM cur_migrateOrg INTO @leg_person_pk	-- get the first record
	WHILE @@FETCH_STATUS = 0
	BEGIN
		-- Update DM_Org_Migrated with the @org_pk
		UPDATE DM_migrateOrg
		   SET org_pk = @org_pk
		 WHERE leg_person_pk = @leg_person_pk

		-- Get the next org_pk
		SET @org_pk = @org_pk + 1
		-- Get the next record
		FETCH NEXT FROM cur_migrateOrg INTO @leg_person_pk
	END
	CLOSE cur_migrateOrg
	DEALLOCATE cur_migrateOrg	

-- Add to External_Organizations
	INSERT INTO dbo.External_Organizations
		(org_pk, org_nm, marked_for_deletion_fg,
		created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
	 SELECT org_pk, 
		CASE WHEN FirstName = ' ' THEN Coalesce(Substring(LastName,1,4), '')
			ELSE Coalesce(RTrim(FirstName) + ' ', '') + Coalesce(Substring(LastName,1,4), '') END,
		0, 
		created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt
	   FROM DM_migrateOrg dm
	  WHERE NOT EXISTS (SELECT org_pk FROM dbo.External_Organizations ext
			     WHERE ext.org_pk = dm.org_pk )
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

-- If location information is available, 
	-- Populate Org_Locations
		INSERT INTO Org_Locations
			(org_pk, location_primary_fg,
			created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
		 SELECT org_pk, 1, created_user_pk, created_dt, lst_mod_user_pk, 
			COALESCE(SubString(AddressChange_Dt, 1, 10), GetDate())
		   FROM DM_migrateOrg
		  WHERE Len(LTRIM(COALESCE(MailingAddress1,''))+
			LTRIM(COALESCE(MailingAddress2, ''))+
			LTRIM(COALESCE(City,''))+LTRIM(COALESCE(State,''))) > 0
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Populate Org_Address
		INSERT INTO Org_Address (org_locations_pk, org_addr_type, 
			addr1, addr2, city, state, zipcode, zip_plus, 
			addr_bad_fg, addr_bad_dt, province, country,
			created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
		 SELECT loc.org_locations_pk, 
			(SELECT com_cd_pk FROM Common_Codes 
			  WHERE com_cd_type_key = 'OrgAddressType' 
			    AND com_cd_desc = 'Regular'),
			COALESCE(leg.MailingAddress1, ''),
			leg.MailingAddress2, leg.City, leg.State, 
			CASE ZipCode WHEN '00000' THEN NullIf(ZipCode, '00000') ELSE ZipCode END,
			CASE Zip_4 WHEN '0000' THEN NullIf(Zip_4, '0000') ELSE Zip_4 END, 
			CASE leg.Mailable_Add_Flag WHEN 'Y' THEN 0 ELSE 1 END,
			CASE leg.Mailable_Add_Flag WHEN 'N' THEN GetDate() END,
			province, 
			(SELECT com_cd_pk FROM Common_Codes 
			  WHERE com_cd_type_key = 'Country' 
			    AND com_cd_cd = country),
			leg.created_user_pk, leg.created_dt, leg.lst_mod_user_pk, COALESCE(SubString(AddressChange_Dt, 1, 10), GetDate())
		   FROM DM_migrateOrg leg
		   JOIN Org_Locations loc ON leg.org_pk = loc.org_pk
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- If phone exists, populate Org_Phone
		INSERT INTO Org_Phone (org_locations_pk, org_phone_type, country_cd, 
			area_code, phone_no,
			created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
		 SELECT loc.org_locations_pk,
			(SELECT com_cd_pk FROM Common_Codes 
			  WHERE com_cd_type_key = 'OrgPhoneType' 
			    AND com_cd_desc = 'Office'), 1, 
			SubString(PhoneNumber, 1, 3),
			SubString(PhoneNumber, 4, 7),
			leg.created_user_pk, leg.created_dt, leg.lst_mod_user_pk, COALESCE(SubString(AddressChange_Dt, 1, 10), GetDate())
		   FROM DM_migrateOrg leg
		   JOIN Org_Locations loc ON leg.org_pk = loc.org_pk
		  WHERE SubString(PhoneNumber, 4, 7) IS NOT NULL
		     OR PhoneNumber <> ''
		     OR PhoneNumber <> '0000000000' 
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

COMMIT TRAN sp_migrateOrganization
PRINT 'sp_migrateOrganization has completed'

-->> Display the error
E_ERROR:
	IF @errorCode <> 0
	BEGIN
		PRINT 'Error occurred in sp_migrateOrganization() with error code = ' + CONVERT(VARCHAR, @errorCode)
		ROLLBACK TRAN sp_migrateOrganization 
	END

RETURN @errorCode

GO

-- =============================================
-- example to execute the store procedure
-- =============================================
--EXECUTE sp_migrateOrganization

