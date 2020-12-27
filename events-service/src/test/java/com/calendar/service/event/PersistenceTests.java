package com.calendar.service.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import com.calendar.service.event.persistence.EventRepository;
import com.calendar.service.model.Event;
import com.calendar.service.model.User;
import com.calendar.service.validators.EventDatesValidator;

@RunWith(SpringRunner.class)
@DataMongoTest
public class PersistenceTests {

	@Autowired
	MongoOperations mongoTemplate;
	
	@Autowired
	private EventRepository repository;
		
	@Autowired
	private EventDatesValidator eventDatesValidator;

	private Event savedEntity;
	private User user;	


	@Before
	public void setupDb() {
		repository.deleteAll();
		mongoTemplate.dropCollection(User.class);
		
		user = new User("userTest", "1234567", "user@gmail.com");
		mongoTemplate.insert(user, "users");
				
		Event entity = new Event(new Long(1), user, "Title test", "notes test", new Date(), new Date());
		savedEntity = repository.save(entity);				 
	}

	@Test
	public void create() {
		Event newEntity = new Event(new Long(2), user, "Title test", "notes test", new Date(), new Date());
		repository.save(newEntity);

		Event foundEntity = repository.findByEventId(newEntity.getEventId()).get();
		foundEntity.equals(newEntity);

		assertEquals(2, repository.count());
	}

	@Test
	public void update() {
		
		savedEntity.setNotes("new note");
		repository.save(savedEntity);

		Event foundEntity = repository.findByEventId(savedEntity.getEventId()).get();
		assertEquals(1, (long) foundEntity.getVersion());
		assertEquals("new note", foundEntity.getNotes());
	}

	@Test
	public void delete() {
		repository.delete(savedEntity);
		assertFalse(repository.existsById(savedEntity.getEventId()));
	}
	
	@Test(expected = DuplicateKeyException.class)
   	public void duplicateError() {		
		Event entity = new Event(new Long(1), user, "Title test", "notes test", new Date(), new Date());
        repository.save(entity);
    }
	
	@Test(expected = ConstraintViolationException.class)
   	public void nameError() {		
		Event entity = new Event(new Long(3), user, null, "", new Date(), new Date());
        repository.save(entity);
    }
	
	@Test()
   	public void datesError()  {		
		
		LocalDateTime startDateTime = LocalDateTime.now().minusMinutes(1);
		LocalDateTime endDateTime = LocalDateTime.now();				
		
		Event entity = new Event(new Long(4), user, "Title test", "notes test", 
				LocalDateTimeToDate(endDateTime), LocalDateTimeToDate(startDateTime));
		
		Errors errors = new BeanPropertyBindingResult(entity, "event");
				
		eventDatesValidator.validate(entity, errors);
		
		assertEquals(errors.hasErrors(), true);
		
    }
	
	
	private Date LocalDateTimeToDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone( ZoneId.systemDefault()).toInstant());
	}
	
		

}
