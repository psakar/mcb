package com.chare.mcb.www;


import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.chare.mcb.entity.CardTransaction;
import com.chare.mcb.entity.Role;
import com.chare.mcb.entity.Statement;
import com.chare.mcb.entity.StatementLine;
import com.chare.mcb.entity.TransferType;
import com.chare.mcb.repository.StatementRepository;
import com.chare.mcb.repository.TransferTypeRepository;
import com.chare.mcb.service.CardTransactionParser;
import com.chare.mcb.service.PostingFileGenerator;
import com.chare.mcb.service.TransferTypeResolver;
import com.chare.mcb.service.UserPreferences;
import com.chare.mcb.www.FeeTypePage.TransferTypeChoiceRenderer;
import com.chare.repository.Repository;
import com.chare.repository.Sort;
import com.chare.wicket.DropDownChoice;
import com.chare.wicket.Label;
import com.chare.wicket.PropertyModelFormatted;
import com.chare.wicket.TextData;

@Secured(required = { Role.UPLOAD_STATEMENTS }, requiredAnyOf = {  })
public class StatementPage extends PanelPage {

	@SpringBean
	private StatementRepository repository;

	public StatementPage(PageParameters parameters) {
		super(parameters);
		add(createPanel(createModel(toIdInteger(parameters.get(ID_PARAM)))));
	}

	private IModel<Statement> createModel(Integer id) {
		return new ModelFactory<Integer, Statement>().createModel(id, repository, new Factory<Statement>(Statement.class));
	}

	private Component createPanel(IModel<Statement> model) {
		return new StatementPanel(PANEL_ID, model);
	}

	@Override
	protected IModel<String> createTitleModel() {
		return new AbstractReadOnlyModel<String> (){
			@Override
			public String getObject() {
				return new ResourceModel("statement.title").getObject() + " " + StringUtils.defaultString(((Statement)get(PANEL_ID).getDefaultModelObject()).number);
			}
		};
	}


	static class StatementPanel extends org.apache.wicket.markup.html.panel.Panel {

		//		@SpringBean(name = com.chare.service.Config.USER_PREFERENCES_SESSION_ID)
		//		private UserPreferences userPreferences;

		static final String AMOUNT_ID = "amount";
		static final String NUMBER_ID = "number";
		static final String STATEMENT_DATE_ID = "statementDate";
		static final String SOURCE_FILENAME_ID = "sourceFilename";
		static final String OPENING_BALANCE_ID = "openingBalance";
		static final String CLOSING_BALANCE_ID = "closingBalance";
		static final String LIST_ID = "list";
		static final String VALUE_DATE_ID = "valueDate";
		static final String DETAILS1_ID = "details1";
		static final String DETAILS2_ID = "details2";
		static final String DETAILS3_ID = "details3";
		static final String DETAILS4_ID = "details4";
		static final String REFERENCE1_ID = "reference1";
		static final String REFERENCE2_ID = "reference2";
		static final String SWIFT_TYPE_ID = "swiftType";

		static final String GENERATE_ID = "generate";
		static final String RESOLVE_ID = "resolve";
		static final String POSTING_FILE_ID = "postingFile";

		@SpringBean
		private StatementRepository repository;
		@SpringBean
		private UserPreferences userPreferences;
		@SpringBean
		private TransferTypeRepository transferTypeRepository;
		@SpringBean
		private CardTransactionParser cardTransactionParser;
		@SpringBean
		private PostingFileGenerator postingFileGenerator;
		@SpringBean
		private TransferTypeResolver transferTypeResolver;

		public StatementPanel(final String id, final IModel<Statement> model) {
			super(id, model);
			add(new Link<String>(GENERATE_ID, new ResourceModel("generate", "Generate posting file")){
				@Override
				public void onClick() {
					try {
						postingFileGenerator.generatePostingFileFor(model.getObject(), userPreferences.getUser());
					} catch (Exception e) {
						Logger.getLogger(getClass()).error("Generate posting file error " + e.getMessage(), e);
						error(e.getMessage() + " "); //FIXME localize
					}
				}
			});
			PageParameters pageParameters = new PageParameters();
			pageParameters.set(ID_PARAM, model.getObject().postingFileId);
			add(new BookmarkablePageLink<String>(POSTING_FILE_ID, PostingFilePage.class, pageParameters));
			add(new Link<String>(RESOLVE_ID, new ResourceModel("resolve", "Resolve card transactions")){
				@Override
				public void onClick() {
					try {
						Statement entity = model.getObject();
						transferTypeResolver.resolve(entity);
						repository.persist(entity);
					} catch (IllegalArgumentException ee) {
						error(ee.getMessage()); //FIXME localize
					} catch (Exception e) {
						Logger.getLogger(getClass()).error("Resolve card transactions error " + e.getMessage(), e);
						error(e.getMessage()); //FIXME localize
					}
				}
			});
			add(new TextData(NUMBER_ID, new PropertyModel<String>(model, "number")));
			add(new TextData(SOURCE_FILENAME_ID, new PropertyModel<String>(model, "sourceFilename")));
			add(new TextData(STATEMENT_DATE_ID, new PropertyModelFormatted<Date>(model, "statementDate", userPreferences.getILocale().getDateFormat())));
			add(new TextData(OPENING_BALANCE_ID, new PropertyModelFormatted<Number>(model, "openingBalance", userPreferences.getILocale().getCurrencyNumberFormat())));
			add(new TextData(CLOSING_BALANCE_ID, new PropertyModelFormatted<Number>(model, "closingBalance", userPreferences.getILocale().getCurrencyNumberFormat())));
			add(createLineList(LIST_ID, model));
		}

