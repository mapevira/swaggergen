# API TRON Swagger Generator

## Description

This project is an application that generates a Swagger file in YAML format from Java classes located in a specific directory. The application recursively scans the directory, analyzes the structure of the classes, and generates a Swagger file that documents the API models.

## Features

### SwaggerApp

The `SwaggerApp` class is the main entry point of the application. Its responsibilities include:

- Scanning Java source files in a specific directory.
- Analyzing class definitions and generating a Swagger YAML file with model definitions.

#### Main Methods

- `main(String[] args)`: Starts the execution of the application, scans the Java source files, analyzes class definitions, and generates the Swagger file.

### SwaggerGenerator

The `SwaggerGenerator` class is a utility responsible for generating a Swagger file in YAML format from a set of models.

#### Main Methods

- `generateSwaggerFile(Map<String, Map<String, String>> models, String filePath)`: Generates a Swagger 2.0 YAML file with model definitions from the provided map of models.

### ObjectAnalyzer

The `ObjectAnalyzer` class provides methods to analyze the structure of classes and extract information about their properties.

#### Main Methods

- `analyzeClass(Class<?> clazz)`: Analyzes the structure of a class and returns a map with the definitions of its properties.
- `getGenericType(Field field)`: Gets the generic type of a field.

## Usage

To run the application, simply execute the `main` method of the `SwaggerApp` class. The application will generate a `swagger.yaml` file in the root directory of the project based on the Java classes found in the specified directory.

## Dependencies

- Java 17
- Spring Boot
- Maven
- Lombok

## Author

- architecture - rperezv

## Version

- 23/10/2024 - 09:55
