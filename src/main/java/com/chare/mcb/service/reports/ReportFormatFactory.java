package com.chare.mcb.service.reports;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class ReportFormatFactory extends com.chare.reports.ReportFormatFactory {

	@Override
	protected NumberFormat createCurrencyNumberFormat(Locale locale) {
		DecimalFormat format = new DecimalFormat("# ##0.00;-# ##0.00", createDecimalFormatSymbols());
		//NumberFormat format = NumberFormat.getNumberInstance(Locale.GERMAN);
		format.setMaximumFractionDigits(2);
		format.setMinimumFractionDigits(2);
		format.setGroupingUsed(true);
		format.setPositiveSuffix("");
		format.setGroupingSize(3);
		return format;
	}

	@Override
	protected NumberFormat createIntegerFormat(Locale locale) {
		DecimalFormat format = new DecimalFormat("# ##0;-# ##0", createDecimalFormatSymbols());
		//NumberFormat format = NumberFormat.getNumberInstance(Locale.GERMAN);
		format.setMaximumFractionDigits(0);
		format.setMinimumFractionDigits(0);
		format.setGroupingUsed(true);
		format.setPositiveSuffix("");
		format.setGroupingSize(3);
		return format;
	}

	@Override
	protected NumberFormat createNumberFormat(Locale locale) {
		DecimalFormat format = new DecimalFormat("# ##0.00;-# ##0.00", createDecimalFormatSymbols());
		//NumberFormat format = NumberFormat.getNumberInstance(Locale.GERMAN);
		format.setMaximumFractionDigits(4);
		format.setMinimumFractionDigits(0);
		format.setGroupingUsed(true);
		format.setPositiveSuffix("");
		format.setGroupingSize(3);
		return format;
	}

	private DecimalFormatSymbols createDecimalFormatSymbols() {
		DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance(Locale.ENGLISH);
		decimalFormatSymbols.setGroupingSeparator(' ');
		decimalFormatSymbols.setCurrencySymbol("");
		decimalFormatSymbols.setDecimalSeparator(',');
		return decimalFormatSymbols;
	}

	@Override
	protected NumberFormat createPercentFormat(Locale locale) {
		DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance(Locale.ENGLISH);
		decimalFormatSymbols.setGroupingSeparator(' ');
		decimalFormatSymbols.setCurrencySymbol("");
		decimalFormatSymbols.setDecimalSeparator(',');
		DecimalFormat format = new DecimalFormat("# ##0.##%;-# ##0.##%", decimalFormatSymbols);
		//NumberFormat format = NumberFormat.getPercentInstance(Locale.GERMAN);
		format.setMaximumFractionDigits(2);
		format.setMinimumFractionDigits(0);
		format.setGroupingUsed(true);
		format.setNegativeSuffix("%");
		format.setPositiveSuffix("%");
		format.setMultiplier(100);
		format.setGroupingSize(3);
		return format;
	}

	@Override
	protected DateFormat createDateFormat(Locale locale, TimeZone timezone) {
		return new SimpleDateFormat("dd.MM.yy");
	}
}
