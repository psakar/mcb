package com.chare.mcb.www;

import static org.mockito.Mockito.*;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.Bean;

import com.chare.mcb.entity.Role;
import com.chare.mcb.repository.TransferTypeRepository;
import com.chare.mcb.www.TransferTypePage.TransferTypePanel;

public class TransferTypePageTest extends WicketTestCase {

	private TransferTypePage page;

	@Override
	@Before
	public void before() throws Exception {
		super.before();
		authenticateUser();
		user.addRole(Role.APP_ADMIN_ROLE);
		startPage();
	}

	private void startPage() {
		page = tester.startPage(TransferTypePage.class, new PageParameters());
	}

	@Test
	public void testPageRendered() throws Exception {
		assertPageRenderedWithNoErrorOrInfoMessages(page.getClass());
	}


	@Test
	public void testContainsChildren() throws Exception {
		assertComponentIs(TransferTypePanel.class, TransferTypePage.PANEL_ID);
	}

	@Test
	public void testIsSecured() throws Exception {
		assertPageIsSecured(TransferTypePage.class);
	}


	@Override
	protected Class<?> getCustomConfig() {
		return TestConfig.class;
	}

	static class TestConfig extends PageConfig {
		@Bean
		public TransferTypeRepository rootAccountFullRepository() {
			return mock(TransferTypeRepository.class);
		}
	}

}