if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[DM_Affiliate_RO]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[DM_Affiliate_RO]
GO

CREATE TABLE [dbo].[DM_Affiliate_RO] (
	[aff_pk] [int] NOT NULL ,
	[officer_cd] [int] NOT NULL ,
	[officer_sequence] [int] NULL 
) ON [PRIMARY]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[DM_Affiliate_RO_Error]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[DM_Affiliate_RO_Error]
GO

CREATE TABLE [dbo].[DM_Affiliate_RO_Error] (
	[aff_pk] [int] NULL ,
	[officer_cd] [int] NULL ,
	[officer_sequence] [int] NULL 
) ON [PRIMARY]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[DM_migrateAffiliate_ERROR]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[DM_migrateAffiliate_ERROR]
GO

CREATE TABLE [dbo].[DM_migrateAffiliate_ERROR] (
	[leg_aff_pk] [int] NOT NULL ,
	[ERROR] varchar(255) NULL,
	[Affiliate_Type] [varchar] (255) NULL ,
	[State] [varchar] (255) NULL ,
	[CouncilRetiree_chap] [varchar] (255) NULL ,
	[aff_localSubChapter] [varchar] (255) NULL ,
	[SubLocal] [varchar] (255) NULL ,
	[AffiliateSequence] [varchar] (255) NULL ,
	[CharterStatus] [varchar] (255) NULL ,
	[NewUnitCode] [varchar] (255) NULL ,
	[InformationSource] [varchar] (255) NULL ,
	[UnitWideNoMail] [varchar] (255) NULL ,
	[OfficerCode] [varchar] (255) NULL ,
	[OfficerSequenceNumber] [varchar] (255) NULL ,
	[MthofElection] [varchar] (255) NULL ,
	[UnitName] [varchar] (255) NULL ,
	[PrimaryAddress] [varchar] (255) NULL ,
	[AuxiliaryAddress] [varchar] (255) NULL ,
	[City] [varchar] (255) NULL ,
	[StateofResidence] [varchar] (255) NULL ,
	[ZipCode] [varchar] (255) NULL ,
	[Plus4] [varchar] (255) NULL ,
	[MailableAddress] [varchar] (255) NULL ,
	[MassChangeFlag] [varchar] (255) NULL ,
	[UnitTelephone] [varchar] (255) NULL ,
	[Last_Update_DT] [varchar] (255) NULL 
) ON [PRIMARY]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[DM_migrateAffiliate]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [DM_migrateAffiliate]
GO

CREATE TABLE [DM_migrateAffiliate] (
	[leg_aff_pk] [int] NOT NULL ,
	[Affiliate_Type] [varchar] (255) NULL ,
	[State] [varchar] (255) NULL ,
	[CouncilRetiree_chap] [varchar] (255) NULL ,
	[aff_localSubChapter] [varchar] (255) NULL ,
	[SubLocal] [varchar] (255) NULL ,
	[AffiliateSequence] [varchar] (255) NULL ,
	[CharterStatus] [varchar] (255) NULL ,
	[NewUnitCode] [varchar] (255) NULL ,
	[InformationSource] [varchar] (255) NULL ,
	[UnitWideNoMail] [varchar] (255) NULL ,
	[OfficerCode] [varchar] (255) NULL ,
	[OfficerSequenceNumber] [varchar] (255) NULL ,
	[MthofElection] [varchar] (255) NULL ,
	[UnitName] [varchar] (255) NULL ,
	[PrimaryAddress] [varchar] (255) NULL ,
	[AuxiliaryAddress] [varchar] (255) NULL ,
	[City] [varchar] (255) NULL ,
	[StateofResidence] [varchar] (255) NULL ,
	[ZipCode] [varchar] (255) NULL ,
	[Plus4] [varchar] (255) NULL ,
	[MailableAddress] [varchar] (255) NULL ,
	[MassChangeFlag] [varchar] (255) NULL ,
	[UnitTelephone] [varchar] (255) NULL ,
	[Last_Update_DT] [varchar] (255) NULL,
	created_user_pk User_pk DEFAULT 10000002,
	created_dt datetime DEFAULT GetDate(),
	lst_mod_user_pk User_pk DEFAULT 10000002, 
	lst_mod_dt datetime DEFAULT GetDate() 
) ON [PRIMARY]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[DM_migrateOrg]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[DM_migrateOrg]
GO

