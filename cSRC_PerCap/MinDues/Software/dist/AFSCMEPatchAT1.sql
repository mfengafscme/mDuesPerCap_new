
----------------------------
-- createViews.sql Changes
----------------------------

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[V_Affiliate]') and OBJECTPROPERTY(id, N'IsView') = 1)
drop view [dbo].[V_Affiliate]
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[V_Member]') and OBJECTPROPERTY(id, N'IsView') = 1)
drop view [dbo].[V_Member]
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[V_Person]') and OBJECTPROPERTY(id, N'IsView') = 1)
drop view [dbo].[V_Person]
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[V_Person_Address]') and OBJECTPROPERTY(id, N'IsView') = 1)
drop view [dbo].[V_Person_Address]
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[V_Person_Phone]') and OBJECTPROPERTY(id, N'IsView') = 1)
drop view [dbo].[V_Person_Phone]
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[V_Person_Relation]') and OBJECTPROPERTY(id, N'IsView') = 1)
drop view [dbo].[V_Person_Relation]

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
	CASE duplicate_ssn_fg WHEN 0 THEN 'False' WHEN 1 then 'True' ELSE null END as duplicate_ssn_fg,	
	CASE political_objector_fg WHEN 0 THEN 'False' WHEN 1 then 'True' ELSE null END as political_objector_fg,	
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
--email
	primary_email.person_email_addr as primary_email,
	prm_email_lst_mod_user.user_id as pemail_lst_mod_user_id,
	primary_email.lst_mod_dt as pemail_lst_mod_dt, 
	prm_email_created_user.user_id as pemail_created_user_id,
	primary_email.created_dt as pemail_created_dt, 
	alternate_email.person_email_addr as alternate_email,
	alt_email_lst_mod_user.user_id as aemail_lst_mod_user_id,
	alternate_email.lst_mod_dt as aemail_lst_mod_dt, 
	alt_email_created_user.user_id as aemail_created_user_id,
	alternate_email.created_dt as aemail_created_dt
FROM	Person p
LEFT OUTER JOIN Person_Political_Legislative pl ON pl.person_pk = p.person_pk
LEFT OUTER JOIN Person_Demographics d ON d.person_pk = p.person_pk
LEFT OUTER JOIN Person_Disabilities dis ON dis.person_pk = p.person_pk
LEFT OUTER JOIN Person_Disability_Accmdtn dis_acc ON dis_acc.person_pk = p.person_pk
LEFT OUTER JOIN Person_Language p_lang ON p_lang.person_pk = p.person_pk AND p_lang.primary_language_fg=1
LEFT OUTER JOIN Person_Language o_lang ON o_lang.person_pk = p.person_pk AND (o_lang.primary_language_fg<>1 OR o_lang.primary_language_fg is null)
LEFT OUTER JOIN Person_Email primary_email ON primary_email.person_pk = p.person_pk AND primary_email.email_type=71001
LEFT OUTER JOIN Person_Email alternate_email ON alternate_email.person_pk = p.person_pk AND alternate_email.email_type=71002
LEFT OUTER JOIN Common_Codes suffixes ON p.suffix_nm = suffixes.com_cd_pk
LEFT OUTER JOIN Common_Codes prefixes ON p.prefix_nm = prefixes.com_cd_pk
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
INNER JOIN Users created_user ON p.created_user_pk = created_user.person_pk
INNER JOIN Users lst_mod_user ON p.lst_mod_user_pk = lst_mod_user.person_pk
INNER JOIN Users demo_created_user ON d.created_user_pk = demo_created_user.person_pk
INNER JOIN Users demo_lst_mod_user ON d.lst_mod_user_pk = demo_lst_mod_user.person_pk
LEFT OUTER JOIN Users prm_email_created_user ON primary_email.created_user_pk = prm_email_created_user.person_pk
LEFT OUTER JOIN Users prm_email_lst_mod_user ON primary_email.lst_mod_user_pk = prm_email_lst_mod_user.person_pk
LEFT OUTER JOIN Users alt_email_created_user ON alternate_email.created_user_pk = alt_email_created_user.person_pk
LEFT OUTER JOIN Users alt_email_lst_mod_user ON alternate_email.lst_mod_user_pk = alt_email_lst_mod_user.person_pk


GO

CREATE VIEW V_Person_Address  AS
SELECT  paddr.person_pk,

