package com.chare.mcb.www;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.*;

import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.util.visit.IVisit;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class ReadOnlyVisitorTest extends WicketTestCase {

	private ReadOnlyVisitor visitor;
	@Mock
	private IVisit<Void> visit;

	@Override
	@Before
	public void before() throws Exception {
		super.before();
		initMocks(this);
		visitor = new ReadOnlyVisitor();
	}

	@Test
	public void testVisitingDropDownChoice() throws Exception {
		DropDownChoice<String> choice = new DropDownChoice<String>("choice");
		visitor.component(choice, visit);
		verifyZeroInteractions(visit);
		assertFalse(choice.isEnabled());
	}

	@Test
	public void testVisitingRadioChoice() throws Exception {
		RadioChoice<String> choice = new RadioChoice<String>("choice");
		visitor.component(choice, visit);
		verifyZeroInteractions(visit);
		assertFalse(choice.isEnabled());
	}

	@Test
	public void testVisitingTextField() throws Exception {
		TextField<String> textField = spy(new TextField<String>("field"));
		visitor.component(textField, visit);
		verifyZeroInteractions(visit);
		assertTrue(textField.isEnabled());
		verify(textField).add((Behavior) anyVararg());
	}

}
