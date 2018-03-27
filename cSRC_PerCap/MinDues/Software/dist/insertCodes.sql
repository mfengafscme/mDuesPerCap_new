---
--- Generated file.  Do not edit.
---
SET IDENTITY_INSERT Common_Codes ON
DECLARE @category_id int 
---
--- Update Codes
---
INSERT INTO Common_Code_Category
VALUES ('Update', 'Update Codes', 'A')
set @category_id = @@identity
---
--- Update Type
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Update Type',         'Update Type' , 'A',         @category_id, 'UpdateType'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(13001, 'F', 'Full', 'A', 'UpdateType', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(13002, 'P', 'Partial', 'A', 'UpdateType', '2')

---
--- Update File Queue
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Update File Queue',         'Upload File Queue' , 'A',         @category_id, 'UpdateFileQueue'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(14001, 'I', 'Initial', 'A', 'UpdateFileQueue', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(14002, 'G', 'Good', 'A', 'UpdateFileQueue', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(14003, 'B', 'Bad', 'A', 'UpdateFileQueue', '3')

---
--- Update File Status
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Update File Status',         'Status of an update file' , 'A',         @category_id, 'UpdateFileStatus'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(15001, '1', 'Review', 'A', 'UpdateFileStatus', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(15002, '2', 'Pending', 'A', 'UpdateFileStatus', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(15003, '3', 'Processed', 'A', 'UpdateFileStatus', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(15004, '4', 'Rejected', 'A', 'UpdateFileStatus', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(15005, '5', 'Uploaded', 'A', 'UpdateFileStatus', '5')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(15006, '6', 'Invalid', 'A', 'UpdateFileStatus', '6')

---
--- Update File Type
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Update File Type',         'Type of Update File' , 'A',         @category_id, 'UpdateFileType'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(16001, 'M', 'Member', 'A', 'UpdateFileType', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(16002, 'O', 'Officer', 'A', 'UpdateFileType', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(16003, 'R', 'Rebate', 'A', 'UpdateFileType', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(16004, 'P', 'Participation', 'A', 'UpdateFileType', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(16005, 'A', 'Affiliate        ', 'A', 'UpdateFileType', '5')

---
--- Member Update Fields
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Member Update Fields',         'The fields of member update' , 'A',         @category_id, 'MemberUpdateFields'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(74001, '1', 'Prefix', 'A', 'MemberUpdateFields', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(74002, '2', 'First Name', 'A', 'MemberUpdateFields', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(74003, '3', 'Middle Name', 'A', 'MemberUpdateFields', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(74004, '4', 'Last Name', 'A', 'MemberUpdateFields', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(74005, '5', 'Suffix', 'A', 'MemberUpdateFields', '5')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(74006, '6', 'Primary Address 1', 'A', 'MemberUpdateFields', '6')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(74007, '7', 'Primary Address 2', 'A', 'MemberUpdateFields', '7')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(74008, '8', 'City', 'A', 'MemberUpdateFields', '8')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(74009, '9', 'State', 'A', 'MemberUpdateFields', '9')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(74010, '10', 'Zip/Postal Code', 'A', 'MemberUpdateFields', '10')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(74011, '11', 'Bad Address Flag', 'A', 'MemberUpdateFields', '11')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(74012, '12', 'Mail Flag', 'A', 'MemberUpdateFields', '12')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(74013, '13', 'Home Telephone', 'A', 'MemberUpdateFields', '13')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(74014, '14', 'Member Status', 'A', 'MemberUpdateFields', '14')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(74015, '15', 'Gender', 'A', 'MemberUpdateFields', '15')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(74016, '16', 'Joined Date', 'A', 'MemberUpdateFields', '16')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(74017, '17', 'Registered Voter', 'A', 'MemberUpdateFields', '17')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(74018, '18', 'Political Party', 'A', 'MemberUpdateFields', '18')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(74019, '19', 'Social Security Number', 'A', 'MemberUpdateFields', '19')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(74020, '20', 'Primary Information Source', 'A', 'MemberUpdateFields', '20')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(74021, '21', 'Unique Affiliate Member ID', 'A', 'MemberUpdateFields', '21')

---
--- Update Field Error
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Update Field Error',         'Indicates Exception Type during Apply Update' , 'A',         @category_id, 'UpdateFieldError'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(79001, 'A', 'Add', 'A', 'UpdateFieldError', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(79002, 'C', 'Change', 'A', 'UpdateFieldError', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(79003, 'I', 'Deactivate', 'A', 'UpdateFieldError', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(79004, 'D', 'Duplicate', 'A', 'UpdateFieldError', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(79005, 'R', 'Recent', 'A', 'UpdateFieldError', '5')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(79006, 'U', 'Unknown', 'A', 'UpdateFieldError', '6')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(79007, 'Q', 'Required', 'A', 'UpdateFieldError', '7')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(79008, 'V', 'Invalid', 'A', 'UpdateFieldError', '8')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(79009, 'M', 'Remove', 'A', 'UpdateFieldError', '9')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(79010, 'N', 'Renew', 'A', 'UpdateFieldError', '10')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(79011, 'T', 'Vacate', 'A', 'UpdateFieldError', '11')

---
--- Rebate Update Fields
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Rebate Update Fields',         'The fields of rebate update' , 'A',         @category_id, 'RebateUpdateFields'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(81001, '1', 'AFSCME Member Number', 'A', 'RebateUpdateFields', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(81002, '2', 'Social Security Number', 'A', 'RebateUpdateFields', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(81003, '3', 'SSN Duplicate Flag', 'A', 'RebateUpdateFields', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(81004, '4', 'First Name', 'A', 'RebateUpdateFields', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(81005, '5', 'Middle Name', 'A', 'RebateUpdateFields', '5')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(81006, '6', 'Last Name', 'A', 'RebateUpdateFields', '6')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(81007, '7', 'Address 1', 'A', 'RebateUpdateFields', '7')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(81008, '8', 'Address 2', 'A', 'RebateUpdateFields', '8')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(81009, '9', 'City', 'A', 'RebateUpdateFields', '9')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(81010, '10', 'Province', 'A', 'RebateUpdateFields', '10')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(81011, '11', 'State', 'A', 'RebateUpdateFields', '11')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(81012, '12', 'Zip/Postal Code', 'A', 'RebateUpdateFields', '12')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(81013, '13', 'Zip Plus', 'A', 'RebateUpdateFields', '13')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(81014, '14', 'Country', 'A', 'RebateUpdateFields', '14')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(81015, '15', 'Member Type', 'A', 'RebateUpdateFields', '15')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(81016, '16', 'Member Status', 'A', 'RebateUpdateFields', '16')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(81017, '17', 'Dues Type', 'A', 'RebateUpdateFields', '17')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(81018, '18', 'Number of Months', 'A', 'RebateUpdateFields', '18')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(81019, '19', 'Acceptance Code', 'A', 'RebateUpdateFields', '19')

---
--- Officer Update Fields
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Officer Update Fields',         'The fields of officer update' , 'A',         @category_id, 'OfficerUpdateFields'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(82001, '1', 'AFSCME Member Number', 'A', 'OfficerUpdateFields', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(82002, '2', 'Social Security Number', 'A', 'OfficerUpdateFields', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(82003, '3', 'Term Expiration Date', 'A', 'OfficerUpdateFields', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(82004, '4', 'Officer Title', 'A', 'OfficerUpdateFields', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(82005, '5', 'Prefix', 'A', 'OfficerUpdateFields', '5')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(82006, '6', 'First Name', 'A', 'OfficerUpdateFields', '6')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(82007, '7', 'Middle Name', 'A', 'OfficerUpdateFields', '7')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(82008, '8', 'Last Name', 'A', 'OfficerUpdateFields', '8')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(82009, '9', 'Suffix', 'A', 'OfficerUpdateFields', '9')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(82010, '10', 'Address 1', 'A', 'OfficerUpdateFields', '10')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(82011, '11', 'Address 2', 'A', 'OfficerUpdateFields', '11')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(82012, '12', 'City', 'A', 'OfficerUpdateFields', '12')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(82013, '13', 'Province', 'A', 'OfficerUpdateFields', '13')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(82014, '14', 'State', 'A', 'OfficerUpdateFields', '14')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(82015, '15', 'Zip/Postal Code', 'A', 'OfficerUpdateFields', '15')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(82016, '16', 'Zip Plus', 'A', 'OfficerUpdateFields', '16')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(82017, '17', 'Country', 'A', 'OfficerUpdateFields', '17')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(82018, '18', 'Primary Phone Country Code', 'A', 'OfficerUpdateFields', '18')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(82019, '19', 'Primary Phone Area Code', 'A', 'OfficerUpdateFields', '19')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(82020, '20', 'Primary Phone Number', 'A', 'OfficerUpdateFields', '20')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(82021, '21', 'Affiliate Member Number', 'A', 'OfficerUpdateFields', '21')

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

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(82032, '32', 'Affiliate AffiliateType', 'A', 'OfficerUpdateFields', '32')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(82033, '33', 'Affiliate Local/Sub Chapter', 'A', 'OfficerUpdateFields', '33')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(82034, '34', 'Affiliate State/National Type', 'A', 'OfficerUpdateFields', '34')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(82035, '35', 'Affiliate Sub Unit', 'A', 'OfficerUpdateFields', '35')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(82036, '36', 'Affiliate Council/Retiree Chapter', 'A', 'OfficerUpdateFields', '36')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(82037, '37', 'Transaction Type', 'A', 'OfficerUpdateFields', '37')

---
--- System Codes
---
INSERT INTO Common_Code_Category
VALUES ('System', 'System Codes', 'A')
set @category_id = @@identity
---
--- Challenge Question
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Challenge Question',         'Security Question used to retrieve passwords' , 'A',         @category_id, 'ChallengeQuestion'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(1001, '1', 'What is your favorite pet''s name?', 'A', 'ChallengeQuestion', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(1002, '2', 'What is your Mother''s maiden name?', 'A', 'ChallengeQuestion', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(1003, '3', 'What town were you born in?', 'A', 'ChallengeQuestion', '3')

---
--- Phone Type
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Phone Type',         'Phone Type' , 'A',         @category_id, 'PhoneType'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(3001, '1', 'Home', 'A', 'PhoneType', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(3002, '2', 'Work', 'A', 'PhoneType', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(3003, '3', 'Cell', 'A', 'PhoneType', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(3004, '4', 'Fax', 'A', 'PhoneType', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(3005, '5', 'Other', 'A', 'PhoneType', '5')

