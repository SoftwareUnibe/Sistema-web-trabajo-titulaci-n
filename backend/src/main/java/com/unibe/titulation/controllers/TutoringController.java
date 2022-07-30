package com.unibe.titulation.controllers;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.unibe.titulation.dtos.Message;
import com.unibe.titulation.entities.TutoringConstancy;
import com.unibe.titulation.entities.TutoringHours;
import com.unibe.titulation.services.TutoringConstancyService;
import com.unibe.titulation.services.TutoringHoursService;
import com.unibe.titulation.services.PDFService.TutoringConstancyPDF;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/tutoring")
@CrossOrigin(origins = "*")
public class TutoringController {

    private final TutoringHoursService tutoringHoursService;
    private final TutoringConstancyPDF tutoringConstancyPDF;
    private final TutoringConstancyService tutoringConstancyService;

    @Autowired
    public TutoringController(TutoringHoursService tutoringHoursService, TutoringConstancyPDF tutoringConstancyPDF,
            TutoringConstancyService tutoringConstancyService) {
        this.tutoringHoursService = tutoringHoursService;
        this.tutoringConstancyPDF = tutoringConstancyPDF;
        this.tutoringConstancyService = tutoringConstancyService;
    }

    @GetMapping("/{topic_id}")
    public List<TutoringHours> getTutoringConstancyByTopicId(@PathVariable("topic_id") String topicId) {
        return this.tutoringHoursService.getTutoringConstancyByTopicId(topicId);
    }
    @PreAuthorize("hasAnyRole('ROLE_TEACHER','CAREER_DIRECTOR')")
    @PostMapping()
    public ResponseEntity<Message> createTutoringConstancy(@Valid @RequestBody TutoringHours tutoringConstancy,
                                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return new ResponseEntity<>(new Message("Revise los campos e intente nuevamente"), HttpStatus.BAD_REQUEST);
        this.tutoringHoursService.createTutoringContancy(tutoringConstancy);
        return new ResponseEntity<>(new Message("Constancia de tutoria añadida correctamente"), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole('ROLE_TEACHER','CAREER_DIRECTOR')")
    @PutMapping("/{id}")
    public ResponseEntity<Message> updateTutoringContancy(@PathVariable("id") String id,
            @Valid @RequestBody TutoringHours tutoringConstancy, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return new ResponseEntity<>(new Message("Revise los campos e intente nuevamente"), HttpStatus.BAD_REQUEST);
        this.tutoringHoursService.updateTutoringConstancy(tutoringConstancy, id);
        return new ResponseEntity<>(new Message("Constancia de tutoria actualizada correctamente"), HttpStatus.OK);
    }
    @GetMapping("/tutoringConstancy/{topic_id}")
    public ResponseEntity<Boolean> existByTopicId(@PathVariable("topic_id") String topicId) {
        return new ResponseEntity<>(this.tutoringConstancyService.existByTopicId(topicId), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole('ROLE_TEACHER','CAREER_DIRECTOR')")
    @PostMapping("/createConstancy")
    public void createConstancy(@RequestBody TutoringConstancy tutoringConstancy) {
        this.tutoringConstancyService.saveConstancy(tutoringConstancy);
    }

    @GetMapping("/pdf/{topicId}")
    public void downloadPdf(@PathVariable String topicId, HttpServletResponse response) {
        try {
            Path file = Paths.get(tutoringConstancyPDF.generateConstancy(topicId).getAbsolutePath());
            if (Files.exists(file)) {
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition", "attachment; filename" + file.getFileName());
                Files.copy(file, response.getOutputStream());
                response.getOutputStream().flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
