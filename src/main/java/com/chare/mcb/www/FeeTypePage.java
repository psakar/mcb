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

import com.chare.core.LanguageIndex;
import com.chare.mcb.entity.CardType;
import com.chare.mcb.entity.FeeType;
import com.chare.mcb.entity.Role;
import com.chare.mcb.entity.TransferType;
import com.chare.mcb.repository.CardTypeRepository;
import com.chare.mcb.repository.FeeTypeRepository;
import com.chare.mcb.repository.TransferTypeRepository;
import com.chare.mcb.service.UserPreferences;
import com.chare.repository.Repository;
import com.chare.repository.Sort;
import com.chare.wicket.Button;
import com.chare.wicket.DropDownChoice;
import com.chare.wicket.TextField;

@Secured(required = { Role.APP_ADMIN }, requiredAnyOf = {  })
public class FeeTypePage extends PanelPage {

	@SpringBean
	private FeeTypeRepository repository;



	public FeeTypePage(PageParameters parameters) {
		super(parameters);
		add(createPanel(createModel(toIdString(parameters.get(ID_PARAM)))));
	}

	private IModel<FeeType> createModel(String id) {
		return new ModelFactory<String, FeeType>().createModel(id, repository, new Factory<FeeType>(FeeType.class));
	}

	private Component createPanel(IModel<FeeType> model) {
		return new FeeTypePanel(PANEL_ID, model);
	}

	@Override
	protected IModel<String> createTitleModel() {
		return new AbstractReadOnlyModel<String> (){
			@Override
			public String getObject() {
				return new ResourceModel("cardType.title").getObject() + " " + StringUtils.defaultString(((FeeType)get(PANEL_ID).getDefaultModelObject()).code);
			}
		};
	}

	public static class FeeTypePanel extends org.apache.wicket.markup.html.panel.Panel {

		@SpringBean
		private FeeTypeRepository repository;

		@SpringBean
		private TransferTypeRepository transferTypeRepository;

		@SpringBean
		private CardTypeRepository cardTypeRepository;

		@SpringBean(name = com.chare.service.Config.USER_PREFERENCES_SESSION_ID)
		protected UserPreferences userPreferences;

		public FeeTypePanel(String id, IModel<FeeType> model) {
			super(id, model);
			add(createForm(model));
		}

