package com.unibe.titulation.controllers;

import com.unibe.titulation.dtos.AntiPlagiarismLinkAndReaderDesignation;
import com.unibe.titulation.dtos.Message;
import com.unibe.titulation.dtos.PaymentTableDto;
import com.unibe.titulation.dtos.TopicStudentTableDto;
import com.unibe.titulation.entities.TopicStudent;
import com.unibe.titulation.services.TopicStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(TopicStudentController.TOPICSTUDENT)
@CrossOrigin(origins = "*")
public class TopicStudentController {
    public static final String TOPICSTUDENT = "/student_topic";
    public static final String ID = "/{id}";
    public static final String CAREER = "/career";
    public static final String CAREERID = "/{career}";
    public static final String CAREERNAME = "/{career_name}";
    public static final String STUDENT = "/student";
    public static final String STUDENTCI = "/{student}";
    public static final String TOPICSTATUS = "/topic_status";
    public static final String TOPICSTATUSNAME = "/{topic_status}";
    public static final String PAYMENT = "/payment";

    private final TopicStudentService topicStudentService;

    @Autowired
    public TopicStudentController(TopicStudentService topicStudentService) {
        this.topicStudentService = topicStudentService;
    }


    @GetMapping(value = ID)
    public ResponseEntity<?> getTopicStudentById(@PathVariable String id) {
        Optional<TopicStudent> topicStudentOptional = this.topicStudentService.findTopicStudentById(id);
        if (topicStudentOptional.isPresent()) {
            return new ResponseEntity<>(topicStudentOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Message("El tema no existe o no ha asido asignado"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = STUDENT + "/{student_id}")
    public ResponseEntity<?> getTopicStudentByStudentId(@PathVariable String student_id) {
        Optional<TopicStudent> topicStudentOptional = this.topicStudentService.findTopicStudentByStudent_Id(student_id);
        if (topicStudentOptional.isPresent()) {
            return new ResponseEntity<>(topicStudentOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Message("El tema no ha sido asignado a un estudiante o no existe"),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = TOPICSTATUS + TOPICSTATUSNAME)
    public List<TopicStudentTableDto> getTopicsByTopicStatus(@PathVariable String topic_status) {
        return this.topicStudentService.findTopicStudentByStatus(topic_status);
    }

    @GetMapping("/verifiedPayment/{topicId}")
    public boolean verifiedPayment(@PathVariable String topicId) {
        return this.topicStudentService.checkPaymentStatusPairTheme(topicId);
    }

    @GetMapping(value = PAYMENT)
    public List<PaymentTableDto> getTopicsPaymentByTopicStatus() {
        return this.topicStudentService.findTopicStudentToPayment();
    }

    @GetMapping("/topic/{topicId}")
    public List<TopicStudent> getTopicStudentsByTopic_Id(@PathVariable("topicId") String topicId) {
        List<TopicStudent> topicStudents = this.topicStudentService.findTopicStudentsByTopic_Id(topicId);
        for (int i = 0; i < topicStudents.size(); i++) {
            topicStudents.get(i).getStudent().setPassword("");
        }
        return topicStudents;
    }

    @GetMapping(value = CAREER + CAREERID + TOPICSTATUS + TOPICSTATUSNAME)
    public List<TopicStudentTableDto> getTopicStudentsByCareer(@PathVariable String career, @PathVariable String topic_status) {
        return this.topicStudentService.findTopicStudentsByStudent_Career(career, topic_status);
    }

    @PreAuthorize("hasRole('ROLE_CAREER_DIRECTOR')")
    @GetMapping("/assignLector/{careerId}")
    public List<AntiPlagiarismLinkAndReaderDesignation> getTopicStudentsByUserCareerId(@PathVariable String careerId) {
        return this.topicStudentService.getTopicStudentsByUserCi(careerId);
    }

    @PreAuthorize("hasRole('ROLE_CAREER_DIRECTOR')")
    @GetMapping(value = STUDENT + STUDENTCI + CAREER + CAREERNAME)
    public ResponseEntity<?> getTopicStudentByStudent(@PathVariable String student, @PathVariable String career_name) {
        Optional<TopicStudent> topicStudentOptional = this.topicStudentService.findTopicStudentByStudent_Ci(student, career_name);
        if (topicStudentOptional.isPresent()) {
            return new ResponseEntity<>(topicStudentOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Message("El estudiante no tiene un tema en ejecución o no está registrado en la carrera " + career_name), HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @PostMapping
    public ResponseEntity<?> assignedTopicStudent(@RequestBody TopicStudent topicStudent) {
        try {
            boolean response = this.topicStudentService.assignedTopic(topicStudent);
            if (response)
                return new ResponseEntity<>(new Message("El tema fue asignado"), HttpStatus.ACCEPTED);
            return new ResponseEntity<>(new Message("El tema tiene sufucientes autores"),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message("Los datos enviados no son los correctos"), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @PostMapping("/pair")
    public ResponseEntity<?> assignedTopicTwoStudent(@RequestBody List<TopicStudent> topicStudent) {
        try {
            boolean response = this.topicStudentService.assignedTopicPair(topicStudent);
            if (response)
                return new ResponseEntity<>(new Message("El tema fue asignado"), HttpStatus.ACCEPTED);
            return new ResponseEntity<>(new Message("El tema tiene sufucientes autores"),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message("Los datos enviados no son los correctos"), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @DeleteMapping(value = ID)
    public ResponseEntity<?> deleteTopicStudent(@PathVariable String id) {
        try {

            if (this.topicStudentService.deleteTopicStudent(id))
                return new ResponseEntity<>(new Message("La asignación fue eliminada con éxito"), HttpStatus.ACCEPTED);
            return new ResponseEntity<>(new Message("El pago del tema ya fue registrado"),
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message("Esta asignación no puede ser eliminada porque está siendo usada"), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ROLE_FINANCIAL')")
    @PatchMapping(value = STUDENT + STUDENTCI)
    public ResponseEntity<?> paymentDenunciation(@PathVariable String student) {
        try {
            if (this.topicStudentService.updatePaymentDenunciation(student, "Pagado"))
                return new ResponseEntity<>(new Message("El registro de pago fue realizado con éxito"), HttpStatus.ACCEPTED);
            return new ResponseEntity<>(new Message("El tema no ha sido asignado a un estudiante o no existe"),
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message("Los datos enviados no son los correctos"), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('CAREER_DIRECTOR')")
    @PatchMapping(value = ID)
    public ResponseEntity<?> updateTopicEvaluation(@PathVariable String id, @RequestBody TopicStudent topicStudent) {
        try {
            boolean response = this.topicStudentService.updateTopicEvaluation(id, topicStudent);
            if (response)
                return new ResponseEntity<>(new Message("La evaluación del tema fue actualizada con éxito"),
                        HttpStatus.ACCEPTED);
            return new ResponseEntity<>(new Message("El estudiante aún no ha presentado la Propuesta de Tema"),
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message("Los datos enviados no son los correctos"), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @PatchMapping("/letterLink/{topicId}")
    public ResponseEntity<Message> updateAntiPlagiarismLetterLink(@PathVariable String topicId) {
        this.topicStudentService.updateAntiPlagiarismLetterSentStatus(topicId);
        return new ResponseEntity<>(new Message("Se ha actualizo el link de la carta antiplagio"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @PatchMapping("/updateThesisSent/{studentId}")
    public ResponseEntity<Message> updateThesisDocumentLink(@PathVariable String studentId) {
        this.topicStudentService.updateThesisSentStatus(studentId);
        return new ResponseEntity<>(new Message("Se ha actualizado el campo correctamente"), HttpStatus.OK);
    }
}
