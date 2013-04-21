if not exists(
SELECT * FROM sys.syscolumns WHERE tname = 'statement' and cname='postingFileId'
) begin
ALTER TABLE statement ADD postingFileId int null
end
go


ALTER TABLE DBA.statement ADD CONSTRAINT FK_STATEMENT_REFERENCE_POSTING_FILE FOREIGN KEY ( postingFileId ASC ) REFERENCES DBA.postingFile ( id )
go
