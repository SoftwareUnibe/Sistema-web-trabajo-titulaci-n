package com.unibe.titulation.services.PDFService;
import com.unibe.titulation.entities.ReaderAccordance;
import com.unibe.titulation.entities.TopicStudent;
import com.unibe.titulation.security.entity.User;
import com.unibe.titulation.services.DesignationTTService;
import com.unibe.titulation.services.ReaderAccordanceService;
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
import java.util.List;

@Service
public class ReaderAccordancePDF {

    private static final String PDF_RESOURCES = "/pdf-resources/";

    private SpringTemplateEngine templateEngine;
    private ReaderAccordanceService readerAccordanceService;
    private TopicStudentService topicStudentService;
    private DesignationTTService designationTTService;

    @Autowired
    public ReaderAccordancePDF(SpringTemplateEngine templateEngine, ReaderAccordanceService readerAccordanceService,
                               TopicStudentService topicStudentService, DesignationTTService designationTTService) {
        this.templateEngine = templateEngine;
        this.readerAccordanceService = readerAccordanceService;
        this.topicStudentService = topicStudentService;
        this.designationTTService = designationTTService;
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

    private Context getContext(String topicId) throws ParseException {
        ReaderAccordance readerAccordance = this.readerAccordanceService.findByTopicId(topicId).get();
        List<TopicStudent> topicStudents = this.topicStudentService.findTopicStudentsByTopic_Id(topicId);
        User tutor = this.designationTTService.findByTopicStudent_Id(topicStudents.get(0).getId()).get().getTeacher();
        Context context = new Context();
        context.setVariable("accordance", readerAccordance);
        context.setVariable("students", topicStudents);
        context.setVariable("tutorName", tutor.getDegree()+' '+tutor.getName()+' '+tutor.getLastName());
        context.setVariable("twoStudents", topicStudents.get(0).getTopic().isTwoStudents());
        return context;
    }

    private String loadAndFillTemplate(Context context) {
        return templateEngine.process("reader-accordance", context);
    }

    private File renderPdf(String html) throws Exception {
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

    public File generatePdf(String topicId) throws Exception{
        Context context = getContext(topicId);
        String html = loadAndFillTemplate(context);
        String xhtml = convertToXhtml(html);
        return renderPdf(xhtml);
    }

}