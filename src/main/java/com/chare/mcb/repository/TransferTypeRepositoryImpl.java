package com.chare.mcb.repository;

import javax.persistence.EntityManager;

import com.chare.mcb.entity.TransferType;
import com.chare.repository.JpaRepositoryImpl;

class TransferTypeRepositoryImpl extends JpaRepositoryImpl<String, TransferType> implements TransferTypeRepository {

	TransferTypeRepositoryImpl() {
	}

	protected TransferTypeRepositoryImpl(EntityManager entityManager) {
		setEntityManager(entityManager);
	}

}
