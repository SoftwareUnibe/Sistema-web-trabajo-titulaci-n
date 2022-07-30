package com.unibe.titulation.controllers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unibe.titulation.entities.ReaderAccordance;
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

import java.util.ArrayList;
import java.util.Date;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ReaderAccordanceControllerTest {
    @Autowired
    private MockMvc mockMvc;
    final String BASE_URL = "/readerAccordance/";
    @Autowired
    ObjectMapper mapper;
    @Test
    public void getAccordanceByTopic() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL+"topic/d6a08642-b774-4d6b-aace-f420a30b87ab")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void getAccordanceInPdf() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL+"pdf/d6a08642-b774-4d6b-aace-f420a30b87ab")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void getProcessResultInPdf() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL+"readerProcessResultPDF/d6a08642-b774-4d6b-aace-f420a30b87ab")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void saveAccordance() throws Exception {
        Topic topic = new Topic();
        topic.setId("37e823b5-749e-4693-a934-86dc64b2eed9");
        ArrayList<String> observations = new ArrayList<>();
        observations.add("Test");
        ReaderAccordance readerAccordance = new ReaderAccordance("",new Date(),true, observations,
                new Date(),topic);
        mockMvc.perform(MockMvcRequestBuilders
                        .post(BASE_URL+"create")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(readerAccordance)))
                .andExpect(status().isOk());
    }
}
