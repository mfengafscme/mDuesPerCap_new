--
-- External Organizations and all tables associated with it
--

DECLARE @org_pk int, @location_pk int, @org_addr_pk int

--
-- AFLCIO LIBRARY
--
INSERT INTO org_parent values(24002)
SET @org_pk = @@identity

INSERT INTO External_Organizations
(org_pk,   org_nm,           org_web_site,     lst_mod_dt, lst_mod_user_pk, created_user_pk, created_dt, ext_org_type, org_email_domain, marked_for_deletion_fg)
VALUES
(@org_pk, 'AFLCIO LIBRARY', 'www.aflcio.org', getdate() , 10000001       , 10000001       , getdate() , 28001       , NULL            , 0                     )

INSERT INTO Org_Locations
(location_nm   , location_primary_fg, lst_mod_user_pk, created_dt, created_user_pk, lst_mod_dt, org_pk)
VALUES
('Headquarters', 1                  , 10000001       ,  getdate(), 10000001       , getdate() , @org_pk)
SET @location_pk = @@identity

INSERT INTO Org_Address 
(addr1,            addr2, org_addr_type, city        , zipcode, lst_mod_dt, addr_bad_fg, created_dt, created_user_pk, lst_mod_user_pk, zip_plus, country, county, province, state,    addr_bad_dt, attention_line,   org_locations_pk)
VALUES
('815 16TH ST NW', null , 72001        , 'WASHINGTON', '20006', getdate() , 0          , getdate() , 10000001       , 10000001       , null,     null,    null,   null    , 'DC' ,    null       , 'ATTN: Librarian', @location_pk)
SET @org_addr_pk = @@identity

INSERT INTO Org_Phone 
(area_code, phone_no, org_phone_type, lst_mod_dt, country_cd, phone_bad_fg, created_dt, created_user_pk, phone_bad_dt, lst_mod_user_pk, phone_extension, org_locations_pk)
VALUES
(202      , 5551234 , 73001         , getdate() , 1         , 0           , getdate() , 10000001      , null        , 10000001       , null           , @location_pk)

INSERT INTO Org_Phone 
(area_code, phone_no, org_phone_type, lst_mod_dt, country_cd, phone_bad_fg, created_dt, created_user_pk, phone_bad_dt, lst_mod_user_pk, phone_extension, org_locations_pk)
VALUES
(202      , 5559999 , 73002         , getdate() , 1         , 1           , getdate() , 10000001      , getdate()    , 10000001       , null           , @location_pk)


INSERT INTO Ext_Org_Associates 
(org_pk,  person_pk, org_pos_title, org_locations_pk, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk) 
VALUES
(@org_pk, 10000101 , 76001        , @location_pk    , getdate() , 10000001       , getdate() , 10000001)

INSERT INTO Ext_Org_Associate_Comments 
(org_pk,  person_pk, comment_txt,    comment_dt, created_user_pk) 
VALUES
(@org_pk, 10000101, 'Comment Test' , getdate() , 10000001)

INSERT INTO Ext_Org_Associate_Comments 
(org_pk,  person_pk, comment_txt,    comment_dt, created_user_pk) 
VALUES
(@org_pk, 10000101, 'Comment Test for Another' , getdate() , 10000001)


INSERT INTO Ext_Org_Associates 
(org_pk,  person_pk, org_pos_title, org_locations_pk, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk) 
VALUES
(@org_pk, 10000401 , 76003        , @location_pk    , getdate() , 10000001       , getdate() , 10000001)

INSERT INTO Ext_Org_Associate_Comments 
(org_pk,  person_pk, comment_txt,             comment_dt, created_user_pk) 
VALUES
(@org_pk, 10000401, 'Comment for Associate' , getdate() , 10000001)


INSERT INTO Ext_Org_Associates 
(org_pk,  person_pk, org_pos_title, org_locations_pk, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk) 
VALUES
(@org_pk, 10000601 , 76002        , @location_pk    , getdate() , 10000001       , getdate() , 10000001)

INSERT INTO Ext_Org_Associates 
(org_pk,  person_pk, org_pos_title, org_locations_pk, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk) 
VALUES
(@org_pk, 10000602 , 76004        , @location_pk    , getdate() , 10000001       , getdate() , 10000001)


--
-- ALAMEDA COUNTY LIB 
--
INSERT INTO org_parent values(24002)
SET @org_pk = @@identity

INSERT INTO External_Organizations
(org_pk,   org_nm,                 org_web_site,     lst_mod_dt, lst_mod_user_pk, created_user_pk, created_dt, ext_org_type, org_email_domain, marked_for_deletion_fg)
VALUES
(@org_pk, 'ALAMEDA COUNTY LIB ',  NULL        ,     getdate() , 10000001       , 10000001       , getdate() , 28001       , NULL            , 0                     )

INSERT INTO Org_Locations
(location_nm   , location_primary_fg, lst_mod_user_pk, created_dt, created_user_pk, lst_mod_dt, org_pk)
VALUES
('Headquarters', 1                  , 10000001       ,  getdate(), 10000001       , getdate() , @org_pk)
SET @location_pk = @@identity

INSERT INTO Org_Address 
(addr1,             addr2,             org_addr_type, city        , zipcode, lst_mod_dt, addr_bad_fg, created_dt, created_user_pk, lst_mod_user_pk, zip_plus, country, county, province, state,    addr_bad_dt, attention_line, org_locations_pk)
VALUES
('3121 DIABLO AVE', 'BUS & GOVT LIB' , 72001          , 'HAYWARD'   , '94545', getdate() , 1          , getdate() , 10000001       , 10000001       , null,     null,    null,   null   , 'CA',    getdate() , null, @location_pk)
SET @org_addr_pk = @@identity

INSERT INTO Org_Phone 
(area_code, phone_no, org_phone_type, lst_mod_dt, country_cd, phone_bad_fg, created_dt, created_user_pk, phone_bad_dt, lst_mod_user_pk, phone_extension, org_locations_pk)
VALUES
(703      , 5065770 , 73001           , getdate() , 1         , 0           , getdate() , 10000001      , null        , 10000001       , null           , @location_pk)


--
-- BEVERLY BRUNINGA LBR  
--
INSERT INTO org_parent values(24002)
SET @org_pk = @@identity

INSERT INTO External_Organizations
(org_pk,   org_nm,                 org_web_site,     lst_mod_dt, lst_mod_user_pk, created_user_pk, created_dt, ext_org_type, org_email_domain, marked_for_deletion_fg)
VALUES
(@org_pk, 'BEVERLY BRUNINGA LBR',  NULL        ,     getdate() , 10000001       , 10000001       , getdate() , 28001       , NULL            , 0                     )

INSERT INTO Org_Locations
(location_nm   , location_primary_fg, lst_mod_user_pk, created_dt, created_user_pk, lst_mod_dt, org_pk)
VALUES
('Headquarters', NULL               , 10000001       ,  getdate(), 10000001       , getdate() , @org_pk)
SET @location_pk = @@identity

INSERT INTO Org_Address 
(addr1,                  addr2,         org_addr_type, city        , zipcode, lst_mod_dt, addr_bad_fg, created_dt, created_user_pk, lst_mod_user_pk, zip_plus, country, county, province, state,    addr_bad_dt, attention_line, org_locations_pk)
VALUES
('TENN MUNC LEAG #317', '226 CAPITOL' , 72002        , 'NASHVILLE' , '37219', getdate() , 0          , getdate() , 10000001       , 10000001       , null,     null,    null,   null    , 'TN' ,    null       , null, @location_pk)
SET @org_addr_pk = @@identity

