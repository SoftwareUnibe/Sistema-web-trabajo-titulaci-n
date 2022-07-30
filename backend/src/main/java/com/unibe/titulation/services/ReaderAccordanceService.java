package com.unibe.titulation.services;

import java.util.Optional;

import javax.transaction.Transactional;

import com.unibe.titulation.entities.ReaderAccordance;
import com.unibe.titulation.repositories.ReaderAccordanceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ReaderAccordanceService {
    
    private final ReaderService readerService;
    private final ReaderAccordanceRepository readerAccordanceRepository;

    @Autowired
    public ReaderAccordanceService(ReaderService readerService, ReaderAccordanceRepository readerAccordanceRepository) {
        this.readerService = readerService;
        this.readerAccordanceRepository = readerAccordanceRepository;
    }

    public Optional<ReaderAccordance> findByTopicId(String topicId){
        return this.readerAccordanceRepository.findByTopic_Id(topicId);
    }
    public void save(ReaderAccordance readerAccordance ){
        String state = readerAccordance.isAccordance()?"En conformidad":"En no conformidad";
        this.readerService.updateReaderState(readerAccordance.getTopic().getId(),state);
        this.readerAccordanceRepository.save(readerAccordance);
    }

    
}
