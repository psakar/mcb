if not exists(
SELECT * FROM sys.syscolumns WHERE tname = 'card' and cname='lastStatementDate'
) begin
ALTER TABLE card ADD lastStatementDate date null
end
go

if not exists(
SELECT * FROM sys.syscolumns WHERE tname = 'card' and cname='nextStatementDate'
) begin
ALTER TABLE card ADD nextStatementDate date null
end
go