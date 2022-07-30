package com.unibe.titulation.services.PDFService;

import com.unibe.titulation.entities.ReaderAccordance;
import com.unibe.titulation.security.entity.User;
import com.unibe.titulation.security.service.UserService;
import com.unibe.titulation.services.ReaderAccordanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
public class ReaderProcessResultPDF {

    private static final String PDF_RESOURCES = "/pdf-resources/";
    private final SpringTemplateEngine templateEngine;
    private final ReaderAccordanceService readerAccordanceService;
    private final UserService userService;

    @Autowired
    public ReaderProcessResultPDF(SpringTemplateEngine templateEngine, ReaderAccordanceService readerAccordanceService,
                                  UserService userService) {
        this.templateEngine = templateEngine;
        this.readerAccordanceService = readerAccordanceService;
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

    private Context getContext(String topicId) throws ParseException {
        ReaderAccordance readerAccordance = this.readerAccordanceService.findByTopicId(topicId).get();
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String ci = userDetails.getUsername();
        User user = this.userService.getByCi(ci).get();
        String userFullName = user.getName() + ' ' + user.getLastName();
        String topicName = readerAccordance.getTopic().getName();
        Date maxDate = readerAccordance.getMaxDate();
        Date emissionDate = readerAccordance.getDate();
        Date actualDate = new Date();
        Context context = new Context();
        context.setVariable("maxDate", maxDate);
        context.setVariable("actualDate", actualDate);
        context.setVariable("userFullName", userFullName);
        context.setVariable("emissionDate", emissionDate);
        context.setVariable("topicName", topicName);
        return context;
    }

    private String loadAndFillTemplate(Context context) {
        return templateEngine.process("reader-process-result", context);
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

    public File generatePdf(String topicId) throws Exception {
        Context context = getContext(topicId);
        String html = loadAndFillTemplate(context);
        String xhtml = convertToXhtml(html);
        return renderPdf(xhtml);
    }
}
