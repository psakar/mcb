DROP VIEW IF EXISTS statementItemView;

DROP TABLE statementLine IF EXISTS;
DROP TABLE statement IF EXISTS;
DROP TABLE cardFeeType IF EXISTS;
DROP TABLE card IF EXISTS;
DROP TABLE booking IF EXISTS;
DROP TABLE postingFile IF EXISTS;
DROP TABLE feeType IF EXISTS;
DROP TABLE transferType IF EXISTS;
DROP TABLE userRoles IF EXISTS;
DROP TABLE role IF EXISTS;
DROP TABLE users IF EXISTS;
DROP TABLE settings IF EXISTS;
DROP TABLE calendar IF EXISTS;
DROP TABLE cardType IF EXISTS;
DROP TABLE language IF EXISTS;


DROP DOMAIN T_ID IF EXISTS;
DROP DOMAIN T_BOOLEAN IF EXISTS;
DROP DOMAIN T_ID_REF IF EXISTS;
DROP DOMAIN T_DATE_NULL IF EXISTS;
DROP DOMAIN T_DATETIME_NULL IF EXISTS;
DROP DOMAIN T_PASSWORD IF EXISTS;
DROP DOMAIN T_PASSWORD_NULL IF EXISTS;
DROP DOMAIN T_DESCRIPTION_NULL IF EXISTS;
DROP DOMAIN T_DATE IF EXISTS;

CREATE DOMAIN IF NOT EXISTS T_BOOLEAN AS BOOLEAN NOT NULL DEFAULT 0;
CREATE DOMAIN IF NOT EXISTS T_ID_REF AS INT NOT NULL;
CREATE DOMAIN IF NOT EXISTS T_DATE AS DATE NOT NULL DEFAULT TODAY();
CREATE DOMAIN IF NOT EXISTS T_DATE_NULL AS DATE NULL;
CREATE DOMAIN IF NOT EXISTS T_DATETIME_NULL AS TIMESTAMP NULL;
CREATE DOMAIN IF NOT EXISTS T_PASSWORD AS BINARY(32) NOT NULL;
CREATE DOMAIN IF NOT EXISTS T_PASSWORD_NULL AS BINARY(32) NULL;
CREATE DOMAIN IF NOT EXISTS T_DESCRIPTION_NULL AS VARCHAR(50) NULL;

CREATE TABLE IF NOT EXISTS language (
	id INT NOT NULL,
	code VARCHAR(2) NOT NULL,
	description1 T_DESCRIPTION_NULL,
	description2 T_DESCRIPTION_NULL,
	description3 T_DESCRIPTION_NULL,
	PRIMARY KEY ( id ASC )
);
COMMENT ON COLUMN language.code IS 'ISO code';
CREATE UNIQUE INDEX language_code ON language ( code ASC );

CREATE TABLE IF NOT EXISTS cardType (
	code VARCHAR(10) NOT NULL,
	description1 VARCHAR(50) NOT NULL,
	description2 VARCHAR(50) NOT NULL,
	description3 VARCHAR(50) NOT NULL,
	version timestamp NOT NULL DEFAULT CURRENT TIMESTAMP,
	PRIMARY KEY ( code ASC )
);

CREATE TABLE IF NOT EXISTS calendar (
	id_date DATE NOT NULL,
	holiday t_boolean NOT NULL DEFAULT 0,
	PRIMARY KEY ( id_date ASC )
);
COMMENT ON TABLE calendar IS 'List of bussines days and holidays';
CREATE INDEX holiday ON calendar ( id_date ASC, holiday ASC );


CREATE TABLE IF NOT EXISTS settings (
	id IDENTITY,
	code VARCHAR(15) NOT NULL,
	description1 T_DESCRIPTION_NULL NULL,
	description2 T_DESCRIPTION_NULL NULL,
	description3 T_DESCRIPTION_NULL NULL,
	type INTEGER NOT NULL,
	value VARCHAR(255) NULL,
	PRIMARY KEY ( id ASC )
);

CREATE UNIQUE INDEX settings ON settings ( code ASC );

