package com.calendar.service.user.persistence;

import org.springframework.data.repository.CrudRepository;

import com.calendar.service.model.User;

import java.util.Optional;


public interface UserRepository extends CrudRepository<User, Long> {
	Optional<User> findByUserId(Long UserId);
}

