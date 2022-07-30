package com.unibe.titulation.controllers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unibe.titulation.entities.ProductAndWorkEvaluation;
import com.unibe.titulation.entities.Topic;
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

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProductAndWorkEvaluationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    final String BASE_URL = "/evaluation/";
    @Autowired
    ObjectMapper mapper;
    @Test
    public void getEvaluationByTopicId() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL+"topic/835f1c4d-f062-4568-9b1f-869142a70150")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void getEvaluationInPdf() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL+"pdf/835f1c4d-f062-4568-9b1f-869142a70150")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void saveEvaluation() throws Exception {
        Topic topic = new Topic();
        topic.setId("37e823b5-749e-4693-a934-86dc64b2eed9");
        User reader= new User();
        reader.setId("5e16ccd8-b0a6-42bb-b23e-0c82c5fd33eb");
        List<Float> workNotes= Arrays.asList(10F,10F,10F,10F,10F,10F,10F);
        ProductAndWorkEvaluation evaluation = new ProductAndWorkEvaluation(new Date(),topic,workNotes,
                null,"test",10F);
        mockMvc.perform(MockMvcRequestBuilders
                        .post(BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(evaluation)))
                .andExpect(status().isOk());
    }
}
