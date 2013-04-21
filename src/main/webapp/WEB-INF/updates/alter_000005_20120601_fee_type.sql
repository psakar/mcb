if exists (select 1
						from  sysobjects
						where  id = object_id('feeType')
						and    type = 'U')
	 drop table feeType
go

CREATE TABLE DBA.feeType (
	code varchar(10) NOT NULL,
	description1 varchar(50) NOT NULL,
	description2 varchar(50) NOT NULL,
	description3 varchar(50) NOT NULL,

	cardType varchar(10) NOT NULL,

	amount decimal(9,2) NOT NULL default 0,
	percentage decimal(9,2) NOT NULL default 0,
	settlementAccount char(15) NULL,

	transferType varchar(10) NOT NULL,


	version datetime NOT NULL DEFAULT current timestamp,

	PRIMARY KEY ( code )
)
go

ALTER TABLE DBA.feeType ADD CONSTRAINT FK_FEE_TYPE_REFERENCE_CARD_TYPE NOT NULL FOREIGN KEY ( cardType ASC ) REFERENCES DBA.cardType ( code )
go
ALTER TABLE DBA.feeType ADD CONSTRAINT FK_FEE_TYPE_REFERENCE_TRANSFER_TYPE NOT NULL FOREIGN KEY ( transferType ASC ) REFERENCES DBA.transferType ( code )
go


INSERT INTO feeType VALUES ('B_ICFEE', 'Issue Business card fee', 'Poplatok za vydanie Business kárty', 'Poplatek za vydání Business karty', 'BUSINESS', 5, 0, 'PROFIT123456EUR', 'ICFEE', current timestamp)
go
INSERT INTO feeType VALUES ('G_ICFEE', 'Issue Gold card fee', 'Poplatok za vydanie Gold kárty', 'Poplatek za vydání Gold karty', 'GOLD', 15, 0, 'PROFIT123456EUR', 'ICFEE', current timestamp)
go