CREATE TABLE IF NOT EXISTS users (
	id IDENTITY,
	template T_BOOLEAN NOT NULL DEFAULT 0,
	username VARCHAR(20) NOT NULL DEFAULT '?',
	password T_PASSWORD NOT NULL,
	surname VARCHAR(40) NOT NULL DEFAULT '?',
	name VARCHAR(40) NOT NULL DEFAULT '?',
	languageId INT NOT NULL DEFAULT 1,
	email VARCHAR(50) NOT NULL,
	phone VARCHAR(35) NULL,
	enabled BOOLEAN NOT NULL DEFAULT 0,
	expires T_DATETIME_NULL NULL,
	unsuccessfulCount INT NOT NULL DEFAULT 0,
	lastAccess T_DATETIME_NULL NULL,
	password1 T_PASSWORD_NULL NULL,
	password2 T_PASSWORD_NULL NULL,
	password3 T_PASSWORD_NULL NULL,
	template_usersId INT NULL,
	PRIMARY KEY ( id ASC )
);

CREATE UNIQUE INDEX users ON users ( username ASC );

/* FIXME languageIndex, languageCode
ALTER TABLE users ADD CONSTRAINT IF NOT EXISTS FK_USERS_REFERENCE_LANGUAGE FOREIGN KEY ( languageId ) REFERENCES language ( id );
*/

CREATE TABLE IF NOT EXISTS role (
	code VARCHAR(20) NOT NULL,
	description1 T_DESCRIPTION_NULL NULL,
	description2 T_DESCRIPTION_NULL NULL,
	description3 T_DESCRIPTION_NULL NULL,
	PRIMARY KEY ( code ASC )
);

CREATE TABLE IF NOT EXISTS userRoles (
	id IDENTITY,
	usersId T_ID_REF NOT NULL,
	role VARCHAR(20) NOT NULL,
	PRIMARY KEY ( id ASC )
);
CREATE UNIQUE INDEX userRoles ON userRoles ( usersId ASC, role ASC );

ALTER TABLE userRoles ADD CONSTRAINT IF NOT EXISTS FK_USER_ROLES_REFERENCE_ROLE FOREIGN KEY ( role ) REFERENCES role ( code );
ALTER TABLE userRoles ADD CONSTRAINT IF NOT EXISTS FK_USERS_RO_REFERENCE_USERS FOREIGN KEY ( usersId ) REFERENCES users ( id );


CREATE TABLE IF NOT EXISTS transferType (
	code VARCHAR(10) NOT NULL,
	description1 T_DESCRIPTION_NULL NULL,
	description2 T_DESCRIPTION_NULL NULL,
	description3 T_DESCRIPTION_NULL NULL,
	settlementType INTEGER NOT NULL DEFAULT 0,
	settlementAccount CHAR(15) NULL,
	version timestamp NOT NULL DEFAULT CURRENT TIMESTAMP,
	cardTransactionType INTEGER NULL,
	PRIMARY KEY ( code ASC )
);


CREATE TABLE IF NOT EXISTS feeType (
	code VARCHAR(10) NOT NULL,
	description1 T_DESCRIPTION_NULL NULL,
	description2 T_DESCRIPTION_NULL NULL,
	description3 T_DESCRIPTION_NULL NULL,
	cardType VARCHAR(10) NOT NULL,
	amount DECIMAL(9,2) NOT NULL DEFAULT 0,
	percentage DECIMAL(9,2) NOT NULL DEFAULT 0,
	settlementAccount CHAR(15) NULL,
	transferType VARCHAR(10) NOT NULL,
	version timestamp NOT NULL DEFAULT CURRENT TIMESTAMP,
	PRIMARY KEY ( code ASC )
);

ALTER TABLE feeType ADD CONSTRAINT IF NOT EXISTS FK_FEE_TYPE_REFERENCE_CARD_TYPE FOREIGN KEY ( cardType ) REFERENCES cardType ( code );
ALTER TABLE feeType ADD CONSTRAINT IF NOT EXISTS FK_FEE_TYPE_REFERENCE_TRANSFER_TYPE FOREIGN KEY ( transferType ) REFERENCES transferType ( code );


CREATE TABLE IF NOT EXISTS postingFile (
	id IDENTITY,
	filename VARCHAR(50) NOT NULL,
	created datetime NOT NULL DEFAULT CURRENT TIMESTAMP,
	createdUsersId INTEGER NOT NULL,
	approved datetime NULL,
	approvedUsersId INTEGER NULL,
	version timestamp NOT NULL DEFAULT CURRENT TIMESTAMP,
	PRIMARY KEY ( id ASC )
);
CREATE UNIQUE INDEX filename ON postingFile ( filename ASC );