CREATE TABLE [DM_migrateOrg] (
	[leg_person_pk] [int] NOT NULL IDENTITY(5000000,1), 
	[org_pk] [int] NULL,
	[MemberId] [varchar] (255) NULL ,
	[Affiliate_Type] [varchar] (255) NULL ,
	[Affiliate_State] [varchar] (255) NULL ,
	[Council] [varchar] (255) NULL ,
	[Affiliate_Local] [varchar] (255) NULL ,
	[SubLocal] [varchar] (255) NULL ,
	[List_Code] [varchar] (255) NULL ,
	[Social_Security] [varchar] (255) NULL ,
	[Gender] [varchar] (255) NULL ,
	[Salutation] [varchar] (255) NULL ,
	[LastName] [varchar] (255) NULL ,
	[FirstName] [varchar] (255) NULL ,
	[MiddleName] [varchar] (255) NULL ,
	[Suffix] [varchar] (255) NULL ,
	[MailingAddress1] [varchar] (255) NULL ,
	[MailingAddress2] [varchar] (255) NULL ,
	[City] [varchar] (255) NULL ,
	[State] [varchar] (255) NULL ,
	[ZipCode] [varchar] (255) NULL ,
	[Zip_4] [varchar] (255) NULL ,
	[Province] [varchar] (255) NULL ,
	[Country] [varchar] (255) NULL ,
	[AddressType] [varchar] (255) NULL ,
	[Mailable_Add_Flag] [varchar] (255) NULL ,
	[No_Mail_Flag] [varchar] (255) NULL ,
	[Email] [varchar] (255) NULL ,
	[PhoneNumber] [varchar] (255) NULL ,
	[StatusCode] [varchar] (255) NULL ,
	[DateJoined] [varchar] (255) NULL ,
	[Register_Voter] [varchar] (255) NULL ,
	[Political_Party] [varchar] (255) NULL ,
	[Information_Source] [varchar] (255) NULL ,
	[AffiliateId] [varchar] (255) NULL ,
	[UnitCode] [varchar] (255) NULL ,
	[AddressChange_Dt] [varchar] (255) NULL ,
	[AlternateMailName] [varchar] (255) NULL,
	created_user_pk User_pk DEFAULT 10000002,
	created_dt datetime DEFAULT GetDate(),
	lst_mod_user_pk User_pk DEFAULT 10000002, 
	lst_mod_dt datetime DEFAULT GetDate() 
) ON [PRIMARY]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'DM_migratePerson') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table DM_migratePerson
GO

