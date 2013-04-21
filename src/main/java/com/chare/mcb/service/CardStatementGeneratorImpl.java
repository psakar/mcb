package com.chare.mcb.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.chare.core.Utils;
import com.chare.mcb.entity.Card;
import com.chare.mcb.entity.CardStatement;
import com.chare.mcb.entity.CardStatementLine;
import com.chare.mcb.entity.StatementPeriod;
import com.chare.mcb.repository.CalendarRepository;
import com.chare.mcb.repository.CardRepository;

class CardStatementGeneratorImpl implements CardStatementGenerator {

	private final CardRepository cardRepository;
	private final CardStatementExporter statementExporter;
	private final CardStatementLinesMerger statementLinesMerger;
	private final StatementPeriodCalculator statementPeriodCalculator;

	CardStatementGeneratorImpl(CardRepository cardRepository, CardStatementExporter statementExporter, CardStatementLinesMerger statementLinesMerger, StatementPeriodCalculator statementPeriodCalculator) {
		this.cardRepository = cardRepository;
		this.statementExporter = statementExporter;
		this.statementLinesMerger = statementLinesMerger;
		this.statementPeriodCalculator = statementPeriodCalculator;
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void generate(Card card) {
		Date statementStartDate = statementPeriodCalculator.calculateStatementStartDate(card.lastStatementDate, card.activeFrom);
		Date statementEndDate = card.getStatementEndDate();
		int statementNumber = statementPeriodCalculator.calculateNextStatementNr(card.lastStatementNr);

		List<CardStatementLine> lines = cardRepository.loadCardStatementLines(card.number, statementStartDate, statementEndDate);
		List<CardStatementLine> mergedLines = statementLinesMerger.convertToLinesWithFees(lines);
		CardStatement statement = new CardStatement(card, mergedLines, statementStartDate, statementEndDate, statementNumber, getLanguageIndex(card.languageId));

		statementExporter.export(statement);

		prepareCardForNextStatement(card, statementEndDate, statementNumber);
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public File generateOnDemand(String cardNumber, Date statementStartDate, Date statementEndDate, Integer statementNumber) {

		Card card = cardRepository.findByNumber(cardNumber);
		if (card == null)
			throw new IllegalArgumentExceptionWithParameters("Card not found", new Object[] {cardNumber});

		List<CardStatementLine> lines = cardRepository.loadCardStatementLines(card.number, statementStartDate, statementEndDate);
		List<CardStatementLine> mergedLines = statementLinesMerger.convertToLinesWithFees(lines);
		CardStatement statement = new CardStatement(card, mergedLines, statementStartDate, statementEndDate, statementNumber, getLanguageIndex(card.languageId));
		if (statement.lines.size() == 0)
			return null;

		return statementExporter.export(statement);

	}


	private void prepareCardForNextStatement(Card card, Date statementEndDate,
			int statementNumber) {
		card.lastStatementDate = statementEndDate;
		card.nextStatementDate = statementPeriodCalculator.calculateNextStatementDate(statementEndDate, card.statementPeriod);
		card.lastStatementNr = statementNumber;
		cardRepository.persist(card);
	}

	static class StatementPeriodCalculator {
		private final CalendarRepository calendarRepository;

		public StatementPeriodCalculator(CalendarRepository calendarRepository) {
			this.calendarRepository = calendarRepository;
		}

		Date calculateStatementStartDate(Date lastStatementDate, Date activeFrom) {
			return lastStatementDate == null
					? calendarRepository.findFirstWorkingDayInclusive(activeFrom)
							: calendarRepository.findFirstWorkingDayInclusive(Utils.dateAddDays(lastStatementDate, 1));
		}
		Date calculateNextStatementDate(Date lastStatementDate, StatementPeriod statementPeriod) {
			Date date = null;
			if (statementPeriod == StatementPeriod.WEEKLY)
				date = Utils.dateAddDays(lastStatementDate, 7);
			else if (statementPeriod == StatementPeriod.MONTHLY)
				date = Utils.addTime(Calendar.MONTH, 1, lastStatementDate);
			else
				date = Utils.dateAddDays(lastStatementDate, 1);
			return calendarRepository.findFirstWorkingDayInclusive(date);
		}
		int calculateNextStatementNr(Integer lastStatementNr) {
			return (lastStatementNr == null ? 0 : lastStatementNr) + 1;
		}
	}



	private int getLanguageIndex(int languageId) {
		//		LanguageMapping languageMapping = new LanguageMapping(getLanguageCodes(Application.DEFAULT_LOCALES));

		if (languageId == 3)
			return 3;
		if (languageId == 2)
			return 2;
		return 1;
	}
	/*
	private String[] getLanguageCodes(List<Locale> applicationLocales) {
		List<String> langugageCodes = new ArrayList<String>();
		for (Locale locale : applicationLocales) {
			langugageCodes.add(locale.getLanguage());
		}
		return langugageCodes.toArray(new String[] {});
	}
	 */
	static class CardStatementLinesMerger {
		public List<CardStatementLine> convertToLinesWithFees (List<CardStatementLine> lines) {
			List<CardStatementLine> unprocessedLines = new ArrayList<CardStatementLine>(lines);
			List<CardStatementLine> processedLines = new ArrayList<CardStatementLine>();
			List<CardStatementLine> result = new ArrayList<CardStatementLine>();
			for (CardStatementLine line : lines) {
				if (processedLines.contains(line))
					continue;
				processedLines.add(line);
				unprocessedLines.remove(line);
				if (line.isRelatedFeeLine()) {
					CardStatementLine relatedLine = findRelatedLineForLineWithFee(line, unprocessedLines);
					if (relatedLine == null) {
						Logger.getLogger(getClass()).error("Related line for fee not found");
						result.add(line);
					} else {
						relatedLine.setRelatedLineWithFee(line);
						unprocessedLines.remove(relatedLine);
						processedLines.add(relatedLine);
						result.add(relatedLine);
					}
				} else {
					result.add(line);
					CardStatementLine relatedLineWithFee = findRelatedLineWithFee(line, unprocessedLines);
					if (relatedLineWithFee != null) {
						line.setRelatedLineWithFee(relatedLineWithFee);
						unprocessedLines.remove(relatedLineWithFee);
						processedLines.add(relatedLineWithFee);
					}
				}
			}
			return result;
		}

		private CardStatementLine findRelatedLineForLineWithFee(
				CardStatementLine line, List<CardStatementLine> lines) {
			for (CardStatementLine item : lines) {
				if (line.isRelatedFeeLineOfLine(item))
					return item;
			}
			return null;
		}

		private CardStatementLine findRelatedLineWithFee(
				CardStatementLine line, List<CardStatementLine> lines) {
			for (CardStatementLine item : lines) {
				if (item.isRelatedFeeLineOfLine(line))
					return item;
			}
			return null;
		}
	}
}
