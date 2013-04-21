package com.chare.mcb;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.chare.core.Db;
import com.chare.core.Utils;
import com.chare.entity.EntityWithId;
import com.chare.repository.ListAndCountRepository;
import com.chare.repository.Repository;

public abstract class ListAndCountRepositoryTestCase<Type extends EntityWithId<?>, RepositoryType extends ListAndCountRepository<Type>> extends JpaTransactionalTestCase {

	protected RepositoryType repository;

	@Override
	@Before
	public void before() throws Exception {
		super.before();
		repository = getRepository();
	}

	protected abstract RepositoryType getRepository();
	protected abstract String getTableName();

	@Test
	public void testTransactional() throws Exception {
		JpaRepositoryTestCase.assertPublicMethodsAreAnnotatedTransactional(repository.getClass());
	}

	@Test
	public void testCount() throws Exception {
		int correct = Utils.isNull(Db.dlookup(connectionProvider, "SELECT count(*) FROM " + getTableName(), null, null, -1), -1);
		assertEquals(correct, repository.getCount(null));
	}

	@Test
	public void testList() throws Exception {
		int correct = Utils.isNull(Db.dlookup(connectionProvider, "SELECT count(*) FROM " + getTableName(), null, null, -1), -1);
		assertEquals(correct, repository.find(Repository.FIRST, Repository.ALL_RESUTLS, Repository.NO_RESTRICTION, Repository.NO_SORT).size());
	}

}
