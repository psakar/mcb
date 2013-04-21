package com.chare.mcb.repository;

import java.util.Date;
import java.util.List;

import com.chare.mcb.entity.Card;
import com.chare.mcb.entity.CardStatementLine;
import com.chare.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Integer, Card> {

	Card findByNumber(String number);

	// najdi karty, pro které má být generovaný výpis, (card.nextStatementDate <= today)
	// a existuje zaúčtovaná transakce s datem zaúčtování od počátečního data výpisu do konečného data
	// počáteční datum výpisu je následující den po datu posledního výpisu
	List<Card> findCardsToGenerateStatement();

	List<CardStatementLine> loadCardStatementLines(String number, Date statementPeriodStart,
			Date statementPeriodEnd);

}
