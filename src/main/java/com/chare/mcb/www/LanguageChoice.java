package com.chare.mcb.www;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.chare.mcb.www.ChoiceValueDropDownChoice.ChoiceIdModel;
import com.chare.wicket.DropDownChoice;

public class LanguageChoice extends DropDownChoice<ChoiceValue> {


	public static List<ChoiceValue> convertToChoices(List<Locale> locales) {
		List<ChoiceValue> choices = new ArrayList<ChoiceValue>();
		int index = 1;
		for (Locale locale : locales) {
			choices.add(new ChoiceValue(index, locale.getLanguage()));
			index ++;
		}
		return choices;
	}

	public LanguageChoice(String id, ChoiceIdModel model, List<? extends ChoiceValue> choices) {
		super(id, model, choices, new ChoiceRenderer<ChoiceValue>());
		setNullValid(false);
	}

}