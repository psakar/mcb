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

import com.chare.mcb.entity.CardType;
import com.chare.mcb.repository.CardTypeRepository;
import com.chare.mcb.repository.UserRepository;
import com.chare.mcb.service.UserPreferences;
import com.chare.mcb.www.CardTypeList.CardTypeListPanel;
import com.chare.repository.Restriction;

public class CardTypeListPanelTest extends WicketTestCase {

	private CardTypeListPanel cardTypePanel;

	@Override
	@Before
	public void before() throws Exception {
		super.before();
		cardTypePanel = new CardTypeListPanel("id");
		tester.startComponentInPage(cardTypePanel);
	}

	@Test
	public void testPanelContents() throws Exception {
		assertNotNull(cardTypePanel.get(AbstractListPanel.LIST_ID));
		assertNotNull(cardTypePanel.get(AbstractListPanel.NAVIGATOR_ID));
		tester.assertNoErrorMessage();
	}

	@Test
	public void testListContents() throws Exception {
		tester.assertContains(TestConfig.cardType.code);
		tester.assertContains(TestConfig.cardType.description.description1);
		tester.assertContains(TestConfig.cardType.description.description2);
		tester.assertContains(TestConfig.cardType.description.description3);
		tester.assertNoErrorMessage();
	}

	@Test
	@Ignore
	// FIXME
	public void testViewLink() throws Exception {
		tester.clickLink("panel:component:component:" + AbstractListPanel.LIST_ID + ":rows:1:view");
		tester.assertRenderedPage(CardTypePage.class);
	}

	@Test
	public void testIsSecured() throws Exception {
		assertPageIsSecured(CardTypeList.class);
	}

	@Override
	protected Class<?> getCustomConfig() {
		return TestConfig.class;
	}

	@Configuration
	static class TestConfig {
		public static CardType cardType;

		@SuppressWarnings("unchecked")
		@Bean
		public CardTypeRepository cardTypeDataProvider() {
			cardType = CardTypePanelTest.createCardType();

			List<CardType> list = Arrays.asList(cardType);
			CardTypeRepository repository = mock(CardTypeRepository.class);
			when(repository.find(anyInt(), anyInt(), any(List.class), any(List.class))).thenReturn(list);
			when(repository.findById(anyString())).thenReturn(cardType);
			when(repository.getCount(anyListOf(Restriction.class))).thenReturn(1);

			return repository;
		}

		@Bean
		public UserRepository userRepository() {
			return mock(UserRepository.class);
		}
		@Bean
		@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
		public UserPreferences userPreferences() {
			return new UserPreferences();
		}
	}

}