INSERT INTO Org_Phone 
(area_code, phone_no, org_phone_type, lst_mod_dt, country_cd, phone_bad_fg, created_dt, created_user_pk, phone_bad_dt, lst_mod_user_pk, phone_extension, org_locations_pk)
VALUES
(615      , 5065771 , 73001         , getdate() , 1         , 0           , getdate() , 10000001      , null        , 10000001       , null           , @location_pk)


INSERT INTO Ext_Org_Associates 
(org_pk,  person_pk, org_pos_title, org_locations_pk, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk) 
VALUES
(@org_pk, 10000101 , 76002        , @location_pk    , getdate() , 10000001       , getdate() , 10000001)

INSERT INTO Ext_Org_Associate_Comments 
(org_pk,  person_pk, comment_txt,                    comment_dt, created_user_pk) 
VALUES
(@org_pk, 10000101, 'Comment Test for Another Org' , getdate() , 10000001)


INSERT INTO Ext_Org_Associates 
(org_pk,  person_pk, org_pos_title, org_locations_pk, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk) 
VALUES
(@org_pk, 10000402 , 76005        , @location_pk    , getdate() , 10000001       , getdate() , 10000001)



--
-- BSNSS PRFSSNL WMNS FND  
--
INSERT INTO org_parent values(24002)
SET @org_pk = @@identity

INSERT INTO External_Organizations
(org_pk,   org_nm,                 org_web_site,     lst_mod_dt, lst_mod_user_pk, created_user_pk, created_dt, ext_org_type, org_email_domain, marked_for_deletion_fg)
VALUES
(@org_pk, 'BSNSS PRFSSNL WMNS FND',  NULL        ,     getdate() , 10000001       , 10000001       , getdate() , 28003       , NULL            , 0                     )

INSERT INTO Org_Locations
(location_nm   , location_primary_fg, lst_mod_user_pk, created_dt, created_user_pk, lst_mod_dt, org_pk)
VALUES
('Headquarters', NULL               , 10000001       ,  getdate(), 10000001       , getdate() , @org_pk)
SET @location_pk = @@identity

INSERT INTO Org_Address 
(addr1,              addr2, org_addr_type, city         , zipcode, lst_mod_dt, addr_bad_fg, created_dt, created_user_pk, lst_mod_user_pk, zip_plus, country, county, province, state,    addr_bad_dt, attention_line, org_locations_pk)
VALUES
('2012 MASS AVE NW', NULL , 72001        , 'WASHINGTON' , '20036', getdate() , 0          , getdate() , 10000001       , 10000001       , null,     null,    null,   null    , 'DC' ,    null       , null, @location_pk)
SET @org_addr_pk = @@identity

INSERT INTO Org_Phone 
(area_code, phone_no, org_phone_type, lst_mod_dt, country_cd, phone_bad_fg, created_dt, created_user_pk, phone_bad_dt, lst_mod_user_pk, phone_extension, org_locations_pk)
VALUES
(202      , 5065770 , 73002         , getdate() , 1         , 0           , getdate() , 10000001      , null        , 10000001       , null           , @location_pk)


--
-- BUFFALO ERIE CO LIB   
--
INSERT INTO org_parent values(24002)
SET @org_pk = @@identity

INSERT INTO External_Organizations
(org_pk,   org_nm,                 org_web_site,     lst_mod_dt, lst_mod_user_pk, created_user_pk, created_dt, ext_org_type, org_email_domain, marked_for_deletion_fg)
VALUES
(@org_pk, 'BUFFALO ERIE CO LIB',  NULL        ,     getdate() , 10000001       , 10000001       , getdate() , 28001       , NULL            , 0                     )

INSERT INTO Org_Locations
(location_nm      , location_primary_fg, lst_mod_user_pk, created_dt, created_user_pk, lst_mod_dt, org_pk)
VALUES
('Downtown Branch', 1                  , 10000001       ,  getdate(), 10000001       , getdate() , @org_pk)
SET @location_pk = @@identity

INSERT INTO Org_Address 
(addr1,                         addr2, org_addr_type, city        , zipcode, lst_mod_dt, addr_bad_fg, created_dt, created_user_pk, lst_mod_user_pk, zip_plus, country, county, province, state,    addr_bad_dt, attention_line,     org_locations_pk)
VALUES
('BUS-LABOR DEPT LAFAYETTE SQ', NULL , 72001        , 'BUFFALO' ,   '14203', getdate() , 0          , getdate() , 10000001       , 10000001       , null,     null,    null,   null    , 'NY' ,    null       , 'Librarian Manager', @location_pk)
SET @org_addr_pk = @@identity

INSERT INTO Org_Address 
(addr1,              addr2, org_addr_type, city        , zipcode, lst_mod_dt, addr_bad_fg, created_dt, created_user_pk, lst_mod_user_pk, zip_plus, country, county, province, state,    addr_bad_dt, attention_line, org_locations_pk)
VALUES
('Loading Dock',     NULL , 72002        , 'BUFFALO' ,   '14203', getdate() , 0          , getdate() , 10000001       , 10000001       , null,     null,    null,   null    , 'NY' ,    null       , 'Dock Manager', @location_pk)
SET @org_addr_pk = @@identity

INSERT INTO Org_Phone 
(area_code, phone_no, org_phone_type, lst_mod_dt, country_cd, phone_bad_fg, created_dt, created_user_pk, phone_bad_dt, lst_mod_user_pk, phone_extension, org_locations_pk)
VALUES
(716      , 5555770 , 73001         , getdate() , 1         , 0           , getdate() , 10000001      , null        , 10000001       , null           , @location_pk)

INSERT INTO Org_Phone 
(area_code, phone_no, org_phone_type, lst_mod_dt, country_cd, phone_bad_fg, created_dt, created_user_pk, phone_bad_dt, lst_mod_user_pk, phone_extension, org_locations_pk)
VALUES
(716      , 5555771 , 73002        , getdate() , 1         , 0           , getdate() , 10000001      , null        , 10000001       , null           , @location_pk)


--
-- CALIFORNIA STATE LIBRARY   
--
INSERT INTO org_parent values(24002)
SET @org_pk = @@identity

INSERT INTO External_Organizations
(org_pk,   org_nm,                     org_web_site,     lst_mod_dt, lst_mod_user_pk, created_user_pk, created_dt, ext_org_type, org_email_domain, marked_for_deletion_fg)
VALUES
(@org_pk, 'CALIFORNIA STATE LIBRARY',  NULL        ,     getdate() , 10000001       , 10000001       , getdate() , 28001       , NULL            , 0                     )

INSERT INTO Org_Locations
(location_nm   , location_primary_fg, lst_mod_user_pk, created_dt, created_user_pk, lst_mod_dt, org_pk)
VALUES
('Main Headquarters', 1                  , 10000001       ,  getdate(), 10000001       , getdate() , @org_pk)
SET @location_pk = @@identity

INSERT INTO Org_Address 
(addr1,                 addr2,         org_addr_type, city        ,  zipcode, lst_mod_dt, addr_bad_fg, created_dt, created_user_pk, lst_mod_user_pk, zip_plus, country, county, province, state,    addr_bad_dt, attention_line, org_locations_pk)
VALUES
('PERIODICALS SECTION', 'BOX 942837' , 72001        , 'SACRAMENTO' , '94237', getdate() , 0          , getdate() , 10000001       , 10000001       , null,     null,    null,   null    , 'CA' ,    null       , null, @location_pk)
SET @org_addr_pk = @@identity

