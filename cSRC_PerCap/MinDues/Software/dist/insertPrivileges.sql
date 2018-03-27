-- Privilege_Category = S for system, A for Affiliate, M for Member,
-- P for Person, R for Organization, O for Officer.
--
-- Privilege_Verb = G for Global (Reserved for Privilege_Category = System),
-- E for Maintain/Edit, V for View, S for Search
--
-- Privilege_Is_Data_Utility = 1 for Data Utility, 0 for Main.
--
-- Privilege_Key must equal Privilege key in acl.xml
--
-- Privilege_name(s) for both view and maintain for the same functionality
-- must be the same, ex : MaintainAffiliateDetail and ViewAffiliateDetail
-- both have a name of Detail


-- System
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('MaintainUsers','Maintain Users','G', 0, 'S')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('MaintainQueries','Maintain Queries','G', 0, 'S')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('GenerateReports','Generate Reports','G', 0, 'S')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('MaintainPrivileges','Maintain Privileges','G', 0, 'S')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('MaintainCodes','Maintain Codes','G', 0, 'S')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('GenerateMailingList','Generate Mailing List','G', 0, 'S')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('UploadAffiliateFiles','Upload Affiliate Data','G', 0,'S')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('UploadInternationalFiles','Upload International Files','G', 0, 'S')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('MaintainMemberPrivileges','Maintain Member Privileges','G', 1, 'S')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('ProcessReturnedMail','Process Returned Mail','G', 0, 'S')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('MaintainMailingListInfo','Mailing List','E', 0, 'S')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('ViewMailingListInfo','Mailing List','V', 0, 'S')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('Maintain12MonthRebateAmount','12 Month Rebate Amount','E', 0, 'S')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('MaintainApplyUpdate','Apply Update','E', 0, 'S')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('PerformMassChange','Perform Mass Change','G', 0, 'S')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('SearchApplyUpdateLog','Search Apply Update Log','G', 0, 'S')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('ViewApplyUpdate','Apply Update','V', 0, 'S')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('ScheduleNCOAUpdate','Schedule NCOAUpdate','G', 0, 'S')


-- Affiliate
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('MaintainAffiliateDetail','Detail','E', 0,'A')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('ViewAffiliateDetail','Detail','V', 0,'A')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('SearchAffiliate','Basic','S', 0,'A')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('SearchPowerAffiliate','Power','S', 0,'A')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('MaintainAffiliateOfficers','Officers','E', 0,'A')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('MaintainAffiliateOfficerTitles','Officer Titles','E', 0,'A')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('MaintainAffiliateStaffDetail','Staff','E', 0,'A')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('MaintainCharterInfo','Charter','E', 0, 'A')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('MaintainConstitutionInfo','Constitution','E', 0, 'A')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('MaintainFinancialInfo','Financial','E', 0, 'A')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('MaintainMembershipReportingInfo','Membership Reporting','E', 0,'A')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('ViewAffiliateChangeHistory','Change History','V', 0,'A')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('ViewAffiliateOfficers','Officers','V', 0,'A')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('ViewAffiliateOfficerTitles','Officer Titles','V', 0,'A')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('ViewAffiliateStaffInfo','Staff','V', 0,'A')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('ViewCharterInfo','Charter','V', 0, 'A')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('ViewCommentHistory','Comment History','V', 0, 'A')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('ViewConstitutionInfo','Constitution','V', 0, 'A')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('ViewFinancialInfo','Financial','V', 0, 'A')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('ViewMembershipReportingInfo','Membership Reporting','V', 0,'A')


-- Member
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('MaintainMemberDetail','Detail','E', 0,'M')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('SearchVendorMember','Vendor','S', 0,'M')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('MaintainAnnualMemberCardRun','Annual Member Card Run','E', 0,'M')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('MaintainEmployerInfo','Employer','E', 0, 'M')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('MaintainMemberAffiliateInfo','Member Affiliate','E', 0,'M')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('MaintainParticipationDetail','Participation Detail','E', 0, 'M')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('MaintainParticipationGroups','Participation Groups','E', 0, 'M')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('SearchMember','Basic','S', 0,'M')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('SearchPowerMember','Power','S', 0,'M')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('ViewAnnualMemberCardRun','Annual Member Card Run','V', 0,'M')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('ViewEmployerInfo','Employer','V', 0, 'M')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('ViewMemberAffiliateInfo','Member Affiliate','V', 0,'M')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('ViewMemberDetail','Detail','V', 0,'M')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('ViewParticipationDetail','Participation Detail','V', 0, 'M')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('ViewParticipationGroups','Participation Groups','V', 0, 'M')