---
--- AFSCME Departments
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('AFSCME Departments',         'AFSCME Departments' , 'A',         @category_id, 'Department'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(4001, 'MD', 'Membership/Affiliate Relations', 'A', 'Department', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(4002, 'FO', 'Field Office', 'A', 'Department', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(4003, 'HQ', 'Headquarters', 'A', 'Department', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(4004, 'PD', 'PEOPLE', 'A', 'Department', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(4005, 'IT', 'Information Technology', 'A', 'Department', '5')

---
--- Start Page
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Start Page',         'User''s preferred start page' , 'A',         @category_id, 'StartPage'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(7001, 'A', 'Afscme Main Menu', 'A', 'StartPage', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(7002, 'D', 'Affiliate Data Utility', 'A', 'StartPage', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(7003, 'M', 'View Personal Information', 'A', 'StartPage', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(7004, 'V', 'Vendor Member Search', 'A', 'StartPage', '4')

---
--- Country
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Country',         'Country' , 'A',         @category_id, 'Country'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9001, 'US', 'United States', 'A', 'Country', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9002, 'CA', 'Canada', 'A', 'Country', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9003, 'AD', 'Andorra', 'A', 'Country', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9004, 'AE', 'United Arab Emirates', 'A', 'Country', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9005, 'AF', 'Afghanistan', 'A', 'Country', '5')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9006, 'AG', 'Antigua & Barbuda Inc. Redonda', 'A', 'Country', '6')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9007, 'AI', 'Anguilla', 'A', 'Country', '7')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9008, 'AL', 'Albania', 'A', 'Country', '8')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9009, 'AM', 'Armenia', 'A', 'Country', '9')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9010, 'AN', 'Netherlands Antilles', 'A', 'Country', '10')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9011, 'AO', 'Angola', 'A', 'Country', '11')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9012, 'AR', 'Argentina', 'A', 'Country', '12')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9013, 'AT', 'Austria', 'A', 'Country', '13')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9014, 'AU', 'Australia', 'A', 'Country', '14')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9015, 'AW', 'Aruba', 'A', 'Country', '15')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9016, 'AZ', 'Azerbaijan', 'A', 'Country', '16')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9017, 'BA', 'Bosnia-Herzegovina', 'A', 'Country', '17')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9018, 'BB', 'Barbados', 'A', 'Country', '18')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9019, 'BD', 'Bangladesh', 'A', 'Country', '19')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9020, 'BE', 'Belgium', 'A', 'Country', '20')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9021, 'BF', 'Burkina Faso', 'A', 'Country', '21')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9022, 'BG', 'Bulgaria', 'A', 'Country', '22')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9023, 'BH', 'Bahrain', 'A', 'Country', '23')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9024, 'BI', 'Burundi', 'A', 'Country', '24')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9025, 'BJ', 'Benin', 'A', 'Country', '25')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9026, 'BM', 'Bermuda', 'A', 'Country', '26')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9027, 'BN', 'Brunei Darussalem', 'A', 'Country', '27')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9028, 'BO', 'Bolivia', 'A', 'Country', '28')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9029, 'BR', 'Brazil', 'A', 'Country', '29')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9030, 'BS', 'Bahamas', 'A', 'Country', '30')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9031, 'BT', 'Bhutan', 'A', 'Country', '31')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9032, 'BW', 'Botswana', 'A', 'Country', '32')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9033, 'BY', 'Belarus', 'A', 'Country', '33')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9034, 'BZ', 'Belize', 'A', 'Country', '34')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9035, 'CF', 'Central African Republic', 'A', 'Country', '35')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9036, 'CG', 'Republic of the Congo Brazzaville', 'A', 'Country', '36')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9037, 'CH', 'Switzerland', 'A', 'Country', '37')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9038, 'CI', 'Ivory Coast', 'A', 'Country', '38')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9039, 'CL', 'Chile', 'A', 'Country', '39')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9040, 'CM', 'Cameroon', 'A', 'Country', '40')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9041, 'CN', 'China', 'A', 'Country', '41')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9042, 'CO', 'Colombia', 'A', 'Country', '42')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9043, 'CR', 'Costa Rica', 'A', 'Country', '43')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9044, 'CU', 'Cuba', 'A', 'Country', '44')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9045, 'CV', 'Cape Verde', 'A', 'Country', '45')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9046, 'CY', 'Cyprus', 'A', 'Country', '46')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9047, 'CZ', 'Czech Republic', 'A', 'Country', '47')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9048, 'DE', 'Germany', 'A', 'Country', '48')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9049, 'DJ', 'Djibouti', 'A', 'Country', '49')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9050, 'DK', 'Denmark', 'A', 'Country', '50')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9051, 'DM', 'Dominica', 'A', 'Country', '51')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9052, 'DO', 'Dominican Republic', 'A', 'Country', '52')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9053, 'DZ', 'Algeria', 'A', 'Country', '53')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9054, 'EC', 'Ecuador', 'A', 'Country', '54')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9055, 'EE', 'Estonia', 'A', 'Country', '55')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9056, 'EG', 'Egypt', 'A', 'Country', '56')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9057, 'ER', 'Eritrea', 'A', 'Country', '57')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9058, 'ES', 'Spain', 'A', 'Country', '58')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9059, 'ET', 'Ethiopia', 'A', 'Country', '59')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9060, 'FI', 'Finland', 'A', 'Country', '60')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9061, 'FJ', 'Fiji', 'A', 'Country', '61')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9062, 'FK', 'Falkland Islands', 'A', 'Country', '62')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9063, 'FR', 'France', 'A', 'Country', '63')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9064, 'GA', 'Gabon', 'A', 'Country', '64')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9065, 'GB', 'Great Britain & Northern Ireland', 'A', 'Country', '65')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9066, 'GD', 'Grenada', 'A', 'Country', '66')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9067, 'GE', 'Republic of Georgia', 'A', 'Country', '67')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9068, 'GF', 'French Guiana', 'A', 'Country', '68')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9069, 'GH', 'Ghana', 'A', 'Country', '69')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9070, 'GI', 'Gibraltar', 'A', 'Country', '70')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9071, 'GL', 'Greenland', 'A', 'Country', '71')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9072, 'GM', 'Gambia', 'A', 'Country', '72')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9073, 'GN', 'Guinea', 'A', 'Country', '73')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9074, 'GP', 'Guadeloupe', 'A', 'Country', '74')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9075, 'GQ', 'Equatorial Guinea', 'A', 'Country', '75')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9076, 'GR', 'Greece', 'A', 'Country', '76')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9077, 'GT', 'Guatemala', 'A', 'Country', '77')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9078, 'GW', 'Guinea Bissau', 'A', 'Country', '78')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9079, 'GY', 'Guyana', 'A', 'Country', '79')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9080, 'HK', 'Hong Kong', 'A', 'Country', '80')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9081, 'HN', 'Honduras', 'A', 'Country', '81')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9082, 'HR', 'Croatia', 'A', 'Country', '82')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9083, 'HT', 'Haiti', 'A', 'Country', '83')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9084, 'HU', 'Hungary', 'A', 'Country', '84')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9085, 'ID', 'Indonesia', 'A', 'Country', '85')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9086, 'IE', 'Ireland', 'A', 'Country', '86')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9087, 'IL', 'Israel', 'A', 'Country', '87')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9088, 'IN', 'India', 'A', 'Country', '88')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9089, 'IQ', 'Iraq', 'A', 'Country', '89')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9090, 'IR', 'Iran', 'A', 'Country', '90')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9091, 'IS', 'Iceland', 'A', 'Country', '91')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9092, 'IT', 'Italy', 'A', 'Country', '92')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9093, 'JM', 'Jamaica', 'A', 'Country', '93')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9094, 'JO', 'Jordan', 'A', 'Country', '94')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9095, 'JP', 'Japan', 'A', 'Country', '95')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9096, 'KE', 'Kenya', 'A', 'Country', '96')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9097, 'KG', 'Kyrgyzstan', 'A', 'Country', '97')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9098, 'KH', 'Cambodia', 'A', 'Country', '98')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9099, 'KI', 'Kiribati', 'A', 'Country', '99')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9100, 'KM', 'Comoros', 'A', 'Country', '100')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9101, 'KN', 'Saint Christopher St.Kitts & Nevis', 'A', 'Country', '101')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9102, 'KP', 'Dem. Peoples Rep. Korea North', 'A', 'Country', '102')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9103, 'KR', 'Republic of Korea South', 'A', 'Country', '103')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9104, 'KW', 'Kuwait', 'A', 'Country', '104')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9105, 'KY', 'Cayman Islands', 'A', 'Country', '105')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9106, 'KZ', 'Kazakhstan', 'A', 'Country', '106')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9107, 'LA', 'Laos', 'A', 'Country', '107')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9108, 'LB', 'Lebanon', 'A', 'Country', '108')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9109, 'LC', 'Saint Lucia', 'A', 'Country', '109')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9110, 'LI', 'Liechtenstein', 'A', 'Country', '110')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9111, 'LK', 'Sri Lanka', 'A', 'Country', '111')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9112, 'LR', 'Liberia', 'A', 'Country', '112')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9113, 'LS', 'Lesotho', 'A', 'Country', '113')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9114, 'LT', 'Lithuania', 'A', 'Country', '114')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9115, 'LU', 'Luxembourg', 'A', 'Country', '115')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9116, 'LV', 'Latvia', 'A', 'Country', '116')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9117, 'LY', 'Libya', 'A', 'Country', '117')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9118, 'MA', 'Morocco', 'A', 'Country', '118')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9119, 'MC', 'Monaco', 'A', 'Country', '119')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9120, 'MD', 'Moldova', 'A', 'Country', '120')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9121, 'MG', 'Madagascar', 'A', 'Country', '121')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9122, 'MK', 'Republic of Macedonia', 'A', 'Country', '122')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9123, 'ML', 'Mali', 'A', 'Country', '123')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9124, 'MM', 'Burma Myanmar', 'A', 'Country', '124')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9125, 'MN', 'Mongolia', 'A', 'Country', '125')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9126, 'MO', 'Macao', 'A', 'Country', '126')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9127, 'MQ', 'Martinique', 'A', 'Country', '127')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9128, 'MR', 'Mauritania', 'A', 'Country', '128')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9129, 'MS', 'Monserrat', 'A', 'Country', '129')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9130, 'MT', 'Malta', 'A', 'Country', '130')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9131, 'MU', 'Mauritius', 'A', 'Country', '131')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9132, 'MV', 'Maldives', 'A', 'Country', '132')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9133, 'MW', 'Malawi', 'A', 'Country', '133')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9134, 'MX', 'Mexico', 'A', 'Country', '134')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9135, 'MY', 'Malaysia', 'A', 'Country', '135')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9136, 'MZ', 'Mozambique', 'A', 'Country', '136')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9137, 'NA', 'Namibia', 'A', 'Country', '137')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9138, 'NC', 'New Caledonia', 'A', 'Country', '138')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9139, 'NE', 'Niger', 'A', 'Country', '139')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9140, 'NG', 'Nigeria', 'A', 'Country', '140')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9141, 'NI', 'Nicaragua', 'A', 'Country', '141')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9142, 'NL', 'Netherlands', 'A', 'Country', '142')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9143, 'NO', 'Norway', 'A', 'Country', '143')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9144, 'NP', 'Nepal', 'A', 'Country', '144')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9145, 'NR', 'Nauru', 'A', 'Country', '145')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9146, 'NZ', 'New Zealand', 'A', 'Country', '146')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9147, 'OM', 'Oman', 'A', 'Country', '147')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9148, 'PA', 'Panama', 'A', 'Country', '148')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9149, 'PE', 'Peru', 'A', 'Country', '149')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9150, 'PF', 'French Polynesia', 'A', 'Country', '150')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9151, 'PG', 'Papua New Guinea', 'A', 'Country', '151')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9152, 'PH', 'Philippines', 'A', 'Country', '152')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9153, 'PK', 'Pakistan', 'A', 'Country', '153')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9154, 'PL', 'Poland', 'A', 'Country', '154')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9155, 'PM', 'Saint Pierre & Miquelon', 'A', 'Country', '155')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9156, 'PN', 'Pitcairn Islands', 'A', 'Country', '156')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9157, 'PT', 'Portugal', 'A', 'Country', '157')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9158, 'PY', 'Paraguay', 'A', 'Country', '158')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9159, 'QA', 'Qatar', 'A', 'Country', '159')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9160, 'RE', 'Reunion', 'A', 'Country', '160')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9161, 'RO', 'Romania', 'A', 'Country', '161')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9162, 'RU', 'Russia', 'A', 'Country', '162')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9163, 'RW', 'Rwanda', 'A', 'Country', '163')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9164, 'SA', 'Saudi Arabia', 'A', 'Country', '164')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9165, 'SB', 'Solomon Islands Inc. Santa Cruz Is.', 'A', 'Country', '165')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9166, 'SC', 'Seychelles', 'A', 'Country', '166')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9167, 'SD', 'Sudan', 'A', 'Country', '167')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9168, 'SE', 'Sweden', 'A', 'Country', '168')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9169, 'SG', 'Singapore', 'A', 'Country', '169')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9170, 'SH', 'Saint Helena', 'A', 'Country', '170')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9171, 'SI', 'Slovenia', 'A', 'Country', '171')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9172, 'SK', 'Slovak Republic Slovakia', 'A', 'Country', '172')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9173, 'SL', 'Sierra Leone', 'A', 'Country', '173')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9174, 'SM', 'San Marino', 'A', 'Country', '174')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9175, 'SN', 'Senegal', 'A', 'Country', '175')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9176, 'SO', 'Somalia', 'A', 'Country', '176')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9177, 'SR', 'Suriname', 'A', 'Country', '177')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9178, 'ST', 'Sao Tome & Principe', 'A', 'Country', '178')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9179, 'SV', 'El Salvador', 'A', 'Country', '179')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9180, 'SY', 'Syria', 'A', 'Country', '180')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9181, 'SZ', 'Swaziland', 'A', 'Country', '181')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9182, 'TC', 'Turks & Caicos Islands', 'A', 'Country', '182')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9183, 'TD', 'Chad', 'A', 'Country', '183')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9184, 'TG', 'Togo', 'A', 'Country', '184')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9185, 'TH', 'Thailand', 'A', 'Country', '185')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9186, 'TJ', 'Tajikistan', 'A', 'Country', '186')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9187, 'TM', 'Turkmenistan', 'A', 'Country', '187')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9188, 'TN', 'Tunisia', 'A', 'Country', '188')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9189, 'TO', 'Tonga', 'A', 'Country', '189')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9190, 'TP', 'East Timor Indonesia', 'A', 'Country', '190')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9191, 'TR', 'Turkey', 'A', 'Country', '191')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9192, 'TT', 'Trinidad & Tobago', 'A', 'Country', '192')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9193, 'TV', 'Tuvalu', 'A', 'Country', '193')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9194, 'TW', 'Taiwan', 'A', 'Country', '194')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9195, 'TZ', 'Tanzania', 'A', 'Country', '195')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9196, 'UA', 'Ukraine', 'A', 'Country', '196')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9197, 'UG', 'Uganda', 'A', 'Country', '197')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9198, 'UY', 'Uruguay', 'A', 'Country', '198')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9199, 'UZ', 'Uzbekistan', 'A', 'Country', '199')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9200, 'VA', 'Vatican City', 'A', 'Country', '200')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9201, 'VC', 'Saint Vincent & the Grenadines', 'A', 'Country', '201')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9202, 'VE', 'Venezuela', 'A', 'Country', '202')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9203, 'VG', 'British Virgin Islands', 'A', 'Country', '203')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9204, 'VN', 'Vietnam', 'A', 'Country', '204')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9205, 'VU', 'Vanuatu', 'A', 'Country', '205')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9206, 'WF', 'Wallis & Futuna Islands', 'A', 'Country', '206')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9207, 'WS', 'Western Samoa', 'A', 'Country', '207')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9208, 'YE', 'Yemen', 'A', 'Country', '208')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9209, 'YU', 'Serbia-Montenegro Yugoslavia', 'A', 'Country', '209')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9210, 'ZA', 'South Africa', 'A', 'Country', '210')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9211, 'ZM', 'Zambia', 'A', 'Country', '211')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9212, 'ZR', 'Democratic Republic of the Congo', 'A', 'Country', '212')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(9213, 'ZW', 'Zimbabwe', 'A', 'Country', '213')

---
--- States
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('States',         'US States' , 'A',         @category_id, 'State'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10001, 'AK', 'Alaska', 'A', 'State', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10002, 'AL', 'Alabama', 'A', 'State', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10003, 'AR', 'Arkansas', 'A', 'State', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10004, 'AZ', 'Arizona', 'A', 'State', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10005, 'CA', 'California', 'A', 'State', '5')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10006, 'CO', 'Colorado', 'A', 'State', '6')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10007, 'CT', 'Connecticut', 'A', 'State', '7')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10008, 'DC', 'District of Columbia', 'A', 'State', '8')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10009, 'DE', 'Delaware', 'A', 'State', '9')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10010, 'FL', 'Florida', 'A', 'State', '10')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10011, 'GA', 'Georgia', 'A', 'State', '11')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10012, 'HI', 'Hawaii', 'A', 'State', '12')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10013, 'IA', 'Iowa', 'A', 'State', '13')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10014, 'ID', 'Idaho', 'A', 'State', '14')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10015, 'IL', 'Illinois', 'A', 'State', '15')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10016, 'IN', 'Indiana', 'A', 'State', '16')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10017, 'KS', 'Kansas', 'A', 'State', '17')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10018, 'KY', 'Kentucky', 'A', 'State', '18')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10019, 'LA', 'Louisiana', 'A', 'State', '19')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10020, 'MA', 'Massachusetts', 'A', 'State', '20')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10021, 'MD', 'Maryland', 'A', 'State', '21')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10022, 'ME', 'Maine', 'A', 'State', '22')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10023, 'MI', 'Michigan', 'A', 'State', '23')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10024, 'MN', 'Minnesota', 'A', 'State', '24')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10025, 'MO', 'Missouri', 'A', 'State', '25')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10026, 'MS', 'Mississippi', 'A', 'State', '26')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10027, 'MT', 'Montana', 'A', 'State', '27')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10028, 'NC', 'North Carolina', 'A', 'State', '28')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10029, 'ND', 'North Dakota', 'A', 'State', '29')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10030, 'NE', 'Nebraska', 'A', 'State', '30')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10031, 'NH', 'New Hampshire', 'A', 'State', '31')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10032, 'NJ', 'New Jersey', 'A', 'State', '32')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10033, 'NM', 'New Mexico', 'A', 'State', '33')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10034, 'NV', 'Nevada', 'A', 'State', '34')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10035, 'NY', 'New York', 'A', 'State', '35')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10036, 'OH', 'Ohio', 'A', 'State', '36')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10037, 'OK', 'Oklahoma', 'A', 'State', '37')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10038, 'OR', 'Oregon', 'A', 'State', '38')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10039, 'PA', 'Pennsylvania', 'A', 'State', '39')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10040, 'PR', 'Puerto Rico', 'A', 'State', '40')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10041, 'RI', 'Rhode Island', 'A', 'State', '41')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10042, 'SC', 'South Carolina', 'A', 'State', '42')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10043, 'SD', 'South Dakota', 'A', 'State', '43')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10044, 'TN', 'Tennessee', 'A', 'State', '44')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10045, 'TX', 'Texas', 'A', 'State', '45')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10046, 'UT', 'Utah', 'A', 'State', '46')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10047, 'VA', 'Virginia', 'A', 'State', '47')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10048, 'VT', 'Vermont', 'A', 'State', '48')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10049, 'WA', 'Washington', 'A', 'State', '49')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10050, 'WI', 'Wisconsin', 'A', 'State', '50')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10051, 'WV', 'West Virginia', 'A', 'State', '51')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(10052, 'WY', 'Wyoming', 'A', 'State', '52')

