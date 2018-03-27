  
 CREATE INDEX XIE1Aff_Admin_CouncilsRegCouncilPk ON Aff_Admin_Councils
 (
        regular_council_aff_pk
 )
go
 
 CREATE INDEX XIF215Aff_Charter_County ON Aff_Charter_County
 (
        aff_pk
 )
go
 
 CREATE INDEX XIF140Aff_Chng_History ON Aff_Chng_History
 (
        aff_pk
 )
go
 
 CREATE INDEX XIF139Aff_Chng_History_Dtl ON Aff_Chng_History_Dtl
 (
        aff_transaction_pk
 )
go
 
 CREATE INDEX XIF153Aff_Chng_History_Juris_Dtl ON Aff_Chng_History_Juris_Dtl
 (
        aff_transaction_pk
 )
go
 
 CREATE INDEX XIF136Aff_Comments ON Aff_Comments
 (
        aff_pk
 )
go
 
 CREATE INDEX XIF140Aff_Employer_Sector ON Aff_Employer_Sector
 (
        aff_pk
 )
go
 
 CREATE INDEX XIF131Aff_Locals_Serviced_By_Staff ON Aff_Locals_Serviced_By_Staff
 (
        person_pk,
        aff_pk
 )
go
 
 CREATE INDEX XIF133Aff_Locals_Serviced_By_Staff ON Aff_Locals_Serviced_By_Staff
 (
        serviced_aff_pk
 )
go
 
 CREATE INDEX XIF134Aff_Mbr_Activity ON Aff_Mbr_Activity
 (
        aff_pk
 )
go
 
 CREATE INDEX XIF99Aff_Mbr_Activity ON Aff_Mbr_Activity
 (
        time_pk
 )
go
 
 CREATE INDEX XIF134Aff_Members ON Aff_Members
 (
        aff_pk
 )
go
 
 CREATE INDEX XIE1Aff_MembersPersonPk ON Aff_Members
 (
        person_pk
 )
go
 
 CREATE INDEX XAK1Aff_MembersMbrTypeAffPk ON Aff_Members
 (
        mbr_type,
        aff_pk
 )
go
 
 CREATE INDEX XIE3Aff_MembersMbrStatus ON Aff_Members
 (
        mbr_status
 )
go
 
 CREATE INDEX XIF149Aff_Multi_State ON Aff_Multi_State
 (
        aff_pk
 )
go
 
 CREATE INDEX XIF159Aff_Off_Activity ON Aff_Off_Activity
 (
        time_pk
 )
go
 
 CREATE INDEX XIF217Aff_Off_Activity ON Aff_Off_Activity
 (
        aff_pk
 )
go
 
 CREATE INDEX XIF173Aff_Office_Comments ON Aff_Office_Comments
 (
        aff_pk
 )
go
 
 CREATE INDEX XIF219Aff_Officer_Groups ON Aff_Officer_Groups
 (
        aff_pk
 )
go
 
 CREATE INDEX XIF220Aff_Officer_Groups ON Aff_Officer_Groups
 (
        afscme_office_pk
 )
go
 
 CREATE INDEX XIF215Aff_Organizations ON Aff_Organizations
 (
        new_aff_id_src
 )
go
 
 CREATE INDEX XIF217Aff_Organizations ON Aff_Organizations
 (
        auto_eboard_parent_title
 )
go
 
 CREATE INDEX XIF219Aff_Organizations ON Aff_Organizations
 (
        auto_eboard_aff_title
 )
go
 
 CREATE INDEX XIE1Aff_OrganizationsParentAff ON Aff_Organizations
 (
        parent_aff_fk,
        aff_pk
 )
go
 
 CREATE INDEX XAK2Aff_OrganizationsLocalStateNat ON Aff_Organizations
 (
        aff_localSubChapter,
        aff_stateNat_type
 )
go
 
 CREATE INDEX XIE3Aff_OrganizationsCouncil ON Aff_Organizations
 (
        aff_councilRetiree_chap
 )
go
 
 CREATE INDEX XIF125Aff_Staff ON Aff_Staff
 (
        person_pk
 )
go
 
 CREATE INDEX XIF129Aff_Staff ON Aff_Staff
 (
        aff_pk
 )
go
 
 CREATE INDEX XIF140Aff_Staff ON Aff_Staff
 (
        org_locations_pk
 )
