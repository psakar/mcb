package com.chare.mcb;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;
import javax.validation.ValidatorFactory;



public abstract class JpaTestCase extends com.chare.test.JpaTestCase {

	//	protected Lookup lookup;

	@Override
	public void before() throws Exception {
		super.before();
		//		lookup = new Lookup(entityManager);
	}

	@Override
	protected String getPeristenceUnitName() {
		return Application.name;
	}

	@Override
	protected EntityManagerFactory createEntityManagerFactory(
			String peristenceUnitName, DataSource dataSource,
			ValidatorFactory validatorFactory) {
		Properties persistenceProperties = com.chare.repository.Config.createPersistenceProperties(dataSource, validatorFactory);
		//    persistenceProperties.put("jadira.usertype.autoRegisterUserTypes", true);
		return Persistence.createEntityManagerFactory(peristenceUnitName, persistenceProperties);
	}
}
