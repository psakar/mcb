package com.chare.mcb.repository;

import com.chare.mcb.entity.Setting;
import com.chare.repository.JpaRepository;

public interface SettingRepository extends JpaRepository<Integer, Setting> {

	public static final String MIRROR_ACC = "MIRROR_ACC";
	public static final String TRANSACT_ACC = "TRANSACT_ACC";
	public static final String COST_ACC = "COST_ACC";
	public static final String EXPORT_DIR = "EXPORT_DIR";
	public static final String CARD_STMT_DIR = "CARD_STMT_DIR";
	public static final String FILENAME_TEMPLATE = "FILENAME";
	public static final String FILENAME_TEMPLATE_DEFAULT = "MCB_${datetime;yyyyMMdd_HHmmss}";

	String getValue(String code, String defaultValue);
	void setValue(String code, String value);

	//	Date getValue(String code, Date defaultValue);


}
