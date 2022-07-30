package com.unibe.titulation.repositories;

import com.unibe.titulation.entities.TopicStudent;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TopicStudentRepository extends JpaRepository<TopicStudent, String> {

    List<TopicStudent> findTopicStudentsByTopic_TopicStatus(String topicStatus);
    List<TopicStudent> findTopicStudentsByStudent_Career_IdAndTopic_TopicStatus(String career, String topicStatus);
    Optional<TopicStudent> findTopicStudentByStudent_CiAndTopic_TopicStatusAndStudent_Career_Name(String ci, String topicStatus, String career);
    Optional<TopicStudent> findTopicStudentByStudent_CiAndTopic_TopicStatus(String ci, String topicStatus);
    List<TopicStudent> findTopicStudentsByTopic_Id(String id);
    Optional<TopicStudent>findTopicStudentByStudent_Id(String id);
    List<TopicStudent> findTopicStudentsByTopic_Career_IdAndTopicEvaluationNot(String carerId, String topicEvaluationName);
    List<TopicStudent> findByStudent_Career_IdAndTopic_Id(String studentCareerId, String TopicId);
    List<TopicStudent> findByTopic_IdIn(Collection<String> topicIds);
    List<TopicStudent> findByStudent_EmailIn(Collection<String> emails);
}