INSERT INTO Org_Phone 
(area_code, phone_no, org_phone_type, lst_mod_dt, country_cd, phone_bad_fg, created_dt, created_user_pk, phone_bad_dt, lst_mod_user_pk, phone_extension, org_locations_pk)
VALUES
(916      , 5065770 , 73002         , getdate() , 1         , 1           , getdate() , 10000001      , getdate()         , 10000001       , null           , @location_pk)


--
-- CENTER FOR LAB EDUC & RESEARC  
--
INSERT INTO org_parent values(24002)
SET @org_pk = @@identity

INSERT INTO External_Organizations
(org_pk,   org_nm,                           org_web_site,     lst_mod_dt, lst_mod_user_pk, created_user_pk, created_dt, ext_org_type, org_email_domain, marked_for_deletion_fg)
VALUES
(@org_pk, 'CENTER FOR LAB EDUC & RESEARC',  NULL        ,     getdate() , 10000001       , 10000001       , getdate() , 28002       , NULL            , 0                     )

INSERT INTO Org_Locations
(location_nm   , location_primary_fg, lst_mod_user_pk, created_dt, created_user_pk, lst_mod_dt, org_pk)
VALUES
('Headquarters', 1               , 10000001       ,  getdate(), 10000001       , getdate() , @org_pk)
SET @location_pk = @@identity

INSERT INTO Org_Address 
(addr1,                       addr2,                     org_addr_type, city        ,  zipcode, lst_mod_dt, addr_bad_fg, created_dt, created_user_pk, lst_mod_user_pk, zip_plus, country, county, province, state,    addr_bad_dt, attention_line, org_locations_pk)
VALUES
('UNIV OF AL AT BIRMINGHAM', 'SCH OF BUS UNIV STATION' , 72001        , 'BIRMINGHAM' , '35294', getdate() , 0          , getdate() , 10000001       , 10000001       , null,     null,    null,   null    , 'AL' ,    null       , null, @location_pk)
SET @org_addr_pk = @@identity

INSERT INTO Org_Phone 
(area_code, phone_no, org_phone_type, lst_mod_dt, country_cd, phone_bad_fg, created_dt, created_user_pk, phone_bad_dt, lst_mod_user_pk, phone_extension, org_locations_pk)
VALUES
(205      , 5065770 , 73002         , getdate() , 1         , 0           , getdate() , 10000001      , null        , 10000001       , null           , @location_pk)


--
-- DETROIT PUB LIB  
--
INSERT INTO org_parent values(24002)
SET @org_pk = @@identity

INSERT INTO External_Organizations
(org_pk,   org_nm,             org_web_site,     lst_mod_dt, lst_mod_user_pk, created_user_pk, created_dt, ext_org_type, org_email_domain, marked_for_deletion_fg)
VALUES
(@org_pk, 'DETROIT PUB LIB',  NULL        ,     getdate() , 10000001       , 10000001       , getdate() , 28001       , NULL            , 0                     )

INSERT INTO Org_Locations
(location_nm   , location_primary_fg, lst_mod_user_pk, created_dt, created_user_pk, lst_mod_dt, org_pk)
VALUES
('Minor Branch', NULL               , 10000001       ,  getdate(), 10000001       , getdate() , @org_pk)
SET @location_pk = @@identity

INSERT INTO Org_Address 
(addr1,               addr2,           org_addr_type, city           ,  zipcode, lst_mod_dt, addr_bad_fg, created_dt, created_user_pk, lst_mod_user_pk, zip_plus, country, county, province, state,    addr_bad_dt, attention_line, org_locations_pk)
VALUES
('5201 WOODWARD AV', 'SOC ECON PER ' , 72001        , 'DETROIT'      , '48202', getdate() , 0          , getdate() , 10000001        , 10000001       , null,     null,    null,   null    , 'MI' ,    null       , null, @location_pk)
SET @org_addr_pk = @@identity

INSERT INTO Org_Phone 
(area_code, phone_no, org_phone_type, lst_mod_dt, country_cd, phone_bad_fg, created_dt, created_user_pk, phone_bad_dt, lst_mod_user_pk, phone_extension, org_locations_pk)
VALUES
(313      , 1234567 , 73001           , getdate() , 1         , 0           , getdate() , 10000001      , null        , 10000001       , null           , @location_pk)


--
-- DREXEL UNIV LIBR  
--
INSERT INTO org_parent values(24002)
SET @org_pk = @@identity

INSERT INTO External_Organizations
(org_pk,   org_nm,              org_web_site,     lst_mod_dt, lst_mod_user_pk, created_user_pk, created_dt, ext_org_type, org_email_domain, marked_for_deletion_fg)
VALUES
(@org_pk, 'DREXEL UNIV LIBR',  NULL        ,     getdate() , 10000001       , 10000001       , getdate() , 28001       , NULL            , 0                     )

INSERT INTO Org_Locations
(location_nm   , location_primary_fg, lst_mod_user_pk, created_dt, created_user_pk, lst_mod_dt, org_pk)
VALUES
('Headquarters', 1               , 10000001       ,  getdate(), 10000001       , getdate() , @org_pk)
SET @location_pk = @@identity

INSERT INTO Org_Address 
(addr1,                  addr2,           org_addr_type, city           ,  zipcode, lst_mod_dt, addr_bad_fg, created_dt, created_user_pk, lst_mod_user_pk, zip_plus, country, county, province, state,    addr_bad_dt, attention_line, org_locations_pk)
VALUES
('32ND AND CHESTNUT ST', 'SERIALS DEPT' , 72001          , 'PHILADELPHIA' , '19104', getdate() , 0          , getdate() , 10000001       ,  10000001       , null,     null,    null,   null  ,  'PA',    null       , null, @location_pk)
SET @org_addr_pk = @@identity

INSERT INTO Org_Address 
(addr1,                  addr2,             org_addr_type, city           ,  zipcode, lst_mod_dt, addr_bad_fg, created_dt, created_user_pk, lst_mod_user_pk, zip_plus, country, county, province, state,    addr_bad_dt, attention_line, org_locations_pk)
VALUES
('32ND AND CHESTNUT ST', 'SHIPPING OFFICE' , 72002          , 'PHILADELPHIA' , '19104', getdate() , 0          , getdate() , 10000001       ,  10000001       , null,     null,    null,   null  , 'PA',    null       , null, @location_pk)
SET @org_addr_pk = @@identity

INSERT INTO Org_Phone 
(area_code, phone_no, org_phone_type, lst_mod_dt, country_cd, phone_bad_fg, created_dt, created_user_pk, phone_bad_dt, lst_mod_user_pk, phone_extension, org_locations_pk)
VALUES
(215      , 5550123 , 73001           , getdate() , 1         , 0           , getdate() , 10000001      , null        , 10000001       , null           , @location_pk)

INSERT INTO Org_Phone 
(area_code, phone_no, org_phone_type, lst_mod_dt, country_cd, phone_bad_fg, created_dt, created_user_pk, phone_bad_dt, lst_mod_user_pk, phone_extension, org_locations_pk)
VALUES
(215      , 5550111 , 73002           , getdate() , 1         , 0           , getdate() , 10000001      , null        , 10000001       , null           , @location_pk)


