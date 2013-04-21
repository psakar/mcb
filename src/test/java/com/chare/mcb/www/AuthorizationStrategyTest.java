package com.chare.mcb.www;
import static com.chare.mcb.entity.Role.*;
import static org.junit.Assert.*;

import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.WebPage;
import org.junit.Before;
import org.junit.Test;

import com.chare.mcb.entity.Role;
import com.chare.mcb.entity.User;

public class AuthorizationStrategyTest extends WicketTestCase {

	private AuthorizationStrategy strategy;


	@Override
	@Before
	public void before() throws Exception {
		super.before();
		strategy = new AuthorizationStrategy(userPreferences);
	}

	@Test
	public void testIsActionAuthorized() {
		assertEquals(true, strategy.isActionAuthorized(null, null));
	}

	@Test(expected = RestartResponseAtInterceptPageException.class)
	public void testUserIsRedirectedToLoginIfNotAuthenticatedForSecuredPage() {
		strategy.isInstantiationAuthorized(SecuredPage.class);
	}

	@Test
	public void testIsInstantiationAuthorized() {


		assertIsInstantiationAuthorized(false, SecuredPage.class);
		assertIsInstantiationAuthorized(false, SecuredPage.class, APP_ADMIN_ROLE);
		assertIsInstantiationAuthorized(true, SecuredPage.class, EXPORT_POSTINGS_ROLE);

		assertIsInstantiationAuthorized(true, SecuredPageAutheticatedOnly.class);
		assertIsInstantiationAuthorized(true, SecuredPageAutheticatedOnly.class, APP_ADMIN_ROLE);
		assertIsInstantiationAuthorized(true, SecuredPageAutheticatedOnly.class, EXPORT_POSTINGS_ROLE);

		assertIsInstantiationAuthorized(false, SecuredByAnyOfPage.class, EXPORT_POSTINGS_ROLE);
		assertIsInstantiationAuthorized(true, SecuredByAnyOfPage.class, UPLOAD_STATEMENTS_ROLE);
		assertIsInstantiationAuthorized(true, SecuredByAnyOfPage.class, APP_ADMIN_ROLE);

		assertIsInstantiationAuthorized(false, SecuredByCombinationPage.class, EXPORT_POSTINGS_ROLE);
		assertIsInstantiationAuthorized(false, SecuredByCombinationPage.class, UPLOAD_STATEMENTS_ROLE);
		assertIsInstantiationAuthorized(true, SecuredByCombinationPage.class, EXPORT_POSTINGS_ROLE, UPLOAD_STATEMENTS_ROLE);
		assertIsInstantiationAuthorized(true, SecuredByCombinationPage.class, EXPORT_POSTINGS_ROLE, UPLOAD_STATEMENTS_ROLE, APP_ADMIN_ROLE);
	}

	private void authenticateUser(User user) {
		user.setId(1);
	}

	private void assertIsInstantiationAuthorized(boolean expected, Class<? extends Component> componentClass, Role ... roles) {
		setUserRoles(roles);
		if (expected)
			assertEquals(expected, strategy.isInstantiationAuthorized(componentClass));
		else
			try {
				strategy.isInstantiationAuthorized(componentClass);
				fail("Expected RestartResponseAtInterceptPageException exception");
			} catch (RestartResponseAtInterceptPageException e) {
			}
	}

	private void setUserRoles(Role ... roles) {
		User user = new User();
		user.addRoles(roles);
		authenticateUser(user);
		userPreferences.setUser(user);
	}



	@Test
	public void testIsSecured() throws Exception {
		assertTrue(AuthorizationStrategy.isSecured(SecuredPage.class));
		assertFalse(AuthorizationStrategy.isSecured(UnsecuredPage.class));
	}


	private static class UnsecuredPage extends WebPage {

	}

	@Secured(required = { }, requiredAnyOf = {  })
	private static class SecuredPageAutheticatedOnly extends WebPage { }

	@Secured(required = { Role.EXPORT_POSTINGS }, requiredAnyOf = {  })
	private static class SecuredPage extends WebPage { }
	@Secured(required = {  }, requiredAnyOf = { Role.UPLOAD_STATEMENTS, APP_ADMIN  })
	private static class SecuredByAnyOfPage extends WebPage {	}
	@Secured(required = {  Role.EXPORT_POSTINGS }, requiredAnyOf = { UPLOAD_STATEMENTS})
	private static class SecuredByCombinationPage extends WebPage {	}

}
