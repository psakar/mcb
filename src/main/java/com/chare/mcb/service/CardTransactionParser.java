package com.chare.mcb.service;

import com.chare.mcb.entity.StatementLine;

public interface CardTransactionParser {
	void parseCardTransaction(StatementLine line);
}
