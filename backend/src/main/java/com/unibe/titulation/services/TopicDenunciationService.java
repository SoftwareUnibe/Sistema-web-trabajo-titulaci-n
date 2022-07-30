package com.unibe.titulation.services;

import com.unibe.titulation.entities.TopicDenunciation;
import com.unibe.titulation.repositories.TopicDenunciationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.List;

@Service
@Transactional
public class TopicDenunciationService {

    private final TopicDenunciationRepository topicDenunciationRepository;
    private final TopicStudentService topicStudentService;

    @Autowired
    public TopicDenunciationService(TopicDenunciationRepository topicDenunciationRepository,
            TopicStudentService topicStudentService) {
        this.topicDenunciationRepository = topicDenunciationRepository;
        this.topicStudentService = topicStudentService;
    }

    public Optional<TopicDenunciation> findTopicDenunciationByTopicStudentId(String id) {
        return this.topicDenunciationRepository.findTopicDenunciationByTopicStudent_Id(id);
    }

    public List<TopicDenunciation> findTopicDenunciationByTopicId(String topicId) {
        return this.topicDenunciationRepository.findTopicDenunciationByTopicStudent_Topic_Id(topicId);
    }

    public Optional<TopicDenunciation> findTopicDenunciationByStudentId(String id) {

        return this.topicDenunciationRepository.findTopicDenunciationByTopicStudent_Student_Id(id);
    }
    public Optional<TopicDenunciation> findTopicDenunciationById(String id) {

        return this.topicDenunciationRepository.findById(id);
    }

    public void updateTopicDenunciation( TopicDenunciation topicDenunciation) {
        this.topicDenunciationRepository.save(topicDenunciation);
    }

    public boolean deleteTopicDenunciation(String id) {
        Optional<TopicDenunciation> topicDenunciation = this.topicDenunciationRepository.findById(id);
        String student = topicDenunciation.get().getTopicStudent().getStudent().getCi();
        if (!topicDenunciation.isPresent())
            return false;
        topicDenunciationRepository.deleteById(id);
        this.topicStudentService.updatePaymentDenunciation(student, "No pagado");
        return true;
    }
}
