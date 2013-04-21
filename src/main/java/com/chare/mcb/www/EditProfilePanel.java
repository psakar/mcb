package com.chare.mcb.www;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.chare.mcb.Application;
import com.chare.mcb.entity.User;
import com.chare.mcb.repository.UserRepository;
import com.chare.mcb.service.Config;
import com.chare.mcb.service.UserPreferences;
import com.chare.mcb.www.ChoiceValueDropDownChoice.ChoiceIdModel;
import com.chare.wicket.Button;
import com.chare.wicket.Label;
import com.chare.wicket.TextData;
import com.chare.wicket.TextField;
import com.chare.wicket.panel.FormWithPanel;
import com.chare.wicket.panel.Panel;

public class EditProfilePanel extends Panel<User> {

	private static final Logger logger = Logger.getLogger(EditProfilePanel.class);

	static final String FORM_ID = "form";

	@SpringBean(name = Config.USER_PREFERENCES_SESSION_ID)
	private UserPreferences userPreferences;
	@SpringBean
	private UserRepository userRepository;


	public EditProfilePanel(String id, IModel<User> model) {
		super(id, model);
		initComponents();
	}

	private void initComponents() {
		add(createForm());
	}

	protected FormWithPanel<User> createForm() {
		final FormWithPanel<User> form = new FormWithPanel<User>(FORM_ID) {
			@Override
			protected void onSubmit() {
				try {
					User user = (User) getDefaultModelObject();
					//					if (validatorService.validate(user, responseProxy.getMessageResponse())) {
					user.resetUnsuccessfulCount();
					userRepository.persist(user);
					//					}
				} catch (Exception e) {
					logger.error("Save error " + e.getMessage(), e);
					error("Error"); // ({$1} {$2} {$3})_#2000;", new String[] { e.getMessage() });
				}
			};
		};
		@SuppressWarnings("unchecked")
		IModel<User> model = (IModel<User>) getDefaultModel();
		form.setDefaultModel(model);
		Panel<User> formPanel = form.getPanel();

		addUserProperties(model, formPanel);
		formPanel.add(new Button("save", new ResourceModel("save")));
		return form;
	}

	private void addUserProperties(IModel<User> model,
			Panel<User> formPanel) {
		User user = model.getObject();
		formPanel.add(new Label("username_", new ResourceModel("user.username")));
		formPanel.add(new TextData("username", new PropertyModel<String>(model, "username")));
		formPanel.add(new Label("surname_", new ResourceModel("user.surname")));
		formPanel.add(new TextData("surname", new PropertyModel<String>(model, "surname")));
		formPanel.add(new Label("name_", new ResourceModel("user.name")));
		formPanel.add(new TextData("name", new PropertyModel<String>(model, "name")));
		formPanel.add(new LabelMandatory("email_", new ResourceModel("user.email")));
		formPanel.add(new TextField<String>("email", new PropertyModel<String>(model, "email")).setSize(50).setMaxLength(50));
		formPanel.add(new Label("phone_", new ResourceModel("user.phone")));
		formPanel.add(new TextField<String>("phone", new PropertyModel<String>(model, "phone")));
		formPanel.add(new LabelMandatory("languageId_", new ResourceModel("user.languageId")));
		List<ChoiceValue> languageChoices = LanguageChoice.convertToChoices(Application.DEFAULT_LOCALES);
		formPanel.add(new LanguageChoice("languageId", new ChoiceIdModel(model, "languageId"), languageChoices));
		formPanel.add(new Label("unsuccessfulCount_", new ResourceModel("user.unsuccessfulCount")));
		formPanel.add(new TextData("unsuccessfulCount", String.valueOf(user.getUnsuccessfulCount())));
		formPanel.add(new Label("lastAccess_", new ResourceModel("user.lastAccess")));
		formPanel.add(new TextData("lastAccess", formatDateTime(user.getLastAccess())));
	}

	private String formatDateTime(Date date) {
		if (date == null)
			return "";
		return userPreferences.getUser().getDateTimeFormat().format(date);
	}

	//FIXME unsuccessfulCount - reset to 0 after successful login
}