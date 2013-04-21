package com.chare.mcb.www;

import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.chare.mcb.entity.FeeType;
import com.chare.mcb.entity.Role;
import com.chare.mcb.repository.FeeTypeRepository;
import com.chare.mcb.service.UserPreferences;
import com.chare.wicket.Label;

@Secured(required = { Role.APP_ADMIN }, requiredAnyOf = {  })
public class FeeTypeList extends PanelPage {

	public FeeTypeList(PageParameters pageParameters) {
		super(pageParameters);
		add(new FeeTypeListPanel(PANEL_ID));
	}

	@Override
	protected IModel<String> createTitleModel() {
		return new ResourceModel("feeTypes.title");
	}

	static class FeeTypeListPanel extends AbstractListPanel<String, FeeType> {

		@SpringBean
		private FeeTypeRepository repository;
		@SpringBean
		private UserPreferences userPreferences;

		public FeeTypeListPanel(String id) {
			super(id, CREATE_NAVIGATOR, HIDE_EXPORT_TO_CSV_LINK);
			addOrdering(getTable());
		}

		@Override
		public SortableDataProvider<FeeType> createDataProvider(IModel<List<FeeType>> listModel) {
			return new RepositorySortableDataProvider<FeeType>(repository);
		}

		private void addOrdering(ListTable<String, FeeType> table) {
			SortableDataProvider<FeeType> dataProvider = table.getDataProvider();
			table.add(new BookmarkablePageLink<Void>("new", FeeTypePage.class));
			DataView<FeeType> dataView = table.getDataView();
			table.add(new Order("code", "code", dataProvider, dataView));
			table.add(new Order("description1", "description.description1", dataProvider, dataView));
			table.add(new Order("description2", "description.description2", dataProvider, dataView));
			table.add(new Order("description3", "description.description3", dataProvider, dataView));
			table.add(new Order("cardType", "cardType.description.description" + userPreferences.getLanguageIndex(), dataProvider, dataView));
			table.add(new Order("amount", "calculation.amount", dataProvider, dataView));
			table.add(new Order("percentage", "calculation.percentage", dataProvider, dataView));
			table.add(new Order("settlementAccount", "settlementAccount", dataProvider, dataView));
			table.add(new Order("transferType", "transferType.description.description" + userPreferences.getLanguageIndex(), dataProvider, dataView));
		}
		/*
      cardType
      amount
      percentage
      settlementAccount
      transferType
		 */
		@Override
		public void populateItem(Item<FeeType> item, FeeType entity) {
			item.add(new BookmarkablePageLink<Void>("edit", FeeTypePage.class, createEditPageParameters(entity.getId())));
			item.add(new Label("code", entity.code));
			item.add(new Label("description1", entity.description.description1));
			item.add(new Label("description2", entity.description.description2));
			item.add(new Label("description3", entity.description.description3));
			int languageIndex = userPreferences.getLanguageIndex();
			item.add(new Label("cardType", entity.cardType.description.getDescription(languageIndex)));
			item.add(new Label("amount", userPreferences.getILocale().getCurrencyFormat().format(entity.calculation.amount)));
			item.add(new Label("percentage", userPreferences.getILocale().getPercentFormat().format(entity.calculation.percentage)));
			item.add(new Label("settlementAccount", entity.settlementAccount));
			item.add(new Label("transferType", entity.transferType.code + " - " + entity.transferType.description.getDescription(languageIndex)));
			item.add(new RemoveEntityLink<String, FeeType>("remove", entity.getId(), repository, "errorDeleteFeeType"));
		}
	}

}