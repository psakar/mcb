if not exists(
SELECT * FROM settings WHERE code = 'TRANSACT_ACC'
) begin
INSERT INTO settings (id, code, description1, description2, description3, type, value)
VALUES (2, 'TRANSACT_ACC', 'Technical account of payment cards EC/MC', 'Technické konto platebních karet EC/MC', 'Technické konto platobných kariet EC/MC', 0, '197931361000EUR')
end
go
if not exists(
SELECT * FROM settings WHERE code = 'MIRROR_ACC'
) begin
INSERT INTO settings (id, code, description1, description2, description3, type, value)
VALUES (3, 'MIRROR_ACC', 'UniCredit Bank Slovakia a.s.', 'UniCredit Bank Slovakia a.s.', 'UniCredit Bank Slovakia a.s.', 0, '197720002500EUR')
end
go

if not exists(
SELECT * FROM settings WHERE code = 'COST_ACC'
) begin
INSERT INTO settings (id, code, description1, description2, description3, type, value)
VALUES (4, 'COST_ACC', 'Cost account EC/MC', 'Účet nákladů EC/MC', 'Konto nákladov EC/MC', 0, '19746106106000')
end
go

if not exists(
SELECT * FROM settings WHERE code = 'EXPORT_DIR'
) begin
INSERT INTO settings (id, code, description1, description2, description3, type, value)
VALUES (5, 'EXPORT_DIR', 'Directory for exported posting files', 'Adresář pro export posting souborů', 'Adresář pro export posting souborů', 0, '.')
end
go
