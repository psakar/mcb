package com.chare.mcb.entity;


import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class CardStatementLineTest {

	private CardStatementLine line;

	@Before
	public void before() throws Exception {
		MockitoAnnotations.initMocks(this);
		line = new CardStatementLine();
		line.reference2 = "//A-feeReference";
	}



	@Test
	public void testDefaults() throws Exception {
		assertFalse(line.isRelatedFeeLine());
	}

	@Test
	public void testCalculateTransactionFee() throws Exception {
		assertEquals(new BigDecimal("1"), calculateTransactionFeeAmount(new BigDecimal("1"), new BigDecimal("0")));
		assertEquals(new BigDecimal("3"), calculateTransactionFeeAmount(new BigDecimal("1"), new BigDecimal("2")));
	}

	private BigDecimal calculateTransactionFeeAmount(BigDecimal amount,
			BigDecimal fee) {
		line.amount = amount;
		line.cardTransaction = new CardTransaction();
		line.cardTransaction.feeAmount = fee;
		return line.calculateTransactionFeeAmount();
	}


	@Test
	public void testSetRelatedLineWithFee() throws Exception {
		CardStatementLine lineWithFee = createRelatedLineWithFee();

		line.setRelatedLineWithFee(lineWithFee);

		assertEquals(lineWithFee, line.getRelatedLineWithFee());
	}

	private CardStatementLine createRelatedLineWithFee() {
		CardTransaction cardTransaction = new CardTransaction();
		cardTransaction.amount = new BigDecimal("10.12");
		cardTransaction.currency = Booking.CURRENCY;
		cardTransaction.feeAmount = new BigDecimal("5.50");
		CardStatementLine lineWithFee = new CardStatementLine();
		lineWithFee.cardTransaction = cardTransaction;
		lineWithFee.amount = cardTransaction.amount;
		lineWithFee.transferType = new TransferType();
		lineWithFee.transferType.cardTransactionType = CardTransactionType.TRANSACTION_FEE;
		lineWithFee.reference2 = line.reference2;
		return lineWithFee;
	}

	@Test
	public void testIsRelatedFeeLineOfLine() throws Exception {
		CardStatementLine lineWithFee = createRelatedLineWithFee();

		assertTrue(line.isRelatedFeeLineOfLine(lineWithFee));
	}


	@Test
	public void testIsRelatedFeeLine() throws Exception {
		line.transferType = new TransferType();
		line.transferType.cardTransactionType = CardTransactionType.TRANSACTION_FEE;

		assertTrue(line.isRelatedFeeLine());
	}

	@Test
	public void testIsFeeLine() throws Exception {
		line.transferType = new TransferType();
		line.transferType.cardTransactionType = CardTransactionType.FEE;

		assertTrue(line.isFeeLine());
	}


	@Test
	public void testGetFeeReference() throws Exception {
		assertFeeReference("1202777040", "//CLT-1202777040");
		assertFeeReference(null, "//CLT1202777040");
		assertFeeReference(null, null);
		assertFeeReference(null, "-");
		assertFeeReference(null, "-1202777040");
		assertFeeReference(null, "-1202777040");
		assertFeeReference(null, "//-1202777040");
		assertFeeReference("1202777040", "//C-1202777040");
	}



	private void assertFeeReference(String expected, String reference2) {
		line.reference2 = reference2;
		assertEquals(expected, line.getFeeReference());
	}



	@Test
	public void testFeeLineIsDisplayed() throws Exception {
		line.setRelatedLineWithFee(createRelatedLine(new BigDecimal("10"), new BigDecimal("1")));
		assertTrue(line.showFeeLine());
	}

	private CardStatementLine createRelatedLine(BigDecimal amount, BigDecimal feeAmount) {
		CardStatementLine relatedLine = new CardStatementLine();
		relatedLine.transferType = new TransferType();
		relatedLine.transferType.cardTransactionType = CardTransactionType.TRANSACTION_FEE;
		relatedLine.amount = amount;
		if (feeAmount != null) {
			relatedLine.cardTransaction = new CardTransaction();
			relatedLine.cardTransaction.feeAmount = feeAmount;
		}
		return relatedLine;
	}


	@Test
	public void testFeeLineWithZeroAmountIsNotDisplayed() throws Exception {
		line.setRelatedLineWithFee(createRelatedLine(new BigDecimal("10"), new BigDecimal("-10")));
		assertFalse(line.showFeeLine());
	}

	@Test
	public void testFeeWithZeroBankFee() throws Exception {
		line.transferType = new TransferType();
		line.transferType.cardTransactionType = CardTransactionType.FEE;
		line.cardTransaction = new CardTransaction();
		line.amount = new BigDecimal("123.45");

		assertEquals(line.amount, line.getFee());
		assertEquals(BigDecimal.ZERO, line.getBankFee());
	}

	@Test
	public void testZeroFeeWitBankFee() throws Exception {
		line.transferType = new TransferType();
		line.transferType.cardTransactionType = CardTransactionType.FEE;
		line.cardTransaction = new CardTransaction();
		line.cardTransaction.feeAmount = new BigDecimal("123.45");
		line.amount = BigDecimal.ZERO;

		assertEquals(line.cardTransaction.feeAmount, line.getFee());
		assertEquals(line.cardTransaction.feeAmount, line.getBankFee());
	}


	@Test
	public void testBankFee() throws Exception {
		line.transferType = new TransferType();
		line.transferType.cardTransactionType = CardTransactionType.FEE;
		line.cardTransaction = new CardTransaction();
		line.amount = new BigDecimal("123.45");
		line.cardTransaction.feeAmount = new BigDecimal("10.45");

		assertEquals(line.cardTransaction.feeAmount, line.getBankFee());
		assertEquals(line.amount.add(line.cardTransaction.feeAmount), line.getFee());
	}

	@Test
	public void testBankFeeIsZeroForCardTransaction() throws Exception {
		line.transferType = new TransferType();
		line.transferType.cardTransactionType = CardTransactionType.TRANSACTION;
		line.amount = new BigDecimal("123.45");

		assertEquals(BigDecimal.ZERO, line.getBankFee());
	}

	@Test
	public void testBankFeeAmountIsZeroForCardTransactionFee() throws Exception {
		line.transferType = new TransferType();
		line.transferType.cardTransactionType = CardTransactionType.TRANSACTION_FEE;
		line.amount = new BigDecimal("123.45");
		line.cardTransaction.feeAmount = new BigDecimal("10.00");

		assertEquals(line.amount.add(line.cardTransaction.feeAmount), line.getFee());
		assertEquals(line.cardTransaction.feeAmount, line.getBankFee());
	}




	@Test
	public void testGetAmountWithFeesWhenFeesAreZero() throws Exception {
		line.amount = new BigDecimal("123.45");

		assertEquals(line.amount, line.getAmountWithFees());
	}

	@Test
	public void testGetAmountWithFeesForCardFee() throws Exception {
		line.amount = new BigDecimal("123.45");
		line.transferType = new TransferType();
		line.transferType.cardTransactionType = CardTransactionType.FEE;
		line.cardTransaction = new CardTransaction();
		line.cardTransaction.feeAmount = new BigDecimal("10.00");

		assertEquals(line.amount.add(line.cardTransaction.feeAmount), line.getAmountWithFees());
		/*
		{
			BigDecimal total = amount.add(getBankFee());
			if (relatedLineWithFee != null)
				total = total.add(relatedLineWithFee.getFee());
			return total;
		}
		 */
	}

	@Test
	public void testGetAmountWithFeesForCardTransactionWithFee() throws Exception {
		line.amount = new BigDecimal("123.45");
		line.transferType = new TransferType();
		line.transferType.cardTransactionType = CardTransactionType.TRANSACTION;
		line.cardTransaction = new CardTransaction();

		line.setRelatedLineWithFee(createRelatedLine(new BigDecimal("10"), new BigDecimal("1")));

		assertEquals(line.amount.add(line.getRelatedLineWithFee().getFee()), line.getAmountWithFees());
	}

}
