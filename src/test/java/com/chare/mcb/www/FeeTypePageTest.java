package com.chare.mcb.www;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.Bean;

import com.chare.mcb.entity.Role;
import com.chare.mcb.repository.CardTypeRepository;
import com.chare.mcb.repository.FeeTypeRepository;
import com.chare.mcb.repository.TransferTypeRepository;
import com.chare.mcb.www.FeeTypePage.FeeTypePanel;

public class FeeTypePageTest extends WicketTestCase {

	private FeeTypePage page;

	@Override
	@Before
	public void before() throws Exception {
		super.before();
		authenticateUser();
		user.addRole(Role.APP_ADMIN_ROLE);
		startPage();
	}

	private void startPage() {
		page = tester.startPage(FeeTypePage.class);
	}

	@Test
	public void testPageRendered() throws Exception {
		assertPageRenderedWithNoErrorOrInfoMessages(page.getClass());
	}

	@Test
	public void testContainsChildren() throws Exception {
		assertComponentIs(FeeTypePanel.class, FeeTypePage.PANEL_ID);
	}

	@Test
	public void testIsSecured() throws Exception {
		assertPageIsSecured(FeeTypePage.class);
	}


	@Override
	protected Class<?> getCustomConfig() {
		return TestConfig.class;
	}

	static class TestConfig extends PageConfig {

		@Bean
		public CardTypeRepository CardTypeRepository() {
			return mock(CardTypeRepository.class);
		}

		@Bean
		public TransferTypeRepository transferTypeRepository() {
			return mock(TransferTypeRepository.class);
		}

		@Bean
		public FeeTypeRepository feeTypeRepository() {
			return mock(FeeTypeRepository.class);
		}
	}

}