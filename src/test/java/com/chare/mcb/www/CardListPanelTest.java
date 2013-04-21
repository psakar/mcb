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

import com.chare.mcb.entity.Address;
import com.chare.mcb.entity.CardItem;
import com.chare.mcb.entity.StatementPeriod;
import com.chare.mcb.repository.CardItemRepository;
import com.chare.mcb.repository.CardRepository;
import com.chare.mcb.service.UserPreferences;
import com.chare.mcb.www.CardList.CardListPanel;
import com.chare.repository.Restriction;

public class CardListPanelTest extends WicketTestCase {

	private CardListPanel userPanel;

	@Override
	@Before
	public void before() throws Exception {
		super.before();
		userPanel = new CardListPanel("id");
		tester.startComponentInPage(userPanel);
	}

	@Test
	public void testPanelContents() throws Exception {
		assertNotNull(userPanel.get(AbstractListPanel.LIST_ID));
		assertNotNull(userPanel.get(AbstractListPanel.NAVIGATOR_ID));
		tester.assertNoErrorMessage();
	}

	@Test
	public void testListContents() throws Exception {
		tester.assertContains(TestConfig.card.number);
		tester.assertContains(TestConfig.card.cardType.description.getDescription(user.getLanguageIndex()));
		tester.assertContains(TestConfig.card.holderName);
		//		tester.assertContains(String.valueOf(TestConfig.user.getUnsuccessfulCount()));
		//		tester.assertContains("No");
		tester.assertNoErrorMessage();
	}

	@Test
	@Ignore
	// FIXME
	public void testViewLink() throws Exception {
		tester.clickLink("panel:component:component:" + AbstractListPanel.LIST_ID + ":rows:1:view");
		tester.assertRenderedPage(CardPage.class);
	}

	@Override
	protected Class<?> getCustomConfig() {
		return TestConfig.class;
	}

	@Configuration
	static class TestConfig {
		public static CardItem card;


		@Bean
		public CardRepository cardRepository() {
			return mock(CardRepository.class);
		}

		@SuppressWarnings("unchecked")
		@Bean
		public CardItemRepository userItemRepository() {
			card = new CardItem();
			card.setId(5);
			card.number = "number";
			card.cardType = CardTypePanelTest.createCardType();
			card.holderName = "holderName";
			card.address = new Address();
			card.address.name = "name";
			card.statementPeriod = StatementPeriod.DAILY;

			List<CardItem> list = Arrays.asList(card);

			Class<CardItemRepository> repositoryClass = CardItemRepository.class;
			CardItemRepository repository = mock(repositoryClass);
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
