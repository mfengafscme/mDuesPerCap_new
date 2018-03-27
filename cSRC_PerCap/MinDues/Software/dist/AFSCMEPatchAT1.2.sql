-- AFSCME Patch AT1.2
-- Create missing jobs
-- Set configurable timeframes for Acceptance testing

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
--* SUMMARY: Stored Procedure that will handle the background processing of NCOA 
--* Transactions from AFSCME at a scheduled time.
--***************************************************************************************

--***************************************************************************************
--*
--* 1. Remove the initiateNCOAIntegration task from the current database.
--* 2. Add the initiateNCOAIntegration task onto the current database.
--* 3. Schedule to run initiateNCOAIntegration task daily at 9:00 PM.
--*
--***************************************************************************************
DECLARE @taskName varchar(100)
DECLARE @dbName varchar(100)
DECLARE @ProcName varchar(100)

SELECT @dbName = db_name()
SELECT @taskName = 'initiateNCOAIntegration_' + @dbName
SELECT @ProcName = 'EXECUTE ' + @dbname + '.dbo.initiateNCOAIntegration'

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
	@command = @ProcName,
	@description = 'Nightly Run Task to Initiate NCOA Integration.'	

EXECUTE msdb..sp_add_jobschedule 
	@job_name = @taskName,
	@name = 'ScheduledNCOAIntegration',
	@freq_type = 4, 
	@freq_interval = 1,
	@active_start_time = 013000
GO

--***************************************************************************************
--* SUMMARY: Set configurable timeframes for NCOA, Apply Update and SMA for 
--* Acceptance testing.
--***************************************************************************************

UPDATE COM_App_Config_Data
SET variable_value='1', lst_mod_dt=GetDate()
WHERE variable_name = 'AUPUpdateRecentTimeLimit'

GO

UPDATE COM_App_Config_Data
SET variable_value='1', lst_mod_dt=GetDate()
WHERE variable_name = 'ConfigTimeframe'

GO

UPDATE COM_App_Config_Data
SET variable_value='1', lst_mod_dt=GetDate()
WHERE variable_name = 'MemberSMAUpdateTimeFrame'

GO
