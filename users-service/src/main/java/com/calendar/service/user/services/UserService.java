package com.calendar.service.user.services;



import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.calendar.service.model.User;

public interface UserService {
	
	@GetMapping(value = "/user/{userEmail}/", produces = "application/json")
	User getUser(@PathVariable String userEmail);
	
	@PostMapping(value = "/user", consumes = "application/json", produces = "application/json")
	User createUser(@RequestBody User body, BindingResult bindingResult);

	@PutMapping(value = "/user", consumes = "application/json", produces = "application/json")
	User updateUser(@RequestBody User body, BindingResult bindingResult);

}
