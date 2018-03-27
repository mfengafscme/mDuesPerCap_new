-- =============================================
-- Create view of Common Codes and DM_Legacy_Code_Mapping
-- =============================================
IF EXISTS (SELECT TABLE_NAME 
	   FROM   INFORMATION_SCHEMA.VIEWS 
	   WHERE  TABLE_NAME = N'DM_Code_Mapping_view')
    DROP VIEW DM_Code_Mapping_view
GO

CREATE VIEW DM_Code_Mapping_view
AS 
	SELECT  [com_cd_pk], [com_cd_cd], [com_cd_desc], [row_status_cd], 
		[com_cd_type_key], [com_cd_sort_key], [Code_Type], [Code], [Legacy_Code], [Description], [Priority]
	  FROM Common_Codes c
	  FULL OUTER JOIN DM_Legacy_Code_Mapping d ON c.com_cd_cd = d.code AND c.com_cd_type_key = d.[Code_Type]
	 WHERE  (c.com_cd_cd IS NOT NULL) 
	   AND (d.Description NOT LIKE '%unknown%') 
	   AND (d.[Code_Type] NOT LIKE 'mailing%') 
	   AND (d.Description NOT IN ('Republic of Panama', 'Catch-All'))
GO

