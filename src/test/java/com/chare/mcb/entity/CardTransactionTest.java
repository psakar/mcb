package com.chare.mcb.entity;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CardTransactionTest {

	private CardTransaction transaction;

	@Before
	public void setUp() throws Exception {
		transaction = new CardTransaction();
		//		transaction.cardNumber = "123456789012345678abcdef";
	}



	@Test
	public void testGetDetails() throws Exception {
		assertEquals("", getDetails(null, null));
		assertEquals("", getDetails(" ", null));
		assertEquals("", getDetails(null, " "));
		assertEquals("a", getDetails("a", null));
		assertEquals("b", getDetails(null, "b"));
		assertEquals("a b", getDetails("a", "b"));
	}

	private String getDetails(String details1, String details2) {
		transaction.details1 = details1;
		transaction.details2 = details2;
		return transaction.getDetails();
	}
}
