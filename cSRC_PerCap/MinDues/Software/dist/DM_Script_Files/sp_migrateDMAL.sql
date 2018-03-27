--- =============================================
-- Procedure to handle the Person/Member DMAL file(s)
-- =============================================
-- creating the store procedure
IF EXISTS (SELECT name 
	   FROM   sysobjects 
	   WHERE  name = N'sp_migrateDMAL' 
	   AND 	  type = 'P')
    DROP PROCEDURE sp_migrateDMAL
GO

CREATE PROCEDURE sp_migrateDMAL 
AS
DECLARE @user_pk integer
DECLARE @errorCode int

SET @user_pk = (SELECT person_pk FROM users WHERE user_id = 'migration')

BEGIN TRAN sp_migrateDMAL

--> DELETE FROM DM_migratePerson_DMAL
--> DELETE FROM DM_migratePerson_DMAL_ERROR

--> List Code of 07 and 31 are Affiliate Staff
	-- Insert into the Aff_Staff
	INSERT INTO Aff_Staff
		(aff_pk, person_pk, created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
	 SELECT (SELECT aff_pk FROM aff_Organizations 
		  WHERE Affiliate_Type = aff_type
		    AND Affiliate_Local = aff_localSubChapter
		    AND Affiliate_State = aff_stateNat_type
		    AND Council = aff_councilRetiree_chap
		    AND SubLocal = aff_subUnit
		    AND SubString(UnitCode, 6, 1) = aff_code),
		leg_person_pk, created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt
	   FROM DM_migratePerson
	  WHERE List_Code IN ('07','31')
	    AND (SELECT aff_pk FROM aff_Organizations 
		  WHERE Affiliate_Type = aff_type
		    AND Affiliate_Local = aff_localSubChapter
		    AND Affiliate_State = aff_stateNat_type
		    AND Council = aff_councilRetiree_chap
		    AND SubLocal = aff_subUnit
		    AND SubString(UnitCode, 6, 1) = aff_code) IS NOT NULL
--	    AND leg_person_pk IS NULL
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR
	-- Extra: Not in requirements.  Set the Org_Location primary key for the staff member
	UPDATE a
	   SET a.org_locations_pk = o.org_locations_pk
	  FROM Org_Locations o
	  JOIN Aff_Staff a ON org_pk = aff_pk
	   AND location_primary_fg = 1
	-- Extra: Not in requirements.  Add Affiliate Staff to User table.
	-- Default Start Page to Affiliate Data Utility 
	INSERT INTO Users
		(person_pk, dept, start_page, user_id, pwd, challenge_question, bad_login_attempt_ct)
		SELECT person_pk, NULL, 'D', person_pk, NULL, 1001, 0
		  FROM Aff_Staff 

--> List Code of 03 and 72 are AFSCME Staff
	INSERT INTO Users
		(person_pk, dept, start_page, user_id, pwd, challenge_question, bad_login_attempt_ct)
	 SELECT leg_person_pk, 
		(SELECT com_cd_pk FROM Common_Codes
		  WHERE com_cd_type_key = 'Department' 
		    AND com_cd_cd = 'FO'), 'A', leg_person_pk, NULL, 1001, 0
	   FROM DM_migratePerson
	  WHERE List_Code = '03'
--	    AND leg_person_pk IS NULL

	INSERT INTO Users
		(person_pk, dept, start_page, user_id, pwd, challenge_question, bad_login_attempt_ct)
	 SELECT leg_person_pk, 
		(SELECT com_cd_pk FROM Common_Codes
		  WHERE com_cd_type_key = 'Department' 
		    AND com_cd_cd = 'HQ'), 'A', leg_person_pk, NULL, 1001, 0
	   FROM DM_migratePerson
	  WHERE List_Code = '72'
--	    AND leg_person_pk IS NULL
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

--> These need to be added to a Mailing List
	-- Add these to the Org Mailing List
	INSERT INTO Mailing_List_orgs
		(MLBO_mailing_list_pk, org_pk, org_locations_pk, 
		created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
	 SELECT MLBO_mailing_list_pk, org_pk,
		(SELECT org_locations_pk FROM Org_Locations 
		  WHERE org_pk = o.org_pk),
		@user_pk, GetDate(), @user_pk, GetDate()					
	   FROM DM_migrateOrg o
	   JOIN Mailing_Lists_by_Orgs m ON m.mailing_list_legacy_cd = o.List_Code
	  WHERE List_Code IS NOT NULL
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Add these to the Person Mailing List
	INSERT INTO MLBP_Persons
		(person_pk, MLBP_mailing_list_pk, address_pk, 
		created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
	 SELECT leg_person_pk, MLBP_mailing_list_pk,
		(SELECT address_pk FROM Person_SMA 
		  WHERE person_pk = p.leg_person_pk AND current_fg = 1),
		p.created_user_pk, p.created_dt, p.lst_mod_user_pk, p.lst_mod_dt			
	   FROM DM_migratePerson p
	   JOIN Mailing_Lists_by_Person m ON m.mailing_list_legacy_cd = p.List_Code
	  WHERE List_Code IS NOT NULL
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Extra: Not in requirements.  
	-- Add those that are in the Person table but NOT in the User table to User table.
	-- Default Start Page to View Personal Information 
	INSERT INTO Users
		(person_pk, dept, start_page, user_id, pwd, challenge_question, bad_login_attempt_ct)
		SELECT person_pk, NULL, 'M', person_pk, NULL, 1001, 0
		  FROM Person p WHERE NOT EXISTS (SELECT * FROM Users u WHERE u.person_pk = p.person_pk) 
--> 
--***************************************************************
COMMIT TRAN sp_migrateDMAL
PRINT 'sp_migrateDMAL has completed'

-->> Display the error
E_ERROR:
	IF @errorCode <> 0
	BEGIN
		PRINT 'Error occurred in sp_migrateDMAL() with error code = ' + CONVERT(VARCHAR, @errorCode)
		ROLLBACK TRAN sp_migrateDMAL 
	END

RETURN @errorCode

GO

-- =============================================
-- example to execute the store procedure
-- =============================================
--EXECUTE sp_migrateDMAL	
