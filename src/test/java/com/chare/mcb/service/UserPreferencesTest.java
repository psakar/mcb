package com.chare.mcb.service;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.chare.mcb.entity.User;

public class UserPreferencesTest {




	private UserPreferences preferences;


	@Before
	public void before() throws Exception {
		MockitoAnnotations.initMocks(this);
		preferences = new UserPreferences();
	}


	@Test
	public void testIsUserNotAuthenticated() throws Exception {
		assertFalse(preferences.isUserAuthenticated());
	}
	@Test
	public void testIsUserAuthenticated() throws Exception {
		preferences.setUser(new User(1));
		assertTrue(preferences.isUserAuthenticated());
	}
}
