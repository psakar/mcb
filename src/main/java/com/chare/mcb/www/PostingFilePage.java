package com.chare.mcb.www;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.chare.mcb.entity.Booking;
import com.chare.mcb.entity.PostingFile;
import com.chare.mcb.entity.Role;
import com.chare.mcb.repository.PostingFileRepository;
import com.chare.mcb.service.PostingFileExporter;
import com.chare.mcb.service.UserPreferences;
import com.chare.wicket.Label;
import com.chare.wicket.PropertyModelFormatted;
import com.chare.wicket.TextData;

@Secured(required = { Role.EXPORT_POSTINGS }, requiredAnyOf = {  })
public class PostingFilePage extends PanelPage {

	@SpringBean
	private PostingFileRepository repository;

	public PostingFilePage(PageParameters parameters) {
		super(parameters);
		add(createPanel(createModel(toIdInteger(parameters.get(ID_PARAM)))));
	}

	private IModel<PostingFile> createModel(Integer id) {
		return new ModelFactory<Integer, PostingFile>().createModel(id, repository, new Factory<PostingFile>(PostingFile.class));
	}

	private Component createPanel(IModel<PostingFile> model) {
		return new PostingFilePanel(PANEL_ID, model);
	}

	@Override
	protected IModel<String> createTitleModel() {
		return new AbstractReadOnlyModel<String> (){
			@Override
			public String getObject() {
				return new ResourceModel("postingFile.title").getObject() + " " + StringUtils.defaultString(((PostingFile)get(PANEL_ID).getDefaultModelObject()).filename);
			}
		};
	}


	static class PostingFilePanel extends org.apache.wicket.markup.html.panel.Panel {

		//		@SpringBean(name = com.chare.service.Config.USER_PREFERENCES_SESSION_ID)
		//		private UserPreferences userPreferences;


		static final String FILENAME_ID = "filename";
		static final String EXPORTED_ID = "exported";
		static final String EXPORTED_USER_ID = "exportedUser";
		static final String CREATED_ID = "created";
		static final String CREATED_USER_ID = "createdUser";
		static final String STATEMENT_ID = "statement";

		static final String EXPORT_ID = "export";
		static final String DISAPPROVE_ID = "disapprove";

		static final String LIST_ID = "list";

		static final String NUMBER_ID = "number";
		static final String AMOUNT_ID = "amount";
		static final String VALUE_DATE_ID = "valueDate";
		static final String BUSINESS_DATE_ID = "businessDate";
		static final String DETAILS_ID = "details";
		static final String CREDIT_ACCOUNT_ID = "creditAccount";
		static final String DEBIT_ACCOUNT_ID = "debitAccount";


		@SpringBean
		private PostingFileExporter exporter;
		@SpringBean
		private UserPreferences userPreferences;

		public PostingFilePanel(final String id, final IModel<PostingFile> model) {
			super(id, model);
			add(new Link<String>(EXPORT_ID, new ResourceModel("postingFile.export", "Export")){
				@Override
				public void onClick() {
					try {
						exporter.export(model.getObject(), userPreferences.getUser());
					} catch (Exception e) {
						Logger.getLogger(getClass()).error("Export posting file error " + e.getMessage(), e);
						error(e.getMessage()); //FIXME localize
					}
				}
			});
			//			PageParameters pageParameters = new PageParameters();
			//			pageParameters.set(ID_PARAM, model.getObject().statmentId);
			//			add(new BookmarkablePageLink<String>(STATEMENT_ID, PostingFileList.class, pageParameters));
			add(new Link<String>(DISAPPROVE_ID, new ResourceModel("postingFile.disapprove", "Disapprove")){
				@Override
				public void onClick() {
					try {
						exporter.export(model.getObject(), userPreferences.getUser());
					} catch (Exception e) {
						Logger.getLogger(getClass()).error("Disapprove posting file error " + e.getMessage(), e);
						error(e.getMessage()); //FIXME localize
					}
				}
			});
			add(new TextData(FILENAME_ID, new PropertyModel<String>(model, "filename")));
			add(new TextData(CREATED_ID, new PropertyModelFormatted<String>(model, "created", userPreferences.getILocale().getDateTimeFormat())));
			add(new TextData(CREATED_USER_ID, new PropertyModel<String>(model, "createdUser.username")));
			add(new TextData(EXPORTED_ID, new PropertyModelFormatted<String>(model, "approved", userPreferences.getILocale().getDateTimeFormat())));
			add(new TextData(EXPORTED_USER_ID, new PropertyModel<String>(model, "approvedUser.username")));
			add(createLineList(LIST_ID, model));
		}

		@Override
		protected void onConfigure() {
			super.onConfigure();
			PostingFile file = (PostingFile) getDefaultModelObject();
			boolean userCanApproveOrDisapprove = file.canBeApprovedBy(userPreferences.getUser());
			get(EXPORT_ID).setVisible(userCanApproveOrDisapprove);
			get(DISAPPROVE_ID).setVisible(userCanApproveOrDisapprove);
		}

		private Component createLineList(String id, IModel<PostingFile> model) {
			SortableDataProvider<Booking> dataProvider = createDataProvider(new PropertyModel<List<Booking>>(model, "bookings"));
			ListTable<Integer, Booking> table = new ListTable<Integer, Booking>(id, dataProvider) {

				@Override
				public void populateItem(Item<Booking> item, Booking entity) {

					item.add(new Label(NUMBER_ID, (item.getIndex() + 1) + ""));
					item.add(new Label(AMOUNT_ID, formatAmount(entity.amount) + " " + entity.currency));
					item.add(new Label(VALUE_DATE_ID, formatDate(entity.creditValueDate)));
					item.add(new Label(BUSINESS_DATE_ID, formatDate(entity.businessDate)));
					item.add(new Label(CREDIT_ACCOUNT_ID, entity.creditAccount));
					item.add(new Label(DEBIT_ACCOUNT_ID, entity.debitAccount));
					item.add(new Label(DETAILS_ID, entity.details));
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

		private SortableDataProvider<Booking> createDataProvider(IModel<List<Booking>> listModel) {
			return new ListDataProvider<Booking>(listModel);
		}

	}


}