CREATE TABLE DM_migratePerson (
	[leg_person_pk] [int] NOT NULL ,
	[MemberId] [varchar] (255) NULL ,
	[Affiliate_Type] [varchar] (255) NULL ,
	[Affiliate_State] [varchar] (255) NULL ,
	[Council] [varchar] (255) NULL ,
	[Affiliate_Local] [varchar] (255) NULL ,
	[SubLocal] [varchar] (255) NULL ,
	[List_Code] [varchar] (255) NULL ,
	[Social_Security] [varchar] (255) NULL ,
	[Gender] [varchar] (255) NULL ,
	[Salutation] [varchar] (255) NULL ,
	[LastName] [varchar] (255) NULL ,
	[FirstName] [varchar] (255) NULL ,
	[MiddleName] [varchar] (255) NULL ,
	[Suffix] [varchar] (255) NULL ,
	[MailingAddress1] [varchar] (255) NULL ,
	[MailingAddress2] [varchar] (255) NULL ,
	[City] [varchar] (255) NULL ,
	[State] [varchar] (255) NULL ,
	[ZipCode] [varchar] (255) NULL ,
	[Zip_4] [varchar] (255) NULL ,
	[Province] [varchar] (255) NULL ,
	[Country] [varchar] (255) NULL ,
	[AddressType] [varchar] (255) NULL ,
	[Mailable_Add_Flag] [varchar] (255) NULL ,
	[No_Mail_Flag] [varchar] (255) NULL ,
	[Email] [varchar] (255) NULL ,
	[PhoneNumber] [varchar] (255) NULL ,
	[StatusCode] [varchar] (255) NULL ,
	[DateJoined] [varchar] (255) NULL ,
	[Register_Voter] [varchar] (255) NULL ,
	[Political_Party] [varchar] (255) NULL ,
	[Information_Source] [varchar] (255) NULL ,
	[AffiliateId] [varchar] (255) NULL ,
	[UnitCode] [varchar] (255) NULL ,
	[AddressChange_Dt] [varchar] (255) NULL ,
	[AlternateMailName] [varchar] (255) NULL ,
	created_user_pk User_pk DEFAULT 10000002,
	created_dt datetime DEFAULT GetDate(),
	lst_mod_user_pk User_pk DEFAULT 10000002, 
	lst_mod_dt datetime DEFAULT GetDate()
) ON [PRIMARY]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'DM_migratePerson_ERROR') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table DM_migratePerson_ERROR
GO

CREATE TABLE DM_migratePerson_ERROR (
	[leg_person_pk] [int] NOT NULL ,
	[ERROR] varchar(255) NULL,
	[MemberId] [varchar] (255) NULL ,
	[Affiliate_Type] [varchar] (255) NULL ,
	[Affiliate_State] [varchar] (255) NULL ,
	[Council] [varchar] (255) NULL ,
	[Affiliate_Local] [varchar] (255) NULL ,
	[SubLocal] [varchar] (255) NULL ,
	[List_Code] [varchar] (255) NULL ,
	[Social_Security] [varchar] (255) NULL ,
	[Gender] [varchar] (255) NULL ,
	[Salutation] [varchar] (255) NULL ,
	[LastName] [varchar] (255) NULL ,
	[FirstName] [varchar] (255) NULL ,
	[MiddleName] [varchar] (255) NULL ,
	[Suffix] [varchar] (255) NULL ,
	[MailingAddress1] [varchar] (255) NULL ,
	[MailingAddress2] [varchar] (255) NULL ,
	[City] [varchar] (255) NULL ,
	[State] [varchar] (255) NULL ,
	[ZipCode] [varchar] (255) NULL ,
	[Zip_4] [varchar] (255) NULL ,
	[Province] [varchar] (255) NULL ,
	[Country] [varchar] (255) NULL ,
	[AddressType] [varchar] (255) NULL ,
	[Mailable_Add_Flag] [varchar] (255) NULL ,
	[No_Mail_Flag] [varchar] (255) NULL ,
	[Email] [varchar] (255) NULL ,
	[PhoneNumber] [varchar] (255) NULL ,
	[StatusCode] [varchar] (255) NULL ,
	[DateJoined] [varchar] (255) NULL ,
	[Register_Voter] [varchar] (255) NULL ,
	[Political_Party] [varchar] (255) NULL ,
	[Information_Source] [varchar] (255) NULL ,
	[AffiliateId] [varchar] (255) NULL ,
	[UnitCode] [varchar] (255) NULL ,
	[AddressChange_Dt] [varchar] (255) NULL ,
	[AlternateMailName] [varchar] (255) NULL 
) ON [PRIMARY]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[DM_migrateOfficer]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[DM_migrateOfficer]
GO

