package com.monev.superdupermarkt.types;

import java.time.LocalDate;

import com.monev.superdupermarkt.Product;

/**
 * Represents a type of cheese product in the SuperDuperMarket system. The
 * cheese products must have a quality above 30 and their expiration date must
 * be between 50 and 100 days in the future. Each day a cheese product loses -1
 * quality.
 */
public class Cheese extends Product {

   /**
    * Constructs a Cheese product with the specified properties.
    *
    * @param id           The product ID
    * @param name         The product name
    * @param quality      The initial quality of the cheese (must be at least 30)
    * @param expiryDate   The expiry date of the cheese
    * @param defaultPrice The base price of the cheese
    */
   public Cheese(String id, String name, double quality, LocalDate expiryDate, double defaultPrice) {
      super(id, name, quality, expiryDate, defaultPrice);
      if (quality < 30) {
         throw new IllegalArgumentException("Cheese quality must be at least 30 to be put on stands.");
      }
      if (50 >= getDifferenceToExpiryDate() || getDifferenceToExpiryDate() >= 100) {
         throw new IllegalArgumentException("Cheese expiry date must be between 50 and 100 days in the future.");
      }
   }

   @Override
   public void updateQuality() {
      if (getQuality() > 0) {
         setQuality(getQuality() - 1); // Cheese loses quality by 1 each day
      }
   }

   @Override
   public String toString() {
      String desc = super.toString();
      StringBuilder sb = new StringBuilder(desc);
      sb.append("Should be removed from shells: ");
      sb.append((getQuality() <= 30) ? "Yes" : "No");
      sb.append(System.getProperty("line.separator"));
      return sb.toString();
   }

}
