package com.mapfre.tron.api.swaggergen.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Utility class responsible for generating a Swagger file (in YAML format)
 * from a set of models. The generated file follows the Swagger 2.0 specification
 * and includes basic information about the API, such as title, version, and definitions
 * of various object models.
 * <p>
 * This class writes the Swagger file to the specified file path and logs
 * the process to track execution.
 * <p>
 * The Swagger file is generated with object definitions, where each object
 * includes its properties and types. Some object models and properties are excluded
 * based on specific conditions.
 *
 * @author architecture - rperezv
 * @version 23/10/2024 - 09:52
 * @since jdk 1.17
 */
@Slf4j
public class SwaggerGenerator {

    /**
     * Generates a Swagger 2.0 YAML file with model definitions from the provided map of models.
     *
     * @param models   a map where each key is the model name and each value is another map
     *                 that represents the properties of the model (field names as keys and
     *                 field types as values)
     * @param filePath the path where the Swagger YAML file should be generated
     * @throws IOException if an I/O error occurs while writing to the file
     */
    public static void generateSwaggerFile(final Map<String, Map<String, String>> models, final String filePath)
            throws IOException {

        log.info("SwaggerGenerator running...");

        // Open the file writer to write the Swagger content
        FileWriter writer = new FileWriter(filePath);

        // Write basic Swagger information such as version, description, and title
        writer.write("swagger: '2.0'\n");
        writer.write("info:\n  description: 'API TRON Objects'\n  version: '1.0.0'\n  title: 'API TRON Objects'\n");
        writer.write("paths: {}\n");
        writer.write("definitions:\n");

        // Write the initial definitions section
        String sourceFilePath = "source_definitions.txt";
        try (Stream<String> lines = Files.lines(Paths.get(sourceFilePath))) {
            lines.forEach(line -> {
                try {
                    writer.write(line + System.lineSeparator());
                } catch (IOException e) {
                    log.error(e.getMessage(), e);;
                }
            });

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        // Loop through the models and add each model's definition to the Swagger file
        for (String modelName : models.keySet()) {

            // Retrieve properties of the model
            Map<String, String> properties = models.get(modelName);

            // Skip certain models that should not be included in the Swagger file
            if (properties != null && !properties.isEmpty()
                    && !"AObjCPT".equals(modelName)
                    && !"ObjNwtPrcAux".equals(modelName)
                    && !"ObjNwtDto".equals(modelName)
                    && !"Dates".equals(modelName)
                    && !"CInsConstant".equals(modelName)
                    && !"AObjCCT".equals(modelName)) {

                // Write the model's name and structure (type: object) to the file
                writer.write("  " + modelName + ":\n    type: object\n    properties:\n");

                // Loop through the properties of the model and write each property to the file
                properties.keySet().stream()
                    .filter(property -> !"atrPT".equals(property))
                    .forEach(property -> {
                        try {
                            // Handle array types
                            if (properties.get(property).contains("type: array")) {
                                writer.write("      " + property + ":\n" + properties.get(property) + "\n");
                            }
                            // Handle references to other models
                            else if (properties.get(property).contains("$") && !"oTrnPrcS".equals(property)) {
                                String ref = properties.get(property).substring(properties.get(property).indexOf("$"), properties.get(property).length() - 1);
                                writer.write("      " + property + ":\n        " + ref + "'\n");
                            }
                            // Handle simple properties
                            else if (!"serialVersionUID".equals(property) && !"oTrnPrcS".equals(property)) {
                                writer.write("      " + property + ":\n        type: " + properties.get(property) + "\n");
                            }
                        } catch (IOException e) {
                            log.error(e.getMessage(), e);
                        }
                    });

            }

        }

        writer.close();
    }

}
