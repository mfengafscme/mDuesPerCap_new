declare @report_id int

SET IDENTITY_INSERT Report ON

INSERT INTO Report
(report_pk , report_nm , report_desc , report_pend_entity_fg, report_mailing_list_fg , report_custom_fg , report_update_corresp_fg , report_handler_class , report_is_count_fg , report_last_update_dt , report_last_update_uid , report_owner_pk , report_category )
VALUES (2, 'Person Mailing List by Mail Code', 'Generate a mailing list for a specific mailing list code', 0, 1, 0, 0, "/Reporting/Specialized/MailingListInput.jsp", 0, getdate(), 'admin', 1, 'Membership')

INSERT INTO Report
(report_pk , report_nm , report_desc , report_pend_entity_fg, report_mailing_list_fg , report_custom_fg , report_update_corresp_fg , report_handler_class , report_is_count_fg , report_last_update_dt , report_last_update_uid , report_owner_pk , report_category )
VALUES (3, 'Political Rebate Application File', 'Prepare information for printing onto Rebate Applications.', 0, 0, 0, 0, "/Reporting/Specialized/PRBApplicationFileInput.jsp", 0, getdate(), 'admin', 1, 'Membership')

INSERT INTO Report
(report_pk , report_nm , report_desc , report_pend_entity_fg, report_mailing_list_fg , report_custom_fg , report_update_corresp_fg , report_handler_class , report_is_count_fg , report_last_update_dt , report_last_update_uid , report_owner_pk , report_category )
VALUES (4, 'Preliminary Roster, Rebate Update File', 'This report provides a listing of the individuals that have requested Political Rebates for Membership within the selected Affiliate.', 0, 0, 0, 0, "/viewPreliminaryRosterAffiliate.action", 0, getdate(), 'admin', 1, 'Membership')

INSERT INTO Report
(report_pk , report_nm , report_desc , report_pend_entity_fg, report_mailing_list_fg , report_custom_fg , report_update_corresp_fg , report_handler_class , report_is_count_fg , report_last_update_dt , report_last_update_uid , report_owner_pk , report_category )
VALUES (5, 'Rebate Check File, Check Register, Final Roster', 'This report provides a listing of the individuals that are getting checks, a listing of the members that have received Rebate checks, and the final listing of the individuals that were given a Rebate for the current rebate year.', 0, 0, 0, 0, "/Reporting/Specialized/RebateCheckFileInput.jsp", 0, getdate(), 'admin', 1, 'Membership')

INSERT INTO Report
(report_pk , report_nm , report_desc , report_pend_entity_fg, report_mailing_list_fg , report_custom_fg , report_update_corresp_fg , report_handler_class , report_is_count_fg , report_last_update_dt , report_last_update_uid , report_owner_pk , report_category )
VALUES (6, 'Membership Activity', 'Shows membership activity for the current year', 0, 0, 0, 0, "/membershipActivityReport.action", 0, getdate(), 'admin', 1, 'Membership')

INSERT INTO Report
(report_pk , report_nm , report_desc , report_pend_entity_fg, report_mailing_list_fg , report_custom_fg , report_update_corresp_fg , report_handler_class , report_is_count_fg , report_last_update_dt , report_last_update_uid , report_owner_pk , report_category )
VALUES (7, 'Officer Credential Cards', 'Officer Credential Cards', 0, 0, 0, 0, "/Reporting/Specialized/OfficerCredentialCardsInput.jsp", 0, getdate(), 'admin', 1, 'Membership')

INSERT INTO Report
(report_pk , report_nm , report_desc , report_pend_entity_fg, report_mailing_list_fg , report_custom_fg , report_update_corresp_fg , report_handler_class , report_is_count_fg , report_last_update_dt , report_last_update_uid , report_owner_pk , report_category )
VALUES (8, 'Org Mailing List', 'Shows a list of organizations within a given mailing list', 0, 1, 0, 0, "/Reporting/Specialized/OrgMailingListInput.jsp", 0, getdate(), suser_sname(), 1, 'Membership')

INSERT INTO Report
(report_pk , report_nm , report_desc , report_pend_entity_fg, report_mailing_list_fg , report_custom_fg , report_update_corresp_fg , report_handler_class , report_is_count_fg , report_last_update_dt , report_last_update_uid , report_owner_pk , report_category )
VALUES (9, 'Affiliate Counts by Status', 'This report provides a listing of affiliates by affiliate status.', 0, 0, 0, 0, "/Reporting/Specialized/AffiliateCountByStatus.jsp", 0, getdate(), 'admin', 1, 'Membership')

INSERT INTO Report
(report_pk , report_nm , report_desc , report_pend_entity_fg, report_mailing_list_fg , report_custom_fg , report_update_corresp_fg , report_handler_class , report_is_count_fg , report_last_update_dt , report_last_update_uid , report_owner_pk , report_category )
VALUES (10, 'Officer Expiration Listing', 'This report provides a listing of the officers with current term expiration date and next available term expiration date in state, council and local order.', 0, 0, 0, 0, "/officerExpirationReport.action", 0, getdate(), 'admin', 1, 'Membership')

