package com.unibe.titulation.services.PDFService;


import com.unibe.titulation.entities.TopicProposal;
import com.unibe.titulation.services.TopicProposalService;
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
public class TopicProposalPDF {

    private static final String PDF_RESOURCES = "/pdf-resources/";

    private SpringTemplateEngine templateEngine;
    private TopicProposalService topicProposalService;

    @Autowired
    public TopicProposalPDF(SpringTemplateEngine templateEngine, TopicProposalService topicProposalService) {
        this.templateEngine = templateEngine;
        this.topicProposalService = topicProposalService;
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

    private Context getContextTopicProposal(String id) {
        TopicProposal topicProposal = this.topicProposalService.findTopicProposalByTopicId(id).get(0);
        Context context = new Context();
        context.setVariable("topicProposal", topicProposal);
        return context;
    }

    private String loadAndFillTemplate(Context context) {
        return templateEngine.process("topic-proposal", context);
    }

    private File renderTopicProposalPdf(String html) throws Exception {
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

    public File generateProposalPdf(String topicId) throws Exception{
        Context context = getContextTopicProposal(topicId);
        String html = loadAndFillTemplate(context);
        String xhtml = convertToXhtml(html);
        return renderTopicProposalPdf(xhtml);
    }
}