--address
	CASE addr_prmry_fg WHEN 0 THEN 'False' WHEN 1 then 'True' ELSE null END as prmry_fg,
	CASE addr_bad_fg WHEN 0 THEN 'False' WHEN 1 then 'True' ELSE null END as bad_fg,
	CASE addr_private_fg WHEN 0 THEN 'False' WHEN 1 then 'True' ELSE null END as private_fg,
	addr1, addr2, city, states.com_cd_desc as state, zipcode, zip_plus,
	ISNULL(paddr.addr1, '') +
	ISNULL(' ' + paddr.addr2, '') +
	ISNULL(' ' + paddr.city + ',', '') +
	ISNULL(' ' + paddr.state, '') +
	ISNULL(' ' + paddr.zipcode, '') + 
	ISNULL('-' + paddr.zip_plus, '')
	AS full_address,
	states.com_cd_desc as state_desc,
	paddr.carrier_route_info ,
	countries.com_cd_desc as country,
	'1' + CONVERT(varchar, paddr.address_pk) as address_id,
	addrtype.com_cd_desc as addr_type,
	addr_marked_bad_dt as bad_dt,
	addr_lst_mod_user.user_id as addr_lst_mod_user_id,
	paddr.lst_mod_dt as addr_lst_mod_dt, 
	addr_created_user.user_id as addr_created_user_id,
	paddr.created_dt as addr_created_dt 
FROM	Person_Address paddr
LEFT OUTER JOIN Common_Codes states ON states.com_cd_cd = paddr.state AND states.com_cd_type_key = 'State' 
LEFT OUTER JOIN Common_Codes countries ON paddr.country = countries.com_cd_pk 
LEFT OUTER JOIN Common_Codes addrtype ON paddr.addr_type = addrtype.com_cd_pk 
LEFT OUTER JOIN Users addr_created_user ON paddr.created_user_pk = addr_created_user.person_pk
LEFT OUTER JOIN Users addr_lst_mod_user ON paddr.lst_mod_user_pk = addr_lst_mod_user.person_pk

GO

CREATE VIEW V_Person_Phone  AS
SELECT  ph.person_pk,

--phone
	ISNULL(ph.country_cd + ' ', '') +
	ISNULL(ph.area_code + '-', '') +
	ISNULL(ph.phone_no, '') +
	ISNULL(' ' + ph.phone_extension, '')
	AS full_phone,
	ph.area_code, ph.phone_no, ph.country_cd, ph.phone_extension, phone_types.com_cd_desc as phone_type,
	CASE phone_prmry_fg WHEN 0 THEN 'False' WHEN 1 then 'True' ELSE null END as prmry_fg,
	phone_marked_bad_dt as bad_dt,	
	phone_lst_mod_user.user_id as phone_lst_mod_user_id,
	ph.lst_mod_dt as phone_lst_mod_dt, 
	phone_created_user.user_id as phone_created_user_id,
	ph.created_dt as phone_created_dt
FROM	Person_Phone ph 
LEFT OUTER JOIN Common_Codes phone_types ON ph.phone_type = phone_types.com_cd_pk
LEFT OUTER JOIN Users phone_created_user ON ph.created_user_pk = phone_created_user.person_pk
LEFT OUTER JOIN Users phone_lst_mod_user ON ph.lst_mod_user_pk = phone_lst_mod_user.person_pk

GO

CREATE VIEW V_Person_Relation  AS
SELECT  rel.person_pk,

--relative
	relative_type.com_cd_desc as relative_type, rel.relative_first_nm as relative_first_nm, 
	rel.relative_middle_nm as relative_middle_nm, rel.relative_last_nm as relative_last_nm, 
	suffixes.com_cd_desc as relative_suffix_nm,
	rel.relative_birth_dt as relative_birth_dt
FROM	Person_Relation rel
LEFT OUTER JOIN Common_Codes relative_type ON rel.person_relative_type = relative_type.com_cd_pk
LEFT OUTER JOIN Common_Codes suffixes ON rel.relative_suffix_nm = suffixes.com_cd_pk


GO


CREATE VIEW V_Member AS
SELECT
--member
	m.person_pk, 
	m.aff_pk, member_types.com_cd_desc as mbr_type, member_statuses.com_cd_desc as mbr_status, 
	mbr_join_dt, mbr_retired_dt, mbr_card_sent_dt,
	CASE no_mail_fg WHEN 0 THEN 'False' WHEN 1 then 'True' ELSE null END as no_mail_fg,
	CASE lost_time_language_fg WHEN 0 THEN 'False' WHEN 1 then 'True' ELSE null END as lost_time_language_fg,
	dues_types.com_cd_desc as dues_type,
	mbr_dues_rate as dues_rate,
	dues_freq.com_cd_desc as dues_frequency,
	CASE mbr_ret_dues_renewal_fg WHEN 0 THEN 'False' WHEN 1 then 'True' ELSE null END as ret_dues_renewal_fg,
--record info
	lst_mod_user.user_id as lst_mod_user_id,
	m.lst_mod_dt, 
	created_user.user_id as created_user_id,
	m.created_dt, 
