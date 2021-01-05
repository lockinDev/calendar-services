package com.calendar.service.model;

import java.util.Objects;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {

	@Id
	private String id;

	@Version
	private Integer version;

	@NotEmpty(message = "Name must not be empty")
	@Size(min = 4, message = "Name must be longer than 3 letters ")
	@Indexed(unique = true)
	private String name;

	@NotEmpty(message = "Password must not be empty")
	@Size(min = 6, message = "Password must be longer than 3 letters ")
	private String password;

	@NotEmpty(message = "Email must not be empty")
	@Indexed(unique = true)
	@Email(message = "Email should be valid")
	private String email;

	public User() {
	}

	public User(
			@NotEmpty(message = "Name must not be empty") @Size(min = 4, message = "Name must be longer than 3 letters ") String name,
			@NotEmpty(message = "Password must not be empty") @Size(min = 6, message = "Password must be longer than 3 letters ") String password,
			@NotEmpty(message = "Email must not be empty") @Email(message = "Email should be valid") String email) {
		super();
		this.name = name;
		this.password = password;
		this.email = email;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof User))
			return false;
		User other = (User) obj;
		return Objects.equals(email, other.email) && Objects.equals(name, other.name);
	}
	
	

}
