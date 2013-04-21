package com.chare.mcb.www;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.IMarkupFragment;
import org.apache.wicket.markup.MarkupParser;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.chare.mcb.Application;
import com.chare.mcb.entity.Role;
import com.chare.mcb.entity.User;
import com.chare.mcb.service.UserPreferences;

public class MenuPanel extends WebComponent {


	@SpringBean
	private UserPreferences userPreferences;

	public MenuPanel(String id) {
		super(id);
		setRenderBodyOnly(true);
	}

	@Override
	public void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag) {
		replaceComponentTagBody(markupStream, openTag, createContent());
	}

	@Override
	public IMarkupFragment getMarkup() {
		try {
			return new MarkupParser("<div wicket:id=\"menu\">menu</div>").parse();
		} catch (Exception e) {
			return null;
		}
	}

	private String createContent() {
		return isUserAuthenticated() ? createAuthenticatedMenu() : unauthenticatedMenu();
	}

	private String createAuthenticatedMenu() {
		User user = userPreferences.getUser();
		String menu = "";
		if (user.hasAnyOfRoles(Role.UPLOAD_STATEMENTS, Role.EXPORT_POSTINGS)) {
			menu += new MainMenuItem().create(2, "m1", translate("menu.mcb"));
			menu += new MenuItem().create(3, "m11", Application.CARD_LIST, translate("menu.mcb.cards"));
			if (user.hasRoles(Role.UPLOAD_STATEMENTS)) {
				menu += new MenuItem().create(3, "m12", Application.UPLOAD_STATEMENT_PAGE, translate("menu.mcb.uploadStatement"));
				menu += new MenuItem().create(3, "m13", Application.STATEMENT_LIST, translate("menu.mcb.statements"));
			}
			if (user.hasRoles(Role.EXPORT_POSTINGS))
				menu += new MenuItem().create(3, "m14", Application.POSTING_FILE_LIST, translate("menu.mcb.exportPostings"));
		}
		if (user.hasAnyOfRoles(Role.USER_ADMIN, Role.APP_ADMIN)) {
			menu += new MainMenuItem().create(2, "m4", translate("administration"));
			if (user.hasRoles(Role.USER_ADMIN))
				menu += new MenuItem().create(3, "m41", Application.USER_LIST, translate("menu.administration.users"));
			if (user.hasRoles(Role.APP_ADMIN)) {
				menu += new MenuItem().create(3, "m42", Application.SETTING_LIST, translate("menu.administration.settings"));
				menu += new MenuItem().create(3, "m43", Application.CARD_TYPE_LIST, translate("menu.administration.cardTypes"));
				menu += new MenuItem().create(3, "m44", Application.TRANSFER_TYPE_LIST, translate("menu.administration.transferTypes"));
				menu += new MenuItem().create(3, "m45", Application.FEE_TYPE_LIST, translate("menu.administration.feeTypes"));
				menu += new MenuItem().create(3, "m46", Application.GENERATE_CARD_STATEMENT_PAGE, translate("menu.administration.generateCardStatement"));
				menu += new MenuItem().create(3, "m47", Application.CALENDAR_LIST, translate("menu.administration.calendar"));
			}
		}
		menu += new MainMenuItem().create(2, "m5", translate("menu.profile"));
		menu += new MenuItem().create(3, "m51", Application.PROFILE_PAGE, translate("menu.profile.info"));
		menu += new MenuItem().create(3, "m52", Application.CHANGE_PASSWORD_PAGE, translate("menu.profile.changePassword"));
		menu += new MenuItem().create(3, "m53", Application.EDIT_PROFILE_PAGE, translate("menu.profile.edit"));

		return menu;
	}

	private String translate(String string) {
		return getLocalizer().getString(string, this);
	}
	private String unauthenticatedMenu() {
		String menu = new MainMenuItem().create(2, "m1", translate("menu.mcb"));
		menu += new MenuItem().create(3, "m11", Application.LOGIN_PAGE, translate("menu.login"), null);
		return menu;
	}

	protected boolean isUserAuthenticated() {
		return userPreferences.isUserAuthenticated();
	}

	static class MainMenuItem {
		String create(int level, String id, String title) {
			return
					"      <DIV CLASS='l" + level + "' ID='" + id + "'>\n" +
					"        <TABLE class='buttonlevel1' BORDER='0' CELLPADDING='0' CELLSPACING='0' WIDTH='166'>\n" +
					"          <TR>\n" +
					"            <TD width='166' style='text-indent: 0; margin-left: 0; margin-right: 0'>\n" +
					"              <SCRIPT>document.write(toglS)</SCRIPT><IMG SRC='./images/open.gif' BORDER='0' HSPACE='6' align='left' width='12' height='12'/>\n" +
					"              " + title +
					"            </TD>\n" +
					"          </TR>\n" +
					"        </TABLE>\n" +
					"      </DIV>\n";
		}
	}

	static class MenuItem {
		private String create(int level, String id, String url, String title) {
			return create(level, id, url, title, "main");
		}

		private String create(int level, String id, String url, String title, String target) {
			return
					"      <DIV CLASS='l" + level + "' ID='" + id + "'>\n" +
					"        <TABLE class='buttonlevel2' BORDER='0' CELLPADDING='0' CELLSPACING='0' WIDTH='166'>\n" +
					"          <TR>\n" +
					"            <TD width='25' VALIGN='TOP'></TD>\n" +
					"            <TD width='141' style='text-indent: 0; margin-left: 0; margin-right: 0'>\n" +
					"              <A HREF='" + url + "'" + (target!=null ? " target='" + target + "'" : "" ) +">" + title + "</A>\n" +
					"            </TD>\n" +
					"          </TR>\n" +
					"        </TABLE>\n" +
					"      </DIV>\n";
		}

	}


}