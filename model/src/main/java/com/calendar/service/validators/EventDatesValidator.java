package com.calendar.service.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.calendar.service.model.Event;

public class EventDatesValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return Event.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Event event = (Event) target;
		
		if(event.getStart().after(event.getEnd())) {
			errors.rejectValue("start", "start must be before to end date");
		}
		
	}

}
