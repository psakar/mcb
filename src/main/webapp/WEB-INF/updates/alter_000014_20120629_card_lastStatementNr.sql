if not exists(
SELECT * FROM sys.syscolumns WHERE tname = 'card' and cname='lastStatementNr'
) begin
ALTER TABLE card ADD lastStatementNr int null
end
go