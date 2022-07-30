package com.unibe.titulation.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
public class Faculty {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private String id;
	@NotNull
	@NotBlank
	@Column(unique = true)
	private String name;
	
	public Faculty() {
	}

	public Faculty(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if(obj == null || getClass() !=  obj.getClass()) return false;
		Faculty faculty = (Faculty) obj;
		return Objects.equals(id, faculty.getId())
				&& Objects.equals(name, faculty.getName());
	}
	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}

}
