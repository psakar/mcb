package com.chare.mcb.entity;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TransferTypeTest {

	private TransferType type;

	@Before
	public void setUp() {
		type = new TransferType();
	}

	@Test
	public void testDefaults() throws Exception {
		assertEquals(SettlementType.CLIENT_SETTLEMENT, type.settlementType);
		assertNotNull(type.description);
	}

	@Test
	public void testAuditInfo() throws Exception {
		type.code = "TT";
		type.description.description1 = "t 1";
		type.description.description2 = "t 2";
		type.description.description3 = "t 3";
		type.settlementAccount = "account";
		type.settlementType = SettlementType.CLIENT_SETTLEMENT;
		type.cardTransactionType = CardTransactionType.FEE;
		assertEquals("transferType;TT;t 1;t 2;t 3;CLIENT_SETTLEMENT;account;FEE;", type.getAuditInfo());
	}
}
