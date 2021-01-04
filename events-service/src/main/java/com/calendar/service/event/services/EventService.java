package com.calendar.service.event.services;

import java.util.Collection;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.calendar.service.model.Event;


public interface EventService {

	@GetMapping(value = "/event/{eventId}", produces = "application/json")
	Event getEvent(@PathVariable Long eventId);
	
	@GetMapping(value = "/event", produces = "application/json")
	Collection<Event> getEvents();

	@PostMapping(value = "/event", consumes = "application/json", produces = "application/json")
	Event createEvent(@RequestBody  Event body, BindingResult bindingResult);

	@PutMapping(value = "/event", consumes = "application/json", produces = "application/json")
	Event updateEvent(@RequestBody Event body, BindingResult bindingResult);

	@DeleteMapping(value = "/event/{eventId}")
	void deleteEvent(@PathVariable Long eventId);

}
