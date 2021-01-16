package com.calendar.service.user;

import static junit.framework.TestCase.assertTrue;
import static org.assertj.core.api.Assertions.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.calendar.service.model.User;
import com.calendar.service.model.User;
import com.calendar.service.user.persistence.UserRepository;
import com.calendar.service.user.services.UserServiceImpl;
import com.calendar.service.util.http.GlobalControllerExceptionHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=RANDOM_PORT, properties = {"spring.data.mongodb.port: 0"})
public class UserServiceApplicationTests {

	@Autowired
	private UserRepository repository;
	
	@Autowired
	private UserServiceImpl userServiceImpl; 
	
	@Autowired
	private GlobalControllerExceptionHandler globalControllerExceptionHandler;
	
	@Autowired
	private PasswordEncoder bCryptPasswordEncoder;
	
    private MockMvc client;    
	ObjectMapper objectMapper = new ObjectMapper();
	
	@Before
	public void setupDb() {
		
		client = MockMvcBuilders.standaloneSetup(userServiceImpl).
				setControllerAdvice(globalControllerExceptionHandler ).build();
		
		repository.deleteAll();
	}
	
	@Test
	public void getUserByEmail() throws Exception {

		User newEntity = new User("userTest1", "1234567", "user1@gmail.com");

		postAndVerifyUser(newEntity, OK);

		assertTrue(repository.findUserByEmail(newEntity.getEmail()).isPresent());

		getAndVerifyUser(newEntity.getEmail(), OK);
	}
	
	
	@Test
	public void verifyUpdatePassword() throws Exception {

		String password = "1234567"; 
		User newEntity = new User("userTest2", password, "user2@gmail.com");

		postAndVerifyUser(newEntity, OK);
		
		newEntity = getAndVerifyUser(newEntity.getEmail(), OK);
		
		newEntity.setName("user");
		newEntity.setPassword("12345678");
		
		User user = putAndVerifyUser(newEntity, OK);
		
		assertTrue(bCryptPasswordEncoder.matches(password, user.getPassword()));

	}
		
	
	private User getAndVerifyUser(String email, HttpStatus expectedStatus) throws Exception {
		return getAndVerifyUserRequest(email, expectedStatus);
	}

	private User getAndVerifyUserRequest(String email, HttpStatus expectedStatus) throws Exception {
		ResultActions resultActions = client.perform(get("/user/"+email+"/").
				 accept(MediaType.APPLICATION_JSON).
				 contentType(MediaType.APPLICATION_JSON)
		).andExpect(matcher(expectedStatus))
		 .andExpect(jsonPath("$[*]", Matchers.hasSize(5)))
       .andExpect(jsonPath("$.email").value(email));
		
		return convertResponse(resultActions);	
	}

	public void postAndVerifyUser(User user, HttpStatus expectedStatus) throws Exception {
		String json = objectMapper.writeValueAsString(user);
		System.out.println("///////*****" + json);
		 client.perform(
				post("/user").contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("utf-8")
				.content(json)).andExpect(matcher(expectedStatus)).andReturn();
	
	}
	
	public User putAndVerifyUser(User user, HttpStatus expectedStatus) throws Exception {
		String json = objectMapper.writeValueAsString(user);
		 ResultActions resultActions = client.perform(
				put("/user").contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("utf-8")
				.content(json)).andExpect(matcher(expectedStatus))
		 .andExpect(jsonPath("$.name").value(user.getName()));
		 
		 return convertResponse(resultActions);		 		
	}
	
	private User convertResponse(ResultActions resultActions) throws UnsupportedEncodingException, JsonMappingException, JsonProcessingException {
		MvcResult mvcResult = resultActions.andReturn();
		String contentString = mvcResult.getResponse().getContentAsString();
		
		return objectMapper.readValue(contentString,  User.class);
	}
	
	private ResultMatcher matcher(HttpStatus status) {
		return result -> assertEquals("Status", status.value(), result.getResponse().getStatus());
	}
	
	
}
