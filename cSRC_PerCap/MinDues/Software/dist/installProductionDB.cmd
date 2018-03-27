SETLOCAL

call setEnv.cmd

set SQL_CMD=isql -S %AFSCME_DB_SERVER% -U %AFSCME_DB_USER% -P %AFSCME_DB_PASSWORD%

echo IF EXISTS (SELECT name FROM master.dbo.sysdatabases WHERE name = N'%AFSCME_DB_NAME%') DROP DATABASE [%AFSCME_DB_NAME%] CREATE DATABASE [%AFSCME_DB_NAME%] | %SQL_CMD% -b -d master
if errorlevel 1 goto failed

%SQL_CMD% -b -d %AFSCME_DB_NAME% -i createTables.sql
if errorlevel 1 goto failed
%SQL_CMD% -b -d %AFSCME_DB_NAME% -i createViews.sql
if errorlevel 1 goto failed
%SQL_CMD% -b -d %AFSCME_DB_NAME% -i createTriggers.sql
if errorlevel 1 goto failed
%SQL_CMD% -b -d %AFSCME_DB_NAME% -i insertCodes.sql
if errorlevel 1 goto failed
%SQL_CMD% -b -d %AFSCME_DB_NAME% -i insertAfscmeOfficerData.sql
if errorlevel 1 goto failed
%SQL_CMD% -b -d %AFSCME_DB_NAME% -i insertFields.sql
if errorlevel 1 goto failed
%SQL_CMD% -b -d %AFSCME_DB_NAME% -i insertReports.sql
if errorlevel 1 goto failed
%SQL_CMD% -b -d %AFSCME_DB_NAME% -i insertPrivileges.sql
if errorlevel 1 goto failed
%SQL_CMD% -b -d %AFSCME_DB_NAME% -i insertRoles.sql
if errorlevel 1 goto failed
%SQL_CMD% -b -d %AFSCME_DB_NAME% -i insertZips_All.sql
if errorlevel 1 goto failed
%SQL_CMD% -b -d %AFSCME_DB_NAME% -i insertTimeDimension.sql
if errorlevel 1 goto failed
%SQL_CMD% -b -d %AFSCME_DB_NAME% -i insertAdminUser.sql
if errorlevel 1 goto failed
%SQL_CMD% -b -d %AFSCME_DB_NAME% -i insertNCOACodes.sql
if errorlevel 1 goto failed
%SQL_CMD% -b -d %AFSCME_DB_NAME% -i insertCOMAppTables.sql
if errorlevel 1 goto failed

rem STORED PROCEDURES
%SQL_CMD% -b -d %AFSCME_DB_NAME% -i StoredProcMassChange.sql
if errorlevel 1 goto failed
%SQL_CMD% -b -d %AFSCME_DB_NAME% -i createNCOAStoredProcedures.sql
if errorlevel 1 goto failed

rem CREATE JOB
%SQL_CMD% -b -d %AFSCME_DB_NAME% -i NCOATransformJob.sql
if errorlevel 1 goto failed

goto done

:failed
echo DB Install Failed
ENDLOCAL
exit /b 1

:done
echo DB Install Complete
ENDLOCAL

