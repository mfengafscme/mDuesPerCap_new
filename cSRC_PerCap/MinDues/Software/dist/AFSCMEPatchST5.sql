
----------------------------
-- createViews.sql Changes
----------------------------

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[V_Officer]') and OBJECTPROPERTY(id, N'IsView') = 1)
drop view [dbo].[V_Officer]
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[V_Affiliate]') and OBJECTPROPERTY(id, N'IsView') = 1)
drop view [dbo].[V_Affiliate]
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[V_Person_Address]') and OBJECTPROPERTY(id, N'IsView') = 1)
drop view [dbo].[V_Person_Address]
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[V_Officer_Address]') and OBJECTPROPERTY(id, N'IsView') = 1)
drop view [dbo].[V_Officer_Address]
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
	'1' + CONVERT(varchar, paddr.address_pk) as address_id,
	addr_lst_mod_user.user_id as addr_lst_mod_user_id,
	paddr.lst_mod_dt as addr_lst_mod_dt, 
	addr_created_user.user_id as addr_created_user_id,
	paddr.created_dt as addr_created_dt 
FROM	Person_Address paddr
LEFT OUTER JOIN Common_Codes states ON states.com_cd_cd = paddr.state AND states.com_cd_type_key = 'State' 
LEFT OUTER JOIN Users addr_created_user ON paddr.created_user_pk = addr_created_user.person_pk
LEFT OUTER JOIN Users addr_lst_mod_user ON paddr.lst_mod_user_pk = addr_lst_mod_user.person_pk

GO


create view V_Officer_Address  as
select 
 o.person_pk,
 addr1, 
 addr2, 
 city,
 states.com_cd_desc as state,
 zipcode,
 zip_plus,
 pa.lst_mod_user_pk,
 pa.lst_mod_dt,
 pa.created_user_pk,
 pa.created_dt,
 '1' + CONVERT(varchar, address_pk) as address_id
FROM Officer_History o
INNER JOIN Person_Address pa ON o.pos_addr_from_person_pk = pa.address_pk
LEFT OUTER JOIN Common_Codes states ON states.com_cd_cd = pa.state AND states.com_cd_type_key = 'State' 
UNION
select 
 o.person_pk,
 addr1, 
 addr2, 
 city,
 states.com_cd_desc as state,
 zipcode,
 zip_plus,
 oa.lst_mod_user_pk,
 oa.lst_mod_dt,
 oa.created_user_pk,
 oa.created_dt,
 '2' + CONVERT(varchar, org_addr_pk) as address_id
FROM Officer_History o
INNER JOIN Org_Address oa ON o.pos_addr_from_org_pk = oa.org_addr_pk
LEFT OUTER JOIN Common_Codes states ON states.com_cd_cd = oa.state AND states.com_cd_type_key = 'State' 

GO

CREATE VIEW V_Officer  AS
SELECT
--officer
 groups.aff_pk, o.person_pk, titles.com_cd_desc AS title, o.pos_start_dt, o.pos_end_dt, o.pos_expiration_dt,
 CASE pos_steward_fg WHEN 0 THEN 'False' WHEN 1 then 'True' ELSE null END as pos_steward_fg,
 CASE suspended_fg WHEN 0 THEN 'False' WHEN 1 then 'True' ELSE null END as suspended_fg,
--record info
 lst_mod_user.user_id as lst_mod_user_id,
 o.lst_mod_dt, 
 created_user.user_id as created_user_id,
 o.created_dt, 
--address
 address_id, addr1,  addr2,  city, state, zipcode, zip_plus,
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
 addr.created_dt as addr_created_dt 
FROM Officer_History o
LEFT OUTER JOIN V_Officer_Address addr ON addr.person_pk = o.person_pk
INNER JOIN Aff_Officer_Groups groups ON groups.office_group_id = o.office_group_id AND o.aff_pk = groups.aff_pk
INNER JOIN AFSCME_Offices offices ON o.afscme_office_pk = offices.afscme_office_pk 
INNER JOIN Common_Codes titles ON titles.com_cd_pk = offices.afscme_title_nm
INNER JOIN Users created_user ON o.created_user_pk = created_user.person_pk
INNER JOIN Users lst_mod_user ON o.lst_mod_user_pk = lst_mod_user.person_pk
LEFT OUTER JOIN Users addr_created_user ON addr.created_user_pk = addr_created_user.person_pk
LEFT OUTER JOIN Users addr_lst_mod_user ON addr.lst_mod_user_pk = addr_lst_mod_user.person_pk

GO

CREATE VIEW V_Affiliate  AS
SELECT aff_pk,
	 ISNULL(aff_type, '') +
	 + ISNULL(aff_localSubChapter, '') +
	 + ISNULL(aff_stateNat_type, '') +
	 + ISNULL(aff_subUnit, '') +
	 + ISNULL(aff_councilRetiree_chap, '')
	 as affiliate_id,
	aff_abbreviated_nm, aff_type, aff_localSubChapter, affStates.com_cd_desc as aff_stateNat_type, aff_subUnit, aff_councilRetiree_chap,
--record info
	lst_mod_user.user_id as lst_mod_user_id,
	a.lst_mod_dt, 
	created_user.user_id as created_user_id,
	a.created_dt, 
--address
	addr1, addr2, city, states.com_cd_desc as state, zipcode, zip_plus, attention_line,
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
LEFT OUTER JOIN Common_Codes states ON states.com_cd_cd = addr.state AND states.com_cd_type_key = 'State' 
LEFT OUTER JOIN Common_Codes affStates ON affStates.com_cd_cd = a.aff_stateNat_type AND affStates.com_cd_type_key = 'AffiliateState' 
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
-- fix for Term Expiration Date (defect #638)
----------------------------

UPDATE Common_Codes
SET com_cd_desc = 'Term Expiration Date'
WHERE com_cd_pk = 82003
AND   com_cd_cd = '3'


GO
