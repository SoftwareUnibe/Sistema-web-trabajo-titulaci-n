package com.unibe.titulation.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.GenericGenerator;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;


@Entity
public class ProductAndWorkEvaluation {
    @Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private String id;
    @Column(columnDefinition = "DATE")
    private Date date;
    @NotNull
    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private Topic topic;
    @Column
    @NotEmpty
    @ElementCollection
    private List<Float> workEvaluation;
    @Column
    @ElementCollection
    private List<Float> productEvaluation;
    @Lob
    @Column
    private String commentary;
    @NotNull
    @Column
    private Float finalNote;
    public ProductAndWorkEvaluation() {
    }
    public ProductAndWorkEvaluation(Date date, Topic topic, @NotEmpty List<Float> workEvaluation,
             List<Float> productEvaluation, String commentary, Float finalNote) {
        this.date = date;
        this.topic = topic;
        this.workEvaluation = workEvaluation;
        this.productEvaluation = productEvaluation;
        this.commentary = commentary;
        this.finalNote = finalNote;
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
    public Topic getTopic() {
        return topic;
    }
    public void setTopic(Topic topic) {
        this.topic = topic;
    }
    public List<Float> getWorkEvaluation() {
        return workEvaluation;
    }
    public void setWorkEvaluation(List<Float> workEvaluation) {
        this.workEvaluation = workEvaluation;
    }
    public List<Float> getProductEvaluation() {
        return productEvaluation;
    }
    public void setProductEvaluation(List<Float> productEvaluation) {
        this.productEvaluation = productEvaluation;
    }
    public String getCommentary() {
        return commentary;
    }
    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }
    public Float getFinalNote() {
        return finalNote;
    }
    public void setFinalNote(Float finalNote) {
        this.finalNote = finalNote;
    }
 

    
}
