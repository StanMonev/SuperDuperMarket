package com.monev.superdupermarkt;

import com.monev.superdupermarkt.types.Cheese;
import com.monev.superdupermarkt.types.CommonProduct;
import com.monev.superdupermarkt.types.Meat;
import com.monev.superdupermarkt.types.Wine;
import com.monev.superdupermarkt.util.MeatTypeEnum;
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

/**
 * This is the main application class that starts the SuperDuperMarkt application.
 */
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
			System.out.println();
			System.out.println("--------------------------------------------------------------------------");
			System.out.println();

            String input = scanner.nextLine();
            int choice = validateIntegerInput(input, 1, 6);

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
            
            // Sleep for a second so that all outputs are printed out before the next menu prompt.
            try {
				Thread.sleep(1000);
				System.out.println();
				System.out.println("-----------------------------------Menu---------------------------------------");
				System.out.println();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
    }
    
    /**
     * Prompts the user to add a new product to the system.
     * The user is guided through selecting a product type and providing the necessary details.
     *
     * @param scanner Scanner object to read user input
     */

    private static void addItem(Scanner scanner) {
        System.out.println("Choose the type of item:");
        List<String> classNames = null;
        try {
        	// Dynamically retrieve and display product types available in the system
        	classNames = PackageClassNameFinder.getClassNamesInPackage("com.monev.superdupermarkt.types");
        	int i = 1;
        	for(String name: classNames) {
        		System.out.println(i + ". "+ name);
        		i++;
        	}
        }catch(Exception e) {
        	System.err.println("There was an error trying to find the product types in the project. The program will now close.");
        	System.exit(1);
        }
    	
        Product product = null;

        try {
        	product = getProduct(scanner, classNames);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage() + " Returning to main menu.");
            return;
        }

        products.add(product);
        System.out.println("Product added successfully.");
    }
    
    /**
     * Retrieves a product from the users input.
     * @param scanner Scanner object to read user input.
     * @param classNames List of class names that contain the different product types.
     * @return The product the user wants to add to the inventory.
     * @throws IllegalArgumentException
     */
    private static Product getProduct(Scanner scanner, List<String> classNames) throws IllegalArgumentException {
    	Product product = null;
        String typeInput = scanner.nextLine();
        int typeChoice = validateIntegerInput(typeInput, 1, classNames.size());

        if (typeChoice == -1) {
        	throw new IllegalArgumentException("Invalid item type. Returning to main menu.");
        }

        System.out.println("Enter Product ID:");
        String id = scanner.nextLine();
        
        if(containsID(products, id)) {
        	throw new IllegalArgumentException("Product with the same ID has already been added. Returning to main menu.");
        }
        
        System.out.println("Enter Product Name:");
        String name = scanner.nextLine();

        System.out.println("Enter Product Quality (0-100):");
        int quality = validateIntegerInput(scanner.nextLine(), 0, 100);

        if (quality == -1) {
        	throw new IllegalArgumentException("Invalid quality value. Must be between 0 and 50. Returning to main menu.");
        }

        System.out.println("Enter Product Expiry Date ('dd-mm-yyyy' or 'none' if there is no expiration date or is unknown, Cheese CANNOT be none!):");
        LocalDate expiryDate = validateDateInput(scanner.nextLine());

        System.out.println("Enter Base Price (e.g., 1.00):");
        double basePrice = validateDoubleInput(scanner.nextLine());

        if (basePrice == -1) {
        	throw new IllegalArgumentException("Invalid price format. Must be a valid number (e.g., 1.00). Returning to main menu.");
        }
        switch (classNames.get(typeChoice-1)) {
            case "Common Product":
                product = new CommonProduct(id, name, quality, expiryDate, basePrice);
                break;
            case "Cheese":
                product = new Cheese(id, name, quality, expiryDate, basePrice);
                break;
            case "Wine":
                product = new Wine(id, name, quality, expiryDate, basePrice);
                break;
            case "Meat":
            	System.out.println("Enter Meat Type:");
            	int i = 1;
            	for(MeatTypeEnum type: MeatTypeEnum.values()) {
                	System.out.println(i+". " + type.toString());
                	i++;
            	}
                MeatTypeEnum meatType = getMeatTypeChoice(scanner.nextLine());
                if(meatType == null) {
                    throw new IllegalArgumentException("Invalid input.");
                }

                System.out.println("Is the meat vacuum packed?  1.Yes  2.No ");
                int choice = validateIntegerInput(scanner.nextLine(), 1, 2);
                if(choice == -1) {
                    throw new IllegalArgumentException("Invalid input.");
                }
                boolean isVacuumPacked = choice == 1 ? true : false;

                product = new Meat(id, name, quality, expiryDate, basePrice, meatType, isVacuumPacked);
                break;
            default:
            	throw new IllegalArgumentException("Invalid type selected. Returning to main menu.");
        }
        
        return product;
    }
    
    /**
     * Retrieves the type of meat from the user input
     * @param input
     * @return
     */
    private static MeatTypeEnum getMeatTypeChoice(String input) {
    	int choice = validateIntegerInput(input, 1, MeatTypeEnum.values().length);
    	if(choice == -1) return null;
    	return MeatTypeEnum.values()[choice-1];
    }

    
    /**
     * Displays all the products currently added to the system.
     * If no products have been added, it displays a relevant message.
     */
    private static void showAddedItems() {
        if (products.isEmpty()) {
            System.err.println("No products added yet.");
        } else {
            for (Product product : products) {
                System.out.println(product);
            }
        }
    }


    /**
     * Simulates quality changes over a user-specified number of days for all added products.
     * This method clones each product to avoid altering the original product's state.
     *
     * @param scanner Scanner object to read user input
     */
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
        
        // Clone each product and simulate the quality change for the specified number of days
        for (Product product : products) {
        	Product productClone = product.clone(); 
            System.out.println("Simulating quality changes for Product ID: " + productClone.getId());
            productClone.simulateQualityChange(days);
        }
    }
    
	/**
	 * Imports products from a CSV file. The file path is provided by the user.
	 * The imported products are added to the existing list of products.
	 *
	 * @param scanner Scanner object to read user input
	 */
    private static void importFromCSV(Scanner scanner) {
    	try {
	        System.out.println("Enter CSV file path:");
	        String filePath = scanner.nextLine();
	        List<Product> importedProducts = ProductImporter.importFromCSV(filePath);
	        products.addAll(importedProducts);
	        System.out.println("Imported " + importedProducts.size() + " products from CSV.");
    	} catch(Exception e) {
    		System.err.println("There was a error, while importing the data: " + e);
    		System.err.println("Returning to main menu.");
    	}
    }

    
    /**
     * Imports products from an SQL database. The connection details are provided by the user.
     * The imported products are added to the existing list of products.
     *
     * @param scanner Scanner object to read user input
     */
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
    		System.err.println("Returning to main menu.");
        }
    }

    /**
     * Validates user input as an integer within a specified range.
     * Returns -1 if the input is invalid.
     *
     * @param input The input string to validate
     * @param min   The minimum acceptable value
     * @param max   The maximum acceptable value
     * @return The validated integer value or -1 if invalid
     */
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

    /**
     * Validates the user input as a date by parsing it.
     * It will return null if an error occurred.
     * @param input The input string to validate
     * @return The validated date as LocalDate or null if there was an error
     */
    private static LocalDate validateDateInput(String input) throws IllegalArgumentException {
        try {
        	if(!"none".equals(input.toLowerCase())) {
                return LocalDate.parse(input, dateFormatter);
        	}else {
        		return null;
        	}
        } catch (DateTimeParseException e) {
        	throw new IllegalArgumentException("The date could not be parsed. Please check your date input and follow the standard format 'dd-mm-yyyy' or type 'none' if there is not expiration date.");
        }
    }
    
    /**
     * Validates the user input as a double and returns it or -1 if it the input was wrong.
     * @param input The input string to validate
     * @return The validated double value or -1 if invalid.
     */

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
    
    /**
     * Checks if product with the same ID already exists in the list.
     * @param list The list that contains the items
     * @param id The id of the item to be checked
     * @return True if the item is already in the list, otherwise False
     */
    private static boolean containsID(final List<Product> list, final String id){
        return list.stream().filter(o -> o.getId().equals(id)).findFirst().isPresent();
    }
}
