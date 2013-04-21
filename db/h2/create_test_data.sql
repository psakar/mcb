INSERT INTO settings (code,description1,description2,description3,type,value) SELECT 'DB_VERSION','Version of database schema','Version of database schema','Verze schématu databáze',0,'1.000025'
FROM DUAL WHERE NOT EXISTS (SELECT * FROM settings WHERE code = 'DB_VERSION')
;

INSERT INTO role (code,description1,description2,description3)  SELECT 'APP_ADMIN','Application administration','Application administration','Správa aplikace'
FROM DUAL WHERE NOT EXISTS (SELECT * FROM role WHERE code = 'APP_ADMIN')
;
INSERT INTO role (code,description1,description2,description3) SELECT 'EXPORT_POSTINGS','Export postings','Export postings','Export zaúčtování'
FROM DUAL WHERE NOT EXISTS (SELECT * FROM role WHERE code = 'EXPORT_POSTINGS')
;
INSERT INTO role (code,description1,description2,description3) SELECT 'UPLOAD_STATEMENTS','Upload statements','Upload statements','Nahrání výpisů'
FROM DUAL WHERE NOT EXISTS (SELECT * FROM role WHERE code = 'UPLOAD_STATEMENTS')
;
INSERT INTO role (code,description1,description2,description3) SELECT 'USER_ADMIN','User administration','User administration','Správa uživatelů'
FROM DUAL WHERE NOT EXISTS (SELECT * FROM role WHERE code = 'USER_ADMIN')
;

/*password start123*/
INSERT INTO users (template,username,password,surname,name,languageId,email,phone,enabled,expires,unsuccessfulCount,lastAccess,password1,password2,password3,template_usersId) SELECT 0,'testik',X'a3b9c163f6c520407ff34cfdb83ca5c6','Surname2','Name',4,'info@chare.eu','(+420) 123 456 789',1,NULL,0,'2012-05-30 14:20:52.794',NULL,NULL,NULL,NULL
FROM DUAL WHERE NOT EXISTS (SELECT * FROM users WHERE username = 'testik')
;
INSERT INTO users (template,username,password,surname,name,languageId,email,phone,enabled,expires,unsuccessfulCount,lastAccess,password1,password2,password3,template_usersId) SELECT 0,'appadmin',X'a3b9c163f6c520407ff34cfdb83ca5c6','Appadmin','ěščřžýáíéĚŠČŘŽÝÁÍÉ',4,'info@chare.eu','(+420) 123 456 789',1,NULL,0,'2012-05-29 11:59:56.523',NULL,NULL,NULL,NULL
FROM DUAL WHERE NOT EXISTS (SELECT * FROM users WHERE username = 'appadmin')
;
INSERT INTO users (template,username,password,surname,name,languageId,email,phone,enabled,expires,unsuccessfulCount,lastAccess,password1,password2,password3,template_usersId) SELECT 0,'upload',X'a3b9c163f6c520407ff34cfdb83ca5c6','Upload','Jiří',3,'info@chare.eu','(+420) 123 456 789',1,NULL,0,'2003-09-08 13:25:36.950',NULL,NULL,NULL,NULL
FROM DUAL WHERE NOT EXISTS (SELECT * FROM users WHERE username = 'upload')
;
INSERT INTO users (template,username,password,surname,name,languageId,email,phone,enabled,expires,unsuccessfulCount,lastAccess,password1,password2,password3,template_usersId) SELECT 0,'export',X'a3b9c163f6c520407ff34cfdb83ca5c6','Export','Name',3,'info@chare.eu','(+420) 123 456 789',1,NULL,0,'2003-09-08 13:25:36.950',NULL,NULL,NULL,NULL
FROM DUAL WHERE NOT EXISTS (SELECT * FROM users WHERE username = 'export')
;
INSERT INTO users (template,username,password,surname,name,languageId,email,phone,enabled,expires,unsuccessfulCount,lastAccess,password1,password2,password3,template_usersId) SELECT 0,'useradmin',X'a3b9c163f6c520407ff34cfdb83ca5c6','Useradmin','Name',1,'info@chare.eu', NULL, 1, NULL,0,NULL,NULL,NULL,NULL,NULL
FROM DUAL WHERE NOT EXISTS (SELECT * FROM users WHERE username = 'useradmin')
;

