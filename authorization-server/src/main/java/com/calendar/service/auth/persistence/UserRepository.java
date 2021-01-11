package com.calendar.service.auth.persistence;

import org.springframework.data.repository.CrudRepository;

import com.calendar.service.model.User;

import java.util.Optional;


public interface UserRepository extends CrudRepository<User, Long>  {
	Optional<User> findUserByName(String userName);
	Optional<User> findUserByEmail(String userEmail);
}
