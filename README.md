# Automated Testing with RestAssured in Java

## Description
This project contains an example of automated API tests written in Java using RestAssured. The tests utilize the API provided by OpenAI. You can find more information about the OpenAI API [here](https://platform.openai.com/docs/api-reference).

## Requirements
- Java Development Kit (JDK) version 11 or newer
- Maven - a tool for managing dependencies and building projects
- OpenAI API key - You need to generate an API key from [OpenAI's website](https://platform.openai.com/api-keys)

## Installation and Configuration
1. Clone the project using the `git clone` command.
2. Open the project in your preferred IDE.
3. Make sure you have installed and configured Java and Maven properly.
4. Paste your OpenAI API key into the `config.properties` file.

## Running Tests
1. Open the project in your IDE.
2. Run the tests by clicking the "Run" button or using the appropriate options in your IDE.

## Project Structure
- `src/test/java/tests`: Contains automated tests.
- `src/test/java/utils`: Contains supporting classes.
- `src/main/java/reporting`: Contains ExtentReport configuration.
- `src/test/resources`: Contains test resources, such as configuration files and response schemas.
- `pom.xml`: Maven configuration file containing project information and dependencies.

## Author
Mariusz Wawreszuk - wawreszuk.mariusz@gmail.com
