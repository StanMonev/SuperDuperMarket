package com.monev.superdupermarkt;

import static org.junit.Assert.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.monev.superdupermarkt.types.CommonProduct;

public class ProductTest {

   private Product product;

   @BeforeEach
   public void setUp() {
      // Create a CommonProduct instance for testing, as Product is abstract
      product = new CommonProduct("001", "Test Product", 40.0, LocalDate.now().plusDays(10), 10.0);
   }

   @Test
   public void testPositiveDifferenceToExpiryDate() {
      assertEquals(10, product.getDifferenceToExpiryDate(), "The positive difference to the expiry date is incorrect.");
   }

   @Test
   public void testNegativeDifferenceToExpiryDate() {
      CommonProduct product = new CommonProduct("002", "Test Product 2", 40.0, LocalDate.now().minusDays(10), 10.0);
      assertEquals(-10, product.getDifferenceToExpiryDate(),
            "The negative difference to the expiry date is incorrect.");
   }

   @Test
   public void testDailyPriceCalculation() {
      assertEquals(14.00, product.calculateDailyPrice(), 0.01, "Daily price calculation is incorrect.");
   }

   @Test
   public void testClone() throws CloneNotSupportedException {
      Product clonedProduct = (Product) product.clone();
      assertNotSame(product, clonedProduct);
      assertEquals(product.getId(), clonedProduct.getId());
      assertEquals(product.getQuality(), clonedProduct.getQuality());
   }

   @Test
   public void testSimulateQualityChange() {
      product.simulateQualityChange(5);
      assertTrue(product.getQuality() < 40.0); // The quality should have decreased
   }
}
