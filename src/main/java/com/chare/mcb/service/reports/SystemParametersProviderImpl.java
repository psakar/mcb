/**
 *
 */
package com.chare.mcb.service.reports;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.jasperreports.engine.JRParameter;

import com.chare.core.Titles.TitleSources;
import com.chare.infrastructure.reportService.PrintRequest;
import com.chare.infrastructure.reportService.ReportServiceImpl.SystemParametersProvider;

public class SystemParametersProviderImpl implements SystemParametersProvider {

	private final ReportFormatFactory reportFormatFactory;
	private final TitleSources titleSources;

	public SystemParametersProviderImpl(TitleSources titleSources) {
		this.titleSources = titleSources;
		reportFormatFactory = new ReportFormatFactory();
	}

	@Override
	public Map<String, Object> get(PrintRequest printRequest) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		Locale locale = printRequest.locale;
		parameters.put(JRParameter.REPORT_LOCALE, locale);
		parameters.put(JRParameter.REPORT_RESOURCE_BUNDLE, getResourceBundle(locale));
		parameters.put(JRParameter.REPORT_FORMAT_FACTORY, reportFormatFactory);
		return parameters ;
	}

	private ResourceBundle getResourceBundle(Locale locale) {
		return titleSources.get(locale.getLanguage()).getResourceBundle();
	}

}