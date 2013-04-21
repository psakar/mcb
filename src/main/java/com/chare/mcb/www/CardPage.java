package com.chare.mcb.www;


import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
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

import com.chare.mcb.Application;
import com.chare.mcb.entity.Card;
import com.chare.mcb.entity.CardType;
import com.chare.mcb.entity.Role;
import com.chare.mcb.entity.StatementPeriod;
import com.chare.mcb.repository.CardRepository;
import com.chare.mcb.repository.CardTypeRepository;
import com.chare.mcb.service.UserPreferences;
import com.chare.mcb.www.ChoiceValueDropDownChoice.ChoiceIdModel;
import com.chare.mcb.www.FeeTypePage.CardTypeChoiceRenderer;
import com.chare.repository.Repository;
import com.chare.repository.Sort;
import com.chare.wicket.TextField;

@Secured(required = { }, requiredAnyOf = { Role.UPLOAD_STATEMENTS, Role.EXPORT_POSTINGS })
public class CardPage extends PanelPage {

	@SpringBean
	private CardRepository repository;

	public CardPage(PageParameters parameters) {
		super(parameters);
		add(createPanel(createModel(toIdInteger(parameters.get(ID_PARAM)))));
	}

	private IModel<Card> createModel(Integer id) {
		return new ModelFactory<Integer, Card>().createModel(id, repository, new Factory<Card>(Card.class));
	}

	private Component createPanel(IModel<Card> model) {
		return new CardPanel(PANEL_ID, model);
	}

	@Override
	protected IModel<String> createTitleModel() {
		return new AbstractReadOnlyModel<String> (){
			@Override
			public String getObject() {
				return new ResourceModel("card.title").getObject() + " " + StringUtils.defaultString(((Card)get(PANEL_ID).getDefaultModelObject()).number);
			}
		};
	}


	static class CardPanel extends org.apache.wicket.markup.html.panel.Panel {

		@SpringBean(name = com.chare.service.Config.USER_PREFERENCES_SESSION_ID)
		private UserPreferences userPreferences;

		@SpringBean
		private CardRepository repository;
		@SpringBean
		private CardTypeRepository cardTypeRepository;

		public CardPanel(String id, IModel<Card> model) {
			super(id, model);
			add(createForm(model));
		}

