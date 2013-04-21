package com.chare.mcb.repository;

import static org.junit.Assert.*;

import org.junit.Test;

import com.chare.core.Db;
import com.chare.mcb.JpaRepositoryTestCase;
import com.chare.mcb.entity.EntityWithIdInteger;
import com.chare.mcb.entity.Setting;

public class SettingRepositoryImplTest extends JpaRepositoryTestCase<Integer, Setting, SettingRepository> {

	@Override
	protected SettingRepository getRepository() {
		SettingRepositoryImpl repository = new SettingRepositoryImpl(entityManager);
		return repository;
	}

	@Override
	protected String getTableName() {
		return EntityWithIdInteger.SETTINGS_TABLE;
	}

	@Override
	protected Setting setupEntity(Setting entity) {
		entity.code = "code";
		entity.value = "value";
		entity.description1 = "description1";
		entity.description2 = "description2";
		entity.description3 = "description3";
		entity.type = 0;
		return entity;
	}

	@Override
	protected void assertPersistedEntity(Setting loaded, Setting entity) {
		assertEquals(entity.value, loaded.value);
		assertEquals(entity.id, loaded.id);
		assertEquals(entity.type, loaded.type);
		assertEquals(entity.code, loaded.code);
		assertEquals(entity.description1, loaded.description1);
		assertEquals(entity.description2, loaded.description2);
		assertEquals(entity.description3, loaded.description3);
	}

	@Test
	public void testFindValue() throws Exception {
		String code = lookup("SELECT MIN(code) FROM settings");
		String value = lookup("SELECT value FROM settings WHERE code = '" + code + "'");
		assertEquals(value, repository.getValue(code, ""));
	}

	@Test
	public void testFindValueWithNotExistingCode() throws Exception {
		String code = "12345";
		assertNull(lookup("SELECT id FROM settings WHERE code = " + Db.convertString(code)));
		assertEquals("ABC", repository.getValue(code, "ABC"));
	}

	//	@Test
	//	public void testGetPrinterName() {
	//		String printerName = repository.getValue(SettingRepository.FANSET_COUNT, "NONSENSE");
	//		Object expected = lookup("SELECT value FROM settings WHERE code = " + Db.convertString(SettingRepository.FANSET_COUNT));
	//		assertEquals(expected, printerName);
	//	}

	//	@Test
	//	public void testGetReportDate() throws Exception {
	//		Date value = repository.getValue(SettingRepository.REPORT_DATE, (Date) null);
	//		assertEquals(SettingRepositoryImpl.getDateFormat().parse((String) getValue("SELECT value FROM settings WHERE code = '" + SettingRepository.REPORT_DATE + "'")), value);
	//	}

	@Test
	public void setValue() throws Exception {
		String code = lookup("SELECT MIN(code) FROM settings");
		repository.setValue(code, "newValue");
		assertEquals("newValue", repository.getValue(code, ""));
	}

}
