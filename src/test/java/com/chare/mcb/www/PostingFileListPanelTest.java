package com.chare.mcb.www;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import com.chare.mcb.entity.PostingFile;
import com.chare.mcb.entity.User;
import com.chare.mcb.repository.PostingFileRepository;
import com.chare.mcb.repository.UserRepository;
import com.chare.mcb.service.PostingFileExporter;
import com.chare.mcb.service.UserPreferences;
import com.chare.mcb.www.PostingFileList.PostingFileListPanel;
import com.chare.repository.Restriction;

public class PostingFileListPanelTest extends WicketTestCase {

	private PostingFileListPanel postingFilePanel;

	@Override
	@Before
	public void before() throws Exception {
		super.before();
		postingFilePanel = new PostingFileListPanel("id");
		tester.startComponentInPage(postingFilePanel);
	}

	@Test
	public void testPanelContents() throws Exception {
		assertNotNull(postingFilePanel.get(AbstractListPanel.LIST_ID));
		assertNotNull(postingFilePanel.get(AbstractListPanel.NAVIGATOR_ID));
		tester.assertNoErrorMessage();
	}

	@Test
	public void testListContents() throws Exception {
		tester.assertContains(TestConfig.entity.filename);
		tester.assertContains(TestConfig.entity.getCreatedUser().username);
		tester.assertNoErrorMessage();
	}

	@Test
	@Ignore
	// FIXME
	public void testViewLink() throws Exception {
		tester.clickLink("panel:component:component:" + AbstractListPanel.LIST_ID + ":rows:1:view");
		//		tester.assertRenderedPage(PostingFilePage.class);
	}

	@Test
	public void testIsSecured() throws Exception {
		assertPageIsSecured(PostingFileList.class);
	}

	@Override
	protected Class<?> getCustomConfig() {
		return TestConfig.class;
	}

	@Configuration
	static class TestConfig {
		public static PostingFile entity;

		@SuppressWarnings("unchecked")
		@Bean
		public PostingFileRepository PostingFileDataProvider() {
			entity = createPostingFile();

			List<PostingFile> list = Arrays.asList(entity);
			PostingFileRepository repository = mock(PostingFileRepository.class);
			when(repository.find(anyInt(), anyInt(), any(List.class), any(List.class))).thenReturn(list);
			when(repository.findById(anyInt())).thenReturn(entity);
			when(repository.getCount(anyListOf(Restriction.class))).thenReturn(1);

			return repository;
		}

		private PostingFile createPostingFile() {
			PostingFile file = new PostingFile();
			file.setId(1);
			file.setCreatedUser(new User(2, "username"));
			file.filename = "filename";
			return file;
		}


		@Bean
		public PostingFileExporter postingFileExporter() {
			return mock(PostingFileExporter.class);
		}

		@Bean
		public UserRepository userRepository() {
			return mock(UserRepository.class);
		}
		@Bean
		@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
		public UserPreferences userPreferences() {
			return new UserPreferences();
		}
	}

}
