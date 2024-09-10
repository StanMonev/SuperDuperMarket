package com.monev.superdupermarkt.types;

import java.time.LocalDate;

import com.monev.superdupermarkt.Product;

/**
 * This class is the default extension of the product class. The quality change
 * of the CommonProduct is quality - 24 (1 for each hour of the day) * 0.01
 */
public class CommonProduct extends Product {

   public CommonProduct(String id, String name, double quality, LocalDate expiryDate, double defaultPrice) {
      super(id, name, quality, expiryDate, defaultPrice);

      if (expiryDate == null) {
         setExpiryDate(LocalDate.now());
      }
   }

   @Override
   public void updateQuality() {
      if (getQuality() > 0) {
         setQuality((double) (Math.round((getQuality() - 0.24) * 100) / 100.0));
      }
   }

   @Override
   public boolean isExpired() {
      return LocalDate.now().isAfter(getExpiryDate());
   }

}
