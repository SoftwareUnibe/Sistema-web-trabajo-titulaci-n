package com.unibe.titulation.services;

import com.unibe.titulation.entities.FinalDegreeCalendar;
import com.unibe.titulation.repositories.FinalDegreeCalendarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FinalDegreeCalendarService {

    private final FinalDegreeCalendarRepository finalDegreeCalendarRepository;

    @Autowired
    public FinalDegreeCalendarService(FinalDegreeCalendarRepository finalDegreeCalendarRepository) {
        this.finalDegreeCalendarRepository = finalDegreeCalendarRepository;
    }

    public List<FinalDegreeCalendar> getAllFinalDegreeCalendars(){
        return this.finalDegreeCalendarRepository.findAll();
    }

    public FinalDegreeCalendar createFinalCalendar(FinalDegreeCalendar finalDegreeCalendar){
       return this.finalDegreeCalendarRepository.save(finalDegreeCalendar);
    }

    public Optional<FinalDegreeCalendar> getFinalCalendarById(String id){
        return this.finalDegreeCalendarRepository.findById(id);
    }

    public List<FinalDegreeCalendar> getFinalCalendarsByCareerId(String careerId){
        return this.finalDegreeCalendarRepository.findByCareer_id(careerId);
    }
}
