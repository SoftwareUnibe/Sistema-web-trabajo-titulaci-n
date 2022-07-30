package com.unibe.titulation.services.PDFService;

import com.unibe.titulation.entities.TopicApprovalNotification;
import com.unibe.titulation.security.entity.User;
import com.unibe.titulation.security.service.UserService;
import com.unibe.titulation.services.ApprovalNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;

@Service
public class ApprovalNotificationPDF {

    private static final String PDF_RESOURCES = "/pdf-resources/";



    private SpringTemplateEngine templateEngine;
    private ApprovalNotificationService approvalNotificationService;
    private UserService userService;
    @Autowired
    public ApprovalNotificationPDF(SpringTemplateEngine templateEngine, ApprovalNotificationService approvalNotificationService, UserService userService) {
        this.templateEngine = templateEngine;
        this.approvalNotificationService = approvalNotificationService;
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

    public File generateApprovalPdf(String topicStudentId) throws Exception{
        Context context = getContextApprovalNotification(topicStudentId);
        String html = loadAndFillTemplate(context);
        String xhtml = convertToXhtml(html);
        return renderApprovalPdf(xhtml);
    }



    private File renderApprovalPdf(String html) throws Exception {
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


    private Context getContextApprovalNotification(String id) {
        TopicApprovalNotification topicApprovalNotification = this.approvalNotificationService.findApprovalNotificationByTopicStudent_Id(id).get();
        User careerDirector = this.userService.getOneUserByRoles_RolNameAndCareer_Id("ROLE_CAREER_DIRECTOR", topicApprovalNotification.getTopicStudent().getTopic().getCareer().getId());
        String careerDirectorName = careerDirector.getName() + ' ' + careerDirector.getLastName() + ' ' + careerDirector.getSecondLastName();
        Context context = new Context();
        context.setVariable("director", careerDirectorName);
        context.setVariable("notification", topicApprovalNotification);
        return context;
    }


    private String loadAndFillTemplate(Context context) {
        return templateEngine.process("approval-notification", context);
    }

}