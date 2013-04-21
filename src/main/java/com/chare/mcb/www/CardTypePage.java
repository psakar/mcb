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

import com.chare.mcb.entity.CardType;
import com.chare.mcb.entity.Role;
import com.chare.mcb.repository.CardTypeRepository;
import com.chare.wicket.Button;
import com.chare.wicket.TextField;

@Secured(required = { Role.APP_ADMIN }, requiredAnyOf = {  })
public class CardTypePage extends PanelPage {

	@SpringBean
	private CardTypeRepository repository;

	public CardTypePage(PageParameters parameters) {
		super(parameters);
		add(createPanel(createModel(toIdString(parameters.get(ID_PARAM)))));
	}

	private IModel<CardType> createModel(String id) {
		return new ModelFactory<String, CardType>().createModel(id, repository, new Factory<CardType>(CardType.class));
	}

	private Component createPanel(IModel<CardType> model) {
		return new CardTypePanel(PANEL_ID, model);
	}

	@Override
	protected IModel<String> createTitleModel() {
		return new AbstractReadOnlyModel<String> (){
			@Override
			public String getObject() {
				return new ResourceModel("cardType.title").getObject() + " " + StringUtils.defaultString(((CardType)get(PANEL_ID).getDefaultModelObject()).code);
			}
		};
	}

	public static class CardTypePanel extends org.apache.wicket.markup.html.panel.Panel {

		@SpringBean
		private CardTypeRepository repository;

		public CardTypePanel(String id, IModel<CardType> model) {
			super(id, model);
			add(createForm(model));
		}

		protected Form<CardType> createForm(IModel<CardType> model) {
			final Form<CardType> form = new EntityForm<CardType>(FORM_ID, model, repository);
			form.add(new LabelMandatory("codeLabel", new ResourceModel("cardType.code")));
			TextField<String> codeTextField = new TextField<String>("code", new PropertyModel<String>(model, "code"));
			form.add(codeTextField.setRequired(true));
			form.add(new FeedbackLabel("codeFeedback", new Model<String>(), codeTextField).setOutputMarkupId(true));



			form.add(new LabelMandatory("description1Label", new ResourceModel("cardType.description1")));
			FormComponent<String> description1Field = new TextField<String>("description1", new PropertyModel<String>(model, "description.description1")).setRequired(true);
			form.add(description1Field);
			form.add(new FeedbackLabel("description1Feedback", new Model<String>(), description1Field).setOutputMarkupId(true));

			form.add(new LabelMandatory("description2Label", new ResourceModel("cardType.description2")));
			FormComponent<String> description2Field = new TextField<String>("description2", new PropertyModel<String>(model, "description.description2")).setRequired(true);
			form.add(description2Field);
			form.add(new FeedbackLabel("description2Feedback", new Model<String>(), description1Field).setOutputMarkupId(true));

			form.add(new LabelMandatory("description3Label", new ResourceModel("cardType.description3")));
			FormComponent<String> description3Field = new TextField<String>("description3", new PropertyModel<String>(model, "description.description3")).setRequired(true);
			form.add(description3Field);
			form.add(new FeedbackLabel("description3Feedback", new Model<String>(), description1Field).setOutputMarkupId(true));

			form.add(new Button("save", new ResourceModel("save")));

			form.visitChildren(FormComponent.class, new PropertyValidatorVisitor());

			return form;
		}

	}


}
