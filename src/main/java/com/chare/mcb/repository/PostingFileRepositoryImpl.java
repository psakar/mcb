package com.chare.mcb.repository;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.chare.core.Utils;
import com.chare.mcb.entity.Booking;
import com.chare.mcb.entity.PostingFile;
import com.chare.mcb.entity.Statement;
import com.chare.repository.JpaRepositoryImpl;

class PostingFileRepositoryImpl extends JpaRepositoryImpl<Integer, PostingFile> implements PostingFileRepository {

	PostingFileRepositoryImpl() {
	}

	protected PostingFileRepositoryImpl(EntityManager entityManager) {
		setEntityManager(entityManager);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public int findLastSequenceNr(Date businessDate) {
		Query query = getEntityManager().createQuery("SELECT MAX(s.sequenceNr) FROM " + Booking.class.getName() + " s WHERE s.businessDate = :businessDate");
		query.setParameter("businessDate", businessDate);
		@SuppressWarnings("unchecked")
		Integer sequenceNr = (Integer) Utils.first(query.getResultList());
		return sequenceNr == null ? 0 : sequenceNr;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public int findLastReferenceNr(Date businessDate) {
		String hql = "SELECT MAX(SUBSTRING(s.trReference, 12)) FROM " + Booking.class.getName() + " s WHERE substring(s.trReference, 1, 11) = :businessDate";
		Query query = getEntityManager().createQuery(hql);
		query.setParameter("businessDate", new Booking().createTrReferencePrefix(businessDate));
		@SuppressWarnings("unchecked")
		String referenceNr = (String) Utils.first(query.getResultList());
		return referenceNr == null ? 0 : Integer.parseInt(referenceNr);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void delete(PostingFile entity) {
		getEntityManager().createQuery("UPDATE " + Statement.class.getName() + " SET postingFileId = null WHERE postingFileId = " + entity.getId()).executeUpdate();
		super.delete(entity);
	}
}
