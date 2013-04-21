package com.chare.mcb.www;


import org.junit.Before;
import org.junit.Test;

import com.chare.mcb.entity.Role;

public class CardListTest extends WicketTestCase {

	private CardList page;

	@Override
	@Before
	public void before() throws Exception {
		super.before();
		authenticateUser();
		user.addRole(Role.EXPORT_POSTINGS_ROLE);
		startPage();
	}

	private void startPage() {
		page = tester.startPage(CardList.class);
	}

	@Test
	public void testPageRendered() throws Exception {
		assertPageRenderedWithNoErrorOrInfoMessages(page.getClass());
	}

	@Override
	protected Class<?> getCustomConfig() {
		return CardListPanelTest.TestConfig.class;
	}

}
