package com.calendar.service.auth.builds;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.calendar.service.auth.persistence.UserRepository;
import com.calendar.service.model.User;

@Service
public class UserDetailsRepositoryService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public CustomUserDetails loadUserByUsername(String username) {
		Supplier<UsernameNotFoundException> s = () -> new UsernameNotFoundException("Problem during authentication!");

		User u = userRepository.findUserByEmail(username).orElseThrow(s);

		return new CustomUserDetails(u);

	}
}
