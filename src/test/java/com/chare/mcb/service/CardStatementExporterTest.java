package com.chare.mcb.service;


import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;

import net.sf.jasperreports.engine.JRParameter;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.chare.core.TitleProperties;
import com.chare.core.Utils;
import com.chare.infrastructure.reportService.PrintRequest;
import com.chare.infrastructure.reportService.ReportServiceImpl.SystemParametersProvider;
import com.chare.mcb.entity.Card;
import com.chare.mcb.entity.CardStatement;
import com.chare.mcb.repository.SettingRepository;
import com.chare.mcb.service.CardStatementExporter.DateFactory;
import com.chare.mcb.service.reports.ReportFormatFactory;

public class CardStatementExporterTest {


	private CardStatementExporter exporter;

	@Mock
	private SettingRepository settingRepository;

	@Mock
	private DateFactory dateFactory;

	@Mock
	private SystemParametersProvider systemParametersProvider;

	private String dir;

	private File reportDirectory = new File("src/main/jasperreports");




	@Before
	public void before() throws Exception {
		MockitoAnnotations.initMocks(this);
		//systemParametersProvider = new SystemParametersProviderImpl(titleSources);
		exporter = new CardStatementExporter(reportDirectory, systemParametersProvider, settingRepository, dateFactory);

		dir = Utils.getTemporaryDirectory();
		when(settingRepository.getValue(SettingRepository.CARD_STMT_DIR, null)).thenReturn(dir);
		when(dateFactory.getNow()).thenReturn(new Date());

	}

	@Test
	public void testStatementExported() throws Exception {
		ReportTestCase.assertReportCompiles(CardStatementExporter.REPORT);
		ReportTestCase.assertReportCompiles(CardStatementExporter.REPORT_SUBREPORT);
		CardStatement statement = CardStatementFactoryForJasperReportDesigner.createBeanCollection().iterator().next();
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(JRParameter.REPORT_LOCALE, Locale.ENGLISH);
		parameters.put(JRParameter.REPORT_RESOURCE_BUNDLE, new TitleProperties("en", new Properties()));
		parameters.put(JRParameter.REPORT_FORMAT_FACTORY, new ReportFormatFactory());
		when(systemParametersProvider.get(any(PrintRequest.class))).thenReturn(parameters);
		exporter.export(statement);

		assertPdfExportedToExportDirectory(statement.card, 1);
	}

	private void assertPdfExportedToExportDirectory(Card card, Integer lastStatementNr) {
		String filename = exporter.createFilename(card, lastStatementNr);
		File exportedFile = new File(new File(dir), filename);
		assertTrue(exportedFile.exists());
	}



	@Test
	public void testDateTimeFormatted() throws Exception {
		Date date = Utils.getDate(2012, 1, 31);
		date = DateUtils.addHours(date, 3);
		date = DateUtils.addMinutes(date, 5);
		date = DateUtils.addSeconds(date, 8);
		assertEquals("20120131_030508", exporter.getDateTimeFormat().format(date));
	}

	@Test
	public void testCreateFilename() throws Exception {
		Card card = createCard();
		String year = Utils.getDatePart(Calendar.YEAR, new Date()) + "";
		String numberFormatted = "002";
		String dateTimeFormatted = exporter.getDateTimeFormat().format(new Date());
		assertEquals(card.number
				+ "_" + card.settlementAccount
				+ "_" + year
				+ "_" + numberFormatted
				+ "_" + dateTimeFormatted
				+ ".pdf", exporter.createFilename(card, 2));
	}

	@Test
	public void testCreateFilenameOnDemand() throws Exception {
		Card card = createCard();
		String year = Utils.getDatePart(Calendar.YEAR, new Date()) + "";
		String numberFormatted = "___";
		String dateTimeFormatted = exporter.getDateTimeFormat().format(new Date());
		assertEquals(card.number
				+ "_" + card.settlementAccount
				+ "_" + year
				+ "_" + numberFormatted
				+ "_" + dateTimeFormatted
				+ ".pdf", exporter.createFilename(card, null));
	}

	protected Card createCard() {
		Card card = new Card();
		card.number = "1234567890123456";
		card.settlementAccount = "197123456700EUR";
		card.lastStatementNr = 1;
		return card;
	}
}
