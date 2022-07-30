package com.unibe.titulation.controllers;

import com.unibe.titulation.security.entity.User;
import com.unibe.titulation.services.AcademicUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(AcademicUserController.ACADEMIC)
@CrossOrigin(origins = "*")
public class AcademicUserController {
    public static final String ACADEMIC = "/academic";
    public static final String CAREER = "/career";
    public static final String CAREERID = "/{career}";
    private final AcademicUserService academicUserService;

    @Autowired
    public AcademicUserController(AcademicUserService academicUserService) {
        this.academicUserService = academicUserService;
    }

    @GetMapping(value = CAREER + CAREERID)
    public List<User> getUserByCareerDirector(@PathVariable String career) {
        return this.academicUserService.findUserByCareerDirector(career);
    }
}
