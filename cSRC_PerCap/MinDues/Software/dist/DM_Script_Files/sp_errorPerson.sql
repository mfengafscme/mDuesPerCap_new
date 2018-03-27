-- =============================================
-- Check for Person/Member Errors and remove from processing
-- =============================================
-- creating the store procedure
IF EXISTS (SELECT name 
	   FROM   sysobjects 
	   WHERE  name = N'sp_errorPerson' 
	   AND 	  type = 'P')
    DROP PROCEDURE sp_errorPerson
GO

CREATE PROCEDURE sp_errorPerson 
AS
DECLARE @errorCode int

BEGIN TRAN sp_errorPerson
-- Set the Unknown values to NULL
 UPDATE DM_migratePerson 
    SET Salutation = CASE Salutation WHEN 'U' THEN NULLIF(Salutation, 'U')
				     WHEN '8' THEN NULLIF(Salutation, '8') 
				     ELSE Salutation END,
	Register_Voter = NULLIF(Register_Voter, 'U'),
	Political_Party = NULLIF(Political_Party, 'U'),
	Gender = NULLIF(Gender, 'U'),
	DateJoined = CASE DateJoined WHEN '' THEN NULLIF(DateJoined, '')
				     WHEN '000000' THEN NULLIF(DateJoined, '000000') 
				     ELSE DateJoined END,
	Suffix = CASE Suffix WHEN 'U' THEN NULLIF(Suffix, 'U')
			     WHEN '' THEN NULLIF(Suffix, '')
			     WHEN '7' THEN NULLIF(Suffix, '7') 
			     ELSE Suffix END
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Invalid Status Code (Member Error)
	INSERT INTO DM_migratePerson_ERROR
		(leg_person_pk, ERROR, MemberId, Affiliate_Type, Affiliate_State, 
		Council, Affiliate_Local, SubLocal, List_Code, Social_Security, 
		Gender, Salutation, LastName, FirstName, MiddleName, Suffix, 
		MailingAddress1, MailingAddress2, City, State, ZipCode, Zip_4, 
		Province, Country, AddressType, Mailable_Add_Flag, No_Mail_Flag, 
		Email, PhoneNumber, StatusCode, DateJoined, Register_Voter, 
		Political_Party, Information_Source, AffiliateId, UnitCode, 
		AddressChange_Dt, AlternateMailName)
	 SELECT leg_person_pk, 'Invalid StatusCode - ' + COALESCE(StatusCode, 'NULL'),  
		MemberId, Affiliate_Type, Affiliate_State, Council, 
		Affiliate_Local, SubLocal, List_Code, Social_Security, 
		Gender, Salutation, LastName, FirstName, MiddleName, Suffix, 
		MailingAddress1, MailingAddress2, City, State, ZipCode, 
		Zip_4, Province, Country, AddressType, Mailable_Add_Flag, 
		No_Mail_Flag, Email, PhoneNumber, StatusCode, DateJoined, 
		Register_Voter, Political_Party, Information_Source, AffiliateId, 
		UnitCode, AddressChange_Dt, AlternateMailName 
	   FROM DM_migratePerson
	  WHERE StatusCode NOT IN ('X','R','A','O','T','N','C')
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Invalid Salutation Legacy Code
--	UPDATE DM_migratePerson SET Salutation = NULL WHERE Salutation IN ('U','8')
	-- Get error code
	INSERT INTO DM_migratePerson_ERROR
		(leg_person_pk, ERROR, MemberId, Affiliate_Type, Affiliate_State, 
		Council, Affiliate_Local, SubLocal, List_Code, Social_Security, 
		Gender, Salutation, LastName, FirstName, MiddleName, Suffix, 
		MailingAddress1, MailingAddress2, City, State, ZipCode, Zip_4, 
		Province, Country, AddressType, Mailable_Add_Flag, No_Mail_Flag, 
		Email, PhoneNumber, StatusCode, DateJoined, Register_Voter, 
		Political_Party, Information_Source, AffiliateId, UnitCode, 
		AddressChange_Dt, AlternateMailName)
	 SELECT leg_person_pk, 'Invalid Salutation/Prefix Legacy Code - ' + COALESCE(Salutation, 'NULL'), 
		MemberId, Affiliate_Type, Affiliate_State, Council, 
		Affiliate_Local, SubLocal, List_Code, Social_Security, 
		Gender, Salutation, LastName, FirstName, MiddleName, Suffix, 
		MailingAddress1, MailingAddress2, City, State, ZipCode, 
		Zip_4, Province, Country, AddressType, Mailable_Add_Flag, 
		No_Mail_Flag, Email, PhoneNumber, StatusCode, DateJoined, 
		Register_Voter, Political_Party, Information_Source, AffiliateId, 
		UnitCode, AddressChange_Dt, AlternateMailName 
	   FROM DM_migratePerson
	  WHERE Salutation NOT IN (SELECT Legacy_Code FROM DM_Code_Mapping_view
				    WHERE com_cd_type_key = 'Prefix')
	    AND Salutation IS NOT NULL
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Invalid Suffix Legacy Code
--	UPDATE DM_migratePerson SET Suffix = NULL WHERE Suffix IN ('U','', '7')
	-- Get error code
	INSERT INTO DM_migratePerson_ERROR
		(leg_person_pk, ERROR, MemberId, Affiliate_Type, Affiliate_State, 
		Council, Affiliate_Local, SubLocal, List_Code, Social_Security, 
		Gender, Salutation, LastName, FirstName, MiddleName, Suffix, 
		MailingAddress1, MailingAddress2, City, State, ZipCode, Zip_4, 
		Province, Country, AddressType, Mailable_Add_Flag, No_Mail_Flag, 
		Email, PhoneNumber, StatusCode, DateJoined, Register_Voter, 
		Political_Party, Information_Source, AffiliateId, UnitCode, 
		AddressChange_Dt, AlternateMailName)
	 SELECT leg_person_pk, 'Invalid Suffix Legacy Code - ' + COALESCE(Suffix, 'NULL'),
		MemberId, Affiliate_Type, Affiliate_State, Council, 
		Affiliate_Local, SubLocal, List_Code, Social_Security, 
		Gender, Salutation, LastName, FirstName, MiddleName, Suffix, 
		MailingAddress1, MailingAddress2, City, State, ZipCode, 
		Zip_4, Province, Country, AddressType, Mailable_Add_Flag, 
		No_Mail_Flag, Email, PhoneNumber, StatusCode, DateJoined, 
		Register_Voter, Political_Party, Information_Source, AffiliateId, 
		UnitCode, AddressChange_Dt, AlternateMailName 
	   FROM DM_migratePerson
	  WHERE Suffix NOT IN (SELECT Legacy_Code FROM DM_Code_Mapping_view
				WHERE com_cd_type_key = 'Suffix')
	    AND Suffix IS NOT NULL
	    AND Suffix <> '*'	-- Organization
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Invalid Registered Voter Legacy Code
--	UPDATE DM_migratePerson SET Register_Voter = NULL WHERE Register_Voter = 'U'
	INSERT INTO DM_migratePerson_ERROR
		(leg_person_pk, ERROR, MemberId, Affiliate_Type, Affiliate_State, 
		Council, Affiliate_Local, SubLocal, List_Code, Social_Security, 
		Gender, Salutation, LastName, FirstName, MiddleName, Suffix, 
		MailingAddress1, MailingAddress2, City, State, ZipCode, Zip_4, 
		Province, Country, AddressType, Mailable_Add_Flag, No_Mail_Flag, 
		Email, PhoneNumber, StatusCode, DateJoined, Register_Voter, 
		Political_Party, Information_Source, AffiliateId, UnitCode, 
		AddressChange_Dt, AlternateMailName)
	 SELECT leg_person_pk, 'Invalid Registered Voter Legacy Code - ' + COALESCE(Register_Voter, 'NULL'),
		MemberId, Affiliate_Type, Affiliate_State, Council, 
		Affiliate_Local, SubLocal, List_Code, Social_Security, 
		Gender, Salutation, LastName, FirstName, MiddleName, Suffix, 
		MailingAddress1, MailingAddress2, City, State, ZipCode, 
		Zip_4, Province, Country, AddressType, Mailable_Add_Flag, 
		No_Mail_Flag, Email, PhoneNumber, StatusCode, DateJoined, 
		Register_Voter, Political_Party, Information_Source, AffiliateId, 
		UnitCode, AddressChange_Dt, AlternateMailName 
	   FROM DM_migratePerson
	  WHERE Register_Voter NOT IN (SELECT Legacy_Code FROM DM_Code_Mapping_view
				    WHERE com_cd_type_key = 'RegisteredVoter')
	    AND Register_Voter IS NOT NULL
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Invalid Political Party Legacy Code
--	UPDATE DM_migratePerson SET Political_Party = NULL WHERE Political_Party = 'U'
	INSERT INTO DM_migratePerson_ERROR
		(leg_person_pk, ERROR, MemberId, Affiliate_Type, Affiliate_State, 
		Council, Affiliate_Local, SubLocal, List_Code, Social_Security, 
		Gender, Salutation, LastName, FirstName, MiddleName, Suffix, 
		MailingAddress1, MailingAddress2, City, State, ZipCode, Zip_4, 
		Province, Country, AddressType, Mailable_Add_Flag, No_Mail_Flag, 
		Email, PhoneNumber, StatusCode, DateJoined, Register_Voter, 
		Political_Party, Information_Source, AffiliateId, UnitCode, 
		AddressChange_Dt, AlternateMailName)
	 SELECT leg_person_pk, 'Invalid Political Party Legacy Code - ' + COALESCE(Political_Party, 'NULL'),
		MemberId, Affiliate_Type, Affiliate_State, Council, 
		Affiliate_Local, SubLocal, List_Code, Social_Security, 
		Gender, Salutation, LastName, FirstName, MiddleName, Suffix, 
		MailingAddress1, MailingAddress2, City, State, ZipCode, 
		Zip_4, Province, Country, AddressType, Mailable_Add_Flag, 
		No_Mail_Flag, Email, PhoneNumber, StatusCode, DateJoined, 
		Register_Voter, Political_Party, Information_Source, AffiliateId, 
		UnitCode, AddressChange_Dt, AlternateMailName 
	   FROM DM_migratePerson
	  WHERE Political_Party NOT IN (SELECT Legacy_Code FROM DM_Code_Mapping_view
				    WHERE com_cd_type_key = 'PoliticalParty')
	    AND Political_Party IS NOT NULL
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Invalid Gender Legacy Code
--	UPDATE DM_migratePerson SET Gender = NULL WHERE Gender = 'U'
	INSERT INTO DM_migratePerson_ERROR
		(leg_person_pk, ERROR, MemberId, Affiliate_Type, Affiliate_State, 
		Council, Affiliate_Local, SubLocal, List_Code, Social_Security, 
		Gender, Salutation, LastName, FirstName, MiddleName, Suffix, 
		MailingAddress1, MailingAddress2, City, State, ZipCode, Zip_4, 
		Province, Country, AddressType, Mailable_Add_Flag, No_Mail_Flag, 
		Email, PhoneNumber, StatusCode, DateJoined, Register_Voter, 
		Political_Party, Information_Source, AffiliateId, UnitCode, 
		AddressChange_Dt, AlternateMailName)
	 SELECT leg_person_pk, 'Invalid Gender Legacy Code - ' + COALESCE(Gender, 'NULL'),
		MemberId, Affiliate_Type, Affiliate_State, Council, 
		Affiliate_Local, SubLocal, List_Code, Social_Security, 
		Gender, Salutation, LastName, FirstName, MiddleName, Suffix, 
		MailingAddress1, MailingAddress2, City, State, ZipCode, 
		Zip_4, Province, Country, AddressType, Mailable_Add_Flag, 
		No_Mail_Flag, Email, PhoneNumber, StatusCode, DateJoined, 
		Register_Voter, Political_Party, Information_Source, AffiliateId, 
		UnitCode, AddressChange_Dt, AlternateMailName 
	   FROM DM_migratePerson
	  WHERE Gender NOT IN (SELECT Legacy_Code FROM DM_Code_Mapping_view
				    WHERE com_cd_type_key = 'Gender')
	    AND Gender IS NOT NULL
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Remove these records from the PersonMember table to prevent processing
	DELETE FROM DM_migratePerson 
	 WHERE leg_person_pk IN (SELECT leg_person_pk FROM DM_migratePerson_ERROR)
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

