
 
 exec sp_addtype Comments_default, "varbinary(150)", "NULL"
go
 
 exec sp_addtype Common_code_key, "int", "NULL"
go
 
 exec sp_addtype fg, "smallint", "NULL"
go
 
 exec sp_addtype nullable_user_pk, "int", "NULL"
go
 
 exec sp_addtype Type, "char(1)", "NOT NULL"
go
 
 exec sp_addtype User_pk, "int", "NOT NULL"
go
 
 CREATE TABLE NCOA_Error_Cds (
        ncoa_error_cd_pk     int NOT NULL,
        ncoa_error_short_desc varchar(50) NOT NULL,
        ncoa_error_long_desc varchar(254) NOT NULL,
        PRIMARY KEY NONCLUSTERED (ncoa_error_cd_pk)
 )
go
 
 
 CREATE TABLE AFSCME_Offices (
        afscme_office_pk     int IDENTITY,
        afscme_title_nm      Common_code_key NOT NULL,
        afscme_title_desc    varchar(150) NULL,
        priority             smallint NOT NULL,
        elected_officer_fg   fg NOT NULL,
        created_user_pk      User_pk,
        created_dt           datetime NOT NULL,
        lst_mod_user_pk      User_pk,
        lst_mod_dt           datetime NOT NULL,
        PRIMARY KEY NONCLUSTERED (afscme_office_pk)
 )
go
 
 
 CREATE TABLE Org_Parent (
        org_pk               int IDENTITY,
        org_subtype          Common_code_key,
        PRIMARY KEY NONCLUSTERED (org_pk)
 )
go
 
 
 CREATE TABLE Aff_Organizations (
        aff_pk               int NOT NULL,
        parent_aff_fk        int NULL,
        aff_type             char(1) NOT NULL,
        aff_localSubChapter  varchar(4) NOT NULL,
        aff_stateNat_type    varchar(2) NOT NULL,
        aff_subUnit          varchar(4) NOT NULL,
        aff_councilRetiree_chap varchar(4) NOT NULL,
        aff_code             char(1) NOT NULL,
        old_aff_unit_cd_legacy char(6) NULL,
        old_aff_no_other     char(6) NULL,
        aff_abbreviated_nm   varchar(29) NOT NULL,
        aff_long_nm          varchar(254) NULL,
        aff_status           Common_code_key NOT NULL,
        aff_afscme_region    Common_code_key,
        aff_mult_employers_fg fg DEFAULT 0,
        aff_afscme_leg_district Common_code_key,
        aff_sub_locals_allowed_fg fg,
        aff_multiple_offices_fg fg,
        mbr_yearly_card_run_done_fg fg,
        mbr_renewal          Common_code_key,
        aff_web_url          varchar(100) NULL,
        aff_ann_mbr_card_run_group Common_code_key,
        auto_eboard_parent_title int NULL,
        auto_eboard_aff_title int NULL,
        mbr_allow_view_fg    fg NOT NULL DEFAULT 0,
        mbr_allow_edit_fg    fg NOT NULL DEFAULT 0,
        aff_subUnit_crs      char(4) NULL,
        created_user_pk      User_pk,
        created_dt           datetime NOT NULL,
        lst_mod_user_pk      User_pk,
        lst_mod_dt           datetime NOT NULL,
        new_aff_id_src       int NULL,
        PRIMARY KEY NONCLUSTERED (aff_pk), 
        FOREIGN KEY (new_aff_id_src)
                              REFERENCES Aff_Organizations, 
        FOREIGN KEY (auto_eboard_aff_title)
                              REFERENCES AFSCME_Offices, 
        FOREIGN KEY (auto_eboard_parent_title)
                              REFERENCES AFSCME_Offices, 
        FOREIGN KEY (aff_pk)
                              REFERENCES Org_Parent
                              ON DELETE CASCADE, 
        FOREIGN KEY (aff_pk)
                              REFERENCES Aff_Organizations,
        UNIQUE (
               aff_type,
               aff_localSubChapter,
               aff_stateNat_type,
               aff_subUnit,
               aff_councilRetiree_chap,
               aff_code
        )
 )
go
 
 
 CREATE TABLE Aff_Officer_Groups (
        aff_pk               int NOT NULL,
        afscme_office_pk     int NOT NULL,
        office_group_id      int NOT NULL,
        affiliate_office_title varchar(30) NULL,
        length_of_term       Common_code_key NOT NULL,
        max_number_in_office smallint NOT NULL,
        month_of_election    Common_code_key NOT NULL,
        current_term_end     smallint NULL,
        delegate_priority    smallint NULL,
        executive_board_fg   fg,
        reporting_officer_fg fg,
        created_user_pk      User_pk,
        created_dt           datetime NULL,
        lst_mod_user_pk      User_pk,
        lst_mod_dt           datetime NOT NULL,
        PRIMARY KEY NONCLUSTERED (aff_pk, afscme_office_pk, 
               office_group_id), 
        FOREIGN KEY (afscme_office_pk)
                              REFERENCES AFSCME_Offices, 
        FOREIGN KEY (aff_pk)
                              REFERENCES Aff_Organizations
 )
go
 
 
 CREATE TABLE Person (
        person_pk            int IDENTITY(10000001,1),
        ssn                  char(9) NULL,
        valid_ssn_fg         fg DEFAULT NULL,
        duplicate_ssn_fg     fg,
        member_fg            fg NOT NULL,
        mbr_expelled_dt      datetime NULL,
        mbr_barred_fg        fg NOT NULL DEFAULT 0,
        alternate_mailing_nm varchar(130) NULL,
        prefix_nm            Common_code_key,
        first_nm             varchar(25) NULL,
        last_nm              varchar(25) NULL,
        middle_nm            varchar(25) NULL,
        suffix_nm            Common_code_key,
        nick_nm              varchar(25) NULL,
        marked_for_deletion_fg fg NOT NULL DEFAULT 0,
        created_user_pk      User_pk,
        created_dt           datetime NOT NULL,
        lst_mod_user_pk      User_pk,
        lst_mod_dt           datetime NOT NULL,
        person_mst_lst_mod_user_pk nullable_user_pk,
        person_mst_lst_mod_dt datetime NULL,
        mbr_mst_lst_mod_user_pk nullable_user_pk,
        mbr_mst_lst_mod_dt   datetime NULL,
        PRIMARY KEY NONCLUSTERED (person_pk)
 )
go
 
 
 CREATE TABLE Person_Email (
        email_pk             int IDENTITY,
        email_type           Common_code_key NOT NULL DEFAULT 0,
        person_pk            int NOT NULL,
        email_bad_fg         fg DEFAULT 0,
        person_email_addr    varchar(50) NULL,
        email_marked_bad_dt  datetime NULL,
        created_user_pk      User_pk,
        created_dt           datetime NOT NULL,
        lst_mod_user_pk      User_pk,
        lst_mod_dt           datetime NOT NULL,
        PRIMARY KEY (email_pk), 
        FOREIGN KEY (person_pk)
                              REFERENCES Person
 )
go
 
 
 CREATE TABLE COM_Application_Functions (
        application_function_pk int IDENTITY,
        application_function_nm varchar(30) NULL,
        PRIMARY KEY NONCLUSTERED (application_function_pk)
 )
go
 
 
 CREATE TABLE COM_Application_Notification (
        application_notification_pk int IDENTITY,
        created_user_pk      User_pk,
        created_dt           datetime NOT NULL,
        email_pk             int NOT NULL,
        lst_mod_user_pk      User_pk,
        application_function_pk int NOT NULL,
        lst_mod_dt           datetime NOT NULL,
        PRIMARY KEY NONCLUSTERED (application_notification_pk), 
        FOREIGN KEY (email_pk)
                              REFERENCES Person_Email, 
        FOREIGN KEY (application_function_pk)
                              REFERENCES COM_Application_Functions
 )
go
 
 
 CREATE TABLE Mass_Change_Batch_Control (
        aff_pk               int NOT NULL,
        mass_chng_request_dt datetime NOT NULL,
        requesting_user_pk   User_pk,
        mass_chng_priority   int NOT NULL,
        mass_chng_type       Common_code_key NOT NULL,
        new_aff_pk           int NULL,
        new_aff_type         char(1) NULL,
        new_aff_localSubChapter char(4) NULL,
        new_aff_stateNat_type char(2) NULL,
        new_aff_subUnit      char(4) NULL,
        new_aff_councilRetiree_chap char(4) NULL,
        new_aff_code         char(1) NULL,
        new_aff_status       Common_code_key,
        new_flag_value       smallint NULL,
        new_select_value     Common_code_key,
        mass_chng_error_fg   fg,
        mass_chng_completed_dt datetime NULL,
        PRIMARY KEY NONCLUSTERED (aff_pk, mass_chng_request_dt, 
               requesting_user_pk, mass_chng_priority), 
        FOREIGN KEY (aff_pk)
                              REFERENCES Aff_Organizations
 )
