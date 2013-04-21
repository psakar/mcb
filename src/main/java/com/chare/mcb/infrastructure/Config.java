package com.chare.mcb.infrastructure;

import java.io.File;
import java.util.List;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitPostProcessor;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.chare.databaseUpgrader.DatabaseUpgrade;
import com.chare.databaseUpgrader.DatabaseUpgrader;
import com.chare.databaseUpgrader.DatabaseUpgradesReader;
import com.chare.infrastructure.EntityScannerPersistenceUnitPostProcessor;
import com.chare.infrastructure.EnvironmentConfig;
import com.chare.mcb.Application;
import com.chare.mcb.entity.User;

@Configuration
public abstract class Config {

	@Autowired
	private DataSource dataSource;

	@Autowired
	@Qualifier(EnvironmentConfig.UPDATE_DIRECTORY)
	private File updateDirectory;


	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private ResourcePatternResolver resourceLoader;


	@Bean
	@Autowired
	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) throws Exception {
		JpaTransactionManager transactionManager = new JpaTransactionManager(entityManagerFactory);
		transactionManager.setJpaDialect(new HibernateJpaDialect());
		transactionManager.setDataSource(dataSource);
		return transactionManager;
	}


	@Bean
	public DatabaseUpgrader databaseUpgrader() {
		List<DatabaseUpgrade> databaseUpgrades = new DatabaseUpgradesReader().readUpdates(updateDirectory.getAbsolutePath());
		return new DatabaseUpgraderImpl(dataSource, databaseUpgrades);
	}

	@Bean
	@Autowired
	public EntityManagerFactory entityManagerFactory(ValidatorFactory validatorFactory, PersistenceUnitPostProcessor persistenceUnitPostProcessor) throws Exception {
		if (!databaseUpgrader().upgrade())
			throw new IllegalStateException("Error during upgrade of database");

		EntityManagerFactory entityManagerFactory = lookupEntityManagerFactoryJndi(Application.name);
		if (entityManagerFactory != null) {
			Logger.getLogger(getClass()).info("Using entityManagerFactory " + Application.name + "  found in JNDI context");
			return entityManagerFactory;
		}
		LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
		factoryBean.setDataSource(dataSource);
		factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		//FIXME not working in JBOSS - why ? factoryBean.setPackagesToScan(User.class.getPackage().getName());
		factoryBean.setPersistenceUnitPostProcessors(persistenceUnitPostProcessor);
		factoryBean.setPersistenceUnitName(Application.name);
		factoryBean.setJpaProperties(com.chare.repository.Config.addValidatorFactoryToPersistenceProperties(validatorFactory, new Properties()));
		factoryBean.afterPropertiesSet();
		return factoryBean.getObject();
		//		return Persistence.createEntityManagerFactory(Application.name, com.chare.repository.Config.createPersistenceProperties(dataSource, validatorFactory));
	}

	private EntityManagerFactory lookupEntityManagerFactoryJndi(String name) {
		try {
			return (EntityManagerFactory) new InitialContext().lookup("java:jboss/persistence-units/" + name);
		} catch (NamingException e) {
			Logger.getLogger(getClass()).info("EntityManagerFactory " + name + " not found in JNDI context - " + e.getMessage());
		}
		return null;
	}

	@Bean
	public PersistenceUnitPostProcessor createPersistenceUnitPostProcessor() {
		return EntityScannerPersistenceUnitPostProcessor.createForPackageOfClass(User.class, resourceLoader);
	}


	@Bean
	public ValidatorFactory validatorFactory() throws Exception {
		return new ValidatorFactoryCreator().create(applicationContext);
	}

	@Bean
	public Validator validator() throws Exception {
		return validatorFactory().getValidator();
	}

	@Bean
	public PersistenceExceptionTranslator persistenceExceptionTranslator() {
		return new HibernateJpaDialect();
	}
}
