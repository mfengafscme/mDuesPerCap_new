-- =============================================
-- Procedure to handle the Officer file(s) - Office Groups
-- =============================================
-- creating the store procedure
IF EXISTS (SELECT name 
	   FROM   sysobjects 
	   WHERE  name = N'sp_migrateOfficeGroups' 
	   AND 	  type = 'P')
    DROP PROCEDURE sp_migrateOfficeGroups
GO

CREATE PROCEDURE sp_migrateOfficeGroups 
AS
DECLARE @user_pk integer, 
	@membership_dept_cd varchar(8),
	@dept_type_key varchar(20)
DECLARE @errorCode int

SET @user_pk = (SELECT person_pk FROM users WHERE user_id = 'migration')
SET @membership_dept_cd = 'MD'
SET @dept_type_key = 'Department'

BEGIN TRAN sp_migrateOfficeGroups

-- DELETE FROM DM_migrateOfficer
-- DELETE FROM DM_migrateOfficer_ERROR

--> Use Office Number to find the Office in the AFSCME_Offices
	UPDATE DM_migrateOfficer
	   SET TitleCodeId = afscme_office_pk
	  FROM DM_migrateOfficer
	  JOIN AFSCME_Offices ON afscme_title_nm = TitleCodeId
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

/* Commented out, document's approach is for a cursor
-- Check for an Office Group entry in Aff_Officer_Groups for an Office with the 
-- same Month of Election and Current Term End
	-- If no Office Group, create the group with "1" in '# in Office'
	-- If Office Group, increment the '# in Office'
	-- Put data into Aff_Officer_Groups
		-- count of number in group, calc month of election, calc term end
	 INSERT INTO Aff_Officer_Groups
		 (aff_pk, afscme_office_pk, max_number_in_office, office_group_id, 
		 month_of_election, current_term_end, length_of_term,
		 created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt) 
	  SELECT aff_pk, TitleCodeId, COUNT(aff_pk+TitleCodeId) max_number_in_office, 
		 TitleCodeSeq office_group_id, 
		 SubString(Office_Exp_Date, 1, 2) month_of_election,
		 SubString(Office_Exp_Date, 3, 4) current_term_end,
		 (SELECT com_cd_pk FROM Common_Codes 
		   WHERE com_cd_type_key = 'TermLength' 
		     AND com_cd_cd = DATEDIFF(year, CONVERT(DateTime, Last_Filled_Dt), CONVERT(DateTime, SubString(Office_Exp_Date, 1, 2)+'/01/'+SubString(Office_Exp_Date, 3, 4)))
	 	     AND DATEDIFF(year, CONVERT(DateTime, Last_Filled_Dt), CONVERT(DateTime, SubString(Office_Exp_Date, 1, 2)+'/01/'+SubString(Office_Exp_Date, 3, 4))) < 5) TermLength,
	 	 @user_pk, GetDate(), @user_pk, CONVERT(DateTime, Last_Update_Dt)
	    FROM DM_migrateOfficer
	   WHERE aff_pk IS NOT NULL
	     AND SubString(Office_Exp_Date, 1, 2) IS NOT NULL
	     AND (SELECT com_cd_pk FROM Common_Codes 
		   WHERE com_cd_type_key = 'TermLength' 
		     AND com_cd_cd = DATEDIFF(year, CONVERT(DateTime, Last_Filled_Dt), CONVERT(DateTime, SubString(Office_Exp_Date, 1, 2)+'/01/'+SubString(Office_Exp_Date, 3, 4)))
	 	     AND DATEDIFF(year, CONVERT(DateTime, Last_Filled_Dt), CONVERT(DateTime, SubString(Office_Exp_Date, 1, 2)+'/01/'+SubString(Office_Exp_Date, 3, 4))) < 5) IS NOT NULL
	GROUP BY aff_pk, TitleCodeId, TitleCodeSeq, Office_Exp_Date, Last_Filled_Dt, Last_Update_Dt
	ORDER BY aff_pk
*/
-->> Populate the Officer Groups
-->  Create a temporary table to load and calculate the data
	CREATE TABLE #tmpAff_Officer_Groups (
		aff_pk int NULL ,
		afscme_office_pk int NULL ,
		office_group_id int NOT NULL IDENTITY(1,1) ,
		length_of_term int NULL ,
		max_number_in_office smallint NULL ,
		month_of_election int NULL ,
		current_term_end smallint NULL ,
		reporting_officer_fg smallint NULL DEFAULT 0) 
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

--> Set office_group_id -- ideal to count within each affiliate
--  Opted to use Identity field instead