---
--- Address Source
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Address Source',         'Source of address update' , 'A',         @category_id, 'AddressSource'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(11001, 'U', 'Affiliate Update file', 'A', 'AddressSource', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(11002, 'S', 'Affiliate Staff', 'A', 'AddressSource', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(11003, 'O', 'Owner', 'A', 'AddressSource', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(11004, 'A', 'AFSCME International Staff', 'A', 'AddressSource', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(11005, 'N', 'NCOA Update', 'A', 'AddressSource', '5')

---
--- Person Address Type
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Person Address Type',         'Type of person''s address' , 'A',         @category_id, 'PersonAddressType'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(12001, '1', 'Home', 'A', 'PersonAddressType', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(12002, '2', 'Work', 'A', 'PersonAddressType', '2')

---
--- Country Citizenship
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Country Citizenship',         'Country of Citizenship' , 'A',         @category_id, 'CountryCitizenship'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19001, 'AD', 'Andorra', 'A', 'CountryCitizenship', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19002, 'AE', 'United Arab Emirates', 'A', 'CountryCitizenship', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19003, 'AF', 'Afghanistan', 'A', 'CountryCitizenship', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19004, 'AG', 'Antigua & Barbuda (Inc. Redonda)', 'A', 'CountryCitizenship', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19005, 'AI', 'Anguilla', 'A', 'CountryCitizenship', '5')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19006, 'AL', 'Albania', 'A', 'CountryCitizenship', '6')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19007, 'AM', 'Armenia', 'A', 'CountryCitizenship', '7')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19008, 'AN', 'Netherlands Antilles', 'A', 'CountryCitizenship', '8')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19009, 'AO', 'Angola', 'A', 'CountryCitizenship', '9')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19010, 'AR', 'Argentina', 'A', 'CountryCitizenship', '10')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19011, 'AT', 'Austria', 'A', 'CountryCitizenship', '11')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19012, 'AU', 'Australia', 'A', 'CountryCitizenship', '12')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19013, 'AW', 'Aruba', 'A', 'CountryCitizenship', '13')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19014, 'AZ', 'Azerbaijan', 'A', 'CountryCitizenship', '14')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19015, 'BA', 'Bosnia-Herzegovina', 'A', 'CountryCitizenship', '15')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19016, 'BB', 'Barbados', 'A', 'CountryCitizenship', '16')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19017, 'BD', 'Bangladesh', 'A', 'CountryCitizenship', '17')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19018, 'BE', 'Belgium', 'A', 'CountryCitizenship', '18')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19019, 'BF', 'Burkina Faso', 'A', 'CountryCitizenship', '19')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19020, 'BG', 'Bulgaria', 'A', 'CountryCitizenship', '20')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19021, 'BH', 'Bahrain', 'A', 'CountryCitizenship', '21')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19022, 'BI', 'Burundi', 'A', 'CountryCitizenship', '22')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19023, 'BJ', 'Benin', 'A', 'CountryCitizenship', '23')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19024, 'BM', 'Bermuda', 'A', 'CountryCitizenship', '24')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19025, 'BN', 'Brunei Darussalem', 'A', 'CountryCitizenship', '25')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19026, 'BO', 'Bolivia', 'A', 'CountryCitizenship', '26')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19027, 'BR', 'Brazil', 'A', 'CountryCitizenship', '27')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19028, 'BS', 'Bahamas', 'A', 'CountryCitizenship', '28')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19029, 'BT', 'Bhutan', 'A', 'CountryCitizenship', '29')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19030, 'BW', 'Botswana', 'A', 'CountryCitizenship', '30')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19031, 'BY', 'Belarus', 'A', 'CountryCitizenship', '31')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19032, 'BZ', 'Belize', 'A', 'CountryCitizenship', '32')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19033, 'CA', 'Canada', 'A', 'CountryCitizenship', '33')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19034, 'CF', 'Central African Republic', 'A', 'CountryCitizenship', '34')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19035, 'CG', 'Republic of the Congo (Brazzaville)', 'A', 'CountryCitizenship', '35')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19036, 'CH', 'Switzerland', 'A', 'CountryCitizenship', '36')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19037, 'CI', 'Ivory Coast', 'A', 'CountryCitizenship', '37')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19038, 'CL', 'Chile', 'A', 'CountryCitizenship', '38')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19039, 'CM', 'Cameroon', 'A', 'CountryCitizenship', '39')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19040, 'CN', 'China', 'A', 'CountryCitizenship', '40')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19041, 'CO', 'Colombia', 'A', 'CountryCitizenship', '41')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19042, 'CR', 'Costa Rica', 'A', 'CountryCitizenship', '42')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19043, 'CU', 'Cuba', 'A', 'CountryCitizenship', '43')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19044, 'CV', 'Cape Verde', 'A', 'CountryCitizenship', '44')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19045, 'CY', 'Cyprus', 'A', 'CountryCitizenship', '45')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19046, 'CZ', 'Czech Republic', 'A', 'CountryCitizenship', '46')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19047, 'DE', 'Germany', 'A', 'CountryCitizenship', '47')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19048, 'DJ', 'Djibouti', 'A', 'CountryCitizenship', '48')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19049, 'DK', 'Denmark', 'A', 'CountryCitizenship', '49')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19050, 'DM', 'Dominica', 'A', 'CountryCitizenship', '50')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19051, 'DO', 'Dominican Republic', 'A', 'CountryCitizenship', '51')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19052, 'DZ', 'Algeria', 'A', 'CountryCitizenship', '52')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19053, 'EC', 'Ecuador', 'A', 'CountryCitizenship', '53')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19054, 'EE', 'Estonia', 'A', 'CountryCitizenship', '54')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19055, 'EG', 'Egypt', 'A', 'CountryCitizenship', '55')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19056, 'ER', 'Eritrea', 'A', 'CountryCitizenship', '56')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19057, 'ES', 'Spain', 'A', 'CountryCitizenship', '57')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19058, 'ET', 'Ethiopia', 'A', 'CountryCitizenship', '58')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19059, 'FI', 'Finland', 'A', 'CountryCitizenship', '59')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19060, 'FJ', 'Fiji', 'A', 'CountryCitizenship', '60')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19061, 'FK', 'Falkland Islands', 'A', 'CountryCitizenship', '61')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19062, 'FR', 'France', 'A', 'CountryCitizenship', '62')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19063, 'GA', 'Gabon', 'A', 'CountryCitizenship', '63')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19064, 'GB', 'Great Britain & Northern Ireland', 'A', 'CountryCitizenship', '64')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19065, 'GD', 'Grenada', 'A', 'CountryCitizenship', '65')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19066, 'GE', 'Republic of Georgia', 'A', 'CountryCitizenship', '66')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19067, 'GF', 'French Guiana', 'A', 'CountryCitizenship', '67')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19068, 'GH', 'Ghana', 'A', 'CountryCitizenship', '68')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19069, 'GI', 'Gibraltar', 'A', 'CountryCitizenship', '69')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19070, 'GL', 'Greenland', 'A', 'CountryCitizenship', '70')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19071, 'GM', 'Gambia', 'A', 'CountryCitizenship', '71')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19072, 'GN', 'Guinea', 'A', 'CountryCitizenship', '72')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19073, 'GP', 'Guadeloupe', 'A', 'CountryCitizenship', '73')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19074, 'GQ', 'Equatorial Guinea', 'A', 'CountryCitizenship', '74')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19075, 'GR', 'Greece', 'A', 'CountryCitizenship', '75')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19076, 'GT', 'Guatemala', 'A', 'CountryCitizenship', '76')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19077, 'GW', 'Guinea Bissau', 'A', 'CountryCitizenship', '77')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19078, 'GY', 'Guyana', 'A', 'CountryCitizenship', '78')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19079, 'HK', 'Hong Kong', 'A', 'CountryCitizenship', '79')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19080, 'HN', 'Honduras', 'A', 'CountryCitizenship', '80')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19081, 'HR', 'Croatia', 'A', 'CountryCitizenship', '81')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19082, 'HT', 'Haiti', 'A', 'CountryCitizenship', '82')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19083, 'HU', 'Hungary', 'A', 'CountryCitizenship', '83')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19084, 'ID', 'Indonesia', 'A', 'CountryCitizenship', '84')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19085, 'IE', 'Ireland', 'A', 'CountryCitizenship', '85')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19086, 'IL', 'Israel', 'A', 'CountryCitizenship', '86')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19087, 'IN', 'India', 'A', 'CountryCitizenship', '87')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19088, 'IQ', 'Iraq', 'A', 'CountryCitizenship', '88')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19089, 'IR', 'Iran', 'A', 'CountryCitizenship', '89')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19090, 'IS', 'Iceland', 'A', 'CountryCitizenship', '90')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19091, 'IT', 'Italy', 'A', 'CountryCitizenship', '91')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19092, 'JM', 'Jamaica', 'A', 'CountryCitizenship', '92')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19093, 'JO', 'Jordan', 'A', 'CountryCitizenship', '93')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19094, 'JP', 'Japan', 'A', 'CountryCitizenship', '94')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19095, 'KE', 'Kenya', 'A', 'CountryCitizenship', '95')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19096, 'KG', 'Kyrgyzstan', 'A', 'CountryCitizenship', '96')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19097, 'KH', 'Cambodia', 'A', 'CountryCitizenship', '97')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19098, 'KI', 'Kiribati', 'A', 'CountryCitizenship', '98')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19099, 'KM', 'Comoros', 'A', 'CountryCitizenship', '99')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19100, 'KN', 'Saint Christopher (St.Kitts) & Nevis', 'A', 'CountryCitizenship', '100')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19101, 'KP', 'Dem. Peoples Rep. Korea (North)', 'A', 'CountryCitizenship', '101')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19102, 'KR', 'Republic of Korea (South)', 'A', 'CountryCitizenship', '102')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19103, 'KW', 'Kuwait', 'A', 'CountryCitizenship', '103')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19104, 'KY', 'Cayman Islands', 'A', 'CountryCitizenship', '104')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19105, 'KZ', 'Kazakhstan', 'A', 'CountryCitizenship', '105')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19106, 'LA', 'Laos', 'A', 'CountryCitizenship', '106')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19107, 'LB', 'Lebanon', 'A', 'CountryCitizenship', '107')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19108, 'LC', 'Saint Lucia', 'A', 'CountryCitizenship', '108')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19109, 'LI', 'Liechtenstein', 'A', 'CountryCitizenship', '109')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19110, 'LK', 'Sri Lanka', 'A', 'CountryCitizenship', '110')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19111, 'LR', 'Liberia', 'A', 'CountryCitizenship', '111')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19112, 'LS', 'Lesotho', 'A', 'CountryCitizenship', '112')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19113, 'LT', 'Lithuania', 'A', 'CountryCitizenship', '113')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19114, 'LU', 'Luxembourg', 'A', 'CountryCitizenship', '114')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19115, 'LV', 'Latvia', 'A', 'CountryCitizenship', '115')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19116, 'LY', 'Libya', 'A', 'CountryCitizenship', '116')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19117, 'MA', 'Morocco', 'A', 'CountryCitizenship', '117')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19118, 'MC', 'Monaco', 'A', 'CountryCitizenship', '118')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19119, 'MD', 'Moldova', 'A', 'CountryCitizenship', '119')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19120, 'MG', 'Madagascar', 'A', 'CountryCitizenship', '120')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19121, 'MK', 'Republic of Macedonia', 'A', 'CountryCitizenship', '121')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19122, 'ML', 'Mali', 'A', 'CountryCitizenship', '122')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19123, 'MM', 'Burma (Myanmar)', 'A', 'CountryCitizenship', '123')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19124, 'MN', 'Mongolia', 'A', 'CountryCitizenship', '124')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19125, 'MO', 'Macao', 'A', 'CountryCitizenship', '125')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19126, 'MQ', 'Martinique', 'A', 'CountryCitizenship', '126')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19127, 'MR', 'Mauritania', 'A', 'CountryCitizenship', '127')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19128, 'MS', 'Monserrat', 'A', 'CountryCitizenship', '128')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19129, 'MT', 'Malta', 'A', 'CountryCitizenship', '129')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19130, 'MU', 'Mauritius', 'A', 'CountryCitizenship', '130')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19131, 'MV', 'Maldives', 'A', 'CountryCitizenship', '131')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19132, 'MW', 'Malawi', 'A', 'CountryCitizenship', '132')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19133, 'MX', 'Mexico', 'A', 'CountryCitizenship', '133')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19134, 'MY', 'Malaysia', 'A', 'CountryCitizenship', '134')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19135, 'MZ', 'Mozambique', 'A', 'CountryCitizenship', '135')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19136, 'NA', 'Namibia', 'A', 'CountryCitizenship', '136')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19137, 'NC', 'New Caledonia', 'A', 'CountryCitizenship', '137')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19138, 'NE', 'Niger', 'A', 'CountryCitizenship', '138')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19139, 'NG', 'Nigeria', 'A', 'CountryCitizenship', '139')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19140, 'NI', 'Nicaragua', 'A', 'CountryCitizenship', '140')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19141, 'NL', 'Netherlands', 'A', 'CountryCitizenship', '141')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19142, 'NO', 'Norway', 'A', 'CountryCitizenship', '142')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19143, 'NP', 'Nepal', 'A', 'CountryCitizenship', '143')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19144, 'NR', 'Nauru', 'A', 'CountryCitizenship', '144')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19145, 'NZ', 'New Zealand', 'A', 'CountryCitizenship', '145')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19146, 'OM', 'Oman', 'A', 'CountryCitizenship', '146')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19147, 'PA', 'Panama', 'A', 'CountryCitizenship', '147')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19148, 'PE', 'Peru', 'A', 'CountryCitizenship', '148')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19149, 'PF', 'French Polynesia', 'A', 'CountryCitizenship', '149')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19150, 'PG', 'Papua New Guinea', 'A', 'CountryCitizenship', '150')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19151, 'PH', 'Philippines', 'A', 'CountryCitizenship', '151')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19152, 'PK', 'Pakistan', 'A', 'CountryCitizenship', '152')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19153, 'PL', 'Poland', 'A', 'CountryCitizenship', '153')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19154, 'PM', 'Saint Pierre & Miquelon', 'A', 'CountryCitizenship', '154')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19155, 'PN', 'Pitcairn Islands', 'A', 'CountryCitizenship', '155')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19156, 'PT', 'Portugal', 'A', 'CountryCitizenship', '156')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19157, 'PY', 'Paraguay', 'A', 'CountryCitizenship', '157')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19158, 'QA', 'Qatar', 'A', 'CountryCitizenship', '158')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19159, 'RE', 'Reunion', 'A', 'CountryCitizenship', '159')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19160, 'RO', 'Romania', 'A', 'CountryCitizenship', '160')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19161, 'RU', 'Russia', 'A', 'CountryCitizenship', '161')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19162, 'RW', 'Rwanda', 'A', 'CountryCitizenship', '162')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19163, 'SA', 'Saudi Arabia', 'A', 'CountryCitizenship', '163')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19164, 'SB', 'Solomon Islands (Inc. Santa Cruz Is.)', 'A', 'CountryCitizenship', '164')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19165, 'SC', 'Seychelles', 'A', 'CountryCitizenship', '165')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19166, 'SD', 'Sudan', 'A', 'CountryCitizenship', '166')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19167, 'SE', 'Sweden', 'A', 'CountryCitizenship', '167')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19168, 'SG', 'Singapore', 'A', 'CountryCitizenship', '168')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19169, 'SH', 'Saint Helena', 'A', 'CountryCitizenship', '169')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19170, 'SI', 'Slovenia', 'A', 'CountryCitizenship', '170')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19171, 'SK', 'Slovak Republic (Slovakia)', 'A', 'CountryCitizenship', '171')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19172, 'SL', 'Sierra Leone', 'A', 'CountryCitizenship', '172')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19173, 'SM', 'San Marino', 'A', 'CountryCitizenship', '173')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19174, 'SN', 'Senegal', 'A', 'CountryCitizenship', '174')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19175, 'SO', 'Somalia', 'A', 'CountryCitizenship', '175')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19176, 'SR', 'Suriname', 'A', 'CountryCitizenship', '176')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19177, 'ST', 'Sao Tome & Principe', 'A', 'CountryCitizenship', '177')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19178, 'SV', 'El Salvador', 'A', 'CountryCitizenship', '178')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19179, 'SY', 'Syria', 'A', 'CountryCitizenship', '179')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19180, 'SZ', 'Swaziland', 'A', 'CountryCitizenship', '180')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19181, 'TC', 'Turks & Caicos Islands', 'A', 'CountryCitizenship', '181')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19182, 'TD', 'Chad', 'A', 'CountryCitizenship', '182')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19183, 'TG', 'Togo', 'A', 'CountryCitizenship', '183')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19184, 'TH', 'Thailand', 'A', 'CountryCitizenship', '184')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19185, 'TJ', 'Tajikistan', 'A', 'CountryCitizenship', '185')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19186, 'TM', 'Turkmenistan', 'A', 'CountryCitizenship', '186')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19187, 'TN', 'Tunisia', 'A', 'CountryCitizenship', '187')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19188, 'TO', 'Tonga', 'A', 'CountryCitizenship', '188')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19189, 'TP', 'East Timor (Indonesia)', 'A', 'CountryCitizenship', '189')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19190, 'TR', 'Turkey', 'A', 'CountryCitizenship', '190')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19191, 'TT', 'Trinidad & Tobago', 'A', 'CountryCitizenship', '191')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19192, 'TV', 'Tuvalu', 'A', 'CountryCitizenship', '192')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19193, 'TW', 'Taiwan', 'A', 'CountryCitizenship', '193')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19194, 'TZ', 'Tanzania', 'A', 'CountryCitizenship', '194')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19195, 'UA', 'Ukraine', 'A', 'CountryCitizenship', '195')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19196, 'UG', 'Uganda', 'A', 'CountryCitizenship', '196')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19197, 'US', 'United States of America', 'A', 'CountryCitizenship', '197')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19198, 'UY', 'Uruguay', 'A', 'CountryCitizenship', '198')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19199, 'UZ', 'Uzbekistan', 'A', 'CountryCitizenship', '199')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19200, 'VA', 'Vatican City', 'A', 'CountryCitizenship', '200')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19201, 'VC', 'Saint Vincent & the Grenadines', 'A', 'CountryCitizenship', '201')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19202, 'VE', 'Venezuela', 'A', 'CountryCitizenship', '202')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19203, 'VG', 'British Virgin Islands', 'A', 'CountryCitizenship', '203')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19204, 'VN', 'Vietnam', 'A', 'CountryCitizenship', '204')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19205, 'VU', 'Vanuatu', 'A', 'CountryCitizenship', '205')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19206, 'WF', 'Wallis & Futuna Islands', 'A', 'CountryCitizenship', '206')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19207, 'WS', 'Western Samoa', 'A', 'CountryCitizenship', '207')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19208, 'YE', 'Yemen', 'A', 'CountryCitizenship', '208')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19209, 'YU', 'Serbia-Montenegro (Yugoslavia)', 'A', 'CountryCitizenship', '209')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19210, 'ZA', 'South Africa', 'A', 'CountryCitizenship', '210')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19211, 'ZM', 'Zambia', 'A', 'CountryCitizenship', '211')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19212, 'ZR', 'Democratic Republic of the Congo', 'A', 'CountryCitizenship', '212')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(19213, 'ZW', 'Zimbabwe', 'A', 'CountryCitizenship', '213')

