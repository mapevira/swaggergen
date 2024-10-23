package com.mapfre.tron.api.swaggergen.bootstrap;

import com.mapfre.tron.api.swaggergen.components.FileUtils;
import com.mapfre.tron.api.swaggergen.components.ObjectAnalyzer;
import com.mapfre.tron.api.swaggergen.components.SwaggerGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jt, Spring Framework Guru.
 *
 * @author architecture - rperezv
 * @version 23/10/2024 - 14:49
 * @since jdk 1.17
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AppRunner implements CommandLineRunner {

    private final SwaggerGenerator  swaggerGenerator;

    @Override
    public void run(String... args) throws Exception {
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
        swaggerGenerator.generateSwaggerFile(swaggerModels, "swagger.yaml");

        log.info("SwaggerApp stopped");
    }
}
