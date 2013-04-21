package com.chare.mcb.service;

import java.io.InputStream;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.chare.mcb.entity.Statement;
import com.chare.mcb.repository.StatementRepository;
import com.chare.payment.service.BlockOfLines;
import com.chare.service.LinesReader;
import com.chare.service.LinesReader.ReadingSpecification;

class UploadFileServiceImpl implements UploadFileService {

	private final LinesReader linesReader;
	private final SwiftParser swiftParser;
	private final StatementRepository statementRepository;
	private final TransferTypeResolver transferTypeResolver;
	private final SwiftBlocksParser swiftBlocksParser;

	UploadFileServiceImpl(LinesReader linesReader, SwiftParser swiftParser, SwiftBlocksParser swiftBlocksParser, StatementRepository statementRepository, TransferTypeResolver transferTypeResolver) {
		this.linesReader = linesReader;
		this.swiftParser = swiftParser;
		this.swiftBlocksParser = swiftBlocksParser;
		this.statementRepository = statementRepository;
		this.transferTypeResolver = transferTypeResolver;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<Statement> uploadFile(InputStream inputStream, String filename) {

		List<String> lines;
		try {
			lines = linesReader.read(inputStream, getSpecification());
			List<BlockOfLines> blocks = swiftParser.parse(lines);
			List<Statement> statements = swiftBlocksParser.parse(blocks);
			for (Statement statement : statements) {
				statement.sourceFilename = filename;
				statementRepository.persist(statement);
			}
			for (Statement statement : statements) {
				transferTypeResolver.resolve(statement);
			}
			return statements;
		} catch (Exception e) {
			throw new RuntimeException("Can not parse statement " + e.getMessage(), e);
		}
	}

	public ReadingSpecification getSpecification() {
		return new ReadingSpecification("cp1250", 10000, 80);
	}

}
