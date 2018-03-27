
-------------------------------------------------------------------------
---
--- INSERT TEST DATA FOR UPDATE
---
-------------------------------------------------------------------------


DECLARE @org_pk int, @person_pk int

-- insert a Local 646 in Hawaii
INSERT INTO org_parent values(24001)
SET @org_pk = @@identity

INSERT INTO Aff_Organizations
(old_aff_unit_cd_legacy, old_aff_no_other, aff_abbreviated_nm, parent_aff_fk, aff_type, aff_localSubChapter, aff_stateNat_type, aff_subUnit, aff_councilRetiree_chap, aff_web_url          , mbr_allow_edit_fg, mbr_allow_view_fg, lst_mod_dt, lst_mod_user_pk, created_user_pk, created_dt, auto_eboard_aff_title, aff_afscme_region, aff_long_nm , aff_mult_employers_fg, aff_afscme_leg_district, aff_sub_locals_allowed_fg, aff_multiple_offices_fg, mbr_yearly_card_run_done_fg, mbr_renewal, aff_ann_mbr_card_run_group, aff_code, aff_status, aff_pk , auto_eboard_parent_title, new_aff_id_src, aff_subUnit_crs)
VALUES
(NULL                  , NULL            , 'Hawaii Local 646', NULL         , 'L'     , '646'              , 'HI'             , ''        , ''                    , 'http://local646.com', 1                , 1                , getdate() , 10000001       , 10000001       , getdate() , null                 , null             , null        , 1                    , 21018                  , 1                        , 1                      , 1                          , null       , 25001                     , 'A'     , 17002     , @org_pk, NULL                    , NULL          , NULL           )

INSERT INTO Aff_Charter 
(aff_pk , charter_nm                     , charter_juris                          , charter_dt, charter_lst_chg_eff_dt, charter_cd, lst_mod_user_pk, lst_mod_dt, created_dt, created_user_pk) 
VALUES	
(@org_pk, 'Hawaii Local 646 Charter Name', 'Hawaii Local 646 Charter Jurisdiction', getDate() , getDate()             , 18001     , 10000001       , getDate() , getDate() , 10000001)

INSERT INTO Aff_Charter_County 
(aff_pk , county_nm) 
VALUES 
(@org_pk, 'Maui')

INSERT INTO Aff_Constitution 
(aff_pk , aff_agreement_dt, most_current_approval_dt, approved_const_fg, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk) 
VALUES	
(@org_pk, getDate()       , getDate()               , 1                , getDate() , 10000001       , getDate() , 10000001)

INSERT INTO Aff_Fin_Info (aff_pk)
VALUES (@org_pk)

INSERT INTO Aff_Mbr_Rpt_Info
(aff_pk , created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk)
VALUES
(@org_pk, getDate() , 10000001       , getDate() , 10000001)

INSERT INTO Person
(prefix_nm, mbr_expelled_dt, first_nm, last_nm, mbr_barred_fg, middle_nm, suffix_nm, nick_nm, lst_mod_dt, alternate_mailing_nm, ssn        , created_dt, created_user_pk, valid_ssn_fg, duplicate_ssn_fg, lst_mod_user_pk, marked_for_deletion_fg, member_fg)
VALUES
(NULL     , NULL           ,'Carl'   , 'Aaron', 0            , NULL     , NULL     , NULL   , getDate() , NULL                , '576083897', getDate() , 10000001       , 1           , 0               , 10000001       , 0                     , 1        )

SET @person_pk = @@identity

INSERT INTO Aff_Members
(mbr_no_old_afscme, aff_pk , mbr_join_dt, mbr_retired_dt, no_cards_fg, no_mail_fg, no_legislative_mail_fg, no_public_emp_fg, lst_mod_dt, mbr_card_sent_dt, lst_mod_user_pk, created_dt, created_user_pk, mbr_status, mbr_dues_rate, mbr_no_local, mbr_ret_dues_renewal_fg, mbr_type, mbr_dues_type, mbr_dues_frequency, lost_time_language_fg, primary_information_source, person_pk )   
VALUES
(NULL             , @org_pk, NULL       , NULL          , NULL       , NULL      , NULL                  , NULL            , getDate() , NULL            , 10000001       , getdate() , 10000001       , 31001     , NULL         , NULL        , NULL                   , 29001   , NULL         , NULL              , NULL                 , NULL                      , @person_pk)

INSERT INTO Person_Email
(person_pk , person_email_addr, lst_mod_dt, email_bad_fg, email_type, created_dt, created_user_pk, email_marked_bad_dt, lst_mod_user_pk)
VALUES
(@person_pk, ''               , getDate() , 0           , 71001     , getDate() , 10000001       , null               , 10000001       )

INSERT INTO Person_Email
(person_pk , person_email_addr, lst_mod_dt, email_bad_fg, email_type, created_dt, created_user_pk, email_marked_bad_dt, lst_mod_user_pk)
VALUES
(@person_pk, ''               , getDate() , 0           , 71002     , getDate() , 10000001       , null               , 10000001       )

INSERT INTO Person_Demographics
(person_pk, lst_mod_user_pk, lst_mod_dt, created_dt, created_user_pk)
VALUES
(@person_pk, 10000001, getDate(), getDate(), 10000001)

INSERT INTO Person_Political_Legislative
(person_pk, lst_mod_user_pk, lst_mod_dt, created_dt, created_user_pk)
VALUES
(@person_pk, 10000001, getDate(), getDate(), 10000001)

