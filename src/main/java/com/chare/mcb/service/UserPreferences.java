package com.chare.mcb.service;

import com.chare.mcb.entity.User;


public class UserPreferences extends com.chare.service.UserPreferences<User> {

	public UserPreferences() {
		super(new User());
	}

	public boolean isUserAuthenticated() {
		return getUser().isPersistent();
	}

}
