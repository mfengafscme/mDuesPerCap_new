--- =============================================
-- Procedure to handle the errors for Person/Member DMAL file(s)
-- =============================================
-- creating the store procedure
IF EXISTS (SELECT name 
	   FROM   sysobjects 
	   WHERE  name = N'sp_errorDMAL' 
	   AND 	  type = 'P')
    DROP PROCEDURE sp_errorDMAL
GO

CREATE PROCEDURE sp_errorDMAL 
AS
DECLARE @errorCode int
BEGIN TRAN sp_errorDMAL

--> DELETE FROM DM_migratePerson_DMAL_ERROR
--> Identify the errors
	-- Those without a List Code are an error
	INSERT INTO DM_migratePerson_DMAL_ERROR
		(leg_person_pk, ERROR, MemberId, Affiliate_Type, Affiliate_State, Council, 
		Affiliate_Local, SubLocal, List_Code, Social_Security, Gender, Salutation, 
		LastName, FirstName, MiddleName, Suffix, MailingAddress1, MailingAddress2, 
		City, State, ZipCode, Zip_4, Province, Country, AddressType, Mailable_Add_Flag, 
		No_Mail_Flag, Email, PhoneNumber, StatusCode, DateJoined, Register_Voter, 
		Political_Party, Information_Source, AffiliateId, UnitCode, AddressChange_Dt, 
		AlternateMailName)
	 SELECT leg_person_pk, 'No List Code', MemberId, Affiliate_Type, 
		Affiliate_State, Council, Affiliate_Local, SubLocal, List_Code, Social_Security, 
		Gender, Salutation, LastName, FirstName, MiddleName, Suffix, MailingAddress1, 
		MailingAddress2, City, State, ZipCode, Zip_4, Province, Country, AddressType, 
		Mailable_Add_Flag, No_Mail_Flag, Email, PhoneNumber, StatusCode, DateJoined, 
		Register_Voter, Political_Party, Information_Source, AffiliateId, UnitCode, 
		AddressChange_Dt, AlternateMailName
	   FROM LEG_PersonMember_DMAL
	  WHERE List_Code IS NULL
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Those with an invalid List Code are an error
	INSERT INTO DM_migratePerson_DMAL_ERROR
		(leg_person_pk, ERROR, MemberId, Affiliate_Type, Affiliate_State, Council, 
		Affiliate_Local, SubLocal, List_Code, Social_Security, Gender, Salutation, 
		LastName, FirstName, MiddleName, Suffix, MailingAddress1, MailingAddress2, 
		City, State, ZipCode, Zip_4, Province, Country, AddressType, Mailable_Add_Flag, 
		No_Mail_Flag, Email, PhoneNumber, StatusCode, DateJoined, Register_Voter, 
		Political_Party, Information_Source, AffiliateId, UnitCode, AddressChange_Dt, 
		AlternateMailName)
	 SELECT leg_person_pk, 'Invalid List Code - '+List_Code, MemberId, Affiliate_Type, 
		Affiliate_State, Council, Affiliate_Local, SubLocal, List_Code, Social_Security, 
		Gender, Salutation, LastName, FirstName, MiddleName, Suffix, MailingAddress1, 
		MailingAddress2, City, State, ZipCode, Zip_4, Province, Country, AddressType, 
		Mailable_Add_Flag, No_Mail_Flag, Email, PhoneNumber, StatusCode, DateJoined, 
		Register_Voter, Political_Party, Information_Source, AffiliateId, UnitCode, 
		AddressChange_Dt, AlternateMailName
	   FROM LEG_PersonMember_DMAL
	  WHERE Convert(int, List_Code) NOT IN (SELECT mailing_list_legacy_cd
				    FROM Mailing_Lists_by_Person)
	    AND Convert(int, List_Code) NOT IN (SELECT mailing_list_legacy_cd
				    FROM Mailing_Lists_by_Orgs)
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

