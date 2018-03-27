-- Final v 1.0
-- Patch run after data migration was complete.  Additional states were found to be
-- invalid and caused an error to occur in the application when attempting to display.

-- Person Address
UPDATE person_address
   SET State = ''
 WHERE state not in (
		SELECT com_cd_cd
		  FROM common_codes
		 WHERE com_cd_type_key = 'state')

GO

-- Organization/Affiliate Address
UPDATE org_address
   SET State = ''
 WHERE state not in (
		SELECT com_cd_cd
		  FROM common_codes
		 WHERE com_cd_type_key = 'state')

GO

