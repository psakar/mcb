package com.chare.mcb.service.reports;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.TimeZone;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.chare.core.Utils;
public class ReportFormatFactoryTest {
	ReportFormatFactory formatFactory;

	@Before
	public void setup() {
		formatFactory = new ReportFormatFactory();
	}

	@Test
	public void testPercentFormat() throws Exception {

		NumberFormat format = formatFactory.createNumberFormat(ReportFormatFactory.PERCENT, Locale.ENGLISH);
		Assert.assertEquals("1,23%", format.format(new BigDecimal(0.01234)));
		Assert.assertEquals("1%", format.format(new BigDecimal(0.01)));
		Assert.assertEquals("1,2%", format.format(new BigDecimal(0.012)));

		Assert.assertEquals("-1,23%", format.format(new BigDecimal(-0.01234)));
		Assert.assertEquals("-1%", format.format(new BigDecimal(-0.01)));
		Assert.assertEquals("-1,2%", format.format(new BigDecimal(-0.012)));
	}

	@Test
	public void testCurrencyNumberFormat() throws Exception {
		NumberFormat format = formatFactory.createNumberFormat(ReportFormatFactory.CURRENCY_NUMBER, Locale.ENGLISH);

		Assert.assertEquals("1,23", format.format(new BigDecimal(1.234)));
		Assert.assertEquals("1,00", format.format(new BigDecimal(1)));
		Assert.assertEquals("1,20", format.format(new BigDecimal(1.2)));
		Assert.assertEquals("1 000,20", format.format(new BigDecimal(1000.2)));

		Assert.assertEquals("-1,23", format.format(new BigDecimal(-1.234)));
		Assert.assertEquals("-1,00", format.format(new BigDecimal(-1)));
		Assert.assertEquals("-1,20", format.format(new BigDecimal(-1.2)));
		Assert.assertEquals("-1 000,20", format.format(new BigDecimal(-1000.2)));
	}

	@Test
	public void testIntegerNumberFormat() throws Exception {
		NumberFormat format = formatFactory.createNumberFormat(ReportFormatFactory.INTEGER, Locale.ENGLISH);
		Assert.assertEquals("120", format.format(new BigDecimal(120)));
		Assert.assertEquals("1 000", format.format(new BigDecimal(1000)));

		Assert.assertEquals("-120", format.format(new BigDecimal(-120)));
		Assert.assertEquals("-1 000", format.format(new BigDecimal(-1000)));
	}

	@Test
	public void testNumberFormat() throws Exception {
		NumberFormat format = formatFactory.createNumberFormat(ReportFormatFactory.NUMBER, Locale.ENGLISH);
		Assert.assertEquals("1,2345", format.format(new BigDecimal(1.23454)));
		Assert.assertEquals("1,2346", format.format(new BigDecimal(1.23456)));
		Assert.assertEquals("1,2345", format.format(new BigDecimal(1.2345)));
		Assert.assertEquals("1,234", format.format(new BigDecimal(1.234)));
		Assert.assertEquals("1,23", format.format(new BigDecimal(1.23)));
		Assert.assertEquals("1,2", format.format(new BigDecimal(1.2)));
		Assert.assertEquals("1", format.format(new BigDecimal(1)));
		Assert.assertEquals("1 000,2", format.format(new BigDecimal(1000.2)));


		Assert.assertEquals("-1,2345", format.format(new BigDecimal(-1.23454)));
		Assert.assertEquals("-1,2346", format.format(new BigDecimal(-1.23456)));
		Assert.assertEquals("-1,2345", format.format(new BigDecimal(-1.2345)));
		Assert.assertEquals("-1,234", format.format(new BigDecimal(-1.234)));
		Assert.assertEquals("-1,23", format.format(new BigDecimal(-1.23)));
		Assert.assertEquals("-1,2", format.format(new BigDecimal(-1.2)));
		Assert.assertEquals("-1", format.format(new BigDecimal(-1)));
		Assert.assertEquals("-1 000,2", format.format(new BigDecimal(-1000.2)));

	}

	@Test
	public void testDateFormat() throws Exception {
		DateFormat format = formatFactory.createDateFormat(ReportFormatFactory.DATE, Locale.ENGLISH, TimeZone.getDefault());
		Assert.assertEquals("31.12.09", format.format(Utils.getDate(2009, 12, 31)));
	}

}
