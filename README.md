# SuperDuperMarkt Application
Welcome to **SuperDuperMarkt**, a console-based application that simulates the management of different types of products, such as cheese, wine, meat, and other common items. This application allows users to add products, simulate quality changes over time, and manage inventory based on product characteristics.

## Table of Contents
- [Features](#features)
- [Implementation Logic](#implementation-logic)
- [Installation](#installation)
- [Usage](#usage)
- [Product Types](#product-types)
- [Testing](#testing)
## Features
- **Add Products**: Input various products such as cheese, wine, meat, or common items through the console.
- **Simulate Quality Changes**: Simulate how product quality changes over a specified number of days.
- **Manage Inventory**: View all added products and their current state.
- **CSV Importing**: Import products from a CSV file or a SQL table.
- **Dynamic Product Handling**: Easily extend the application to handle new types of products.
## Implementation Logic
The abstract class `Product` is the base for the whole logic of the program. It holds all the main attributes for the products and allows further dynamic development of the environment. The developer can add multiple types of products that will extend the abstract class, add new functionalities and will allow the desired scalability for the program. The classes `PackageClassNameFinder` and `ProductImporter` allow for further scalability of the program, as they add the options to easily find and create the different types of products and import them from different sources.
## Installation
1. Clone the Repository:

```bash
 git clone https://github.com/yourusername/superdupermarket.git
 cd superdupermarket
```
2. Build the Project:

- Use your preferred Java IDE (e.g., Eclipse, IntelliJ) or build the project using Maven.
3. Run the Application:

- In your IDE, run the Application.java file.
- Or from the command line (if you have Java installed):
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
 4. Import products from CSV.
 5. Import products from SQL.
 6. Exit
```
### Adding an Item
- Choose **1** to add an item.
- Follow the prompts to select the product type, input the product ID, quality (0-100), expiry date (in `dd-mm-yyyy` format), and base price.
### Showing Added Items
- You can view all added items with their current state by selecting the "Show added items" option.
### Simulating Quality Changes
- After adding items, you can simulate quality changes over a number of days by choosing the appropriate option from the menu.
- The application will show how the quality of each item changes over time based on the rules defined for each product type.
### Importing products from CSV
- Products can be imported from a CSV file, from a path provided by the user and the file must include these properties: type, id, name, quality, expiry_date, base_price, meat_type, is_vacuum_packed(for Meat)
### Importing products from SQL
- Products can be imported from a SQL connection, details of which are provided by the user and the table must include these properties: type, id, name, quality, expiry_date, base_price, meat_type, is_vacuum_packed(for Meat)
### Exiting the Application
- Choose `Exit` to close the application.

## Product Types
- **Cheese**: Cheese loses quality by 1 each day and can only be sold if its quality is 30 or higher. It gains daily price change.
- **Wine**: Wine does not lose quality and gains +1 quality every 10 days after its expiry date until a quality of 50 is reached. Wines do not change prices once on the stands.
- **Meat**: There are different types of meat that can be stored either freshly cut or in a vacuumed packaging. Depending on the storage they can have different expiration dates and most of the time the freshly cut meat does not have an expiration date and will be set dynamially. The expiration date of freshly cut meat is between 2 and 5 days and 5 to 10 days for vacuum packed meat. Meat also loses quality depending on the meat types lasting days, whether it's vacuum packaged or freshly cut. After the expiration date it loses twice as much quality. The quality of the meat must be at least 50. If it drops below 50 it must be removed from the shells.
- **Common Product**: A general product type with customizable attributes.

## Testing
This is a Mavin project and it has Java Unit Tests. The project details are described in the `pom.xml` file. 
### Available Tests
Tests are available for the following classes: **Product**, **Cheese**, **Meat**, **Wine**, **ProductImporter**, **PackageClassNameFinderTest** 
### Running the tests
To run the tests simply open the project in your IDE of choice (preferably Eclipse), right-click on the project and run as a JUnit Test. You can also use Maven to run all tests with `mvn clean test`
