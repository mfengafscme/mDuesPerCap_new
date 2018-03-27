-- =============================================
-- Create procedure to add codes to the system
-- =============================================
-- creating the store procedure
IF EXISTS (SELECT name 
	   FROM   sysobjects 
	   WHERE  name = N'sp_migrateCodeTables' 
	   AND 	  type = 'P')
    DROP PROCEDURE sp_migrateCodeTables
GO

CREATE PROCEDURE sp_migrateCodeTables 
AS
DECLARE @user_pk Integer, 
	@membership_dept_cd varchar(8),
	@dept_type_key varchar(20)
DECLARE @errorCode int

SET @user_pk = (SELECT person_pk FROM users WHERE user_id = 'migration')
SET @membership_dept_cd = 'MD'
SET @dept_type_key = 'Department'

BEGIN TRAN sp_migrateCodeTables

-->	Create code records for the Mailing Lists by Person
	INSERT Mailing_Lists_by_Person 
		(MLBP_mailing_list_nm, dept, mailing_list_legacy_cd, 
		created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk)
	SELECT Description, 
		(SELECT com_cd_pk FROM Common_Codes 
		  WHERE com_cd_cd = @membership_dept_cd AND com_cd_type_key = @dept_type_key),
		Code, GetDate(), @user_pk, GetDate(), @user_pk
	  FROM DM_Legacy_Code_Mapping
	 WHERE Code_Type = 'MailingListPerson'
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

-->	Create code records for the Mailing Lists by Organization
	INSERT Mailing_Lists_by_Orgs 
		(MLBO_mailing_list_nm, officer_mailing_list_fg, 
		mailing_list_legacy_cd, created_dt, created_user_pk, 
		lst_mod_dt, lst_mod_user_pk)
	SELECT Description, NULL, Code, GetDate(), @user_pk, GetDate(), @user_pk
	  FROM DM_Legacy_Code_Mapping
	 WHERE Code_Type = 'MailingListOrg'
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

COMMIT TRAN sp_migrateCodeTables
PRINT 'sp_migrateCodeTables has completed'

-->> Display the error
E_ERROR:
	IF @errorCode <> 0
	BEGIN
		PRINT 'Error occurred in sp_migrateCodeTables() with error code = ' + CONVERT(VARCHAR, @errorCode)
		ROLLBACK TRAN sp_migrateCodeTables 
	END

RETURN @errorCode

GO

-- =============================================
-- example to execute the store procedure
-- =============================================
--EXECUTE sp_migrateCodeTables

