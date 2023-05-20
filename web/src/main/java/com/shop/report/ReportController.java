package com.shop.report;

import com.shop.utilities.PdfUtility;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@CrossOrigin
@RequestMapping(ReportController.REPORT_URL)
public class ReportController {
    public static final String REPORT_URL = "/api/v1/report";

    @PostMapping("/download")
    public ResponseEntity<byte[]> downloadReport(@RequestBody ReportDto report) {
        PdfUtility pdfUtility = new PdfUtility(report);
        ByteArrayOutputStream reportContent = pdfUtility.export();

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        String filename = "report_" + currentDateTime + ".pdf";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", filename);

        return ResponseEntity.ok()
            .headers(headers)
            .body(reportContent.toByteArray());
    }
}