--employer
	employer_name,
	emp_job_titles.com_cd_desc as emp_job_title,
	emp_sectors.com_cd_desc as emp_sector,
	emp_salary_ranges.com_cd_desc as emp_salary_range,
	employee_salary,
	emp_job_site
FROM Aff_Members m
LEFT OUTER JOIN Aff_Mbr_Employers emp ON emp.aff_pk = m.aff_pk AND emp.person_pk = m.person_pk
LEFT OUTER JOIN Common_Codes emp_job_titles ON emp.emp_job_title = emp_job_titles.com_cd_pk
LEFT OUTER JOIN Common_Codes emp_sectors ON emp.emp_sector = emp_sectors.com_cd_pk
LEFT OUTER JOIN Common_Codes emp_salary_ranges ON emp.emp_salary_range = emp_salary_ranges.com_cd_pk
LEFT OUTER JOIN Common_Codes dues_types ON m.mbr_dues_type = dues_types.com_cd_pk
LEFT OUTER JOIN Common_Codes dues_freq ON m.mbr_dues_frequency = dues_freq.com_cd_pk
LEFT OUTER JOIN Common_Codes member_types ON m.mbr_type = member_types.com_cd_pk
LEFT OUTER JOIN Common_Codes member_statuses ON m.mbr_status = member_statuses.com_cd_pk
INNER JOIN Users created_user ON m.created_user_pk = created_user.person_pk
INNER JOIN Users lst_mod_user ON m.lst_mod_user_pk = lst_mod_user.person_pk

GO


CREATE VIEW V_Affiliate  AS
SELECT a.aff_pk,
	 ISNULL(aff_type, '') +
	 + ISNULL(aff_localSubChapter, '') +
	 + ISNULL(aff_stateNat_type, '') +
	 + ISNULL(aff_subUnit, '') +
	 + ISNULL(aff_councilRetiree_chap, '')
	 as affiliate_id,
	aff_abbreviated_nm, aff_type, aff_localSubChapter, affStates.com_cd_desc as aff_stateNat_type, 
	aff_subUnit, aff_councilRetiree_chap, affiliate_statuses.com_cd_desc as aff_status,
	card_runs.com_cd_desc as ann_card_run_type,
	renewals.com_cd_desc as mbr_renewal,
--mbrshp reporting info
	CASE unit_wide_no_mbr_cards_fg WHEN 0 THEN 'False' WHEN 1 then 'True' ELSE null END as unit_wide_no_mbr_cards_fg,
	CASE unit_wide_no_pe_mail_fg WHEN 0 THEN 'False' WHEN 1 then 'True' ELSE null END as unit_wide_no_pe_mail_fg,
--record info
	lst_mod_user.user_id as lst_mod_user_id,
	a.lst_mod_dt, 
	created_user.user_id as created_user_id,
	a.created_dt, 
--constitution
	CASE approved_const_fg WHEN 0 THEN 'False' WHEN 1 then 'True' ELSE null END as approved_const_fg,
	CASE auto_delegate_prvsn_fg WHEN 0 THEN 'False' WHEN 1 then 'True' ELSE null END as auto_delegate_prvsn_fg,
	most_current_approval_dt, aff_agreement_dt, 
	off_elections.com_cd_desc as off_election_method,
	CASE const_regions_fg WHEN 0 THEN 'False' WHEN 1 then 'True' ELSE null END as const_regions_fg,
	mtg_freq.com_cd_desc as meeting_frequency,	
--address
	addr1, addr2, city, states.com_cd_desc as state, zipcode, zip_plus, attention_line,
	'2' + CONVERT(varchar, org_addr_pk) as address_id,
	 CASE l.location_primary_fg WHEN 0 THEN 'False' WHEN 1 then 'True' ELSE null END as location_primary_fg,
	addrtype.com_cd_desc as addr_type,
	CASE addr_bad_fg WHEN 0 THEN 'False' WHEN 1 then 'True' ELSE null END as bad_fg,
	addr_bad_dt as bad_dt,
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
 	phone_types.com_cd_desc as phone_type,
	ISNULL(p.country_cd + ' ', '') +
	ISNULL(p.area_code + '-', '') +
	ISNULL(p.phone_no, '') +
	ISNULL(' ' + p.phone_extension, '')
	AS full_phone,
	p.area_code, p.phone_no, p.country_cd, p.phone_extension,
	CASE phone_bad_fg WHEN 0 THEN 'False' WHEN 1 then 'True' ELSE null END as phone_bad_fg,
	phone_bad_dt as phone_bad_dt,
	phone_lst_mod_user.user_id as phone_lst_mod_user_id,
	p.lst_mod_dt as phone_lst_mod_dt, 
	phone_created_user.user_id as phone_created_user_id,
	p.created_dt as phone_created_dt 
