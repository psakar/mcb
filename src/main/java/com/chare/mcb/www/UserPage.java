package com.chare.mcb.www;


import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import com.chare.core.LanguageIndex;
import com.chare.mcb.Application;
import com.chare.mcb.entity.Role;
import com.chare.mcb.entity.User;
import com.chare.mcb.repository.UserRepository;
import com.chare.mcb.service.Config;
import com.chare.mcb.service.UserPreferences;
import com.chare.mcb.www.ChoiceValueDropDownChoice.ChoiceIdModel;
import com.chare.wicket.Button;
import com.chare.wicket.CheckBox;
import com.chare.wicket.Label;
import com.chare.wicket.TextData;
import com.chare.wicket.TextField;

@Secured(required = { Role.USER_ADMIN }, requiredAnyOf = {  })
public class UserPage extends PanelPage {

	public static final String USER_PANEL_ID = "userPanel";
	public static final String ROLE_LIST_PANEL_ID = "roleListPanel";

	@SpringBean
	private UserRepository repository;

	public UserPage(PageParameters parameters) {
		super(parameters);
		add(createPanel(createModel(toIdInteger(parameters.get(ID_PARAM)))));
	}

	private IModel<User> createModel(Integer id) {
		return new ModelFactory<Integer, User>().createModel(id, repository, new Factory<User>(User.class));
	}

	private Component createPanel(IModel<User> model) {
		return new UserPanel(PANEL_ID, model);
	}

	@Override
	protected IModel<String> createTitleModel() {
		return new AbstractReadOnlyModel<String> (){
			@Override
			public String getObject() {
				return new ResourceModel("user.title").getObject() + " " + StringUtils.defaultString(((User)get(PANEL_ID).getDefaultModelObject()).username);
			}
		};
	}


	static class UserPanel extends org.apache.wicket.markup.html.panel.Panel {

		@SpringBean(name = Config.USER_PREFERENCES_SESSION_ID)
		private UserPreferences userPreferences;
		@SpringBean
		private UserRepository userRepository;

		public UserPanel(String id, IModel<User> model) {
			super(id, model);
			add(createForm(model));
		}

		protected Form<User> createForm(IModel<User> model) {
			final Form<User> form = new EntityForm<User>(PanelPage.FORM_ID, model, userRepository) {
				@Override
				protected User persist(User entity) {
					entity.resetUnsuccessfulCount();
					return super.persist(entity);
				}
			};

			User user = model.getObject();

			form.add(new LabelMandatory("username_", new ResourceModel("user.username")));
			TextField<String> usernameTextField = new TextField<String>("username", new PropertyModel<String>(model, "username"));
			usernameTextField.setRequired(true);
			usernameTextField.setMaxLength(20);
			usernameTextField.add(StringValidator.lengthBetween(6, 20));
			form.add(usernameTextField);
			form.add(new FeedbackLabel("usernameFeedback", usernameTextField));

			form.add(new LabelMandatory("surname_", new ResourceModel("user.surname")));
			TextField<String> surnameTextField = new TextField<String>("surname", new PropertyModel<String>(model, "surname"));
			form.add(surnameTextField.setRequired(true));
			form.add(new FeedbackLabel("surnameFeedback", surnameTextField));

			form.add(new LabelMandatory("name_", new ResourceModel("user.name")));
			TextField<String> nameTextField = new TextField<String>("name", new PropertyModel<String>(model, "name"));
			form.add(nameTextField.setRequired(true));
			form.add(new FeedbackLabel("nameFeedback", nameTextField));

			form.add(new LabelMandatory("email_", new ResourceModel("user.email")));
			TextField<String> emailTextField = new TextField<String>("email", new PropertyModel<String>(model, "email"));
			form.add(emailTextField.setSize(50).setMaxLength(50).setRequired(true));
			form.add(new FeedbackLabel("emailFeedback", emailTextField));

			form.add(new Label("phone_", new ResourceModel("user.phone")));
			form.add(new TextField<String>("phone", new PropertyModel<String>(model, "phone")));

			form.add(new LabelMandatory("languageId_", new ResourceModel("user.languageId")));
			List<ChoiceValue> languageChoices = LanguageChoice.convertToChoices(Application.DEFAULT_LOCALES);
			form.add(new LanguageChoice("languageId", new ChoiceIdModel(model, "languageId"), languageChoices));

			form.add(new Label("unsuccessfulCount_", new ResourceModel("user.unsuccessfulCount")));
			form.add(new TextData("unsuccessfulCount", String.valueOf(user.getUnsuccessfulCount())));

			form.add(new Label("lastAccess_", new ResourceModel("user.lastAccess")));
			form.add(new TextData("lastAccess", formatDateTime(user.getLastAccess())));

			form.add(new Label("enabled_", new ResourceModel("user.enabled")));
			form.add(new CheckBox("enabled", new PropertyModel<Boolean>(model, "enabled")));

			form.add(new Button("save", new ResourceModel("save")));
			form.visitChildren(FormComponent.class, new PropertyValidatorVisitor());

			form.add(new RoleListPanel(ROLE_LIST_PANEL_ID, model));

			return form;
		}