INSERT INTO userRoles (usersId,role) SELECT (SELECT id FROM users WHERE username = 'testik'),'APP_ADMIN'
FROM DUAL WHERE NOT EXISTS (SELECT * FROM userRoles WHERE role = 'APP_ADMIN' AND usersId = (SELECT id FROM users WHERE username = 'testik'))
;
INSERT INTO userRoles (usersId,role) SELECT (SELECT id FROM users WHERE username = 'testik'),'USER_ADMIN'
FROM DUAL WHERE NOT EXISTS (SELECT * FROM userRoles WHERE role = 'USER_ADMIN' AND usersId = (SELECT id FROM users WHERE username = 'testik'))
;
INSERT INTO userRoles (usersId,role) SELECT (SELECT id FROM users WHERE username = 'testik'),'EXPORT_POSTINGS'
FROM DUAL WHERE NOT EXISTS (SELECT * FROM userRoles WHERE role = 'EXPORT_POSTINGS' AND usersId = (SELECT id FROM users WHERE username = 'testik'))
;
INSERT INTO userRoles (usersId,role) SELECT (SELECT id FROM users WHERE username = 'testik'),'UPLOAD_STATEMENTS'
FROM DUAL WHERE NOT EXISTS (SELECT * FROM userRoles WHERE role = 'UPLOAD_STATEMENTS' AND usersId = (SELECT id FROM users WHERE username = 'testik'))
;
INSERT INTO userRoles (usersId,role) SELECT (SELECT id FROM users WHERE username = 'appadmin'),'APP_ADMIN'
FROM DUAL WHERE NOT EXISTS (SELECT * FROM userRoles WHERE role = 'APP_ADMIN' AND usersId = (SELECT id FROM users WHERE username = 'appadmin'))
;
INSERT INTO userRoles (usersId,role) SELECT (SELECT id FROM users WHERE username = 'upload'),'UPLOAD_STATEMENTS'
FROM DUAL WHERE NOT EXISTS (SELECT * FROM userRoles WHERE role = 'UPLOAD_STATEMENTS' AND usersId = (SELECT id FROM users WHERE username = 'upload'))
;
INSERT INTO userRoles (usersId,role) SELECT (SELECT id FROM users WHERE username = 'export'),'EXPORT_POSTINGS'
FROM DUAL WHERE NOT EXISTS (SELECT * FROM userRoles WHERE role = 'EXPORT_POSTINGS' AND usersId = (SELECT id FROM users WHERE username = 'export'))
;
INSERT INTO userRoles (usersId,role) SELECT (SELECT id FROM users WHERE username = 'useradmin'),'USER_ADMIN'
FROM DUAL WHERE NOT EXISTS (SELECT * FROM userRoles WHERE role = 'USER_ADMIN' AND usersId = (SELECT id FROM users WHERE username = 'useradmin'))
;

INSERT INTO language (id,code,description1,description2,description3) VALUES(1,'EN','English','Englisch','Anglický')
;
INSERT INTO language (id,code,description1,description2,description3) VALUES(2,'DE','German','Deutsch','Německý')
;
INSERT INTO language (id,code,description1,description2,description3) VALUES(3,'CZ','Czech','Tschechisch','Český')
;
INSERT INTO language (id,code,description1,description2,description3) VALUES(4,'SK','Slovak','Slovak','Slovenský')
;

INSERT INTO cardType VALUES ('BUSINESS', 'Business', 'Business', 'Business', current timestamp)
;
INSERT INTO cardType VALUES ('GOLD', 'Gold', 'Zlatá', 'Zlatá', current timestamp)
;

