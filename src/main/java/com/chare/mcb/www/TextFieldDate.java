package com.chare.mcb.www;

import java.text.Format;
import java.util.Date;

import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.chare.mcb.service.UserPreferences;

public class TextFieldDate extends com.chare.wicket.TextFieldDate {

	@SpringBean(name = com.chare.service.Config.USER_PREFERENCES_SESSION_ID)
	private UserPreferences userPreferences;

	public TextFieldDate(final String id) {
		super(id);
	}

	public TextFieldDate(final String id, final IModel<Date> object) {
		super(id, object);
	}

	@Override
	protected Format getFormat() {
		return userPreferences.getUser().getDateFormat();
	}

}
