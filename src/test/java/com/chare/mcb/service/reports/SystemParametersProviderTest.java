package com.chare.mcb.service.reports;

import static org.junit.Assert.*;

import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import net.sf.jasperreports.engine.JRParameter;

import org.junit.Test;

import com.chare.core.CustomLocale;
import com.chare.core.TitleProperties;
import com.chare.core.Titles.TitleSources;
import com.chare.infrastructure.reportService.PrintRequest;

public class SystemParametersProviderTest {
	static final Locale TEST_LOCALE = Locale.ENGLISH;
	static final PrintRequest TEST_PRINT_REQUEST = new PrintRequest("reportName" , PrintRequest.NO_PARAMETERS, TEST_LOCALE, PrintRequest.NO_PRINTER, PrintRequest.NO_EXPORT, false, null);

	@Test
	public void testParametersAreSet() throws Exception {
		TitleProperties titles = createMockTitles(TEST_LOCALE);
		SystemParametersProviderImpl systemParametersProvider = new SystemParametersProviderImpl(createMockTitleSources(titles));

		Map<String, Object> systemParameters = systemParametersProvider.get(TEST_PRINT_REQUEST);

		assertEquals(titles, systemParameters.get(JRParameter.REPORT_RESOURCE_BUNDLE));
		assertEquals(TEST_PRINT_REQUEST.locale, systemParameters.get(JRParameter.REPORT_LOCALE));
		assertEquals(ReportFormatFactory.class, systemParameters.get(JRParameter.REPORT_FORMAT_FACTORY).getClass());
	}

	public static TitleSources createMockTitleSources() {
		return createMockTitleSources(Locale.ENGLISH, Locale.GERMAN, CustomLocale.LOCALE_CZ);
	}

	public static TitleSources createMockTitleSources(Locale ... locales) {
		TitleSources titleSources = new TitleSources();
		for (Locale locale : locales)
			addLocaleToMockTitleSources(locale, titleSources);
		return titleSources;
	}

	public static TitleSources createMockTitleSources(TitleProperties titleProperties) {
		TitleSources titleSources = new TitleSources();
		titleSources.put(titleProperties.getLanguageCode(), titleProperties);
		return titleSources;
	}

	public static void addLocaleToMockTitleSources(Locale locale, TitleSources titleSources) {
		titleSources.put(locale.getLanguage(), createMockTitles(locale));
	}

	public static TitleProperties createMockTitles(Locale locale) {
		TitleProperties titleProperties = new TitleProperties(locale.getLanguage(), new Properties());
		return titleProperties;
	}

}
