CREATE TRIGGER DBO.UpdateTrigMLBPPersons
ON MLBP_Persons
FOR UPDATE 
AS
DECLARE @person_pk int, @lst_mod_user_pk User_pk, @lst_mod_dt datetime
SELECT @lst_mod_user_pk = I.lst_mod_user_pk, @lst_mod_dt = I.lst_mod_dt, 
@person_pk = I.person_pk 
FROM MLBP_Persons PD INNER JOIN Inserted I  
ON PD.person_pk = I.person_pk
UPDATE Person SET person_mst_lst_mod_user_pk = @lst_mod_user_pk, 
person_mst_lst_mod_dt = @lst_mod_dt, mbr_mst_lst_mod_user_pk = @lst_mod_user_pk, 
mbr_mst_lst_mod_dt = @lst_mod_dt 
WHERE person_pk = @person_pk
GO

CREATE TRIGGER DBO.UpdateTrigPersonPhone
ON Person_Phone
FOR UPDATE 
AS
DECLARE @person_pk int, @lst_mod_user_pk User_pk, @lst_mod_dt datetime
SELECT @lst_mod_user_pk = I.lst_mod_user_pk, @lst_mod_dt = I.lst_mod_dt, 
@person_pk = I.person_pk 
FROM Person_Phone PD INNER JOIN Inserted I  
ON PD.person_pk = I.person_pk
UPDATE Person SET person_mst_lst_mod_user_pk = @lst_mod_user_pk, 
person_mst_lst_mod_dt = @lst_mod_dt, mbr_mst_lst_mod_user_pk = @lst_mod_user_pk, 
mbr_mst_lst_mod_dt = @lst_mod_dt  
WHERE person_pk = @person_pk
GO

CREATE TRIGGER DBO.UpdateTrigPersonEmail
ON Person_Email
FOR UPDATE 
AS
DECLARE @person_pk int, @lst_mod_user_pk User_pk, @lst_mod_dt datetime
SELECT @lst_mod_user_pk = I.lst_mod_user_pk, @lst_mod_dt = I.lst_mod_dt, 
@person_pk = I.person_pk 
FROM Person_Email PD INNER JOIN Inserted I  
ON PD.person_pk = I.person_pk
UPDATE Person SET person_mst_lst_mod_user_pk = @lst_mod_user_pk, 
person_mst_lst_mod_dt = @lst_mod_dt, mbr_mst_lst_mod_user_pk = @lst_mod_user_pk, 
mbr_mst_lst_mod_dt = @lst_mod_dt 
WHERE person_pk = @person_pk
GO

CREATE TRIGGER DBO.UpdateTrigPersonAddress
ON Person_Address
FOR UPDATE 
AS
DECLARE @person_pk int, @lst_mod_user_pk User_pk, @lst_mod_dt datetime
SELECT @lst_mod_user_pk = I.lst_mod_user_pk, @lst_mod_dt = I.lst_mod_dt, 
@person_pk = I.person_pk 
FROM Person_Address PD INNER JOIN Inserted I  
ON PD.person_pk = I.person_pk
UPDATE Person SET person_mst_lst_mod_user_pk = @lst_mod_user_pk, 
person_mst_lst_mod_dt = @lst_mod_dt, mbr_mst_lst_mod_user_pk = @lst_mod_user_pk, 
mbr_mst_lst_mod_dt = @lst_mod_dt 
WHERE person_pk = @person_pk
GO

CREATE TRIGGER DBO.UpdateTrigPersonRelation
ON Person_Relation
FOR UPDATE 
AS
DECLARE @person_pk int, @lst_mod_user_pk User_pk, @lst_mod_dt datetime
SELECT @lst_mod_user_pk = I.lst_mod_user_pk, @lst_mod_dt = I.lst_mod_dt, 
@person_pk = I.person_pk 
FROM Person_Relation PD INNER JOIN Inserted I  
ON PD.person_pk = I.person_pk
UPDATE Person SET person_mst_lst_mod_user_pk = @lst_mod_user_pk, 
person_mst_lst_mod_dt = @lst_mod_dt, mbr_mst_lst_mod_user_pk = @lst_mod_user_pk, 
mbr_mst_lst_mod_dt = @lst_mod_dt 
WHERE person_pk = @person_pk
GO

CREATE TRIGGER DBO.UpdateTrigPersonDisabilityAccmdtn
ON Person_Disability_Accmdtn
FOR UPDATE 
AS
DECLARE @person_pk int, @lst_mod_user_pk User_pk, @lst_mod_dt datetime
SELECT @lst_mod_user_pk = I.lst_mod_user_pk, @lst_mod_dt = I.lst_mod_dt, 
@person_pk = I.person_pk 
FROM Person_Disability_Accmdtn PD INNER JOIN Inserted I  
ON PD.person_pk = I.person_pk
UPDATE Person SET person_mst_lst_mod_user_pk = @lst_mod_user_pk, 
person_mst_lst_mod_dt = @lst_mod_dt, mbr_mst_lst_mod_user_pk = @lst_mod_user_pk, 
mbr_mst_lst_mod_dt = @lst_mod_dt 
WHERE person_pk = @person_pk
GO

