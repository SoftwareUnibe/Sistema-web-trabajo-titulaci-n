package com.unibe.titulation.repositories;

import java.util.Optional;

import com.unibe.titulation.entities.TutoringConstancy;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TutoringConstancyRepository extends JpaRepository<TutoringConstancy, String>{
    boolean existsByTopic_Id(String topic_id);
    Optional<TutoringConstancy> findByTopic_Id(String topic_id);
}
