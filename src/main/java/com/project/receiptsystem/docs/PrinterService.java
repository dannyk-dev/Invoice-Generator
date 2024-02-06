package com.project.receiptsystem.docs;

import com.spire.pdf.PdfDocument;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterJob;

public class PrinterService {

    private final PrinterJob printJob;
    private final PageFormat pgFormat;

    public PrinterService() {
        this.printJob = PrinterJob.getPrinterJob();
        this.pgFormat = printJob.defaultPage();
    }

    public PrinterJob setupPrinter(PdfDocument pdfInvoice) {
        printJob.setPrintable(pdfInvoice, this.pgFormat);
        Paper pgPaper = pgFormat.getPaper();
        pgPaper.setImageableArea(0, 0, pgFormat.getWidth(), pgFormat.getHeight());
        pgFormat.setPaper(pgPaper);

        return this.printJob;
    }


}
