package com.unibe.titulation.controllers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unibe.titulation.entities.Reader;
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

import java.util.Date;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ReaderControllerTest {
    @Autowired
    private MockMvc mockMvc;
    final String BASE_URL = "/reader/";
    @Autowired
    ObjectMapper mapper;

    @Test
    public void getReaderById() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL+"2433b632-b72d-4563-b7cb-a227d7a92ced")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void getReaderDesignationsByReaderCi() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL+"listByReader/2433b632-b72d-4563-b7cb-a227d7a92ced")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void getReaderByStudentIdAndTopicId() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL+"studentReaders/f59b6ada-f26f-4562-abc8-f4e2c9c952fc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void getReaderDetailById() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL+"reader/2433b632-b72d-4563-b7cb-a227d7a92ced")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void checkStatusToCreateCalendar() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL+"checkStatus/a105316d-85cf-45a7-a020-fbc388f0fd5f")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void assignReader() throws Exception {
        Topic topic = new Topic();
        topic.setId("37e823b5-749e-4693-a934-86dc64b2eed9");
        User reader= new User();
        reader.setId("5e16ccd8-b0a6-42bb-b23e-0c82c5fd33eb");
        Reader readerToCreate= new Reader("",new Date(),new Date(),reader,topic,"Asignado");
        mockMvc.perform(MockMvcRequestBuilders
                        .post(BASE_URL+"createReader")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(readerToCreate)))
                .andExpect(status().isOk());
    }
    @Test
    public void getDesignationInPdf() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL+"pdf/2433b632-b72d-4563-b7cb-a227d7a92ced")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
