package com.chare.mcb.repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.chare.core.Utils;
import com.chare.mcb.entity.Statement;
import com.chare.repository.JpaRepositoryImpl;

class StatementRepositoryImpl extends JpaRepositoryImpl<Integer, Statement> implements StatementRepository {

	StatementRepositoryImpl() {
	}

	protected StatementRepositoryImpl(EntityManager entityManager) {
		setEntityManager(entityManager);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public Statement findByNumber(String number, int year) {
		TypedQuery<Statement> query = getEntityManager().createQuery("FROM " + Statement.class.getName() + " WHERE number = :number AND year = :year", Statement.class);
		query.setParameter("number", number);
		query.setParameter("year", year);
		query.setMaxResults(1);
		return Utils.first(query.getResultList());
	}
}
