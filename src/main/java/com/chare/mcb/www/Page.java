package com.chare.mcb.www;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Session;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ValidationErrorFeedback;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.context.i18n.LocaleContextHolder;

import com.chare.core.CustomLocale;
import com.chare.core.ILocale;
import com.chare.core.ILocaleProvider;
import com.chare.mcb.entity.User;
import com.chare.mcb.service.UserPreferences;
import com.chare.wicket.AlertPanel;

public abstract class Page extends WebPage implements ILocaleProvider, ILocale {

	protected final static Log log = LogFactory.getLog(WebApplication.class);
	protected boolean _initialized;

	@SpringBean(name = com.chare.service.Config.USER_PREFERENCES_SESSION_ID)
	protected UserPreferences userPreferences;


	public Page(PageParameters pageParameters) {
		super(pageParameters);
		setPageAndUserLocale(getLocale(getLanguage(pageParameters)));
		add(createHeadTitle());
		add(createAlertPanel());
	}


	/**
	 * method for initialization of component data model called from constructor
	 * before component tree initialization overriding methods should call
	 * super.initDataModel as first statement
	 */

	private Label createHeadTitle() {
		return new Label("headTitle", createTitleModel());
	}

	private AlertPanel createAlertPanel() {
		AlertPanel alertPanel = new AlertPanel(AlertPanel.name);
		alertPanel.setOutputMarkupId(true);
		alertPanel.setMessageFilter(new ValidationMessageFilter());
		return alertPanel;
	}

	static class ValidationMessageFilter implements IFeedbackMessageFilter {
		@Override
		public boolean accept(FeedbackMessage message) {
			return !(message.getMessage() instanceof ValidationErrorFeedback);
		}
	}

	protected IModel<String> createTitleModel() {
		return new ResourceModel(getTitleCode(), "Master Card Bratislava");
	}

	protected String getTitleCode() {
		return "MCB";
	}


	public User getUser() {
		return userPreferences.getUser();
	}

	protected String getHelpLink() {
		return "./help/" + getHelpName() + "_" + getLocale().getLanguage() + ".html";
	}

	protected String getHelpName() {
		return "index";
	}

	public String getTitleCode(String message) {
		if (!message.endsWith(";")) {
			if (message.lastIndexOf("_#") >= 0 )
				log.error("Message " + message + " not terminated by ;");
			return null;
		}
		int pos = message.lastIndexOf("_#");
		if (pos < 0) {
			log.error("Message " + message + " does not contain messsage code");
			return null;
		}
		pos = pos + 2;
		int len = message.length();
		if (pos == len) {
			log.error("Message " + message + " contains empty message code");
			return null;
		}
		return message.substring(pos, len - 1);
	}

	@Override
	public final boolean isVersioned() {
		return false;
	}

	@Override
	public final Locale getLocale() {
		return getUser().getLocale();
	}

	public final int getLanguageIndex() {
		return getUser().getLanguageIndex();
	}

	@Override
	public final DateFormat getDateFormat() {
		return getUser().getDateFormat();
	}

	@Override
	public final DateFormat getDateTimeFormat() {
		return getUser().getDateTimeFormat();
	}

	@Override
	public final DateFormat getTimeFormat() {
		return getUser().getTimeFormat();
	}

	@Override
	public final NumberFormat getCurrencyFormat() {
		return getUser().getCurrencyFormat();
	}

	@Override
	public final NumberFormat getCurrencyNumberFormat() {
		return getUser().getCurrencyNumberFormat();
	}

	@Override
	public final NumberFormat getNumberFormat() {
		return getUser().getNumberFormat();
	}

	@Override
	public final NumberFormat getIntegerFormat() {
		return getUser().getIntegerFormat();
	}

	@Override
	public final NumberFormat getPercentFormat() {
		return getUser().getPercentFormat();
	}

	protected void setPageAndUserLocale(Locale locale) {
		if (locale != null) {
			Session.get().setLocale(locale);
			LocaleContextHolder.setLocale(locale);
			userPreferences.getUser().setLocale(locale);
		}
	}

	@Override
	public ILocale getILocale() {
		return getUser();
	}

	public AlertPanel getAlertPanel() {
		return (AlertPanel)get(AlertPanel.name);
	}

	public static class PageInitializationException extends RuntimeException {
		private final Class<? extends Page> redirectPage;

		public PageInitializationException(Class<? extends Page> redirectPage) {
			this.redirectPage = redirectPage;
		}

		public Class<? extends Page> getRedirectPage() {
			return redirectPage;
		}
	}

	private Locale getLocale(String language) {
		if (language == null)
			return null;
		if (language.compareTo(Locale.ENGLISH.getLanguage()) == 0)
			return Locale.ENGLISH;
		if (language.compareTo(Locale.GERMAN.getLanguage()) == 0)
			return Locale.GERMAN;
		if (language.compareTo(CustomLocale.LOCALE_CZ.getLanguage()) == 0)
			return CustomLocale.LOCALE_CZ;
		if (language.compareTo(User.localeSk.getLanguage()) == 0)
			return User.localeSk;
		return null;
	}

	private String getLanguage(PageParameters pageParameters) {
		String language = pageParameters.get("lang").toOptionalString();
		if (language == null)
			language = pageParameters.get("language").toOptionalString();
		if (language == null)
			language = pageParameters.get("locale").toOptionalString();
		return language;
	}
}
