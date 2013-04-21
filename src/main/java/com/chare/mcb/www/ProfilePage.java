package com.chare.mcb.www;

import java.util.List;

import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.chare.core.LanguageIndex;
import com.chare.mcb.entity.Role;
import com.chare.wicket.Label;
import com.chare.wicket.TextData;
import com.chare.wicket.panel.Panel;
import com.chare.wicket.panel.TableLayoutManager;

@Secured(required = {  }, requiredAnyOf = {  })
public class ProfilePage extends PanelPage {

	public ProfilePage() {
		super(new PageParameters());
		add(createPanel());
	}

	private Panel<Void> createPanel() {
		Panel<Void> panel = createDefaultPanel();
		panel.add(new Label("sectiontitle", new ResourceModel("profile.info")));
		Panel<Void> profile = new Panel<Void>("profile", null, new TableLayoutManager(2).setBorder(1).setColumnWidth(150, 1, -1));

		profile.add(new Label("_username", new ResourceModel("user.username")));
		profile.add(new TextData("username", new PropertyModel<String>(this, "user.username")));
		profile.add(new Label("_name", new ResourceModel("user.name")));
		profile.add(new TextData("name", new PropertyModel<String>(this, "user.name")));
		profile.add(new Label("_surName", new ResourceModel("user.surname")));
		profile.add(new TextData("surname", new PropertyModel<String>(this, "user.surname")));
		profile.add(new Label("_email", new ResourceModel("user.email")));
		profile.add(new TextData("email", new PropertyModel<String>(this, "user.email")));
		profile.add(new Label("_phone", new ResourceModel("user.phone")));
		profile.add(new TextData("phone", new PropertyModel<String>(this, "user.phone")));
		profile.add(new Label("_lang", new ResourceModel("user.localeCode")));
		profile.add(new TextData("lang", new PropertyModel<String>(this, "user.getLocaleCode()")));
		profile.add(new Label("_lastAccess", new ResourceModel("user.lastAccess")));
		profile.add(new TextData("lastAccess", new PropertyModel<String>/*FIXMEDateTime*/(this, "user.lastAccess")));//, userPreferences)));
		profile.add(new Label("_roles", new ResourceModel("user.roles")));
		profile.add(new TextData("roles", new RolesModel(new PropertyModel<List<Role>>(this, "user.roles"), getUser())));

		panel.add(profile);
		return panel;
	}

	@Override
	protected String getTitleCode() {
		return "profile.title";
	}
	static class RolesModel extends Model<String> {
		private final PropertyModel<List<Role>> model;
		private final LanguageIndex languageIndex;

		public RolesModel(PropertyModel<List<Role>> model, LanguageIndex languageIndex) {
			this.model = model;
			this.languageIndex = languageIndex;
		}

		@Override
		public String getObject() {
			List<Role> roles = model.getObject();
			String list = "";
			for (Role role : roles) {
				list += ", " + role.getDescription(languageIndex);
			}
			return list.length() > 0 ? list.substring(2) : list;
		}
	}

}
