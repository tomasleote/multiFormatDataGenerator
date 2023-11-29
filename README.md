# Multi format data generator

- [Introduction](#introduction)
- [Goal](#goal)
- [Specification](#specification)
- [User Interface Instructions](#user-interface-instructions)
  - [Introduction](#introduction-1)
  - [Using the UI](#using-the-ui)
  - [Generators and Properties](#generators-and-properties)
  - [Additional Evaluators](#additional-evaluators)
  - [Example Configurations](#example-configurations)
- [Notes](#notes)
- [Troubleshooting](#troubleshooting)

## Introduction
This code generates random numbers with specific formats, and can include ASCII characters as well. The platform consists of different modules like data masking, synthetic data generation, data subsetting, and a centralized test data portal. For this project, we propose a solution to develop a JAVA generator that returns a stream of unique numbers that can have various format requirements.

Important to note that the view is not working 100% yet, try using one of the mentioned examples.

## Goal
Developing a synthetic data generator that can generate thousands of unique values per second with different format requirements. As an addition, a generic user interface could be built inside a C# application that could provide a method to define the unique number requirements.

## Specification
JAVA generator that can generate a stream of unique keys with different requirements and formats.

It should implement the Java interface `com.controller.generators.MainGenerator`.

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

### Example Configurations
Below are detailed instructions for setting up generators based on the integration tests provided earlier.

#### Dutch BSN Number Generator Setup
1. **Template Format:** Enter `{0}` in the template format field.
2. **Generator Configuration:**
   - For `SEQUENTIALNUMBERGENERATOR`:
     - `length`: "9"
     - `start`: "100000000"
     - `step`: "1"
     - `padding-length`: "0"
     - `input`: "0"
   - For `EVALUATION`:
     - `formula`: "(9*A + 8*B + 7*C + 6*D + 5*E + 4*F + 3*G + 2*H - I) % 11 == 0"
     - `input`: "0"
3. **Generate:** Click "Generate" to produce valid BSN numbers.

#### Belgian SSN Generator Setup
...

#### Full Generator Process Setup
1. **Template Format:** Enter `{0}--{1}--{2}` in the template format field.
2. **Generator Configuration:**
   - For `SEQUENTIALNUMBERGENERATOR`:
     - `length`: "3"
     - `start`: "100"
     - `step`: "1"
     - `padding-length`: "3"
     - `format`: "{1}{2}-{3}"
     - `input`: "0"
   - For `CALCULATION`:
     - `formula`: "A+B+C"
     - `input`: "0"
     - `length`: "2"
     - `format`: "{1}{2}AAA"
   - For `EVALUATION`:
     - `formula`: "A%2==0"
     - `input`: "0"
     - `length`: "3"
   - For `SEQUENTIALASCIIGENERATOR`:
     - `list`: "a,b,c"
     - `length`: "3"
     - `start`: "aaa"
     - `padding-length`: "3"
     - `format`: "{1}--{2}--{3}"
     - `input`: "3"
3. **Generate:** Click "Generate" to produce complex formatted sequences.

### Notes
- Remember to use the dropdown to select the number of additional evaluators required.
- The evaluator's properties are similar to those of the Calculation generator but are used to filter rather than format values.

Example Java libraries implementing some kind of expression evaluator (to use or to get inspiration from):

 - https://www.janino.net/use.html#expression_evaluator
 - https://commons.apache.org/proper/commons-jexl/
 - https://sourceforge.net/projects/mxparser/
 - https://mathparser.org/
 - https://juel.sourceforge.net/

### Troubleshooting
If you encounter any issues while using the UI, ensure all required properties are filled in and that the format string correctly matches the number of configured generators.

For more details and examples, please refer to the provided links and example configurations.

[...]

