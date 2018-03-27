---
--- Generated file.  Do not edit.
---
declare @parent_pk int
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person', 'I', 'Member ID', 'person_pk', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person', 'S', 'Full Name', 'full_name', 'Detail', 2.0)
SET @parent_pk = @@identity
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	('Prefix', 'P', 'V_Person', 'C', 'Prefix Name', 'prefix_nm', 'Detail', 1.0)
INSERT INTO [Report_Field_Aggregate] 
	(child_field_pk, parent_field_pk) VALUES(@@identity, @parent_pk)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person', 'S', 'First Name', 'first_nm', 'Detail', 1.0)
INSERT INTO [Report_Field_Aggregate] 
	(child_field_pk, parent_field_pk) VALUES(@@identity, @parent_pk)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person', 'S', 'Middle Name', 'middle_nm', 'Detail', 1.0)
INSERT INTO [Report_Field_Aggregate] 
	(child_field_pk, parent_field_pk) VALUES(@@identity, @parent_pk)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person', 'S', 'Last Name', 'last_nm', 'Detail', 1.0)
INSERT INTO [Report_Field_Aggregate] 
	(child_field_pk, parent_field_pk) VALUES(@@identity, @parent_pk)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	('Suffix', 'P', 'V_Person', 'C', 'Suffix Name', 'suffix_nm', 'Detail', 1.0)
INSERT INTO [Report_Field_Aggregate] 
	(child_field_pk, parent_field_pk) VALUES(@@identity, @parent_pk)
--end children
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person', 'S', 'Alternate Mailing Name', 'alternate_mailing_nm', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person', 'S', 'Nickname', 'nick_nm', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person', 'S', 'Social Security Number', 'ssn', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person', 'B', 'Duplicate SSN', 'duplicate_ssn_fg', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person', 'B', 'Barred', 'mbr_barred_fg', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person', 'D', 'Expelled Date', 'mbr_expelled_dt', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person', 'B', 'Marked For Deletion', 'marked_for_deletion_fg', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person', 'B', 'Political Objector', 'political_objector_fg', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person', 'S', 'Primary Email', 'primary_email', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person', 'S', 'Primary Email Created User ID', 'pemail_created_user_id', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person', 'D', 'Primary Email Created Date', 'pemail_created_dt', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person', 'S', 'Primary Email Updated User ID', 'pemail_lst_mod_user_id', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person', 'D', 'Primary Email Updated Date', 'pemail_lst_mod_dt', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person', 'S', 'Alternate Email', 'alternate_email', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person', 'S', 'Alternate Email Created User ID', 'aemail_created_user_id', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person', 'D', 'Alternate Email Created Date', 'aemail_created_dt', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person', 'S', 'Alternate Email Updated User ID', 'aemail_lst_mod_user_id', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person', 'D', 'Alternate Email Updated Date', 'aemail_lst_mod_dt', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person', 'S', 'Created User ID', 'created_user_id', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person', 'D', 'Created Date', 'created_dt', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person', 'S', 'Last Updated User ID', 'lst_mod_user_id', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person', 'D', 'Last Updated Date', 'lst_mod_dt', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person', 'D', 'Date of Birth', 'dob', 'Demographics', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	('Gender', 'P', 'V_Person', 'C', 'Gender', 'gender', 'Demographics', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	('EthnicOrigin', 'P', 'V_Person', 'C', 'Ethnic Origin', 'ethnic_origin', 'Demographics', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	('Religion', 'P', 'V_Person', 'C', 'Religion', 'religion', 'Demographics', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	('MaritalStatus', 'P', 'V_Person', 'C', 'Marital Status', 'marital_status', 'Demographics', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	('Region', 'P', 'V_Person', 'C', 'Region', 'region', 'Demographics', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	('CountryCitizenship', 'P', 'V_Person', 'C', 'Citizenship', 'citizenship', 'Demographics', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person', 'B', 'Deceased', 'deceased_fg', 'Demographics', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person', 'D', 'Deceased Date', 'deceased_dt', 'Demographics', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	('Disabilities', 'P', 'V_Person', 'C', 'Disabilities', 'disability', 'Demographics', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	('Accomodations', 'P', 'V_Person', 'C', 'Accommodations', 'disability_accmdtn', 'Demographics', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	('Languages', 'P', 'V_Person', 'C', 'Primary Language', 'primary_language', 'Demographics', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	('Languages', 'P', 'V_Person', 'C', 'Other Language', 'other_languages', 'Demographics', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person', 'S', 'Created User ID', 'demo_created_user_id', 'Demographics', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person', 'D', 'Created Date', 'demo_created_dt', 'Demographics', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person', 'S', 'Last Updated User ID', 'demo_lst_mod_user_id', 'Demographics', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person', 'D', 'Last Updated Date', 'demo_lst_mod_dt', 'Demographics', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person_Address', 'S', 'Full Address', 'full_address', 'Address', 1.0)
SET @parent_pk = @@identity
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person_Address', 'I', 'ID', 'address_id', 'Address', 1.0)
INSERT INTO [Report_Field_Aggregate] 
	(child_field_pk, parent_field_pk) VALUES(@@identity, @parent_pk)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person_Address', 'S', 'Address 1', 'addr1', 'Address', 1.0)
