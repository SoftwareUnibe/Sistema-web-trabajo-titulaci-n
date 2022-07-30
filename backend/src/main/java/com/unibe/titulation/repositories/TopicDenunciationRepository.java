package com.unibe.titulation.repositories;

import com.unibe.titulation.entities.TopicDenunciation;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface TopicDenunciationRepository extends JpaRepository<TopicDenunciation, String> {

    Optional<TopicDenunciation> findTopicDenunciationByTopicStudent_Id(String id);
    Optional<TopicDenunciation> findTopicDenunciationByTopicStudent_Student_Id(String id);
    List<TopicDenunciation> findTopicDenunciationByTopicStudent_Topic_Id(String topicId);
}
