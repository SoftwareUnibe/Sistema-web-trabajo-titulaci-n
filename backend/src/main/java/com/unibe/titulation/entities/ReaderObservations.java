package com.unibe.titulation.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class ReaderObservations {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @NotEmpty
    @Column
    @ElementCollection
    private List<String> mainObservation;
    @NotEmpty
    @Column
    @ElementCollection
    private List<String> descObservation;
    @Column(columnDefinition = "DATE")
    private Date date;
    @NotNull
    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private Topic topic;

    public ReaderObservations() {
    }

    public ReaderObservations(String id, @NotEmpty List<String> mainObservation, @NotEmpty List<String> descObservation,
            Date date, @NotNull Topic topic) {
        this.id = id;
        this.mainObservation = mainObservation;
        this.descObservation = descObservation;
        this.date = date;
        this.topic = topic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getMainObservation() {
        return mainObservation;
    }

    public void setMainObservation(List<String> mainObservation) {
        this.mainObservation = mainObservation;
    }

    public List<String> getDescObservation() {
        return descObservation;
    }

    public void setDescObservation(List<String> descObservation) {
        this.descObservation = descObservation;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

}
