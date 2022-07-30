package com.unibe.titulation.repositories;

import com.unibe.titulation.entities.Reader;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;



public interface ReaderRepository extends JpaRepository<Reader, String> {
    List<Reader> findByReader_Ci(String reader_ci);
    Optional<Reader> findByTopic_Id(String topic_id);
    List<Reader> findByStateAndTopic_Career_Id(String state, String careerId);
    boolean existsByStateAndTopic_Career_Id(String state, String careerId);
    List<Reader> findByTopic_IdIn(Collection<String> topicsId);

    List<Reader> findByTopic_Career_Id(String careerId);
    void deleteByTopic_Id(String careerId);
}
