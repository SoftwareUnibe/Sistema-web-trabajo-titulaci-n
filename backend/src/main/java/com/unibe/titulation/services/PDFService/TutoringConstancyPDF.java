package com.unibe.titulation.services.PDFService;

import com.unibe.titulation.entities.TopicStudent;
import com.unibe.titulation.entities.TutoringConstancy;
import com.unibe.titulation.entities.TutoringHours;
import com.unibe.titulation.security.entity.User;
import com.unibe.titulation.services.DesignationTTService;
import com.unibe.titulation.services.TopicStudentService;
import com.unibe.titulation.services.TutoringConstancyService;
import com.unibe.titulation.services.TutoringHoursService;
import org.w3c.tidy.Tidy;
import org.w3c.dom.Document;
import org.springframework.core.io.ClassPathResource;
import org.thymeleaf.context.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.thymeleaf.spring5.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

@Service
public class TutoringConstancyPDF {
    private static final String PDF_RESOURCES = "/pdf-resources/";
    private SpringTemplateEngine templateEngine;
    private TutoringHoursService tutoringHoursService;
    private TopicStudentService topicStudentService;
    private DesignationTTService designationTTService;
    private  TutoringConstancyService tutoringConstancyService ;
    @Autowired
    public TutoringConstancyPDF(SpringTemplateEngine templateEngine, TutoringHoursService tutoringHoursService,
            TopicStudentService topicStudentService, DesignationTTService designationTTService, TutoringConstancyService tutoringConstancyService) {
        this.templateEngine = templateEngine;
        this.tutoringHoursService = tutoringHoursService;
        this.topicStudentService = topicStudentService;
        this.designationTTService = designationTTService;
        this.tutoringConstancyService= tutoringConstancyService;
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

    private Context getContextTuringConstancy(String topicId) {
        List<TutoringHours> constancyList = this.tutoringHoursService.getTutoringConstancyByTopicId(topicId);
        List<TopicStudent> students = this.topicStudentService.findTopicStudentsByTopic_Id(topicId);
        List<String> periodList = Arrays.asList("");
        User tutor = this.designationTTService.findByTopicStudent_Id(students.get(0).getId()).get().getTeacher();
        periodList.set(0, constancyList.get(0).getPeriod());
        for (int i = 0; i < constancyList.size(); i++) {
            if (!constancyList.get(i).getPeriod().equals(constancyList.get(0).getPeriod()))
                periodList.set(1, constancyList.get(i).getPeriod());
        }
        TutoringConstancy tutoringConstancy=this.tutoringConstancyService.getByTopicId(topicId);
        Date currentDate = tutoringConstancy.getGenerationDate();
        Integer totalHours = constancyList.stream().mapToInt(o -> o.getHours()).sum();
        Context context = new Context();
        context.setVariable("periodList", periodList);
        context.setVariable("tutoringOfPeriodList", constancyList);
        context.setVariable("students", students);
        context.setVariable("tutor", tutor);
        context.setVariable("totalHours", totalHours);
        context.setVariable("currentDate", currentDate);
        return context;
    }

    private String loadAndFillTemplate(Context context) {
        return templateEngine.process("tutoring-constancy", context);
    }

    private File renderConstancyPdf(String html) throws Exception {
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

    public File generateConstancy(String topicId) throws Exception {
        Context context = getContextTuringConstancy(topicId);
        String html = loadAndFillTemplate(context);
        String xhtml = convertToXhtml(html);
        return renderConstancyPdf(xhtml);
    }

}
