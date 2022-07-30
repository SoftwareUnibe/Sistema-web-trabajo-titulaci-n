package com.unibe.titulation.repositories;

import com.unibe.titulation.entities.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, String> {

	List<Topic> findTopicsByTopicStatusAndCareer_Id(String topicStatus, String career);
	List<Topic> findTopicsByTopicStatus(String topicStatus);
	long countByCareer_Id(String careerId);
}
