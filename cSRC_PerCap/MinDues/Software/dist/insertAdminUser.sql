---
--- Generated file.  Do not edit.
---
	DECLARE @person_pk int, @org_pk int, @org_addr_pk int, @location_pk int, @addr_pk int
--
-- Adding User admin
--
	INSERT INTO Person
	(prefix_nm, mbr_expelled_dt, first_nm, last_nm, mbr_barred_fg, middle_nm, suffix_nm, nick_nm, lst_mod_dt, alternate_mailing_nm, ssn , created_dt, created_user_pk, valid_ssn_fg, duplicate_ssn_fg, lst_mod_user_pk, marked_for_deletion_fg, member_fg) 
	VALUES
	(null     , null           , 'admin', 'admin', 0            , '', NULL     , NULL   , getdate() , null                , 112223307, getdate() , 10000001       , 1           , 0               , 10000001       , 0                    , 1)
	SET @person_pk = @@identity
	INSERT INTO Person_Email
	(person_pk  ,  person_email_addr, lst_mod_dt  , email_type,  created_dt, created_user_pk, lst_mod_user_pk)
	VALUES
	(@person_pk, 'kvogel@grci.com'          , getdate()   , 71001     ,  getdate() , 10000001       , 10000001) 
	INSERT INTO Person_Email
	(person_pk  ,  person_email_addr, lst_mod_dt  , email_type,  created_dt, created_user_pk, lst_mod_user_pk)
	VALUES
	(@person_pk, ''          , getdate()   , 71002     ,  getdate() , 10000001       , 10000001) 
	INSERT INTO Person_Political_Legislative
	(person_pk  , political_objector_fg, political_do_not_call_fg)
	VALUES
	(@person_pk, 1                    , 0                       )
	INSERT INTO Person_Demographics
	(person_pk,   dob, deceased_fg, deceased_dt, gender , ethnic_origin, religion, marital_status, region_fk, lst_mod_user_pk, lst_mod_dt, created_dt, created_user_pk, scholarship_fg, citizenship)
	VALUES
	(@person_pk, '1962-03-25', 0        , NULL       , 33001, 46006, 62008, 49002, 61004, 10000001, getdate(), getdate(), 10000001, 0, 19165)
	INSERT INTO Person_Address 
    (addr1    , addr2, city   , state   , zipcode, zip_plus, carrier_route_info, country, county, eff_dt, end_dt, lst_mod_dt, addr_prmry_fg, addr_private_fg, addr_bad_fg, created_user_pk, created_dt, addr_source, addr_type, addr_marked_bad_dt, province, person_pk, dept, lst_mod_user_pk)
	VALUES
	('174 Pine Lane', NULL , 'Bend', 'OR', 97701   , NULL    , NULL              , 9001   , NULL  , NULL  , NULL  , getdate(), 1      , 0              , 0           , 10000001      , getdate(), 'A'        , 12001    , null              , null    , @person_pk, 4001, 10000001)
	SET @addr_pk = @@identity

	INSERT INTO Person_SMA
	(person_pk  , address_pk, sequence, current_fg, determined_dt)
	VALUES
	(@person_pk, @addr_pk, 1       , 1         , getdate())
	INSERT INTO Person_Address 
    (addr1    , addr2, city   , state   , zipcode, zip_plus, carrier_route_info, country, county, eff_dt, end_dt, lst_mod_dt, addr_prmry_fg, addr_private_fg, addr_bad_fg, created_user_pk, created_dt, addr_source, addr_type, addr_marked_bad_dt, province, person_pk, dept, lst_mod_user_pk)
	VALUES
	('303 Apple Street', NULL , 'Bend', 'OR', 97701   , NULL    , NULL              , 9001   , NULL  , NULL  , NULL  , getdate(), 0      , 0              , 0           , 10000001      , getdate(), 'A'        , 12002    , null              , null    , @person_pk, 4001, 10000001)
	SET @addr_pk = @@identity

	INSERT INTO Users
	(person_pk, dept, start_page, user_id, pwd, challenge_question, bad_login_attempt_ct)
	VALUES (@person_pk, 4001, 'A', 'admin', '5f4dcc3b5aa765d61d8327deb882cf99', 1001, 0)
	INSERT INTO User_Roles (person_pk, role_pk) SELECT @person_pk, role_pk FROM Roles WHERE role_name = 'Super User'
	INSERT INTO User_Affiliates (person_pk, aff_pk) SELECT @person_pk, aff_pk FROM Aff_Organizations
