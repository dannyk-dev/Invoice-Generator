package com.project.receiptsystem;

import com.spire.pdf.PdfDocument;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileService {

    public String createTemplate() {
        InputStream inputStream = getClass().getResourceAsStream("/com/project/receiptsystem/Invoice-Template.docx");

        try {
            Path tempFile = Files.createTempFile("Invoice-Template", ".docx");

            if (inputStream != null) {
                Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
                inputStream.close();
            }

            return tempFile.toAbsolutePath().toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String appInstallDirectory(String additionalPath) {
        Path workingDirectory = Paths.get(System.getenv("ProgramFiles"), "ReceiptSystem", "resources", additionalPath);
        System.out.println(workingDirectory.toAbsolutePath());
        return workingDirectory.toAbsolutePath().toString();
    }

    public PdfDocument getInvoicePDF() {
        PdfDocument pdf = new PdfDocument();
        pdf.loadFromFile(this.appInstallDirectory("temp/Invoice.pdf"));

        return pdf;
    }

    public File getInvoiceFile() {
        return new File("Invoice-Template.docx");
    }

    public File getPreviewImage() {
        try {
            File previewImage = new File(this.appInstallDirectory("temp/preview.png"));

            if (previewImage.exists() && previewImage.isFile())
                return previewImage;

            System.out.println("File does not exist");
        } catch (Exception e) {
            System.out.println("Error: URL Syntax error");
            e.printStackTrace();
        }
        return null;
    }
}
