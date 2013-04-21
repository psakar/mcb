package com.chare.mcb.www;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import com.chare.mcb.entity.UserItem;
import com.chare.mcb.repository.UserItemRepository;
import com.chare.mcb.repository.UserRepository;
import com.chare.mcb.service.UserPreferences;
import com.chare.mcb.www.UserList.UserListPanel;
import com.chare.repository.Restriction;

public class UserListPanelTest extends WicketTestCase {

	private UserListPanel userPanel;

	@Override
	@Before
	public void before() throws Exception {
		super.before();
		userPanel = new UserListPanel("id");
		tester.startComponentInPage(userPanel);
	}

	@Test
	public void testPanelContents() throws Exception {
		assertNotNull(userPanel.get(AbstractListPanel.LIST_ID));
		assertNotNull(userPanel.get(AbstractListPanel.NAVIGATOR_ID));
		tester.assertNoErrorMessage();
	}

	@Test
	public void testListContents() throws Exception {
		tester.assertContains(TestConfig.user.username);
		tester.assertContains(TestConfig.user.name);
		tester.assertContains(TestConfig.user.surname);
		tester.assertContains(String.valueOf(TestConfig.user.getUnsuccessfulCount()));
		tester.assertContains("No");
		tester.assertNoErrorMessage();
	}

	@Test
	@Ignore
	// FIXME
	public void testViewLink() throws Exception {
		tester.clickLink("panel:component:component:" + AbstractListPanel.LIST_ID + ":rows:1:view");
		tester.assertRenderedPage(UserPage.class);
	}

	@Override
	protected Class<?> getCustomConfig() {
		return TestConfig.class;
	}

	@Configuration
	static class TestConfig {
		public static UserItem user;


		@Bean
		public UserRepository userRepository() {
			return mock(UserRepository.class);
		}

		@SuppressWarnings("unchecked")
		@Bean
		public UserItemRepository userItemRepository() {
			user = new UserItem();
			user.setId(5);
			user.username = "username";
			user.name = "name";
			user.surname = "surname";
			user.enabled = false;
			List<UserItem> list = Arrays.asList(user);

			Class<UserItemRepository> repositoryClass = UserItemRepository.class;
			UserItemRepository repository = mock(repositoryClass);
			when(repository.find(anyInt(), anyInt(), any(List.class), any(List.class))).thenReturn(list);
			when(repository.getCount(anyListOf(Restriction.class))).thenReturn(1);

			return repository;
		}

		@Bean
		@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
		public UserPreferences userPreferences() {
			return new UserPreferences();
		}

	}

}