INSERT INTO card (number, cardType, cardHolderName,email,phone,activeFrom,activeTo,title,name,name2,street,zip,town,country,settlementAccount,statementPeriod,version, languageId)
SELECT '1234567890123456', 'BUSINESS', 'Card holder name',NULL,NULL,'2012-05-30',NULL,NULL,'Name Surname',NULL,'Street','ZIP CODE','Town',NULL,'123456789012CZK',0,'2012-05-30 15:52:55.653', 1
FROM DUAL WHERE NOT EXISTS (
SELECT * FROM card WHERE number = '1234567890123456'
)
;
INSERT INTO card (number, cardType, cardHolderName,email,phone,activeFrom,activeTo,title,name,name2,street,zip,town,country,settlementAccount,statementPeriod,version, languageId)
SELECT '9999567890123456', 'GOLD', 'Card holder name Gold',NULL,NULL,'2012-05-30',NULL,NULL,'Name Surname',NULL,'Street','ZIP CODE','Town',NULL,'123456789012CZK',0,'2012-05-30 15:52:55.653', 1
FROM DUAL WHERE NOT EXISTS (
SELECT * FROM card WHERE number = '9999567890123456'
)
;


INSERT INTO statement (number,account,statementDate,sourceFilename,openingBalance,closingBalance,closingAvailableBalance,year,version)
SELECT '00001','1234567','2012-01-01','sourceFilename',1.20,2.34,2.56,2012,'2012-06-09 10:53:13.000'
FROM DUAL WHERE NOT EXISTS (
SELECT * FROM statement WHERE number = '00001'
)
;
INSERT INTO statement (number,account,statementDate,sourceFilename,openingBalance,closingBalance,closingAvailableBalance,year,version)
SELECT '00002','1234567','2012-01-02','sourceFilename',2.34,4.34,6.56,2012,'2012-06-10 10:53:13.000'
FROM DUAL WHERE NOT EXISTS (
SELECT * FROM statement WHERE number = '00002'
)
;

INSERT INTO transferType (code,description1,description2,description3,settlementType,settlementAccount,version,cardTransactionType)
SELECT 'FMSCCLT','Card payment','Platba kartou','Platba kartou',0,null, '2012-06-30 13:43:38',0 FROM DUAL
WHERE NOT EXISTS (
SELECT * FROM transferType WHERE code = 'FMSCCLT'
)
;
INSERT INTO transferType (code,description1,description2,description3,settlementType,settlementAccount,version,cardTransactionType)
SELECT 'FMSCFEE','Fee for card payment','Poplatok za platbu kartou','Poplatek za platbu kartou',2,null, '2012-06-30 13:43:52',1
WHERE NOT EXISTS (
SELECT * FROM transferType WHERE code = 'FMSCFEE'
)
;

INSERT INTO transferType (code,description1,description2,description3,settlementType,settlementAccount,version,cardTransactionType)
SELECT 'CMFEE','Card management fee','Poplatok za správu karet','Poplatek za správu karet',1,'COST56789012EUR', '2012-06-01 11:02:43',null
WHERE NOT EXISTS (
SELECT * FROM transferType WHERE code = 'CMFEE'
)
;

INSERT INTO transferType (code,description1,description2,description3,settlementType,settlementAccount,version,cardTransactionType)
SELECT 'ICFEE','Issued card fee','Poplatok za vydanie kárty','Poplatek za vydání karty',2,'PROFIT789012EUR', '2012-06-01 11:02:43',null
WHERE NOT EXISTS (
SELECT * FROM transferType WHERE code = 'ICFEE'
)
;

INSERT INTO transferType (code,description1,description2,description3,settlementType,settlementAccount,version,cardTransactionType)
SELECT 'SWIFT','Fee for SWIFT messages','Poplatek za SWIFT zprávy','Poplatek za SWIFT zprávy',1,'197111444400EUR', '2012-06-05 18:37:45',null
WHERE NOT EXISTS (
SELECT * FROM transferType WHERE code = 'SWIFT'
)
;

INSERT INTO transferType (code,description1,description2,description3,settlementType,settlementAccount,version,cardTransactionType)
SELECT 'FMSCCRG','Card management fee','Card management fee','Card management fee',2,null, '2012-06-30 13:44:55',2
WHERE NOT EXISTS (
SELECT * FROM transferType WHERE code = 'FMSCCRG'
)
;

