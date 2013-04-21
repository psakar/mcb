if not exists(
SELECT * FROM sys.syscolumns WHERE tname = 'card' and cname='languageId'
) begin
ALTER TABLE card ADD languageId int null
ALTER TABLE card ADD limit decimal(9,2) null
UPDATE card SET languageId = 1
ALTER TABLE card MODIFY languageId int not null
end
go