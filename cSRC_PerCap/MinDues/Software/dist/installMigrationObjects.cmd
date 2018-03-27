SETLOCAL

call setEnv.cmd

set SQL_CMD=isql -S %AFSCME_DB_SERVER% -U %AFSCME_DB_USER% -P %AFSCME_DB_PASSWORD%

%SQL_CMD% -b -d %AFSCME_DB_NAME% -i C:\AFSCME_Migration\DM_Script_Files\Create_Legacy_Tables.sql
if errorlevel 1 goto failed
%SQL_CMD% -b -d %AFSCME_DB_NAME% -i C:\AFSCME_Migration\DM_Script_Files\DM_Code_Mapping_view.sql
if errorlevel 1 goto failed
%SQL_CMD% -b -d %AFSCME_DB_NAME% -i C:\AFSCME_Migration\DM_Script_Files\sp_migrateCodeTables.sql
if errorlevel 1 goto failed
%SQL_CMD% -b -d %AFSCME_DB_NAME% -i C:\AFSCME_Migration\DM_Script_Files\sp_errorAffiliate.sql
if errorlevel 1 goto failed
%SQL_CMD% -b -d %AFSCME_DB_NAME% -i C:\AFSCME_Migration\DM_Script_Files\sp_migrateAffiliate.sql
if errorlevel 1 goto failed
%SQL_CMD% -b -d %AFSCME_DB_NAME% -i C:\AFSCME_Migration\DM_Script_Files\sp_errorPerson.sql
if errorlevel 1 goto failed
%SQL_CMD% -b -d %AFSCME_DB_NAME% -i C:\AFSCME_Migration\DM_Script_Files\sp_errorDMAL.sql
if errorlevel 1 goto failed
%SQL_CMD% -b -d %AFSCME_DB_NAME% -i C:\AFSCME_Migration\DM_Script_Files\sp_migrateOrganization.sql
if errorlevel 1 goto failed
%SQL_CMD% -b -d %AFSCME_DB_NAME% -i C:\AFSCME_Migration\DM_Script_Files\sp_prepPersonData.sql
if errorlevel 1 goto failed
%SQL_CMD% -b -d %AFSCME_DB_NAME% -i C:\AFSCME_Migration\DM_Script_Files\sp_migratePerson.sql
if errorlevel 1 goto failed
%SQL_CMD% -b -d %AFSCME_DB_NAME% -i C:\AFSCME_Migration\DM_Script_Files\sp_migrateMember.sql
if errorlevel 1 goto failed
%SQL_CMD% -b -d %AFSCME_DB_NAME% -i C:\AFSCME_Migration\DM_Script_Files\sp_migrateOfficer.sql
if errorlevel 1 goto failed
%SQL_CMD% -b -d %AFSCME_DB_NAME% -i C:\AFSCME_Migration\DM_Script_Files\sp_migrateDMAL.sql
if errorlevel 1 goto failed
%SQL_CMD% -b -d %AFSCME_DB_NAME% -i C:\AFSCME_Migration\DM_Script_Files\sp_errorOfficer.sql
if errorlevel 1 goto failed
%SQL_CMD% -b -d %AFSCME_DB_NAME% -i C:\AFSCME_Migration\DM_Script_Files\sp_migrateRebate.sql
if errorlevel 1 goto failed
%SQL_CMD% -b -d %AFSCME_DB_NAME% -i C:\AFSCME_Migration\DM_Script_Files\sp_migrateOfficerGroups.sql
if errorlevel 1 goto failed

rem %SQL_CMD% -b -d %AFSCME_DB_NAME% -i insertTestData_Users.sql
rem if errorlevel 1 goto failed

goto done

:failed
echo Migration Install FAILED
ENDLOCAL
exit /b 1

:done
echo Migration Install Complete
ENDLOCAL