INSERT INTO feeType (code,description1,description2,description3,cardType,amount,percentage,settlementAccount,transferType,version)
SELECT 'B_ICFEE','Issue Business card fee','Poplatok za vydanie Business kárty','Poplatek za vydání Business karty','BUSINESS',5.00,0.01,'PROFIT123456EUR','ICFEE', '2012-06-01 23:24:18'
FROM DUAL WHERE NOT EXISTS (
SELECT code FROM feeType WHERE code = 'B_ICFEE'
)
;
INSERT INTO feeType (code,description1,description2,description3,cardType,amount,percentage,settlementAccount,transferType,version)
SELECT 'G_ICFEE','Issue Gold card fee','Poplatok za vydanie Gold kárty','Poplatek za vydání Gold karty','GOLD',15.00,0.00,'PROFIT123456EUR','ICFEE', '2012-06-01 11:02:43'
FROM DUAL WHERE NOT EXISTS (
SELECT code FROM feeType WHERE code = 'B_ICFEE'
)
;
INSERT INTO feeType (code,description1,description2,description3,cardType,amount,percentage,settlementAccount,transferType,version)
SELECT 'FEE_BUS','Fee business card','Fee business card','Fee business card','BUSINESS',10.00,0.00,null,'FMSCFEE', '2012-06-29 10:18:29'
FROM DUAL WHERE NOT EXISTS (
SELECT code FROM feeType WHERE code = 'FEE_BUS'
)
;
INSERT INTO feeType (code,description1,description2,description3,cardType,amount,percentage,settlementAccount,transferType,version)
SELECT 'G_CMFEE','Card management fee','Card management fee','Card management fee','GOLD',10.81,0.00,null,'FMSCCRG', '2012-06-30 13:45:40'
FROM DUAL WHERE NOT EXISTS (
SELECT code FROM feeType WHERE code = 'G_CMFEE'
)
;
INSERT INTO feeType (code,description1,description2,description3,cardType,amount,percentage,settlementAccount,transferType,version)
SELECT 'B_CMFEE','Card management fee','Card management fee','Card management fee','BUSINESS',10.00,0.00,null,'FMSCCRG', '2012-06-30 13:46:23'
FROM DUAL WHERE NOT EXISTS (
SELECT code FROM feeType WHERE code = 'B_CMFEE'
)
;
INSERT INTO feeType (code,description1,description2,description3,cardType,amount,percentage,settlementAccount,transferType,version)
SELECT 'G_FMSCFEE','Gold fee','Gold fee','Gold fee','GOLD',10.00,0.00,'197111444500EUR','FMSCFEE', '2012-07-16 18:46:20'
FROM DUAL WHERE NOT EXISTS (
SELECT code FROM feeType WHERE code = 'G_FMSCFEE'
)
;
INSERT INTO feeType (code,description1,description2,description3,cardType,amount,percentage,settlementAccount,transferType,version)
SELECT 'B_FMSCFEE','Business card transaction fee','Business card transaction fee','Business card transaction fee','BUSINESS',5.00,0.00,'197111444599EUR','FMSCFEE', '2012-07-17 08:30:40'
FROM DUAL WHERE NOT EXISTS (
SELECT code FROM feeType WHERE code = 'B_FMSCFEE'
)

INSERT INTO calendar ( id_date,holiday ) values ( '2012-01-01', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-01-02', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-01-03', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-01-04', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-01-05', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-01-06', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-01-07', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-01-08', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-01-09', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-01-10', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-01-11', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-01-12', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-01-13', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-01-14', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-01-15', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-01-16', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-01-17', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-01-18', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-01-19', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-01-20', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-01-21', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-01-22', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-01-23', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-01-24', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-01-25', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-01-26', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-01-27', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-01-28', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-01-29', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-01-30', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-01-31', 0 ) ;