INSERT INTO Org_Locations
(location_nm   ,      location_primary_fg, lst_mod_user_pk, created_dt, created_user_pk, lst_mod_dt, org_pk)
VALUES
('Law School Branch', NULL              , 10000001       ,  getdate(), 10000001       , getdate() , @org_pk)
SET @location_pk = @@identity

INSERT INTO Org_Address 
(addr1,                  addr2,           org_addr_type, city           ,  zipcode, lst_mod_dt, addr_bad_fg, created_dt, created_user_pk, lst_mod_user_pk, zip_plus, country, county, province, state,    addr_bad_dt, attention_line, org_locations_pk)
VALUES
('30TH AND WALNUT ST', 'TORTS DEPT' ,    72001          , 'PHILADELPHIA' , '19104', getdate() , 0          , getdate() , 10000001       ,  10000001       , null,     null,    null,   null    , 'PA',    null       , null, @location_pk)
SET @org_addr_pk = @@identity

INSERT INTO Org_Address 
(addr1,                  addr2,             org_addr_type, city           ,  zipcode, lst_mod_dt, addr_bad_fg, created_dt, created_user_pk, lst_mod_user_pk, zip_plus, country, county, province, state,    addr_bad_dt, attention_line, org_locations_pk)
VALUES
('30TH AND WALNUT ST', 'SHIPPING OFFICE' , 72002          , 'PHILADELPHIA' , '19104', getdate() , 0          , getdate() , 10000001       ,  10000001       , null,     null,    null,   null    , 'PA',    null       , null, @location_pk)
SET @org_addr_pk = @@identity

INSERT INTO Org_Phone 
(area_code, phone_no, org_phone_type, lst_mod_dt, country_cd, phone_bad_fg, created_dt, created_user_pk, phone_bad_dt, lst_mod_user_pk, phone_extension, org_locations_pk)
VALUES
(215      , 5553333 , 73001           , getdate() , 1         , 0           , getdate() , 10000001      , null        , 10000001       , null           , @location_pk)

INSERT INTO Org_Phone 
(area_code, phone_no, org_phone_type, lst_mod_dt, country_cd, phone_bad_fg, created_dt, created_user_pk, phone_bad_dt, lst_mod_user_pk, phone_extension, org_locations_pk)
VALUES
(215      , 5553311 , 73002           , getdate() , 1         , 0           , getdate() , 10000001      , null        , 10000001       , null           , @location_pk)


--
-- GOLDEN GATE UNIVERSITY 
--
INSERT INTO org_parent values(24002)
SET @org_pk = @@identity

INSERT INTO External_Organizations
(org_pk,   org_nm,                    org_web_site,     lst_mod_dt, lst_mod_user_pk, created_user_pk, created_dt, ext_org_type, org_email_domain, marked_for_deletion_fg)
VALUES
(@org_pk, 'GOLDEN GATE UNIVERSITY',  NULL        ,     getdate() , 10000001       , 10000001       , getdate() , 28002       , NULL            , 0                     )

INSERT INTO Org_Locations
(location_nm   , location_primary_fg, lst_mod_user_pk, created_dt, created_user_pk, lst_mod_dt, org_pk)
VALUES
('Headquarters', 1                  , 10000001       ,  getdate(), 10000001       , getdate() , @org_pk)
SET @location_pk = @@identity

INSERT INTO Org_Address 
(addr1,                          addr2, org_addr_type, city            , zipcode, lst_mod_dt, addr_bad_fg, created_dt, created_user_pk, lst_mod_user_pk, zip_plus, country, county, province, state,    addr_bad_dt, attention_line, org_locations_pk)
VALUES
('LIBR PER DEPT 536 MISSION ST', NULL , 72002        , 'SAN FRANCISCO' , '94105', getdate() , 0          , getdate() , 10000001       , 10000001       , null,     null,    null,   null    , 'CA',    null       , null, @location_pk)
SET @org_addr_pk = @@identity

INSERT INTO Org_Phone 
(area_code, phone_no, org_phone_type, lst_mod_dt, country_cd, phone_bad_fg, created_dt, created_user_pk, phone_bad_dt, lst_mod_user_pk, phone_extension, org_locations_pk)
VALUES
(415      , 5065770 , 73002         , getdate() , 1011         , 1           , getdate() , 10000001      , getdate()        , 10000001       , null           , @location_pk)


INSERT INTO Ext_Org_Associates 
(org_pk,  person_pk, org_pos_title, org_locations_pk, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk) 
VALUES
(@org_pk, 10000701 , 76003        , @location_pk    , getdate() , 10000001       , getdate() , 10000001)


INSERT INTO Ext_Org_Associates 
(org_pk,  person_pk, org_pos_title, org_locations_pk, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk) 
VALUES
(@org_pk, 10000705 , 76006        , NULL,             getdate() , 10000001       , getdate() , 10000001)

INSERT INTO Ext_Org_Associate_Comments 
(org_pk,  person_pk, comment_txt,                  comment_dt, created_user_pk) 
VALUES
(@org_pk, 10000705, 'Org Associate Comment Test' , getdate() , 10000001)



--
-- IND REL LIBR MIT 
--
INSERT INTO org_parent values(24002)
SET @org_pk = @@identity

INSERT INTO External_Organizations
(org_pk,   org_nm,              org_web_site,     lst_mod_dt, lst_mod_user_pk, created_user_pk, created_dt, ext_org_type, org_email_domain, marked_for_deletion_fg)
VALUES
(@org_pk, 'IND REL LIBR MIT',  NULL        ,     getdate() , 10000001       , 10000001       , getdate() , 28001       , NULL            , 0                     )

INSERT INTO Org_Locations
(location_nm   , location_primary_fg, lst_mod_user_pk, created_dt, created_user_pk, lst_mod_dt, org_pk)
VALUES
('Headquarters', NULL               , 10000001       ,  getdate(), 10000001       , getdate() , @org_pk)
SET @location_pk = @@identity

INSERT INTO Org_Address 
(addr1,              addr2, org_addr_type, city            , zipcode, lst_mod_dt, addr_bad_fg, created_dt, created_user_pk, lst_mod_user_pk, zip_plus, country, county, province, state,    addr_bad_dt, attention_line, org_locations_pk)
VALUES
('BLDG E53 RM 238 ', NULL , 72001          , 'CAMBRIDGE' ,    '02139', getdate() , 0          , getdate() , 10000001       ,  10000001       , null,     null,    null,   null  , 'MA',    null       , null, @location_pk)
SET @org_addr_pk = @@identity

INSERT INTO Org_Phone 
(area_code, phone_no, org_phone_type, lst_mod_dt, country_cd, phone_bad_fg, created_dt, created_user_pk, phone_bad_dt, lst_mod_user_pk, phone_extension, org_locations_pk)
VALUES
(617      , 5065770 , 73001          , getdate() , 1         , 0           , getdate() , 10000001      , null        , 10000001       , null           , @location_pk)


INSERT INTO Ext_Org_Associates 
(org_pk,  person_pk, org_pos_title, org_locations_pk, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk) 
VALUES
(@org_pk, 10000201 , 76004        , @location_pk    , getdate() , 10000001       , getdate() , 10000001)


--
-- INST OF INDUS RELATIONS
--
INSERT INTO org_parent values(24002)
SET @org_pk = @@identity