		protected Form<Card> createForm(IModel<Card> model) {
			final Form<Card> form = new EntityForm<Card>(PanelPage.FORM_ID, model, repository);

			ResourceModel numberLabel = new ResourceModel("card.number");
			form.add(new LabelMandatory("numberLabel", numberLabel));
			TextField<String> number = new TextField<String>("number", new PropertyModel<String>(model, "number"));
			number.setRequired(true);
			number.setSize(16);
			number.add(StringValidator.exactLength(16));
			number.setLabel(numberLabel);
			form.add(number);
			form.add(new FeedbackLabel("numberFeedback", number).setOutputMarkupId(true));

			ResourceModel cardTypeLabel = new ResourceModel("card.cardType");
			form.add(new LabelMandatory("cardTypeLabel", cardTypeLabel));
			DropDownChoice<CardType> cardType = new DropDownChoice<CardType>("cardType", new PropertyModel<CardType>(model, "cardType"), createCardTypeChoices(), new CardTypeChoiceRenderer(userPreferences.getUser()));
			cardType.setRequired(true);
			cardType.setNullValid(false);
			cardType.setLabel(cardTypeLabel);
			form.add(cardType);
			form.add(new FeedbackLabel("cardTypeFeedback", new Model<String>(), cardType).setOutputMarkupId(true));

			ResourceModel holderNameLabel = new ResourceModel("card.holderName");
			form.add(new LabelMandatory("holderNameLabel", holderNameLabel));
			TextField<String> holderName = new TextField<String>("holderName", new PropertyModel<String>(model, "holderName"));
			holderName.setRequired(true);
			holderName.setSize(40);
			holderName.add(StringValidator.lengthBetween(3, 40));
			holderName.setLabel(holderNameLabel);
			form.add(holderName);
			form.add(new FeedbackLabel("holderNameFeedback", holderName).setOutputMarkupId(true));

			ResourceModel emailLabel = new ResourceModel("card.email");
			form.add(new LabelMandatory("emailLabel", emailLabel));
			TextField<String> email = new TextField<String>("email", new PropertyModel<String>(model, "email"));
			email.setSize(50);
			email.setMaxLength(50);
			email.setRequired(true);
			email.add(StringValidator.lengthBetween(3, 50));
			email.setLabel(emailLabel);
			form.add(email);
			form.add(new FeedbackLabel("emailFeedback", email).setOutputMarkupId(true));

			ResourceModel phoneLabel = new ResourceModel("card.phone");
			form.add(new LabelMandatory("phoneLabel", phoneLabel));
			TextField<String> phone = new TextField<String>("phone", new PropertyModel<String>(model, "phone"));
			phone.setRequired(true);
			phone.setSize(35);
			phone.setMaxLength(35);
			phone.add(StringValidator.lengthBetween(1, 35));
			phone.setLabel(phoneLabel);
			form.add(phone);
			form.add(new FeedbackLabel("phoneFeedback", phone).setOutputMarkupId(true));

			ResourceModel addressTitleLabel = new ResourceModel("card.addressTitle");
			form.add(new Label("addressTitleLabel", addressTitleLabel));
			TextField<String> addressTitle = new TextField<String>("addressTitle", new PropertyModel<String>(model, "address.title"));
			addressTitle.setSize(35);
			addressTitle.setMaxLength(35);
			addressTitle.add(StringValidator.maximumLength(35));
			addressTitle.setLabel(addressTitleLabel);
			form.add(addressTitle);
			form.add(new FeedbackLabel("addressTitleFeedback", addressTitle).setOutputMarkupId(true));

			ResourceModel addressNameLabel = new ResourceModel("card.addressName");
			form.add(new LabelMandatory("addressNameLabel", addressNameLabel));
			TextField<String> addressName = new TextField<String>("addressName", new PropertyModel<String>(model, "address.name"));
			addressName.setRequired(true);
			addressName.setSize(40);
			addressName.setMaxLength(40);
			addressName.add(StringValidator.lengthBetween(1, 40));
			addressName.setLabel(addressNameLabel);
			form.add(addressName);
			form.add(new FeedbackLabel("addressNameFeedback", addressName).setOutputMarkupId(true));

			ResourceModel addressName2Label = new ResourceModel("card.addressName2");
			form.add(new Label("addressName2Label", addressName2Label));
			TextField<String> addressName2 = new TextField<String>("addressName2", new PropertyModel<String>(model, "address.name2"));
			addressName2.setSize(40);
			addressName2.setMaxLength(40);
			addressName2.add(StringValidator.maximumLength(40));
			addressName2.setLabel(addressName2Label);
			form.add(addressName2);
			form.add(new FeedbackLabel("addressName2Feedback", addressName2).setOutputMarkupId(true));

			ResourceModel addressStreetLabel = new ResourceModel("card.addressStreet");
			form.add(new LabelMandatory("addressStreetLabel", addressStreetLabel));
			TextField<String> addressStreet = new TextField<String>("addressStreet", new PropertyModel<String>(model, "address.street"));
			addressStreet.setRequired(true);
			addressStreet.setSize(40);
			addressStreet.setMaxLength(40);
			addressStreet.add(StringValidator.lengthBetween(1, 40));
			addressStreet.setLabel(addressStreetLabel);
			form.add(addressStreet);
			form.add(new FeedbackLabel("addressStreetFeedback", addressStreet).setOutputMarkupId(true));

			ResourceModel addressZipLabel = new ResourceModel("card.addressZip");
			form.add(new LabelMandatory("addressZipLabel", addressZipLabel));
			TextField<String> addressZip = new TextField<String>("addressZip", new PropertyModel<String>(model, "address.zip"));
			addressZip.setRequired(true);
			addressZip.setSize(10);
			addressZip.setMaxLength(10);
			addressZip.add(StringValidator.lengthBetween(1, 10));
			addressZip.setLabel(addressZipLabel);
			form.add(addressZip.setRequired(true));
			form.add(new FeedbackLabel("addressZipFeedback", addressZip).setOutputMarkupId(true));

			ResourceModel addressTownLabel = new ResourceModel("card.addressTown");
			form.add(new LabelMandatory("addressTownLabel", addressTownLabel));
			TextField<String> addressTown = new TextField<String>("addressTown", new PropertyModel<String>(model, "address.town"));
			addressTown.setRequired(true);
			addressTown.setSize(40);
			addressTown.setMaxLength(40);
			addressTown.add(StringValidator.lengthBetween(1, 40));
			addressTown.setLabel(addressTownLabel);
			form.add(addressTown.setRequired(true));
			form.add(new FeedbackLabel("addressTownFeedback", addressTown).setOutputMarkupId(true));

			ResourceModel addressCountryLabel = new ResourceModel("card.addressCountry");
			form.add(new Label("addressCountryLabel", addressCountryLabel));
			TextField<String> addressCountry = new TextField<String>("addressCountry", new PropertyModel<String>(model, "address.country"));
			addressCountry.setRequired(true);
			addressCountry.setSize(40);
			addressCountry.setMaxLength(40);
			addressCountry.add(StringValidator.maximumLength(40));
			addressCountry.setLabel(addressCountryLabel);
			form.add(addressCountry);
			form.add(new FeedbackLabel("addressCountryFeedback", addressCountry).setOutputMarkupId(true));

			ResourceModel activeFromLabel = new ResourceModel("card.activeFrom");
			form.add(new LabelMandatory("activeFromLabel", activeFromLabel));
			TextFieldDate activeFrom = new TextFieldDate("activeFrom", new PropertyModel<Date>(model, "activeFrom"));
			activeFrom.setRequired(true);
			activeFrom.setLabel(activeFromLabel);
			form.add(activeFrom);
			form.add(new FeedbackLabel("activeFromFeedback", activeFrom).setOutputMarkupId(true));

			ResourceModel activeToLabel = new ResourceModel("card.activeTo");
			form.add(new Label("activeToLabel", activeToLabel));
			TextFieldDate activeTo = new TextFieldDate("activeTo", new PropertyModel<Date>(model, "activeTo"));
			activeTo.setLabel(activeToLabel);
			form.add(activeTo);
			form.add(new FeedbackLabel("activeToFeedback", activeTo).setOutputMarkupId(true));

			ResourceModel settlementAccountLabel = new ResourceModel("card.settlementAccount");
			form.add(new LabelMandatory("settlementAccountLabel", settlementAccountLabel));
			TextField<String> settlementAccount = new TextField<String>("settlementAccount", new PropertyModel<String>(model, "settlementAccount"));
			settlementAccount.add(new AttributeModifier("maxlength", new Model<String>("15")));
			settlementAccount.setSize(15);
			settlementAccount.add(StringValidator.lengthBetween(14, 15));
			settlementAccount.setLabel(settlementAccountLabel);
			settlementAccount.setRequired(true);
			form.add(settlementAccount);
			form.add(new FeedbackLabel("settlementAccountFeedback", settlementAccount).setOutputMarkupId(true));

			ResourceModel statementPeriodLabel = new ResourceModel("card.statementPeriod");
			form.add(new LabelMandatory("statementPeriodLabel", statementPeriodLabel));
			DropDownChoice<StatementPeriod> statementPeriodField = new DropDownChoice<StatementPeriod>("statementPeriod", new PropertyModel<StatementPeriod>(model, "statementPeriod"), createStatementPeriodChoices(), new StatementPeriodChoiceRenderer());
			statementPeriodField.setRequired(true);
			statementPeriodField.setNullValid(false);
			statementPeriodField.setLabel(statementPeriodLabel);
			form.add(statementPeriodField);
			form.add(new FeedbackLabel("statementPeriodFeedback", new Model<String>(), statementPeriodField).setOutputMarkupId(true));

			ResourceModel limitLabel = new ResourceModel("card.limit");
			form.add(new Label("limitLabel", limitLabel));
			TextFieldCurrency limitField = new TextFieldCurrency("limit", new PropertyModel<Number>(model, "limit"));
			limitField.setLabel(limitLabel);
			form.add(limitField);
			form.add(new FeedbackLabel("limitFeedback", new Model<String>(), limitField).setOutputMarkupId(true));

			ResourceModel lastStatementNrLabel = new ResourceModel("card.lastStatementNr");
			form.add(new Label("lastStatementNrLabel", lastStatementNrLabel));
			TextFieldInteger lastStatementNr = new TextFieldInteger("lastStatementNr", new PropertyModel<Integer>(model, "lastStatementNr"));
			lastStatementNr.setLabel(lastStatementNrLabel);
			form.add(lastStatementNr);
			form.add(new FeedbackLabel("lastStatementNrFeedback", new Model<String>(), lastStatementNr).setOutputMarkupId(true));

			ResourceModel lastStatementDateLabel = new ResourceModel("card.lastStatementDate");
			form.add(new Label("lastStatementDateLabel", lastStatementDateLabel));
			TextFieldDate lastStatementDate = new TextFieldDate("lastStatementDate", new PropertyModel<Date>(model, "lastStatementDate"));
			lastStatementDate.setLabel(lastStatementDateLabel);
			form.add(lastStatementDate);
			form.add(new FeedbackLabel("lastStatementDateFeedback", lastStatementDate).setOutputMarkupId(true));

			ResourceModel nextStatementDateLabel = new ResourceModel("card.nextStatementDate");
			form.add(new Label("nextStatementDateLabel", nextStatementDateLabel));
			TextFieldDate nextStatementDate = new TextFieldDate("nextStatementDate", new PropertyModel<Date>(model, "nextStatementDate"));
			nextStatementDate.setLabel(nextStatementDateLabel);
			form.add(nextStatementDate);
			form.add(new FeedbackLabel("nextStatementDateFeedback", nextStatementDate).setOutputMarkupId(true));

			ResourceModel languageLabel = new ResourceModel("card.language");
			form.add(new LabelMandatory("languageLabel", languageLabel));
			List<ChoiceValue> languageChoices = LanguageChoice.convertToChoices(Application.DEFAULT_LOCALES);
			LanguageChoice language = new LanguageChoice("language", new ChoiceIdModel(model, "languageId"), languageChoices);
			language.setRequired(true);
			language.setNullValid(false);
			language.setLabel(languageLabel);
			form.add(language);
			form.add(new FeedbackLabel("languageFeedback", new Model<String>(), language).setOutputMarkupId(true));

			form.add(new Button("save", new ResourceModel("save")));
			form.visitChildren(FormComponent.class, new PropertyValidatorVisitor());

			return form;
		}

