package com.unibe.titulation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unibe.titulation.entities.Career;
import com.unibe.titulation.entities.FinalDegreeCalendar;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FinalDegreeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    final String BASE_URL = "/finalDegreeCalendar/";
    @Autowired
    ObjectMapper mapper;

    @Test
    public void getAllCalendars() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void getCalendarById() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL+"6f027191-83cb-4585-8781-9cd1620e7d28")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void getCalendarByCareerId() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL+"list/a105316d-85cf-45a7-a020-fbc388f0fd5f")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void createCalendar() throws Exception{
        Career career = new Career();
        career.setId("b94dfe70-c04f-42dd-b90f-822660076ffa");
        FinalDegreeCalendar finalDegreeCalendar = new FinalDegreeCalendar(career,"Test period");
        mockMvc.perform(MockMvcRequestBuilders
                        .post(BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(finalDegreeCalendar)))
                .andExpect(status().isOk());
    }
}
