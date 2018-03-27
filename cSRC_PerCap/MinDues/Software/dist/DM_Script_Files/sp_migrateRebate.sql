--- =============================================
-- Procedure to handle the Rebate file(s)
-- =============================================
-- creating the store procedure
IF EXISTS (SELECT name 
	   FROM   sysobjects 
	   WHERE  name = N'sp_migrateRebate' 
	   AND 	  type = 'P')
    DROP PROCEDURE sp_migrateRebate
GO

CREATE PROCEDURE sp_migrateRebate 
AS
DECLARE @user_pk integer,
	@StatusActive int,
	@StatusTemporary int,
	@TypeRegular int,
	@TypeRetiree int,
	@TypeAgencyFeePayer int,
	@TypeUnionShop int,	
	@errorCode int

SET @StatusActive = (SELECT com_cd_pk FROM Common_Codes 
		      WHERE com_cd_cd = 'A' AND com_cd_type_key = 'RebateMbrStatus')
SET @StatusTemporary = (SELECT com_cd_pk FROM Common_Codes 
			 WHERE com_cd_cd = 'T' AND com_cd_type_key = 'RebateMbrStatus')
SET @TypeRegular = (SELECT com_cd_pk FROM Common_Codes 
		     WHERE com_cd_cd = 'R' AND com_cd_type_key = 'RebateMbrType')
SET @TypeRetiree = (SELECT com_cd_pk FROM Common_Codes 
		     WHERE com_cd_cd = 'T' AND com_cd_type_key = 'RebateMbrType')
SET @TypeAgencyFeePayer = (SELECT com_cd_pk FROM Common_Codes 
			    WHERE com_cd_cd = 'A' AND com_cd_type_key = 'RebateMbrType')
SET @TypeUnionShop = (SELECT com_cd_pk FROM Common_Codes 
		       WHERE com_cd_cd = 'U' AND com_cd_type_key = 'RebateMbrType')
SET @user_pk = (SELECT person_pk FROM users WHERE user_id = 'migration')

BEGIN TRAN sp_migrateRebate

-->> 
/*
DELETE FROM DM_migrateRebate
DELETE FROM PRB_Roster_Persons
DELETE FROM PRB_Rbt_Year_Info
DELETE FROM PRB_Rebate_Check_Info
DELETE FROM PRB_Request_Affs
DELETE FROM PRB_Requests
DELETE FROM PRB_App_Affs
DELETE FROM PRB_Apps
*/
-->> Copy over all the records to the processing table
	INSERT INTO DM_migrateRebate
		(Primary_Key, person_pk, aff_pk, reb_year, reb_ssn, reb_ssn_dup, 
		reb_state, reb_council, reb_unit_code, reb_lname, reb_fname, 
		reb_initial, reb_addr1, reb_addr2, reb_city, reb_state_abbr, 
		reb_zip, reb_zip4, reb_roster_status, reb_mbr_status, reb_nbr_months, 
		reb_source, reb_label_status, reb_check_status, reb_check_nbr, 
		reb_suffix, reb_check_amt, reb_keyed_date, 
		reb_application_mailed_date, reb_application_returned_date, 
		reb_application_evaluation_cde, reb_comment, reb_comment_analysis_code)
	 SELECT Primary_Key, NULL, NULL, reb_year, reb_ssn, reb_ssn_dup, 
		reb_state, reb_council, reb_unit_code, reb_lname, reb_fname, 
		reb_initial, reb_addr1, reb_addr2, reb_city, reb_state_abbr, 
		reb_zip, reb_zip4, reb_roster_status, reb_mbr_status, reb_nbr_months, 
		reb_source, reb_label_status, reb_check_status, reb_check_nbr, 
		reb_suffix, reb_check_amt, reb_keyed_date, 
		reb_application_mailed_date, reb_application_returned_date, 
		reb_application_evaluation_cde, reb_comment, reb_comment_analysis_code 
	FROM LEG_Rebate r
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

