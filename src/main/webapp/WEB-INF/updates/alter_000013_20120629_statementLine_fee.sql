if not exists(
SELECT * FROM sys.syscolumns WHERE tname = 'statementLine' and cname='feeAmount'
) begin
ALTER TABLE statementLine ADD feeAmount decimal(9,2) NULL
ALTER TABLE statementLine ADD feeCurrency varchar(3) NULL
end
go


