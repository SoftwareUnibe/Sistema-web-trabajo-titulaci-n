package com.unibe.titulation.services;

import java.util.List;

import javax.transaction.Transactional;

import com.unibe.titulation.entities.TutoringHours;
import com.unibe.titulation.repositories.TutoringHoursRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TutoringHoursService {

    private final TutoringHoursRepository tutoringConstancyRepository;

    @Autowired
    public TutoringHoursService(TutoringHoursRepository tutoringConstancyRepository) {
        this.tutoringConstancyRepository = tutoringConstancyRepository;
    }

    public List<TutoringHours> getTutoringConstancyByTopicId(String topicId){
        return this.tutoringConstancyRepository.findByTopic_Id(topicId);
    }
    public void createTutoringContancy(TutoringHours tutoringConstancy){
        this.tutoringConstancyRepository.save(tutoringConstancy);
    }
    public void updateTutoringConstancy(TutoringHours tutoringConstancy, String id){
        TutoringHours tutoringConstancyToSave= this.tutoringConstancyRepository.findById(id).get();
        tutoringConstancyToSave.setPeriod(tutoringConstancy.getPeriod());
        tutoringConstancyToSave.setActivity(tutoringConstancy.getActivity());
        tutoringConstancyToSave.setDate(tutoringConstancy.getDate());
        tutoringConstancyToSave.setHours(tutoringConstancy.getHours());
        this.tutoringConstancyRepository.save(tutoringConstancyToSave);
    }

    
    
}