go
 
 CREATE INDEX XIE1Aff_StaffPersonPk ON Aff_Staff
 (
        person_pk
 )
go
 
 CREATE INDEX XIF139Aff_Staff_Comments ON Aff_Staff_Comments
 (
        person_pk,
        aff_pk
 )
go
 
 CREATE INDEX XIF151AMC_Group_Completed_Info ON AMC_Group_Completed_Info
 (
        aff_ann_mbr_card_run_group
 )
go
 
 CREATE INDEX XIF152AMC_Group_Completed_Info ON AMC_Group_Completed_Info
 (
        aff_pk
 )
go
 
 CREATE INDEX XIF131AUP_Member_Pre_Upd_Smry ON AUP_Member_Pre_Upd_Smry
 (
        queue_pk
 )
go
 
 CREATE INDEX XIF132AUP_Member_Pre_Upd_Smry ON AUP_Member_Pre_Upd_Smry
 (
        aff_pk
 )
go
 
 CREATE INDEX XIF204AUP_Officer_Pre_Off_Dtl ON AUP_Officer_Pre_Off_Dtl
 (
        aff_pk
 )
go
 
 CREATE INDEX XIF205AUP_Officer_Pre_Off_Dtl ON AUP_Officer_Pre_Off_Dtl
 (
        queue_pk
 )
go
 
 CREATE INDEX XIE1AUP_Officer_Pre_Off_Dtl ON AUP_Officer_Pre_Off_Dtl
 (
        aff_pk,
        queue_pk
 )
go
 
 CREATE INDEX XIF133AUP_Officer_Pre_Upd_Smry ON AUP_Officer_Pre_Upd_Smry
 (
        queue_pk
 )
go
 
 CREATE INDEX XIF134AUP_Officer_Pre_Upd_Smry ON AUP_Officer_Pre_Upd_Smry
 (
        aff_pk
 )
go
 
 CREATE INDEX XIF141AUP_Pre_Err_Dtl ON AUP_Pre_Err_Dtl
 (
        aff_pk,
	  queue_pk,        
	  record_id
 )
go
 
 CREATE INDEX XIF104AUP_Pre_Err_Smry ON AUP_Pre_Err_Smry
 (
        person_pk
 )
go
 
  
 CREATE INDEX XIF131AUP_Pre_Err_Smry ON AUP_Pre_Err_Smry
 (
        queue_pk
 )
go
 
 CREATE INDEX XIF135AUP_Pre_Fld_Chg_Smry ON AUP_Pre_Fld_Chg_Smry
 (
        queue_pk
 )
go
 
 CREATE INDEX XIF214AUP_Queue_Mgmt ON AUP_Queue_Mgmt
 (
        aff_pk
 )
go
 
 CREATE INDEX XIF215AUP_Queue_Mgmt ON AUP_Queue_Mgmt
 (
        org_pk
 )
go
  
 CREATE INDEX XIF129AUP_Rebate_Pre_Upd_Smry ON AUP_Rebate_Pre_Upd_Smry
 (
        queue_pk
 )
go
 
 CREATE INDEX XIF130AUP_Rebate_Pre_Upd_Smry ON AUP_Rebate_Pre_Upd_Smry
 (
        aff_pk
 )
go
 
 CREATE INDEX XIF129AUP_Rv_Err_Dtl ON AUP_Rv_Err_Dtl
 (
        aff_pk,
	  queue_pk,
        record_id
 )
go
 
 CREATE INDEX XIF115AUP_Rv_Err_Smry ON AUP_Rv_Err_Smry
 (
        person_pk
 )
go
 
 CREATE INDEX XIF127AUP_Rv_Err_Smry ON AUP_Rv_Err_Smry
 (
        queue_pk,
        aff_pk
 )
go
 
 CREATE INDEX XIF125AUP_Rv_Smry ON AUP_Rv_Smry
 (
        aff_pk
 )
go
 
 CREATE INDEX XIF126AUP_Rv_Smry ON AUP_Rv_Smry
 (
        queue_pk
 )
go
 
 CREATE INDEX XIF173COM_Application_Notification ON COM_Application_Notification
 (
        application_function_pk
 )
go
 
 CREATE INDEX XIF212COM_Application_Notification ON COM_Application_Notification
 (
        email_pk
 )
