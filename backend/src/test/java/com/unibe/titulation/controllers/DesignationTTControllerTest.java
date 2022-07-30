package com.unibe.titulation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unibe.titulation.dtos.DesignationsTT_Table;
import com.unibe.titulation.entities.*;
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
import static org.hamcrest.Matchers.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DesignationTTControllerTest {
    @Autowired
    private MockMvc mockMvc;
    final String BASE_URL = "/designationTT/";
    @Autowired
    ObjectMapper mapper;

    Faculty faculty = new Faculty("2d71c12f-8a25-408f-9a68-1b97b1fa992c", "Salud y Bienestar");
    Career career = new Career("b94dfe70-c04f-42dd-b90f-822660076ffa", "Enfermeria", faculty, "Licenciado/a en enfermería ",
            false);
    User completeStudent = new User("Estudiante", "1259874651", "enfersdas@gmail.com", "sdssd", "Enfermeria",
            "asdas", "1259874651", "$2a$10$1EjNPgBTET2eIeBIzUchu.aCc7DxayuZdbbHq1IjibTrmoQucvgua", true, career,
            null);
    User teacherOfCompleteStudent = new User("Profe 2", "1658479875", "saulm54512@gmail.com", "PR2",
            "Enfermeria", "docente", "1259874651",
            "$2a$10$VHY6Z15.ZyHQXAbiqwUpgeG0L8nUXcsJGnAy5j/kfLkVjAQvAeWBu", false, null, "Mgst.");
    //7d0cae07-105c-4ba1-9db4-8e4de05981c6
    Topic completeTopic = new Topic("835f1c4d-f062-4568-9b1f-869142a70150", "Enfermeria 1", "Investigación", career,
            "Enfermeria 1 Enfermeria 1 Enfermeria 1 Enfermeria 1 Enfermeria 1", "En ejecución", false, true);

    TopicStudent completeTopicStudent = new TopicStudent("c1a05089-6ffa-417a-b0ce-ac9185f7e6f0", completeTopic, new Date(2022 - 05 - 30),
            "Aprobado", "Pagado", completeStudent, true, true);



    @Test
    public void getDesignationTTById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL+"14f2647c-577d-4705-a1a4-4b55ba015dc6").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void generateStudentLetterPdf() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL+"pdf/"+"c1a05089-6ffa-417a-b0ce-ac9185f7e6f0").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void generateTeacherLetterPdf() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL+"pdf/tutor/c1a05089-6ffa-417a-b0ce-ac9185f7e6f0/careerDirector/1326547895")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void getTeacherDesignations() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL+"teacher/1658479875")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(1)));
    }
    @Test
    public void getTeacherDesignationsWithoutAntiPlagiarismLetter() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL+"antiPlagiarismLetterLink/1658479875")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(1)));
    }
    @Test
    public void getDesignationsDtoByUserCareer() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL+"designationTTDto/1326547895")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void createDesignationTT() throws Exception {
        Topic topic = new Topic();
        topic.setId("d88b86ea-7fab-46ca-9cd3-fa911973782b");
        User teacher = new User();
        teacher.setId("5e16ccd8-b0a6-42bb-b23e-0c82c5fd33eb");
        TopicStudent topicStudent= new TopicStudent();
        topicStudent.setId("a92c1361-e330-4d4d-87f2-7777e1821e6a");
        DesignationTT designationTT = new DesignationTT("",new Date(),topicStudent,teacher);
        mockMvc.perform(MockMvcRequestBuilders
                        .post(BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(designationTT)))
                .andExpect(status().isOk());
    }
    @Test
    public void updateDesignationTT() throws Exception {
        DesignationTT designationTT = new DesignationTT("42619d22-c71f-4552-af5e-25ecbaa14eb0",
                new Date(2022 - 05 - 30), this.completeTopicStudent, this.teacherOfCompleteStudent);
        mockMvc.perform(MockMvcRequestBuilders
                        .put(BASE_URL+"update/42619d22-c71f-4552-af5e-25ecbaa14eb0")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(designationTT)))
                .andExpect(status().isOk());
    }
    @Test
    public void createDesignationTTinPairs() throws Exception {
        User teacher = new User();
        teacher.setId("5e16ccd8-b0a6-42bb-b23e-0c82c5fd33eb");
        TopicStudent topicStudentOne = new TopicStudent();
        topicStudentOne.setId("a92c1361-e330-4d4d-87f2-7777e1821e6a");
        TopicStudent topicStudentTwo = new TopicStudent();
        topicStudentTwo.setId("600e1f30-2c33-45c6-8e13-1ecb70159b18");
        DesignationTT designationTTOne = new DesignationTT("",new Date(),topicStudentOne,teacher);
        DesignationTT designationTTTwo = new DesignationTT("",new Date(),topicStudentTwo,teacher);
        List<DesignationTT> designationTTList=new ArrayList<>();
        designationTTList.add(designationTTOne);
        designationTTList.add(designationTTTwo);
        DesignationsTT_Table designationsTT_table=new DesignationsTT_Table(designationTTList);
        mockMvc.perform(MockMvcRequestBuilders
                        .post(BASE_URL+"pair")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(designationsTT_table)))
                .andExpect(status().isOk());
    }
}