		@Override
		protected void onConfigure() {
			super.onConfigure();
			Statement statement = (Statement) getDefaultModelObject();
			get(GENERATE_ID).setVisible(statement.isReadyToGenerateBookings());
			get(RESOLVE_ID).setVisible(statement.requiresResolution());
			get(POSTING_FILE_ID).setVisible(statement.hasGeneratedPostingFile());
		}

		private Component createLineList(String id, IModel<Statement> model) {
			SortableDataProvider<StatementLine> dataProvider = createDataProvider(new PropertyModel<List<StatementLine>>(model, "lines"));
			ListTable<Integer, StatementLine> table = new ListTable<Integer, StatementLine>(id, dataProvider) {

				@Override
				protected void modifyRowClass(Item<StatementLine> item) {
					StatementLine entity = item.getModelObject();
					String rowClass = null;
					if (!entity.getStatement().hasGeneratedPostingFile() && entity.requiresResolution())
						rowClass = "resolve";
					item.add(new AttributeAppender("class", new Model<String>(rowClass), " "));
				}

				@Override
				public void populateItem(Item<StatementLine> item, StatementLine entity) {

					item.add(new Label(NUMBER_ID, (entity.number + 1) + ""));
					item.add(new Label(AMOUNT_ID, formatAmount(entity.amount)));
					item.add(new Label(VALUE_DATE_ID, formatDate(entity.valueDate)));
					item.add(new Label(DETAILS1_ID, entity.details1));
					item.add(new Label(DETAILS2_ID, entity.details2));
					item.add(new Label(DETAILS3_ID, entity.details3));
					item.add(new Label(DETAILS4_ID, entity.details4));

					item.add(new Label(REFERENCE1_ID, entity.reference1));
					item.add(new Label(REFERENCE2_ID, entity.reference2));
					item.add(new Label(SWIFT_TYPE_ID, entity.swiftType));

					CardTransaction cardTransaction = entity.getCardTransaction();
					if (cardTransaction == null)
						cardTransaction = new CardTransaction();
					item.add(new Label("cardTransactionCardNumber", cardTransaction.cardNumber));
					item.add(new Label("cardTransactionDate", formatDate(cardTransaction.date)));
					item.add(new Label("cardTransactionAmount", formatAmount(cardTransaction.amount) + " " + StringUtils.defaultString(cardTransaction.currency)));
					item.add(new Label("cardTransactionFeeType", (cardTransaction.feeType == null ? "" : cardTransaction.feeType.code)));
					item.add(new Label("cardTransactionFee", formatAmount(cardTransaction.feeAmount) + " " + StringUtils.defaultString(cardTransaction.feeCurrency)));
					item.add(new Label("cardTransactionDetails1", cardTransaction.details1));
					item.add(new Label("cardTransactionDetails2", cardTransaction.details2));
					item.add(createForm(FORM_ID, item.getModel()));
				}

				private String formatAmount(BigDecimal amount) {
					if (amount == null)
						return "";
					return userPreferences.getILocale().getCurrencyNumberFormat().format(amount);
				}

				private String formatDate(Date date) {
					if (date == null)
						return "";
					return userPreferences.getILocale().getDateFormat().format(date);
				}
			};
			table.setOutputMarkupId(true);
			return table;
		}

		private SortableDataProvider<StatementLine> createDataProvider(IModel<List<StatementLine>> listModel) {
			return new ListDataProvider<StatementLine>(listModel);
		}


		protected Form<StatementLine> createForm(String id, final IModel<StatementLine> model) {
			final Form<StatementLine> form = new Form<StatementLine>(id, model) {
				@Override
				protected void onSubmit() {
					try {
						StatementLine entity = model.getObject();
						cardTransactionParser.parseCardTransaction(entity);
						repository.persist(entity.getStatement());
					} catch (Exception e) {
						Logger.getLogger(getClass()).error("Save error " + e.getMessage(), e);
						error(new ResourceModel("error").getObject() + " " + e.getMessage());//FIXME "Error ({$1} {$2} {$3})_#2000;", new String[] { e.getMessage() });
					}
				}
			};

			FormComponent<TransferType> transferTypeField = new DropDownChoice<TransferType>("transferType", new PropertyModel<TransferType>(model, "transferType"), createTransferTypeChoices(), new TransferTypeChoiceRenderer(userPreferences.getUser()));
			transferTypeField.setRequired(true);
			transferTypeField.setLabel(new ResourceModel("statementLine.transferType"));
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
	}


}