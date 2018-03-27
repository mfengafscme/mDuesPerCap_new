DECLARE @org_pk int, @new_aff_pk int, @council_pk int, @parent_pk int

-- insert a Council
INSERT INTO org_parent values(24001)
SET @org_pk = @@identity

INSERT INTO Aff_Organizations
(old_aff_unit_cd_legacy, old_aff_no_other, aff_abbreviated_nm , parent_aff_fk, aff_type, aff_localSubChapter, aff_stateNat_type, aff_subUnit, aff_councilRetiree_chap, aff_web_url             , mbr_allow_edit_fg, mbr_allow_view_fg, lst_mod_dt, lst_mod_user_pk, created_user_pk, created_dt, auto_eboard_aff_title, aff_afscme_region, aff_long_nm , aff_mult_employers_fg, aff_afscme_leg_district, aff_sub_locals_allowed_fg, aff_multiple_offices_fg, mbr_yearly_card_run_done_fg, mbr_renewal, aff_ann_mbr_card_run_group, aff_code, aff_status, aff_pk , auto_eboard_parent_title, new_aff_id_src, aff_subUnit_crs)
VALUES
(NULL                  , NULL            , 'Test Council 1000', NULL         , 'C'     , ''                , 'VA'         ,     ''        , '1000'                 , 'http://council1000.com', 1                , 1                , getdate() , 10000001       , 10000001       , getdate() , null                 , null             , null        , 1                    , 21018                  , 1                        , 1                      , 1                          , null       , 25001                     , 'A'     , 17002     , @org_pk, NULL                    , NULL          , null           )

INSERT INTO Aff_Charter 
(aff_pk , charter_nm                      , charter_juris                           , charter_dt, charter_lst_chg_eff_dt, charter_cd, lst_mod_user_pk, lst_mod_dt, created_dt, created_user_pk) 
VALUES	
(@org_pk, 'Test Council 1000 Charter Name', 'Test Council 1000 Charter Jurisdiction', getDate() , getDate()             , 18001     , 10000001       , getDate() , getDate() , 10000001)

INSERT INTO Aff_Charter_County 
(aff_pk , county_nm) 
VALUES 
(@org_pk, 'Fairfax')

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

-- insert a Local and Sub Local for above Council
SET @council_pk = @org_pk

-- insert a new Local
INSERT INTO org_parent values(24001)
SET @org_pk = @@identity

INSERT INTO Aff_Organizations
(old_aff_unit_cd_legacy, old_aff_no_other, aff_abbreviated_nm    , parent_aff_fk, aff_type, aff_localSubChapter, aff_stateNat_type, aff_subUnit, aff_councilRetiree_chap, aff_web_url     , mbr_allow_edit_fg, mbr_allow_view_fg, lst_mod_dt, lst_mod_user_pk, created_user_pk, created_dt, auto_eboard_aff_title, aff_afscme_region, aff_long_nm , aff_mult_employers_fg, aff_afscme_leg_district, aff_sub_locals_allowed_fg, aff_multiple_offices_fg, mbr_yearly_card_run_done_fg, mbr_renewal, aff_ann_mbr_card_run_group, aff_code, aff_status, aff_pk , auto_eboard_parent_title, new_aff_id_src, aff_subUnit_crs)
VALUES
(''                    , NULL            , 'Local 100 of Cn 1000', @council_pk  , 'L'     , '100'              , 'VA'         ,     ''        , '1000'                 , 'http://local100.com', 0                , 0                , getdate() , 10000001       , 10000001       , getdate() , null                 , null             , null        , 1                    , 21018                  , 1                        , 1                      , 1                          , null       , 25001                     , 'A'     , 17002     , @org_pk, NULL                    , NULL          , null           )

INSERT INTO Aff_Charter 
(aff_pk , charter_nm                         , charter_juris                              , charter_dt, charter_lst_chg_eff_dt, charter_cd, lst_mod_user_pk, lst_mod_dt, created_dt, created_user_pk) 
VALUES	
(@org_pk, 'Local 100 of Cn 1000 Charter Name', 'Local 100 of Cn 1000 Charter Jurisdiction', getDate() , getDate()             , 18001     , 10000001       , getDate() , getDate() , 10000001)

INSERT INTO Aff_Charter_County 
(aff_pk , county_nm) 
VALUES 
(@org_pk, 'Fairfax')

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

-- insert a Sub Local
SET @parent_pk = @org_pk

INSERT INTO org_parent values(24001)
SET @org_pk = @@identity

