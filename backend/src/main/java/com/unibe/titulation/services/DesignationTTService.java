package com.unibe.titulation.services;

import com.unibe.titulation.dtos.AntiPlagiarismLinkAndReaderDesignation;
import com.unibe.titulation.dtos.DesignationTT_TableDto;
import com.unibe.titulation.emailpassword.service.EmailService;
import com.unibe.titulation.entities.DesignationTT;
import com.unibe.titulation.entities.TopicStudent;
import com.unibe.titulation.repositories.DesignationTTRepository;
import com.unibe.titulation.security.entity.User;
import com.unibe.titulation.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.internet.AddressException;
import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class DesignationTTService {

    DesignationTTRepository designationTTRepository;
    TopicStudentService topicStudentService;
    UserService userService;
    private final EmailService emailService;
    @Autowired
    public DesignationTTService(DesignationTTRepository designationTTRepository,
                                TopicStudentService topicStudentService, UserService userService,
                                EmailService emailService) {
        this.designationTTRepository = designationTTRepository;
        this.topicStudentService = topicStudentService;
        this.userService = userService;
        this.emailService = emailService;
    }

    public List<DesignationTT> getAllDesignationByUserCareer(String userCi) {
        User user = this.userService.getByCi(userCi).get();
        return this.designationTTRepository.findDesignationTTByTopicStudent_Topic_Career_Id(user.getCareer().getId());
    }

    public List<DesignationTT_TableDto> getAllDesignationDtoByUserCareer(String userCi) {
        List<DesignationTT_TableDto> designationTTTableDto = new ArrayList<>();
        List<DesignationTT> designationTTList = this.getAllDesignationByUserCareer(userCi);
        for (int i = 0; i < designationTTList.size(); i++) {
            List<TopicStudent> topicStudents = this.topicStudentService.findTopicStudentsByTopic_Id(designationTTList.get(i).getTopicStudent().getTopic().getId());
            DesignationTT_TableDto designationTTTableDto1 = new DesignationTT_TableDto();
            designationTTTableDto1.setDate(designationTTList.get(i).getDate());
            designationTTTableDto1.setTeacher(designationTTList.get(i).getTeacher());
            designationTTTableDto1.setTopicStudent(topicStudents);
            designationTTTableDto.add(designationTTTableDto1);
        }
        return designationTTTableDto;
    }

    public Optional<DesignationTT> getDesignationTTById(String designationTT_Id) {
        return this.designationTTRepository.findById(designationTT_Id);
    }

    public void createDesignationTT(DesignationTT designationTT) throws AddressException {
        String tutorEmail = designationTT.getTeacher().getEmail();
        List<String> studentEmail = new ArrayList<>();
        String emailSubject = "Designación de tutor trabajo de titulación";
        String emailMessage = "Estimado (a) estudiante, se le ha asignado un tutor a su trabajo de titulación. " +
                "Para más información revise el sistema de titulación en el apartado correspondiente a Tutorías. ";
        String emailMessageToTeacher = "Estimado (a) docente, usted ha sido asignado como tutor de un trabajo de titulación. " +
                "Para más información, revise el sistema de titulación en el apartado de tutorías.";
        studentEmail.add(designationTT.getTopicStudent().getStudent().getEmail());
        this.emailService.sendNotificationToStudent(studentEmail, emailMessage, emailSubject);
        this.emailService.sendNotificationToTeacher(tutorEmail, emailMessageToTeacher, emailSubject);
        this.designationTTRepository.save(designationTT);
    }

    public void createDesignationTTPairStudents(List<DesignationTT> designationTTList) throws AddressException {
        String tutorEmail = designationTTList.get(0).getTeacher().getEmail();
        List<String> studentEmail = new ArrayList<>();
        String emailSubject = "Designación de tutor trabajo de titulación";
        String emailMessage = "Estimado (a) estudiante, se le ha asignado un tutor a su trabajo de titulación. " +
                "Para más información revise el sistema de titulación en el apartado correspondiente a Tutorías. ";
        String emailMessageToTeacher = "Estimado (a) docente, usted ha sido asignado como tutor de un trabajo de titulación. " +
                "Para más información, revise el sistema de titulación en el apartado de tutorías.";

        for (DesignationTT designationTT : designationTTList) {
            this.designationTTRepository.save(designationTT);
        }
        studentEmail.add("saulmme12@gmail.com");
        studentEmail.add("futbolmate@hotmail.com");
        this.emailService.sendNotificationToStudent(studentEmail, emailMessage, emailSubject);
        this.emailService.sendNotificationToTeacher(tutorEmail, emailMessageToTeacher, emailSubject);
    }

    public Optional<DesignationTT> findByTopicStudent_Id(String topicStudentId) {
        return this.designationTTRepository.findByTopicStudent_Id(topicStudentId);
    }


    public void deleteDesignationTTById(String designationTT_Id) {
        this.designationTTRepository.deleteById(designationTT_Id);
    }

    public List<DesignationTT_TableDto> findByTeacherCi(String ci) {
        List<DesignationTT_TableDto> designationTTTableDto = new ArrayList<>();
        List<DesignationTT> designationTTList = designationTTRepository.findByTeacher_Ci(ci);
        for (DesignationTT designationTT : designationTTList) {
            List<TopicStudent> topicStudents = this.topicStudentService.findTopicStudentsByTopic_Id(designationTT.getTopicStudent().getTopic().getId());
            DesignationTT_TableDto designationTTTableDto1 = new DesignationTT_TableDto();
            designationTTTableDto1.setDate(designationTT.getDate());
            designationTTTableDto1.setTeacher(designationTT.getTeacher());
            designationTTTableDto1.setTopicStudent(topicStudents);
            designationTTTableDto.add(designationTTTableDto1);
        }
        return designationTTTableDto;

    }

    public List<DesignationTT> findDesignationTTByTopicStudent_Topic_Id(String topicId) {
        return this.designationTTRepository.findDesignationTTByTopicStudent_Topic_Id(topicId);
    }

    public boolean existsByTopicStudent_Id(String topicStudentId) {
        return this.designationTTRepository.existsByTopicStudent_Id(topicStudentId);
    }

    public boolean existsByTopicStudent_Topic_Id(String topicId) {
        return this.designationTTRepository.existsByTopicStudent_Topic_Id(topicId);
    }

    public List<AntiPlagiarismLinkAndReaderDesignation> getDesignationsTTByTeacherCi(String teacher_Ci) {
        List<DesignationTT> designationTTList = this.designationTTRepository.findByTeacher_Ci(teacher_Ci);
        List<AntiPlagiarismLinkAndReaderDesignation> filteredArray = new ArrayList<>();
        for (DesignationTT designationTT : designationTTList) {
            String topicId = designationTT.getTopicStudent().getTopic().getId();
            TopicStudent topicStudent = designationTT.getTopicStudent();
            String studentsName = designationTT.getTopicStudent().getStudent().getName() + ' ' + designationTT.getTopicStudent().getStudent().getLastName();
            String email = designationTT.getTopicStudent().getStudent().getEmail();
            AntiPlagiarismLinkAndReaderDesignation temporalArray = new AntiPlagiarismLinkAndReaderDesignation();
            temporalArray.setTopicName(topicStudent.getTopic().getName());
            temporalArray.setTopicId(topicStudent.getTopic().getId());
            temporalArray.setLetterSent(topicStudent.isAntiPlagiarismLetterSent());

            List<String> names = new ArrayList<>();
            List<String> emails = new ArrayList<>();
            names.add(studentsName);
            emails.add(email);
            temporalArray.setStudent(names);
            temporalArray.setEmail(emails);

            if (filteredArray.size() == 0 || !filteredArray.get(filteredArray.size() - 1).getTopicId().equals(topicId)) {
                filteredArray.add(temporalArray);
            } else {
                filteredArray.get(filteredArray.size() - 1).getStudent().add(studentsName);
                filteredArray.get(filteredArray.size() - 1).getEmail().add(email);
            }
        }
        return filteredArray;
    }

}
