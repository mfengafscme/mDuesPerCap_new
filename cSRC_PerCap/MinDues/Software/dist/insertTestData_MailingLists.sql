DECLARE @user_pk int
SET  @user_pk = 10000001

-----------------------------------------------------
---
--- Clean out data if not already done so
---
-----------------------------------------------------
delete from mailing_list_orgs
delete from mlbp_persons
delete from mailing_lists_by_orgs
delete from mailing_lists_by_person

--------------------------------------------------------------
---
--- Organization Mailing Lists Common Codes -- Explicit add
---
--------------------------------------------------------------
SET IDENTITY_INSERT Mailing_Lists_by_Orgs ON

insert into Mailing_Lists_by_Orgs (mlbo_mailing_list_pk, mlbo_mailing_list_nm, officer_mailing_list_fg, mailing_list_legacy_cd, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk)
values (99,'PE Bulk Mail File', 0, null, getdate(), @user_pk, getdate(), @user_pk)

SET IDENTITY_INSERT Mailing_Lists_by_Orgs OFF

---------------------------------------------------------
---
--- Person Mailing Lists Common Codes -- Explicit add
---
---------------------------------------------------------
SET IDENTITY_INSERT Mailing_Lists_by_Person ON
insert into Mailing_Lists_by_Person (mlbp_mailing_list_pk, mlbp_mailing_list_nm, dept, mailing_list_legacy_cd, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk)
values (01,'Area Directors', 0, null, getdate(), @user_pk, getdate(), @user_pk)

insert into Mailing_Lists_by_Person (mlbp_mailing_list_pk, mlbp_mailing_list_nm, dept, mailing_list_legacy_cd, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk)
values (02,'International Vice-Presidents', 0, null, getdate(), @user_pk, getdate(), @user_pk)

insert into Mailing_Lists_by_Person (mlbp_mailing_list_pk, mlbp_mailing_list_nm, dept, mailing_list_legacy_cd, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk)
values (03,'International Field Staff', 0, null, getdate(), @user_pk, getdate(), @user_pk)

insert into Mailing_Lists_by_Person (mlbp_mailing_list_pk, mlbp_mailing_list_nm, dept, mailing_list_legacy_cd, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk)
values (04,'Judicial Panel', 0, null, getdate(), @user_pk, getdate(), @user_pk)

insert into Mailing_Lists_by_Person (mlbp_mailing_list_pk, mlbp_mailing_list_nm, dept, mailing_list_legacy_cd, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk)
values (05,'Council Directors/Presidents', 0, null, getdate(), @user_pk, getdate(), @user_pk)

insert into Mailing_Lists_by_Person (mlbp_mailing_list_pk, mlbp_mailing_list_nm, dept, mailing_list_legacy_cd, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk)
values (06,'Directors/Presidents-Lrg. Unaffil. Locals', 0, null, getdate(), @user_pk, getdate(), @user_pk)

insert into Mailing_Lists_by_Person (mlbp_mailing_list_pk, mlbp_mailing_list_nm, dept, mailing_list_legacy_cd, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk)
values (07,'Council & Local Staff', 0, null, getdate(), @user_pk, getdate(), @user_pk)

insert into Mailing_Lists_by_Person (mlbp_mailing_list_pk, mlbp_mailing_list_nm, dept, mailing_list_legacy_cd, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk)
values (25,'Council Presidents', 0, null, getdate(), @user_pk, getdate(), @user_pk)

insert into Mailing_Lists_by_Person (mlbp_mailing_list_pk, mlbp_mailing_list_nm, dept, mailing_list_legacy_cd, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk)
values (28,'Ohio Local 11 Presidents', 0, null, getdate(), @user_pk, getdate(), @user_pk)

insert into Mailing_Lists_by_Person (mlbp_mailing_list_pk, mlbp_mailing_list_nm, dept, mailing_list_legacy_cd, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk)
values (99,'PE Bulk Mail File', 0, null, getdate(), @user_pk, getdate(), @user_pk)

SET IDENTITY_INSERT Mailing_Lists_by_Person OFF


-------------------------------------------
---
--- Organization Mailing Lists Test Data
---
-------------------------------------------
DECLARE @mailing_list_pk int, @org_pk int, @location_pk int
INSERT INTO org_parent (org_subtype)
VALUES(24001)
SET @org_pk = @@identity

