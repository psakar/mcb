package com.chare.mcb.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class CardStatement implements Serializable {
	public final Card card;
	public final Date periodStart;
	public final Date periodEnd;
	public final List<CardStatementLine> lines;
	public final Date statementDate;
	public final Integer number;
	public final int languageIndex;

	public CardStatement(Card card, List<CardStatementLine> lines, Date periodStart, Date periodEnd, Integer number, int languageIndex) {
		this.card = card;
		this.lines = lines;
		this.periodStart = periodStart;
		this.periodEnd = periodEnd;
		this.number = number;
		this.languageIndex = languageIndex;
		this.statementDate = new Date();
	}

	public CardStatement getStatement() {
		return this;
	}

	public String getCardNumberFormatted() {
		StringBuffer number = new StringBuffer(card.number);
		number.insert(12, " ");
		number.insert(8, " ");
		number.insert(4, " ");
		return number.toString();
	}


}
