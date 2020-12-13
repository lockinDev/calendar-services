package com.calendar.service.event.services;

import java.util.ArrayList;
import java.util.Collection;

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
		 LOG.debug(String.format("getEvent: found entity : %s", eventId));
		return new Event();
	}

	@Override
	public Collection<Event> getEvents() {
		 LOG.debug(String.format("getEvents: found entitys "));
		return new ArrayList<Event>();
	}

	@Override
	public Event createEvent(Event body) {
        LOG.debug(String.format("createEvent: entity created for : %s", body));
        return new Event();
	}

	@Override
	public Event updateEvent(Event body) {
		LOG.debug(String.format("updateEvent: entity update for : %s", body));
        return new Event();
	}

	@Override
	public void deleteEvent(int eventId) {
        LOG.debug("deleteEvent: tries to delete an entity with eventId: {}", eventId);
	}
	
	
	
}