-- Person
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('ViewPersonDetail','Person Detail','V', 0, 'P')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('MaintainAddressInfo','Addresses','E', 0, 'P')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('MaintainEmailAddressInfo','Email Address','E', 0, 'P')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('MaintainPersonDetail','Person Detail','E', 0, 'P')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('MaintainPhoneNumberInfo','Phone Numbers','E', 0, 'P')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('MaintainPoliticalLegislativeInfo','Political Legislative','E', 0, 'P')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('MaintainGeneralDemographicInfo','General Demographics','E', 0, 'P')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('MaintainPoliticalRebateInfo','Political Rebate','E', 0, 'P')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('SearchPerson','Person','S', 0, 'P')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('SearchPowerPerson','Power','S', 0, 'P')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('ViewAddressInformation','Addresses','V', 0, 'P')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('ViewCorrespondenceHistory','Correspondence History','V', 0, 'P')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('ViewEmailAddressInfo','Email Address','V', 0, 'P')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('ViewGeneralDemographicInfo','General Demographics','V', 0, 'P')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('ViewPoliticalLegislativeInfo','Political Legislative','V', 0, 'P')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('ViewPoliticalRebateInfo','Political Rebate','V', 0, 'P')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('ViewPhoneNumberInfo','Phone Numbers','V', 0, 'P')


-- Organization
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('SearchOrganizations','Basic','S', 0, 'R')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('MaintainOrganizationDetail','Detail','E', 0, 'R')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('ViewOrganizationDetail','Detail','V', 0, 'R')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('MaintainLocationInfo','Locations','E', 0, 'R')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('ViewLocationInfo','Locations','V', 0, 'R')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('MaintainOrganizationAssociateDetail','Associate','E', 0, 'R')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('ViewOrganizationAssociateInfo','Associate','V', 0, 'R')


-- Officer
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('MaintainAddressSelection','Address Selection','E', 0, 'O')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('MaintainLocationSelection','Location Selection','E', 0, 'O')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('MaintainOfficerDetail','Detail','E', 0,'O')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('SearchOfficer','Basic','S', 0,'O')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('ViewAddressSelection','Address Selection','V', 0, 'O')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('ViewLocationSelection','Location Selection','V', 0, 'O')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('ViewOfficerDetail','Detail','V', 0,'O')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('ViewOfficerHistory','Officer History','V', 0,'O')


