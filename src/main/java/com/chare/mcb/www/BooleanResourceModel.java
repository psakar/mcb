package com.chare.mcb.www;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

public class BooleanResourceModel extends AbstractReadOnlyModel<String> implements IModel<String> {
	private final Boolean value;

	public BooleanResourceModel(Boolean value) {
		this.value = value;
	}
	@Override
	public String getObject() {
		return (value != null && value ? new ResourceModel("yes").getObject() : new ResourceModel("no").getObject());
	}
}