package com.unibe.titulation.controllers;

import java.util.Optional;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.unibe.titulation.dtos.Message;
import com.unibe.titulation.entities.ProductAndWorkEvaluation;
import com.unibe.titulation.services.ProductAndWorkEvaluationService;
import com.unibe.titulation.services.PDFService.ReaderEvaluationPDF;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/evaluation")
@CrossOrigin("*")
public class ProductAndWorkEvaluationController {
    private final ProductAndWorkEvaluationService productAndWorkEvaluationService;
    private final ReaderEvaluationPDF readerEvaluationPDF;

    @Autowired
    public ProductAndWorkEvaluationController(ProductAndWorkEvaluationService productAndWorkEvaluationService,
            ReaderEvaluationPDF readerEvaluationPDF) {
        this.productAndWorkEvaluationService = productAndWorkEvaluationService;
        this.readerEvaluationPDF = readerEvaluationPDF;
    }
    @GetMapping("/topic/{topic_id}")
    public ResponseEntity<Object> getByTopicId(@PathVariable("topic_id")String topicId){
        Optional<ProductAndWorkEvaluation> evaluation = this.productAndWorkEvaluationService.getByTopicId(topicId);
        if(!evaluation.isPresent())
            return new ResponseEntity<>(new Message("No encontrado"), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(evaluation.get(), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole('ROLE_TEACHER','ROLE_CAREER_DIRECTOR')")
    @PostMapping()
    public ResponseEntity<Object> saveEvaluation(@Valid @RequestBody ProductAndWorkEvaluation productAndWorkEvaluation, BindingResult bindingResult){
        if (bindingResult.hasErrors()) 
            return new ResponseEntity<>(new Message("Revise los campos e intente nuevamente"), HttpStatus.BAD_REQUEST);
        String state = productAndWorkEvaluation.getFinalNote()>6?"Evaluado":"Siguiente jornada";
        this.productAndWorkEvaluationService.saveEvaluation(productAndWorkEvaluation,state);
        return new ResponseEntity<>(new Message("Guardado"), HttpStatus.OK);
    }   
    
    @GetMapping("/pdf/{topic_id}")
    public  void generateEvaluationPdf(@PathVariable("topic_id") String id, HttpServletResponse response){
        try {
            Path file = Paths.get(readerEvaluationPDF.generateReaderEvaluationPdf(id).getAbsolutePath());
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
