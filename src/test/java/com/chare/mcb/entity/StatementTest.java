package com.chare.mcb.entity;


import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.chare.core.Utils;

public class StatementTest {

	private Statement statement;

	@Before
	public void setUp() {
		statement = new Statement();
	}

	@Test
	public void testDefaults() throws Exception {
		assertEquals(BigDecimal.ZERO, statement.openingBalance);
		assertEquals(BigDecimal.ZERO, statement.closingBalance);
		assertEquals(0, statement.getLines().size());
		assertFalse(statement.isReadyToGenerateBookings());
	}



	@Test
	public void testAddLineSetIndex() throws Exception {
		statement.addLine();
		statement.addLine();
		int index = 0;
		for (StatementLine line : statement.getLines()) {
			assertEquals(index, line.number);
			index ++;
		}
	}

	@Test
	public void testStatementWithLineWithoutTransferTypeIsNotReadyToGenerateBookings() throws Exception {
		statement.addLine();
		assertFalse(statement.isReadyToGenerateBookings());
	}


	@Test
	public void testStatementWithAllLinesWithTransferTypeIsReadyToGenerateBookings() throws Exception {
		StatementLine line = statement.addLine();
		line.transferType = new TransferType("code");
		assertTrue(statement.isReadyToGenerateBookings());
	}

	@Test
	public void testStatementWithReferenceToPostingFileIsNotReadyToGenerateBookings() throws Exception {
		StatementLine line = statement.addLine();
		line.transferType = new TransferType("code");
		statement.postingFileId = 1;
		assertFalse(statement.isReadyToGenerateBookings());
	}
	@Test
	public void testStatementRequiresResolutionIfHasLineWhichRequiresResolution() throws Exception {
		StatementLine line = mock(StatementLine.class);
		when(line.requiresResolution()).thenReturn(true);

		statement.getLines().add(line);

		assertTrue(statement.requiresResolution());
		assertFalse(statement.isReadyToGenerateBookings());
	}

	@Test
	public void testAuditInfo() throws Exception {
		statement.number = "123";
		statement.statementDate = Utils.getDate(2012, 1, 1);
		statement.beforePersist();
		assertEquals("statement;123;2012;", statement.getAuditInfo());
	}
}