go
 
 
 CREATE TABLE Person_Address (
        address_pk           int IDENTITY,
        person_pk            int NOT NULL,
        addr_type            Common_code_key NOT NULL,
        dept                 Common_code_key,
        addr_source          char(1) NOT NULL,
        addr_source_if_aff_apply_upd int NULL,
        addr_prmry_fg        fg DEFAULT 0,
        addr_bad_fg          fg DEFAULT 0,
        addr_marked_bad_dt   datetime NULL,
        addr_private_fg      fg DEFAULT 0,
        addr1                varchar(50) NULL,
        addr2                varchar(50) NULL,
        city                 varchar(25) NULL,
        state                char(2) NULL,
        zipcode              varchar(12) NULL,
        zip_plus             char(4) NULL,
        province             varchar(25) NULL,
        carrier_route_info   varchar(50) NULL,
        country              Common_code_key,
        county               varchar(25) NULL,
        eff_dt               datetime NULL,
        end_dt               datetime NULL,
        created_user_pk      User_pk,
        created_dt           datetime NOT NULL,
        lst_mod_user_pk      Common_code_key NOT NULL,
        lst_mod_dt           datetime NOT NULL,
        PRIMARY KEY (address_pk), 
        FOREIGN KEY (addr_source_if_aff_apply_upd)
                              REFERENCES Aff_Organizations, 
        FOREIGN KEY (person_pk)
                              REFERENCES Person
 )
go
 
 
 CREATE TABLE Person_SMA (
        address_pk           int NOT NULL,
        person_pk            int NOT NULL,
        sequence             int NOT NULL,
        determined_dt        datetime NULL,
        current_fg           fg NOT NULL,
        PRIMARY KEY NONCLUSTERED (address_pk, person_pk, sequence), 
        FOREIGN KEY (person_pk)
                              REFERENCES Person, 
        FOREIGN KEY (address_pk)
                              REFERENCES Person_Address
 )
go
 
 
 CREATE TABLE NCOA_Transactions (
        person_pk            int NOT NULL,
        address_pk           int NOT NULL,
        last_nm              varchar(25) NULL,
        extracted_sma_addr1  varchar(50) NULL,
        extracted_sma_addr2  varchar(50) NULL,
        extracted_sma_city   varchar(25) NULL,
        extracted_sma_state  char(2) NULL,
        extracted_sma_zipcode varchar(12) NULL,
        vendor_transaction_cd varchar(2) NULL,
        vendor_key           varchar(27) NULL,
        vendor_addr1         varchar(50) NULL,
        vendor_addr2         varchar(50) NULL,
        vendor_city          varchar(15) NULL,
        vendor_state         char(2) NULL,
        vendor_zipcode       char(5) NULL,
        vendor_source        char(1) NULL,
        vendor_zip_plus      char(4) NULL,
        vendor_carrier_route varchar(50) NULL,
        vendor_ncoa_change_source char(4) NULL,
        vendor_ncoa_change_dt datetime NULL,
        PRIMARY KEY NONCLUSTERED (person_pk), 
        FOREIGN KEY (address_pk)
                              REFERENCES Person_Address, 
        FOREIGN KEY (person_pk)
                              REFERENCES Person
 )
go
 
 
 CREATE TABLE Aff_Chng_History (
        aff_transaction_pk   int IDENTITY,
        aff_pk               int NULL,
        aff_section          Common_code_key NOT NULL,
        chng_user_pk         User_pk,
        chng_dt              datetime NOT NULL,
        PRIMARY KEY NONCLUSTERED (aff_transaction_pk), 
        FOREIGN KEY (aff_pk)
                              REFERENCES Aff_Organizations
 )
go
 
 
 CREATE TABLE Aff_Chng_History_Juris_Dtl (
        aff_transaction_pk   int NOT NULL,
        juris_value_old_new  smallint NOT NULL,
        juris_value          varchar(5000) NULL,
        PRIMARY KEY NONCLUSTERED (aff_transaction_pk, 
               juris_value_old_new), 
        FOREIGN KEY (aff_transaction_pk)
                              REFERENCES Aff_Chng_History
 )
go
 
 
 CREATE TABLE External_Organizations (
        org_pk               int NOT NULL DEFAULT 0,
        org_nm               varchar(29) NULL,
        org_web_site         varchar(100) NULL,
        org_email_domain     varchar(50) NULL,
        ext_org_type         Common_code_key,
        marked_for_deletion_fg fg NOT NULL,
        created_user_pk      User_pk,
        created_dt           datetime NOT NULL,
        lst_mod_user_pk      User_pk,
        lst_mod_dt           datetime NOT NULL,
        PRIMARY KEY NONCLUSTERED (org_pk), 
        FOREIGN KEY (org_pk)
                              REFERENCES Org_Parent
                              ON DELETE CASCADE
 )
go
 
 
 CREATE TABLE AUP_Queue_Mgmt (
        queue_pk             int IDENTITY,
        aff_pk               int NULL,
        org_pk               int NULL,
        file_nm              varchar(100) NULL,
        file_storage_path    varchar(254) NULL,
        file_queue           Common_code_key,
        upd_file_type        Common_code_key NOT NULL,
        upd_type             Common_code_key,
        upd_file_status      Common_code_key,
        upd_file_comments    varchar(254) NULL,
        upd_file_received_dt datetime NOT NULL,
        upd_file_valid_dt    datetime NULL,
        upd_file_processed_dt datetime NULL,
        created_user_pk      User_pk,
        created_dt           datetime NOT NULL,
        lst_mod_user_pk      User_pk,
        lst_mod_dt           datetime NULL,
        PRIMARY KEY NONCLUSTERED (queue_pk), 
        FOREIGN KEY (org_pk)
                              REFERENCES External_Organizations, 
        FOREIGN KEY (aff_pk)
                              REFERENCES Aff_Organizations
 )
go
 
 
 CREATE TABLE AUP_Officer_Pre_Upd_Smry (
        queue_pk             int NOT NULL,
        aff_id               int IDENTITY,
        aff_pk               int NULL,
        off_system_cnt       int NOT NULL,
        off_file_cnt         int NOT NULL,
        off_replaced_cnt     int NOT NULL,
        off_chg_cnt          int NOT NULL,
        off_vacant_cnt       int NOT NULL,
        off_cards_cnt        int NOT NULL,
        off_add_cnt          int NOT NULL,
        aff_error_fg         fg,
        aff_warning_fg       fg,
        aff_err_aff_type     char(1) NULL,
        aff_err_aff_localSubChapter varchar(4) NULL,
        aff_err_aff_stateNat_type varchar(2) NULL,
        aff_err_aff_subUnit  varchar(4) NULL,
        aff_err_aff_councilRetiree_chap varchar(4) NULL, 
        PRIMARY KEY NONCLUSTERED (queue_pk, aff_id),
        FOREIGN KEY (aff_pk)
                              REFERENCES Aff_Organizations, 
        FOREIGN KEY (queue_pk)
                              REFERENCES AUP_Queue_Mgmt
 )
go
 
 
 CREATE TABLE AUP_Officer_Pre_Off_Dtl (
        aff_pk               int NOT NULL,
        queue_pk             int NOT NULL,
        afscme_office_pk     int NOT NULL,
        office_group_id      int NOT NULL,
        officers_allowed_num int NOT NULL,
        officers_in_file_cnt int NOT NULL,
        PRIMARY KEY NONCLUSTERED (aff_pk, queue_pk, afscme_office_pk, 
               office_group_id), 
        FOREIGN KEY (queue_pk)
                              REFERENCES AUP_Queue_Mgmt, 
        FOREIGN KEY (aff_pk)
                              REFERENCES Aff_Organizations
 )
go
 
 
 CREATE TABLE Officer_Comments (
        officer_history_surrogate_key int NOT NULL,
        comment_dt           datetime NOT NULL,
        comment_txt          varchar(254) NOT NULL,
        created_user_pk      User_pk,
        PRIMARY KEY NONCLUSTERED (officer_history_surrogate_key, 
               comment_dt)
 )
go
 
 
 CREATE TABLE AUP_Pre_Fld_Chg_Smry (
        queue_pk             int NOT NULL,
        upd_field_nm         Common_code_key NOT NULL,
        upd_field_chg_cnt    int NOT NULL,
        PRIMARY KEY NONCLUSTERED (queue_pk, upd_field_nm), 
        FOREIGN KEY (queue_pk)
                              REFERENCES AUP_Queue_Mgmt
 )
go
 
 
 CREATE TABLE AUP_Member_Pre_Upd_Smry (
        queue_pk             int NOT NULL,
        aff_id               int IDENTITY,
        aff_pk               int NULL,
        mbr_system_cnt       int NOT NULL,
        mbr_file_cnt         int NOT NULL,
        mbr_added_cnt        int NULL,
        mbr_inactivated_cnt  int NOT NULL,
        mbr_changed_cnt      int NOT NULL,
        t_mbrs_created_cnt   int NOT NULL,
        mbr_matched_cnt      int NOT NULL,
        mbr_nonmatched_cnt   int NOT NULL,
        aff_error_fg         fg,
        aff_warning_fg       fg,
        aff_err_aff_type     char(1) NULL,
        aff_err_aff_localSubChapter varchar(4) NULL,
        aff_err_aff_subUnit  varchar(4) NULL,
        aff_err_aff_councilRetiree_chap varchar(4) NULL,
        PRIMARY KEY NONCLUSTERED (queue_pk, aff_id), 
        FOREIGN KEY (aff_pk)
                              REFERENCES Aff_Organizations, 
        FOREIGN KEY (queue_pk)
                              REFERENCES AUP_Queue_Mgmt
 )
