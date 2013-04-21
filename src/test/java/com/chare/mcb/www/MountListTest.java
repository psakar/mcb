package com.chare.mcb.www;

import static org.mockito.Mockito.*;

import org.junit.Test;

import com.chare.mcb.Application;
import com.chare.wicket.MountList.PageMount;

public class MountListTest {

	@Test
	public void testPages() throws Exception {
		PageMount pageMount = mock(PageMount.class);
		MountList list = new MountList();
		list.mount(pageMount);
		//		verify(pageMount).mount(Application.PAGE_UNAUTHORIZED, UnauthorizedPage.class);
		verify(pageMount).mount(Application.LOGIN_PAGE, LoginPage.class);
		verify(pageMount).mount(Application.LOGOUT_PAGE, LogoutPage.class);
		verify(pageMount).mount(Application.MENU_PAGE, MenuPage.class);
		verify(pageMount).mount(Application.PROFILE_PAGE, ProfilePage.class);
		verify(pageMount).mount(Application.CHANGE_PASSWORD_PAGE, ChangePasswordPage.class);
		verify(pageMount).mount(Application.EDIT_PROFILE_PAGE, EditProfilePage.class);

		verify(pageMount).mount(Application.SETTING_LIST, SettingList.class);
		verify(pageMount).mount(Application.SETTING_PAGE, SettingPage.class);

		verify(pageMount).mount(Application.USER_LIST, UserList.class);
		verify(pageMount).mount(Application.USER_PAGE, UserPage.class);

		verify(pageMount).mount(Application.CARD_LIST, CardList.class);
		verify(pageMount).mount(Application.CARD_PAGE, CardPage.class);

		verify(pageMount).mount(Application.CARD_TYPE_LIST, CardTypeList.class);
		verify(pageMount).mount(Application.CARD_TYPE_PAGE, CardTypePage.class);

		verify(pageMount).mount(Application.FEE_TYPE_LIST, FeeTypeList.class);
		verify(pageMount).mount(Application.FEE_TYPE_PAGE, FeeTypePage.class);

		verify(pageMount).mount(Application.TRANSFER_TYPE_LIST, TransferTypeList.class);
		verify(pageMount).mount(Application.TRANSFER_TYPE_PAGE, TransferTypePage.class);

		verify(pageMount).mount(Application.STATEMENT_LIST, StatementList.class);
		verify(pageMount).mount(Application.STATEMENT_PAGE, StatementPage.class);

		verify(pageMount).mount(Application.UPLOAD_STATEMENT_PAGE, UploadStatementPage.class);

		verify(pageMount).mount(Application.POSTING_FILE_LIST, PostingFileList.class);
		verify(pageMount).mount(Application.POSTING_FILE_PAGE, PostingFilePage.class);

		verify(pageMount).mount(Application.GENERATE_CARD_STATEMENT_PAGE, GenerateCardStatementPage.class);

		verify(pageMount).mount(Application.CALENDAR_LIST, CalendarList.class);
	}
}
