--
--Test Reports
--

declare @report_pk int

-- 1 ************************************************************************************************
-- insert a mailing report with output fields, selection criteria, and order by fields.

INSERT INTO [Report]
VALUES ('Mailing List Report', 'A mailing list test report', 0, 1, 1, 0, null, 0, getdate(), 'yqi', 6, null)

SET @report_pk = @@identity

-- output fields
INSERT INTO [Report_Output_Fields] (report_pk, report_field_pk, field_output_order) VALUES (@report_pk, 2, 2)
INSERT INTO [Report_Output_Fields] (report_pk, report_field_pk, field_output_order) VALUES (@report_pk, 3, 3)
INSERT INTO [Report_Output_Fields] (report_pk, report_field_pk, field_output_order) VALUES (@report_pk, 1, 1)
INSERT INTO [Report_Output_Fields] (report_pk, report_field_pk, field_output_order) VALUES (@report_pk, 6, 4)
INSERT INTO [Report_Output_Fields] (report_pk, report_field_pk, field_output_order) VALUES (@report_pk, 7, 5)
INSERT INTO [Report_Output_Fields] (report_pk, report_field_pk, field_output_order) VALUES (@report_pk, 8, 6)
INSERT INTO [Report_Output_Fields] (report_pk, report_field_pk, field_output_order) VALUES (@report_pk, 9, 7)

-- sort fields

INSERT INTO [Report_Sort_Fields] VALUES (9, @report_pk, 1, 'A')
INSERT INTO [Report_Sort_Fields] VALUES (3, @report_pk, 2, 'A')

-- criteria

INSERT INTO [Report_Selection_Criteria]
VALUES (1, '11111', 'GE', null, @report_pk, 9, 1)


-- 2 ************************************************************************************************
-- insert a mailing list report with output fields, selection criteria, and order by fields.

INSERT INTO [Report]
VALUES ('Another Mailing List Report', 'Another mailing list test report description', 0, 1, 1, 0, null, 0, getdate(), 'yqi', 6, null)

SET @report_pk = @@identity

-- output fields
INSERT INTO [Report_Output_Fields] VALUES (@report_pk, 4, 2)
INSERT INTO [Report_Output_Fields] VALUES (@report_pk, 1, 1)
INSERT INTO [Report_Output_Fields] VALUES (@report_pk, 6, 4)
INSERT INTO [Report_Output_Fields] VALUES (@report_pk, 7, 5)
INSERT INTO [Report_Output_Fields] VALUES (@report_pk, 8, 6)
INSERT INTO [Report_Output_Fields] VALUES (@report_pk, 9, 7)

-- sort fields
-- sort by 'state' and 'last name'
INSERT INTO [Report_Sort_Fields] VALUES (9, @report_pk, 1, 'A')
INSERT INTO [Report_Sort_Fields] VALUES (3, @report_pk, 2, 'A')

-- 3 ************************************************************************************************
-- insert a regular report with output fields and order by fields

INSERT INTO [Report]
VALUES ('Custom Query YQI', 'Custom Query YQI description', 0, 0, 1, 0, null, 0, getdate(), 'yqi', 6, null)

SET @report_pk = @@identity

-- output fields
INSERT INTO [Report_Output_Fields] VALUES (@report_pk, 2, 1)
INSERT INTO [Report_Output_Fields] VALUES (@report_pk, 3, 2)
INSERT INTO [Report_Output_Fields] VALUES (@report_pk, 9, 4)

-- sort fields
INSERT INTO [Report_Sort_Fields] VALUES (9, @report_pk, 1, 'D')

-- 4 ************************************************************************************************
-- insert a regular report with output fields and order by fields

INSERT INTO [Report]
VALUES ('Custom Query One', 'Custom Query One description', 0, 0, 1, 0, null, 0, getdate(), 'kvogel', 2, null)

SET @report_pk = @@identity

-- output fields
INSERT INTO [Report_Output_Fields] VALUES (@report_pk, 2, 1)
INSERT INTO [Report_Output_Fields] VALUES (@report_pk, 3, 2)
INSERT INTO [Report_Output_Fields] VALUES (@report_pk, 9, 4)

-- sort fields
INSERT INTO [Report_Sort_Fields] VALUES (9, @report_pk, 1, 'D')
	
