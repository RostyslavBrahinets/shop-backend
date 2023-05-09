package com.shop.utilities;

import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.shop.product.Product;
import com.shop.report.ReportDto;

import java.awt.*;
import java.io.ByteArrayOutputStream;

import static com.lowagie.text.Element.ALIGN_CENTER;

public class PdfUtility {
    private final ReportDto report;

    public PdfUtility(ReportDto report) {
        this.report = report;
    }

    public ByteArrayOutputStream export() throws DocumentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (Document document = new Document(PageSize.A4)) {
            PdfWriter.getInstance(document, outputStream);

            document.open();

            Paragraph p = new Paragraph("Report", getFont(32, true));
            p.setAlignment(ALIGN_CENTER);

            document.add(p);

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100f);
            table.setWidths(new float[]{3.0f, 1.5f});
            table.setSpacingBefore(10);

            writeTableHeader(table);
            writeTableData(table);

            document.add(table);
        }

        return outputStream;
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBorderColor(Color.WHITE);
        cell.setPadding(2);

        cell.setPhrase(new Phrase("Name", getFont(18, true)));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Price", getFont(18, true)));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBorderColor(Color.WHITE);
        cell.setPadding(2);

        for (Product product : report.products()) {
            cell.setPhrase(new Phrase(product.getName(), getFont(18, false)));
            table.addCell(cell);

            cell.setPhrase(new Phrase(String.valueOf(product.getPrice()), getFont(18, false)));
            table.addCell(cell);
        }

        cell.setPhrase(new Phrase("Amount Price", getFont(18, true)));
        table.addCell(cell);

        cell.setPhrase(new Phrase(String.valueOf(report.priceAmount()), getFont(18, true)));
        table.addCell(cell);
    }

    private static Font getFont(int size, boolean isBold) {
        String style = isBold ? FontFactory.HELVETICA_BOLD : FontFactory.HELVETICA;
        Font font = FontFactory.getFont(style);
        font.setSize(size);
        font.setColor(Color.BLACK);
        return font;
    }
}