--Data Utility
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('DataUtilityMaintainQueries','Maintain Queries','G', 1, 'S')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('DataUtilityGenerateReports','Generate Reports','G', 1, 'S')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('DataUtilityGenerateMailingList','Generate Mailing List','G', 1, 'S')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('DataUtilityMemberBasicSearch','Basic Member Search','S', 1, 'M')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES
('DataUtilitySearchPowerMember', 'Power Member Search', 'S', 1, 'M')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES
('DataUtilityMaintainMemberDetail', 'Member Detail', 'E', 1, 'M')
Insert into Privileges 
(privilege_key, privilege_category, privilege_is_data_utility, privilege_verb, privilege_name)
Values
('DataUtilityViewMemberDetail', 'M', 1, 'V', 'Member Detail')
Insert into Privileges 
(privilege_key, privilege_category, privilege_is_data_utility, privilege_verb, privilege_name)
Values
('DataUtilityViewGeneralDemographicInfo', 'M', 1, 'V', 'General Demographic')
Insert into Privileges 
(privilege_key, privilege_category, privilege_is_data_utility, privilege_verb, privilege_name)
Values
('DataUtilityMaintainGeneralDemographicInfo', 'M', 1, 'E', 'General Demographic')
Insert into Privileges 
(privilege_key, privilege_category, privilege_is_data_utility, privilege_verb, privilege_name)
Values
('DataUtilityViewMemberAffiliateInfo', 'M', 1, 'V', 'Member Affiliate')
Insert into Privileges 
(privilege_key, privilege_category, privilege_is_data_utility, privilege_verb, privilege_name)
Values
('DataUtilityMaintainMemberAffiliateInfo', 'M', 1, 'E', 'Member Affiliate')
Insert into Privileges 
(privilege_key, privilege_category, privilege_is_data_utility, privilege_verb, privilege_name)
Values
('DataUtilityMaintainAddressInfo', 'M', 1, 'E', 'Address')
Insert into Privileges 
(privilege_key, privilege_category, privilege_is_data_utility, privilege_verb, privilege_name)
Values
('DataUtilityViewAddressInformation', 'M', 1, 'V', 'Address')
Insert into Privileges 
(privilege_key, privilege_category, privilege_is_data_utility, privilege_verb, privilege_name)
Values
('DataUtilityMaintainPhoneNumberInfo', 'M', 1, 'E', 'Phone Number')
Insert into Privileges 
(privilege_key, privilege_category, privilege_is_data_utility, privilege_verb, privilege_name)
Values
('DataUtilityViewPhoneNumberInfo', 'M', 1, 'V', 'Phone Number')
Insert into Privileges 
(privilege_key, privilege_category, privilege_is_data_utility, privilege_verb, privilege_name)
Values
('DataUtilityMaintainEmailAddressInfo', 'M', 1, 'E', 'Email Address')
Insert into Privileges 
(privilege_key, privilege_category, privilege_is_data_utility, privilege_verb, privilege_name)
Values
('DataUtilityViewEmailAddressInfo', 'M', 1, 'V', 'Email Address')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES('DataUtilityViewCharterInfo','Charter','V',1,'A')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES('DataUtilityViewConstitutionInfo','Constitution','V',1,'A')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES('DataUtilityViewFinancialInfo','Financial','V',1,'A')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES('DataUtilityViewAffiliateChangeHistory','Change History','V',1,'A')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES('DataUtilityMaintainAffiliateStaffDetail','Staff','E',1,'A')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES('DataUtilityViewAffiliateStaffInfo','Staff','V',1,'A')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES('DataUtilityMaintainLocationInfo','Locations','E',1,'A')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES('DataUtilityViewLocationInfo','Locations','V',1,'A')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES('DataUtilityViewAffiliateOfficerTitles','Officer Titles','V',1,'A')
-- INSERT INTO Privileges 
-- (privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
-- VALUES('DataUtilityViewOfficerDetail','Officers','V',1,'A')
-- INSERT INTO Privileges 
-- (privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
-- VALUES('DataUtilityMaintainOfficerDetail','Officers','E',1,'A')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('DataUtilityViewAffiliateHierarchy','Hierarchy','V', 1, 'A')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('DataUtilityUploadAffiliateFiles','Upload Affiliate Data','G', 1, 'S')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('DataUtilityMaintainPoliticalLegislativeInfo','Political/Legislative','E', 1, 'M')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('DataUtilityViewPoliticalLegislativeInfo','Political/Legislative','V', 1, 'M')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('DataUtilityViewMaintainMailingListInfo','Mailing Lists','V', 1, 'M')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('DataUtilityViewPoliticalRebateInfo','Political Rebate','V', 1, 'M')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('DataUtilityMaintainPoliticalRebateInfo','Political Rebate','E', 1, 'M')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('DataUtilityMaintainEmployerInfo','Employer','E', 1, 'M')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('DataUtilityViewEmployerInfo','Employer','V', 1, 'M')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('DataUtilityViewApplyUpdate','Apply Update','V', 1, 'S')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES('DataUtilityViewAffiliateDetail','Affiliate Detail','V',1,'A')
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES('DataUtilityViewAffiliateOfficers','Officers','V',1,'A')
-- Added this line
INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES('DataUtilityMaintainAffiliateOfficers','Officers','E',1,'A')