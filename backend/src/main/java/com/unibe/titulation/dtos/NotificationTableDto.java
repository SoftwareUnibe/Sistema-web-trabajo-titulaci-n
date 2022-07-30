package com.unibe.titulation.dtos;

import java.util.ArrayList;

import com.unibe.titulation.entities.TopicStudent;


public class NotificationTableDto {
    private String id, ci, student, career, topic, topicStatus, topicEvaluation;
    private ArrayList<String> observations;
    private TopicStudent topicStudent;

    public NotificationTableDto() {
    }

    public NotificationTableDto(String id, String ci, String student, String career, String topic, String topicStatus,
                                String topicEvaluation, ArrayList<String> observations, TopicStudent topicStudent ) {
        this.id = id;
        this.ci = ci;
        this.student = student;
        this.career = career;
        this.topic = topic;
        this.topicStatus = topicStatus;
        this.topicEvaluation = topicEvaluation;
        this.observations = observations;
        this.topicStudent = topicStudent;
    }

    public TopicStudent getTopicStudent() {
        return topicStudent;
    }

    public void setTopicStudent(TopicStudent topicStudent) {
        this.topicStudent = topicStudent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTopicStatus() {
        return topicStatus;
    }

    public void setTopicStatus(String topicStatus) {
        this.topicStatus = topicStatus;
    }

    public String getTopicEvaluation() {
        return topicEvaluation;
    }

    public void setTopicEvaluation(String topicEvaluation) {
        this.topicEvaluation = topicEvaluation;
    }

    public ArrayList<String> getObservations() {
        return observations;
    }

    public void setObservations(ArrayList<String> observations) {
        this.observations = observations;
    }
}
