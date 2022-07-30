package com.unibe.titulation.services.PDFService.DesignationTT;

import com.unibe.titulation.entities.DesignationTT;
import com.unibe.titulation.security.entity.User;

import com.unibe.titulation.security.service.UserService;
import com.unibe.titulation.services.DesignationTTService;
import com.unibe.titulation.services.TopicStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.text.ParseException;
import java.util.Date;

@Service
public class StudentLetterPDF {

    private static final String PDF_RESOURCES = "/pdf-resources/";


    private SpringTemplateEngine templateEngine;
    private DesignationTTService designationTTService;
    private TopicStudentService topicStudentService;
    private UserService userService;

    @Autowired
    public StudentLetterPDF(SpringTemplateEngine templateEngine, DesignationTTService designationTTService,
                            TopicStudentService topicStudentService, UserService userService) {
        this.templateEngine = templateEngine;
        this.designationTTService = designationTTService;
        this.topicStudentService = topicStudentService;
        this.userService = userService;
    }

    private String convertToXhtml(String html) throws UnsupportedEncodingException {
        Tidy tidy = new Tidy();
        tidy.setXHTML(true);
        tidy.setIndentContent(true);
        tidy.setPrintBodyOnly(true);
        tidy.setInputEncoding("UTF-8");
        tidy.setOutputEncoding("UTF-8");
        tidy.setSmartIndent(true);
        tidy.setShowWarnings(false);
        tidy.setQuiet(true);
        tidy.setTidyMark(false);

        Document htmlDOM = tidy.parseDOM(new ByteArrayInputStream(html.getBytes()), null);

        OutputStream out = new ByteArrayOutputStream();
        tidy.pprint(htmlDOM, out);
        return out.toString();
    }

    private Context getContextStudentDesignationLetter(String topicStudent_Id) throws ParseException {
        DesignationTT designationTT = this.designationTTService.findByTopicStudent_Id(topicStudent_Id).get();
        //CAREER DIRECTOR NAME      ;
        User careerDirector = this.userService.getOneUserByRoles_RolNameAndCareer_Id("ROLE_CAREER_DIRECTOR", designationTT.getTopicStudent().getTopic().getCareer().getId());
        String careerDirectorName = careerDirector.getName() + ' ' + careerDirector.getLastName() + ' ' + careerDirector.getSecondLastName();
        User studentData = designationTT.getTopicStudent().getStudent();
        String studentCompleteName = studentData.getName() + ' ' + studentData.getSecondName() + ' ' + studentData.getLastName() + ' ' + studentData.getSecondLastName();
        String faculty = designationTT.getTopicStudent().getStudent().getCareer().getFaculty().getName();
        String career = designationTT.getTopicStudent().getStudent().getCareer().getName();
        String topicName = designationTT.getTopicStudent().getTopic().getName();
        Date actualDate = new Date();
        Context context = new Context();
        context.setVariable("designation", designationTT);
        context.setVariable("studentCompleteName", studentCompleteName);
        context.setVariable("createdBy", careerDirectorName);
        context.setVariable("faculty", faculty);
        context.setVariable("career", career);
        context.setVariable("topicName", topicName);
        context.setVariable("actualDate", actualDate);
        return context;
    }

    private String loadAndFillTemplate(Context context) {
        return templateEngine.process("/tutor-designation/student-letter", context);
    }

    private File renderTopicDenunciationPdf(String html) throws Exception {
        File file = File.createTempFile("students", ".pdf");
        OutputStream outputStream = new FileOutputStream(file);
        ITextRenderer renderer = new ITextRenderer(20f * 4f / 3f, 20);
        renderer.setDocumentFromString(html, new ClassPathResource(PDF_RESOURCES).getURL().toExternalForm());
        renderer.layout();
        renderer.createPDF(outputStream);
        outputStream.close();
        file.deleteOnExit();
        return file;
    }

    public File generateStudentLetterPdf(String topicStudent_Id) throws Exception {
        Context context = getContextStudentDesignationLetter(topicStudent_Id);
        String html = loadAndFillTemplate(context);
        String xhtml = convertToXhtml(html);
        return renderTopicDenunciationPdf(xhtml);
    }
}