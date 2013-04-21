package com.chare.mcb.www;

import org.apache.wicket.Component;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.chare.core.Utils;
import com.chare.mcb.entity.User;
import com.chare.mcb.repository.UserRepository;
import com.chare.wicket.Label;
import com.chare.wicket.panel.Panel;

@Secured(required = { }, requiredAnyOf = { })
public class ChangePasswordPage extends PanelPage {

	@SpringBean
	private UserRepository userRepository;

	public ChangePasswordPage() {
		super(new PageParameters());
		add(createPanel());
	}

	private Panel<Void> createPanel() {
		Panel<Void> panel = createDefaultPanel();
		panel.add(new Label("sectiontitle", ""));
		panel.add(createChangePasswordForm());
		return panel;
	}

	private Component createChangePasswordForm() {
		ChangePasswordForm form = new ChangePasswordForm("changePassword", true) {
			@Override
			protected void onChangePassword(String oldPassword,
					String newPassword) {
				User user = getUser();

				if (oldPassword != null) {
					if (!Utils.comparePasswords(user.getPassword(), Utils.encodePassword(oldPassword.getBytes()))) {
						error(new ResourceModel("wrongPassword").getObject().toString());
						return;
					}
				}
				user.setPassword(newPassword);
				user.setUnsuccessfulCount(0);
				try {
					userRepository.persist(user);
					info(new ResourceModel("passwordChanged").getObject().toString());
				}
				catch (Exception e) {
					log.error("Change password error " + e.getMessage(), e);
					error("Failed to change password.", null);
				}
			}
		};
		form.getPanel().get("cancel").setVisible(false);
		return form;
	}

	@Override
	protected String getTitleCode() {
		return "changePassword.title";
	}
}
