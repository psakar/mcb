package com.chare.mcb.repository;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.chare.core.Utils;
import com.chare.mcb.JpaRepositoryTestCase;
import com.chare.mcb.entity.EntityWithIdInteger;
import com.chare.mcb.entity.Statement;
import com.chare.mcb.entity.StatementLine;

public class StatementRepositoryImplTest extends JpaRepositoryTestCase<Integer, Statement, StatementRepository> {

	@Override
	protected String getTableName() {
		return EntityWithIdInteger.STATEMENT_TABLE;
	}

	@Override
	protected Statement setupEntity(Statement entity) {
		entity.number = StringUtils.right("1234567890123456" + createStringFromMilliseconds(), 4);
		entity.statementDate = Utils.getToday();
		entity.sourceFilename = createStringFromMilliseconds();
		entity.account = "account";
		//		entity.cardType = lookupEntity("SELECT * FROM " + EntityWithIdInteger.CARD_TYPE_TABLE + " WHERE code = 'BUSINESS'", StatementType.class);
		/*FIXME
		 * 		Address address = entity.getMailAddress();
		address.name = "name";
		address.street = "street";
		address.zip = "zip";
		address.town = "town";
		 */
		return entity;
	}

	@Override
	protected void assertPersistedEntity(Statement loaded, Statement entity) {
		assertEquals(entity.id, loaded.id);
		assertArrayEquals(entity.getLines().toArray(), loaded.getLines().toArray());
	}

	@Override
	protected StatementRepository getRepository() {
		return new StatementRepositoryImpl(entityManager);
	}

	@Test
	public void testFindByNumber() throws Exception {
		Integer id = findId(getTableName());
		String number = lookup("SELECT number FROM " + getTableName() + " WHERE id = " + id);
		Date statementDate = lookup("SELECT statementDate FROM " + getTableName() + " WHERE id = " + id);
		assertEquals(id, repository.findByNumber(number, Utils.getDatePart(Calendar.YEAR, statementDate)).getId());
	}

	@Test
	public void testStatementLinesArePersited() throws Exception {
		Statement entity = create();
		setupEntity(entity);
		StatementLine line = entity.addLine();
		line.amount = new BigDecimal("-1.23");
		line.details1 = "details1";
		line.details2 = "details2";
		line.details3 = "details3";
		line.details4 = "details4";
		line.valueDate = Utils.getTomorrow();
		/* FIXME
		FeeType feeType = lookupEntity("SELECT * FROM " + EntityWithIdInteger.FEE_TYPE_TABLE + " WHERE code = 'B_ICFEE'", FeeType.class);
		entity.addFeeType(feeType);
		 */
		repository.persist(entity);
		assertNotNull(entity.getId());
		entityManager.flush();
		entityManager.clear();
		Statement loaded = repository.findById(entity.getId());
		assertNotNull(loaded.getId());
		repository.delete(entity);
		assertPersistedEntity(loaded, entity);
	}
}