go
 
 
 CREATE TABLE AUP_Rebate_Pre_Upd_Smry (
        queue_pk             int NOT NULL,
        aff_id               int IDENTITY,
        aff_pk               int NULL,
        records_sent_cnt     int NULL,
        records_received_cnt int NULL,
        mbr_unknown_cnt      int NOT NULL,
        council_acc_chg_cnt  int NOT NULL,
        local_acc_chg_cnt    int NOT NULL,
        no_chg_cnt           int NOT NULL,
        aff_error_fg         fg,
        aff_warning_fg       fg,
        aff_err_aff_type     char(1) NULL,
        aff_err_aff_localSubChapter varchar(4) NULL,
        aff_err_aff_subUnit  varchar(4) NULL,
        aff_err_aff_councilRetiree_chap varchar(4) NULL,
        PRIMARY KEY NONCLUSTERED (queue_pk, aff_id), 
        FOREIGN KEY (aff_pk)
                              REFERENCES Aff_Organizations, 
        FOREIGN KEY (queue_pk)
                              REFERENCES AUP_Queue_Mgmt
 )
go
 
 
 CREATE TABLE Person_Disabilities (
        person_pk            int NOT NULL,
        disability           Common_code_key NOT NULL,
        comment_txt          varchar(254) NULL,
        created_user_pk      User_pk,
        created_dt           datetime NOT NULL,
        lst_mod_user_pk      User_pk,
        lst_mod_dt           datetime NOT NULL,
        PRIMARY KEY NONCLUSTERED (person_pk, disability), 
        FOREIGN KEY (person_pk)
                              REFERENCES Person
 )
go
 
 
 CREATE TABLE PRB_12_Month_Rebate (
        rbt_year             smallint NOT NULL,
        rbt_pct              decimal(5,2) NOT NULL,
        rbt_full_time_amt    decimal(6,2) NOT NULL,
        rbt_part_time_amt    decimal(6,2) NOT NULL,
        rbt_lower_part_time_amt decimal(6,2) NOT NULL,
        rbt_retiree_amt      decimal(6,2) NOT NULL,
        created_user_pk      User_pk,
        created_dt           datetime NOT NULL,
        lst_mod_user_pk      User_pk,
        lst_mod_dt           datetime NOT NULL,
        PRIMARY KEY NONCLUSTERED (rbt_year)
 )
go
 
 
 CREATE TABLE PRB_Rbt_Year_Info (
        person_pk            int NOT NULL,
        rbt_year             smallint NOT NULL,
        comment_txt          varchar(254) NULL,
        person_rbt_year_status Common_code_key,
        PRIMARY KEY NONCLUSTERED (person_pk, rbt_year), 
        FOREIGN KEY (person_pk)
                              REFERENCES Person
 )
go
 
 
 CREATE TABLE PRB_Rebate_Check_Info (
        person_pk            int NOT NULL,
        rbt_year             smallint NOT NULL,
        rbt_check_nbr_1      int NULL,
        rbt_check_amt_1      money NULL,
        rbt_check_1_run_dt   datetime NULL,
        rbt_check_1_returned_fg fg,
        rbt_check_nbr_2      int NULL,
        rbt_check_amt_2      money NULL,
        rbt_check_2_run_dt   datetime NULL,
        rbt_check_2_returned_fg fg,
        judicial_pnl_case_num int NULL,
        suppl_check_nbr      int NULL,
        suppl_check_amt      money NULL,
        suppl_check_run_dt   datetime NULL,
        suppl_check_returned_fg fg,
        created_user_pk      User_pk,
        created_dt           datetime NOT NULL,
        lst_mod_user_pk      User_pk,
        lst_mod_dt           datetime NOT NULL,
        PRIMARY KEY NONCLUSTERED (person_pk, rbt_year), 
        FOREIGN KEY (person_pk)
                              REFERENCES Person
 )
go
 
 
 CREATE TABLE PRB_Roster_Persons (
        aff_pk               int NOT NULL,
        person_pk            int NOT NULL,
        rbt_year             smallint NOT NULL,
        rbt_check_nbr        int NULL,
        roster_aff_status    Common_code_key,
        roster_duration_in_aff Common_code_key,
        roster_filed_with    Common_code_key,
        roster_acceptance_cd Common_code_key,
        rebate_year_mbr_type Common_code_key,
        rebate_year_mbr_status Common_code_key,
        rebate_year_mbr_dues_rate Common_code_key,
        rebate_year_amt      money NULL,
        created_user_pk      User_pk,
        created_dt           datetime NOT NULL,
        lst_mod_user_pk      User_pk,
        lst_mod_dt           datetime NOT NULL,
        file_generated_dt    datetime NULL,
        PRIMARY KEY NONCLUSTERED (aff_pk, person_pk, rbt_year), 
        FOREIGN KEY (person_pk, rbt_year)
                              REFERENCES PRB_Rbt_Year_Info, 
        FOREIGN KEY (aff_pk)
                              REFERENCES Aff_Organizations
 )
go
 
 
 CREATE TABLE PRB_Apps (
        prb_app_pk           int IDENTITY,
        app_mailed_dt        datetime NULL,
        app_returned_dt      datetime NULL,
        app_status           Common_code_key,
        prb_evaluation_cd    Common_code_key,
        prb_comment_anal_cd  Common_code_key,
        aff_roster_generated_fg fg NOT NULL DEFAULT 0,
        comment_txt          varchar(254) NULL,
        created_user_pk      User_pk,
        created_dt           datetime NOT NULL,
        lst_mod_user_pk      User_pk,
        lst_mod_dt           datetime NOT NULL,
        PRIMARY KEY NONCLUSTERED (prb_app_pk)
 )
go
 
 
 CREATE TABLE PRB_App_Affs (
        aff_pk               int NOT NULL,
        prb_app_pk           int NOT NULL,
        app_duration_in_aff  Common_code_key,
        app_months_in_aff    smallint NULL,
        app_filed_with       int NULL,
        PRIMARY KEY NONCLUSTERED (aff_pk, prb_app_pk), 
        FOREIGN KEY (aff_pk)
                              REFERENCES Aff_Organizations, 
        FOREIGN KEY (prb_app_pk)
                              REFERENCES PRB_Apps
 )
go
 
 
 CREATE TABLE PRB_Requests (
        rqst_pk              int IDENTITY,
        rqst_dt              datetime NULL,
        person_pk            int NOT NULL,
        rqst_rebate_year     smallint NOT NULL,
        rqst_cert_mail_num   varchar(20) NULL,
        rqst_denied_fg       fg NOT NULL DEFAULT 0,
        rqst_denied_reason   Common_code_key,
        rqst_denied_dt       datetime NULL,
        rqst_keyed_dt        datetime NOT NULL,
        rqst_resubmit_fg     fg NOT NULL DEFAULT 0,
        rqst_resubmit_denied_reason Common_code_key,
        rqst_resubmit_denied_dt datetime NULL,
        rqst_status          Common_code_key,
        comment_txt          varchar(254) NULL,
        prb_app_pk           int NULL,
        created_user_pk      User_pk,
        created_dt           datetime NOT NULL,
        lst_mod_user_pk      User_pk,
        lst_mod_dt           datetime NOT NULL,
        PRIMARY KEY NONCLUSTERED (rqst_pk), 
        FOREIGN KEY (person_pk)
                              REFERENCES Person, 
        FOREIGN KEY (prb_app_pk)
                              REFERENCES PRB_Apps,
        UNIQUE (
               rqst_dt,
               person_pk
        )
 )
go
 
 
 CREATE TABLE PRB_Request_Affs (
        aff_pk               int NOT NULL,
        rqst_pk              int NOT NULL,
        rqst_duration_in_aff Common_code_key,
        rqst_months_in_affiliate smallint NULL,
        rqst_filed_with      Common_code_key,
        PRIMARY KEY NONCLUSTERED (aff_pk, rqst_pk), 
        FOREIGN KEY (rqst_pk)
                              REFERENCES PRB_Requests, 
        FOREIGN KEY (aff_pk)
                              REFERENCES Aff_Organizations
 )
go
 
 
 CREATE TABLE Aff_Mbr_Rpt_Info (
        aff_pk               int NOT NULL,
        aff_info_reporting_source Common_code_key,
        unit_wide_no_mbr_cards_fg fg,
        unit_wide_no_pe_mail_fg fg,
        comment_txt          varchar(254) NULL,
        created_user_pk      User_pk,
        created_dt           datetime NOT NULL,
        lst_mod_user_pk      User_pk,
        lst_mod_dt           datetime NOT NULL,
        PRIMARY KEY NONCLUSTERED (aff_pk), 
        FOREIGN KEY (aff_pk)
                              REFERENCES Aff_Organizations
 )
go
 
 
 CREATE TABLE Org_Locations (
        org_locations_pk     int IDENTITY,
        location_primary_fg  fg,
        location_nm          varchar(50) NULL,
        org_pk               int NOT NULL,
        created_user_pk      User_pk,
        created_dt           datetime NOT NULL,
        lst_mod_user_pk      User_pk,
        lst_mod_dt           datetime NOT NULL,
        PRIMARY KEY NONCLUSTERED (org_locations_pk), 
        FOREIGN KEY (org_pk)
                              REFERENCES Org_Parent
 )
go
 
 
 CREATE TABLE Mailing_Lists_by_Orgs (
        MLBO_mailing_list_pk int IDENTITY,
        MLBO_mailing_list_nm varchar(100) NOT NULL,
        officer_mailing_list_fg fg,
        mailing_list_legacy_cd smallint NULL,
        created_user_pk      User_pk,
        created_dt           datetime NOT NULL,
        lst_mod_user_pk      User_pk,
        lst_mod_dt           datetime NOT NULL,
        PRIMARY KEY NONCLUSTERED (MLBO_mailing_list_pk)
 )
