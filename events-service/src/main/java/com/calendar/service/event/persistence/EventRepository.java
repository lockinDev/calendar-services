package com.calendar.service.event.persistence;

import org.springframework.data.repository.CrudRepository;

import com.calendar.service.model.Event;

import java.util.Optional;


public interface EventRepository extends CrudRepository<Event, Long> {
	Optional<Event> findByEventId(Long eventId);
}