---
--- Employer Sector
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Employer Sector',         'Employer Sector' , 'A',         @category_id, 'EmployerSector'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(20001, '1', 'State', 'A', 'EmployerSector', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(20002, '2', 'County', 'A', 'EmployerSector', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(20003, '3', 'Private', 'A', 'EmployerSector', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(20004, '4', 'City', 'A', 'EmployerSector', '4')

---
--- Organization Sub Type
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Organization Sub Type',         'Organization Sub Type' , 'A',         @category_id, 'OrganizationSubType'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(24001, 'A', 'Affiliate Sub Type', 'A', 'OrganizationSubType', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(24002, 'O', 'Organization Sub Type', 'A', 'OrganizationSubType', '2')

---
--- Employee Job Titles
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Employee Job Titles',         'AFSCME Employee Job Titles' , 'A',         @category_id, 'EmployeeJobTitle'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(43001, '1', 'Employment', ' Recruitment and Placement Specialists', 'EmployeeJobTitle', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(43002, '2', 'Human Resources Services', 'A', 'EmployeeJobTitle', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(43003, '3', 'Emergency Management Specialists', 'A', 'EmployeeJobTitle', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(43004, '4', 'Law Enforcement Oficer', 'A', 'EmployeeJobTitle', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(43005, '5', 'Public Relations Managers', 'A', 'EmployeeJobTitle', '5')

---
--- Employee Salary Range
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Employee Salary Range',         'AFSCME Employee Salary Ranges' , 'A',         @category_id, 'EmployeeSalaryRange'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(44001, '1', '$0-25,000', 'A', 'EmployeeSalaryRange', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(44002, '2', '$25,001-50,000', 'A', 'EmployeeSalaryRange', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(44003, '3', '$50,001-75,000', 'A', 'EmployeeSalaryRange', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(44004, '4', '$76,001-100,000', 'A', 'EmployeeSalaryRange', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(44005, '5', '$100,000+', 'A', 'EmployeeSalaryRange', '5')

---
--- Employee Sectors
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Employee Sectors',         'AFSCME Employee Sectors' , 'A',         @category_id, 'EmployeeSector'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(45001, '1', 'Healthcare and Social Assistance', 'A', 'EmployeeSector', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(45002, '2', 'Transportation and Warehousing', 'A', 'EmployeeSector', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(45003, '3', 'Public Administration', 'A', 'EmployeeSector', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(45004, '4', 'Education Services', 'A', 'EmployeeSector', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(45005, '5', 'Waste Management and Remediation Services', 'A', 'EmployeeSector', '5')

---
--- Meeting Frequency
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Meeting Frequency',         'Meeting Frequency' , 'A',         @category_id, 'MeetingFrequency'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(50001, '1', 'Monthly', 'A', 'MeetingFrequency', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(50002, '2', 'Bi-Annually', 'A', 'MeetingFrequency', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(50003, '3', 'Annually', 'A', 'MeetingFrequency', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(50004, '4', 'Quarterly', 'A', 'MeetingFrequency', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(50005, '5', 'Other', 'A', 'MeetingFrequency', '5')

---
--- Point Of Contact
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Point Of Contact',         'Point Of Contact' , 'A',         @category_id, 'POC'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(52001, '1', 'Membership', 'A', 'POC', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(52002, '2', 'PAC', 'A', 'POC', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(52003, '3', 'IT', 'A', 'POC', '3')

