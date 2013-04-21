package com.chare.mcb.www;

import static org.mockito.Mockito.*;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.chare.mcb.entity.PostingFile;
import com.chare.mcb.entity.Role;
import com.chare.mcb.repository.PostingFileRepository;
import com.chare.mcb.service.PostingFileExporter;
import com.chare.mcb.www.PostingFilePage.PostingFilePanel;

public class PostingFilePageTest extends WicketTestCase {

	private PostingFilePage page;

	@Override
	@Before
	public void before() throws Exception {
		super.before();
		authenticateUser();
		user.addRole(Role.EXPORT_POSTINGS_ROLE);
	}

	private void startPage() {
		page = tester.startPage(PostingFilePage.class);
	}

	@Test
	public void testPageRendered() throws Exception {
		startPage();
		assertPageRenderedWithNoErrorOrInfoMessages(page.getClass());
	}

	@Test
	@Ignore
	// FIXME
	public void testPersistingPersistentUserRedirectsToUserList() throws Exception {
		PageParameters parameters = new PageParameters();
		parameters.set(PanelPage.ID_PARAM, "1");
		tester.startPage(UserPage.class, parameters);
		FormTester formTester = tester.newFormTester(getPath(UserPage.PANEL_ID, UserPage.USER_PANEL_ID, PanelPage.FORM_ID));
		formTester.submit();
		tester.assertRenderedPage(UserList.class);
	}

	@Test
	public void testContainsChildren() throws Exception {
		startPage();
		assertComponentIs(PostingFilePanel.class, PanelPage.PANEL_ID);
	}

	@Override
	protected Class<?> getCustomConfig() {
		return TestConfig.class;
	}

	@Configuration
	static class TestConfig {
		@Bean
		public PostingFileRepository statementRepository() {
			PostingFileRepository repository = mock(PostingFileRepository.class);
			when(repository.findById(1)).thenReturn(new PostingFile());
			return repository;
		}


		@Bean
		public PostingFileExporter postingFileExporter() {
			return mock(PostingFileExporter.class);
		}
	}

}