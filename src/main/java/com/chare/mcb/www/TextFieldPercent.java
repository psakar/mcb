package com.chare.mcb.www;

import java.text.DecimalFormat;
import java.text.Format;

import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.chare.mcb.service.UserPreferences;

public class TextFieldPercent extends com.chare.wicket.TextFieldPercent {

	@SpringBean(name = com.chare.service.Config.USER_PREFERENCES_SESSION_ID)
	private UserPreferences userPreferences;

	public TextFieldPercent(final String id) {
		super(id);
	}

	public TextFieldPercent(final String id, final IModel<Number> object) {
		super(id, object);
	}

	@Override
	protected Format getFormat() {
		return userPreferences.getUser().getPercentFormat();
	}

	@Override
	public String getTextFormat() {
		return ((DecimalFormat) userPreferences.getUser().getPercentFormat()).toPattern();
	}
}
