--***************************************************************************************
--* SUMMARY: Stored Procedures that will handle the background processing of mass change 
--* requests at a scheduled time.
--***************************************************************************************

--***************************************************************************************
--*
--* 1. Remove the Mass Change task from the current database.
--* 2. Add the Mass Change task onto the current database.
--* 3. Schedule to run Mass Change task daily at 9:00 PM.
--*
--***************************************************************************************
DECLARE @taskName varchar(100)
DECLARE @dbName varchar(100)

SELECT @dbName = db_name()
SELECT @taskName = 'MassChange_' + @dbName

IF EXISTS (SELECT 1 FROM msdb..sysjobs WHERE name = @taskName) 
BEGIN 
	PRINT 'Drop task ' + @taskName
	EXECUTE msdb..sp_droptask @taskName
END

EXECUTE msdb..sp_addtask 
	@name = @taskName,
	@subsystem = 'TSQL',
	@server = @@SERVERNAME,
	@username = 'sa',
	@databasename = @dbName,
	@enabled = 1,
	@freqtype = 2,
	@activestartdate = 20030810,
	@activeenddate = 99991231,
	@activestarttimeofday = 0,
	@activeendtimeofday = 235959,
	@command = 'EXECUTE initiateMassChange',
	@description = 'Nightly Run Task to Initiate Affiliate Mass Change.'	

EXECUTE msdb..sp_add_jobschedule 
	@job_name = @taskName,
	@name = 'ScheduledMassChange',
	@freq_type = 4, 
	@freq_interval = 1,
	@active_start_time = 210000
GO

--***************************************************************************************
--* Send Mail via SMTP with JMail object.
--***************************************************************************************
IF OBJECT_ID('sendmail') IS NOT NULL DROP PROC sendmail
GO
CREATE PROCEDURE sendmail 
	@fromName varchar(100),		-- From Name 
	@fromEmail varchar(50), 	-- From Email 
	@toEmail varchar(50)='', 	-- To Email 
	@subject varchar(100),		-- Subject 
	@body varchar(5000)='',		-- Message Body 
	@attachment varchar(100)='',	-- Attachment 
	@recipientBCC varchar(200)='',-- BCC 
	@recipientCC varchar(200)=''	-- CC 
AS

DECLARE @JMail int
DECLARE @JMailRetVal0 int
DECLARE @JMailRetVal1 int
DECLARE @JMailRetVal2 int
DECLARE @server varchar(50)

-- Get the name of SMTP server
SELECT @server = (SELECT variable_value FROM COM_App_Config_Data 
			WHERE  variable_name='SMTPServerName')

-- Create the JMail object 
EXEC @JMailRetVal0=sp_OACreate 'JMail.SMTPMail', @Jmail OUT
IF @JMailRetVal0 <> 0
BEGIN
	PRINT 'Unable to create Jmail object'
	RETURN -1
END 

EXECUTE sp_OASetProperty @JMail, 'ServerAddress', @server
EXECUTE sp_OASetProperty @JMail, 'SenderName', @fromName
EXECUTE sp_OASetProperty @JMail, 'Sender', @fromEmail
EXECUTE sp_OASetProperty @JMail, 'Subject', @subject

IF NOT(@attachment='')
	EXECUTE sp_OAMethod @JMail, 'Addattachment', NULL , @attachment
IF NOT(@recipientBCC='')
	EXECUTE sp_OAMethod @JMail, 'AddRecipientBCC', NULL , @recipientBCC
IF NOT(@recipientCC='')
	EXECUTE sp_OAMethod @JMail, 'AddRecipientCC', NULL , @recipientCC

IF NOT(@toEmail='')
BEGIN
	EXECUTE @JMailRetVal1 = sp_OAMethod @Jmail, 'AddRecipient', Null ,@toEmail
	IF @JmailRetVal1 <> 0
	BEGIN
		PRINT 'Could not add recipient'
		RETURN -1
	END
END

EXECUTE sp_OASetProperty @JMail, 'Body', @body
EXECUTE @JmailRetVal2 = sp_OAMethod @JMail, 'Execute' 

IF @JmailRetVal2 <> 0
BEGIN
	PRINT 'Could not execute Jmail'
	RETURN -1
END

EXECUTE sp_OADestroy @Jmail
RETURN 0
GO

--***************************************************************************************
--* Used to log Affiliate Changes to the Aff_Chng_History and Aff_Chng_History_Dtl 
--* tables.
--* 
--* @param affPk The Affiliate Primary Key
--* @param sectionCodePk The Code Primary Key of the section was changed
--* @param userPk The Primary Key of the User that requested the change
--* @param fieldCodePk The Code Primary Key of the field that was changed
--* @param oldValue The field value prior to the change
--* @param newValue The field value after to the change
--* 
--* @return 0 if logging was successful, or Error Code if logging failed.
--* 
--***************************************************************************************
IF OBJECT_ID('addChangeToHistory') IS NOT NULL DROP PROC addChangeToHistory
GO 
CREATE PROCEDURE addChangeToHistory 
	@affPk int, 
	@sectionCodePk int, 
	@userPk int,
	@fieldCodePk int,
	@oldValue varchar(254),
	@newValue varchar(254) 
AS
BEGIN
DECLARE @errorCode int

	BEGIN TRAN addChangeToHistory 

	-- Add Aff_Chng_History record
	PRINT ' '
	PRINT 'Add Aff_Chng_History record'
	INSERT INTO Aff_Chng_History
		 (aff_pk, aff_section, chng_user_pk, chng_dt)
	VALUES (@affPk, @sectionCodePk, @userPk, getDate())

	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Add Aff_Chng_History_Dtl record
	IF (SELECT @@IDENTITY) IS NOT NULL
	BEGIN
		PRINT ' '
		PRINT 'Add Aff_Chng_History_Dtl record'
		INSERT INTO Aff_Chng_History_Dtl
			(aff_transaction_pk, aff_field_chnged, new_value, old_value)
		VALUES (@@IDENTITY, @fieldCodePk, @newValue, @oldValue)
			
		-- Get error code
		SELECT @errorCode = @@ERROR
		IF @errorCode <> 0 GOTO E_ERROR
	END
	
	COMMIT TRAN addChangeToHistory 

E_ERROR:
	IF @errorCode <> 0
	BEGIN
		PRINT 'Error occurred in addChangeToHistory() with error code = ' + CONVERT(VARCHAR, @errorCode)
		ROLLBACK TRAN addChangeToHistory 
	END

RETURN @errorCode
END
GO

--***************************************************************************************
--* Update Aff_Mbr_Activity table for the added or deactivated members
--* 
--* @param affPk Affiliate from which to added or deactivated all Members
--* @param activityType Type of Activity (Add-30001, Update-30002, Deactivate-30003)
--***************************************************************************************
IF OBJECT_ID('recordAffMbrActivity') IS NOT NULL DROP PROC recordAffMbrActivity
GO 
CREATE PROCEDURE recordAffMbrActivity
	@affPk int,
	@activityType int