-- These can be processed but flag that an error was found in the data
	-- Invalid Join Date (Member Error)
	INSERT INTO DM_migratePerson_ERROR
		(leg_person_pk, ERROR, MemberId, Affiliate_Type, Affiliate_State, 
		Council, Affiliate_Local, SubLocal, List_Code, Social_Security, 
		Gender, Salutation, LastName, FirstName, MiddleName, Suffix, 
		MailingAddress1, MailingAddress2, City, State, ZipCode, Zip_4, 
		Province, Country, AddressType, Mailable_Add_Flag, No_Mail_Flag, 
		Email, PhoneNumber, StatusCode, DateJoined, Register_Voter, 
		Political_Party, Information_Source, AffiliateId, UnitCode, 
		AddressChange_Dt, AlternateMailName)
	 SELECT leg_person_pk, 'Loaded but Invalid Join Date - ' + COALESCE(DateJoined, 'NULL'),
		MemberId, Affiliate_Type, Affiliate_State, Council, 
		Affiliate_Local, SubLocal, List_Code, Social_Security, 
		Gender, Salutation, LastName, FirstName, MiddleName, Suffix, 
		MailingAddress1, MailingAddress2, City, State, ZipCode, 
		Zip_4, Province, Country, AddressType, Mailable_Add_Flag, 
		No_Mail_Flag, Email, PhoneNumber, StatusCode, DateJoined, 
		Register_Voter, Political_Party, Information_Source, AffiliateId, 
		UnitCode, AddressChange_Dt, AlternateMailName 
	   FROM DM_migratePerson
	  WHERE Len(DateJoined) <> 6
	     OR CAST(Substring(DateJoined, 1, 2) AS INTEGER) NOT BETWEEN 1 AND 12
	    AND DateJoined IS NOT NULL
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- For those with errors, set DateJoined to NULL to be able to continue processing
	UPDATE DM_migratePerson SET DateJoined = NULL
	  WHERE Len(DateJoined) <> 6
	     OR CAST(Substring(DateJoined, 1, 2) AS INTEGER) NOT BETWEEN 1 AND 12
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Potential duplicate Person
	INSERT INTO DM_migratePerson_ERROR
		(leg_person_pk, ERROR, MemberId, Affiliate_Type, Affiliate_State, 
		Council, Affiliate_Local, SubLocal, List_Code, Social_Security, 
		Gender, Salutation, LastName, FirstName, MiddleName, Suffix, 
		MailingAddress1, MailingAddress2, City, State, ZipCode, Zip_4, 
		Province, Country, AddressType, Mailable_Add_Flag, No_Mail_Flag, 
		Email, PhoneNumber, StatusCode, DateJoined, Register_Voter, 
		Political_Party, Information_Source, AffiliateId, UnitCode, 
		AddressChange_Dt, AlternateMailName)
	 SELECT leg_person_pk, 'Loaded but Potential Duplicate Person',
		MemberId, Affiliate_Type, Affiliate_State, Council, 
		Affiliate_Local, SubLocal, List_Code, Social_Security, 
		Gender, Salutation, LastName, FirstName, MiddleName, Suffix, 
		MailingAddress1, MailingAddress2, City, State, ZipCode, 
		Zip_4, Province, Country, AddressType, Mailable_Add_Flag, 
		No_Mail_Flag, Email, PhoneNumber, StatusCode, DateJoined, 
		Register_Voter, Political_Party, Information_Source, AffiliateId, 
		UnitCode, AddressChange_Dt, AlternateMailName 
	   FROM DM_migratePerson
	  WHERE MemberId IN ( SELECT MemberId FROM DM_migratePerson
			       WHERE MemberID IS NOT NULL AND MemberId <> ''
			       GROUP BY MemberId, LastName, FirstName, MiddleName, 
					Suffix, City, State, ZipCode, Zip_4 
			      HAVING Count(*) > 1)
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR


COMMIT TRAN sp_errorPerson
PRINT 'sp_errorPerson has completed'

-->> Display the error
E_ERROR:
	IF @errorCode <> 0
	BEGIN
		PRINT 'Error occurred in sp_errorPerson() with error code = ' + CONVERT(VARCHAR, @errorCode)
		ROLLBACK TRAN sp_errorPerson 
	END

RETURN @errorCode

GO

-- =============================================
-- example to execute the store procedure
-- =============================================
--EXECUTE sp_errorPerson 

