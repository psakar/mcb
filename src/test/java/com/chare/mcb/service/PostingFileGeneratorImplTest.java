package com.chare.mcb.service;


import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.chare.core.Utils;
import com.chare.mcb.entity.PostingFile;
import com.chare.mcb.entity.Statement;
import com.chare.mcb.entity.StatementLine;
import com.chare.mcb.entity.TransferType;
import com.chare.mcb.entity.User;
import com.chare.mcb.repository.CalendarRepository;
import com.chare.mcb.repository.PostingFileRepository;
import com.chare.mcb.repository.SettingRepository;
import com.chare.mcb.repository.StatementRepository;
import com.chare.service.NameGenerator;

public class PostingFileGeneratorImplTest {


	private PostingFileGeneratorImpl generator;
	@Mock
	private PostingFileRepository postingFileRepository;
	@Mock
	private BookingGenerator bookingGenerator;
	@Mock
	private NameGenerator nameGenerator;
	@Mock
	private StatementRepository statementRepository;
	@Mock
	private CalendarRepository calendarRepository;
	@Mock
	private SettingRepository settingRepository;

	private Statement statement;
	private String filename;


	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		generator = new PostingFileGeneratorImpl(postingFileRepository, bookingGenerator, nameGenerator, statementRepository, calendarRepository, settingRepository);
		statement = new Statement();
		filename = "" + new Date().getTime();
		when(settingRepository.getValue(SettingRepository.FILENAME_TEMPLATE, SettingRepository.FILENAME_TEMPLATE_DEFAULT)).thenReturn(SettingRepository.FILENAME_TEMPLATE_DEFAULT);
		when(nameGenerator.createName(SettingRepository.FILENAME_TEMPLATE_DEFAULT)).thenReturn(filename);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyStatementCanNotGeneratePostingFile() throws Exception {
		generator.generatePostingFileFor(statement, new User(1, "user1"));
	}

	@Test
	public void testGenerate() throws Exception {
		addLines(statement, 2);
		when(calendarRepository.isHoliday(Utils.getToday())).thenReturn(false);

		PostingFile postingFile = generator.generatePostingFileFor(statement, new User(null, "user2"));

		assertForAllLinesWasGeneratedBookingAndBookDateSet(postingFile);
		verify(postingFileRepository, times(1)).persist(postingFile);
		verify(statementRepository, times(1)).persist(statement);
		assertEquals(filename, postingFile.filename);
	}

	private void assertForAllLinesWasGeneratedBookingAndBookDateSet(
			PostingFile postingFile) {
		for (StatementLine line : statement.getLines()) {
			verify(bookingGenerator, times(1)).generateFor(line, postingFile);
			assertEquals(Utils.getToday(), line.bookDate);
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPostingFileCanBeGeneratedOnlyDuringBusinessDay() throws Exception {
		addLines(statement, 2);
		when(calendarRepository.isHoliday(Utils.getToday())).thenReturn(true);

		generator.generatePostingFileFor(statement, new User(null, "user2"));

	}



	private void addLines(Statement statement, int count) {
		for(int i = 0; i < count; i++) {
			addLine(statement);
		}
	}

	public StatementLine addLine(Statement statement) {
		StatementLine statementLine = statement.addLine();
		statementLine.setId(statement.getLines().size());
		statementLine.transferType = new TransferType();
		return statementLine;
	}
}
