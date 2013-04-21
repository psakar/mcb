package com.chare.mcb.www;

import org.apache.wicket.markup.html.form.IChoiceRenderer;

public class ChoiceRenderer<T extends ChoiceValue> implements IChoiceRenderer<T> {
	@Override
	public Object getDisplayValue(T object) {
		return object.getDescription() == null ? "" : object.getDescription();
	}

	@Override
	public String getIdValue(T object, int index) {
		return String.valueOf(object.getId());
	}
}