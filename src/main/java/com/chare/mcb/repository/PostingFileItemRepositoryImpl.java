package com.chare.mcb.repository;

import javax.persistence.EntityManager;

import com.chare.mcb.entity.PostingFileItem;
import com.chare.repository.JpaRepositoryImpl;

class PostingFileItemRepositoryImpl extends JpaRepositoryImpl<Integer, PostingFileItem> implements PostingFileItemRepository {

	PostingFileItemRepositoryImpl() {
	}

	protected PostingFileItemRepositoryImpl(EntityManager entityManager) {
		setEntityManager(entityManager);
	}

}
