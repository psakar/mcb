if not exists(
SELECT * FROM sys.syscolumns WHERE tname = 'booking' and cname='creditAccount' and length=17
) begin
ALTER TABLE booking MODIFY creditAccount varchar(17) null
end
go
if not exists(
SELECT * FROM sys.syscolumns WHERE tname = 'booking' and cname='debitAccount' and length=17
) begin
ALTER TABLE booking MODIFY debitAccount varchar(17) null
end
go

if exists(
SELECT * FROM sys.syscolumns WHERE tname = 'booking' and cname='valueDate'
) begin
ALTER TABLE booking DROP valueDate
end
go
