package com.chare.mcb.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.chare.core.Utils;
import com.chare.mcb.entity.Address;
import com.chare.mcb.entity.Booking;
import com.chare.mcb.entity.Card;
import com.chare.mcb.entity.CardStatement;
import com.chare.mcb.entity.CardStatementLine;
import com.chare.mcb.entity.CardTransaction;
import com.chare.mcb.entity.Description;
import com.chare.mcb.entity.FeeType;

public class CardStatementFactoryForJasperReportDesigner {

	public static Collection<CardStatement> createBeanCollection() {
		return Arrays.asList(new CardStatementFactoryForJasperReportDesigner().create());
	}

	public static Collection<CardStatementLine> createBeanCollectionLines() {
		return new CardStatementFactoryForJasperReportDesigner().create().lines;
	}

	CardStatement create() {
		Card card = new Card();
		card.number = "1234567890123456";
		card.limit = new BigDecimal("1234.56");
		card.holderName = "Cardholder Name";
		card.settlementAccount = "197123456700EUR";
		Address address = card.getMailAddress();
		address.title = "Title";
		address.name = "Name1 123456789123456789012345678901234567890";
		address.name2 = "Name2";
		address.street = "Street";
		address.town = "Town";
		address.zip = "149 00";
		address.country = "Czech Republic";
		Date periodStart = Utils.getDate(2011, 12, 31);
		Date periodEnd = Utils.getDate(2012, 1, 31);
		List<CardStatementLine> lines = new ArrayList<CardStatementLine>();
		for(int i = 0; i < 50; i++) {
			CardStatementLine line = createLine(Utils.dateAddDays(Utils.getToday(), i), new BigDecimal("123.45").add(new BigDecimal(i)).setScale(2), (i % 2 == 0 ? new BigDecimal(i) : null), (i % 2 == 0 ? new BigDecimal(2) : null));
			lines.add(line);
		}
		CardStatement statement = new CardStatement(card, lines, periodStart, periodEnd, 1, 1);
		return statement;
	}

	private CardStatementLine createLine(Date bookDate, BigDecimal amount, BigDecimal feeAmount, BigDecimal generatedFeeAmount) {
		CardStatementLine line = new CardStatementLine();
		line.bookDate = bookDate;
		line.amount = amount;
		CardTransaction cardTransaction = line.cardTransaction;
		cardTransaction.date = Utils.dateAddDays(bookDate, -1);
		cardTransaction.details1 = "detail1 " + new Date().getTime();
		cardTransaction.details2 = "detail2 " + new Date().getTime();
		cardTransaction.amount = amount;
		cardTransaction.currency = "USD";
		cardTransaction.feeAmount = feeAmount;
		if (feeAmount != null) {
			CardStatementLine lineWithFee = new CardStatementLine();
			line.bookDate = bookDate;
			line.amount = feeAmount;
			CardTransaction feeTransaction = new CardTransaction();
			feeTransaction.date = Utils.dateAddDays(bookDate, -1);
			feeTransaction.details1 = "fee1 " + new Date().getTime();
			feeTransaction.details2 = "fee2 " + new Date().getTime();
			feeTransaction.amount = feeAmount;
			feeTransaction.currency = Booking.CURRENCY;
			feeTransaction.feeAmount = generatedFeeAmount;
			feeTransaction.feeType = new FeeType("ft", new Description("desc 1", "desc 2", "desc 3"));
			lineWithFee.cardTransaction = feeTransaction;
			line.setRelatedLineWithFee(lineWithFee);
		}
		return line ;
	}
}
