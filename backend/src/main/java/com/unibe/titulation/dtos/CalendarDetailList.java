package com.unibe.titulation.dtos;

import com.unibe.titulation.entities.CalendarDetail;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

public class CalendarDetailList {

    @Valid
    @NotEmpty
    public List<CalendarDetail> calendarDetailList;

    public CalendarDetailList() {
    }

    public CalendarDetailList(List<CalendarDetail> calendarDetailList) {
        this.calendarDetailList = calendarDetailList;
    }

    public List<CalendarDetail> getCalendarDetailList() {
        return calendarDetailList;
    }

    public void setCalendarDetailList(List<CalendarDetail> calendarDetailList) {
        this.calendarDetailList = calendarDetailList;
    }
}