--> Create the distinct officer groups based on aff_pk, Title, TermLength, Month of Election, and Current Term End
-- TermLength is calculated as Last_Filled_Dt minus Office_Exp_Date in number of Years
	 INSERT INTO #tmpAff_Officer_Groups
		 (aff_pk, afscme_office_pk, length_of_term, month_of_election, current_term_end) 
	  SELECT DISTINCT aff_pk, TitleCodeId, 
		 DATEDIFF(year, CONVERT(DateTime, Last_Filled_Dt), CONVERT(DateTime, SubString(Office_Exp_Date, 1, 2)+'/01/'+SubString(Office_Exp_Date, 3, 4))) TermLength,
		 Convert(int, SubString(Office_Exp_Date, 1, 2)) month_of_election,
		 SubString(Office_Exp_Date, 3, 4) current_term_end
		    FROM DM_migrateOfficer
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR
--> Update DM_migrateOfficer with the Officer Group ID for each officer
	UPDATE o
	   SET TitleCodeSeq = office_group_id
	  FROM #tmpAff_Officer_Groups t		--25869
	  RIGHT OUTER JOIN DM_migrateOfficer o ON t.aff_pk = o.aff_pk
		AND t.afscme_office_pk = o.TitleCodeId
		AND t.length_of_term = DATEDIFF(year, CONVERT(DateTime, Last_Filled_Dt), CONVERT(DateTime, SubString(Office_Exp_Date, 1, 2)+'/01/'+SubString(Office_Exp_Date, 3, 4)))
		AND t.month_of_election = Convert(int, SubString(Office_Exp_Date, 1, 2))
		AND t.current_term_end = SubString(Office_Exp_Date, 3, 4)
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR
--> Get the max number in office (count of officers in each office group)
--  Since we are using an Identity, we can count on the officer_group_id
--  Temporary, need to have new file from AFSCME to provide group break outs
	UPDATE t
	   SET max_number_in_office = (SELECT Count(*) 
					 FROM DM_migrateOfficer o
					WHERE TitleCodeSeq = office_group_id)
	  FROM #tmpAff_Officer_Groups t
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR
--> Update the Length of Term for the Common Code value
	UPDATE t
	   SET length_of_term = com_cd_pk
	  FROM #tmpAff_Officer_Groups t
	  JOIN Common_Codes c ON t.length_of_term = c.com_cd_cd
	 WHERE c.com_cd_type_key = 'TermLength'
	   AND c.com_cd_cd < 5
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR
--> Update the Month of Election with the Common Code value
	UPDATE t
	   SET month_of_election = com_cd_pk
	  FROM #tmpAff_Officer_Groups t
	  JOIN Common_Codes c ON t.month_of_election = c.com_cd_cd
	 WHERE c.com_cd_type_key = 'MonthOffcrElection'
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR
--> Only the following positions are allowed to have a Length of Term equal to ‘Indefinite’:
--	Financial Reporting Officer
--	Administrator
--	Steward
--	Executive Director
--	Director
--  ??? Do these positions ALWAYS have the Length of Term set to 'Indefinite'???
--  No rules supplied in Data Migration document

--> Update with the Reporting Officer Flag
	UPDATE t
	   SET reporting_officer_fg = 1
	  FROM DM_Affiliate_RO r
	  JOIN #tmpAff_Officer_Groups t ON t.aff_pk = r.aff_pk
	   AND afscme_office_pk = officer_cd
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

