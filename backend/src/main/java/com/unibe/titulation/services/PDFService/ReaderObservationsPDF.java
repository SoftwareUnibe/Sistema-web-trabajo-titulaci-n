package com.unibe.titulation.services.PDFService;

import org.springframework.stereotype.Service;

import com.unibe.titulation.entities.Reader;
import com.unibe.titulation.entities.ReaderObservations;
import com.unibe.titulation.entities.TopicStudent;
import com.unibe.titulation.services.ReaderObservationsService;
import com.unibe.titulation.services.ReaderService;
import com.unibe.titulation.services.TopicStudentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.text.ParseException;
import java.util.List;
@Service
public class ReaderObservationsPDF {
    private static final String PDF_RESOURCES = "/pdf-resources/";
    private SpringTemplateEngine templateEngine;
    private final ReaderObservationsService readerObservationsService;
    private final TopicStudentService topicStudentService;
    private final ReaderService readerService;
    @Autowired
    public ReaderObservationsPDF(SpringTemplateEngine templateEngine,
            ReaderObservationsService readerObservationsService, TopicStudentService topicStudentService, ReaderService readerService) {
        this.templateEngine = templateEngine;
        this.readerObservationsService = readerObservationsService;
        this.topicStudentService = topicStudentService;
        this.readerService = readerService;
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
        ReaderObservations readerObservations = this.readerObservationsService.findByTopic(topicId).get();
        Reader reader = this.readerService.getReaderByTopicId(topicId).get();
        List<TopicStudent> topicStudents = this.topicStudentService.findTopicStudentsByTopic_Id(topicId);
        Context context = new Context();
        context.setVariable("date", reader.getDate());
        context.setVariable("readerObs", readerObservations);
        context.setVariable("students", topicStudents);
        context.setVariable("twoStudents", topicStudents.get(0).getTopic().isTwoStudents());
        context.setVariable("readerName", reader.getReader().getName() + ' '+ reader.getReader().getSecondName() + ' '+reader.getReader().getLastName()+' '+ reader.getReader().getSecondLastName() );
        context.setVariable("mainList", readerObservations.getMainObservation());
        context.setVariable("descList", readerObservations.getDescObservation());
        return context;
    }

    private String loadAndFillTemplate(Context context) {
        return templateEngine.process("reader-observations", context);
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
