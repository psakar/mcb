package com.chare.mcb.www;

import java.util.Date;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;

import com.chare.mcb.entity.Role;

public class ProfilePageTest extends WicketTestCase {


	@Override
	public void before() throws Exception {
		super.before();
		user.username = "username";
		user.email = "email";
		user.languageId = 1;
		user.name = "name";
		user.phone = "phone";
		user.setLoginTimestamp(new Date());
		user.setLoginTimestamp(new Date());// second change updates last access date
		user.surname = "surname";
	}

	@Test
	public void testRender() {
		authenticateUser();
		user.addRoles(Role.APP_ADMIN_ROLE);
		tester.startPage(ProfilePage.class);
		tester.assertRenderedPage(ProfilePage.class);
		assertContent(tester);
	}


	private void assertContent(WicketTester tester) {
		tester.assertContains(user.username);
		tester.assertContains(user.email);
		tester.assertContains(user.name);
		tester.assertContains(user.phone);
		tester.assertContains(user.surname);
		tester.assertContains(user.getLocale().getLanguage());
		//FIXME		tester.assertContains(user.getDateFormat().format(user.getLastAccess().toDate()));
	}

	@Test
	public void testPageIsSecured() {
		assertPageIsSecured(ProfilePage.class);
	}

}
