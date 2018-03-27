-- AFSCME Patch AT2
-- Updated NCOA stored procedure
-- Set data for NCOA testing
-- Updated Codes for typos in State names
-- Updated Roles_Privileges for change for Vendor
-- Re-create Views for changes
-- Add Field rows for state_code, aff_type, province, country, attention_line
-- Update Field rows to change zip fields from Int to String display type
-- Update Report row for Officer Credentials Cards change
-- Add script to delete from NCOA_Activity and NCOA_Activity_Dtl 
-- Set the member_fg in Person to 0 if person is not in Aff_Members


----------------------------
-- Prepare NCOA for Testing
----------------------------

----------------------------
-- Add script to delete from NCOA_Activity and NCOA_Activity_Dtl
----------------------------
DELETE FROM NCOA_Activity
GO

DELETE FROM NCOA_Activity_Dtl
GO 

--***************************************************************************************
--* SUMMARY: Stored Procedures that will initiate the NCOA Process
--*	Modified to update the Last Updated By and Date when an address is marked Bad. 
--***************************************************************************************

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[initiateNCOAIntegration]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
drop procedure [dbo].[initiateNCOAIntegration]
GO

SET QUOTED_IDENTIFIER ON 
GO
SET ANSI_NULLS ON 
GO

CREATE      PROCEDURE dbo.initiateNCOAIntegration
as
/*************************************************************************************
* Name: initiateNCOAIntegration
* Desc: Checks whether there is an entry in the nightly run table. If there are 
*	records then it processes all the records in the NCOA Transactions table
*	and performs actions depending on the transaction code
* Created date: 09/24/03	
* Author : Sivakumar
* Modified date : 09/24/03 
* Modified date : 12/02/03 Added created_user_pk for updates to lst_mod fields
**************************************************************************************/
BEGIN
	DECLARE @NCOA_Activity_PK int
	DECLARE @person_pk int, @address_pk int, @last_nm varchar(25), @extracted_sma_addr1 varchar(50)
  	DECLARE @extracted_sma_addr2 varchar(50), @extracted_sma_city varchar(25), @extracted_sma_state char(2)
	DECLARE @extracted_sma_zipcode varchar(12), @ncoa_transaction_cd char(1), @vendor_key varchar(27)
	DECLARE @vendor_addr1 varchar(50), @vendor_addr2 varchar(50), @vendor_city varchar(15),	@vendor_state char(2)
	DECLARE @vendor_zipcode char(5), @vendor_source char(1), @vendor_zip_plus char(4)
	DECLARE @vendor_carrier_route varchar(50), @vendor_ncoa_change_source char(4), @vendor_ncoa_change_dt datetime
	DECLARE @ISSMA tinyint, @errorCode int
	DECLARE @email_addr varchar(50), @msg varchar(8000)
	DECLARE @created_user_pk user_pk

	 SELECT @created_user_pk = created_user_pk FROM NCOA_Activity 
	  WHERE convert(varchar, NCOA_Activity_requested_dt, 101) = convert(varchar, getdate(), 101) AND 
		NCOA_Trans_run_completed_fg <> 1

	-- Prepare the transactions by setting all the State codes to Uppercase
	 UPDATE NCOA_Transactions
	    SET extracted_sma_state = Upper(extracted_sma_state),
		vendor_state = Upper(vendor_state)

	SELECT @errorCode = 0	
	SELECT @NCOA_Activity_PK = NCOA_ACTIVITY_PK 
					   FROM NCOA_ACTIVITY 
					   WHERE convert(varchar, NCOA_Activity_requested_dt, 101) = convert(varchar, getdate(), 101)
					   AND NCOA_Trans_run_completed_fg <> 1

	SELECT @email_addr = RTRIM(LOWER(person_email_addr)) FROM NCOA_ACTIVITY ncoa_activity
				   LEFT OUTER JOIN PERSON_EMAIL person_email ON person_email.person_pk = ncoa_activity.created_user_pk
				         AND (person_email.person_email_addr IS NOT NULL AND LEN(person_email.person_email_addr) > 0)
				   WHERE convert(varchar, NCOA_Activity_requested_dt, 101) = convert(varchar, getdate(), 101)
				         AND NCOA_Trans_run_completed_fg <> 1
	
	IF @NCOA_Activity_PK > 0
	BEGIN
		-- Open a cursor to fetch the records from the NCOA transactions table. 
		DECLARE NCOA_TRAN_CURSOR CURSOR 
		FOR 
		SELECT 
			N.person_pk,
			address_pk,
			last_nm,
			extracted_sma_addr1,
			extracted_sma_addr2,
			extracted_sma_city,
			extracted_sma_state,
			extracted_sma_zipcode,
			standard_trans_cd,
			vendor_key,
			vendor_addr1,
			vendor_addr2,
			vendor_city,
			vendor_state,
			vendor_zipcode,
			vendor_source,
			vendor_zip_plus,
			vendor_carrier_route,
			vendor_ncoa_change_source,
			vendor_ncoa_change_dt
		FROM
			NCOA_TRANSACTIONS N LEFT OUTER JOIN NCOA_Transaction_Cds T
		ON
			T.mapped_trans_cd = N.vendor_transaction_cd
		
		OPEN NCOA_TRAN_CURSOR
		
		FETCH NEXT FROM NCOA_TRAN_CURSOR INTO 
			@person_pk,
			@address_pk,
			@last_nm,
			@extracted_sma_addr1,
			@extracted_sma_addr2,
			@extracted_sma_city,
			@extracted_sma_state,
			@extracted_sma_zipcode,
			@ncoa_transaction_cd,
			@vendor_key,
			@vendor_addr1,
			@vendor_addr2,
			@vendor_city,
			@vendor_state,
			@vendor_zipcode,
			@vendor_source,
			@vendor_zip_plus,
			@vendor_carrier_route,
			@vendor_ncoa_change_source,
			@vendor_ncoa_change_dt

		IF @@FETCH_STATUS <> 0
		BEGIN
			raiserror('No row exists in NCOA Transactions table', 1, 16)
			UPDATE NCOA_Activity set ncoa_trans_run_error_fg = 1 
			WHERE ncoa_activity_pk = @ncoa_activity_pk
			CLOSE NCOA_TRAN_CURSOR
			DEALLOCATE NCOA_TRAN_CURSOR
			RETURN
		END

		IF NOT EXISTS (SELECT TOP 1 person_pk FROM NCOA_Transactions NT, NCOA_Transaction_Cds NTC WHERE 
			vendor_transaction_cd = mapped_trans_cd)
		BEGIN
			raiserror('No record in the transaction table contains a valid transaction code.', 1, 16)
			UPDATE NCOA_Activity set ncoa_trans_run_error_fg = 1 
			WHERE ncoa_activity_pk = @ncoa_activity_pk
			CLOSE NCOA_TRAN_CURSOR
			DEALLOCATE NCOA_TRAN_CURSOR
			RETURN
		END

		IF NOT EXISTS (SELECT 1 FROM NCOA_Transactions NT, Person_address PA WHERE 
			NT.address_pk = PA.address_pk)
		BEGIN
			raiserror('No record in the transaction table contains a valid address key.', 1, 16)
			UPDATE NCOA_Activity set ncoa_trans_run_error_fg = 1 
			WHERE ncoa_activity_pk = @ncoa_activity_pk
			CLOSE NCOA_TRAN_CURSOR
			DEALLOCATE NCOA_TRAN_CURSOR
			RETURN
		END

		WHILE @@FETCH_STATUS = 0
		BEGIN	
			/** NCOA transactions is basically based on the transaction code and transactions are mainly 
			categorized by their vendor address. So the procedure will first find whether there is a 
			vendor address coming in and then by the transaction code.
			**/
			print @person_pk

			IF @vendor_addr1 IS NOT NULL
			BEGIN
				IF @ncoa_transaction_cd = 'A'
				BEGIN
					-- 'ZIP PLUS 4 CHANGE'
					-- print 'ZIP PLUS 4 CHANGE'
					IF (isnull(@extracted_sma_addr1,0) = isnull(@vendor_addr1,0)) AND (isnull(@extracted_sma_addr2,0) = isnull(@vendor_addr2,0)) AND 
					(isnull(@extracted_sma_city,0) = isnull(@vendor_city,0)) AND (isnull(@extracted_sma_state,0) = isnull(@vendor_state,0)) AND 
					(isnull(@extracted_sma_zipcode,0) = isnull(@vendor_zipcode,0))
					--Check whether the SMA address and the vendor address are the same
					BEGIN
						IF @vendor_zip_plus IS NULL 
							INSERT INTO NCOA_Activity_Dtl(NCOA_Activity_PK, address_pk, person_pk, standard_trans_cd, ncoa_error_cd_pk) 
							values (@NCOA_Activity_PK, @address_pk, @person_pk, @ncoa_transaction_cd, 9)
						ELSE IF ISNUMERIC(@vendor_zip_plus) = 0 OR @vendor_zip_plus = ''
							INSERT INTO NCOA_Activity_Dtl(NCOA_Activity_PK, address_pk, person_pk, standard_trans_cd, ncoa_error_cd_pk) 
							values (@NCOA_Activity_PK, @address_pk, @person_pk, @ncoa_transaction_cd, 10)
						ELSE
							UPDATE Person_address SET ZIP_PLUS = @vendor_zip_plus 
							WHERE address_pk = @address_pk and person_pk = @person_pk
					END
					ELSE
					BEGIN
						--If the SMA address and the vendor address are not the same then 
						--the new address is inserted into the Person address table
						IF @vendor_zip_plus IS NULL 
							INSERT INTO NCOA_Activity_Dtl(NCOA_Activity_PK, address_pk, person_pk, standard_trans_cd, ncoa_error_cd_pk) 
							values (@NCOA_Activity_PK, @address_pk, @person_pk, @ncoa_transaction_cd, 9)
						ELSE IF ISNUMERIC(@vendor_zip_plus) = 0 OR @vendor_zip_plus = ''
							INSERT INTO NCOA_Activity_Dtl(NCOA_Activity_PK, address_pk, person_pk, standard_trans_cd, ncoa_error_cd_pk) 
							values (@NCOA_Activity_PK, @address_pk, @person_pk, @ncoa_transaction_cd, 10)
						ELSE
						BEGIN
							EXEC InsertPersonAddress @person_pk, @address_pk output
							-- Execute SMA Rules
							EXEC SetPersonSMA @address_pk, @person_pk
						END
					END
					
				END
				ELSE IF @ncoa_transaction_cd IN('E', 'G', 'K', 'P')
				BEGIN
					-- 'BAD ADDRESS'
					--If transaction codes are either Error, Box closed, delete or undeliverable then 
					-- the address is marked as bad in the person address table.
					UPDATE Person_address SET addr_bad_fg = 1, addr_marked_bad_dt = @vendor_ncoa_change_dt,
								lst_mod_user_pk = @created_user_pk, lst_mod_dt = getdate()  
					WHERE address_pk = @address_pk and person_pk = @person_pk

					EXEC InsertPersonAddress @person_pk, @address_pk output
					--Execute SMA Rules
					--PerformSystemAddressCheck checks whether this address can be marked as SMA 

					EXEC performSystemAddressCheck @address_pk, @person_pk, @IsSMA output
	
					IF @IsSMA = 1
						EXEC SetPersonSMA @address_pk, @person_pk
					ELSE
						UPDATE Person_SMA SET current_fg=0 WHERE current_fg=1 AND person_pk=@person_pk
	
				END
				ELSE IF @ncoa_transaction_cd IN ('M', 'S')
				BEGIN
					-- 'MOVE'
					-- print 'MOVE'
					--If transaction code indicated Move then this address is inserted into the 
					-- Person address table, checked for SMA and marked.
					EXEC InsertPersonAddress @person_pk, @address_pk output
					--Execute SMA Rules
					EXEC performSystemAddressCheck @address_pk, @person_pk, @IsSMA output
					IF @IsSMA = 1
						EXEC SetPersonSMA @address_pk, @person_pk
				END
				ELSE IF @ncoa_transaction_cd = 'Z'
				BEGIN
					-- 'ZIP CODE CHANGE'
					-- print 'ZIP CODE CHANGE'
					-- Checks whether SMA and vendor address are same. If they are same then only the 
					-- zipcode is changed, else the new address is inserted.
					IF (isnull(@extracted_sma_addr1,0) = isnull(@vendor_addr1,0)) AND (isnull(@extracted_sma_addr2,0) = isnull(@vendor_addr2,0)) AND 
					(isnull(@extracted_sma_city,0) = isnull(@vendor_city,0)) AND (isnull(@extracted_sma_state,0) = isnull(@vendor_state,0)) AND 
					(isnull(@extracted_sma_zipcode,0) = isnull(@vendor_zipcode,0))
					BEGIN
						IF @vendor_zipcode IS NULL 
							INSERT INTO NCOA_Activity_Dtl(NCOA_Activity_PK, address_pk, person_pk, standard_trans_cd, ncoa_error_cd_pk) 
							values (@NCOA_Activity_PK, @address_pk, @person_pk, @ncoa_transaction_cd, 6)
						ELSE IF ISNUMERIC(@vendor_zipcode) = 0 OR @vendor_zipcode = ''
							INSERT INTO NCOA_Activity_Dtl(NCOA_Activity_PK, address_pk, person_pk, standard_trans_cd, ncoa_error_cd_pk) 
							values (@NCOA_Activity_PK, @address_pk, @person_pk, @ncoa_transaction_cd, 7)
						ELSE
						BEGIN
							UPDATE Person_address SET zipcode = @vendor_zipcode 
							WHERE address_pk = @address_pk and person_pk = @person_pk

							-- Execute SMA Rules
							EXEC performSystemAddressCheck @address_pk, @person_pk, @IsSMA output
							IF @IsSMA = 1
								EXEC SetPersonSMA @address_pk, @person_pk
						END
					END
					ELSE
					BEGIN
						print 'In'
						print @vendor_zipcode 
						IF @vendor_zipcode IS NULL 
							INSERT INTO NCOA_Activity_Dtl(NCOA_Activity_PK, address_pk, person_pk, standard_trans_cd, ncoa_error_cd_pk) 
							values (@NCOA_Activity_PK, @address_pk, @person_pk, @ncoa_transaction_cd, 6)
						ELSE IF ISNUMERIC(@vendor_zipcode) = 0 OR @vendor_zipcode = ''
							INSERT INTO NCOA_Activity_Dtl(NCOA_Activity_PK, address_pk, person_pk, standard_trans_cd, ncoa_error_cd_pk) 
							values (@NCOA_Activity_PK, @address_pk, @person_pk, @ncoa_transaction_cd, 7)
						ELSE
						BEGIN
							EXEC InsertPersonAddress @person_pk, @address_pk output
					
							-- Execute SMA Rules
							EXEC performSystemAddressCheck @address_pk, @person_pk, @IsSMA output
							IF @IsSMA = 1
								EXEC SetPersonSMA @address_pk, @person_pk
						END
					END
				END
				ELSE
				BEGIN
					IF @ncoa_transaction_cd IS NULL 
						INSERT INTO NCOA_Activity_Dtl(NCOA_Activity_PK, address_pk, person_pk, standard_trans_cd, ncoa_error_cd_pk) 
						values (@NCOA_Activity_PK, @address_pk, @person_pk, NULL, 8)
					ELSE
						INSERT INTO NCOA_Activity_Dtl(NCOA_Activity_PK, address_pk, person_pk, standard_trans_cd, ncoa_error_cd_pk) 
						values (@NCOA_Activity_PK, @address_pk, @person_pk, NULL, 1)
				END
			END
			ELSE
			BEGIN
				IF @ncoa_transaction_cd = 'A'
				BEGIN
					-- 'ZIP PLUS 4 CHANGE'
					-- print 'ZIP PLUS 4 CHANGE'
					IF @vendor_zip_plus IS NULL
						INSERT INTO NCOA_Activity_Dtl(NCOA_Activity_PK, address_pk, person_pk, standard_trans_cd, ncoa_error_cd_pk) 
						values (@NCOA_Activity_PK, @address_pk, @person_pk, @ncoa_transaction_cd, 9)
					ELSE IF ISNUMERIC(@vendor_zip_plus) = 0 OR @vendor_zip_plus = ''
						INSERT INTO NCOA_Activity_Dtl(NCOA_Activity_PK, address_pk, person_pk, standard_trans_cd, ncoa_error_cd_pk) 
						values (@NCOA_Activity_PK, @address_pk, @person_pk, @ncoa_transaction_cd, 10)
					ELSE
						UPDATE Person_address SET ZIP_PLUS = @vendor_zip_plus 
						WHERE address_pk = @address_pk and person_pk = @person_pk
				END
				ELSE IF @ncoa_transaction_cd IN('E', 'G', 'K', 'P')
				BEGIN
					-- 'BAD ADDRESS'
					-- 'BAD ADDRESS'
					UPDATE Person_address SET addr_bad_fg = 1, addr_marked_bad_dt = @vendor_ncoa_change_dt 
					WHERE address_pk = @address_pk and person_pk = @person_pk
					
					IF EXISTS(SELECT 1 FROM Person_SMA WHERE person_pk = @person_pk AND 
					address_pk = @address_pk AND current_fg = 1)
					BEGIN
						-- 'Find another SMA'
						SELECT TOP 1 @address_pk = address_pk from Person_Address
						WHERE addr_private_fg = 0 AND addr_bad_fg = 0 AND addr_type = 12001
						AND (dept in (4001, 4004))
						AND (end_dt IS NULL OR end_dt > getdate())
						AND person_pk = @person_pk ORDER BY lst_mod_dt DESC

						IF @@rowcount > 0 
						BEGIN
							EXEC performSystemAddressCheck @address_pk, @person_pk, @IsSMA output
							IF @IsSMA = 1
								EXEC SetPersonSMA @address_pk, @person_pk
							ELSE
								UPDATE Person_SMA SET current_fg=0 WHERE current_fg=1 AND person_pk=@person_pk
						END
					END
				END
				ELSE IF @ncoa_transaction_cd IN ('M', 'S')
				BEGIN
					-- 'MOVE'
					-- print 'MOVE'
					raiserror('Vendor address must be supplied if transaction code indicates Move.', 1, 16)
					INSERT INTO NCOA_Activity_Dtl(NCOA_Activity_PK, address_pk, person_pk, standard_trans_cd, ncoa_error_cd_pk) 
					values (@NCOA_Activity_PK, @address_pk, @person_pk, @ncoa_transaction_cd, 3)
				END
				ELSE IF @ncoa_transaction_cd = 'Z'
				-- 'ZIP CHANGE'
				-- print 'ZIP CHANGE'
				BEGIN
					IF @vendor_zipcode IS NULL 
						INSERT INTO NCOA_Activity_Dtl(NCOA_Activity_PK, address_pk, person_pk, standard_trans_cd, ncoa_error_cd_pk) 
						values (@NCOA_Activity_PK, @address_pk, @person_pk, @ncoa_transaction_cd, 6)
					ELSE IF ISNUMERIC(@vendor_zipcode) = 0 OR @vendor_zipcode = ''
						INSERT INTO NCOA_Activity_Dtl(NCOA_Activity_PK, address_pk, person_pk, standard_trans_cd, ncoa_error_cd_pk) 
						values (@NCOA_Activity_PK, @address_pk, @person_pk, @ncoa_transaction_cd, 7)
					ELSE
						UPDATE Person_address SET zipcode = @vendor_zipcode 
						WHERE address_pk = @address_pk and person_pk = @person_pk
				END
				ELSE
				BEGIN
					IF @ncoa_transaction_cd IS NULL 
						INSERT INTO NCOA_Activity_Dtl(NCOA_Activity_PK, address_pk, person_pk, standard_trans_cd, ncoa_error_cd_pk) 
						values (@NCOA_Activity_PK, @address_pk, @person_pk, NULL, 8)
					ELSE
						INSERT INTO NCOA_Activity_Dtl(NCOA_Activity_PK, address_pk, person_pk, standard_trans_cd, ncoa_error_cd_pk) 
						values (@NCOA_Activity_PK, @address_pk, @person_pk, NULL, 1)
				END
			END

			SELECT @errorCode = @@ERROR
	
			FETCH NEXT FROM NCOA_TRAN_CURSOR INTO 
			@person_pk,
			@address_pk,
			@last_nm,
			@extracted_sma_addr1,
			@extracted_sma_addr2,
			@extracted_sma_city,
			@extracted_sma_state,
			@extracted_sma_zipcode,
			@ncoa_transaction_cd,
			@vendor_key,
			@vendor_addr1,
			@vendor_addr2,
			@vendor_city,
			@vendor_state,
			@vendor_zipcode,
			@vendor_source,
			@vendor_zip_plus,
			@vendor_carrier_route,
			@vendor_ncoa_change_source,
			@vendor_ncoa_change_dt
		END
		CLOSE NCOA_TRAN_CURSOR
		DEALLOCATE NCOA_TRAN_CURSOR
	END
	ELSE
	BEGIN
		Raiserror('No active transaction to process. Job completed.', 1, 16)
		RETURN
	END
	UPDATE NCOA_Activity set ncoa_trans_run_completed_fg = 1, ncoa_trans_run_error_fg = @errorCode, 
	ncoa_trans_run_dt = getdate() WHERE ncoa_activity_pk = @ncoa_activity_pk

	-----------------------
	-- HLM Fix defect #675
	-----------------------      
	IF (@email_addr IS NOT NULL AND LEN(@email_addr) > 0) 
	BEGIN
		-- Build email body 
		IF (@errorCode = 0) 
			SELECT @msg = 'The request for NCOA has been processed successfully.'							  
		ELSE
			SELECT @msg = 'The process for the NCOA request has failed.'

		-- Execute sendmail 
		PRINT ' '
		PRINT 'Execute sendmail: emailAddr=' + @email_addr + ', message=' + @msg
		EXECUTE sendmail 
			@fromName = 'AFSCME IT',
			@fromEmail = 'user@afscme.org',
			@toEmail = @email_addr,
			@subject='AFSCME NCOA Result', 
			@body=@msg
	END