AS
BEGIN
SET NOCOUNT ON
DECLARE @timePk int,
	  @activityCount int

	PRINT ' '
	PRINT 'Update Aff_Mbr_Activity table for the added or deactivated members with Affiliate pk ' + CONVERT(VARCHAR, @affPk)

	-- Get the time primary key based on current year and current month
	SELECT @timePk = (SELECT time_pk FROM Time_Dim
				WHERE calendar_year=YEAR(getDate()) and calendar_month=MONTH(getDate()))
	
	-- Get the membership activity count
	SELECT @activityCount = (SELECT membership_activity_count FROM Aff_Mbr_Activity WHERE
		    			 aff_pk = @affPk AND time_pk = @timePk AND
		    			 membership_activity_type = @activityType) 

	-- Record added or deactivated members to Aff_Mbr_Activity table
	IF (@activityCount IS NULL)
		INSERT INTO Aff_Mbr_Activity (aff_pk, time_pk, membership_activity_type, membership_activity_count)
		VALUES (@affPk, @timePk, @activityType, 1)	
	ELSE
		UPDATE Aff_Mbr_Activity
		SET   membership_activity_count = @activityCount+1
		WHERE aff_pk = @affPk AND
			time_pk = @timePk AND
			membership_activity_type = @activityType

SET NOCOUNT OFF
RETURN @@ERROR
END
GO

--***************************************************************************************
--* Inactivates all the Members in the associated Affiliate.
--* 
--* Update Aff_Mbr_Activity table for the deactivated members
--* 
--* @param affPk Affiliate from which to inactivate all Members
--* @param requestingUserPk Requesting User's Primary Key
--***************************************************************************************
IF OBJECT_ID('inactivateMembers') IS NOT NULL DROP PROC inactivateMembers
GO 
CREATE PROCEDURE inactivateMembers
	@affPk int,
	@requestingUserPk int
AS
BEGIN	
DECLARE @errorCode int

	PRINT ' '
	PRINT 'Inactives all Members associated with Affiliate Pk ' + CONVERT(VARCHAR, @affPk)

	-- Inactivate all Members in the associated Affiliate
	UPDATE Aff_Members 
	SET    mbr_status = 31002,
		 lst_mod_dt = getDate(),
		 lst_mod_user_pk = @requestingUserPk
	WHERE  aff_pk = @affPk

	-- Set officer history end term
	UPDATE Officer_History
	SET    pos_end_dt = getDate(),
		 lst_mod_dt = getDate(),
		 lst_mod_user_pk = @requestingUserPk
	WHERE  aff_pk = @affPk		 

	-- Record deactivated members to Aff_Mbr_Activity table
	EXECUTE @errorCode = recordAffMbrActivity
			@affPk = @affPk,
			@activityType = 30003

E_ERROR:
	IF @errorCode <> 0
	BEGIN
 		PRINT 'Error occurred in inactiveMembers() with error code = ' + CONVERT(VARCHAR, @errorCode)
	END

RETURN @errorCode
END
GO 
              
--***************************************************************************************
--* Moves Members from one Affiliate to another. The system must inactivate the 
--* Members from one affiliate, and add those Members to another Affiliate.
--* 
--* Update Aff_Mbr_Activity table for the added members
--* 
--* @param currentAffPk Primary Key of the Affiliate from which Members are moving.
--* @param newAffPk Primary Key of the Affiliate to which Members are being moved.
--* @param requestingUserPk Requesting User's Primary Key
--***************************************************************************************
IF OBJECT_ID('moveMembers') IS NOT NULL DROP PROC moveMembers
GO 
CREATE PROCEDURE moveMembers
	@currentAffPk int,
	@newAffPk int,
	@requestingUserPk int
AS
BEGIN
DECLARE @errorCode int
SELECT @errorCode = 0

	PRINT ' '
	PRINT 'Move all members from Affiliate ' + CONVERT(VARCHAR, @currentAffPk) + ' to Affiliate ' + CONVERT(VARCHAR, @newAffPk)

	-- Inactivate the Members of the current Affiliate
	EXECUTE @errorCode = inactivateMembers
			@affPk = @currentAffPk,
			@requestingUserPk = @requestingUserPk

	IF @errorCode = 0 
	BEGIN
		-- Add the Members to the new Affiliate
		INSERT Aff_Members
			(mbr_no_old_afscme, aff_pk, mbr_join_dt, mbr_retired_dt,
     			 no_cards_fg, no_mail_fg, no_legislative_mail_fg,
     			 no_public_emp_fg, lst_mod_dt, mbr_card_sent_dt, lst_mod_user_pk,
	      	 created_dt, created_user_pk, mbr_status, mbr_dues_rate,
		       mbr_no_local, mbr_ret_dues_renewal_fg, mbr_type, mbr_dues_type,
			 mbr_dues_frequency, lost_time_language_fg, primary_information_source,
		       person_pk)
		SELECT mbr_no_old_afscme, @newAffPk, getDate(), mbr_retired_dt, 
			 no_cards_fg, no_mail_fg, no_legislative_mail_fg, no_public_emp_fg, 
			 getDate(), mbr_card_sent_dt, lst_mod_user_pk, getDate(),
			 created_user_pk, 31001, mbr_dues_rate, mbr_no_local, mbr_ret_dues_renewal_fg, 
			 mbr_type, mbr_dues_type, mbr_dues_frequency, lost_time_language_fg, 
			 primary_information_source, person_pk
		FROM  Aff_Members
		WHERE aff_pk = @currentAffPk	

		-- Record added members to Aff_Mbr_Activity table
		EXECUTE @errorCode = recordAffMbrActivity
				@affPk = @newAffPk,
				@activityType = 30001
	END

RETURN @errorCode
END
GO 

--***************************************************************************************
--* Updates the appropriate affiliate identifier field for the Affiliate and all 
--* it's Sub Affiliates with the new value based on the parent Affiliate's Type.
--* 
--* For Councils, this will update the aff_councilRetiree_chap for itself and all 
--* of the Locals and Sub Locals.
--* For Locals, this will update the aff_localSubChapter for itself and all of the 
--* Sub Locals.
--* For Retiree Chapters, this will update the aff_councilRetiree_chap for itself 
--* and all of the Sub Chapters.
--* For Sub Chapters, this will update the aff_localSubChapter for itself only 
--* (does not effect any other Affiliates.)
--* For Sub Locals, this will set the aff_subUnit for itself only (does not effect 
--* any other Affiliates.)
--* 
--* @param Primary Key of the Affiliate at the top of the hierarchy.@param affPk
--* @param affType
--* @param localNumber
--* @param requestingUserPk Requesting User's Primary Key
--* @param affCode The incoming code.
--***************************************************************************************
IF OBJECT_ID('changeAffiliateIdentifier') IS NOT NULL DROP PROC changeAffiliateIdentifier
GO 
CREATE PROCEDURE changeAffiliateIdentifier
	@affPk int, 
	@affType char,
	@localNumber varchar(4),
	@affState varchar(2),
	@requestingUserPk int,
	@newCode char
