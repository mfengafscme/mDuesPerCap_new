if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[SetPersonSMA]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
drop procedure [dbo].[SetPersonSMA]
GO

SET QUOTED_IDENTIFIER ON 
GO
SET ANSI_NULLS ON 
GO


CREATE  PROCEDURE DBO.SetPersonSMA(@address_pk int, @person_pk int)
as
BEGIN
/*************************************************************************************
* Name: SetPersonSMA
* Desc: Inserts a new record into Person_SMA and mark this address as 
* the System Mailing Address for that person. Also updates the previous SMA.
* Date: 09/25/03
* Author: Sivakumar Jayaraman
* Modified Date: 09/25/03
*  
**************************************************************************************/
	SET NOCOUNT ON
	DECLARE @nextseq int 
	
	SET @nextseq = isnull((SELECT sequence FROM Person_SMA WHERE current_fg=1 AND person_pk = @person_pk), 0)
	
	UPDATE Person_SMA SET current_fg=0 WHERE current_fg=1 AND person_pk=@person_pk
	
	INSERT INTO Person_SMA(address_pk, person_pk, sequence, determined_dt, current_fg) 
	VALUES(@address_pk, @person_pk, @nextseq, getdate(), 1)
END



GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[performSystemAddressCheck]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
drop procedure [dbo].[performSystemAddressCheck]
GO

SET QUOTED_IDENTIFIER ON 
GO
SET ANSI_NULLS ON 
GO

CREATE PROCEDURE dbo.performSystemAddressCheck(@address_pk int, @person_pk int, @IsSMA tinyint output)
as
BEGIN
/*************************************************************************************
* Name: performSystemAddressCheck
* Desc: Duplicate of all rules associated with System Mailing Address. Returns IsSMA 
*	parameter as 1 or 0 depending on whether the address can be made as SMA or not
* Date: 09/25/03
* Author: Sivakumar Jayaraman
* Modified Date: 09/25/03
*  
**************************************************************************************/
DECLARE @ConfTimeFrame tinyint, @extracted_sma_addr1 varchar(50), @vendor_addr1 varchar(50)
DECLARE @extracted_sma_addr2 varchar(50), @vendor_addr2 varchar(50), @extracted_sma_city varchar(25)
DECLARE	@vendor_city varchar(25), @extracted_sma_state char(2), @vendor_state char(2)
DECLARE	@extracted_sma_zipcode varchar(12), @vendor_zipcode varchar(12), @detdate datetime
DECLARE @vendor_ncoa_change_dt varchar(25), @MemberSMAUpdateTimeFrame Tinyint

SELECT @ConfTimeFrame = convert(int, variable_value) FROM Com_App_config_data WHERE 
variable_name = 'ConfigTimeFrame'

SELECT @MemberSMAUpdateTimeFrame = convert(int, variable_value) FROM Com_App_config_data WHERE 
variable_name = 'MemberSMAUpdateTimeFrame'

SELECT @detdate	= determined_dt FROM Person_SMA WHERE person_pk = @person_pk and current_fg = 1

SELECT
	@extracted_sma_addr1 	= extracted_sma_addr1, 
	@vendor_addr1 		= vendor_addr1, 
	@extracted_sma_addr2 	= extracted_sma_addr2, 
	@vendor_addr2 		= vendor_addr2, 
	@extracted_sma_city 	= extracted_sma_city, 
	@vendor_city 		= vendor_city, 
	@extracted_sma_state 	= extracted_sma_state, 
	@vendor_state		= vendor_state, 
	@extracted_sma_zipcode	= extracted_sma_zipcode, 
	@vendor_zipcode		= vendor_zipcode,
	@vendor_ncoa_change_dt	= vendor_ncoa_change_dt
FROM 
	dbo.NCOA_Transactions NCOA 
WHERE
	NCOA.person_pk = @person_pk

Declare @NCOA_Activity_PK int

