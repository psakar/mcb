package com.chare.mcb.www;

import java.io.File;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.util.value.ValueMap;
import org.apache.wicket.validation.validator.StringValidator;

import com.chare.mcb.entity.Role;
import com.chare.mcb.service.CardStatementGenerator;
import com.chare.wicket.TextField;


@Secured(required = { Role.APP_ADMIN }, requiredAnyOf = { })
public class GenerateCardStatementPage extends PanelPage {

	public GenerateCardStatementPage(PageParameters parameters) {
		super(parameters);
		add(createPanel());
	}

	private Component createPanel() {
		return new CardStatementParametersPanel(PANEL_ID);
	}

	@Override
	protected String getTitleCode() {
		return "generateCardStatement.title";
	}

	static class CardStatementParametersPanel extends org.apache.wicket.markup.html.panel.Panel {

		static final String BUTTON_ID = "button";

		static final String CARD_NUMBER_ID = "cardNumber";
		static final String DATE_FROM_ID = "dateFrom";
		static final String DATE_TO_ID = "dateTo";
		static final String STATEMENT_NUMBER_ID = "statementNumber";

		private final ValueMap valueMap;

		@SpringBean
		private CardStatementGenerator cardStatementGenerator;

		public CardStatementParametersPanel(String id) {
			super(id);
			valueMap = new ValueMap();
			add(createForm());
		}

		protected Form<Void> createForm() {

			Form<Void> form = new Form<Void>(FORM_ID, null) {
				@Override
				protected void onSubmit() {
					CardStatementParametersPanel.this.onSubmit();
				};
			};

			ResourceModel cardNumberLabel = new ResourceModel("generateCardStatement.cardNumber");
			form.add(new LabelMandatory("cardNumberLabel", cardNumberLabel));

			TextField<String> cardNumber = new TextField<String>(CARD_NUMBER_ID, new PropertyModel<String>(valueMap, CARD_NUMBER_ID));
			cardNumber.setType(String.class);
			cardNumber.setRequired(true);
			cardNumber.setLabel(cardNumberLabel);
			cardNumber.setSize(16);
			cardNumber.add(StringValidator.exactLength(16));
			form.add(cardNumber);

			form.add(new FeedbackLabel(CARD_NUMBER_ID + "Feedback", cardNumber).setOutputMarkupId(true));


			ResourceModel dateFromLabel = new ResourceModel("generateCardStatement.dateFrom");
			form.add(new LabelMandatory("dateFromLabel", dateFromLabel));

			TextFieldDate dateFrom = new TextFieldDate(DATE_FROM_ID, new PropertyModel<Date>(valueMap, DATE_FROM_ID));
			dateFrom.setRequired(true);
			dateFrom.setLabel(cardNumberLabel);
			form.add(dateFrom);

			form.add(new FeedbackLabel(DATE_FROM_ID + "Feedback", dateFrom).setOutputMarkupId(true));

			ResourceModel dateToLabel = new ResourceModel("generateCardStatement.dateTo");
			form.add(new LabelMandatory("dateToLabel", dateToLabel));

			TextFieldDate dateTo = new TextFieldDate(DATE_TO_ID, new PropertyModel<Date>(valueMap, DATE_TO_ID));
			dateTo.setRequired(true);
			dateTo.setLabel(dateToLabel);
			form.add(dateTo);

			form.add(new FeedbackLabel(DATE_TO_ID + "Feedback", dateTo).setOutputMarkupId(true));


			ResourceModel statementNumberLabel = new ResourceModel("generateCardStatement.statementNumber");
			form.add(new Label("statementNumberLabel", statementNumberLabel));

			TextFieldInteger statementNumber = new TextFieldInteger(STATEMENT_NUMBER_ID, new PropertyModel<Integer>(valueMap, STATEMENT_NUMBER_ID));
			statementNumber.setLabel(statementNumberLabel);
			statementNumber.setType(Integer.class);
			form.add(statementNumber);

			form.add(new FeedbackLabel(STATEMENT_NUMBER_ID + "Feedback", statementNumber).setOutputMarkupId(true));


			Button button = new Button(BUTTON_ID, new ResourceModel("generateCardStatement.generate", "Generate")) {
				@Override
				public void onSubmit() {
					CardStatementParametersPanel.this.onSubmit();
				};
			};
			form.add(button);
			return form;
		}

		protected void onSubmit() {
			String cardNumber = valueMap.getString(CARD_NUMBER_ID);
			Date dateFrom = (Date) valueMap.get(DATE_FROM_ID);
			Date dateTo = (Date) valueMap.get(DATE_TO_ID);
			Integer statementNumber = valueMap.getAsInteger(STATEMENT_NUMBER_ID);
			File generatedPdf = null;
			try {
				generatedPdf = cardStatementGenerator.generateOnDemand(cardNumber, dateFrom, dateTo, statementNumber);
			} catch (Exception e) {
				Logger.getLogger(getClass()).error("Generate card statement error " + e.getMessage(), e);
				error(new StringResourceModel("error", null, "Error ${0}", e.getMessage()).getObject());
				return;
			}
			if (generatedPdf != null)
				downloadFile(generatedPdf);
			else
				error(new ResourceModel("generateCardStatement.emptyStatement").getObject());
		}

		private void downloadFile(File file) {
			IResourceStream resourceStream = new FileResourceStream(
					new org.apache.wicket.util.file.File(file));
			getRequestCycle().replaceAllRequestHandlers(
					new ResourceStreamRequestHandler(resourceStream)
					{
						@Override
						public void respond(IRequestCycle requestCycle)
						{
							super.respond(requestCycle);
						}
					}.setFileName(file.getName())
					.setContentDisposition(ContentDisposition.ATTACHMENT)
					.setCacheDuration(Duration.NONE));
		}

	}
}