package com.mapfre.tron.api.swaggergen.components;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Utility class for file operations, specifically for finding Java files and extracting package names.
 * <p>
 * This class provides methods to recursively search a directory for Java source files, extract the class names,
 * and map them to their corresponding package names.
 * </p>
 *
 * The resulting map can be used to dynamically load and analyze classes using reflection or for other purposes
 * such as code generation.
 *
 * @author architecture - rperezv
 * @version 23/10/2024 - 09:49
 * @since jdk 1.17
 */
public class FileUtils {

    /**
     * Recursively searches the specified directory for Java files and maps each found class name
     * to its corresponding package name.
     * <p>
     * This method explores directories and subdirectories, identifying `.java` files, extracting the class names
     * from the filenames, and reading the source files to obtain their package declarations. The result is a map
     * where the key is the class name and the value is the package name.
     *
     * @param dir The root directory to search for Java files.
     * @return A map where each class name is associated with its package name.
     * @throws IOException If an error occurs while reading a file or directory.
     */
    public static Map<String, String> findJavaFilesWithPackages(File dir) throws IOException {
        Map<String, String> classPackageMap = new HashMap<>();

        // Loop through files and directories in the provided directory
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isDirectory()) {
                // Recursively search subdirectories for Java files
                classPackageMap.putAll(findJavaFilesWithPackages(file));
            } else if (file.getName().endsWith(".java")) {
                // Extract the class name by removing the ".java" extension
                String className = file.getName().replace(".java", "");

                // Extract the package name from the file content
                String packageName = extractPackageName(file);

                // Store the class name and package name in the map
                classPackageMap.put(className, packageName);
            }
        }
        return classPackageMap;
    }

    /**
     * Extracts the package name from a given Java source file.
     * <p>
     * This method reads the content of a `.java` file line by line, looking for the line that starts with
     * the `package` keyword, which indicates the package declaration. Once found, it removes the `package`
     * keyword and the semicolon, returning the clean package name.
     *
     * @param file The Java source file to analyze.
     * @return The package name of the class in the file, or an empty string if no package is declared.
     * @throws IOException If an error occurs while reading the file.
     */
    private static String extractPackageName(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // Read the file line by line, searching for the package declaration
            return reader.lines()
                    // Clean and return the package name
                    .filter(line -> line.startsWith("package "))
                    .map(line -> line.replace("package ", "").replace(";", "").trim())
                    // Return the first matching line
                    .findFirst()
                    // If no package declaration is found, return an empty string
                    .orElse("");
        }
    }

}
