This folder contains the appliction distribution files.  The distribution files are all that is needed to run the application.

FILES
=====


'installDB.cmd'
Installs the application database.  Called by 'install.cmd'

'createDB.sql'
SQL for dropping the old database, and creating the new one.  Called by 'installDB.cmd'

'createTables.sql'
SQL for creating all the tables in the database.  Called by 'installDB.cmd'

'insertFields.sql'
SQL for inserting all the initial data into the Report_Fields and Report_Field_Aggregate tables.

'AFSCMEEnterprise.ear'
The Enterprise Application Archive of the application code, configuration data, and resources


CONFIGURING INSTALLATION
========================

The install will check for the following environment variables.  If they are not set, defaults will be used. 

Variable            Default                Description
========================================================================================================================
AFSCME_DB_SERVER    vna016                 Name of the Database Machine
AFSCME_DB_USER      afscme                 SQL Server user name
AFSCME_DB_PASSWORD  emcsfa                 SQL Server password.
AFSCME_DB_NAME      afscme_oltp            Database Name. 
