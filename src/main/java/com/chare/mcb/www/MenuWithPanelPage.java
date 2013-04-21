package com.chare.mcb.www;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.chare.wicket.panel.Panel;
import com.chare.wicket.panel.TableLayoutManager;

public abstract class MenuWithPanelPage extends Page {

	public static final String PANEL_ID = "panel";

	public MenuWithPanelPage(PageParameters pageParameters) {
		super(pageParameters);
		add(createTitle());
		add(createPanel());
	}

	protected Label createTitle() {
		return new Label("bodyTitle", createTitleModel());
	}

	protected Component createPanel() {
		return new Panel<Void>(PANEL_ID, null, new TableLayoutManager(1).setWidth(800).setBorder(0));
	}

	@SuppressWarnings("unchecked")
	public Panel<Void> getPanel() {
		return (Panel<Void>) get(PANEL_ID);
	}
}
