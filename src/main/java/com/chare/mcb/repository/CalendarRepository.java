package com.chare.mcb.repository;

import java.util.Date;

import com.chare.mcb.entity.Calendar;
import com.chare.repository.JpaRepository;

public interface CalendarRepository extends JpaRepository<Integer, Calendar> {

	public Boolean isHoliday(Date date);

	public Date findDistantWorkingDay(Date date, int days);

	public Date findFirstWorkingDayInclusive(Date date);

	public Date findLastWorkingDayInclusive(Date date);

	public Integer getDateDiffWorkingDays(Date dateFrom, Date dateTo);

	void changeHolidayValue(Date date);

}
