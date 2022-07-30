package com.unibe.titulation.entities;

import com.unibe.titulation.security.entity.User;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class DesignationTT {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @NotNull
    @Column(columnDefinition = "DATE")
    private Date date;

    @NotNull
    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private TopicStudent topicStudent;

    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private User teacher;

    public DesignationTT() {
    }

    public DesignationTT(String id, Date date, TopicStudent topicStudent, User teacher) {
        this.id = id;
        this.date = date;
        this.topicStudent = topicStudent;
        this.teacher = teacher;
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

    public TopicStudent getTopicStudent() {
        return topicStudent;
    }

    public void setTopicStudent(TopicStudent topicStudent) {
        this.topicStudent = topicStudent;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }
}