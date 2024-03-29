package com.project.receiptsystem;

import com.project.receiptsystem.db.DBService;
import com.project.receiptsystem.docs.InvoiceDocument;
import com.project.receiptsystem.receipt.Product;
import com.project.receiptsystem.receipt.ProductData;
import com.project.receiptsystem.receipt.Receipt;
import com.project.receiptsystem.receipt.ReceiptController;
import com.project.receiptsystem.receipt.ReceiptData;
import com.project.receiptsystem.ui.UIController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.apache.commons.cli.CommandLine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Application extends javafx.application.Application {

    static CommandLine cmd;

    @Override
    public void start(Stage stage) throws IOException {
        FileService fileService = new FileService();

        Font.loadFont(getClass().getResourceAsStream("fonts/Prompt-Regular.ttf"), 15);
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("job.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 992, 1020);
        UIController ui = fxmlLoader.getController();
        ui.setInvoicePreview(fileService.getPreviewImage());

        stage.setTitle("Receipt Generator!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        DBService db = new DBService();
        InvoiceDocument doc;

        try {
            db.connect();
            ReceiptController controller = new ReceiptController(db.getConnection());

            cmd = new Commands().parseCommands(args);
            Receipt rcp = controller.fetchByClientId(cmd.getOptionValue("doc"));
            List<Product> prods = controller.findProductsByDocumentNumber(cmd.getOptionValue("doc"));

            if (cmd.hasOption("p")) {
                doc = new InvoiceDocument(rcp, prods);
                doc.writeToDocument().exportToPDF().previewImage();

                launch(args);
            }
        } catch (Exception e) {
            System.out.println("An error occured while accessing the database");
            e.printStackTrace();
        } finally {
            db.close();
        }
    }
}