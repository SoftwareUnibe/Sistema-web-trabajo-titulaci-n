package com.unibe.titulation.controllers;

import com.unibe.titulation.dtos.Message;
import com.unibe.titulation.entities.Career;
import com.unibe.titulation.services.CareerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(CareerController.CAREER)
@CrossOrigin(origins = "*")
public class CareerController {
    public static final String CAREER = "/career";
    public static final String ID = "/{id}";
    public static final String FACULTY = "/faculty";
    public static final String FACULTYNAME = "/{name_faculty}";

    private final CareerService careerService;
    @Autowired
    public CareerController(CareerService careerService) {
        this.careerService = careerService;
    }

    @GetMapping
    public List<Career> getAllCareers() {
        return this.careerService.findAllCareers();
    }

    @GetMapping(value = ID)
    public ResponseEntity<?> getCareerById(@PathVariable String id) {
        Optional<Career> careerOptional = this.careerService.findCareerById(id);
        if (careerOptional.isPresent()) {
            return new ResponseEntity<>(careerOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Message("La carrera no existe"), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = FACULTY + FACULTYNAME)
    public List<Career> getCareersByFaculty(@PathVariable String name_faculty) {
        return this.careerService.findCareersByFaculty(name_faculty);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DEAN')")
    @PostMapping
    public ResponseEntity<?> addCareer(@RequestBody Career career)  {
        try {
            this.careerService.addCareer(career);
            return new ResponseEntity<>(new Message("Carrera anadida correctamente"), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message("Los datos enviados no son los correctos"),HttpStatus.BAD_REQUEST);
        }
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DEAN')")
    @PutMapping
    public ResponseEntity<?> edit(@RequestBody Career career)  {
        try {
            this.careerService.addCareer(career);
            return new ResponseEntity<>(new Message("Carrera actualizada correctamente"), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message("Los datos enviados no son los correctos"),HttpStatus.BAD_REQUEST);
        }
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DEAN')")
    @DeleteMapping(value = ID)
    public ResponseEntity<?> deleteCareerById(@PathVariable String id) {
        try {
            if (this.careerService.deleteCareerById(id))
                return new ResponseEntity<>(new Message("La carrera fue eliminada con éxito"), HttpStatus.ACCEPTED);
            return new ResponseEntity<>(new Message("La carrera no existe"), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message("La carrera no puede ser eliminada"),HttpStatus.BAD_REQUEST);
        }
    }
}