INSERT INTO Aff_Organizations
(old_aff_unit_cd_legacy, old_aff_no_other, aff_abbreviated_nm         , parent_aff_fk, aff_type, aff_localSubChapter, aff_stateNat_type, aff_subUnit, aff_councilRetiree_chap, aff_web_url                  , mbr_allow_edit_fg, mbr_allow_view_fg, lst_mod_dt, lst_mod_user_pk, created_user_pk, created_dt, auto_eboard_aff_title, aff_afscme_region, aff_long_nm , aff_mult_employers_fg, aff_afscme_leg_district, aff_sub_locals_allowed_fg, aff_multiple_offices_fg, mbr_yearly_card_run_done_fg, mbr_renewal, aff_ann_mbr_card_run_group, aff_code, aff_status, aff_pk , auto_eboard_parent_title, new_aff_id_src, aff_subUnit_crs)
VALUES
(''                    , NULL            , 'Sub Local 1 of L100 C1000', @parent_pk   , 'U'     , '100'              , 'VA'         ,     '1'        , '1000'                 , 'http://subloc1-local100.com', 0                , 0                , getdate() , 10000001       , 10000001       , getdate() , null                 , null             , null        , 1                    , 21018                  , 1                        , 1                      , 1                          , null       , 25001                     , 'A'     , 17002     , @org_pk, NULL                    , NULL          , null           )

INSERT INTO Aff_Charter 
(aff_pk , charter_nm                              , charter_juris                                   , charter_dt, charter_lst_chg_eff_dt, charter_cd, lst_mod_user_pk, lst_mod_dt, created_dt, created_user_pk) 
VALUES	
(@org_pk, 'Sub Local 1 of L100 C1000 Charter Name', 'Sub Local 1 of L100 C1000 Charter Jurisdiction', getDate() , getDate()             , 18001     , 10000001       , getDate() , getDate() , 10000001)

INSERT INTO Aff_Charter_County 
(aff_pk , county_nm) 
VALUES 
(@org_pk, 'Fairfax')

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

-- insert a new affiliate
INSERT INTO org_parent values(24001)
SET @org_pk = @@identity
SET @new_aff_pk = @org_pk

INSERT INTO Aff_Organizations
(old_aff_unit_cd_legacy, old_aff_no_other, aff_abbreviated_nm, parent_aff_fk, aff_type, aff_localSubChapter, aff_stateNat_type, aff_subUnit, aff_councilRetiree_chap, aff_web_url     , mbr_allow_edit_fg, mbr_allow_view_fg, lst_mod_dt, lst_mod_user_pk, created_user_pk, created_dt, auto_eboard_aff_title, aff_afscme_region, aff_long_nm , aff_mult_employers_fg, aff_afscme_leg_district, aff_sub_locals_allowed_fg, aff_multiple_offices_fg, mbr_yearly_card_run_done_fg, mbr_renewal, aff_ann_mbr_card_run_group, aff_code, aff_status, aff_pk , auto_eboard_parent_title, new_aff_id_src, aff_subUnit_crs)
VALUES
(''                    , NULL            , 'Test Local 1000' , @council_pk  , 'L'     , '1000'             , 'VA'         ,     ''        , '1000'                 , 'http://a.b.com', 0                , 0                , getdate() , 10000001       , 10000001       , getdate() , null                 , null             , null        , 1                    , 21018                  , 1                        , 1                      , 1                          , null       , 25001                     , 'A'     , 17002     , @org_pk, NULL                    , NULL          , null           )

INSERT INTO Aff_Charter 
(aff_pk , charter_nm                    , charter_juris                         , charter_dt, charter_lst_chg_eff_dt, charter_cd, lst_mod_user_pk, lst_mod_dt, created_dt, created_user_pk) 
VALUES	
(@org_pk, 'Test Local 1000 Charter Name', 'Test Local 1000 Charter Jurisdiction', getDate() , getDate()             , 18001     , 10000001       , getDate() , getDate() , 10000001)

INSERT INTO Aff_Charter_County 
(aff_pk , county_nm) 
VALUES 
(@org_pk, 'Fairfax')

INSERT INTO Aff_Constitution 
(aff_pk , aff_agreement_dt, most_current_approval_dt, approved_const_fg, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk) 
VALUES	
(@org_pk, getDate()       , getDate()               , 1                , getDate() , 10000001       , getDate() , 10000001)

INSERT INTO Aff_Comments
(aff_pk , comment_txt     , comment_dt, created_user_pk)
VALUES
(@org_pk, 'SAMPLE COMMENT', GETDATE() , 10000001)

INSERT INTO Aff_Employer_Sector 
(aff_pk , aff_employer_sector)
VALUES
(@org_pk, 20001) 

INSERT INTO Aff_Employer_Sector 
(aff_pk , aff_employer_sector)
VALUES
(@org_pk, 20002) 