INSERT INTO external_organizations (org_pk, org_nm, org_web_site, org_email_domain, ext_org_type, marked_for_deletion_fg, created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
values(@org_pk, 'Test Organization', 'tester@att.com', '@att.com', 1, 0, 10000001, getdate(), 10000001, getdate())

--
-- Headquarters location
--
INSERT INTO Org_Locations
(location_nm, location_primary_fg, lst_mod_user_pk, created_dt, created_user_pk, lst_mod_dt, org_pk)
VALUES ('Headquarters', NULL, 10000001, getdate(), 10000001, getdate(), @org_pk)
SET @location_pk = @@identity

INSERT INTO Org_Address 
(addr1,addr2, org_addr_type, city, zipcode, lst_mod_dt, addr_bad_fg, created_dt, created_user_pk, lst_mod_user_pk, zip_plus, country, county, province, state,addr_bad_dt, attention_line, org_locations_pk)
VALUES ('563 Pear Lane', null, 72001, 'Vienna', '22182', getdate() , 0, getdate() , 10000001, 10000001,null,null,null, null, 'VA', null, null, @location_pk)

INSERT INTO Org_Address 
(addr1,addr2, org_addr_type, city, zipcode, lst_mod_dt, addr_bad_fg, created_dt, created_user_pk, lst_mod_user_pk, zip_plus, country, county, province, state,addr_bad_dt, attention_line, org_locations_pk)
VALUES ('300 Apple Lane', null, 72002, 'Vienna', '22182', getdate() , 0, getdate() , 10000001, 10000001,null,null,null, null, 'VA', null, null, @location_pk)

INSERT INTO Mailing_List_Orgs
(MLBO_mailing_list_pk, mailing_list_bulk_cnt, org_pk, org_locations_pk, lst_mod_dt, lst_mod_user_pk, created_dt, created_user_pk) 
VALUES (99, 100, @org_pk, @location_pk, getdate(), @user_pk, getdate(), @user_pk)

--
-- DC Branch location
--
INSERT INTO Org_Locations
(location_nm, location_primary_fg, lst_mod_user_pk, created_dt, created_user_pk, lst_mod_dt, org_pk)
VALUES ('DC Branch', NULL, 10000001, getdate(), 10000001, getdate(), @org_pk)

SET @location_pk = @@identity

INSERT INTO Org_Address 
(addr1,addr2, org_addr_type, city, zipcode, lst_mod_dt, addr_bad_fg, created_dt, created_user_pk, lst_mod_user_pk, zip_plus, country, county, province, state,addr_bad_dt, attention_line, org_locations_pk)
VALUES ('563 Pear Lane', null, 72001, 'Vienna', '22182', getdate() , 0, getdate() , 10000001, 10000001,null,null,null, null, 'VA', null, null, @location_pk)

INSERT INTO Org_Address 
(addr1,addr2, org_addr_type, city, zipcode, lst_mod_dt, addr_bad_fg, created_dt, created_user_pk, lst_mod_user_pk, zip_plus, country, county, province, state,addr_bad_dt, attention_line, org_locations_pk)
VALUES ('300 Apple Lane', null, 72002, 'Vienna', '22182', getdate() , 0, getdate() , 10000001, 10000001,null,null,null, null, 'VA', null, null, @location_pk)

--
-- Vienna Branch location
--
INSERT INTO Org_Locations
(location_nm, location_primary_fg, lst_mod_user_pk, created_dt, created_user_pk, lst_mod_dt, org_pk)
VALUES ('Vienna Branch', NULL, 10000001, getdate(), 10000001, getdate(), @org_pk)
SET @location_pk = @@identity

INSERT INTO Org_Address 
(addr1,addr2, org_addr_type, city, zipcode, lst_mod_dt, addr_bad_fg, created_dt, created_user_pk, lst_mod_user_pk, zip_plus, country, county, province, state,addr_bad_dt, attention_line, org_locations_pk)
VALUES ('563 Pear Lane', null, 72001, 'Vienna', '22182', getdate() , 0, getdate() , 10000001, 10000001,null,null,null, null, 'VA', null, null, @location_pk)

INSERT INTO Org_Address 
(addr1,addr2, org_addr_type, city, zipcode, lst_mod_dt, addr_bad_fg, created_dt, created_user_pk, lst_mod_user_pk, zip_plus, country, county, province, state,addr_bad_dt, attention_line, org_locations_pk)
VALUES ('300 Apple Lane', null, 72002, 'Vienna', '22182', getdate() , 0, getdate() , 10000001, 10000001,null,null,null, null, 'VA', null, null, @location_pk)

--
-- Orange County Branch location with org_pk set to 60
--
SET @org_pk = 60

INSERT INTO Org_Locations
(location_nm, location_primary_fg, lst_mod_user_pk, created_dt, created_user_pk, lst_mod_dt, org_pk)
VALUES ('Orange County Branch ', NULL, 10000001, getdate(), 10000001, getdate(), @org_pk)
SET @location_pk = @@identity

INSERT INTO Org_Address 
(addr1,addr2, org_addr_type, city, zipcode, lst_mod_dt, addr_bad_fg, created_dt, created_user_pk, lst_mod_user_pk, zip_plus, country, county, province, state,addr_bad_dt, attention_line, org_locations_pk)
VALUES ('563 Sugar Lane', null, 72001, 'Vienna', '22182', getdate() , 0, getdate() , 10000001, 10000001,null,null,null, null, 'VA', null, null, @location_pk)

INSERT INTO Org_Address 
(addr1,addr2, org_addr_type, city, zipcode, lst_mod_dt, addr_bad_fg, created_dt, created_user_pk, lst_mod_user_pk, zip_plus, country, county, province, state,addr_bad_dt, attention_line, org_locations_pk)
VALUES ('300 Salt Lane', null, 72002, 'Vienna', '22182', getdate() , 0, getdate() , 10000001, 10000001,null,null,null, null, 'VA', null, null, @location_pk)

INSERT INTO Mailing_List_Orgs
(MLBO_mailing_list_pk, mailing_list_bulk_cnt, org_pk, org_locations_pk, lst_mod_dt, lst_mod_user_pk, created_dt, created_user_pk) 
values (99, 8, @org_pk, @location_pk, getdate(), @user_pk, getdate(), @user_pk)

-------------------------------------------
---
--- Person Mailing Lists Test Data
---
-------------------------------------------
---
--- John Doe person
---
DECLARE @person_pk int, @address_pk int
INSERT INTO Person
(ssn, member_fg, mbr_barred_fg, first_nm, last_nm, middle_nm, marked_for_deletion_fg, created_user_pk,created_dt,lst_mod_user_pk, lst_mod_dt)
VALUES (216959635, 0, 0, 'John', 'Doe','K.', 0, 10000001, getdate(), 10000001, getdate())
SET @person_pk = @@identity

INSERT INTO Person_Email
(person_pk, person_email_addr, lst_mod_dt, email_type, created_dt, created_user_pk, lst_mod_user_pk)
VALUES (@person_pk, 'jdoe@grci.com', getdate(), 71001, getdate(), 10000001, 10000001) 

INSERT INTO Person_Address 
(addr1, addr2, city, state, zipcode, zip_plus, carrier_route_info, country, county,  eff_dt, end_dt, lst_mod_dt, addr_prmry_fg,  addr_private_fg, addr_bad_fg, created_user_pk, created_dt, addr_source, addr_type, addr_marked_bad_dt, province, person_pk, dept, lst_mod_user_pk)
VALUES ('8 Lemon Road', NULL , 'Vienna', 'VA', 22182, NULL, NULL, 9001, NULL, NULL, NULL, getdate(), 1, 0, 0, 10000001, getdate(), 'A', 12001, null, null, @person_pk, 4001, 10000001)
SET @address_pk = @@identity

INSERT INTO Person_Address 
(addr1, addr2, city, state , zipcode, zip_plus, carrier_route_info, country, county, eff_dt, end_dt, lst_mod_dt, addr_prmry_fg, addr_private_fg, addr_bad_fg, created_user_pk, created_dt, addr_source, addr_type, addr_marked_bad_dt, province, person_pk, dept, lst_mod_user_pk)
VALUES ('8 WaterMelon Road', NULL , 'Vienna', 'VA', 22182, NULL, NULL, 9001, NULL, NULL, NULL, getdate(), 2, 0, 0, 10000001, getdate(), 'A', 12001, null, null, @person_pk, 4001, 10000001)

INSERT INTO MLBP_Persons 
(person_pk,MLBP_mailing_list_pk, address_pk, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk)
values (@person_pk,1,@address_pk, getdate(), @user_pk, getdate(), @user_pk)
INSERT INTO MLBP_Persons 
(person_pk,MLBP_mailing_list_pk, address_pk, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk)
values (@person_pk,2,@address_pk, getdate(), @user_pk, getdate(), @user_pk)
INSERT INTO MLBP_Persons
(person_pk,MLBP_mailing_list_pk, address_pk, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk)
values (@person_pk,3,@address_pk, getdate(), @user_pk, getdate(), @user_pk)
INSERT INTO MLBP_Persons
(person_pk,MLBP_mailing_list_pk, address_pk, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk)
values (@person_pk,99,@address_pk, getdate(), @user_pk, getdate(), @user_pk)

---
--- Admin person
---
SET @person_pk = 10000001
SET @address_pk = 1
INSERT INTO MLBP_Persons 
(person_pk,MLBP_mailing_list_pk, address_pk, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk)
values (@person_pk,1,@address_pk, getdate(), @user_pk, getdate(), @user_pk)
INSERT INTO MLBP_Persons 
(person_pk,MLBP_mailing_list_pk, address_pk, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk)
values (@person_pk,2,@address_pk, getdate(), @user_pk, getdate(), @user_pk)

---
--- Linda Harrison person
---
SET @person_pk = 10000002
SET @address_pk = 2
INSERT INTO MLBP_Persons 
(person_pk,MLBP_mailing_list_pk, address_pk, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk)
values (@person_pk,1,@address_pk, getdate(), @user_pk, getdate(), @user_pk)
INSERT INTO MLBP_Persons
(person_pk,MLBP_mailing_list_pk, address_pk, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk)
values (@person_pk,2,@address_pk, getdate(), @user_pk, getdate(), @user_pk)

