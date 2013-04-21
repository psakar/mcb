package com.chare.mcb.www;

import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.chare.mcb.entity.Role;
import com.chare.mcb.entity.Setting;
import com.chare.mcb.repository.SettingRepository;
import com.chare.wicket.Label;

@Secured(required = { Role.APP_ADMIN }, requiredAnyOf = {  })
public class SettingList extends PanelPage {

	public SettingList() {
		super(new PageParameters());
		add(new SettingListPanel(PANEL_ID));
	}

	@Override
	protected String getTitleCode() {
		return "settings.title";
	}

	static class SettingListPanel extends AbstractListPanel<Integer, Setting> {

		@SpringBean
		private SettingRepository repository;

		public SettingListPanel(String id) {
			super(id, CREATE_NAVIGATOR, HIDE_EXPORT_TO_CSV_LINK);
			addOrdering(getTable());
		}

		@Override
		public SortableDataProvider<Setting> createDataProvider(IModel<List<Setting>> listModel) {
			return new RepositorySortableDataProvider<Setting>(repository);
		}

		private void addOrdering(ListTable<Integer,Setting> table) {
			SortableDataProvider<Setting> dataProvider = table.getDataProvider();
			table.add(new BookmarkablePageLink<Void>("new", SettingPage.class));
			DataView<Setting> dataView = table.getDataView();
			table.add(new Order("code", "code", dataProvider, dataView));
			table.add(new Order("value", "value", dataProvider, dataView));
			table.add(new Order("type", "type", dataProvider, dataView));
			table.add(new Order("description1", "description1", dataProvider, dataView));
			table.add(new Order("description2", "description2", dataProvider, dataView));
			table.add(new Order("description3", "description3", dataProvider, dataView));
		}

		@Override
		public void populateItem(Item<Setting> item, Setting entity) {
			item.add(new BookmarkablePageLink<Void>("edit", SettingPage.class, createEditPageParameters(entity.getId())));
			item.add(new Label("code", entity.code));
			item.add(new Label("value", entity.value));
			item.add(new Label("type", String.valueOf(entity.type)));
			item.add(new Label("description1", entity.description1));
			item.add(new Label("description2", entity.description2));
			item.add(new Label("description3", entity.description3));
			item.add(new RemoveEntityLink<Integer, Setting>("remove", entity.getId(), repository, "errorDeleteSetting"));
		}

	}

}