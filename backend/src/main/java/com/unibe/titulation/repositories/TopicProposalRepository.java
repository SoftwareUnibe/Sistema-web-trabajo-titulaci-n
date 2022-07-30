package com.unibe.titulation.repositories;

import com.unibe.titulation.entities.TopicProposal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;
public interface TopicProposalRepository extends JpaRepository<TopicProposal, String> {
    List<TopicProposal> findTopicProposalByTopicStudent_Topic_Id(String id);
    Optional<TopicProposal> findTopicProposalByTopicStudent_Student_Ci(String id);
    Optional<TopicProposal> findTopicProposalByTopicStudent_Id(String id);
    List<TopicProposal> findTopicProposalByTopicStudent_Topic_Career_Id(String id);
}
