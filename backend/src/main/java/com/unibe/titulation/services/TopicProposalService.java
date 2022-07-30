package com.unibe.titulation.services;

import com.unibe.titulation.dtos.TopicProposalTableDto;
import com.unibe.titulation.entities.TopicDenunciation;
import com.unibe.titulation.entities.TopicProposal;
import com.unibe.titulation.entities.TopicStudent;
import com.unibe.titulation.repositories.TopicProposalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TopicProposalService {
    private final TopicProposalRepository topicProposalRepository;
    private final TopicDenunciationService topicDenunciationService;
    private final TopicStudentService topicStudentService;
    private final DesignationTTService designationTTService;

    @Autowired
    public TopicProposalService(TopicProposalRepository topicProposalRepository,
                                TopicDenunciationService topicDenunciationService, TopicStudentService topicStudentService,
                                DesignationTTService designationTTService) {
        this.topicProposalRepository = topicProposalRepository;
        this.topicDenunciationService = topicDenunciationService;
        this.topicStudentService = topicStudentService;
        this.designationTTService = designationTTService;
    }

    public List<TopicProposal> findAllTopicProposals() {
        return this.topicProposalRepository.findAll();
    }

    public List<TopicProposal> findTopicProposalByTopicId(String id) {
        return this.topicProposalRepository.findTopicProposalByTopicStudent_Topic_Id(id);
    }

    public Optional<TopicProposal> findTopicProposalById(String id) {
        return this.topicProposalRepository.findById(id);
    }

    public Optional<TopicProposal> findTopicProposalByStudentId(String student_Id) {
        return this.topicProposalRepository.findTopicProposalByTopicStudent_Student_Ci(student_Id);
    }

    public List<TopicProposalTableDto> findTopicProposalByCareer(String careerId, String status) {
        List<TopicProposalTableDto> topicProposalTableDto = new ArrayList<TopicProposalTableDto>();
        List<TopicProposal> topicProposal = this.topicProposalRepository.findTopicProposalByTopicStudent_Topic_Career_Id(careerId);
        for (int i = 0; i < topicProposal.size(); i++) {
            if (topicProposal.get(i).getTopicStudent().getTopic().getTopicStatus().equals(status)) {
                List<TopicStudent> topicStudents = this.topicStudentService.findTopicStudentsByTopic_Id(topicProposal.get(i).getTopicStudent().getTopic().getId());
                TopicProposalTableDto topicProposalTableDto1 = new TopicProposalTableDto();
                topicProposalTableDto1.setId(topicProposal.get(i).getId());
                topicProposalTableDto1.setTopicDescription(topicProposal.get(i).getTopicDescription());
                topicProposalTableDto1.setObjectiveGeneral(topicProposal.get(i).getObjectiveGeneral());
                topicProposalTableDto1.setObjectivesSpecific(topicProposal.get(i).getObjectivesSpecific());
                topicProposalTableDto1.setStudyJustification(topicProposal.get(i).getStudyJustification());
                topicProposalTableDto1.setTopicStudent(topicStudents);
                if (!this.designationTTService.existsByTopicStudent_Id(topicProposal.get(i).getTopicStudent().getId()))
                    topicProposalTableDto.add(topicProposalTableDto1);
            }
        }
        return topicProposalTableDto;
    }

    public TopicProposalTableDto findOneTopicProposalById(String proposalId) {
        TopicProposalTableDto topicProposalTableDto = new TopicProposalTableDto();
        Optional<TopicProposal> topicProposal = this.findTopicProposalById(proposalId);
        String topicId = topicProposal.get().getTopicStudent().getTopic().getId();
        List<TopicStudent> topicStudents = this.topicStudentService.findTopicStudentsByTopic_Id(topicId);
        for (int i = 0; i < topicStudents.size(); i++) {
            topicProposalTableDto.setId(topicProposal.get().getId());
            topicProposalTableDto.setTopicDescription(topicProposal.get().getTopicDescription());
            topicProposalTableDto.setObjectiveGeneral(topicProposal.get().getObjectiveGeneral());
            topicProposalTableDto.setObjectivesSpecific(topicProposal.get().getObjectivesSpecific());
            topicProposalTableDto.setStudyJustification(topicProposal.get().getStudyJustification());
            topicProposalTableDto.setTopicStudent(topicStudents);
        }
        return topicProposalTableDto;

    }


    public boolean createTopicProposal(TopicProposal topicProposal) {
        Optional<TopicDenunciation> topicDenunciationOptional = this.topicDenunciationService.findTopicDenunciationByTopicStudentId(topicProposal.getTopicStudent().getId());
        if (!topicDenunciationOptional.isPresent())
            return false;
        this.topicProposalRepository.save(topicProposal);
        return true;
    }


    public boolean updateTopicProposal(String id, TopicProposal topicProposal) {
        Optional<TopicProposal> topicProposalOptional = this.topicProposalRepository.findById(id);
        if (!topicProposalOptional.isPresent()) return false;
        TopicProposal topicProposal1 = topicProposalOptional.get();
        topicProposal1.setTopicDescription(topicProposal.getTopicDescription());
        topicProposal1.setObjectiveGeneral(topicProposal.getObjectiveGeneral());
        topicProposal1.setObjectivesSpecific(topicProposal.getObjectivesSpecific());
        topicProposal1.setStudyJustification(topicProposal.getStudyJustification());
        topicProposal1.setScope(topicProposal.getScope());
        topicProposal1.setBibliographicReferences(topicProposal.getBibliographicReferences());
        this.topicProposalRepository.save(topicProposal1);
        return true;
    }

    public boolean deleteTopicProposal(String id) {
        Optional<TopicProposal> topicProposalOptional = this.topicProposalRepository.findById(id);
        if (!topicProposalOptional.isPresent()) return false;
        this.topicProposalRepository.deleteById(id);
        return true;
    }
}
