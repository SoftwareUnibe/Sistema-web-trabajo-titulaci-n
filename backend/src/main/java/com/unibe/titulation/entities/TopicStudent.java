package com.unibe.titulation.entities;
import com.unibe.titulation.security.entity.User;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class TopicStudent {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private Topic topic;
    @NotNull
    @Column( columnDefinition = "DATE")
    private Date assignedDate;
    @NotNull
    @Column
    private String topicEvaluation = "No presentado", paymentDenunciation = "No pagado";
    @NotNull
    @OneToOne(optional = false, cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private User student;

    @Column
    private boolean thesisSent;

    @Column
    private boolean antiPlagiarismLetterSent;

    public TopicStudent() {
    }

    public TopicStudent(String id, Topic topic, Date assignedDate, String topicEvaluation,
                        String paymentDenunciation, User student,
                        boolean thesisSent, boolean antiPlagiarismLetterSent) {
        this.id = id;
        this.topic = topic;
        this.assignedDate = assignedDate;
        this.topicEvaluation = topicEvaluation;
        this.paymentDenunciation = paymentDenunciation;
        this.student = student;
        this.thesisSent = thesisSent;
        this.antiPlagiarismLetterSent = antiPlagiarismLetterSent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
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

    public String getPaymentDenunciation() {
        return paymentDenunciation;
    }

    public void setPaymentDenunciation(String paymentDenunciation) {
        this.paymentDenunciation = paymentDenunciation;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public boolean isThesisSent() {
        return thesisSent;
    }

    public void setThesisSent(boolean thesisSent) {
        this.thesisSent = thesisSent;
    }

    public boolean isAntiPlagiarismLetterSent() {
        return antiPlagiarismLetterSent;
    }

    public void setAntiPlagiarismLetterSent(boolean antiPlagiarismLetterSent) {
        this.antiPlagiarismLetterSent = antiPlagiarismLetterSent;
    }
}