CREATE TABLE [dbo].[DM_migrateOfficer] (
	[leg_officer_pk] [int] IDENTITY ,	
	[leg_person_pk] [int] NULL ,	
	[aff_pk] [int] NULL ,	
	[MemberId] [varchar] (255) NULL ,
	[Affiliate_Type] [varchar] (255) NULL ,
	[Affiliate_State] [varchar] (255) NULL ,
	[Affiliate_Council] [varchar] (255) NULL ,
	[Affiliate_Local] [varchar] (255) NULL ,
	[Affiliate_Seq] [varchar] (255) NULL ,
	[SubLocal] [varchar] (255) NULL ,
	[AffiliateId] [varchar] (255) NULL ,
	[UnitCode] [varchar] (255) NULL ,
	[TitleCodeId] [varchar] (255) NULL ,
	[TitleCodeSeq] [varchar] (255) NULL ,
	[Gender] [varchar] (255) NULL ,
	[Salutation] [varchar] (255) NULL ,
	[LastName] [varchar] (255) NULL ,
	[FirstName] [varchar] (255) NULL ,
	[MiddleName] [varchar] (255) NULL ,
	[Suffix] [varchar] (255) NULL ,
	[Home_Addr1] [varchar] (255) NULL ,
	[Home_Addr2] [varchar] (255) NULL ,
	[Home_City] [varchar] (255) NULL ,
	[Home_State] [varchar] (255) NULL ,
	[Home_ZipCode] [varchar] (255) NULL ,
	[Home_Zip4] [varchar] (255) NULL ,
	[Home_Province] [varchar] (255) NULL ,
	[Home_Country] [varchar] (255) NULL ,
	[Work_Addr1] [varchar] (255) NULL ,
	[Work_Addr2] [varchar] (255) NULL ,
	[Work_City] [varchar] (255) NULL ,
	[Work_State] [varchar] (255) NULL ,
	[Work_ZipCode] [varchar] (255) NULL ,
	[Work_Zip4] [varchar] (255) NULL ,
	[Work_Province] [varchar] (255) NULL ,
	[Work_Country] [varchar] (255) NULL ,
	[Email] [varchar] (255) NULL ,
	[Phone_Number] [varchar] (255) NULL ,
	[Office_Mail_Code] [varchar] (255) NULL ,
	[Office_Exp_Date] [varchar] (255) NULL ,
	[Last_Filled_Dt] [varchar] (255) NULL ,
	[Last_Update_Dt] [varchar] (255) NULL ,
	created_user_pk User_pk DEFAULT 10000002,
	created_dt datetime DEFAULT GetDate(),
	lst_mod_user_pk User_pk DEFAULT 10000002, 
	lst_mod_dt datetime DEFAULT GetDate()
) ON [PRIMARY]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[DM_migrateOfficer_ERROR]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[DM_migrateOfficer_ERROR]
GO

