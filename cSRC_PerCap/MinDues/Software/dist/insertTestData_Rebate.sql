-------------------------------------------
---
--- INSERT 12 MONTH REBATE AMOUNT RECORDS
---
-------------------------------------------
INSERT INTO PRB_12_Month_Rebate (rbt_year, rbt_pct, rbt_full_time_amt, rbt_part_time_amt, rbt_lower_part_time_amt, rbt_retiree_amt, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk)
VALUES (2000, 2, 8.45, 6.23, 4.84, 3.95, getdate(), 10000001, getdate(), 10000001)

INSERT INTO PRB_12_Month_Rebate (rbt_year, rbt_pct, rbt_full_time_amt, rbt_part_time_amt, rbt_lower_part_time_amt, rbt_retiree_amt, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk)
VALUES (2001, 1, 3.75, 2.50, 2.03, 1.75, getdate(), 10000001, getdate(), 10000001)

INSERT INTO PRB_12_Month_Rebate (rbt_year, rbt_pct, rbt_full_time_amt, rbt_part_time_amt, rbt_lower_part_time_amt, rbt_retiree_amt, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk)
VALUES (2002, 1, 3.75, 2.50, 2.03, 1.75, getdate(), 10000001, getdate(), 10000001)


------------------------------------------------------------
---
--- DELETE POLITICAL REBATE RECORDS IF NOT ALREADY DONE SO
---
------------------------------------------------------------
DELETE FROM PRB_Roster_Persons
DELETE FROM PRB_Rbt_Year_Info 
DELETE FROM PRB_Request_Affs
DELETE FROM PRB_Requests 
DELETE FROM PRB_App_Affs
DELETE FROM PRB_Apps 
DELETE FROM PRB_Rebate_Check_Info

-------------------------------------------------------------------------
---
--- INSERT POLITICAL REBATE RECORDS
---
--- Expected outcome:
--- 2002 - Check Issued (at Annual Rebate Info - Person Roster level)
--- 2001 - Denied (at Request level), 
--- 2000 - Denied (at Application level)
--- 1999 - In Progress (at Annual Rebate Info - Person Roster level)
-------------------------------------------------------------------------

---
--- TABLE PRB_Rbt_Year_Info 
---

-- Admin
INSERT INTO PRB_Rbt_Year_Info (person_pk, rbt_year, comment_txt, person_rbt_year_status)
VALUES (10000001, 2000, null, null)

INSERT INTO PRB_Rbt_Year_Info (person_pk, rbt_year, comment_txt, person_rbt_year_status)
VALUES (10000001, 2001, null, null)

INSERT INTO PRB_Rbt_Year_Info (person_pk, rbt_year, comment_txt, person_rbt_year_status)
VALUES (10000001, 2002, null, null)

-- Adam Monroe or whoever with person_pk 10000002
INSERT INTO PRB_Rbt_Year_Info (person_pk, rbt_year, comment_txt, person_rbt_year_status)
VALUES (10000002, 1999, null, null)

INSERT INTO PRB_Rbt_Year_Info (person_pk, rbt_year, comment_txt, person_rbt_year_status)
VALUES (10000002, 2000, null, null)

INSERT INTO PRB_Rbt_Year_Info (person_pk, rbt_year, comment_txt, person_rbt_year_status)
VALUES (10000002, 2001, null, null)

INSERT INTO PRB_Rbt_Year_Info (person_pk, rbt_year, comment_txt, person_rbt_year_status)
VALUES (10000002, 2002, null, null)


---
--- TABLE PRB_Apps 
---
SET IDENTITY_INSERT PRB_Apps ON
INSERT INTO PRB_Apps (app_mailed_dt, prb_app_pk, app_returned_dt, comment_txt, prb_comment_anal_cd, prb_evaluation_cd, app_status, lst_mod_dt, lst_mod_user_pk, created_dt, created_user_pk, aff_roster_generated_fg)
VALUES (getdate(), 1, null, null, null, null, 75001, getdate(), 10000001, getdate(), 10000001, 0)

INSERT INTO PRB_Apps (app_mailed_dt, prb_app_pk, app_returned_dt, comment_txt, prb_comment_anal_cd, prb_evaluation_cd, app_status, lst_mod_dt, lst_mod_user_pk, created_dt, created_user_pk, aff_roster_generated_fg)
VALUES (getdate(), 2, null, null, null, null, 75001, getdate(), 10000001, getdate(), 10000001, 0)

