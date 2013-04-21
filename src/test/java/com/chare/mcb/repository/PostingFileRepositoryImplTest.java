package com.chare.mcb.repository;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.chare.core.Db;
import com.chare.core.Utils;
import com.chare.mcb.JpaRepositoryTestCase;
import com.chare.mcb.entity.Booking;
import com.chare.mcb.entity.EntityWithIdInteger;
import com.chare.mcb.entity.PostingFile;
import com.chare.mcb.entity.Statement;
import com.chare.mcb.entity.User;

public class PostingFileRepositoryImplTest extends JpaRepositoryTestCase<Integer, PostingFile, PostingFileRepository> {

	@Override
	protected String getTableName() {
		return EntityWithIdInteger.POSTING_FILE_TABLE;
	}

	@Override
	protected PostingFile setupEntity(PostingFile entity) {
		entity.filename = createStringFromMilliseconds();
		entity.setCreatedUser(new User(1));
		return entity;
	}

	@Override
	protected void assertPersistedEntity(PostingFile loaded, PostingFile entity) {
		assertEquals(entity.id, loaded.id);
		assertArrayEquals(entity.getBookings().toArray(), loaded.getBookings().toArray());
	}

	@Override
	protected PostingFileRepository getRepository() {
		return new PostingFileRepositoryImpl(entityManager);
	}

	@Test
	public void testBookingsArePersited() throws Exception {
		PostingFile entity = createPostingFileWithBooking();
		repository.persist(entity);
		assertNotNull(entity.getId());
		entityManager.flush();
		entityManager.clear();
		PostingFile loaded = repository.findById(entity.getId());
		assertNotNull(loaded.getId());
		repository.delete(entity);
		assertPersistedEntity(loaded, entity);
	}

	private PostingFile createPostingFileWithBooking() {
		PostingFile entity = new PostingFile();
		setupEntity(entity);
		Booking line = entity.addBooking();
		line.amount = new BigDecimal("-1.23");
		line.details = "details";
		line.bankToBankInfo = "bankToBankInfo";
		line.orderingBankAddress = "orderingBankAddress";
		line.orderingBankName = "orderingBankName";
		line.creditValueDate = Utils.getTomorrow();
		line.debitValueDate = Utils.getTomorrow();
		return entity;
	}
	@Override
	@Test
	public void testFindingById() throws Exception {
		//do not test
	}

	@Override
	public void testFindingByRestrictions() throws Exception {
		//do not test
	}

	@Test
	public void testFindLastSequenceNr() throws Exception {
		Date businessDate = Utils.getToday();
		int sequenceNr = 123;
		createBookingWithReferenceNr(0, sequenceNr);
		assertEquals(sequenceNr, repository.findLastSequenceNr(businessDate));
	}

	@Test
	public void testFindLastSequneceNrForNewBusinessDate() throws Exception {
		Date businessDate = Utils.getDate(2022, 1, 1);
		assertNull(lookup("SELECT MAX(sequenceNr) FROM booking WHERE businessDate = " + Db.convertDate(businessDate)));
		assertEquals(0, repository.findLastSequenceNr(businessDate));
	}

	@Test
	public void testFindLastReferenceNr() throws Exception {
		Date businessDate = Utils.getToday();
		int referenceNr = 9876;
		createBookingWithReferenceNr(referenceNr, 0);

		assertEquals(referenceNr, repository.findLastReferenceNr(businessDate));
	}

	private void createBookingWithReferenceNr(int referenceNr, int sequenceNr) {
		PostingFile postingFile = createPostingFileWithBooking();
		Booking booking = postingFile.getBookings().get(0);
		booking.trReference = booking.createTrReference(Utils.getToday(), referenceNr);
		booking.sequenceNr = sequenceNr;
		entityManager.persist(postingFile);
		entityManager.flush();
	}

	@Test
	public void testFindLastReferenceNrForNewBusinessDate() throws Exception {
		Date businessDate = Utils.getDate(2022, 1, 1);
		assertNull(lookup("SELECT MAX(SUBSTRING(trReference, 12)) FROM booking WHERE substring(trReference,1,11) = " + Db.convertString(new Booking().createTrReferencePrefix(businessDate))));
		assertEquals(0, repository.findLastReferenceNr(businessDate));
	}

	@Test
	public void testPostingFileDeleteClearsStatementReference() throws Exception {
		PostingFile postingFile = createAndPersistPostingFile();
		Statement statement = createAndPersistStatementReferencingPostingFile(postingFile.getId());

		repository.delete(postingFile);

		flushAndClearEntityManager();
		assertStatementReferenceToPostingFileIsCleared(statement);
	}

	private void assertStatementReferenceToPostingFileIsCleared(
			Statement statement) {
		Statement loaded = entityManager.find(Statement.class, statement.getId());
		assertNull(loaded.postingFileId);
	}

	private PostingFile createAndPersistPostingFile() {
		PostingFile entity = createPostingFileWithBooking();
		repository.persist(entity);
		entityManager.flush();
		return entity;
	}

	private Statement createAndPersistStatementReferencingPostingFile(Integer postingFileId) {
		Statement statement = createStatement();
		statement.postingFileId = postingFileId;
		entityManager.persist(statement);
		entityManager.flush();
		return statement;
	}

	private Statement createStatement() {
		Statement statement = new Statement();
		statement.number = StringUtils.right("1234567890123456" + createStringFromMilliseconds(), 4);
		statement.statementDate = Utils.getToday();
		statement.sourceFilename = createStringFromMilliseconds();
		statement.account = "account";
		return statement;
	}
}
