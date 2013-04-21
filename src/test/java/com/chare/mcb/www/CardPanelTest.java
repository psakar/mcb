package com.chare.mcb.www;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.apache.wicket.Component;
import org.apache.wicket.model.Model;
import org.junit.Test;
import org.springframework.context.annotation.Bean;

import com.chare.mcb.entity.Card;
import com.chare.mcb.repository.CardRepository;
import com.chare.mcb.repository.CardTypeRepository;
import com.chare.mcb.www.CardPage.CardPanel;
import com.chare.wicket.TextField;

public class CardPanelTest extends WicketTestCase {


	@Test
	public void testRendering() throws Exception {
		Card displayedEntity = createDisplayedEntity();
		tester.startComponentInPage(createPanel(displayedEntity));
		tester.assertNoErrorMessage();
		assertDisplaysEntity(displayedEntity);
	}

	private void assertDisplaysEntity(Card card) {
		tester.assertContains(card.number);
		tester.assertContains(card.holderName);
		tester.assertContains(card.email);
		tester.assertContains(card.phone);
		//FIXME other
	}

	private CardPanel createPanel(Card entity) {
		return new CardPanel("id", new Model<Card>(entity));
	}

	private Card createDisplayedEntity() {
		Card entity = new Card();
		entity.setId(4);
		entity.number = "number";
		entity.holderName = "holderName";
		entity.email = "email";
		entity.phone = "phone";
		return entity;
	}

	@Test
	public void testContainsChildren() throws Exception {
		CardPanel panel = createPanel(createDisplayedEntity());
		tester.startComponentInPage(panel);
		tester.assertNoErrorMessage();
		assertEquals(TextField.class, getComponentClass(panel, "number"));
		assertEquals(LanguageChoice.class, getComponentClass(panel, "language"));
		//FIXME other
	}

	private Class<? extends Component> getComponentClass(Component panel, String componentId) {
		return panel.get(getPath(PanelPage.FORM_ID, componentId)).getClass();
	}


	@Override
	protected Class<?> getCustomConfig() {
		return TestConfig.class;
	}

	static class TestConfig extends PageConfig {
		@Bean
		public CardRepository cardRepository() {
			return mock(CardRepository.class);
		}

		@Bean
		public CardTypeRepository cardTypeRepository() {
			return mock(CardTypeRepository.class);
		}
	}

}
