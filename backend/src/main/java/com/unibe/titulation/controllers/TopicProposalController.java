package com.unibe.titulation.controllers;

import com.unibe.titulation.dtos.Message;
import com.unibe.titulation.dtos.TopicProposalTableDto;
import com.unibe.titulation.entities.TopicProposal;
import com.unibe.titulation.services.PDFService.TopicProposalPDF;
import com.unibe.titulation.services.TopicProposalService;
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
import java.util.List;

@RestController
@RequestMapping(TopicProposalController.PROPOSAL)
@CrossOrigin(origins = "*")
public class TopicProposalController {
    public static final String PROPOSAL = "/proposal";
    public static final String ID = "/{id}";

    private final TopicProposalService topicProposalService;
    private final TopicProposalPDF topicProposalPDF;

    @Autowired
    public TopicProposalController(TopicProposalService topicProposalService, TopicProposalPDF topicProposalPDF) {
        this.topicProposalService = topicProposalService;
        this.topicProposalPDF = topicProposalPDF;
    }
    @GetMapping("/topic/{id}")
    public List<TopicProposal> getTopicProposalByTopicId(@PathVariable String id) {
        return this.topicProposalService.findTopicProposalByTopicId(id);
    }

    @GetMapping("/career/{id}")
    public Optional<TopicProposal> getTopicProposalById(@PathVariable String id){
        return this.topicProposalService.findTopicProposalById(id);
    }

    @GetMapping("student/{student_Ci}")
    public ResponseEntity<?> getTopicProposalByStudentId(@PathVariable String student_Ci) {
        Optional<TopicProposal> topicProposal = this.topicProposalService.findTopicProposalByStudentId(student_Ci);
        if (topicProposal.isPresent()) {
            return new ResponseEntity<>(topicProposal.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Message("El estudiante no presenta propuestas de tema"), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/career/{career_Id}/topic_status/{topic_status}")
    public List<TopicProposalTableDto> getTopicProposalByCareerId(@PathVariable String career_Id, @PathVariable String topic_status){
        return this.topicProposalService.findTopicProposalByCareer(career_Id, topic_status);
    }

    @GetMapping("/topic_proposal/{proposal_Id}")
    public TopicProposalTableDto getOneTopicProposalById(@PathVariable String proposal_Id){
        return this.topicProposalService.findOneTopicProposalById(proposal_Id);
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @PostMapping
    public ResponseEntity<?> createTopicProposal(@RequestBody TopicProposal topicProposal)  {
        try {
            boolean response = this.topicProposalService.createTopicProposal(topicProposal);
            if (response)
                return new ResponseEntity<>(new Message("Propuesta de tema enviada correctamente"), HttpStatus.ACCEPTED);
            return new ResponseEntity<>(new Message("La denuncia de tema aún no ha sido enviada"), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message("Los datos enviados no son los correctos"), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @PutMapping(value = ID)
    public ResponseEntity<?> updateTopicProposal(@RequestBody TopicProposal topicProposal, @PathVariable String id) {
        try {
            if (this.topicProposalService.updateTopicProposal(id, topicProposal))
                return new ResponseEntity<>(new Message("La propuesta de tema fue editada"), HttpStatus.ACCEPTED);
            return new ResponseEntity<>(new Message("La propuesta de tema no existe"), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message("Los datos enviados no son los correctos"), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @DeleteMapping(value = ID)
    public ResponseEntity<?> deleteTopicProposal(@PathVariable String id) {
        if (this.topicProposalService.deleteTopicProposal(id))
            return new ResponseEntity<>(new Message("La propuesta de tema fue eliminada"), HttpStatus.ACCEPTED);
        return new ResponseEntity<>(new Message("La propuesta de tema no existe"), HttpStatus.NOT_FOUND);
    }

    @GetMapping("/pdf/{topicId}")
    public void downloadPdf(@PathVariable String topicId, HttpServletResponse response){
        try {
            Path file = Paths.get(topicProposalPDF.generateProposalPdf(topicId).getAbsolutePath());
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
