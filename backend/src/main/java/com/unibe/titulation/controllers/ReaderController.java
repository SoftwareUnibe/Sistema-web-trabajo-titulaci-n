package com.unibe.titulation.controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


import com.unibe.titulation.dtos.Message;
import com.unibe.titulation.entities.Reader;
import com.unibe.titulation.services.PDFService.ReaderDesignationLetterPDF;
import com.unibe.titulation.services.ReaderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/reader")
public class ReaderController {

    private final ReaderService readerService;
    private final ReaderDesignationLetterPDF readerDesignationLetterPDF;

    @Autowired
    public ReaderController(ReaderService readerService, ReaderDesignationLetterPDF readerDesignationLetterPDF) {
        this.readerService = readerService;
        this.readerDesignationLetterPDF = readerDesignationLetterPDF;
    }

    @GetMapping("/{id}")
    public Optional<Reader> getReaderById1(@PathVariable("id") String id) {
        return this.readerService.getReaderById(id);
    }
    @PreAuthorize("hasAnyRole('ROLE_TEACHER','ROLE_CAREER_DIRECTOR')")
    @GetMapping("/listByReader/{reader_ci}")
    public List<Reader> getReaderDesignationsByReaderCi(@PathVariable("reader_ci") String reader_ci) {
        return this.readerService.getReadersByReaderCi(reader_ci);
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @GetMapping("/studentReaders/{student_Id}")
    public ResponseEntity<Object> getReaderByStudentIdAndTopicId(@PathVariable String student_Id) {
        Optional<Reader> reader = this.readerService.getReaderByStudentIdAndTopicId(student_Id);
        if (!reader.isPresent())
            return new ResponseEntity<>(new Message("No existe un lector asignado"), HttpStatus.NOT_FOUND);
        reader.get().getReader().setPassword("");
        return new ResponseEntity<>(reader.get(), HttpStatus.OK);
    }

    @GetMapping("/reader/{id}")
    public ResponseEntity<Object> getReaderDetailById(@PathVariable("id") String id) {
        Optional<Reader> reader = this.readerService.getReaderById(id);
        if (!reader.isPresent())
            return new ResponseEntity<>(new Message("No se encuentra"), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(reader.get(), HttpStatus.OK);
    }

    @GetMapping("/checkStatus/{careerId}")
    public ResponseEntity<Message> checkStatusToCreateCalendar(@PathVariable String careerId) {
        boolean status = this.readerService.existsReaderByStatusAndCareerId(careerId);
        if (!status)
            return new ResponseEntity<>(new Message("No existen estudiantes que puedan continuar con este proceso"), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(new Message("Si existen estudiantes para continuar con este proceso"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_CAREER_DIRECTOR')")
    @PostMapping("/createReader")
    public ResponseEntity<?> assignReader(@RequestBody @Valid Reader reader, BindingResult bindingResult) throws AddressException {
        if (bindingResult.hasErrors())
            return new ResponseEntity<>(new Message("Campos incompletos, revise e intente nuevamente"), HttpStatus.BAD_REQUEST);

        this.readerService.saveReader(reader);
        return new ResponseEntity<>(new Message("Se ha asignado al docente como lector"), HttpStatus.OK);
    }

    @GetMapping("/pdf/{id}")
    public void generateReaderLetterPdf(@PathVariable String id, HttpServletResponse response) {
        try {
            Path file = Paths.get(readerDesignationLetterPDF.generateReaderDesignationLetterPdf(id).getAbsolutePath());
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
    @GetMapping("/all/{career_id}")
    public ResponseEntity<List<Reader>> getAllByCareer(@PathVariable String career_id) {
        return new ResponseEntity<>(this.readerService.getAllByCareer(career_id),HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ROLE_CAREER_DIRECTOR')")
    @DeleteMapping("/resetProcess/{topic_id}")
    public ResponseEntity<Message> resetProcess(@PathVariable String topic_id){
        this.readerService.resetReaderProcess(topic_id);
        return new ResponseEntity<>(new Message("Eliminado correctamente"),HttpStatus.OK);
    }
}
