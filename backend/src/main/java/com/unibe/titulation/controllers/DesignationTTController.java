package com.unibe.titulation.controllers;


import com.unibe.titulation.dtos.AntiPlagiarismLinkAndReaderDesignation;
import com.unibe.titulation.dtos.DesignationTT_TableDto;
import com.unibe.titulation.dtos.DesignationsTT_Table;
import com.unibe.titulation.dtos.Message;
import com.unibe.titulation.entities.DesignationTT;
import com.unibe.titulation.services.DesignationTTService;
import com.unibe.titulation.services.PDFService.DesignationTT.StudentLetterPDF;
import com.unibe.titulation.services.PDFService.DesignationTT.TutorLetterPDF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/designationTT")
@CrossOrigin(origins = "*")
public class DesignationTTController {

    DesignationTTService designationTTService;
    private final StudentLetterPDF studentLetterPDF;
    private final TutorLetterPDF tutorLetterPDF;

    @Autowired
    public DesignationTTController(DesignationTTService designationTTService, StudentLetterPDF studentLetterPDF, TutorLetterPDF tutorLetterPDF) {
        this.designationTTService = designationTTService;
        this.studentLetterPDF = studentLetterPDF;
        this.tutorLetterPDF = tutorLetterPDF;
    }

    @GetMapping("/{designationTT_id}")
    public ResponseEntity<?> getDesignationTTById(@PathVariable String designationTT_id) {
        Optional<DesignationTT> designationTT = this.designationTTService.getDesignationTTById(designationTT_id);
        if (!designationTT.isPresent())
            return new ResponseEntity<>(new Message("No se ha encontrado"), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(designationTT.get(), HttpStatus.OK);
    }

    @GetMapping("/designationTTDto/{userCi}")
    public List<DesignationTT_TableDto> getAllDesignationTTDto(@PathVariable("userCi")String userCi){
        return this.designationTTService.getAllDesignationDtoByUserCareer(userCi);
    }

    @GetMapping("/topic/{topicStudent_Id}")
    public ResponseEntity<?> findByTopicStudent_Id(@PathVariable String topicStudent_Id) {
        Optional<DesignationTT> designationTT = this.designationTTService.findByTopicStudent_Id(topicStudent_Id);
        if (!designationTT.isPresent())
            return new ResponseEntity<>(new Message("No se ha encontrado"), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(designationTT.get(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('CAREER_DIRECTOR')")
    @PostMapping()
    public ResponseEntity<?> createDesignationTT(@Valid @RequestBody DesignationTT designationTT, BindingResult bindingResult) throws AddressException {
        if (bindingResult.hasErrors())
            return new ResponseEntity<>(new Message("Faltan campos por llenar, revise e intente nuevamente"), HttpStatus.BAD_REQUEST);
        this.designationTTService.createDesignationTT(designationTT);

        return new ResponseEntity<>(new Message("Se han generado los documentos"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('CAREER_DIRECTOR')")
    @PostMapping("/pair")
    public ResponseEntity<?> createDesignationTTPairStudents(@RequestBody @Valid DesignationsTT_Table designationTT, BindingResult bindingResult) throws AddressException {
        if (bindingResult.hasErrors())
            return new ResponseEntity<>(new Message("Faltan campos por llenar, revise e intente nuevamente"), HttpStatus.BAD_REQUEST);
        this.designationTTService.createDesignationTTPairStudents(designationTT.getDesignationTTList());
        return new ResponseEntity<>(new Message("Se han generado los documentos"), HttpStatus.OK);
    }


    @GetMapping("/teacher/{teacher_ci}")
    public ResponseEntity<List<DesignationTT_TableDto>> getDesignationByTeacher(@PathVariable String teacher_ci) {
        List<DesignationTT_TableDto> foundList = this.designationTTService.findByTeacherCi(teacher_ci);
        List<DesignationTT_TableDto> returnList= new ArrayList<>();
        for (int i = 0; i < foundList.size(); i++) {
            if (!foundList.get(i).getTopicStudent().get(0).getTopic().isTwoStudents())
                returnList.add(foundList.get(i));
            if (foundList.get(i).getTopicStudent().get(0).getTopic().isTwoStudents() && i%2!=0 )
                returnList.add(foundList.get(i));
        }
        return new ResponseEntity<>(returnList, HttpStatus.OK);
    }

    @PutMapping("/update/{designationTT_id}")
    public ResponseEntity<?> updateDesignationTTById(@PathVariable String designationTT_id,
                                                     @RequestBody DesignationTT designationTT, BindingResult bindingResult) throws AddressException {
        if (bindingResult.hasErrors())
            return new ResponseEntity<>(new Message("Revise los campos e intente nuevamente"), HttpStatus.BAD_REQUEST);
        DesignationTT designationTT1 = this.designationTTService.getDesignationTTById(designationTT_id).get();
        this.designationTTService.createDesignationTT(designationTT1);
        return new ResponseEntity<>(new Message("Actualizado con éxito"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_CAREER_DIRECTOR')")
    @GetMapping("/antiPlagiarismLetterSent/{teacherCi}")
    public ResponseEntity<List<AntiPlagiarismLinkAndReaderDesignation>> getDesignationsTTByTeacherCi(@PathVariable String teacherCi) {
        return new ResponseEntity<>(this.designationTTService.getDesignationsTTByTeacherCi(teacherCi),HttpStatus.OK);
    }

    @GetMapping("/pdf/{topicStudent_Id}")
    public void downloadStudentDesignationLetterAsPdf(@PathVariable String topicStudent_Id, HttpServletResponse response) {
        try {
            Path file = Paths.get(studentLetterPDF.generateStudentLetterPdf(topicStudent_Id).getAbsolutePath());
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

    @GetMapping("/pdf/tutor/{topicStudent_Id}/careerDirector/{careerDirectorCi}")
    public void downloadTutorDesignationLetterAsPdf(@PathVariable String topicStudent_Id,
                                                    @PathVariable String careerDirectorCi, HttpServletResponse response) {
        try {
            Path file = Paths.get(tutorLetterPDF.generateTutorLetterPdf(topicStudent_Id, careerDirectorCi).getAbsolutePath());
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
