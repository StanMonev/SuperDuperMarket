package com.monev.superdupermarkt.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TestUtil {

   public static LocalDate getDateInRangeFromToday(int min, int max) {
      return LocalDate.now().plusDays((long) Math.floor(Math.random() * (max - min + 1) + min));
   }

   public static String getDateToString(LocalDate date) {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      return date.format(formatter);
   }

}
