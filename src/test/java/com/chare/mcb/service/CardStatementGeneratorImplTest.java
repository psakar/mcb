package com.chare.mcb.service;


import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.chare.core.Utils;
import com.chare.mcb.JpaRepositoryTestCase;
import com.chare.mcb.entity.Card;
import com.chare.mcb.entity.CardStatement;
import com.chare.mcb.entity.CardStatementLine;
import com.chare.mcb.entity.CardType;
import com.chare.mcb.repository.CardRepository;
import com.chare.mcb.service.CardStatementGeneratorImpl.CardStatementLinesMerger;
import com.chare.mcb.service.CardStatementGeneratorImpl.StatementPeriodCalculator;

public class CardStatementGeneratorImplTest {


	private CardStatementGeneratorImpl generator;

	@Mock
	private CardRepository cardRepository;
	@Mock
	private CardStatementExporter statementExporter;
	@Mock
	private CardStatementLinesMerger statementLinesMerger;

	@Captor
	private ArgumentCaptor<CardStatement> cardStatementCaptor;

	@Mock
	private StatementPeriodCalculator statementPeriodCalculator;



	@Before
	public void before() throws Exception {
		MockitoAnnotations.initMocks(this);
		generator = new CardStatementGeneratorImpl(cardRepository, statementExporter, statementLinesMerger, statementPeriodCalculator);
	}



	@Test
	public void testGeneratedStatement() throws Exception {

		Card card = new Card();
		card.number = "1234567890123456";
		card.nextStatementDate = Utils.getToday();
		card.lastStatementDate = Utils.getDate(2011, 12, 31);

		Integer statementNr = 1;
		Date statementPeriodStart = Utils.getToday();
		Date statementPeriodEnd = card.getStatementEndDate();

		List<CardStatementLine> cardStatementLines = new ArrayList<CardStatementLine>();
		when(cardRepository.loadCardStatementLines(card.number, statementPeriodStart, statementPeriodEnd)).thenReturn(cardStatementLines);
		when(statementLinesMerger.convertToLinesWithFees(cardStatementLines)).thenReturn(cardStatementLines);

		Date nextStatementDate = Utils.getTomorrow();
		when(statementPeriodCalculator.calculateNextStatementNr(card.lastStatementNr)).thenReturn(statementNr);
		when(statementPeriodCalculator.calculateStatementStartDate(card.lastStatementDate, card.activeFrom)).thenReturn(statementPeriodStart);
		when(statementPeriodCalculator.calculateNextStatementDate(statementPeriodEnd, card.statementPeriod)).thenReturn(nextStatementDate);

		generator.generate(card);

		assertCreatedCardStatement(card, cardStatementLines, statementPeriodStart, statementPeriodEnd, statementNr);
		assertStatementWasExported();
		assertCardIsPreparedForNextGenerating(card, nextStatementDate, statementNr, statementPeriodEnd);

	}



	protected void assertStatementWasExported() {
		verify(statementExporter, times(1)).export(cardStatementCaptor.getValue());
	}



	protected void assertCreatedCardStatement(Card card,
			List<CardStatementLine> cardStatementLines, Date statementPeriodStart,
			Date statementPeriodEnd, Integer statementNumber) {
		verify(statementExporter, times(1)).export(cardStatementCaptor.capture());
		CardStatement cardStatement = cardStatementCaptor.getValue();

		assertEquals(card, cardStatement.card);
		assertEquals(cardStatementLines, cardStatement.lines);
		assertEquals(statementPeriodStart, cardStatement.periodStart);
		assertEquals(statementPeriodEnd, cardStatement.periodEnd);
		assertEquals(statementNumber, cardStatement.number);
	}

	private void assertCardIsPreparedForNextGenerating(Card card, Date nextStatementDate, Integer lastStatementNr, Date lastStatementDate) {
		assertEquals(nextStatementDate, card.nextStatementDate);
		assertEquals(lastStatementDate, card.lastStatementDate);
		assertEquals(lastStatementNr, card.lastStatementNr);
		verify(cardRepository, times(1)).persist(card);
	}


	@Test
	public void testStatementGeneratedOnDemand() throws Exception {

		String cardNumber = "1234567890123456";
		Date dateFrom = Utils.getToday();
		Date dateTo = Utils.getDate(2011, 12, 31);
		Integer statementNumber = 1;

		List<CardStatementLine> cardStatementLines = new ArrayList<CardStatementLine>();
		cardStatementLines.add(new CardStatementLine());

		Card card = new Card(null, cardNumber, new CardType("CT"));
		when(cardRepository.findByNumber(cardNumber)).thenReturn(card);


		when(cardRepository.loadCardStatementLines(card.number, dateFrom, dateTo)).thenReturn(cardStatementLines);
		when(statementLinesMerger.convertToLinesWithFees(cardStatementLines)).thenReturn(cardStatementLines);

		generator.generateOnDemand(cardNumber, dateFrom, dateTo, statementNumber);

		assertCreatedCardStatement(card, cardStatementLines, dateFrom, dateTo, statementNumber);
		assertStatementWasExported();
		assertCardIsNotChanged(card);

	}

	@Test(expected = IllegalArgumentExceptionWithParameters.class)
	public void testStatementNotGeneratedOnDemandWhenCardNumberIsWrong() throws Exception {
		generator.generateOnDemand(null, null, null, null);
	}


	private void assertCardIsNotChanged(Card card) {
		verify(cardRepository, never()).persist(card);
	}



	@Test
	public void testTransactional() throws Exception {
		JpaRepositoryTestCase.assertPublicMethodsAreAnnotatedTransactional(generator.getClass());
	}
}