INSERT INTO Report
(report_pk , report_nm , report_desc , report_pend_entity_fg, report_mailing_list_fg , report_custom_fg , report_update_corresp_fg , report_handler_class , report_is_count_fg , report_last_update_dt , report_last_update_uid , report_owner_pk , report_category, report_status )
VALUES (11, 'Enriched Data', NULL, 0, 0, 0, 0, NULL, 0, getdate(), 'admin', 1, 'Membership', 0)

INSERT INTO Report
(report_pk , report_nm , report_desc , report_pend_entity_fg, report_mailing_list_fg , report_custom_fg , report_update_corresp_fg , report_handler_class , report_is_count_fg , report_last_update_dt , report_last_update_uid , report_owner_pk , report_category )
VALUES (12, 'Membership Batch Update', 'This report captures the members that failed to be updated, after the Apply Update function has completed.', 0, 0, 0, 0, "/viewApplyUpdateQueue.action", 0, getdate(), 'admin', 1, 'Membership')

INSERT INTO Report
(report_pk , report_nm , report_desc , report_pend_entity_fg, report_mailing_list_fg , report_custom_fg , report_update_corresp_fg , report_handler_class , report_is_count_fg , report_last_update_dt , report_last_update_uid , report_owner_pk , report_category )
VALUES (13, 'Officer Statistical Summary', 'This report provides a listing of officer counts per local and council with a break down of the officer statistics.', 0, 0, 0, 0, "/Reporting/Specialized/OfficerStatisticalSummary.jsp", 0, getdate(), 'admin', 1, 'Membership')

INSERT INTO Report
(report_pk , report_nm , report_desc , report_pend_entity_fg, report_mailing_list_fg , report_custom_fg , report_update_corresp_fg , report_handler_class , report_is_count_fg , report_last_update_dt , report_last_update_uid , report_owner_pk , report_category )
VALUES (14, 'Officer Statistical Summary Detail', 'This report provides a listing of officer counts per local and council with a break down of the officer statistics in detail.', 0, 0, 0, 0, "/Reporting/Specialized/OfficerStatisticalSunmmaryDetail.jsp", 0, getdate(), 'admin', 1, 'Membership')

INSERT INTO Report
(report_pk , report_nm , report_desc , report_pend_entity_fg, report_mailing_list_fg , report_custom_fg , report_update_corresp_fg , report_handler_class , report_is_count_fg , report_last_update_dt , report_last_update_uid , report_owner_pk , report_category )
VALUES (15, 'Recently Elected President', 'This report provides a listing of new presidents elected by the affiliates.', 0, 0, 0, 0, "/recentlyElectedPresidentsReport.action", 0, getdate(), 'admin', 1, 'Membership')

INSERT INTO Report
(report_pk , report_nm , report_desc , report_pend_entity_fg, report_mailing_list_fg , report_custom_fg , report_update_corresp_fg , report_handler_class , report_is_count_fg , report_last_update_dt , report_last_update_uid , report_owner_pk , report_category )
VALUES (16, 'Recently Elected Treasurers', 'This report provides a listing of new Treasurers elected by the affiliates.', 0, 0, 0, 0, "/recentlyElectedTreasurersReport.action", 0, getdate(), 'admin', 1, 'Membership')

INSERT INTO Report
(report_pk , report_nm , report_desc , report_pend_entity_fg, report_mailing_list_fg , report_custom_fg , report_update_corresp_fg , report_handler_class , report_is_count_fg , report_last_update_dt , report_last_update_uid , report_owner_pk , report_category )
VALUES (17, 'Mermbership Roster', 'This report provides a listing of active member information.', 0, 0, 0, 0, "/Reporting/Specialized/MembershipRoster.jsp", 0, getdate(), 'admin', 1, 'Membership')

INSERT INTO Report
(report_pk , report_nm , report_desc , report_pend_entity_fg, report_mailing_list_fg , report_custom_fg , report_update_corresp_fg , report_handler_class , report_is_count_fg , report_last_update_dt , report_last_update_uid , report_owner_pk , report_category )
VALUES (18, 'Invalid address for reporting officer', 'This report provides a listing of reporting officers with bad address.', 0, 0, 0, 0, "/Reporting/Specialized/InvalidAddressForRO.jsp", 0, getdate(), 'admin', 1, 'Membership')

INSERT INTO Report
(report_pk , report_nm , report_desc , report_pend_entity_fg, report_mailing_list_fg , report_custom_fg , report_update_corresp_fg , report_handler_class , report_is_count_fg , report_last_update_dt , report_last_update_uid , report_owner_pk , report_category )
VALUES (19, 'Non-mailable roster', 'This report provides a listing of active members with bad address.', 0, 0, 0, 0, "/Reporting/Specialized/NonMailableRoster.jsp", 0, getdate(), 'admin', 1, 'Membership')

INSERT INTO Report
(report_pk , report_nm , report_desc , report_pend_entity_fg, report_mailing_list_fg , report_custom_fg , report_update_corresp_fg , report_handler_class , report_is_count_fg , report_last_update_dt , report_last_update_uid , report_owner_pk , report_category )
VALUES (20, 'NCOA-ACS report', 'This report provides a listing of applied address changes.', 0, 0, 0, 0, "/Reporting/Specialized/NcoaAcs.jsp", 0, getdate(), 'admin', 1, 'Membership')


SET IDENTITY_INSERT Report OFF

