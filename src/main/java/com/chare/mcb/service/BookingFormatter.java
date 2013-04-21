package com.chare.mcb.service;

import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.chare.mcb.entity.Booking;

public class BookingFormatter {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyMMdd");
	private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyMMdd_HHmmssSS");
	private static final DecimalFormat SEQUENCE_NR_FORMAT = new DecimalFormat("00000");
	private static final DecimalFormat AMOUNT_FORMAT = new DecimalFormat("000000000000.00", new DecimalFormatSymbols(Locale.US));

	public String format(Booking booking) {
		StringWriter writer = new StringWriter();

		writer.append(DATE_FORMAT.format(booking.businessDate)); // BUSSINESS_DATE [6];
		writer.append(booking.source); // APPL_PREF[2];
		writer.append(Booking.BRANCH); // BRANCH[3];
		writer.append(SEQUENCE_NR_FORMAT.format(booking.sequenceNr));
		writer.append("2"); //REC_STATUS[1];
		writer.append(AMOUNT_FORMAT.format(booking.amount)); //AMOUNT[15];
		writer.append(formatString(booking.currency, 3, ' ')); //CURR[3];
		writer.append(formatString(booking.debitAccount, 15, ' ')); //DR_ACCT[15];
		writer.append(formatString(booking.creditAccount, 15, ' ')); //CR_ACCT[15];
		writer.append(DATE_FORMAT.format(booking.debitValueDate)); //DR_VALUE_DATE[6];
		writer.append(DATE_FORMAT.format(booking.creditValueDate)); //CR_VALUE_DATE[6];
		writer.append("   "); //REASON_CODE[3];
		//java.util.Date dd2 = dd;
		// TBD dd2=dd+2;
		writer.append(DATE_FORMAT.format(booking.businessDate)); //REQ_EXE_DATE[6]; dd+2
		writer.append(formatString(booking.trReference, 16, ' ')); //TRAN_REF_NUM[16];
		writer.append("                "); //REF_REL_TRAN[16];
		writer.append(formatString(booking.details, 140, ' ')); //DOP[140];
		writer.append(formatString(booking.bankToBankInfo, 210, ' ')); //BBI[210];
		writer.append("0000"); //SUM_DEBITS[4];
		writer.append("0000"); //SUM_CREDITS[4];
		writer.append(DATETIME_FORMAT.format(booking.businessDate)); //TIME_STAMP[15];
		writer.append("                                   "); //BOOKING_TEXT[210];
		writer.append("                                   "); //BOOKING_TEXT[210];
		writer.append("                                   "); //BOOKING_TEXT[210];
		writer.append("                                   "); //BOOKING_TEXT[210];
		writer.append("                                   "); //BOOKING_TEXT[210];
		writer.append("                                   "); //BOOKING_TEXT[210];
		//    w.append("   "); //ORIGINAL_CURR[3];
		//    w.append("        "); //ORIGINAL_AMOUNT[15];
		writer.append(formatString(booking.currency, 3, ' ')); //CURR[3];
		writer.append(AMOUNT_FORMAT.format(booking.amount)); //AMOUNT[15];
		writer.append("                "); //CHEQUE_NO[16];
		writer.append("                                   "); //BENEFICIARY[140];
		writer.append("                                   "); //BENEFICIARY[140];
		writer.append("                                   "); //BENEFICIARY[140];
		writer.append("                                   "); //BENEFICIARY[140];
		writer.append("                                   "); //OBK_NAM[35];
		writer.append("                                   "); //OBK_ADDRESS[105];
		writer.append("                                   "); //OBK_ADDRESS[105];
		writer.append("                                   "); //OBK_ADDRESS[105];
		writer.append("          "); //LOCAL_KEY[10];
		writer.append("                                                                           "); //FILL[475] - 75;
		writer.append("                                                                                                    "); //FILL[475] - 100;
		writer.append("                                                                                                    "); //FILL[475] - 100;
		writer.append("                                                                                                    "); //FILL[475] - 100;
		writer.append("                                                                                                    "); //FILL[475] - 100;
		writer.append("\r\n"); //CRLF[2];

		return writer.toString();
	}

	String formatString(String s, int len, char fill) {
		if (len < 1 || s == null)
			return "";
		s = s.replace('\t', ' ');
		s = s.replace('\r', ' ');
		s = s.replace('\n', ' ');

		s = s.replace('ě', 'e');
		s = s.replace('š', 's');
		s = s.replace('č', 'c');
		s = s.replace('ř', 'r');
		s = s.replace('ž', 'z');
		s = s.replace('ý', 'y');
		s = s.replace('á', 'a');
		s = s.replace('í', 'i');
		s = s.replace('é', 'e');
		s = s.replace('Ě', 'e');
		s = s.replace('Š', 's');
		s = s.replace('Č', 'c');
		s = s.replace('Ř', 'r');
		s = s.replace('Ž', 'z');
		s = s.replace('Ý', 'y');
		s = s.replace('Á', 'a');
		s = s.replace('Í', 'a');
		s = s.replace('É', 'e');

		s = s.replace('Ď', 'd');
		s = s.replace('Ň', 'n');
		s = s.replace('ň', 'n');
		s = s.replace('Ž', 'z');

		s = s.replace('ó', 'o');
		s = s.replace('Ó', 'o');
		s = s.replace('ů', 'u');
		s = s.replace('Ú', 'u');
		s = s.replace('ú', 'u');

		s = s.replace('Ä', 'a');
		s = s.replace('ä', 'a');
		s = s.replace('Ü', 'u');
		s = s.replace('ü', 'u');
		s = s.replace('Ö', 'o');
		s = s.replace('ö', 'o');

		s = s.replaceAll("[^a-zA-Z0-9+-. ]", " ");

		if (s.length() >= len)
			return s.substring(0, len);
		for (int i = s.length(); i < len; i++)
			s += fill;
		return s;
	}

}
