package com.unibe.titulation.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@Entity
public class TopicProposal {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @OneToOne(optional = false, cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private TopicStudent topicStudent;

    @Lob
    @NotNull
    @NotBlank
    private String topicDescription, objectiveGeneral, studyJustification, scope ;

    @Lob
    @Column(nullable = false)
    private ArrayList<String> objectivesSpecific = new ArrayList<String>();
    @Lob
    private ArrayList<String> bibliographicReferences = new ArrayList<String>();

    public TopicProposal() {
    }

    public TopicProposal(String id, TopicStudent topicStudent, String topicDescription, String objectiveGeneral,
            String studyJustification, String scope, ArrayList<String> objectivesSpecific,
            ArrayList<String> bibliographicReferences) {
        this.id = id;
        this.topicStudent = topicStudent;
        this.topicDescription = topicDescription;
        this.objectiveGeneral = objectiveGeneral;
        this.studyJustification = studyJustification;
        this.scope = scope;
        this.objectivesSpecific = objectivesSpecific;
        this.bibliographicReferences = bibliographicReferences;
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

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public ArrayList<String> getObjectivesSpecific() {
        return objectivesSpecific;
    }

    public void setObjectivesSpecific(ArrayList<String> objectivesSpecific) {
        this.objectivesSpecific = objectivesSpecific;
    }

    public ArrayList<String> getBibliographicReferences() {
        return bibliographicReferences;
    }

    public void setBibliographicReferences(ArrayList<String> bibliographicReferences) {
        this.bibliographicReferences = bibliographicReferences;
    }

   
}