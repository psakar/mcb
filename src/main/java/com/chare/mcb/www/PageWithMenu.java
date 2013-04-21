package com.chare.mcb.www;

import java.util.Locale;
import java.util.Properties;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.chare.core.CustomLocale;
import com.chare.core.Utils;
import com.chare.infrastructure.EnvironmentConfig;
import com.chare.mcb.entity.User;
import com.chare.wicket.AttributeSetter;

public abstract class PageWithMenu extends Page
{

	public static final String VERSION_ID = "version";
	public static final String MENU_ID = "menu";

	@SpringBean(name=EnvironmentConfig.BUILD_PROPERTIES)
	private Properties buildProperties;

	public PageWithMenu(PageParameters pageParameters) {
		super(pageParameters);
		add(new Label("metaTitle", new ResourceModel("menu.mcb")));
		add(new ExternalLink("metaUpperLink1", new Model<String>("../"), new ResourceModel("menu.home", "Home")));
		add(new ExternalLink("metaUpperLink2", new Model<String>("../ebp/"), new ResourceModel("menu.ebp", "EBP")));
		add(new ExternalLink("metaUpperLink3", new Model<String>("../ccrs/"), new ResourceModel("menu.ccrs", "CCRS")));

		if (userPreferences.isUserAuthenticated()) {
			add(createCell("enCell", "metaLink1", "mailto:support@chare.eu", "menu.contact", "Contact", false, true));
			add(createCell("czCell", "metaLink2", "javascript:showHelpWin();", "menu.help", "Help", false, true));
			add(createCell("skCell", "metaLink3", "logout", "menu.logout", "Logout", false, true));
		} else {
			add(createCell("enCell", "metaLink1", "login?lang=en", null, "English", true, compareLocale(Locale.ENGLISH)));
			add(createCell("czCell", "metaLink2", "login?lang=cs", null, "Česky", true, compareLocale(CustomLocale.LOCALE_CZ)));
			add(createCell("skCell", "metaLink3", "login?lang=sk", null, "Slovensky", true, compareLocale(User.localeSk)));
		}
		add(new Label(VERSION_ID, Utils.getBuildInfo(buildProperties)));
		add(new MenuPanel(MENU_ID));
	}


	private boolean compareLocale(Locale value) {
		return !getLocale().getLanguage().equals(value.getLanguage());
	}

	private Component createCell(String name, String linkName, String linkDefaultUrl, String linkLabel, String linkDefaultLabel, boolean testLocale, boolean active) {
		MarkupContainer enCell = new WebMarkupContainer(name);
		if (testLocale && !active) {
			enCell.add(new AttributeSetter<String>("class", new Model<String>("lang_")));
			enCell.add(new Label(linkName, (linkLabel == null
					? new Model<String>(linkDefaultLabel)
							: new ResourceModel(linkLabel, linkDefaultLabel))));
		} else
			enCell.add(new ExternalLink(linkName, new Model<String>(linkDefaultUrl), (linkLabel == null
			? new Model<String>(linkDefaultLabel)
					: new ResourceModel(linkLabel, linkDefaultLabel))));
		return enCell;
	}

}
