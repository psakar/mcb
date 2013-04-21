package com.chare.mcb.www;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.UrlEncoder;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;

import com.chare.entity.EntityWithId;
import com.chare.mcb.service.UserPreferences;

public abstract class AbstractListPanel<IdType, Type extends EntityWithId<IdType>> extends Panel {

	static final String LIST_ID = "list";
	static final String NAVIGATOR_ID = "navigator";
	static final String NAVIGATOR_LABEL_ID = "navigatorLabel";
	static final String SHOW_ALL_ID = "showAll";
	static final String EXPORT_TO_CSV_ID = "exportToCsv";

	static final boolean CREATE_NAVIGATOR = true;
	static final boolean SHOW_EXPORT_TO_CSV_LINK = true;
	static final boolean HIDE_EXPORT_TO_CSV_LINK = false;

	static final<T> IModel<List<T>> createEmptyModel(Class<T> itemClass) {
		return null;
	}

	@SpringBean
	protected UserPreferences userPreferences;

	public AbstractListPanel(String id, boolean hasNavigator, boolean showExportToCsvLink) {
		this(id, hasNavigator, null, showExportToCsvLink);
	}
	public AbstractListPanel(String id, boolean hasNavigator, IModel<List<Type>> listModel, boolean showExportToCsvLink) {
		super(id);
		SortableDataProvider<Type> dataProvider = createDataProvider(listModel);
		ListTable<IdType, Type> table = createList(LIST_ID, dataProvider);
		add(table);
		DataView<Type> dataView = table.getDataView();
		if (hasNavigator) {
			add(new PagingNavigator(NAVIGATOR_ID, dataView));
			add(new NavigatorLabel(NAVIGATOR_LABEL_ID, dataView));
			add(createShowAllLink(SHOW_ALL_ID, dataView));
			add(createExportToCsvLink(EXPORT_TO_CSV_ID, dataProvider, showExportToCsvLink));
		}
	}

	private Component createShowAllLink(String id, final DataView<Type> dataView) {
		return new Link<String>(id, new ResourceModel("showAll", "Zobrazit vše")) {

			@Override
			public void onClick() {
				dataView.setItemsPerPage(Integer.MAX_VALUE);
			}

		};
	}

	private Component createExportToCsvLink(String id, final IDataProvider<Type> dataProvider, final boolean visible) {
		Link<String> link = new Link<String>(id, new ResourceModel("exportToCsv", "Export do CSV")) {

			@Override
			public void onClick() {

				String separator = ";";
				String eol = "\r\n";

				StringBuffer buffer = new StringBuffer();
				String header = createCsvHeader(separator);
				if (header != null) {
					buffer.append(header);
					buffer.append(eol);
				}
				int size = dataProvider.size();
				Logger.getLogger(getClass()).info("Export " + size + " records");
				Iterator<? extends Type> iterator = dataProvider.iterator(1, size);
				while (iterator.hasNext()) {
					Type item = iterator.next();
					buffer.append(exportToCsv(item, separator));
					buffer.append(eol);
				}

				String fileName = UrlEncoder.QUERY_INSTANCE.encode("export_" + new Date().getTime() + ".csv", getRequest().getCharset());

				IResourceStream resourceStream = new StringResourceStream(buffer.toString(), "text/csv");
				ResourceStreamRequestHandler resourceStreamRequestHandler = new ResourceStreamRequestHandler(resourceStream);
				resourceStreamRequestHandler.setFileName(fileName);
				resourceStreamRequestHandler.setContentDisposition(ContentDisposition.ATTACHMENT);
				getRequestCycle().scheduleRequestHandlerAfterCurrent(resourceStreamRequestHandler);

			}

		};
		link.setVisible(visible);
		return link;
	}

	protected String createCsvHeader(String separator) {
		return null;
	}

	protected String formatCsvText(String text) {
		return "\"" + StringUtils.defaultString(text) + "\"";
	}
	protected String formatCsvInteger(Integer intVal) {
		return intVal == null ? "" : intVal.toString();
	}

	protected String formatCsvNumber(Number number) {
		return number == null ? "" : number.toString();//FIXME format
	}

	protected String formatCsvDate(Date date) {
		return date == null ? "" : new SimpleDateFormat("yyyy-MM-dd").format(date);
	}

	protected String formatCsvDateTime(Date date) {
		return date == null ? "" : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}

	protected String formatCsvTime(Date date) {
		return date == null ? "" : new SimpleDateFormat("HH:mm:ss").format(date);
	}

	protected String exportToCsv(Type item, String separator) {
		return item.toString();
	}

	protected abstract SortableDataProvider<Type> createDataProvider(IModel<List<Type>> listModel);

	protected abstract void populateItem(Item<Type> item, Type entity);


	private ListTable<IdType, Type> createList(String id, SortableDataProvider<Type> dataProvider) {

		ListTable<IdType, Type> table = new ListTable<IdType, Type>(id, dataProvider) {
			@Override
			protected void populateItem(Item<Type> item, Type entity) {
				super.populateItem(item, entity);
				AbstractListPanel.this.populateItem(item, entity);
			}
		};
		table.getDataView().setItemsPerPage(20);
		table.setOutputMarkupId(true);
		return table;
	}

	public static class Order extends OrderByBorder {
		private final DataView<?> dataView;
		public Order(String id, String property, ISortStateLocator stateLocator, DataView<?> dataView) {
			super(id, property, stateLocator);
			this.dataView = dataView;
		}
		@Override
		protected void onSortChanged() {
			dataView.setCurrentPage(0);
		}
	}

	protected final String formatDate(Date date) {
		return date == null ? "" : getDateFormat().format(date);
	}

	protected final NumberFormat getCurrencyNumberFormat() {
		return userPreferences.getUser().getCurrencyNumberFormat();
	}

	protected final String formatCurrencyNumber(Number number) {
		return number == null ? "" : getCurrencyNumberFormat().format(number);
	}

	protected final DateFormat getDateFormat() {
		return userPreferences.getUser().getDateFormat();
	}

	protected final String formatDateTime(Date date) {
		return date == null ? "" : getDateTimeFormat().format(date);
	}

	protected final DateFormat getDateTimeFormat() {
		return userPreferences.getUser().getDateTimeFormat();
	}

	protected final int getLanguageIndex() {
		return userPreferences.getLanguageIndex();
	}

	@SuppressWarnings("unchecked")
	public ListTable<IdType, Type> getTable() {
		return (ListTable<IdType, Type>) get(LIST_ID);
	}

}
