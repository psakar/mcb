package com.chare.mcb.repository;

import com.chare.mcb.ListAndCountRepositoryTestCase;
import com.chare.mcb.entity.EntityWithIdInteger;
import com.chare.mcb.entity.StatementItem;

public class StatementItemRepositoryImplTest extends ListAndCountRepositoryTestCase<StatementItem, StatementItemRepository> {

	@Override
	protected String getTableName() {
		return EntityWithIdInteger.STATEMENT_TABLE;
	}


	@Override
	protected StatementItemRepository getRepository() {
		return new StatementItemRepositoryImpl(entityManager);
	}
}