INSERT INTO External_Organizations
(org_pk,   org_nm,                     org_web_site,     lst_mod_dt, lst_mod_user_pk, created_user_pk, created_dt, ext_org_type, org_email_domain, marked_for_deletion_fg)
VALUES
(@org_pk, 'INST OF INDUS RELATIONS',  NULL        ,     getdate() , 10000001       , 10000001       , getdate() , 28003       , NULL            , 0                     )

INSERT INTO Org_Locations
(location_nm   , location_primary_fg, lst_mod_user_pk, created_dt, created_user_pk, lst_mod_dt, org_pk)
VALUES
('Headquarters', NULL               , 10000001       ,  getdate(), 10000001       , getdate() , @org_pk)
SET @location_pk = @@identity

INSERT INTO Org_Address 
(addr1,             addr2, org_addr_type, city         , zipcode, lst_mod_dt, addr_bad_fg, created_dt, created_user_pk, lst_mod_user_pk, zip_plus, country, county, province, state,    addr_bad_dt, attention_line, org_locations_pk)
VALUES
('761 HARRISON AV', NULL , 72001        , 'BOSTON' ,    '02118', getdate() , 0          , getdate() , 10000001       ,  10000001       , null,     null,    null,   null    , 'MA' ,    null       , null, @location_pk)
SET @org_addr_pk = @@identity

INSERT INTO Org_Phone 
(area_code, phone_no, org_phone_type, lst_mod_dt, country_cd, phone_bad_fg, created_dt, created_user_pk, phone_bad_dt, lst_mod_user_pk, phone_extension, org_locations_pk)
VALUES
(617      , 5065770 , 73001         , getdate() , 1         , 0           , getdate() , 10000001      , null        , 10000001       , null           , @location_pk)


--
-- LABOR EDUCATION PROJECT
--
INSERT INTO org_parent values(24002)
SET @org_pk = @@identity

INSERT INTO External_Organizations
(org_pk,   org_nm,                     org_web_site,     lst_mod_dt, lst_mod_user_pk, created_user_pk, created_dt, ext_org_type, org_email_domain, marked_for_deletion_fg)
VALUES
(@org_pk, 'LABOR EDUCATION PROJECT',  NULL        ,     getdate() , 10000001       , 10000001       , getdate() , 28003       , NULL            , 0                     )

INSERT INTO Org_Locations
(location_nm   , location_primary_fg, lst_mod_user_pk, created_dt, created_user_pk, lst_mod_dt, org_pk)
VALUES
('Headquarters', 1                  , 10000001       ,  getdate(), 10000001       , getdate() , @org_pk)
SET @location_pk = @@identity

INSERT INTO Org_Address 
(addr1,                    addr2, org_addr_type, city         , zipcode, lst_mod_dt, addr_bad_fg, created_dt, created_user_pk, lst_mod_user_pk, zip_plus, country, county, province, state,    addr_bad_dt, attention_line, org_locations_pk)
VALUES
('853 BROADWAY ROOM 2014', NULL , 72001          , 'NEW YORK' ,    '10003', getdate() , 0          , getdate() , 10000001      , 10000001       , null,     null,    null,   null  , 'NY' ,    null       , null, @location_pk)
SET @org_addr_pk = @@identity

INSERT INTO Org_Phone 
(area_code, phone_no, org_phone_type, lst_mod_dt, country_cd, phone_bad_fg, created_dt, created_user_pk, phone_bad_dt, lst_mod_user_pk, phone_extension, org_locations_pk)
VALUES
(212      , 5065770 , 73001         , getdate() , 1         , 0           , getdate() , 10000001      , null        , 10000001       , null           , @location_pk)


--
-- LBR REL ASSOCNINC  
--
INSERT INTO org_parent values(24002)
SET @org_pk = @@identity

INSERT INTO External_Organizations
(org_pk,   org_nm,              org_web_site,     lst_mod_dt, lst_mod_user_pk, created_user_pk, created_dt, ext_org_type, org_email_domain, marked_for_deletion_fg)
VALUES
(@org_pk, 'LBR REL ASSOCNINC', NULL        ,     getdate() , 10000001       , 10000001       , getdate() , 28003       , NULL            , 0                     )

INSERT INTO Org_Locations
(location_nm   , location_primary_fg, lst_mod_user_pk, created_dt, created_user_pk, lst_mod_dt, org_pk)
VALUES
('Headquarters', NULL               , 10000001       ,  getdate(), 10000001       , getdate() , @org_pk)
SET @location_pk = @@identity

INSERT INTO Org_Address 
(addr1,                addr2, org_addr_type, city           ,  zipcode, lst_mod_dt, addr_bad_fg, created_dt, created_user_pk, lst_mod_user_pk, zip_plus, country, county, province, state,    addr_bad_dt, attention_line, org_locations_pk)
VALUES
('7501 GOLDEN VALLEY', NULL , 72002        , 'GOLDEN VALLEY' , '55427', getdate() , 0          , getdate() , 10000001       , 10000001       , null,     null,    null,   null    , 'MN',    null       , null, @location_pk)
SET @org_addr_pk = @@identity

INSERT INTO Org_Phone 
(area_code, phone_no, org_phone_type, lst_mod_dt, country_cd, phone_bad_fg, created_dt, created_user_pk, phone_bad_dt, lst_mod_user_pk, phone_extension, org_locations_pk)
VALUES
(703      , 5065770 , 73001           , getdate() , 1         , 0           , getdate() , 10000001      , null        , 10000001       , null           , @location_pk)


--
-- LIBRARY OF CONGRESS  
--
INSERT INTO org_parent values(24002)
SET @org_pk = @@identity

INSERT INTO External_Organizations
(org_pk,   org_nm,                org_web_site,     lst_mod_dt, lst_mod_user_pk, created_user_pk, created_dt, ext_org_type, org_email_domain, marked_for_deletion_fg)
VALUES
(@org_pk, 'LIBRARY OF CONGRESS', 'www.loc.gov',    getdate() , 10000001       , 10000001       , getdate() , 28001       , NULL            , 0                     )

INSERT INTO Org_Locations
(location_nm   , location_primary_fg, lst_mod_user_pk, created_dt, created_user_pk, lst_mod_dt, org_pk)
VALUES
('Headquarters', 1                 , 10000001       ,  getdate(), 10000001       , getdate() , @org_pk)
SET @location_pk = @@identity

INSERT INTO Org_Address 
(addr1,                    addr2, org_addr_type, city           ,  zipcode, lst_mod_dt, addr_bad_fg, created_dt, created_user_pk, lst_mod_user_pk, zip_plus, country, county, province, state,    addr_bad_dt, attention_line, org_locations_pk)
VALUES
('LABOR SECTION ECON DIV', NULL , 72001        , 'WASHINGTON' ,    '20540', getdate() , 0          , getdate() , 10000001       , 10000001       , null,     null,    null,   null    , 'DC' ,    null       , null, @location_pk)
SET @org_addr_pk = @@identity

INSERT INTO Org_Phone 
(area_code, phone_no, org_phone_type, lst_mod_dt, country_cd, phone_bad_fg, created_dt, created_user_pk, phone_bad_dt, lst_mod_user_pk, phone_extension, org_locations_pk)
VALUES
(202      , 5065770 , 73001           , getdate() , 1         , 0           , getdate() , 10000001      , null        , 10000001       , null           , @location_pk)


--
-- MINN AFL CIO RESEARCH 
--
INSERT INTO org_parent values(24002)
SET @org_pk = @@identity

