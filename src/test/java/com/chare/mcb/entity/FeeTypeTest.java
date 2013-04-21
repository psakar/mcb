package com.chare.mcb.entity;


import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

public class FeeTypeTest {

	private FeeType type;

	@Before
	public void setUp() {
		type = new FeeType();
	}

	@Test
	public void testDefaults() throws Exception {
		assertNotNull(type.description);
		assertNotNull(type.calculation);
	}

	@Test
	public void testAuditInfo() throws Exception {
		type.code = "FT";
		type.description.description1 = "t 1";
		type.description.description2 = "t 2";
		type.description.description3 = "t 3";
		type.cardType = new CardType("GOLD");
		type.transferType = new TransferType("TT");
		type.settlementAccount = "account";
		type.calculation.amount = new BigDecimal("100");
		type.calculation.percentage = new BigDecimal("1.23");

		assertEquals("feeType;FT;t 1;t 2;t 3;GOLD;TT;account;100;1.23;", type.getAuditInfo());
	}
}
