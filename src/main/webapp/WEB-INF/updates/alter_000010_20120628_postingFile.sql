if exists (select 1
						from  sysobjects
						where  id = object_id('booking')
						and    type = 'U')
	 drop table booking
go
if exists (select 1
						from  sysobjects
						where  id = object_id('postingFile')
						and    type = 'U')
	 drop table postingFile
go

CREATE TABLE DBA.postingFile (

	id int NOT NULL default autoincrement,
	filename varchar(50) NOT NULL,

	created datetime NOT NULL DEFAULT current timestamp,
	createdUsersId int NOT NULL,

	approved datetime NULL,
	approvedUsersId int NULL,

	version datetime NOT NULL DEFAULT current timestamp,

	PRIMARY KEY ( id )
)
go

CREATE UNIQUE INDEX filename ON DBA.postingFile (filename ASC )
go
