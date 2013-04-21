package com.chare.mcb.entity;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PostingFileTest {

	private PostingFile postingFile;

	@Before
	public void setUp() {
		postingFile = new PostingFile();
	}

	@Test
	public void testDefaults() throws Exception {
		assertNull(postingFile.getCreated());
		assertFalse(postingFile.isExported());
	}

	@Test
	public void testAddBooking() throws Exception {
		Booking booking = postingFile.addBooking();
		assertEquals(postingFile, booking.postingFile);
		assertEquals(1, postingFile.getBookings().size());
	}

	@Test
	public void testSetCreatedUser() throws Exception {
		User user = new User();
		postingFile.setCreatedUser(user);
		assertNotNull(postingFile.getCreated());
		assertEquals(user, postingFile.getCreatedUser());
	}

	@Test
	public void testCanBeApprovedByDifferentUser() throws Exception {
		postingFile.setCreatedUser(new User(1));
		assertTrue(postingFile.canBeApprovedBy(new User(2)));
	}

	@Test
	public void testCanNotBeApprovedBySameUser() throws Exception {
		postingFile.setCreatedUser(new User(1));
		assertFalse(postingFile.canBeApprovedBy(postingFile.getCreatedUser()));
	}

	@Test
	public void testCanNotBeApprovedWhenAlreadyApproved() throws Exception {
		postingFile.setCreatedUser(new User(1));
		postingFile.setApprovedUser(new User(2));
		assertFalse(postingFile.canBeApprovedBy(new User(3)));
	}

	@Test
	public void testCanNotBeApprovedWhenNotSetCreatedBy() throws Exception {
		assertFalse(postingFile.canBeApprovedBy(new User(1)));
		assertFalse(postingFile.canBeApprovedBy(null));
	}

	@Test
	public void testSetApprovedUser() throws Exception {
		postingFile.setCreatedUser(new User(1));
		User user = new User(2);
		postingFile.setApprovedUser(user);
		assertNotNull(postingFile.getApproved());
		assertEquals(user, postingFile.getApprovedUser());
	}



	@Test
	public void testIsExportedWhenApprovedBy() throws Exception {
		postingFile.setCreatedUser(new User(1));
		postingFile.setApprovedUser(new User(2));
		assertTrue(postingFile.isExported());
	}

}
