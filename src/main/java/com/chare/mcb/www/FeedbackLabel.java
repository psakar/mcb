package com.chare.mcb.www;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Label displaying feedback messages for FormComponents.
 * <p>
 * You can use this Label to show the error message near the actual
 * FormComponent, instead of in the FeedbackPanel It's safe to remove the
 * FeedbackPanel if you use this class for every FormComponent in your Form.
 * <p>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class FeedbackLabel extends Label {

	/**
	 * Field component holds a reference to the {@link Component} this
	 * FeedbackLabel belongs to
	 */

	private FormComponent component;
	/** Field text holds a model of the text to be shown in the FeedbackLabel */
	private IModel text = null;


	public FeedbackLabel(String id, FormComponent component) {
		super(id, new Model<String>());
		this.component = component;
	}

	/**
	 * Call this constructor if you just want to display the FeedbackMessage of
	 * the component
	 *
	 * @param id
	 *          The non-null id of this component
	 * @param component
	 *          The {@link FormComponent} to show the FeedbackMessage for.
	 */
	public FeedbackLabel(String id, IModel<String> model, FormComponent component) {
		super(id, model);
		this.component = component;
	}

	/**
	 * Call this constructor if you want to display a custom text
	 *
	 * @param id
	 *          The non-null id of this component
	 * @param component
	 *          The {@link FormComponent} to show the custom text for.
	 * @param text
	 *          The custom text to show when the FormComponent has a
	 *          FeedbackMessage
	 */

	public FeedbackLabel(String id, FormComponent component, String text) {
		this(id, component, new Model(text));
	}

	/**
	 * Call this constructor if you want to display a custom model (for easy i18n)
	 *
	 * @param id
	 *          The non-null id of this component
	 * @param component
	 *          The {@link FormComponent} to show the custom model for.
	 * @param iModel
	 *          The custom model to show when the {@link FormComponent} has a
	 *          FeedbackMessage
	 */
	public FeedbackLabel(String id, FormComponent component, IModel iModel) {
		super(id);
		this.component = component;
		this.text = iModel;
	}

	/**
	 * Set the content of this FeedbackLabel, depending on if the component has a
	 * FeedbackMessage.
	 *
	 * The HTML class attribute will be filled with the error level of the
	 * feedback message. That way, you can easily style different messages
	 * differently. Examples:
	 *
	 * class = "feedbacklabel INFO" class = "feedbacklabel ERROR" class =
	 * "feedbacklabel DEBUG" class = "feedbacklabel FATAL"
	 *
	 *
	 * @see Component
	 */
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		this.setDefaultModel(null);
		if (component.getFeedbackMessage() != null) {
			if (this.text != null) {
				this.setDefaultModel(text);
			} else {
				this.setDefaultModel(new Model(component.getFeedbackMessage().getMessage()));
			}

			//this.add(new AttributeModifier("class", new Model("feedbacklabel " + component.getFeedbackMessage().getLevelAsString())));
			this.add(new AttributeModifier("style", new Model("color:red")));
		} else {
			this.setDefaultModel(null);
		}
	}
}