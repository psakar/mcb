package com.chare.mcb.repository;

import com.chare.mcb.ListAndCountRepositoryTestCase;
import com.chare.mcb.entity.EntityWithIdInteger;
import com.chare.mcb.entity.UserItem;

public class UserItemRepositoryImplTest extends ListAndCountRepositoryTestCase<UserItem, UserItemRepository> {

	@Override
	protected String getTableName() {
		return EntityWithIdInteger.USERS_TABLE;
	}


	@Override
	protected UserItemRepository getRepository() {
		return new UserItemRepositoryImpl(entityManager);
	}

}
