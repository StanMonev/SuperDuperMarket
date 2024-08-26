package com.monev.superdupermarkt.util;

import com.monev.superdupermarkt.Product;
import com.monev.superdupermarkt.types.Cheese;
import com.monev.superdupermarkt.types.CommonProduct;
import com.monev.superdupermarkt.types.Meat;
import com.monev.superdupermarkt.types.Wine;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Utility class for importing products into the SuperDuperMarket system.
 * Supports importing from various formats (CSV, JSON, XML) and SQL databases.
 */
public class ProductImporter {

    // Delimiter used to split CSV file lines
    private static final String CSV_SPLIT_BY = ",";

    // Map to store product creation functions for different product types
    private static final Map<String, BiFunction<String[], LocalDate, Product>> productCreators = new HashMap<>();

    static {
        // Register product creation functions for supported types
        productCreators.put("Cheese", ProductImporter::createCheese);
        productCreators.put("Wine", ProductImporter::createWine);
        productCreators.put("Meat", ProductImporter::createMeat);
        productCreators.put("CommonProduct", ProductImporter::createCommonProduct);
    }

    // Enum for different supported file formats
    public enum FileFormat {
        CSV, JSON, XML
    }

    /**
     * Factory method to import products based on file format.
     *
     * @param fileName The name of the file to import products from.
     * @param format   The format of the file (CSV, JSON, XML).
     * @return A list of imported products.
     */
    public static List<Product> importFromFile(String fileName, FileFormat format) {
        switch (format) {
            case CSV:
                return importFromCSV(fileName);
            case JSON:
            	return null; //Can be implemented in the future
            case XML:
            	return null; //Can be implemented in the future
            default:
                throw new IllegalArgumentException("Unsupported file format: " + format);
        }
    }

    /**
     * Imports products from a CSV file.
     *
     * @param fileName The path to the CSV file.
     * @return A list of products created from the CSV file data.
     */
    public static List<Product> importFromCSV(String fileName) {
        List<Product> products = new ArrayList<>();
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            br.readLine(); // Skip the header line

            while ((line = br.readLine()) != null) {
                String[] values = line.split(CSV_SPLIT_BY);
                String type = values[0];
                try {
                    LocalDate expiryDate = getDate(values[4]);
                    products.add(createProduct(type, values, expiryDate));
                } catch (Exception e) {
                    System.err.println("Failed to create product of type " + type + ": " + e.getMessage());
                    continue;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return products;
    }

    /**
     * Imports products from an SQL database table.
     *
     * @param connection The SQL database connection.
     * @param tableName  The name of the table to import products from.
     * @return A list of products created from the SQL table data.
     */
    public static List<Product> importFromSQL(Connection connection, String tableName) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM " + tableName;

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            while (rs.next()) {
                String type = rs.getString("type");
                String[] values = {
                        rs.getString("type"),
                        rs.getString("id"),
                        rs.getString("name"),
                        String.valueOf(rs.getInt("quality")),
                        rs.getString("expiry_date"),
                        String.valueOf(rs.getDouble("base_price"))
                };
                LocalDate expiryDate = rs.getDate("expiry_date").toLocalDate();

                try {
                    products.add(createProduct(type, values, expiryDate));
                } catch (Exception e) {
                    System.err.println("Failed to create product of type " + type + ": " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    /**
     * Creates a product instance based on the type of product.
     * Supports Cheese, Wine, Meat, and CommonProduct types.
     * New product types can be added by registering them in the `productCreators` map.
     *
     * @param type       The type of product to create (e.g., "Cheese", "Wine", "Meat").
     * @param values     The product details as a string array (e.g., [id, name, quality, expiryDate, defaultPrice]).
     * @param expiryDate The expiry date of the product.
     * @return The created product instance.
     */
    private static Product createProduct(String type, String[] values, LocalDate expiryDate) {
        BiFunction<String[], LocalDate, Product> creator = productCreators.get(type);
        if (creator != null) {
            return creator.apply(values, expiryDate);
        } else {
        	creator = productCreators.get("CommonProduct");
            return creator.apply(values, expiryDate);
        }
    }

    /**
     * Creates a Cheese product.
     *
     * @param values     The product details as a string array.
     * @param expiryDate The expiry date of the product.
     * @return A new Cheese product.
     */
    private static Product createCheese(String[] values, LocalDate expiryDate) {
        return new Cheese(values[1], values[2], Integer.parseInt(values[3]), expiryDate, Double.parseDouble(values[5]));
    }

    /**
     * Creates a Wine product.
     *
     * @param values     The product details as a string array.
     * @param expiryDate The expiry date of the product.
     * @return A new Wine product.
     */
    private static Product createWine(String[] values, LocalDate expiryDate) {
        return new Wine(values[1], values[2], Integer.parseInt(values[3]), expiryDate, Double.parseDouble(values[5]));
    }

    /**
     * Creates a Meat product.
     *
     * @param values     The product details as a string array.
     * @param expiryDate The expiry date of the product.
     * @return A new Meat product.
     */
    private static Product createMeat(String[] values, LocalDate expiryDate) {
        // Assuming meatType and isVacuumPacked values are present in the CSV/JSON/XML/SQL data
    	MeatTypeEnum meatType = MeatTypeEnum.valueOf(values[6].toUpperCase()); // Should always be upper case. Example: "BEEF"
        boolean isVacuumPacked = Boolean.parseBoolean(values[7]); // Example: "true"
        return new Meat(values[1], values[2], Integer.parseInt(values[3]), expiryDate, Double.parseDouble(values[5]), meatType, isVacuumPacked);
    }

    /**
     * Creates a CommonProduct (default product type).
     *
     * @param values     The product details as a string array.
     * @param expiryDate The expiry date of the product.
     * @return A new CommonProduct.
     */
    private static Product createCommonProduct(String[] values, LocalDate expiryDate) {
        return new CommonProduct(values[1], values[2], Integer.parseInt(values[3]), expiryDate, Double.parseDouble(values[5]));
    }

    /**
     * Parses a date string in the format "yyyy-MM-dd" into a LocalDate object.
     * If the string is null it will return a null value.
     *
     * @param dateStr The date string to parse.
     * @return The parsed LocalDate object.
     */
    private static LocalDate getDate(String dateStr) {
    	
    	if(dateStr.equals("null")) return null;
    	
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateStr, formatter);
    }
}
