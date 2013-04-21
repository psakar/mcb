package com.chare.mcb.repository;

import javax.persistence.EntityManager;

import com.chare.mcb.entity.UserItem;
import com.chare.repository.JpaRepositoryImpl;

class UserItemRepositoryImpl extends JpaRepositoryImpl<Integer, UserItem> implements UserItemRepository {

	UserItemRepositoryImpl() {
	}

	protected UserItemRepositoryImpl(EntityManager entityManager) {
		setEntityManager(entityManager);
	}

}
