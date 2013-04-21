package com.chare.mcb.www;

import static org.mockito.Mockito.*;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.chare.mcb.entity.Role;
import com.chare.mcb.entity.Statement;
import com.chare.mcb.repository.StatementRepository;
import com.chare.mcb.repository.TransferTypeRepository;
import com.chare.mcb.service.CardTransactionParser;
import com.chare.mcb.service.PostingFileGenerator;
import com.chare.mcb.service.TransferTypeResolver;
import com.chare.mcb.www.StatementPage.StatementPanel;

public class StatementPageTest extends WicketTestCase {

	private StatementPage page;

	@Override
	@Before
	public void before() throws Exception {
		super.before();
		authenticateUser();
		user.addRole(Role.UPLOAD_STATEMENTS_ROLE);
	}

	private void startPage() {
		page = tester.startPage(StatementPage.class);
	}

	@Test
	public void testPageRendered() throws Exception {
		startPage();
		assertPageRenderedWithNoErrorOrInfoMessages(page.getClass());
	}

	@Test
	@Ignore
	// FIXME
	public void testPersistingPersistentUserRedirectsToUserList() throws Exception {
		PageParameters parameters = new PageParameters();
		parameters.set(PanelPage.ID_PARAM, "1");
		tester.startPage(UserPage.class, parameters);
		FormTester formTester = tester.newFormTester(getPath(UserPage.PANEL_ID, UserPage.USER_PANEL_ID, PanelPage.FORM_ID));
		formTester.submit();
		tester.assertRenderedPage(UserList.class);
	}

	@Test
	public void testContainsChildren() throws Exception {
		startPage();
		assertComponentIs(StatementPanel.class, PanelPage.PANEL_ID);
	}

	@Override
	protected Class<?> getCustomConfig() {
		return TestConfig.class;
	}

	@Configuration
	static class TestConfig {
		@Bean
		public CardTransactionParser cardTransactionParser() {
			return mock(CardTransactionParser.class);
		}

		@Bean
		public PostingFileGenerator postingFileGenerator() {
			return mock(PostingFileGenerator.class);
		}

		@Bean
		public StatementRepository statementRepository() {
			StatementRepository repository = mock(StatementRepository.class);
			when(repository.findById(1)).thenReturn(new Statement());
			return repository;
		}

		@Bean
		public TransferTypeRepository transferTypeRepository() {
			return mock(TransferTypeRepository.class);
		}

		@Bean
		public TransferTypeResolver transferTypeResolver() {
			return mock(TransferTypeResolver.class);
		}
	}

}