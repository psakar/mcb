package com.chare.mcb.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.chare.core.Utils;
import com.chare.mcb.entity.Booking;
import com.chare.mcb.repository.PostingFileRepository;
import com.chare.mcb.service.PostingFileExporterImpl.ReferenceGenerator;

public class ReferenceGeneratorTest {




	private ReferenceGenerator generator;
	@Mock
	private PostingFileRepository postingFileRepository;

	@Before
	public void before() throws Exception {
		MockitoAnnotations.initMocks(this);
		generator = new ReferenceGenerator(postingFileRepository);
	}



	@Test
	public void testSequenceNumberAndTrReferenceIsFilledInForEveryBooking() throws Exception {
		List<Booking> bookings = Arrays.asList(new Booking(), new Booking());
		Date businessDate = Utils.getToday();
		int lastSequenceNr = 10;
		int lastReferenceNr = 9;
		when(postingFileRepository.findLastReferenceNr(businessDate)).thenReturn(lastReferenceNr);
		when(postingFileRepository.findLastSequenceNr(businessDate)).thenReturn(lastSequenceNr);

		generator.fillInReferences(bookings);

		assertSequenceNumberAndTrReferenceIsFilledIn(bookings, lastSequenceNr, lastReferenceNr);
	}



	private void assertSequenceNumberAndTrReferenceIsFilledIn(
			List<Booking> bookings, int lastSequenceNr, int lastReferenceNr) {
		for (Booking booking : bookings) {
			lastSequenceNr ++;
			lastReferenceNr ++;
			assertEquals(booking.createTrReference(Utils.getToday(), lastReferenceNr), booking.trReference);
			assertEquals(lastSequenceNr, booking.sequenceNr);
		}
	}
}
