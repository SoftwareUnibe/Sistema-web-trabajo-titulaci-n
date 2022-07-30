package com.unibe.titulation.entities;

import java.util.ArrayList;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class ReaderAccordance {
    @Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private String id;
    @NotNull
    @Column(columnDefinition = "DATE")
    private Date date;
    @NotNull
    private boolean accordance;
    @Lob
    private ArrayList<String> observations = new ArrayList<>();
    @NotNull
    @Column(columnDefinition = "DATE")
    private Date maxDate;
    @NotNull
    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private Topic topic;
    public ReaderAccordance() {
    }
    public ReaderAccordance(String id, @NotNull Date date, @NotNull boolean accordance,
            ArrayList<String> observations, @NotNull Date maxDate, @NotNull Topic topic) {
        this.id = id;
        this.date = date;
        this.accordance = accordance;
        this.observations = observations;
        this.maxDate = maxDate;
        this.topic = topic;
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
    public boolean isAccordance() {
        return accordance;
    }
    public void setAccordance(boolean accordance) {
        this.accordance = accordance;
    }
    public ArrayList<String> getObservations() {
        return observations;
    }
    public void setObservations(ArrayList<String> observations) {
        this.observations = observations;
    }
    public Date getMaxDate() {
        return maxDate;
    }
    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }
    public Topic getTopic() {
        return topic;
    }
    public void setTopic(Topic topic) {
        this.topic = topic;
    }
    
   
    


}
