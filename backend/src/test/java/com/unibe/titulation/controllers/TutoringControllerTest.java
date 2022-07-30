package com.unibe.titulation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unibe.titulation.entities.Topic;
import com.unibe.titulation.entities.TutoringConstancy;
import com.unibe.titulation.entities.TutoringHours;
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

import java.util.Date;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TutoringControllerTest {
    @Autowired
    private MockMvc mockMvc;
    final String BASE_URL = "/tutoring/";
    @Autowired
    ObjectMapper mapper;

    @Test
    public void getTutoringConstancyByTopicId() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL+"37e823b5-749e-4693-a934-86dc64b2eed9")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void existByTopicId() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL+"tutoringConstancy/37e823b5-749e-4693-a934-86dc64b2eed9")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$", is(true))));
    }
    @Test
    public void generateConstancyInPdf() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL+"pdf/c0742c92-90e7-4064-8ac4-e7cdf30b8c98")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void registerHour() throws Exception {
        Topic topic = new Topic();
        topic.setId("d88b86ea-7fab-46ca-9cd3-fa911973782b");
        TutoringHours tutoringHour=new TutoringHours("",topic,1,"test",new Date(),
                15,"ABRIL 2022-AGOSTO 2022");
        mockMvc.perform(MockMvcRequestBuilders
                        .post(BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(tutoringHour)))
                .andExpect(status().isOk());
    }
    @Test
    public void updateHour() throws Exception {
        Topic topic = new Topic();
        topic.setId("c0742c92-90e7-4064-8ac4-e7cdf30b8c98");
        TutoringHours tutoringHour=new TutoringHours("ff7c980a-a534-4976-8466-3cff6428c188",topic,
                1,"test",new Date(),
                15,"ABRIL 2022-AGOSTO 2022");
        mockMvc.perform(MockMvcRequestBuilders
                        .put(BASE_URL+"ff7c980a-a534-4976-8466-3cff6428c188")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(tutoringHour)))
                .andExpect(status().isOk());
    }
    @Test
    public void generateConstancy() throws Exception {
        Topic topic = new Topic();
        topic.setId("37e823b5-749e-4693-a934-86dc64b2eed9");
        User tutor= new User();
        tutor.setId("5e16ccd8-b0a6-42bb-b23e-0c82c5fd33eb");
        TutoringConstancy tutoringConstancy = new TutoringConstancy("",new Date(),topic,tutor);
        mockMvc.perform(MockMvcRequestBuilders
                        .post(BASE_URL+"createConstancy")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(tutoringConstancy)))
                .andExpect(status().isOk());
    }
}
