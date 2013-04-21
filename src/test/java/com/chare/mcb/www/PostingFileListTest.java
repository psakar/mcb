package com.chare.mcb.www;


import org.junit.Before;
import org.junit.Test;

import com.chare.mcb.entity.Role;

public class PostingFileListTest extends WicketTestCase {

	private PostingFileList page;

	@Override
	@Before
	public void before() throws Exception {
		super.before();
		authenticateUser();
		user.addRole(Role.EXPORT_POSTINGS_ROLE);
		startPage();
	}

	private void startPage() {
		page = tester.startPage(PostingFileList.class);
	}

	@Test
	public void testPageRendered() throws Exception {
		assertPageRenderedWithNoErrorOrInfoMessages(page.getClass());
	}

	@Override
	protected Class<?> getCustomConfig() {
		return PostingFileListPanelTest.TestConfig.class;
	}

}
