package com.chare.mcb.www;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;


public class ReadOnlyVisitor implements IVisitor<Component, Void> {
	@Override
	public void component(final Component component, final IVisit<Void> visit) {
		if (DropDownChoice.class.isAssignableFrom(component.getClass()) ||
				RadioGroup.class.isAssignableFrom(component.getClass()) ||
				RadioChoice.class.isAssignableFrom(component.getClass()) ||
				Radio.class.isAssignableFrom(component.getClass()) ||
				//TextFieldDate.class.isAssignableFrom(component.getClass()) ||
				CheckBox.class.isAssignableFrom(component.getClass()) ||
				org.apache.wicket.markup.html.form.Button.class.isAssignableFrom(component.getClass()))
			component.setEnabled(false);
		else
			component.add(new AttributeModifier("readonly", new Model<String>("readonly")));
	}
}