INSERT INTO [Report_Field_Aggregate] 
	(child_field_pk, parent_field_pk) VALUES(@@identity, @parent_pk)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person_Address', 'S', 'Address 2', 'addr2', 'Address', 1.0)
INSERT INTO [Report_Field_Aggregate] 
	(child_field_pk, parent_field_pk) VALUES(@@identity, @parent_pk)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person_Address', 'S', 'City', 'city', 'Address', 1.0)
INSERT INTO [Report_Field_Aggregate] 
	(child_field_pk, parent_field_pk) VALUES(@@identity, @parent_pk)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	('State', 'P', 'V_Person_Address', 'C', 'State', 'state', 'Address', 1.0)
INSERT INTO [Report_Field_Aggregate] 
	(child_field_pk, parent_field_pk) VALUES(@@identity, @parent_pk)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person_Address', 'S', 'Zip Code', 'zipcode', 'Address', 1.0)
INSERT INTO [Report_Field_Aggregate] 
	(child_field_pk, parent_field_pk) VALUES(@@identity, @parent_pk)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person_Address', 'S', 'Zip Plus', 'zip_plus', 'Address', 1.0)
INSERT INTO [Report_Field_Aggregate] 
	(child_field_pk, parent_field_pk) VALUES(@@identity, @parent_pk)
