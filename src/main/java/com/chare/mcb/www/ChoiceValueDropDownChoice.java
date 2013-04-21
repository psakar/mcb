package com.chare.mcb.www;

import java.util.List;
import java.util.Locale;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.chare.mcb.Application;
import com.chare.wicket.DropDownChoice;

public class ChoiceValueDropDownChoice extends DropDownChoice<ChoiceValue> {

	public ChoiceValueDropDownChoice(String id, IModel<ChoiceValue> model, List<ChoiceValue> choices) {
		super(id, model, choices, new ChoiceRenderer<ChoiceValue>());
	}

	public ChoiceValueDropDownChoice(String id) {
		super(id);
		setChoiceRenderer(new ChoiceRenderer<ChoiceValue>());
	}

	public static class ChoiceIdModel extends Model<ChoiceValue> {
		private ChoiceValue choice;
		private PropertyModel<Integer> model;

		public ChoiceIdModel(Object modelObject, String expression) {
			this.model = new PropertyModel<Integer>(modelObject, expression);

			Integer id = model.getObject();
			if (id != null)
				this.choice = new ChoiceValue(id, findDescription(id));
		}
		@Override
		public ChoiceValue getObject() {
			return choice;
		}
		@Override
		public void setObject(ChoiceValue object) {
			choice = object;
			if (choice != null)
				model.setObject(choice.getId());
		}

	}

	public static String findDescription(Integer id) {
		Locale locale = Application.findLocale(id);
		return locale == null ? null : locale.getLanguage();
	}

}
