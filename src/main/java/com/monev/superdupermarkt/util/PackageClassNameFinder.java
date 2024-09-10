package com.monev.superdupermarkt.util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to find and list all class names within a specified Java
 * package. This is useful for dynamically loading classes or for general
 * introspection purposes.
 */
public class PackageClassNameFinder {

   /**
    * Retrieves a list of class names from a specified package. The method looks up
    * the package in the classpath, decodes the path to handle special characters,
    * and returns all class names found in that package.
    *
    * @param packageName The package name to search for classes.
    * @return A list of class names found in the specified package.
    * @throws ClassNotFoundException       If the package path is not found.
    * @throws UnsupportedEncodingException If the package path cannot be decoded.
    */
   public static List<String> getClassNamesInPackage(String packageName)
         throws ClassNotFoundException, UnsupportedEncodingException {
      List<String> classNames = new ArrayList<>();

      // Get the package's directory path
      String packagePath = packageName.replace('.', '/');

      // Get the class loader
      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      URL packageURL = classLoader.getResource(packagePath);

      if (packageURL != null) {
         File directory = new File(URLDecoder.decode(packageURL.getFile(), "UTF-8"));
         if (directory.exists() && directory.isDirectory()) {
            // Filter to only class files
            String[] classFiles = directory.list(new FilenameFilter() {
               @Override
               public boolean accept(File dir, String name) {
                  return name.endsWith(".class");
               }
            });

            // Convert class file names to class names
            if (classFiles != null) {
               for (String classFile : classFiles) {
                  String className = packageName + '.' + classFile.substring(0, classFile.length() - 6);
                  String formattedClassName = formatClassName(className);
                  classNames.add(formattedClassName);
               }
            }
         }
      }

      // Ensure "Common Product" is first if present
      classNames.sort((a, b) -> a.contains("Common Product") ? -1 : 1);

      return classNames;
   }

   /**
    * Formats the class name by separating camel case words with spaces. This is
    * useful for displaying class names in a more readable format.
    *
    * @param className The fully qualified class name.
    * @return The formatted class name.
    */
   private static String formatClassName(String className) {
      String simpleName = className.substring(className.lastIndexOf('.') + 1);
      String formattedName = simpleName.replaceAll("([a-z])([A-Z]+)", "$1 $2");

      return formattedName;
   }
}
