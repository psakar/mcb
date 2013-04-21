package com.chare.mcb.www;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.chare.wicket.Label;

public class LabelMandatory extends Label {

	public LabelMandatory(final String id) {
		super(id);
		setCssStyle();
	}

	public LabelMandatory(final String id, String label) {
		this(id, new Model<String>(label));
	}

	public LabelMandatory(final String id, IModel<String> model) {
		super(id, model);
		setCssStyle();
	}

	public void setCssStyle() {
		setRenderBodyOnly(false);
		add(new AttributeModifier("style", new Model<String>("color:blue")));
	}

	//@Override
	//protected String getCssStyle() {
	//	return "mandatory";
	//}
}
