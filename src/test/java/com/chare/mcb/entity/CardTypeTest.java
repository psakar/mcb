package com.chare.mcb.entity;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CardTypeTest {

	private CardType type;

	@Before
	public void setUp() {
		type = new CardType();
	}

	@Test
	public void testDefaults() throws Exception {
		assertNotNull(type.description);
	}

	@Test
	public void testAuditInfo() throws Exception {
		type.code = "GOLD";
		type.description.description1 = "Gold 1";
		type.description.description2 = "Gold 2";
		type.description.description3 = "Gold 3";
		assertEquals("cardType;GOLD;Gold 1;Gold 2;Gold 3;", type.getAuditInfo());
	}
}
