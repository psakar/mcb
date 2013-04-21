package com.chare.mcb.www;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.apache.wicket.Component;
import org.apache.wicket.model.Model;
import org.junit.Test;
import org.springframework.context.annotation.Bean;

import com.chare.mcb.entity.User;
import com.chare.mcb.repository.UserRepository;
import com.chare.mcb.www.UserPage.UserPanel;
import com.chare.wicket.CheckBox;
import com.chare.wicket.TextData;
import com.chare.wicket.TextField;

public class UserPanelTest extends WicketTestCase {


	@Test
	public void testRendering() throws Exception {
		User displayedEntity = createDisplayedEntity();
		tester.startComponentInPage(createPanel(displayedEntity));
		tester.assertNoErrorMessage();
		assertDisplaysEntity(displayedEntity);
	}

	private void assertDisplaysEntity(User user) {
		tester.assertContains(user.getUsername());
		tester.assertContains(user.name);
		tester.assertContains(user.surname);
		tester.assertContains(user.email);
		tester.assertContains(user.phone);
	}

	private UserPanel createPanel(User entity) {
		return new UserPanel("id", new Model<User>(entity));
	}

	private User createDisplayedEntity() {
		User user = new User(5, "username");
		user.name = "name";
		user.surname = "surname";
		user.setUnsuccessfulCount(3);
		user.enabled = false;
		user.email = "email";
		user.phone = "phone";
		return user;
	}

	@Test
	public void testContainsChildren() throws Exception {
		UserPanel panel = createPanel(createDisplayedEntity());
		tester.startComponentInPage(panel);
		tester.assertNoErrorMessage();
		assertEquals(TextField.class, getComponentClass(panel, "username"));
		assertEquals(LanguageChoice.class, getComponentClass(panel, "languageId"));
		assertEquals(TextData.class, getComponentClass(panel, "unsuccessfulCount"));
		assertEquals(TextData.class, getComponentClass(panel, "lastAccess"));
		assertEquals(CheckBox.class, getComponentClass(panel, "enabled"));
	}

	private Class<? extends Component> getComponentClass(Component panel, String componentId) {
		return panel.get(getPath(PanelPage.FORM_ID, componentId)).getClass();
	}


	@Override
	protected Class<?> getCustomConfig() {
		return TestConfig.class;
	}

	static class TestConfig extends PageConfig {
		@Bean
		public UserRepository rootAccountFullRepository() {
			return mock(UserRepository.class);
		}

	}

}
