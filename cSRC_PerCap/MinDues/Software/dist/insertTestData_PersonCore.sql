DECLARE @dup_person_pk int

/*******************************************************************/
    /** Create a duplicate ssn person */
INSERT INTO Person(prefix_nm, mbr_expelled_dt, first_nm, last_nm, 
	mbr_barred_fg, middle_nm, suffix_nm, nick_nm, lst_mod_dt, 
	alternate_mailing_nm, ssn, created_dt, created_user_pk, valid_ssn_fg, 
	duplicate_ssn_fg, lst_mod_user_pk, marked_for_deletion_fg, member_fg)
 VALUES(NULL, NULL,'Duplicate','Bill',0,NULL,NULL,NULL,
	GetDate(),NULL,'773844400',GetDate(),10000001,1,0,10000001,0,1)
    SET @dup_person_pk = @@identity

    /** Inserts a new comment for a person */
INSERT INTO Person_Comments(person_pk, comment_txt, comment_dt, created_user_pk) 
  VALUES(10000002, 'Life is what happens to you while you are busy making other plans', GetDate(), 10000001 ) 
INSERT INTO Person_Comments(person_pk, comment_txt, comment_dt, created_user_pk) 
  VALUES(10000002, 'The weather is here, wish you were beautiful', GetDate(), 10000001 ) 
INSERT INTO Person_Comments(person_pk, comment_txt, comment_dt, created_user_pk) 
  VALUES(@dup_person_pk, 'Just when you thought it could not possibly rain another drop...', GetDate(), 10000001 ) 
INSERT INTO Person_Email
	([person_pk], [person_email_addr], [lst_mod_dt], [email_bad_fg], 
	[email_type], [created_dt], [created_user_pk], [email_marked_bad_dt], 
	[lst_mod_user_pk])
VALUES(@dup_person_pk, '', GetDate(), 0, 71001, GetDate(), 10000001, null, 10000001)
INSERT INTO Person_Email
	([person_pk], [person_email_addr], [lst_mod_dt], [email_bad_fg], 
	[email_type], [created_dt], [created_user_pk], [email_marked_bad_dt], 
	[lst_mod_user_pk])
VALUES(@dup_person_pk, '', GetDate(), 0, 71002, GetDate(), 10000001, null, 10000001)

