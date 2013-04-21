package com.chare.mcb.www;

import java.text.DecimalFormat;
import java.text.Format;

import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.chare.mcb.service.UserPreferences;

public class TextFieldNumber extends com.chare.wicket.TextFieldNumber {

	@SpringBean(name = com.chare.service.Config.USER_PREFERENCES_SESSION_ID)
	private UserPreferences userPreferences;

	public TextFieldNumber(final String id) {
		super(id);
	}

	public TextFieldNumber(final String id, final IModel<Number> object) {
		super(id, object);
	}

	@Override
	protected Format getFormat() {
		return userPreferences.getUser().getNumberFormat();
	}
	@Override
	public String getTextFormat() {
		return ((DecimalFormat) userPreferences.getUser().getNumberFormat()).toPattern();
	}

}