END

GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO

--***************************************************************************************
--* SUMMARY: NCOA data for Acceptance Testing
--***************************************************************************************
INSERT INTO NCOA_Transactions(person_pk, address_pk, last_nm, extracted_sma_addr1, extracted_sma_addr2, extracted_sma_city, extracted_sma_state, extracted_sma_zipcode, vendor_transaction_cd, vendor_key, vendor_addr1, vendor_addr2, vendor_city, vendor_state, vendor_zipcode, vendor_source, vendor_zip_plus, vendor_carrier_route, vendor_ncoa_change_source, vendor_ncoa_change_dt) VALUES( 10544206, 109385, 'HOUSEWORTH', '501 NORTH MAPLE', NULL, 'CALHOUN', 'MO', '65323', '10', NULL, '501 NORTH MAPLE', NULL, 'CALHOUN', 'MO', '65323', NULL, '4000', NULL, NULL,  GetDate() )
INSERT INTO NCOA_Transactions(person_pk, address_pk, last_nm, extracted_sma_addr1, extracted_sma_addr2, extracted_sma_city, extracted_sma_state, extracted_sma_zipcode, vendor_transaction_cd, vendor_key, vendor_addr1, vendor_addr2, vendor_city, vendor_state, vendor_zipcode, vendor_source, vendor_zip_plus, vendor_carrier_route, vendor_ncoa_change_source, vendor_ncoa_change_dt) VALUES( 10544208, 109561, 'HOWE', '198 SE 685TH RD', NULL, 'WARRENSBURG', 'MO', '64093', '16', NULL, '198 SE 685TH RD', NULL, 'WARRENSBURG', 'MO', '64093', NULL, NULL,  NULL, NULL,  GetDate() )
INSERT INTO NCOA_Transactions(person_pk, address_pk, last_nm, extracted_sma_addr1, extracted_sma_addr2, extracted_sma_city, extracted_sma_state, extracted_sma_zipcode, vendor_transaction_cd, vendor_key, vendor_addr1, vendor_addr2, vendor_city, vendor_state, vendor_zipcode, vendor_source, vendor_zip_plus, vendor_carrier_route, vendor_ncoa_change_source, vendor_ncoa_change_dt) VALUES( 10544209, 109386, 'IRVINE', '163 SOUTHWEST 601', NULL, 'CENTERVIEW', 'MO', '64019', '16', NULL, '163 SOUTHWEST 601', NULL, 'CENTERVIEW', 'MO', '64019', NULL, NULL,  NULL, NULL,  GetDate() )
INSERT INTO NCOA_Transactions(person_pk, address_pk, last_nm, extracted_sma_addr1, extracted_sma_addr2, extracted_sma_city, extracted_sma_state, extracted_sma_zipcode, vendor_transaction_cd, vendor_key, vendor_addr1, vendor_addr2, vendor_city, vendor_state, vendor_zipcode, vendor_source, vendor_zip_plus, vendor_carrier_route, vendor_ncoa_change_source, vendor_ncoa_change_dt) VALUES( 10544210, 109758, 'LAWSON', '100 SOUTH MITCHELL', NULL, 'WARRENSBURG', 'MO', '64093', '16', NULL, '100 SOUTH MITCHELL', NULL, 'WARRENSBURG', 'MO', '64093', NULL, NULL,  NULL, NULL,  GetDate() )
INSERT INTO NCOA_Transactions(person_pk, address_pk, last_nm, extracted_sma_addr1, extracted_sma_addr2, extracted_sma_city, extracted_sma_state, extracted_sma_zipcode, vendor_transaction_cd, vendor_key, vendor_addr1, vendor_addr2, vendor_city, vendor_state, vendor_zipcode, vendor_source, vendor_zip_plus, vendor_carrier_route, vendor_ncoa_change_source, vendor_ncoa_change_dt) VALUES( 10544216, 109724, 'MOLNAR', '675 NE MM HWY', NULL, 'KNOB NOSTER', 'MO', '65336', '14', NULL, '2451 NW ROSE STREET', NULL, 'KNOB NOSTER', 'MO', '65336', NULL, NULL,  NULL, NULL,  GetDate() )
INSERT INTO NCOA_Transactions(person_pk, address_pk, last_nm, extracted_sma_addr1, extracted_sma_addr2, extracted_sma_city, extracted_sma_state, extracted_sma_zipcode, vendor_transaction_cd, vendor_key, vendor_addr1, vendor_addr2, vendor_city, vendor_state, vendor_zipcode, vendor_source, vendor_zip_plus, vendor_carrier_route, vendor_ncoa_change_source, vendor_ncoa_change_dt) VALUES( 10544217, 109868, 'MORRILL', '24 SW 600', NULL, 'WARRENSBURG', 'MO', '64093', '14', NULL, '24 SW 600', NULL, 'WARRENSBURG', 'MO', '64093', NULL, NULL,  NULL, NULL,  GetDate() )
INSERT INTO NCOA_Transactions(person_pk, address_pk, last_nm, extracted_sma_addr1, extracted_sma_addr2, extracted_sma_city, extracted_sma_state, extracted_sma_zipcode, vendor_transaction_cd, vendor_key, vendor_addr1, vendor_addr2, vendor_city, vendor_state, vendor_zipcode, vendor_source, vendor_zip_plus, vendor_carrier_route, vendor_ncoa_change_source, vendor_ncoa_change_dt) VALUES( 10544218, 109455, 'PATILLO', '300 ZOLL APT. A', NULL, 'WARRENSBURG', 'MO', '64093', '14', NULL, '300 ZOLL APT. A', NULL, 'WARRENSBURG', 'MO', '64093', NULL, NULL,  NULL, NULL,  GetDate() )
INSERT INTO NCOA_Transactions(person_pk, address_pk, last_nm, extracted_sma_addr1, extracted_sma_addr2, extracted_sma_city, extracted_sma_state, extracted_sma_zipcode, vendor_transaction_cd, vendor_key, vendor_addr1, vendor_addr2, vendor_city, vendor_state, vendor_zipcode, vendor_source, vendor_zip_plus, vendor_carrier_route, vendor_ncoa_change_source, vendor_ncoa_change_dt) VALUES( 10544219, 109678, 'PERRY', '241 SE 111TH ROAD', NULL, 'WARRENSBURG', 'MO', '64093', '16', NULL, '445 NORTHWEST ROSE STREET', NULL, 'CALHOUN', 'MO', '65323', NULL, NULL,  NULL, NULL,  GetDate() )
INSERT INTO NCOA_Transactions(person_pk, address_pk, last_nm, extracted_sma_addr1, extracted_sma_addr2, extracted_sma_city, extracted_sma_state, extracted_sma_zipcode, vendor_transaction_cd, vendor_key, vendor_addr1, vendor_addr2, vendor_city, vendor_state, vendor_zipcode, vendor_source, vendor_zip_plus, vendor_carrier_route, vendor_ncoa_change_source, vendor_ncoa_change_dt) VALUES( 10544220, 109531, 'PIERCE', '403 WEST MILLER', NULL, 'APPLETON CITY', 'MO', '64724', '16', NULL, '403 WEST MILLER', NULL, 'APPLETON CITY', 'MO', '64724', NULL, NULL,  NULL, NULL,  GetDate() )
INSERT INTO NCOA_Transactions(person_pk, address_pk, last_nm, extracted_sma_addr1, extracted_sma_addr2, extracted_sma_city, extracted_sma_state, extracted_sma_zipcode, vendor_transaction_cd, vendor_key, vendor_addr1, vendor_addr2, vendor_city, vendor_state, vendor_zipcode, vendor_source, vendor_zip_plus, vendor_carrier_route, vendor_ncoa_change_source, vendor_ncoa_change_dt) VALUES( 10544221, 109699, 'PULLIAM', '280 NORTHWEST 21 HWY', NULL, 'WARRENSBURG', 'MO', '64093', '16', NULL, '280 NORTHWEST 21 HWY', NULL, 'WARRENSBURG', 'MO', '64093', NULL, NULL,  NULL, NULL,  GetDate() )
INSERT INTO NCOA_Transactions(person_pk, address_pk, last_nm, extracted_sma_addr1, extracted_sma_addr2, extracted_sma_city, extracted_sma_state, extracted_sma_zipcode, vendor_transaction_cd, vendor_key, vendor_addr1, vendor_addr2, vendor_city, vendor_state, vendor_zipcode, vendor_source, vendor_zip_plus, vendor_carrier_route, vendor_ncoa_change_source, vendor_ncoa_change_dt) VALUES( 10544222, 109532, 'RIBAS', '663 SOUTHEAST 1250', NULL, 'LEETON', 'MO', '64761', '16', NULL, '663 SOUTHEAST 1250', NULL, 'LEETON', 'MO', '64761', NULL, NULL,  NULL, NULL,  GetDate() )
INSERT INTO NCOA_Transactions(person_pk, address_pk, last_nm, extracted_sma_addr1, extracted_sma_addr2, extracted_sma_city, extracted_sma_state, extracted_sma_zipcode, vendor_transaction_cd, vendor_key, vendor_addr1, vendor_addr2, vendor_city, vendor_state, vendor_zipcode, vendor_source, vendor_zip_plus, vendor_carrier_route, vendor_ncoa_change_source, vendor_ncoa_change_dt) VALUES( 10544223, 109700, 'RIGGS', '176 SE COUNTY ROAD Y, LOT 3', NULL, 'WARRENSBURG', 'MO', '64093', '16', NULL, '176 SE COUNTY ROAD Y, LOT 3', NULL, 'WARRENSBURG', 'MO', '64093', NULL, NULL,  NULL, NULL,  GetDate() )
INSERT INTO NCOA_Transactions(person_pk, address_pk, last_nm, extracted_sma_addr1, extracted_sma_addr2, extracted_sma_city, extracted_sma_state, extracted_sma_zipcode, vendor_transaction_cd, vendor_key, vendor_addr1, vendor_addr2, vendor_city, vendor_state, vendor_zipcode, vendor_source, vendor_zip_plus, vendor_carrier_route, vendor_ncoa_change_source, vendor_ncoa_change_dt) VALUES( 10544224, 109869, 'ROBERSON', '17 SE 250 VALLEY VIEW RD.', NULL, 'WARRENSBURG', 'MO', '64093', NULL, NULL,  '17 SE 250 VALLEY VIEW RD.', NULL, 'WARRENSBURG', 'MO', '64093', NULL, NULL,  NULL, NULL,  GetDate() )
INSERT INTO NCOA_Transactions(person_pk, address_pk, last_nm, extracted_sma_addr1, extracted_sma_addr2, extracted_sma_city, extracted_sma_state, extracted_sma_zipcode, vendor_transaction_cd, vendor_key, vendor_addr1, vendor_addr2, vendor_city, vendor_state, vendor_zipcode, vendor_source, vendor_zip_plus, vendor_carrier_route, vendor_ncoa_change_source, vendor_ncoa_change_dt) VALUES( 10544225, 109387, 'SCHNAKENBERG', 'RT. 3 BOX 174', NULL, 'CONCORDIA', 'MO', '64020', '16', NULL, 'RT. 3 BOX 174', NULL, 'CONCORDIA', 'MO', '64020', NULL, NULL,  NULL, NULL,  GetDate() )
INSERT INTO NCOA_Transactions(person_pk, address_pk, last_nm, extracted_sma_addr1, extracted_sma_addr2, extracted_sma_city, extracted_sma_state, extracted_sma_zipcode, vendor_transaction_cd, vendor_key, vendor_addr1, vendor_addr2, vendor_city, vendor_state, vendor_zipcode, vendor_source, vendor_zip_plus, vendor_carrier_route, vendor_ncoa_change_source, vendor_ncoa_change_dt) VALUES( 10544226, 109562, 'SCHNEIDER', '242 SE 111', NULL, 'WARRENSBURG', 'MO', '64093', '16', NULL, '242 SE 111', NULL, 'WARRENSBURG', 'MO', '64093', NULL, NULL,  NULL, NULL,  GetDate() )
INSERT INTO NCOA_Transactions(person_pk, address_pk, last_nm, extracted_sma_addr1, extracted_sma_addr2, extracted_sma_city, extracted_sma_state, extracted_sma_zipcode, vendor_transaction_cd, vendor_key, vendor_addr1, vendor_addr2, vendor_city, vendor_state, vendor_zipcode, vendor_source, vendor_zip_plus, vendor_carrier_route, vendor_ncoa_change_source, vendor_ncoa_change_dt) VALUES( 11308373, 872587, 'ARABSHAHI', '11622 GONSALVES ST', NULL, 'CERRITOS', 'CA', '90701', '14', NULL, '11622 GONSALVES ST', NULL, 'CERRITOS', 'CA', '90701', NULL, NULL,  NULL, NULL,  GetDate() )
INSERT INTO NCOA_Transactions(person_pk, address_pk, last_nm, extracted_sma_addr1, extracted_sma_addr2, extracted_sma_city, extracted_sma_state, extracted_sma_zipcode, vendor_transaction_cd, vendor_key, vendor_addr1, vendor_addr2, vendor_city, vendor_state, vendor_zipcode, vendor_source, vendor_zip_plus, vendor_carrier_route, vendor_ncoa_change_source, vendor_ncoa_change_dt) VALUES( 11308376, 872528, 'ARDALAN', '341 SANDLEWOOD AVE.', NULL, 'LA HABRA', 'CA', '90631', '14', NULL, '341 SANDLEWOOD AVE.', NULL, 'LA HABRA', 'CA', '90631', NULL, NULL,  NULL, NULL,  GetDate() )
INSERT INTO NCOA_Transactions(person_pk, address_pk, last_nm, extracted_sma_addr1, extracted_sma_addr2, extracted_sma_city, extracted_sma_state, extracted_sma_zipcode, vendor_transaction_cd, vendor_key, vendor_addr1, vendor_addr2, vendor_city, vendor_state, vendor_zipcode, vendor_source, vendor_zip_plus, vendor_carrier_route, vendor_ncoa_change_source, vendor_ncoa_change_dt) VALUES( 11308388, 872530, 'AZMI', '2338 ANABAS AVE.', NULL, 'SAN PEDRO', 'CA', '90732', '14', NULL, '2338 ANABAS AVE.', NULL, 'SAN PEDRO', 'CA', '90732', NULL, NULL,  NULL, NULL,  GetDate() )
INSERT INTO NCOA_Transactions(person_pk, address_pk, last_nm, extracted_sma_addr1, extracted_sma_addr2, extracted_sma_city, extracted_sma_state, extracted_sma_zipcode, vendor_transaction_cd, vendor_key, vendor_addr1, vendor_addr2, vendor_city, vendor_state, vendor_zipcode, vendor_source, vendor_zip_plus, vendor_carrier_route, vendor_ncoa_change_source, vendor_ncoa_change_dt) VALUES( 11308754, 872880, 'FERNANDO', '3855 LANDFAIR RD', NULL, 'PASADENA', 'CA', '91107', '10', NULL, '3855 LANDFAIR RD', NULL, 'PASADENA', 'CA', '91107', NULL, '3212', NULL, NULL,  GetDate() )
INSERT INTO NCOA_Transactions(person_pk, address_pk, last_nm, extracted_sma_addr1, extracted_sma_addr2, extracted_sma_city, extracted_sma_state, extracted_sma_zipcode, vendor_transaction_cd, vendor_key, vendor_addr1, vendor_addr2, vendor_city, vendor_state, vendor_zipcode, vendor_source, vendor_zip_plus, vendor_carrier_route, vendor_ncoa_change_source, vendor_ncoa_change_dt) VALUES( 11309243, 873500, 'MATUSAK', NULL, NULL,  NULL, 'CA', NULL, '14', NULL, '71612 CACTUS DR', NULL, '29 PALMS', 'CA', '92210', NULL, NULL,  NULL, NULL,  GetDate() )
INSERT INTO NCOA_Transactions(person_pk, address_pk, last_nm, extracted_sma_addr1, extracted_sma_addr2, extracted_sma_city, extracted_sma_state, extracted_sma_zipcode, vendor_transaction_cd, vendor_key, vendor_addr1, vendor_addr2, vendor_city, vendor_state, vendor_zipcode, vendor_source, vendor_zip_plus, vendor_carrier_route, vendor_ncoa_change_source, vendor_ncoa_change_dt) VALUES( 11309320, 873312, 'MITRA', NULL, NULL,  NULL, 'CA', NULL, '14', NULL, '1122 BELMONT AVE.', NULL, 'LONG BEACH', 'CA', '90804', NULL, NULL,  NULL, NULL,  GetDate() )
INSERT INTO NCOA_Transactions(person_pk, address_pk, last_nm, extracted_sma_addr1, extracted_sma_addr2, extracted_sma_city, extracted_sma_state, extracted_sma_zipcode, vendor_transaction_cd, vendor_key, vendor_addr1, vendor_addr2, vendor_city, vendor_state, vendor_zipcode, vendor_source, vendor_zip_plus, vendor_carrier_route, vendor_ncoa_change_source, vendor_ncoa_change_dt) VALUES( 11309371, 873281, 'NADON', NULL, NULL,  NULL, 'CA', NULL, '15', NULL, 'P.O.BOX 107', NULL, 'DESERT CENTER', 'CA', '92239', NULL, NULL,  NULL, NULL,  GetDate() )
INSERT INTO NCOA_Transactions(person_pk, address_pk, last_nm, extracted_sma_addr1, extracted_sma_addr2, extracted_sma_city, extracted_sma_state, extracted_sma_zipcode, vendor_transaction_cd, vendor_key, vendor_addr1, vendor_addr2, vendor_city, vendor_state, vendor_zipcode, vendor_source, vendor_zip_plus, vendor_carrier_route, vendor_ncoa_change_source, vendor_ncoa_change_dt) VALUES( 11309469, 873369, 'PEREZ', NULL, NULL,  NULL, 'CA', NULL, '14', NULL, '1071 SW. VERDUGO AVE', NULL, 'BURBANK', 'CA', '91505', NULL, NULL,  NULL, NULL,  GetDate() )
INSERT INTO NCOA_Transactions(person_pk, address_pk, last_nm, extracted_sma_addr1, extracted_sma_addr2, extracted_sma_city, extracted_sma_state, extracted_sma_zipcode, vendor_transaction_cd, vendor_key, vendor_addr1, vendor_addr2, vendor_city, vendor_state, vendor_zipcode, vendor_source, vendor_zip_plus, vendor_carrier_route, vendor_ncoa_change_source, vendor_ncoa_change_dt) VALUES( 11309630, 873754, 'SCHLANG', '240 18TH ST.', NULL, 'SANTA MONICA', 'CA', NULL, '17', NULL, '240 18TH ST.', NULL, 'SANTA MONICA', 'CA', '90065', NULL, NULL,  NULL, NULL,  GetDate() )
INSERT INTO NCOA_Transactions(person_pk, address_pk, last_nm, extracted_sma_addr1, extracted_sma_addr2, extracted_sma_city, extracted_sma_state, extracted_sma_zipcode, vendor_transaction_cd, vendor_key, vendor_addr1, vendor_addr2, vendor_city, vendor_state, vendor_zipcode, vendor_source, vendor_zip_plus, vendor_carrier_route, vendor_ncoa_change_source, vendor_ncoa_change_dt) VALUES( 11309645, 873381, 'SEARCY', NULL, NULL,  NULL, 'CA', NULL, '14', NULL, '239 18TH ST.', NULL, 'SANTA MONICA', 'CA', '90065', NULL, NULL,  NULL, NULL,  GetDate() )
INSERT INTO NCOA_Transactions(person_pk, address_pk, last_nm, extracted_sma_addr1, extracted_sma_addr2, extracted_sma_city, extracted_sma_state, extracted_sma_zipcode, vendor_transaction_cd, vendor_key, vendor_addr1, vendor_addr2, vendor_city, vendor_state, vendor_zipcode, vendor_source, vendor_zip_plus, vendor_carrier_route, vendor_ncoa_change_source, vendor_ncoa_change_dt) VALUES( 11309676, 873736, 'SIMONEK', NULL, NULL,  NULL, 'CA', NULL, '16', NULL, '3500  MONOGRM AVE.', NULL, 'LONG BEACH', 'CA', '90808', NULL, NULL,  NULL, NULL,  GetDate() )
INSERT INTO NCOA_Transactions(person_pk, address_pk, last_nm, extracted_sma_addr1, extracted_sma_addr2, extracted_sma_city, extracted_sma_state, extracted_sma_zipcode, vendor_transaction_cd, vendor_key, vendor_addr1, vendor_addr2, vendor_city, vendor_state, vendor_zipcode, vendor_source, vendor_zip_plus, vendor_carrier_route, vendor_ncoa_change_source, vendor_ncoa_change_dt) VALUES( 11309687, 873583, 'SLIDER', NULL, NULL,  NULL, 'CA', NULL, '16', NULL, '4508  MONOGRM AVE.', NULL, 'LONG BEACH', 'CA', '90808', NULL, NULL,  NULL, NULL,  GetDate() )
INSERT INTO NCOA_Transactions(person_pk, address_pk, last_nm, extracted_sma_addr1, extracted_sma_addr2, extracted_sma_city, extracted_sma_state, extracted_sma_zipcode, vendor_transaction_cd, vendor_key, vendor_addr1, vendor_addr2, vendor_city, vendor_state, vendor_zipcode, vendor_source, vendor_zip_plus, vendor_carrier_route, vendor_ncoa_change_source, vendor_ncoa_change_dt) VALUES( 11402029, 965665, 'ACEVEDO', '1614 SUMMIT AVE APT 8', NULL, 'UNION CITY', 'NJ', '07087', '15', NULL, '1614 SUMMIT AVE APT 8', NULL, 'UNION CITY', 'NJ', '07087', NULL, NULL,  NULL, NULL,  GetDate() )
INSERT INTO NCOA_Transactions(person_pk, address_pk, last_nm, extracted_sma_addr1, extracted_sma_addr2, extracted_sma_city, extracted_sma_state, extracted_sma_zipcode, vendor_transaction_cd, vendor_key, vendor_addr1, vendor_addr2, vendor_city, vendor_state, vendor_zipcode, vendor_source, vendor_zip_plus, vendor_carrier_route, vendor_ncoa_change_source, vendor_ncoa_change_dt) VALUES( 11402030, 966202, 'ACOSTA', '28 MILTON AVE APT 2', NULL, 'JERSEY CITY', 'NJ', '07307', '14', NULL, '28 MILTON AVE APT 2', NULL, 'JERSEY CITY', 'NJ', '07307', NULL, NULL,  NULL, NULL,  GetDate() )
INSERT INTO NCOA_Transactions(person_pk, address_pk, last_nm, extracted_sma_addr1, extracted_sma_addr2, extracted_sma_city, extracted_sma_state, extracted_sma_zipcode, vendor_transaction_cd, vendor_key, vendor_addr1, vendor_addr2, vendor_city, vendor_state, vendor_zipcode, vendor_source, vendor_zip_plus, vendor_carrier_route, vendor_ncoa_change_source, vendor_ncoa_change_dt) VALUES( 11402031, 966171, 'ADAMS', '32 WINFIELD AVE', NULL, 'JERSEY CITY', 'NJ', '07305', '14', NULL, '32 WINFIELD AVE', NULL, 'JERSEY CITY', 'NJ', '07305', NULL, NULL,  NULL, NULL,  GetDate() )

