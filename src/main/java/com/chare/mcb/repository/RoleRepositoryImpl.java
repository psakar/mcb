package com.chare.mcb.repository;

import javax.persistence.EntityManager;

import com.chare.mcb.entity.Role;
import com.chare.repository.JpaRepositoryImpl;

class RoleRepositoryImpl extends JpaRepositoryImpl<String, Role> implements RoleRepository {

	RoleRepositoryImpl() {
	}

	protected RoleRepositoryImpl(EntityManager entityManager) {
		setEntityManager(entityManager);
	}

}