go
 
 
 CREATE TABLE Mailing_List_Orgs (
        MLBO_mailing_list_pk int NOT NULL,
        org_pk               int NOT NULL,
        org_locations_pk     int NULL,
        mailing_list_bulk_cnt smallint NULL,
        created_user_pk      User_pk,
        created_dt           datetime NOT NULL,
        lst_mod_user_pk      User_pk,
        lst_mod_dt           datetime NOT NULL,
        PRIMARY KEY NONCLUSTERED (MLBO_mailing_list_pk, org_pk), 
        FOREIGN KEY (org_pk)
                              REFERENCES Org_Parent, 
        FOREIGN KEY (org_locations_pk)
                              REFERENCES Org_Locations, 
        FOREIGN KEY (MLBO_mailing_list_pk)
                              REFERENCES Mailing_Lists_by_Orgs
 )
go
 
 
 CREATE TABLE Aff_Multi_State (
        aff_pk               int NOT NULL,
        state                char(2) NOT NULL,
        PRIMARY KEY NONCLUSTERED (aff_pk, state), 
        FOREIGN KEY (aff_pk)
                              REFERENCES Aff_Organizations
 )
go
 
 
 CREATE TABLE Aff_Admin_Councils (
        admin_legislative_cncl_aff_pk int NOT NULL,
        regular_council_aff_pk int NOT NULL,
        created_user_pk      nullable_user_pk,
        created_dt           datetime NULL,
        lst_mod_user_pk      nullable_user_pk,
        lst_mod_dt           datetime NULL,
        PRIMARY KEY NONCLUSTERED (admin_legislative_cncl_aff_pk, 
               regular_council_aff_pk), 
        FOREIGN KEY (admin_legislative_cncl_aff_pk)
                              REFERENCES Aff_Organizations, 
        FOREIGN KEY (regular_council_aff_pk)
                              REFERENCES Aff_Organizations
 )
go
 
 
 CREATE TABLE Aff_Employer_Sector (
        aff_pk               int NOT NULL,
        aff_employer_sector  Common_code_key NOT NULL,
        PRIMARY KEY NONCLUSTERED (aff_pk, aff_employer_sector), 
        FOREIGN KEY (aff_pk)
                              REFERENCES Aff_Organizations
 )
go
 
 
 CREATE TABLE Person_Political_Legislative (
        person_pk            int NOT NULL,
        political_do_not_call_fg fg,
        political_party      Common_code_key,
        political_registered_voter Common_code_key,
        pac_contributor_fg   fg,
        political_objector_fg fg,
        ward_number          char(4) NULL,
        precinct_number      char(10) NULL,
        precinct_name        varchar(25) NULL,
        created_user_pk      nullable_user_pk,
        created_dt           datetime NULL,
        lst_mod_user_pk      nullable_user_pk,
        lst_mod_dt           datetime NULL,
        PRIMARY KEY NONCLUSTERED (person_pk), 
        FOREIGN KEY (person_pk)
                              REFERENCES Person
 )
go
 
 
 CREATE TABLE Common_Code_Category (
        com_cd_cat_pk        int IDENTITY,
        com_cd_cat_nm        varchar(50) NOT NULL,
        com_cd_cat_desc      varchar(150) NULL,
        row_status_cd        char(1) NOT NULL,
        PRIMARY KEY NONCLUSTERED (com_cd_cat_pk)
 )
go
 
 
 CREATE TABLE Common_Code_Type (
        com_cd_type_key      varchar(20) NOT NULL,
        com_cd_type_desc     varchar(30) NULL,
        remarks              varchar(50) NULL,
        category_fk          int NOT NULL,
        row_status_cd        char(1) NOT NULL DEFAULT 'A',
        created_user_pk      nullable_user_pk,
        created_dt           datetime NULL,
        PRIMARY KEY (com_cd_type_key), 
        FOREIGN KEY (category_fk)
                              REFERENCES Common_Code_Category
 )
go
 
 
 CREATE TABLE Report_Fields (
        report_field_pk      int IDENTITY,
        field_entity_type    Type,
        field_category_name  varchar(40) NOT NULL,
        field_table_nm       varchar(30) NOT NULL,
        com_cd_type_key      varchar(20) NULL,
        field_column_name    varchar(30) NOT NULL,
        field_display_name   varchar(50) NOT NULL,
        field_display_type   Type,
        field_print_width    decimal(4,2) NULL,
        PRIMARY KEY NONCLUSTERED (report_field_pk), 
        FOREIGN KEY (com_cd_type_key)
                              REFERENCES Common_Code_Type
 )
go
 
 
 CREATE TABLE Report_Field_Aggregate (
        child_field_pk       int NOT NULL,
        parent_field_pk      int NOT NULL,
        PRIMARY KEY NONCLUSTERED (child_field_pk, parent_field_pk), 
        FOREIGN KEY (parent_field_pk)
                              REFERENCES Report_Fields, 
        FOREIGN KEY (child_field_pk)
                              REFERENCES Report_Fields
 )
go
 
 
 CREATE TABLE Report (
        report_pk            int IDENTITY,
        report_category      varchar(50) NULL,
        report_nm            varchar(100) NOT NULL,
        report_desc          varchar(254) NULL,
        report_pend_entity_fg fg NOT NULL,
        report_mailing_list_fg fg NOT NULL,
        report_custom_fg     fg NOT NULL,
        report_update_corresp_fg fg NOT NULL,
        report_handler_class varchar(100) NULL,
        report_is_count_fg   fg NOT NULL,
        report_last_update_uid varchar(20) NULL,
        report_last_update_dt datetime NULL,
        report_owner_pk      int NOT NULL,
        report_status        fg NOT NULL DEFAULT 1,
        PRIMARY KEY NONCLUSTERED (report_pk)
 )
go
 
 
 CREATE TABLE Report_Sort_Fields (
        report_pk            int NOT NULL,
        report_field_pk      int NOT NULL,
        field_sort_order     smallint NOT NULL,
        field_sort_direction char(1) NULL,
        PRIMARY KEY NONCLUSTERED (report_pk, report_field_pk), 
        FOREIGN KEY (report_pk)
                              REFERENCES Report, 
        FOREIGN KEY (report_field_pk)
                              REFERENCES Report_Fields
 )
go
 
 
 CREATE TABLE Report_Selection_Criteria (
        report_pk            int NOT NULL,
        report_field_pk      int NOT NULL,
        report_criterion_sequence_pk int NOT NULL,
        criterion_value      varchar(150) NULL,
        criterion_operator   char(2) NOT NULL,
        criterion_editable_fg fg NOT NULL,
        criterion_value2     varchar(150) NULL,
        PRIMARY KEY NONCLUSTERED (report_pk, report_field_pk, 
               report_criterion_sequence_pk), 
        FOREIGN KEY (report_field_pk)
                              REFERENCES Report_Fields, 
        FOREIGN KEY (report_pk)
                              REFERENCES Report
 )
go
 
 
 CREATE TABLE Common_Codes (
        com_cd_pk            int IDENTITY,
        com_cd_cd            varchar(8) NOT NULL,
        com_cd_desc          varchar(50) NULL,
        com_cd_type_key      varchar(20) NOT NULL,
        com_cd_sort_key      varchar(20) NULL,
        row_status_cd        char(1) NOT NULL DEFAULT 'A',
        created_user_pk      nullable_user_pk,
        created_dt           datetime NULL,
        lst_mod_user_pk      nullable_user_pk,
        lst_mod_dt           datetime NULL,
        PRIMARY KEY (com_cd_pk), 
        FOREIGN KEY (com_cd_type_key)
                              REFERENCES Common_Code_Type
 )
go
 
 
 CREATE TABLE Criteria_Codevalue (
        com_cd_pk            int NOT NULL,
        report_pk            int NOT NULL,
        report_field_pk      int NOT NULL,
        report_criterion_sequence_pk int NOT NULL,
        PRIMARY KEY NONCLUSTERED (com_cd_pk, report_pk, 
               report_field_pk, report_criterion_sequence_pk), 
        FOREIGN KEY (report_pk, report_field_pk, 
               report_criterion_sequence_pk)
                              REFERENCES Report_Selection_Criteria, 
        FOREIGN KEY (com_cd_pk)
                              REFERENCES Common_Codes
 )
go
 
 
 CREATE TABLE Person_Comments (
        person_pk            int NOT NULL,
        comment_dt           datetime NOT NULL,
        comment_txt          varchar(254) NOT NULL,
        created_user_pk      User_pk,
        PRIMARY KEY NONCLUSTERED (person_pk, comment_dt), 
        FOREIGN KEY (person_pk)
                              REFERENCES Person
 )
go
 
 
 CREATE TABLE Report_OrderByFields (
        report_pk            int NOT NULL,
        report_field_pk      int NOT NULL,
        sort_order_seq       int NOT NULL,
        asc_dsc_cd           char(1) NOT NULL,
        PRIMARY KEY NONCLUSTERED (report_pk, report_field_pk), 
        FOREIGN KEY (report_field_pk)
                              REFERENCES Report_Fields, 
        FOREIGN KEY (report_pk)
                              REFERENCES Report
 )
