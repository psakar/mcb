package com.chare.mcb.repository;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.chare.core.Utils;
import com.chare.mcb.JpaRepositoryTestCase;
import com.chare.mcb.entity.Address;
import com.chare.mcb.entity.Booking;
import com.chare.mcb.entity.Card;
import com.chare.mcb.entity.CardStatementLine;
import com.chare.mcb.entity.CardType;
import com.chare.mcb.entity.EntityWithIdInteger;
import com.chare.mcb.entity.FeeType;
import com.chare.mcb.entity.Statement;
import com.chare.mcb.entity.StatementLine;

public class CardRepositoryImplTest extends JpaRepositoryTestCase<Integer, Card, CardRepositoryImpl> {

	@Override
	protected String getTableName() {
		return EntityWithIdInteger.CARD_TABLE;
	}

	@Override
	protected Card setupEntity(Card entity) {
		entity.number = StringUtils.right("1234567890123456" + createStringFromMilliseconds(), 16);
		entity.email = "email@a.bc";
		entity.holderName = "name";
		entity.phone = "phone";
		entity.cardType = lookupEntity("SELECT * FROM " + EntityWithIdInteger.CARD_TYPE_TABLE + " WHERE code = 'BUSINESS'", CardType.class);
		entity.settlementAccount = "123456789012CZK";
		Address address = entity.getMailAddress();
		address.name = "name";
		address.street = "street";
		address.zip = "zip";
		address.town = "town";
		address.country = "country";
		return entity;
	}

	@Override
	protected void assertPersistedEntity(Card loaded, Card entity) {
		assertEquals(entity.id, loaded.id);
		assertArrayEquals(entity.getFeeTypes().toArray(), loaded.getFeeTypes().toArray());
	}

	@Override
	protected CardRepositoryImpl getRepository() {
		return new CardRepositoryImpl(entityManager);
	}

	@Test
	public void testFindByNumber() throws Exception {
		Integer id = findId(getTableName());
		String number = lookup("SELECT number FROM " + getTableName() + " WHERE id = " + id);
		assertEquals(id, repository.findByNumber(number).getId());
	}

	@Test
	public void testCardFeeTypesArePersited() throws Exception {
		Card entity = create();
		setupEntity(entity);

		FeeType feeType = lookupEntity("SELECT * FROM " + EntityWithIdInteger.FEE_TYPE_TABLE + " WHERE code = 'B_ICFEE'", FeeType.class);
		entity.addFeeType(feeType);

		repository.persist(entity);
		assertNotNull(entity.getId());
		entityManager.flush();
		entityManager.clear();
		Card loaded = repository.findById(entity.getId());
		assertNotNull(loaded.getId());
		repository.delete(entity);
		assertPersistedEntity(loaded, entity);
	}



	@Test
	public void testCardWithTransactionBookedInStatementPeriodAndNextStatementDateBeforeTomorrowIsFound() throws Exception {
		Card card = createCard();
		card.lastStatementDate = Utils.getDate(2012, 1, 1);
		card.nextStatementDate = Utils.getToday();
		repository.persist(card);

		Statement statement = createStatementWithLineWithCardTransactionForCardBookedAtDate(card.number, Utils.dateAddDays(card.lastStatementDate, 1));
		entityManager.persist(statement);

		List<Card> list = repository.findCardsToGenerateStatement();

		assertTrue(list.contains(card));
	}

	@Test
	public void testCardWithTransactionNotBookedInStatementPeriodAndNextStatementDateBeforeTomorrowIsNotFound() throws Exception {
		Card card = createCard();
		card.lastStatementDate = Utils.getDate(2012, 1, 1);
		card.nextStatementDate = Utils.getToday();
		repository.persist(card);

		Statement statement = createStatementWithLineWithCardTransactionForCardBookedAtDate(card.number, Utils.dateAddDays(card.lastStatementDate, -10));
		entityManager.persist(statement);

		List<Card> list = repository.findCardsToGenerateStatement();

		assertFalse(list.contains(card));
	}

	@Test
	public void testCardWithTransactionNotBookedAndNextStatementDateBeforeTomorrowIsNotFound() throws Exception {
		Card card = createCard();
		card.nextStatementDate = Utils.getToday();
		repository.persist(card);

		Statement statement = createStatementWithLineWithCardTransactionForCardBookedAtDate(card.number, null);
		entityManager.persist(statement);

		List<Card> list = repository.findCardsToGenerateStatement();

		assertFalse(list.contains(card));
	}

	private Statement createStatementWithLineWithCardTransactionForCardBookedAtDate(
			String cardNumber, Date bookDate) {
		Statement statement = new Statement();
		statement.number = StringUtils.right("1234567890123456" + createStringFromMilliseconds(), 4);
		statement.statementDate = Utils.getToday();
		statement.sourceFilename = createStringFromMilliseconds();
		statement.account = "account";
		StatementLine line = statement.addLine();
		line.amount = BigDecimal.ONE;
		line.valueDate = Utils.getToday();
		line.bookDate = bookDate;
		line.setCardTransaction(BigDecimal.ZERO, Booking.CURRENCY, cardNumber, Utils.getYesterday(), "details1", "details2");
		return statement;
	}

	@Test
	public void testCardWithNextStatementDateBeforeTomorrowWithoutTransactionIsNotFound() throws Exception {
		Card card = createCard();
		card.nextStatementDate = Utils.getToday();
		repository.persist(card);

		List<Card> list = repository.findCardsToGenerateStatement();

		assertFalse(list.contains(card));
	}

	@Test
	public void testCardWithoutNextStatementDateIsNotFound() throws Exception {
		Card card = createCard();
		repository.persist(card);

		List<Card> list = repository.findCardsToGenerateStatement();

		assertFalse(list.contains(card));
	}

	@Test
	public void testCardWithNextStatementDateAfterTodayIsNotFound() throws Exception {
		Card card = createCard();
		card.nextStatementDate = Utils.getTomorrow();
		repository.persist(card);

		List<Card> list = repository.findCardsToGenerateStatement();

		assertFalse(list.contains(card));
	}

	private Card createCard() {
		Card card = new Card();
		setupEntity(card);
		return card;
	}



	@Test
	public void testLoadCardStatement() throws Exception {
		Card card = createCard();
		card.lastStatementDate = Utils.getDate(2012, 1, 1);
		card.nextStatementDate = Utils.getToday();
		repository.persist(card);

		Statement statement = createStatementWithLineWithCardTransactionForCardBookedAtDate(card.number, Utils.dateAddDays(card.lastStatementDate, 1));
		entityManager.persist(statement);

		Date statementPeriodStart = card.lastStatementDate;
		Date statementPeriodEnd = card.getStatementEndDate();

		List<CardStatementLine> cardStatementLines = repository.loadCardStatementLines(card.number, statementPeriodStart, statementPeriodEnd);

		assertEquals(1, cardStatementLines.size());
	}
}