go
 
 CREATE INDEX XIE1COM_Democracy_RangedZipStartStop ON COM_Democracy_Ranged
 (
        zipcode,
        start_zip_plus,
        stop_zip_plus,
        countyfips
 )
go
 
 CREATE INDEX XIF173COM_Officer_Card_Run ON COM_Officer_Card_Run
 (
        aff_pk
 )
go
 
 CREATE INDEX XIF174COM_Officer_Card_Run ON COM_Officer_Card_Run
 (
        person_pk
 )
go
 
 CREATE INDEX XIF115Common_Code_Type ON Common_Code_Type
 (
        category_fk
 )
go
 
 CREATE INDEX XIF130Common_Codes ON Common_Codes
 (
        com_cd_type_key
 )
go
 
 CREATE INDEX XIE1Common_CodesPkDesc ON Common_Codes
 (
        com_cd_pk,
        com_cd_desc
 )
go
 
 CREATE INDEX XIF114Criteria_Codevalue ON Criteria_Codevalue
 (
        report_pk,
        report_field_pk,
        report_criterion_sequence_pk
 )
go
 
 CREATE INDEX XIF121Criteria_Codevalue ON Criteria_Codevalue
 (
        com_cd_pk
 )
go
 
 CREATE INDEX XIF165Ext_Org_Associate_Comments ON Ext_Org_Associate_Comments
 (
        org_pk,
        person_pk
 )
go
 
  
 CREATE INDEX XIF164Ext_Org_Associates ON Ext_Org_Associates
 (
        person_pk
 )
go
 
 CREATE INDEX XIF170Ext_Org_Associates ON Ext_Org_Associates
 (
        org_locations_pk
 )
go
 
 CREATE INDEX XIE1Ext_Org_AssociatesPersonPk ON Ext_Org_Associates
 (
        person_pk
 )
go
 
 CREATE INDEX XIE1External_OrganizationsOrgNm ON External_Organizations
 (
        org_nm
 )
go
 
 CREATE INDEX XIF136Mailing_List_Orgs ON Mailing_List_Orgs
 (
        MLBO_mailing_list_pk
 )
go
 
 CREATE INDEX XIF178Mailing_List_Orgs ON Mailing_List_Orgs
 (
        org_locations_pk
 )
go
 
 CREATE INDEX XIE1Mailing_List_OrgsOrgPk ON Mailing_List_Orgs
 (
        org_pk
 )
go
 
 CREATE INDEX XIF175Mass_Change_Batch_Control ON Mass_Change_Batch_Control
 (
        aff_pk
 )
go
 
 CREATE INDEX XIF217Member_Participation ON Member_Participation
 (
        particip_detail_pk
 )
go
 
 CREATE INDEX XIF218Member_Participation ON Member_Participation
 (
        particip_outcome_pk
 )
go
 
 CREATE INDEX XIF219Member_Participation ON Member_Participation
 (
        person_pk
 )
go
 
 CREATE INDEX XIF130MLBP_Persons ON MLBP_Persons
 (
        MLBP_mailing_list_pk
 )
go
  
 CREATE INDEX XIF139MLBP_Persons ON MLBP_Persons
 (
        person_pk
 )
go
 
 CREATE INDEX XIE1MLBP_PersonsAddressPk ON MLBP_Persons
 (
        address_pk
 )
go
 
 CREATE INDEX XIF156NCOA_Activity_Dtl ON NCOA_Activity_Dtl
 (
        address_pk
 )
go
 
 CREATE INDEX XIF157NCOA_Activity_Dtl ON NCOA_Activity_Dtl
 (
        ncoa_activity_pk
 )
go
 
 CREATE INDEX XIF158NCOA_Activity_Dtl ON NCOA_Activity_Dtl
 (
        person_pk
 )
go
 
 
 CREATE INDEX XIF154NCOA_Transactions ON NCOA_Transactions
 (
        address_pk
 )
go
 
CREATE INDEX XIF132Officer_History ON Officer_History
 (
        person_pk
 )
go
 
 CREATE INDEX XIF163Officer_History ON Officer_History
 (
      person_pk,  
	position_mbr_affiliation
)
go
 
 CREATE INDEX XIF217Officer_History ON Officer_History
 (
        aff_pk,
        afscme_office_pk,
	  office_group_id
 )