--> Copy valid Officer Groups to Aff_Officer_Groups
	INSERT INTO Aff_Officer_Groups
		(aff_pk, afscme_office_pk, office_group_id, length_of_term, 
		max_number_in_office, month_of_election, current_term_end, reporting_officer_fg, 
		created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
	 SELECT aff_pk, afscme_office_pk, office_group_id, length_of_term,
		max_number_in_office, month_of_election, current_term_end, reporting_officer_fg, 
		@user_pk, GetDate(), @user_pk, GetDate()
	   FROM #tmpAff_Officer_Groups t
	  WHERE t.aff_pk IS NOT NULL
	    AND t.afscme_office_pk IS NOT NULL
	    AND t.office_group_id IS NOT NULL
	    AND t.length_of_term IN (SELECT com_cd_pk FROM Common_Codes WHERE com_cd_type_key = 'TermLength')
	    AND t.max_number_in_office IS NOT NULL
	    AND t.max_number_in_office <> 0
	    AND t.month_of_election IS NOT NULL
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

--> Copy invalid Officer Groups to DM_Aff_Officer_Groups_ERROR
	--  Invalid aff_pk
	INSERT INTO DM_Aff_Officer_Groups_ERROR
		(aff_pk, afscme_office_pk, ERROR, office_group_id, length_of_term, 
		max_number_in_office, month_of_election, current_term_end, reporting_officer_fg, 
		created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
	 SELECT aff_pk, afscme_office_pk, 'Invalid Affiliate - '+ Convert(varchar(100), aff_pk), office_group_id, length_of_term,
		max_number_in_office, month_of_election, current_term_end, reporting_officer_fg,
		@user_pk, GetDate(), @user_pk, GetDate()
	   FROM #tmpAff_Officer_Groups t
	  WHERE t.aff_pk IS NULL
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	--  Invalid afscme_office_pk
	INSERT INTO DM_Aff_Officer_Groups_ERROR
		(aff_pk, afscme_office_pk, ERROR, office_group_id, length_of_term, 
		max_number_in_office, month_of_election, current_term_end, reporting_officer_fg, 
		created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
	 SELECT aff_pk, afscme_office_pk, 'Invalid afscme_office_pk - '+ Convert(varchar(100), afscme_office_pk), office_group_id, length_of_term,
		max_number_in_office, month_of_election, current_term_end, reporting_officer_fg, 
		@user_pk, GetDate(), @user_pk, GetDate()
	   FROM #tmpAff_Officer_Groups t
	  WHERE t.afscme_office_pk IS NULL
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	--  Invalid office_group_id
	INSERT INTO DM_Aff_Officer_Groups_ERROR
		(aff_pk, afscme_office_pk, ERROR, office_group_id, length_of_term, 
		max_number_in_office, month_of_election, current_term_end, reporting_officer_fg, 
		created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
	 SELECT aff_pk, afscme_office_pk, 'Invalid office_group_id - '+ Convert(varchar(100), office_group_id), office_group_id, length_of_term,
		max_number_in_office, month_of_election, current_term_end, reporting_officer_fg, 
		@user_pk, GetDate(), @user_pk, GetDate()
	   FROM #tmpAff_Officer_Groups t
	  WHERE t.office_group_id IS NULL
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	--  Invalid length_of_term
	INSERT INTO DM_Aff_Officer_Groups_ERROR
		(aff_pk, afscme_office_pk, ERROR, office_group_id, length_of_term, 
		max_number_in_office, month_of_election, current_term_end, reporting_officer_fg, 
		created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
	 SELECT aff_pk, afscme_office_pk, 'Invalid length_of_term - '+ Convert(varchar(100), length_of_term), office_group_id, length_of_term,
		max_number_in_office, month_of_election, current_term_end, reporting_officer_fg, 
		@user_pk, GetDate(), @user_pk, GetDate()
	   FROM #tmpAff_Officer_Groups t
	  WHERE t.length_of_term NOT IN (SELECT com_cd_pk FROM Common_Codes WHERE com_cd_type_key = 'TermLength')
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	--  Invalid max_number_in_office
	INSERT INTO DM_Aff_Officer_Groups_ERROR
		(aff_pk, afscme_office_pk, ERROR, office_group_id, length_of_term, 
		max_number_in_office, month_of_election, current_term_end, reporting_officer_fg, 
		created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
	 SELECT aff_pk, afscme_office_pk, 'Invalid max_number_in_office - '+ Convert(varchar(100), max_number_in_office), office_group_id, length_of_term,
		max_number_in_office, month_of_election, current_term_end, reporting_officer_fg, 
		@user_pk, GetDate(), @user_pk, GetDate()
	   FROM #tmpAff_Officer_Groups t
	  WHERE t.max_number_in_office IS NULL
	     OR t.max_number_in_office = 0
	    AND t.month_of_election IS NOT NULL
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	--  Invalid month_of_election
	INSERT INTO DM_Aff_Officer_Groups_ERROR
		(aff_pk, afscme_office_pk, ERROR, office_group_id, length_of_term, 
		max_number_in_office, month_of_election, current_term_end, reporting_officer_fg, 
		created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
	 SELECT aff_pk, afscme_office_pk, 
		'Invalid month_of_election - '+ COALESCE(Convert(varchar(100), month_of_election), '*NULL*'), 
		office_group_id, length_of_term,
		max_number_in_office, month_of_election, current_term_end, reporting_officer_fg, 
		@user_pk, GetDate(), @user_pk, GetDate()
	   FROM #tmpAff_Officer_Groups t
	  WHERE t.month_of_election IS NULL
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

--> Finished with the temporary table
	DROP TABLE #tmpAff_Officer_Groups
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR
-->> END Populate the Officer Groups


COMMIT TRAN sp_migrateOfficeGroups
PRINT 'sp_migrateOfficeGroups has completed'

-->> Display the error
E_ERROR:
	IF @errorCode <> 0
	BEGIN
		PRINT 'Error occurred in sp_migrateOfficeGroups() with error code = ' + CONVERT(VARCHAR, @errorCode)
		ROLLBACK TRAN sp_migrateOfficeGroups 
	END

RETURN @errorCode
	
GO

-- =============================================
-- example to execute the store procedure
-- =============================================
--EXECUTE sp_migrateOfficeGroups 
