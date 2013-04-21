package com.chare.mcb.service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.chare.mcb.entity.Booking;
import com.chare.mcb.entity.Card;
import com.chare.mcb.entity.CardTransaction;
import com.chare.mcb.entity.CardTransactionType;
import com.chare.mcb.entity.FeeType;
import com.chare.mcb.entity.StatementLine;
import com.chare.mcb.repository.CardRepository;
import com.chare.mcb.repository.FeeTypeRepository;

class CardTransactionParserImpl implements CardTransactionParser {
	private static final String CARD_NR_PREFIX = "999Card ";
	private final CardRepository cardRepository;
	private final FeeTypeRepository feeTypeRepository;

	CardTransactionParserImpl(CardRepository cardRepository, FeeTypeRepository feeTypeRepository) {
		this.cardRepository = cardRepository;
		this.feeTypeRepository = feeTypeRepository;
	}

	@Override
	public void parseCardTransaction(StatementLine line) {
		if (line.parseCardTransactionDetails()) {
			if (line.getCardTransaction() == null)
				if (line.transferType.cardTransactionType == CardTransactionType.TRANSACTION_FEE) {
					line.setCardTransaction(line.amount, line.getCurrency(), parseCardNumber(line.details1), parseDate(line.details2), parseDetails1(line.details2), line.details3);
				} if (line.transferType.cardTransactionType == CardTransactionType.TRANSACTION) {
					line.setCardTransaction(parseAmount(line.details4), parseCurrency(line.details4), parseCardNumber(line.details1), parseDate(line.details2), parseDetails1(line.details2), line.details3);
				} if (line.transferType.cardTransactionType == CardTransactionType.FEE) {
					line.setCardTransaction(line.amount, line.getCurrency(), parseCardNumber(line.details1), null, line.details2, null);
				}
				if (line.transferType.cardTransactionType == CardTransactionType.TRANSACTION_FEE || line.transferType.cardTransactionType == CardTransactionType.FEE)
					if (line.getCardTransaction().feeType == null)
						findFee(line);
		} else {
			line.clearCardTransaction();
		}
	}


	private void findFee(StatementLine line) {
		CardTransaction cardTransaction = line.getCardTransaction();
		Card card = cardRepository.findByNumber(cardTransaction.cardNumber);
		cardTransaction.feeType = feeTypeRepository.find(card, line.transferType);
		FeeType feeType = cardTransaction.feeType;
		if (feeType == null) {
			cardTransaction.feeAmount = null;
			cardTransaction.feeCurrency = null;
		} else {
			cardTransaction.feeAmount = feeType.calculation.calculate(line.amount);
			cardTransaction.feeCurrency = Booking.CURRENCY;
		}
	}

	private String parseDetails1(String details) {
		return details.substring(11);
	}

	private BigDecimal parseAmount(String details) {
		String amount = details.substring(0, details.length() - 4);
		return new BigDecimal(amount.replace(".", "").replace(",", "."));
	}

	private String parseCurrency(String details) {
		return StringUtils.right(details, 3);
	}


	private Date parseDate(String details) {
		try {
			return getDateFormat().parse(details.substring(0, 10));
		} catch (ParseException e) {
			throw new IllegalArgumentException("Can not parse transaction date from " + details + " - " + e.getMessage(), e);
		}
	}

	private DateFormat getDateFormat() {
		return new SimpleDateFormat("yyyy.MM.dd");
	}

	private String parseCardNumber(String details) {
		if (!(details != null && details.startsWith(CARD_NR_PREFIX) && details.length() == 27 && details.charAt(12) == '-' && details.charAt(17) == '-' && details.charAt(22) == '-' ))
			throw new IllegalArgumentException("Can not parse card number - expected first line of details starting with " + CARD_NR_PREFIX);
		return details.substring(CARD_NR_PREFIX.length()).replace("-", "");
	}
}