INSERT INTO External_Organizations
(org_pk,   org_nm,                  org_web_site,     lst_mod_dt, lst_mod_user_pk, created_user_pk, created_dt, ext_org_type, org_email_domain, marked_for_deletion_fg)
VALUES
(@org_pk, 'MINN AFL CIO RESEARCH', NULL,             getdate() , 10000001       , 10000001       , getdate() , 28004       , NULL            , 0                     )

INSERT INTO Org_Locations
(location_nm   , location_primary_fg, lst_mod_user_pk, created_dt, created_user_pk, lst_mod_dt, org_pk)
VALUES
('Headquarters', NULL               , 10000001       ,  getdate(), 10000001       , getdate() , @org_pk)
SET @location_pk = @@identity

INSERT INTO Org_Address 
(addr1,                  addr2, org_addr_type, city           ,  zipcode, lst_mod_dt, addr_bad_fg, created_dt, created_user_pk, lst_mod_user_pk, zip_plus, country, county, province, state,    addr_bad_dt, attention_line, org_locations_pk)
VALUES
('525 PARK STREET #110', NULL , 72001          , 'SAINT PAUL' ,     '55103', getdate() , 0          , getdate() , 10000001       , 10000001       , null,     null,    null,   null  , 'MN',    null       , null, @location_pk)
SET @org_addr_pk = @@identity

INSERT INTO Org_Phone 
(area_code, phone_no, org_phone_type, lst_mod_dt, country_cd, phone_bad_fg, created_dt, created_user_pk, phone_bad_dt, lst_mod_user_pk, phone_extension, org_locations_pk)
VALUES
(612      , 5065770 , 73001         , getdate() , 1         , 0           , getdate() , 10000001      , null        , 10000001       , null           , @location_pk)


--
-- MUNICIPAL REF LIBRARY 
--
INSERT INTO org_parent values(24002)
SET @org_pk = @@identity

INSERT INTO External_Organizations
(org_pk,   org_nm,                  org_web_site,     lst_mod_dt, lst_mod_user_pk, created_user_pk, created_dt, ext_org_type, org_email_domain, marked_for_deletion_fg)
VALUES
(@org_pk, 'MUNICIPAL REF LIBRARY', NULL,             getdate() , 10000001       , 10000001       , getdate() , 28001        , NULL            , 0)

INSERT INTO Org_Locations
(location_nm   , location_primary_fg, lst_mod_user_pk, created_dt, created_user_pk, lst_mod_dt, org_pk)
VALUES
('Headquarters', 1                  , 10000001       ,  getdate(), 10000001       , getdate() , @org_pk)
SET @location_pk = @@identity

INSERT INTO Org_Address 
(addr1,               addr2,   org_addr_type, city           ,  zipcode, lst_mod_dt, addr_bad_fg, created_dt, created_user_pk, lst_mod_user_pk, zip_plus, country, county, province, state,    addr_bad_dt, attention_line, org_locations_pk)
VALUES
('RM 1004 CITY HALL', NULL , 72001          ,  'CHICAGO' ,        '60602', getdate() , 0          , getdate() , 10000001       , 10000001       , null,     null,    null,   null  , 'IL',    null       , null, @location_pk)
SET @org_addr_pk = @@identity

INSERT INTO Org_Phone 
(area_code, phone_no, org_phone_type, lst_mod_dt, country_cd, phone_bad_fg, created_dt, created_user_pk, phone_bad_dt, lst_mod_user_pk, phone_extension, org_locations_pk)
VALUES
(815      , 5065770 , 73001         , getdate() , 1         , 0           , getdate() , 10000001      , null        , 10000001       , null           , @location_pk)


--
-- MUNICIPAL REF LIBRARY 
--
INSERT INTO org_parent values(24002)
SET @org_pk = @@identity

INSERT INTO External_Organizations
(org_pk,   org_nm,                  org_web_site,     lst_mod_dt, lst_mod_user_pk, created_user_pk, created_dt, ext_org_type, org_email_domain, marked_for_deletion_fg)
VALUES
(@org_pk, 'MUNICIPAL REF LIBRARY', NULL,             getdate() , 10000001       , 10000001       , getdate() , 28001       , NULL            , 0                     )

INSERT INTO Org_Locations
(location_nm   , location_primary_fg, lst_mod_user_pk, created_dt, created_user_pk, lst_mod_dt, org_pk)
VALUES
('Headquarters', NULL               , 10000001       ,  getdate(), 10000001       , getdate() , @org_pk)
SET @location_pk = @@identity

INSERT INTO Org_Address 
(addr1,               addr2,   org_addr_type, city           ,  zipcode, lst_mod_dt, addr_bad_fg, created_dt, created_user_pk, lst_mod_user_pk, zip_plus, country, county, province, state,    addr_bad_dt, attention_line, org_locations_pk)
VALUES
('CITY HALL RM 224', NULL , 72001          ,  'CINCINNATI' ,    '45202', getdate() , 0          , getdate() , 10000001         , 10000001       , null,     null,    null,   null  , 'OH',    null       , null, @location_pk)
SET @org_addr_pk = @@identity

INSERT INTO Org_Phone 
(area_code, phone_no, org_phone_type, lst_mod_dt, country_cd, phone_bad_fg, created_dt, created_user_pk, phone_bad_dt, lst_mod_user_pk, phone_extension, org_locations_pk)
VALUES
(703      , 5065770 , 73001         , getdate() , 1         , 0           , getdate() , 10000001      , null        , 10000001       , null           , @location_pk)


--
-- MUNICIPAL REF LIBRARY 
--
INSERT INTO org_parent values(24002)
SET @org_pk = @@identity

INSERT INTO External_Organizations
(org_pk,   org_nm,                  org_web_site,     lst_mod_dt, lst_mod_user_pk, created_user_pk, created_dt, ext_org_type, org_email_domain, marked_for_deletion_fg)
VALUES
(@org_pk, 'MUNICIPAL REF LIBRARY', NULL,             getdate() , 10000001       , 10000001       , getdate() , 28001        , NULL            , 1                     )

INSERT INTO Org_Locations
(location_nm   , location_primary_fg, lst_mod_user_pk, created_dt, created_user_pk, lst_mod_dt, org_pk)
VALUES
('Headquarters', 1                  , 10000001       ,  getdate(), 10000001       , getdate() , @org_pk)
SET @location_pk = @@identity

INSERT INTO Org_Address 
(addr1,            addr2,   org_addr_type, city        ,  zipcode, lst_mod_dt, addr_bad_fg, created_dt, created_user_pk, lst_mod_user_pk, zip_plus, country, county, province, state,    addr_bad_dt, attention_line, org_locations_pk)
VALUES
('2 WOODWARD AVE', NULL , 72001          ,  'DETROIT' ,    '48226', getdate() , 0          , getdate() , 10000001        ,  10000001       , null,     null,    null,   null , 'MI',    null       , null, @location_pk)
SET @org_addr_pk = @@identity

INSERT INTO Org_Phone 
(area_code, phone_no, org_phone_type, lst_mod_dt, country_cd, phone_bad_fg, created_dt, created_user_pk, phone_bad_dt, lst_mod_user_pk, phone_extension, org_locations_pk)
VALUES
(313      , 5065770 , 73001         , getdate() , 1         , 0           , getdate() , 10000001      , null        , 10000001       , null           , @location_pk)


--
-- SANTA BARBARA COUNTY EMPLS AS 
--
INSERT INTO org_parent values(24002)
SET @org_pk = @@identity

