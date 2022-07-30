package com.unibe.titulation.services.PDFService;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import javax.transaction.Transactional;

import com.unibe.titulation.entities.ProductAndWorkEvaluation;
import com.unibe.titulation.entities.Reader;
import com.unibe.titulation.entities.TopicStudent;
import com.unibe.titulation.security.entity.User;
import com.unibe.titulation.services.DesignationTTService;
import com.unibe.titulation.services.ProductAndWorkEvaluationService;
import com.unibe.titulation.services.ReaderService;
import com.unibe.titulation.services.TopicStudentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.w3c.tidy.Tidy;
import org.springframework.core.io.ClassPathResource;
import org.thymeleaf.context.Context;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;
import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ReaderEvaluationPDF {

    private static final String PDF_RESOURCES = "/pdf-resources/";
    private SpringTemplateEngine templateEngine;
    private final ReaderService readerService;
    private final TopicStudentService topicStudentService;
    private final DesignationTTService designationTTService;
    private final ProductAndWorkEvaluationService productAndWorkEvaluationService;
    @Autowired
    public ReaderEvaluationPDF(SpringTemplateEngine templateEngine, ReaderService readerService,
                               TopicStudentService topicStudentService, DesignationTTService designationTTService,ProductAndWorkEvaluationService productAndWorkEvaluationService) {
        this.templateEngine = templateEngine;
        this.readerService = readerService;
        this.topicStudentService = topicStudentService;
        this.designationTTService = designationTTService;
        this.productAndWorkEvaluationService=productAndWorkEvaluationService;
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

    private Context getContextReaderEvaluation(String topicId) throws ParseException {
        Reader reader = this.readerService.getReaderByTopicId(topicId).get();
        String career=reader.getTopic().getCareer().getName();
        String faculty=reader.getTopic().getCareer().getFaculty().getName();
        String tittle=reader.getTopic().getName();
        ProductAndWorkEvaluation productAndWorkEvaluation=productAndWorkEvaluationService.getByTopicId(topicId).get();
        List<TopicStudent> topicStudents= new ArrayList<>();
        topicStudents=this.topicStudentService.findTopicStudentsByTopic_Id(topicId);
        User tutor= this.designationTTService.findByTopicStudent_Id(topicStudents.get(0).getId()).get().getTeacher();

        Context context = new Context();
        Float totalNote= productAndWorkEvaluation.getFinalNote();
        context.setVariable("hasProduct", productAndWorkEvaluation.getTopic().getCareer().isHasProduct());
        context.setVariable("faculty", faculty);
        context.setVariable("career",career);
        context.setVariable("date", productAndWorkEvaluation.getDate());
        context.setVariable("tittle",tittle);
        context.setVariable("topicStudents",topicStudents);
        context.setVariable("tutor", tutor.getName()+' '+tutor.getSecondName()+' '+tutor.getLastName()+' '+tutor.getSecondLastName());
        context.setVariable("readerName", reader.getReader().getName()+' '+reader.getReader().getSecondName()+' '+reader.getReader().getLastName()+' '+reader.getReader().getSecondLastName());
        context.setVariable("workNotes", productAndWorkEvaluation.getWorkEvaluation());
        context.setVariable("productNotes", productAndWorkEvaluation.getProductEvaluation());
        context.setVariable("notes", productAndWorkEvaluation.getCommentary());
        context.setVariable("totalNote", totalNote);
        return context;
    }

    private String loadAndFillTemplate(Context context) {
        return templateEngine.process("reader-evaluation", context);
    }

    private File renderReaderEvaluationPdf(String html) throws Exception {
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

    public File generateReaderEvaluationPdf(String topicId) throws Exception{
        Context context = getContextReaderEvaluation(topicId);
        String html = loadAndFillTemplate(context);
        String xhtml = convertToXhtml(html);
        return renderReaderEvaluationPdf(xhtml);
    }

}