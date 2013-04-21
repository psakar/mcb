package com.chare.mcb.repository;

import javax.persistence.EntityManager;

import com.chare.mcb.entity.StatementItem;
import com.chare.repository.JpaRepositoryImpl;

class StatementItemRepositoryImpl extends JpaRepositoryImpl<Integer, StatementItem> implements StatementItemRepository {

	StatementItemRepositoryImpl() {
	}

	protected StatementItemRepositoryImpl(EntityManager entityManager) {
		setEntityManager(entityManager);
	}

}
