if exists(
SELECT * FROM sys.syscolumns WHERE tname = 'users' and cname='gsm'
) begin
ALTER TABLE users DROP gsm
ALTER TABLE users DROP fax
end
go