package com.unibe.titulation.repositories;

import java.util.Optional;

import com.unibe.titulation.entities.ReaderObservations;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReaderObservationsRepository extends JpaRepository<ReaderObservations, String> {
    public Optional<ReaderObservations> findByTopic_Id(String topicId);
    void deleteByTopic_Id(String topicId);
    boolean existsByTopic_Id(String topicId);
}
