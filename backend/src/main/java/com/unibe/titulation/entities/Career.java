package com.unibe.titulation.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
public class Career {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private String id;
	@NotNull @NotBlank
	@Column(unique = true)
	private String name, degree;

	@NotNull
	private boolean hasProduct;

	@ManyToOne(optional = false, cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
	private Faculty faculty;

	public Career() {
	}

	public Career(String id, String name, Faculty faculty, String degree, boolean hasProduct) {
		super();
		this.id = id;
		this.name = name;
		this.faculty = faculty;
		this.degree = degree;
		this.hasProduct=hasProduct;
	}

	public boolean isHasProduct() {
		return hasProduct;
	}

	public void setHasProduct(boolean hasProduct) {
		this.hasProduct = hasProduct;
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

	public Faculty getFaculty() {
		return faculty;
	}

	public void setFaculty(Faculty faculty) {
		this.faculty = faculty;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if(obj == null || getClass() !=  obj.getClass()) return false;
		Career career = (Career) obj;
		return Objects.equals(id, career.getId())
				&& Objects.equals(name, career.getName())
				&& Objects.equals(degree, career.getDegree());
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, degree);
	}
}
