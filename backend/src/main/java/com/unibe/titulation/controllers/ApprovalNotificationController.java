package com.unibe.titulation.controllers;

import com.unibe.titulation.dtos.Message;
import com.unibe.titulation.dtos.NotificationTableDto;
import com.unibe.titulation.entities.TopicApprovalNotification;
import com.unibe.titulation.services.ApprovalNotificationService;
import com.unibe.titulation.services.PDFService.ApprovalNotificationPDF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(ApprovalNotificationController.APPROVAL)
@CrossOrigin(origins = "*")
public class ApprovalNotificationController {
    public static final String APPROVAL = "/approval_notification";
    public static final String ID = "/{id}";
    public static final String STUDENT = "/student";
    public static final String CAREER = "/career";
    public static final String CAREERID = "/{career_id}";

    private final ApprovalNotificationService approvalNotificationService;
    private final ApprovalNotificationPDF approvalNotificationPDF;

    @Autowired
    public ApprovalNotificationController(ApprovalNotificationService approvalNotificationService, ApprovalNotificationPDF approvalNotificationPDF) {
        this.approvalNotificationService = approvalNotificationService;
        this.approvalNotificationPDF = approvalNotificationPDF;
    }

    @GetMapping(value = CAREER + CAREERID)
    public List<NotificationTableDto> getApprovalNotificationsByCareer(@PathVariable String career_id) {
        return this.approvalNotificationService.findApprovalNotificationsByCareer(career_id);
    }

    @GetMapping(value = ID)
    public ResponseEntity<?> getApprovalNotificationById(@PathVariable String id) {
        Optional<TopicApprovalNotification> approvalNotificationOptional = this.approvalNotificationService
                .findApprovalNotificationByTopicStudent_Id(id);
        if (approvalNotificationOptional.isPresent()) {
            return new ResponseEntity<>(approvalNotificationOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Message("La notificacion no existe"), HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @GetMapping(value = STUDENT)
    public ResponseEntity<?> getApprovalNotificationByStudent() {
        Optional<TopicApprovalNotification> approvalNotificationOptional = this.approvalNotificationService
                .findApprovalNotificationByStudentId();
        if (approvalNotificationOptional.isPresent()) {
            return new ResponseEntity<>(approvalNotificationOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Message("El estudiante no tiene ninguna notificación"), HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ROLE_CAREER_DIRECTOR')")
    @PostMapping
    public ResponseEntity<?> createApprovalNotification(
            @RequestBody TopicApprovalNotification topicApprovalNotification) {
        try {
            boolean response = this.approvalNotificationService.createApprovalNotification(topicApprovalNotification);

            if (response)
                return new ResponseEntity<>(new Message("La notificación de aprobación de tema fue creada"), HttpStatus.ACCEPTED);

            return new ResponseEntity<>(new Message("El estudiante aún no ha presentado la propuesta de Tema"), HttpStatus.NOT_FOUND);
        } catch (Exception e) {

            return new ResponseEntity<>(new Message("Datos incorrectos"), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ROLE_CAREER_DIRECTOR')")
    @PostMapping("/pair")
    public ResponseEntity<?> createApprovalNotificationPairStudents(
            @RequestBody List<TopicApprovalNotification> topicApprovalNotification) {
        try {
            boolean response = this.approvalNotificationService.createApprovalNotificationPairStudents(topicApprovalNotification);
            if (response)
                return new ResponseEntity<>(new Message("La notificación de aprobación de tema fue creada"), HttpStatus.ACCEPTED);
            return new ResponseEntity<>(new Message("El estudiante aún no ha presentado la propuesta de Tema"), HttpStatus.NOT_FOUND);
        } catch (Exception e) {

            return new ResponseEntity<>(new Message("Datos incorrectos"), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ROLE_CAREER_DIRECTOR')")
    @PutMapping(value = ID)
    public ResponseEntity<?> updateApprovalNotification(
            @RequestBody TopicApprovalNotification topicApprovalNotification, @PathVariable String id) {
        try {
            if (this.approvalNotificationService.updateApprovalNotification(id, topicApprovalNotification))
                return new ResponseEntity<>(new Message("La notificación de aprobación de tema fue editada"),
                        HttpStatus.ACCEPTED);
            return new ResponseEntity<>(new Message("La notificación de aprobación de tema no existe"), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message("Los datos enviados no son los correctos"), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ROLE_CAREER_DIRECTOR')")
    @DeleteMapping(value = ID)
    public ResponseEntity<?> deleteApprovalNotification(@PathVariable String id) {
        if (this.approvalNotificationService.deleteApprovalNotification(id))
            return new ResponseEntity<>(new Message("La notificación de aprobación de tema fue eliminada"), HttpStatus.ACCEPTED);
        return new ResponseEntity<>(new Message("La notificación de aprobación de tema no existe"), HttpStatus.NOT_FOUND);
    }

    @GetMapping("/pdf/{topicStudentId}")
    public void downloadPdf(@PathVariable String topicStudentId, HttpServletResponse response) {
        try {
            Path file = Paths.get(approvalNotificationPDF.generateApprovalPdf(topicStudentId).getAbsolutePath());
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