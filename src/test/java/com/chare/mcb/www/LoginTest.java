package com.chare.mcb.www;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.persistence.EntityManagerFactory;

import org.apache.wicket.Page;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.chare.mcb.Application;
import com.chare.mcb.entity.User;
import com.chare.mcb.service.AuthenticationService;
import com.chare.mcb.www.LoginPage;
import com.chare.mcb.www.LoginPage.LoginForm;

public class LoginTest extends WicketTestCase {

	private Page page;
	private AuthenticationService authenticationService;

	@Override
	@Before
	public void before() throws Exception {
		super.before();
		page = tester.startPage(LoginPage.class);
		authenticationService = applicationContext.getBean(AuthenticationService.class);
		tester.setFollowRedirects(false);
	}

	@Override
	protected Class<?>[] getConfigs() {
		return new Class<?>[] { TestConfig.class };
	}

	@Configuration
	static class TestConfig extends WebApplicationConfig {

		@Bean
		public EntityManagerFactory entityManagerFactory () {
			return mock(EntityManagerFactory.class);
		}


	}

	@Test
	public void testPageIsDisplayed() throws Exception {
		tester.assertNoErrorMessage();
	}

	@Test
	public void testStructure() throws Exception {
		assertEquals(LoginPage.LoginForm.class, page.get(LoginPage.LOGIN_FORM_ID).getClass());
	}

	@Test
	public void testLoginAuthenticatesUser() throws Exception {
		String username = "username";
		String password = "password";
		when(authenticationService.authenticate(username, password, tester.getRequest().getRemoteAddr())).thenReturn(new User(1));
		fillAndSubmitLoginForm(username, password);
		verify(authenticationService, times(1)).authenticate(username, password, tester.getLastRequest().getRemoteAddr());
	}

	public void fillAndSubmitLoginForm(String username, String password) {
		FormTester formTester = tester.newFormTester(LoginPage.LOGIN_FORM_ID, false);
		formTester.setValue(LoginForm.USERNAME_ID, username);
		formTester.setValue(LoginForm.PASSWORD_ID, password);
		formTester.submit();
	}

	@Test
	public void testAuthenticatedUser() throws Exception {
		User authenticatedUser = new User(10);
		assertLogin(Application.MENU_PAGE, authenticatedUser);
	}

	private void assertLogin(String expectedUrl,	User authenticatedUser) {
		String username = "username";
		String password = "password";
		when(authenticationService.authenticate(username, password, tester.getLastRequest().getRemoteAddr())).thenReturn(authenticatedUser);
		fillAndSubmitLoginForm(username, password);
		assertEquals(authenticatedUser, userPreferences.getUser());
		assertEquals(expectedUrl, tester.getLastResponse().getRedirectLocation());
	}


}
