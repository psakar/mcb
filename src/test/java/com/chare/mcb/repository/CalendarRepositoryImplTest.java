package com.chare.mcb.repository;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import com.chare.core.Utils;
import com.chare.mcb.JpaTransactionalTestCase;

public class CalendarRepositoryImplTest extends JpaTransactionalTestCase {

	private static final Date SATURDAY = Utils.getDate(2012, 7, 21);
	private static final Date WORKING_DAY = Utils.getDate(2012, 7, 12);
	private static final Date WORKING_DAY_MONDAY = Utils.getDate(2012, 7, 16);
	private static final Date WORKING_DAY_TUESDAY = Utils.getDate(2012, 7, 17);
	private static final Date WORKING_DAY_NEXT_MONDAY = Utils.getDate(2012, 7, 23);

	private CalendarRepositoryImpl calendarRepository;

	@Override
	public void before() throws Exception {
		super.before();
		calendarRepository = new CalendarRepositoryImpl(entityManager);
	}

	@Test
	public void testWorkingDayIsRecognized() throws Exception {
		assertFalse(calendarRepository.isHoliday(WORKING_DAY));
	}

	@Test
	public void testHolidayDayIsRecognized() throws Exception {
		assertTrue(calendarRepository.isHoliday((Utils.getDate(2012, 7, 14))));
	}

	@Test
	public void testGettingWorkingDateAfterSaturday() throws Exception {
		Date workingDay = calendarRepository.findFirstWorkingDayInclusive(SATURDAY);
		assertEquals(Utils.getDate(2012, 7, 23), workingDay);
	}

	@Test
	public void testGettingWorkingDateAfterSaturdayWithMonthChange() throws Exception {
		Date workingDay = calendarRepository.findFirstWorkingDayInclusive(Utils.getDate(2012, 9, 30));
		assertEquals(Utils.getDate(2012, 10, 1), workingDay);
	}

	@Test
	public void testGettingWorkingDateAfterWorkingDay() throws Exception {
		Date workingDay = calendarRepository.findFirstWorkingDayInclusive(Utils.getDate(2012, 7, 20));
		assertEquals(Utils.getDate(2012, 7, 20), workingDay);
	}

	@Test
	public void testFindFirstWorkingDayAfterNumberOfWorkingDays() throws Exception {
		Date workingDay = calendarRepository.findDistantWorkingDay(Utils.getDate(2012, 7, 14), 2);
		assertEquals(Utils.getDate(2012, 7, 17), workingDay);
	}

	@Test
	public void testFindFirstWorkingDayAfterNumberOfWorkingDaysWithMonthChange() throws Exception {
		Date workingDay = calendarRepository.findDistantWorkingDay(Utils.getDate(2012, 7, 27), 5);
		assertEquals(Utils.getDate(2012, 8, 3), workingDay);
	}

	@Test
	public void testFindPreviousWorkingDateForHoliday() throws Exception {
		assertEquals(Utils.dateAddDays(SATURDAY, -1), calendarRepository.findLastWorkingDayInclusive(SATURDAY));
	}

	@Test
	public void testFindPreviousWorkingDateForWorkingDate() throws Exception {
		assertEquals(WORKING_DAY, calendarRepository.findLastWorkingDayInclusive(WORKING_DAY));
	}

	@Test
	public void testDateDiffWorkdingDaysSameDate() throws Exception {
		assertEquals(0, (int)calendarRepository.getDateDiffWorkingDays(WORKING_DAY, WORKING_DAY));
	}

	@Test
	public void testDateDiffWorkdingDaysFollowingWorkingDates() throws Exception {
		assertEquals(1, (int)calendarRepository.getDateDiffWorkingDays(WORKING_DAY_MONDAY, WORKING_DAY_TUESDAY));
	}

	@Test
	public void testDateDiffWorkdingDaysFollowingWorkingDatesSwitched() throws Exception {
		assertEquals(-1, (int)calendarRepository.getDateDiffWorkingDays(WORKING_DAY_TUESDAY, WORKING_DAY_MONDAY));
	}

	@Test
	public void testDateDiffWorkdingDaysWorkingDatesIncludingWeekend() throws Exception {
		assertEquals(5, (int)calendarRepository.getDateDiffWorkingDays(WORKING_DAY_MONDAY, WORKING_DAY_NEXT_MONDAY));
	}

	@Test
	public void testDateDiffWorkdingDaysWorkingDatesIncludingWeekendSwitched() throws Exception {
		assertEquals(-5, (int)calendarRepository.getDateDiffWorkingDays(WORKING_DAY_NEXT_MONDAY, WORKING_DAY_MONDAY));
	}

	@Test
	public void testChangeHolidayValue() throws Exception {
		final Date date = Utils.getDate(2012, 4, 1);
		boolean holiday = calendarRepository.isHoliday(date);
		calendarRepository.changeHolidayValue(date);

		flushAndClearEntityManager();
		assertEquals(holiday, !calendarRepository.isHoliday(date));
	}

}
