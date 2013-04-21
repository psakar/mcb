package com.chare.mcb.www;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

public class PropertyValidatorVisitor implements IVisitor<FormComponent<?>, Void> {

	private boolean hasPropertyValidator(FormComponent<?> fc) {
		//for (IValidator<?> validator : fc.getValidators()) {
		//			if (validator instanceof PropertyValidator) {
		//				return true;
		//			}
		//}
		return false;
	}
	//	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void component(FormComponent<?> component, IVisit<Void> visit) {
		//final ValidationContext validation = ValidationContext.get();

		if (!hasPropertyValidator(component)) {
			//			IProperty property = validation.resolveProperty(component.getModel());
			//			if (property != null) {
			//				component.add(new PropertyValidator());
			//			}
		}
	}

}