CREATE TRIGGER DBO.UpdateTrigPersonDemographics
ON Person_Demographics
FOR UPDATE 
AS
DECLARE @person_pk int, @lst_mod_user_pk User_pk, @lst_mod_dt datetime
SELECT @lst_mod_user_pk = I.lst_mod_user_pk, @lst_mod_dt = I.lst_mod_dt, 
@person_pk = I.person_pk 
FROM Person_Demographics PD INNER JOIN Inserted I  
ON PD.person_pk = I.person_pk
UPDATE Person SET person_mst_lst_mod_user_pk = @lst_mod_user_pk, 
person_mst_lst_mod_dt = @lst_mod_dt, mbr_mst_lst_mod_user_pk = @lst_mod_user_pk, 
mbr_mst_lst_mod_dt = @lst_mod_dt 
WHERE person_pk = @person_pk
GO

CREATE TRIGGER DBO.UpdateTrigPersonDisabilities
ON Person_Disabilities
FOR UPDATE 
AS
DECLARE @person_pk int, @lst_mod_user_pk User_pk, @lst_mod_dt datetime
SELECT @lst_mod_user_pk = I.lst_mod_user_pk, @lst_mod_dt = I.lst_mod_dt, 
@person_pk = I.person_pk 
FROM Person_Disabilities PD INNER JOIN Inserted I  
ON PD.person_pk = I.person_pk
UPDATE Person SET person_mst_lst_mod_user_pk = @lst_mod_user_pk, 
person_mst_lst_mod_dt = @lst_mod_dt, mbr_mst_lst_mod_user_pk = @lst_mod_user_pk, 
mbr_mst_lst_mod_dt = @lst_mod_dt 
WHERE person_pk = @person_pk
GO

CREATE TRIGGER DBO.UpdateTrigOfficerHistory
ON Officer_History
FOR UPDATE 
AS
DECLARE @person_pk int, @lst_mod_user_pk User_pk, @lst_mod_dt datetime
SELECT @lst_mod_user_pk = I.lst_mod_user_pk, @lst_mod_dt = I.lst_mod_dt, 
@person_pk = I.person_pk 
FROM Officer_History PD INNER JOIN Inserted I  
ON PD.person_pk = I.person_pk
UPDATE Person SET person_mst_lst_mod_user_pk = @lst_mod_user_pk, 
person_mst_lst_mod_dt = @lst_mod_dt, mbr_mst_lst_mod_user_pk = @lst_mod_user_pk, 
mbr_mst_lst_mod_dt = @lst_mod_dt 
WHERE person_pk = @person_pk
GO

CREATE TRIGGER DBO.UpdateTrigPersonComments
ON Person_Comments
FOR UPDATE 
AS
DECLARE @person_pk int, @created_user_pk User_pk, @comment_dt datetime
SELECT @created_user_pk = I.created_user_pk, @comment_dt = I.comment_dt, 
@person_pk = I.person_pk 
FROM Person_Comments PC INNER JOIN Inserted I  
ON PC.person_pk = I.person_pk
UPDATE Person SET person_mst_lst_mod_user_pk = @created_user_pk, 
person_mst_lst_mod_dt = @comment_dt, mbr_mst_lst_mod_user_pk = @created_user_pk, 
mbr_mst_lst_mod_dt = @comment_dt 
WHERE person_pk = @person_pk
GO

CREATE TRIGGER DBO.UpdateTrigAffMembers
ON Aff_Members
FOR UPDATE
AS
DECLARE @person_pk int, @mbr_mst_lst_mod_user_pk User_pk, @mbr_mst_lst_mod_dt datetime
SELECT @mbr_mst_lst_mod_user_pk = I.lst_mod_user_pk, @mbr_mst_lst_mod_dt = I.lst_mod_dt, 
@person_pk = I.person_pk 
FROM Aff_Members AM INNER JOIN Inserted I  
ON AM.person_pk = I.person_pk
UPDATE Person SET mbr_mst_lst_mod_user_pk = @mbr_mst_lst_mod_user_pk, 
mbr_mst_lst_mod_dt = @mbr_mst_lst_mod_dt 
WHERE person_pk = @person_pk
GO

CREATE TRIGGER DBO.UpdateTrigMemberParticipation
ON Member_Participation
FOR UPDATE
AS
DECLARE @person_pk int, @mbr_mst_lst_mod_user_pk User_pk, @mbr_mst_lst_mod_dt datetime
SELECT @mbr_mst_lst_mod_user_pk = I.lst_mod_user_pk, @mbr_mst_lst_mod_dt = I.lst_mod_dt, 
@person_pk = I.person_pk 
FROM Member_Participation AM INNER JOIN Inserted I  
ON AM.person_pk = I.person_pk
UPDATE Person SET mbr_mst_lst_mod_user_pk = @mbr_mst_lst_mod_user_pk, 
mbr_mst_lst_mod_dt = @mbr_mst_lst_mod_dt 
WHERE person_pk = @person_pk
GO

