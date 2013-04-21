if exists (select 1
						from  sysobjects
						where  id = object_id('statementLine')
						and    type = 'U')
	 drop table statementLine
go
if exists (select 1
						from  sysobjects
						where  id = object_id('statement')
						and    type = 'U')
	 drop table statement
go

CREATE TABLE DBA.statement (

	id int NOT NULL default autoincrement,
	number varchar(5) NOT NULL,
	account varchar(15) NOT NULL,

	statementDate t_date NOT NULL,

	sourceFilename varchar(50) NOT NULL,

	openingBalance decimal(9,2) NOT NULL default 0,
	closingBalance decimal(9,2) NOT NULL default 0,
	closingAvailableBalance decimal(9,2) NOT NULL default 0,

	year int NOT NULL,

	version datetime NOT NULL DEFAULT current timestamp,

	PRIMARY KEY ( id )
)
go

CREATE UNIQUE INDEX accountStatementNumber ON DBA.statement (account, year, number ASC )
go
