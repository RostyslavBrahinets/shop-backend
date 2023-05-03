package com.shop.report;

import com.shop.utilities.PdfUtility;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping(ReportController.REPORT_URL)
public class ReportController {
    public static final String REPORT_URL = "/api/report";

    @PostMapping("/download")
    public void downloadReport(
        @RequestBody ReportDto report,
        HttpServletResponse response
    ) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=report_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);
        PdfUtility pdfUtility = new PdfUtility(report);
        pdfUtility.export(response);
    }
}
