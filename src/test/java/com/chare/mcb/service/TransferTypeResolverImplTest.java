package com.chare.mcb.service;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.chare.mcb.entity.Statement;
import com.chare.mcb.entity.StatementLine;
import com.chare.mcb.entity.TransferType;
import com.chare.mcb.repository.TransferTypeRepository;

public class TransferTypeResolverImplTest {




	private TransferTypeResolverImpl resolver;

	@Mock
	private TransferTypeRepository repository;

	@Mock
	private CardTransactionParser parser;


	@Before
	public void before() throws Exception {
		MockitoAnnotations.initMocks(this);
		resolver = new TransferTypeResolverImpl(repository, parser);
	}

	@Test
	public void testTransferTypeIsNotSearchedForLineWithExistingOne() throws Exception {
		StatementLine line = createStatementLineWithTransferType(new TransferType("code"));

		resolver.resolve(line.getStatement());

		verifyZeroInteractions(repository);
	}

	@Test
	public void testCardDetailsAreResolvedForLineWithTransferType() throws Exception {
		StatementLine line = createStatementLineWithTransferType(new TransferType("code"));

		resolver.resolve(line.getStatement());

		verify(parser, times(1)).parseCardTransaction(line);
	}


	@Test
	public void testTransferTypeIsFilledInWhenLineTransferTypeCodeIsFound() throws Exception {
		StatementLine line = createStatementLine();
		line.swiftType = "MSTS";
		line.reference2 = "//FEE-123";
		String transferTypeCode = line.getTransferTypeCode();
		TransferType transferType = new TransferType(transferTypeCode);
		when(repository.findById(transferTypeCode)).thenReturn(transferType);

		resolver.resolve(line.getStatement());

		assertEquals(transferType, line.transferType);
		verify(parser, times(1)).parseCardTransaction(line);
	}


	@Test
	public void testTransferTypeIsFilledInForEachStatementLine() throws Exception {
		StatementLine line = createStatementLine();
		line.swiftType = "MSTS";
		line.reference2 = "//FEE-123";

		StatementLine line2 = line.getStatement().addLine();
		line2.swiftType = "MSTS";
		line2.reference2 = "//FEE-123";

		String transferTypeCode = line.getTransferTypeCode();
		TransferType transferType = new TransferType(transferTypeCode);
		when(repository.findById(transferTypeCode)).thenReturn(transferType);

		Statement statement = line.getStatement();
		resolver.resolve(statement);

		assertEquals(transferType, line.transferType);
		assertEquals(transferType, line2.transferType);

		verify(repository, times(statement.getLines().size())).findById(transferTypeCode);
		verify(parser, times(statement.getLines().size())).parseCardTransaction(any(StatementLine.class));
	}

	@Test
	public void testTransferTypeIsNotFilledInWhenLineTransferTypeCodeIsNotFound() throws Exception {
		StatementLine line = createStatementLine();

		resolver.resolve(line.getStatement());

		assertEquals(null, line.transferType);
		verifyZeroInteractions(repository);
	}


	private StatementLine createStatementLineWithTransferType(TransferType transferType) {
		StatementLine line = createStatementLine();
		line.transferType = transferType;
		return line;
	}

	private StatementLine createStatementLine() {
		Statement statement = new Statement();
		StatementLine line = statement.addLine();
		return line;
	}
}
