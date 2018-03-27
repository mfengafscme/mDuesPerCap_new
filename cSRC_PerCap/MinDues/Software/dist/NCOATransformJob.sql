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