# Multi format data generator

## Introduction
This code generates random numbers with specific formats, and can include ASCII characters as well. The platform consists of different modules like data masking, synthetic data generation, data subsetting, and a centralized test data portal. For this project, we propose a solution to develop a JAVA generator that returns a stream of unique numbers that can have various format requirements.

Important to note that the view is not working yet.

## Goal
Developing a synthetic data generator that can generate thousands of unique values per second with different format requirements. As an addition, a generic user interface could be built inside a C# application that could provide a method to define the unique number requirements.

## Specification
JAVA generator that can generate a stream of unique keys with different requirements and formats.

It should implement the Java interface `com.datprof.generators.controller.generators.MainGenerator`.

### [Current Examples and Descriptions]

## User Interface Instructions

### Introduction
The Synthetic Data Generator UI provides a user-friendly interface to generate data according to various specifications. It features a dynamic form that allows users to configure different types of generators, each with specific properties.

### Using the UI
1. **Template Format:** Begin by entering a template format in the provided field. This format should outline the structure of your desired output, using placeholders like `{0}`, `{1}`, etc., to indicate where generated values will be inserted.
2. **Generator Configuration:** Based on the template format, input fields for each generator will appear. Select the type of generator from the dropdown and fill in the properties required for that generator.
3. **Generate:** Once all generators are configured, press the "Generate" button to produce the output.
4. **Clear Output:** If you need to clear the output area, use the "Clear Output" button.

### Generators and Properties
Each generator type has specific properties:

- **Sequential Number Generator:**
  - `length`: The number of digits in the generated sequence.
  - `start`: The starting number of the sequence.
  - `step`: The increment between each number in the sequence.
  - `padding-length`: Ensures the generated number has a fixed length by adding leading zeros if necessary.
  - `format`: The specific format to apply to the generated number, e.g., `XX-{0}-XX`.

- **Calculation:**
  - `input`: The index of another generator whose output will be used as input for this generator.
  - `formula`: A mathematical expression used to calculate the generated value.
  - `length`: The length of the generated calculation output.
  - `format`: The format in which the calculated value will be output.

- **Sequential ASCII Generator:**
  - `list`: A comma-separated list of characters to be used in generating sequences.
  - `length`, `start`, `step`, `padding-length`, and `format` properties similar to the Sequential Number Generator.

- **Evaluation:**
  - `input`: Similar to Calculation, specifies the dependency on another generator.
  - `formula`: A logical expression to evaluate the truthiness of generated values. Only values that satisfy the formula will be output.

### Additional Evaluators
You can add additional evaluator generators that do not contribute directly to the output format but are used to filter values based on logical conditions.

### Notes
- Remember to use the dropdown to select the number of additional evaluators required.
- The evaluator's properties are similar to those of the Calculation generator but are used to filter rather than format values.

### Example Usage
- To generate a Dutch BSN number, you could set a Sequential Number Generator with the appropriate length and step, and then add an Evaluation generator with the BSN validation formula.

### Troubleshooting
If you encounter any issues while using the UI, ensure all required properties are filled in and that the format string correctly matches the number of configured generators.

For more details and examples, please refer to the provided links and example configurations.

[...]

