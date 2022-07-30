package com.unibe.titulation.services;

import com.unibe.titulation.dtos.AntiPlagiarismLinkAndReaderDesignation;
import com.unibe.titulation.dtos.PaymentTableDto;
import com.unibe.titulation.dtos.TopicStudentTableDto;
import com.unibe.titulation.entities.Topic;
import com.unibe.titulation.entities.TopicDenunciation;
import com.unibe.titulation.entities.TopicStudent;
import com.unibe.titulation.repositories.TopicDenunciationRepository;
import com.unibe.titulation.repositories.TopicProposalRepository;
import com.unibe.titulation.repositories.TopicRepository;
import com.unibe.titulation.repositories.TopicStudentRepository;
import com.unibe.titulation.security.entity.User;
import com.unibe.titulation.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TopicStudentService {

    private final TopicStudentRepository topicStudentRepository;
    private final TopicRepository topicRepository;
    private final UserService userService;
    private final TopicDenunciationRepository topicDenunciationRepository;
    private final TopicService topicService;
    private final TopicProposalRepository topicProposalRepository;
    private final DesignationTTService designationTTService;

    @Autowired
    public TopicStudentService(TopicStudentRepository topicStudentRepository,
                               TopicProposalRepository topicProposalRepository,
                               TopicDenunciationRepository topicDenunciationRepository, TopicRepository topicRepository,
                               UserService userService, TopicService topicService,
                               @Lazy DesignationTTService designationTTService) {
        this.topicStudentRepository = topicStudentRepository;
        this.topicRepository = topicRepository;
        this.userService = userService;
        this.topicDenunciationRepository = topicDenunciationRepository;
        this.topicService = topicService;
        this.topicProposalRepository = topicProposalRepository;
        this.designationTTService = designationTTService;
    }

    public List<TopicStudentTableDto> findTopicStudentsByStudent_Career(String career, String topicStatus) {
        List<TopicStudent> topicStudentList = this.topicStudentRepository
                .findTopicStudentsByStudent_Career_IdAndTopic_TopicStatus(career, topicStatus);
        List<TopicStudentTableDto> topicStudentDtoList = new ArrayList<>();
        for (TopicStudent topicStudent : topicStudentList) {
            TopicStudentTableDto topicStudentDto = new TopicStudentTableDto(topicStudent.getId(),
                    topicStudent.getTopic().getName(), topicStudent.getTopic().getArticulation(),
                    topicStudent.getPaymentDenunciation(), topicStudent.getStudent().getCi(),
                    topicStudent.getStudent().getCareer().getName(),
                    topicStudent.getAssignedDate(), topicStudent.getTopicEvaluation(), topicStudent.getTopic());
            topicStudentDtoList.add(topicStudentDto);
        }
        return topicStudentDtoList;
    }

    public Optional<TopicStudent> findTopicStudentByStudent_Ci(String ci, String career) {
        return this.topicStudentRepository.findTopicStudentByStudent_CiAndTopic_TopicStatusAndStudent_Career_Name(ci,
                "En ejecución", career);
    }

    public List<TopicStudent> findByTopicIdIn(List<String> topicId) {
        return this.topicStudentRepository.findByTopic_IdIn(topicId);
    }

    public Optional<TopicStudent> findTopicStudentByStudent_Id(String id) {
        // SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication().getPrincipal()
        return this.topicStudentRepository.findTopicStudentByStudent_Id(id);
    }

    public List<TopicStudentTableDto> findTopicStudentByStatus(String topicStatus) {
        List<TopicStudent> topicStudentList = this.topicStudentRepository
                .findTopicStudentsByTopic_TopicStatus(topicStatus);
        List<TopicStudentTableDto> topicStudentDtoList = new ArrayList<>();
        for (TopicStudent topicStudent : topicStudentList) {
            TopicStudentTableDto topicStudentDto = new TopicStudentTableDto(topicStudent.getId(),
                    topicStudent.getTopic().getName(), topicStudent.getTopic().getArticulation(),
                    topicStudent.getPaymentDenunciation(), topicStudent.getStudent().getCi(),
                    topicStudent.getStudent().getCareer().getName(),
                    topicStudent.getAssignedDate(), topicStudent.getTopicEvaluation(), topicStudent.getTopic());
            topicStudentDtoList.add(topicStudentDto);
        }
        return topicStudentDtoList;
    }

    public List<PaymentTableDto> findTopicStudentToPayment() {
        List<TopicStudent> topicStudentList = this.topicStudentRepository
                .findTopicStudentsByTopic_TopicStatus("Seleccionado");
        List<PaymentTableDto> paymentTableDtoList = new ArrayList<>();
        for (TopicStudent topicStudent : topicStudentList) {
            String name = topicStudent.getStudent().getName() + " " +
                    topicStudent.getStudent().getSecondName() + " " + topicStudent.getStudent().getLastName()
                    + " " + topicStudent.getStudent().getSecondLastName();
            PaymentTableDto paymentTableDto = new PaymentTableDto(name, topicStudent.getStudent().getCi(),
                    topicStudent.getStudent().getCareer().getName(), topicStudent.getPaymentDenunciation());
            paymentTableDtoList.add(paymentTableDto);
        }
        return paymentTableDtoList;
    }


    public Optional<TopicStudent> findTopicStudentById(String id) {
        return this.topicStudentRepository.findById(id);
    }

    public List<TopicStudent> findTopicStudentsByTopic_Id(String topicId) {
        return this.topicStudentRepository.findTopicStudentsByTopic_Id(topicId);
    }

    public List<AntiPlagiarismLinkAndReaderDesignation> getTopicStudentsByUserCi(String userCi) {
        User user = this.userService.getByCi(userCi).get();
        List<TopicStudent> topicStudent = this.topicStudentRepository.findTopicStudentsByTopic_Career_IdAndTopicEvaluationNot(user.getCareer().getId(), "No presentado");
        List<TopicStudent> filteredArray = topicStudent.stream().filter(data -> !data.getTopic().isHasReader()).collect(Collectors.toList());
        List<AntiPlagiarismLinkAndReaderDesignation> arrayWithoutDuplicates = new ArrayList<>();
        for (int i = 0; i < filteredArray.size(); i++) {
            if (this.designationTTService.existsByTopicStudent_Topic_Id(filteredArray.get(i).getTopic().getId())) {
                String topicId = filteredArray.get(i).getTopic().getId();
                String studentsName = filteredArray.get(i).getStudent().getName() + ' ' + topicStudent.get(i).getStudent().getLastName();
                String topicName = filteredArray.get(i).getTopic().getName();

                AntiPlagiarismLinkAndReaderDesignation temporalArray = new AntiPlagiarismLinkAndReaderDesignation();
                temporalArray.setTopicName(topicName);
                temporalArray.setTopicId(topicId);
                List<String> names = new ArrayList<>();
                names.add(studentsName);
                temporalArray.setStudent(names);

                if (arrayWithoutDuplicates.size() == 0 || !arrayWithoutDuplicates.get(i - 1).getTopicId().equals(topicId)) {
                    arrayWithoutDuplicates.add(temporalArray);
                } else {
                    arrayWithoutDuplicates.get(i - 1).getStudent().add(studentsName);
                }
            }
        }
        return arrayWithoutDuplicates;
    }

    public boolean checkPaymentStatusPairTheme(String topicId) {
        List<TopicStudent> topicStudentList = this.topicStudentRepository
                .findTopicStudentsByTopic_Id(topicId);
        List<TopicDenunciation> topicDenunciationList = this.topicDenunciationRepository.findTopicDenunciationByTopicStudent_Topic_Id(topicId);
        if (topicStudentList.size() == 1)
            return true;
        if (topicDenunciationList.size() == 2)
            return true;
        return false;

    }

    public boolean assignedTopic(TopicStudent topicStudent) {
        List<TopicStudent> topicStudentList = this.topicStudentRepository
                .findTopicStudentsByTopic_Id(topicStudent.getTopic().getId());
        if (topicStudentList.size() >= 2)
            return false;
        Optional<Topic> topic = this.topicRepository.findById(topicStudent.getTopic().getId());
        if (!topic.isPresent())
            return false;
        Topic topicToSave = topic.get();
        topicStudent.setTopic(topicToSave);
        if (topicStudentList.size() == 1)
            topicToSave.setTwoStudents(true);
        this.topicStudentRepository.save(topicStudent);
        String topicId = topic.get().getId();
        topicToSave.setTopicStatus("Seleccionado");
        topicToSave.setTwoStudents(topicStudent.getTopic().isTwoStudents());
        this.topicService.updateTopicById(topicId, topicStudent.getTopic());
        return true;

    }

    public boolean assignedTopicPair(List<TopicStudent> topicStudent) {
        Optional<Topic> topic = this.topicRepository.findById(topicStudent.get(0).getTopic().getId());
        if (!topic.isPresent())
            return false;
        Topic topicToSave = topic.get();
        for (int i = 0; i < topicStudent.size(); i++) {
            topicStudent.get(i).setTopic(topicToSave);
            this.topicStudentRepository.save(topicStudent.get(i));
        }
        topicToSave.setTwoStudents(true);
        String topicId = topic.get().getId();
        topicToSave.setTopicStatus("Seleccionado");
        topicToSave.setTwoStudents(topicStudent.get(0).getTopic().isTwoStudents());
        this.topicService.updateTopicById(topicId, topicStudent.get(0).getTopic());
        return true;

    }

    public boolean deleteTopicStudent(String id) {
        Optional<TopicStudent> topicStudent = this.findTopicStudentById(id);
        if (!topicStudent.isPresent())
            return false;
        if (Objects.equals(topicStudent.get().getPaymentDenunciation(), "Pagado"))
            return false;
        this.topicStudentRepository.deleteById(id);
        Topic updateTopic = topicStudent.get().getTopic();

        List<TopicStudent> topicStudentList = this.topicStudentRepository
                .findTopicStudentsByTopic_Id(updateTopic.getId());
        if (topicStudentList.isEmpty()) {
            updateTopic.setTwoStudents(false);
            updateTopic.setTopicStatus("Disponible");
            this.topicService.updateTopicById(updateTopic.getId(), updateTopic);
        } else {
            updateTopic.setTwoStudents(false);
            updateTopic.setTopicStatus("Disponible");
        }
        return true;
    }

    public boolean updatePaymentDenunciation(String ci, String paymentDenunciation) {
        String topicStatus = "Seleccionado";
        Optional<TopicStudent> topicStudentOptional = this.topicStudentRepository
                .findTopicStudentByStudent_CiAndTopic_TopicStatus(ci, topicStatus);
        if (!topicStudentOptional.isPresent())
            return false;
        TopicStudent topicStudent1 = topicStudentOptional.get();
        topicStudent1.setPaymentDenunciation(paymentDenunciation);
        topicStudentRepository.save(topicStudent1);
        return true;
    }

    public boolean updateTopicEvaluation(String id, TopicStudent topicStudent) {
        Optional<TopicStudent> topicStudentOptional = this.findTopicStudentById(id);
        TopicStudent topicStudent1 = topicStudentOptional.get();
        topicStudent1.setTopicEvaluation(topicStudent.getTopicEvaluation());
        topicStudentRepository.save(topicStudent1);
        return true;

    }

    public void updateAntiPlagiarismLetterSentStatus(String topicId) {
        List<TopicStudent> topicStudents = this.topicStudentRepository.findTopicStudentsByTopic_Id(topicId);
        for (int i = 0; i < topicStudents.size(); i++) {
            if (!topicStudents.get(i).isAntiPlagiarismLetterSent()) {
                topicStudents.get(i).setAntiPlagiarismLetterSent(true);
                this.topicStudentRepository.save(topicStudents.get(i));
            }
        }
    }

    public void updateThesisSentStatus(String studentId) {
        TopicStudent topicStudentToSave = this.findTopicStudentByStudent_Id(studentId).get();
        List<TopicStudent> topicStudentList = this.topicStudentRepository.findTopicStudentsByTopic_Id(topicStudentToSave.getTopic().getId());
        for (int i = 0; i < topicStudentList.size(); i++) {
            if (!topicStudentList.get(i).isThesisSent()) {
                topicStudentList.get(i).setThesisSent(true);
                this.topicStudentRepository.save(topicStudentList.get(i));
            }
        }
    }

    public boolean createTopicDenunciation(TopicDenunciation topicDenunciation) {
        Optional<TopicStudent> topicStudent = this.topicStudentRepository
                .findById(topicDenunciation.getTopicStudent().getId());
        if (!topicStudent.isPresent())
            return false;
        if (topicStudent.get().getPaymentDenunciation().equals("Pagado")) {
            this.topicService.updateTopicStatus(topicStudent.get().getTopic().getId(), "Seleccionado");
            this.topicDenunciationRepository.save(topicDenunciation);
            return true;
        }
        return false;
    }


    public List<TopicStudent> getStudentByCareerIdAndTopicId(String careerId, String topicId) {
        return this.topicStudentRepository.findByStudent_Career_IdAndTopic_Id(careerId, topicId);
    }

    public List<TopicStudent> getTopicStudentByStudentsEmails(List<String> emails){
        return this.topicStudentRepository.findByStudent_EmailIn(emails);
    }
}
