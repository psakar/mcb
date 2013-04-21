package com.chare.mcb.www;

import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.chare.mcb.entity.User;

public class EditProfilePage extends PanelPage {

	public EditProfilePage(PageParameters parameters) {
		super(parameters);
		add(new EditProfilePanel(PANEL_ID, new Model<User>(getUser())));
	}

	@Override
	protected String getTitleCode() {
		return "editProfile.title";
	}
}
