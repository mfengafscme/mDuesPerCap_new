-- =============================================
-- Procedure to prepare the Person/Member data
-- =============================================
-- creating the store procedure
IF EXISTS (SELECT name 
	   FROM   sysobjects 
	   WHERE  name = N'sp_prepPersonData' 
	   AND 	  type = 'P')
    DROP PROCEDURE sp_prepPersonData
GO

CREATE PROCEDURE sp_prepPersonData 
AS
DECLARE @errorCode int

BEGIN TRAN sp_prepPersonData

	-- Copy over only the records that are Persons to the DM_migratePerson table
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
	  WHERE Suffix <> '*' AND UnitCode <> 'L6000A'

	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

--> See if Person already is in system (DMAL)
	-- Find the Person pk for those with a MemberID
	 UPDATE d
	    SET d.leg_person_pk = m.leg_person_pk
	   FROM DM_migratePerson_DMAL d
	   JOIN DM_migratePerson m ON m.MemberId = d.MemberId
	  WHERE d.MemberId IS NOT NULL
	    AND d.List_Code IS NOT NULL
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- From remaining records, check for SSN and confirm with First Name, Last Name 
	 UPDATE d
	    SET d.leg_person_pk = p.leg_person_pk
	   FROM DM_migratePerson_DMAL d
	   JOIN DM_migratePerson p ON p.Social_Security = d.Social_Security
	    AND p.FirstName = d.FirstName
	    AND p.LastName = d.LastName
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Update List Code for matching Person
	 UPDATE p
	    SET p.List_Code = d.List_Code
	   FROM DM_migratePerson_DMAL d
	   JOIN DM_migratePerson p ON p.leg_person_pk = d.leg_person_pk
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

/**********************************************************************************/
--> These people exist only in the DMAL and need to be added to the Person load
	-- Copy over the Persons in DMAL that do not have a Person_pk 
	-- (LEG_PersonMember will assign the Person_pk)
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
	    AND Suffix IS NULL -- <> '*' 
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Copy over the missing DMAL records that are Persons to the DM_migratePerson table
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
	  WHERE leg_person_pk NOT IN (SELECT leg_person_pk FROM DM_migratePerson)
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR


COMMIT TRAN sp_prepPersonData
PRINT 'sp_prepPersonData has completed'

-->> Display the error
E_ERROR:
	IF @errorCode <> 0
	BEGIN
		PRINT 'Error occurred in sp_prepPersonData() with error code = ' + CONVERT(VARCHAR, @errorCode)
		ROLLBACK TRAN sp_prepPersonData 
	END

RETURN @errorCode

GO

-- =============================================
-- example to execute the store procedure
-- =============================================
--EXECUTE sp_prepPersonData

