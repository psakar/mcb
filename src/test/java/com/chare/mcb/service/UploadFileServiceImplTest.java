package com.chare.mcb.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.chare.mcb.entity.Statement;
import com.chare.mcb.repository.StatementRepository;
import com.chare.payment.service.BlockOfLines;
import com.chare.service.LinesReader;

public class UploadFileServiceImplTest {


	private UploadFileServiceImpl uploadFile;

	@Mock
	private LinesReader linesReader;

	@Mock
	private SwiftParser swiftParser;

	@Mock
	private StatementRepository statementRepository;

	@Mock
	private TransferTypeResolver transferTypeResolver;

	@Mock
	private SwiftBlocksParser swiftBlocksParser;

	@Before
	public void before() throws Exception {
		MockitoAnnotations.initMocks(this);
		uploadFile = new UploadFileServiceImpl(linesReader, swiftParser, swiftBlocksParser, statementRepository, transferTypeResolver);
	}

	@Test
	public void testFileProcessing() throws Exception {
		FileInputStream inputStream = mock(FileInputStream.class);

		List<String> lines = Arrays.asList("line1", "line2");
		when(linesReader.read(inputStream, uploadFile.getSpecification())).thenReturn(lines);

		List<BlockOfLines> blocks = Arrays.asList(new BlockOfLines(0, 0, lines));
		when(swiftParser.parse(lines)).thenReturn(blocks);

		Statement statement1 = new Statement(1);
		Statement statement2 = new Statement(2);
		List<Statement> statements = Arrays.asList(statement1, statement2);
		when(swiftBlocksParser.parse(blocks)).thenReturn(statements);

		String filename = "test.txt";

		List<Statement> parsedStatements = uploadFile.uploadFile(inputStream, filename);

		assertStatementsWereParsed(statements, parsedStatements);

		assertEveryStatementWasPersisted(parsedStatements, filename);
		assertTransferTypesWereResolvedForEveryStatement(parsedStatements);
	}

	private void assertStatementsWereParsed(List<Statement> expectedStatements,
			List<Statement> statements) {
		assertArrayEquals(expectedStatements.toArray(), statements.toArray());
	}

	private void assertEveryStatementWasPersisted(List<Statement> statements, String filename) {
		for (Statement statement : statements) {
			assertEquals(filename, statement.sourceFilename);
			verify(statementRepository, times(1)).persist(statement);
		}
	}

	private void assertTransferTypesWereResolvedForEveryStatement(List<Statement> statements) {
		for (Statement statement : statements) {
			verify(transferTypeResolver, times(1)).resolve(statement);
		}
	}



}