AS
BEGIN
SET NOCOUNT ON
DECLARE @errorCode int
DECLARE @subAffPk int
DECLARE @parentPk int
DECLARE subAffCursor CURSOR READ_ONLY FOR
SELECT aff_pk FROM Aff_Organizations WHERE parent_aff_fk = @affPk
SET NOCOUNT OFF

	BEGIN TRAN changeAffiliateIdentifier


	IF (@affType = '#')
	BEGIN
		-- This is disassociate, just wipe out the council
		-- and clear the parent pk
		-- then wipe out the council for sub affiliates
		IF (@localNumber IS NULL)
		BEGIN
			UPDATE Aff_Organizations 
			SET    	 lst_mod_dt = getDate(),
				 lst_mod_user_pk = @requestingUserPk,
				 aff_councilRetiree_chap = '',
				 parent_aff_fk = NULL
			WHERE  aff_pk = @affPk
			
			DECLARE subAffCursor2 CURSOR READ_ONLY FOR
			SELECT aff_pk FROM Aff_Organizations WHERE parent_aff_fk = @affPk
			
			-- Open cursor to fetch a sub affiliate
			OPEN subAffCursor2
			FETCH NEXT FROM subAffCursor2 INTO @subAffPk
			
			WHILE (@@FETCH_STATUS=0) 			
			BEGIN
				UPDATE Aff_Organizations 
				SET    	 lst_mod_dt = getDate(),
					 lst_mod_user_pk = @requestingUserPk,
					 aff_councilRetiree_chap = ''
				WHERE  aff_pk = @subAffPk
				
				FETCH NEXT FROM subAffCursor2 INTO @subAffPk
			END
			
			CLOSE subAffCursor2
			DEALLOCATE subAffCursor2;
						
		END
		ELSE
		BEGIN
			-- This is associate, find council's pk and store it
			-- as the parent pk, and store council number in locals record
			DECLARE councilCursor CURSOR READ_ONLY FOR
			SELECT aff_pk FROM Aff_Organizations WHERE aff_councilRetiree_chap = @localNumber		
			
			-- Open cursor to fetch each a council
			OPEN councilCursor;
			FETCH NEXT FROM councilCursor INTO @parentPk
			
			-- update locals
			UPDATE Aff_Organizations 
			SET    aff_councilRetiree_chap = @localNumber,
				 parent_aff_fk = @parentPk,
				 lst_mod_dt = getDate(),
				 lst_mod_user_pk = @requestingUserPk,
				 aff_code = @newCode
			WHERE  aff_pk = @affPk
			
			-- update any possible sub locals
			UPDATE Aff_Organizations 
			SET    aff_councilRetiree_chap = @localNumber,			 
				 lst_mod_dt = getDate(),
				 lst_mod_user_pk = @requestingUserPk				 
			WHERE  parent_aff_fk = @affPk
			
			-- Close cursor
			CLOSE councilCursor;
			DEALLOCATE councilCursor;
		END
	END


	----------------------------------------------------
	-- Update the Affiliate itself
	----------------------------------------------------
	IF (@affType = 'C' OR @affType = 'R')
		-- Update aff_councilRetiree_chap 
		UPDATE Aff_Organizations 
		SET    aff_councilRetiree_chap = @localNumber,
			 aff_stateNat_type = @affState,
			 lst_mod_dt = getDate(),
			 lst_mod_user_pk = @requestingUserPk,
			 aff_code = @newCode
		WHERE  aff_pk = @affPk
	ELSE IF (@affType = 'L' OR @affType = 'S')
		-- Update aff_localSubChapter 
		UPDATE Aff_Organizations 
		SET    aff_localSubChapter = @localNumber,
			 aff_stateNat_type = @affState,
			 lst_mod_dt = getDate(),
			 lst_mod_user_pk = @requestingUserPk,
			 aff_code = @newCode
		WHERE  aff_pk = @affPk
	ELSE IF (@affType = 'U')
		-- For SubLocals, update aff_subUnit for itself only
		UPDATE Aff_Organizations 
		SET    aff_subUnit = @localNumber,
			 aff_stateNat_type = @affState,
			 lst_mod_dt = getDate(),
			 lst_mod_user_pk = @requestingUserPk,
			 aff_code = @newCode
		WHERE  aff_pk = @affPk

	-- Get error code	
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR


	----------------------------------------------------
	-- Update its sub Affiliates
	----------------------------------------------------
	IF EXISTS (SELECT 1 FROM Aff_Organizations WHERE parent_aff_fk = @affPk) 
	BEGIN
		-- For Locals, update aff_localSubChapter for its Sub Locals
		IF (@affType = 'L')
		BEGIN
			UPDATE Aff_Organizations 
			SET    aff_localSubChapter = @localNumber,
				 aff_stateNat_type = @affState,
				 lst_mod_dt = getDate(),
				 lst_mod_user_pk = @requestingUserPk
			WHERE  parent_aff_fk = @affPk

			-- Get error code	
			SELECT @errorCode = @@ERROR
			IF @errorCode <> 0 GOTO E_ERROR
		END


		-- For Councils, update aff_councilRetiree_chap for itself and its Locals/Sub Locals
		-- For Retirees, update aff_councilRetiree_chap for itself and its Sub Chapters
		IF (@affType = 'C' OR @affType = 'R')
		BEGIN
			UPDATE Aff_Organizations 
			SET    aff_councilRetiree_chap = @localNumber,
				 aff_stateNat_type = @affState,
				 lst_mod_dt = getDate(),
				 lst_mod_user_pk = @requestingUserPk
			WHERE  parent_aff_fk = @affPk

			-- Get error code	
			SELECT @errorCode = @@ERROR
			IF @errorCode <> 0 GOTO E_ERROR

			-- Open cursor to fetch each sub Affiliate
			OPEN subAffCursor;
			FETCH NEXT FROM subAffCursor INTO @subAffPk

			-- Process each sub Affiliate
			WHILE (@@FETCH_STATUS=0) 
			BEGIN
				-- Update aff_councilRetiree_chap to all sub Affiliates 
				UPDATE Aff_Organizations 
				SET    aff_councilRetiree_chap = @localNumber,
					 aff_stateNat_type = @affState,
					 lst_mod_dt = getDate(),
					 lst_mod_user_pk = @requestingUserPk
				WHERE  parent_aff_fk = @subAffPk

				-- Fetch next sub Affiliate record
				FETCH NEXT FROM subAffCursor INTO @subAffPk
			END

			-- Close cursor
			CLOSE subAffCursor
		END
	END

	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	COMMIT TRAN changeAffiliateIdentifier

