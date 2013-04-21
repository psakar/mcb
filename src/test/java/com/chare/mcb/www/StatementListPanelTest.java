package com.chare.mcb.www;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import com.chare.mcb.entity.StatementItem;
import com.chare.mcb.repository.StatementItemRepository;
import com.chare.mcb.repository.StatementRepository;
import com.chare.mcb.service.UserPreferences;
import com.chare.mcb.www.StatementList.StatementListPanel;
import com.chare.repository.Restriction;

public class StatementListPanelTest extends WicketTestCase {

	private StatementListPanel panel;

	@Override
	@Before
	public void before() throws Exception {
		super.before();
		panel = new StatementListPanel("id");
		tester.startComponentInPage(panel);
	}

	@Test
	public void testPanelContents() throws Exception {
		assertNotNull(panel.get(AbstractListPanel.LIST_ID));
		assertNotNull(panel.get(AbstractListPanel.NAVIGATOR_ID));
		tester.assertNoErrorMessage();
	}

	@Test
	public void testListContents() throws Exception {
		tester.assertContains(TestConfig.entity.number);
		//		tester.assertContains(TestConfig.card.cardType.description.getDescription(user.getLanguageIndex()));
		//		tester.assertContains(TestConfig.card.holderName);
		//		tester.assertContains(String.valueOf(TestConfig.user.getUnsuccessfulCount()));
		//		tester.assertContains("No");
		tester.assertNoErrorMessage();
	}

	@Test
	@Ignore
	// FIXME
	public void testViewLink() throws Exception {
		tester.clickLink("panel:component:component:" + AbstractListPanel.LIST_ID + ":rows:1:view");
		tester.assertRenderedPage(StatementPage.class);
	}

	@Override
	protected Class<?> getCustomConfig() {
		return TestConfig.class;
	}

	@Configuration
	static class TestConfig {
		public static StatementItem entity;


		@Bean
		public StatementRepository statementRepository() {
			return mock(StatementRepository.class);
		}

		@SuppressWarnings("unchecked")
		@Bean
		public StatementItemRepository userItemRepository() {
			entity = new StatementItem();
			entity.setId(5);
			entity.number = "number";
			List<StatementItem> list = Arrays.asList(entity);

			Class<StatementItemRepository> repositoryClass = StatementItemRepository.class;
			StatementItemRepository repository = mock(repositoryClass);
			when(repository.find(anyInt(), anyInt(), any(List.class), any(List.class))).thenReturn(list);
			when(repository.getCount(anyListOf(Restriction.class))).thenReturn(1);

			return repository;
		}

		@Bean
		@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
		public UserPreferences userPreferences() {
			return new UserPreferences();
		}

	}

}