---
--- Rebate Codes
---
INSERT INTO Common_Code_Category
VALUES ('Rebate', 'Rebate Codes', 'A')
set @category_id = @@identity
---
--- Rebate Comment Analysis Codes
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Rebate Comment Analysis Codes',         'Rebate Comment Analysis Codes' , 'A',         @category_id, 'RbtCommentAnalCode'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53001, '31', 'General Objection - CN 31 members whose applications may have been thrown out', 'A', 'RbtCommentAnalCode', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53002, '21', 'Objects to Section 211', ' exclusion for employer provided educational benefits (NY)', 'RbtCommentAnalCode', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53003, 'AD', 'Objects to support of Arthur Eve (New York)', 'A', 'RbtCommentAnalCode', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53004, 'AG', 'Opposes Anti-Gun politicians', 'A', 'RbtCommentAnalCode', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53005, 'AI', 'Objects to DC 37 Illegal Activities', 'A', 'RbtCommentAnalCode', '5')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53006, 'AS', 'Objects to Support of NY Assemblyman Silver', 'A', 'RbtCommentAnalCode', '6')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53007, 'BE', 'Objects to Bilingual Education', 'A', 'RbtCommentAnalCode', '7')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53008, 'BG', 'Objects to support of Benjamin Gilman (NY)', 'A', 'RbtCommentAnalCode', '8')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53009, 'BV', 'Objects to Buying Votes', 'A', 'RbtCommentAnalCode', '9')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53010, 'CB', 'Objects to any activity that is not directly related to collective bargaining', 'A', 'RbtCommentAnalCode', '10')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53011, 'CH', 'Objects to support of Hillary Rodham Clinton', 'A', 'RbtCommentAnalCode', '11')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53012, 'CI', 'Lack of Committment/Expenditures on Corrections Issues', 'A', 'RbtCommentAnalCode', '12')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53013, 'CP', 'Corrupt Political Activites', 'A', 'RbtCommentAnalCode', '13')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53014, 'CV', 'Activities that are contrary to Christian Values (abortion', ' homosexual organizations)', 'RbtCommentAnalCode', '14')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53015, 'DH', 'Dues too High', 'A', 'RbtCommentAnalCode', '15')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53016, 'DP', 'Objects to Support of Democratic Politicians', 'A', 'RbtCommentAnalCode', '16')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53017, 'DT', 'Objects to Drug Testing (Cn 31)', 'A', 'RbtCommentAnalCode', '17')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53018, 'E', 'Entitlement based on union membership', 'A', 'RbtCommentAnalCode', '18')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53019, 'EI', 'Objects to certain Environmental Issues', 'A', 'RbtCommentAnalCode', '19')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53020, 'G', 'General Objection to AFSCME''s spending any dues money for any political candidate', 'A', 'RbtCommentAnalCode', '20')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53021, 'GO', 'Objects to Current Gubernatorial Administration', 'A', 'RbtCommentAnalCode', '21')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53022, 'HC', 'Health Care/Medicaide', 'A', 'RbtCommentAnalCode', '22')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53023, 'IP', 'Objects to support of Incumbent Politicians', 'A', 'RbtCommentAnalCode', '23')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53024, 'LL', 'Objects to political expenditures at the Local Level', 'A', 'RbtCommentAnalCode', '24')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53025, 'LW', 'Objects to Licensing of Child Welfare workers', 'A', 'RbtCommentAnalCode', '25')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53026, 'MA', 'Objects to Media Advertising', 'A', 'RbtCommentAnalCode', '26')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53027, 'MC', 'Objects to support of Michael Coleman', 'A', 'RbtCommentAnalCode', '27')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53028, 'MG', 'Objects to support of Maryland Governor & Baltimore County Executive', 'A', 'RbtCommentAnalCode', '28')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53029, 'MR', 'Multiple Reasons', 'A', 'RbtCommentAnalCode', '29')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53030, 'MV', 'Wants Membership Vote for any expenditures for political purposes', 'A', 'RbtCommentAnalCode', '30')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53031, 'NR', 'Non-Responsive', 'A', 'RbtCommentAnalCode', '31')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53032, 'NS', 'No Specific objection', 'A', 'RbtCommentAnalCode', '32')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53033, 'OC', 'Objects to AFSCME''s Choice of Candidates', 'A', 'RbtCommentAnalCode', '33')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53034, 'OG', 'Objects to support of Mayor Guliani (NY)', 'A', 'RbtCommentAnalCode', '34')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53035, 'OH', 'Objects to Hawaiian government''s policies and projects', 'A', 'RbtCommentAnalCode', '35')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53036, 'OL', 'Opposes Lobbyists', 'A', 'RbtCommentAnalCode', '36')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53037, 'OM', 'Objects to Support of NYC Mayoral Candidate Mark Green', 'A', 'RbtCommentAnalCode', '37')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53038, 'PA', 'Objects to Pataki Administration/and money given in support of him & NY State Senate', 'A', 'RbtCommentAnalCode', '38')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53039, 'PB', 'Personal Business - Not Union Business', 'A', 'RbtCommentAnalCode', '39')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53040, 'PC', 'Against contributing to Political Campaigns', 'A', 'RbtCommentAnalCode', '40')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53041, 'PD', 'Objects to Capital Punishment', 'A', 'RbtCommentAnalCode', '41')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53042, 'PG', 'Use of Union Funds for Personal Gain', 'A', 'RbtCommentAnalCode', '42')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53043, 'PJ', 'Objects to Privatization of Jobs', 'A', 'RbtCommentAnalCode', '43')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53044, 'PP', 'Objects to Privatization of Prisons', 'A', 'RbtCommentAnalCode', '44')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53045, 'RC', 'Objects to contributions to local Republican Candidates', 'A', 'RbtCommentAnalCode', '45')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53046, 'RE', 'Funding of Re-election campaigns', 'A', 'RbtCommentAnalCode', '46')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53047, 'RO', 'Religious Objections (non-specific)', 'A', 'RbtCommentAnalCode', '47')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53048, 'SF', 'Objects to Support of Federal Programs', 'A', 'RbtCommentAnalCode', '48')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53049, 'SM', 'Objects to Socialized Medicine', 'A', 'RbtCommentAnalCode', '49')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53050, 'SO', 'Supports Ban on Soft Money', 'A', 'RbtCommentAnalCode', '50')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53051, 'SP', 'Objects to Socialist Party', 'A', 'RbtCommentAnalCode', '51')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53052, 'SS', 'Objects to Social Security taxes', 'A', 'RbtCommentAnalCode', '52')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53053, 'TA', 'Trips Abroad', 'A', 'RbtCommentAnalCode', '53')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53054, 'TF', 'Objects to subsidies to Tobacco Farms/Farmers and Tobacco companies', 'A', 'RbtCommentAnalCode', '54')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53055, 'TR', 'Objects to support of Tom Ridge (PA)', 'A', 'RbtCommentAnalCode', '55')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53056, 'TT', 'Objects to support of Tommy Thompson (WI)', 'A', 'RbtCommentAnalCode', '56')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53057, 'UN', 'Union Non-supportive of members'' interests', 'A', 'RbtCommentAnalCode', '57')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53058, 'US', 'Objects to expenditures for Union Sponsored Functions', 'A', 'RbtCommentAnalCode', '58')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53059, 'WB', 'Objects to the Whistle Blower Protection Bill (NY)', 'A', 'RbtCommentAnalCode', '59')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53060, 'WR', 'Objects to expenditures for Womens Rights', 'A', 'RbtCommentAnalCode', '60')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(53061, 'WS', 'Wasteful Spending of Union Funds', 'A', 'RbtCommentAnalCode', '61')

---
--- Rebate Acceptance Codes
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Rebate Acceptance Codes',         'Rebate Acceptance Codes' , 'A',         @category_id, 'RebateAcceptanceCode'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(54001, 'C', 'Council Accepted', 'A', 'RebateAcceptanceCode', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(54002, 'L', 'Local Accepted', 'A', 'RebateAcceptanceCode', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(54003, 'D', 'Denied', 'A', 'RebateAcceptanceCode', '3')

---
--- Rebate Application Evaluation Codes
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Rebate Application Evaluation Codes',         'Rebate Application Evaluation Codes' , 'A',         @category_id, 'RebateAppEvalCode'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(55001, 'NR', 'Not Returned', 'A', 'RebateAppEvalCode', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(55002, 'NT', 'Not Timely; not returned in 30 days', 'A', 'RebateAppEvalCode', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(55003, 'Q1', 'Not indicated or NO to Question 1', 'A', 'RebateAppEvalCode', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(55004, 'Q7', 'Not indicated or NO to Question 7', 'A', 'RebateAppEvalCode', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(55005, 'QB', 'Not indicated or NO to Questions 1 & 7', 'A', 'RebateAppEvalCode', '5')

---
--- Rebate Duration
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Rebate Duration',         'Duration dues were paid' , 'A',         @category_id, 'RebateDuration'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(56001, '1', '1 Month', 'A', 'RebateDuration', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(56002, '2', '2 Months', 'A', 'RebateDuration', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(56003, '3', '3 Months', 'A', 'RebateDuration', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(56004, '4', '4 Months', 'A', 'RebateDuration', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(56005, '5', '5 Months', 'A', 'RebateDuration', '5')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(56006, '6', '6 Months', 'A', 'RebateDuration', '6')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(56007, '7', '7 Months', 'A', 'RebateDuration', '7')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(56008, '8', '8 Months', 'A', 'RebateDuration', '8')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(56009, '9', '9 Months', 'A', 'RebateDuration', '9')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(56010, '10', '10 Months', 'A', 'RebateDuration', '10')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(56011, '11', '11 Months', 'A', 'RebateDuration', '11')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(56012, '12', '12 Months', 'A', 'RebateDuration', '12')

---
--- Rebate Filed With
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Rebate Filed With',         'Rebate Filed With' , 'A',         @category_id, 'RebateFiledWith'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(57001, 'C', 'Council', 'A', 'RebateFiledWith', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(57002, 'L', 'Local', 'A', 'RebateFiledWith', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(57003, 'B', 'Both', 'A', 'RebateFiledWith', '3')

---
--- Rebate Member Status
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Rebate Member Status',         'Rebate Member Status' , 'A',         @category_id, 'RebateMbrStatus'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(58001, 'A', 'Active', 'A', 'RebateMbrStatus', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(58002, 'I', 'Inactive', 'A', 'RebateMbrStatus', '2')

---
--- Rebate Member Type
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Rebate Member Type',         'Rebate Member Type' , 'A',         @category_id, 'RebateMbrType'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(59001, 'R', 'Regular', 'A', 'RebateMbrType', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(59002, 'T', 'Retiree', 'A', 'RebateMbrType', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(59003, 'S', 'Retiree Spouse', 'A', 'RebateMbrType', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(59004, 'U', 'Union Shop', 'A', 'RebateMbrType', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(59005, 'O', 'Union Shop Objector', 'A', 'RebateMbrType', '5')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(59006, 'A', 'Agency Fee Payer', 'A', 'RebateMbrType', '6')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(59007, 'N', 'Non Member', 'A', 'RebateMbrType', '7')

---
--- Rebate Reason Denied
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Rebate Reason Denied',         'Reason a rebate was denied' , 'A',         @category_id, 'RebateReasonDenied'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(60001, 'M', 'Multiple Submissions', 'A', 'RebateReasonDenied', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(60002, 'NC', 'Not Certified', 'A', 'RebateReasonDenied', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(60003, 'NT', 'Not Timely', 'A', 'RebateReasonDenied', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(60004, 'P', 'Prior Years Invalid', 'A', 'RebateReasonDenied', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(60005, 'Z', 'Not an AFSCME Member', 'A', 'RebateReasonDenied', '5')

---
--- Rebate Status
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Rebate Status',         'Status of a rebate application' , 'A',         @category_id, 'RebateStatus'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(75001, 'A', 'Approved', 'A', 'RebateStatus', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(75002, 'D', 'Denied', 'A', 'RebateStatus', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(75003, 'IP', 'In Progress', 'A', 'RebateStatus', '3')

---
--- Rebate Roster Status
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Rebate Roster Status',         'Rebate Roster Status' , 'A',         @category_id, 'PRBRosterStatus'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(77001, 'P', 'Preliminary', 'A', 'PRBRosterStatus', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(77002, 'F', 'Final', 'A', 'PRBRosterStatus', '2')

---
--- Person Codes
---
INSERT INTO Common_Code_Category
VALUES ('Person', 'Person Codes', 'A')
set @category_id = @@identity
---
--- Political Party
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Political Party',         'Political Party' , 'A',         @category_id, 'PoliticalParty'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(2001, 'D', 'Democrat', 'A', 'PoliticalParty', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(2002, 'R', 'Republican', 'A', 'PoliticalParty', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(2003, 'I', 'Independent', 'A', 'PoliticalParty', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(2004, 'O', 'Other', 'A', 'PoliticalParty', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(2005, 'U', 'Unknown', 'A', 'PoliticalParty', '5')

---
--- Gender
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Gender',         'Gender' , 'A',         @category_id, 'Gender'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(33001, 'M', 'Male', 'A', 'Gender', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(33002, 'F', 'Female', 'A', 'Gender', '2')

---
--- Registered Voter
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Registered Voter',         'Registered Voter' , 'A',         @category_id, 'RegisteredVoter'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(34001, 'Y', 'Yes', 'A', 'RegisteredVoter', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(34002, 'N', 'No', 'A', 'RegisteredVoter', '2')

