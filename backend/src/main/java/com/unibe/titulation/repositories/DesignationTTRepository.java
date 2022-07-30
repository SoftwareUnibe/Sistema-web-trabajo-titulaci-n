package com.unibe.titulation.repositories;

import com.unibe.titulation.entities.DesignationTT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DesignationTTRepository extends JpaRepository<DesignationTT, String> {
    Optional<DesignationTT> findByTopicStudent_Id(String topicId);
    List<DesignationTT> findByTeacher_Ci(String teacherCi);
    List<DesignationTT> findByTopicStudent_Topic_Career_Id(String careerId);
    List<DesignationTT> findDesignationTTByTopicStudent_Topic_Career_Id(String topicId);
    List<DesignationTT> findDesignationTTByTopicStudent_Topic_Id(String topicId);
    @Query(value = "select teacher_id from designationtt", nativeQuery = true)
    List<String> getAllTeachersAsTutorsIds();

    boolean existsByTopicStudent_Id(String topicStudentId);
    boolean existsByTopicStudent_Topic_Id(String topicStudentId);

}
