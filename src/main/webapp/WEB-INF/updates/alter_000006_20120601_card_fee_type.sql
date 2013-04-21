if exists (select 1
						from  sysobjects
						where  id = object_id('cardFeeType')
						and    type = 'U')
	 drop table cardFeeType
go

CREATE TABLE DBA.cardFeeType (

	id int NOT NULL default autoincrement,
	feeType varchar(10) NOT NULL,
	cardId int NOT NULL,
	amount decimal(9,2) NOT NULL default 0,
	percentage decimal(9,2) NOT NULL default 0,


	PRIMARY KEY ( id )
)
go

ALTER TABLE DBA.cardFeeType ADD CONSTRAINT FK_CARD_FEE_TYPE_REFERENCE_FEE_TYPE NOT NULL FOREIGN KEY ( feeType ASC ) REFERENCES DBA.feeType ( code )
go
ALTER TABLE DBA.cardFeeType ADD CONSTRAINT FK_CARD_FEE_TYPE_REFERENCE_CARD NOT NULL FOREIGN KEY ( cardId ASC ) REFERENCES DBA.card ( id )
go