CREATE TRIGGER DBO.UpdateTrigAffMbrEmployers
ON Aff_Mbr_Employers
FOR UPDATE
AS
DECLARE @person_pk int, @mbr_mst_lst_mod_user_pk User_pk, @mbr_mst_lst_mod_dt datetime
SELECT @mbr_mst_lst_mod_user_pk = I.lst_mod_user_pk, @mbr_mst_lst_mod_dt = I.lst_mod_dt, 
@person_pk = I.person_pk 
FROM Aff_Mbr_Employers AM INNER JOIN Inserted I  
ON AM.person_pk = I.person_pk
UPDATE Person SET mbr_mst_lst_mod_user_pk = @mbr_mst_lst_mod_user_pk, 
mbr_mst_lst_mod_dt = @mbr_mst_lst_mod_dt 
WHERE person_pk = @person_pk
GO

CREATE TRIGGER DBO.UpdateTrigPersonMst
ON Person
FOR UPDATE AS
IF UPDATE(ssn) OR 
UPDATE(valid_ssn_fg) OR
UPDATE(duplicate_ssn_fg) OR
UPDATE(alternate_mailing_nm) OR
UPDATE(prefix_nm) OR
UPDATE(first_nm) OR
UPDATE(middle_nm) OR
UPDATE(last_nm) OR
UPDATE(suffix_nm) OR
UPDATE(nick_nm) OR
UPDATE(marked_for_deletion_fg)
BEGIN
DECLARE @person_pk int, @person_mst_lst_mod_user_pk User_pk, @person_mst_lst_mod_dt datetime
SELECT @person_mst_lst_mod_user_pk = I.lst_mod_user_pk, @person_mst_lst_mod_dt = I.lst_mod_dt, 
@person_pk = I.person_pk 
FROM Person P INNER JOIN Inserted I  
ON P.person_pk = I.person_pk
UPDATE Person SET person_mst_lst_mod_user_pk = @person_mst_lst_mod_user_pk, 
person_mst_lst_mod_dt = @person_mst_lst_mod_dt
WHERE person_pk = @person_pk
END
GO

CREATE TRIGGER DBO.UpdateTrigPersonMbr
ON Person
FOR UPDATE AS
IF UPDATE(member_fg) OR 
UPDATE(mbr_expelled_dt) OR
UPDATE(mbr_barred_fg)
BEGIN
DECLARE @person_pk int, @mbr_mst_lst_mod_user_pk User_pk, @mbr_mst_lst_mod_dt datetime
SELECT @mbr_mst_lst_mod_user_pk = I.lst_mod_user_pk, @mbr_mst_lst_mod_dt = I.lst_mod_dt, 
@person_pk = I.person_pk 
FROM Person P INNER JOIN Inserted I  
ON P.person_pk = I.person_pk
UPDATE Person SET mbr_mst_lst_mod_user_pk = @mbr_mst_lst_mod_user_pk, 
mbr_mst_lst_mod_dt = @mbr_mst_lst_mod_dt 
WHERE person_pk = @person_pk
END
GO

CREATE TRIGGER DBO.InsUpdTrigAff_Staff
ON Aff_Staff
FOR INSERT, UPDATE 
AS
DECLARE @person_pk int, @lst_mod_user_pk User_pk, @lst_mod_dt datetime
SELECT @lst_mod_user_pk = I.lst_mod_user_pk, @lst_mod_dt = I.lst_mod_dt, 
@person_pk = I.person_pk 
FROM Aff_Staff AFS INNER JOIN Inserted I  
ON AFS.person_pk = I.person_pk
UPDATE Person SET person_mst_lst_mod_user_pk = @lst_mod_user_pk, 
person_mst_lst_mod_dt = @lst_mod_dt
WHERE person_pk = @person_pk
GO

CREATE TRIGGER DBO.InsUpdTrigExt_Org_Associates
ON Ext_Org_Associates
FOR INSERT, UPDATE 
AS
DECLARE @person_pk int, @lst_mod_user_pk User_pk, @lst_mod_dt datetime
SELECT @lst_mod_user_pk = I.lst_mod_user_pk, @lst_mod_dt = I.lst_mod_dt, 
@person_pk = I.person_pk 
FROM Ext_Org_Associates EOA INNER JOIN Inserted I  
ON EOA.person_pk = I.person_pk
UPDATE Person SET person_mst_lst_mod_user_pk = @lst_mod_user_pk, 
person_mst_lst_mod_dt = @lst_mod_dt
WHERE person_pk = @person_pk
