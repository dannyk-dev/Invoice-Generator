package com.project.receiptsystem.docs;

import com.project.receiptsystem.FileService;
import com.project.receiptsystem.receipt.Product;
import com.project.receiptsystem.receipt.ProductData;
import com.project.receiptsystem.receipt.Receipt;
import com.project.receiptsystem.receipt.ReceiptData;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.spire.doc.Table;
import com.spire.doc.fields.Field;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.graphics.PdfImageType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

public class InvoiceDocument {

    Document doc;
    private final String[][] purchaseData;
    FileService fileService;

    public InvoiceDocument(Receipt user, List<Product> purchaseData) {
        this.fileService = new FileService();
        this.purchaseData = this.unpackProducts(purchaseData);
        this.doc = new Document();
        this.doc.loadFromFile(fileService.createTemplate());
        this.setupDocument(user);
    }

    private void setupDocument(Receipt user) {
        try {
            for (Map.Entry<ReceiptData, String> item : user.getReceipt().entrySet()) {
                String tempText = String.format("#%s", item.getKey());
                String newValue = item.getValue() != null ? item.getValue() : "";

                this.doc.replace(tempText, newValue, true, true);
            }
        } catch (Exception e) {
            System.out.println("An error has occurred");
            e.printStackTrace();
        }
    }

    private String[][] unpackProducts(List<Product> purchases) {
        DecimalFormat df = new DecimalFormat("###, ###, ###.00 MZN");

        return purchases
                .stream()
                .map(product -> new String[]{
                        product.getProduct(ProductData.SEQUENCE),
                        product.getProduct(ProductData.PRODUCT_DESCRIPTION),
                        product.getProduct(ProductData.QUANTITIES),
                        df.format(product.getProduct(ProductData.AMOUNT_UNIT)),
                        df.format(product.getProduct(ProductData.TOTAL_AMOUNT)),
                }).toArray(String[][]::new);
    }

    private void addRows(Table table, int rowNum) {
        for (int i = 0; i < rowNum; i++) {
            table.getRows().insert(2 + i, table.getRows().get(1).deepClone());
            for (Object object : table.getRows().get(2 + i).getCells().get(3).getParagraphs().get(0).getChildObjects()
            ) {
                if (object instanceof Field field) {
                    field.setCode(String.format("=B%d*C%d\\# \"0.00\"", 3 + i, 3 + i));
                }
                break;
            }
        }
    }

    private void populateTable(Table table) {
        for (int r = 0; r < this.purchaseData.length; r++) {
            for (int c = 0; c < this.purchaseData[r].length; c++) {
                table.getRows().get(r+1).getCells().get(c).getParagraphs().get(0).setText(this.purchaseData[r][c]);
            }
        }
    }

    public InvoiceDocument writeToDocument() {
        Table table = this.doc.getSections().get(0).getTables().get(3);

        if (this.purchaseData.length > 1)
            this.addRows(table, this.purchaseData.length - 1);

        this.populateTable(table);
        this.doc.isUpdateFields(true);
        return this;
    }

    public InvoiceDocument exportToPDF() {
        this.doc.saveToFile(this.fileService.appInstallDirectory("temp/Invoice.pdf"), FileFormat.PDF);
        return this;
    }

    public void previewImage() {
        try {
            PdfDocument invoice = this.fileService.getInvoicePDF();

            BufferedImage img = invoice
                    .saveAsImage(0, PdfImageType.Bitmap, 64, 64)
                    .getSubimage(0, 243, 529, 640);

            BufferedImage cropped = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics g = cropped.createGraphics();
            g.drawImage(img, 0, 0, null);

            ImageIO.write(cropped, "PNG", new File(this.fileService.appInstallDirectory("temp/preview.png")));

        } catch (IOException e) {
            System.out.println("Error creating preview...");
            e.printStackTrace();
        }
    }
}
