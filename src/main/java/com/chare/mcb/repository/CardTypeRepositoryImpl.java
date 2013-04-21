package com.chare.mcb.repository;

import javax.persistence.EntityManager;

import com.chare.mcb.entity.CardType;
import com.chare.repository.JpaRepositoryImpl;

class CardTypeRepositoryImpl extends JpaRepositoryImpl<String, CardType> implements CardTypeRepository {

	CardTypeRepositoryImpl() {
	}

	protected CardTypeRepositoryImpl(EntityManager entityManager) {
		setEntityManager(entityManager);
	}

}
