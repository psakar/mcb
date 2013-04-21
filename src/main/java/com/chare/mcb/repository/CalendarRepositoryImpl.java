package com.chare.mcb.repository;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.chare.core.Utils;
import com.chare.mcb.entity.Calendar;
import com.chare.repository.JpaRepositoryImpl;

class CalendarRepositoryImpl extends JpaRepositoryImpl<Integer, Calendar> implements CalendarRepository {

	@PersistenceContext
	private EntityManager entityManager;

	CalendarRepositoryImpl() {}

	protected CalendarRepositoryImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Boolean isHoliday(Date date) {
		Query query = entityManager.createNativeQuery("SELECT holiday FROM calendar WHERE id_date = :date");
		query.setParameter("date", date);
		Boolean result = (Boolean)Utils.first(query.getResultList());
		if (result == null)
			throw new IllegalArgumentException("Calendar not set for date " + date);
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Date findDistantWorkingDay(Date date, int days) {
		if (isHoliday(date))
			days = days - 1;
		String sql = "SELECT id_date FROM calendar c WHERE holiday = 0 AND c.id_date >= :date AND (SELECT count(*) FROM calendar cc WHERE holiday = 0 AND cc.id_date >= :date AND cc.id_date < c.id_date) = :days";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("date", date);
		query.setParameter("days", days);
		return (Date)Utils.first(query.getResultList());
	}

	/**
	 * @return if date is working date returns the date otherwise first following working date
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Date findFirstWorkingDayInclusive(Date date) {
		String sql = "SELECT MIN(id_date) FROM calendar WHERE holiday = 0 AND id_date >= :date";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("date", date);
		return (Date)Utils.first(query.getResultList());
	}

	/**
	 * @return if date is working date returns the date otherwise first previous working date
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Date findLastWorkingDayInclusive(Date date) {
		String sql = "SELECT MAX(id_date) FROM calendar WHERE holiday = 0 AND id_date <= :date";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("date", date);
		return (Date)Utils.first(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer getDateDiffWorkingDays(Date dateFrom, Date dateTo) {
		/*
		String sql = "SELECT dateDiffWorkingDays(:dateFrom, :dateTo)";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("dateFrom", dateFrom);
		query.setParameter("dateTo", dateTo);
		return (Integer)Utils.first(query.getResultList());
		 */
		/*
	  if @dateFrom > @dateTo then
      set @cnt = isnull((select count()*-1 from calendar where id_date >= @dateTo and id_date < @dateFrom and holiday = 0),null)
    else
      set @cnt = isnull((select count() from calendar where id_date > @dateFrom and id_date <= @dateTo and holiday = 0),null)
    end if;
		 */
		String sql = dateFrom.after(dateTo)
				? "select count(*) * -1 from calendar where id_date >= :dateTo and id_date < :dateFrom and holiday = 0"
						: "select count(*) from calendar where id_date > :dateFrom and id_date <= :dateTo and holiday = 0";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("dateFrom", dateFrom);
		query.setParameter("dateTo", dateTo);
		return Utils.isNull(Utils.first(query.getResultList()), (Integer) null);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void changeHolidayValue(Date date) {
		String sql = "UPDATE calendar SET holiday = 1-holiday WHERE id_date = :id_date";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("id_date", date);
		query.executeUpdate();
	}

}
