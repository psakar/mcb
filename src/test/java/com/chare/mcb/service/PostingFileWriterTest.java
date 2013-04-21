package com.chare.mcb.service;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.chare.core.Utils;
import com.chare.mcb.entity.Booking;
import com.chare.mcb.entity.PostingFile;
import com.chare.mcb.repository.SettingRepository;

public class PostingFileWriterTest {

	private static final File OUTPUT_DIRECTORY = new File(Utils.getTemporaryDirectory());
	private static final String OUTPUT_FILENAME = "posting.txt";

	@Mock
	private BookingFormatter bookingFormatter;
	@Mock
	private SettingRepository settingRepository;

	private PostingFileWriter postingFileWriter;
	private PostingFile postingFile;
	private File exportedFile;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		postingFileWriter = new PostingFileWriter(bookingFormatter, settingRepository);
		postingFile = createPostingFile();
		exportedFile = new File(OUTPUT_DIRECTORY, OUTPUT_FILENAME);
	}

	@After
	public void tearDown() {
		exportedFile.delete();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWritingToInvalidDirectory() throws Exception {
		postingFileWriter.write(postingFile);
	}

	private PostingFile createPostingFile() {
		PostingFile postingFile = new PostingFile();
		postingFile.filename = OUTPUT_FILENAME;
		postingFile.addBooking();
		postingFile.addBooking();
		return postingFile;
	}

	@Test
	public void testWrite() throws Exception {
		when(settingRepository.getValue(SettingRepository.EXPORT_DIR, null)).thenReturn(OUTPUT_DIRECTORY.getAbsolutePath());
		when(bookingFormatter.format(any(Booking.class))).thenReturn("X");
		PostingFileExporterImplTest.addBookings(postingFile, 2);

		postingFileWriter.write(postingFile);

		for (Booking booking : postingFile.getBookings()) {
			verify(bookingFormatter, times(1)).format(booking);
		}
		assertEquals(StringUtils.leftPad("", postingFile.getBookings().size(), "X"), FileUtils.readFileToString(exportedFile));
	}

}
