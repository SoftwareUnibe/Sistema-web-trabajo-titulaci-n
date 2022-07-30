package com.unibe.titulation.dtos;

import com.unibe.titulation.entities.TopicStudent;
import com.unibe.titulation.security.entity.User;
import java.util.Date;
import java.util.List;

public class DesignationTT_TableDto {

    private Date date;
    private List<TopicStudent> topicStudent;
    private User teacher;

    public DesignationTT_TableDto() {
    }

    public DesignationTT_TableDto(Date date, List<TopicStudent> topicStudent, User teacher) {
        this.date = date;
        this.topicStudent = topicStudent;
        this.teacher = teacher;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<TopicStudent> getTopicStudent() {
        return topicStudent;
    }

    public void setTopicStudent(List<TopicStudent> topicStudent) {
        this.topicStudent = topicStudent;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }
}
