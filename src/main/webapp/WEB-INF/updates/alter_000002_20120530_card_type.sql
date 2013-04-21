if exists (select 1
						from  sysobjects
						where  id = object_id('cardType')
						and    type = 'U')
	 drop table cardType
go

CREATE TABLE DBA.cardType (
	code varchar(10) NOT NULL,
	description1 varchar(50) NOT NULL,
	description2 varchar(50) NOT NULL,
	description3 varchar(50) NOT NULL,

	version datetime NOT NULL DEFAULT current timestamp,

	PRIMARY KEY ( code )
)
go

INSERT INTO cardType VALUES ('BUSINESS', 'Business', 'Business', 'Business', current timestamp)
go
INSERT INTO cardType VALUES ('GOLD', 'Gold', 'Zlatá', 'Zlatá', current timestamp)
go
