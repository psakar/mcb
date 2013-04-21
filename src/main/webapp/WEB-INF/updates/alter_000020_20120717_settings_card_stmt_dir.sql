if not exists(
SELECT * FROM settings WHERE code = 'CARD_STMT_DIR'
) begin
INSERT INTO settings (id, code, description1, description2, description3, type, value)
VALUES (6, 'CARD_STMT_DIR', 'Directory for card statements', 'Adresář pro výpisy karet', 'Adresář pro výpisy karet', 0, '.')
end
go
