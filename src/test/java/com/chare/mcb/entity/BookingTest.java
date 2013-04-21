package com.chare.mcb.entity;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.chare.core.Utils;

public class BookingTest {

	private Booking booking;

	@Before
	public void setUp() {
		booking = new Booking();
	}

	@Test
	public void testDefaults() throws Exception {
		assertEquals(0, booking.sequenceNr);
		assertEquals(Booking.CURRENCY, booking.currency);
		assertEquals(BigDecimal.ZERO, booking.amount);
		assertEquals(Utils.getToday(), booking.businessDate);
	}

}
