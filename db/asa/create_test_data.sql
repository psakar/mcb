INSERT INTO DBA.settings (id,code,description1,description2,description3,type,value) VALUES(1,'DB_VERSION','Version of database schema','Version of database schema','Verze schématu databáze',0,'1.000002')
go

INSERT INTO DBA.role (code,description1,description2,description3) VALUES('USER_ADMIN','User administration','User administration','Správa uživatelů');
go
INSERT INTO DBA.role (code,description1,description2,description3) VALUES('UPLOAD_STATEMENTS','Upload statements','Upload statements','Nahrání výpisů');
go
INSERT INTO DBA.role (code,description1,description2,description3) VALUES('EXPORT_POSTINGS','Export postings','Export postings','Export zaúčtování');
go
INSERT INTO DBA.role (code,description1,description2,description3) VALUES('APP_ADMIN','Application administration','Application administration','Správa aplikace')
go

/*password start123*/
INSERT INTO DBA.users (id,template,username,password,surname,name,languageId,email,phone,enabled,expires,unsuccessfulCount,lastAccess,password1,password2,password3,template_usersId) VALUES(0,0,'testik',0xa3b9c163f6c520407ff34cfdb83ca5c6,'Surname2','Name',4,'info@chare.eu','(+420) 221 193 111',1,NULL,0,'2012-05-30 14:20:52.794',NULL,NULL,NULL,NULL);
go
INSERT INTO DBA.users (id,template,username,password,surname,name,languageId,email,phone,enabled,expires,unsuccessfulCount,lastAccess,password1,password2,password3,template_usersId) VALUES(1,0,'appadmin',0xa3b9c163f6c520407ff34cfdb83ca5c6,'Appadmin','ěščřžýáíéĚŠČŘŽÝÁÍÉ',4,'saki@chare.eu','(+420) 221 193 674',1,NULL,0,'2012-05-29 11:59:56.523',NULL,NULL,NULL,NULL);
go
INSERT INTO DBA.users (id,template,username,password,surname,name,languageId,email,phone,enabled,expires,unsuccessfulCount,lastAccess,password1,password2,password3,template_usersId) VALUES(2,0,'upload',0xa3b9c163f6c520407ff34cfdb83ca5c6,'Upload','Jiří',3,'info@chare.eu','(+420) 221 193 674',1,NULL,0,'2003-09-08 13:25:36.950',NULL,NULL,NULL,NULL);
go
INSERT INTO DBA.users (id,template,username,password,surname,name,languageId,email,phone,enabled,expires,unsuccessfulCount,lastAccess,password1,password2,password3,template_usersId) VALUES(3,0,'export',0xa3b9c163f6c520407ff34cfdb83ca5c6,'Export','Name',3,'info@chare.eu','(+420) 221 193 674',1,NULL,0,'2003-09-08 13:25:36.950',NULL,NULL,NULL,NULL);
go
INSERT INTO DBA.users (id,template,username,password,surname,name,languageId,email,phone,enabled,expires,unsuccessfulCount,lastAccess,password1,password2,password3,template_usersId) VALUES(4,0,'useradmin',0xa3b9c163f6c520407ff34cfdb83ca5c6,'Useradmin','Name',1,'info@chare.eu','',1,NULL,0,NULL,NULL,NULL,NULL,NULL);
go

INSERT INTO DBA.userRoles (id,usersId,role) VALUES(1,0,'APP_ADMIN');
go
INSERT INTO DBA.userRoles (id,usersId,role) VALUES(2,0,'USER_ADMIN');
go
INSERT INTO DBA.userRoles (id,usersId,role) VALUES(3,0,'EXPORT_POSTINGS');
go
INSERT INTO DBA.userRoles (id,usersId,role) VALUES(4,0,'UPLOAD_STATEMENTS');
go
INSERT INTO DBA.userRoles (id,usersId,role) VALUES(5,1,'APP_ADMIN');
go
INSERT INTO DBA.userRoles (id,usersId,role) VALUES(11,2,'UPLOAD_STATEMENTS');
go
INSERT INTO DBA.userRoles (id,usersId,role) VALUES(22,3,'EXPORT_POSTINGS');
go
INSERT INTO DBA.userRoles (id,usersId,role) VALUES(23,4,'USER_ADMIN')
go


INSERT INTO DBA.language (id,code,description1,description2,description3) VALUES(1,'EN','English','Englisch','Anglický');
go
INSERT INTO DBA.language (id,code,description1,description2,description3) VALUES(2,'DE','German','Deutsch','Německý');
go
INSERT INTO DBA.language (id,code,description1,description2,description3) VALUES(3,'CZ','Czech','Tschechisch','Český');
go
INSERT INTO DBA.language (id,code,description1,description2,description3) VALUES(4,'SK','Slovak','Slovak','Slovenský')
go

INSERT INTO cardType VALUES ('BUSINESS', 'Business', 'Business', 'Business', current timestamp)
go
INSERT INTO cardType VALUES ('GOLD', 'Gold', 'Zlatá', 'Zlatá', current timestamp)
go

INSERT INTO DBA.card (id,number, cardType, cardHolderName,email,phone,activeFrom,activeTo,title,name,name2,street,zip,town,country,settlementAccount,statementPeriod,version) VALUES(1,'1234567890123456', 'BUSINESS', 'Card holder name',NULL,NULL,'2012-05-30',NULL,NULL,'Name Surname',NULL,'Street','ZIP CODE','Town',NULL,'123456789012CZK',0,'2012-05-30 15:52:55.653')
go
INSERT INTO DBA.card (id,number, cardType, cardHolderName,email,phone,activeFrom,activeTo,title,name,name2,street,zip,town,country,settlementAccount,statementPeriod,version) VALUES(2,'9999567890123456', 'GOLD', 'Card holder name Gold',NULL,NULL,'2012-05-30',NULL,NULL,'Name Surname',NULL,'Street','ZIP CODE','Town',NULL,'123456789012CZK',0,'2012-05-30 15:52:55.653')
go


INSERT INTO DBA.statement (id,number,account,statementDate,sourceFilename,openingBalance,closingBalance,closingAvailableBalance,year,version) VALUES(1,'00001','1234567','2012-01-01','sourceFilename',1.20,2.34,2.56,2012,'2012-06-09 10:53:13.000')
go
INSERT INTO DBA.statement (id,number,account,statementDate,sourceFilename,openingBalance,closingBalance,closingAvailableBalance,year,version) VALUES(2,'00002','1234567','2012-01-02','sourceFilename',2.34,4.34,6.56,2012,'2012-06-10 10:53:13.000')
go