go
 
 
 CREATE TABLE Users (
        person_pk            int NOT NULL,
        dept                 Common_code_key,
        start_page           Type,
        user_id              varchar(10) NOT NULL,
        pwd                  char(32) NULL,
        remarks              varchar(50) NULL,
        challenge_question   Common_code_key,
        challenge_response   varchar(50) NULL,
        last_session         datetime NULL,
        bad_login_attempt_ct smallint NULL,
        lockout_dt           datetime NULL,
        pwd_expiration       datetime NULL,
        PRIMARY KEY (person_pk), 
        FOREIGN KEY (person_pk)
                              REFERENCES Person
 )
go
 
 
 CREATE TABLE Pended_Entities (
        person_pk            int NOT NULL,
        report_pk            int NOT NULL,
        entity_pk            int NOT NULL,
        PRIMARY KEY NONCLUSTERED (person_pk, report_pk, entity_pk), 
        FOREIGN KEY (person_pk)
                              REFERENCES Users, 
        FOREIGN KEY (report_pk)
                              REFERENCES Report
 )
go
 
 
 CREATE TABLE Privileges (
        privilege_key        varchar(100) NOT NULL,
        privilege_category   Type,
        privilege_is_data_utility fg,
        privilege_verb       Type,
        privilege_name       varchar(50) NULL,
        PRIMARY KEY (privilege_key)
 )
go
 
 
 CREATE TABLE Roles (
        role_pk              int IDENTITY,
        role_name            varchar(30) NULL,
        role_description     varchar(50) NULL,
        PRIMARY KEY (role_pk)
 )
go
 
 
 CREATE TABLE User_Roles (
        person_pk            int NOT NULL,
        role_pk              int NOT NULL,
        PRIMARY KEY (person_pk, role_pk), 
        FOREIGN KEY (role_pk)
                              REFERENCES Roles
                              ON DELETE CASCADE, 
        FOREIGN KEY (person_pk)
                              REFERENCES Users
 )
go
 
 
 CREATE TABLE Aff_Charter (
        aff_pk               int NOT NULL,
        new_aff_no           int NULL,
        aff_eff_dt           datetime NULL,
        charter_dt           datetime NULL,
        charter_cd           Common_code_key,
        charter_nm           varchar(250) NULL,
        charter_juris        varchar(5000) NULL,
        charter_lst_chg_eff_dt datetime NULL,
        created_user_pk      User_pk,
        created_dt           datetime NOT NULL,
        lst_mod_user_pk      User_pk,
        lst_mod_dt           datetime NOT NULL,
        PRIMARY KEY (aff_pk), 
        FOREIGN KEY (aff_pk)
                              REFERENCES Aff_Organizations
 )
go
 
 
 CREATE TABLE Aff_Charter_County (
        aff_pk               int NOT NULL,
        county_nm            varchar(25) NOT NULL,
        PRIMARY KEY (aff_pk, county_nm), 
        FOREIGN KEY (aff_pk)
                              REFERENCES Aff_Charter
 )
go
 
 
 CREATE TABLE Person_Phone (
        phone_pk             int IDENTITY,
        person_pk            int NOT NULL,
        dept                 Common_code_key,
        phone_type           Common_code_key NOT NULL,
        phone_prmry_fg       fg DEFAULT 0,
        phone_bad_fg         fg DEFAULT 0,
        phone_marked_bad_dt  datetime NULL,
        phone_private_fg     fg DEFAULT 0,
        phone_do_not_call_fg fg,
        country_cd           varchar(5) NULL,
        area_code            char(3) NULL,
        phone_no             varchar(15) NOT NULL,
        phone_extension      varchar(5) NULL,
        created_user_pk      User_pk,
        created_dt           datetime NOT NULL,
        lst_mod_user_pk      User_pk,
        lst_mod_dt           datetime NOT NULL,
        PRIMARY KEY (phone_pk), 
        FOREIGN KEY (person_pk)
                              REFERENCES Person
 )
go
 
 
 CREATE TABLE Aff_Members (
        aff_pk               int NOT NULL,
        person_pk            int NOT NULL,
        mbr_no_local         varchar(15) NULL,
        mbr_no_old_afscme    char(17) NULL,
        mbr_status           Common_code_key NOT NULL,
        mbr_type             Common_code_key NOT NULL,
        lost_time_language_fg fg,
        mbr_card_sent_dt     datetime NULL,
        primary_information_source Common_code_key,
        mbr_join_dt          datetime NULL,
        mbr_dues_type        Common_code_key,
        mbr_dues_rate        money NULL,
        mbr_dues_frequency   Common_code_key,
        mbr_retired_dt       datetime NULL,
        mbr_ret_dues_renewal_fg fg,
        no_cards_fg          fg DEFAULT 0,
        no_mail_fg           fg,
        no_public_emp_fg     fg DEFAULT 0,
        no_legislative_mail_fg fg DEFAULT 0,
        created_user_pk      User_pk,
        created_dt           datetime NOT NULL,
        lst_mod_user_pk      User_pk,
        lst_mod_dt           datetime NOT NULL,
        PRIMARY KEY NONCLUSTERED (aff_pk, person_pk), 
        FOREIGN KEY (person_pk)
                              REFERENCES Person, 
        FOREIGN KEY (aff_pk)
                              REFERENCES Aff_Organizations
 )
go
 
 
 CREATE TABLE Aff_Mbr_Employers (
        person_pk            int NOT NULL,
        aff_pk               int NOT NULL,
        employer_name        varchar(50) NULL,
        emp_job_title        Common_code_key,
        emp_sector           Common_code_key,
        emp_job_site         varchar(100) NULL,
        employee_salary      money NULL,
        emp_salary_range     Common_code_key,
        created_user_pk      User_pk,
        created_dt           datetime NOT NULL,
        lst_mod_user_pk      User_pk,
        lst_mod_dt           datetime NOT NULL,
        PRIMARY KEY (person_pk, aff_pk), 
        FOREIGN KEY (aff_pk, person_pk)
                              REFERENCES Aff_Members
 )
go
 
 
 CREATE TABLE Person_Language (
        person_pk            int NOT NULL,
        language             Common_code_key NOT NULL,
        primary_language_fg  fg,
        created_user_pk      nullable_user_pk,
        created_dt           datetime NULL,
        lst_mod_user_pk      nullable_user_pk,
        lst_mod_dt           datetime NULL,
        PRIMARY KEY (person_pk, language), 
        FOREIGN KEY (person_pk)
                              REFERENCES Person
 )
go
 
 
 CREATE TABLE Person_Relation (
        person_relative_pk   int IDENTITY,
        person_pk            int NOT NULL,
        person_relative_type Common_code_key,
        relative_first_nm    varchar(25) NOT NULL,
        relative_last_nm     varchar(25) NOT NULL,
        relative_middle_nm   varchar(25) NULL,
        relative_suffix_nm   Common_code_key,
        relative_birth_dt    datetime NULL,
        relative_deceased_dt datetime NULL,
        created_user_pk      User_pk,
        created_dt           datetime NULL,
        lst_mod_user_pk      User_pk,
        lst_mod_dt           datetime NOT NULL,
        PRIMARY KEY (person_relative_pk), 
        FOREIGN KEY (person_pk)
                              REFERENCES Person
 )
go
 
 
 CREATE TABLE Person_Demographics (
        person_pk            int NOT NULL,
        dob                  datetime NULL,
        deceased_fg          int NULL,
        deceased_dt          datetime NULL,
        gender               Common_code_key,
        ethnic_origin        Common_code_key,
        citizenship          Common_code_key,
        religion             Common_code_key,
        marital_status       Common_code_key,
        region_fk            int NULL,
        scholarship_fg       fg,
        created_user_pk      User_pk,
        created_dt           datetime NOT NULL,
        lst_mod_user_pk      User_pk DEFAULT suser_sname(),
        lst_mod_dt           datetime NOT NULL,
        PRIMARY KEY NONCLUSTERED (person_pk), 
        FOREIGN KEY (person_pk)
                              REFERENCES Person
 )
go
 
 
 CREATE TABLE Org_Phone (
        org_phone_pk         int IDENTITY,
        org_phone_type       Common_code_key NOT NULL,
        org_locations_pk     int NOT NULL,
        phone_bad_fg         fg,
        phone_bad_dt         datetime NULL,
        country_cd           varchar(5) NULL,
        area_code            char(3) NULL,
        phone_no             varchar(15) NOT NULL,
        phone_extension      varchar(5) NULL,
        created_user_pk      User_pk,
        created_dt           datetime NOT NULL,
        lst_mod_user_pk      User_pk,
        lst_mod_dt           datetime NOT NULL,
        PRIMARY KEY (org_phone_pk), 
        FOREIGN KEY (org_locations_pk)
                              REFERENCES Org_Locations
                              ON DELETE CASCADE
 )
go
 
 
 CREATE TABLE Org_Address (
        org_addr_pk          int IDENTITY,
        org_addr_type        Common_code_key NOT NULL,
        org_locations_pk     int NOT NULL,
        addr_bad_fg          fg,
        addr_bad_dt          datetime NULL,
        attention_line       varchar(75) NULL,
        addr1                varchar(50) NOT NULL,
        addr2                varchar(50) NULL,
        city                 varchar(25) NULL,
        state                char(2) NULL,
        zipcode              varchar(12) NULL,
        zip_plus             char(4) NULL,
        country              Common_code_key,
        county               varchar(25) NULL,
        province             varchar(25) NULL,
        created_user_pk      User_pk,
        created_dt           datetime NOT NULL,
        lst_mod_user_pk      User_pk,
        lst_mod_dt           datetime NOT NULL,
        PRIMARY KEY (org_addr_pk), 
        FOREIGN KEY (org_locations_pk)
                              REFERENCES Org_Locations
                              ON DELETE CASCADE
 )
