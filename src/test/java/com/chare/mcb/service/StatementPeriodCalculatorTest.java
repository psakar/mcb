package com.chare.mcb.service;


import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.chare.core.Utils;
import com.chare.mcb.entity.StatementPeriod;
import com.chare.mcb.repository.CalendarRepository;
import com.chare.mcb.service.CardStatementGeneratorImpl.StatementPeriodCalculator;

public class StatementPeriodCalculatorTest {

	private StatementPeriodCalculator calculator;

	@Mock
	private CalendarRepository calendarRepository;




	@Before
	public void before() throws Exception {
		MockitoAnnotations.initMocks(this);
		calculator = new StatementPeriodCalculator(calendarRepository);
	}

	@Test
	public void testStatementPeriodStartIsActiveFromDateIfLastStatementDateNotFilledIn() throws Exception {
		Date activeFrom = Utils.getYesterday();
		Date nextBusinessDay = Utils.getTomorrow();
		when(calendarRepository.findFirstWorkingDayInclusive(activeFrom)).thenReturn(nextBusinessDay);

		assertEquals(nextBusinessDay, calculator.calculateStatementStartDate(null, activeFrom));
	}

	@Test
	public void testStatementPeriodStartIsNextBusinessDayOfLastStatementDateIfLastStatementDateIsFilledIn() throws Exception {
		Date lastStatementDate = Utils.getYesterday();
		Date nextBusinessDay = Utils.getToday();
		when(calendarRepository.findFirstWorkingDayInclusive(Utils.dateAddDays(lastStatementDate, 1))).thenReturn(nextBusinessDay);

		assertEquals(nextBusinessDay, calculator.calculateStatementStartDate(lastStatementDate, null));
	}


	@Test
	public void testStatementWeeklyPeriod() throws Exception {
		StatementPeriod statementPeriod = StatementPeriod.WEEKLY;
		Date lastStatementDate = Utils.getYesterday();

		Date businessDay = Utils.dateAddDays(Utils.getToday(), 10);
		when(calendarRepository.findFirstWorkingDayInclusive(Utils.dateAddDays(lastStatementDate, 7))).thenReturn(businessDay);

		calculator.calculateNextStatementDate(lastStatementDate, statementPeriod);

		assertEquals(businessDay, calculator.calculateNextStatementDate(lastStatementDate, statementPeriod));
	}

	@Test
	public void testPrepareForNextStatementMonthly() throws Exception {

		StatementPeriod statementPeriod = StatementPeriod.MONTHLY;
		Date lastStatementDate = Utils.getYesterday();

		Date businessDay = Utils.dateAddDays(Utils.getToday(), 40);
		when(calendarRepository.findFirstWorkingDayInclusive(Utils.addTime(Calendar.MONTH, 1, lastStatementDate))).thenReturn(businessDay);

		calculator.calculateNextStatementDate(lastStatementDate, statementPeriod);

		assertEquals(businessDay, calculator.calculateNextStatementDate(lastStatementDate, statementPeriod));

	}

	@Test
	public void testCalculateNextStatementNr() throws Exception {
		int lastStatementNr = new Random().nextInt();
		assertEquals(lastStatementNr + 1, calculator.calculateNextStatementNr(lastStatementNr));
	}

	@Test
	public void testCalculateNextStatementNrForFirstStatement() throws Exception {
		Integer lastStatementNr = null;
		assertEquals(1, calculator.calculateNextStatementNr(lastStatementNr));
	}

}