E_ERROR:
	IF @errorCode <> 0 
	BEGIN
		PRINT 'Error occurred in changeAffiliateIdentifier() with error code = ' + CONVERT(VARCHAR, @errorCode)
		ROLLBACK TRAN changeAffiliateIdentifier
	END

DEALLOCATE subAffCursor
RETURN @errorCode
END
GO
    
--***************************************************************************************
--* For Council's it deactivates the Council and disassociates all the Sub Affiliates.
--* For all other Affiliate Types, it deactivates all Affiliates and Sub Affiliates and 
--* inactivates the Members.  For all Affiliates that are deactivated, remove them from
--* any Mailing Lists which they are associated
--* 
--* @param affPk the Affiliate's Primary Key
--* @param requestingUserPk Requesting User's Primary Key
--***************************************************************************************
IF OBJECT_ID('changeAffiliateStatusDeactivated') IS NOT NULL DROP PROC changeAffiliateStatusDeactivated
GO 
CREATE PROCEDURE changeAffiliateStatusDeactivated
	@affPk int,
	@requestingUserPk int
AS
BEGIN
SET NOCOUNT ON
DECLARE @subAffPk int
DECLARE @affType char
DECLARE @errorCode int
DECLARE @primeAffType char
-- Get the Type of the Affiliate being deactivated
SELECT @primeAffType = (SELECT aff_type from Aff_Organizations WHERE aff_pk = @affPk)

	BEGIN TRAN changeAffiliateStatusDeactivated

	-- Create cursor containing this Affiliate, all Sub Affiliates, 
	-- and if needed, all of THEIR Sub Affiliates in reverse order by aff_type
	DECLARE affCursor CURSOR READ_ONLY FOR
	SELECT aff_pk, aff_type FROM Aff_Organizations WHERE aff_pk = @affPk OR parent_aff_fk = @affPk OR parent_aff_fk IN (SELECT aff_pk FROM Aff_Organizations WHERE parent_aff_fk = @affPk) ORDER BY aff_type DESC

	OPEN affCursor;
	FETCH NEXT FROM affCursor INTO @subAffPk, @affType
	
	-- Process each sub Affiliate
	WHILE (@@FETCH_STATUS=0) 
	BEGIN

		-- Set THIS Affiliate's Status to Deactivated
		-- If its not a Council being deactivated, deactivate all affiliates
		IF ((@primeAffType <> 'C') OR (@affPk = @subAffPk))
			UPDATE Aff_Organizations
			SET    Aff_Status = 17004,
				 lst_mod_dt = getDate(),
				 lst_mod_user_pk = @requestingUserPk
			WHERE  aff_pk = @subAffPk
	
		-- Delete this Affiliate's Mailing Lists if its not a council being deactivated
		-- Councils are not on Mailing Lists	
		IF (@primeAffType <> 'C' AND @affType <> 'C')		
			DELETE FROM Mailing_List_Orgs	WHERE org_pk = @affPk	
	
		-- Set this Affiliate's Members to inactive if its not a Council being deactivated
		-- Councils do not have Members		
		IF (@primeAffType <> 'C' AND @affType <> 'C')
			EXECUTE @errorCode = inactivateMembers 
				@affPk = @subAffPk,
				@requestingUserPk = @requestingUserPk
				
		-- Get error code
		SELECT @errorCode = @@ERROR
		IF @errorCode <> 0 
			GOTO E_ERROR							

		-- Update the appropriate field based on the Affiliate Type when 
		-- Council is Affiliate being processed		
		IF (@primeAffType = 'C')
		BEGIN
			IF (@affType = 'U')
				UPDATE Aff_Organizations
				SET 	 aff_councilRetiree_chap = ' ',					 
					 lst_mod_dt = getDate(),
					 lst_mod_user_pk = @requestingUserPk
				WHERE  aff_pk=@subAffPk
			IF (@affType = 'L')
				UPDATE Aff_Organizations
				SET 	 aff_councilRetiree_chap = ' ',
					 parent_aff_fk = NULL,
					 lst_mod_dt = getDate(),
					 lst_mod_user_pk = @requestingUserPk
				WHERE  aff_pk=@subAffPk					
				
		END		
		
		-- set any officers to inactive
		UPDATE Officer_History
		SET pos_end_dt = getDate()
		WHERE aff_pk = @subAffPk
		AND pos_end_dt IS NULL
			
		-- Fetch next sub Affiliate record
		FETCH NEXT FROM affCursor INTO @subAffPk, @affType			

	END
	
	CLOSE affCursor;		
	DEALLOCATE affCursor;	

	COMMIT TRAN changeAffiliateStatusDeactivated

E_ERROR:
	IF @errorCode <> 0 
	BEGIN
		PRINT 'Error occurred in changeAffiliateStatusDeactivated() with error code = ' + CONVERT(VARCHAR, @errorCode)
		ROLLBACK TRAN changeAffiliateStatusDeactivated
	END

SET NOCOUNT OFF
RETURN @errorCode
END
GO
    
--***************************************************************************************
--* "Moves" all members from the old Sub Local to the new Local and changes the old 
--* Affiliate's status to 'Split'.
--* 
--* Only valid for a Sub Local - Validation check performed in front end 
--* 
--* Call moveMembers from the old Local to the new Local.
--* 
--* @param oldAffPk Old Affiliate's Primary Key
--* @param newAffPk New Affiliate's Primary Key
--* @param requestingUserPk Requesting User's Primary Key
--***************************************************************************************
IF OBJECT_ID('changeAffiliateStatusSplit') IS NOT NULL DROP PROC changeAffiliateStatusSplit
GO 
CREATE PROCEDURE changeAffiliateStatusSplit
	@oldAffPk int,
	@newAffPk int,
	@requestingUserPk int
AS
BEGIN
DECLARE @errorCode int 

	BEGIN TRAN changeAffiliateStatusSplit

	-- Execute moveMembers
	EXECUTE @errorCode = moveMembers
			@currentAffPk = @oldAffPk,
			@newAffPk = @newAffPk,
			@requestingUserPk = @requestingUserPk 

	-- set any officers to inactive
	UPDATE Officer_History
	SET pos_end_dt = getDate()
	WHERE aff_pk = @oldAffPk
	AND pos_end_dt IS NULL

	-- Set Affiliate Status to 'Split'
	-- Set source to new aff
	UPDATE Aff_Organizations
	SET    aff_status = 17011,
		new_aff_id_src = @newAffPk,
		 lst_mod_dt = getDate(), 
		 lst_mod_user_pk = @requestingUserPk
	WHERE  aff_pk = @oldAffPk

	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode = 0 COMMIT TRAN changeAffiliateStatusSplit
	ELSE	
	BEGIN
		PRINT 'Error occurred in changeAffiliateStatusSplit() with error code = ' + CONVERT(VARCHAR, @errorCode)
		ROLLBACK TRAN changeAffiliateStatusSplit
	END

