package com.unibe.titulation.controllers;

import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.unibe.titulation.dtos.Message;
import com.unibe.titulation.entities.ReaderObservations;
import com.unibe.titulation.services.ReaderObservationsService;
import com.unibe.titulation.services.PDFService.ReaderObservationsPDF;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/readerObservations")
@CrossOrigin("*")
public class ReaderObservationsController {
    private final ReaderObservationsService readerObservationsService;
    private final ReaderObservationsPDF readerObservationsPDF;

    @Autowired
    public ReaderObservationsController(ReaderObservationsService readerObservationsService,
            ReaderObservationsPDF readerObservationsPDF) {
        this.readerObservationsService = readerObservationsService;
        this.readerObservationsPDF = readerObservationsPDF;
    }

    @GetMapping("/topic/{topic_id}")
    public ResponseEntity<Object> getByTopic(@PathVariable("topic_id") String topicId) {
        Optional<ReaderObservations> readerObservations = this.readerObservationsService.findByTopic(topicId);
        if (!readerObservations.isPresent())
            return new ResponseEntity<>(new Message("No encontrado"), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(readerObservations.get(), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole('ROLE_TEACHER','CAREER_DIRECTOR')")
    @PostMapping("/create")
    public ResponseEntity<Message> create(@Valid @RequestBody ReaderObservations readerObservations,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return new ResponseEntity<>(new Message("Revise los campos e intente nuevamente"), HttpStatus.BAD_REQUEST);
        this.readerObservationsService.save(readerObservations);
        return new ResponseEntity<>(new Message("Creado"), HttpStatus.OK);
    }

    @GetMapping("/pdf/{topic_id}")
    public  void generateReaderLetterPdf(@PathVariable("topic_id") String id, HttpServletResponse response){
        try {
            Path file = Paths.get(readerObservationsPDF.generatePdf(id).getAbsolutePath());
            if (Files.exists(file)){
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition", "attachment; filename"+ file.getFileName());
                Files.copy(file, response.getOutputStream());
                response.getOutputStream().flush();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @GetMapping("/verify/{topic_id}")
    public  ResponseEntity<Boolean> existsByTopicId(@PathVariable("topic_id") String id){
        return new ResponseEntity<>(this.readerObservationsService.existsByTopicId(id), HttpStatus.OK);
    }
}
