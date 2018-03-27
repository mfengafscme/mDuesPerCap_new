DECLARE @year INT, @month INT
SET @year = 2003
WHILE (@year <= 2025)
 BEGIN
  SET @month = 1
  WHILE (@month <= 12)
   BEGIN
    INSERT into Time_Dim (calendar_year, calendar_month) VALUES (@year, @month)
     SET @month = @month + 1
   END
  SET @year = @year+ 1
END

