package com.unibe.titulation.services;

import java.util.Optional;

import javax.transaction.Transactional;

import com.unibe.titulation.entities.ReaderObservations;
import com.unibe.titulation.repositories.ReaderObservationsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class ReaderObservationsService {

    private final ReaderObservationsRepository readerObservationsRepository;
    private final ReaderService readerService;

    @Autowired
    public ReaderObservationsService(ReaderObservationsRepository readerObservationsRepository,
            ReaderService readerService) {
        this.readerObservationsRepository = readerObservationsRepository;
        this.readerService = readerService;
    }

    public Optional<ReaderObservations> findByTopic(String topicId) {
        return this.readerObservationsRepository.findByTopic_Id(topicId);
    }

    public void save(ReaderObservations readerObservations) {
        this.readerService.updateReaderState(readerObservations.getTopic().getId(), "Evaluado con observaciones");
        this.readerObservationsRepository.save(readerObservations);
    }

    public void delete(String id) {
        this.readerObservationsRepository.deleteById(id);
    }

    public boolean existsByTopicId(String topicId){
        return this.readerObservationsRepository.existsByTopic_Id(topicId);
    }
}
