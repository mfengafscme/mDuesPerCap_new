	----------------------------
	-- NCOA_Transaction_Cds records
	----------------------------

	INSERT INTO NCOA_Transaction_Cds 
	(standard_trans_cd,  mapped_trans_cd, standard_trans_cd_desc)
	VALUES 
	('A', '10', 'Zip Plus 4 change')

	INSERT INTO NCOA_Transaction_Cds 
	(standard_trans_cd,  mapped_trans_cd, standard_trans_cd_desc)
	VALUES 
	('E', '11', 'Error')

	INSERT INTO NCOA_Transaction_Cds 
	(standard_trans_cd,  mapped_trans_cd, standard_trans_cd_desc)
	VALUES 
	('G', '12', 'Box Closed')

	INSERT INTO NCOA_Transaction_Cds 
	(standard_trans_cd,  mapped_trans_cd, standard_trans_cd_desc)
	VALUES 
	('K', '13', 'Kill/Delete')

	INSERT INTO NCOA_Transaction_Cds 
	(standard_trans_cd,  mapped_trans_cd, standard_trans_cd_desc)
	VALUES 
	('M', '14', 'Move')

	INSERT INTO NCOA_Transaction_Cds 
	(standard_trans_cd,  mapped_trans_cd, standard_trans_cd_desc)
	VALUES 
	('P', '15', 'Undeliverable')

	INSERT INTO NCOA_Transaction_Cds 
	(standard_trans_cd,  mapped_trans_cd, standard_trans_cd_desc)
	VALUES 
	('S', '16', 'Move')

	INSERT INTO NCOA_Transaction_Cds 
	(standard_trans_cd,  mapped_trans_cd, standard_trans_cd_desc)
	VALUES 
	('Z', '17', 'Zip Change')

	----------------------------
	-- NCOA_Error_Cds records
	----------------------------

	INSERT INTO NCOA_Error_Cds 
	(ncoa_error_cd_pk, ncoa_error_short_desc, ncoa_error_long_desc)
	VALUES
	(1, 'Invalid transaction code', 'Invalid transaction code.')

	INSERT INTO NCOA_Error_Cds 
	(ncoa_error_cd_pk, ncoa_error_short_desc, ncoa_error_long_desc)
	VALUES
	(2, 'Zip plus not supplied', 'Vendor zip plus code must be supplied if transaction code indicates Zip Plus 4 change.')

	INSERT INTO NCOA_Error_Cds 
	(ncoa_error_cd_pk, ncoa_error_short_desc, ncoa_error_long_desc)
	VALUES
	(3, 'Vendor address not supplied', 'Vendor address must be supplied if transaction code indicates Move.')

	INSERT INTO NCOA_Error_Cds 
	(ncoa_error_cd_pk, ncoa_error_short_desc, ncoa_error_long_desc)
	VALUES
	(4, 'No active transaction', 'No active transaction to process. Job terminated.')

	INSERT INTO NCOA_Error_Cds 
	(ncoa_error_cd_pk, ncoa_error_short_desc, ncoa_error_long_desc)
	VALUES
	(5, 'Invalid effective date', 'Invalid effective date in NCOA file. SMA process cannot be completed.')

	INSERT INTO NCOA_Error_Cds 
	(ncoa_error_cd_pk, ncoa_error_short_desc, ncoa_error_long_desc)
	VALUES
	(6, 'Zipcode is NULL', 'Zipcode is NULL')

	INSERT INTO NCOA_Error_Cds 
	(ncoa_error_cd_pk, ncoa_error_short_desc, ncoa_error_long_desc)
	VALUES
	(7, 'Invalid Zipcode', 'Invalid Zipcode')

	INSERT INTO NCOA_Error_Cds 
	(ncoa_error_cd_pk, ncoa_error_short_desc, ncoa_error_long_desc)
	VALUES
	(8, 'Transaction code is NULL', 'Transaction code is NULL')

	INSERT INTO NCOA_Error_Cds 
	(ncoa_error_cd_pk, ncoa_error_short_desc, ncoa_error_long_desc)
	VALUES
	(9, 'Zip plus code is NULL', 'Zip plus code is NULL')

	INSERT INTO NCOA_Error_Cds 
	(ncoa_error_cd_pk, ncoa_error_short_desc, ncoa_error_long_desc)
	VALUES
	(10, 'Invalid Zip plus code', 'Invalid Zip plus code')

	INSERT INTO NCOA_Error_Cds 
	(ncoa_error_cd_pk, ncoa_error_short_desc, ncoa_error_long_desc)
	VALUES
	(11, 'NCOA Processing date not within config time frame.', 'NCOA Processing date is not within configurable time frame.')

	INSERT INTO NCOA_Error_Cds 
	(ncoa_error_cd_pk, ncoa_error_short_desc, ncoa_error_long_desc)
	VALUES
	(12, 'Member has updated the SMA recently.', 'Member has updated the SMA recently. Current address cannot be made SMA.')

