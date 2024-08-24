package com.monev.superdupermarkt.util;

import com.monev.superdupermarkt.Product;
import com.monev.superdupermarkt.types.Cheese;
import com.monev.superdupermarkt.types.CommonProduct;
import com.monev.superdupermarkt.types.Wine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductImporter {

    private static final String CSV_SPLIT_BY = ",";

    public static List<Product> importFromCSV(String fileName) {
        List<Product> products = new ArrayList<>();
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            br.readLine(); // Skip the header line

            while ((line = br.readLine()) != null) {
                String[] values = line.split(CSV_SPLIT_BY);
                String type = values[0];
                String id = values[1];
                int quality = Integer.parseInt(values[2]);
                LocalDate expiryDate = getDate(values[3]);
                double defaultPrice = Double.parseDouble(values[4]);

                try {
                    products.add(createProduct(type, id, quality, expiryDate, defaultPrice));
                } catch (Exception e) {
                    System.err.println("Failed to create product of type " + type + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return products;
    }

    public static List<Product> importFromSQL(Connection connection, String tableName) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM " + tableName;

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            while (rs.next()) {
                String type = rs.getString("type");
                String id = rs.getString("id");
                int quality = rs.getInt("quality");
                LocalDate expiryDate = rs.getDate("expiry_date").toLocalDate();
                double defaultPrice = rs.getDouble("base_price");

                try {
                    products.add(createProduct(type, id, quality, expiryDate, defaultPrice));
                } catch (Exception e) {
                    System.err.println("Failed to create product of type " + type + ": " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    private static Product createProduct(String type, String id, int quality, LocalDate expiryDate, double defaultPrice) {
        switch (type) {
            case "Cheese":
                return new Cheese(id, quality, expiryDate, defaultPrice);
            case "Wine":
                return new Wine(id, quality, expiryDate, defaultPrice);
            default:
                return new CommonProduct(id, quality, expiryDate, defaultPrice);
        }
    }

    private static LocalDate getDate(String s) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy").withLocale(Locale.GERMAN);
        return LocalDate.parse(s, formatter);
    }
}
