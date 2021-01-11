package com.calendar.service.auth;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.calendar.service.auth.persistence.UserRepository;
import com.calendar.service.model.User;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { AuthorizationServerApplication.class },
				properties = { "spring.data.mongodb.port: 0" })
public class AuthorizationServerApplicationTests {

	@Autowired
	private UserRepository repository;

	@Autowired
	private PasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private MockMvc mvc;

	@Before
	public void setupDb() {

		repository.deleteAll();

		User newEntity = new User("userTest1", bCryptPasswordEncoder.encode("1234567"), "user1@gmail.com");
		repository.save(newEntity);
	}

	@Test
	public void testAccessTokenIsObtainedUsingValidUserAndClient() throws Exception {
		mvc.perform(post("/oauth/token").with(httpBasic("client", "secret")).param("grant_type", "password")
				.param("username", "user1@gmail.com").param("password", "1234567"))
				.andExpect(jsonPath("$.access_token").exists()).andExpect(status().isOk());
	}
}