GO



----------------------------
-- insertCodes.sql Changes
----------------------------

----------------------------
-- fix for State Description typos
-- (includes DE, MA, OK - defect #783)
----------------------------

UPDATE Common_Codes
SET com_cd_desc = 'Delaware'
WHERE com_cd_pk = 10008
AND   com_cd_cd = 'DE'


UPDATE Common_Codes
SET com_cd_desc = 'Massachusetts'
WHERE com_cd_pk = 10019
AND   com_cd_cd = 'MA'


UPDATE Common_Codes
SET com_cd_desc = 'Oklahoma'
WHERE com_cd_pk = 10036
AND   com_cd_cd = 'OK'


UPDATE Common_Codes
SET com_cd_desc = 'Delaware'
WHERE com_cd_pk = 5009
AND   com_cd_cd = 'DE'


UPDATE Common_Codes
SET com_cd_desc = 'Massachusetts'
WHERE com_cd_pk = 5020
AND   com_cd_cd = 'MA'


UPDATE Common_Codes
SET com_cd_desc = 'Oklahoma'
WHERE com_cd_pk = 5037
AND   com_cd_cd = 'OK'


GO


----------------------------
-- insertRoles.sql Changes
----------------------------

----------------------------
-- add privileges for Vendor (role_pk = 3) if it does NOT exist
-- (defect #799)
----------------------------

if NOT exists (select * from Roles_Privileges where role_pk = 3 and privilege_key = 'SearchVendorMember')

INSERT INTO Roles_Privileges (role_pk, privilege_key) 
SELECT 3, privilege_key FROM Privileges
	WHERE privilege_key = 'SearchVendorMember'
	

GO


----------------------------
-- createViews.sql Changes
----------------------------

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[V_Affiliate]') and OBJECTPROPERTY(id, N'IsView') = 1)
drop view [dbo].[V_Affiliate]
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[V_Member]') and OBJECTPROPERTY(id, N'IsView') = 1)
drop view [dbo].[V_Member]
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[V_Person_Address]') and OBJECTPROPERTY(id, N'IsView') = 1)
drop view [dbo].[V_Person_Address]
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[V_Officer]') and OBJECTPROPERTY(id, N'IsView') = 1)
drop view [dbo].[V_Officer]
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[V_Officer_Address]') and OBJECTPROPERTY(id, N'IsView') = 1)
drop view [dbo].[V_Officer_Address]


GO

CREATE VIEW V_Person_Address  AS
SELECT  paddr.person_pk,

--address
	CASE addr_prmry_fg WHEN 0 THEN 'False' WHEN 1 then 'True' ELSE null END as prmry_fg,
	CASE addr_bad_fg WHEN 0 THEN 'False' WHEN 1 then 'True' ELSE null END as bad_fg,
	CASE addr_private_fg WHEN 0 THEN 'False' WHEN 1 then 'True' ELSE null END as private_fg,
	addr1, addr2, city, states.com_cd_desc as state, zipcode, zip_plus,
	ISNULL(paddr.addr1, '') +
	ISNULL(' ' + paddr.addr2, '') +
	ISNULL(' ' + paddr.city + ',', '') +
	ISNULL(' ' + paddr.state, '') +
	ISNULL(' ' + paddr.zipcode, '') + 
	ISNULL('-' + paddr.zip_plus, '')
	AS full_address,
	states.com_cd_cd as state_cd,
	states.com_cd_desc as state_desc, province,
	paddr.carrier_route_info ,
	countries.com_cd_desc as country,
	'1' + CONVERT(varchar, paddr.address_pk) as address_id,
	addrtype.com_cd_desc as addr_type,
	addr_marked_bad_dt as bad_dt,
	addr_lst_mod_user.user_id as addr_lst_mod_user_id,
	paddr.lst_mod_dt as addr_lst_mod_dt, 
	addr_created_user.user_id as addr_created_user_id,
	paddr.created_dt as addr_created_dt 
FROM	Person_Address paddr
LEFT OUTER JOIN Common_Codes states ON states.com_cd_cd = paddr.state AND states.com_cd_type_key = 'State' 
LEFT OUTER JOIN Common_Codes countries ON paddr.country = countries.com_cd_pk 
LEFT OUTER JOIN Common_Codes addrtype ON paddr.addr_type = addrtype.com_cd_pk 
LEFT OUTER JOIN Users addr_created_user ON paddr.created_user_pk = addr_created_user.person_pk
LEFT OUTER JOIN Users addr_lst_mod_user ON paddr.lst_mod_user_pk = addr_lst_mod_user.person_pk
WHERE (end_dt IS NULL OR end_dt > getdate())


GO


CREATE VIEW V_Member AS
SELECT
--member
	m.person_pk, 
	m.aff_pk, member_types.com_cd_desc as mbr_type, mbr_type as mbr_type_pk,
	member_statuses.com_cd_desc as mbr_status, mbr_status as mbr_status_pk,
	mbr_join_dt, mbr_retired_dt, mbr_card_sent_dt,
	CASE no_mail_fg WHEN 0 THEN 'False' WHEN 1 then 'True' ELSE null END as no_mail_fg,
	CASE lost_time_language_fg WHEN 0 THEN 'False' WHEN 1 then 'True' ELSE null END as lost_time_language_fg,
	dues_types.com_cd_desc as dues_type,
	mbr_dues_rate as dues_rate,
	dues_freq.com_cd_desc as dues_frequency,
	CASE mbr_ret_dues_renewal_fg WHEN 0 THEN 'False' WHEN 1 then 'True' ELSE null END as ret_dues_renewal_fg,
--record info
	lst_mod_user.user_id as lst_mod_user_id,
	m.lst_mod_dt, 
	created_user.user_id as created_user_id,
	m.created_dt, 
--employer
	employer_name,
	emp_job_titles.com_cd_desc as emp_job_title,
	emp_sectors.com_cd_desc as emp_sector,
	emp_salary_ranges.com_cd_desc as emp_salary_range,
	employee_salary,
	emp_job_site
FROM Aff_Members m
LEFT OUTER JOIN Aff_Mbr_Employers emp ON emp.aff_pk = m.aff_pk AND emp.person_pk = m.person_pk
LEFT OUTER JOIN Common_Codes emp_job_titles ON emp.emp_job_title = emp_job_titles.com_cd_pk
LEFT OUTER JOIN Common_Codes emp_sectors ON emp.emp_sector = emp_sectors.com_cd_pk
LEFT OUTER JOIN Common_Codes emp_salary_ranges ON emp.emp_salary_range = emp_salary_ranges.com_cd_pk
LEFT OUTER JOIN Common_Codes dues_types ON m.mbr_dues_type = dues_types.com_cd_pk
LEFT OUTER JOIN Common_Codes dues_freq ON m.mbr_dues_frequency = dues_freq.com_cd_pk
LEFT OUTER JOIN Common_Codes member_types ON m.mbr_type = member_types.com_cd_pk
LEFT OUTER JOIN Common_Codes member_statuses ON m.mbr_status = member_statuses.com_cd_pk
INNER JOIN Users created_user ON m.created_user_pk = created_user.person_pk
INNER JOIN Users lst_mod_user ON m.lst_mod_user_pk = lst_mod_user.person_pk

GO

create view V_Officer_Address  as
select 
 o.person_pk,
 addr1, 
 addr2, 
 city,
 states.com_cd_desc as state,
 states.com_cd_cd as state_cd,
 zipcode,
 zip_plus,
 province, countries.com_cd_desc as country,
 pa.lst_mod_user_pk,
 pa.lst_mod_dt,
 pa.created_user_pk,
 pa.created_dt,
 '1' + CONVERT(varchar, address_pk) as address_id
FROM Officer_History o
INNER JOIN Person_Address pa ON o.pos_addr_from_person_pk = pa.address_pk
LEFT OUTER JOIN Common_Codes states ON states.com_cd_cd = pa.state AND states.com_cd_type_key = 'State' 
LEFT OUTER JOIN Common_Codes countries ON pa.country = countries.com_cd_pk 
UNION
select 
 o.person_pk,
 addr1, 
 addr2, 
 city,
 states.com_cd_desc as state,
 states.com_cd_cd as state_cd,
 zipcode,
 zip_plus,
 province, countries.com_cd_desc as country,
 oa.lst_mod_user_pk,
 oa.lst_mod_dt,
 oa.created_user_pk,
 oa.created_dt,
 '2' + CONVERT(varchar, org_addr_pk) as address_id
FROM Officer_History o
INNER JOIN Org_Address oa ON o.pos_addr_from_org_pk = oa.org_addr_pk
LEFT OUTER JOIN Common_Codes states ON states.com_cd_cd = oa.state AND states.com_cd_type_key = 'State' 
LEFT OUTER JOIN Common_Codes countries ON oa.country = countries.com_cd_pk 

GO

CREATE VIEW V_Officer  AS
SELECT
--officer
 groups.aff_pk, o.person_pk, titles.com_cd_desc AS title, o.pos_start_dt, o.pos_end_dt, o.pos_expiration_dt,
 CASE pos_steward_fg WHEN 0 THEN 'False' WHEN 1 then 'True' ELSE null END as pos_steward_fg,
 CASE suspended_fg WHEN 0 THEN 'False' WHEN 1 then 'True' ELSE null END as suspended_fg,
--record info
 lst_mod_user.user_id as lst_mod_user_id,
 o.lst_mod_dt, 
 created_user.user_id as created_user_id,
 o.created_dt, 
--address
 address_id, addr1,  addr2,  city, state, state_cd, zipcode, zip_plus, province, country,
 ISNULL(addr1, '') +
 ISNULL(' ' + addr2, '') +
 ISNULL(' ' + city + ',', '') +
 ISNULL(' ' + state, '') +
 ISNULL(' ' + zipcode, '') +
 ISNULL('-' + zip_plus, '')
 as full_address,
 addr_lst_mod_user.user_id as addr_lst_mod_user_id,
 addr.lst_mod_dt as addr_lst_mod_dt, 
 addr_created_user.user_id as addr_created_user_id,
 addr.created_dt as addr_created_dt 
FROM Officer_History o
LEFT OUTER JOIN V_Officer_Address addr ON addr.person_pk = o.person_pk
INNER JOIN Aff_Officer_Groups groups ON groups.office_group_id = o.office_group_id AND o.aff_pk = groups.aff_pk
INNER JOIN AFSCME_Offices offices ON o.afscme_office_pk = offices.afscme_office_pk 
INNER JOIN Common_Codes titles ON titles.com_cd_pk = offices.afscme_title_nm
INNER JOIN Users created_user ON o.created_user_pk = created_user.person_pk
INNER JOIN Users lst_mod_user ON o.lst_mod_user_pk = lst_mod_user.person_pk
LEFT OUTER JOIN Users addr_created_user ON addr.created_user_pk = addr_created_user.person_pk
LEFT OUTER JOIN Users addr_lst_mod_user ON addr.lst_mod_user_pk = addr_lst_mod_user.person_pk

GO

CREATE VIEW V_Affiliate  AS
SELECT a.aff_pk,
	 ISNULL(aff_type, '') +
	 + ISNULL(aff_localSubChapter, '') +
	 + ISNULL(aff_stateNat_type, '') +
	 + ISNULL(aff_subUnit, '') +
	 + ISNULL(aff_councilRetiree_chap, '')
	 as affiliate_id,
	aff_abbreviated_nm, aff_type, aff_localSubChapter, affStates.com_cd_desc as aff_stateNat_type, 
	aff_subUnit, aff_councilRetiree_chap, affStates.com_cd_cd as aff_stateNat_cd, 
	affiliate_statuses.com_cd_desc as aff_status, aff_status as aff_status_pk,
	card_runs.com_cd_desc as ann_card_run_type,
	renewals.com_cd_desc as mbr_renewal,
--mbrshp reporting info
	CASE unit_wide_no_mbr_cards_fg WHEN 0 THEN 'False' WHEN 1 then 'True' ELSE null END as unit_wide_no_mbr_cards_fg,
	CASE unit_wide_no_pe_mail_fg WHEN 0 THEN 'False' WHEN 1 then 'True' ELSE null END as unit_wide_no_pe_mail_fg,
--record info
	lst_mod_user.user_id as lst_mod_user_id,
	a.lst_mod_dt, 
	created_user.user_id as created_user_id,
	a.created_dt, 
--constitution
	CASE approved_const_fg WHEN 0 THEN 'False' WHEN 1 then 'True' ELSE null END as approved_const_fg,
	CASE auto_delegate_prvsn_fg WHEN 0 THEN 'False' WHEN 1 then 'True' ELSE null END as auto_delegate_prvsn_fg,
	most_current_approval_dt, aff_agreement_dt, 
	off_elections.com_cd_desc as off_election_method,
	CASE const_regions_fg WHEN 0 THEN 'False' WHEN 1 then 'True' ELSE null END as const_regions_fg,
	mtg_freq.com_cd_desc as meeting_frequency,	
--address
	addr1, addr2, city, states.com_cd_desc as state, zipcode, zip_plus, attention_line,
	'2' + CONVERT(varchar, org_addr_pk) as address_id, states.com_cd_cd as state_cd, 
	 province, countries.com_cd_desc as country,
	 CASE l.location_primary_fg WHEN 0 THEN 'False' WHEN 1 then 'True' ELSE null END as location_primary_fg,
	addrtype.com_cd_desc as addr_type,
	CASE addr_bad_fg WHEN 0 THEN 'False' WHEN 1 then 'True' ELSE null END as bad_fg,
	addr_bad_dt as bad_dt,
 ISNULL(addr1, '') +
 ISNULL(' ' + addr2, '') +
 ISNULL(' ' + city + ',', '') +
 ISNULL(' ' + state, '') +
 ISNULL(' ' + zipcode, '') +
 ISNULL('-' + zip_plus, '')
 as full_address,
 addr_lst_mod_user.user_id as addr_lst_mod_user_id,
 addr.lst_mod_dt as addr_lst_mod_dt, 
 addr_created_user.user_id as addr_created_user_id,
 addr.created_dt as addr_created_dt, 
--phone
 	phone_types.com_cd_desc as phone_type,
	ISNULL(p.country_cd + ' ', '') +
	ISNULL(p.area_code + '-', '') +
	ISNULL(p.phone_no, '') +
	ISNULL(' ' + p.phone_extension, '')
	AS full_phone,
	p.area_code, p.phone_no, p.country_cd, p.phone_extension,
	CASE phone_bad_fg WHEN 0 THEN 'False' WHEN 1 then 'True' ELSE null END as phone_bad_fg,
	phone_bad_dt as phone_bad_dt,
	phone_lst_mod_user.user_id as phone_lst_mod_user_id,
	p.lst_mod_dt as phone_lst_mod_dt, 
	phone_created_user.user_id as phone_created_user_id,
	p.created_dt as phone_created_dt 
FROM Aff_Organizations a
LEFT OUTER JOIN Aff_Mbr_Rpt_Info amri ON a.aff_pk = amri.aff_pk
LEFT OUTER JOIN Aff_Constitution ac ON a.aff_pk = ac.aff_pk
LEFT OUTER JOIN Org_Locations l ON a.aff_pk = l.org_pk AND l.location_primary_fg = 1
LEFT OUTER JOIN Org_Phone p ON p.org_locations_pk = l.org_locations_pk
LEFT OUTER JOIN Org_Address addr ON addr.org_locations_pk = l.org_locations_pk
LEFT OUTER JOIN Common_Codes addrtype ON addr.org_addr_type = addrtype.com_cd_pk
LEFT OUTER JOIN Common_Codes phone_types ON p.org_phone_type = phone_types.com_cd_pk
LEFT OUTER JOIN Common_Codes states ON states.com_cd_cd = addr.state AND states.com_cd_type_key = 'State' 
LEFT OUTER JOIN Common_Codes affStates ON affStates.com_cd_cd = a.aff_stateNat_type AND affStates.com_cd_type_key = 'AffiliateState' 
LEFT OUTER JOIN Common_Codes countries ON addr.country = countries.com_cd_pk 
LEFT OUTER JOIN Common_Codes affiliate_statuses ON a.aff_status = affiliate_statuses.com_cd_pk
LEFT OUTER JOIN Common_Codes card_runs ON a.aff_ann_mbr_card_run_group = card_runs.com_cd_pk
LEFT OUTER JOIN Common_Codes renewals ON a.mbr_renewal = renewals.com_cd_pk
LEFT OUTER JOIN Common_Codes off_elections ON ac.off_election_method = off_elections.com_cd_pk
LEFT OUTER JOIN Common_Codes mtg_freq ON ac.meeting_frequency = mtg_freq.com_cd_pk
INNER JOIN Users created_user ON a.created_user_pk = created_user.person_pk
INNER JOIN Users lst_mod_user ON a.lst_mod_user_pk = lst_mod_user.person_pk
LEFT OUTER JOIN Users addr_created_user ON addr.created_user_pk = addr_created_user.person_pk
LEFT OUTER JOIN Users addr_lst_mod_user ON addr.lst_mod_user_pk = addr_lst_mod_user.person_pk
LEFT OUTER JOIN Users phone_created_user ON p.created_user_pk = phone_created_user.person_pk
LEFT OUTER JOIN Users phone_lst_mod_user ON p.lst_mod_user_pk = phone_lst_mod_user.person_pk


GO


----------------------------
-- insertFields.sql Changes
----------------------------

----------------------------
-- Insert rows to support Mailing Lists
-- (insert state_code fields and aff_type)
-- (Should insert only 5 rows)
----------------------------


INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person_Address', 'S', 'State Code', 'state_cd', 'Address', 1.0)


INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'O', 'V_Officer', 'S', 'State Code', 'state_cd', 'Address', 1.0)


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
	(null, 'A', 'V_Affiliate', 'S', 'State Code', 'state_cd', 'Primary Location Address', 1.0)


GO


----------------------------
-- Insert rows to support Mailing Lists
-- (insert province and country fields and attention_line)
-- (Should insert only 6 rows)
----------------------------


INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'P', 'V_Person_Address', 'S', 'Province', 'province', 'Address', 1.0)


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
	(null, 'A', 'V_Affiliate', 'S', 'Province', 'province', 'Primary Location Address', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	('Country', 'A', 'V_Affiliate', 'C', 'Country', 'country', 'Primary Location Address', 1.0)
INSERT INTO [Report_Fields] 
	(com_cd_type_key, field_entity_type, field_table_nm, field_display_type, field_display_name, field_column_name, field_category_name, field_print_width)
	VALUES
	(null, 'A', 'V_Affiliate', 'S', 'Attention Line', 'attention_line', 'Primary Location Address', 1.0)

GO


----------------------------
-- Update rows for Zip Code 
-- (change to field_display_type from Integer to String for zips)
-- (Should update only 6 rows)
----------------------------

UPDATE [Report_Fields] 
SET field_display_type = 'S'
WHERE field_table_nm = 'V_Person_Address'
AND   field_column_name = 'zipcode'


UPDATE [Report_Fields] 
SET field_display_type = 'S'
WHERE field_table_nm = 'V_Person_Address'
AND   field_column_name = 'zip_plus'


UPDATE [Report_Fields] 
SET field_display_type = 'S'
WHERE field_table_nm = 'V_Officer'
AND   field_column_name = 'zipcode'


UPDATE [Report_Fields] 
SET field_display_type = 'S'
WHERE field_table_nm = 'V_Officer'
AND   field_column_name = 'zip_plus'


UPDATE [Report_Fields] 
SET field_display_type = 'S'
WHERE field_table_nm = 'V_Affiliate'
AND   field_column_name = 'zipcode'


UPDATE [Report_Fields] 
SET field_display_type = 'S'
WHERE field_table_nm = 'V_Affiliate'
AND   field_column_name = 'zip_plus'


GO


----------------------------
-- Insert rows for new fields in the Roles_Report_Fields
-- for Super User (role_pk = 1)
-- (Should insert only 11 rows)
----------------------------

INSERT INTO Roles_Report_Fields (role_pk, report_field_pk) 
SELECT 1, report_field_pk FROM Report_Fields
WHERE report_field_pk > 176

GO



----------------------------
-- insertReports.sql Changes
----------------------------

----------------------------
-- fix for Officer Credential Cards validation error display
----------------------------


UPDATE Report
SET report_handler_class = '/Reporting/Specialized/OfficerCredentialCardsInput.jsp'
WHERE report_pk = 7
AND   report_nm = 'Officer Credential Cards' 


GO

----------------------------
-- Correct invalid member_fg being set in DM
----------------------------

UPDATE p
SET member_fg = 0
FROM Person p
WHERE not exists (SELECT 'x' FROM Aff_Members a WHERE a.person_pk = p.person_pk)
AND member_fg = 1
GO 