FROM Aff_Organizations a
LEFT OUTER JOIN Aff_Mbr_Rpt_Info amri ON a.aff_pk = amri.aff_pk
LEFT OUTER JOIN Aff_Constitution ac ON a.aff_pk = ac.aff_pk
LEFT OUTER JOIN Org_Locations l ON a.aff_pk = l.org_pk AND l.location_primary_fg = 1
LEFT OUTER JOIN Org_Phone p ON p.org_locations_pk = l.org_locations_pk
LEFT OUTER JOIN Org_Address addr ON addr.org_locations_pk = l.org_locations_pk
LEFT OUTER JOIN Common_Codes addrtype ON addr.org_addr_type = addrtype.com_cd_pk
LEFT OUTER JOIN Common_Codes phone_types ON p.org_phone_type = phone_types.com_cd_pk
LEFT OUTER JOIN Common_Codes states ON states.com_cd_cd = addr.state AND states.com_cd_type_key = 'State' 
LEFT OUTER JOIN Common_Codes affStates ON affStates.com_cd_cd = a.aff_stateNat_type AND affStates.com_cd_type_key = 'AffiliateState' 
LEFT OUTER JOIN Common_Codes affiliate_statuses ON a.aff_status = affiliate_statuses.com_cd_pk
LEFT OUTER JOIN Common_Codes card_runs ON a.aff_ann_mbr_card_run_group = card_runs.com_cd_pk
LEFT OUTER JOIN Common_Codes renewals ON a.mbr_renewal = renewals.com_cd_pk
LEFT OUTER JOIN Common_Codes off_elections ON ac.off_election_method = off_elections.com_cd_pk
LEFT OUTER JOIN Common_Codes mtg_freq ON ac.meeting_frequency = mtg_freq.com_cd_pk
INNER JOIN Users created_user ON a.created_user_pk = created_user.person_pk
INNER JOIN Users lst_mod_user ON a.lst_mod_user_pk = lst_mod_user.person_pk
LEFT OUTER JOIN Users addr_created_user ON addr.created_user_pk = addr_created_user.person_pk
LEFT OUTER JOIN Users addr_lst_mod_user ON addr.lst_mod_user_pk = addr_lst_mod_user.person_pk
LEFT OUTER JOIN Users phone_created_user ON p.created_user_pk = phone_created_user.person_pk
LEFT OUTER JOIN Users phone_lst_mod_user ON p.lst_mod_user_pk = phone_lst_mod_user.person_pk

GO


----------------------------
-- insertCodes.sql Changes
----------------------------

----------------------------
-- fix for Puerto Rico (typo and add)
----------------------------

UPDATE Common_Codes
SET com_cd_desc = 'Puerto Rico'
WHERE com_cd_pk = 5040
AND   com_cd_cd = 'PR'



SET IDENTITY_INSERT Common_Codes ON

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10052, 'PR', 'Puerto Rico', 'A', 'State', '52')

SET IDENTITY_INSERT Common_Codes OFF


GO

----------------------------------
-- insertCOMAppTables.sql Changes 
--  (these changes are needed for Apply Update email)
----------------------------------

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


GO


--------------------------------------
-- ADDITIONAL PATCH FIXES FOR PAMELA
--
-- Set a default department for the Admin and Migration users to Membership/Affiliate Relations
--------------------------------------

UPDATE Users
   SET dept = 4001
 WHERE person_pk < 10000003


GO


----------------------------
-- insertFields.sql Changes
----------------------------

----------------------------
-- DELETE ALL ROWS FROM TABLES: 
-- (1) Roles_Report_Fields
-- (2) Report_Field_Aggregate
-- (3) Report_Fields
----------------------------

DELETE FROM Roles_Report_Fields

GO

DELETE FROM Report_Field_Aggregate

GO

DELETE FROM Report_Fields

GO


-----------------------------------------------
-- RESET THE IDENTITY FIELD FOR Report_Fields
-----------------------------------------------

DBCC CHECKIDENT (Report_Fields, RESEED, 0)

GO


----------------------------------
-- STOP!! GO DO THIS NOW!!
--
--******************************************************************************************************
-- RUN THE updated insertFields.sql File to re-populate Report_Fields and Report_Field_Aggregate tables
--******************************************************************************************************
--
-- STOP!! GO DO THIS NOW!!
----------------------------------


----------------------------
-- INSERT ALL ROWS for the Roles_Report_Fields 
----------------------------

INSERT INTO Roles_Report_Fields (role_pk, report_field_pk) SELECT (SELECT role_pk FROM Roles WHERE Role_Name = 'Super User'), report_field_pk FROM Report_Fields

GO

