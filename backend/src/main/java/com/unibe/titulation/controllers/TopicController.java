package com.unibe.titulation.controllers;

import com.unibe.titulation.dtos.TopicTableDto;
import com.unibe.titulation.entities.Topic;
import com.unibe.titulation.services.TopicService;
import com.unibe.titulation.dtos.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(TopicController.TOPIC)
@CrossOrigin(origins = "*")
public class TopicController {
    public static final String TOPIC = "/topic";
    public static final String ID = "/{id}";
    public static final String CAREER = "/career";
    public static final String CAREERID = "/{id_career}";

    private final TopicService topicService;
    @Autowired
    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @GetMapping(value = ID)
    public ResponseEntity<?> getTopicById(@PathVariable String id) {
        Optional<Topic> topicOptional = this.topicService.findTopicById(id);
        if (!topicOptional.isPresent()) {
            return new ResponseEntity<>(new Message("El tema no  existe"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(topicOptional.get(), HttpStatus.OK);
    }
    @GetMapping (value = "/size" + "/{career_id}")
    public ResponseEntity<?> getTopicsSize(@PathVariable String career_id){
        if (this.topicService.findTopicsByCareer(career_id).isEmpty()){
            return new ResponseEntity<>(new Message("No existen temas"), HttpStatus.NOT_FOUND) ;
        }
        Long topicsLength = this.topicService.getSizeTopicsByCareer(career_id);
        return new ResponseEntity<>(topicsLength, HttpStatus.OK) ;
    }
    @GetMapping()
    public List<TopicTableDto> findTopicsByTopicStatus() {
        return this.topicService.findTopicsByTopicStatus();
    }

    @GetMapping(value = CAREER + CAREERID)
    public List<TopicTableDto> getTopicsByCareer(@PathVariable String id_career) {
        return this.topicService.findTopicsByCareer(id_career);
    }

    @PreAuthorize("hasRole('ROLE_CAREER_DIRECTOR')")
    @PostMapping
    public ResponseEntity<?> addTopic(@RequestBody Topic topic)  {
        try {
            this.topicService.addTopic(topic);
            return new ResponseEntity<>(new Message("El tema fue creado"), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message("Los datos enviados no son los correctos"), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ROLE_CAREER_DIRECTOR')")
    @PutMapping(value = ID)
    public ResponseEntity<?> updateTopicById(@RequestBody Topic topic, @PathVariable String id) {
        try {
            if (this.topicService.updateTopicById(id, topic))
                return new ResponseEntity<>(new Message("El tema fue editado con éxito"), HttpStatus.ACCEPTED);
            return new ResponseEntity<>(new Message("El tema no  existe"), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message("Los datos enviados no son los correctos"), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ROLE_CAREER_DIRECTOR')")
    @DeleteMapping(value = ID)
    public ResponseEntity<?> deleteTopicById(@PathVariable String id)  {
        try {
            if (this.topicService.deleteTopicById(id))
                return new ResponseEntity<>("\"El tema fue eliminado con éxito\"", HttpStatus.ACCEPTED);
            return new ResponseEntity<>("\"El tema no  existe\"", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message("El tema no puede ser eliminado porque está siendo usado"), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping(value = ID)
    public ResponseEntity<?> updateTopicStatus(@PathVariable String id)  {
        try {
            if (topicService.updateTopicStatus(id, "Ejecutado"))
                return new ResponseEntity<>("\"El estado del tema fue actualizado con éxito\"", HttpStatus.ACCEPTED);
            return new ResponseEntity<>("\"El tema no ha sido asignado a un estudiante o no existe\"",
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message("Los datos enviados no son los correctos"), HttpStatus.BAD_REQUEST);
        }
    }
}
