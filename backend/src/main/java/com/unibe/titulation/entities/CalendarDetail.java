package com.unibe.titulation.entities;

import com.unibe.titulation.security.entity.User;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class CalendarDetail {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private Career career;

    @NotNull
    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private User student;

    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private User tribunalBoss;

    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private User tutor;

    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private User reader;

    @NotNull
    @Column(columnDefinition = "DATE")
    private Date date;

    @NotNull
    @Column(columnDefinition = "TIME")
    private Date hour;

    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private FinalDegreeCalendar finalDegreeCalendar;

    @NotNull
    @Column
    private String secretary;

    public CalendarDetail() {
    }

    public CalendarDetail(String id, Career career, User student, User tribunalBoss, User tutor, User reader, Date date, Date hour, FinalDegreeCalendar finalDegreeCalendar, String secretary) {
        this.id = id;
        this.career = career;
        this.student = student;
        this.tribunalBoss = tribunalBoss;
        this.tutor = tutor;
        this.reader = reader;
        this.date = date;
        this.hour = hour;
        this.finalDegreeCalendar = finalDegreeCalendar;
        this.secretary = secretary;
    }

    public FinalDegreeCalendar getFinalDegreeCalendar() {
        return finalDegreeCalendar;
    }

    public void setFinalDegreeCalendar(FinalDegreeCalendar finalDegreeCalendar) {
        this.finalDegreeCalendar = finalDegreeCalendar;
    }

    public String getSecretary() {
        return secretary;
    }

    public void setSecretary(String secretary) {
        this.secretary = secretary;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Career getCareer() {
        return career;
    }

    public void setCareer(Career career) {
        this.career = career;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public User getTribunalBoss() {
        return tribunalBoss;
    }

    public void setTribunalBoss(User tribunalBoss) {
        this.tribunalBoss = tribunalBoss;
    }

    public User getTutor() {
        return tutor;
    }

    public void setTutor(User tutor) {
        this.tutor = tutor;
    }

    public User getReader() {
        return reader;
    }

    public void setReader(User reader) {
        this.reader = reader;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getHour() {
        return hour;
    }

    public void setHour(Date hour) {
        this.hour = hour;
    }
}
