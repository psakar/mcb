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

import com.chare.mcb.entity.Setting;
import com.chare.mcb.repository.SettingRepository;
import com.chare.mcb.repository.UserRepository;
import com.chare.mcb.service.UserPreferences;
import com.chare.mcb.www.SettingList.SettingListPanel;
import com.chare.repository.Restriction;

public class SettingListPanelTest extends WicketTestCase {

	private SettingListPanel settingPanel;

	@Override
	@Before
	public void before() throws Exception {
		super.before();
		settingPanel = new SettingListPanel("id");
		tester.startComponentInPage(settingPanel);
	}

	@Test
	public void testPanelContents() throws Exception {
		assertNotNull(settingPanel.get(AbstractListPanel.LIST_ID));
		assertNotNull(settingPanel.get(AbstractListPanel.NAVIGATOR_ID));
		tester.assertNoErrorMessage();
	}

	@Test
	public void testListContents() throws Exception {
		tester.assertContains(TestConfig.setting.code);
		tester.assertContains(TestConfig.setting.description1);
		tester.assertContains(TestConfig.setting.description2);
		tester.assertContains(TestConfig.setting.description3);
		tester.assertContains(TestConfig.setting.value);
		tester.assertContains(String.valueOf(TestConfig.setting.type));
		tester.assertNoErrorMessage();
	}

	@Test
	@Ignore
	// FIXME
	public void testViewLink() throws Exception {
		tester.clickLink("panel:component:component:" + AbstractListPanel.LIST_ID + ":rows:1:view");
		tester.assertRenderedPage(SettingPage.class);
	}

	@Test
	public void testIsSecured() throws Exception {
		assertPageIsSecured(SettingList.class);
	}

	@Override
	protected Class<?> getCustomConfig() {
		return TestConfig.class;
	}

	@Configuration
	static class TestConfig {
		public static Setting setting;

		@SuppressWarnings("unchecked")
		@Bean
		public SettingRepository settingDataProvider() {
			setting = new Setting(5, "code", "desc1", "desc2", "desc3", 3, "value");

			List<Setting> list = Arrays.asList(setting);
			SettingRepository repository = mock(SettingRepository.class);
			when(repository.find(anyInt(), anyInt(), any(List.class), any(List.class))).thenReturn(list);
			when(repository.findById(anyInt())).thenReturn(setting);
			when(repository.getCount(anyListOf(Restriction.class))).thenReturn(1);

			return repository;
		}

		@Bean
		public UserRepository userRepository() {
			return mock(UserRepository.class);
		}
		@Bean
		@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
		public UserPreferences userPreferences() {
			return new UserPreferences();
		}
	}

}
