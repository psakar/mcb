package com.chare.mcb.www;

import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.chare.mcb.entity.PostingFile;
import com.chare.mcb.entity.Role;
import com.chare.mcb.repository.PostingFileRepository;
import com.chare.mcb.service.PostingFileExporter;
import com.chare.mcb.service.UserPreferences;
import com.chare.wicket.Label;

@Secured(required = { Role.EXPORT_POSTINGS }, requiredAnyOf = {  })
public class PostingFileList extends PanelPage {

	public PostingFileList() {
		super(new PageParameters());
		add(new PostingFileListPanel(PANEL_ID));
	}

	@Override
	protected String getTitleCode() {
		return "postingFiles.title";
	}

	static class PostingFileListPanel extends AbstractListPanel<Integer, PostingFile> {

		static final String CREATED_ID = "created";
		static final String CREATED_USERNAME_ID = "createdUsername";
		static final String EXPORTED_ID = "exported";
		static final String EXPORTED_USERNAME_ID = "exportedUsername";

		static final String FILENAME_ID = "filename";
		static final String EXPORT_ID = "export";
		static final String DIAPPROVE_ID = "disapprove";
		@SpringBean
		private PostingFileRepository repository;

		public PostingFileListPanel(String id) {
			super(id, CREATE_NAVIGATOR, HIDE_EXPORT_TO_CSV_LINK);
			addOrdering(getTable());
		}

		@Override
		public SortableDataProvider<PostingFile> createDataProvider(IModel<List<PostingFile>> listModel) {
			return new RepositorySortableDataProvider<PostingFile>(repository);
		}

		private void addOrdering(ListTable<Integer,PostingFile> table) {
			SortableDataProvider<PostingFile> dataProvider = table.getDataProvider();
			//table.add(new BookmarkablePageLink<Void>("new", PostingFilePage.class));
			DataView<PostingFile> dataView = table.getDataView();
			table.add(new Order("filename", "filename", dataProvider, dataView));
			table.add(new Order("created", "created", dataProvider, dataView));
			table.add(new Order("createdUsername", "createdUser.username", dataProvider, dataView));
			table.add(new Order("exported", "approved", dataProvider, dataView));
			table.add(new Order("exportedUsername", "approvedUser.username", dataProvider, dataView));
		}

		@Override
		public void populateItem(Item<PostingFile> item, PostingFile entity) {
			//			item.add(new BookmarkablePageLink<Void>("edit", PostingFilePage.class, createEditPageParameters(entity.getId())));
			item.add(new Label(FILENAME_ID, entity.filename));
			item.add(new Label(CREATED_ID, formatDateTime(entity.getCreated())));
			item.add(new Label(CREATED_USERNAME_ID, entity.getCreatedUser().username));
			item.add(new Label(EXPORTED_ID, formatDateTime(entity.getApproved())));
			item.add(new Label(EXPORTED_USERNAME_ID, ( entity.getApprovedUser() == null ? "" : entity.getApprovedUser().username)));
			ExportPostingFileLink generateLink = new ExportPostingFileLink(EXPORT_ID, entity.getId(), repository, "errorExportDeletePostingFile");
			boolean userCanApproveOrDisapprove = entity.canBeApprovedBy(userPreferences.getUser());
			generateLink.setVisible(userCanApproveOrDisapprove);
			item.add(generateLink);
			DisapprovePostingFileLink disapproveLink = new DisapprovePostingFileLink(DIAPPROVE_ID, entity.getId(), repository, "errorExportDeletePostingFile");
			disapproveLink.setVisible(userCanApproveOrDisapprove);
			item.add(disapproveLink);
			//item.add(new RemoveEntityLink<Integer, PostingFile>("remove", entity.getId(), repository, "errorDeletePostingFile"));
		}

	}
	static class ExportPostingFileLink extends EntityLink<Integer, PostingFile> {

		@SpringBean
		private UserPreferences userPreferences;

		@SpringBean
		private PostingFileExporter exporter;

		public ExportPostingFileLink(String id, Integer entityId, PostingFileRepository repository, String error) {
			super(id, entityId, repository, error);
		}

		@Override
		protected void onClick(PostingFile postingFile) {
			exporter.export(postingFile, userPreferences.getUser());
		}
	}
	static class DisapprovePostingFileLink extends EntityLink<Integer, PostingFile> {

		@SpringBean
		private UserPreferences userPreferences;

		@SpringBean
		private PostingFileExporter exporter;

		public DisapprovePostingFileLink(String id, Integer entityId, PostingFileRepository repository, String error) {
			super(id, entityId, repository, error);
		}

		@Override
		protected void onClick(PostingFile postingFile) {
			exporter.disapprove(postingFile, userPreferences.getUser());
		}
	}


}