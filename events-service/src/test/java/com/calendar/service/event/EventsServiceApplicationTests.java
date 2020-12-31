package com.calendar.service.event;

import static junit.framework.TestCase.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Date;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.calendar.service.event.persistence.EventRepository;
import com.calendar.service.event.services.EventServiceImpl;
import com.calendar.service.model.Event;
import com.calendar.service.model.User;
import com.calendar.service.util.http.GlobalControllerExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=RANDOM_PORT, properties = {"spring.data.mongodb.port: 0"})
public class EventsServiceApplicationTests {
 	
	@Autowired
	MongoOperations mongoTemplate;
	
	@Autowired
	private EventRepository repository;
	
	@Autowired
	private EventServiceImpl eventServiceImpl; 
	
	@Autowired
	private GlobalControllerExceptionHandler globalControllerExceptionHandler;
	
    private MockMvc client;    
	private User user;
	ObjectMapper objectMapper = new ObjectMapper();
	
	@Before
	public void setupDb() {
		
		client = MockMvcBuilders.standaloneSetup(eventServiceImpl).
				setControllerAdvice(globalControllerExceptionHandler ).build();
		
		repository.deleteAll();
		mongoTemplate.dropCollection(User.class);
		
		user = new User("userTest", "1234567", "user@gmail.com");
		mongoTemplate.insert(user, "users");
	}
	
	@Test
	public void getEventById() throws Exception {

		long eventId = 1;
		Event event = new Event(eventId, user, "Title test", "notes test", new Date(), new Date());

		postAndVerifyEvent(event, OK);

		assertTrue(repository.findByEventId(eventId).isPresent());

		getAndVerifyEvent(eventId, OK);
	}
	
	@Test
	public void duplicateError() throws Exception {

		long eventId = 1;
		Event event = new Event(eventId, user, "Title test", "notes test", new Date(), new Date());

		postAndVerifyEvent(event, OK);

		assertTrue(repository.findByEventId(eventId).isPresent());

		postAndVerifyEvent(event, UNPROCESSABLE_ENTITY);
	}
	
	
	
	private void getAndVerifyEvent(Long eventId, HttpStatus expectedStatus) throws Exception {
		 getAndVerifyEvent(eventId.toString(), expectedStatus);
	}

	private void getAndVerifyEvent(String eventIdPath, HttpStatus expectedStatus) throws Exception {
		 client.perform(get("/event/"+eventIdPath).
				 accept(MediaType.APPLICATION_JSON_UTF8).
				 contentType(MediaType.APPLICATION_JSON_UTF8)
		).andExpect(matcher(expectedStatus))
		 .andExpect(jsonPath("$[*]", Matchers.hasSize(8)))
        .andExpect(jsonPath("$.eventId").value(eventIdPath));
	}

	public void postAndVerifyEvent(Event event, HttpStatus expectedStatus) throws Exception {
		String json = objectMapper.writeValueAsString(event);
		 client.perform(
				post("/event").contentType(MediaType.APPLICATION_JSON_UTF8)
				.characterEncoding("utf-8")
				.content(json)).andExpect(matcher(expectedStatus)).andReturn();
	
	}

	private void deleteAndVerifyEvent(Long eventId, HttpStatus expectedStatus) {
		/*return client.delete()
			.uri("/event/" + eventId)
			.accept(APPLICATION_JSON)
			.exchange()
			.expectStatus().isEqualTo(expectedStatus)
			.expectBody();*/
	}
	
	private ResultMatcher matcher(HttpStatus status) {
		return result -> assertEquals("Status", status.value(), result.getResponse().getStatus());
	}
	
	
}