---
--- Suffix
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Suffix',         'Suffix' , 'A',         @category_id, 'Suffix'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(35001, '1', 'Sr', 'A', 'Suffix', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(35002, '2', 'Jr', 'A', 'Suffix', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(35003, '3', 'III', 'A', 'Suffix', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(35004, '4', 'IV', 'A', 'Suffix', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(35005, '5', 'II', 'A', 'Suffix', '5')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(35006, '6', 'Esq', 'A', 'Suffix', '6')

---
--- Prefix
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Prefix',         'Prefix' , 'A',         @category_id, 'Prefix'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(36001, '1', 'Mr', 'A', 'Prefix', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(36002, '2', 'Ms', 'A', 'Prefix', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(36003, '3', 'Mrs', 'A', 'Prefix', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(36004, '4', 'Miss', 'A', 'Prefix', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(36005, '5', 'Rev', 'A', 'Prefix', '5')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(36006, '6', 'Dr', 'A', 'Prefix', '6')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(36007, '7', 'Hon', 'A', 'Prefix', '7')

---
--- Disability Accomodations
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Disability Accomodations',         'Disability Accomodations' , 'A',         @category_id, 'Accomodations'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(37001, '1', 'Wheelchair accessibility', 'A', 'Accomodations', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(37002, '2', 'Refrigeration for medication', 'A', 'Accomodations', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(37003, '3', 'Materials: Braille', 'A', 'Accomodations', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(37004, '4', 'Materials: Cassette', 'A', 'Accomodations', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(37005, '5', 'Materials: Large Type', 'A', 'Accomodations', '5')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(37006, '6', 'ASL Interpreter', 'A', 'Accomodations', '6')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(37007, '7', 'Assisted Listening Device', 'A', 'Accomodations', '7')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(37008, '8', 'Telecommunications Device', 'A', 'Accomodations', '8')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(37009, '9', 'Handicap Rest/Bathroom', 'A', 'Accomodations', '9')

---
--- Disability Types
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Disability Types',         'Disability Types' , 'A',         @category_id, 'Disabilities'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(40001, '1', 'Musculoskeletal', 'A', 'Disabilities', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(40002, '2', 'Blood Serum Disorders', 'A', 'Disabilities', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(40003, '3', 'Cardiovascular/Circulatory', 'A', 'Disabilities', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(40004, '4', 'Respiratory', 'A', 'Disabilities', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(40005, '5', 'Diabetes', 'A', 'Disabilities', '5')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(40006, '6', 'Epilepsy', 'A', 'Disabilities', '6')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(40007, '7', 'Speech', 'A', 'Disabilities', '7')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(40008, '8', 'Language', 'A', 'Disabilities', '8')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(40009, '9', 'Hearing', 'A', 'Disabilities', '9')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(40010, '10', 'Mental', 'A', 'Disabilities', '10')

---
--- Ethnic Origin
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Ethnic Origin',         'Ethnic Origin' , 'A',         @category_id, 'EthnicOrigin'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(46001, '1', 'African-American', 'A', 'EthnicOrigin', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(46002, '2', 'Asian-American', 'A', 'EthnicOrigin', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(46003, '3', 'European-American', 'A', 'EthnicOrigin', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(46004, '4', 'Latino-American', 'A', 'EthnicOrigin', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(46005, '5', 'Native-American', 'A', 'EthnicOrigin', '5')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(46006, '6', 'Asian', 'A', 'EthnicOrigin', '6')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(46007, '7', 'Hispanic', 'A', 'EthnicOrigin', '7')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(46008, '8', 'Caucasian', 'A', 'EthnicOrigin', '8')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(46009, '9', 'Foreign', 'A', 'EthnicOrigin', '9')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(46010, '10', 'Mixed', 'A', 'EthnicOrigin', '10')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(46011, '11', 'Other', 'A', 'EthnicOrigin', '11')

---
--- Languages
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Languages',         'Languages' , 'A',         @category_id, 'Languages'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(48001, '1', 'English', 'A', 'Languages', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(48002, '2', 'Spanish or Spanish Creole', 'A', 'Languages', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(48003, '3', 'French or French Creole', 'A', 'Languages', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(48004, '4', 'German', 'A', 'Languages', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(48005, '5', 'Chinese', 'A', 'Languages', '5')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(48006, '6', 'Italian', 'A', 'Languages', '6')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(48007, '7', 'Indic', 'A', 'Languages', '7')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(48008, '8', 'Vietnamese', 'A', 'Languages', '8')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(48009, '9', 'Portuguese or Portuguese Creole', 'A', 'Languages', '9')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(48010, '10', 'Japanese', 'A', 'Languages', '10')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(48011, '11', 'Greek', 'A', 'Languages', '11')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(48012, '12', 'Native North American languages', 'A', 'Languages', '12')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(48013, '13', 'Tagalog', 'A', 'Languages', '13')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(48014, '14', 'Polish', 'A', 'Languages', '14')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(48015, '15', 'Other Indo-European language', 'A', 'Languages', '15')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(48016, '16', 'Korean', 'A', 'Languages', '16')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(48017, '17', 'Russian', 'A', 'Languages', '17')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(48018, '18', 'Yiddish', 'A', 'Languages', '18')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(48019, '19', 'Scandanavian', 'A', 'Languages', '19')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(48020, '20', 'South Slavic', 'A', 'Languages', '20')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(48021, '21', 'Mon-Khmer', 'A', 'Languages', '21')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(48022, '22', 'Other-Western Germanic language', 'A', 'Languages', '22')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(48023, '23', 'Unspecified', 'A', 'Languages', '23')

---
--- Marital Status
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Marital Status',         'Marital Status' , 'A',         @category_id, 'MaritalStatus'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(49001, '1', 'Single', 'A', 'MaritalStatus', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(49002, '2', 'Married', 'A', 'MaritalStatus', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(49003, '3', 'Domestic Partnership', 'A', 'MaritalStatus', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(49004, '4', 'Separated', 'A', 'MaritalStatus', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(49005, '5', 'Divorced', 'A', 'MaritalStatus', '5')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(49006, '6', 'Widowed', 'A', 'MaritalStatus', '6')

---
--- Religion
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Religion',         'Religion' , 'A',         @category_id, 'Religion'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(62001, '1', 'Catholic', 'A', 'Religion', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(62002, '2', 'Baptist', 'A', 'Religion', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(62003, '3', 'Methodist', 'A', 'Religion', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(62004, '4', 'Lutheran', 'A', 'Religion', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(62005, '5', 'Pentecostal/Charismatic/Foursquare', 'A', 'Religion', '5')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(62006, '6', 'Presbyterian', 'A', 'Religion', '6')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(62007, '7', 'Mormon/Church of Jesus Christ Latter-day Saints', 'A', 'Religion', '7')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(62008, '8', 'Non-denominational Christians', 'A', 'Religion', '8')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(62009, '9', 'Church of Christ', 'A', 'Religion', '9')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(62010, '10', 'Episcopal/Angelican', 'A', 'Religion', '10')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(62011, '11', 'Assemblies of God', 'A', 'Religion', '11')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(62012, '12', 'Congregational/United Church of Christ', 'A', 'Religion', '12')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(62013, '13', 'Seventh-Day Adventist', 'A', 'Religion', '13')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(62014, '14', 'Jewish', 'A', 'Religion', '14')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(62015, '15', 'Islamic', 'A', 'Religion', '15')

---
--- Email Type
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Email Type',         'Email Type' , 'A',         @category_id, 'EmailType'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(71001, '1', 'Primary', 'A', 'EmailType', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(71002, '2', 'Alternate', 'A', 'EmailType', '2')

---
--- Relation
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Relation',         'General Demographic Information' , 'A',         @category_id, 'Relation'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(80001, '1', 'Child', 'A', 'Relation', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(80002, '2', 'Spouse/Domestic Partner', 'A', 'Relation', '2')

---
--- Organization Codes
---
INSERT INTO Common_Code_Category
VALUES ('Organization', 'Organization Codes', 'A')
set @category_id = @@identity
---
--- Organization Type
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Organization Type',         'Types of Organizations' , 'A',         @category_id, 'OrganizationType'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(28001, '1', 'Library', 'A', 'OrganizationType', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(28002, '2', 'University', 'A', 'OrganizationType', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(28003, '3', 'Non-Profit', 'A', 'OrganizationType', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(28004, '4', 'Labor Union', 'A', 'OrganizationType', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(28005, '5', 'PAC', 'D', 'OrganizationType', '5')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(28006, '6', 'Corporation', 'D', 'OrganizationType', '6')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(28007, '7', 'Employer', 'A', 'OrganizationType', '7')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(28008, '8', 'State Party', 'D', 'OrganizationType', '8')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(28009, '9', 'National Party', 'D', 'OrganizationType', '9')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(28010, '10', 'Committee', 'D', 'OrganizationType', '10')

---
--- Organization Address Type
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Organization Address Type',         'Organization Address Type' , 'A',         @category_id, 'OrgAddressType'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(72001, '1', 'Regular', 'A', 'OrgAddressType', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(72002, '2', 'Shipping', 'A', 'OrgAddressType', '2')

---
--- Organization Phone Type
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Organization Phone Type',         'Organization Phone Type' , 'A',         @category_id, 'OrgPhoneType'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(73001, '1', 'Office', 'A', 'OrgPhoneType', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(73002, '2', 'Fax', 'A', 'OrgPhoneType', '2')

---
--- OrganizationPositionTitle
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('OrganizationPositionTitle',         'Title of Org Position for Organization Associate' , 'A',         @category_id, 'OrgPositionTitle'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(76001, '1', 'Organization Member', 'A', 'OrgPositionTitle', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(76002, '2', 'Director', 'A', 'OrgPositionTitle', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(76003, '3', 'President', 'A', 'OrgPositionTitle', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(76004, '4', 'Vice President', 'A', 'OrgPositionTitle', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(76005, '5', 'Senior Engineer', 'A', 'OrgPositionTitle', '5')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(76006, '6', 'Developer', 'A', 'OrgPositionTitle', '6')

---
--- Officer Codes
---
INSERT INTO Common_Code_Category
VALUES ('Officer', 'Officer Codes', 'A')
set @category_id = @@identity
---
--- AFSCME Title
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('AFSCME Title',         'AFSCME Officer Titles' , 'A',         @category_id, 'AFSCMETitle'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6001, '1', 'International President', 'A', 'AFSCMETitle', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6002, '2', 'International Secretary-Treasurer', 'A', 'AFSCMETitle', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6003, '3', 'International Vice-President', 'A', 'AFSCMETitle', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6004, '4', 'Administrator', 'A', 'AFSCMETitle', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6005, '5', 'Deputy Administrator', 'A', 'AFSCMETitle', '5')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6006, '6', 'Executive Director', 'A', 'AFSCMETitle', '6')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6007, '7', 'Director', 'A', 'AFSCMETitle', '7')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6008, '8', 'Regional Director', 'A', 'AFSCMETitle', '8')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6009, '9', 'President', 'A', 'AFSCMETitle', '9')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6010, '10', 'Chairman', 'A', 'AFSCMETitle', '10')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6011, '11', 'Co-Chair', 'A', 'AFSCMETitle', '11')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6012, '12', 'Executive Vice-President', 'A', 'AFSCMETitle', '12')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6013, '13', 'Vice President', 'A', 'AFSCMETitle', '13')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6014, '14', 'Vice-Chairman', 'A', 'AFSCMETitle', '14')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6015, '15', '1st Vice President', 'A', 'AFSCMETitle', '15')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6016, '16', '2nd Vice President', 'A', 'AFSCMETitle', '16')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6017, '17', '3rd Vice President', 'A', 'AFSCMETitle', '17')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6018, '18', '4th Vice President', 'A', 'AFSCMETitle', '18')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6019, '19', '5th Vice President', 'A', 'AFSCMETitle', '19')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6020, '20', 'Conference Board Vice President', 'A', 'AFSCMETitle', '20')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6021, '21', 'Regional Vice-President', 'A', 'AFSCMETitle', '21')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6022, '22', 'District Vice-President', 'A', 'AFSCMETitle', '22')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6023, '23', 'Department Vice-President', 'A', 'AFSCMETitle', '23')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6024, '24', 'Recording Secretary', 'A', 'AFSCMETitle', '24')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6025, '25', 'Secretary', 'A', 'AFSCMETitle', '25')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6026, '26', 'Corresponding Secretary', 'A', 'AFSCMETitle', '26')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6027, '27', 'Assistant Secretary', 'A', 'AFSCMETitle', '27')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6028, '28', 'Secretary Treasurer', 'A', 'AFSCMETitle', '28')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6029, '29', 'Treasurer', 'A', 'AFSCMETitle', '29')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6030, '30', 'Assistant Treasurer', 'A', 'AFSCMETitle', '30')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6031, '31', 'Financial Secretary', 'A', 'AFSCMETitle', '31')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6032, '32', 'Financial Reporting Officer', 'A', 'AFSCMETitle', '32')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6033, '33', 'Executive Board Member', 'A', 'AFSCMETitle', '33')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6034, '34', 'Board of Directors Member', 'A', 'AFSCMETitle', '34')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6035, '35', 'District Board Member', 'A', 'AFSCMETitle', '35')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6036, '36', 'Regional Board Member', 'A', 'AFSCMETitle', '36')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6037, '37', 'Department Board Member', 'A', 'AFSCMETitle', '37')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6038, '38', 'Conference Board Member', 'A', 'AFSCMETitle', '38')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6039, '39', 'Chief Steward', 'A', 'AFSCMETitle', '39')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6040, '40', 'Sergeant-At-Arms', 'A', 'AFSCMETitle', '40')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6041, '41', 'Trustee', 'A', 'AFSCMETitle', '41')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6042, '42', 'Past President', 'A', 'AFSCMETitle', '42')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6043, '43', 'Business Agent', 'A', 'AFSCMETitle', '43')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6044, '44', 'Business Representative', 'A', 'AFSCMETitle', '44')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6045, '45', 'Parliamentarian', 'A', 'AFSCMETitle', '45')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6046, '46', 'Steward', 'A', 'AFSCMETitle', '46')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6047, '47', 'Chapter Chairperson', 'A', 'AFSCMETitle', '47')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6048, '48', 'Chapter Secretary', 'A', 'AFSCMETitle', '48')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6049, '49', 'Chapter Treasurer', 'A', 'AFSCMETitle', '49')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6050, '50', 'Business Manager', 'A', 'AFSCMETitle', '50')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6051, '51', 'Judicial Panel Chairperson', 'A', 'AFSCMETitle', '51')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(6052, '52', 'Judicial Panel Member', 'A', 'AFSCMETitle', '52')