INSERT INTO PRB_Apps (app_mailed_dt, prb_app_pk, app_returned_dt, comment_txt, prb_comment_anal_cd, prb_evaluation_cd, app_status, lst_mod_dt, lst_mod_user_pk, created_dt, created_user_pk, aff_roster_generated_fg)
VALUES (getdate(), 3, null, null, null, null, 75002, getdate(), 10000001, getdate(), 10000001, 0)

INSERT INTO PRB_Apps (app_mailed_dt, prb_app_pk, app_returned_dt, comment_txt, prb_comment_anal_cd, prb_evaluation_cd, app_status, lst_mod_dt, lst_mod_user_pk, created_dt, created_user_pk, aff_roster_generated_fg)
VALUES (getdate(), 4, null, null, null, null, 75001, getdate(), 10000001, getdate(), 10000001, 0)

INSERT INTO PRB_Apps (app_mailed_dt, prb_app_pk, app_returned_dt, comment_txt, prb_comment_anal_cd, prb_evaluation_cd, app_status, lst_mod_dt, lst_mod_user_pk, created_dt, created_user_pk, aff_roster_generated_fg)
VALUES (getdate(), 5, null, null, null, null, 75001, getdate(), 10000001, getdate(), 10000001, 0)

INSERT INTO PRB_Apps (app_mailed_dt, prb_app_pk, app_returned_dt, comment_txt, prb_comment_anal_cd, prb_evaluation_cd, app_status, lst_mod_dt, lst_mod_user_pk, created_dt, created_user_pk, aff_roster_generated_fg)
VALUES (getdate(), 6, null, null, null, null, 75001, getdate(), 10000001, getdate(), 10000001, 0)
SET IDENTITY_INSERT PRB_Apps OFF

---
--- TABLE PRB_App_Affs
---
INSERT INTO PRB_App_Affs(aff_pk, prb_app_pk, app_filed_with, app_duration_in_aff, app_months_in_aff)
VALUES (1, 1, null, null, 0);

INSERT INTO PRB_App_Affs(aff_pk, prb_app_pk, app_filed_with, app_duration_in_aff, app_months_in_aff)
VALUES (1, 2, null, null, 0);

INSERT INTO PRB_App_Affs(aff_pk, prb_app_pk, app_filed_with, app_duration_in_aff, app_months_in_aff)
VALUES (2, 2, null, null, 0);

INSERT INTO PRB_App_Affs(aff_pk, prb_app_pk, app_filed_with, app_duration_in_aff, app_months_in_aff)
VALUES (3, 2, null, null, 0);

INSERT INTO PRB_App_Affs(aff_pk, prb_app_pk, app_filed_with, app_duration_in_aff, app_months_in_aff)
VALUES (6, 3, null, null, 0);

INSERT INTO PRB_App_Affs(aff_pk, prb_app_pk, app_filed_with, app_duration_in_aff, app_months_in_aff)
VALUES (7, 3, null, null, 0);

INSERT INTO PRB_App_Affs(aff_pk, prb_app_pk, app_filed_with, app_duration_in_aff, app_months_in_aff)
VALUES (4, 4, null, null, 0);

INSERT INTO PRB_App_Affs(aff_pk, prb_app_pk, app_filed_with, app_duration_in_aff, app_months_in_aff)
VALUES (5, 4, null, null, 0);

INSERT INTO PRB_App_Affs(aff_pk, prb_app_pk, app_filed_with, app_duration_in_aff, app_months_in_aff)
VALUES (1, 5, null, null, 0);

INSERT INTO PRB_App_Affs(aff_pk, prb_app_pk, app_filed_with, app_duration_in_aff, app_months_in_aff)
VALUES (3, 6, null, null, 0);

INSERT INTO PRB_App_Affs(aff_pk, prb_app_pk, app_filed_with, app_duration_in_aff, app_months_in_aff)
VALUES (4, 6, null, null, 0);

---
--- TABLE PRB_Requests 
---
SET IDENTITY_INSERT PRB_Requests ON

-- Admin
INSERT INTO PRB_Requests (rqst_dt, rqst_rebate_year, rqst_cert_mail_num, rqst_denied_fg, rqst_denied_dt, rqst_denied_reason, rqst_keyed_dt, comment_txt, lst_mod_dt, lst_mod_user_pk, created_dt, created_user_pk, prb_app_pk, rqst_resubmit_fg, rqst_resubmit_denied_dt, rqst_resubmit_denied_reason, rqst_status, rqst_pk, person_pk)
VALUES ('June 2, 2002', 2002, null, 0, getdate(), null, getdate(), null, getdate(), 10000001, getdate(), 10000001, 1, 0, null, null, 75001, 7, 10000001)