CREATE TABLE [dbo].[DM_migrateOfficer_ERROR] (
	[leg_officer_pk] [int] ,	
	[leg_person_pk] [int] NULL ,
	[aff_pk] [int] NULL ,	
	[ERROR] varchar(255) NULL,
	[MemberId] [varchar] (255) NULL ,
	[Affiliate_Type] [varchar] (255) NULL ,
	[Affiliate_State] [varchar] (255) NULL ,
	[Affiliate_Council] [varchar] (255) NULL ,
	[Affiliate_Local] [varchar] (255) NULL ,
	[Affiliate_Seq] [varchar] (255) NULL ,
	[SubLocal] [varchar] (255) NULL ,
	[AffiliateId] [varchar] (255) NULL ,
	[UnitCode] [varchar] (255) NULL ,
	[TitleCodeId] [varchar] (255) NULL ,
	[TitleCodeSeq] [varchar] (255) NULL ,
	[Gender] [varchar] (255) NULL ,
	[Salutation] [varchar] (255) NULL ,
	[LastName] [varchar] (255) NULL ,
	[FirstName] [varchar] (255) NULL ,
	[MiddleName] [varchar] (255) NULL ,
	[Suffix] [varchar] (255) NULL ,
	[Home_Addr1] [varchar] (255) NULL ,
	[Home_Addr2] [varchar] (255) NULL ,
	[Home_City] [varchar] (255) NULL ,
	[Home_State] [varchar] (255) NULL ,
	[Home_ZipCode] [varchar] (255) NULL ,
	[Home_Zip4] [varchar] (255) NULL ,
	[Home_Province] [varchar] (255) NULL ,
	[Home_Country] [varchar] (255) NULL ,
	[Work_Addr1] [varchar] (255) NULL ,
	[Work_Addr2] [varchar] (255) NULL ,
	[Work_City] [varchar] (255) NULL ,
	[Work_State] [varchar] (255) NULL ,
	[Work_ZipCode] [varchar] (255) NULL ,
	[Work_Zip4] [varchar] (255) NULL ,
	[Work_Province] [varchar] (255) NULL ,
	[Work_Country] [varchar] (255) NULL ,
	[Email] [varchar] (255) NULL ,
	[Phone_Number] [varchar] (255) NULL ,
	[Office_Mail_Code] [varchar] (255) NULL ,
	[Office_Exp_Date] [varchar] (255) NULL ,
	[Last_Filled_Dt] [varchar] (255) NULL ,
	[Last_Update_Dt] [varchar] (255) NULL 
) ON [PRIMARY]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[DM_Aff_Officer_Groups_ERROR]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[DM_Aff_Officer_Groups_ERROR]
GO

CREATE TABLE [DM_Aff_Officer_Groups_ERROR] (
	[aff_pk] [int] NULL ,
	[afscme_office_pk] [int] NULL ,
	[ERROR] varchar(255) NULL,
	[office_group_id] [int] NULL ,
	[affiliate_office_title] [varchar] (30) NULL ,
	[length_of_term] [Common_code_key] NULL ,
	[max_number_in_office] [smallint] NULL ,
	[month_of_election] [Common_code_key] NULL ,
	[current_term_end] [smallint] NULL ,
	[delegate_priority] [smallint] NULL ,
	[executive_board_fg] [fg] NULL ,
	[reporting_officer_fg] [fg] NULL ,
	[created_user_pk] [User_pk] NOT NULL ,
	[created_dt] [datetime] NULL ,
	[lst_mod_user_pk] [User_pk] NULL ,
	[lst_mod_dt] [datetime] NULL ,
) 
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[DM_migratePerson_DMAL]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [DM_migratePerson_DMAL]
GO

CREATE TABLE [DM_migratePerson_DMAL] (
	[leg_person_pk] [int] NULL ,
	[MemberId] [varchar] (255) NULL ,
	[Affiliate_Type] [varchar] (255) NULL ,
	[Affiliate_State] [varchar] (255) NULL ,
	[Council] [varchar] (255) NULL ,
	[Affiliate_Local] [varchar] (255) NULL ,
	[SubLocal] [varchar] (255) NULL ,
	[List_Code] [varchar] (255) NULL ,
	[Social_Security] [varchar] (255) NULL ,
	[Gender] [varchar] (255) NULL ,
	[Salutation] [varchar] (255) NULL ,
	[LastName] [varchar] (255) NULL ,
	[FirstName] [varchar] (255) NULL ,
	[MiddleName] [varchar] (255) NULL ,
	[Suffix] [varchar] (255) NULL ,
	[MailingAddress1] [varchar] (255) NULL ,
	[MailingAddress2] [varchar] (255) NULL ,
	[City] [varchar] (255) NULL ,
	[State] [varchar] (255) NULL ,
	[ZipCode] [varchar] (255) NULL ,
	[Zip_4] [varchar] (255) NULL ,
	[Province] [varchar] (255) NULL ,
	[Country] [varchar] (255) NULL ,
	[AddressType] [varchar] (255) NULL ,
	[Mailable_Add_Flag] [varchar] (255) NULL ,
	[No_Mail_Flag] [varchar] (255) NULL ,
	[Email] [varchar] (255) NULL ,
	[PhoneNumber] [varchar] (255) NULL ,
	[StatusCode] [varchar] (255) NULL ,
	[DateJoined] [varchar] (255) NULL ,
	[Register_Voter] [varchar] (255) NULL ,
	[Political_Party] [varchar] (255) NULL ,
	[Information_Source] [varchar] (255) NULL ,
	[AffiliateId] [varchar] (255) NULL ,
	[UnitCode] [varchar] (255) NULL ,
	[AddressChange_Dt] [varchar] (255) NULL ,
	[AlternateMailName] [varchar] (255) NULL ,
	created_user_pk User_pk DEFAULT 10000002,
	created_dt datetime DEFAULT GetDate(),
	lst_mod_user_pk User_pk DEFAULT 10000002, 
	lst_mod_dt datetime DEFAULT GetDate()
) ON [PRIMARY]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[DM_migratePerson_DMAL_ERROR]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [DM_migratePerson_DMAL_ERROR]
GO