-->> Not in Requirements: Rebates for multiple years.  
	-- reb_year in file is the current year, must subtract 1 to get the actual Rebate Year
	UPDATE DM_migrateRebate
	   SET reb_year = CAST(CAST(reb_year AS smallint) - 1 AS varchar(4))
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

-->> Update with the affiliate identifier pk, if found
	UPDATE r
	   SET r.aff_pk = o.aff_pk
	  FROM DM_migrateRebate r
	  JOIN Aff_Organizations o ON r.reb_unit_code = o.old_aff_unit_cd_legacy
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

-->> Update with the person pk, if found
	-- Match on SSN, First Name and Last Name and no duplicate SSN
	UPDATE r
	   SET r.person_pk = p.person_pk
	 FROM DM_migrateRebate r
	 JOIN Person p ON p.ssn = r.reb_ssn 
	  AND p.first_nm = r.reb_fname
	  AND p.last_nm = r.reb_lname
	  AND r.reb_ssn_dup = 0
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

/* comment out, this does not produce good results
	-- Match on Unit Code, First Name and Last Name
	UPDATE r
	   SET r.person_pk = m.person_pk
	-- SELECT *
	  FROM DM_migrateRebate r
	  JOIN Aff_Members m ON r.aff_pk = m.aff_pk
		AND m.person_pk IN (SELECT person_pk FROM Person p WHERE p.first_nm = r.reb_fname AND p.last_nm = r.reb_lname )
	 WHERE r.person_pk IS NULL
-- 355

	-- Match on Address State, First Name and Last Name
	UPDATE r
	   SET r.person_pk = a.person_pk
	-- SELECT *
	  FROM DM_migrateRebate r
	  JOIN Person_Address a ON r.reb_state_abbr = a.state
		AND a.person_pk IN (SELECT person_pk FROM Person p WHERE p.first_nm = r.reb_fname AND p.last_nm = r.reb_lname )
	 WHERE r.person_pk IS NULL
-- 1930
*/

-->> Work with only those Persons that have been found in the System
	-- Put those without a Person PK into the Error table
	INSERT INTO DM_migrateRebate_ERROR
		(Primary_Key, ERROR, person_pk, aff_pk, reb_year, reb_ssn, 
		reb_ssn_dup, reb_state, reb_council, reb_unit_code, reb_lname, 
		reb_fname, reb_initial, reb_addr1, reb_addr2, reb_city, 
		reb_state_abbr, reb_zip, reb_zip4, reb_roster_status, 
		reb_mbr_status, reb_nbr_months, reb_source, reb_label_status, 
		reb_check_status, reb_check_nbr, reb_suffix, reb_check_amt, 
		reb_keyed_date, reb_application_mailed_date, 
		reb_application_returned_date, reb_application_evaluation_cde, 
		reb_comment, reb_comment_analysis_code)
	 SELECT Primary_Key, 'Unable to find Person', person_pk, aff_pk, reb_year, reb_ssn, 
		reb_ssn_dup, reb_state, reb_council, reb_unit_code, reb_lname, 
		reb_fname, reb_initial, reb_addr1, reb_addr2, reb_city, 
		reb_state_abbr, reb_zip, reb_zip4, reb_roster_status, 
		reb_mbr_status, reb_nbr_months, reb_source, reb_label_status, 
		reb_check_status, reb_check_nbr, reb_suffix, reb_check_amt, 
		reb_keyed_date, reb_application_mailed_date, 
		reb_application_returned_date, reb_application_evaluation_cde, 
		reb_comment, reb_comment_analysis_code
	   FROM DM_migrateRebate 
	  WHERE person_pk IS NULL
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Put those that will go in the Roster table without an Affiliate PK into the Error table
	INSERT INTO DM_migrateRebate_ERROR
		(Primary_Key, ERROR, person_pk, aff_pk, reb_year, reb_ssn, 
		reb_ssn_dup, reb_state, reb_council, reb_unit_code, reb_lname, 
		reb_fname, reb_initial, reb_addr1, reb_addr2, reb_city, 
		reb_state_abbr, reb_zip, reb_zip4, reb_roster_status, 
		reb_mbr_status, reb_nbr_months, reb_source, reb_label_status, 
		reb_check_status, reb_check_nbr, reb_suffix, reb_check_amt, 
		reb_keyed_date, reb_application_mailed_date, 
		reb_application_returned_date, reb_application_evaluation_cde, 
		reb_comment, reb_comment_analysis_code)
	 SELECT Primary_Key, 'Cannot be on the Roster with an invalid Affiliate', person_pk, aff_pk, reb_year, reb_ssn, 
		reb_ssn_dup, reb_state, reb_council, reb_unit_code, reb_lname, 
		reb_fname, reb_initial, reb_addr1, reb_addr2, reb_city, 
		reb_state_abbr, reb_zip, reb_zip4, reb_roster_status, 
		reb_mbr_status, reb_nbr_months, reb_source, reb_label_status, 
		reb_check_status, reb_check_nbr, reb_suffix, reb_check_amt, 
		reb_keyed_date, reb_application_mailed_date, 
		reb_application_returned_date, reb_application_evaluation_cde, 
		reb_comment, reb_comment_analysis_code
	   FROM DM_migrateRebate 
	  WHERE aff_pk IS NULL
	    AND reb_roster_status IS NOT NULL
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Remove those without a Person PK
	DELETE r FROM DM_migrateRebate r 
	 WHERE EXISTS (SELECT * FROM DM_migrateRebate_ERROR e WHERE e.Primary_Key = r.Primary_Key)
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

