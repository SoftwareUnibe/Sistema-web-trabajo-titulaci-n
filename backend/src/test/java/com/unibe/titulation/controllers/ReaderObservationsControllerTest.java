package com.unibe.titulation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unibe.titulation.entities.ReaderObservations;
import com.unibe.titulation.entities.Topic;
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
public class ReaderObservationsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    final String BASE_URL = "/readerObservations/";
    @Autowired
    ObjectMapper mapper;
    @Test
    public void getObservationsByTopic() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL+"topic/d6a08642-b774-4d6b-aace-f420a30b87ab")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void getObservationsInPdf() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL+"pdf/d6a08642-b774-4d6b-aace-f420a30b87ab")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void saveObservation() throws Exception {
        Topic topic = new Topic();
        topic.setId("37e823b5-749e-4693-a934-86dc64b2eed9");
        List<String> main = Arrays.asList("Test");
        List<String> desc = Arrays.asList("Test");
        ReaderObservations readerObservations = new ReaderObservations("",main,desc,new Date(),
                topic);
        mockMvc.perform(MockMvcRequestBuilders
                        .post(BASE_URL+"create")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(readerObservations)))
                .andExpect(status().isOk());
    }
}
