package com.unibe.titulation.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.mail.internet.AddressException;
import javax.transaction.Transactional;

import com.unibe.titulation.emailpassword.service.EmailService;
import com.unibe.titulation.entities.Reader;
import com.unibe.titulation.entities.TopicStudent;
import com.unibe.titulation.repositories.ProductAndWorkEvaluationRepository;
import com.unibe.titulation.repositories.ReaderAccordanceRepository;
import com.unibe.titulation.repositories.ReaderObservationsRepository;
import com.unibe.titulation.repositories.ReaderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ReaderService {

    private final ReaderRepository readerRepository;
    private final TopicService topicService;
    private final TopicStudentService topicStudentService;
    private final EmailService emailService;
    private final ReaderAccordanceRepository readerAccordanceRepository;
    private final ProductAndWorkEvaluationRepository productAndWorkEvaluationRepository;
    private final ReaderObservationsRepository readerObservationsRepository;

    @Autowired
    public ReaderService(ReaderRepository readerRepository, TopicService topicService,
                         TopicStudentService topicStudentService, EmailService emailService,
                         ReaderAccordanceRepository readerAccordanceRepository, ProductAndWorkEvaluationRepository productAndWorkEvaluationRepository,
                         ReaderObservationsRepository readerObservationsRepository) {
        this.readerRepository = readerRepository;
        this.topicService = topicService;
        this.topicStudentService = topicStudentService;
        this.emailService = emailService;
        this.readerAccordanceRepository = readerAccordanceRepository;
        this.productAndWorkEvaluationRepository = productAndWorkEvaluationRepository;
        this.readerObservationsRepository = readerObservationsRepository;
    }

    public Optional<Reader> getReaderById(String reader_id) {
        return this.readerRepository.findById(reader_id);
    }

    public Optional<Reader> getReaderByTopicId(String topicId) {
        return this.readerRepository.findByTopic_Id(topicId);
    }

    public Optional<Reader> getReaderByStudentIdAndTopicId(String studentId) {
        TopicStudent topicStudent = this.topicStudentService.findTopicStudentByStudent_Id(studentId).get();
        return this.getReaderByTopicId(topicStudent.getTopic().getId());

    }

    public List<Reader> getReadersByReaderCi(String readerCi) {
        return this.readerRepository.findByReader_Ci(readerCi);
    }

    public void saveReader(Reader reader) throws AddressException {
        String emailMessageToReader = "Estimado (a) docente usted ha sido designado como lector de un trabajo de titulación." +
                "\nPara más información revise el apartado lectorías en el sistema de titulación.";
        String emailMessageToStudent = "Estimado (a) estudiante se le designado un lector a su trabajo de titulación." +
                "\nDebe ingresar al sistema de titulación y enviar su tesis. " +
                "\nPara más información revise el apartado lectorías en el sistema de titulación.";
        String emailSubject = "Designación de lector al trabajo de titulación";
        String readerEmail = reader.getReader().getEmail();
        List<TopicStudent> topicStudents = this.topicStudentService.findTopicStudentsByTopic_Id(reader.getTopic().getId());
        List<String> studentsEmail = topicStudents.stream().map(data -> data.getStudent().getEmail()).collect(Collectors.toList());
        this.emailService.sendNotificationToStudent(studentsEmail, emailMessageToStudent, emailSubject);
        this.emailService.sendNotificationToTeacher(readerEmail, emailMessageToReader, emailSubject);
        this.topicService.setTopicReader(reader.getTopic().getId(), true);
        this.readerRepository.save(reader);
    }

    public void saveReaderCalendar(Reader reader) {
        this.readerRepository.save(reader);
    }

    public void saveReaders(List<Reader> readerList) {
        this.topicService.setTopicReader(readerList.get(0).getTopic().getId(), true);
        for (int i = 0; i < readerList.size(); i++) {
            this.readerRepository.save(readerList.get(i));
        }
    }

    public void updateReaderState(String topic_id, String state) {
        Reader reader = this.readerRepository.findByTopic_Id(topic_id).get();
        reader.setState(state);
        this.readerRepository.save(reader);
    }

    public void updateReaderStateByCareerId(String careerId, String state) {
        List<Reader> reader = this.getReaderByStatusAndCareerId(careerId);
        for (int i = 0; i < reader.size(); i++) {
            reader.get(i).setState(state);
            this.readerRepository.save(reader.get(i));
        }
    }

    public List<Reader> getReaderByStatusAndCareerId(String careerId) {
        return this.readerRepository.findByStateAndTopic_Career_Id("En conformidad", careerId);
    }

    public List<Reader> getReaderByStatusCalendarCreatedAndCareerId(String careerId) {
        return this.readerRepository.findByStateAndTopic_Career_Id("Creando calendario", careerId);
    }

    public boolean existsReaderByStatusAndCareerId(String careerId) {
        return this.readerRepository.existsByStateAndTopic_Career_Id("En conformidad", careerId);
    }

    public List<Reader> getReadersByTopicIdList(List<String> topicId) {
        return this.readerRepository.findByTopic_IdIn(topicId);
    }

    public List<Reader> getAllByCareer(String careerId) {
        return this.readerRepository.findByTopic_Career_Id(careerId);
    }

    public void resetReaderProcess(String topic_id) {
        this.readerAccordanceRepository.deleteByTopic_Id(topic_id);
        this.productAndWorkEvaluationRepository.deleteByTopic_Id(topic_id);
        this.readerAccordanceRepository.deleteByTopic_Id(topic_id);
        this.readerRepository.deleteByTopic_Id(topic_id);
        this.topicService.setTopicReader(topic_id, false);
    }

}
