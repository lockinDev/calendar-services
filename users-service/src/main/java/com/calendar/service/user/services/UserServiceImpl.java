package com.calendar.service.user.services;

import java.util.Collection;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import com.calendar.service.model.User;
import com.calendar.service.user.persistence.UserRepository;
import com.calendar.service.util.exceptions.InvalidInputException;
import com.calendar.service.util.exceptions.NotFoundException;

@RestController
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;
    private PasswordEncoder bCryptPasswordEncoder;
	private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);	
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository,
			PasswordEncoder bCryptPasswordEncoder) {
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
	
	@Override
	public User getUser(String email) {
		User user = getUserFromRepository(email).
				orElseThrow(() -> new NotFoundException("No user found for Id: " + email));

		LOG.debug(String.format("getUser: found entity : %s", email));
		return user;
	}


	@Override
	public User createUser(@Valid User body, BindingResult bindingResult) {
		try {
			
			String passwordEncode = bCryptPasswordEncoder.encode(body.getPassword());
			body.setPassword(passwordEncode);
			
			User user = userRepository.save(body);

			LOG.debug(String.format("createUser: entity created for : %s", body));

			return user;

		} catch (DuplicateKeyException dke) {
			throw new InvalidInputException("Duplicate key, User Id: " + body.getEmail());
		} catch (Exception e) {
			throw new InvalidInputException(
					String.format("Invalid body, Error : %s , User : %s ", e.getMessage(), body));
		}
	}

	@Override
	public User updateUser(User body, BindingResult bindingResult) {
		try {
			
			User userForPassword = getUserFromRepository(body.getEmail()).
					orElseThrow(() -> new NotFoundException("No user found for Email: " + body.getEmail()));
			
			body.setPassword(userForPassword.getPassword());
			
			User user = userRepository.save(body);

			LOG.debug(String.format("updateUser: entity update for : %s", body));

			return user;

		} catch (Exception e) {
			throw new InvalidInputException(
					String.format("Invalid body, Error : %s , User : %s ", e.getMessage(), body));
		}
	}

	private Optional<User> getUserFromRepository(String userEmail){
		return userRepository.findUserByEmail(userEmail);
	}

}
