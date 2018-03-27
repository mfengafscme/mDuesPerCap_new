DECLARE @role_pk int

--create a super user role
SET @role_pk = (SELECT role_pk FROM Roles WHERE role_name = 'Super User')
INSERT INTO Roles_Privileges (role_pk, privilege_key) SELECT @role_pk, privilege_key FROM Privileges

SET @role_pk = (SELECT role_pk FROM Roles WHERE role_name = 'Data Utility User')
INSERT INTO Roles_Privileges (role_pk, privilege_key) SELECT @role_pk, privilege_key FROM Privileges
	WHERE privilege_is_data_utility = 1

SET @role_pk = (SELECT role_pk FROM Roles WHERE role_name = 'Vendor')
INSERT INTO Roles_Privileges (role_pk, privilege_key) SELECT @role_pk, privilege_key FROM Privileges
	WHERE privilege_key = 'VendorMemberSearch'
	
SET @role_pk = (SELECT role_pk FROM Roles WHERE role_name = 'Membership Staff')
INSERT INTO Roles_Privileges (role_pk, privilege_key) SELECT @role_pk, privilege_key FROM Privileges
	WHERE privilege_is_data_utility = 0

-- Create the full Organtization Name
UPDATE o
   SET o.org_nm = Coalesce(FirstName, '') + ' ' + Coalesce(Substring(LastName,1,4), '')
 FROM External_Organizations o
 JOIN DM_migrateOrg m ON m.org_pk = o.org_pk