INSERT INTO PRB_Requests (rqst_dt, rqst_rebate_year, rqst_cert_mail_num, rqst_denied_fg, rqst_denied_dt, rqst_denied_reason, rqst_keyed_dt, comment_txt, lst_mod_dt, lst_mod_user_pk, created_dt, created_user_pk, prb_app_pk, rqst_resubmit_fg, rqst_resubmit_denied_dt, rqst_resubmit_denied_reason, rqst_status, rqst_pk, person_pk)
VALUES ('June 1, 2001', 2001, null, 0, getdate(), null, getdate(), null, getdate(), 10000001, getdate(), 10000001, 2, 0, null, null, 75002, 8, 10000001)

INSERT INTO PRB_Requests (rqst_dt, rqst_rebate_year, rqst_cert_mail_num, rqst_denied_fg, rqst_denied_dt, rqst_denied_reason, rqst_keyed_dt, comment_txt, lst_mod_dt, lst_mod_user_pk, created_dt, created_user_pk, prb_app_pk, rqst_resubmit_fg, rqst_resubmit_denied_dt, rqst_resubmit_denied_reason, rqst_status, rqst_pk, person_pk)
VALUES ('June 1, 2000', 2000, null, 1, getdate(), null, getdate(), null, getdate(), 10000001, getdate(), 10000001, 3, 0, null, null, 75002, 9, 10000001)

INSERT INTO PRB_Requests (rqst_dt, rqst_rebate_year, rqst_cert_mail_num, rqst_denied_fg, rqst_denied_dt, rqst_denied_reason, rqst_keyed_dt, comment_txt, lst_mod_dt, lst_mod_user_pk, created_dt, created_user_pk, prb_app_pk, rqst_resubmit_fg, rqst_resubmit_denied_dt, rqst_resubmit_denied_reason, rqst_status, rqst_pk, person_pk)
VALUES ('June 2, 2000', 2000, null, 1, getdate(), null, getdate(), null, getdate(), 10000001, getdate(), 10000001, 6, 0, null, null, 75001, 10, 10000001)

-- Adam Monroe or whoever with person_pk 10000002
INSERT INTO PRB_Requests (rqst_dt, rqst_rebate_year, rqst_cert_mail_num, rqst_denied_fg, rqst_denied_dt, rqst_denied_reason, rqst_keyed_dt, comment_txt, lst_mod_dt, lst_mod_user_pk, created_dt, created_user_pk, prb_app_pk, rqst_resubmit_fg, rqst_resubmit_denied_dt, rqst_resubmit_denied_reason, rqst_status, rqst_pk, person_pk)
VALUES ('June 1, 2000', 2000, null, 1, getdate(), null, getdate(), null, getdate(), 10000001, getdate(), 10000001, 1, 0, null, null, 75002, 2, 10000002)

INSERT INTO PRB_Requests (rqst_dt, rqst_rebate_year, rqst_cert_mail_num, rqst_denied_fg, rqst_denied_dt, rqst_denied_reason, rqst_keyed_dt, comment_txt, lst_mod_dt, lst_mod_user_pk, created_dt, created_user_pk, prb_app_pk, rqst_resubmit_fg, rqst_resubmit_denied_dt, rqst_resubmit_denied_reason, rqst_status, rqst_pk, person_pk)
VALUES ('June 1, 1999', 1999, null, 1, getdate(), null, getdate(), null, getdate(), 10000001, getdate(), 10000001, 2, 0, null, null, 75003, 3, 10000002)

INSERT INTO PRB_Requests (rqst_dt, rqst_rebate_year, rqst_cert_mail_num, rqst_denied_fg, rqst_denied_dt, rqst_denied_reason, rqst_keyed_dt, comment_txt, lst_mod_dt, lst_mod_user_pk, created_dt, created_user_pk, prb_app_pk, rqst_resubmit_fg, rqst_resubmit_denied_dt, rqst_resubmit_denied_reason, rqst_status, rqst_pk, person_pk)
VALUES ('June 2, 2000', 2000, null, 1, getdate(), null, getdate(), null, getdate(), 10000001, getdate(), 10000001, 3, 0, null, null, 75001, 4, 10000002)

