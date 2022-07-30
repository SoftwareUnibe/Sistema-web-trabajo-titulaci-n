package com.unibe.titulation.controllers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unibe.titulation.security.dto.LoginUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SecurityTest {
    @Autowired
    private MockMvc mockMvc;
    final String BASE_URL="/auth";
    @Autowired
    ObjectMapper mapper;

    @Test
    public void login() throws Exception{
        LoginUser loginUser = new LoginUser();
        loginUser.setUserName("1717171717");
        loginUser.setPassword("1717171717");
        mockMvc.perform(MockMvcRequestBuilders
                        .post(BASE_URL+"login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(loginUser)))
                        .andExpect(status().isOk());
    }
    @Test
    public void logout() throws Exception{
        Cookie cookie = new Cookie("AuthToken","eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxNzE3MTcxNzE" +
                "3Iiwicm9sZXMiOlsiUk9MRV9BRE1JTiJdLCJpYXQiOjE2NTU1Njk0OTcsImV4cCI6MTY1NTU3NjY5N30." +
                "uYwk-Ud0icuywKM5aaoBxnrpI7HmMJHZaUykEx10bZQosksydg4-XVMSc2_n34ki9LdI1PuCmseNR1ERejw7jg");
        mockMvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL+"/logout")
                        .cookie(cookie)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void unauthorizedRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL+"/admin/allUsers")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
    @Test
    public void authorizedRequest() throws Exception {
        Cookie cookie = new Cookie("AuthToken","eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxNzE3MTcxNzE" +
                "3Iiwicm9sZXMiOlsiUk9MRV9BRE1JTiJdLCJpYXQiOjE2NTU1Njk0OTcsImV4cCI6MTY1NTU3NjY5N30." +
                "uYwk-Ud0icuywKM5aaoBxnrpI7HmMJHZaUykEx10bZQosksydg4-XVMSc2_n34ki9LdI1PuCmseNR1ERejw7jg");
        mockMvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL+"/admin/allUsers")
                        .accept(MediaType.APPLICATION_JSON)
                        .cookie(cookie))
                .andExpect(status().isOk());
    }
    @Test
    public void verifyUser() throws Exception {
        Cookie cookie = new Cookie("AuthToken","eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxNzE3MTcxNzE" +
                "3Iiwicm9sZXMiOlsiUk9MRV9BRE1JTiJdLCJpYXQiOjE2NTU1Njk0OTcsImV4cCI6MTY1NTU3NjY5N30." +
                "uYwk-Ud0icuywKM5aaoBxnrpI7HmMJHZaUykEx10bZQosksydg4-XVMSc2_n34ki9LdI1PuCmseNR1ERejw7jg");
        mockMvc.perform(MockMvcRequestBuilders
                        .patch(BASE_URL+"/admin/verifiedUser/1658479875")
                        .accept(MediaType.APPLICATION_JSON)
                        .cookie(cookie))
                .andExpect(status().isOk());
    }
}
