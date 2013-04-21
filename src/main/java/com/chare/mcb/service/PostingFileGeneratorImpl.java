package com.chare.mcb.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.chare.core.Utils;
import com.chare.mcb.entity.PostingFile;
import com.chare.mcb.entity.Statement;
import com.chare.mcb.entity.StatementLine;
import com.chare.mcb.entity.User;
import com.chare.mcb.repository.CalendarRepository;
import com.chare.mcb.repository.PostingFileRepository;
import com.chare.mcb.repository.SettingRepository;
import com.chare.mcb.repository.StatementRepository;
import com.chare.service.NameGenerator;

class PostingFileGeneratorImpl implements PostingFileGenerator {

	private final PostingFileRepository postingFileRepository;
	private final BookingGenerator bookingGenerator;
	private final NameGenerator nameGenerator;
	private final StatementRepository statementRepository;
	private final CalendarRepository calendarRepository;
	private final SettingRepository settingRepository;

	PostingFileGeneratorImpl(PostingFileRepository postingFileRepository, BookingGenerator bookingGenerator, NameGenerator nameGenerator, StatementRepository statementRepository, CalendarRepository calendarRepository, SettingRepository settingRepository) {
		this.postingFileRepository = postingFileRepository;
		this.bookingGenerator = bookingGenerator;
		this.nameGenerator = nameGenerator;
		this.statementRepository = statementRepository;
		this.calendarRepository = calendarRepository;
		this.settingRepository = settingRepository;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public PostingFile generatePostingFileFor(Statement statement, User user) {
		assertPostingFileCanBeGenerated(statement, user.username);
		assertBusinessDay();
		PostingFile postingFile = generatePostingFile(statement, user, createFilename());
		postingFileRepository.persist(postingFile);
		setStatementBooked(statement, postingFile.getId());
		statementRepository.persist(statement);
		return postingFile;
	}

	private String createFilename() {
		return nameGenerator.createName(getFilenameTemplate());
	}

	private String getFilenameTemplate() {
		return settingRepository.getValue(SettingRepository.FILENAME_TEMPLATE, SettingRepository.FILENAME_TEMPLATE_DEFAULT);
	}

	protected void setStatementBooked(Statement statement, Integer postingFileId) {
		statement.postingFileId = postingFileId;
		for (StatementLine line : statement.getLines()) {
			line.bookDate = Utils.getToday();
		}
	}

	private PostingFile generatePostingFile(Statement statement, User user, String filename) {
		PostingFile postingFile = new PostingFile();
		postingFile.filename = filename;
		postingFile.setCreatedUser(user);
		for (StatementLine line : statement.getLines()) {
			bookingGenerator.generateFor(line, postingFile);
		}
		return postingFile;
	}

	private void assertPostingFileCanBeGenerated(Statement statement,
			String username) {
		if (!statement.isReadyToGenerateBookings())
			throw new IllegalArgumentException("Posting file can not be generated.");//FIXME localize
	}

	private void assertBusinessDay() {
		if (calendarRepository.isHoliday(Utils.getToday()))
			throw new IllegalArgumentException("Posting file can be generated only during business day.");//FIXME localize
	}

}
