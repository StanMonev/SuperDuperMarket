package com.monev.superdupermarkt;

import com.monev.superdupermarkt.types.Cheese;
import com.monev.superdupermarkt.types.CommonProduct;
import com.monev.superdupermarkt.types.Wine;
import com.monev.superdupermarkt.util.PackageClassNameFinder;
import com.monev.superdupermarkt.util.ProductImporter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Application {

    private static final List<Product> products = new ArrayList<>();
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Welcome to SuperDuperMarket! Do you wish to:");
            System.out.println("1. Add an item.");
            System.out.println("2. Show added items.");
            System.out.println("3. Simulate item quality for days.");
            System.out.println("4. Import products from CSV.");
            System.out.println("5. Import products from SQL.");
            System.out.println("6. Exit");

            String input = scanner.nextLine();
            int choice = validateIntegerInput(input, 1, 4);

            switch (choice) {
                case 1:
                    addItem(scanner);
                    break;
                case 2:
                    showAddedItems();
                    break;
                case 3:
                    simulateItemQuality(scanner);
                    break;
                case 4:
                    importFromCSV(scanner);
                    break;
                case 5:
                    importFromSQL(scanner);
                    break;
                case 6:
                    System.out.println("Exiting SuperDuperMarket. Goodbye!");
                    scanner.close();
                    return;
                default:
                	System.err.println("Invalid option. Please try again.");
            }
        }
    }
    
    /**
     * 
     * @param scanner
     */

    private static void addItem(Scanner scanner) {
        System.out.println("Choose the type of item:");
        try {
        	List<String> classNames = PackageClassNameFinder.getClassNamesInPackage("com.monev.superdupermarkt.types");
        	int i = 1;
        	for(String name: classNames) {
        		System.out.println(i + ". "+ name);
        		i++;
        	}
        }catch(Exception e) {
        	System.err.println("There was an error trying to find the product types in the project. The program will now close.");
        	System.exit(1);
        }

        String typeInput = scanner.nextLine();
        int typeChoice = validateIntegerInput(typeInput, 1, 3);

        if (typeChoice == -1) {
        	System.err.println("Invalid item type. Returning to main menu.");
            return;
        }

        System.out.println("Enter Product ID:");
        String id = scanner.nextLine();

        System.out.println("Enter Product Quality (0-100):");
        int quality = validateIntegerInput(scanner.nextLine(), 0, 100);

        if (quality == -1) {
        	System.err.println("Invalid quality value. Must be between 0 and 50. Returning to main menu.");
            return;
        }

        System.out.println("Enter Product Expiry Date (dd-mm-yyyy):");
        LocalDate expiryDate = validateDateInput(scanner.nextLine());

        if (expiryDate == null) {
            System.err.println("Invalid date format. Returning to main menu.");
            return;
        }

        System.out.println("Enter Base Price (e.g., 1.00):");
        double basePrice = validateDoubleInput(scanner.nextLine());

        if (basePrice == -1) {
            System.err.println("Invalid price format. Must be a valid number (e.g., 1.00). Returning to main menu.");
            return;
        }

        Product product = null;

        try {
            switch (typeChoice) {
                case 1:
                    product = new Cheese(id, quality, expiryDate, basePrice);
                    break;
                case 2:
                    product = new Wine(id, quality, expiryDate, basePrice);
                    break;
                case 3:
                    product = new CommonProduct(id, quality, expiryDate, basePrice);
                    break;
                default:
                    System.err.println("Invalid type selected. Returning to main menu.");
                    return;
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage() + " Returning to main menu.");
            return;
        }

        products.add(product);
        System.out.println("Product added successfully.");
    }

    private static void showAddedItems() {
        if (products.isEmpty()) {
            System.err.println("No products added yet.");
        } else {
            for (Product product : products) {
                System.out.println(product);
            }
        }
    }

    private static void simulateItemQuality(Scanner scanner) {
        if (products.isEmpty()) {
            System.err.println("No products added yet.");
            return;
        }

        System.out.println("Enter the number of days to simulate:");
        String daysInput = scanner.nextLine();
        int days = validateIntegerInput(daysInput, 1, Integer.MAX_VALUE);

        if (days == -1) {
            System.err.println("Invalid number of days. Returning to main menu.");
            return;
        }

        for (Product product : products) {
        	Product productClone = product.clone(); 
            System.out.println("Simulating quality changes for Product ID: " + productClone.getId());
            productClone.simulateQualityChange(days);
        }
    }
    
    private static void importFromCSV(Scanner scanner) {
        System.out.println("Enter CSV file path:");
        String filePath = scanner.nextLine();
        List<Product> importedProducts = ProductImporter.importFromCSV(filePath);
        products.addAll(importedProducts);
        System.out.println("Imported " + importedProducts.size() + " products from CSV.");
    }

    private static void importFromSQL(Scanner scanner) {
        System.out.println("Enter SQL database connection string:");
        String connectionString = scanner.nextLine();
        System.out.println("Enter SQL username:");
        String username = scanner.nextLine();
        System.out.println("Enter SQL password:");
        String password = scanner.nextLine();
        System.out.println("Enter table name:");
        String tableName = scanner.nextLine();

        try (Connection connection = DriverManager.getConnection(connectionString, username, password)) {
            List<Product> importedProducts = ProductImporter.importFromSQL(connection, tableName);
            products.addAll(importedProducts);
            System.out.println("Imported " + importedProducts.size() + " products from SQL database.");
        } catch (Exception e) {
            System.err.println("Failed to import products from SQL: " + e.getMessage());
        }
    }

    private static int validateIntegerInput(String input, int min, int max) {
        try {
            int value = Integer.parseInt(input);
            if (value >= min && value <= max) {
                return value;
            }
        } catch (NumberFormatException e) {
            // Ignore and return -1 to indicate invalid input
        }
        return -1;
    }

    private static LocalDate validateDateInput(String input) {
        try {
            return LocalDate.parse(input, dateFormatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private static double validateDoubleInput(String input) {
        try {
            double value = Double.parseDouble(input);
            if (value >= 0) {
                return value;
            }
        } catch (NumberFormatException e) {
            // Ignore and return -1 to indicate invalid input
        }
        return -1;
    }
}
