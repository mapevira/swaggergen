package com.mapfre.tron.api.swaggergen.components;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Utility class for analyzing Java classes and generating Swagger model definitions.
 * This class uses reflection to analyze fields of a given class and generate a
 * corresponding Swagger model in the form of a Map<String, String>.
 * <p>
 * The generated model includes field types and references to other Swagger definitions
 * when the field is an object or a list of objects.
 * <p>
 * Recursion prevention is implemented using a set of processed classes to avoid
 * infinite loops when classes contain references to themselves or each other.
 *
 * @author architecture - rperezv
 * @version 23/10/2024 - 09:51
 * @since jdk 1.17
 */
@Slf4j
public class ObjectAnalyzer {

    // Set to track already processed classes and prevent infinite recursion
    private static final Set<Class<?>> processedClasses = new HashSet<>();

    /**
     * Analyzes the structure of the given class and generates a corresponding
     * Swagger model definition as a map of field names to field types or references.
     *
     * @param clazz the class to be analyzed
     * @return a map representing the Swagger model for the given class
     */
    public static Map<String, String> analyzeClass(Class<?> clazz) {

        // Avoids infinite recursion by checking if the class has already been processed
        if (processedClasses.contains(clazz)) {
            Map<String, String> processSwaggerModel = new HashMap<>();
            processSwaggerModel.put(clazz.getName(), "\n        $ref: '#/definitions/" + clazz.getSimpleName());
            return processSwaggerModel;
        }
        processedClasses.add(clazz);

        // Map to hold the Swagger model for the current class
        Map<String, String> swaggerModel = new HashMap<>();

        // Loop through all declared fields of the class
        for (Field field : clazz.getDeclaredFields()) {
            String fieldType = field.getType().getSimpleName();

            // Handle List fields by recursively analyzing their generic types
            if (List.class.isAssignableFrom(field.getType()) &&  !"atrPT".equals(getGenericType(field))) {
                fieldType = "        type: array\n        items:\n          $ref: '#/definitions/" + getGenericType(field) + "'";
            }
            // Handle object fields by referencing their Swagger definitions
            else if (!isPrimitive(field.getType())) {
                if (fieldType.equals("byte[]")) {
                    // Special case for byte arrays
                    fieldType = "string\n        format: binary";
                } else {
                    fieldType = "\n        $ref: '#/definitions/" + fieldType + "'";
                }
            }
            // Handle primitive types and map them to Swagger types
            else if (isPrimitive(field.getType())) {
                // se busca la descripcion en la base de datos

                fieldType = switch (fieldType) {
                    case "int", "Integer" -> "integer\n        format: int32";
                    case "Date", "long", "Long" -> "integer\n        format: int64";
                    case "float", "Float" -> "number\n        format: float";
                    case "double", "Double" -> "number\n        format: double";
                    case "BigDecimal" -> "number";
                    case "boolean", "Boolean" -> "boolean";
                    case "String" -> "string";
                    case "Byte" -> "string\n        format: byte";
                    case "Map" -> "object\n      additionalProperties:\n        type: string";
                    default -> {
                        log.info("Type not found: {}", fieldType);
                        // Default to string if type is unrecognized
                        yield "string";
                    }
                };

            }

            // Add the field name and type to the Swagger model
            swaggerModel.put(field.getName(), fieldType);
        }
        return swaggerModel;
    }

    /**
     * Checks if a class is a primitive type or a standard Java wrapper class.
     *
     * @param type the class to check
     * @return true if the class is a primitive or standard Java type, false otherwise
     */
    private static boolean isPrimitive(Class<?> type) {
        return type.isPrimitive() || type.getName().startsWith("java.");
    }

    /**
     * Retrieves the generic type of field, typically used for lists and collections.
     *
     * @param field the field to analyze
     * @return the simple name of the generic type of the field
     */
    private static String getGenericType(Field field) {
        String typeName = field.getGenericType().getTypeName();
        return typeName.substring(typeName.lastIndexOf(".") + 1, typeName.length() -1);
    }

}
