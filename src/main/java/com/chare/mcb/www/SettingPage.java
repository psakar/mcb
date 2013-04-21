package com.chare.mcb.www;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.chare.mcb.entity.Role;
import com.chare.mcb.entity.Setting;
import com.chare.mcb.repository.SettingRepository;
import com.chare.wicket.Button;
import com.chare.wicket.Label;
import com.chare.wicket.TextField;

@Secured(required = { Role.APP_ADMIN }, requiredAnyOf = {  })
public class SettingPage extends PanelPage {

	@SpringBean
	private SettingRepository repository;

	public SettingPage(PageParameters parameters) {
		super(parameters);
		add(createPanel(createModel(toIdInteger(parameters.get(ID_PARAM)))));
	}

	private IModel<Setting> createModel(Integer id) {
		return new ModelFactory<Integer, Setting>().createModel(id, repository, new Factory<Setting>(Setting.class));
	}

	private Component createPanel(IModel<Setting> model) {
		return new SettingPanel(PANEL_ID, model);
	}

	@Override
	protected IModel<String> createTitleModel() {
		return new AbstractReadOnlyModel<String> (){
			@Override
			public String getObject() {
				return new ResourceModel("setting.title").getObject() + " " + StringUtils.defaultString(((Setting)get(PANEL_ID).getDefaultModelObject()).code);
			}
		};
	}

	public static class SettingPanel extends org.apache.wicket.markup.html.panel.Panel {

		@SpringBean
		private SettingRepository repository;

		public SettingPanel(String id, IModel<Setting> model) {
			super(id, model);
			add(createForm(model));
		}

		protected Form<Setting> createForm(IModel<Setting> model) {
			final Form<Setting> form = new EntityForm<Setting>(FORM_ID, model, repository);
			form.add(new LabelMandatory("codeLabel", new ResourceModel("setting.code")));
			TextField<String> codeTextField = new TextField<String>("code", new PropertyModel<String>(model, "code"));
			form.add(codeTextField.setRequired(true));
			form.add(new FeedbackLabel("codeFeedback", new Model<String>(), codeTextField).setOutputMarkupId(true));

			form.add(new Label("valueLabel", new ResourceModel("setting.value")));
			form.add(new TextField<String>("value", new PropertyModel<String>(model, "value")).setConvertEmptyInputStringToNull(false));

			form.add(new LabelMandatory("typeLabel", new ResourceModel("setting.type")));
			TextField<String> typeTextField = new TextField<String>("type", new PropertyModel<String>(model, "type"));
			form.add(typeTextField);
			form.add(new FeedbackLabel("typeFeedback", new Model<String>(), typeTextField).setOutputMarkupId(true));

			form.add(new Label("description1Label", new ResourceModel("setting.description1")));
			form.add(new TextField<String>("description1", new PropertyModel<String>(model, "description1")).setConvertEmptyInputStringToNull(false));
			form.add(new Label("description2Label", new ResourceModel("setting.description2")));
			form.add(new TextField<String>("description2", new PropertyModel<String>(model, "description2")).setConvertEmptyInputStringToNull(false));
			form.add(new Label("description3Label", new ResourceModel("setting.description3")));
			form.add(new TextField<String>("description3", new PropertyModel<String>(model, "description3")).setConvertEmptyInputStringToNull(false));

			form.add(new Button("save", new ResourceModel("save")));

			form.visitChildren(FormComponent.class, new PropertyValidatorVisitor());

			return form;
		}

	}

}
