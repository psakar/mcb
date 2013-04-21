package com.chare.mcb.service;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

import org.apache.commons.lang.StringUtils;

import com.chare.core.Utils;
import com.chare.infrastructure.reportService.PrintRequest;
import com.chare.infrastructure.reportService.ReportServiceException;
import com.chare.infrastructure.reportService.ReportServiceImpl;
import com.chare.infrastructure.reportService.ReportServiceImpl.SystemParametersProvider;
import com.chare.mcb.Application;
import com.chare.mcb.entity.Card;
import com.chare.mcb.entity.CardStatement;
import com.chare.mcb.repository.SettingRepository;
import com.chare.reports.ReportFileResolver;

public class CardStatementExporter {

	static final String REPORT = "cardStatement";

	public static final String REPORT_SUBREPORT = "cardStatementLine";

	//	private static final String PARAMETER_LANGUAGE_INDEX = "languageIndex";

	private final SettingRepository settingRepository;
	private final File reportDirectory;
	private final SystemParametersProvider systemParametersProvider;

	private final DateFactory dateFactory;

	public CardStatementExporter(File reportDirectory, SystemParametersProvider systemParametersProvider, SettingRepository settingRepository, DateFactory dateFactory) {
		this.reportDirectory = reportDirectory;
		this.systemParametersProvider = systemParametersProvider;
		this.settingRepository = settingRepository;
		this.dateFactory = dateFactory;
	}

	public File export(CardStatement statement) {
		File dir = findCardStatementsDirectory();
		File exportFile = new File(dir, createFilename(statement.card, statement.number));
		exportToPdf(statement, convertToLocale(statement.card.languageId), exportFile);
		return exportFile;
	}

	private Locale convertToLocale(int languageId) {
		return Application.findLocale(languageId);
	}

	protected File findCardStatementsDirectory() {
		String dirName = settingRepository.getValue(SettingRepository.CARD_STMT_DIR, null);
		if (StringUtils.isBlank(dirName))
			throw new IllegalArgumentException("Directory for card statements not setup in settings");
		File dir = new File(dirName);
		if (!(dir.exists() && dir.isDirectory()))
			throw new IllegalArgumentException("Directory for card statements not accessible " + dir.getAbsolutePath());
		return dir;
	}

	String createFilename(Card card, Integer statementNumber) {
		String year = Utils.getDatePart(Calendar.YEAR, new Date()) + "";
		String numberFormatted = (statementNumber ==null ? "___" : StringUtils.leftPad("" + statementNumber, 3, '0'));
		return card.number + "_" + card.settlementAccount + "_" + year + "_" + numberFormatted + "_" + getDateTimeFormat().format(dateFactory.getNow()) + ".pdf";
	}

	DateFormat getDateTimeFormat() {
		return new SimpleDateFormat("yyyyMMdd_hhmmss");
	}

	private File exportToPdf(CardStatement cardStatement, Locale locale, File exportFile) {
		try {
			Map<String, Object> parameters = createParameters(locale);
			File reportFile = getReportFile(REPORT);
			JasperPrint jasperPrint = JasperFillManager.fillReport(reportFile.getAbsolutePath(), parameters, new JRBeanArrayDataSource(Arrays.asList(cardStatement).toArray(), false));
			new ReportServiceImpl.ReportPdfExporter().export(jasperPrint, exportFile);
			return exportFile;
		} catch (ReportServiceException e) {
			throw new IllegalStateException("Generate report failed " + e.getMessage(), e);
		} catch (JRException e) {
			throw new IllegalStateException("Generate report failed " + e.getMessage(), e);
		}
	}

	private Map<String, Object> createParameters(Locale locale) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.putAll(systemParametersProvider.get(createPrintRequestWithLocale(locale)));
		//		parameters.put(PARAMETER_LANGUAGE_INDEX, languageMapping.getLanguageIndex(locale.getLanguage()));
		parameters.put(JRParameter.REPORT_FILE_RESOLVER, new ReportFileResolver(reportDirectory));
		return parameters ;
	}

	private PrintRequest createPrintRequestWithLocale(Locale locale) {
		return new PrintRequest(null, null, locale, null, null, false, null);
	}

	private File getReportFile(String reportName) {
		File file = new File(reportDirectory, reportName + com.chare.reports.Utils.REPORT_EXTENSION);
		if (!file.exists())
			throw new IllegalStateException("Report " + reportName + " compiled version " + file.getAbsolutePath() + " was not found");
		return file;
	}

	static class DateFactory {
		Date getNow() {
			return new Date();
		}
	}
}
