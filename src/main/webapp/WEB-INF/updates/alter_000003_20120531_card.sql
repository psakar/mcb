if exists (select 1
            from  sysobjects
            where  id = object_id('card')
            and    type = 'U')
   drop table card
go

CREATE TABLE DBA.card (
	id int NOT NULL default autoincrement,
	number char(16) NOT NULL,
	cardType varchar(10) NOT NULL,
	cardHolderName varchar(40) NOT NULL,
	email varchar(50) NULL,
	phone varchar(35) NULL,
	activeFrom t_date NOT NULL DEFAULT current timestamp,
	activeTo t_date_null NULL,

	title varchar(40) NULL,
	name varchar(40) NOT NULL,
	name2 varchar(40) NULL,
	street varchar(40) NOT NULL,
	zip varchar(10) NOT NULL,
	town varchar(40) NOT NULL,
	country varchar(40) NULL,

	settlementAccount char(15) NOT NULL,
	statementPeriod int NOT NULL DEFAULT 0,

	version datetime NOT NULL DEFAULT current timestamp,

	PRIMARY KEY ( id )
)
go


CREATE UNIQUE INDEX number ON DBA.card ( number ASC )
go

ALTER TABLE DBA.card ADD CONSTRAINT FK_CARD_REFERENCE_CARD_TYPE NOT NULL FOREIGN KEY ( cardType ASC ) REFERENCES DBA.cardType ( code )
go