INSERT INTO Aff_Comments
(aff_pk , comment_txt               , comment_dt, created_user_pk)
VALUES
(@org_pk, 'SAMPLE COMMENT2 ON SCREEN', GETDATE() , 10000001)

INSERT INTO Aff_Fin_Info (aff_pk)
VALUES (@org_pk)

INSERT INTO Aff_Mbr_Rpt_Info
(aff_pk , created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk)
VALUES
(@org_pk, getDate() , 10000001       , getDate() , 10000001)

-- insert a new affiliate with a merged status indicating that the members have been moved to the Local above. 
INSERT INTO org_parent values(24001)
SET @org_pk = @@identity

INSERT INTO Aff_Organizations
(old_aff_unit_cd_legacy, old_aff_no_other, aff_abbreviated_nm       , parent_aff_fk, aff_type, aff_localSubChapter, aff_stateNat_type, aff_subUnit, aff_councilRetiree_chap, aff_web_url     , mbr_allow_edit_fg, mbr_allow_view_fg, lst_mod_dt, lst_mod_user_pk, created_user_pk, created_dt, auto_eboard_aff_title, aff_afscme_region, aff_long_nm , aff_mult_employers_fg, aff_afscme_leg_district, aff_sub_locals_allowed_fg, aff_multiple_offices_fg, mbr_yearly_card_run_done_fg, mbr_renewal, aff_ann_mbr_card_run_group, aff_code, aff_status, aff_pk , auto_eboard_parent_title, new_aff_id_src, aff_subUnit_crs)
VALUES
(''                    , NULL            , 'Test Merged Local 2000' , @council_pk  , 'L'     , '2000'             , 'VA'             ,     ''    , '1000'                 , 'http://a.b.com', 0                , 0                , getdate() , 10000001       , 10000001       , getdate() , null                 , null             , null        , 1                    , 21018                  , 1                        , 1                      , 1                          , null       , 25001                     , 'B'     , 17006     , @org_pk, NULL                    , @new_aff_pk   , null           )	

INSERT INTO Aff_Charter 
(aff_pk , charter_nm                            , charter_juris                                , charter_dt, charter_lst_chg_eff_dt, charter_cd, lst_mod_user_pk, lst_mod_dt, created_dt, created_user_pk) 
VALUES	
(@org_pk, 'TTest Merged Local 2000 Charter Name', 'Test Merged Local 2000 Charter Jurisdiction', getDate() , getDate()             , 18001     , 10000001       , getDate() , getDate() , 10000001)

INSERT INTO Aff_Charter_County 
(aff_pk , county_nm) 
VALUES 
(@org_pk, 'Fairfax')

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

-- insert an Admin Council 
INSERT INTO org_parent values(24001)
SET @org_pk = @@identity

INSERT INTO Aff_Organizations
(old_aff_unit_cd_legacy, old_aff_no_other, aff_abbreviated_nm       , parent_aff_fk, aff_type, aff_localSubChapter, aff_stateNat_type, aff_subUnit, aff_councilRetiree_chap, aff_web_url               , mbr_allow_edit_fg, mbr_allow_view_fg, lst_mod_dt, lst_mod_user_pk, created_user_pk, created_dt, auto_eboard_aff_title, aff_afscme_region, aff_long_nm , aff_mult_employers_fg, aff_afscme_leg_district, aff_sub_locals_allowed_fg, aff_multiple_offices_fg, mbr_yearly_card_run_done_fg, mbr_renewal, aff_ann_mbr_card_run_group, aff_code, aff_status, aff_pk , auto_eboard_parent_title, new_aff_id_src, aff_subUnit_crs)
VALUES
(''                    , NULL            , 'Admin/Legis Council 1'  , null         , 'C'     , ''                , 'VA'             , ''       , '1'                     , 'http://admincouncil1.com', 0                , 0                , getdate() , 10000001       , 10000001       , getdate() , null                 , null             , null        , 1                    , 21018                  , 1                        , 1                      , 1                          , null       , 25001                     , 'A'     , 17001     , @org_pk, NULL                    , null          , null           )	

INSERT INTO Aff_Charter 
(aff_pk , charter_nm                            , charter_juris                                , charter_dt, charter_lst_chg_eff_dt, charter_cd, lst_mod_user_pk, lst_mod_dt, created_dt, created_user_pk) 
VALUES	
(@org_pk, 'Admin/Legis Council 1 Charter Name'  , 'Admin/Legis Council 1 Charter Jurisdiction' , getDate() , getDate()             , 18001     , 10000001       , getDate() , getDate() , 10000001)

INSERT INTO Aff_Charter_County 
(aff_pk , county_nm) 
VALUES 
(@org_pk, 'Fairfax')

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
