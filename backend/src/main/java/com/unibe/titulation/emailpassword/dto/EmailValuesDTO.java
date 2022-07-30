package com.unibe.titulation.emailpassword.dto;

public class EmailValuesDTO {
    private String mailTo;
    private String tokenPassword;

    public EmailValuesDTO() {
    }

    public EmailValuesDTO(String mailTo, String tokenPassword) {
        this.mailTo = mailTo;
        this.tokenPassword = tokenPassword;
    }



    public String getMailTo() {
        return mailTo;
    }

    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }

    public String getTokenPassword() {
        return tokenPassword;
    }

    public void setTokenPassword(String tokenPassword) {
        this.tokenPassword = tokenPassword;
    }
}