INSERT INTO calendar ( id_date,holiday ) values ( '2012-02-01', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-02-02', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-02-03', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-02-04', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-02-05', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-02-06', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-02-07', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-02-08', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-02-09', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-02-10', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-02-11', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-02-12', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-02-13', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-02-14', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-02-15', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-02-16', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-02-17', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-02-18', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-02-19', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-02-20', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-02-21', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-02-22', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-02-23', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-02-24', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-02-25', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-02-26', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-02-27', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-02-28', 0 ) ;

INSERT INTO calendar ( id_date,holiday ) values ( '2012-03-01', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-03-02', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-03-03', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-03-04', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-03-05', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-03-06', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-03-07', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-03-08', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-03-09', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-03-10', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-03-11', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-03-12', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-03-13', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-03-14', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-03-15', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-03-16', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-03-17', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-03-18', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-03-19', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-03-20', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-03-21', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-03-22', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-03-23', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-03-24', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-03-25', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-03-26', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-03-27', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-03-28', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-03-29', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-03-30', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-03-31', 0 ) ;


INSERT INTO calendar ( id_date,holiday ) values ( '2012-04-01', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-04-02', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-04-03', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-04-04', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-04-05', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-04-06', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-04-07', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-04-08', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-04-09', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-04-10', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-04-11', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-04-12', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-04-13', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-04-14', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-04-15', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-04-16', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-04-17', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-04-18', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-04-19', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-04-20', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-04-21', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-04-22', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-04-23', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-04-24', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-04-25', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-04-26', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-04-27', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-04-28', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-04-29', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-04-30', 0 ) ;

INSERT INTO calendar ( id_date,holiday ) values ( '2012-05-01', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-05-02', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-05-03', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-05-04', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-05-05', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-05-06', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-05-07', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-05-08', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-05-09', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-05-10', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-05-11', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-05-12', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-05-13', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-05-14', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-05-15', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-05-16', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-05-17', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-05-18', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-05-19', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-05-20', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-05-21', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-05-22', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-05-23', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-05-24', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-05-25', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-05-26', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-05-27', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-05-28', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-05-29', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-05-30', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-05-31', 0 ) ;

INSERT INTO calendar ( id_date,holiday ) values ( '2012-06-01', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-06-02', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-06-03', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-06-04', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-06-05', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-06-06', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-06-07', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-06-08', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-06-09', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-06-10', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-06-11', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-06-12', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-06-13', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-06-14', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-06-15', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-06-16', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-06-17', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-06-18', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-06-19', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-06-20', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-06-21', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-06-22', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-06-23', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-06-24', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-06-25', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-06-26', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-06-27', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-06-28', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-06-29', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-06-30', 0 ) ;



INSERT INTO calendar ( id_date,holiday ) values ( '2012-07-01', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-07-02', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-07-03', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-07-04', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-07-05', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-07-06', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-07-07', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-07-08', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-07-09', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-07-10', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-07-11', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-07-12', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-07-13', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-07-14', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-07-15', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-07-16', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-07-17', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-07-18', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-07-19', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-07-20', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-07-21', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-07-22', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-07-23', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-07-24', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-07-25', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-07-26', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-07-27', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-07-28', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-07-29', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-07-30', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-07-31', 0 ) ;

INSERT INTO calendar ( id_date,holiday ) values ( '2012-08-01', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-08-02', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-08-03', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-08-04', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-08-05', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-08-06', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-08-07', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-08-08', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-08-09', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-08-10', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-08-11', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-08-12', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-08-13', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-08-14', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-08-15', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-08-16', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-08-17', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-08-18', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-08-19', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-08-20', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-08-21', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-08-22', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-08-23', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-08-24', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-08-25', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-08-26', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-08-27', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-08-28', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-08-29', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-08-30', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-08-31', 0 ) ;

INSERT INTO calendar ( id_date,holiday ) values ( '2012-09-01', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-09-02', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-09-03', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-09-04', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-09-05', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-09-06', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-09-07', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-09-08', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-09-09', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-09-10', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-09-11', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-09-12', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-09-13', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-09-14', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-09-15', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-09-16', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-09-17', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-09-18', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-09-19', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-09-20', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-09-21', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-09-22', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-09-23', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-09-24', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-09-25', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-09-26', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-09-27', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-09-28', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-09-29', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-09-30', 0 ) ;

INSERT INTO calendar ( id_date,holiday ) values ( '2012-10-01', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-10-02', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-10-03', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-10-04', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-10-05', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-10-06', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-10-07', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-10-08', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-10-09', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-10-10', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-10-11', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-10-12', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-10-13', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-10-14', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-10-15', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-10-16', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-10-17', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-10-18', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-10-19', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-10-20', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-10-21', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-10-22', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-10-23', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-10-24', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-10-25', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-10-26', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-10-27', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-10-28', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-10-29', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-10-30', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-10-31', 0 ) ;

INSERT INTO calendar ( id_date,holiday ) values ( '2012-11-01', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-11-02', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-11-03', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-11-04', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-11-05', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-11-06', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-11-07', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-11-08', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-11-09', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-11-10', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-11-11', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-11-12', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-11-13', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-11-14', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-11-15', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-11-16', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-11-17', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-11-18', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-11-19', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-11-20', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-11-21', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-11-22', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-11-23', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-11-24', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-11-25', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-11-26', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-11-27', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-11-28', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-11-29', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-11-30', 0 ) ;

INSERT INTO calendar ( id_date,holiday ) values ( '2012-12-01', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-12-02', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-12-03', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-12-04', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-12-05', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-12-06', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-12-07', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-12-08', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-12-09', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-12-10', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-12-11', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-12-12', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-12-13', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-12-14', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-12-15', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-12-16', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-12-17', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-12-18', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-12-19', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-12-20', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-12-21', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-12-22', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-12-23', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-12-24', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-12-25', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-12-26', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-12-27', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-12-28', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-12-29', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-12-30', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2012-12-31', 0 ) ;

update calendar set holiday = 1 where DAY_OF_WEEK(id_date) in( 1,7 ) and Year(id_date) = 2012
;

INSERT INTO calendar ( id_date,holiday ) values ( '2013-01-01', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-01-02', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-01-03', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-01-04', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-01-05', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-01-06', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-01-07', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-01-08', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-01-09', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-01-10', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-01-11', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-01-12', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-01-13', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-01-14', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-01-15', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-01-16', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-01-17', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-01-18', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-01-19', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-01-20', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-01-21', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-01-22', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-01-23', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-01-24', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-01-25', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-01-26', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-01-27', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-01-28', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-01-29', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-01-30', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-01-31', 0 ) ;

INSERT INTO calendar ( id_date,holiday ) values ( '2013-02-01', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-02-02', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-02-03', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-02-04', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-02-05', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-02-06', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-02-07', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-02-08', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-02-09', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-02-10', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-02-11', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-02-12', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-02-13', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-02-14', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-02-15', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-02-16', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-02-17', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-02-18', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-02-19', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-02-20', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-02-21', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-02-22', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-02-23', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-02-24', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-02-25', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-02-26', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-02-27', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-02-28', 0 ) ;

INSERT INTO calendar ( id_date,holiday ) values ( '2013-03-01', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-03-02', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-03-03', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-03-04', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-03-05', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-03-06', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-03-07', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-03-08', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-03-09', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-03-10', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-03-11', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-03-12', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-03-13', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-03-14', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-03-15', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-03-16', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-03-17', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-03-18', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-03-19', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-03-20', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-03-21', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-03-22', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-03-23', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-03-24', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-03-25', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-03-26', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-03-27', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-03-28', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-03-29', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-03-30', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-03-31', 0 ) ;


INSERT INTO calendar ( id_date,holiday ) values ( '2013-04-01', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-04-02', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-04-03', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-04-04', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-04-05', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-04-06', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-04-07', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-04-08', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-04-09', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-04-10', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-04-11', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-04-12', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-04-13', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-04-14', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-04-15', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-04-16', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-04-17', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-04-18', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-04-19', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-04-20', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-04-21', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-04-22', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-04-23', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-04-24', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-04-25', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-04-26', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-04-27', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-04-28', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-04-29', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-04-30', 0 ) ;

INSERT INTO calendar ( id_date,holiday ) values ( '2013-05-01', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-05-02', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-05-03', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-05-04', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-05-05', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-05-06', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-05-07', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-05-08', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-05-09', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-05-10', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-05-11', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-05-12', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-05-13', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-05-14', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-05-15', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-05-16', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-05-17', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-05-18', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-05-19', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-05-20', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-05-21', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-05-22', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-05-23', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-05-24', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-05-25', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-05-26', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-05-27', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-05-28', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-05-29', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-05-30', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-05-31', 0 ) ;

INSERT INTO calendar ( id_date,holiday ) values ( '2013-06-01', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-06-02', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-06-03', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-06-04', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-06-05', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-06-06', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-06-07', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-06-08', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-06-09', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-06-10', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-06-11', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-06-12', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-06-13', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-06-14', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-06-15', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-06-16', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-06-17', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-06-18', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-06-19', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-06-20', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-06-21', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-06-22', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-06-23', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-06-24', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-06-25', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-06-26', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-06-27', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-06-28', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-06-29', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-06-30', 0 ) ;



INSERT INTO calendar ( id_date,holiday ) values ( '2013-07-01', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-07-02', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-07-03', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-07-04', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-07-05', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-07-06', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-07-07', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-07-08', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-07-09', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-07-10', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-07-11', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-07-12', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-07-13', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-07-14', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-07-15', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-07-16', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-07-17', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-07-18', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-07-19', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-07-20', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-07-21', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-07-22', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-07-23', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-07-24', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-07-25', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-07-26', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-07-27', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-07-28', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-07-29', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-07-30', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-07-31', 0 ) ;

INSERT INTO calendar ( id_date,holiday ) values ( '2013-08-01', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-08-02', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-08-03', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-08-04', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-08-05', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-08-06', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-08-07', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-08-08', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-08-09', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-08-10', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-08-11', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-08-12', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-08-13', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-08-14', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-08-15', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-08-16', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-08-17', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-08-18', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-08-19', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-08-20', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-08-21', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-08-22', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-08-23', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-08-24', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-08-25', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-08-26', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-08-27', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-08-28', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-08-29', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-08-30', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-08-31', 0 ) ;

INSERT INTO calendar ( id_date,holiday ) values ( '2013-09-01', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-09-02', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-09-03', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-09-04', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-09-05', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-09-06', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-09-07', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-09-08', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-09-09', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-09-10', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-09-11', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-09-12', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-09-13', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-09-14', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-09-15', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-09-16', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-09-17', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-09-18', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-09-19', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-09-20', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-09-21', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-09-22', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-09-23', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-09-24', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-09-25', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-09-26', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-09-27', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-09-28', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-09-29', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-09-30', 0 ) ;

INSERT INTO calendar ( id_date,holiday ) values ( '2013-10-01', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-10-02', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-10-03', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-10-04', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-10-05', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-10-06', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-10-07', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-10-08', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-10-09', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-10-10', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-10-11', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-10-12', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-10-13', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-10-14', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-10-15', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-10-16', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-10-17', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-10-18', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-10-19', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-10-20', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-10-21', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-10-22', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-10-23', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-10-24', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-10-25', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-10-26', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-10-27', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-10-28', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-10-29', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-10-30', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-10-31', 0 ) ;

INSERT INTO calendar ( id_date,holiday ) values ( '2013-11-01', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-11-02', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-11-03', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-11-04', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-11-05', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-11-06', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-11-07', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-11-08', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-11-09', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-11-10', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-11-11', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-11-12', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-11-13', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-11-14', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-11-15', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-11-16', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-11-17', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-11-18', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-11-19', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-11-20', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-11-21', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-11-22', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-11-23', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-11-24', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-11-25', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-11-26', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-11-27', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-11-28', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-11-29', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-11-30', 0 ) ;

INSERT INTO calendar ( id_date,holiday ) values ( '2013-12-01', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-12-02', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-12-03', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-12-04', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-12-05', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-12-06', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-12-07', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-12-08', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-12-09', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-12-10', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-12-11', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-12-12', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-12-13', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-12-14', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-12-15', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-12-16', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-12-17', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-12-18', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-12-19', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-12-20', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-12-21', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-12-22', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-12-23', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-12-24', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-12-25', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-12-26', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-12-27', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-12-28', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-12-29', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-12-30', 0 ) ;
INSERT INTO calendar ( id_date,holiday ) values ( '2013-12-31', 0 ) ;


update calendar set holiday = 1 where DAY_OF_WEEK(id_date) in( 1,7 ) and Year(id_date) = 2013;
