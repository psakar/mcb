package com.chare.mcb.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.chare.mcb.entity.User;
import com.chare.mcb.repository.UserRepository;

public class AuthenticationServiceImplTest {


	private AuthenticationServiceImpl authenticationService;

	@Mock
	private UserRepository userRepository;

	private User user;

	private Date lastAccess;

	private static final String PASSWORD = "start234";
	private static final String IP_ADDRESS = "1.1.1.1";

	@Before
	public void before() throws Exception {
		MockitoAnnotations.initMocks(this);
		authenticationService = new AuthenticationServiceImpl(userRepository);
		lastAccess = new Date();
		user = createUser();
		when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
	}

	private User createUser() {
		User user = new User(1, "username");
		user.setPassword(PASSWORD);
		user.setLoginTimestamp(lastAccess);
		return user;
	}

	@Test(expected = IllegalStateException.class)
	public void testAuthenticateNotExistingUser() throws Exception {
		authenticationService.authenticate("unknown_username", PASSWORD, IP_ADDRESS);
	}

	@Test
	public void testAuthenticateWithWrongPassword() throws Exception {
		try {
			authenticationService.authenticate(user.getUsername(), "wrong_password", IP_ADDRESS);
			fail("Expected " + IllegalStateException.class.getName());
		} catch (Exception e) {
			assertEquals(IllegalStateException.class, e.getClass());
		}
		verify(userRepository).persist(user);
	}

	@Test
	public void testAuthenticateWithCorrectPassword() throws Exception {
		when(userRepository.persist(user)).thenReturn(user);

		assertEquals(user, authenticationService.authenticate(user.getUsername(), PASSWORD, IP_ADDRESS));
		assertNotNull(user.getLoginTimestamp());
		assertEquals(lastAccess, user.getLastAccess());
	}

	@Test
	public void testDisabledUserCanNotAuthenticate() throws Exception {
		user.enabled = false;
		try {
			authenticationService.authenticate(user.getUsername(), PASSWORD, IP_ADDRESS);
			fail("Expected " + IllegalStateException.class.getName());
		} catch (Exception e) {
			assertEquals(IllegalStateException.class, e.getClass());
			assertEquals(AuthenticationServiceImpl.ERR_DISABLED_USER, e.getMessage());
		}
		verify(userRepository).persist(user);
	}


}
