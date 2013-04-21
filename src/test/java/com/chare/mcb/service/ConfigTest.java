package com.chare.mcb.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;

import com.chare.core.Titles.TitleSources;
import com.chare.infrastructure.EnvironmentConfig;
import com.chare.infrastructure.reportService.ReportServiceImpl.SystemParametersProvider;
import com.chare.mcb.Application;
import com.chare.mcb.repository.CalendarRepository;
import com.chare.mcb.repository.CardRepository;
import com.chare.mcb.repository.FeeTypeRepository;
import com.chare.mcb.repository.PostingFileRepository;
import com.chare.mcb.repository.SettingRepository;
import com.chare.mcb.repository.StatementRepository;
import com.chare.mcb.repository.TransferTypeRepository;
import com.chare.mcb.repository.UserRepository;
import com.chare.service.AuditService;
import com.chare.service.UserSessionsList;
import com.chare.test.SpringTestCase;

public class ConfigTest {

	@Test
	public void testConfig() throws Exception {
		AbstractApplicationContext context = SpringTestCase.createContext(Config.class, DependencyConfig.class);
		assertNotNull(context.getBean(AuthenticationService.class));
		assertNotNull(context.getBean(AuditService.class));
		assertNotNull(context.getBean(BookingGenerator.class));
		assertNotNull(context.getBean(CardTransactionParser.class));
		assertNotNull(context.getBean(GenerateCardStatementsTask.class));
		assertNotNull(context.getBean(PostingFileGenerator.class));
		assertNotNull(context.getBean(PostingFileExporter.class));
		assertNotNull(context.getBean(TransferTypeResolver.class));
		assertNotNull(context.getBean(UploadFileService.class));

		assertNotNull(context.getBean(UserSessionsList.class));
		assertNotNull(context.getBean(com.chare.service.Config.USER_PREFERENCES_SESSION_ID, UserPreferences.class));
		//		assertNotNull(context.getBean(ReportService.class));
		assertNotNull(context.getBean(SystemParametersProvider.class));

		context.close();
	}

	@Test
	public void testUserScopeIsSession() {
		AbstractApplicationContext context = SpringTestCase.createContext(Config.class, DependencyConfig.class);

		UserPreferences userPreferences1 = context.getBean(com.chare.service.Config.USER_PREFERENCES_SESSION_ID, UserPreferences.class);
		assertNotNull(userPreferences1);

		UserPreferences userPreferences2 = context.getBean(com.chare.service.Config.USER_PREFERENCES_SESSION_ID, UserPreferences.class);
		assertSame(userPreferences1, userPreferences2); // from session

		//FIXME closeSessionAndStartNew();

		//FIXME assertNotSame(userPreferences1, context.getBean(UserConfig.USER_PREFERENCES, User.class)); // created new in new session
	}


	@Configuration
	static class DependencyConfig {


		@Bean
		public TitleSources titleSources() {
			return new TitleSources();
		}

		@Bean
		public CardRepository cardRepository() {
			return mock(CardRepository.class);
		}


		@Bean
		public CalendarRepository calendarRepository() {
			return mock(CalendarRepository.class);
		}

		@Bean
		public FeeTypeRepository feeTypeRepository() {
			return mock(FeeTypeRepository.class);
		}

		@Bean
		public StatementRepository statementRepository() {
			return mock(StatementRepository.class);
		}

		@Bean
		public TransferTypeRepository transferTypeRepository() {
			return mock(TransferTypeRepository.class);
		}

		@Bean
		public UserRepository userRepository() {
			return mock(UserRepository.class);
		}


		@Bean
		public PostingFileRepository postingFileRepository() {
			return mock(PostingFileRepository.class);
		}

		@Bean
		public BookingGenerator bookingGenerator() {
			return mock(BookingGenerator.class);
		}

		@Bean
		public SettingRepository settingRepository() {
			return mock(SettingRepository.class);
		}

		@Bean
		@Qualifier(EnvironmentConfig.REPORT_DIRECTORY)
		public File reportDirectory() {
			return new File(".");
		}

		@Bean(name = com.chare.infrastructure.Config.APPLICATION_LOCALES)
		public List<Locale> applicationLocales() {
			return Application.DEFAULT_LOCALES;
		}
	}
}
