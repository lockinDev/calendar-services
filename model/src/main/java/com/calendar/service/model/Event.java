package com.calendar.service.model;

import java.util.Date;
import java.util.Objects;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

@Document(collection = "events")
public class Event {

	@Id
	private String id;

	@Version
	private Integer version;
	
	@Indexed(unique = true)
	Long eventId;
	
	@DBRef
	private User user;
	
	@NotEmpty(message = "Title must not be empty")
	private String title;
	
	private String notes;

	@NotNull(message = "start date must not be empty")
	@DateTimeFormat	
	private Date start;
	
	@NotNull(message = "end date must not be empty")
	@DateTimeFormat	
	private Date end;
	
	public Event() {
	}

	public Event(Long eventId, User user, @NotEmpty(message = "Title must not be empty") String title, String notes,
			@NotNull(message = "start date must not be empty") Date start,
			@NotNull(message = "end date must not be empty") Date end) {
		this.eventId = eventId;
		this.user = user;
		this.title = title;
		this.notes = notes;
		this.start = start;
		this.end = end;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}		

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}		
				
	
	@Override
	public int hashCode() {
		return Objects.hash(eventId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Event other = (Event) obj;
		return Objects.equals(eventId, other.eventId);
	}

	@Override
	public String toString() {
		return String.format("Event [id=%s, user=%s, title=%s, notes=%s, start=%s, end=%s]", id, user, title, notes,
				start, end);
	}
	
	
	

}