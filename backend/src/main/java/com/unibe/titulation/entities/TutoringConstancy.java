package com.unibe.titulation.entities;

import java.util.Date;

import javax.persistence.*;

import com.unibe.titulation.security.entity.User;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class TutoringConstancy {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(columnDefinition = "DATE")
    private Date generationDate;
    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private Topic topic;
    @ManyToOne(optional = false, cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private User tutor;

    public TutoringConstancy() {
    }

    public TutoringConstancy(String id, Date generationDate, Topic topic, User tutor) {
        this.id = id;
        this.generationDate = generationDate;
        this.topic = topic;
        this.tutor = tutor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getGenerationDate() {
        return generationDate;
    }

    public void setGenerationDate(Date generationDate) {
        this.generationDate = generationDate;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public User getTutor() {
        return tutor;
    }

    public void setTutor(User tutor) {
        this.tutor = tutor;
    }

}
