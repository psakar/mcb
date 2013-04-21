package com.chare.mcb;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.chare.core.CustomLocale;
import com.chare.mcb.entity.User;

public class Application {

	public static final String name = "mcb";
	public static final List<Locale> DEFAULT_LOCALES = Arrays.asList(Locale.ENGLISH, CustomLocale.LOCALE_CZ, User.localeSk, Locale.GERMAN);

	public static final String MENU_PAGE = "menu";
	public static final String LOGIN_PAGE = "login";
	public static final String LOGOUT_PAGE = "logout";
	public static final String PAGE_UNAUTHORIZED = "unauthorized";

	public static final String CARD_LIST = "cards";
	public static final String CARD_PAGE = "card";
	public static final String PROFILE_PAGE = "profile";
	public static final String EDIT_PROFILE_PAGE = "editProfile";
	public static final String CHANGE_PASSWORD_PAGE = "changePasswordPage";

	public static final String SETTING_LIST = "settings";
	public static final String SETTING_PAGE = "setting";

	public static final String USER_LIST = "users";
	public static final String USER_PAGE = "user";

	public static final String CARD_TYPE_LIST = "cardTypes";
	public static final String CARD_TYPE_PAGE = "cardType";

	public static final String FEE_TYPE_LIST = "feeTypes";
	public static final String FEE_TYPE_PAGE = "feeType";

	public static final String TRANSFER_TYPE_LIST = "transferTypes";
	public static final String TRANSFER_TYPE_PAGE = "transferType";

	public static final String UPLOAD_STATEMENT_PAGE = "upload";
	public static final String POSTING_FILE_LIST = "postingFiles";
	public static final String POSTING_FILE_PAGE = "postingFile";

	public static final String STATEMENT_LIST = "statements";
	public static final String STATEMENT_PAGE = "statement";
	public static final String GENERATE_CARD_STATEMENT_PAGE = "generateCardStatement";

	public static final String CALENDAR_LIST = "calendarList";

	public static Locale findLocale(Integer id) {
		List<Locale> locales = DEFAULT_LOCALES;
		int index = 1;
		for (Locale value : locales) {
			if (id.equals(index))
				return value;
			index++;
		}
		return null;
	}

}