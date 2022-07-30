package com.unibe.titulation.controllers;

import com.unibe.titulation.dtos.Message;
import com.unibe.titulation.entities.TopicStudent;
import com.unibe.titulation.security.entity.User;
import com.unibe.titulation.security.service.UserService;
import com.unibe.titulation.services.TopicStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {
    UserService userService;
    TopicStudentService topicStudentService;

    @Autowired
    public UserController(UserService userService, TopicStudentService topicStudentService) {
        this.userService = userService;
        this.topicStudentService = topicStudentService;
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DEAN')")
    @GetMapping("/{user_id}")
    public ResponseEntity<?> getUserById(@PathVariable String user_id) {
        Optional<User> user = this.userService.getUserById(user_id);
        if (!user.isPresent())
            return new ResponseEntity<>(new Message("El usuario no existe"), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(user.get(), HttpStatus.OK);
    }

    @GetMapping("/{ci}/{career_id}")
    public ResponseEntity<?> getByCiAndCareerId(@PathVariable("ci") String ci, @PathVariable("career_id") String career_id) {
        Optional<User> user = this.userService.getByCiAndCareerId(ci, career_id);
        Optional<TopicStudent> topicStudent = Optional.empty();
        if (!user.isPresent()) {
            return new ResponseEntity<>(new Message("El usuario que buscas no existe"), HttpStatus.BAD_REQUEST);
        } else {
            topicStudent = this.topicStudentService.findTopicStudentByStudent_Id(user.get().getId());
        }

        if (!topicStudent.isPresent()) {
            User responseUser = user.get();
            return new ResponseEntity<>(responseUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Message("El usuario ya tiene un tema asignado"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/role/{user_role}/career/{career_id}")
    public ResponseEntity<?> getByRoleNameAndCareerId(@PathVariable("user_role") String user_role, @PathVariable("career_id") String career_id) {
        List<User> userList = this.userService.getByRoleNameAndCareerId(user_role, career_id);

        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping("/role/{user_role}")
    public ResponseEntity<?> getByRoleName(@PathVariable("user_role") String user_role) {
        List<User> userList = this.userService.getByRoleName(user_role);

        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping("/roleNames/{user_rolesNames}")
    public ResponseEntity<?> findUserByRoleNames(@PathVariable("user_rolesNames") List<String> user_roles) {
        List<User> userList = this.userService.findUserByRoleNames(user_roles);

        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping("/readers")
    public ResponseEntity<?> findPossibleReaders() {
        List<User> userList = this.userService.findUsersByTutorIdsAndRoleNames();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }




}
