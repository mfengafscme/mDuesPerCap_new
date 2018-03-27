-- =============================================
-- Database Patch for Officer Apply Update
-- =============================================

-- If data exists in the Officer Summary table, 
-- remove the constraint to the queue table and other associated tables,
-- delete the Officer related queue values in the queue and associated tables
-- and recreated the constraints
IF (SELECT Count(*) FROM AUP_Officer_Pre_Upd_Smry) > 0 
BEGIN
	-- Remove constraint to the AUP_Queue_Management table
	ALTER TABLE [dbo].[AUP_Officer_Pre_Upd_Smry] 
	DROP CONSTRAINT [FK__AUP_Offic__queue__3A81B327]
	
	ALTER TABLE [dbo].[AUP_Pre_Fld_Chg_Smry] 
	DROP CONSTRAINT [FK__AUP_Pre_F__queue__4222D4EF]

	-- Remove Officer values from Field table
	DELETE --select *
	FROM AUP_Pre_Fld_Chg_Smry
	WHERE queue_pk IN (SELECT distinct queue_pk 
	FROM AUP_Officer_Pre_Upd_Smry)

	ALTER TABLE [dbo].[AUP_Rv_Smry] 
	DROP CONSTRAINT [FK__AUP_Rv_Sm__queue__4589517F]

	ALTER TABLE [dbo].[AUP_Rv_Err_Smry] 
	DROP CONSTRAINT [FK__AUP_Rv_Err_Smry__4A4E069C]

	ALTER TABLE [dbo].[AUP_Rv_Err_Dtl] 
	DROP CONSTRAINT [FK__AUP_Rv_Err_Dtl__4D2A7347]

	ALTER TABLE [dbo].[AUP_Pre_Err_Smry] 
	DROP CONSTRAINT [FK__AUP_Pre_E__queue__3DE82FB7]

	ALTER TABLE [dbo].[AUP_Pre_Err_Dtl] 
	DROP CONSTRAINT [FK__AUP_Pre_Err_Dtl__42ACE4D4]

	-- Remove Officer values from Pre Error Detail table
	DELETE --select *
	FROM AUP_Pre_Err_Dtl
	WHERE queue_pk IN (SELECT distinct queue_pk 
	FROM AUP_Officer_Pre_Upd_Smry)

	-- Remove Officer values from Pre Error table
	DELETE --select *
	FROM AUP_Pre_Err_Smry
	WHERE queue_pk IN (SELECT distinct queue_pk 
	FROM AUP_Officer_Pre_Upd_Smry)

	-- Remove Officer values from Review Error Detail table
	DELETE --select *
	FROM AUP_Rv_Err_Dtl
	WHERE queue_pk IN (SELECT distinct queue_pk 
	FROM AUP_Officer_Pre_Upd_Smry)

	-- Remove Officer values from Review Error table
	DELETE --select *
	FROM AUP_Rv_Err_Smry
	WHERE queue_pk IN (SELECT distinct queue_pk 
	FROM AUP_Officer_Pre_Upd_Smry)

	-- Remove Officer values from Review table
	DELETE --select *
	FROM AUP_Rv_Smry
	WHERE queue_pk IN (SELECT distinct queue_pk 
	FROM AUP_Officer_Pre_Upd_Smry)

	-- Remove Officer values from Queue table
	DELETE --select *
	FROM AUP_Queue_Mgmt
	WHERE queue_pk IN (SELECT distinct queue_pk 
	FROM AUP_Officer_Pre_Upd_Smry)

	-- Recreate the Field table constraint
	ALTER TABLE [dbo].[AUP_Pre_Fld_Chg_Smry] ADD  FOREIGN KEY 
		(
			[queue_pk]
		) REFERENCES [AUP_Queue_Mgmt] (
			[queue_pk]
		)

	-- Recreate the Review table constraint
	ALTER TABLE [dbo].[AUP_Rv_Smry] ADD  FOREIGN KEY 
		(
			[queue_pk]
		) REFERENCES [AUP_Queue_Mgmt] (
			[queue_pk]
		)
	
	-- Recreate the Review Error table constraint
	ALTER TABLE [dbo].[AUP_Rv_Err_Smry] ADD  FOREIGN KEY 
		(
			[aff_pk],
			[queue_pk]
		) REFERENCES [AUP_Rv_Smry] (
			[aff_pk],
			[queue_pk]
		)

	-- Recreate the Review Error Detail table constraint
	ALTER TABLE [dbo].[AUP_Rv_Err_Dtl] ADD  FOREIGN KEY 
		(
			[aff_pk],
			[queue_pk],
			[record_id]
		) REFERENCES [AUP_Rv_Err_Smry] (
			[aff_pk],
			[queue_pk],
			[record_id]
		)

	-- Recreate the Pre Error Summary table constraint
	ALTER TABLE [dbo].[AUP_Pre_Err_Smry] ADD  FOREIGN KEY 
		(
			[queue_pk]
		) REFERENCES [AUP_Queue_Mgmt] (
			[queue_pk]
		)

	-- Recreate the Pre Error Detail table constraint
	ALTER TABLE [dbo].[AUP_Pre_Err_Dtl] ADD  FOREIGN KEY 
		(
			[aff_pk],
			[queue_pk],
			[record_id]
		) REFERENCES [AUP_Pre_Err_Smry] (
			[aff_pk],
			[queue_pk],
			[record_id]
		)

