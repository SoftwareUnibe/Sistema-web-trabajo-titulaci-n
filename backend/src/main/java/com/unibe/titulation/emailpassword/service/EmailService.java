package com.unibe.titulation.emailpassword.service;

import com.unibe.titulation.emailpassword.dto.EmailValuesDTO;
import com.unibe.titulation.entities.CalendarDetail;
import com.unibe.titulation.entities.TopicStudent;
import com.unibe.titulation.repositories.CalendarDetailRepository;
import com.unibe.titulation.services.TopicStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class EmailService {

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    TemplateEngine templateEngine;

    @Value("${mail.urlFront}")
    private String urlFront;

    private final TopicStudentService topicStudentService;
    private final CalendarDetailRepository calendarDetailRepository;

    @Value("${spring.mail.username}")
    private String email;
    @Autowired
    public EmailService(TopicStudentService topicStudentService, CalendarDetailRepository calendarDetailRepository) {
        this.topicStudentService = topicStudentService;
        this.calendarDetailRepository = calendarDetailRepository;
    }

    public void sendResetPasswordEmail(EmailValuesDTO dto) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            Context context = new Context();
            Map<String, Object> model = new HashMap<>();
            model.put("url", urlFront + dto.getTokenPassword());
            context.setVariables(model);
            String htmlText = templateEngine.process("email-template", context);
            helper.setFrom(email);
            helper.setSubject("Restablecer contraseña");
            helper.setTo(dto.getMailTo());
            helper.setText(htmlText, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendThesisDocumentToReader(MultipartFile multipartFile, String readerEmail) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            String fileName = multipartFile.getOriginalFilename();
            helper.setFrom(email);
            helper.setTo(readerEmail);
            helper.setSubject("Documento trabajo titulación");
            helper.setText("Adjunto el documento correspondiente al trabajo de titulación.");
            helper.addAttachment(fileName, multipartFile);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendAntiPlagiarismLetterToStudent(MultipartFile multipartFile, String topicId) throws AddressException {
        MimeMessage message = javaMailSender.createMimeMessage();
        List<TopicStudent> topicStudentFound = this.topicStudentService.findTopicStudentsByTopic_Id(topicId);
        InternetAddress[] emailAddresses = new InternetAddress[topicStudentFound.size()];
        for (int i = 0; i < topicStudentFound.size(); i++) {
            String studentEmail = topicStudentFound.get(i).getStudent().getEmail();
            emailAddresses[i] = new InternetAddress(studentEmail);
        }
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            String fileName = multipartFile.getOriginalFilename();
            helper.setFrom(email);
            helper.setTo(emailAddresses);
            helper.setSubject("Carta antiplagio");
            helper.setText("Adjunto el documento de carta antiplagio de su trabajo de titulación.");
            helper.addAttachment(fileName, multipartFile);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendFinalCalendarNotificationToStudents(List<String> studentsEmail) throws AddressException {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            for (String s : studentsEmail) {
                CalendarDetail calendarDetail = this.calendarDetailRepository.findByStudent_Email(s);
                String datePattern = "yyyy-MM-dd";
                String hourPattern = "HH:mm";
                SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
                SimpleDateFormat hourFormat = new SimpleDateFormat(hourPattern);
                String date = dateFormat.format(calendarDetail.getDate());
                String hour = hourFormat.format(calendarDetail.getHour());
                MimeMessageHelper helper = new MimeMessageHelper(message, false);
                helper.setFrom(email);
                helper.setTo(s);
                helper.setSubject("Notificación defensa oral");
                helper.setText("Estimado (a) estudiante, el horario para la defensa oral de su trabajo de titulación es el siguiente: " +
                        "\n" + "Fecha: " + date + "\nHora: " + hour);
                javaMailSender.send(message);
            }

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendNotificationToStudent(List<String> studentsEmail, String emailMessage, String subject) throws AddressException {
        MimeMessage message = javaMailSender.createMimeMessage();
        InternetAddress[] emailAddresses = new InternetAddress[studentsEmail.size()];
        for (int i = 0; i < studentsEmail.size(); i++) {
            String studentEmail = studentsEmail.get(i);
            emailAddresses[i] = new InternetAddress(studentEmail);
        }
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, false);
            helper.setFrom(email);
            helper.setTo(emailAddresses);
            helper.setSubject(subject);
            helper.setText(emailMessage);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendNotificationToTeacher(String tutorEmail, String emailMessage, String subject) throws AddressException {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, false);
            helper.setFrom(email);
            helper.setTo(tutorEmail);
            helper.setSubject(subject);
            helper.setText(emailMessage);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
