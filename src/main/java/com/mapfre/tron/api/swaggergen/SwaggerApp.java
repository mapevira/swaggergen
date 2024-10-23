package com.mapfre.tron.api.swaggergen;

import com.mapfre.tron.api.swaggergen.utils.FileUtils;
import com.mapfre.tron.api.swaggergen.utils.ObjectAnalyzer;
import com.mapfre.tron.api.swaggergen.utils.SwaggerGenerator;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * The {@code SwaggerApp} class is the main entry point of the application responsible for generating a Swagger file
 * from Java classes within a specific source directory.
 * <p>
 * This application recursively scans a directory for Java source files, analyzes the structure of the classes,
 * and generates a corresponding Swagger YAML file to document the API models.
 * </p>
 * <p><strong>Usage:</strong> The application is initiated from the {@code main} method, and it generates a Swagger definition
 * file named "swagger.yaml" based on the Java classes found in the directory.
 * </p>
 *
 * <p><strong>Dependencies:</strong> The application relies on utility classes such as {@link FileUtils} for handling
 * file operations, {@link ObjectAnalyzer} for analyzing class structures, and {@link SwaggerGenerator} for generating
 * the final Swagger output.</p>
 *
 * @author architecture - rperezv
 * @version 23/10/2024 - 09:55
 * @since jdk 1.17
 */
@Slf4j
public class SwaggerApp {

    /**
     * The main method where the application starts execution. It scans Java source files located in the specified
     * directory, analyzes the class definitions, and generates a Swagger YAML file containing the model definitions.
     *
     * @param args The command-line arguments (not used).
     * @throws ClassNotFoundException if any of the Java classes cannot be found during analysis.
     * @throws IOException if there is an issue with reading the files or writing the output Swagger file.
     */
    public static void main(String[] args) throws ClassNotFoundException, IOException {
        log.info("SwaggerApp running...");

        // Define the source directory where the Java files are located
        File sourceDir = new File("src/main/java/com/mapfre/nwt");

        // Map to hold class names and their corresponding packages
        Map<String, String> classPackageMap = FileUtils.findJavaFilesWithPackages(sourceDir);

        log.debug("classPackageMap = {}", classPackageMap);

        // Map to store the Swagger models (class names and their property definitions)
        Map<String, Map<String, String>> swaggerModels = new HashMap<>();

        // Iterate over the class-package entries found in the source directory
        classPackageMap.forEach((className, packageName) -> {
            try {
                // Construct the full class name (including package if present)
                String fullClassName = packageName.isEmpty() ? className : packageName + "." + className;

                // Load the class dynamically using reflection
                Class<?> clazz = Class.forName(fullClassName);

                // Analyze the class structure to generate its Swagger model definition
                Map<String, String> classDefinition = ObjectAnalyzer.analyzeClass(clazz);

                // Add the class definition to the Swagger models map
                swaggerModels.put(className, classDefinition);
            } catch (ClassNotFoundException e) {
                log.error("Class not found: {}", className, e);
            }
        });

        // Generate the Swagger YAML file with the collected models
        SwaggerGenerator.generateSwaggerFile(swaggerModels, "swagger.yaml");

        log.info("SwaggerApp stopped");
    }

}
