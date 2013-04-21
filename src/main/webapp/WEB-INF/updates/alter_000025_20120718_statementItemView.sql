if exists (select 1
						from  sysobjects
						where  id = object_id('statementItemView')
						and    type = 'V')
	 drop VIEW statementItemView
go

CREATE VIEW statementItemView
AS SELECT s.*, p.filename as postingFilename FROM statement s
LEFT JOIN postingFile p ON s.postingFileId = p.id
go