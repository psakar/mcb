package com.chare.mcb.www;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

import org.apache.wicket.Component;
import org.apache.wicket.model.Model;
import org.junit.Test;
import org.springframework.context.annotation.Bean;

import com.chare.core.Utils;
import com.chare.mcb.entity.Statement;
import com.chare.mcb.entity.StatementLine;
import com.chare.mcb.repository.StatementRepository;
import com.chare.mcb.repository.TransferTypeRepository;
import com.chare.mcb.service.CardTransactionParser;
import com.chare.mcb.service.PostingFileGenerator;
import com.chare.mcb.service.TransferTypeResolver;
import com.chare.mcb.www.StatementPage.StatementPanel;
import com.chare.wicket.TextData;

public class StatementPanelTest extends WicketTestCase {


	@Test
	public void testRendering() throws Exception {
		Statement displayedEntity = createDisplayedEntity();
		StatementPanel panel = createPanel(displayedEntity);
		tester.startComponentInPage(panel);
		tester.assertNoErrorMessage();
		assertDisplaysEntity(displayedEntity);
	}

	private void assertDisplaysEntity(Statement entity) {
		tester.assertContains(entity.number);
		tester.assertContains(entity.sourceFilename);
		tester.assertContains(userPreferences.getILocale().getDateFormat().format(entity.statementDate));
		tester.assertContains(userPreferences.getILocale().getCurrencyNumberFormat().format(entity.openingBalance));
		tester.assertContains(userPreferences.getILocale().getCurrencyNumberFormat().format(entity.closingBalance));
		for (StatementLine line : entity.getLines()) {
			tester.assertContains(line.number + "");
			tester.assertContains(userPreferences.getILocale().getCurrencyNumberFormat().format(line.amount));
			tester.assertContains(userPreferences.getILocale().getDateFormat().format(line.valueDate));
			tester.assertContains(line.details1);
			tester.assertContains(line.details2);
			tester.assertContains(line.details3);
			tester.assertContains(line.details4);
			tester.assertContains(line.reference1);
			tester.assertContains(line.reference2);
			tester.assertContains(line.swiftType);
		}
	}

	private StatementPanel createPanel(Statement entity) {
		return new StatementPanel("id", new Model<Statement>(entity));
	}

	private Statement createDisplayedEntity() {
		Statement entity = new Statement();
		entity.setId(4);
		entity.number = "number";
		entity.sourceFilename = "sourceFilename";
		entity.statementDate = Utils.getYesterday();
		entity.openingBalance = new BigDecimal("123.45");
		entity.closingBalance = new BigDecimal("-789.06");
		StatementLine line = entity.addLine();
		line.amount = new BigDecimal("5544.11");
		line.details1 = "details1";
		line.details2 = "details2";
		line.details3 = "details3";
		line.details4 = "details4";
		line.valueDate = Utils.getTomorrow();
		line.reference1 = "reference1";
		line.reference2 = "reference2";
		line.swiftType = "SWFT";
		return entity;
	}

	@Test
	public void testContainsChildren() throws Exception {
		StatementPanel panel = createPanel(createDisplayedEntity());
		tester.startComponentInPage(panel);
		tester.assertNoErrorMessage();
		assertEquals(TextData.class, getComponentClass(panel, StatementPanel.NUMBER_ID));
		assertEquals(TextData.class, getComponentClass(panel, StatementPanel.STATEMENT_DATE_ID));
		assertEquals(TextData.class, getComponentClass(panel, StatementPanel.SOURCE_FILENAME_ID));
		assertEquals(TextData.class, getComponentClass(panel, StatementPanel.OPENING_BALANCE_ID));
		assertEquals(TextData.class, getComponentClass(panel, StatementPanel.CLOSING_BALANCE_ID));

	}

	private Class<? extends Component> getComponentClass(Component panel, String componentId) {
		return panel.get(getPath(componentId)).getClass();
	}


	@Override
	protected Class<?> getCustomConfig() {
		return TestConfig.class;
	}

	static class TestConfig extends PageConfig {
		@Bean
		public CardTransactionParser cardTransactionParser() {
			return mock(CardTransactionParser.class);
		}


		@Bean
		public PostingFileGenerator postingFileGenerator() {
			return mock(PostingFileGenerator.class);
		}

		@Bean
		public StatementRepository statementRepository() {
			return mock(StatementRepository.class);
		}

		@Bean
		public TransferTypeRepository transferTypeRepository() {
			return mock(TransferTypeRepository.class);
		}

		@Bean
		public TransferTypeResolver transferTypeResolver() {
			return mock(TransferTypeResolver.class);
		}
	}

}