ALTER TABLE postingFile ADD CONSTRAINT IF NOT EXISTS FK_POSTING_FILE_REFERENCE_USERS FOREIGN KEY ( createdUsersId ) REFERENCES users ( id );
ALTER TABLE postingFile ADD CONSTRAINT IF NOT EXISTS FK_POSTING_FILE_REFERENCE_USERS2 FOREIGN KEY ( approvedUsersId ) REFERENCES users ( id );

CREATE TABLE IF NOT EXISTS booking (
	id IDENTITY,
	postingFileId INTEGER NOT NULL,
	sequenceNr INTEGER NOT NULL,
	trReference VARCHAR(16) NULL,
	amount DECIMAL(9,2) NOT NULL DEFAULT 0,
	currency VARCHAR(3) NOT NULL,
	debitAccount VARCHAR(15) NULL,
	debitValueDate T_DATE_NULL NULL,
	creditAccount VARCHAR(15) NULL,
	creditValueDate T_DATE_NULL NULL,
	details VARCHAR(140) NULL,
	businessDate t_date NOT NULL DEFAULT today(),
	source VARCHAR(2) NOT NULL,
	bankToBankInfo VARCHAR(210) NULL,
	orderingBankName VARCHAR(35) NULL,
	orderingBankAddress VARCHAR(105) NULL,
	PRIMARY KEY ( id ASC )
);
ALTER TABLE booking ADD CONSTRAINT FK_BOOKING_REFERENCE_POSTING_FILE FOREIGN KEY ( postingFileId ) REFERENCES postingFile ( id );

DROP TABLE IF EXISTS card
;
CREATE TABLE IF NOT EXISTS card (
	id IDENTITY,
	number CHAR(16) NOT NULL,
	cardType VARCHAR(10) NOT NULL,
	cardHolderName VARCHAR(40) NOT NULL,
	email VARCHAR(50) NULL,
	phone VARCHAR(35) NULL,
	activeFrom t_date NOT NULL DEFAULT today(),
	activeTo t_date_null NULL,
	title VARCHAR(40) NULL,
	name VARCHAR(40) NOT NULL,
	name2 VARCHAR(40) NULL,
	street VARCHAR(40) NOT NULL,
	zip VARCHAR(10) NOT NULL,
	town VARCHAR(40) NOT NULL,
	country VARCHAR(40) NULL,
	settlementAccount CHAR(15) NOT NULL,
	statementPeriod INTEGER NOT NULL DEFAULT 0,
	version timestamp NOT NULL DEFAULT CURRENT TIMESTAMP,
	languageId INTEGER NOT NULL,
	"limit" DECIMAL(9,2) NULL,
	lastStatementNr INTEGER NULL,
	lastStatementDate DATE NULL,
	nextStatementDate DATE NULL,
	PRIMARY KEY ( id ASC )
);

ALTER TABLE card ADD CONSTRAINT FK_CARD_REFERENCE_CARD_TYPE FOREIGN KEY ( cardType ASC ) REFERENCES cardType ( code );
CREATE UNIQUE INDEX number ON card ( number ASC );

CREATE TABLE IF NOT EXISTS cardFeeType (
	id IDENTITY,
	feeType VARCHAR(10) NOT NULL,
	cardId INTEGER NOT NULL,
	amount DECIMAL(9,2) NOT NULL DEFAULT 0,
	percentage DECIMAL(9,2) NOT NULL DEFAULT 0,
	PRIMARY KEY ( id ASC )
);
ALTER TABLE cardFeeType ADD CONSTRAINT FK_CARD_FEE_TYPE_REFERENCE_CARD FOREIGN KEY ( cardId ) REFERENCES card ( id );
ALTER TABLE cardFeeType ADD CONSTRAINT FK_CARD_FEE_TYPE_REFERENCE_FEE_TYPE FOREIGN KEY ( feeType ) REFERENCES feeType ( code );


CREATE TABLE IF NOT EXISTS statement (
	id IDENTITY,
	number VARCHAR(5) NOT NULL,
	account VARCHAR(15) NOT NULL,
	statementDate t_date NOT NULL DEFAULT today(),
	sourceFilename VARCHAR(50) NOT NULL,
	openingBalance DECIMAL(9,2) NOT NULL DEFAULT 0,
	closingBalance DECIMAL(9,2) NOT NULL DEFAULT 0,
	closingAvailableBalance DECIMAL(9,2) NOT NULL DEFAULT 0,
	year INTEGER NOT NULL DEFAULT year(today()),
	version timestamp NOT NULL DEFAULT CURRENT TIMESTAMP,
	postingFileId INTEGER NULL,
	PRIMARY KEY ( id ASC )
);

