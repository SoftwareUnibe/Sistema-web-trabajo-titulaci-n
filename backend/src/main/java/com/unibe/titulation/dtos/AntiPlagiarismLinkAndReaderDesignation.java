package com.unibe.titulation.dtos;

import java.util.List;

public class AntiPlagiarismLinkAndReaderDesignation {

    public String topicId, topicName;
    public boolean letterSent;
    public List<String> student;
    public List<String> email;

    public AntiPlagiarismLinkAndReaderDesignation(String topicId, String topicName,
                                                  boolean letterSent, List<String> student,
                                                  List<String> email) {
        this.topicId = topicId;
        this.topicName = topicName;
        this.letterSent = letterSent;
        this.student = student;
        this.email = email;
    }

    public AntiPlagiarismLinkAndReaderDesignation() {

    }


    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public boolean isLetterSent() {
        return letterSent;
    }

    public void setLetterSent(boolean letterSent) {
        this.letterSent = letterSent;
    }

    public List<String> getStudent() {
        return student;
    }

    public void setStudent(List<String> student) {
        this.student = student;
    }

    public List<String> getEmail() {
        return email;
    }

    public void setEmail(List<String> email) {
        this.email = email;
    }
}
