package com.chare.mcb.www;

import static org.mockito.Mockito.*;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.Bean;

import com.chare.mcb.entity.Role;
import com.chare.mcb.repository.SettingRepository;
import com.chare.mcb.www.SettingPage.SettingPanel;
import com.chare.validation.ValidatorService;

public class SettingPageTest extends WicketTestCase {

	private SettingPage page;

	@Override
	@Before
	public void before() throws Exception {
		super.before();
		authenticateUser();
		user.addRole(Role.APP_ADMIN_ROLE);
		startPage();
	}

	private void startPage() {
		page = tester.startPage(SettingPage.class, new PageParameters());
	}

	@Test
	public void testPageRendered() throws Exception {
		assertPageRenderedWithNoErrorOrInfoMessages(page.getClass());
	}

	@Test
	public void testContainsChildren() throws Exception {
		assertComponentIs(SettingPanel.class, SettingPage.PANEL_ID);
	}

	@Test
	public void testIsSecured() throws Exception {
		assertPageIsSecured(SettingPage.class);
	}


	@Override
	protected Class<?> getCustomConfig() {
		return TestConfig.class;
	}

	static class TestConfig extends PageConfig {
		@Bean
		public SettingRepository rootAccountFullRepository() {
			return mock(SettingRepository.class);
		}
		@Bean
		public ValidatorService validatorService() {
			return mock(ValidatorService.class);
		}
	}

}