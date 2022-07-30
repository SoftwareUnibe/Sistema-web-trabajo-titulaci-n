package com.unibe.titulation.controllers;

import com.unibe.titulation.dtos.Message;
import com.unibe.titulation.entities.TopicDenunciation;
import com.unibe.titulation.services.PDFService.TopicDenunciationPDF;
import com.unibe.titulation.services.TopicDenunciationService;
import com.unibe.titulation.services.TopicStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@RestController
@RequestMapping(TopicDenunciationController.DENUNCIATION)
@CrossOrigin(origins = "*")
public class TopicDenunciationController {
    public static final String DENUNCIATION = "/topic_denunciation";
    public static final String ID = "/{id}";

    private final TopicStudentService topicStudentService;
    private final TopicDenunciationService topicDenunciationService;
    private final TopicDenunciationPDF topicDenunciationPDF;
    @Autowired
    public TopicDenunciationController(TopicStudentService topicStudentService, TopicDenunciationService topicDenunciationService, TopicDenunciationPDF topicDenunciationPDF) {
        this.topicStudentService = topicStudentService;
        this.topicDenunciationService = topicDenunciationService;
        this.topicDenunciationPDF = topicDenunciationPDF;
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @PostMapping
    public ResponseEntity<String> createTopicDenunciation(@RequestBody TopicDenunciation topicDenunciation)
            {
        try {
            boolean response = this.topicStudentService.createTopicDenunciation(topicDenunciation);
            if (response)
                return new ResponseEntity<>("\"La denuncia de tema fue creada\"", HttpStatus.ACCEPTED);
            return new ResponseEntity<>("\"La denuncia de tema aún no ha sido pagada\"", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Los datos enviados no son los correctos", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = ID)
    public ResponseEntity<?> getTopicDenunciationByTopicStudentId(@PathVariable String id) {
        Optional<TopicDenunciation> topicDenunciation = this.topicDenunciationService.findTopicDenunciationByTopicStudentId(id);
        if (topicDenunciation.isPresent()) {
            return new ResponseEntity<>(topicDenunciation.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Message("La denuncia de tema no exite"), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/student/{student_id}")
    public ResponseEntity<?> getTopicDenunciationByStudentId(@PathVariable String student_id) {
        Optional<TopicDenunciation> topicDenunciation = this.topicDenunciationService.findTopicDenunciationByStudentId(student_id);
        if (topicDenunciation.isPresent()) {
            return new ResponseEntity<>(topicDenunciation.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Message("La denuncia de tema no existe"), HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @DeleteMapping(value = ID)
    public ResponseEntity<?> deleteTopicDenunciationById(@PathVariable String id) {
        if (this.topicDenunciationService.deleteTopicDenunciation(id))
            return new ResponseEntity<>(new Message("La denuncia de tema fue eliminada con éxito"), HttpStatus.OK);
        return new ResponseEntity<>(new Message("La denuncia de tema no existe"), HttpStatus.NOT_FOUND);

    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") String id, @RequestBody TopicDenunciation topicDenunciation){
        Optional<TopicDenunciation> topicDenuciationOptional = this.topicDenunciationService.findTopicDenunciationById(id);
        if(!topicDenuciationOptional.isPresent()) 
            return new ResponseEntity<>(new Message("La denuncia no existe"), HttpStatus.BAD_REQUEST);
        TopicDenunciation topicDenuciationToSave = topicDenuciationOptional.get();
        topicDenuciationToSave.setSemesterLevel(topicDenunciation.getSemesterLevel());
        topicDenuciationToSave.setCiudad(topicDenunciation.getCiudad());
        topicDenuciationToSave.setInvestigationLine(topicDenunciation.getInvestigationLine());
        topicDenuciationToSave.setArticulationTopic(topicDenunciation.getArticulationTopic());
        topicDenuciationToSave.setProjectType(topicDenunciation.getProjectType());
        this.topicDenunciationService.updateTopicDenunciation(topicDenunciation);
        
        return new ResponseEntity<>(new Message("Actualizado con exito"), HttpStatus.OK);
    }

    @GetMapping("/pdf/{topicStudentId}")
    public  void downloadPdf(@PathVariable String topicStudentId, HttpServletResponse response){
        try {
            Path file = Paths.get(topicDenunciationPDF.generateApprovalPdf(topicStudentId).getAbsolutePath());
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

}