--
-- Adding User migration
--
	INSERT INTO Person
	(prefix_nm, mbr_expelled_dt, first_nm, last_nm, mbr_barred_fg, middle_nm, suffix_nm, nick_nm, lst_mod_dt, alternate_mailing_nm, ssn , created_dt, created_user_pk, valid_ssn_fg, duplicate_ssn_fg, lst_mod_user_pk, marked_for_deletion_fg, member_fg) 
	VALUES
	(null     , null           , 'migration', 'migration', 0            , '', NULL     , NULL   , getdate() , null                , 435058593, getdate() , 10000001       , 1           , 0               , 10000001       , 0                    , 1)
	SET @person_pk = @@identity
	INSERT INTO Person_Email
	(person_pk  ,  person_email_addr, lst_mod_dt  , email_type,  created_dt, created_user_pk, lst_mod_user_pk)
	VALUES
	(@person_pk, 'kvogel@grci.com'          , getdate()   , 71001     ,  getdate() , 10000001       , 10000001) 
	INSERT INTO Person_Email
	(person_pk  ,  person_email_addr, lst_mod_dt  , email_type,  created_dt, created_user_pk, lst_mod_user_pk)
	VALUES
	(@person_pk, ''          , getdate()   , 71002     ,  getdate() , 10000001       , 10000001) 
	INSERT INTO Person_Political_Legislative
	(person_pk  , political_objector_fg, political_do_not_call_fg)
	VALUES
	(@person_pk, 1                    , 0                       )
	INSERT INTO Person_Demographics
	(person_pk,   dob, deceased_fg, deceased_dt, gender , ethnic_origin, religion, marital_status, region_fk, lst_mod_user_pk, lst_mod_dt, created_dt, created_user_pk, scholarship_fg, citizenship)
	VALUES
	(@person_pk, '1983-02-30', 0        , NULL       , 33001, 46005, 62002, 49001, 61001, 10000001, getdate(), getdate(), 10000001, 0, 19076)
	INSERT INTO Person_Address 
    (addr1    , addr2, city   , state   , zipcode, zip_plus, carrier_route_info, country, county, eff_dt, end_dt, lst_mod_dt, addr_prmry_fg, addr_private_fg, addr_bad_fg, created_user_pk, created_dt, addr_source, addr_type, addr_marked_bad_dt, province, person_pk, dept, lst_mod_user_pk)
	VALUES
	('571 Peach Road', NULL , 'Bend', 'OR', 97701   , NULL    , NULL              , 9001   , NULL  , NULL  , NULL  , getdate(), 1      , 0              , 0           , 10000001      , getdate(), 'A'        , 12001    , null              , null    , @person_pk, 4001, 10000001)
	SET @addr_pk = @@identity

	INSERT INTO Person_SMA
	(person_pk  , address_pk, sequence, current_fg, determined_dt)
	VALUES
	(@person_pk, @addr_pk, 1       , 1         , getdate())
	INSERT INTO Person_Address 
    (addr1    , addr2, city   , state   , zipcode, zip_plus, carrier_route_info, country, county, eff_dt, end_dt, lst_mod_dt, addr_prmry_fg, addr_private_fg, addr_bad_fg, created_user_pk, created_dt, addr_source, addr_type, addr_marked_bad_dt, province, person_pk, dept, lst_mod_user_pk)
	VALUES
	('663 Lemon Road', NULL , 'Vienna', 'VA', 22182   , NULL    , NULL              , 9001   , NULL  , NULL  , NULL  , getdate(), 0      , 0              , 0           , 10000001      , getdate(), 'A'        , 12002    , null              , null    , @person_pk, 4001, 10000001)
	SET @addr_pk = @@identity

	INSERT INTO Users
	(person_pk, dept, start_page, user_id, pwd, challenge_question, bad_login_attempt_ct)
	VALUES (@person_pk, 4001, 'A', 'migration', '5f4dcc3b5aa765d61d8327deb882cf99', 1001, 0)
	INSERT INTO User_Roles (person_pk, role_pk) SELECT @person_pk, role_pk FROM Roles WHERE role_name = 'Super User'
	INSERT INTO User_Affiliates (person_pk, aff_pk) SELECT @person_pk, aff_pk FROM Aff_Organizations
