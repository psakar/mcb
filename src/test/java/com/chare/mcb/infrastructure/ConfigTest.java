package com.chare.mcb.infrastructure;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.transaction.PlatformTransactionManager;

import com.chare.databaseUpgrader.DatabaseUpgrader;
import com.chare.infrastructure.EnvironmentConfig;
import com.chare.test.DbTestCase;
import com.chare.test.SpringTestCase;

public class ConfigTest {

	@Test
	public void testRepositoryConfig() {
		AbstractApplicationContext context = SpringTestCase.createContext(Config.class, DependencyConfig.class);
		assertNotNull(context.getBean(PlatformTransactionManager.class));
		//		assertNotNull(context.getBean(EntityManagerFactory.class));
		assertNotNull(context.getBean(EntityManagerFactory.class));

		assertNotNull(context.getBean(DatabaseUpgrader.class));

		assertNotNull(context.getBean(Validator.class));
		assertNotNull(context.getBean(ValidatorFactory.class));

		//not working even when defined		assertNotNull(context.getBean(PersistenceExceptionTranslator.class));

		context.close();
	}

	@Configuration
	static class DependencyConfig {
		@Bean
		public DataSource dataSource() {
			return DbTestCase.createDataSource();
		}

		@Bean(name =EnvironmentConfig.UPDATE_DIRECTORY)
		public File updateDirectory() {
			return new File(".");
		}

		@Bean
		public ResourcePatternResolver resourcePatternResolver() {
			return mock(ResourcePatternResolver.class);
		}
	}

}