CREATE UNIQUE INDEX accountStatementNumber ON statement ( account ASC, year ASC, number ASC );
ALTER TABLE statement ADD CONSTRAINT FK_STATEMENT_REFERENCE_POSTING_FILE FOREIGN KEY ( postingFileId ASC ) REFERENCES postingFile ( id );

CREATE TABLE IF NOT EXISTS statementLine (
	id IDENTITY,
	statementId INTEGER NOT NULL,
	number INTEGER NOT NULL,
	valueDate t_date NOT NULL DEFAULT today(),
	amount DECIMAL(9,2) NOT NULL DEFAULT 0,
	swiftType VARCHAR(4) NULL,
	reference1 VARCHAR(14) NULL,
	reference2 VARCHAR(16) NULL,
	details1 VARCHAR(35) NULL,
	details2 VARCHAR(35) NULL,
	details3 VARCHAR(35) NULL,
	details4 VARCHAR(35) NULL,
	transferType VARCHAR(10) NULL,
	cardNumber VARCHAR(16) NULL,
	cardTransactionDate t_date_null NULL,
	cardTransactionDetails1 VARCHAR(35) NULL,
	cardTransactionDetails2 VARCHAR(35) NULL,
	cardTransactionAmount DECIMAL(9,2) NULL,
	cardTransactionCurrency VARCHAR(3) NULL,
	feeType VARCHAR(10) NULL,
	version timestamp NOT NULL DEFAULT CURRENT TIMESTAMP,
	feeAmount DECIMAL(9,2) NULL,
	feeCurrency VARCHAR(3) NULL,
	bookDate DATE NULL,
	PRIMARY KEY ( id ASC )
);

CREATE UNIQUE INDEX statementLineNumber ON statementLine ( statementId ASC, number ASC );
ALTER TABLE statementLine ADD CONSTRAINT FK_STATEMENT_LINE_REFERENCE_STATEMENT FOREIGN KEY ( statementId ) REFERENCES statement ( id );
ALTER TABLE statementLine ADD CONSTRAINT FK_STATEMENT_LINE_REFERENCE_TRANSFER_TYPE FOREIGN KEY ( transferType ) REFERENCES transferType ( code );
ALTER TABLE statementLine ADD CONSTRAINT FK_STATEMENT_LINE_REFERENCE_FEE_TYPE FOREIGN KEY ( feeType ) REFERENCES feeType ( code );

CREATE OR REPLACE FORCE VIEW IF NOT EXISTS statementItemView
  as
  select s.*,p.filename as postingFilename from statement as s
      left outer join postingFile as p on s.postingFileId = p.id
;

/*
FIXME

DROP FUNCTION IF EXISTS dateDiffWorkingDays;

CREATE FUNCTION dateDiffWorkingDays( in @dateFrom t_date,in @dateTo t_date )
returns integer
begin
  declare @cnt integer;
  if @dateFrom > @dateTo then
    set @cnt = isnull((select count()*-1 from dba.calendar where id_date >= @dateTo and id_date < @dateFrom and holiday = 0),null)
  else
    set @cnt = isnull((select count() from dba.calendar where id_date > @dateFrom and id_date <= @dateTo and holiday = 0),null)
  end if;
  return @cnt
end;

*/
/*FIXME


CREATE PROCEDURE fill_calendar( y integer )
begin
  declare i integer;
  declare d date;
  declare days integer;
  set days = 366;
  if(remainder(year(now()),4) = 0) then
    set days = 367
  end if;
  set d = date(convert(varchar(4),y)+'-01-01');
  set i = 0;
  while i < days loop
    //    select dateadd(day,i,d);
    insert into calendar( id_date,holiday ) values( dateadd(day,i,d),0 ) ;
    set i = i+1;
    if year(dateadd(day,i,d)) <> y then
      set i = i+100
    end if
  end loop;
  update dba.calendar set holiday = 1 where DOW(id_date) in( 1,7 ) and Year(id_date) = y;
  return(i)
end;

*/