---
--- Method of Officer Election
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Method of Officer Election',         'Method of Officer Election' , 'A',         @category_id, 'MethodOffcrElection'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(51001, '1', 'Mail/Ballot', 'A', 'MethodOffcrElection', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(51002, '2', 'Not Specified', 'A', 'MethodOffcrElection', '2')

---
--- Length of Officer Terms
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Length of Officer Terms',         'Length of Officer Terms' , 'A',         @category_id, 'TermLength'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(63001, '1', '1 year', 'A', 'TermLength', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(63002, '2', '2 years', 'A', 'TermLength', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(63003, '3', '3 years', 'A', 'TermLength', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(63004, '4', '4 years', 'A', 'TermLength', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(63005, '5', 'Indefinite', 'A', 'TermLength', '5')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(63006, '6', 'Temporary', 'A', 'TermLength', '6')

---
--- Month of Officer Election
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Month of Officer Election',         'Month of Officer Election which includes 1-4 Quarters' , 'A',         @category_id, 'MonthOffcrElection'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(78001, '1', 'January', 'A', 'MonthOffcrElection', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(78002, '2', 'February', 'A', 'MonthOffcrElection', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(78003, '3', 'March', 'A', 'MonthOffcrElection', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(78004, '4', 'April', 'A', 'MonthOffcrElection', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(78005, '5', 'May', 'A', 'MonthOffcrElection', '5')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(78006, '6', 'June', 'A', 'MonthOffcrElection', '6')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(78007, '7', 'July', 'A', 'MonthOffcrElection', '7')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(78008, '8', 'August', 'A', 'MonthOffcrElection', '8')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(78009, '9', 'September', 'A', 'MonthOffcrElection', '9')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(78010, '10', 'October', 'A', 'MonthOffcrElection', '10')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(78011, '11', 'November', 'A', 'MonthOffcrElection', '11')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(78012, '12', 'December', 'A', 'MonthOffcrElection', '12')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(78013, '13', '1st Qtr.', 'A', 'MonthOffcrElection', '13')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(78014, '14', '2nd Qtr.', 'A', 'MonthOffcrElection', '14')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(78015, '15', '3rd Qtr.', 'A', 'MonthOffcrElection', '15')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(78016, '16', '4th Qtr.', 'A', 'MonthOffcrElection', '16')

---
--- Member Codes
---
INSERT INTO Common_Code_Category
VALUES ('Member', 'Member Codes', 'A')
set @category_id = @@identity
---
--- Member Type
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Member Type',         'The type of a member' , 'A',         @category_id, 'MemberType'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(29001, 'R', 'Regular', 'A', 'MemberType', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(29002, 'T', 'Retiree', 'A', 'MemberType', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(29003, 'A', 'Agency Fee Payer', 'A', 'MemberType', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(29004, 'U', 'Union Shop', 'A', 'MemberType', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(29005, 'O', 'Union Shop Objector', 'A', 'MemberType', '5')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(29006, 'S', 'Retiree Spouse', 'A', 'MemberType', '6')

---
--- Activity Type
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Activity Type',         'Supports the Memembership and Officer activity tables' , 'A',         @category_id, 'ActivityType'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(30001, 'A', 'Add', 'A', 'ActivityType', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(30002, 'U', 'Update', 'A', 'ActivityType', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(30003, 'D', 'Deactivate', 'A', 'ActivityType', '3')

---
--- Member Status
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Member Status',         'Member Status' , 'A',         @category_id, 'MemberStatus'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(31001, 'A', 'Active', 'A', 'MemberStatus', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(31002, 'I', 'Inactive', 'A', 'MemberStatus', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(31003, 'T', 'Temporary', 'A', 'MemberStatus', '3')

---
--- Dues Frequency
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Dues Frequency',         'Frequency that Dues are paid' , 'A',         @category_id, 'DuesFrequency'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(41001, '1', 'Weekly', 'A', 'DuesFrequency', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(41002, '2', 'Bi-weekly', 'A', 'DuesFrequency', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(41003, '3', 'Semi-monthly', 'A', 'DuesFrequency', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(41004, '4', 'Monthly', 'A', 'DuesFrequency', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(41005, '5', 'Quarterly', 'A', 'DuesFrequency', '5')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(41006, '6', 'Semi-annually', 'A', 'DuesFrequency', '6')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(41007, '7', 'Annually', 'A', 'DuesFrequency', '7')

---
--- Dues Type
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Dues Type',         'Type of Dues' , 'A',         @category_id, 'DuesType'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(42001, 'F', 'Full-time', 'A', 'DuesType', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(42002, 'P', 'Part-time', 'A', 'DuesType', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(42003, 'L', 'Lower Part-time', 'A', 'DuesType', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(42004, 'R', 'Retiree', 'A', 'DuesType', '4')

---
--- Affiliate Codes
---
INSERT INTO Common_Code_Category
VALUES ('Affiliate', 'Affiliate Codes', 'A')
set @category_id = @@identity
---
--- Affiliate States
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Affiliate States',         'Affiliate States' , 'A',         @category_id, 'AffiliateState'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5001, 'NT', 'National', 'A', 'AffiliateState', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5002, 'AK', 'Alaska', 'A', 'AffiliateState', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5003, 'AL', 'Alabama', 'A', 'AffiliateState', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5004, 'AR', 'Arkansas', 'A', 'AffiliateState', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5005, 'AZ', 'Arizona', 'A', 'AffiliateState', '5')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5006, 'CA', 'California', 'A', 'AffiliateState', '6')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5007, 'CO', 'Colorado', 'A', 'AffiliateState', '7')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5008, 'CT', 'Connecticut', 'A', 'AffiliateState', '8')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5009, 'DC', 'District of Columbia', 'A', 'AffiliateState', '9')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5010, 'DE', 'Delaware', 'A', 'AffiliateState', '10')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5011, 'FL', 'Florida', 'A', 'AffiliateState', '11')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5012, 'GA', 'Georgia', 'A', 'AffiliateState', '12')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5013, 'HI', 'Hawaii', 'A', 'AffiliateState', '13')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5014, 'IA', 'Iowa', 'A', 'AffiliateState', '14')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5015, 'ID', 'Idaho', 'A', 'AffiliateState', '15')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5016, 'IL', 'Illinois', 'A', 'AffiliateState', '16')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5017, 'IN', 'Indiana', 'A', 'AffiliateState', '17')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5018, 'KS', 'Kansas', 'A', 'AffiliateState', '18')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5019, 'KY', 'Kentucky', 'A', 'AffiliateState', '19')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5020, 'LA', 'Louisiana', 'A', 'AffiliateState', '20')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5021, 'MA', 'Massachusetts', 'A', 'AffiliateState', '21')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5022, 'MD', 'Maryland', 'A', 'AffiliateState', '22')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5023, 'ME', 'Maine', 'A', 'AffiliateState', '23')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5024, 'MI', 'Michigan', 'A', 'AffiliateState', '24')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5025, 'MN', 'Minnesota', 'A', 'AffiliateState', '25')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5026, 'MO', 'Missouri', 'A', 'AffiliateState', '26')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5027, 'MS', 'Mississippi', 'A', 'AffiliateState', '27')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5028, 'MT', 'Montana', 'A', 'AffiliateState', '28')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5029, 'NC', 'North Carolina', 'A', 'AffiliateState', '29')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5030, 'ND', 'North Dakota', 'A', 'AffiliateState', '30')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5031, 'NE', 'Nebraska', 'A', 'AffiliateState', '31')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5032, 'NH', 'New Hampshire', 'A', 'AffiliateState', '32')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5033, 'NJ', 'New Jersey', 'A', 'AffiliateState', '33')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5034, 'NM', 'New Mexico', 'A', 'AffiliateState', '34')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5035, 'NV', 'Nevada', 'A', 'AffiliateState', '35')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5036, 'NY', 'New York', 'A', 'AffiliateState', '36')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5037, 'OH', 'Ohio', 'A', 'AffiliateState', '37')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5038, 'OK', 'Oklahoma', 'A', 'AffiliateState', '38')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5039, 'OR', 'Oregon', 'A', 'AffiliateState', '39')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5040, 'PA', 'Pennsylvania', 'A', 'AffiliateState', '40')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5041, 'PR', 'Puerto Rico', 'A', 'AffiliateState', '41')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5042, 'RI', 'Rhode Island', 'A', 'AffiliateState', '42')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5043, 'SC', 'South Carolina', 'A', 'AffiliateState', '43')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5044, 'SD', 'South Dakota', 'A', 'AffiliateState', '44')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5045, 'TN', 'Tennessee', 'A', 'AffiliateState', '45')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5046, 'TX', 'Texas', 'A', 'AffiliateState', '46')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5047, 'UT', 'Utah', 'A', 'AffiliateState', '47')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5048, 'VA', 'Virginia', 'A', 'AffiliateState', '48')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5049, 'VT', 'Vermont', 'A', 'AffiliateState', '49')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5050, 'WA', 'Washington', 'A', 'AffiliateState', '50')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5051, 'WI', 'Wisconsin', 'A', 'AffiliateState', '51')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5052, 'WV', 'West Virginia', 'A', 'AffiliateState', '52')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5053, 'WY', 'Wyoming', 'A', 'AffiliateState', '53')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(5054, 'XX', 'CASH RECEIPTS', 'A', 'AffiliateState', '54')

---
--- Affiliate Status
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Affiliate Status',         'Affiliate Status Codes' , 'A',         @category_id, 'AffiliateStatus'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(17001, 'AC', 'Administrative/Legislative Council', 'A', 'AffiliateStatus', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(17002, 'C', 'Chartered', 'A', 'AffiliateStatus', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(17003, 'CU', 'Certified', 'A', 'AffiliateStatus', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(17004, 'D', 'Deactivated', 'A', 'AffiliateStatus', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(17005, 'DU', 'Decertified', 'A', 'AffiliateStatus', '5')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(17006, 'M', 'Merged', 'A', 'AffiliateStatus', '6')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(17007, 'N', 'Not Chartered', 'A', 'AffiliateStatus', '7')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(17008, 'PC', 'Pending Charter', 'A', 'AffiliateStatus', '8')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(17009, 'PD', 'Pending Deactivation', 'A', 'AffiliateStatus', '9')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(17010, 'RA', 'Restricted Administratorship', 'A', 'AffiliateStatus', '10')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(17011, 'S', 'Split', 'A', 'AffiliateStatus', '11')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(17012, 'UA', 'Unrestricted Administratorship', 'A', 'AffiliateStatus', '12')

---
--- Charter Codes
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Charter Codes',         'Reason Affiliates Chartered' , 'A',         @category_id, 'CharterCode'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(18001, '1', 'New ', 'A', 'CharterCode', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(18002, '2', 'Split', 'A', 'CharterCode', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(18003, '3', 'Merge', 'A', 'CharterCode', '3')

