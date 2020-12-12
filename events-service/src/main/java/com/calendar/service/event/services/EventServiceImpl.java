package com.calendar.service.event.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.calendar.service.model.Event;

public class EventServiceImpl implements EventService {
	
	private static final Logger LOG = LoggerFactory.getLogger(EventServiceImpl.class);

	@Override
	public Event getEvent(int eventId) {
		 LOG.debug(String.format("getEvent: found entity : %s", eventId));
		return new Event();
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