INSERT INTO PRB_Requests (rqst_dt, rqst_rebate_year, rqst_cert_mail_num, rqst_denied_fg, rqst_denied_dt, rqst_denied_reason, rqst_keyed_dt, comment_txt, lst_mod_dt, lst_mod_user_pk, created_dt, created_user_pk, prb_app_pk, rqst_resubmit_fg, rqst_resubmit_denied_dt, rqst_resubmit_denied_reason, rqst_status, rqst_pk, person_pk)
VALUES ('June 1, 2001', 2001, null, 0, getdate(), null, getdate(), null, getdate(), 10000001, getdate(), 10000001, 4, 0, null, null, 75002, 5, 10000002)

INSERT INTO PRB_Requests (rqst_dt, rqst_rebate_year, rqst_cert_mail_num, rqst_denied_fg, rqst_denied_dt, rqst_denied_reason, rqst_keyed_dt, comment_txt, lst_mod_dt, lst_mod_user_pk, created_dt, created_user_pk, prb_app_pk, rqst_resubmit_fg, rqst_resubmit_denied_dt, rqst_resubmit_denied_reason, rqst_status, rqst_pk, person_pk)
VALUES ('June 2, 2002', 2002, null, 0, getdate(), null, getdate(), null, getdate(), 10000001, getdate(), 10000001, 5, 0, null, null, 75001, 6, 10000002)

SET IDENTITY_INSERT PRB_Requests OFF


---
--- TABLE PRB_Request_Affs
---
INSERT INTO PRB_Request_Affs(aff_pk, rqst_filed_with, rqst_duration_in_aff, rqst_months_in_affiliate, rqst_pk)
VALUES (3, null, null, null, 2);

INSERT INTO PRB_Request_Affs(aff_pk, rqst_filed_with, rqst_duration_in_aff, rqst_months_in_affiliate, rqst_pk)
VALUES (1, null, null, null, 2);

INSERT INTO PRB_Request_Affs(aff_pk, rqst_filed_with, rqst_duration_in_aff, rqst_months_in_affiliate, rqst_pk)
VALUES (2, null, null, null, 2);

INSERT INTO PRB_Request_Affs(aff_pk, rqst_filed_with, rqst_duration_in_aff, rqst_months_in_affiliate, rqst_pk)
VALUES (1, null, null, null, 3);

INSERT INTO PRB_Request_Affs(aff_pk, rqst_filed_with, rqst_duration_in_aff, rqst_months_in_affiliate, rqst_pk)
VALUES (2, null, null, null, 3);

INSERT INTO PRB_Request_Affs(aff_pk, rqst_filed_with, rqst_duration_in_aff, rqst_months_in_affiliate, rqst_pk)
VALUES (1, null, null, null, 4);

INSERT INTO PRB_Request_Affs(aff_pk, rqst_filed_with, rqst_duration_in_aff, rqst_months_in_affiliate, rqst_pk)
VALUES (2, null, null, null, 4);

INSERT INTO PRB_Request_Affs(aff_pk, rqst_filed_with, rqst_duration_in_aff, rqst_months_in_affiliate, rqst_pk)
VALUES (3, null, null, null, 5);

INSERT INTO PRB_Request_Affs(aff_pk, rqst_filed_with, rqst_duration_in_aff, rqst_months_in_affiliate, rqst_pk)
VALUES (4, null, null, null, 5);

INSERT INTO PRB_Request_Affs(aff_pk, rqst_filed_with, rqst_duration_in_aff, rqst_months_in_affiliate, rqst_pk)
VALUES (1, null, null, null, 6);

INSERT INTO PRB_Request_Affs(aff_pk, rqst_filed_with, rqst_duration_in_aff, rqst_months_in_affiliate, rqst_pk)
VALUES (2, null, null, null, 6);

INSERT INTO PRB_Request_Affs(aff_pk, rqst_filed_with, rqst_duration_in_aff, rqst_months_in_affiliate, rqst_pk)
VALUES (1, null, null, null, 7);

INSERT INTO PRB_Request_Affs(aff_pk, rqst_filed_with, rqst_duration_in_aff, rqst_months_in_affiliate, rqst_pk)
VALUES (2, null, null, null, 7);

INSERT INTO PRB_Request_Affs(aff_pk, rqst_filed_with, rqst_duration_in_aff, rqst_months_in_affiliate, rqst_pk)
VALUES (1, null, null, null, 8);