INSERT INTO External_Organizations
(org_pk,   org_nm,                          org_web_site,     lst_mod_dt, lst_mod_user_pk, created_user_pk, created_dt, ext_org_type, org_email_domain, marked_for_deletion_fg)
VALUES
(@org_pk, 'SANTA BARBARA COUNTY EMPLS AS', NULL,             getdate() , 10000001       , 10000001       , getdate() , 28004       , NULL            , 0                     )

INSERT INTO Org_Locations
(location_nm   , location_primary_fg, lst_mod_user_pk, created_dt, created_user_pk, lst_mod_dt, org_pk)
VALUES
('Headquarters', NULL               , 10000001       ,  getdate(), 10000001       , getdate() , @org_pk)
SET @location_pk = @@identity

INSERT INTO Org_Address 
(addr1,                    addr2,   org_addr_type, city           ,  zipcode, lst_mod_dt, addr_bad_fg, created_dt, created_user_pk, lst_mod_user_pk, zip_plus, country, county, province, state,    addr_bad_dt, attention_line, org_locations_pk)
VALUES
('226 E CANON PERDIDO', 'SUITE F' , 72001        , 'SANTA BARBARA' , '93101', getdate() , 0          , getdate() , 10000001       , 10000001       , null,     null,    null,   null    , 'CA',    null       , null, @location_pk)
SET @org_addr_pk = @@identity

INSERT INTO Org_Phone 
(area_code, phone_no, org_phone_type, lst_mod_dt, country_cd, phone_bad_fg, created_dt, created_user_pk, phone_bad_dt, lst_mod_user_pk, phone_extension, org_locations_pk)
VALUES
(805      , 5065770 , 73002        , getdate() , 1         , 0           , getdate() , 10000001      , null        , 10000001       , null           , @location_pk)


--
-- UNITED LABOR AC
--
INSERT INTO org_parent values(24002)
SET @org_pk = @@identity

INSERT INTO External_Organizations
(org_pk,   org_nm,            org_web_site,     lst_mod_dt, lst_mod_user_pk, created_user_pk, created_dt, ext_org_type, org_email_domain, marked_for_deletion_fg)
VALUES
(@org_pk, 'UNITED LABOR AC', NULL,             getdate() , 10000001       , 10000001       , getdate() , 28004       , NULL            , 0                     )

INSERT INTO Org_Locations
(location_nm   , location_primary_fg, lst_mod_user_pk, created_dt, created_user_pk, lst_mod_dt, org_pk)
VALUES
('Headquarters', NULL               , 10000001       ,  getdate(), 10000001       , getdate() , @org_pk)
SET @location_pk = @@identity

INSERT INTO Org_Address 
(addr1,         addr2,                  org_addr_type, city       ,  zipcode, lst_mod_dt, addr_bad_fg, created_dt, created_user_pk, lst_mod_user_pk, zip_plus, country, county, province, state,    addr_bad_dt, attention_line, org_locations_pk)
VALUES
('PO BOX 420', 'PETER STUYVESANT STA' , 72002        , 'NEW YORK' , '10009', getdate() , 0          , getdate() , 10000001        , 10000001       , null,     null,    null,   null    , 'NY',    null       , null, @location_pk)
SET @org_addr_pk = @@identity

INSERT INTO Org_Phone 
(area_code, phone_no, org_phone_type, lst_mod_dt, country_cd, phone_bad_fg, created_dt, created_user_pk, phone_bad_dt, lst_mod_user_pk, phone_extension, org_locations_pk)
VALUES
(212      , 5065770 , 73001        , getdate() , 1         , 0           , getdate() , 10000001      , null        , 10000001       , null           , @location_pk)


--
-- UNIV OF CONNECTICUT
--
INSERT INTO org_parent values(24002)
SET @org_pk = @@identity

INSERT INTO External_Organizations
(org_pk,   org_nm,               org_web_site,     lst_mod_dt, lst_mod_user_pk, created_user_pk, created_dt, ext_org_type, org_email_domain, marked_for_deletion_fg)
VALUES
(@org_pk, 'UNIV OF CONNECTICUT', NULL,             getdate() , 10000001       , 10000001       , getdate() , 28002       , NULL            , 0                     )

INSERT INTO Org_Locations
(location_nm   , location_primary_fg, lst_mod_user_pk, created_dt, created_user_pk, lst_mod_dt, org_pk)
VALUES
('Headquarters', NULL               , 10000001       ,  getdate(), 10000001       , getdate() , @org_pk)
SET @location_pk = @@identity

INSERT INTO Org_Address 
(addr1,         addr2,  org_addr_type, city     ,  zipcode, lst_mod_dt, addr_bad_fg, created_dt, created_user_pk, lst_mod_user_pk, zip_plus, country, county, province, state,    addr_bad_dt, attention_line, org_locations_pk)
VALUES
('LABOR ED CTR', NULL , 72001        , 'STORRS' , '06268', getdate() , 0          , getdate() , 10000001        , 10000001       , null,     null,    null,   null    , 'CT',    null       , null, @location_pk)
SET @org_addr_pk = @@identity

INSERT INTO Org_Phone 
(area_code, phone_no, org_phone_type, lst_mod_dt, country_cd, phone_bad_fg, created_dt, created_user_pk, phone_bad_dt, lst_mod_user_pk, phone_extension, org_locations_pk)
VALUES
(703      , 5065770 , 73001         , getdate() , 1         , 0           , getdate() , 10000001      , null        , 10000001       , null           , @location_pk)


--
-- UNIVERSITY OF MASSACHUSETTS
--
INSERT INTO org_parent values(24002)
SET @org_pk = @@identity

INSERT INTO External_Organizations
(org_pk,   org_nm,                        org_web_site,     lst_mod_dt, lst_mod_user_pk, created_user_pk, created_dt, ext_org_type, org_email_domain, marked_for_deletion_fg)
VALUES
(@org_pk, 'UNIVERSITY OF MASSACHUSETTS', NULL,             getdate() , 10000001       , 10000001       , getdate() , 28002       , NULL            , 0                     )

INSERT INTO Org_Locations
(location_nm      , location_primary_fg, lst_mod_user_pk, created_dt, created_user_pk, lst_mod_dt, org_pk)
VALUES
('Primary Location', 1                 , 10000001       ,  getdate(), 10000001       , getdate() , @org_pk)
SET @location_pk = @@identity

INSERT INTO Org_Address 
(addr1,           addr2,         org_addr_type, city     ,  zipcode, lst_mod_dt, addr_bad_fg, created_dt, created_user_pk, lst_mod_user_pk, zip_plus, country, county, province, state,    addr_bad_dt, attention_line, org_locations_pk)
VALUES
('LABOR CENTER', 'DRAPER HALL' , 72001        , 'AMHERST' ,'01002', getdate() , 0          , getdate() , 10000001        , 10000001       , null,     null,    null,   null    , 'MA',    null       , null, @location_pk)
SET @org_addr_pk = @@identity

INSERT INTO Org_Phone 
(area_code, phone_no, org_phone_type, lst_mod_dt, country_cd, phone_bad_fg, created_dt, created_user_pk, phone_bad_dt, lst_mod_user_pk, phone_extension, org_locations_pk)
VALUES
(617      , 5555770 , 73001        , getdate() , 1         , 0           , getdate() , 10000001      , null        , 10000001       , null           , @location_pk)


