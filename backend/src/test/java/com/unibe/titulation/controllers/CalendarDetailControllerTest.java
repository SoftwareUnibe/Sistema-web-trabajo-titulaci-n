package com.unibe.titulation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unibe.titulation.dtos.CalendarDetailList;
import com.unibe.titulation.entities.CalendarDetail;
import com.unibe.titulation.entities.Career;
import com.unibe.titulation.entities.FinalDegreeCalendar;
import com.unibe.titulation.security.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CalendarDetailControllerTest {
    @Autowired
    private MockMvc mockMvc;
    final String BASE_URL = "/calendarDetail/";
    @Autowired
    ObjectMapper mapper;
    @Test
    public void getDetailsByCalendarId() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL+"calendarDetails/6f027191-83cb-4585-8781-9cd1620e7d28")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void existDetailsInCalendar() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL+"existsCalendarDetail/6f027191-83cb-4585-8781-9cd1620e7d28")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void getCalendarDetailByCareerId() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL+"a105316d-85cf-45a7-a020-fbc388f0fd5f")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
   @Test
    public void createCalendarDetail() throws Exception{
        Career career = new Career();
        career.setId("b94dfe70-c04f-42dd-b90f-822660076ffa");
        User userTest = new User();
        userTest.setId("f59b6ada-f26f-4562-abc8-f4e2c9c952fc");
        FinalDegreeCalendar finalDegreeCalendar = new FinalDegreeCalendar();
        finalDegreeCalendar.setId("640768e4-4f85-4dce-8531-2c409560240d");
        CalendarDetail calendarDetail = new
                CalendarDetail("",career,userTest,userTest,userTest,userTest,new Date(),
                new Date(),finalDegreeCalendar,"Test");
       CalendarDetailList calendarDetailList = new CalendarDetailList();
       List<CalendarDetail> calendarDetails = new ArrayList<>();
       calendarDetails.add(calendarDetail);
       calendarDetailList.setCalendarDetailList(calendarDetails);
        mockMvc.perform(MockMvcRequestBuilders
                        .post(BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(calendarDetailList)))
                .andExpect(status().isOk());
    }
    @Test
    public void updateCalendarDetail() throws Exception{
        Career career = new Career();
        career.setId("b94dfe70-c04f-42dd-b90f-822660076ffa");
        User userTest = new User();
        userTest.setId("f59b6ada-f26f-4562-abc8-f4e2c9c952fc");
        FinalDegreeCalendar finalDegreeCalendar = new FinalDegreeCalendar();
        finalDegreeCalendar.setId("640768e4-4f85-4dce-8531-2c409560240d");
        CalendarDetail calendarDetail = new
                CalendarDetail("0f8905e5-d5e9-404e-abc0-653358dbb31f",career,userTest,
                userTest,userTest,userTest,new Date(),
                new Date(),finalDegreeCalendar,"Test");
        mockMvc.perform(MockMvcRequestBuilders
                        .put(BASE_URL+"calendar/0f8905e5-d5e9-404e-abc0-653358dbb31f")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(calendarDetail)))
                .andExpect(status().isOk());
    }
}
