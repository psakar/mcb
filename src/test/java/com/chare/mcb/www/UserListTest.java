package com.chare.mcb.www;


import org.junit.Before;
import org.junit.Test;

import com.chare.mcb.entity.Role;

public class UserListTest extends WicketTestCase {

	private UserList page;

	@Override
	@Before
	public void before() throws Exception {
		super.before();
		authenticateUser();
		user.addRole(Role.USER_ADMIN_ROLE);
		startPage();
	}

	private void startPage() {
		page = tester.startPage(UserList.class);
	}

	@Test
	public void testPageRendered() throws Exception {
		assertPageRenderedWithNoErrorOrInfoMessages(page.getClass());
	}

	@Override
	protected Class<?> getCustomConfig() {
		return UserListPanelTest.TestConfig.class;
	}

}
