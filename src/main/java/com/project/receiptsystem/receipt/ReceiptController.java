package com.project.receiptsystem.receipt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReceiptController {
    private ArrayList<Receipt> allReceipts = new ArrayList<>();
    private ArrayList<Product> allProducts = new ArrayList<>();
    Connection dbConn;

    public ReceiptController(Connection dbConn) {
        this.dbConn = dbConn;
    }

    public Receipt fetchByClientId(String clientId) {
        try {
            Statement statement = this.dbConn.createStatement();
            String query = String.format("SELECT * FROM SQLUser.FACTURASRSA WHERE TRIM(B7) = '%s'", clientId.trim());
            ResultSet res = statement.executeQuery(query);

            if (res.next())
                return new Receipt(res);

        } catch (Exception e) {
            System.out.println("Receipt does not exist");
            e.printStackTrace();
        }

        return null;
    }

    public List<Product> findProductsByDocumentNumber(String docNumber) {
        try {
            Statement statement = this.dbConn.createStatement();
            String query = String.format("SELECT * FROM SQLUser.PRODUTOSFRSA WHERE TRIM(A2) = '%s'", docNumber.trim());
            ResultSet res = statement.executeQuery(query);

            while (res.next())
                this.allProducts.add(new Product(res));

            return this.allProducts;
        } catch (Exception e) {
            System.out.println("Products with that Document NUmber does not exist");
            e.printStackTrace();
        }

        return null;
    }
}
