
----------------------------
-- createViews.sql Changes
----------------------------

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[V_Person]') and OBJECTPROPERTY(id, N'IsView') = 1)
drop view [dbo].[V_Person]
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[V_Person_Phone]') and OBJECTPROPERTY(id, N'IsView') = 1)
drop view [dbo].[V_Person_Phone]

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
	alternate_email.person_email_addr as alternate_email
FROM	Person p
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
	phone_lst_mod_user.user_id as phone_lst_mod_user_id,
	ph.lst_mod_dt as phone_lst_mod_dt, 
	phone_created_user.user_id as phone_created_user_id,
	ph.created_dt as phone_created_dt
FROM	Person_Phone ph 
LEFT OUTER JOIN Common_Codes phone_types ON ph.phone_type = phone_types.com_cd_pk
LEFT OUTER JOIN Users phone_created_user ON ph.created_user_pk = phone_created_user.person_pk
LEFT OUTER JOIN Users phone_lst_mod_user ON ph.lst_mod_user_pk = phone_lst_mod_user.person_pk

GO


----------------------------
-- insertFields.sql Changes
----------------------------

----------------------------
-- fix for V_Person_Address view - Issue #160
-- (Should update only 17 rows)
----------------------------

UPDATE [Report_Fields] 
SET field_table_nm = 'V_Person_Address'
WHERE report_field_pk between 37 AND 53
AND   field_category_name = 'Address'
AND   field_entity_type = 'P'


----------------------------
-- fix for V_Person_Phone view - Issue #160
-- (Should update only 10 rows)
----------------------------

UPDATE [Report_Fields] 
SET field_table_nm = 'V_Person_Phone'
WHERE report_field_pk between 54 AND 63
AND   field_category_name = 'Phone'
AND   field_entity_type = 'P'


----------------------------
-- fix for State labels - Defect #710 
----------------------------

UPDATE [Report_Fields] 
SET field_entity_type = 'O'
WHERE report_field_pk = 93
AND   com_cd_type_key = 'State'
AND   field_table_nm = 'V_Officer'


UPDATE [Report_Fields] 
SET field_entity_type = 'A'
WHERE report_field_pk = 115
AND   com_cd_type_key = 'State'
AND   field_table_nm = 'V_Affiliate'


GO

----------------------------
-- Add change for the Privileges 
----------------------------

INSERT INTO Privileges 
(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
VALUES ('DataUtilityMaintainPoliticalRebateInfo','Political Rebate','E', 1, 'M')

GO

INSERT INTO Roles_Privileges 
(role_pk, privilege_key)
VALUES (1, 'DataUtilityMaintainPoliticalRebateInfo')

INSERT INTO Roles_Privileges 
(role_pk, privilege_key)
VALUES (2, 'DataUtilityMaintainPoliticalRebateInfo')
GO

----------------------------
-- Fix error for Political Rebate where Status and Type are reversed 
----------------------------

UPDATE prb_roster_persons
SET rebate_year_mbr_status = rebate_year_mbr_type,
	rebate_year_mbr_type = rebate_year_mbr_status