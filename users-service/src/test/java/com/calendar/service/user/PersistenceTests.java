package com.calendar.service.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.calendar.service.model.User;
import com.calendar.service.user.persistence.UserRepository;

@RunWith(SpringRunner.class)
@DataMongoTest
public class PersistenceTests {
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private PasswordEncoder bCryptPasswordEncoder;
	
	@Before
	public void setupDb() {
		repository.deleteAll();
		
		User entity = new User("userTest", "1234567", "user@gmail.com");
		repository.save(entity);				
	}
	
	@Test
	public void create() {
		User newEntity = new User("userTest1", "1234567", "user1@gmail.com");
		repository.save(newEntity);

		User foundEntity = repository.findUserByEmail(newEntity.getEmail()).get();
		assertTrue(foundEntity.equals(newEntity));

		assertEquals(2, repository.count());
	}
	
	@Test
	public void samePassword() { 
		String password = "1234567";
		User newEntity = new User("userTest1", bCryptPasswordEncoder.encode(password),
				"user1@gmail.com");
		
		repository.save(newEntity);

		User foundEntity = repository.findUserByEmail(newEntity.getEmail()).get();
		assertTrue(bCryptPasswordEncoder.matches(password, foundEntity.getPassword()));
				
	}
	
	@Test(expected = DuplicateKeyException.class)
   	public void duplicateError() {		
		User entity = new User("userTest", "1234567", "user@gmail.com");
        repository.save(entity);
    }
	
	@Test(expected = ConstraintViolationException.class)
   	public void passwordLenghtError() {		
		User entity = new User("userTest1", "12345", "user1@gmail.com");
        repository.save(entity);
    }
	
	@Test(expected = ConstraintViolationException.class)
   	public void nameEmptyError() {		
		User entity = new User("", "12345", "user1@gmail.com");
        repository.save(entity);
    }
	
	@Test(expected = ConstraintViolationException.class)
   	public void emailBadFormatError() {		
		User entity = new User("userTest1", "12345", "user");
        repository.save(entity);
    }
	

}