		private List<CardType> createCardTypeChoices() {
			List<Sort> sorts = Arrays.asList(new Sort("code"));
			return cardTypeRepository.find(Repository.FIRST, Repository.ALL_RESUTLS, Repository.NO_RESTRICTION, sorts);
		}
		/*
		private void afterPersist(boolean newCard, Card user) {
			if (newCard) {
				PageParameters pageParameters = new PageParameters();
				pageParameters.set(ID_PARAM, user.getId());
				setResponsePage(CardPage.class, pageParameters);
			} else {
				setResponsePage(CardList.class);
			}
		}

		private String formatDateTime(Date date) {
			return date == null ? "" : userPreferences.getUser().getDateTimeFormat().format(date);
		}
		 */
	}

	static List<? extends StatementPeriod> createStatementPeriodChoices() {
		return Arrays.asList(StatementPeriod.DAILY, StatementPeriod.WEEKLY, StatementPeriod.MONTHLY);
	}

	static class StatementPeriodChoiceRenderer implements IChoiceRenderer<StatementPeriod> {

		@Override
		public String getDisplayValue(StatementPeriod object) {
			return CardList.getStatementPeriodDescription(object).getObject();
		}

		@Override
		public String getIdValue(StatementPeriod object, int index) {
			return object.ordinal() + "";
		}

	}

}