		protected Form<FeeType> createForm(IModel<FeeType> model) {
			Form<FeeType> form = new EntityForm<FeeType>(FORM_ID, model, repository);

			form.add(new LabelMandatory("codeLabel", new ResourceModel("feeType.code")));
			TextField<String> codeTextField = new TextField<String>("code", new PropertyModel<String>(model, "code"));
			codeTextField.setRequired(true);
			codeTextField.setSize(10);
			codeTextField.add(StringValidator.lengthBetween(1, 10));
			form.add(codeTextField.setRequired(true));
			form.add(new FeedbackLabel("codeFeedback", new Model<String>(), codeTextField).setOutputMarkupId(true));

			form.add(new LabelMandatory("description1Label", new ResourceModel("feeType.description1")));
			FormComponent<String> description1Field = new TextField<String>("description1", new PropertyModel<String>(model, "description.description1")).setRequired(true);
			form.add(description1Field);
			form.add(new FeedbackLabel("description1Feedback", new Model<String>(), description1Field).setOutputMarkupId(true));

			form.add(new LabelMandatory("description2Label", new ResourceModel("feeType.description2")));
			FormComponent<String> description2Field = new TextField<String>("description2", new PropertyModel<String>(model, "description.description2")).setRequired(true);
			form.add(description2Field);
			form.add(new FeedbackLabel("description2Feedback", new Model<String>(), description2Field).setOutputMarkupId(true));

			form.add(new LabelMandatory("description3Label", new ResourceModel("feeType.description3")));
			FormComponent<String> description3Field = new TextField<String>("description3", new PropertyModel<String>(model, "description.description3")).setRequired(true);
			form.add(description3Field);
			form.add(new FeedbackLabel("description3Feedback", new Model<String>(), description3Field).setOutputMarkupId(true));

			form.add(new LabelMandatory("cardTypeLabel", new ResourceModel("feeType.cardType")));
			FormComponent<CardType> cardTypeField = new DropDownChoice<CardType>("cardType", new PropertyModel<CardType>(model, "cardType"), createCardTypeChoices(), new CardTypeChoiceRenderer(userPreferences.getUser()));
			cardTypeField.setRequired(true);
			form.add(cardTypeField);
			form.add(new FeedbackLabel("cardTypeFeedback", new Model<String>(), cardTypeField).setOutputMarkupId(true));

			form.add(new Label("amountLabel", new ResourceModel("feeType.amount")));
			FormComponent<String> amountField = new TextField<String>("amount", new PropertyModel<String>(model, "calculation.amount"));
			form.add(amountField);
			form.add(new FeedbackLabel("amountFeedback", new Model<String>(), amountField).setOutputMarkupId(true));

			form.add(new Label("percentageLabel", new ResourceModel("feeType.percentage")));
			FormComponent<String> percentageField = new TextField<String>("percentage", new PropertyModel<String>(model, "calculation.percentage"));
			form.add(percentageField);
			form.add(new FeedbackLabel("percentageFeedback", new Model<String>(), percentageField).setOutputMarkupId(true));

			ResourceModel settlementAccountLabel = new ResourceModel("transferType.settlementAccount");
			form.add(new Label("settlementAccountLabel", settlementAccountLabel));
			TextField<String> settlementAccount = new TextField<String>("settlementAccount", new PropertyModel<String>(model, "settlementAccount"));
			settlementAccount.setSize(15);
			settlementAccount.add(new AttributeModifier("maxlength", new Model<String>("15")));
			settlementAccount.add(StringValidator.lengthBetween(14, 15));
			settlementAccount.setLabel(settlementAccountLabel);
			form.add(settlementAccount);
			form.add(new FeedbackLabel("settlementAccountFeedback", new Model<String>(), settlementAccount).setOutputMarkupId(true));

			form.add(new LabelMandatory("transferTypeLabel", new ResourceModel("feeType.transferType")));
			List<? extends TransferType> transferTypeChoices = createTransferTypeChoices();
			FormComponent<TransferType> transferTypeField = new DropDownChoice<TransferType>("transferType", new PropertyModel<TransferType>(model, "transferType"), transferTypeChoices, new TransferTypeChoiceRenderer(userPreferences.getUser()));
			transferTypeField.setRequired(true);
			form.add(transferTypeField);
			form.add(new FeedbackLabel("transferTypeFeedback", new Model<String>(), transferTypeField).setOutputMarkupId(true));



			form.add(new Button("save", new ResourceModel("save")));

			form.visitChildren(FormComponent.class, new PropertyValidatorVisitor());

			return form;
		}


		private List<TransferType> createTransferTypeChoices() {
			List<Sort> sorts = Arrays.asList(new Sort("code"));
			return transferTypeRepository.find(Repository.FIRST, Repository.ALL_RESUTLS, Repository.NO_RESTRICTION, sorts);
		}

		private List<CardType> createCardTypeChoices() {
			List<Sort> sorts = Arrays.asList(new Sort("code"));
			return cardTypeRepository.find(Repository.FIRST, Repository.ALL_RESUTLS, Repository.NO_RESTRICTION, sorts);
		}
	}

	static class CardTypeChoiceRenderer implements IChoiceRenderer<CardType> {

		private final LanguageIndex languageIndex;

		public CardTypeChoiceRenderer(LanguageIndex languageIndex) {
			this.languageIndex = languageIndex;
		}

		@Override
		public String getDisplayValue(CardType object) {
			return object.code + " - " + object.description.getDescription(languageIndex.getLanguageIndex());
		}

		@Override
		public String getIdValue(CardType object, int index) {
			return object.getId().toString();
		}

	}

	static class TransferTypeChoiceRenderer implements IChoiceRenderer<TransferType> {

		private final LanguageIndex languageIndex;

		public TransferTypeChoiceRenderer(LanguageIndex languageIndex) {
			this.languageIndex = languageIndex;
		}

		@Override
		public String getDisplayValue(TransferType object) {
			return object.code + " - " + object.description.getDescription(languageIndex.getLanguageIndex());
		}

		@Override
		public String getIdValue(TransferType object, int index) {
			return object.getId().toString();
		}

	}

}
