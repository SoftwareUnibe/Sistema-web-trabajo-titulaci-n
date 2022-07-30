package com.unibe.titulation.entities;

import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.unibe.titulation.security.entity.User;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Reader {
    
    @Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private String id;

    @NotNull
    @Column(columnDefinition = "DATE")
    private Date date;

    @NotNull
    @Column(columnDefinition = "DATE")
    private Date maxDate;

    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private User reader;

    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private Topic topic;

    @NotNull
    @Column
    private String state;

    public Reader(){
    }

    public Reader(@NotNull String id, @NotNull Date date, @NotNull Date maxDate, @NotNull User reader, @NotNull Topic topic,
                  @NotNull String state) {
        this.id = id;
        this.date = date;
        this.maxDate = maxDate;
        this.reader = reader;
        this.topic = topic;
        this.state = state;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public Date getMaxDate() {
        return maxDate;
    }
    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }
    public User getReader() {
        return reader;
    }
    public void setReader(User reader) {
        this.reader = reader;
    }
    public Topic getTopic() {
        return topic;
    }
    public void setTopic(Topic topic) {
        this.topic = topic;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
}
