package com.unibe.titulation.services;

import com.unibe.titulation.dtos.NotificationTableDto;
import com.unibe.titulation.entities.TopicApprovalNotification;
import com.unibe.titulation.entities.TopicProposal;
import com.unibe.titulation.repositories.ApprovalNotificationRepository;
import com.unibe.titulation.repositories.TopicProposalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ApprovalNotificationService {
    private final ApprovalNotificationRepository approvalNotificationRepository;
    private final TopicProposalRepository topicProposalRepository;
    private final TopicService topicService;
    private final TopicStudentService topicStudentService;

    @Autowired
    public ApprovalNotificationService(ApprovalNotificationRepository approvalNotificationRepository,
                                       TopicProposalRepository topicProposalRepository,
                                       TopicService topicService, TopicStudentService topicStudentService) {
        this.approvalNotificationRepository = approvalNotificationRepository;
        this.topicProposalRepository = topicProposalRepository;
        this.topicService = topicService;
        this.topicStudentService = topicStudentService;
    }


    public List<NotificationTableDto> findApprovalNotificationsByCareer(String career) {
        List<TopicApprovalNotification> topicApprovalNotificationList = this.approvalNotificationRepository.findTopicApprovalNotificationsByTopicStudent_Topic_Career_Id(career);
        List<NotificationTableDto> notificationTableDtoArrayList = new ArrayList<>();
        for (TopicApprovalNotification topicApprovalNotification :
                topicApprovalNotificationList) {
            String name = topicApprovalNotification.getTopicStudent().getStudent().getName() + " " +
                    topicApprovalNotification.getTopicStudent().getStudent().getSecondName() + " " + topicApprovalNotification.getTopicStudent().getStudent().getLastName()
                    + " " + topicApprovalNotification.getTopicStudent().getStudent().getSecondLastName();
            NotificationTableDto notificationTableDto = new NotificationTableDto(topicApprovalNotification.getId(),
                    topicApprovalNotification.getTopicStudent().getStudent().getCi(),
                    name,
                    topicApprovalNotification.getTopicStudent().getStudent().getCareer().getName(),
                    topicApprovalNotification.getTopicStudent().getTopic().getName(),
                    topicApprovalNotification.getTopicStudent().getTopic().getTopicStatus(),
                    topicApprovalNotification.getTopicStudent().getTopicEvaluation(),
                    topicApprovalNotification.getObservations(),
                    topicApprovalNotification.getTopicStudent());
            notificationTableDtoArrayList.add(notificationTableDto);
        }
        return notificationTableDtoArrayList;
    }

    public Optional<TopicApprovalNotification> findApprovalNotificationByTopicStudent_Id(String id) {
        return this.approvalNotificationRepository.findTopicApprovalNotificationByTopicStudent_Id(id);
    }

    public Optional<TopicApprovalNotification> findApprovalNotificationByStudentId() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String ci = userDetails.getUsername();
        return this.approvalNotificationRepository.findByTopicStudent_Student_Ci(ci);
    }

    public boolean createApprovalNotification(TopicApprovalNotification topicApprovalNotification) {
        Optional<TopicProposal> topicProposal = this.topicProposalRepository.findTopicProposalByTopicStudent_Id(topicApprovalNotification.getTopicStudent().getId());
        if (!topicProposal.isPresent())
            return false;
        this.approvalNotificationRepository.save(topicApprovalNotification);
        this.topicService.updateTopicStatus(topicApprovalNotification.getTopicStudent().getTopic().getId(), "En ejecución");
        this.topicStudentService.updateTopicEvaluation(topicApprovalNotification.getTopicStudent().getId(), topicApprovalNotification.getTopicStudent());
        return true;
    }

    public boolean createApprovalNotificationPairStudents(List<TopicApprovalNotification> topicApprovalNotification) {
        for (int i = 0; i < topicApprovalNotification.size(); i++) {
            this.approvalNotificationRepository.save(topicApprovalNotification.get(i));
            this.topicStudentService.updateTopicEvaluation(topicApprovalNotification.get(i).getTopicStudent().getId(), topicApprovalNotification.get(i).getTopicStudent());
        }
        this.topicService.updateTopicStatus(topicApprovalNotification.get(0).getTopicStudent().getTopic().getId(), "En ejecución");
        return true;
    }

    public boolean updateApprovalNotification(String id, TopicApprovalNotification topicApprovalNotification) {
        Optional<TopicApprovalNotification> approvalNotificationOptional =
                this.approvalNotificationRepository.findById(id);
        if (!approvalNotificationOptional.isPresent()) return false;
        TopicApprovalNotification topicApprovalNotification1 = approvalNotificationOptional.get();
        topicApprovalNotification1.setMeetingDate(topicApprovalNotification.getMeetingDate());
        topicApprovalNotification1.setObservations(topicApprovalNotification.getObservations());
        topicApprovalNotification1.setMeetingNumber(topicApprovalNotification.getMeetingNumber());
        this.approvalNotificationRepository.save(topicApprovalNotification1);
        return true;
    }

    public boolean deleteApprovalNotification(String id) {
        Optional<TopicApprovalNotification> approvalNotificationOptional =
                this.approvalNotificationRepository.findById(id);
        if (!approvalNotificationOptional.isPresent()) return false;
        this.approvalNotificationRepository.deleteById(id);
        return true;
    }


}