CREATE TABLE [DM_migratePerson_DMAL_ERROR] (
	[leg_person_pk] [int] NULL ,
	[ERROR] [varchar] (255) NULL ,
	[MemberId] [varchar] (255) NULL ,
	[Affiliate_Type] [varchar] (255) NULL ,
	[Affiliate_State] [varchar] (255) NULL ,
	[Council] [varchar] (255) NULL ,
	[Affiliate_Local] [varchar] (255) NULL ,
	[SubLocal] [varchar] (255) NULL ,
	[List_Code] [varchar] (255) NULL ,
	[Social_Security] [varchar] (255) NULL ,
	[Gender] [varchar] (255) NULL ,
	[Salutation] [varchar] (255) NULL ,
	[LastName] [varchar] (255) NULL ,
	[FirstName] [varchar] (255) NULL ,
	[MiddleName] [varchar] (255) NULL ,
	[Suffix] [varchar] (255) NULL ,
	[MailingAddress1] [varchar] (255) NULL ,
	[MailingAddress2] [varchar] (255) NULL ,
	[City] [varchar] (255) NULL ,
	[State] [varchar] (255) NULL ,
	[ZipCode] [varchar] (255) NULL ,
	[Zip_4] [varchar] (255) NULL ,
	[Province] [varchar] (255) NULL ,
	[Country] [varchar] (255) NULL ,
	[AddressType] [varchar] (255) NULL ,
	[Mailable_Add_Flag] [varchar] (255) NULL ,
	[No_Mail_Flag] [varchar] (255) NULL ,
	[Email] [varchar] (255) NULL ,
	[PhoneNumber] [varchar] (255) NULL ,
	[StatusCode] [varchar] (255) NULL ,
	[DateJoined] [varchar] (255) NULL ,
	[Register_Voter] [varchar] (255) NULL ,
	[Political_Party] [varchar] (255) NULL ,
	[Information_Source] [varchar] (255) NULL ,
	[AffiliateId] [varchar] (255) NULL ,
	[UnitCode] [varchar] (255) NULL ,
	[AddressChange_Dt] [varchar] (255) NULL ,
	[AlternateMailName] [varchar] (255) NULL 
) ON [PRIMARY]
GO


