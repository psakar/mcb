package com.chare.mcb.service;


import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.chare.core.Utils;
import com.chare.mcb.entity.Booking;
import com.chare.mcb.entity.PostingFile;
import com.chare.mcb.entity.User;
import com.chare.mcb.repository.CalendarRepository;
import com.chare.mcb.repository.PostingFileRepository;
import com.chare.mcb.service.PostingFileExporterImpl.ReferenceGenerator;

public class PostingFileExporterImplTest {

	private PostingFileExporterImpl exporter;
	@Mock
	private PostingFileRepository postingFileRepository;
	@Mock
	private CalendarRepository calendarRepository;
	@Mock
	private PostingFileWriter postingFileWriter;
	@Mock
	private ReferenceGenerator referenceGenerator;

	private PostingFile postingFile;
	private User approvingUser;


	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		exporter = new PostingFileExporterImpl(postingFileRepository, calendarRepository, postingFileWriter, referenceGenerator);
		postingFile = new PostingFile();
		postingFile.setCreatedUser(new User(1, "user"));
		postingFile.filename = "" + new Date().getTime();

		approvingUser = new User(2, "user2");

	}

	@Test(expected = IllegalArgumentException.class)
	public void testSameUserCanNotExportPostingFile() throws Exception {
		exporter.export(postingFile, postingFile.getCreatedUser());
	}

	@Test
	public void testGenerate() throws Exception {
		addBookings(postingFile, 2);
		when(calendarRepository.isHoliday(Utils.getToday())).thenReturn(false);

		exporter.export(postingFile, approvingUser);

		verify(referenceGenerator).fillInReferences(postingFile.getBookings());
		verify(postingFileWriter).write(postingFile);
		verify(postingFileRepository).persist(postingFile);
		assertPostingFileMarkedApprovedBy(approvingUser);
	}


	private void assertPostingFileMarkedApprovedBy(User user) {
		assertEquals(user, postingFile.getApprovedUser());
		assertNotNull(postingFile.getApproved());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testFileCanBeGeneratedOnlyDuringBusinessDay() throws Exception {
		addBookings(postingFile, 2);
		when(calendarRepository.isHoliday(Utils.getToday())).thenReturn(true);

		exporter.export(postingFile, approvingUser);

	}

	@Test
	public void testDisapproveFile() throws Exception {
		exporter.disapprove(postingFile, approvingUser);
		verify(postingFileRepository).delete(postingFile);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testApprovedFileCanNotBeDisapproved() throws Exception {
		postingFile.setApprovedUser(approvingUser);
		exporter.disapprove(postingFile, approvingUser);
	}

	static void addBookings(PostingFile postingFile, int count) {
		for(int i = 0; i < count; i++) {
			Booking booking = postingFile.addBooking();
			booking.setId(i+1);
		}
	}
}
