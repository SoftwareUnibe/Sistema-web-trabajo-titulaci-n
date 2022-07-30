package com.unibe.titulation.controllers;

import com.unibe.titulation.dtos.Message;
import com.unibe.titulation.entities.Faculty;
import com.unibe.titulation.services.FacultyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(FacultyController.FACULTY)
@CrossOrigin(origins = "*")
public class FacultyController {
    public static final String FACULTY = "/faculty";
    public static final String ID = "/{id}";
    private final FacultyService facultyService;

    @Autowired
    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }


    @GetMapping
    public List<Faculty> getAllFaculty() {
        return this.facultyService.findAllFacultys();
    }

    @GetMapping(value = ID)
    public ResponseEntity<?> getFacultyById(@PathVariable String id) {
        Optional<Faculty> facultyOptional = this.facultyService.findFacultyById(id);
        if (facultyOptional.isPresent()) {
            return new ResponseEntity<>(facultyOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Message("La facultad solicitada no existe"), HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DEAN')")
    @PostMapping
    public ResponseEntity<?> addFaculty(@RequestBody Faculty faculty)  {
        try {
            this.facultyService.addfaculty(faculty);
            return new ResponseEntity<>(new Message("Facultad agregada"), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>( new Message("Los datos enviados no son los correctos"), HttpStatus.BAD_REQUEST);
        }
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DEAN')")
    @PutMapping
    public ResponseEntity<?> editFaculty(@RequestBody Faculty faculty)  {
        try {
            this.facultyService.addfaculty(faculty);
            return new ResponseEntity<>(new Message("Facultad editada"), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>( new Message("Los datos enviados no son los correctos"), HttpStatus.BAD_REQUEST);
        }
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DEAN')")
    @DeleteMapping(value = ID)
    public ResponseEntity<?> deleteFacultyById(@PathVariable String id)  {
        try {
            if (this.facultyService.deleteFacultyById(id))
                return new ResponseEntity<>(new Message("Facultad borrada correctamente"), HttpStatus.ACCEPTED);
            return new ResponseEntity<>(new Message("La Facultad no existe"), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>( new Message("Esta facultad no puede ser eliminada porque está siendo usada"), HttpStatus.BAD_REQUEST);
        }
    }
}
