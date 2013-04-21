package com.chare.mcb.www;

import static org.mockito.Mockito.*;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.chare.mcb.entity.Role;
import com.chare.mcb.entity.User;
import com.chare.mcb.repository.UserRepository;
import com.chare.mcb.www.UserPage.RoleListPanel;
import com.chare.mcb.www.UserPage.UserPanel;

public class UserPageTest extends WicketTestCase {

	private UserPage page;

	@Override
	@Before
	public void before() throws Exception {
		super.before();
		authenticateUser();
		user.addRole(Role.USER_ADMIN_ROLE);
	}

	private void startPage() {
		page = tester.startPage(UserPage.class);
	}

	@Test
	public void testPageRendered() throws Exception {
		startPage();
		assertPageRenderedWithNoErrorOrInfoMessages(page.getClass());
	}

	@Test
	@Ignore
	// FIXME
	public void testPersistingNewUserShowsRolePanel() throws Exception {
		startPage();
		FormTester formTester = tester.newFormTester(getPath(UserPage.PANEL_ID, UserPage.USER_PANEL_ID, PanelPage.FORM_ID));
		formTester.submit();
		tester.assertRenderedPage(UserPage.class);
		tester.assertComponent(getPath(UserPage.PANEL_ID, UserPage.ROLE_LIST_PANEL_ID), RoleListPanel.class);
	}

	@Test
	@Ignore
	// FIXME
	public void testPersistingPersistentUserRedirectsToUserList() throws Exception {
		PageParameters parameters = new PageParameters();
		parameters.set(PanelPage.ID_PARAM, "1");
		tester.startPage(UserPage.class, parameters);
		FormTester formTester = tester.newFormTester(getPath(UserPage.PANEL_ID, UserPage.USER_PANEL_ID, PanelPage.FORM_ID));
		formTester.submit();
		tester.assertRenderedPage(UserList.class);
	}

	@Test
	public void testContainsChildren() throws Exception {
		startPage();
		assertComponentIs(UserPanel.class, UserPage.PANEL_ID);
	}

	@Override
	protected Class<?> getCustomConfig() {
		return TestConfig.class;
	}

	@Configuration
	static class TestConfig {

		@Bean
		public UserRepository userRepository() {
			UserRepository userRepository = mock(UserRepository.class);
			when(userRepository.findById(1)).thenReturn(new User(1, "username"));
			return userRepository;
		}
	}

}