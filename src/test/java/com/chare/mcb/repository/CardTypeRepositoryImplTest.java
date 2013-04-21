package com.chare.mcb.repository;

import static org.junit.Assert.*;

import org.apache.commons.lang.StringUtils;

import com.chare.mcb.JpaRepositoryTestCase;
import com.chare.mcb.entity.CardType;
import com.chare.mcb.entity.EntityWithIdInteger;

public class CardTypeRepositoryImplTest extends JpaRepositoryTestCase<String, CardType, CardTypeRepository> {

	@Override
	protected CardTypeRepository getRepository() {
		return new CardTypeRepositoryImpl(entityManager);
	}

	@Override
	protected String getTableName() {
		return EntityWithIdInteger.CARD_TYPE_TABLE;
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
	protected CardType setupEntity(CardType entity) {
		entity.code = StringUtils.right(createStringFromMilliseconds(), 10);
		entity.description.description1 = "description1";
		entity.description.description2 = "description2";
		entity.description.description3 = "description3";
		return entity;
	}

	@Override
	protected void assertPersistedEntity(CardType loaded, CardType entity) {
		assertEquals(entity.code, loaded.code);
		assertEquals(entity.description, loaded.description);
	}

}
