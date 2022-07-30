package com.unibe.titulation.services.PDFService;

import com.unibe.titulation.entities.Reader;
import com.unibe.titulation.entities.TopicStudent;
import com.unibe.titulation.security.entity.User;
import com.unibe.titulation.services.ReaderService;
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
public class ReaderDesignationLetterPDF {

    private static final String PDF_RESOURCES = "/pdf-resources/";
    private SpringTemplateEngine templateEngine;
    private TopicStudentService topicStudentService;
    private ReaderService readerService;

    @Autowired
    public ReaderDesignationLetterPDF(SpringTemplateEngine templateEngine, TopicStudentService topicStudentService, ReaderService readerService) {
        this.templateEngine = templateEngine;
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

    private Context getContextReaderLetterDesignation(String id) throws ParseException {
        Reader reader = this.readerService.getReaderById(id).get();
        List<TopicStudent> topicStudents = this.topicStudentService.findTopicStudentsByTopic_Id(reader.getTopic().getId());
        User studentOne = topicStudents.get(0).getStudent();
        User studentTwo;
        boolean isTwoStudents = false;
        //String thesisLink = topicStudents.get(0).getThesisLink();
        String studentOneCompleteName = studentOne.getName() +
                ' ' +studentOne.getSecondName() + ' ' + studentOne.getLastName() + ' ' + studentOne.getSecondLastName();
        String studentTwoCompleteName = new String();

        if (topicStudents.get(0).getTopic().isTwoStudents()) {
            studentTwo = topicStudents.get(1).getStudent();
            studentTwoCompleteName = studentTwo.getName() +
                    ' ' +studentTwo.getSecondName() + ' ' + studentTwo.getLastName() + ' ' + studentTwo.getSecondLastName();
            isTwoStudents = true;
        }
        System.out.println("desde el log " + isTwoStudents);

        Context context = new Context();
        context.setVariable("reader", reader);
        context.setVariable("studentOne", studentOneCompleteName);
        context.setVariable("studentTwo", studentTwoCompleteName);
        context.setVariable("isTwoStudents", isTwoStudents);
        //context.setVariable("thesisLink", thesisLink);
        return context;
    }

    private String loadAndFillTemplate(Context context) {
        return templateEngine.process("reader-designation-letter", context);
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

    public File generateReaderDesignationLetterPdf(String id) throws Exception{
        Context context = getContextReaderLetterDesignation(id);
        String html = loadAndFillTemplate(context);
        String xhtml = convertToXhtml(html);
        return renderTopicDenunciationPdf(xhtml);
    }
}
