package com.chare.mcb.service;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.chare.mcb.entity.CardStatement;

public class CardStatementFactoryForJasperReportDesignerTest {

	private CardStatementFactoryForJasperReportDesigner factory;

	@Before
	public void setUp() throws Exception {
		factory = new CardStatementFactoryForJasperReportDesigner();
	}



	@Test
	public void testGeneratesTestStatement() throws Exception {
		CardStatement statement = factory.create();
		assertEquals(50, statement.lines.size());

	}
}
