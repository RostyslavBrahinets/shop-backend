package com.shop.utilities;

import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.shop.dto.ReportDto;
import com.shop.models.Product;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;

public class PdfUtility {
    private final ReportDto reportInfo;

    public PdfUtility(ReportDto reportInfo) {
        this.reportInfo = reportInfo;
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLACK);

        Paragraph p = new Paragraph("Report", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{1.5f, 3.5f, 3.0f, 3.0f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);

        document.close();
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.WHITE);
        cell.setPadding(4);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.BLACK);

        cell.setPhrase(new Phrase("Name", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Price", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Total Cost", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Amount Of Money", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table) {
        for (Product product : reportInfo.getProducts()) {
            table.addCell(product.getName());
            table.addCell(String.valueOf(product.getPrice()));
            table.addCell("");
            table.addCell("");
        }

        table.addCell("");
        table.addCell("");
        table.addCell(String.valueOf(reportInfo.getTotalCost()));
        table.addCell(String.valueOf(reportInfo.getAmountOfMoney()));
    }
}