go
 
 
 CREATE TABLE Aff_Constitution (
        aff_pk               int NOT NULL,
        approved_const_fg    fg,
        approval_dt          datetime NULL,
        aff_agreement_dt     datetime NULL,
        auto_delegate_prvsn_fg fg,
        off_election_method  Common_code_key,
        most_current_approval_dt datetime NULL,
        const_regions_fg     fg,
        meeting_frequency    Common_code_key,
        aff_constitution_doc image NULL,
        const_doc_file_nm    varchar(50) NULL,
        const_doc_file_type  varchar(4) NULL,
        const_doc_file_content_type varchar(50) NULL,
        created_user_pk      User_pk,
        created_dt           datetime NOT NULL,
        lst_mod_user_pk      User_pk,
        lst_mod_dt           datetime NOT NULL,
        PRIMARY KEY (aff_pk), 
        FOREIGN KEY (aff_pk)
                              REFERENCES Aff_Organizations
 )
go
 
 
 CREATE TABLE Officer_History (
        person_pk            int NOT NULL,
        office_group_id      int NOT NULL,
        aff_pk               int NOT NULL,
        afscme_office_pk     int NOT NULL,
        pos_start_dt         datetime NOT NULL,
        officer_history_surrogate_key int IDENTITY,
        position_mbr_affiliation int NULL,
        pos_end_dt           datetime NULL,
        pos_expiration_dt    datetime NULL,
        pos_steward_fg       fg DEFAULT 0,
        office_card_sent_dt  datetime NULL,
        suspended_fg         fg NOT NULL,
        suspended_dt         datetime NULL,
        pos_addr_from_person_pk int NULL,
        pos_addr_from_org_pk int NULL,
        created_user_pk      User_pk,
        created_dt           datetime NOT NULL,
        lst_mod_user_pk      User_pk,
        lst_mod_dt           datetime NOT NULL,
        PRIMARY KEY NONCLUSTERED (person_pk, office_group_id, aff_pk, 
               afscme_office_pk, pos_start_dt), 
        FOREIGN KEY (aff_pk, afscme_office_pk, office_group_id)
                              REFERENCES Aff_Officer_Groups, 
        FOREIGN KEY (person_pk)
                              REFERENCES Person, 
        FOREIGN KEY (pos_addr_from_org_pk)
                              REFERENCES Org_Address, 
        FOREIGN KEY (pos_addr_from_person_pk)
                              REFERENCES Person_Address,
        UNIQUE (
               officer_history_surrogate_key
        )
 )
go
 
 
 CREATE TABLE Report_Output_Fields (
        report_pk            int NOT NULL,
        report_field_pk      int NOT NULL,
        field_output_order   smallint NOT NULL,
        PRIMARY KEY NONCLUSTERED (report_pk, report_field_pk), 
        FOREIGN KEY (report_field_pk)
                              REFERENCES Report_Fields, 
        FOREIGN KEY (report_pk)
                              REFERENCES Report
 )
go
 
 
 CREATE TABLE Time_Dim (
        time_pk              int IDENTITY,
        calendar_year        smallint NOT NULL,
        calendar_month       smallint NOT NULL,
        PRIMARY KEY NONCLUSTERED (time_pk)
 )
go
 
 
 CREATE TABLE Aff_Mbr_Activity (
        aff_pk               int NOT NULL,
        time_pk              int NOT NULL,
        membership_activity_type int NOT NULL,
        membership_activity_count int NOT NULL,
        PRIMARY KEY NONCLUSTERED (aff_pk, time_pk, 
               membership_activity_type), 
        FOREIGN KEY (aff_pk)
                              REFERENCES Aff_Organizations, 
        FOREIGN KEY (time_pk)
                              REFERENCES Time_Dim
 )
go
 
 
 CREATE TABLE Roles_Privileges (
        role_pk              int NOT NULL,
        privilege_key        varchar(100) NOT NULL,
        PRIMARY KEY NONCLUSTERED (role_pk, privilege_key), 
        FOREIGN KEY (privilege_key)
                              REFERENCES Privileges, 
        FOREIGN KEY (role_pk)
                              REFERENCES Roles
                              ON DELETE CASCADE
 )
go
 
 
 CREATE TABLE Roles_Reports (
        role_pk              int NOT NULL,
        report_pk            int NOT NULL,
        PRIMARY KEY NONCLUSTERED (role_pk, report_pk), 
        FOREIGN KEY (report_pk)
                              REFERENCES Report, 
        FOREIGN KEY (role_pk)
                              REFERENCES Roles
                              ON DELETE CASCADE
 )
go
 
 
 CREATE TABLE Roles_Report_Fields (
        role_pk              int NOT NULL,
        report_field_pk      int NOT NULL,
        PRIMARY KEY NONCLUSTERED (role_pk, report_field_pk), 
        FOREIGN KEY (report_field_pk)
                              REFERENCES Report_Fields, 
        FOREIGN KEY (role_pk)
                              REFERENCES Roles
                              ON DELETE CASCADE
 )
go
 
 
 CREATE TABLE Aff_Comments (
        aff_pk               int NOT NULL,
        comment_dt           datetime NOT NULL,
        comment_txt          varchar(254) NOT NULL,
        created_user_pk      User_pk,
        PRIMARY KEY NONCLUSTERED (aff_pk, comment_dt), 
        FOREIGN KEY (aff_pk)
                              REFERENCES Aff_Organizations
 )
go
 
 
 CREATE TABLE Aff_Office_Comments (
        aff_pk               int NOT NULL,
        comment_dt           datetime NOT NULL,
        comment_txt          varchar(254) NOT NULL,
        created_user_pk      User_pk,
        PRIMARY KEY NONCLUSTERED (aff_pk, comment_dt), 
        FOREIGN KEY (aff_pk)
                              REFERENCES Aff_Organizations
 )
go
 
 
 CREATE TABLE Mailing_Lists_by_Person (
        MLBP_mailing_list_pk int IDENTITY,
        MLBP_mailing_list_nm varchar(100) NOT NULL,
        dept                 Common_code_key,
        mailing_list_legacy_cd smallint NULL,
        created_user_pk      User_pk,
        created_dt           datetime NOT NULL,
        lst_mod_user_pk      User_pk,
        lst_mod_dt           datetime NOT NULL,
        PRIMARY KEY NONCLUSTERED (MLBP_mailing_list_pk)
 )
go
 
 
 CREATE TABLE Person_Correspondence_History (
        person_pk            int NOT NULL,
        Correspondence_dt    datetime NOT NULL,
        MLBP_mailing_list_pk int NULL,
        report_pk            int NULL,
        PRIMARY KEY NONCLUSTERED (person_pk, Correspondence_dt), 
        FOREIGN KEY (report_pk)
                              REFERENCES Report, 
        FOREIGN KEY (person_pk)
                              REFERENCES Person, 
        FOREIGN KEY (MLBP_mailing_list_pk)
                              REFERENCES Mailing_Lists_by_Person
 )
go
 
 
 CREATE TABLE MLBP_Persons (
        person_pk            int NOT NULL,
        MLBP_mailing_list_pk int NOT NULL,
        address_pk           int NULL,
        created_user_pk      User_pk,
        created_dt           datetime NULL,
        lst_mod_user_pk      User_pk,
        lst_mod_dt           datetime NULL,
        PRIMARY KEY NONCLUSTERED (person_pk, MLBP_mailing_list_pk), 
        FOREIGN KEY (person_pk)
                              REFERENCES Person, 
        FOREIGN KEY (address_pk)
                              REFERENCES Person_Address, 
        FOREIGN KEY (MLBP_mailing_list_pk)
                              REFERENCES Mailing_Lists_by_Person
 )
go
 
 
 CREATE TABLE Aff_Staff (
        aff_pk               int NOT NULL,
        person_pk            int NOT NULL,
        aff_employer_name    varchar(50) NULL,
        aff_staff_title      Common_code_key,
        staff_poc_for        Common_code_key,
        org_locations_pk     int NULL,
        created_user_pk      User_pk,
        created_dt           datetime NOT NULL,
        lst_mod_user_pk      User_pk,
        lst_mod_dt           datetime NOT NULL,
        PRIMARY KEY NONCLUSTERED (aff_pk, person_pk), 
        FOREIGN KEY (person_pk)
                              REFERENCES Person, 
        FOREIGN KEY (org_locations_pk)
                              REFERENCES Org_Locations, 
        FOREIGN KEY (aff_pk)
                              REFERENCES Aff_Organizations
 )
go
 
 
 CREATE TABLE Aff_Locals_Serviced_By_Staff (
        serviced_aff_pk      int NOT NULL,
        aff_pk               int NOT NULL,
        person_pk            int NOT NULL,
        created_user_pk      nullable_user_pk,
        created_dt           datetime NULL,
        lst_mod_user_pk      nullable_user_pk,
        lst_mod_dt           datetime NULL,
        PRIMARY KEY NONCLUSTERED (serviced_aff_pk, aff_pk, person_pk), 
        FOREIGN KEY (serviced_aff_pk)
                              REFERENCES Aff_Organizations, 
        FOREIGN KEY (aff_pk, person_pk)
                              REFERENCES Aff_Staff
                              ON DELETE CASCADE
 )
