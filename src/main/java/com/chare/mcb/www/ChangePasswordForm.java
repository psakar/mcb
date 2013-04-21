package com.chare.mcb.www;

import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.value.ValueMap;

import com.chare.wicket.Button;
import com.chare.wicket.Label;
import com.chare.wicket.PasswordTextField;
import com.chare.wicket.panel.FormWithPanel;
import com.chare.wicket.panel.Panel;

public class ChangePasswordForm extends FormWithPanel<Void> {
	private static final String OLD_PASSWORD = "oldPassword";
	protected static final String VERIFIED_PASSWORD = "verifiedPassword";
	protected static final String NEW_PASSWORD = "newPassword";

	final ValueMap properties = new ValueMap();

	public ChangePasswordForm(String id, boolean displayOldPassword) {
		super(id);
		Panel<Void> panel = getPanel();
		if (displayOldPassword) {
			panel.add(new Label("oldPasswordLabel", new ResourceModel("oldPassword")));
			panel.add(new PasswordTextField(OLD_PASSWORD, new PropertyModel<String>(properties, OLD_PASSWORD)).setRequired(true));
		}
		panel.add(new Label("newPasswordLabel", new ResourceModel("newPassword")));
		panel.add(new PasswordTextField(NEW_PASSWORD, new PropertyModel<String>(properties, NEW_PASSWORD)).setRequired(true));
		panel.add(new Label("verifiedPasswordLabel", new ResourceModel("verifiedPassword")));
		panel.add(new PasswordTextField(VERIFIED_PASSWORD, new PropertyModel<String>(properties, VERIFIED_PASSWORD)).setRequired(true));
		panel.add(new Button("change", new ResourceModel("changePassword")) {
			@Override
			public void onSubmit() {
				ChangePasswordForm.this.onChangePassword();
			}
		});
		panel.add(new Button("cancel", new ResourceModel("cancel")) {
			@Override
			public void onSubmit() {
				ChangePasswordForm.this.onCancel();
			}
		});
	}

	protected void onChangePassword() {
		String oldPassword = properties.getString(OLD_PASSWORD);
		String newPassword = properties.getString(NEW_PASSWORD);
		String verifiedPassword = properties.getString(VERIFIED_PASSWORD);
		if (newPassword.compareTo(verifiedPassword) != 0) {
			onPasswordNotVerified();
			return;
		}
		onChangePassword(oldPassword, newPassword);
	}

	protected void onPasswordNotVerified() {
		error(new ResourceModel("passwordNotVerified").getObject().toString());
	}

	protected void onChangePassword(String oldPassword, String newPassword) {
	}

	public void onCancel() {
		properties.clear();
	}
}
