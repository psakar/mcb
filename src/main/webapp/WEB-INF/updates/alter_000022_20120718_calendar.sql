if exists (select 1
						from  sysobjects
						where  id = object_id('calendar')
						and    type = 'U')
	 drop table calendar
go

CREATE TABLE DBA.calendar (
	id_date DATE NOT NULL,
	holiday t_boolean NOT NULL DEFAULT 0,
	PRIMARY KEY ( id_date ASC )
)
go

COMMENT ON TABLE DBA.calendar IS 'List of bussines days and holidays'
go

if (not exists (
SELECT * FROM sys.sysindexes WHERE tname = 'calendar' AND iname = 'holiday'
))
CREATE INDEX holiday ON dba.calendar (id_date, holiday)
go
