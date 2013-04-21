UPDATE booking SET creditAccount = left(creditAccount, 15) WHERE creditAccount is not null
go

UPDATE booking SET debitAccount = left(debitAccount, 15) WHERE debitAccount is not null
go

if not exists(
SELECT * FROM sys.syscolumns WHERE tname = 'booking' and cname='creditAccount' and length=15
) begin
ALTER TABLE booking MODIFY creditAccount varchar(15) null
end
go
if not exists(
SELECT * FROM sys.syscolumns WHERE tname = 'booking' and cname='debitAccount' and length=15
) begin
ALTER TABLE booking MODIFY debitAccount varchar(15) null
end
go

