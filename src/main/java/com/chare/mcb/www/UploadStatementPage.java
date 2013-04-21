package com.chare.mcb.www;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.chare.core.IConfiguration;
import com.chare.core.Utils;
import com.chare.mcb.Application;
import com.chare.mcb.entity.Role;
import com.chare.mcb.service.UploadFileService;


@Secured(required = { Role.UPLOAD_STATEMENTS }, requiredAnyOf = { })
public class UploadStatementPage extends PanelPage {

	static final String KEY_DIR_UPLOAD = "DIR_UPLOAD";

	public UploadStatementPage(PageParameters parameters) {
		super(parameters);
		add(createPanel());
	}

	private Component createPanel() {
		return new UploadPanel(PANEL_ID);
	}

	@Override
	protected String getTitleCode() {
		return "uploadStatement.title";
	}

	static class UploadPanel extends org.apache.wicket.markup.html.panel.Panel {

		static final String FILE_ID = "file";

		private FileUploadField fileUploadField;

		@SpringBean
		private IConfiguration configuration;
		@SpringBean
		private UploadFileService uploadFileService;

		public UploadPanel(String id) {
			super(id);
			add(createForm());
		}

		protected Form<Void> createForm() {

			Form<Void> form = new Form<Void>(FORM_ID, null) {
				@Override
				protected void onSubmit() {
					UploadPanel.this.onSubmit();
				};
			};

			form.add(new Label("fileLabel", new ResourceModel(FILE_ID, "File")));

			fileUploadField = new FileUploadField(FILE_ID);
			form.add(fileUploadField);

			Button button = new Button("button", new ResourceModel("import", "Import")) {
				@Override
				public void onSubmit() {
					UploadPanel.this.onSubmit();
				};
			};
			form.add(button);
			form.setMultiPart(true);
			return form;
		}

		protected void onSubmit() {
			final FileUpload upload = fileUploadField.getFileUpload();
			if (upload == null) {
				log.debug("Upload empty");
				return;
			}


			String directory = Utils.addPathSeparator(configuration.getParameter(Application.name, KEY_DIR_UPLOAD, Utils.getTemporaryDirectory()));
			File directoryFile = new File(directory);
			if (!directoryFile.exists()) {
				error(new StringResourceModel("errorUploadStatement", null, "Upload directory ${0} is not accessible.", directory).getObject());
				return;
			}
			String filename = upload.getClientFileName();
			FileInputStream inputStream = null;
			try {
				File file = new File(directoryFile, filename);
				upload.writeTo(file);
				inputStream = new FileInputStream(file);
				uploadFileService.uploadFile(inputStream, filename);
			} catch (Exception e) {
				Logger.getLogger(getClass()).error("Upload statement error " + e.getMessage(), e);
				error(new StringResourceModel("error", null, "Error ${0}", e.getMessage()).getObject());
			} finally {
				IOUtils.closeQuietly(inputStream);
			}

		}

	}
}