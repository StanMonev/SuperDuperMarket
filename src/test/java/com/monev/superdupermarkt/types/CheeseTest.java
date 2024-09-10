package com.monev.superdupermarkt.types;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

public class CheeseTest {

   private Cheese cheese;

   @BeforeEach
   public void setUp() {
      // Create a Cheese instance with valid parameters
      cheese = new Cheese("002", "Gouda", 35.0, LocalDate.now().plusDays(75), 15.0);
   }

   @Test
   public void testInvalidCheeseQuality() {
      Exception exception = assertThrows(IllegalArgumentException.class, () -> {
         new Cheese("003", "Cheddar", 29.0, LocalDate.now().plusDays(75), 12.0);
      });
      assertEquals("Cheese quality must be at least 30 to be put on stands.", exception.getMessage());
   }

   @Test
   public void testUpdateQuality() {
      double initialQuality = cheese.getQuality();
      cheese.updateQuality();
      assertEquals(initialQuality - 1, cheese.getQuality());
   }

   @Test
   public void testToString() {

      StringBuilder sb = new StringBuilder();
      sb.append("Product ID: " + cheese.getId());
      sb.append(System.getProperty("line.separator"));
      sb.append("Product Name: " + cheese.getName());
      sb.append(System.getProperty("line.separator"));
      sb.append("Current Quality: " + cheese.getQuality());
      sb.append(System.getProperty("line.separator"));
      sb.append("Expiry Date: " + cheese.getExpiryDate());
      sb.append(System.getProperty("line.separator"));
      sb.append("Daily Price: " + cheese.calculateDailyPrice() + "€");
      sb.append(System.getProperty("line.separator"));
      sb.append("Should be removed from shells: ");
      sb.append((cheese.getQuality() <= 30) ? "Yes" : "No");
      sb.append(System.getProperty("line.separator"));

      assertEquals(sb.toString(), cheese.toString());
   }
}
