package com.chare.mcb.entity;


import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

public class FeeCalculationTest {

	private FeeCalculation calculation;

	@Before
	public void setUp() {
		calculation = new FeeCalculation();
	}

	@Test
	public void testDefaults() throws Exception {
		assertEquals(BigDecimal.ZERO, calculation.amount);
		assertEquals(BigDecimal.ZERO, calculation.percentage);
	}

	@Test
	public void testEquals() throws Exception {
		calculation.amount = BigDecimal.ONE;
		assertTrue(calculation.equals(new FeeCalculation(BigDecimal.ONE, BigDecimal.ZERO)));

		FeeCalculation other =  new FeeCalculation(BigDecimal.ONE, BigDecimal.TEN);
		assertFalse(calculation.equals(other));
	}

	@Test
	public void testCopyFrom() throws Exception {
		FeeCalculation other =  new FeeCalculation(BigDecimal.ONE, BigDecimal.TEN);

		calculation.copyFrom(other);

		assertEquals(other, calculation);
	}



	@Test
	public void testCalculate() throws Exception {
		calculation.amount = new BigDecimal("1.5");
		calculation.percentage = new BigDecimal("10");

		assertEquals( new BigDecimal("-1.70"), calculation.calculate(new BigDecimal("-2")));

	}
}
