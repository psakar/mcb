package com.chare.mcb.www;

import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.chare.mcb.Application;

@Secured(required = { }, requiredAnyOf = { })
public final class LogoutPage extends Page {

	public LogoutPage(PageParameters pageParameters) {
		super(pageParameters);
		String localeCode = getUser().getLocaleCode();
		getSession().invalidateNow();
		throw new RedirectToUrlException(Application.LOGIN_PAGE + "?locale=" + localeCode);
	}

}