if exists (select * from dbo.sysobjects where id = object_id(N'[DM_migrateRebate_ERROR]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [DM_migrateRebate_ERROR]
GO

CREATE TABLE DM_migrateRebate_ERROR (
	Primary_Key int NOT NULL ,
	ERROR varchar(255) NULL ,
	person_pk int NULL ,
	aff_pk int NULL ,
	reb_year varchar (255) NULL ,
	reb_ssn varchar (255) NULL ,
	reb_ssn_dup varchar (255) NULL ,
	reb_state varchar (255) NULL ,
	reb_council varchar (255) NULL ,
	reb_unit_code varchar (255) NULL ,
	reb_lname varchar (255) NULL ,
	reb_fname varchar (255) NULL ,
	reb_initial varchar (255) NULL ,
	reb_addr1 varchar (255) NULL ,
	reb_addr2 varchar (255) NULL ,
	reb_city varchar (255) NULL ,
	reb_state_abbr varchar (255) NULL ,
	reb_zip varchar (255) NULL ,
	reb_zip4 varchar (255) NULL ,
	reb_roster_status varchar (255) NULL ,
	reb_mbr_status varchar (255) NULL ,
	reb_nbr_months varchar (255) NULL ,
	reb_source varchar (255) NULL ,
	reb_label_status varchar (255) NULL ,
	reb_check_status varchar (255) NULL ,
	reb_check_nbr varchar (255) NULL ,
	reb_suffix varchar (255) NULL ,
	reb_check_amt varchar (255) NULL ,
	reb_keyed_date varchar (255) NULL ,
	reb_application_mailed_date varchar (255) NULL ,
	reb_application_returned_date varchar (255) NULL ,
	reb_application_evaluation_cde varchar (255) NULL ,
	reb_comment varchar (255) NULL ,
	reb_comment_analysis_code varchar (255) NULL 
) 
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[DM_migrateRebate]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [DM_migrateRebate]
GO

CREATE TABLE DM_migrateRebate (
	Primary_Key int NOT NULL ,
	person_pk int NULL ,
	aff_pk int NULL ,
	reb_year varchar (255) NULL ,
	reb_ssn varchar (255) NULL ,
	reb_ssn_dup varchar (255) NULL ,
	reb_state varchar (255) NULL ,
	reb_council varchar (255) NULL ,
	reb_unit_code varchar (255) NULL ,
	reb_lname varchar (255) NULL ,
	reb_fname varchar (255) NULL ,
	reb_initial varchar (255) NULL ,
	reb_addr1 varchar (255) NULL ,
	reb_addr2 varchar (255) NULL ,
	reb_city varchar (255) NULL ,
	reb_state_abbr varchar (255) NULL ,
	reb_zip varchar (255) NULL ,
	reb_zip4 varchar (255) NULL ,
	reb_roster_status varchar (255) NULL ,
	reb_mbr_status varchar (255) NULL ,
	reb_nbr_months varchar (255) NULL ,
	reb_source varchar (255) NULL ,
	reb_label_status varchar (255) NULL ,
	reb_check_status varchar (255) NULL ,
	reb_check_nbr varchar (255) NULL ,
	reb_suffix varchar (255) NULL ,
	reb_check_amt varchar (255) NULL ,
	reb_keyed_date varchar (255) NULL ,
	reb_application_mailed_date varchar (255) NULL ,
	reb_application_returned_date varchar (255) NULL ,
	reb_application_evaluation_cde varchar (255) NULL ,
	reb_comment varchar (255) NULL ,
	reb_comment_analysis_code varchar (255) NULL ,
	created_user_pk User_pk DEFAULT 10000002,
	created_dt datetime DEFAULT GetDate(),
	lst_mod_user_pk User_pk DEFAULT 10000002, 
	lst_mod_dt datetime DEFAULT GetDate()
) 
GO

/*
CREATE 
  INDEX [PK_PersonPk] ON [dbo].[DM_migrateRebate] ([person_pk])
WITH
    DROP_EXISTING
ON [PRIMARY]

CREATE 
  INDEX [PK_PersonPk_AffPk] ON [dbo].[DM_migrateRebate] ([person_pk], [aff_pk])
WITH
    DROP_EXISTING
ON [PRIMARY]

CREATE 
  INDEX [PK_PersonPk] ON [dbo].[DM_migratePerson] ([leg_person_pk])
WITH
    DROP_EXISTING
ON [PRIMARY]
*/
