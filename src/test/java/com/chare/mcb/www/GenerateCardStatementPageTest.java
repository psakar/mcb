package com.chare.mcb.www;


import static org.mockito.Mockito.*;

import java.util.Date;

import org.apache.wicket.util.tester.FormTester;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.chare.core.Utils;
import com.chare.mcb.entity.Role;
import com.chare.mcb.service.CardStatementGenerator;
import com.chare.mcb.www.GenerateCardStatementPage.CardStatementParametersPanel;

public class GenerateCardStatementPageTest extends WicketTestCase {

	@Override
	@Before
	public void before() throws Exception {
		super.before();
		authenticateUser();
		user.addRole(Role.APP_ADMIN_ROLE);
	}

	@Test
	public void testDisplay() {
		assertPageIsRendered(GenerateCardStatementPage.class);
	}

	@Test
	public void testPageIsSecured() {
		assertPageIsSecured(GenerateCardStatementPage.class);
	}

	@Test
	public void testGenerateedFileIsProcessed() throws Exception {
		tester.startPage(GenerateCardStatementPage.class);
		FormTester formTester = tester.newFormTester(convertComponentIdsToPath(PanelPage.PANEL_ID, PanelPage.FORM_ID));
		String cardNumber = "1234567890123456";
		Date dateFrom = Utils.getDate(2012, 1, 1);
		Date dateTo = Utils.getDate(2012, 1, 31);
		Integer number = 1;

		formTester.setValue(CardStatementParametersPanel.CARD_NUMBER_ID, cardNumber);
		formTester.setValue(CardStatementParametersPanel.DATE_FROM_ID, user.getDateFormat().format(dateFrom));
		formTester.setValue(CardStatementParametersPanel.DATE_TO_ID, user.getDateFormat().format(dateTo));
		formTester.setValue(CardStatementParametersPanel.STATEMENT_NUMBER_ID, number + "");

		//		Logger.getLogger("org.apache.wicket").setLevel(Level.DEBUG)
		formTester.submit();

		CardStatementGenerator service = applicationContext.getBean(CardStatementGenerator.class);
		verify(service, times(1)).generateOnDemand(cardNumber, dateFrom, dateTo, number);

	}

	@Override
	protected Class<?> getCustomConfig() {
		return PageConfig.class;
	}

	@Configuration
	protected static class PageConfig {

		@Bean
		public CardStatementGenerator cardStatementGenerator() {
			return mock(CardStatementGenerator.class);
		}
	}
}
