package com.chare.mcb.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.chare.mcb.entity.Setting;
import com.chare.repository.JpaRepositoryImpl;

class SettingRepositoryImpl extends JpaRepositoryImpl<Integer, Setting> implements SettingRepository {

	SettingRepositoryImpl() {
	}

	protected SettingRepositoryImpl(EntityManager entityManager) {
		setEntityManager(entityManager);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public String getValue(String code, String defaultValue) {
		Query query = getEntityManager().createQuery("FROM Setting WHERE code = :code");
		query.setParameter("code", code);
		List<Setting> list = query.getResultList();
		if (list == null || list.size() == 0)
			return defaultValue;
		return list.get(0).value;
	}

	/*	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public Date getValue(String code, Date defaultValue) {
		try {
			return getDateFormat().parse(getValue(code, (String) null));
		} catch (Exception e) {
			return defaultValue;
		}
	}
	protected static DateFormat getDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd");
	}


	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void setValue(String code, String value) {
		Query query = getEntityManager().createQuery("UPDATE Setting SET value = :value WHERE code = :code");
		query.setParameter("code", code);
		query.setParameter("value", value);
		query.executeUpdate();
	}

}
