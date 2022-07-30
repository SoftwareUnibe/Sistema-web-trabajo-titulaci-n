package com.unibe.titulation.repositories;

import java.util.Optional;

import com.unibe.titulation.entities.ReaderAccordance;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReaderAccordanceRepository extends JpaRepository<ReaderAccordance, String> {
    public Optional<ReaderAccordance> findByTopic_Id(String topicId);
    void deleteByTopic_Id(String topicId);
}
