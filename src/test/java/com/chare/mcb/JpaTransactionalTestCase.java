package com.chare.mcb;

import javax.persistence.EntityTransaction;

import org.junit.After;
import org.junit.Before;

public abstract class JpaTransactionalTestCase extends JpaTestCase {

	protected EntityTransaction transaction;

	@Override
	@Before
	public void before() throws Exception {
		super.before();
		transaction = entityManager.getTransaction();
		transaction.begin();
	}

	@After
	public void tearDown() {
		if (transaction!=null && transaction.isActive())
			transaction.rollback();
	}

}
