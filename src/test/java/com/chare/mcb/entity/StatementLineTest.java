package com.chare.mcb.entity;


import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class StatementLineTest {

	@Before
	public void setUp() throws Exception {

	}



	@Test
	public void testGetTransferTypeCode() throws Exception {
		assertGetTransferTypeCode(null, null, null);
		assertGetTransferTypeCode(null, "MSTS", null);
		assertGetTransferTypeCode(null, null, "//CODE-12321");
		assertGetTransferTypeCode(null, "MSTS", "CODE-12321");
		assertGetTransferTypeCode(null, "MSTS", "//CODE12321");
		assertGetTransferTypeCode(null, "MSTS", "CODE-12321");
		assertGetTransferTypeCode("MSTSCODE", "MSTS", "//CODE-12321");
	}



	private void assertGetTransferTypeCode(String expectedTransferTypeCode,
			String swiftType, String reference2) {
		StatementLine line = createStatementLine();

		line.swiftType = swiftType;
		line.reference2 = reference2;

		assertEquals(expectedTransferTypeCode, line.getTransferTypeCode());
	}



	public static StatementLine createStatementLine() {
		Statement statement = new Statement();
		StatementLine line = statement.addLine();
		return line;
	}



	@Test
	public void testCardTransactionDetailsShouldNotBeParsedWhenTransferTypeNotResolved() throws Exception {
		StatementLine line = createStatementLine();
		assertFalse(line.parseCardTransactionDetails());
	}

	@Test
	public void testCardTransactionDetailsShouldBeParsedBasedOnTransferCardTransactionType() throws Exception {
		assertCardTransactionDetailsShouldBeParsed(false, null);
		assertCardTransactionDetailsShouldBeParsed(true, CardTransactionType.TRANSACTION);
		assertCardTransactionDetailsShouldBeParsed(true, CardTransactionType.TRANSACTION_FEE);
		assertCardTransactionDetailsShouldBeParsed(true, CardTransactionType.FEE);
	}
	private void assertCardTransactionDetailsShouldBeParsed(boolean expected, CardTransactionType cardTransactionType) {
		StatementLine line = createStatementLineWithTransferTypeWithCardTransactionType(cardTransactionType);
		assertEquals(expected, line.parseCardTransactionDetails());
	}



	public static StatementLine createStatementLineWithTransferTypeWithCardTransactionType(
			CardTransactionType cardTransactionType) {
		StatementLine line = createStatementLine();
		line.transferType = createTransferType(cardTransactionType);
		return line;
	}



	public static TransferType createTransferType(CardTransactionType cardTransactionType) {
		TransferType transferType = new TransferType("test");
		transferType.cardTransactionType = cardTransactionType;
		return transferType;
	}

	@Test
	public void testStatementWithLineWithTransferTypeWithoutCardTransactionParsedRequiresResolution() throws Exception {
		StatementLine line = createStatementLine();
		line.transferType = new TransferType("code");
		line.transferType.settlementType = SettlementType.CLIENT_SETTLEMENT_WITH_FEE;
		line.transferType.cardTransactionType = CardTransactionType.TRANSACTION_FEE;

		assertTrue(line.requiresResolution());
	}

	@Test
	public void testStatementWithLineWithTransferTypeWithCardTransactionWithoutFeeRequiresResolution() throws Exception {
		StatementLine line = createStatementLine();
		line.transferType = new TransferType("code");
		line.transferType.settlementType = SettlementType.CLIENT_SETTLEMENT_WITH_FEE;
		line.transferType.cardTransactionType = CardTransactionType.TRANSACTION_FEE;
		line.setCardTransaction(BigDecimal.ONE, "EUR", "cardNumber", new Date(), null, null);

		assertTrue(line.requiresResolution());
	}




	@Test
	public void testGenerateBookingDetailsForBankSettlement() throws Exception {
		StatementLine line = createStatementLine();
		line.transferType = new TransferType("code");
		line.details1 = "Details1";
		line.details2 = "Details2";
		line.details3 = "Details3";
		line.details4 = "Details4";

		assertEquals("code Details1 Details2", line.generateBookingDetailsForBankSettlement());
	}
}
