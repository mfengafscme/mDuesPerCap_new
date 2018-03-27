SETLOCAL

call setEnv.cmd

set SQL_CMD=isql -S %AFSCME_DB_SERVER% -U %AFSCME_DB_USER% -P %AFSCME_DB_PASSWORD%

%SQL_CMD% -b -d %AFSCME_DB_NAME% -i AFSCMEPatchST2.sql
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
echo DB Install FAILED
ENDLOCAL
exit /b 1

:done
echo DB Install COMPLETED!!!!!
ENDLOCAL
