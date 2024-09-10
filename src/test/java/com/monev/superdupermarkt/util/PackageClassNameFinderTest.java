package com.monev.superdupermarkt.util;

import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PackageClassNameFinderTest {

   @Test
   public void testGetClassNamesInPackage() {
      try {
         // Assuming the package "com.monev.superdupermarkt.types" contains CheeseTest,
         // WineTest, and CommonProductTest classes
         String packageName = "com.monev.superdupermarkt.types";
         List<String> classNames = PackageClassNameFinder.getClassNamesInPackage(packageName);

         // Validate that the expected class names are found and properly formatted
         assertTrue(classNames.contains("Cheese Test"), "Expected class name 'Cheese Test' not found.");
         assertTrue(classNames.contains("Wine Test"), "Expected class name 'Wine Test' not found.");
         assertTrue(classNames.contains("Common Product Test"), "Expected class name 'Common Product Test' not found.");

         // Ensure "Common Product" is the first in the list if present
         if (classNames.contains("Common Product Test")) {
            assertEquals("Common Product Test", classNames.get(0),
                  "'Common Product Test' should be the first class in the list.");
         }

      } catch (ClassNotFoundException | UnsupportedEncodingException e) {
         fail("Exception occurred while getting class names in package: " + e.getMessage());
      }
   }

   @Test
   public void testFormatClassName() {
      // Testing the private method formatClassName via reflection
      try {
         String inputClassName = "com.monev.superdupermarkt.types.CommonProduct";
         String expectedOutput = "Common Product";

         String formattedName = invokeFormatClassName(inputClassName);

         assertEquals(expectedOutput, formattedName, "The class name formatting is incorrect.");

      } catch (Exception e) {
         fail("Exception occurred while testing formatClassName: " + e.getMessage());
      }
   }

   /**
    * Helper method to invoke the private formatClassName method using reflection.
    */
   private String invokeFormatClassName(String className) throws Exception {
      java.lang.reflect.Method method = PackageClassNameFinder.class.getDeclaredMethod("formatClassName", String.class);
      method.setAccessible(true);
      return (String) method.invoke(null, className);
   }
}