--end children
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person_Address', 'S', 'Carrier Route', 'carrier_route_info', 'Address', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person_Address', 'S', 'State Code', 'state_cd', 'Address', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person_Address', 'S', 'State Description', 'state_desc', 'Address', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person_Address', 'S', 'Province', 'province', 'Address', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	('Country', 'P', 'V_Person_Address', 'C', 'Country', 'country', 'Address', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person_Address', 'B', 'Primary', 'prmry_fg', 'Address', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person_Address', 'B', 'Bad', 'bad_fg', 'Address', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person_Address', 'D', 'Bad Date', 'bad_dt', 'Address', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	('PersonAddressType', 'P', 'V_Person_Address', 'C', 'Code', 'addr_type', 'Address', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person_Address', 'B', 'Private', 'private_fg', 'Address', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person_Address', 'S', 'Created User ID', 'addr_created_user_id', 'Address', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person_Address', 'D', 'Created Date', 'addr_created_dt', 'Address', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person_Address', 'S', 'Last Updated User ID', 'addr_lst_mod_user_id', 'Address', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person_Address', 'D', 'Last Updated Date', 'addr_lst_mod_dt', 'Address', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	('PhoneType', 'P', 'V_Person_Phone', 'C', 'Type', 'phone_type', 'Phone', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person_Phone', 'S', 'Number', 'full_Phone', 'Phone', 1.0)
SET @parent_pk = @@identity
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person_Phone', 'I', 'Country Code', 'country_cd', 'Phone', 1.0)
INSERT INTO [Report_Field_Aggregate] 
	(child_field_pk, parent_field_pk) VALUES(@@identity, @parent_pk)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person_Phone', 'S', 'Area Code', 'area_code', 'Phone', 1.0)
INSERT INTO [Report_Field_Aggregate] 
	(child_field_pk, parent_field_pk) VALUES(@@identity, @parent_pk)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person_Phone', 'S', 'Number', 'phone_no', 'Phone', 1.0)
INSERT INTO [Report_Field_Aggregate] 
	(child_field_pk, parent_field_pk) VALUES(@@identity, @parent_pk)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person_Phone', 'S', 'Extension', 'phone_extension', 'Phone', 1.0)
INSERT INTO [Report_Field_Aggregate] 
	(child_field_pk, parent_field_pk) VALUES(@@identity, @parent_pk)
--end children
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person_Phone', 'B', 'Primary', 'prmry_fg', 'Phone', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person_Phone', 'D', 'Bad Date', 'bad_dt', 'Phone', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person_Phone', 'S', 'Created User ID', 'phone_created_user_id', 'Phone', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person_Phone', 'D', 'Created Date', 'phone_created_dt', 'Phone', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person_Phone', 'S', 'Last Updated User ID', 'phone_lst_mod_user_id', 'Phone', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person_Phone', 'D', 'Last Updated Date', 'phone_lst_mod_dt', 'Phone', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	('Relation', 'P', 'V_Person_Relation', 'C', 'Type', 'relative_type', 'Relation', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person_Relation', 'S', 'First Name', 'relative_first_nm', 'Relation', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person_Relation', 'S', 'Middle Name', 'relative_middle_nm', 'Relation', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person_Relation', 'S', 'Last Name', 'relative_last_nm', 'Relation', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	('Suffix', 'P', 'V_Person_Relation', 'C', 'Suffix Name', 'relative_suffix_nm', 'Relation', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person_Relation', 'D', 'Birth Date', 'relative_birth_dt', 'Relation', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	('MemberType', 'M', 'V_Member', 'C', 'Type', 'mbr_type', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	('MemberStatus', 'M', 'V_Member', 'C', 'Status', 'mbr_status', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'M', 'V_Member', 'B', 'No Mail', 'no_mail_fg', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'M', 'V_Member', 'D', 'Join Date', 'mbr_join_dt', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'M', 'V_Member', 'D', 'Retired Date', 'mbr_retired_dt', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'M', 'V_Member', 'D', 'Card Sent Date', 'mbr_card_sent_dt', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'M', 'V_Member', 'B', 'Lost Time Language', 'lost_time_language_fg', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	('DuesType', 'M', 'V_Member', 'C', 'Dues Type', 'dues_type', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'M', 'V_Member', 'I', 'Dues Rate', 'dues_rate', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	('DuesFrequency', 'M', 'V_Member', 'C', 'Dues Frequency', 'dues_frequency', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'M', 'V_Member', 'B', 'Retiree Annual Dues Renewal', 'ret_dues_renewal_fg', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'M', 'V_Member', 'S', 'Created User ID', 'created_user_id', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'M', 'V_Member', 'D', 'Created Date', 'created_dt', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'M', 'V_Member', 'S', 'Last Updated User ID', 'lst_mod_user_id', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'M', 'V_Member', 'D', 'Last Updated Date', 'lst_mod_dt', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'M', 'V_Member', 'S', 'Employer Name', 'employer_name', 'Employer', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	('EmployeeJobTitle', 'M', 'V_Member', 'C', 'Job Title', 'emp_job_title', 'Employer', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	('EmployeeSector', 'M', 'V_Member', 'C', 'Sector', 'emp_sector', 'Employer', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	('EmployeeSalaryRange', 'M', 'V_Member', 'C', 'Salary Range', 'emp_salary_range', 'Employer', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'M', 'V_Member', 'I', 'Salary', 'employee_salary', 'Employer', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'M', 'V_Member', 'S', 'Job Site', 'emp_job_site', 'Employer', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	('AFSCMETitle', 'O', 'V_Officer', 'C', 'AFSCME Title', 'title', 'Detail', 2.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'O', 'V_Officer', 'D', 'Position Start Date', 'pos_start_dt', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'O', 'V_Officer', 'D', 'Position End Date', 'pos_end_dt', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'O', 'V_Officer', 'D', 'Position Expiration Date', 'pos_expiration_dt', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'O', 'V_Officer', 'B', 'Steward', 'pos_steward_fg', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'O', 'V_Officer', 'B', 'Suspended', 'suspended_fg', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'O', 'V_Officer', 'S', 'Created User ID', 'created_user_id', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'O', 'V_Officer', 'D', 'Created Date', 'created_dt', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'O', 'V_Officer', 'S', 'Last Updated User ID', 'lst_mod_user_id', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'O', 'V_Officer', 'D', 'Last Updated Date', 'lst_mod_dt', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'O', 'V_Officer', 'S', 'Full Address', 'full_address', 'Address', 1.0)
SET @parent_pk = @@identity
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'O', 'V_Officer', 'I', 'ID', 'address_id', 'Address', 1.0)
INSERT INTO [Report_Field_Aggregate] 
	(child_field_pk, parent_field_pk) VALUES(@@identity, @parent_pk)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'O', 'V_Officer', 'S', 'Address1', 'addr1', 'Address', 1.0)
INSERT INTO [Report_Field_Aggregate] 
	(child_field_pk, parent_field_pk) VALUES(@@identity, @parent_pk)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'O', 'V_Officer', 'S', 'Address2', 'addr2', 'Address', 1.0)
INSERT INTO [Report_Field_Aggregate] 
	(child_field_pk, parent_field_pk) VALUES(@@identity, @parent_pk)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'O', 'V_Officer', 'S', 'City', 'city', 'Address', 1.0)
INSERT INTO [Report_Field_Aggregate] 
	(child_field_pk, parent_field_pk) VALUES(@@identity, @parent_pk)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	('State', 'O', 'V_Officer', 'C', 'State', 'state', 'Address', 1.0)
INSERT INTO [Report_Field_Aggregate] 
	(child_field_pk, parent_field_pk) VALUES(@@identity, @parent_pk)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'O', 'V_Officer', 'S', 'Zip Code', 'zipcode', 'Address', 1.0)
INSERT INTO [Report_Field_Aggregate] 
	(child_field_pk, parent_field_pk) VALUES(@@identity, @parent_pk)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'O', 'V_Officer', 'S', 'Zip Plus', 'zip_plus', 'Address', 1.0)
INSERT INTO [Report_Field_Aggregate] 
	(child_field_pk, parent_field_pk) VALUES(@@identity, @parent_pk)
--end children
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'O', 'V_Officer', 'S', 'State Code', 'state_cd', 'Address', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'O', 'V_Officer', 'S', 'Province', 'province', 'Address', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	('Country', 'O', 'V_Officer', 'C', 'Country', 'country', 'Address', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'O', 'V_Officer', 'S', 'Created User ID', 'addr_created_user_id', 'Address', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'O', 'V_Officer', 'D', 'Created Date', 'addr_created_dt', 'Address', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'O', 'V_Officer', 'S', 'Last Updated User ID', 'addr_lst_mod_user_id', 'Address', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'O', 'V_Officer', 'D', 'Last Updated Date', 'addr_lst_mod_dt', 'Address', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate', 'S', 'Affiliate Name', 'aff_abbreviated_nm', 'Detail', 2.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate', 'S', 'Affiliate Identifier', 'affiliate_id', 'Detail', 1.0)
SET @parent_pk = @@identity
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate', 'S', 'Local / Sub Chapter', 'aff_localSubChapter', 'Detail', 1.0)
INSERT INTO [Report_Field_Aggregate] 
	(child_field_pk, parent_field_pk) VALUES(@@identity, @parent_pk)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	('AffiliateState', 'A', 'V_Affiliate', 'C', 'State / Nat Type', 'aff_stateNat_type', 'Detail', 1.0)
INSERT INTO [Report_Field_Aggregate] 
	(child_field_pk, parent_field_pk) VALUES(@@identity, @parent_pk)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate', 'S', 'Sub Unit', 'aff_subUnit', 'Detail', 1.0)
INSERT INTO [Report_Field_Aggregate] 
	(child_field_pk, parent_field_pk) VALUES(@@identity, @parent_pk)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate', 'S', 'Council / Retiree Chapter', 'aff_councilRetiree_chap', 'Detail', 1.0)
INSERT INTO [Report_Field_Aggregate] 
	(child_field_pk, parent_field_pk) VALUES(@@identity, @parent_pk)
--end children
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	('AffiliateStatus', 'A', 'V_Affiliate', 'C', 'Status', 'aff_status', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate', 'S', 'Type', 'aff_type', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate', 'S', 'State / Nat Type Code', 'aff_stateNat_cd', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	('AffiliateCardRunType', 'A', 'V_Affiliate', 'C', 'Annual Card Run Type', 'ann_card_run_type', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	('MemberRenewal', 'A', 'V_Affiliate', 'C', 'Member Renewal', 'mbr_renewal', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate', 'B', 'Unit Wide No Member Cards', 'unit_wide_no_mbr_cards_fg', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate', 'B', 'Unit Wide No PE Mail', 'unit_wide_no_pe_mail_fg', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate', 'S', 'Created User ID', 'created_user_id', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate', 'D', 'Created Date', 'created_dt', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate', 'S', 'Last Updated User ID', 'lst_mod_user_id', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate', 'D', 'Last Updated Date', 'lst_mod_dt', 'Detail', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate', 'B', 'Approved Constitution', 'approved_const_fg', 'Constitution', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate', 'B', 'Automatic Delegate Provision', 'auto_delegate_prvsn_fg', 'Constitution', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate', 'D', 'Most Current Approval Date', 'most_current_approval_dt', 'Constitution', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate', 'D', 'Affiliation Agreement Date', 'aff_agreement_dt', 'Constitution', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	('MethodOffcrElection', 'A', 'V_Affiliate', 'C', 'Method of Officer Election', 'off_election_method', 'Constitution', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate', 'B', 'Constitutional Regions', 'const_regions_fg', 'Constitution', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	('MeetingFrequency', 'A', 'V_Affiliate', 'C', 'Meeting Frequency', 'meeting_frequency', 'Constitution', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate_Address', 'S', 'Full Address', 'full_address', 'Primary Location Address', 1.0)
SET @parent_pk = @@identity
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate_Address', 'I', 'ID', 'address_id', 'Primary Location Address', 1.0)
INSERT INTO [Report_Field_Aggregate] 
	(child_field_pk, parent_field_pk) VALUES(@@identity, @parent_pk)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate_Address', 'S', 'Address1', 'addr1', 'Primary Location Address', 1.0)
INSERT INTO [Report_Field_Aggregate] 
	(child_field_pk, parent_field_pk) VALUES(@@identity, @parent_pk)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate_Address', 'S', 'Address2', 'addr2', 'Primary Location Address', 1.0)
INSERT INTO [Report_Field_Aggregate] 
	(child_field_pk, parent_field_pk) VALUES(@@identity, @parent_pk)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate_Address', 'S', 'City', 'city', 'Primary Location Address', 1.0)
INSERT INTO [Report_Field_Aggregate] 
	(child_field_pk, parent_field_pk) VALUES(@@identity, @parent_pk)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	('State', 'A', 'V_Affiliate_Address', 'C', 'State', 'state', 'Primary Location Address', 1.0)
INSERT INTO [Report_Field_Aggregate] 
	(child_field_pk, parent_field_pk) VALUES(@@identity, @parent_pk)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate_Address', 'S', 'Zip Code', 'zipcode', 'Primary Location Address', 1.0)
INSERT INTO [Report_Field_Aggregate] 
	(child_field_pk, parent_field_pk) VALUES(@@identity, @parent_pk)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate_Address', 'S', 'Zip Plus', 'zip_plus', 'Primary Location Address', 1.0)
INSERT INTO [Report_Field_Aggregate] 
	(child_field_pk, parent_field_pk) VALUES(@@identity, @parent_pk)
--end children
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	('OrgAddressType', 'A', 'V_Affiliate_Address', 'C', 'Type', 'addr_type', 'Primary Location Address', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate_Address', 'S', 'State Code', 'state_cd', 'Primary Location Address', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate_Address', 'S', 'Province', 'province', 'Primary Location Address', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	('Country', 'A', 'V_Affiliate_Address', 'C', 'Country', 'country', 'Primary Location Address', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate_Address', 'S', 'Attention Line', 'attention_line', 'Primary Location Address', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate_Address', 'B', 'Bad', 'bad_fg', 'Primary Location Address', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate_Address', 'D', 'Bad Date', 'bad_dt', 'Primary Location Address', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate_Address', 'S', 'Created User ID', 'addr_created_user_id', 'Primary Location Address', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate_Address', 'D', 'Created Date', 'addr_created_dt', 'Primary Location Address', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate_Address', 'S', 'Last Updated User ID', 'addr_lst_mod_user_id', 'Primary Location Address', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate_Address', 'D', 'Last Updated Date', 'addr_lst_mod_dt', 'Primary Location Address', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	('OrgPhoneType', 'A', 'V_Affiliate_Phone', 'C', 'Type', 'phone_type', 'Primary Location Phone', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate_Phone', 'S', 'Number', 'full_phone', 'Primary Location Phone', 1.0)
SET @parent_pk = @@identity
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate_Phone', 'I', 'Country Code', 'country_cd', 'Primary Location Phone', 1.0)
INSERT INTO [Report_Field_Aggregate] 
	(child_field_pk, parent_field_pk) VALUES(@@identity, @parent_pk)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate_Phone', 'S', 'Area Code', 'area_code', 'Primary Location Phone', 1.0)
INSERT INTO [Report_Field_Aggregate] 
	(child_field_pk, parent_field_pk) VALUES(@@identity, @parent_pk)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate_Phone', 'S', 'Number', 'phone_no', 'Primary Location Phone', 1.0)
INSERT INTO [Report_Field_Aggregate] 
	(child_field_pk, parent_field_pk) VALUES(@@identity, @parent_pk)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate_Phone', 'S', 'Extension', 'phone_extension', 'Primary Location Phone', 1.0)
INSERT INTO [Report_Field_Aggregate] 
	(child_field_pk, parent_field_pk) VALUES(@@identity, @parent_pk)
--end children
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate_Phone', 'B', 'Bad', 'phone_bad_fg', 'Primary Location Phone', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate_Phone', 'D', 'Bad Date', 'phone_bad_dt', 'Primary Location Phone', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate_Phone', 'S', 'Created User ID', 'phone_created_user_id', 'Primary Location Phone', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate_Phone', 'D', 'Created Date', 'phone_created_dt', 'Primary Location Phone', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate_Phone', 'S', 'Last Updated User ID', 'phone_lst_mod_user_id', 'Primary Location Phone', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate_Phone', 'D', 'Last Updated Date', 'phone_lst_mod_dt', 'Primary Location Phone', 1.0)