go
 
 
 CREATE TABLE Aff_Fin_Info (
        aff_pk               int NOT NULL,
        employer_identification_num char(9) NULL,
        per_cap_stat_avg     int NULL,
        per_cap_tax_payment_method varchar(20) NULL,
        per_cap_last_paid_dt datetime NULL,
        per_cap_tax_last_mbr_cnt int NULL,
        per_cap_tax_lst_info_upd_dt datetime NULL,
        comment_txt          varchar(254) NULL,
        created_user_pk      nullable_user_pk,
        created_dt           datetime NULL,
        lst_mod_user_pk      nullable_user_pk,
        lst_mod_dt           datetime NULL,
        PRIMARY KEY NONCLUSTERED (aff_pk), 
        FOREIGN KEY (aff_pk)
                              REFERENCES Aff_Organizations
 )
go
 
 
 CREATE TABLE Aff_Staff_Comments (
        aff_pk               int NOT NULL,
        person_pk            int NOT NULL,
        comment_dt           datetime NOT NULL,
        comment_txt          varchar(254) NOT NULL,
        created_user_pk      User_pk,
        PRIMARY KEY NONCLUSTERED (aff_pk, person_pk, comment_dt), 
        FOREIGN KEY (aff_pk, person_pk)
                              REFERENCES Aff_Staff
                              ON DELETE CASCADE
 )
go
 
 
 CREATE TABLE Aff_Chng_History_Dtl (
        aff_transaction_pk   int NOT NULL,
        aff_field_chnged     Common_code_key NOT NULL,
        old_value            varchar(254) NOT NULL,
        new_value            varchar(254) NOT NULL,
        PRIMARY KEY NONCLUSTERED (aff_transaction_pk, 
               aff_field_chnged), 
        FOREIGN KEY (aff_transaction_pk)
                              REFERENCES Aff_Chng_History
 )
go
 
 
 CREATE TABLE Participation_Cd_Group (
        particip_group_pk    int IDENTITY,
        particip_group_nm    varchar(20) NOT NULL,
        particip_group_desc  varchar(100) NULL,
        PRIMARY KEY NONCLUSTERED (particip_group_pk)
 )
go
 
 
 CREATE TABLE Participation_Cd_Type (
        particip_type_pk     int IDENTITY,
        particip_type_nm     varchar(20) NOT NULL,
        particip_type_desc   varchar(100) NULL,
        particip_group_pk    int NOT NULL,
        PRIMARY KEY NONCLUSTERED (particip_type_pk), 
        FOREIGN KEY (particip_group_pk)
                              REFERENCES Participation_Cd_Group
 )
go
 
 
 CREATE TABLE Participation_Cd_Dtl (
        particip_detail_pk   int IDENTITY,
        particip_detail_nm   varchar(20) NOT NULL,
        particip_detail_shortcut smallint NULL,
        particip_type_pk     int NOT NULL,
        particip_detail_desc varchar(100) NULL,
        particip_detail_status fg NOT NULL DEFAULT 1,
        created_user_pk      User_pk,
        created_dt           datetime NOT NULL,
        lst_mod_user_pk      User_pk,
        lst_mod_dt           datetime NOT NULL,
        PRIMARY KEY NONCLUSTERED (particip_detail_pk), 
        FOREIGN KEY (particip_type_pk)
                              REFERENCES Participation_Cd_Type,
        UNIQUE (
               particip_detail_shortcut
        )
 )
go
 
 
 CREATE TABLE Participation_Cd_Outcomes (
        particip_outcome_pk  int IDENTITY,
        particip_outcome_nm  varchar(20) NOT NULL,
        particip_outcome_desc varchar(100) NULL,
        created_user_pk      User_pk,
        created_dt           datetime NOT NULL,
        lst_mod_user_pk      User_pk,
        lst_mod_dt           datetime NOT NULL,
        PRIMARY KEY NONCLUSTERED (particip_outcome_pk)
 )
go
 
 
 CREATE TABLE Participation_Dtl_Outcomes (
        particip_detail_pk   int NOT NULL,
        particip_outcome_pk  int NOT NULL,
        PRIMARY KEY NONCLUSTERED (particip_detail_pk, 
               particip_outcome_pk), 
        FOREIGN KEY (particip_outcome_pk)
                              REFERENCES Participation_Cd_Outcomes, 
        FOREIGN KEY (particip_detail_pk)
                              REFERENCES Participation_Cd_Dtl
 )
go
 
 
 CREATE TABLE Member_Participation (
        particip_detail_pk   int NOT NULL,
        person_pk            int NOT NULL,
        particip_outcome_pk  int NULL,
        mbr_particip_dt      datetime NULL,
        comment_txt          varchar(254) NULL,
        created_user_pk      User_pk,
        created_dt           datetime NOT NULL,
        lst_mod_user_pk      User_pk,
        lst_mod_dt           datetime NOT NULL,
        PRIMARY KEY NONCLUSTERED (particip_detail_pk, person_pk), 
        FOREIGN KEY (particip_outcome_pk)
                              REFERENCES Participation_Cd_Outcomes, 
        FOREIGN KEY (particip_detail_pk)
                              REFERENCES Participation_Cd_Dtl, 
        FOREIGN KEY (person_pk)
                              REFERENCES Person
 )
go
 
 
 CREATE TABLE Aff_Off_Activity (
        aff_pk               int NOT NULL,
        time_pk              int NOT NULL,
        off_activity_type    Common_code_key NOT NULL,
        off_activity_cnt     int NOT NULL,
        PRIMARY KEY NONCLUSTERED (aff_pk, time_pk, off_activity_type), 
        FOREIGN KEY (aff_pk)
                              REFERENCES Aff_Organizations, 
        FOREIGN KEY (time_pk)
                              REFERENCES Time_Dim
 )
go
 
 
 CREATE TABLE COM_ZipCityState_Vld (
        zipcode              varchar(12) NOT NULL,
        state                char(2) NOT NULL,
        city                 varchar(32) NOT NULL,
        pref                 char(1) NOT NULL,
        type                 char(1) NULL,
        PRIMARY KEY NONCLUSTERED (zipcode, state, city, pref)
 )
go
 
 
 CREATE TABLE COM_App_Config_Data (
        app_var_pk           int IDENTITY,
        variable_name        varchar(25) NULL,
        variable_value       varchar(40) NOT NULL,
        prior_value          varchar(40) NOT NULL,
        created_user_pk      User_pk,
        created_dt           datetime NOT NULL,
        lst_mod_user_pk      User_pk,
        lst_mod_dt           datetime NOT NULL,
        PRIMARY KEY NONCLUSTERED (app_var_pk)
 )
go
 
 
 CREATE TABLE Person_Disability_Accmdtn (
        person_pk            int NOT NULL,
        disability_accmdtn   Common_code_key NOT NULL,
        comment_txt          varchar(254) NULL,
        created_user_pk      User_pk,
        created_dt           datetime NOT NULL,
        lst_mod_user_pk      User_pk,
        lst_mod_dt           datetime NOT NULL,
        PRIMARY KEY NONCLUSTERED (person_pk, disability_accmdtn), 
        FOREIGN KEY (person_pk)
                              REFERENCES Person
 )
go
 
 
 CREATE TABLE AUP_Pre_Err_Smry (
        aff_pk               int NOT NULL,
        queue_pk             int NOT NULL,
        record_id            int NOT NULL,
        person_pk            int NULL,
        last_nm              varchar(25) NULL,
        middle_nm            varchar(25) NULL,
        first_nm             varchar(25) NULL,
        suffix_nm            varchar(25) NULL,
        upd_error_type       Common_code_key,
        PRIMARY KEY NONCLUSTERED (aff_pk, queue_pk, record_id), 
        FOREIGN KEY (queue_pk)
                              REFERENCES AUP_Queue_Mgmt, 
        FOREIGN KEY (aff_pk)
                              REFERENCES Aff_Organizations, 
        FOREIGN KEY (person_pk)
                              REFERENCES Person
 )
go
 
 
 CREATE TABLE AUP_Pre_Err_Dtl (
        aff_pk               int NOT NULL,
        queue_pk             int NOT NULL,
        record_id            int NOT NULL,
        upd_fld_nm           Common_code_key NOT NULL,
        fld_value_in_system  varchar(100) NULL,
        fld_value_in_file    varchar(100) NULL,
        fld_error_fg         fg,
        PRIMARY KEY NONCLUSTERED (aff_pk, queue_pk, record_id, 
               upd_fld_nm), 
        FOREIGN KEY (aff_pk, queue_pk, record_id)
                              REFERENCES AUP_Pre_Err_Smry
 )
