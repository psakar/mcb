package com.chare.mcb.service;

import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.chare.core.Utils;
import com.chare.mcb.entity.Booking;
import com.chare.mcb.entity.PostingFile;
import com.chare.mcb.entity.User;
import com.chare.mcb.repository.CalendarRepository;
import com.chare.mcb.repository.PostingFileRepository;

class PostingFileExporterImpl implements PostingFileExporter {

	private final PostingFileRepository postingFileRepository;
	private final CalendarRepository calendarRepository;
	private final PostingFileWriter postingFileWriter;
	private final ReferenceGenerator referenceGenerator;
	PostingFileExporterImpl(PostingFileRepository postingFileRepository, CalendarRepository calendarRepository, PostingFileWriter postingFileWriter, ReferenceGenerator referenceGenerator) {
		this.postingFileRepository = postingFileRepository;
		this.calendarRepository = calendarRepository;
		this.postingFileWriter = postingFileWriter;
		this.referenceGenerator = referenceGenerator;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void export(PostingFile postingFile, User user) {
		assertPostingFileCanBeExported(postingFile, user);
		assertBusinessDay();
		referenceGenerator.fillInReferences(postingFile.getBookings());
		postingFileWriter.write(postingFile);
		postingFile.setApprovedUser(user);
		postingFileRepository.persist(postingFile);
	}

	static class ReferenceGenerator {
		private final PostingFileRepository postingFileRepository;

		ReferenceGenerator(PostingFileRepository postingFileRepository) {
			this.postingFileRepository = postingFileRepository;
		}

		public void fillInReferences(List<Booking> bookings) {
			Date businessDate = Utils.getToday();
			int sequenceNr = postingFileRepository.findLastSequenceNr(businessDate);
			int referenceNr = postingFileRepository.findLastReferenceNr(businessDate);
			for (Booking booking : bookings) {
				referenceNr++;
				sequenceNr++;
				booking.sequenceNr = sequenceNr;
				booking.trReference = booking.createTrReference(businessDate, referenceNr);
			}
		}
	}

	private void assertPostingFileCanBeExported(PostingFile postingFile, User user) {
		if (!postingFile.canBeApprovedBy(user))
			throw new IllegalArgumentException("Posting file must be exported by other user.");//FIXME localize
	}

	private void assertBusinessDay() {
		if (calendarRepository.isHoliday(Utils.getToday()))
			throw new IllegalArgumentException("Posting file can be exported only during business day.");//FIXME localize
	}

	@Override
	public void disapprove(PostingFile postingFile, User user) {
		if (postingFile.isExported())
			throw new IllegalArgumentException("Exported posting file can not be disapproved.");//FIXME localize
		postingFileRepository.delete(postingFile);
	}

}
