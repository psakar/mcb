package com.chare.mcb.www;

import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.chare.mcb.entity.CardType;
import com.chare.mcb.entity.Role;
import com.chare.mcb.repository.CardTypeRepository;
import com.chare.wicket.Label;

@Secured(required = { Role.APP_ADMIN }, requiredAnyOf = {  })
public class CardTypeList extends PanelPage {

	public CardTypeList() {
		super(new PageParameters());
		add(new CardTypeListPanel(PANEL_ID));
	}

	@Override
	protected IModel<String> createTitleModel() {
		return new ResourceModel("cardTypes.title");
	}

	static class CardTypeListPanel extends AbstractListPanel<String, CardType> {

		@SpringBean
		private CardTypeRepository repository;

		public CardTypeListPanel(String id) {
			super(id, CREATE_NAVIGATOR, HIDE_EXPORT_TO_CSV_LINK);
			addOrdering(getTable());
		}

		@Override
		public SortableDataProvider<CardType> createDataProvider(IModel<List<CardType>> listModel) {
			return new RepositorySortableDataProvider<CardType>(repository);
		}

		private void addOrdering(ListTable<String, CardType> table) {
			table.add(new BookmarkablePageLink<Void>("new", CardTypePage.class));
			DataView<CardType> dataView = table.getDataView();
			ISortStateLocator dataProvider = table.getDataProvider();
			table.add(new Order("code", "code", dataProvider, dataView));
			table.add(new Order("description1", "description.description1", dataProvider, dataView));
			table.add(new Order("description2", "description.description2", dataProvider, dataView));
			table.add(new Order("description3", "description.description3", dataProvider, dataView));
		}

		@Override
		public void populateItem(Item<CardType> item, CardType entity) {
			item.add(new BookmarkablePageLink<Void>("edit", CardTypePage.class, createEditPageParameters(entity.getId())));
			item.add(new Label("code", entity.code));
			item.add(new Label("description1", entity.description.description1));
			item.add(new Label("description2", entity.description.description2));
			item.add(new Label("description3", entity.description.description3));
			item.add(new RemoveEntityLink<String, CardType>("remove", entity.getId(), repository, "errorDeleteCardType"));
		}
	}

}