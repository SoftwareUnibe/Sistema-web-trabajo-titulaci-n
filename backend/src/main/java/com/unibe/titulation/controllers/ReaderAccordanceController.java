package com.unibe.titulation.controllers;

import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.unibe.titulation.dtos.Message;
import com.unibe.titulation.entities.ReaderAccordance;
import com.unibe.titulation.services.PDFService.ReaderProcessResultPDF;
import com.unibe.titulation.services.ReaderAccordanceService;
import com.unibe.titulation.services.PDFService.ReaderAccordancePDF;

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
@RequestMapping("/readerAccordance")
@CrossOrigin("*")
public class ReaderAccordanceController {

    private final ReaderAccordanceService readerAccordanceService;
    private final ReaderAccordancePDF readerAccordancePDF;
    private final ReaderProcessResultPDF readerProcessResultPDF;

    @Autowired
    public ReaderAccordanceController(ReaderAccordanceService readerAccordanceService,
            ReaderAccordancePDF readerAccordancePDF, ReaderProcessResultPDF readerProcessResultPDF) {
        this.readerAccordanceService = readerAccordanceService;
        this.readerAccordancePDF = readerAccordancePDF;
        this.readerProcessResultPDF = readerProcessResultPDF;
    }

    @GetMapping("/topic/{topic_id}")
    public ResponseEntity<Object> getByTopic(@PathVariable("topic_id") String topicId) {
        Optional<ReaderAccordance> readerAccordance = this.readerAccordanceService.findByTopicId(topicId);
        if (!readerAccordance.isPresent())
            return new ResponseEntity<>(new Message("No encontrado"), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(readerAccordance.get(), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole('ROLE_TEACHER','ROLE_CAREER_DIRECTOR')")
    @PostMapping("/create")
    public ResponseEntity<Message> create(@Valid @RequestBody ReaderAccordance readerAccordance,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return new ResponseEntity<>(new Message("Revise los campos e intente nuevamente"), HttpStatus.BAD_REQUEST);
        this.readerAccordanceService.save(readerAccordance);
        return new ResponseEntity<>(new Message("Creado"), HttpStatus.OK);
    }

    @GetMapping("/pdf/{topic_id}")
    public void generateReaderLetterPdf(@PathVariable("topic_id") String id, HttpServletResponse response) {
        try {
            Path file = Paths.get(this.readerAccordancePDF.generatePdf(id).getAbsolutePath());
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

    @GetMapping("/readerProcessResultPDF/{topicId}")
    public void generateReaderProcessResultLetterPDF(@PathVariable String topicId, HttpServletResponse response){
        try {
            Path file = Paths.get(this.readerProcessResultPDF.generatePdf(topicId).getAbsolutePath());
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
