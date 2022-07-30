package com.unibe.titulation.repositories;

import java.util.Optional;

import com.unibe.titulation.entities.ProductAndWorkEvaluation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductAndWorkEvaluationRepository extends JpaRepository<ProductAndWorkEvaluation, String> {
    Optional<ProductAndWorkEvaluation>findByTopic_Id(String topicId);
    void deleteByTopic_Id(String topicId);
}
