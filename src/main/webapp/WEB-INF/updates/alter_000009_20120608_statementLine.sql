if exists (select 1
						from  sysobjects
						where  id = object_id('statementLine')
						and    type = 'U')
	 drop table statementLine
go

CREATE TABLE DBA.statementLine (

	id int NOT NULL default autoincrement,
	statementId int NOT NULL,
	number int NOT NULL,

	valueDate t_date NOT NULL,
	amount decimal(9,2) NOT NULL default 0,

	swiftType varchar(4) NULL,

	reference1 varchar(14) NULL,
	reference2 varchar(16) NULL,

	details1 varchar(35) NULL,
	details2 varchar(35) NULL,
	details3 varchar(35) NULL,
	details4 varchar(35) NULL,

	transferType varchar(10) NULL,



	cardNumber varchar(16) NULL,
	cardTransactionDate t_date_null NULL,
	cardTransactionDetails1 varchar(35) NULL,
	cardTransactionDetails2 varchar(35) NULL,
	cardTransactionAmount decimal(9,2) NULL,
	cardTransactionCurrency varchar(3) NULL,

	feeType varchar(10) NULL,

	PRIMARY KEY ( id )
)
go


CREATE UNIQUE INDEX statementLineNumber ON DBA.statementLine ( statementId ASC, number ASC )
go

ALTER TABLE DBA.statementLine ADD CONSTRAINT FK_STATEMENT_LINE_REFERENCE_STATEMENT NOT NULL FOREIGN KEY ( statementId ASC ) REFERENCES DBA.statement ( id )
go

ALTER TABLE DBA.statementLine ADD CONSTRAINT FK_STATEMENT_LINE_REFERENCE_TRANSFER_TYPE FOREIGN KEY ( transferType ASC ) REFERENCES DBA.transferType ( code )
go

/*ALTER TABLE DBA.statementLine ADD CONSTRAINT FK_STATEMENT_LINE_REFERENCE_CARD NOT NULL FOREIGN KEY ( cardNumber ASC ) REFERENCES DBA.card ( number )*/

ALTER TABLE DBA.statementLine ADD CONSTRAINT FK_STATEMENT_LINE_REFERENCE_FEE_TYPE FOREIGN KEY ( feeType ASC ) REFERENCES DBA.feeType ( code )
go
