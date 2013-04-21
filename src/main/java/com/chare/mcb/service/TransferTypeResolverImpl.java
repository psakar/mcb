package com.chare.mcb.service;

import com.chare.mcb.entity.Statement;
import com.chare.mcb.entity.StatementLine;
import com.chare.mcb.repository.TransferTypeRepository;

class TransferTypeResolverImpl implements TransferTypeResolver {

	private final TransferTypeRepository repository;
	private final CardTransactionParser parser;

	TransferTypeResolverImpl(TransferTypeRepository repository, CardTransactionParser parser) {
		this.repository = repository;
		this.parser = parser;
	}

	@Override
	public void resolve(Statement statement) {
		for (StatementLine line : statement.getLines()) {
			if (line.transferType == null) {
				String transferTypeCode = line.getTransferTypeCode();
				if (transferTypeCode != null)
					line.transferType = repository.findById(transferTypeCode);
			}
			parser.parseCardTransaction(line);
		}

	}


}
