package com.chare.mcb.repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.chare.core.Utils;
import com.chare.mcb.entity.User;
import com.chare.repository.JpaRepositoryImpl;

class UserRepositoryImpl extends JpaRepositoryImpl<Integer, User> implements UserRepository {

	UserRepositoryImpl() {
	}

	protected UserRepositoryImpl(EntityManager entityManager) {
		setEntityManager(entityManager);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public User findByUsername(String username) {
		TypedQuery<User> query = getEntityManager().createQuery("FROM " + User.class.getName() + " WHERE username = :username", User.class);
		query.setParameter("username", username);
		query.setMaxResults(1);
		return Utils.first(query.getResultList());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public User persist(User entity) {
		User persisted = super.persist(entity);
		copyLastAccessValue(entity, persisted);
		return persisted;
	}

	private void copyLastAccessValue(User entity, User persisted) {
		try {
			Utils.setFieldValue(persisted, "lastAccess", entity.getLastAccess());
		} catch (IllegalAccessException e) {
			throw new IllegalStateException("Set last access value error " + e.getMessage(), e);
		}
	}
}
