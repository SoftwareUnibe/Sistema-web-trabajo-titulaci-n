package com.unibe.titulation.dtos;

public class PaymentTableDto {
    private String student,ci,career,status;

    public PaymentTableDto() {
    }

    public PaymentTableDto(String student, String ci, String career, String status) {
        this.student = student;
        this.ci = ci;
        this.career = career;
        this.status = status;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
