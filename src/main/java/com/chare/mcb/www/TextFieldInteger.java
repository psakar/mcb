package com.chare.mcb.www;

import java.text.DecimalFormat;
import java.text.Format;

import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.chare.mcb.service.UserPreferences;

public class TextFieldInteger extends com.chare.wicket.TextFieldInteger {

	@SpringBean(name = com.chare.service.Config.USER_PREFERENCES_SESSION_ID)
	private UserPreferences userPreferences;

	public TextFieldInteger(final String id) {
		super(id);
	}

	public TextFieldInteger(final String id, final IModel<Integer> object) {
		super(id, object);
	}

	@Override
	protected Format getFormat() {
		return userPreferences.getUser().getIntegerFormat();
	}

	@Override
	public String getTextFormat() {
		return ((DecimalFormat) userPreferences.getUser().getIntegerFormat()).toPattern();
	}
}
