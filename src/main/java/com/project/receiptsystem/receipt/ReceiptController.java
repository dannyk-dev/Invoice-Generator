package com.project.receiptsystem.receipt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReceiptController {
    private ArrayList<Receipt> allReceipts = new ArrayList<>();
    private ArrayList<Product> allProducts = new ArrayList<>();
    Connection dbConn;

    public ReceiptController(Connection dbConn) {
        this.dbConn = dbConn;
//        this.setAllReceipts(this.fetchAllReceipts(dbConn));
//        this.setAllProducts(this.fetchAllProducts(dbConn));
    }

    private ArrayList<Receipt> fetchAllReceipts(Connection dbConn) {
        try {
            Statement statement = dbConn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM SQLUser.FACTURASRSA");

            while (resultSet.next())
                this.allReceipts.add(new Receipt(resultSet));

        } catch (Exception e) {
            System.out.println("Error fetching all receipts.");
            e.printStackTrace();
        }

        return this.allReceipts;
    }

    private ArrayList<Product> fetchAllProducts(Connection dbConn) {
        try {
            Statement statement = dbConn.createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM SQLUser.PRODUTOSFRSA");

            while (res.next())
                this.allProducts.add(new Product(res));

        } catch (SQLException e) {
            System.out.println("Error fetching products");
            e.printStackTrace();
        }

        return this.allProducts;
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

    public ArrayList<Receipt> getAllReceipts() {
        return allReceipts;
    }

    public void setAllReceipts(ArrayList<Receipt> allReceipts) {
        this.allReceipts = allReceipts;
    }

    public ArrayList<Product> getAllProducts() {
        return allProducts;
    }

    public void setAllProducts(ArrayList<Product> allProducts) {
        this.allProducts = allProducts;
    }


}
