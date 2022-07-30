package com.unibe.titulation.services;

import com.unibe.titulation.emailpassword.service.EmailService;
import com.unibe.titulation.entities.DesignationTT;
import com.unibe.titulation.entities.Reader;
import com.unibe.titulation.entities.TopicStudent;
import com.unibe.titulation.repositories.CalendarDetailRepository;
import com.unibe.titulation.entities.CalendarDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.internet.AddressException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CalendarDetailService {

    private final ReaderService readerService;
    private final DesignationTTService designationTTService;
    private final TopicStudentService topicStudentService;
    private final CalendarDetailRepository calendarDetailRepository;
    private final EmailService emailService;
    @Autowired
    public CalendarDetailService(ReaderService readerService, DesignationTTService designationTTService,
                                 TopicStudentService topicStudentService,
                                 CalendarDetailRepository calendarDetailRepository, EmailService emailService) {
        this.readerService = readerService;
        this.designationTTService = designationTTService;
        this.topicStudentService = topicStudentService;
        this.calendarDetailRepository = calendarDetailRepository;
        this.emailService = emailService;
    }

    public void saveCalendar(List<CalendarDetail> calendarDetails) {
        for (CalendarDetail calendarDetail : calendarDetails) {
            this.calendarDetailRepository.save(calendarDetail);
        }
    }

    public void updateCalendarDetail(CalendarDetail calendarDetail) {
        CalendarDetail calendarDetailFound = this.calendarDetailRepository.findById(calendarDetail.getId()).get();
        List<CalendarDetail> calendarDetailList = this.calendarDetailRepository.findByFinalDegreeCalendar_Id(calendarDetail.getFinalDegreeCalendar().getId());
        calendarDetailFound.setTribunalBoss(calendarDetail.getTribunalBoss());
        calendarDetailFound.setDate(calendarDetail.getDate());
        calendarDetailFound.setHour(calendarDetail.getHour());
        calendarDetailFound.setSecretary(calendarDetail.getSecretary());
        for (CalendarDetail detail : calendarDetailList) {
            detail.setSecretary(calendarDetail.getSecretary());
            this.calendarDetailRepository.save(detail);
        }
        this.calendarDetailRepository.save(calendarDetailFound);

    }

    public List<CalendarDetail> getCalendarInfo(String careerId) {
        List<Reader> reader = this.readerService.getReaderByStatusCalendarCreatedAndCareerId(careerId);
        List<CalendarDetail> calendarDetails = new ArrayList<>();
        List<String> topicId = new ArrayList<>();
        for (Reader value : reader) {
            topicId.add(value.getTopic().getId());
        }
        List<TopicStudent> topicStudents = this.topicStudentService.findByTopicIdIn(topicId);
        Optional<Reader> readers;
        for (int i = 0, arraySize = topicStudents.size(); i < arraySize; i++) {
            Optional<DesignationTT> tutor = this.designationTTService.findByTopicStudent_Id(topicStudents.get(i).getId());
            readers = this.readerService.getReaderByTopicId(topicStudents.get(i).getTopic().getId());
            CalendarDetail calendarDetail = new CalendarDetail();
            calendarDetail.setId(Integer.toString(i));
            calendarDetail.setStudent(topicStudents.get(i).getStudent());
            calendarDetail.setReader(readers.get().getReader());
            calendarDetail.setTutor(tutor.get().getTeacher());
            calendarDetails.add(calendarDetail);
        }
        return calendarDetails;
    }

    public List<CalendarDetail> getByFinalDegreeCalendarId(String id) {
        return this.calendarDetailRepository.findByFinalDegreeCalendar_Id(id);
    }

    public boolean existsCalendarDetail(String finalDegreeCalendarId) {
        return this.calendarDetailRepository.existsByFinalDegreeCalendar_Id(finalDegreeCalendarId);
    }

    public void generateFinalCalendar(List<String> studentEmails) throws AddressException {
        List<TopicStudent> topicStudents = this.topicStudentService.getTopicStudentByStudentsEmails(studentEmails);
        List<String> topicIds = topicStudents.stream().map(data-> data.getTopic().getId()).distinct().collect(Collectors.toList());
        List<Reader> readers = this.readerService.getReadersByTopicIdList(topicIds);
        this.emailService.sendFinalCalendarNotificationToStudents(studentEmails);
        for (Reader reader : readers) {
            reader.setState("En Defensa");
            this.readerService.saveReaderCalendar(reader);
        }
    }

    public boolean getCalendarStatus(List<String> studentEmails){
        List<TopicStudent> topicStudents = this.topicStudentService.getTopicStudentByStudentsEmails(studentEmails);
        List<String> topicIds = topicStudents.stream().map(data-> data.getTopic().getId()).distinct().collect(Collectors.toList());
        List<Reader> readers = this.readerService.getReadersByTopicIdList(topicIds);
        return readers.get(0).getState().equals("En Defensa");
    }
}
