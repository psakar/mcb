package com.chare.mcb.entity;


import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.chare.core.Utils;
import com.chare.service.AuditUtils;

public class CardTest {

	private Card card;

	@Before
	public void setup() throws Exception {
		card = new Card();
	}

	@Test
	public void testDefaults() throws Exception {
		assertEquals(null, card.getMailAddress().name);
		assertEquals(Utils.getToday(), card.activeFrom);
		assertEquals(StatementPeriod.DAILY, card.statementPeriod);
		assertEquals(0, card.getFeeTypes().size());
		assertEquals(Card.DEFAULT_LANGUAGE, card.languageId);
		assertFalse(card.isInactive());
	}



	@Test
	public void testAddFeeType() throws Exception {
		FeeType feeType = new FeeType();
		card.addFeeType(feeType);

		assertEquals(1, card.getFeeTypes().size());
		assertEquals(feeType, card.getFeeTypes().get(0).getFeeType());
		assertEquals(feeType.calculation, card.getFeeTypes().get(0).calculation);
		assertNotSame(feeType.calculation, card.getFeeTypes().get(0).calculation);
	}



	@Test
	public void testCardWithActiveToAfterTodayIsInactive() throws Exception {
		card.activeTo = Utils.getYesterday();
		assertTrue(card.isInactive());
	}

	@Test
	public void testAuditInfo() throws Exception {
		card.id = 1;
		card.number = "number";
		card.cardType = new CardType("GOLD");
		card.settlementAccount = "settlementAccount";
		card.holderName = "name";
		card.email = "email@a.bc";
		card.phone = "phone";
		Address address = card.getMailAddress();
		address.title = "title";
		address.name = "name";
		address.name2 = "name2";
		address.street = "street";
		address.zip = "zip";
		address.town = "town";
		address.country = "country";
		assertEquals("card;1;number;GOLD;settlementAccount;name;email@a.bc;phone;title;name;name2;street;town;zip;country;DAILY;" + AuditUtils.dateToString(new Date())  + ";;3;;;;", card.getAuditInfo());
	}

}
