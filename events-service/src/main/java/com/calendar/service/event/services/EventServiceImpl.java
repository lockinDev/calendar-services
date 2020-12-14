package com.calendar.service.event.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.calendar.service.event.persistence.EventRepository;
import com.calendar.service.model.Event;

@RestController
public class EventServiceImpl implements EventService {
	
	private EventRepository eventRepository;	
	
	@Autowired
	public EventServiceImpl(EventRepository eventRepository) {
		this.eventRepository = eventRepository;
	}
	
	private static final Logger LOG = LoggerFactory.getLogger(EventServiceImpl.class);

	@Override
	public Event getEvent(int eventId) {
		
		Event event = eventRepository.findByEventId(eventId).get();

		LOG.debug(String.format("getEvent: found entity : %s", eventId));
		return event;
	}

	@Override
	public Collection<Event> getEvents() {
		List<Event> events =  new ArrayList<Event>();		
		eventRepository.findAll().forEach(events::add);;
		
		LOG.debug(String.format("getEvents: found entitys % s"), events.size());
		return events;
	}

	@Override
	public Event createEvent(Event body) {
		
		try {
            Event event = eventRepository.save(body);

            LOG.debug(String.format("createEvent: entity created for : %s", body));
            
            return event;

        } catch (Exception e) {
        	LOG.debug(String.format("Exception: event for : %s", body));
        	
        }
		
		return null; 
	}

	@Override
	public Event updateEvent(Event body) {
		
		try {
            Event event = eventRepository.save(body);

    		LOG.debug(String.format("updateEvent: entity update for : %s", body));
            
            return event;

        } catch (Exception e) {
        	LOG.debug(String.format("Exception: event for : %s", body));
        	
        }
		
		return null; 
	}

	@Override
	public void deleteEvent(int eventId) {
        LOG.debug("deleteEvent: tries to delete an entity with eventId: {}", eventId);
        eventRepository.findByEventId(eventId).ifPresent(e -> eventRepository.delete(e));
	}
	
	
	
}
