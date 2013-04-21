package com.chare.mcb.repository;

import javax.persistence.EntityManager;

import com.chare.mcb.entity.CardItem;
import com.chare.repository.JpaRepositoryImpl;

class CardItemRepositoryImpl extends JpaRepositoryImpl<Integer, CardItem> implements CardItemRepository {

	CardItemRepositoryImpl() {
	}

	protected CardItemRepositoryImpl(EntityManager entityManager) {
		setEntityManager(entityManager);
	}

}
