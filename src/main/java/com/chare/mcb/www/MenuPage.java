package com.chare.mcb.www;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.chare.mcb.Application;

@Secured(required = {}, requiredAnyOf = {})
public class MenuPage extends PageWithMenu {

	static final String IFRAME_ID = "iframe";

	public MenuPage(PageParameters pageParameters) {
		super(pageParameters);
		add(createIframe(IFRAME_ID));
	}

	protected WebMarkupContainer createIframe(String id) {
		WebMarkupContainer iframe = new WebMarkupContainer(id);
		String url = Application.PROFILE_PAGE;
		iframe.add(new AttributeModifier("src", new Model<String>(url)));
		return iframe;
	}

}
