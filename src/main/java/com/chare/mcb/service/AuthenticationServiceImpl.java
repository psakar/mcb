package com.chare.mcb.service;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.chare.core.Utils;
import com.chare.mcb.entity.User;
import com.chare.mcb.repository.UserRepository;

class AuthenticationServiceImpl implements AuthenticationService {

	static final String ERR_DISABLED_USER = "Disabled user";
	private final UserRepository userRepository;

	AuthenticationServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	//	@Transactional(propagation = Propagation.SUPPORTS)
	public User authenticate(String username, String password, String ipAddress) {
		if (StringUtils.isBlank(username))
			throw new IllegalArgumentException("Authenticate user error - username can not be blank");
		User user = userRepository.findByUsername(username);
		if (user == null)
			throw new IllegalStateException("User not found");
		if (!user.enabled) {
			user.recordUnsuccessfulLogin(new Date());
			userRepository.persist(user);
			throw new IllegalStateException(ERR_DISABLED_USER);
		}
		if (StringUtils.isEmpty(password) || !Utils.comparePasswords(user.getPassword(), Utils.encodePassword(password.getBytes()))) {
			user.recordUnsuccessfulLogin(new Date());
			userRepository.persist(user);
			throw new IllegalStateException("Wrong password");
		}
		user.setLoginTimestamp(new Date());
		User persisted = userRepository.persist(user);
		return persisted;
	}


}
