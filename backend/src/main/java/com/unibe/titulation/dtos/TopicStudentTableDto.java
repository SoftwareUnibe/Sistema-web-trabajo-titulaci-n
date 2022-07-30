package com.unibe.titulation.dtos;

import com.unibe.titulation.entities.Topic;


import java.util.Date;

public class TopicStudentTableDto {
    private String id, name, articulation, paymentStatus, ci, career, topicEvaluation;
    private Date assignedDate;
    private Topic topic;

    public TopicStudentTableDto() {
    }

    public TopicStudentTableDto(String id, String name, String articulation, String paymentStatus, String ci, String career,
                                Date assignedDate, String topicEvaluation, Topic topic) {
        this.id = id;
        this.name = name;
        this.articulation = articulation;
        this.paymentStatus = paymentStatus;
        this.ci = ci;
        this.career = career;
        this.assignedDate = assignedDate;
        this.topicEvaluation = topicEvaluation;
        this.topic = topic;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
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

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public Date getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(Date assignedDate) {
        this.assignedDate = assignedDate;
    }

    public String getTopicEvaluation() {
        return topicEvaluation;
    }

    public void setTopicEvaluation(String topicEvaluation) {
        this.topicEvaluation = topicEvaluation;
    }
}
