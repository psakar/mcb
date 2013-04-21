// GENERATED
package com.chare.mcb.www;

import org.apache.log4j.Logger;

import com.chare.mcb.Application;

public class MountList implements com.chare.wicket.MountList {

	static final Logger logger = Logger.getLogger(MountList.class);

	@Override
	public void mount(Mount mount) {
		mount.mount(Application.MENU_PAGE, MenuPage.class);
		mount.mount(Application.LOGIN_PAGE, LoginPage.class);
		mount.mount(Application.LOGOUT_PAGE, LogoutPage.class);
		//		pageMount.mount(Application.PAGE_UNAUTHORIZED, UnauthorizedPage.class);
		mount.mount(Application.PROFILE_PAGE, ProfilePage.class);
		mount.mount(Application.EDIT_PROFILE_PAGE, EditProfilePage.class);
		mount.mount(Application.CHANGE_PASSWORD_PAGE, ChangePasswordPage.class);
		mount.mount(Application.SETTING_LIST, SettingList.class);
		mount.mount(Application.SETTING_PAGE, SettingPage.class);
		mount.mount(Application.USER_LIST, UserList.class);
		mount.mount(Application.USER_PAGE, UserPage.class);

		mount.mount(Application.CARD_LIST, CardList.class);
		mount.mount(Application.CARD_PAGE, CardPage.class);

		mount.mount(Application.CARD_TYPE_LIST, CardTypeList.class);
		mount.mount(Application.CARD_TYPE_PAGE, CardTypePage.class);

		mount.mount(Application.FEE_TYPE_LIST, FeeTypeList.class);
		mount.mount(Application.FEE_TYPE_PAGE, FeeTypePage.class);

		mount.mount(Application.TRANSFER_TYPE_LIST, TransferTypeList.class);
		mount.mount(Application.TRANSFER_TYPE_PAGE, TransferTypePage.class);

		mount.mount(Application.STATEMENT_LIST, StatementList.class);
		mount.mount(Application.STATEMENT_PAGE, StatementPage.class);

		mount.mount(Application.UPLOAD_STATEMENT_PAGE, UploadStatementPage.class);
		mount.mount(Application.POSTING_FILE_LIST, PostingFileList.class);
		mount.mount(Application.POSTING_FILE_PAGE, PostingFilePage.class);

		mount.mount(Application.GENERATE_CARD_STATEMENT_PAGE, GenerateCardStatementPage.class);

		mount.mount(Application.CALENDAR_LIST, CalendarList.class);
	}

}
// END OF GENERATED

