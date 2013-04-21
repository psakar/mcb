package com.chare.mcb.www;

import org.apache.wicket.model.ResourceModel;
import org.junit.Test;

public class MenuPanelTest extends WicketTestCase {

	@Test
	public void testMenuStructure() throws Exception {
		authenticateUser();
		assertContainsLabels(createMenuPanel());
	}

	private void assertContainsLabels(MenuPanel menuPanel) {
		tester.assertContains(getResource("menu.profile"));
	}

	private String getResource(String key) {
		return new ResourceModel(key).getObject().toString();
	}

	private MenuPanel createMenuPanel() {
		MenuPanel menuPanel = new MenuPanel(COMPONENT_ID);
		tester.startComponentInPage(menuPanel);
		return menuPanel;
	}
}