INSERT INTO Org_Locations
(location_nm   , location_primary_fg, lst_mod_user_pk, created_dt, created_user_pk, lst_mod_dt, org_pk)
VALUES
('Headquarters', NULL               , 10000001       ,  getdate(), 10000001       , getdate() , @org_pk)
SET @location_pk = @@identity

INSERT INTO Org_Address 
(addr1,         addr2,                   org_addr_type, city     ,  zipcode, lst_mod_dt, addr_bad_fg, created_dt, created_user_pk, lst_mod_user_pk, zip_plus, country, county, province, state,    addr_bad_dt, attention_line, org_locations_pk)
VALUES
('LABOR REL RES CENTER', 'DRAPER HALL' , 72001        , 'AMHERST' ,'01002', getdate() , 0          , getdate() , 10000001        , 10000001       , null,     null,    null,   null    , 'MA',    null       , null, @location_pk)
SET @org_addr_pk = @@identity

INSERT INTO Org_Phone 
(area_code, phone_no, org_phone_type, lst_mod_dt, country_cd, phone_bad_fg, created_dt, created_user_pk, phone_bad_dt, lst_mod_user_pk, phone_extension, org_locations_pk)
VALUES
(703      , 5065770 , 73001        , getdate() , 1         , 0           , getdate() , 10000001      , null        , 10000001       , null           , @location_pk)


INSERT INTO Ext_Org_Associates 
(org_pk,  person_pk, org_pos_title, org_locations_pk, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk) 
VALUES
(@org_pk, 10000101 , 76002        , @location_pk    , getdate() , 10000001       , getdate() , 10000001)

INSERT INTO Ext_Org_Associate_Comments 
(org_pk,  person_pk, comment_txt,    comment_dt, created_user_pk) 
VALUES
(@org_pk, 10000101, 'Comment Test' , getdate() , 10000001)



--
-- WISCONSIN PUBLIC RADIO 
--
INSERT INTO org_parent values(24002)
SET @org_pk = @@identity

INSERT INTO External_Organizations
(org_pk,   org_nm,                    org_web_site,     lst_mod_dt, lst_mod_user_pk, created_user_pk, created_dt, ext_org_type, org_email_domain, marked_for_deletion_fg)
VALUES
(@org_pk, 'WISCONSIN PUBLIC RADIO ', NULL,             getdate() , 10000001       , 10000001       , getdate() , 28003       , NULL            , 0                     )

INSERT INTO Org_Locations
(location_nm   , location_primary_fg, lst_mod_user_pk, created_dt, created_user_pk, lst_mod_dt, org_pk)
VALUES
('Headquarters', 1                 , 10000001       ,  getdate(), 10000001       , getdate() , @org_pk)
SET @location_pk = @@identity

INSERT INTO Org_Address 
(addr1,               addr2, org_addr_type, city            , zipcode, lst_mod_dt, addr_bad_fg, created_dt, created_user_pk, lst_mod_user_pk, zip_plus, country, county, province, state,    addr_bad_dt, attention_line, org_locations_pk)
VALUES
('2100 MAIN ST #CAC', NULL , 72001        , 'STEVENS POINT' ,'54481', getdate() , 0          , getdate() , 10000001        , 10000001       , null,     null,    null,   null    , 'WI',    null       , null, @location_pk)
SET @org_addr_pk = @@identity

INSERT INTO Org_Phone 
(area_code, phone_no, org_phone_type, lst_mod_dt, country_cd, phone_bad_fg, created_dt, created_user_pk, phone_bad_dt, lst_mod_user_pk, phone_extension, org_locations_pk)
VALUES
(703      , 5065770 , 73002           , getdate() , 1         , 0           , getdate() , 10000001      , null        , 10000001       , null           , @location_pk)


--
-- WOODBURY CNTY LABOR CN 
--
INSERT INTO org_parent values(24002)
SET @org_pk = @@identity

INSERT INTO External_Organizations
(org_pk,   org_nm,                    org_web_site,     lst_mod_dt, lst_mod_user_pk, created_user_pk, created_dt, ext_org_type, org_email_domain, marked_for_deletion_fg)
VALUES
(@org_pk, 'WOODBURY CNTY LABOR CN ', NULL,             getdate() , 10000001       , 10000001       , getdate() , 28004       , NULL            , 0                     )

INSERT INTO Org_Locations
(location_nm   , location_primary_fg, lst_mod_user_pk, created_dt, created_user_pk, lst_mod_dt, org_pk)
VALUES
('Headquarters', NULL               , 10000001       ,  getdate(), 10000001       , getdate() , @org_pk)
SET @location_pk = @@identity

INSERT INTO Org_Address 
(addr1,            addr2, org_addr_type, city            , zipcode, lst_mod_dt, addr_bad_fg, created_dt, created_user_pk, lst_mod_user_pk, zip_plus, country, county, province, state,    addr_bad_dt, attention_line, org_locations_pk)
VALUES
('1209 PIERCE ST', NULL , 72001        , 'SIOUX CITY'    , '51105', getdate() , 0          , getdate() , 10000001       , 10000001       , null,     null,    null,   null    , 'IA',    null       , null, @location_pk)
SET @org_addr_pk = @@identity

INSERT INTO Org_Phone 
(area_code, phone_no, org_phone_type, lst_mod_dt, country_cd, phone_bad_fg, created_dt, created_user_pk, phone_bad_dt, lst_mod_user_pk, phone_extension, org_locations_pk)
VALUES
(712      , 5065770 , 73001         , getdate() , 1         , 0           , getdate() , 10000001      , null        , 10000001       , null           , @location_pk)


--
-- WORD VIEW PUBLISHERS
--
INSERT INTO org_parent values(24002)
SET @org_pk = @@identity

INSERT INTO External_Organizations
(org_pk,   org_nm,                    org_web_site,     lst_mod_dt, lst_mod_user_pk, created_user_pk, created_dt, ext_org_type, org_email_domain, marked_for_deletion_fg)
VALUES
(@org_pk, 'WORD VIEW PUBLISHERS',    NULL,             getdate() , 10000001       , 10000001       , getdate() , 28003       , NULL            , 0                     )

INSERT INTO Org_Locations
(location_nm   , location_primary_fg, lst_mod_user_pk, created_dt, created_user_pk, lst_mod_dt, org_pk)
VALUES
('Headquarters', 1                  , 10000001       ,  getdate(), 10000001       , getdate() , @org_pk)
SET @location_pk = @@identity

INSERT INTO Org_Address 
(addr1,                    addr2, org_addr_type, city      , zipcode, lst_mod_dt, addr_bad_fg, created_dt, created_user_pk, lst_mod_user_pk, zip_plus, country, county, province, state,    addr_bad_dt, attention_line, org_locations_pk)
VALUES
('146 WEST 25 ST 3RD FL', NULL , 72001        , 'NEW YORK' ,'10001', getdate() , 0          , getdate() , 10000001       , 10000001       , null,     null,    null,   null    , 'NY',    null       , null, @location_pk)
SET @org_addr_pk = @@identity

INSERT INTO Org_Phone 
(area_code, phone_no, org_phone_type, lst_mod_dt, country_cd, phone_bad_fg, created_dt, created_user_pk, phone_bad_dt, lst_mod_user_pk, phone_extension, org_locations_pk)
VALUES
(212      , 5065770 , 73001        , getdate() , 1         , 0           , getdate() , 10000001      , null        , 10000001       , null           , @location_pk)

