if exists (select 1
						from  sysobjects
						where  id = object_id('transferType')
						and    type = 'U')
	 drop table transferType
go

CREATE TABLE DBA.transferType (
	code varchar(10) NOT NULL,
	description1 varchar(50) NOT NULL,
	description2 varchar(50) NOT NULL,
	description3 varchar(50) NOT NULL,

	settlementType int NOT NULL DEFAULT 0,
	settlementAccount char(15) NULL,
	version datetime NOT NULL DEFAULT current timestamp,

	PRIMARY KEY ( code )
)
go

INSERT INTO transferType VALUES ('FMSCCLT', 'Card payment', 'Platba kartou', 'Platba kartou', 0, null, current timestamp)
go
INSERT INTO transferType VALUES ('FMSCFEE', 'Fee for card payment', 'Poplatok za platbu kartou', 'Poplatek za platbu kartou', 0, null, current timestamp)
go

INSERT INTO transferType VALUES ('CMFEE', 'Card management fee', 'Poplatok za správu karet', 'Poplatek za správu karet', 1, 'COST56789012EUR', current timestamp)
go
INSERT INTO transferType VALUES ('ICFEE', 'Issued card fee', 'Poplatok za vydanie kárty', 'Poplatek za vydání karty', 2, 'PROFIT789012EUR', current timestamp)
go
