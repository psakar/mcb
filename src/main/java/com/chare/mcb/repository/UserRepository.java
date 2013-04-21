package com.chare.mcb.repository;

import com.chare.mcb.entity.User;
import com.chare.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Integer, User> {

	User findByUsername(String username);

}
