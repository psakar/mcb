package com.chare.mcb.www;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.apache.wicket.util.tester.FormTester;
import org.junit.Test;
import org.springframework.context.annotation.Bean;

import com.chare.mcb.entity.Role;
import com.chare.mcb.repository.UserRepository;
import com.chare.wicket.panel.Panel;


public class ChangePasswordPageTest extends WicketTestCase {

	private static final boolean DISPLAY_OLD_PASSWORD = true;
	private static final boolean DO_NOT_DISPLAY_OLD_PASSWORD = false;

	@Test
	public void testForm() {
		authenticateUser();
		user.addRoles(Role.UPLOAD_STATEMENTS_ROLE);

		tester.startPage(new ChangePasswordPage());

		tester.assertRenderedPage(ChangePasswordPage.class);
		tester.assertNoErrorMessage();
		tester.assertComponent("panel", Panel.class);
		tester.assertComponent("panel:changePassword", ChangePasswordForm.class);

		FormTester formTester = tester.newFormTester("panel:changePassword");
		formTester.submit();

		tester.assertRenderedPage(ChangePasswordPage.class);
	}



	@Test
	public void testPageIsSecured() {
		assertPageIsSecured(ChangePasswordPage.class);
	}

	@Test
	public void testChangePassworForm() {
		ChangePasswordForm form = createChangePasswordForm(DISPLAY_OLD_PASSWORD);

		assertFormComponents(form, DISPLAY_OLD_PASSWORD);
	}


	@Test
	public void testChangePassworFormAdmin() {
		ChangePasswordForm form = createChangePasswordForm(DO_NOT_DISPLAY_OLD_PASSWORD);

		assertFormComponents(form, DO_NOT_DISPLAY_OLD_PASSWORD);
	}

	private ChangePasswordForm createChangePasswordForm(boolean displayOldPassword) {
		Panel<Void> panelWithForm = new Panel<Void>("id");
		ChangePasswordForm form = new ChangePasswordForm("id", displayOldPassword);
		panelWithForm.add(form);

		tester.startComponentInPage(panelWithForm);

		return form;
	}

	private void assertFormComponents(ChangePasswordForm form, boolean displayOldPassword) {
		if (displayOldPassword) {
			assertNotNull(form.getPanel().get("oldPasswordLabel"));
			assertNotNull(form.getPanel().get("oldPassword"));
		} else {
			assertNull(form.getPanel().get("oldPasswordLabel"));
			assertNull(form.getPanel().get("oldPassword"));
		}
		assertNotNull(form.getPanel().get("newPasswordLabel"));
		assertNotNull(form.getPanel().get("newPassword"));
		assertNotNull(form.getPanel().get("verifiedPasswordLabel"));
		assertNotNull(form.getPanel().get("verifiedPassword"));
		assertNotNull(form.getPanel().get("change"));
		assertNotNull(form.getPanel().get("cancel"));
	}

	@Override
	protected Class<?> getCustomConfig() {
		return TestConfig.class;
	}

	static class TestConfig extends PageConfig {
		@Bean
		public UserRepository userRepository() {
			return mock(UserRepository.class);
		}
	}

}
