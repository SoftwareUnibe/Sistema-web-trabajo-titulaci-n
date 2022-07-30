package com.unibe.titulation.controllers;

import com.unibe.titulation.dtos.CalendarDetailList;
import com.unibe.titulation.dtos.Message;
import com.unibe.titulation.entities.CalendarDetail;
import com.unibe.titulation.entities.Reader;
import com.unibe.titulation.services.CalendarDetailService;
import com.unibe.titulation.services.ReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.mail.internet.AddressException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/calendarDetail")
@CrossOrigin(origins = "*")
public class CalendarDetailController {

    private final CalendarDetailService calendarDetailService;
    private final ReaderService readerService;

    @Autowired
    public CalendarDetailController(CalendarDetailService calendarDetailService, ReaderService readerService) {
        this.calendarDetailService = calendarDetailService;
        this.readerService = readerService;
    }

    @PreAuthorize("hasAnyRole('ROLE_LIABLE_TT','ROLE_CAREER_DIRECTOR')")
    @GetMapping("{careerId}")
    public ResponseEntity<List<CalendarDetail>> getCalendarDetailInfo(@PathVariable String careerId) {
        List<CalendarDetail> calendarDetailTableDtos = this.calendarDetailService.getCalendarInfo(careerId);
        for (int i = 0; i < calendarDetailTableDtos.size(); i++) {
            calendarDetailTableDtos.get(i).getReader().setPassword("");
            calendarDetailTableDtos.get(i).getStudent().setPassword("");
            calendarDetailTableDtos.get(i).getTutor().setPassword("");
        }
        return new ResponseEntity<>(calendarDetailTableDtos, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_LIABLE_TT','ROLE_CAREER_DIRECTOR')")
    @GetMapping("/calendarDetails/{finalDegreeId}")
    public ResponseEntity<List<CalendarDetail>> getByFinalDegreeCalendarId(@PathVariable String finalDegreeId) {
        return new ResponseEntity<>(this.calendarDetailService.getByFinalDegreeCalendarId(finalDegreeId), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_LIABLE_TT','ROLE_CAREER_DIRECTOR')")
    @GetMapping("/existsCalendarDetail/{finalCalendarDegreeId}")
    public ResponseEntity<Boolean> checkIfCalendarDetailExists(@PathVariable String finalCalendarDegreeId) {
        return new ResponseEntity<>(this.calendarDetailService.existsCalendarDetail(finalCalendarDegreeId), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_LIABLE_TT','ROLE_CAREER_DIRECTOR')")
    @PutMapping("/calendar/{calendarId}")
    public ResponseEntity<Object> updateCalendarDetail(@RequestBody CalendarDetail calendarDetail, @PathVariable String calendarId, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return new ResponseEntity<>(new Message("Los datos enviados no son los correcots"), HttpStatus.BAD_REQUEST);

        this.calendarDetailService.updateCalendarDetail(calendarDetail);
        return new ResponseEntity<>(new Message("Los datos fueron actualizados correctamente"), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_LIABLE_TT','ROLE_CAREER_DIRECTOR')")
    @PostMapping()
    public ResponseEntity<?> saveCalendar(@RequestBody @Valid CalendarDetailList calendarDetailList,
                                          BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return new ResponseEntity<>(new Message("Campos incompletos, revise e intente de nuevo"), HttpStatus.BAD_REQUEST);

        for (int i = 0; i < calendarDetailList.getCalendarDetailList().size(); i++) {
            if (calendarDetailList.getCalendarDetailList().get(i).getSecretary().isEmpty())
                return new ResponseEntity<>(new Message("Campos incompletos, revise e intente de nuevo"), HttpStatus.BAD_REQUEST);
        }

        this.calendarDetailService.saveCalendar(calendarDetailList.getCalendarDetailList());
        List<Reader> readers = this.readerService.getReaderByStatusCalendarCreatedAndCareerId(calendarDetailList.calendarDetailList.get(0).getCareer().getId());
        for (Reader reader : readers) {
            reader.setState("Creando calendario");
            this.readerService.saveReaderCalendar(reader);
        }
        return new ResponseEntity<>(new Message("Se ha creado el cronograma de defensa final de grado"), HttpStatus.OK);

    }

    @PreAuthorize("hasAnyRole('ROLE_LIABLE_TT','ROLE_CAREER_DIRECTOR')")
    @PutMapping("/finalCalendar")
    public ResponseEntity<Message> saveFinalCalendar(@RequestBody @Valid List<String> studentIds) throws AddressException {

        this.calendarDetailService.generateFinalCalendar(studentIds);
        return new ResponseEntity<>(new Message("Se ha creado el cronograma de defensa final de grado"), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_LIABLE_TT','ROLE_CAREER_DIRECTOR')")
    @GetMapping("/status/{studentIds}")
    public ResponseEntity<Boolean> getCalendarStatus(@PathVariable List<String> studentIds) {
        return new ResponseEntity<>(this.calendarDetailService.getCalendarStatus(studentIds), HttpStatus.OK);
    }
}