END
GO

-- Drop table AUP_Officer_Pre_Off_Dtl
DROP TABLE AUP_Officer_Pre_Off_Dtl
go

-- Create table AUP_Officer_Pre_Off_Dtl
 CREATE TABLE AUP_Officer_Pre_Off_Dtl (
        aff_pk               int NOT NULL,
        queue_pk             int NOT NULL,
        afscme_office_pk     int NOT NULL,
        office_group_id      int NOT NULL,
        officers_allowed_num int NOT NULL,
        officers_in_file_cnt int NOT NULL,
        PRIMARY KEY NONCLUSTERED (aff_pk, queue_pk, afscme_office_pk, 
               office_group_id), 
        FOREIGN KEY (queue_pk)
                              REFERENCES AUP_Queue_Mgmt, 
        FOREIGN KEY (aff_pk)
                              REFERENCES Aff_Organizations
 )
go
 
-- Drop table AUP_Officer_Pre_Upd_Smry
DROP TABLE AUP_Officer_Pre_Upd_Smry
go

-- Create table AUP_Officer_Pre_Upd_Smry
CREATE TABLE AUP_Officer_Pre_Upd_Smry (

        queue_pk             int NOT NULL,
        aff_id               int IDENTITY,
        aff_pk               int NULL,
        off_system_cnt       int NOT NULL,
        off_file_cnt         int NOT NULL,
        off_replaced_cnt     int NOT NULL,
        off_chg_cnt          int NOT NULL,
        off_vacant_cnt       int NOT NULL,
        off_cards_cnt        int NOT NULL,
        off_add_cnt          int NOT NULL,
        aff_error_fg         fg,
        aff_warning_fg       fg,
        aff_err_aff_type     char(1) NULL,
        aff_err_aff_localSubChapter varchar(4) NULL,
        aff_err_aff_stateNat_type varchar(2) NULL,
        aff_err_aff_subUnit  varchar(4) NULL,
        aff_err_aff_councilRetiree_chap varchar(4) NULL, 
        PRIMARY KEY NONCLUSTERED (queue_pk, aff_id),
        FOREIGN KEY (aff_pk)
                              REFERENCES Aff_Organizations, 
        FOREIGN KEY (queue_pk)
                              REFERENCES AUP_Queue_Mgmt
 )
go
 
 CREATE INDEX XIF204AUP_Officer_Pre_Off_Dtl ON AUP_Officer_Pre_Off_Dtl
 (
        aff_pk
 )
go
 
 CREATE INDEX XIF205AUP_Officer_Pre_Off_Dtl ON AUP_Officer_Pre_Off_Dtl
 (
        queue_pk
 )
go
 
 CREATE INDEX XIE1AUP_Officer_Pre_Off_Dtl ON AUP_Officer_Pre_Off_Dtl
 (
        aff_pk,
        queue_pk
 )
go
 
 
 CREATE INDEX XIF133AUP_Officer_Pre_Upd_Smry ON AUP_Officer_Pre_Upd_Smry
 (
        queue_pk
 )
go
 
 CREATE INDEX XIF134AUP_Officer_Pre_Upd_Smry ON AUP_Officer_Pre_Upd_Smry
 (
        aff_pk
 )
go

-- Remove contraint between Officer History and Aff_Members.  Not all officers
-- must be a member. For non-elected offices, an AFSCME or Affiliate Staff can
-- hold the office.

ALTER TABLE [dbo].[Officer_History] DROP CONSTRAINT [FK__Officer_History__625A9A57]

GO

-- Fix typo in the Roles table
UPDATE roles
SET role_description = 'Has access to all the Data Utility functions'
WHERE role_pk = 2


-- Remove these unnecessary privileges
-- Disassoicate privileges from roles
DELETE
FROM Roles_Privileges
WHERE privilege_key = 'DataUtilityViewOfficerDetail'
OR privilege_key = 'DataUtilityMaintainOfficerDetail'
GO 

-- Remove from the privilege table
DELETE
FROM Privileges
WHERE privilege_key = 'DataUtilityViewOfficerDetail'
OR privilege_key = 'DataUtilityMaintainOfficerDetail'
GO

-- Add privileges for the Officer Maintenance under the VDU
if not exists (select * FROM Privileges WHERE privilege_key = 'DataUtilityViewAffiliateOfficers')
	INSERT INTO Privileges 
	(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
	VALUES('DataUtilityViewAffiliateOfficers','Officers','V',1,'A')
GO
if not exists (select * FROM Privileges WHERE privilege_key = 'DataUtilityMaintainAffiliateOfficers')
	INSERT INTO Privileges 
	(privilege_key, privilege_name, privilege_verb, privilege_is_data_utility, privilege_category)
	VALUES('DataUtilityMaintainAffiliateOfficers','Officers','E',1,'A') 
GO

