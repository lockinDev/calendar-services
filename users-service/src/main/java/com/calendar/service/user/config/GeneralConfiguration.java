package com.calendar.service.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration 
public class GeneralConfiguration {
	@Bean
	PasswordEncoder passwordEncoder(){
	    return new BCryptPasswordEncoder();
	}
}
