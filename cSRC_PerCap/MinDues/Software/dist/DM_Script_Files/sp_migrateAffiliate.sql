-- =============================================
-- Process the Affiliate File into the Enterprise System
-- =============================================
-- creating the store procedure
IF EXISTS (SELECT name 
	   FROM   sysobjects 
	   WHERE  name = N'sp_migrateAffiliate' 
	   AND 	  type = 'P')
    DROP PROCEDURE sp_migrateAffiliate
GO

CREATE PROCEDURE sp_migrateAffiliate 
AS
DECLARE @errorCode int

BEGIN TRAN sp_migrateAffiliate

-- Populate Org_Parent	
	-- SET IDENTITY_INSERT to ON
	SET IDENTITY_INSERT Org_Parent ON

	-- All should be Affiliates (subtype = 24001), put all in Org_Parent
	INSERT INTO Org_Parent (org_pk, org_subtype)
	 SELECT leg_aff_pk, '24001'
	   FROM DM_migrateAffiliate
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- SET IDENTITY_INSERT to OFF
	SET IDENTITY_INSERT Org_Parent OFF

-- Populate Aff_Organizations
	-- Transform status, legacy affiliate id
	INSERT INTO Aff_Organizations (aff_pk, aff_type, aff_stateNat_type, 
		aff_councilRetiree_chap, aff_localSubChapter, aff_subUnit, aff_code, aff_status,
		mbr_allow_edit_fg, mbr_allow_view_fg, old_aff_unit_cd_legacy, aff_abbreviated_nm,
		created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
	 SELECT leg_aff_pk, Affiliate_Type, State, CouncilRetiree_chap, 
		aff_localSubChapter, COALESCE(SubLocal, ''), AffiliateSequence, 
		(SELECT com_cd_pk FROM DM_Code_Mapping_view 
		  WHERE Legacy_Code = CharterStatus 
		    AND com_cd_type_key = 'AffiliateStatus') AS aff_status, 1, 1,
		CASE 
			WHEN Affiliate_Type IN ('C', 'R') THEN Affiliate_Type+CouncilRetiree_chap+AffiliateSequence
			ELSE Affiliate_Type+aff_localSubChapter+AffiliateSequence END,
		COALESCE(UnitName, ''), 
		created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt
	   FROM DM_migrateAffiliate
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Remove leading zeros, set all zero or null to blank for the Affiliate Identifier
	 UPDATE Aff_Organizations
	    SET aff_localSubChapter = CASE CAST(aff_localSubChapter AS int) 
			WHEN 0 THEN '' ELSE CAST(CAST(aff_localSubChapter AS int) AS varchar(4)) END, 
		aff_subUnit = CASE CAST(aff_subUnit AS int) 
			WHEN 0 THEN '' ELSE CAST(CAST(aff_subUnit AS int) AS varchar(4)) END, 
		aff_councilRetiree_chap = CASE CAST(aff_councilRetiree_chap AS int) 
			WHEN 0 THEN '' ELSE CAST(CAST(aff_councilRetiree_chap AS int) AS varchar(4)) END
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- parent_aff_fk
		-- If Council or Retiree Chapter, null
		-- If Local and aff_councilRetiree_chap is empty, null
			-- Else find council using the aff_type = 'C'
				-- and aff_stateNat_type = Local's aff_stateNat_type
				-- and aff_councilRetiree_chap = Local's aff_councilRetiree_chap
				UPDATE child
				   SET parent_aff_fk = parent.aff_pk 
				  FROM Aff_Organizations child, Aff_Organizations parent
				 WHERE child.aff_Type = 'L'
				   AND parent.aff_Type = 'C'
				-- Council/Retiree can cross multiple states
				-- AND child.aff_stateNat_type=parent.aff_stateNat_type
				   AND child.aff_councilRetiree_chap = parent.aff_councilRetiree_chap
				-- Get error code
				SELECT @errorCode = @@ERROR
				IF @errorCode <> 0 GOTO E_ERROR
		-- If SubLocal ('U')
			-- Find Local using the aff_type = 'L'
				-- and aff_stateNat_type = SubLocal's aff_stateNat_type
				-- and aff_councilRetiree_chap = SubLocal's aff_councilRetiree_chap
				-- and aff_localSubChapter = SubLocal's aff_localSubChapter
				UPDATE child
				   SET parent_aff_fk = parent.aff_pk 
				  FROM Aff_Organizations child, Aff_Organizations parent
				 WHERE child.aff_Type = 'U'
				   AND parent.aff_Type = 'L'
				   AND child.aff_stateNat_type=parent.aff_stateNat_type
				   AND child.aff_localSubChapter = parent.aff_localSubChapter
				   AND child.aff_councilRetiree_chap = parent.aff_councilRetiree_chap
				-- Get error code
				SELECT @errorCode = @@ERROR
				IF @errorCode <> 0 GOTO E_ERROR
			-- If no match, then this is an ERROR
		-- If Sub Chapter ('S')
			-- Find Retiree Chapter using the aff_type = 'R'
				-- and aff_stateNat_type = Sub Chapter's aff_stateNat_type
				-- and aff_councilRetiree_chap = Sub Chapter's aff_councilRetiree_chap
				UPDATE child
				   SET parent_aff_fk = parent.aff_pk 
				  FROM Aff_Organizations child, Aff_Organizations parent
				 WHERE child.aff_Type = 'S'
				   AND parent.aff_Type = 'R'
				-- Council/Retiree can cross multiple states
				-- AND child.aff_stateNat_type=parent.aff_stateNat_type
				   AND child.aff_councilRetiree_chap = parent.aff_councilRetiree_chap
				-- Get error code
				SELECT @errorCode = @@ERROR
				IF @errorCode <> 0 GOTO E_ERROR

		-- If expecting a parent pk and none is found, then this is an ERROR
		INSERT INTO DM_migrateAffiliate_ERROR 
			(leg_aff_pk, ERROR, Affiliate_Type, State, CouncilRetiree_chap, 
			aff_localSubChapter, SubLocal, AffiliateSequence, CharterStatus, 
			NewUnitCode, InformationSource, UnitWideNoMail, OfficerCode, 
			OfficerSequenceNumber, MthofElection, UnitName, PrimaryAddress, 
			AuxiliaryAddress, City, StateofResidence, ZipCode, Plus4, 
			MailableAddress, MassChangeFlag, UnitTelephone, Last_Update_DT) 
		 SELECT leg_aff_pk, 'Unable to set the parent_aff_fk', Affiliate_Type, State, CouncilRetiree_chap, 
			aff_localSubChapter, SubLocal, AffiliateSequence, CharterStatus, 
			NewUnitCode, InformationSource, UnitWideNoMail, OfficerCode, 
			OfficerSequenceNumber, MthofElection, UnitName, PrimaryAddress, 
			AuxiliaryAddress, City, StateofResidence, ZipCode, Plus4, 
			MailableAddress, MassChangeFlag, UnitTelephone, Last_Update_DT 
		   FROM DM_migrateAffiliate
		  WHERE leg_aff_pk IN (SELECT aff_pk 
					 FROM Aff_Organizations
					WHERE parent_aff_fk IS NULL
					  AND aff_type IN ('L', 'S')
					  AND aff_councilRetiree_chap <> '' )
		-- Get error code
		SELECT @errorCode = @@ERROR
		IF @errorCode <> 0 GOTO E_ERROR

		-- Remove the missing parent pk records from DM_migrateAffiliate
		DELETE FROM DM_migrateAffiliate
		 WHERE leg_aff_pk in (SELECT leg_aff_pk FROM DM_migrateAffiliate_ERROR)
		-- Get error code
		SELECT @errorCode = @@ERROR
		IF @errorCode <> 0 GOTO E_ERROR

		-- Remove the missing parent pk records from Aff_Organizations
		DELETE FROM Aff_Organizations
		 WHERE aff_pk in (SELECT leg_aff_pk FROM DM_migrateAffiliate_ERROR)
		-- Get error code
		SELECT @errorCode = @@ERROR
		IF @errorCode <> 0 GOTO E_ERROR

		-- Remove the matching records from Org_Parent
		DELETE FROM Org_Parent
		 WHERE org_pk in (SELECT leg_aff_pk FROM DM_migrateAffiliate_ERROR)
		-- Get error code
		SELECT @errorCode = @@ERROR
		IF @errorCode <> 0 GOTO E_ERROR

	-- new_aff_id_pk (Merged Affiliates of where members went after the merge)
	-- Those affiliates with status of "merged", update new_aff_id_src
	-- Aff_Organizations with the NewUnitCode in the LEG_Affiliate table 
	-- Need join with the Aff_Organizations table to search the NewUnitCode in the legacy 
	-- DM_migrateAffiliate table and populate the Aff_Organizations new_aff_id_src
	-- with the aff_pk from the legacy table.
		UPDATE merged
		   SET merged.new_aff_id_src = active.aff_pk 
		-- SELECT leg_aff_pk, NewUnitCode, 
		--	merged.old_aff_unit_cd_legacy merg_unit, merged.aff_pk merg_pk, 
		--	active.old_aff_unit_cd_legacy active_unit, active.aff_pk active_pk
		   FROM LEG_Affiliate l
		   JOIN Aff_Organizations active ON active.old_aff_unit_cd_legacy = l.NewUnitCode
		   JOIN Aff_Organizations merged ON merged.aff_pk = l.leg_aff_pk
		  WHERE CharterStatus = 'M'

-- Create stub records for Aff_Charter
	INSERT INTO dbo.Aff_Charter
		(aff_pk, created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
	 SELECT aff_pk,created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt
	   FROM Aff_Organizations
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR
-- Create stub records for Aff_Constitution
	INSERT INTO dbo.Aff_Constitution
		(aff_pk, created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
	 SELECT aff_pk, created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt
	   FROM Aff_Organizations
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

-- Create stub records for Aff_Mbr_Rpt_Info, transform UnitWideNoMail
	INSERT INTO dbo.Aff_Mbr_Rpt_Info
		(aff_pk, aff_info_reporting_source,
		unit_wide_no_mbr_cards_fg, unit_wide_no_pe_mail_fg,
		created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
	 SELECT leg_aff_pk, 
		(SELECT com_cd_pk FROM DM_Code_Mapping_view 
		  WHERE Legacy_Code = InformationSource 
		    AND com_cd_type_key = 'InformationSource'),
	 	(CASE UnitWideNoMail
			WHEN 0 THEN 0
			WHEN 1 THEN 1
			WHEN 2 THEN 0
			WHEN 3 THEN 1
			ELSE 0 END ), 
		(CASE UnitWideNoMail
			WHEN 0 THEN 0
			WHEN 1 THEN 0
			WHEN 2 THEN 1
			WHEN 3 THEN 1
			ELSE 0 END ),
		created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt
	  FROM DM_migrateAffiliate
	 WHERE EXISTS (SELECT aff_pk
			 FROM Aff_Organizations
			WHERE aff_pk = DM_migrateAffiliate.leg_aff_pk)
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

-- Create stub record for Aff_Fin_Info
	INSERT INTO dbo.Aff_Fin_Info
		(aff_pk, created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
	 SELECT aff_pk, created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt
	   FROM Aff_Organizations
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

-- If location information is available, 
	-- Populate Org_Locations
		INSERT INTO Org_Locations
			(org_pk, location_primary_fg, 
			created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
		 SELECT leg_aff_pk, 1, created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt
			Last_Update_DT--CONVERT(datetime, Last_Update_DT)
		   FROM DM_migrateAffiliate
		  WHERE Len(LTRIM(PrimaryAddress)+LTRIM(AuxiliaryAddress)+LTRIM(City)+LTRIM(StateofResidence)) > 0
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Populate Org_Address
		INSERT INTO Org_Address 
			(org_locations_pk, org_addr_type, addr1, addr2, city, 
			state, zipcode, zip_plus, addr_bad_fg, addr_bad_dt,
			created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
		 SELECT loc.org_locations_pk, 
			(SELECT com_cd_pk FROM Common_Codes 
			  WHERE com_cd_type_key = 'OrgAddressType' 
			    AND com_cd_desc = 'Regular'),
			leg.PrimaryAddress, leg.AuxiliaryAddress, leg.City, 
			leg.StateofResidence, 
			CASE ZipCode WHEN '00000' THEN NullIf(ZipCode, '00000') ELSE ZipCode END,
			CASE Plus4 WHEN '0000' THEN NullIf(Plus4, '0000') ELSE Plus4 END, 
			CASE leg.MailableAddress WHEN 'Y' THEN 0 ELSE 1 END,
			CASE leg.MailableAddress WHEN 'N' THEN GetDate() END,
			leg.created_user_pk, leg.created_dt, leg.lst_mod_user_pk, leg.lst_mod_dt
		   FROM DM_migrateAffiliate leg
		   JOIN Org_Locations loc ON leg.leg_aff_pk = loc.org_pk
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- If phone exists, populate Org_Phone
		INSERT INTO Org_Phone 
			(org_locations_pk, org_phone_type, country_cd, 
			area_code, phone_no,
			created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
		 SELECT loc.org_locations_pk,
			(SELECT com_cd_pk FROM Common_Codes 
			  WHERE com_cd_type_key = 'OrgPhoneType' 
			    AND com_cd_desc = 'Office'),
			1, 
			SubString(UnitTelephone, 1, 3),
			SubString(UnitTelephone, 4, 7),
			leg.created_user_pk, leg.created_dt, leg.lst_mod_user_pk, leg.lst_mod_dt
		   FROM DM_migrateAffiliate leg
		   JOIN Org_Locations loc ON leg.leg_aff_pk = loc.org_pk
		  WHERE SubString(UnitTelephone, 4, 7) IS NOT NULL
		     OR UnitTelephone <> ''
		     OR UnitTelephone <> '0000000000' 

	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

-- Populate the DM_Affiliate_RO table for Reporting Officers
	INSERT INTO DM_Affiliate_RO (aff_pk, officer_cd, officer_sequence)
		SELECT leg_aff_pk, 
		       (SELECT afscme_office_pk FROM AFSCME_Offices
			 WHERE afscme_title_nm = 
				(SELECT com_cd_pk FROM DM_Code_Mapping_view 
	 			  WHERE com_cd_type_key = 'AFSCMETitle' 
	  			    AND Convert(int, Legacy_Code) = OfficerCode)), 
			OfficerSequenceNumber
		  FROM DM_migrateAffiliate 
		 WHERE (SELECT afscme_office_pk FROM AFSCME_Offices
			 WHERE afscme_title_nm = 
				(SELECT com_cd_pk FROM DM_Code_Mapping_view 
	 			  WHERE com_cd_type_key = 'AFSCMETitle' 
	  			    AND Convert(int, Legacy_Code) = OfficerCode)) IS NOT NULL
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

-- Not in requirements: Update Sub-Locals Allowed flag for the locals that have 
-- sub-locals in their hierarchy
	UPDATE a
	   SET aff_sub_locals_allowed_fg = 1
	  FROM aff_organizations a
	 WHERE aff_type = 'L'	
	   AND EXISTS (SELECT * FROM aff_organizations a2 WHERE a.aff_pk = a2.parent_aff_fk 
							    AND a2.aff_type = 'U')


COMMIT TRAN sp_migrateAffiliate
PRINT 'sp_migrateAffiliate has completed'

-->> Display the error
E_ERROR:
	IF @errorCode <> 0
	BEGIN
		PRINT 'Error occurred in sp_migrateAffiliate() with error code = ' + CONVERT(VARCHAR, @errorCode)
		ROLLBACK TRAN sp_migrateAffiliate 
	END

RETURN @errorCode
GO

-- =============================================
-- example to execute the store procedure
-- =============================================
--EXECUTE sp_migrateAffiliate 

