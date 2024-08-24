package com.monev.superdupermarkt.util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class PackageClassNameFinder {

	/**
	 * 
	 * @param packageName - The given package name, which should contain the class names to be returned
	 * @return  List of class names found in the given Java package. 
	 * @throws ClassNotFoundException
	 * @throws UnsupportedEncodingException
	 */
    public static List<String> getClassNamesInPackage(String packageName) throws ClassNotFoundException, UnsupportedEncodingException {
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
        classNames.sort((a, b) -> a.equals("Common Product") ? -1 : 1);
        
        return classNames;
    }

    /**
     * 
     * @param className
     * @return
     */
    private static String formatClassName(String className) {
        String simpleName = className.substring(className.lastIndexOf('.') + 1);
        String formattedName = simpleName.replaceAll("([a-z])([A-Z]+)", "$1 $2");
        
        return formattedName;
    }
}
