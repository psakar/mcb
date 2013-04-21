package com.chare.mcb.www;


import org.junit.Before;
import org.junit.Test;

import com.chare.mcb.entity.Role;

public class FeeTypeListTest extends WicketTestCase {

	private FeeTypeList page;

	@Override
	@Before
	public void before() throws Exception {
		super.before();
		authenticateUser();
		user.addRole(Role.APP_ADMIN_ROLE);
		startPage();
	}

	private void startPage() {
		page = tester.startPage(FeeTypeList.class);
	}

	@Test
	public void testPageRendered() throws Exception {
		assertPageRenderedWithNoErrorOrInfoMessages(page.getClass());
	}

	@Override
	protected Class<?> getCustomConfig() {
		return FeeTypePageTest.TestConfig.class;
	}
}