go
 
 CREATE INDEX XIE1Officer_HistoryAffPkPosEnd ON Officer_History
 (
        aff_pk,
        pos_end_dt
 )
go
 
 CREATE INDEX XIF219Org_Address ON Org_Address
 (
        org_locations_pk
 )
go
 
 CREATE INDEX XIE1Org_AddressStateCity ON Org_Address
 (
        state,
        city
 )
go
 
 CREATE INDEX XIE2Org_AddressZip ON Org_Address
 (
        zipcode,
        zip_plus
 )
go
 
 CREATE INDEX XIE3Org_AddressCityState ON Org_Address
 (
        city,
        state
 )
go
 
  
 CREATE INDEX XIE1Org_Locations_org_pk ON Org_Locations
 (
        org_pk,
        location_primary_fg
 )
go
  
 CREATE INDEX XIE1Org_PhoneOrgLocations ON Org_Phone
 (
        org_locations_pk
 )
go
 
 CREATE INDEX XIF144Participation_Cd_Dtl ON Participation_Cd_Dtl
 (
        particip_type_pk
 )
go
 
 CREATE INDEX XIF143Participation_Cd_Type ON Participation_Cd_Type
 (
        particip_group_pk
 )
go
 
 CREATE INDEX XIF145Participation_Dtl_Outcomes ON Participation_Dtl_Outcomes
 (
        particip_detail_pk
 )
go
 
 CREATE INDEX XIF216Participation_Dtl_Outcomes ON Participation_Dtl_Outcomes
 (
        particip_outcome_pk
 )
go
 
 CREATE INDEX XIF112Pended_Entities ON Pended_Entities
 (
        person_pk
 )
go
 
 CREATE INDEX XIF91Pended_Entities ON Pended_Entities
 (
        report_pk
 )
go
 
 CREATE INDEX XIE1PersonNames ON Person
 (
        last_nm,
        first_nm,
        middle_nm
 )
go
 
 CREATE INDEX XIE2PersonSSN ON Person
 (
        ssn
 )
go
 
 CREATE INDEX XIE3PersonSuffixNm ON Person
 (
        suffix_nm
 )
go
 
 CREATE INDEX XIE1Person_AddressStateCity ON Person_Address
 (
        state,
        city
 )
go
 
 CREATE INDEX XIE2Person_AddressCity ON Person_Address
 (
        city
 )
go
 
 CREATE INDEX XIE3Person_AddressPersonPk ON Person_Address
 (
        person_pk
 )
go
 
 CREATE INDEX XIF130Person_Comments ON Person_Comments
 (
        person_pk
 )
go
 
 CREATE INDEX XIF129Person_Correspondence_History ON Person_Correspondence_History
 (
        MLBP_mailing_list_pk
 )
go
 
 CREATE INDEX XIF138Person_Correspondence_History ON Person_Correspondence_History
 (
        person_pk
 )
go
 
 CREATE INDEX XIF214Person_Correspondence_History ON Person_Correspondence_History
 (
        report_pk
 )
go
 
 CREATE INDEX XIF126Person_Disabilities ON Person_Disabilities
 (
        person_pk
 )
go
 
 CREATE INDEX XIF127Person_Disability_Accmdtn ON Person_Disability_Accmdtn
 (
        person_pk
 )
go

 CREATE INDEX XIE1Person_EmailPersonPk ON Person_Email
 (
        person_pk
 )
go
 
 CREATE INDEX XIF128Person_Language ON Person_Language
 (
        person_pk
 )
go

 CREATE INDEX XIE1Person_PhonePersonPk ON Person_Phone
 (
        person_pk
 )
go
 
 CREATE INDEX XIF131Person_Relation ON Person_Relation
 (
        person_pk
 )
go
  
 CREATE INDEX XIF156Person_SMA ON Person_SMA
 (
        person_pk
 )
go
 
 CREATE INDEX XIE1Person_SMAPersonPkCurrent ON Person_SMA
 (
        person_pk,
        current_fg,
        sequence
 )
go
 
 CREATE INDEX XIE2Person_SMAaddress_pk ON Person_SMA
 (
        address_pk
 )
go
 
 CREATE INDEX XIF151PRB_App_Affs ON PRB_App_Affs
 (
        prb_app_pk
 )
