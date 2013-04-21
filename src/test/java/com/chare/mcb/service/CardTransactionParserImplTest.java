package com.chare.mcb.service;


import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.chare.core.Utils;
import com.chare.mcb.entity.Booking;
import com.chare.mcb.entity.Card;
import com.chare.mcb.entity.CardTransaction;
import com.chare.mcb.entity.CardTransactionType;
import com.chare.mcb.entity.CardType;
import com.chare.mcb.entity.FeeType;
import com.chare.mcb.entity.StatementLine;
import com.chare.mcb.entity.StatementLineTest;
import com.chare.mcb.repository.CardRepository;
import com.chare.mcb.repository.FeeTypeRepository;

public class CardTransactionParserImplTest {

	private CardTransactionParser parser;

	@Mock
	private CardRepository cardRepository;
	@Mock
	private FeeTypeRepository feeTypeRepository;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		parser = new CardTransactionParserImpl(cardRepository, feeTypeRepository);
	}



	@Test
	public void testParsingLineWithoutCardTransaction() throws Exception {
		StatementLine line = StatementLineTest.createStatementLineWithTransferTypeWithCardTransactionType(null);
		parser.parseCardTransaction(line);
		assertNull(line.getCardTransaction());
	}

	@Test
	public void testParsingLineWithCardTransaction() throws Exception {
		StatementLine line = StatementLineTest.createStatementLineWithTransferTypeWithCardTransactionType(CardTransactionType.TRANSACTION);
		line.details1 = "999Card 5412-6650-0000-3559";
		line.details2 = "2012.02.24 TS CS MICHALOVCE";
		line.details3 = "SVK - MICHALOVCE (TVTMI001)";
		line.details4 = "85,21 EUR";
		parser.parseCardTransaction(line);
		CardTransaction transaction = line.getCardTransaction();
		assertEquals("5412665000003559", transaction.cardNumber);
		assertEquals(Utils.getDate(2012, 2, 24), transaction.date);
		assertEquals("TS CS MICHALOVCE", transaction.details1);
		assertEquals("SVK - MICHALOVCE (TVTMI001)", transaction.details2);
		assertEquals(new BigDecimal("85.21"), transaction.amount);
		assertEquals("EUR", transaction.currency);
	}

	@Test
	public void testParsingLineWithCardTransactionFee() throws Exception {
		StatementLine line = StatementLineTest.createStatementLineWithTransferTypeWithCardTransactionType(CardTransactionType.TRANSACTION_FEE);
		line.amount = new BigDecimal("0.1");
		line.details1 = "999Card 5412-6650-0000-3559";
		line.details2 = "2012.02.24 TS CS MICHALOVCE";
		line.details3 = "SVK - MICHALOVCE (TVTMI001)";
		line.details4 = "Poplatok za platbu kartou";

		String cardNumber = "5412665000003559";

		Card card = new Card(1, cardNumber, new CardType("GOLD"));

		when(cardRepository.findByNumber(cardNumber)).thenReturn(card);
		FeeType feeType = new FeeType("FEETYPE");
		feeType.transferType = line.transferType;
		feeType.calculation.amount = BigDecimal.ONE;
		when(feeTypeRepository.find(card, line.transferType)).thenReturn(feeType);

		parser.parseCardTransaction(line);
		CardTransaction transaction = line.getCardTransaction();
		assertEquals(cardNumber, transaction.cardNumber);
		assertEquals(Utils.getDate(2012, 2, 24), transaction.date);
		assertEquals("TS CS MICHALOVCE", transaction.details1);
		assertEquals("SVK - MICHALOVCE (TVTMI001)", transaction.details2);
		assertEquals(line.amount, transaction.amount);
		assertEquals(Booking.CURRENCY, transaction.currency);
		assertEquals(feeType, transaction.feeType);
		assertEquals(new BigDecimal("-1.00"), transaction.feeAmount);
		assertEquals(Booking.CURRENCY, transaction.feeCurrency);
	}

	@Test
	public void testParsingLineWithCardFee() throws Exception {
		StatementLine line = StatementLineTest.createStatementLineWithTransferTypeWithCardTransactionType(CardTransactionType.FEE);
		line.amount = new BigDecimal("0.1");
		line.details1 = "999Card 5412-6650-0000-3559";
		line.details2 = "Poplatok za spravu karty";

		String cardNumber = "5412665000003559";

		Card card = new Card(1, cardNumber, new CardType("GOLD"));

		when(cardRepository.findByNumber(cardNumber)).thenReturn(card);
		FeeType feeType = new FeeType("FEETYPE");
		feeType.transferType = line.transferType;
		feeType.calculation.amount = BigDecimal.ONE;
		when(feeTypeRepository.find(card, line.transferType)).thenReturn(feeType);

		parser.parseCardTransaction(line);

		CardTransaction transaction = line.getCardTransaction();
		assertEquals(cardNumber, transaction.cardNumber);
		assertEquals(null, transaction.date);
		assertEquals(line.details2, transaction.details1);
		assertEquals(null, transaction.details2);
		assertEquals(line.amount, transaction.amount);
		assertEquals(line.getCurrency(), transaction.currency);
		assertEquals(feeType, transaction.feeType);
		assertEquals(feeType.calculation.calculate(line.amount), transaction.feeAmount);
		assertEquals(line.getCurrency(), transaction.feeCurrency);
	}

	@Test
	public void testParsingLineWithTransactionFeeWhenFeeTypeNotFound() throws Exception {
		StatementLine line = StatementLineTest.createStatementLineWithTransferTypeWithCardTransactionType(CardTransactionType.TRANSACTION_FEE);
		line.amount = new BigDecimal("0.1");
		line.details1 = "999Card 5412-6650-0000-3559";
		line.details2 = "2012.02.24 TS CS MICHALOVCE";
		line.details3 = "SVK - MICHALOVCE (TVTMI001)";
		line.details4 = "Poplatok za platbu kartou";

		String cardNumber = "5412665000003559";

		parser.parseCardTransaction(line);
		CardTransaction transaction = line.getCardTransaction();
		assertEquals(cardNumber, transaction.cardNumber);
		assertEquals(Utils.getDate(2012, 2, 24), transaction.date);
		assertEquals("TS CS MICHALOVCE", transaction.details1);
		assertEquals("SVK - MICHALOVCE (TVTMI001)", transaction.details2);
		assertEquals(line.amount, transaction.amount);
		assertEquals(Booking.CURRENCY, transaction.currency);
		assertEquals(null, transaction.feeType);
		assertEquals(null, transaction.feeAmount);
		assertEquals(null, transaction.feeCurrency);
	}

}
