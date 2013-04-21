package com.chare.mcb.www;

import static org.mockito.Mockito.*;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.chare.mcb.repository.UserRepository;

public class EditProfilePageTest extends WicketTestCase {


	@Override
	public void before() throws Exception {
		super.before();
		authenticateUser();
		user.email = "email";
		user.languageId = 1;
		user.name = "name";
		user.phone = "phone";
		user.surname = "surname";
	}

	@Test
	public void testRender() {
		tester.startPage(EditProfilePage.class);
		tester.assertRenderedPage(EditProfilePage.class);
		assertContent(tester);
	}

	private void assertContent(WicketTester tester) {
		tester.assertContains(user.username);
		tester.assertContains(user.name);
		tester.assertContains(user.surname);
		tester.assertContains(user.email);
		tester.assertContains(user.phone);
	}

	@Override
	protected Class<?> getCustomConfig() {
		return PageConfig.class;
	}

	@Configuration
	protected static class PageConfig {

		@Bean
		public UserRepository userRepository() {
			return mock(UserRepository.class);
		}
	}

}
