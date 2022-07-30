package com.unibe.titulation.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@Entity()
public class TopicDenunciation {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @OneToOne(optional = false, cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private TopicStudent topicStudent;

    @NotNull
    @NotBlank
    private String semesterLevel, projectType, investigationModality, investigationLine, ciudad, articulationTopic;
    @NotNull
    @Column(columnDefinition = "DATE")
    private Date date;

    public TopicDenunciation() {
    }

    public TopicDenunciation(String id, TopicStudent topicStudent, String semesterLevel, String projectType,
                             String investigationModality, String investigationLine, Date date, String ciudad,
                             String articulationTopic) {
        this.id = id;
        this.topicStudent = topicStudent;
        this.semesterLevel = semesterLevel;
        this.projectType = projectType;
        this.investigationModality = investigationModality;
        this.investigationLine = investigationLine;
        this.date = date;
        this.ciudad = ciudad;
        this.articulationTopic = articulationTopic;
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

    public String getSemesterLevel() {
        return semesterLevel;
    }

    public void setSemesterLevel(String semesterLevel) {
        this.semesterLevel = semesterLevel;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getInvestigationModality() {
        return investigationModality;
    }

    public void setInvestigationModality(String investigationModality) {
        this.investigationModality = investigationModality;
    }

    public String getInvestigationLine() {
        return investigationLine;
    }

    public void setInvestigationLine(String investigationLine) {
        this.investigationLine = investigationLine;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getArticulationTopic() {
        return articulationTopic;
    }

    public void setArticulationTopic(String articulationTopic) {
        this.articulationTopic = articulationTopic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        TopicDenunciation that = (TopicDenunciation) o;
        return Objects.equals(id, that.id) && Objects.equals(topicStudent, that.topicStudent)
                && semesterLevel == that.semesterLevel && projectType == that.projectType
                && investigationModality == that.investigationModality && investigationLine == that.investigationLine
                && Objects.equals(date, that.date) && Objects.equals(ciudad, that.ciudad)
                && Objects.equals(articulationTopic, that.articulationTopic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, topicStudent, semesterLevel, projectType, investigationModality, investigationLine,
                date, ciudad, articulationTopic);
    }
}