-->> Set the Application Mailed Date to NULL if no value is provided
--   Set the Application Returned Date to NULL if no value is provided
--   Set the Duration to NULL if no value is provided
--   Set the Roster Status to NULL if no value is provided
	 UPDATE DM_migrateRebate
	    SET reb_application_mailed_date = NULLIF(reb_application_mailed_date, '00000000'),
		reb_application_returned_date = NULLIF(reb_application_returned_date, '00000000'), 
		reb_nbr_months = NULLIF(reb_nbr_months, '00'),
		reb_roster_status = NULLIF(Len(reb_roster_status), 0)

-->> For those with a Person PK, create a Rebate Year Information record
	INSERT INTO PRB_Rbt_Year_Info
		(person_pk, rbt_year)
	  SELECT person_pk, reb_year
	    FROM DM_migrateRebate
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

-->> Process each record individually
	DECLARE @person_pk int, 
		@rbt_year int,
		@rqst_pk int,
		@app_pk int
	-- Set up a cursor for the Rebate records 
	DECLARE cur_migrateRebate CURSOR
	 FOR SELECT person_pk, reb_year
	       FROM DM_migrateRebate
	OPEN cur_migrateRebate
	-- get the first record
	FETCH NEXT FROM cur_migrateRebate INTO @person_pk, @rbt_year
	WHILE @@FETCH_STATUS = 0
	BEGIN
		-- Create the Request record	
		INSERT INTO PRB_Requests
			(rqst_dt, person_pk, rqst_rebate_year, rqst_denied_fg,  
			rqst_keyed_dt, rqst_resubmit_fg, 
			created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
		 SELECT '04/16/'+  reb_year, person_pk, reb_year, 0,
			reb_keyed_date, 0, 
			created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt
		   FROM DM_migrateRebate
		  WHERE person_pk = @person_pk AND reb_year = @rbt_year
		-- Get error code
		SELECT @errorCode = @@ERROR
		IF @errorCode <> 0 GOTO E_ERROR

		-- Get the rqst_pk Identity value
		SET @rqst_pk = @@IDENTITY
		-- If NO Application Mailed Date, create the Request Affiliates and go to next record
		IF (SELECT Count(*) FROM DM_migrateRebate WHERE person_pk = @person_pk AND reb_year = @rbt_year
		       AND reb_application_mailed_date IS NULL) > 0
		BEGIN
			INSERT INTO PRB_Request_Affs
				(rqst_pk, aff_pk, rqst_duration_in_aff, rqst_filed_with)
			 SELECT @rqst_pk, aff_pk, 
				(SELECT com_cd_pk FROM Common_Codes 
				  WHERE com_cd_type_key = 'RebateDuration' 
				    AND com_cd_cd = reb_nbr_months),
				CASE reb_source WHEN '4' THEN NULL
				ELSE (SELECT com_cd_pk FROM Common_Codes 
				       WHERE com_cd_type_key = 'RebateFiledWith' 
				         AND com_cd_cd = reb_source) END
			   FROM DM_migrateRebate
			  WHERE person_pk = @person_pk AND reb_year = @rbt_year
			    AND reb_application_mailed_date IS NULL
		-- Get error code
		SELECT @errorCode = @@ERROR
		IF @errorCode <> 0 GOTO E_ERROR
		END
		-- If Application Mailed Date exists, create Application record
		-- Not in Requirements: Include the Comments
		ELSE
		BEGIN		
			INSERT INTO PRB_Apps
				(app_mailed_dt, app_returned_dt, 
				prb_evaluation_cd, prb_comment_anal_cd, 
				aff_roster_generated_fg, comment_txt,
				--app_status, , 
				created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
			 SELECT reb_application_mailed_date, reb_application_returned_date,
				(SELECT com_cd_pk FROM Common_Codes 
				  WHERE com_cd_type_key = 'RebateAppEvalCode' 
				    AND com_cd_cd = reb_application_evaluation_cde),
				(SELECT com_cd_pk FROM Common_Codes 
				  WHERE com_cd_type_key = 'RbtCommentAnalCode' 
				    AND com_cd_cd = reb_comment_analysis_code),
				1, reb_comment, 
				created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt
			   FROM DM_migrateRebate
			  WHERE person_pk = @person_pk AND reb_year = @rbt_year
			    AND reb_application_mailed_date IS NOT NULL
			-- Get error code
			SELECT @errorCode = @@ERROR
			IF @errorCode <> 0 GOTO E_ERROR

			-- Get the prb_app_pk Identity value
			SET @app_pk = @@IDENTITY
			-- Update the Request record with the prb_app_pk Identity value
			UPDATE PRB_Requests
			   SET prb_app_pk = @app_pk
			 WHERE person_pk = @person_pk AND rqst_pk = @rqst_pk
			-- Get error code
			SELECT @errorCode = @@ERROR
			IF @errorCode <> 0 GOTO E_ERROR

			-- If NO Roster Status, create the Application Affiliate(s) with the prb_app_pk Identity value and go to next record
			IF (SELECT Count(*) FROM DM_migrateRebate 
			     WHERE person_pk = @person_pk AND reb_year = @rbt_year
			       AND reb_roster_status IS NULL) > 0
			BEGIN
				INSERT INTO PRB_App_Affs
					(prb_app_pk, aff_pk, app_duration_in_aff, app_filed_with)
				 SELECT @app_pk, aff_pk, 
					(SELECT com_cd_pk FROM Common_Codes 
					  WHERE com_cd_type_key = 'RebateDuration' 
					    AND com_cd_cd = reb_nbr_months),
				CASE reb_nbr_months WHEN '4' THEN NULL
				ELSE (SELECT com_cd_pk FROM Common_Codes 
				       WHERE com_cd_type_key = 'RebateFiledWith' 
				         AND com_cd_cd = reb_source) END
				   FROM DM_migrateRebate
				  WHERE person_pk = @person_pk AND reb_year = @rbt_year
				    AND reb_roster_status IS NULL
				-- Get error code
				SELECT @errorCode = @@ERROR
				IF @errorCode <> 0 GOTO E_ERROR
			END
			-- If Roster Status exists, create the Roster record
			ELSE
			BEGIN
				INSERT INTO PRB_Roster_Persons
					(aff_pk, person_pk, rbt_year, --rbt_check_nbr, 
					--roster_aff_status, 
					roster_duration_in_aff, roster_filed_with, 
					--roster_acceptance_cd, 
					rebate_year_mbr_type, rebate_year_mbr_status, 
					--rebate_year_mbr_dues_rate, rebate_year_amt, 
					created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
					--file_generated_dt)
				 SELECT aff_pk, person_pk, reb_year,
					(SELECT com_cd_pk FROM Common_Codes 
					  WHERE com_cd_type_key = 'RebateDuration' 
					    AND com_cd_cd = reb_nbr_months),
					CASE reb_nbr_months 
						WHEN '4' THEN NULL
						ELSE (SELECT com_cd_pk FROM Common_Codes 
						       WHERE com_cd_type_key = 'RebateFiledWith' 
						         AND com_cd_cd = reb_source) END,
					CASE
						WHEN SubString(reb_unit_code, 1, 1) IN ('L', 'S', 'R', 'U') AND reb_mbr_status IN ('A','O') 
						THEN @StatusActive
						WHEN SubString(reb_unit_code, 1, 1) IN ('L', 'S', 'R', 'U') AND reb_mbr_status = 'T' 
						THEN @StatusTemporary
						WHEN reb_mbr_status = 'N' THEN @StatusActive
						WHEN reb_mbr_status = 'C' THEN @StatusActive
						ELSE 0 END,
					CASE
						WHEN SubString(reb_unit_code, 1, 1) IN ('L', 'U') AND reb_mbr_status IN ('A','O') 
						THEN @TypeRegular
						WHEN SubString(reb_unit_code, 1, 1) IN ('R', 'S') AND reb_mbr_status IN ('A','O') 
						THEN @TypeRetiree
						WHEN SubString(reb_unit_code, 1, 1) IN ('L', 'U') AND reb_mbr_status = 'T' 
						THEN @TypeRegular
						WHEN SubString(reb_unit_code, 1, 1) IN ('R', 'S') AND reb_mbr_status = 'T' 
						THEN @TypeRetiree
						WHEN reb_mbr_status = 'N' THEN @TypeAgencyFeePayer
						WHEN reb_mbr_status = 'C' THEN @TypeUnionShop
						ELSE 0 END,
					created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt 
				   FROM DM_migrateRebate
					  WHERE person_pk = @person_pk AND reb_year = @rbt_year
					    AND reb_roster_status IS NOT NULL
				-- Get error code
				SELECT @errorCode = @@ERROR
				IF @errorCode <> 0 GOTO E_ERROR

				-- If Check Number exists, create the Check Info record
				INSERT INTO PRB_Rebate_Check_Info
					(person_pk, rbt_year, rbt_check_nbr_1, rbt_check_amt_1, 
					created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
				 SELECT person_pk, reb_year, reb_check_nbr, 
					CONVERT(money, reb_check_amt), 
					created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt 
				   FROM DM_migrateRebate
					  WHERE person_pk = @person_pk AND reb_year = @rbt_year
					    AND reb_check_nbr <> 0
				-- Get error code
				SELECT @errorCode = @@ERROR
				IF @errorCode <> 0 GOTO E_ERROR
			END
		END
		-- Get the next record
		FETCH NEXT FROM cur_migrateRebate INTO @person_pk, @rbt_year
	END
	CLOSE cur_migrateRebate
	DEALLOCATE cur_migrateRebate	

-- Insert Default information for the 12-Month Rebate Amounts for each Rebate Year in the system
INSERT INTO PRB_12_Month_Rebate
	(rbt_year, rbt_pct, rbt_full_time_amt, rbt_part_time_amt, 
	rbt_lower_part_time_amt, rbt_retiree_amt, 
	created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
SELECT Distinct(rbt_year), 0, 0, 0, 0, 0, @user_pk, GetDate(), @user_pk, GetDate()
FROM PRB_Rbt_Year_Info


COMMIT TRAN changeAffiliateIdentifier
PRINT 'sp_migrateRebate has completed'

-->> Display the error
E_ERROR:
	IF @errorCode <> 0
	BEGIN
		PRINT 'Error occurred in sp_migrateRebate() with error code = ' + CONVERT(VARCHAR, @errorCode)
		ROLLBACK TRAN sp_migrateRebate 
	END

RETURN @errorCode

--***************************************************************
GO

-- =============================================
-- example to execute the store procedure
-- =============================================
--EXECUTE sp_migrateRebate	
