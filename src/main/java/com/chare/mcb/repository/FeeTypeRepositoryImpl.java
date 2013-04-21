package com.chare.mcb.repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.chare.core.Utils;
import com.chare.mcb.entity.Card;
import com.chare.mcb.entity.FeeType;
import com.chare.mcb.entity.TransferType;
import com.chare.repository.JpaRepositoryImpl;

class FeeTypeRepositoryImpl extends JpaRepositoryImpl<String, FeeType> implements FeeTypeRepository {

	FeeTypeRepositoryImpl() {
	}

	protected FeeTypeRepositoryImpl(EntityManager entityManager) {
		setEntityManager(entityManager);
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public FeeType find(Card card, TransferType transferType) {
		if (card == null || transferType == null)
			return null;
		TypedQuery<FeeType> query = getEntityManager().createQuery("FROM " + FeeType.class.getName() + " WHERE cardType = :cardType AND transferType = :transferType", FeeType.class);
		query.setParameter("cardType", card.cardType);
		query.setParameter("transferType", transferType);
		return Utils.first(query.getResultList());
	}

}
