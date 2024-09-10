package com.monev.superdupermarkt.util;

import com.monev.superdupermarkt.Product;
import com.monev.superdupermarkt.types.Cheese;
import com.monev.superdupermarkt.types.Wine;
import com.monev.superdupermarkt.types.CommonProduct;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProductImporterCSVTest {

   private LocalDate cheeseExpiryDate;
   private LocalDate wineExpiryDate;
   private LocalDate commonProductExpiryDate;

   @Test
   public void testReadProductsFromCSV() throws IOException {
      // Create a temporary CSV file for testing
      File tempFile = File.createTempFile("test-products", ".csv");
      tempFile.deleteOnExit();

      // Define expiry dates for the test products
      cheeseExpiryDate = TestUtil.getDateInRangeFromToday(50, 100);
      wineExpiryDate = TestUtil.getDateInRangeFromToday(1, 100);
      commonProductExpiryDate = TestUtil.getDateInRangeFromToday(1, 100);

      // Format dates to the correct string format
      String formattedCheeseExpiryDate = TestUtil.getDateToString(cheeseExpiryDate);
      String formattedWineExpiryDate = TestUtil.getDateToString(wineExpiryDate);
      String formattedCommonProductExpiryDate = TestUtil.getDateToString(commonProductExpiryDate);

      // Write sample data to the CSV file
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
         writer.write("type,id,name,quality,expiryDate,defaultPrice\n");
         writer.write("Cheese,C001,SuperDuperCheese,35," + formattedCheeseExpiryDate + ",5.00\n");
         writer.write("Wine,W001,SuperDuperWine,45," + formattedWineExpiryDate + ",10.00\n");
         writer.write("CommonProduct,CP001,SuperDuperBread,20," + formattedCommonProductExpiryDate + ",2.50\n");
      }

      // Use the ProductImporter to import products from the CSV file
      List<Product> products = ProductImporter.importFromCSV(tempFile.getAbsolutePath());

      // Assert that the correct number of products were imported
      assertEquals(3, products.size(), "There should be 3 products imported from the CSV.");

      // Validate each product's details
      Product cheese = products.get(0);
      assertTrue(cheese instanceof Cheese, "The first product should be of type Cheese.");
      assertEquals("C001", cheese.getId(), "Cheese ID should be C001.");
      assertEquals("SuperDuperCheese", cheese.getName(), "Cheese Name should be SuperDuperCheese.");
      assertEquals(35, cheese.getQuality(), "Cheese quality should be 35.");
      assertEquals(cheeseExpiryDate, cheese.getExpiryDate(), "Cheese expiry date should match.");
      assertEquals(5.00, cheese.getDefaultPrice(), 0.01, "Cheese default price should be 5.00.");

      Product wine = products.get(1);
      assertTrue(wine instanceof Wine, "The second product should be of type Wine.");
      assertEquals("W001", wine.getId(), "Wine ID should be W001.");
      assertEquals("SuperDuperWine", wine.getName(), "Wine Name should be SuperDuperWine.");
      assertEquals(45, wine.getQuality(), "Wine quality should be 45.");
      assertEquals(wineExpiryDate, wine.getExpiryDate(), "Wine expiry date should match.");
      assertEquals(10.00, wine.getDefaultPrice(), 0.01, "Wine default price should be 10.00.");

      Product commonProduct = products.get(2);
      assertTrue(commonProduct instanceof CommonProduct, "The third product should be of type CommonProduct.");
      assertEquals("CP001", commonProduct.getId(), "Common Product ID should be CP001.");
      assertEquals("SuperDuperBread", commonProduct.getName(), "Common Product Name should be SuperDuperBread.");
      assertEquals(20, commonProduct.getQuality(), "Common Product quality should be 20.");
      assertEquals(commonProductExpiryDate, commonProduct.getExpiryDate(), "Common Product expiry date should match.");
      assertEquals(2.50, commonProduct.getDefaultPrice(), 0.01, "Common Product default price should be 2.50.");
   }
}