go
 
 
 CREATE TABLE AUP_Rv_Smry (
        aff_pk               int NOT NULL,
        queue_pk             int NOT NULL,
        trans_cmpltd_cnt     int NOT NULL,
        trans_attempted_cnt  int NOT NULL,
        trans_err_cnt        int NOT NULL,
        adds_attempted_cnt   int NOT NULL,
        adds_cmpltd_cnt      int NOT NULL,
        inactReplace_attempted_cnt int NOT NULL,
        inactReplace_cmpltd_cnt int NOT NULL,
        inactReplace_madeTemp_cnt int NOT NULL,
        chg_attempted_cnt    int NOT NULL,
        chg_cmpltd_cnt       int NOT NULL,
        vacant_attempted_cnt int NOT NULL,
        vacant_cmpltd_cnt    int NOT NULL,
        PRIMARY KEY NONCLUSTERED (aff_pk, queue_pk), 
        FOREIGN KEY (queue_pk)
                              REFERENCES AUP_Queue_Mgmt, 
        FOREIGN KEY (aff_pk)
                              REFERENCES Aff_Organizations
 )
go
 
 
 CREATE TABLE AUP_Rv_Err_Smry (
        aff_pk               int NOT NULL,
        queue_pk             int NOT NULL,
        record_id            int NOT NULL,
        person_pk            int NULL,
        last_nm              varchar(25) NULL,
        middle_nm            varchar(25) NULL,
        first_nm             varchar(25) NULL,
        suffix_nm            varchar(25) NULL,
        fld_error_type       Common_code_key,
        PRIMARY KEY NONCLUSTERED (aff_pk, queue_pk, record_id), 
        FOREIGN KEY (person_pk)
                              REFERENCES Person, 
        FOREIGN KEY (aff_pk, queue_pk)
                              REFERENCES AUP_Rv_Smry
 )
go
 
 
 CREATE TABLE AUP_Rv_Err_Dtl (
        aff_pk               int NOT NULL,
        queue_pk             int NOT NULL,
        record_id            int NOT NULL,
        upd_fld_nm           Common_code_key NOT NULL,
        fld_value_in_system  varchar(100) NULL,
        fld_value_in_file    varchar(100) NULL,
        fld_error_fg         fg,
        PRIMARY KEY NONCLUSTERED (aff_pk, queue_pk, record_id, 
               upd_fld_nm), 
        FOREIGN KEY (aff_pk, queue_pk, record_id)
                              REFERENCES AUP_Rv_Err_Smry
 )
go
 
 
 CREATE TABLE COM_Weekly_Mbr_Card_Run (
        person_pk            int NOT NULL,
        aff_pk               int NOT NULL,
        created_user_pk      nullable_user_pk,
        created_dt           datetime NULL,
        lst_mod_user_pk      nullable_user_pk,
        lst_mod_dt           datetime NULL,
        PRIMARY KEY NONCLUSTERED (person_pk, aff_pk), 
        FOREIGN KEY (aff_pk, person_pk)
                              REFERENCES Aff_Members
 )
go
 
 
 CREATE TABLE AMC_Control_Panel (
        aff_ann_mbr_card_run_group Common_code_key NOT NULL,
        currently_selected_fg fg,
        yearly_card_run_dt   datetime NULL,
        PRIMARY KEY NONCLUSTERED (aff_ann_mbr_card_run_group)
 )
go
 
 
 CREATE TABLE COM_Aff_Special_Text (
        aff_pk               int NOT NULL,
        special_cc_run_txt   varchar(75) NULL,
        created_user_pk      nullable_user_pk,
        created_dt           datetime NULL,
        lst_mod_user_pk      nullable_user_pk,
        lst_mod_dt           datetime NULL,
        PRIMARY KEY NONCLUSTERED (aff_pk), 
        FOREIGN KEY (aff_pk)
                              REFERENCES Aff_Organizations
 )
go
 
 
 CREATE TABLE AMC_Group_Completed_Info (
        aff_pk               int NOT NULL,
        aff_ann_mbr_card_run_group Common_code_key NOT NULL,
        amc_card_run_dt      datetime NULL,
        PRIMARY KEY NONCLUSTERED (aff_pk, aff_ann_mbr_card_run_group), 
        FOREIGN KEY (aff_pk)
                              REFERENCES Aff_Organizations, 
        FOREIGN KEY (aff_ann_mbr_card_run_group)
                              REFERENCES AMC_Control_Panel
 )
go
 
 
 CREATE TABLE COM_Democracy_Ranged (
        democracy_ranged_pk  int IDENTITY,
        zipcode              char(5) NULL,
        start_zip_plus       char(4) NULL,
        stop_zip_plus        char(4) NULL,
        state                char(2) NULL,
        countyfips           char(3) NULL,
        congdist             char(2) NULL,
        upperdist            char(3) NULL,
        lowerdist            char(3) NULL,
        PRIMARY KEY NONCLUSTERED (democracy_ranged_pk)
 )
go
 
 
 CREATE TABLE NCOA_Activity (
        ncoa_activity_pk     int IDENTITY,
        ncoa_activity_requested_dt datetime NOT NULL,
        ncoa_trans_run_completed_fg fg NOT NULL DEFAULT 0,
        ncoa_trans_run_error_fg fg NOT NULL DEFAULT 0,
        ncoa_trans_run_dt    datetime NULL,
        created_user_pk      User_pk,
        PRIMARY KEY NONCLUSTERED (ncoa_activity_pk)
 )
go
 
 
 CREATE TABLE NCOA_Activity_Dtl (
        ncoa_activity_pk     int NOT NULL,
        address_pk           int NOT NULL,
        person_pk            int NULL,
        standard_trans_cd    varchar(2) NULL,
        ncoa_error_cd_pk     int NULL,
        PRIMARY KEY NONCLUSTERED (ncoa_activity_pk, address_pk), 
        FOREIGN KEY (ncoa_error_cd_pk)
                              REFERENCES NCOA_Error_Cds, 
        FOREIGN KEY (person_pk)
                              REFERENCES NCOA_Transactions, 
        FOREIGN KEY (address_pk)
                              REFERENCES Person_Address, 
        FOREIGN KEY (ncoa_activity_pk)
                              REFERENCES NCOA_Activity
 )
go
 
 
 CREATE TABLE User_Affiliates (
        person_pk            int NOT NULL,
        aff_pk               int NOT NULL,
        PRIMARY KEY NONCLUSTERED (person_pk, aff_pk), 
        FOREIGN KEY (aff_pk)
                              REFERENCES Aff_Organizations, 
        FOREIGN KEY (person_pk)
                              REFERENCES Users
 )
go
 
 
 CREATE TABLE COM_AFL_CIO_COPE (
        person_pk            int NOT NULL,
        gender               Common_code_key,
        ethnic_origin        Common_code_key,
        county               varchar(25) NULL,
        political_registered_voter Common_code_key,
        congdist             char(2) NULL,
        upperdist            char(2) NULL,
        lowerdist            char(2) NULL,
        ward_number          char(4) NULL,
        precinct_number      char(10) NULL,
        precinct_name        varchar(25) NULL,
        political_party      Common_code_key,
        PRIMARY KEY NONCLUSTERED (person_pk), 
        FOREIGN KEY (person_pk)
                              REFERENCES Person
 )
go
 
 
 CREATE TABLE Ext_Org_Associates (
        org_pk               int NOT NULL DEFAULT 0,
        person_pk            int NOT NULL,
        org_locations_pk     int NULL,
        org_pos_title        Common_code_key,
        created_user_pk      User_pk,
        created_dt           datetime NOT NULL,
        lst_mod_user_pk      User_pk,
        lst_mod_dt           datetime NOT NULL,
        PRIMARY KEY NONCLUSTERED (org_pk, person_pk), 
        FOREIGN KEY (org_locations_pk)
                              REFERENCES Org_Locations, 
        FOREIGN KEY (person_pk)
                              REFERENCES Person, 
        FOREIGN KEY (org_pk)
                              REFERENCES External_Organizations
 )
go
 
 
 CREATE TABLE Ext_Org_Associate_Comments (
        org_pk               int NOT NULL DEFAULT 0,
        person_pk            int NOT NULL,
        comment_dt           datetime NOT NULL,
        comment_txt          varchar(254) NOT NULL,
        created_user_pk      User_pk,
        PRIMARY KEY NONCLUSTERED (org_pk, person_pk, comment_dt), 
        FOREIGN KEY (org_pk, person_pk)
                              REFERENCES Ext_Org_Associates
                              ON DELETE CASCADE
 )
go
 
 
 CREATE TABLE COM_Officer_Card_Run (
        officer_history_surrogate_key int NOT NULL,
        aff_pk               int NOT NULL,
        person_pk            int NOT NULL,
        created_user_pk      nullable_user_pk,
        created_dt           datetime NULL,
        lst_mod_user_pk      nullable_user_pk,
        lst_mod_dt           datetime NULL,
        PRIMARY KEY NONCLUSTERED (officer_history_surrogate_key), 
        FOREIGN KEY (person_pk)
                              REFERENCES Person, 
        FOREIGN KEY (aff_pk)
                              REFERENCES Aff_Organizations
 )
go
 
 
 CREATE TABLE NCOA_Transaction_Cds (
        trans_cd_pk          int IDENTITY,
        standard_trans_cd    varchar(2) NOT NULL,
        mapped_trans_cd      varchar(10) NULL,
        standard_trans_cd_desc varchar(50) NULL,
        PRIMARY KEY NONCLUSTERED (trans_cd_pk)
 )
go
 
 
 CREATE TABLE COM_App_Log (
        event_nm             varchar(30) NOT NULL,
        event_logged_dt      datetime NOT NULL,
        event_user_id        varchar(10) NULL,
        event_data_string    varchar(5000) NULL,
        PRIMARY KEY NONCLUSTERED (event_nm, event_logged_dt)
 )
go
 
 