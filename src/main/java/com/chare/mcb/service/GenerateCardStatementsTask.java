package com.chare.mcb.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.chare.mcb.entity.Card;
import com.chare.mcb.repository.CardRepository;


public class GenerateCardStatementsTask implements Runnable {

	private final CardRepository cardRepository;
	private final CardStatementGenerator statementGenerator;

	public GenerateCardStatementsTask(CardRepository cardRepository, CardStatementGenerator statementGenerator) {
		this.cardRepository = cardRepository;
		this.statementGenerator = statementGenerator;
	}

	@Override
	public void run() {
		List<Card> cards = cardRepository.findCardsToGenerateStatement();
		Logger.getLogger(getClass()).info("Generate card statements for " + cards.size() + " cards");
		for (Card card : cards) {
			try {
				Date periodStart = card.lastStatementDate;
				Date periodEnd = card.nextStatementDate;
				statementGenerator.generate(card);
				Logger.getLogger(getClass()).info("Generated card statement " + card.lastStatementNr + " for " + card.number + " for period " + formatDate(periodStart) + " - " + formatDate(periodEnd));
			} catch (Exception e) {
				Logger.getLogger(getClass()).error("Generate statement for card " + card.number + " error - " + e.getMessage(), e);
			}
		}
	}

	private String formatDate(Date date) {

		return date == null ? "" : new SimpleDateFormat("dd.MM.yyyy").format(date);
	}
}
