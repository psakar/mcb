package com.chare.mcb.www;


import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.InputStream;

import org.apache.wicket.util.file.File;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.chare.core.IConfiguration;
import com.chare.mcb.Application;
import com.chare.mcb.entity.Role;
import com.chare.mcb.service.UploadFileService;
import com.chare.mcb.www.UploadStatementPage.UploadPanel;

public class UploadStatementPageTest extends WicketTestCase {

	@Override
	@Before
	public void before() throws Exception {
		super.before();
		authenticateUser();
		user.addRole(Role.UPLOAD_STATEMENTS_ROLE);
	}

	@Test
	public void testDisplay() {
		assertPageIsRendered(UploadStatementPage.class);
	}

	@Test
	public void testPageIsSecured() {
		assertPageIsSecured(UploadStatementPage.class);
	}

	@Test
	public void testUploadedFileIsProcessed() throws Exception {
		tester.startPage(UploadStatementPage.class);
		FormTester formTester = tester.newFormTester(convertComponentIdsToPath(PanelPage.PANEL_ID, PanelPage.FORM_ID));
		File file = new File("src/test/files/empty.txt");
		formTester.setFile(UploadPanel.FILE_ID, file, "text/plain");

		formTester.submit();

		UploadFileService service = applicationContext.getBean(UploadFileService.class);
		verify(service, times(1)).uploadFile(any(InputStream.class), eq(file.getName()));

	}

	@Override
	protected Class<?> getCustomConfig() {
		return PageConfig.class;
	}

	@Configuration
	protected static class PageConfig {
		@Bean
		public UploadFileService uploadFileService() {
			return mock(UploadFileService.class);
		}

		@Bean
		public IConfiguration configuration() {
			com.chare.core.Configuration configuration = new com.chare.core.Configuration();
			configuration.getProperties().setProperty(com.chare.core.Configuration.createKey(Application.name, UploadStatementPage.KEY_DIR_UPLOAD), ".");
			return configuration;
		}
	}
}
