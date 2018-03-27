if exists (select * from dbo.sysobjects where id = object_id(N'[NCOA_Transaction_Cds]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [NCOA_Transaction_Cds]
GO

CREATE TABLE NCOA_Transaction_Cds (
        trans_cd_pk          int IDENTITY,
        standard_trans_cd    varchar(2) NOT NULL,
        mapped_trans_cd      varchar(10) NULL,
        standard_trans_cd_desc varchar(50) NULL,
        PRIMARY KEY NONCLUSTERED (trans_cd_pk)
 )
go

	----------------------------
	-- NCOA_Transaction_Cds records
	----------------------------

	INSERT INTO NCOA_Transaction_Cds 
	(standard_trans_cd,  mapped_trans_cd, standard_trans_cd_desc)
	VALUES 
	('A', '10', 'Zip Plus 4 change')

	INSERT INTO NCOA_Transaction_Cds 
	(standard_trans_cd,  mapped_trans_cd, standard_trans_cd_desc)
	VALUES 
	('E', '11', 'Error')

	INSERT INTO NCOA_Transaction_Cds 
	(standard_trans_cd,  mapped_trans_cd, standard_trans_cd_desc)
	VALUES 
	('G', '12', 'Box Closed')

	INSERT INTO NCOA_Transaction_Cds 
	(standard_trans_cd,  mapped_trans_cd, standard_trans_cd_desc)
	VALUES 
	('K', '13', 'Kill/Delete')

	INSERT INTO NCOA_Transaction_Cds 
	(standard_trans_cd,  mapped_trans_cd, standard_trans_cd_desc)
	VALUES 
	('M', '14', 'Move')

	INSERT INTO NCOA_Transaction_Cds 
	(standard_trans_cd,  mapped_trans_cd, standard_trans_cd_desc)
	VALUES 
	('P', '15', 'Undeliverable')

	INSERT INTO NCOA_Transaction_Cds 
	(standard_trans_cd,  mapped_trans_cd, standard_trans_cd_desc)
	VALUES 
	('S', '16', 'Move')

	INSERT INTO NCOA_Transaction_Cds 
	(standard_trans_cd,  mapped_trans_cd, standard_trans_cd_desc)
	VALUES 
	('Z', '17', 'Zip Change')


go

----------------------------
-- insertCodes.sql Changes
----------------------------

----------------------------
-- fix for Minnesota
----------------------------

UPDATE Common_Codes
SET com_cd_desc = 'Minnesota'
WHERE com_cd_pk = 10023
AND   com_cd_cd = 'MN'

UPDATE Common_Codes
SET com_cd_desc = 'Minnesota'
WHERE com_cd_pk = 5024
AND   com_cd_cd = 'MN'


----------------------------
-- adds for OfficerUpdateFields
----------------------------

SET IDENTITY_INSERT Common_Codes ON

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(82022, '22', 'Pos AffiliateType', 'A', 'OfficerUpdateFields', '22')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(82023, '23', 'Pos Local/Sub Chapter', 'A', 'OfficerUpdateFields', '23')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(82024, '24', 'Pos State/National Type', 'A', 'OfficerUpdateFields', '24')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(82025, '25', 'Pos Sub Unit', 'A', 'OfficerUpdateFields', '25')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(82026, '26', 'Pos Council/Retiree Chapter', 'A', 'OfficerUpdateFields', '26')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(82027, '27', 'Home AffiliateType', 'A', 'OfficerUpdateFields', '27')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(82028, '28', 'Home Local/Sub Chapter', 'A', 'OfficerUpdateFields', '28')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(82029, '29', 'Home State/National Type', 'A', 'OfficerUpdateFields', '29')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(82030, '30', 'Home Sub Unit', 'A', 'OfficerUpdateFields', '30')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(82031, '31', 'Home Council/Retiree Chapter', 'A', 'OfficerUpdateFields', '31')

SET IDENTITY_INSERT Common_Codes OFF


----------------------------
-- fix for MemberUpdateFields - description changes for defect 
----------------------------

UPDATE Common_Codes
SET com_cd_desc = 'Zip/Postal Code'
WHERE com_cd_pk = 74010
AND   com_cd_cd = '10'

UPDATE Common_Codes
SET com_cd_desc = 'Bad Address Flag'
WHERE com_cd_pk = 74011
AND   com_cd_cd = '11'

UPDATE Common_Codes
SET com_cd_desc = 'Mail Flag'
WHERE com_cd_pk = 74012
AND   com_cd_cd = '12'

UPDATE Common_Codes
SET com_cd_desc = 'Home Telephone'
WHERE com_cd_pk = 74013
AND   com_cd_cd = '13'


UPDATE Common_Codes
SET com_cd_desc = 'Joined Date'
WHERE com_cd_pk = 74016
AND   com_cd_cd = '16'


go

----------------------------
-- insertReports.sql Changes
----------------------------

----------------------------
-- fix to move OrgMailingList
-- to Mailing Lists from Reports
----------------------------

UPDATE Report
SET report_mailing_list_fg = 1
WHERE report_pk = 8
AND   report_nm = 'Org Mailing List' 


----------------------------
-- fixes or adds for Kyung's reports
----------------------------

UPDATE Report
SET report_desc = 'This report provides a listing of affiliates by affiliate status.'
WHERE report_pk = 9
AND   report_nm = 'Affiliate Counts by Status' 

UPDATE Report
SET report_handler_class = "/officerExpirationReport.action"
WHERE report_pk = 10
AND   report_nm = 'Officer Expiration Listing' 


UPDATE Report
SET report_desc = 'This report provides a listing of officer counts per local and council with a break down of the officer statistics.',
    report_handler_class = "/Reporting/Specialized/OfficerStatisticalSummary.jsp"
WHERE report_pk = 13
AND   report_nm = 'Officer Statistical Summary' 

UPDATE Report
SET report_nm = 'Officer Statistical Summary Detail',
    report_desc = 'This report provides a listing of officer counts per local and council with a break down of the officer statistics in detail.',
    report_handler_class = "/Reporting/Specialized/OfficerStatisticalSunmmaryDetail.jsp"
WHERE report_pk = 14

UPDATE Report
SET report_nm = 'Recently Elected President',
    report_desc = 'This report provides a listing of new presidents elected by the affiliates.',
    report_handler_class = "/recentlyElectedPresidentsReport.action"
WHERE report_pk = 15



SET IDENTITY_INSERT Report ON

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
VALUES (20, 'NCOA/ACS report', 'This report provides a listing of applied address changes.', 0, 0, 0, 0, "/Reporting/Specialized/NcoaAcs.jsp", 0, getdate(), 'admin', 1, 'Membership')

SET IDENTITY_INSERT Report OFF


go

----------------------------
-- createViews.sql Changes
----------------------------

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[V_Affiliate]') and OBJECTPROPERTY(id, N'IsView') = 1)
drop view [dbo].[V_Affiliate]
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[V_Person]') and OBJECTPROPERTY(id, N'IsView') = 1)
drop view [dbo].[V_Person]

GO


CREATE VIEW V_Person  AS
SELECT  p.person_pk,

--name 
	prefixes.com_cd_desc as prefix_nm, first_nm, last_nm, middle_nm, suffixes.com_cd_desc as suffix_nm, 
    ISNULL(p.last_nm, '') +
	ISNULL(' ' + suffixes.com_cd_desc, '') +
	ISNULL(',' + ISNULL(prefixes.com_cd_desc, '') + ISNULL(' ' + p.first_nm, '') +	ISNULL(' ' + p.middle_nm, '') , '')
	AS full_name,
--record info
	lst_mod_user.user_id as lst_mod_user_id,
	p.lst_mod_dt, 
	created_user.user_id as created_user_id,
	p.created_dt, 
--other detail    
	alternate_mailing_nm, nick_nm, ssn, mbr_expelled_dt,
	CASE mbr_barred_fg WHEN 0 THEN 'False' WHEN 1 then 'True' ELSE null END as mbr_barred_fg,
	CASE marked_for_deletion_fg WHEN 0 THEN 'False' WHEN 1 then 'True' ELSE null END as marked_for_deletion_fg,

--address
	CASE addr_prmry_fg WHEN 0 THEN 'False' WHEN 1 then 'True' ELSE null END as prmry_fg,
	CASE addr_bad_fg WHEN 0 THEN 'False' WHEN 1 then 'True' ELSE null END as bad_fg,
	CASE addr_private_fg WHEN 0 THEN 'False' WHEN 1 then 'True' ELSE null END as private_fg,
	addr1, addr2, city, state, zipcode, zip_plus,
	ISNULL(paddr.addr1, '') +
	ISNULL(' ' + paddr.addr2, '') +
	ISNULL(' ' + paddr.city + ',', '') +
	ISNULL(' ' + paddr.state, '') +
	ISNULL(' ' + paddr.zipcode, '') + 
	ISNULL('-' + paddr.zip_plus, '')
	AS full_address,
	states.com_cd_desc as state_desc,
	paddr.carrier_route_info ,
	'1' + CONVERT(varchar, paddr.address_pk) as address_id,
	addr_lst_mod_user.user_id as addr_lst_mod_user_id,
	paddr.lst_mod_dt as addr_lst_mod_dt, 
	addr_created_user.user_id as addr_created_user_id,
	paddr.created_dt as addr_created_dt, 
--demographics
	d.dob, genders.com_cd_desc as gender, ethnic_origins.com_cd_desc as ethnic_origin, religions.com_cd_desc as religion, marital_statuses.com_cd_desc as marital_status, regions.com_cd_desc as region, citizenships.com_cd_desc as citizenship,
	CASE d.deceased_fg WHEN 0 THEN 'False' WHEN 1 then 'True' ELSE null END as deceased_fg,
	d.deceased_dt as deceased_dt,
	disabilities.com_cd_desc as disability,
	disability_accmdtn.com_cd_desc as disability_accmdtn,
	primary_language.com_cd_desc as primary_language,
	other_languages.com_cd_desc as other_languages,	
	demo_lst_mod_user.user_id as demo_lst_mod_user_id,
	d.lst_mod_dt as demo_lst_mod_dt, 
	demo_created_user.user_id as demo_created_user_id,
	d.created_dt as demo_created_dt, 
--phone
	ISNULL(ph.country_cd + ' ', '') +
	ISNULL(ph.area_code + '-', '') +
	ISNULL(ph.phone_no, '') +
	ISNULL(' ' + ph.phone_extension, '')
	AS full_phone,
	ph.area_code, ph.phone_no, ph.country_cd, ph.phone_extension, phone_types.com_cd_desc as phone_type,
	phone_lst_mod_user.user_id as phone_lst_mod_user_id,
	ph.lst_mod_dt as phone_lst_mod_dt, 
	phone_created_user.user_id as phone_created_user_id,
	ph.created_dt as phone_created_dt, 
--email
	primary_email.person_email_addr as primary_email,
	alternate_email.person_email_addr as alternate_email
FROM	Person p
LEFT OUTER JOIN Person_Demographics d ON d.person_pk = p.person_pk
LEFT OUTER JOIN Person_Disabilities dis ON dis.person_pk = p.person_pk
LEFT OUTER JOIN Person_Disability_Accmdtn dis_acc ON dis_acc.person_pk = p.person_pk
LEFT OUTER JOIN Person_Language p_lang ON p_lang.person_pk = p.person_pk AND p_lang.primary_language_fg=1
LEFT OUTER JOIN Person_Language o_lang ON o_lang.person_pk = p.person_pk AND (o_lang.primary_language_fg<>1 OR o_lang.primary_language_fg is null)
LEFT OUTER JOIN Person_Phone ph ON ph.person_pk = p.person_pk
LEFT OUTER JOIN Person_Email primary_email ON primary_email.person_pk = p.person_pk AND primary_email.email_type=71001
LEFT OUTER JOIN Person_Email alternate_email ON alternate_email.person_pk = p.person_pk AND alternate_email.email_type=71002
LEFT OUTER JOIN Common_Codes suffixes ON p.suffix_nm = suffixes.com_cd_pk
LEFT OUTER JOIN Common_Codes prefixes ON p.prefix_nm = prefixes.com_cd_pk
LEFT OUTER JOIN Common_Codes phone_types ON ph.phone_type = phone_types.com_cd_pk
LEFT OUTER JOIN Common_Codes genders ON d.gender = genders.com_cd_pk
LEFT OUTER JOIN Common_Codes ethnic_origins ON d.ethnic_origin = ethnic_origins.com_cd_pk
LEFT OUTER JOIN Common_Codes religions ON d.religion = religions.com_cd_pk
LEFT OUTER JOIN Common_Codes marital_statuses ON d.marital_status = marital_statuses.com_cd_pk
LEFT OUTER JOIN Common_Codes regions ON d.region_fk = regions.com_cd_pk
LEFT OUTER JOIN Common_Codes citizenships ON d.citizenship = citizenships.com_cd_pk
LEFT OUTER JOIN Common_Codes disabilities ON dis.disability = disabilities.com_cd_pk
LEFT OUTER JOIN Common_Codes disability_accmdtn ON dis_acc.disability_accmdtn = disability_accmdtn.com_cd_pk
LEFT OUTER JOIN Common_Codes primary_language ON p_lang.language = primary_language.com_cd_pk
LEFT OUTER JOIN Common_Codes other_languages ON o_lang.language = other_languages.com_cd_pk
LEFT OUTER JOIN Person_Address paddr ON p.person_pk = paddr.person_pk 

LEFT OUTER JOIN Common_Codes states ON states.com_cd_cd = paddr.state AND states.com_cd_type_key = 'State' 
INNER JOIN Users created_user ON p.created_user_pk = created_user.person_pk
INNER JOIN Users lst_mod_user ON p.lst_mod_user_pk = lst_mod_user.person_pk
LEFT OUTER JOIN Users phone_created_user ON ph.created_user_pk = phone_created_user.person_pk
LEFT OUTER JOIN Users phone_lst_mod_user ON ph.lst_mod_user_pk = phone_lst_mod_user.person_pk
INNER JOIN Users demo_created_user ON d.created_user_pk = demo_created_user.person_pk
INNER JOIN Users demo_lst_mod_user ON d.lst_mod_user_pk = demo_lst_mod_user.person_pk
LEFT OUTER JOIN Users addr_created_user ON paddr.created_user_pk = addr_created_user.person_pk
LEFT OUTER JOIN Users addr_lst_mod_user ON paddr.lst_mod_user_pk = addr_lst_mod_user.person_pk


GO

CREATE VIEW V_Affiliate  AS
SELECT aff_pk,
	 ISNULL(aff_type, '') +
	 + ISNULL(aff_localSubChapter, '') +
	 + ISNULL(aff_stateNat_type, '') +
	 + ISNULL(aff_subUnit, '') +
	 + ISNULL(aff_councilRetiree_chap, '')
	 as affiliate_id,
	aff_abbreviated_nm, aff_type, aff_localSubChapter, aff_stateNat_type, aff_subUnit, aff_councilRetiree_chap,
--record info
	lst_mod_user.user_id as lst_mod_user_id,
	a.lst_mod_dt, 
	created_user.user_id as created_user_id,
	a.created_dt, 
--address
	addr1, addr2, city, state, zipcode, zip_plus, attention_line,
	'2' + CONVERT(varchar, org_addr_pk) as address_id,
	 CASE l.location_primary_fg WHEN 0 THEN 'False' WHEN 1 then 'True' ELSE null END as location_primary_fg,
 ISNULL(addr1, '') +
 ISNULL(' ' + addr2, '') +
 ISNULL(' ' + city + ',', '') +
 ISNULL(' ' + state, '') +
 ISNULL(' ' + zipcode, '') +
 ISNULL('-' + zip_plus, '')
 as full_address,
 addr_lst_mod_user.user_id as addr_lst_mod_user_id,
 addr.lst_mod_dt as addr_lst_mod_dt, 
 addr_created_user.user_id as addr_created_user_id,
 addr.created_dt as addr_created_dt, 
--phone
	ISNULL(p.country_cd + ' ', '') +
	ISNULL(p.area_code + '-', '') +
	ISNULL(p.phone_no, '') +
	ISNULL(' ' + p.phone_extension, '')
	AS full_phone,
	p.area_code, p.phone_no, p.country_cd, p.phone_extension,
	phone_lst_mod_user.user_id as phone_lst_mod_user_id,
	p.lst_mod_dt as phone_lst_mod_dt, 
	phone_created_user.user_id as phone_created_user_id,
	p.created_dt as phone_created_dt 
FROM Aff_Organizations a
LEFT OUTER JOIN Org_Locations l ON a.aff_pk = l.org_pk AND l.location_primary_fg = 1
LEFT OUTER JOIN Org_Phone p ON p.org_locations_pk = l.org_locations_pk
LEFT OUTER JOIN Org_Address addr ON addr.org_locations_pk = l.org_locations_pk
INNER JOIN Users created_user ON a.created_user_pk = created_user.person_pk
INNER JOIN Users lst_mod_user ON a.lst_mod_user_pk = lst_mod_user.person_pk
LEFT OUTER JOIN Users addr_created_user ON addr.created_user_pk = addr_created_user.person_pk
LEFT OUTER JOIN Users addr_lst_mod_user ON addr.lst_mod_user_pk = addr_lst_mod_user.person_pk
LEFT OUTER JOIN Users phone_created_user ON p.created_user_pk = phone_created_user.person_pk
LEFT OUTER JOIN Users phone_lst_mod_user ON p.lst_mod_user_pk = phone_lst_mod_user.person_pk


go

----------------------------
-- insertFields.sql Changes
----------------------------

INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person', 'B', 'Marked For Deletion', 'marked_for_deletion_fg', 'Detail', 1.0)


go


----------------------------
-- CHANGE NEEDED for insertFields.sql change to work!! 
-- [Check that report_field_pk equals the one for Marked for Deletion in
--  Report_Fields table] 
----------------------------

INSERT INTO [Roles_Report_Fields] 
	(role_pk, report_field_pk)
	VALUES
	(1, 130)


go

