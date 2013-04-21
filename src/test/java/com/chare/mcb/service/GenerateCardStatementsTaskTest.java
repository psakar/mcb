package com.chare.mcb.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.chare.core.Utils;
import com.chare.mcb.entity.Card;
import com.chare.mcb.repository.CardRepository;

public class GenerateCardStatementsTaskTest {

	@Mock
	private CardRepository cardRepository;

	@Mock
	private CardStatementGenerator statementGenerator;

	private GenerateCardStatementsTask task;

	@Before
	public void before() throws Exception {
		MockitoAnnotations.initMocks(this);
		task = new GenerateCardStatementsTask(cardRepository, statementGenerator);
	}


	@Test
	public void testTaskIsRunabble() throws Exception {
		assertTrue(task instanceof Runnable);
	}

	@Test
	public void testStatementsAreGeneratedForWhenCardWithTransactionExistsAndStatementPeriodIsDue() throws Exception {
		List<Card> cards = Arrays.asList(createCardForStatement(1));
		when(cardRepository.findCardsToGenerateStatement()).thenReturn(cards);

		task.run();

		assertStatementWasGeneratedForEveryCard(cards);
	}

	@Test
	public void testErrorDuringGeneratingOfOneStatmentDoNotStopGenerating() throws Exception {
		Card card1 = createCardForStatement(1);
		Card card2 = createCardForStatement(2);
		List<Card> cards = Arrays.asList(card1, card2);
		when(cardRepository.findCardsToGenerateStatement()).thenReturn(cards);

		doThrow(new IllegalArgumentException()).when(statementGenerator).generate(card1);

		task.run();

		assertStatementWasGeneratedForEveryCard(cards);
	}


	protected void assertStatementWasGeneratedForEveryCard(List<Card> cards) {
		for (Card card : cards) {
			verify(statementGenerator, times(1)).generate(card);
		}
	}

	private Card createCardForStatement(Integer id) {
		Card card = new Card();
		card.setId(id);
		card.number = "1234567890123456";
		card.lastStatementDate = null;
		card.nextStatementDate = Utils.getToday();
		return card;
	}



	@Test
	public void testNoStatementsIsGeneratedWhenNoCardHasTransactionFromLastStatementDate() throws Exception {

	}

}