RETURN @errorCode
END
GO
  
--***************************************************************************************
--* "Moves" all members from the old Local to the new Affiliate and changes the old 
--* Affiliate's status to 'Merged'.  Members are deactivated in the old Affiliate 
--* and new records will be created in the new Affiliate.
--* 
--* Only valid for Locals - Validation check performed in front end 
--* 
--* Call moveMembers from the old Local to the new Local.
--* 
--* @param oldAffPk Old Affiliate's Primary Key
--* @param newAffPk New Affiliate's Primary Key
--* @param requestingUserPk Requesting User's Primary Key
--***************************************************************************************
IF OBJECT_ID('changeAffiliateStatusMerged') IS NOT NULL DROP PROC changeAffiliateStatusMerged 
GO 
CREATE PROCEDURE changeAffiliateStatusMerged
	@oldAffPk int,
	@newAffPk int,
	@requestingUserPk int
AS
BEGIN
DECLARE @errorCode int
DECLARE @subAffPk int
SELECT @errorCode = 0

	BEGIN TRAN changeAffiliateStatusMerged

	-- Execute moveMembers 
	EXECUTE @errorCode = moveMembers
			@currentAffPk = @oldAffPk,
			@newAffPk = @newAffPk,
			@requestingUserPk = @requestingUserPk
		
	-- set any officers to inactive
	UPDATE Officer_History
	SET    pos_end_dt = getDate()
	WHERE  aff_pk = @oldAffPk
	AND    pos_end_dt IS NULL		
		
	-- Set Affiliate Status to 'Merged'
	UPDATE Aff_Organizations
	SET    aff_status = 17006,
		 lst_mod_dt = getDate(), 
		 lst_mod_user_pk = @requestingUserPk,
		 new_aff_id_src = @newAffPk
	WHERE  aff_pk = @oldAffPk	

	DECLARE subAffCursor CURSOR READ_ONLY FOR
	SELECT aff_pk FROM Aff_Organizations WHERE parent_aff_fk = @oldAffPk

	-- Open cursor to fetch each sub Affiliate
	OPEN subAffCursor;
	FETCH NEXT FROM subAffCursor INTO @subAffPk

	WHILE (@@FETCH_STATUS=0)
	BEGIN

		-- Set all sub Affiliate Statuses to Merged
		UPDATE Aff_Organizations
		SET    Aff_Status = 17006,
			 lst_mod_dt = getDate(),
			 lst_mod_user_pk = @requestingUserPk,
			 new_aff_id_src = @newAffPk
		WHERE  aff_pk = @subAffPk
		
		-- set any officers to inactive
		UPDATE Officer_History
		SET    pos_end_dt = getDate()
		WHERE  aff_pk = @subAffPk
		AND    pos_end_dt IS NULL		

		-- Move all the members of the sub Affiliate to the new Affiliate
		EXECUTE @errorCode = moveMembers
			@currentAffPk = @subAffPk,
			@newAffPk = @newAffPk,
			@requestingUserPk = @requestingUserPk
			
		FETCH NEXT FROM subAffCursor INTO @subAffPk			
			
	END
	
	CLOSE subAffCursor;		
	DEALLOCATE subAffCursor;

	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode = 0 COMMIT TRAN changeAffiliateStatusMerged
	ELSE	
	BEGIN
		PRINT 'Error occurred in changeAffiliateStatusMerged() with error code = ' + CONVERT(VARCHAR, @errorCode)
		ROLLBACK TRAN changeAffiliateStatusMerged
	END

RETURN @errorCode
END
GO
    
--***************************************************************************************
--* Updates the Member Renewal field for the Affiliate and all of it's sub 
--* Affiliates.
--* 
--* @param affPk Primary Key of the Affiliate at the top of the hierarchy.
--* @param mbrRenewal TRUE if set and FALSE if not set
--* @param requestingUserPk Requesting User's Primary Key
--***************************************************************************************
IF OBJECT_ID('setMemberRenewal') IS NOT NULL DROP PROC setMemberRenewal
GO 
CREATE PROCEDURE setMemberRenewal
	@affPk int,
	@mbrRenewal int,
	@requestingUserPk int
AS
BEGIN
SET NOCOUNT ON
DECLARE @mbrRenewalCode int, @errorCode int, @subAffPk int

	BEGIN TRAN setMemberRenewal

	-- Set the Member Renewal Common Code
	IF (@mbrRenewal = 1)
		SELECT @mbrRenewalCode = 23002
	ELSE
		SELECT @mbrRenewalCode = 23001

	-- Update the Renewal Member field for the Affiliate
	UPDATE Aff_Organizations 
	SET    mbr_renewal = @mbrRenewalCode,
		 lst_mod_dt = getDate(), 
		 lst_mod_user_pk = @requestingUserPk
	WHERE  aff_pk = @affPk OR parent_aff_fk = @affPk

	-- Update the Renewal Member field for the Members of the Affiliate
	UPDATE Aff_Members 
	SET    mbr_ret_dues_renewal_fg = @mbrRenewal,
		 lst_mod_dt = getDate(), 
		 lst_mod_user_pk = @requestingUserPk
	WHERE  aff_pk = @affPk OR aff_pk IN (SELECT aff_pk from Aff_Organizations where parent_aff_fk = @affPk)

	-- Get error code	
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	COMMIT TRAN setMemberRenewal

E_ERROR:
	IF @errorCode <> 0 
	BEGIN
		PRINT 'Error occurred in setMemberRenewal() with error code = ' + CONVERT(VARCHAR, @errorCode)
		ROLLBACK TRAN setMemberRenewal
	END

SET NOCOUNT OFF
RETURN @errorCode
END
GO 

--***************************************************************************************
--* Updates the Affiliate Membership Reporting Information field for the Affiliate and 
--* all of it's sub Affiliates.
--* 
--* @param affPk Primary Key of the Affiliate at the top of the hierarchy.
--* @param requestingUserPk Primary Key of the user who is requesting the mass change.
--* @param field Affiliate Membership Reporting Information column
--* @param fieldValue Affiliate Membership Reporting Information value
--***************************************************************************************
IF OBJECT_ID('setAffMembershipReportingInfo') IS NOT NULL DROP PROC setAffMembershipReportingInfo
GO 
CREATE PROCEDURE setAffMembershipReportingInfo
	@affPk int,
	@requestingUserPk int,
	@field varchar(50),
	@fieldValue int
AS
BEGIN
SET NOCOUNT ON
DECLARE @subAffPk int,
	  @sqlSubString NVARCHAR(300),
	  @sqlString NVARCHAR(500),
	  @errorCode int
