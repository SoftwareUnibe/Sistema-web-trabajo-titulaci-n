package com.unibe.titulation.repositories;

import com.unibe.titulation.entities.TopicApprovalNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApprovalNotificationRepository extends JpaRepository<TopicApprovalNotification, String> {
    Optional<TopicApprovalNotification> findTopicApprovalNotificationByTopicStudent_Id(String id);
    Optional<TopicApprovalNotification> findByTopicStudent_Student_Ci(String id);
    List<TopicApprovalNotification> findTopicApprovalNotificationsByTopicStudent_Topic_Career_Id(String career);
}
