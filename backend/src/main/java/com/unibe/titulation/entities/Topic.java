package com.unibe.titulation.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
public class Topic {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private String id;
	@NotBlank @NotNull
	@Column(unique = true)
	private String name;

	@Column(nullable = false)
	private String articulation, topicStatus = "Disponible";

	@ManyToOne(optional = false, cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
	private Career career;

	@Lob
	@NotBlank @NotNull
	private String description;

	@Column(nullable = false)
	private boolean twoStudents;

	@Column
	private boolean hasReader = false;

	public Topic() {
	}

	public Topic(String id, String name, String articulation, Career career, String description, String topicStatus,
				 boolean twoStudents, boolean hasReader) {
		this.id = id;
		this.name = name;
		this.articulation = articulation;
		this.career = career;
		this.description = description;
		this.topicStatus = topicStatus;
		this.twoStudents = twoStudents;
		this.hasReader = hasReader;
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

	public String getArticulation() {
		return articulation;
	}

	public void setArticulation(String articulation) {
		this.articulation = articulation;
	}

	public String getTopicStatus() {
		return topicStatus;
	}

	public boolean isTwoStudents() {
		return twoStudents;
	}

	public void setTwoStudents(boolean twoStudents) {
		this.twoStudents = twoStudents;
	}

	public void setTopicStatus(String topicStatus) {
		this.topicStatus = topicStatus;
	}

	public Career getCareer() {
		return career;
	}

	public void setCareer(Career career) {
		this.career = career;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isHasReader() {
		return hasReader;
	}

	public void setHasReader(boolean hasReader) {
		this.hasReader = hasReader;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, description);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Topic topic = (Topic) obj;
		return Objects.equals(id, topic.getId()) && Objects.equals(name, topic.getName())
				&& Objects.equals(description, topic.getDescription());
	}
}
