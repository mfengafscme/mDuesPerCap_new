-- =============================================
-- Process the Affiliate File into the Enterprise System
-- =============================================
-- creating the store procedure
IF EXISTS (SELECT name 
	   FROM   sysobjects 
	   WHERE  name = N'sp_errorAffiliate' 
	   AND 	  type = 'P')
    DROP PROCEDURE sp_errorAffiliate
GO

CREATE PROCEDURE sp_errorAffiliate 
AS
DECLARE @errorCode int

BEGIN TRAN sp_errorAffiliate

--> Transfer records that fail error checks to DM_migrateAffiliate_ERROR
	-- Invalid CharterStatus, affiliate must have a valid CharterStatus/AffiliateStatus
	INSERT INTO DM_migrateAffiliate_ERROR 
		(leg_aff_pk, ERROR, Affiliate_Type, State, CouncilRetiree_chap, 
		aff_localSubChapter, SubLocal, AffiliateSequence, CharterStatus, 
		NewUnitCode, InformationSource, UnitWideNoMail, OfficerCode, 
		OfficerSequenceNumber, MthofElection, UnitName, PrimaryAddress, 
		AuxiliaryAddress, City, StateofResidence, ZipCode, Plus4, 
		MailableAddress, MassChangeFlag, UnitTelephone, Last_Update_DT) 
	 SELECT leg_aff_pk, 'Invalid CharterStatus - '+ CharterStatus, 
		Affiliate_Type, State, CouncilRetiree_chap, 
		aff_localSubChapter, SubLocal, AffiliateSequence, CharterStatus, 
		NewUnitCode, InformationSource, UnitWideNoMail, OfficerCode, 
		OfficerSequenceNumber, MthofElection, UnitName, PrimaryAddress, 
		AuxiliaryAddress, City, StateofResidence, ZipCode, Plus4, 
		MailableAddress, MassChangeFlag, UnitTelephone, Last_Update_DT 
	   FROM LEG_Affiliate
	  WHERE CharterStatus NOT IN (SELECT Distinct Legacy_Code
					FROM DM_Code_Mapping_view
				       WHERE com_cd_type_key = 'AffiliateStatus'
					 AND Legacy_Code IS NOT NULL)
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- If SubLocal has a value and the type is not set to 'U', error
	INSERT INTO DM_migrateAffiliate_ERROR 
		(leg_aff_pk, ERROR, Affiliate_Type, State, CouncilRetiree_chap, 
		aff_localSubChapter, SubLocal, AffiliateSequence, CharterStatus, 
		NewUnitCode, InformationSource, UnitWideNoMail, OfficerCode, 
		OfficerSequenceNumber, MthofElection, UnitName, PrimaryAddress, 
		AuxiliaryAddress, City, StateofResidence, ZipCode, Plus4, 
		MailableAddress, MassChangeFlag, UnitTelephone, Last_Update_DT) 
	 SELECT leg_aff_pk, 'SubLocal has a value and the Type is not set to U', 
		Affiliate_Type, State, CouncilRetiree_chap, 
		aff_localSubChapter, SubLocal, AffiliateSequence, CharterStatus, 
		NewUnitCode, InformationSource, UnitWideNoMail, OfficerCode, 
		OfficerSequenceNumber, MthofElection, UnitName, PrimaryAddress, 
		AuxiliaryAddress, City, StateofResidence, ZipCode, Plus4, 
		MailableAddress, MassChangeFlag, UnitTelephone, Last_Update_DT 
	   FROM LEG_Affiliate
	  WHERE SubLocal IS NOT NULL
	    AND SubLocal <> '000' 
	    AND Affiliate_Type <> 'U'
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- If SubLocal does NOT have a value and the type is set to 'U', error
	INSERT INTO DM_migrateAffiliate_ERROR 
		(leg_aff_pk, ERROR, Affiliate_Type, State, CouncilRetiree_chap, 
		aff_localSubChapter, SubLocal, AffiliateSequence, CharterStatus, 
		NewUnitCode, InformationSource, UnitWideNoMail, OfficerCode, 
		OfficerSequenceNumber, MthofElection, UnitName, PrimaryAddress, 
		AuxiliaryAddress, City, StateofResidence, ZipCode, Plus4, 
		MailableAddress, MassChangeFlag, UnitTelephone, Last_Update_DT) 
	 SELECT leg_aff_pk, 'SubLocal value missing and the Type is set to U', 
		Affiliate_Type, State, CouncilRetiree_chap, 
		aff_localSubChapter, SubLocal, AffiliateSequence, CharterStatus, 
		NewUnitCode, InformationSource, UnitWideNoMail, OfficerCode, 
		OfficerSequenceNumber, MthofElection, UnitName, PrimaryAddress, 
		AuxiliaryAddress, City, StateofResidence, ZipCode, Plus4, 
		MailableAddress, MassChangeFlag, UnitTelephone, Last_Update_DT 
	   FROM LEG_Affiliate
	  WHERE (SubLocal IS NULL OR SubLocal = '000')
	    AND Affiliate_Type = 'U'
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- If aff_localSubChapter has no value and the type is set to 'L' or 'S', error
	INSERT INTO DM_migrateAffiliate_ERROR 
		(leg_aff_pk, ERROR, Affiliate_Type, State, CouncilRetiree_chap, 
		aff_localSubChapter, SubLocal, AffiliateSequence, CharterStatus, 
		NewUnitCode, InformationSource, UnitWideNoMail, OfficerCode, 
		OfficerSequenceNumber, MthofElection, UnitName, PrimaryAddress, 
		AuxiliaryAddress, City, StateofResidence, ZipCode, Plus4, 
		MailableAddress, MassChangeFlag, UnitTelephone, Last_Update_DT) 
	 SELECT leg_aff_pk, 'aff_localSubChapter has no value and the Type is set to L or S',
		Affiliate_Type, State, CouncilRetiree_chap, 
		aff_localSubChapter, SubLocal, AffiliateSequence, CharterStatus, 
		NewUnitCode, InformationSource, UnitWideNoMail, OfficerCode, 
		OfficerSequenceNumber, MthofElection, UnitName, PrimaryAddress, 
		AuxiliaryAddress, City, StateofResidence, ZipCode, Plus4, 
		MailableAddress, MassChangeFlag, UnitTelephone, Last_Update_DT 
	   FROM LEG_Affiliate
	  WHERE (aff_localSubChapter IS NULL
	    OR aff_localSubChapter = '0000') 
	    AND Affiliate_Type IN ('L','S')
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- If CouncilRetiree_chap has no value and the type is set to 'C' or 'R', error
	INSERT INTO DM_migrateAffiliate_ERROR 
		(leg_aff_pk, ERROR, Affiliate_Type, State, CouncilRetiree_chap, 
		aff_localSubChapter, SubLocal, AffiliateSequence, CharterStatus, 
		NewUnitCode, InformationSource, UnitWideNoMail, OfficerCode, 
		OfficerSequenceNumber, MthofElection, UnitName, PrimaryAddress, 
		AuxiliaryAddress, City, StateofResidence, ZipCode, Plus4, 
		MailableAddress, MassChangeFlag, UnitTelephone, Last_Update_DT) 
	 SELECT leg_aff_pk, 'CouncilRetiree_chap has no value and the Type is set to C or R',
		Affiliate_Type, State, CouncilRetiree_chap, 
		aff_localSubChapter, SubLocal, AffiliateSequence, CharterStatus, 
		NewUnitCode, InformationSource, UnitWideNoMail, OfficerCode, 
		OfficerSequenceNumber, MthofElection, UnitName, PrimaryAddress, 
		AuxiliaryAddress, City, StateofResidence, ZipCode, Plus4, 
		MailableAddress, MassChangeFlag, UnitTelephone, Last_Update_DT 
	   FROM LEG_Affiliate
	  WHERE (CouncilRetiree_chap IS NULL
	    OR CouncilRetiree_chap = '0000') 
	    AND Affiliate_Type IN ('C','R')
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Violates unique constraint for Affiliate Identifier fields
	INSERT INTO DM_migrateAffiliate_ERROR 
		(leg_aff_pk, ERROR, Affiliate_Type, State, CouncilRetiree_chap, 
		aff_localSubChapter, SubLocal, AffiliateSequence, CharterStatus, 
		NewUnitCode, InformationSource, UnitWideNoMail, OfficerCode, 
		OfficerSequenceNumber, MthofElection, UnitName, PrimaryAddress, 
		AuxiliaryAddress, City, StateofResidence, ZipCode, Plus4, 
		MailableAddress, MassChangeFlag, UnitTelephone, Last_Update_DT) 
	 SELECT leg_aff_pk, 'Violates unique constraint for Affiliate Identifier fields', 
		Affiliate_Type, State, CouncilRetiree_chap, 
		aff_localSubChapter, SubLocal, AffiliateSequence, CharterStatus, 
		NewUnitCode, InformationSource, UnitWideNoMail, OfficerCode, 
		OfficerSequenceNumber, MthofElection, UnitName, PrimaryAddress, 
		AuxiliaryAddress, City, StateofResidence, ZipCode, Plus4, 
		MailableAddress, MassChangeFlag, UnitTelephone, Last_Update_DT 
	   FROM LEG_Affiliate l
	  WHERE EXISTS (SELECT *
			  FROM LEG_Affiliate l2
			 WHERE l.Affiliate_Type + l.aff_localSubChapter + l.AffiliateSequence + l.State + COALESCE(l.SubLocal, '') + l.CouncilRetiree_chap = 
			       l2.Affiliate_Type + l2.aff_localSubChapter + l2.AffiliateSequence + l2.State + COALESCE(l2.SubLocal, '') + l2.CouncilRetiree_chap
			 GROUP BY Affiliate_Type, aff_localSubChapter, AffiliateSequence, State, SubLocal, CouncilRetiree_chap
			HAVING Count(*) > 1 )
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

	-- Move records not in the Error table to DM_migrateAffiliate
	INSERT INTO DM_migrateAffiliate 
		(leg_aff_pk, Affiliate_Type, State, CouncilRetiree_chap, 
		aff_localSubChapter, SubLocal, AffiliateSequence, CharterStatus, 
		NewUnitCode, InformationSource, UnitWideNoMail, OfficerCode, 
		OfficerSequenceNumber, MthofElection, UnitName, PrimaryAddress, 
		AuxiliaryAddress, City, StateofResidence, ZipCode, Plus4, 
		MailableAddress, MassChangeFlag, UnitTelephone, Last_Update_DT) 
	 SELECT leg_aff_pk,  
		Affiliate_Type, State, CouncilRetiree_chap, 
		aff_localSubChapter, SubLocal, AffiliateSequence, CharterStatus, 
		NewUnitCode, InformationSource, UnitWideNoMail, OfficerCode, 
		OfficerSequenceNumber, MthofElection, UnitName, PrimaryAddress, 
		AuxiliaryAddress, City, StateofResidence, ZipCode, Plus4, 
		MailableAddress, MassChangeFlag, UnitTelephone, Last_Update_DT 
	   FROM LEG_Affiliate a
	  WHERE NOT EXISTS (SELECT * FROM DM_migrateAffiliate_ERROR e
			     WHERE a.leg_aff_pk = e.leg_aff_pk )
	-- Get error code
	SELECT @errorCode = @@ERROR
	IF @errorCode <> 0 GOTO E_ERROR

COMMIT TRAN sp_errorAffiliate
PRINT 'sp_errorAffiliate has completed'

-->> Display the error
E_ERROR:
	IF @errorCode <> 0
	BEGIN
		PRINT 'Error occurred in sp_errorAffiliate() with error code = ' + CONVERT(VARCHAR, @errorCode)
		ROLLBACK TRAN sp_errorAffiliate 
	END

RETURN @errorCode
GO

-- =============================================
-- example to execute the store procedure
-- =============================================
--EXECUTE sp_errorAffiliate 