DECLARE subAffCursor CURSOR READ_ONLY FOR
SELECT aff_pk FROM Aff_Organizations WHERE parent_aff_fk = @affPk
SET NOCOUNT OFF

	BEGIN TRAN setAffMembershipReportingInfo

	-- Build the Sub SQL Statement
	SELECT @sqlSubString = N'UPDATE Aff_Mbr_Rpt_Info ' +
					'SET ' + @field + '=' + CAST(@fieldValue AS NVARCHAR(10)) +
					', lst_mod_dt = getDate() ' +
					', lst_mod_user_pk = ' + CAST(@requestingUserPk AS NVARCHAR(10)) +
					' WHERE aff_pk ' 				
	
	-- Update Aff_Mbr_Rpt_Info record
	SELECT @sqlString = @sqlSubString + '=' + CAST(@affPk AS NVARCHAR(10))
	EXECUTE sp_executesql @sqlString

	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Process each sub Affiliate
	IF EXISTS (SELECT 1 FROM Aff_Organizations WHERE parent_aff_fk = @affPk) 
	BEGIN
		-- Update Aff_Mbr_Rpt_Info record for its sub Affiliate
		SELECT @sqlString = @sqlSubString + 
					  ' IN (SELECT aff_pk FROM aff_organizations WHERE parent_aff_fk=' +
					  CAST(@affPk AS NVARCHAR(10)) + ')'

		EXECUTE sp_executesql @sqlString

		-- Get error code
		SELECT @errorCode = @@ERROR
		IF @errorCode <> 0 GOTO E_ERROR

		-- Open cursor to fetch each sub Affiliate
		OPEN subAffCursor;
		FETCH NEXT FROM subAffCursor INTO @subAffPk

		WHILE (@@FETCH_STATUS=0) 
		BEGIN
			-- Process each sub Affiliate within this Affiliate
			IF EXISTS (SELECT 1 FROM Aff_Organizations WHERE parent_aff_fk = @subAffPk) 
			BEGIN 
				-- Update Aff_Mbr_Rpt_Info record for its sub Affiliate
				SELECT @sqlString = @sqlSubString + 
					  ' IN (SELECT aff_pk FROM aff_organizations WHERE parent_aff_fk=' +
					  CAST(@subAffPk AS NVARCHAR(10)) + ')'

				EXECUTE sp_executesql @sqlString
			END

			-- Fetch next record
			FETCH NEXT FROM subAffCursor INTO @subAffPk
		END

		-- Close cursor
		CLOSE subAffCursor
	END

	COMMIT TRAN setAffMembershipReportingInfo

E_ERROR:
	IF @errorCode <> 0 
	BEGIN
		PRINT 'Error occurred in setAffMembershipReportingInfo() with error code = ' + CONVERT(VARCHAR, @errorCode)
		ROLLBACK TRAN setAffMembershipReportingInfo
	END

DEALLOCATE subAffCursor
RETURN @errorCode
END
GO 
       

--***************************************************************************************
--* Loops on all the Mass Change requests, and calls other Stored Procedures to 
--* perform each individual mass change.
--* 
--* changeAffiliateIdentifier
--* changeAffiliateStatusDeactivated
--* changeAffiliateStatusMerged
--* changeAffiliateStatusSplit
--* setMemberRenewal
--* setMembershipInfoReportingSource -> setAffMembershipReportingInfo
--* setUnitWideNoPEMail -> setAffMembershipReportingInfo
--* setUnitWideNoCards -> setAffMembershipReportingInfo
--* 
--* For each mass change, it sets the the mass_chng_completed_dt and the 
--* mass_chng_error_fg (if an error occurred) for that entry.
--* 
--* For each mass change, it calls addChangeToHistory
--***************************************************************************************
IF OBJECT_ID('performMassChange') IS NOT NULL DROP PROC performMassChange
GO 
CREATE PROCEDURE performMassChange
AS
BEGIN
SET NOCOUNT ON
DECLARE massChgCursor CURSOR FOR
SELECT aff_pk, requesting_user_pk, mass_chng_type,
	 new_aff_pk, new_select_value, new_flag_value, new_aff_stateNat_type,
	 new_aff_councilRetiree_chap, new_aff_localSubChapter, new_aff_subUnit, 
	 new_aff_type, mass_chng_request_dt, mass_chng_priority, new_aff_code
FROM Mass_Change_Batch_Control
WHERE mass_chng_completed_dt IS NULL
ORDER BY mass_chng_priority