INSERT INTO PRB_Request_Affs(aff_pk, rqst_filed_with, rqst_duration_in_aff, rqst_months_in_affiliate, rqst_pk)
VALUES (2, null, null, null, 9);

INSERT INTO PRB_Request_Affs(aff_pk, rqst_filed_with, rqst_duration_in_aff, rqst_months_in_affiliate, rqst_pk)
VALUES (6, null, null, null, 10);

INSERT INTO PRB_Request_Affs(aff_pk, rqst_filed_with, rqst_duration_in_aff, rqst_months_in_affiliate, rqst_pk)
VALUES (7, null, null, null, 10);



--
--- TABLE PRB_Rebate_Check_Info
---

-- Admin
INSERT INTO PRB_Rebate_Check_Info (person_pk, rbt_year, lst_mod_dt, lst_mod_user_pk, created_dt, created_user_pk, rbt_check_nbr_2, rbt_check_nbr_1)
VALUES (10000001, 2002, getdate(), 10000001, getdate(), 10000001, 123, 456)

INSERT INTO PRB_Rebate_Check_Info (person_pk, rbt_year, lst_mod_dt, lst_mod_user_pk, created_dt, created_user_pk, rbt_check_nbr_2, rbt_check_nbr_1)
VALUES (10000001, 2001, getdate(), 10000001, getdate(), 10000001, null, null)

-- Adam Monroe or whoever with person_pk 10000002
INSERT INTO PRB_Rebate_Check_Info (person_pk, rbt_year, lst_mod_dt, lst_mod_user_pk, created_dt, created_user_pk, rbt_check_nbr_2, rbt_check_nbr_1)
VALUES (10000002, 2002, getdate(), 10000001, getdate(), 10000001, 123, 456)

INSERT INTO PRB_Rebate_Check_Info (person_pk, rbt_year, lst_mod_dt, lst_mod_user_pk, created_dt, created_user_pk, rbt_check_nbr_2, rbt_check_nbr_1)
VALUES (10000002, 2001, getdate(), 10000001, getdate(), 10000001, null, null)

INSERT INTO PRB_Rebate_Check_Info (person_pk, rbt_year, lst_mod_dt, lst_mod_user_pk, created_dt, created_user_pk, rbt_check_nbr_2, rbt_check_nbr_1)
VALUES (10000002, 1999, getdate(), 10000001, getdate(), 10000001, null, null)


---
--- TABLE PRB_Roster_Persons
---

-- Admin
INSERT INTO PRB_Roster_Persons (aff_pk, person_pk, roster_acceptance_cd, roster_aff_status, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk, rbt_year)
VALUES (1, 10000001, 54002, 77001, getdate(), 10000001, getdate(), 10000001, 2000)

INSERT INTO PRB_Roster_Persons (aff_pk, person_pk, roster_acceptance_cd, roster_aff_status, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk, rbt_year)
VALUES (1, 10000001, 54002, 77002, getdate(), 10000001, getdate(), 10000001, 2001)

INSERT INTO PRB_Roster_Persons (aff_pk, person_pk, roster_acceptance_cd, roster_aff_status, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk, rbt_year)
VALUES (1, 10000001, 54003, 77002, getdate(), 10000001, getdate(), 10000001, 2002)

-- Adam Monroe or whoever with person_pk 10000002
INSERT INTO PRB_Roster_Persons (aff_pk, person_pk, roster_acceptance_cd, roster_aff_status, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk, rbt_year)
VALUES (1, 10000002, 54001, 77002, getdate(), 10000001, getdate(), 10000001, 1999)

INSERT INTO PRB_Roster_Persons (aff_pk, person_pk, roster_acceptance_cd, roster_aff_status, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk, rbt_year)
VALUES (1, 10000002, 54002, 77001, getdate(), 10000001, getdate(), 10000001, 2000)

INSERT INTO PRB_Roster_Persons (aff_pk, person_pk, roster_acceptance_cd, roster_aff_status, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk, rbt_year)
VALUES (1, 10000002, 54002, 77002, getdate(), 10000001, getdate(), 10000001, 2001)

INSERT INTO PRB_Roster_Persons (aff_pk, person_pk, roster_acceptance_cd, roster_aff_status, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk, rbt_year)
VALUES (1, 10000002, 54003, 77002, getdate(), 10000001, getdate(), 10000001, 2002)
