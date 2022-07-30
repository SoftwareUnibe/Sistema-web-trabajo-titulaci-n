package com.unibe.titulation.services.PDFService;

import com.unibe.titulation.entities.TopicDenunciation;
import com.unibe.titulation.security.entity.User;
import com.unibe.titulation.security.service.UserService;
import com.unibe.titulation.services.TopicDenunciationService;
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

@Service
public class TopicDenunciationPDF {

    private static final String PDF_RESOURCES = "/pdf-resources/";

    private SpringTemplateEngine templateEngine;
    private TopicDenunciationService topicDenunciationService;
    private UserService userService;
    @Autowired
    public TopicDenunciationPDF(SpringTemplateEngine templateEngine, TopicDenunciationService topicDenunciationService,UserService userService) {
        this.templateEngine = templateEngine;
        this.topicDenunciationService = topicDenunciationService;
        this.userService=userService;
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

    private Context getContextTopicDenunciation(String id) throws ParseException {
        TopicDenunciation topicDenunciation = this.topicDenunciationService.findTopicDenunciationByTopicStudentId(id).get();
        User careerDirector = this.userService.getOneUserByRoles_RolNameAndCareer_Id("ROLE_CAREER_DIRECTOR", topicDenunciation.getTopicStudent().getTopic().getCareer().getId());
        String careerDirectorName = careerDirector.getName() + ' ' + careerDirector.getLastName() + ' ' + careerDirector.getSecondLastName();
        Context context = new Context();
        context.setVariable("director", careerDirectorName);
        context.setVariable("topicDenunciation", topicDenunciation);
        return context;
    }

    private String loadAndFillTemplate(Context context) {
        return templateEngine.process("topic-denunciation", context);
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

    public File generateApprovalPdf(String topicStudentId) throws Exception{
        Context context = getContextTopicDenunciation(topicStudentId);
        String html = loadAndFillTemplate(context);
        String xhtml = convertToXhtml(html);
        return renderTopicDenunciationPdf(xhtml);
    }

}
