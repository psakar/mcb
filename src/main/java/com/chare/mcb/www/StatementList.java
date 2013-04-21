package com.chare.mcb.www;

import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.chare.mcb.entity.Role;
import com.chare.mcb.entity.Statement;
import com.chare.mcb.entity.StatementItem;
import com.chare.mcb.repository.StatementItemRepository;
import com.chare.mcb.repository.StatementRepository;
import com.chare.repository.Sort;
import com.chare.wicket.Label;
import com.chare.wicket.TextData;

@Secured(required = { Role.UPLOAD_STATEMENTS }, requiredAnyOf = {  })
public class StatementList extends PanelPage {

	public StatementList() {
		super(new PageParameters());
		add(new StatementListPanel(PANEL_ID));
	}

	@Override
	protected String getTitleCode() {
		return "statements.title";
	}

	static class StatementListPanel extends AbstractListPanel<Integer, StatementItem> {

		@SpringBean
		private StatementItemRepository repository;

		@SpringBean
		private StatementRepository entityRepository;

		public StatementListPanel(String id) {
			super(id, CREATE_NAVIGATOR, HIDE_EXPORT_TO_CSV_LINK);
			addOrdering(getTable());
		}

		@Override
		public SortableDataProvider<StatementItem> createDataProvider(IModel<List<StatementItem>> listModel) {
			RepositorySortableDataProvider<StatementItem> dataProvider = new RepositorySortableDataProvider<StatementItem>(repository);
			dataProvider.addSort(new Sort("year", Sort.DOWN));
			dataProvider.addSort(new Sort("number", Sort.DOWN));
			return dataProvider;
		}

		private void addOrdering(ListTable<Integer,StatementItem> table) {
			ISortStateLocator dataProvider = table.getDataProvider();
			table.add(new BookmarkablePageLink<Void>("uploadStatement", UploadStatementPage.class));
			table.add(new OrderByBorder("year", "year", dataProvider));
			table.add(new OrderByBorder("number", "number", dataProvider));
			table.add(new OrderByBorder("postingFile", "postingFileId", dataProvider));
			//			table.add(new OrderByBorder("cardType", "cardType.description" + getLanguageIndex(), dataProvider));
		}

		@Override
		public void populateItem(Item<StatementItem> item, StatementItem entity) {
			Integer id = entity.getId();
			item.add(new BookmarkablePageLink<Void>("edit", StatementPage.class, createEditPageParameters(id)));
			item.add(new Label("year", entity.year + ""));
			item.add(new Label("number", entity.number));
			//			item.add(new Label("cardType", entity.cardType.description.getDescription(getLanguageIndex())));
			//			item.add(new Label("activeFrom", formatDate(entity.activeFrom)));
			//			item.add(new Label("statementPeriod",  getStatementPeriodDescription(entity.statementPeriod)));

			PageParameters parameters = new PageParameters();
			boolean hasPostingFile = (entity.postingFileId != null);
			if (hasPostingFile)
				parameters.add(ID_PARAM, entity.postingFileId);
			BookmarkablePageLink<String> postingFileLink = new BookmarkablePageLink<String>("postingFile", PostingFilePage.class, parameters);
			postingFileLink.setVisible(hasPostingFile);
			postingFileLink.add(new TextData("filename", entity.postingFilename));
			item.add(postingFileLink);
			item.add(new RemoveEntityLink<Integer, Statement>("remove", id, entityRepository, "errorDeleteStatement"));
		}

	}


}