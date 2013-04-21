package com.chare.mcb.www;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.apache.wicket.model.Model;
import org.junit.Test;
import org.springframework.context.annotation.Bean;

import com.chare.mcb.entity.Setting;
import com.chare.mcb.repository.SettingRepository;
import com.chare.mcb.www.SettingPage.SettingPanel;
import com.chare.wicket.TextField;

public class SettingPanelTest extends WicketTestCase {

	private Setting setting;

	@Test
	public void testRendering() throws Exception {
		tester.startComponentInPage(createPanel());
		tester.assertNoErrorMessage();
		assertDisplaysEntity();
	}

	private SettingPanel createPanel() {
		setting = createSetting();
		return new SettingPanel("id", new Model<Setting>(setting));
	}

	private Setting createSetting() {
		return new Setting(5, "code", "desc1", "desc2", "desc3", 3, "value");
	}

	@Test
	public void testContainsChildren() throws Exception {
		assertEquals(TextField.class, createPanel().get(getPath(PanelPage.FORM_ID, "code")).getClass());
	}


	private void assertDisplaysEntity() {
		tester.assertContains(setting.code);
		tester.assertContains(setting.description1);
		tester.assertContains(setting.description2);
		tester.assertContains(setting.description3);
		tester.assertContains(setting.value);
		tester.assertContains(String.valueOf(setting.type));
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
	}

}