DECLARE 
	@localNumber char(4),
	@affPk int,
	@requestingUserPk int,
	@massChangeType int,
	@newAffPk int,
	@newSelectValue int,
	@newFlagValue int,
	@newAffState char(2),
	@affCouncilRetireeChap char(4),
	@affLocalSubChapter char(4),
	@affSubUnit char(4),
	@affType char,
	@requestDate datetime,
	@priority int,
	@newCode char,
	@fieldCodePk int,
	@sectionCodePk int,
	@oldValue varchar(254),
	@newValue varchar(254),
	@stateOldValue varchar(254),
	@stateNewValue varchar(254),	
	@errorCode int

	-- Set the section code to Membership Reporting,
	-- a field in Aff_Chng_History table
	SELECT @sectionCodePk = 39007

	-- Open cursor
	OPEN massChgCursor;
	FETCH NEXT FROM massChgCursor INTO 
		@affPk,
		@requestingUserPk,
		@massChangeType,
		@newAffPk,
		@newSelectValue,
		@newFlagValue,
		@newAffState,
		@affCouncilRetireeChap,
		@affLocalSubChapter,
		@affSubUnit,
		@affType,
		@requestDate,
		@priority,
		@newCode

	WHILE (@@FETCH_STATUS=0) 
	BEGIN		
		-- Perform Mass Change request
		IF (@massChangeType = 22001)
		BEGIN
			PRINT '---------------------------------------'
			PRINT 'Perform MembershipInfoReportingSource..'
			PRINT '---------------------------------------'

			-- Set Affiliate Change History fields
			SELECT @fieldCodePk = 64069
			SELECT @newValue = CONVERT(VARCHAR, @newSelectValue)
			SELECT @oldValue = CONVERT(VARCHAR, (SELECT isnull(aff_info_reporting_source, 47009)
									 FROM   Aff_Mbr_Rpt_Info 
									 WHERE  aff_pk = @affPk))

			-- Execute setMembershipInfoReportingSource
			EXECUTE @errorCode = setAffMembershipReportingInfo
					@affPk = @affPk,
					@requestingUserPk = @requestingUserPk,
					@field = 'aff_info_reporting_source',
					@fieldValue = @newSelectValue
			
			SELECT @newValue = (SELECT com_cd_desc 
						  FROM  Common_Codes cc WHERE cc.com_cd_pk = @newValue)
			SELECT @oldValue = (SELECT com_cd_desc 
						  FROM  Common_Codes cc WHERE cc.com_cd_pk = @oldValue)
		END

		ELSE IF (@massChangeType = 22002)
		BEGIN
			PRINT '---------------------------------------'
			PRINT 'Perform setMemberRenewal..'
			PRINT '---------------------------------------'

			-- Set Affiliate Change History fields
			SELECT @fieldCodePk = 64009
			IF (@newFlagValue = 1)
				SELECT @newValue = '23002'
			ELSE
				SELECT @newValue = '23001'

			SELECT @oldValue = CONVERT(VARCHAR, (SELECT isnull(mbr_renewal, 23001)
									 FROM   Aff_Organizations
									 WHERE  aff_pk = @affPk))

			-- Execute setMemberRenewal
			EXECUTE @errorCode = setMemberRenewal
					@affPk = @affPk,
					@mbrRenewal = @newFlagValue,
					@requestingUserPk = @requestingUserPk
					
			SELECT @newValue = (SELECT com_cd_desc 
						  FROM  Common_Codes cc WHERE cc.com_cd_pk = @newValue)
			SELECT @oldValue = (SELECT com_cd_desc 
						  FROM  Common_Codes cc WHERE cc.com_cd_pk = @oldValue)
		END

		ELSE IF (@massChangeType = 22003)
		BEGIN
			PRINT '---------------------------------------'
			PRINT 'Perform changeAffiliateIdentifier..'
			PRINT '---------------------------------------'

			-- Set Affiliate Change History fields
			SELECT @fieldCodePk = 64068
			
			
			
			IF (@affType = 'C' OR @affType = 'R' OR @affType = '#')
			BEGIN
				SELECT @localNumber = @affCouncilRetireeChap 
				SELECT @oldValue = CONVERT(VARCHAR, (SELECT aff_councilRetiree_chap 
								             FROM   Aff_Organizations
										 WHERE  aff_pk = @affPk))
			END
			ELSE IF (@affType = 'L' OR @affType = 'S')
			BEGIN
				SELECT @localNumber = @affLocalSubChapter
				SELECT @oldValue = CONVERT(VARCHAR, (SELECT aff_localSubChapter
								             FROM   Aff_Organizations
										 WHERE  aff_pk = @affPk))
			END
			ELSE IF (@affType = 'U')
			BEGIN
				SELECT @localNumber = @affSubUnit
				SELECT @oldValue = CONVERT(VARCHAR, (SELECT aff_subUnit
								             FROM   Aff_Organizations
										 WHERE  aff_pk = @affPk))
			END
			
			IF (@affType = '#')
			BEGIN
				SELECT @newAffState = CONVERT(VARCHAR, (SELECT aff_stateNat_Type
								         FROM   Aff_Organizations
									 WHERE  aff_pk = @affPk))				
								
			END

			SELECT @stateOldValue = CONVERT(VARCHAR, (SELECT aff_stateNat_Type
							             FROM   Aff_Organizations
									 WHERE  aff_pk = @affPk))
			SELECT @stateNewValue = CONVERT(VARCHAR, @newAffState)
			IF (@stateOldValue <> @stateNewValue)
				EXECUTE addChangeToHistory 
					@affPk = @affPk,
					@sectionCodePk = @sectionCodePk,
					@userPk = @requestingUserPk,
					@fieldCodePk = @fieldCodePk,
					@oldValue = @stateOldValue,
					@newValue = @stateNewValue 				

			IF (@localNumber IS NOT NULL)
				SELECT @newValue = CONVERT(VARCHAR, @localNumber)
			ELSE
				SELECT @newValue = ' '

			-- Execute changeAffiliateIdentifier
			EXECUTE @errorCode = changeAffiliateIdentifier
					@affPk   = @affPk,
					@affType = @affType,
					@localNumber = @localNumber,
					@affState = @newAffState,
					@requestingUserPk = @requestingUserPk,
					@newCode = @newCode
		END

		ELSE IF (@massChangeType = 22004)
		BEGIN
			PRINT '---------------------------------------'
			PRINT 'Perform setUnitWideNoCards..'
			PRINT '---------------------------------------'

			-- Set Affiliate Change History fields
			SELECT @fieldCodePk = 64066
			IF (@newFlagValue = 1)
				SELECT @newValue = 'true'
			ELSE 
				SELECT @newValue = 'false'
			SELECT @oldValue = (SELECT CASE 
						  WHEN unit_wide_no_mbr_cards_fg = 1 
				        	 	THEN 'true' 
						  	ELSE 'false'
						  END checkFlag
						  FROM   Aff_Mbr_Rpt_Info 
						  WHERE  aff_pk = @affPk)

			-- Execute setUnitWideNoCards
			EXECUTE @errorCode = setAffMembershipReportingInfo
					@affPk = @affPk,
					@requestingUserPk = @requestingUserPk,
					@field = 'unit_wide_no_mbr_cards_fg',
					@fieldValue = @newFlagValue
		END

		ELSE IF (@massChangeType = 22005)
		BEGIN
			PRINT '---------------------------------------'
			PRINT 'Perform setUnitWideNoPEMail..'
			PRINT '---------------------------------------'

			-- Set Affiliate Change History fields
			SELECT @fieldCodePk = 64067
			IF (@newFlagValue = 1)
				SELECT @newValue = 'true'
			ELSE 
				SELECT @newValue = 'false'
			SELECT @oldValue = (SELECT CASE 
						  WHEN unit_wide_no_pe_mail_fg = 1 
							THEN 'true' 
							ELSE 'false'
						  END checkFlag
						  FROM   Aff_Mbr_Rpt_Info 
						  WHERE  aff_pk = @affPk)

			-- Execute setUnitWideNoPEMail
			EXECUTE @errorCode = setAffMembershipReportingInfo
					@affPk = @affPk,
					@requestingUserPk = @requestingUserPk,
					@field = 'unit_wide_no_pe_mail_fg',
					@fieldValue = @newFlagValue
		END

		ELSE IF (@massChangeType = 22006)
		BEGIN
			PRINT '---------------------------------------'
			PRINT 'Perform changeAffiliateStatusDeactivated..'
			PRINT '---------------------------------------'

			-- Set Affiliate Change History fields
			SELECT @fieldCodePk = 64065
			SELECT @newValue = 'Inactive'
			SELECT @oldValue = 'Active'

			-- Execute changeAffiliateStatusDeactivated
			EXECUTE @errorCode = changeAffiliateStatusDeactivated
					@affPk = @affPk,
					@requestingUserPk = @requestingUserPk
		END

		ELSE IF (@massChangeType = 22007)
		BEGIN
			PRINT '---------------------------------------'
			PRINT 'Perform changeAffiliateStatusMerged..'
			PRINT '---------------------------------------'

			-- Set Affiliate Change History fields
			SELECT @fieldCodePk = 64065
			SELECT @newValue = 'Merged'
			SELECT @oldValue = (SELECT com_cd_desc 
						  FROM  Aff_Organizations o 
						  JOIN  Common_Codes cc ON cc.com_cd_pk = o.aff_status
                                      WHERE o.aff_pk = @affPk)

			-- Execute changeAffiliateStatusMerged
			EXECUTE @errorCode = changeAffiliateStatusMerged
					@oldAffPk = @affPk,
					@newAffPk = @newAffPk,
					@requestingUserPk = @requestingUserPk
		END

		ELSE IF (@massChangeType = 22008)
		BEGIN
			PRINT '---------------------------------------'
			PRINT 'Perform changeAffiliateStatusSplit..'
			PRINT '---------------------------------------'

			-- Set Affiliate Change History fields
			SELECT @fieldCodePk = 64065
			SELECT @newValue = 'Split'
			SELECT @oldValue = (SELECT com_cd_desc 
						  FROM  Aff_Organizations o 
						  JOIN  Common_Codes cc ON cc.com_cd_pk = o.aff_status
                                      WHERE o.aff_pk = @affPk)

			-- Execute changeAffiliateStatusSplit
			EXECUTE @errorCode = changeAffiliateStatusSplit
					@oldAffPk = @affPk,
					@newAffPk = @newAffPk,
					@requestingUserPk = @requestingUserPk
		END

		-- Execute addChangeToHistory
		PRINT ' '
		PRINT 'addChangeToHistory ..'
		IF (@oldValue <> @newValue)
		EXECUTE addChangeToHistory 
				@affPk = @affPk,
				@sectionCodePk = @sectionCodePk,
				@userPk = @requestingUserPk,
				@fieldCodePk = @fieldCodePk,
				@oldValue = @oldValue,
				@newValue = @newValue 

		-- Set the completion date
		PRINT ' '
		PRINT 'Set the Mass Change completion date, error flag ..'
		UPDATE Mass_Change_Batch_Control
		SET    mass_chng_completed_dt = getDate(),
			 mass_chng_error_fg = @errorCode
		WHERE  aff_pk = @affPk AND 
			 mass_chng_request_dt = @requestDate AND
              	 requesting_user_pk = @requestingUserPk AND
			 mass_chng_priority = @priority

		-- Fetch next Mass Change record to perform
		FETCH NEXT FROM massChgCursor INTO 
			@affPk,
			@requestingUserPk,
			@massChangeType,
			@newAffPk,
			@newSelectValue,
			@newFlagValue,
			@newAffState,
			@affCouncilRetireeChap,
			@affLocalSubChapter,
			@affSubUnit,
			@affType,
			@requestDate,
			@priority,
			@newCode
	END

	-- Close cursor
	CLOSE massChgCursor
	DEALLOCATE massChgCursor
