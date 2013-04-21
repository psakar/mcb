if not exists(
SELECT * FROM sys.syscolumns WHERE tname = 'statementLine' and cname='bookDate'
) begin
ALTER TABLE statementLine ADD bookDate date null
end
go