
------------------------------------------------------------
---
--- COM_App_Config_Data Table
---
------------------------------------------------------------

------------------------------------------------------------
---
--- INSERT POLITICAL REBATE APPLICATION CONFIG DATA RECORDS
---
------------------------------------------------------------
SET IDENTITY_INSERT COM_App_Config_Data ON
INSERT INTO COM_App_Config_Data (lst_mod_dt, lst_mod_user_pk, created_dt, created_user_pk, prior_value, variable_value, app_var_pk, variable_name)
VALUES (getDate(), 10000001, getDate(), 10000001, '10', '10', 1, 'PRBAppMailedDate')

INSERT INTO COM_App_Config_Data (lst_mod_dt, lst_mod_user_pk, created_dt, created_user_pk, prior_value, variable_value, app_var_pk, variable_name)
VALUES (getDate(), 10000001, getDate(), 10000001, '12/30/2002', '12/31/2003', 2, 'FiscalYearEnd')
SET IDENTITY_INSERT COM_App_Config_Data OFF


------------------------------------------------------------
---
--- INSERT APPLY UPDATE APPLICATION CONFIG DATA RECORDS
---
------------------------------------------------------------

INSERT INTO COM_App_Config_Data (variable_name, variable_value, prior_value, created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
VALUES ('AUPUpdateRecentTimeLimit', '14', '14', 10000001, GETDATE(), 10000001, GETDATE() )


------------------------------------------------------------
---
--- INSERT MASS CHANGE CONFIG DATA RECORDS
---
------------------------------------------------------------

INSERT INTO COM_App_Config_Data (variable_name, variable_value, prior_value, created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
VALUES ('SMTPServerName', 'smtp.grci.com', 'smtp.grci.com', 10000001, GETDATE(), 10000001, GETDATE() )


------------------------------------------------------------
---
--- INSERT APPLICATION CONFIG DATA RECORD FOR NCOA PROCESS
---
------------------------------------------------------------

INSERT INTO COM_App_Config_Data (variable_name, variable_value, prior_value, created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
VALUES ('ConfigTimeframe', '10', '10', 10000001, GETDATE(), 10000001, GETDATE() )

INSERT INTO COM_App_Config_Data (variable_name, variable_value, prior_value, created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
VALUES ('MemberSMAUpdateTimeFrame', '10', '10', 10000001, GETDATE(), 10000001, GETDATE() )


------------------------------------------------------------
---
--- COM_Application_Functions Table
---
------------------------------------------------------------

SET IDENTITY_INSERT COM_Application_Functions ON

INSERT INTO COM_Application_Functions (application_function_pk, application_function_nm)
VALUES (1, 'Apply Update')

SET IDENTITY_INSERT COM_Application_Functions OFF

------------------------------------------------------------
---
--- COM_Application_Notification Table
---
------------------------------------------------------------

--
-- Email for Lana (email_pk = 3541)  -- commented out since delivered for Production too
--
--INSERT INTO COM_Application_Notification (application_function_pk, email_pk, created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
--VALUES (1, 3541, 10000001, GETDATE(), 10000001, GETDATE() )


--
-- Email for Len (email_pk = 3537)  -- commented out for developers (so extra emails are not sent to testers)
--
--INSERT INTO COM_Application_Notification (application_function_pk, email_pk, created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
--VALUES (1, 3537, 10000001, GETDATE(), 10000001, GETDATE() )


--
-- Email for Dave (email_pk = 3531)  -- commented out for developers (so extra emails are not sent to testers)
--
--INSERT INTO COM_Application_Notification (application_function_pk, email_pk, created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
--VALUES (1, 3531, 10000001, GETDATE(), 10000001, GETDATE() )



