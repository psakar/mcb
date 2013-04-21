package com.chare.mcb.repository;

import static org.junit.Assert.*;

import org.apache.commons.lang.StringUtils;

import com.chare.mcb.JpaRepositoryTestCase;
import com.chare.mcb.entity.EntityWithIdInteger;
import com.chare.mcb.entity.TransferType;

public class TransferTypeRepositoryImplTest extends JpaRepositoryTestCase<String, TransferType, TransferTypeRepository> {

	@Override
	protected TransferTypeRepository getRepository() {
		TransferTypeRepositoryImpl repository = new TransferTypeRepositoryImpl(entityManager);
		return repository;
	}

	@Override
	protected String getTableName() {
		return EntityWithIdInteger.TRANSFER_TYPE_TABLE;
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
	protected TransferType setupEntity(TransferType entity) {
		entity.code = StringUtils.right(createStringFromMilliseconds(), 10);
		entity.description.description1 = "description1";
		entity.description.description2 = "description2";
		entity.description.description3 = "description3";
		return entity;
	}

	@Override
	protected void assertPersistedEntity(TransferType loaded, TransferType entity) {
		assertEquals(entity.code, loaded.code);
		assertEquals(entity.description, loaded.description);
	}

}
