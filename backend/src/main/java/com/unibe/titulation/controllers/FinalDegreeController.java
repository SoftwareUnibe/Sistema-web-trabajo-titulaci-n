package com.unibe.titulation.controllers;

import com.unibe.titulation.dtos.Message;
import com.unibe.titulation.entities.FinalDegreeCalendar;
import com.unibe.titulation.services.FinalDegreeCalendarService;
import com.unibe.titulation.services.ReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/finalDegreeCalendar")
public class FinalDegreeController {

    private final FinalDegreeCalendarService finalDegreeCalendarService;
    private final ReaderService readerService;

    @Autowired
    public FinalDegreeController(FinalDegreeCalendarService finalDegreeCalendarService, ReaderService readerService) {
        this.finalDegreeCalendarService = finalDegreeCalendarService;
        this.readerService = readerService;
    }

    @PreAuthorize("hasAnyRole('ROLE_LIABLE_TT','ROLE_CAREER_DIRECTOR')")
    @GetMapping()
    public List<FinalDegreeCalendar> getAllFinalDegreeCalendar() {
        return this.finalDegreeCalendarService.getAllFinalDegreeCalendars();
    }

    @PreAuthorize("hasAnyRole('ROLE_LIABLE_TT','ROLE_CAREER_DIRECTOR')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getFinalDegreeCalendar(@PathVariable String id) {
        Optional<FinalDegreeCalendar> finalDegreeCalendar = this.finalDegreeCalendarService.getFinalCalendarById(id);
        if (!finalDegreeCalendar.isPresent())
            return new ResponseEntity<>(new Message("No existe el documento"), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(finalDegreeCalendar.get(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_LIABLE_TT','ROLE_CAREER_DIRECTOR')")
    @GetMapping("/list/{careerId}")
    public List<FinalDegreeCalendar> getFinalDegreeCalendarByCareerId(@PathVariable String careerId) {
        return this.finalDegreeCalendarService.getFinalCalendarsByCareerId(careerId);
    }

    @PreAuthorize("hasAnyRole('ROLE_LIABLE_TT','ROLE_CAREER_DIRECTOR')")
    @PostMapping()
    public ResponseEntity<Object> createFinalDegreeCalendar(@RequestBody @Valid FinalDegreeCalendar finalDegreeCalendar, BindingResult bindingResult) {
        FinalDegreeCalendar finalDegreeCalendarCreated = this.finalDegreeCalendarService.createFinalCalendar(finalDegreeCalendar);
        this.readerService.updateReaderStateByCareerId(finalDegreeCalendarCreated.getCareer().getId(), "Creando calendario");
        return new ResponseEntity<>(finalDegreeCalendarCreated, HttpStatus.OK);
    }

}
