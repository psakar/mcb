package com.chare.mcb.repository;

import static org.junit.Assert.*;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.chare.mcb.JpaRepositoryTestCase;
import com.chare.mcb.entity.Card;
import com.chare.mcb.entity.CardType;
import com.chare.mcb.entity.EntityWithIdInteger;
import com.chare.mcb.entity.FeeType;
import com.chare.mcb.entity.TransferType;

public class FeeTypeRepositoryImplTest extends JpaRepositoryTestCase<String, FeeType, FeeTypeRepository> {

	@Override
	protected FeeTypeRepository getRepository() {
		FeeTypeRepositoryImpl repository = new FeeTypeRepositoryImpl(entityManager);
		return repository;
	}

	@Override
	protected String getTableName() {
		return EntityWithIdInteger.FEE_TYPE_TABLE;
	}

	@Override
	protected String getPrimaryKeyColumnName() {
		return "code";
	}

	@Override
	protected String getPrimaryKeyColumnType() {
		return "varchar";
	}

	@Override
	protected FeeType setupEntity(FeeType entity) {
		entity.code = StringUtils.right(createStringFromMilliseconds(), 10);
		entity.description.description1 = "description1";
		entity.description.description2 = "description2";
		entity.description.description3 = "description3";

		entity.cardType = lookupEntity("SELECT * FROM " + EntityWithIdInteger.CARD_TYPE_TABLE + " WHERE code = 'BUSINESS'", CardType.class);

		entity.transferType = lookupEntity("SELECT * FROM " + EntityWithIdInteger.TRANSFER_TYPE_TABLE + " WHERE code = 'ICFEE'", TransferType.class);
		return entity;
	}

	@Override
	protected void assertPersistedEntity(FeeType loaded, FeeType entity) {
		assertEquals(entity.code, loaded.code);
		assertEquals(entity.description, loaded.description);
	}



	@Test
	public void testFind() throws Exception {
		assertEquals(null, repository.find(null, null));
		Card card = lookupEntity("SELECT * FROM " + EntityWithIdInteger.CARD_TABLE + " WHERE cardType = 'BUSINESS'", Card.class);
		FeeType feeType = lookupEntity("SELECT * FROM " + EntityWithIdInteger.FEE_TYPE_TABLE + " WHERE cardType = 'BUSINESS'", FeeType.class);
		assertEquals(null, repository.find(card, null));
		assertEquals(null, repository.find(null, feeType.transferType));
		assertEquals(feeType, repository.find(card, feeType.transferType));
	}
}
