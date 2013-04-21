package com.chare.mcb.www;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import com.chare.mcb.entity.CardTransactionType;
import com.chare.mcb.entity.Role;
import com.chare.mcb.entity.SettlementType;
import com.chare.mcb.entity.TransferType;
import com.chare.mcb.repository.TransferTypeRepository;
import com.chare.wicket.Button;
import com.chare.wicket.DropDownChoice;
import com.chare.wicket.TextField;

@Secured(required = { Role.APP_ADMIN }, requiredAnyOf = {  })
public class TransferTypePage extends PanelPage {

	@SpringBean
	private TransferTypeRepository repository;

	public TransferTypePage(PageParameters parameters) {
		super(parameters);
		add(createPanel(createModel(toIdString(parameters.get(ID_PARAM)))));
	}

	private IModel<TransferType> createModel(String id) {
		return new ModelFactory<String, TransferType>().createModel(id, repository, new Factory<TransferType>(TransferType.class));
	}

	private Component createPanel(IModel<TransferType> model) {
		return new TransferTypePanel(PANEL_ID, model);
	}

	@Override
	protected IModel<String> createTitleModel() {
		return new AbstractReadOnlyModel<String> (){
			@Override
			public String getObject() {
				return new ResourceModel("transferType.title").getObject() + " " + StringUtils.defaultString(((TransferType)get(PANEL_ID).getDefaultModelObject()).code);
			}
		};
	}

	public static class TransferTypePanel extends org.apache.wicket.markup.html.panel.Panel {

		@SpringBean
		private TransferTypeRepository repository;

		public TransferTypePanel(String id, IModel<TransferType> model) {
			super(id, model);
			add(createForm(model));
		}

		protected Form<TransferType> createForm(IModel<TransferType> model) {
			Form<TransferType> form = new EntityForm<TransferType>(FORM_ID, model, repository);

			form.add(new LabelMandatory("codeLabel", new ResourceModel("transferType.code")));
			TextField<String> codeTextField = new TextField<String>("code", new PropertyModel<String>(model, "code"));
			form.add(codeTextField.setRequired(true));
			form.add(new FeedbackLabel("codeFeedback", new Model<String>(), codeTextField).setOutputMarkupId(true));

			form.add(new LabelMandatory("description1Label", new ResourceModel("transferType.description1")));
			FormComponent<String> description1Field = new TextField<String>("description1", new PropertyModel<String>(model, "description.description1")).setRequired(true);
			form.add(description1Field);
			form.add(new FeedbackLabel("description1Feedback", new Model<String>(), description1Field).setOutputMarkupId(true));

			form.add(new LabelMandatory("description2Label", new ResourceModel("transferType.description2")));
			FormComponent<String> description2Field = new TextField<String>("description2", new PropertyModel<String>(model, "description.description2")).setRequired(true);
			form.add(description2Field);
			form.add(new FeedbackLabel("description2Feedback", new Model<String>(), description1Field).setOutputMarkupId(true));

			form.add(new LabelMandatory("description3Label", new ResourceModel("transferType.description3")));
			FormComponent<String> description3Field = new TextField<String>("description3", new PropertyModel<String>(model, "description.description3")).setRequired(true);
			form.add(description3Field);
			form.add(new FeedbackLabel("description3Feedback", new Model<String>(), description1Field).setOutputMarkupId(true));

			form.add(new LabelMandatory("settlementTypeLabel", new ResourceModel("transferType.settlementType")));
			List<? extends SettlementType> settlementTypeChoices = createSettlementTypeChoices();
			FormComponent<SettlementType> settlementTypeField = new DropDownChoice<SettlementType>("settlementType", new PropertyModel<SettlementType>(model, "settlementType"), settlementTypeChoices, new SettlementTypeChoiceRenderer());
			form.add(settlementTypeField);
			form.add(new FeedbackLabel("settlementTypeFeedback", new Model<String>(), description1Field).setOutputMarkupId(true));

			ResourceModel settlementAccountLabel = new ResourceModel("transferType.settlementAccount");
			form.add(new Label("settlementAccountLabel", settlementAccountLabel));
			TextField<String> settlementAccount = new TextField<String>("settlementAccount", new PropertyModel<String>(model, "settlementAccount"));
			settlementAccount.setSize(15);
			settlementAccount.add(new AttributeModifier("maxlength", new Model<String>("15")));
			settlementAccount.add(StringValidator.lengthBetween(14, 15));
			settlementAccount.setLabel(settlementAccountLabel);
			form.add(settlementAccount);
			form.add(new FeedbackLabel("settlementAccountFeedback", new Model<String>(), description1Field).setOutputMarkupId(true));

			form.add(new LabelMandatory("cardTransactionTypeLabel", new ResourceModel("transferType.cardTransactionType")));
			List<? extends CardTransactionType> cardTransactionTypeChoices = createCardTransactionTypeChoices();
			FormComponent<CardTransactionType> cardTransactionTypeField = new DropDownChoice<CardTransactionType>("cardTransactionType", new PropertyModel<CardTransactionType>(model, "cardTransactionType"), cardTransactionTypeChoices, new CardTransactionTypeChoiceRenderer());
			form.add(cardTransactionTypeField);
			form.add(new FeedbackLabel("cardTransactionTypeFeedback", new Model<String>(), description1Field).setOutputMarkupId(true));

			form.add(new Button("save", new ResourceModel("save")));

			form.visitChildren(FormComponent.class, new PropertyValidatorVisitor());

			return form;
		}


	}

	static List<? extends SettlementType> createSettlementTypeChoices() {
		return Arrays.asList(SettlementType.CLIENT_SETTLEMENT, SettlementType.BANK_SETTLEMENT, SettlementType.CLIENT_SETTLEMENT_WITH_FEE);
	}

	static class SettlementTypeChoiceRenderer implements IChoiceRenderer<SettlementType> {

		@Override
		public String getDisplayValue(SettlementType object) {
			return TransferTypeList.getSettlementTypeDescription(object).getObject();
		}

		@Override
		public String getIdValue(SettlementType object, int index) {
			return object.ordinal() + "";
		}

	}

	static List<? extends CardTransactionType> createCardTransactionTypeChoices() {
		return Arrays.asList(CardTransactionType.TRANSACTION, CardTransactionType.TRANSACTION_FEE, CardTransactionType.FEE);
	}

	static class CardTransactionTypeChoiceRenderer implements IChoiceRenderer<CardTransactionType> {

		@Override
		public String getDisplayValue(CardTransactionType object) {
			return TransferTypeList.getCardTransactionTypeDescription(object).getObject();
		}

		@Override
		public String getIdValue(CardTransactionType object, int index) {
			return object.ordinal() + "";
		}

	}

}