SET NOCOUNT OFF
END
GO 

    
--***************************************************************************************
--* The main method that is called to start the integration process. This method 
--* will check if there is an entry in the nightly run table. If there is, it calls 
--* the performMassChange method to process the mass change requests. 
--* 
--* Once mass change processes are completed, it uses the values in the 
--* mass_chng_error_fg and the mass_chng_completed_dt fields to determine what type 
--* of email to send to the user if the user provided a valid email address.
--***************************************************************************************
IF OBJECT_ID('initiateMassChange') IS NOT NULL DROP PROC initiateMassChange
GO 
CREATE PROCEDURE initiateMassChange
AS
BEGIN
DECLARE @massChangeRec int,
 	  @errorFlag smallint,
	  @emailAddr varchar(50),
	  @massChangeRequestedDate datetime,
	  @massChangeType varchar(50),
	  @msg varchar(8000),
	  @curDate datetime

SET NOCOUNT ON

-- Get current date and time
SELECT @curDate = getDate()

-- Check for any existing entry in the nightly run table
SELECT @massChangeRec = COUNT(*) FROM Mass_Change_Batch_Control 
                        WHERE DATEDIFF(DAY, mass_chng_request_dt, getDate()) >= 0 AND
 					mass_chng_completed_dt IS NULL

-- Get the results of the mass change process
DECLARE massChgResultCursor CURSOR READ_ONLY FOR
SELECT mass_chng_error_fg, RTRIM(LOWER(person_email_addr)), mass_chng_request_dt,
	(SELECT com_cd_desc FROM common_codes WHERE com_cd_pk = mass_chng_type)
FROM Mass_Change_Batch_Control mass_change
JOIN Person_Email person_email ON person_email.person_pk = mass_change.requesting_user_pk
	AND (person_email.person_email_addr IS NOT NULL AND LEN(person_email.person_email_addr) > 0)
WHERE DATEDIFF(MILLISECOND, @curDate, mass_chng_completed_dt) > 0

SET NOCOUNT OFF

	-- Execute performMassChange
	IF (@massChangeRec > 0) 
	BEGIN
		PRINT ' '
		PRINT 'Execute performMassChange'
		EXECUTE performMassChange

		-- Open cursor
		OPEN massChgResultCursor;
		FETCH NEXT FROM massChgResultCursor INTO 
				@errorFlag, 
				@emailAddr, 
				@massChangeRequestedDate,
				@massChangeType

		WHILE (@@FETCH_STATUS=0) 
		BEGIN
			-- Build email body 
			IF (@errorFlag = 0) 
				SELECT @msg = 'The request for Mass Change has been processed successfully.'							  
			ELSE
				SELECT @msg = 'The process for the Mass Change request has failed.'

			SELECT @msg = @msg + CHAR(13) + CHAR(10) +
				'Mass Change Requested Date: ' + CONVERT(VARCHAR, @massChangeRequestedDate) + CHAR(13) + CHAR(10) +
				'Mass Change Type: ' + @massChangeType													

			-- Execute sendmail 
			PRINT ' '
			PRINT 'Execute sendmail: emailAddr=' + @emailAddr + ', message=' + @msg
			EXECUTE sendmail 
				@fromName = 'AFSCME IT',
				@fromEmail = 'user@afscme.org',
				@toEmail = @emailAddr,
				@subject='AFSCME Mass Change Result', 
				@body=@msg

			-- Fetch next row
			FETCH NEXT FROM massChgResultCursor INTO
				@errorFlag, 
				@emailAddr, 
				@massChangeRequestedDate,
				@massChangeType
		END

		-- Close cursor
		CLOSE massChgResultCursor
	END
	-- Deallocate cursor
	DEALLOCATE massChgResultCursor
END
GO 

--***************************************************************************************
--* This trigger will be fired after a row is added to table Mass_Change_Batch_Control.
--* This trigger is currently inactivated. If the requirement is changed to applying 
--* Mass Change changes on the fly, then we need to activate the trigger. Currently, the
--* design is to execute the changes of Mass Change on a scheduled time.
--***************************************************************************************
--IF EXISTS (SELECT 1 FROM sysobjects 
--     WHERE name = 'TIUD_MassChangeBatchControl' AND type = 'TR')
--     DROP TRIGGER TIUD_MassChangeBatchControl
--GO
--
--CREATE TRIGGER TIUD_MassChangeBatchControl ON Mass_Change_Batch_Control
--AFTER INSERT AS
--	EXECUTE initiateMassChange	
--GO
