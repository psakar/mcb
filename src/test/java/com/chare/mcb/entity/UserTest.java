package com.chare.mcb.entity;

import static com.chare.mcb.entity.Role.*;
import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.chare.core.LanguageIndex;
import com.chare.core.Utils;

public class UserTest {

	private User user;

	@Before
	public void setup() throws Exception {
		user = new User();
	}

	@Test
	public void testDefaults() throws Exception {
		assertEquals(false, user.isTemplate());
		assertNull(user.getLastAccess());
		assertNull(user.getLoginTimestamp());
		assertTrue(user.enabled);
		assertEquals(0, user.getRoles().size());
		assertArrayEquals(Utils.encodePassword(User.DEFAULT_PASSWORD.getBytes()), user.getPassword());
	}

	@Test
	public void getFullname() throws Exception {
		assertFullname("", "", "");
		assertFullname("a b", "a", "b");
		assertFullname("a", "a", null);
		assertFullname("a", "a", "");
		assertFullname("b", null, "b");
		assertFullname("b", "", "b");
		assertFullname("", null, null);
	}

	private void assertFullname(String expected, String surname, String name) {
		user.surname = surname;
		user.name = name;
		assertEquals(expected, user.getFullname());
	}

	@Test
	public void testEquals() throws Exception {
		assertFalse(new User(null).equals(new User(null)));
		assertFalse(new User(null).equals(new User(1)));
		assertFalse(new User(1).equals(new User(null)));
		assertFalse(new User(1).equals(new User(2)));
		assertTrue(new User(2).equals(new User(2)));
	}

	@Test
	public void testIsLanguageIndex() throws Exception {
		assertTrue(user instanceof LanguageIndex);
	}

	@Test
	public void testSetPassword() throws Exception {
		assertPassword(null, null);
		assertPassword(Utils.encodePassword("start123".getBytes()), "start123");
	}

	public void assertPassword(byte[] expectedPassword, String password) {
		user.setPassword(password);
		assertArrayEquals(expectedPassword, user.getPassword());
	}

	@Test
	public void testResettingPassword() throws Exception {
		user.setUnsuccessfulCount(4);
		user.setPassword("password");
		user.resetPassword();
		assertEquals(0, user.getUnsuccessfulCount());
		assertArrayEquals(Utils.encodePassword(User.DEFAULT_PASSWORD.getBytes()), user.getPassword());
	}

	@Test
	public void testResettingUnsuccessfulCountForDisabledUser() throws Exception {
		user.enabled = false;
		user.setUnsuccessfulCount(4);
		user.resetUnsuccessfulCount();
		assertEquals(4, user.getUnsuccessfulCount());
	}

	@Test
	public void testResettingUnsuccessfulCountForEnabledUser() throws Exception {
		user.enabled = true;
		user.setUnsuccessfulCount(4);
		user.resetUnsuccessfulCount();
		assertEquals(0, user.getUnsuccessfulCount());
	}



	@Test
	public void testSetFirstLoginTimestamp() throws Exception {
		user.setLoginTimestamp(new Date());
		assertEquals(null, user.getLastAccess());
		//		user.setLastAccess(lastAccess);
		//		Date now = new Date();
		//		user.setLoginTimestamp(now);
		//		assertEquals(now, user.getLoginTimestamp());

	}
	@Test
	public void testSetNextLoginTimestamp() throws Exception {
		Date lastAccess = Utils.getYesterday();
		int unsuccessfulCount = 10;
		user.setUnsuccessfulCount(unsuccessfulCount);
		user.setLoginTimestamp(lastAccess);
		user.setLoginTimestamp(new Date());
		assertEquals(lastAccess, user.getLastAccess());
		assertEquals(unsuccessfulCount, user.getLastUnsuccessfulCount());
		assertEquals(0, user.getUnsuccessfulCount());
	}



	@Test
	public void testHasRoles() throws Exception {
		assertTrue(user.hasRoles());
		assertFalse(user.hasRoles(APP_ADMIN));

		user = new User();
		user.addRoles(APP_ADMIN_ROLE);
		assertTrue(user.hasRoles(APP_ADMIN));
		assertFalse(user.hasRoles(APP_ADMIN, USER_ADMIN));


		user = new User();
		user.addRoles(APP_ADMIN_ROLE, UPLOAD_STATEMENTS_ROLE, EXPORT_POSTINGS_ROLE);
		assertFalse(user.hasRoles(APP_ADMIN, USER_ADMIN));
		assertTrue(user.hasRoles(APP_ADMIN, UPLOAD_STATEMENTS));
	}

	@Test
	public void testHasAnyOfRoles() throws Exception {
		assertTrue(user.hasAnyOfRoles());
		assertFalse(user.hasAnyOfRoles(APP_ADMIN));

		user = new User();
		user.addRoles(APP_ADMIN_ROLE);
		assertFalse(user.hasAnyOfRoles(USER_ADMIN));
		assertTrue(user.hasAnyOfRoles(APP_ADMIN, USER_ADMIN));

	}



	@Test
	public void testRemoveRole() throws Exception {
		Role role = APP_ADMIN_ROLE;
		user.addRole(role);

		user.removeRole(role);

		assertFalse(user.hasRoles(role.code));
	}

	@Test
	public void testAuditInfo() throws Exception {
		user.id = 1;
		user.username = "username";
		user.surname = "surname";
		user.name = "name";
		user.addRole(APP_ADMIN_ROLE);
		assertEquals("user;1;name;surname;username;;;1;1;;0;0;[APP_ADMIN]", user.getAuditInfo());
	}
}
