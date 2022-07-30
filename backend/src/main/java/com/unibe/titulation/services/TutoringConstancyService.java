package com.unibe.titulation.services;

import javax.transaction.Transactional;

import com.unibe.titulation.entities.TutoringConstancy;
import com.unibe.titulation.repositories.TutoringConstancyRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TutoringConstancyService {
    
    private final TutoringConstancyRepository tutoringConstancyRepository;

    @Autowired
    public TutoringConstancyService(TutoringConstancyRepository tutoringConstancyRepository) {
        this.tutoringConstancyRepository = tutoringConstancyRepository;
    }

    public boolean existByTopicId(String topicId){
        return this.tutoringConstancyRepository.existsByTopic_Id(topicId);
    }
    public TutoringConstancy getByTopicId(String topicId){
        return this.tutoringConstancyRepository.findByTopic_Id(topicId).get();
    }
    public void saveConstancy(TutoringConstancy tutoringConstancy){
        this.tutoringConstancyRepository.save(tutoringConstancy);
    }
}
