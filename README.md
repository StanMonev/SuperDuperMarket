# SuperDuperMarket Application
Welcome to **SuperDuperMarket**, a console-based application that simulates the management of different types of products, such as cheese, wine, and other common items. This application allows users to add products, simulate quality changes over time, and manage inventory based on product characteristics.

## Table of Contents
- [Features](#features)
- [Project Structure](#project-structure)
- [Installation](#installation)
- [Usage](#usage)
- [Product Types](#product-types)
- [Simulating Quality Changes](#simulating-quality-changes)
- [Extending the Application](#extending-the-application)
## Features
- **Add Products**: Input various products such as cheese, wine, or common items through the console.
- **Simulate Quality Changes**: Simulate how product quality changes over a specified number of days.
- **Manage Inventory**: View all added products and their current state.
- **CSV Importing**: Import products from a CSV file (feature in development).
- **Dynamic Product Handling**: Easily extend the application to handle new types of products.
## Project Structure
```
src/
└── com/
    └── monev/
        └── superdupermarkt/
            ├── Application.java
            ├── Product.java
            ├── util/
            │   └── PackageClassFinder.java
            |   └── CSVImporter.java
            └── types/
                ├── Cheese.java
                ├── Wine.java
                └── CommonProduct.java
```
## Key Classes
- **Application.java**: The entry point for the console application.
- **Product.java**: The abstract base class for all products.
- **CSVImporter.java**: Handles the import of products from CSV files.
- **PackageClassFinder.java**: Utility class for dynamically finding classes within a package.
- **Cheese.java, Wine.java, CommonProduct.java**: Specific implementations of different product types.
## Installation
1. Clone the Repository:

```bash
 git clone https://github.com/yourusername/superdupermarket.git
 cd superdupermarket
```
2. Build the Project:

- Use your preferred Java IDE (e.g., Eclipse, IntelliJ) or build the project using Maven/Gradle.
3. Run the Application:

- In your IDE, run the Application.java file.
- Or from the command line:
```bash
 java -cp target/classes com.monev.superdupermarkt.Application
```
## Usage
Once the application starts, you will see the following prompt:

```
 Welcome to SuperDuperMarket! Do you wish to:
 1. Add an item.
 2. Show added items.
 3. Simulate item quality for days.
 4. Exit
```
### Adding an Item
- Choose **1** to add an item.
- Follow the prompts to select the product type, input the product ID, quality (0-100), expiry date (in `dd-mm-yyyy` format), and base price.
### Showing Added Items
- You can view all added items with their current state by selecting the "Show added items" option.
### Simulating Quality Changes
- After adding items, you can simulate quality changes over a number of days by choosing the appropriate option from the menu.
- The application will show how the quality of each item changes over time based on the rules defined for each product type.
### Exiting the Application
- Choose `Exit` to close the application.
## Product Types
- **Cheese**: Cheese loses quality by 1 each day and can only be sold if its quality is 30 or higher. It gains daily price change.
- **Wine**: Wine does not lose quality and gains +1 quality every 10 days after its expiry date until a quality of 50 is reached. Wines do not change prices once on the stands.
- **Common Product**: A general product type with customizable attributes.
## Simulating Quality Changes
The application allows you to simulate the quality changes for all added products over a specified number of days. The simulation is done on copies of the products to ensure the original data remains unchanged.

## Extending the Application
The application is designed to be easily extensible:

- **Adding New Product Types**: Create a new class in the `com.monev.superdupermarkt.types` package that extends the `Product` class and implement the `updateQuality` method.
- **Dynamic Handling**: The application dynamically discovers and handles all product types found in the `types` package.
