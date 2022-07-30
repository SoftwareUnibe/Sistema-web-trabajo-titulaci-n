package com.unibe.titulation.services;

import java.util.*;

import javax.transaction.Transactional;

import com.unibe.titulation.entities.ProductAndWorkEvaluation;
import com.unibe.titulation.repositories.ProductAndWorkEvaluationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ProductAndWorkEvaluationService {
    private final ProductAndWorkEvaluationRepository productAndWorkEvaluationRepository;
    private final ReaderService readerService;

    @Autowired
    public ProductAndWorkEvaluationService(ProductAndWorkEvaluationRepository productAndWorkEvaluationRepository,
            ReaderService readerService) {
        this.productAndWorkEvaluationRepository = productAndWorkEvaluationRepository;
        this.readerService = readerService;
    }

    public Optional<ProductAndWorkEvaluation> getByTopicId(String topicId) {
        return this.productAndWorkEvaluationRepository.findByTopic_Id(topicId);
    }

    public void saveEvaluation(ProductAndWorkEvaluation productAndWorkEvaluation, String state) {
        ProductAndWorkEvaluation evaluationToSave = productAndWorkEvaluation;
        evaluationToSave.setDate(new Date());
        this.readerService.updateReaderState(productAndWorkEvaluation.getTopic().getId(), state);
        this.productAndWorkEvaluationRepository.save(evaluationToSave);
    }
}
