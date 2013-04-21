package com.chare.mcb.www;

import static org.junit.Assert.*;

import org.junit.Test;

import com.chare.mcb.Application;
import com.chare.mcb.entity.Role;

public class MenuPageTest extends WicketTestCase {

	@Override
	public void before() throws Exception {
		super.before();
	}

	@Test
	public void testDisplay() {
		authenticateUser();
		user.addRole(Role.APP_ADMIN_ROLE);
		assertPageIsRendered(MenuPage.class);
		assertTrue(tester.getLastResponse().getDocument().contains("src=\"" + Application.PROFILE_PAGE + "\""));
	}

	@Test
	public void testPageIsSecured() {
		assertPageIsSecured(MenuPage.class);
	}

}
