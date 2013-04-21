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

import com.chare.mcb.entity.CardTransactionType;
import com.chare.mcb.entity.Role;
import com.chare.mcb.entity.SettlementType;
import com.chare.mcb.entity.TransferType;
import com.chare.mcb.repository.TransferTypeRepository;
import com.chare.wicket.Label;

@Secured(required = { Role.APP_ADMIN }, requiredAnyOf = {  })
public class TransferTypeList extends PanelPage {


	public TransferTypeList() {
		super(new PageParameters());
		add(new TransferTypeListPanel(PANEL_ID));
	}

	@Override
	protected IModel<String> createTitleModel() {
		return new ResourceModel("transferTypes.title");
	}

	static class TransferTypeListPanel extends AbstractListPanel<String, TransferType> {

		@SpringBean
		private TransferTypeRepository repository;

		public TransferTypeListPanel(String id) {
			super(id, CREATE_NAVIGATOR, HIDE_EXPORT_TO_CSV_LINK);
			addOrdering(getTable());
		}

		@Override
		public SortableDataProvider<TransferType> createDataProvider(IModel<List<TransferType>> listModel) {
			return new RepositorySortableDataProvider<TransferType>(repository);
		}

		private void addOrdering(ListTable<String, TransferType> table) {

			table.add(new BookmarkablePageLink<Void>("new", TransferTypePage.class));
			DataView<TransferType> dataView = table.getDataView();
			ISortStateLocator dataProvider = table.getDataProvider();
			table.add(new Order("code", "code", dataProvider, dataView));
			table.add(new Order("description1", "description.description1", dataProvider, dataView));
			table.add(new Order("description2", "description.description2", dataProvider, dataView));
			table.add(new Order("description3", "description.description3", dataProvider, dataView));
			table.add(new Order("settlementType", "settlementType", dataProvider, dataView));
			table.add(new Order("settlementAccount", "settlementAccount", dataProvider, dataView));
		}

		@Override
		public void populateItem(Item<TransferType> item, TransferType entity) {
			item.add(new BookmarkablePageLink<Void>("edit", TransferTypePage.class, createEditPageParameters(entity.getId())));
			item.add(new Label("code", entity.code));
			item.add(new Label("description1", entity.description.description1));
			item.add(new Label("description2", entity.description.description2));
			item.add(new Label("description3", entity.description.description3));
			item.add(new Label("settlementType", getSettlementTypeDescription(entity.settlementType)));
			item.add(new Label("settlementAccount", entity.settlementAccount));
			item.add(new RemoveEntityLink<String, TransferType>("remove", entity.getId(), repository, "errorDeleteTransferType"));
		}
	}

	static IModel<String> getSettlementTypeDescription(SettlementType settlementType) {
		return new ResourceModel("settlementType." + settlementType.ordinal());
	}

	static IModel<String> getCardTransactionTypeDescription(CardTransactionType cardTransactionType) {
		return new ResourceModel("cardTransactionType." + cardTransactionType.ordinal());
	}
}