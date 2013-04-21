if exists (select 1
						from  sysobjects
						where  id = object_id('booking')
						and    type = 'U')
	 drop table booking
go

CREATE TABLE DBA.booking (

	id int NOT NULL default autoincrement,
	postingFileId int NOT NULL,

	sequenceNr          integer                        not null,
	trReference         varchar(16)                    null,
	amount               decimal(9,2)  not null default 0                     ,
	currency      varchar(3)                       not null,
	debitAccount         varchar(15)                    null,
	debitValueDate       t_date                         null,
	creditAccount           varchar(15)                    null,
	creditValueDate    t_date                         null,
	details              varchar(140)                   null,
	valueDate           t_date                         not null default 'getdate()',
	businessDate        t_date                         not null default 'getdate()',
	source               varchar(2)                     not null,
	bankToBankInfo    varchar(210)                   null,
	orderingBankName   varchar(35)                    null,
	orderingBankAddress varchar(105)                  null,


	PRIMARY KEY ( id )
)
go


ALTER TABLE DBA.booking ADD CONSTRAINT FK_BOOKING_REFERENCE_POSTING_FILE NOT NULL FOREIGN KEY ( postingFileId ASC ) REFERENCES DBA.postingFile ( id )
go

