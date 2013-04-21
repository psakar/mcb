if not exists(
SELECT * FROM sys.syscolumns WHERE tname = 'transferType' and cname='cardTransactionType'
) begin
ALTER TABLE transferType ADD cardTransactionType int null
end
go