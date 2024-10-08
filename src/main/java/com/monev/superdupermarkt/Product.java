package com.monev.superdupermarkt;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * This is the base class for all products. It initializes a product with all
 * necessary variables and provides with set, get methods. It is also a
 * cloneable object and can simulate the product changes in given days in the
 * future.
 */
public abstract class Product implements Cloneable {

   private String id;
   private String name;
   private double quality;
   private LocalDate expiryDate;
   private final double defaultPrice;
   private boolean isExpired = false;
   private LocalDate today = LocalDate.now();

   public Product(String id, String name, double quality, LocalDate expiryDate, double defaultPrice) {
      this.id = id;
      this.name = name;
      this.quality = quality;
      this.expiryDate = expiryDate;
      this.defaultPrice = defaultPrice;
   }

   public String getId() {
      return id;
   }

   public String getName() {
      return name;
   }

   public double getQuality() {
      return quality;
   }

   public LocalDate getExpiryDate() {
      return expiryDate;
   }

   public double getDefaultPrice() {
      return defaultPrice;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setQuality(double quality) {
      this.quality = quality;
   }

   public void setExpiryDate(LocalDate expiryDate) {
      this.expiryDate = expiryDate;
   }

   /**
    * Calculates the difference between the current date and the expiration date.
    * 
    * @return The difference as a long value.
    */
   public long getDifferenceToExpiryDate() {
      return ChronoUnit.DAYS.between(today, expiryDate);
   }

   public double calculateDailyPrice() {
      return (double) Math.round((defaultPrice + 0.10 * quality) * 100) / 100;
   }

   public boolean isExpired() {
      return this.isExpired;
   }

   public void setIsExpired(boolean expired) {
      this.isExpired = expired;
   }

   /**
    * Simulates the product change for the given days in the future. It will print
    * the product details and changes for each day.
    * 
    * @param days For how many days the function should simulate the product
    *             changes.
    */
   public void simulateQualityChange(long days) {
      // LocalDate currentDate = LocalDate.now();
      LocalDate endDate = today.plusDays(days);

      while (!today.isAfter(endDate)) {
         System.out.println("Date: " + today);
         System.out.println(this);

         // Update product's quality for the day
         this.updateQuality();

         // Move to the next day
         today = today.plusDays(1);
         this.setIsExpired(today.isAfter(expiryDate));
      }
   }

   @Override
   public Product clone() {
      try {
         return (Product) super.clone();
      } catch (CloneNotSupportedException e) {
         throw new RuntimeException("Cloning not supported", e);
      }
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("Product ID: " + this.getId());
      sb.append(System.getProperty("line.separator"));
      sb.append("Product Name: " + this.getName());
      sb.append(System.getProperty("line.separator"));
      sb.append("Current Quality: " + this.getQuality());
      sb.append(System.getProperty("line.separator"));
      sb.append("Expiry Date: " + this.getExpiryDate());
      sb.append(System.getProperty("line.separator"));
      sb.append("Daily Price: " + this.calculateDailyPrice() + "€");
      sb.append(System.getProperty("line.separator"));
      return sb.toString();
   }

   public abstract void updateQuality();

}
