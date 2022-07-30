package com.unibe.titulation.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

@Entity
public class TopicApprovalNotification {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private TopicStudent topicStudent;
    @NotNull
    private Date date;

    @NotNull
    private LocalDate meetingDate;

    @NotNull @NotBlank
    private String meetingNumber;

    @Lob
    private ArrayList<String>  observations = new ArrayList<>();

    public TopicApprovalNotification(){}

    public TopicApprovalNotification(String id, TopicStudent topicStudent, Date date, LocalDate meetingDate, String meetingNumber, ArrayList<String>  observations) {
        this.id = id;
        this.topicStudent = topicStudent;
        this.date = date;
        this.meetingDate = meetingDate;
        this.meetingNumber = meetingNumber;
        this.observations = observations;

    }

    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TopicStudent getTopicStudent() {
        return topicStudent;
    }

    public void setTopicStudent(TopicStudent topicStudent) {
        this.topicStudent = topicStudent;
    }



    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public LocalDate getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(LocalDate meetingDate) {
        this.meetingDate = meetingDate;
    }

    public String getMeetingNumber() {
        return meetingNumber;
    }

    public void setMeetingNumber(String meetingNumber) {
        this.meetingNumber = meetingNumber;
    }

    public ArrayList<String> getObservations() {
        return observations;
    }

    public void setObservations(ArrayList<String> observations) {
        this.observations = observations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TopicApprovalNotification that = (TopicApprovalNotification) o;
        return Objects.equals(id, that.id) && Objects.equals(topicStudent, that.topicStudent) && Objects.equals(date, that.date) && Objects.equals(meetingDate, that.meetingDate) && Objects.equals(meetingNumber, that.meetingNumber)  && Objects.equals(observations, that.observations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, topicStudent, date, meetingDate, meetingNumber, observations);
    }
}