		protected void afterPersist(boolean wasInserted, User user) {
			if (wasInserted) {
				PageParameters pageParameters = new PageParameters();
				pageParameters.set(ID_PARAM, user.getId());
				setResponsePage(UserPage.class, pageParameters);
			} else {
				setResponsePage(UserList.class);
			}
		}

		private String formatDateTime(Date date) {
			return date == null ? "" : userPreferences.getUser().getDateTimeFormat().format(date);
		}

		//FIXME saki + Martin - unsuccessfulCount - reset to 0 after successful login
	}

	static class RoleListPanel extends org.apache.wicket.markup.html.panel.Panel {

		static final String FORM_ID = "form";
		static final String ROWS_ID = "rows";

		private Role role;

		public RoleListPanel(String id, IModel<User> model) {
			super(id, model);
			Form<Void> form = new Form<Void>(FORM_ID);
			form.add(createDataView(model));
			form.add(createRoleChoice(model.getObject()));
			form.add(createAddRoleButton());
			add(form);
		}

		@Override
		public boolean isVisible() {
			return super.isVisible() && ((User)getDefaultModelObject()).isPersistent();
		}

		private AddRoleButton createAddRoleButton() {
			return new AddRoleButton("add");
		}

		class AddRoleButton extends Button {
			public AddRoleButton(String id) {
				super(id, new ResourceModel("add"));
			}
			@Override
			public void onSubmit() {
				User user = (User)RoleListPanel.this.getDefaultModelObject();
				if (role == null) {
					error(new ResourceModel("pleaseSelectRole", "Please select role.").getObject());
					return;
				}
				if (!user.hasRoles(role.code))
					user.addRole(role);
			}
		}

		static class RoleRenderer implements IChoiceRenderer<Role> {
			private final LanguageIndex languageIndex;
			public RoleRenderer(LanguageIndex languageIndex) {
				this.languageIndex = languageIndex;
			}
			@Override
			public Object getDisplayValue(Role object) {
				return object.getDescription(languageIndex);
			}
			@Override
			public String getIdValue(Role object, int index) {
				return String.valueOf(object.getId());
			}
		}

		private DropDownChoice<Role> createRoleChoice(LanguageIndex languageIndex) {
			return new DropDownChoice<Role>("role", new PropertyModel<Role>(this, "role"), Role.ROLES, new RoleRenderer(languageIndex));
		}


		private DataView<Role> createDataView(final IModel<User> model) {
			final User user = model.getObject();
			IDataProvider<Role> dataProvider = new ListDataProvider<Role>(new PropertyModel<List<Role>>(model, "roles"));
			DataView<Role> dataView = new DataView<Role>(ROWS_ID, dataProvider) {
				@Override
				protected void populateItem(Item<Role> item) {
					final Role role = item.getModelObject();
					item.add(new Label("code", role.code));
					item.add(new Label("description", role.getDescription(user)));
					item.add(new Link<Void>("remove") {
						@Override
						public void onClick() {
							model.getObject().removeRole(role);
						}
					});
				}
			};
			return dataView;
		}
	}
}