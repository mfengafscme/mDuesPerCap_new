DECLARE @role_pk int

--create a super user role
INSERT INTO Roles VALUES
('Super User', 'Role with all privileges in the system')
SET @role_pk = @@identity
INSERT INTO Roles_Privileges (role_pk, privilege_key) SELECT @role_pk, privilege_key FROM Privileges
INSERT INTO Roles_Reports (role_pk, report_pk) SELECT @role_pk, report_pk FROM Report WHERE report_custom_fg=0
INSERT INTO Roles_Report_Fields (role_pk, report_field_pk) SELECT @role_pk, report_field_pk FROM Report_Fields

INSERT INTO Roles VALUES
('Data Utility User', 'Has access to all the Data Utiltiy functions')
SET @role_pk = @@identity
INSERT INTO Roles_Privileges (role_pk, privilege_key) SELECT @role_pk, privilege_key FROM Privileges
	WHERE privilege_is_data_utility = 1

INSERT INTO Roles VALUES
('Vendor', 'Standard role for vendors')
SET @role_pk = @@identity
INSERT INTO Roles_Privileges (role_pk, privilege_key) SELECT @role_pk, privilege_key FROM Privileges
	WHERE privilege_key = 'SearchVendorMember'
	
INSERT INTO Roles VALUES
('Membership Staff', 'Standard role for AFSCME Membership Staff')
SET @role_pk = @@identity
INSERT INTO Roles_Privileges (role_pk, privilege_key) SELECT @role_pk, privilege_key FROM Privileges
	WHERE privilege_is_data_utility = 0