---
--- Legislative District
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Legislative District',         'Legislative District' , 'A',         @category_id, 'LegislativeDistrict'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(21001, '01', 'Hawaiian District ', 'A', 'LegislativeDistrict', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(21002, '02', 'Northwestern District', 'A', 'LegislativeDistrict', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(21003, '03', 'California District', 'A', 'LegislativeDistrict', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(21004, '04', 'Southwestern District', 'A', 'LegislativeDistrict', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(21005, '05', 'North-Central District', 'A', 'LegislativeDistrict', '5')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(21006, '06', 'Midwestern District', 'A', 'LegislativeDistrict', '6')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(21007, '07', 'Wisconsin District', 'A', 'LegislativeDistrict', '7')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(21008, '08', 'Michigan District', 'A', 'LegislativeDistrict', '8')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(21009, '09', 'Illinois District', 'A', 'LegislativeDistrict', '9')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(21010, '10', 'Central District', 'A', 'LegislativeDistrict', '10')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(21011, '11', 'Ohio District', 'A', 'LegislativeDistrict', '11')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(21012, '12', 'Northern New England District', 'A', 'LegislativeDistrict', '12')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(21013, '13', 'Southern New England District', 'A', 'LegislativeDistrict', '13')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(21014, '14', 'New York City District', 'A', 'LegislativeDistrict', '14')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(21015, '15', 'Civil Service Employees Assoc.', 'A', 'LegislativeDistrict', '15')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(21016, '16', 'New York State District', 'A', 'LegislativeDistrict', '16')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(21017, '17', 'Pennsylvania District', 'A', 'LegislativeDistrict', '17')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(21018, '18', 'Eastern District', 'A', 'LegislativeDistrict', '18')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(21019, '19', 'Capital District', 'A', 'LegislativeDistrict', '19')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(21020, '20', 'Southern District', 'A', 'LegislativeDistrict', '20')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(21021, '21', 'Carribean District', 'A', 'LegislativeDistrict', '21')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(21022, '22', 'Ohio CSEA District', 'A', 'LegislativeDistrict', '22')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(21023, '23', 'OAPSE District', 'A', 'LegislativeDistrict', '23')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(21024, '24', 'NUHHCE District', 'A', 'LegislativeDistrict', '24')

---
--- Mass Change
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Mass Change',         'Mass Change' , 'A',         @category_id, 'MassChange'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(22001, 'IS', 'Membership Information Reporting Source ', 'A', 'MassChange', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(22002, 'MR', 'Member Renewal ', 'A', 'MassChange', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(22003, 'NA', 'New Affiliate Identifier', 'A', 'MassChange', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(22004, 'NC', 'Unit Wide No Member Cards ', 'A', 'MassChange', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(22005, 'NP', 'Unit Wide No PE Mail ', 'A', 'MassChange', '5')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(22006, 'SD', 'Status Change - Deactivated', 'A', 'MassChange', '6')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(22007, 'SM', 'Status Change - Merged', 'A', 'MassChange', '7')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(22008, 'SS', 'Status Change - Split', 'A', 'MassChange', '8')

---
--- Member Renewal
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Member Renewal',         'Member Renewal' , 'A',         @category_id, 'MemberRenewal'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(23001, '1', 'Checkoff', 'A', 'MemberRenewal', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(23002, '2', 'Annual Renewal', 'A', 'MemberRenewal', '2')

---
--- Affiliate Card Run Type
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Affiliate Card Run Type',         'Type of Card Run for an Affiliate' , 'A',         @category_id, 'AffiliateCardRunType'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(25001, '1', 'Regular Members', 'A', 'AffiliateCardRunType', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(25002, '2', 'Retiree Members', 'A', 'AffiliateCardRunType', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(25003, '3', 'CSEA Regular Members', 'A', 'AffiliateCardRunType', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(25004, '4', 'CSEA Retiree Members', 'A', 'AffiliateCardRunType', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(25005, '5', 'Puerto Rico Members', 'A', 'AffiliateCardRunType', '5')

---
--- Affiliate Types
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Affiliate Types',         'Affiliate Types' , 'A',         @category_id, 'AffiliateType'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(27001, 'C', 'Council', 'A', 'AffiliateType', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(27002, 'L', 'Local', 'A', 'AffiliateType', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(27003, 'U', 'Sub Local', 'A', 'AffiliateType', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(27004, 'R', 'Retiree Chapter', 'A', 'AffiliateType', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(27005, 'S', 'Retiree Sub Chapter', 'A', 'AffiliateType', '5')

---
--- Affiliate Staff Title
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Affiliate Staff Title',         'Affiliate Staff Title' , 'A',         @category_id, 'AffStaffTitle'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(32001, 'ES1', 'Engineer Specialist', 'A', 'AffStaffTitle', '1')

---
--- Affiliate Sections
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Affiliate Sections',         'Sections that are used for recording and searching Affiliate Change History' , 'A',         @category_id, 'AffiliateSections'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(39001, '1', 'Detail', 'A', 'AffiliateSections', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(39002, '2', 'Location', 'A', 'AffiliateSections', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(39003, '3', 'Charter', 'A', 'AffiliateSections', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(39004, '4', 'Constitution', 'A', 'AffiliateSections', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(39005, '5', 'Officer Titles', 'A', 'AffiliateSections', '5')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(39006, '6', 'Financial', 'A', 'AffiliateSections', '6')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(39007, '7', 'Membership Reporting', 'A', 'AffiliateSections', '7')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(39008, '8', 'Affiliate Identifier', 'A', 'AffiliateSections', '8')

---
--- Primary Information Source
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Primary Information Source',         'Primary Information Source' , 'A',         @category_id, 'InformationSource'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(47001, '1', 'INTERNATIONAL', 'A', 'InformationSource', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(47002, '2', 'COUNCIL (MANUAL)', 'A', 'InformationSource', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(47003, '3', 'LOCAL (MANUAL)', 'A', 'InformationSource', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(47004, '4', 'TAPE', 'A', 'InformationSource', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(47005, '5', 'CD', 'A', 'InformationSource', '5')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(47006, '6', 'Diskette', 'A', 'InformationSource', '6')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(47007, '7', 'FTP', 'A', 'InformationSource', '7')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(47008, '8', 'Website', 'A', 'InformationSource', '8')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(47009, '9', 'OTHER', 'A', 'InformationSource', '9')

---
--- AFSCME Regions
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('AFSCME Regions',         'AFSCME Affiliate Regions' , 'A',         @category_id, 'Region'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(61001, '1', 'East', 'A', 'Region', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(61002, '2', 'Central', 'A', 'Region', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(61003, '3', 'West', 'A', 'Region', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(61004, '4', 'Various', 'A', 'Region', '4')

---
--- Change History Fields
---
INSERT INTO Common_Code_Type
(com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key)
VALUES
('Change History Fields',         'Fields that are recorded for change history' , 'A',         @category_id, 'ChangeHistoryFields'         )
INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64001, '1', 'Abbreviated Affiliate Name', 'A', 'ChangeHistoryFields', '1')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64002, '2', 'AFSCME Legislative District', 'A', 'ChangeHistoryFields', '2')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64003, '3', 'AFSCME Region', 'A', 'ChangeHistoryFields', '3')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64004, '4', 'Multiple Employers', 'A', 'ChangeHistoryFields', '4')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64005, '5', 'Employer Sector', 'A', 'ChangeHistoryFields', '5')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64006, '6', 'Sub-Locals Allowed', 'A', 'ChangeHistoryFields', '6')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64007, '7', 'Multiple Offices', 'A', 'ChangeHistoryFields', '7')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64008, '8', 'Annual Card Run Type', 'A', 'ChangeHistoryFields', '8')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64009, '9', 'Member Renewal', 'A', 'ChangeHistoryFields', '9')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64010, '10', 'Affiliate Website', 'A', 'ChangeHistoryFields', '10')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64011, '11', 'Charter Name', 'A', 'ChangeHistoryFields', '11')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64012, '12', 'Charter Jurisdiction', 'A', 'ChangeHistoryFields', '12')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64013, '13', 'Charter Code', 'A', 'ChangeHistoryFields', '13')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64014, '14', 'Last Change Effective Date', 'A', 'ChangeHistoryFields', '14')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64015, '15', 'Charter Date', 'A', 'ChangeHistoryFields', '15')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64016, '16', 'Counties', 'A', 'ChangeHistoryFields', '16')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64017, '17', 'Council Affiliations', 'A', 'ChangeHistoryFields', '17')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64018, '18', 'Most Current Approval Date', 'A', 'ChangeHistoryFields', '18')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64019, '19', 'Affiliation Agreement Date', 'A', 'ChangeHistoryFields', '19')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64020, '20', 'Method of Officer Election', 'A', 'ChangeHistoryFields', '20')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64021, '21', 'Constitutional Regions', 'A', 'ChangeHistoryFields', '21')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64022, '22', 'Meeting Frequency for Affiliate Meetings', 'A', 'ChangeHistoryFields', '22')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64023, '23', 'Employer Identification Number', 'A', 'ChangeHistoryFields', '23')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64024, '24', 'Title of Location', 'A', 'ChangeHistoryFields', '24')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64025, '25', 'Primary Flag', 'A', 'ChangeHistoryFields', '25')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64026, '26', 'Main Address Attention', 'A', 'ChangeHistoryFields', '26')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64027, '27', 'Main Address 1', 'A', 'ChangeHistoryFields', '27')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64028, '28', 'Main Address 2', 'A', 'ChangeHistoryFields', '28')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64029, '29', 'Main Address City', 'A', 'ChangeHistoryFields', '29')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64030, '30', 'Main Address State', 'A', 'ChangeHistoryFields', '30')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64031, '31', 'Main Address Zip Code', 'A', 'ChangeHistoryFields', '31')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64032, '32', 'Main Address Zip + 4', 'A', 'ChangeHistoryFields', '32')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64033, '33', 'Main Address County', 'A', 'ChangeHistoryFields', '33')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64034, '34', 'Main Address Province', 'A', 'ChangeHistoryFields', '34')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64035, '35', 'Main Address Country', 'A', 'ChangeHistoryFields', '35')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64036, '36', 'Main Address Bad Flag', 'A', 'ChangeHistoryFields', '36')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64037, '37', 'Main Address Date Marked Bad', 'A', 'ChangeHistoryFields', '37')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64038, '38', 'Ship To Address Attention', 'A', 'ChangeHistoryFields', '38')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64039, '39', 'Ship To Address 1', 'A', 'ChangeHistoryFields', '39')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64040, '40', 'Ship To Address 2', 'A', 'ChangeHistoryFields', '40')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64041, '41', 'Ship To Address City', 'A', 'ChangeHistoryFields', '41')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64042, '42', 'Ship To Address State', 'A', 'ChangeHistoryFields', '42')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64043, '43', 'Ship To Address Zip Code', 'A', 'ChangeHistoryFields', '43')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64044, '44', 'Ship To Address Zip + 4', 'A', 'ChangeHistoryFields', '44')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64045, '45', 'Ship To Address County', 'A', 'ChangeHistoryFields', '45')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64046, '46', 'Ship To Address Province', 'A', 'ChangeHistoryFields', '46')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64047, '47', 'Ship To Address Country', 'A', 'ChangeHistoryFields', '47')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64048, '48', 'Ship To Address Bad Flag', 'A', 'ChangeHistoryFields', '48')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64049, '49', 'Ship To Address Date Marked Bad', 'A', 'ChangeHistoryFields', '49')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64050, '50', 'Office Country Code', 'A', 'ChangeHistoryFields', '50')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64051, '51', 'Office Area Code', 'A', 'ChangeHistoryFields', '51')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64052, '52', 'Office Phone Number', 'A', 'ChangeHistoryFields', '52')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64053, '53', 'Office Bad Address Flag', 'A', 'ChangeHistoryFields', '53')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64054, '54', 'Office Date Marked Bad', 'A', 'ChangeHistoryFields', '54')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64055, '55', 'Fax Country Code', 'A', 'ChangeHistoryFields', '55')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64056, '56', 'Fax Area Code', 'A', 'ChangeHistoryFields', '56')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64057, '57', 'Fax Phone Number', 'A', 'ChangeHistoryFields', '57')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64058, '58', 'Fax Bad Address Flag', 'A', 'ChangeHistoryFields', '58')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64059, '59', 'Fax Date Marked Bad', 'A', 'ChangeHistoryFields', '59')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64060, '60', 'Affiliate Type', 'A', 'ChangeHistoryFields', '60')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64061, '61', 'Local/Sub Chapter', 'A', 'ChangeHistoryFields', '61')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64062, '62', 'State/National Type', 'A', 'ChangeHistoryFields', '62')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64063, '63', 'Sub Unit', 'A', 'ChangeHistoryFields', '63')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64064, '64', 'Council/Retiree Chapter', 'A', 'ChangeHistoryFields', '64')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64065, '65', 'Affiliate Status', 'A', 'ChangeHistoryFields', '65')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64066, '66', 'Unit Wide No Member Cards', 'A', 'ChangeHistoryFields', '66')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64067, '67', 'Unit Wide No PE Mail', 'A', 'ChangeHistoryFields', '67')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64068, '68', 'New Affiliate Identifier Source', 'A', 'ChangeHistoryFields', '68')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64069, '69', 'Membership Information Reporting Source', 'A', 'ChangeHistoryFields', '69')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64070, '70', 'Affiliate Title', 'A', 'ChangeHistoryFields', '70')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64071, '71', '# with Title', 'A', 'ChangeHistoryFields', '71')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64072, '72', 'Month of Election', 'A', 'ChangeHistoryFields', '72')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64073, '73', 'Length of Term', 'A', 'ChangeHistoryFields', '73')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64074, '74', 'Term End', 'A', 'ChangeHistoryFields', '74')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64075, '75', 'Delegate Priority', 'A', 'ChangeHistoryFields', '75')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64076, '76', 'Reporting Officer', 'A', 'ChangeHistoryFields', '76')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64077, '77', 'E-Bd', 'A', 'ChangeHistoryFields', '77')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64078, '78', 'Auto E-Board Affiliate Title', 'A', 'ChangeHistoryFields', '78')

INSERT INTO Common_Codes (com_cd_pk, com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key)
VALUES(64079, '79', 'Auto E-Board Sub-Affiliate Title', 'A', 'ChangeHistoryFields', '79')

SET IDENTITY_INSERT Common_Codes OFF
