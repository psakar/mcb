package com.chare.mcb.www;


import org.junit.Before;
import org.junit.Test;

import com.chare.mcb.entity.Role;

public class StatementListTest extends WicketTestCase {

	private StatementList page;

	@Override
	@Before
	public void before() throws Exception {
		super.before();
		authenticateUser();
		user.addRole(Role.UPLOAD_STATEMENTS_ROLE);
		startPage();
	}

	private void startPage() {
		page = tester.startPage(StatementList.class);
	}

	@Test
	public void testPageRendered() throws Exception {
		assertPageRenderedWithNoErrorOrInfoMessages(page.getClass());
	}

	@Override
	protected Class<?> getCustomConfig() {
		return StatementListPanelTest.TestConfig.class;
	}

}
