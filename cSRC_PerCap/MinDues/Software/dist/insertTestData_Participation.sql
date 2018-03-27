DECLARE @group_pk int, @type_pk int, @detail_pk int, @detail_pk2 int, @outcome_pk int

INSERT INTO Participation_Cd_Group (particip_group_nm, particip_group_desc)
VALUES('Group 1', 'Group 1 Desc')
SET @group_pk = @@identity

INSERT INTO Participation_Cd_Type (particip_type_nm, particip_type_desc, particip_group_pk)
VALUES('Type 1','Type 1 Desc',@group_pk)
SET @type_pk = @@identity

INSERT INTO Participation_Cd_Dtl(particip_detail_nm, particip_detail_shortcut, particip_type_pk,
particip_detail_desc, particip_detail_status, created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
VALUES('Detail 1',1,@type_pk,'Detail 1 Desc',1,10001812,getDate(),10001812,getDate())
SET @detail_pk = @@identity
INSERT INTO Participation_Cd_Dtl(particip_detail_nm, particip_detail_shortcut, particip_type_pk,
particip_detail_desc, particip_detail_status, created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt)
VALUES('Detail 2',2,@type_pk,'Detail 2 Desc',1,10001812,getDate(),10001812,getDate())
SET @detail_pk2 = @@identity

INSERT INTO Participation_Cd_Outcomes(particip_outcome_nm, particip_outcome_desc, created_user_pk,
created_dt, lst_mod_user_pk, lst_mod_dt)
VALUES('Outcome 1','Outcome 1 Desc',10001812,getDate(),10001812,getDate())
SET @outcome_pk = @@identity

INSERT INTO Participation_Dtl_Outcomes(particip_detail_pk, particip_outcome_pk)
VALUES(@detail_pk,@outcome_pk)

INSERT INTO Participation_Cd_Outcomes(particip_outcome_nm, particip_outcome_desc, created_user_pk,
created_dt, lst_mod_user_pk, lst_mod_dt)
VALUES('Outcome 2','Outcome 2 Desc',10001812,getDate(),10001812,getDate())
SET @outcome_pk = @@identity

INSERT INTO Participation_Dtl_Outcomes(particip_detail_pk, particip_outcome_pk)
VALUES(@detail_pk,@outcome_pk)

INSERT INTO Participation_Cd_Outcomes(particip_outcome_nm, particip_outcome_desc, created_user_pk,
created_dt, lst_mod_user_pk, lst_mod_dt)
VALUES('Outcome 3','Outcome 3 Desc',10001812,getDate(),10001812,getDate())
SET @outcome_pk = @@identity

INSERT INTO Participation_Dtl_Outcomes(particip_detail_pk, particip_outcome_pk)
VALUES(@detail_pk2,@outcome_pk)

INSERT INTO Participation_Cd_Outcomes(particip_outcome_nm, particip_outcome_desc, created_user_pk,
created_dt, lst_mod_user_pk, lst_mod_dt)
VALUES('Outcome 4','Outcome 4 Desc',10001812,getDate(),10001812,getDate())
SET @outcome_pk = @@identity

INSERT INTO Participation_Dtl_Outcomes(particip_detail_pk, particip_outcome_pk)
VALUES(@detail_pk2,@outcome_pk)