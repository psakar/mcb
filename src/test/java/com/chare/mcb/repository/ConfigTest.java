package com.chare.mcb.repository;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.persistence.EntityManagerFactory;

import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;

import com.chare.test.SpringTestCase;

public class ConfigTest {

	@Test
	public void testRepositoryConfig() {
		AbstractApplicationContext context = SpringTestCase.createContext(Config.class, DependencyConfig.class);

		assertNotNull(context.getBean(CalendarRepository.class));
		assertNotNull(context.getBean(CardRepository.class));
		assertNotNull(context.getBean(CardItemRepository.class));
		assertNotNull(context.getBean(CardTypeRepository.class));
		assertNotNull(context.getBean(FeeTypeRepository.class));
		assertNotNull(context.getBean(PostingFileRepository.class));
		assertNotNull(context.getBean(SettingRepository.class));
		assertNotNull(context.getBean(StatementRepository.class));
		assertNotNull(context.getBean(StatementItemRepository.class));
		assertNotNull(context.getBean(TransferTypeRepository.class));
		assertNotNull(context.getBean(UserItemRepository.class));
		assertNotNull(context.getBean(UserRepository.class));

		context.close();
	}

	@Configuration
	static class DependencyConfig {

		@Bean
		public EntityManagerFactory entityManagerFactory() {
			return mock(EntityManagerFactory.class);
		}
	}

}