go
 
 CREATE INDEX XIF152PRB_App_Affs ON PRB_App_Affs
 (
        aff_pk
 )
go
 
 CREATE INDEX XIE1PRB_App_AffsAppPk ON PRB_App_Affs
 (
        prb_app_pk
 )
go
 
 CREATE INDEX XIF144PRB_Rbt_Year_Info ON PRB_Rbt_Year_Info
 (
        person_pk
 )
go
 
 CREATE INDEX XIF143PRB_Rebate_Check_Info ON PRB_Rebate_Check_Info
 (
        person_pk
 )
go
 
 CREATE INDEX XIF147PRB_Request_Affs ON PRB_Request_Affs
 (
        aff_pk
 )
go
 
 CREATE INDEX XIE1PRB_Request_AffsRequestPk ON PRB_Request_Affs
 (
        rqst_pk
 )
go
 
 CREATE INDEX XIF150PRB_Requests ON PRB_Requests
 (
        prb_app_pk
 )
go
 
 CREATE INDEX XIF218PRB_Requests ON PRB_Requests
 (
        person_pk
 )
go
 
 CREATE INDEX XIF153PRB_Roster_Persons ON PRB_Roster_Persons
 (
        aff_pk
 )
go
 
 CREATE INDEX XIF157PRB_Roster_Persons ON PRB_Roster_Persons
 (
        rbt_year,
        person_pk
 )
go
 
 CREATE INDEX XIE1PRB_Roster_PersonsPersonPk ON PRB_Roster_Persons
 (
        person_pk
 )
go
 
 CREATE INDEX XIF140Report_Field_Aggregate ON Report_Field_Aggregate
 (
        child_field_pk
 )
go
 
 CREATE INDEX XIF141Report_Field_Aggregate ON Report_Field_Aggregate
 (
        parent_field_pk
 )
go
 
 CREATE INDEX XIF117Report_Fields ON Report_Fields
 (
        com_cd_type_key
 )
go
 
 CREATE INDEX XIF92Report_OrderByFields ON Report_OrderByFields
 (
        report_pk
 )
go
 
 CREATE INDEX XIF93Report_OrderByFields ON Report_OrderByFields
 (
        report_field_pk
 )
go
 
 CREATE INDEX XIF86Report_Output_Fields ON Report_Output_Fields
 (
        report_pk
 )
go
 
 CREATE INDEX XIF87Report_Output_Fields ON Report_Output_Fields
 (
        report_field_pk
 )
go
 
 CREATE INDEX XIF120Report_Selection_Criteria ON Report_Selection_Criteria
 (
        report_pk
 )
go
 
 CREATE INDEX XIF121Report_Selection_Criteria ON Report_Selection_Criteria
 (
        report_field_pk
 )
go
 
 CREATE INDEX XIF122Report_Sort_Fields ON Report_Sort_Fields
 (
        report_field_pk
 )
go
 
 CREATE INDEX XIF124Report_Sort_Fields ON Report_Sort_Fields
 (
        report_pk
 )
go
 
 CREATE INDEX XIF101Roles_Privileges ON Roles_Privileges
 (
        role_pk
 )
go
 
 CREATE INDEX XIF85Roles_Privileges ON Roles_Privileges
 (
        privilege_key
 )
go
 
 CREATE INDEX XIF106Roles_Report_Fields ON Roles_Report_Fields
 (
        role_pk
 )
go
 
 CREATE INDEX XIF107Roles_Report_Fields ON Roles_Report_Fields
 (
        report_field_pk
 )
go
 
 CREATE INDEX XIF103Roles_Reports ON Roles_Reports
 (
        role_pk
 )
go
 
 CREATE INDEX XIF104Roles_Reports ON Roles_Reports
 (
        report_pk
 )
go
 
 CREATE INDEX XIF155User_Affiliates ON User_Affiliates
 (
        person_pk
 )
go
 
 CREATE INDEX XIF156User_Affiliates ON User_Affiliates
 (
        aff_pk
 )
go
 
 CREATE INDEX XIF100User_Roles ON User_Roles
 (
        role_pk
 )
go
 
 CREATE INDEX XIF82User_Roles ON User_Roles
 (
        person_pk
 )
go
 
 CREATE INDEX XIE1UsersUserId ON Users
 (
        user_id
 )
go
 
 