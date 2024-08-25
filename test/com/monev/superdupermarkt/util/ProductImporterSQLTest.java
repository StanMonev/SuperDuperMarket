package com.monev.superdupermarkt.util;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.monev.superdupermarkt.Product;

class ProductImporterSQLTest {
	
    private Connection connection;
    private LocalDate cheeseExpiryDate;	
    private LocalDate wineExpiryDate;
	private LocalDate commonProductExpiryDate;

	@BeforeEach
	void setUp() throws Exception {
        // Set up the in-memory database connection
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        
        cheeseExpiryDate = TestUtil.getDateInRangeFromToday(50, 100);
        wineExpiryDate = TestUtil.getDateInRangeFromToday(1, 100);
        commonProductExpiryDate = TestUtil.getDateInRangeFromToday(1, 100);
        
        String formattedCheeseExpiryDate = TestUtil.getDateToString(cheeseExpiryDate);
        String formattedWineExpiryDate = TestUtil.getDateToString(wineExpiryDate);
        String formattedCommonProductExpiryDate = TestUtil.getDateToString(commonProductExpiryDate);

        try (Statement stmt = connection.createStatement()) {
            // Create a table and insert test data
            String createTableSQL = "CREATE TABLE products (" +
                    "type VARCHAR(50), " +
                    "id VARCHAR(50), " +
                    "name VARCHAR(50), " +
                    "quality INT, " +
                    "expiry_date DATE, " +
                    "base_price DOUBLE)";
            stmt.execute(createTableSQL);

            String insertDataSQL = "INSERT INTO products (type, id, name, quality, expiry_date, base_price) VALUES " +
                    "('Cheese', 'C001', 'SuperDuperCheese', 40, '"+formattedCheeseExpiryDate+"', 5.0), " +
                    "('Wine', 'W001', 'SuperDuperWine', 10, '"+formattedWineExpiryDate+"', 15.0), " +
                    "('CommonProduct', 'P001', 'SuperDuperBread', 30, '"+formattedCommonProductExpiryDate+"', 10.0)";
            stmt.execute(insertDataSQL);
        }
	}

	@AfterEach
	void tearDown() throws Exception {
        if (connection != null) {
            connection.close();
        }
	}

	@Test
	void test() {
        List<Product> products = ProductImporter.importFromSQL(connection, "products");
        
        

        // Check if the right number of products are imported
        assertEquals(3, products.size());

        // Validate the details of the imported products
        Product cheese = products.get(0);
        assertEquals("C001", cheese.getId());
        assertEquals("SuperDuperCheese", cheese.getName());
        assertEquals(40, cheese.getQuality());
        assertEquals(cheeseExpiryDate, cheese.getExpiryDate());
        assertEquals(5.0, cheese.getDefaultPrice(), 0.01);

        Product wine = products.get(1);
        assertEquals("W001", wine.getId());
        assertEquals("SuperDuperWine", wine.getName());
        assertEquals(10, wine.getQuality());
        assertEquals(wineExpiryDate, wine.getExpiryDate());
        assertEquals(15.0, wine.getDefaultPrice(), 0.01);

        Product commonProduct = products.get(2);
        assertEquals("P001", commonProduct.getId());
        assertEquals("SuperDuperBread", commonProduct.getName());
        assertEquals(30, commonProduct.getQuality());
        assertEquals(commonProductExpiryDate, commonProduct.getExpiryDate());
        assertEquals(10.0, commonProduct.getDefaultPrice(), 0.01);
	}

}
