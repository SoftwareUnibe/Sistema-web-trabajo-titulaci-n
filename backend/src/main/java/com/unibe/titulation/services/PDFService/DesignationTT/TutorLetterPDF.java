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
import java.util.*;

@Service
public class TutorLetterPDF {
    private static final String PDF_RESOURCES = "/pdf-resources/";
    private SpringTemplateEngine templateEngine;
    private DesignationTTService designationTTService;
    private TopicStudentService topicStudentService;
    private UserService userService;

    @Autowired
    public TutorLetterPDF(SpringTemplateEngine templateEngine, DesignationTTService designationTTService, TopicStudentService topicStudentService, UserService userService) {
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

    private Context getContextTutorDesignationLetter(String topicStudent_Id, String careerDirectorCi) throws ParseException {
        List<DesignationTT> designationTT = this.designationTTService.findDesignationTTByTopicStudent_Topic_Id(topicStudent_Id);
        Optional<User> careerDirector = this.userService.getByCi(careerDirectorCi);

        String careerName = this.changeCareerName(designationTT.get(0).getTopicStudent().getTopic().getCareer().getName());

        User studentOneData = designationTT.get(0).getTopicStudent().getStudent();
        User studentTwoData = new User();
        if (designationTT.get(0).getTopicStudent().getTopic().isTwoStudents()) {
            studentTwoData = designationTT.get(1).getTopicStudent().getStudent();
        }
        String studentOneCompleteName = studentOneData.getName() + ' ' + studentOneData.getSecondName() + ' ' + studentOneData.getLastName() + ' ' + studentOneData.getSecondLastName();
        String studentTwoCompleteName = studentTwoData.getName() + ' ' + studentTwoData.getSecondName() + ' ' + studentTwoData.getLastName() + ' ' + studentTwoData.getSecondLastName();
        String faculty = designationTT.get(0).getTopicStudent().getStudent().getCareer().getFaculty().getName();
        Date actualDate = new Date();
        Context context = new Context();
        context.setVariable("designation", designationTT);
        context.setVariable("studentOne", studentOneCompleteName);
        context.setVariable("studentTwo", studentTwoCompleteName);
        context.setVariable("careerDirector", careerDirector);
        context.setVariable("careerName", careerName);
        context.setVariable("faculty", faculty);
        context.setVariable("actualDate", actualDate);
        return context;
    }

    private String changeCareerName(String careerName) {
        switch (careerName) {
            case "Ingeniería en Software":
                careerName = "Software";
                break;
            case "Nutrición":
                careerName = "Nutrición";
                break;
            case "Enfermeria":
                careerName = "Enfermeria";
                break;
            default:
                break;
        }
        return careerName;
    }

    private String loadAndFillTemplate(Context context) {
        return templateEngine.process("/tutor-designation/tutor-letter", context);
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

    public File generateTutorLetterPdf(String topicStudent_Id, String careerDirectorCi) throws Exception {
        Context context = getContextTutorDesignationLetter(topicStudent_Id, careerDirectorCi);
        String html = loadAndFillTemplate(context);
        String xhtml = convertToXhtml(html);
        return renderTopicDenunciationPdf(xhtml);
    }

}