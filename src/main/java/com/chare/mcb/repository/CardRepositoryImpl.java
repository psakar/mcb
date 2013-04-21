package com.chare.mcb.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.chare.core.Utils;
import com.chare.mcb.entity.Card;
import com.chare.mcb.entity.CardStatementLine;
import com.chare.mcb.entity.StatementLine;
import com.chare.repository.JpaRepositoryImpl;

class CardRepositoryImpl extends JpaRepositoryImpl<Integer, Card> implements CardRepository {

	CardRepositoryImpl() {
	}

	protected CardRepositoryImpl(EntityManager entityManager) {
		setEntityManager(entityManager);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public Card findByNumber(String number) {
		TypedQuery<Card> query = getEntityManager().createQuery("FROM " + Card.class.getName() + " WHERE number = :number", Card.class);
		query.setParameter("number", number);
		query.setMaxResults(1);
		return Utils.first(query.getResultList());
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public List<Card> findCardsToGenerateStatement() {
		TypedQuery<Card> query = getEntityManager().createQuery("FROM " + Card.class.getName() + " card WHERE card.nextStatementDate <= :today AND exists (SELECT line FROM " + StatementLine.class.getName() + " line WHERE line.cardTransaction.cardNumber = card.number AND line.bookDate <= card.nextStatementDate AND (line.bookDate > card.lastStatementDate OR card.lastStatementDate is null))", Card.class);
		query.setParameter("today", Utils.getToday(), TemporalType.DATE);
		return query.getResultList();
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public List<CardStatementLine> loadCardStatementLines(String cardNumber,
			Date periodStart, Date periodEnd) {
		TypedQuery<CardStatementLine> query = getEntityManager().createQuery("FROM " + CardStatementLine.class.getName() + " cardStatementLine WHERE cardStatementLine.bookDate >= :periodStart AND cardStatementLine.bookDate <= :periodEnd AND cardStatementLine.cardTransaction.cardNumber = :cardNumber", CardStatementLine.class);
		query.setParameter("periodStart", periodStart, TemporalType.DATE);
		query.setParameter("periodEnd", periodEnd, TemporalType.DATE);
		query.setParameter("cardNumber", cardNumber);
		return query.getResultList();
	}
}
