package com.chare.mcb.www;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.Bean;

import com.chare.mcb.entity.Role;
import com.chare.mcb.repository.CardTypeRepository;
import com.chare.mcb.www.CardTypePage.CardTypePanel;

public class CardTypePageTest extends WicketTestCase {

	private CardTypePage page;

	@Override
	@Before
	public void before() throws Exception {
		super.before();
		authenticateUser();
		user.addRole(Role.APP_ADMIN_ROLE);
		startPage();
	}

	private void startPage() {
		page = tester.startPage(CardTypePage.class);
	}

	@Test
	public void testPageRendered() throws Exception {
		assertPageRenderedWithNoErrorOrInfoMessages(page.getClass());
	}

	@Test
	public void testContainsChildren() throws Exception {
		assertComponentIs(CardTypePanel.class, CardTypePage.PANEL_ID);
	}

	@Test
	public void testIsSecured() throws Exception {
		assertPageIsSecured(CardTypePage.class);
	}


	@Override
	protected Class<?> getCustomConfig() {
		return TestConfig.class;
	}

	static class TestConfig extends PageConfig {
		@Bean
		public CardTypeRepository rootAccountFullRepository() {
			return mock(CardTypeRepository.class);
		}
	}

}