package com.chare.mcb.service;

import java.io.File;
import java.util.Date;

import com.chare.mcb.entity.Card;

public interface CardStatementGenerator {

	public void generate(Card card);

	public File generateOnDemand(String cardNumber, Date statementStartDate, Date statementEndDate, Integer statementNumber);
}
