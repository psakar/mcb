package com.chare.mcb.service;

import org.junit.Test;

public class ReportTest extends ReportTestCase {

	@Test
	public void testReport() throws Exception {
		assertReportCompiles(CardStatementExporter.REPORT);
		assertReportCompiles(CardStatementExporter.REPORT_SUBREPORT);
	}

}
