package com.unibe.titulation.dtos;

import com.unibe.titulation.entities.TopicStudent;

import java.util.ArrayList;
import java.util.List;

public class TopicProposalTableDto {

    private String id;
    private List<TopicStudent> topicStudent;
    private String topicDescription, objectiveGeneral, studyJustification;
    private ArrayList<String> objectivesSpecific = new ArrayList<String>();


    public TopicProposalTableDto(String id, List<TopicStudent> topicStudent, String topicDescription,
                                 String objectiveGeneral, String studyJustification, ArrayList<String> objectivesSpecific) {
        this.id = id;
        this.topicStudent = topicStudent;
        this.topicDescription = topicDescription;
        this.objectiveGeneral = objectiveGeneral;
        this.studyJustification = studyJustification;
        this.objectivesSpecific = objectivesSpecific;
    }

    public TopicProposalTableDto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<TopicStudent> getTopicStudent() {
        return topicStudent;
    }

    public void setTopicStudent(List<TopicStudent> topicStudent) {
        this.topicStudent = topicStudent;
    }

    public String getTopicDescription() {
        return topicDescription;
    }

    public void setTopicDescription(String topicDescription) {
        this.topicDescription = topicDescription;
    }

    public String getObjectiveGeneral() {
        return objectiveGeneral;
    }

    public void setObjectiveGeneral(String objectiveGeneral) {
        this.objectiveGeneral = objectiveGeneral;
    }

    public String getStudyJustification() {
        return studyJustification;
    }

    public void setStudyJustification(String studyJustification) {
        this.studyJustification = studyJustification;
    }

    public ArrayList<String> getObjectivesSpecific() {
        return objectivesSpecific;
    }

    public void setObjectivesSpecific(ArrayList<String> objectivesSpecific) {
        this.objectivesSpecific = objectivesSpecific;
    }

}