package com.calendar.service.event.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import com.calendar.service.event.persistence.EventRepository;
import com.calendar.service.model.Event;
import com.calendar.service.util.exceptions.InvalidInputException;
import com.calendar.service.util.exceptions.NotFoundException;

@RestController
public class EventServiceImpl implements EventService {
	
	private EventRepository eventRepository;	
	
	@Autowired
	public EventServiceImpl(EventRepository eventRepository) {
		this.eventRepository = eventRepository;
	}
	
	private static final Logger LOG = LoggerFactory.getLogger(EventServiceImpl.class);

	@Override
	public Event getEvent(Long eventId) {
		
		Event event = eventRepository.findByEventId(eventId).
				orElseThrow(() -> new NotFoundException("No event found for Id: " + eventId));

		LOG.debug(String.format("getEvent: found entity : %s", eventId));
		return event;
	}

	@Override
	public Collection<Event> getEvents() {
		List<Event> events =  new ArrayList<Event>();		
		eventRepository.findAll().forEach(events::add);
		
		LOG.debug(String.format("getEvents: found entitys % s"), events.size());
		return events;
	}

	@Override
	public Event createEvent(@Valid Event body, BindingResult bindingResult) {
		
		try {
			
			if(bindingResult.hasErrors()) {
	            throw new Exception(bindingResult.getFieldErrors().stream()
	            		.map(error -> error.getDefaultMessage())
	            		.reduce("",(message1, message2) -> message1.concat(", "+message2) ));
			}
			
            Event event = eventRepository.save(body);

            LOG.debug(String.format("createEvent: entity created for : %s", body));
            
            return event;

        } catch (Exception e) {
            throw new InvalidInputException(String.format("Invalid body, Error : %s , Event : %s ", e.getMessage(),  body));
        }
		
	}

	@Override
	public Event updateEvent(@Valid Event body, BindingResult bindingResult) {
		
		try {
			
			if(bindingResult.hasErrors()) {
	            throw new Exception(bindingResult.getFieldErrors().stream()
	            		.map(error -> error.getDefaultMessage())
	            		.reduce("",(message1, message2) -> message1.concat(message2) ));
			}
			
            Event event = eventRepository.save(body);

    		LOG.debug(String.format("updateEvent: entity update for : %s", body));
            
            return event;

		} catch (Exception e) {
            throw new InvalidInputException(String.format("Invalid body, Error : %s , Event : %s ", e.getMessage(),  body));
        }
		
	}

	@Override
	public void deleteEvent(Long eventId) {
        LOG.debug("deleteEvent: tries to delete an entity with eventId: {}", eventId);
        eventRepository.findByEventId(eventId).ifPresent(e -> eventRepository.delete(e));
	}
	
	
	
}
