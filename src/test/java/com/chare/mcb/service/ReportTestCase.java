package com.chare.mcb.service;

import static org.junit.Assert.*;

import java.io.File;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;

import org.junit.Ignore;

import com.chare.mcb.service.reports.ReportFormatFactory;
import com.chare.reports.Utils;

@Ignore
public class ReportTestCase {

	public static final File REPORT_SRC_DIR = new File("src/main/jasperreports");

	public static void assertReportCompiles(String reportName) throws JRException {
		File inputJrxmlFile = new File(REPORT_SRC_DIR, reportName + Utils.SOURCE_EXTENSION);
		assertTrue(inputJrxmlFile.exists());
		File outputJasperFile = new File(REPORT_SRC_DIR, reportName + Utils.REPORT_EXTENSION);
		if (outputJasperFile.exists())
			assertTrue(outputJasperFile.delete());
		Utils.compileReport(inputJrxmlFile.getAbsolutePath(), outputJasperFile.getAbsolutePath());
		assertTrue(outputJasperFile.exists());
		assertReportDefaults(inputJrxmlFile);
	}

	//	@SuppressWarnings("deprecation")
	public static void assertReportDefaults(File inputJrxmlFile) throws JRException {
		JasperReport design = JasperCompileManager.compileReport(inputJrxmlFile.getAbsolutePath());
		assertEquals("Report " + inputJrxmlFile.getAbsolutePath() + " format factory is not " + ReportFormatFactory.class.getName(), ReportFormatFactory.class.getName(), design.getFormatFactoryClass());
		assertEquals("Report " + inputJrxmlFile.getAbsolutePath() + " resource bundle not titles", "titles", design.getResourceBundle());
		//		assertEquals("Report " + filename + " font name is not Courier", "Courier", design.getDefaultFont().getName());
		//		assertEquals("Report " + filename + " font name is not Courier", "Courier New", design.getDefaultFont().getFontName());
		///		assertEquals("Report " + filename + " pdf font name is not cour.ttf", "cour.ttf", design.getDefaultFont().getPdfFontName());
		//		assertEquals("Report " + filename + " is not pdfEmbedded", true, design.getDefaultFont().isPdfEmbedded());
	}

}
