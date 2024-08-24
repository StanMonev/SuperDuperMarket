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

	@BeforeEach
	void setUp() throws Exception {
        // Set up the in-memory database connection
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");

        try (Statement stmt = connection.createStatement()) {
            // Create a table and insert test data
            String createTableSQL = "CREATE TABLE products (" +
                    "type VARCHAR(50), " +
                    "id VARCHAR(50), " +
                    "quality INT, " +
                    "expiry_date DATE, " +
                    "base_price DOUBLE)";
            stmt.execute(createTableSQL);

            String insertDataSQL = "INSERT INTO products (type, id, quality, expiry_date, base_price) VALUES " +
                    "('Cheese', 'C001', 40, '2024-08-29', 5.0), " +
                    "('Wine', 'W001', 10, '2024-08-29', 15.0), " +
                    "('CommonProduct', 'P001', 30, '2024-08-29', 10.0)";
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
        assertEquals(40, cheese.getQuality());
        assertEquals(LocalDate.of(2024, 8, 29), cheese.getExpiryDate());
        assertEquals(5.0, cheese.getDefaultPrice(), 0.01);

        Product wine = products.get(1);
        assertEquals("W001", wine.getId());
        assertEquals(10, wine.getQuality());
        assertEquals(LocalDate.of(2024, 8, 29), wine.getExpiryDate());
        assertEquals(15.0, wine.getDefaultPrice(), 0.01);

        Product commonProduct = products.get(2);
        assertEquals("P001", commonProduct.getId());
        assertEquals(30, commonProduct.getQuality());
        assertEquals(LocalDate.of(2024, 8, 29), commonProduct.getExpiryDate());
        assertEquals(10.0, commonProduct.getDefaultPrice(), 0.01);
	}

}
