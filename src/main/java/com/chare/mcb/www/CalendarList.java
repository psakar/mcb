package com.chare.mcb.www;

import java.util.Date;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.chare.mcb.entity.Calendar;
import com.chare.mcb.entity.Role;
import com.chare.mcb.repository.CalendarRepository;
import com.chare.wicket.Label;

@Secured(required = { Role.APP_ADMIN }, requiredAnyOf = {  })
public class CalendarList extends PanelPage {

	public CalendarList() {
		super(new PageParameters());
		add(new CalendarListPanel(PANEL_ID));
	}

	@Override
	protected String getTitleCode() {
		return "calendar.title";
	}

	static class CalendarListPanel extends AbstractListPanel<Integer, Calendar> {

		@SpringBean
		private CalendarRepository repository;

		public CalendarListPanel(String id) {
			super(id, CREATE_NAVIGATOR, HIDE_EXPORT_TO_CSV_LINK);
			addOrdering(getTable());
		}

		@Override
		public SortableDataProvider<Calendar> createDataProvider(IModel<List<Calendar>> listModel) {
			return new RepositorySortableDataProvider<Calendar>(repository);
		}

		private void addOrdering(ListTable<Integer,Calendar> table) {
			SortableDataProvider<Calendar> dataProvider = table.getDataProvider();
			DataView<Calendar> dataView = table.getDataView();
			table.add(new Order("id_date", "id_date", dataProvider, dataView));
			table.add(new Order("holiday", "holiday", dataProvider, dataView));
		}

		@Override
		public void populateItem(Item<Calendar> item, Calendar entity) {
			item.add(new Label("id_date", formatDate(entity.id_date)));
			item.add(new Label("holiday", (entity.holiday == 1 ? "X" : "")));
			item.add(new ChangeHolidayLink("changeHoliday", entity.id_date, repository));
		}

		static class ChangeHolidayLink extends org.apache.wicket.markup.html.link.Link<Void> {

			protected CalendarRepository repository;
			protected final Date entityId;

			public ChangeHolidayLink(String id, Date entityId, CalendarRepository repository) {
				super(id);
				this.entityId = entityId;
				this.repository = repository;
			}

			@Override
			public void onClick() {
				repository.changeHolidayValue(entityId);
			}
		}

	}

}