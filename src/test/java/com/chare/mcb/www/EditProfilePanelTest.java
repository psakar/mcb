package com.chare.mcb.www;

import static org.junit.Assert.*;

import org.apache.wicket.Component;
import org.apache.wicket.model.Model;
import org.junit.Test;

import com.chare.mcb.entity.User;
import com.chare.wicket.TextData;
import com.chare.wicket.panel.FormWithPanel;

public class EditProfilePanelTest extends WicketTestCase {

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
	public void testRendering() throws Exception {
		EditProfilePanel panel = createPanel();

		tester.startComponentInPage(panel);

		tester.assertNoErrorMessage();
		assertContainsChildren(panel);
		assertDisplaysEntity();
	}

	private void assertDisplaysEntity() {
		tester.assertContains(user.username);
		tester.assertContains(user.name);
		tester.assertContains(user.surname);
		tester.assertContains(user.email);
		tester.assertContains(user.phone);
	}

	protected EditProfilePanel createPanel() {
		return new EditProfilePanel("id", new Model<User>(user));
	}

	private void assertContainsChildren(EditProfilePanel panel) throws Exception {
		assertEquals(TextData.class, getComponentClass(panel, "username"));
		assertEquals(LanguageChoice.class, getComponentClass(panel, "languageId"));
		assertEquals(TextData.class, getComponentClass(panel, "unsuccessfulCount"));
		assertEquals(TextData.class, getComponentClass(panel, "lastAccess"));
	}

	private Class<? extends Component> getComponentClass(Component panel, String componentId) {
		return panel.get(getPath(EditProfilePanel.FORM_ID, FormWithPanel.FORM_PANEL_NAME, componentId)).getClass();
	}

	@Override
	protected Class<?> getCustomConfig() {
		return EditProfilePageTest.PageConfig.class;
	}
}