--> List Code of 07 and 31 are Affiliate Staff
	-- Affiliate Staff must have an Affiliate to be associated with
	 INSERT INTO DM_migratePerson_DMAL_ERROR
		(leg_person_pk, ERROR, MemberId, Affiliate_Type, Affiliate_State, Council, 
		Affiliate_Local, SubLocal, List_Code, Social_Security, Gender, Salutation, 
		LastName, FirstName, MiddleName, Suffix, MailingAddress1, MailingAddress2, 
		City, State, ZipCode, Zip_4, Province, Country, AddressType, Mailable_Add_Flag, 
		No_Mail_Flag, Email, PhoneNumber, StatusCode, DateJoined, Register_Voter, 
		Political_Party, Information_Source, AffiliateId, UnitCode, AddressChange_Dt, 
		AlternateMailName)
	 SELECT leg_person_pk, 'Affiliate Staff must have a valid affiliate', MemberId, Affiliate_Type, Affiliate_State, Council, 
		Affiliate_Local, SubLocal, List_Code, Social_Security, Gender, Salutation, 
		LastName, FirstName, MiddleName, Suffix, MailingAddress1, MailingAddress2, 
		City, State, ZipCode, Zip_4, Province, Country, AddressType, Mailable_Add_Flag, 
		No_Mail_Flag, Email, PhoneNumber, StatusCode, DateJoined, Register_Voter, 
		Political_Party, Information_Source, AffiliateId, UnitCode, AddressChange_Dt, 
		AlternateMailName 
	   FROM LEG_PersonMember_DMAL d
	  WHERE (SELECT aff_pk FROM aff_Organizations 
		   WHERE Affiliate_Type = aff_type
		     AND Affiliate_Local = aff_localSubChapter
		     AND Affiliate_State = aff_stateNat_type
		     AND Council = aff_councilRetiree_chap) = NULL
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Put the remaining into the processing table
	 INSERT INTO DM_migratePerson_DMAL
		(leg_person_pk, MemberId, Affiliate_Type, Affiliate_State, Council, 
		Affiliate_Local, SubLocal, List_Code, Social_Security, Gender, Salutation, 
		LastName, FirstName, MiddleName, Suffix, MailingAddress1, MailingAddress2, 
		City, State, ZipCode, Zip_4, Province, Country, AddressType, Mailable_Add_Flag, 
		No_Mail_Flag, Email, PhoneNumber, StatusCode, DateJoined, Register_Voter, 
		Political_Party, Information_Source, AffiliateId, UnitCode, AddressChange_Dt, 
		AlternateMailName)
	 SELECT leg_person_pk, MemberId, Affiliate_Type, Affiliate_State, Council, 
		Affiliate_Local, SubLocal, 
		CASE WHEN State = 'ZZ' THEN NULL ELSE List_Code END, 
		Social_Security, Gender, Salutation, 
		LastName, FirstName, MiddleName, Suffix, MailingAddress1, MailingAddress2, 
		City, 
		CASE WHEN State = 'ZZ' THEN NULL ELSE STATE END, 
		ZipCode, Zip_4, Province, Country, AddressType, Mailable_Add_Flag, 
		No_Mail_Flag, Email, PhoneNumber, StatusCode, DateJoined, Register_Voter, 
		Political_Party, Information_Source, AffiliateId, UnitCode, AddressChange_Dt, 
		AlternateMailName 
	   FROM LEG_PersonMember_DMAL d
	  WHERE List_Code IS NOT NULL
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

COMMIT TRAN sp_errorDMAL
PRINT 'sp_errorDMAL has completed'

-->> Display the error
E_ERROR:
	IF @errorCode <> 0
	BEGIN
		PRINT 'Error occurred in sp_errorDMAL() with error code = ' + CONVERT(VARCHAR, @errorCode)
		ROLLBACK TRAN sp_errorDMAL 
	END

RETURN @errorCode

GO

-- =============================================
-- example to execute the store procedure
-- =============================================
--EXECUTE sp_errorDMAL