SELECT @NCOA_Activity_PK = NCOA_ACTIVITY_PK 
FROM NCOA_ACTIVITY 
WHERE convert(varchar, NCOA_Activity_requested_dt, 101) = convert(varchar, getdate(), 101) AND 
NCOA_Trans_run_completed_fg <> 1

IF isdate(@vendor_ncoa_change_dt) = 0	
BEGIN
	raiserror('Invalid effective date in NCOA file. SMA process cannot be completed.', 1, 16)
	
	INSERT INTO NCOA_Activity_Dtl(NCOA_Activity_PK, address_pk, person_pk, standard_trans_cd, ncoa_error_cd_pk) 
	values (@NCOA_Activity_PK, @address_pk, @person_pk, null, 5)
	SET @IsSMA = 0
	RETURN
END

IF datediff(d, getdate(), @detdate) > @MemberSMAUpdateTimeFrame OR @detdate IS NULL OR @detdate = ''
BEGIN
	raiserror('Member has updated the SMA recently. Current address cannot be made SMA.', 1, 16)

	INSERT INTO NCOA_Activity_Dtl(NCOA_Activity_PK, address_pk, person_pk, standard_trans_cd, ncoa_error_cd_pk) 
	values (@NCOA_Activity_PK, @address_pk, @person_pk, null, 14)
	SET @IsSMA = 0
	RETURN
END


IF datediff(d, @vendor_ncoa_change_dt, getdate()) > @ConfTimeFrame
BEGIN
	raiserror('NCOA Processing date is not within configurable time frame. Current address cannot be made SMA.', 1, 16)

	INSERT INTO NCOA_Activity_Dtl(NCOA_Activity_PK, address_pk, person_pk, standard_trans_cd, ncoa_error_cd_pk) 
	values (@NCOA_Activity_PK, @address_pk, @person_pk, null, 13)
	SET @IsSMA = 0
	RETURN
END

SET @IsSMA = 1
END

GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[InsertPersonAddress]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
drop procedure [dbo].[InsertPersonAddress]
GO

SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO




CREATE      PROCEDURE DBO.InsertPersonAddress(@person_pk int, @address_pk int output)
AS
/*************************************************************************************
* Name: InsertPersonAddress
* Desc: InsertPersonAddress inserts record into the person address table with 
*	default values for address type as HOME, department as NCOA, country as USA.
*	address_pk parameter acts as both input and output parameters. The input is 
*	taken to identify the address key in the NCOA_Transactions table. The output 
*	is the address key which is inserted into the person address table.
* Created date: 09/24/03	
* Author : Sivakumar
* Modified date : 09/24/03
* Modified date : 10/28/2003 Changed Country_pk from 8001 to 9001 
**************************************************************************************/
DECLARE @created_user_pk user_pk
SELECT @created_user_pk = created_user_pk FROM NCOA_Activity 
WHERE convert(varchar, NCOA_Activity_requested_dt, 101) = convert(varchar, getdate(), 101) AND 
NCOA_Trans_run_completed_fg <> 1

INSERT dbo.Person_Address
(person_pk, addr_type, dept, addr_source, addr_source_if_aff_apply_upd, 
addr_prmry_fg, addr_bad_fg, addr_marked_bad_dt, addr_private_fg, addr1, addr2, city, 
state, zipcode, zip_plus, province, carrier_route_info, country, county, eff_dt, 
end_dt, created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
SELECT 
person_pk, 12001, NULL, 'N', NULL, NULL, 0, NULL, 0, vendor_addr1, 
vendor_addr2, vendor_city, vendor_state, vendor_zipcode, vendor_zip_plus, NULL, 
vendor_carrier_route, 9001, NULL, vendor_ncoa_change_dt, NULL, @created_user_pk, 
getdate(), @created_user_pk, getdate() 
FROM 
	NCOA_Transactions 
WHERE 
	person_pk = @person_pk and address_pk = @address_pk

set @address_pk = ident_current('Person_address')





GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO

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


GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO
