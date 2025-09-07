package com.controller.business;

import com.model.GeneratorConfiguration;
import com.model.Template;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Handles validation logic for templates, configurations, and user inputs.
 * Provides comprehensive validation with detailed error messages.
 * 
 * Responsibilities:
 * - Validate template format syntax
 * - Validate generator configurations
 * - Validate user input fields
 * - Provide validation error messages
 */
public class ValidationController {
    
    /**
     * Validation result container.
     */
    public static class ValidationResult {
        private final boolean valid;
        private final List<String> errors;
        private final List<String> warnings;
        
        private ValidationResult(boolean valid, List<String> errors, List<String> warnings) {
            this.valid = valid;
            this.errors = new ArrayList<>(errors);
            this.warnings = new ArrayList<>(warnings);
        }
        
        public boolean isValid() { return valid; }
        public List<String> getErrors() { return new ArrayList<>(errors); }
        public List<String> getWarnings() { return new ArrayList<>(warnings); }
        
        public static ValidationResult success() {
            return new ValidationResult(true, new ArrayList<>(), new ArrayList<>());
        }
        
        public static ValidationResult success(List<String> warnings) {
            return new ValidationResult(true, new ArrayList<>(), warnings);
        }
        
        public static ValidationResult failure(List<String> errors) {
            return new ValidationResult(false, errors, new ArrayList<>());
        }
        
        public static ValidationResult failure(String error) {
            List<String> errors = new ArrayList<>();
            errors.add(error);
            return new ValidationResult(false, errors, new ArrayList<>());
        }
    }
    
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{(\\d+)\\}");
    private static final int MAX_GENERATORS = 10;
    private static final int MAX_TEMPLATE_LENGTH = 1000;
    
    /**
     * Validates a template format string.
     * 
     * @param templateFormat The format string to validate
     * @return ValidationResult with errors and warnings
     */
    public ValidationResult validateTemplateFormat(String templateFormat) {
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        
        // Null or empty check
        if (templateFormat == null || templateFormat.trim().isEmpty()) {
            errors.add("Template format cannot be empty");
            return ValidationResult.failure(errors);
        }
        
        // Length check
        if (templateFormat.length() > MAX_TEMPLATE_LENGTH) {
            errors.add("Template format is too long (max " + MAX_TEMPLATE_LENGTH + " characters)");
        }
        
        // Find all placeholders
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(templateFormat);
        List<Integer> indices = new ArrayList<>();
        
        while (matcher.find()) {
            try {
                int index = Integer.parseInt(matcher.group(1));
                indices.add(index);
                
                if (index >= MAX_GENERATORS) {
                    errors.add("Generator index " + index + " exceeds maximum allowed (" + MAX_GENERATORS + ")");
                }
            } catch (NumberFormatException e) {
                errors.add("Invalid placeholder format: " + matcher.group(0));
            }
        }
        
        // Check if there are any placeholders at all
        if (indices.isEmpty()) {
            errors.add("Template format must contain at least one placeholder (e.g., {0})");
        }
        
        // Check for gaps in indices
        if (!indices.isEmpty()) {
            indices.sort(Integer::compareTo);
            int expectedNext = 0;
            
            for (Integer index : indices) {
                if (index > expectedNext) {
                    warnings.add("Generator indices have gaps. Missing: " + expectedNext + " to " + (index - 1));
                    expectedNext = index + 1;
                } else if (index.equals(expectedNext)) {
                    expectedNext++;
                }
            }
        }
        
        // Check for duplicate indices (not necessarily an error, but worth noting)
        long uniqueCount = indices.stream().distinct().count();
        if (uniqueCount < indices.size()) {
            warnings.add("Template contains duplicate placeholder indices");
        }
        
        if (!errors.isEmpty()) {
            return ValidationResult.failure(errors);
        }
        
        return ValidationResult.success(warnings);
    }
    
    /**
     * Validates a complete generator configuration.
     * 
     * @param configuration The configuration to validate
     * @return ValidationResult with detailed feedback
     */
    public ValidationResult validateConfiguration(GeneratorConfiguration configuration) {
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        
        if (configuration == null) {
            errors.add("Configuration cannot be null");
            return ValidationResult.failure(errors);
        }
        
        // Validate generator configs
        Map<String, GeneratorConfiguration.GeneratorConfig> configs = configuration.getGeneratorConfigs();
        if (configs == null || configs.isEmpty()) {
            errors.add("Configuration must contain at least one generator");
            return ValidationResult.failure(errors);
        }
        
        // Validate each generator config
        for (Map.Entry<String, GeneratorConfiguration.GeneratorConfig> entry : configs.entrySet()) {
            String generatorId = entry.getKey();
            GeneratorConfiguration.GeneratorConfig config = entry.getValue();
            
            ValidationResult configResult = validateGeneratorConfig(generatorId, config);
            errors.addAll(configResult.getErrors());
            warnings.addAll(configResult.getWarnings());
        }
        
        // Check for circular dependencies in evaluators
        ValidationResult dependencyResult = validateDependencies(configuration);
        errors.addAll(dependencyResult.getErrors());
        warnings.addAll(dependencyResult.getWarnings());
        
        if (!errors.isEmpty()) {
            return ValidationResult.failure(errors);
        }
        
        return ValidationResult.success(warnings);
    }
    
    /**
     * Validates a single generator configuration.
     * 
     * @param generatorId The ID of the generator
     * @param config The configuration to validate
     * @return ValidationResult for this specific generator
     */
    public ValidationResult validateGeneratorConfig(String generatorId, GeneratorConfiguration.GeneratorConfig config) {
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        
        if (config == null) {
            errors.add("Generator " + generatorId + ": Configuration cannot be null");
            return ValidationResult.failure(errors);
        }
        
        String type = config.getType();
        if (type == null || type.trim().isEmpty()) {
            errors.add("Generator " + generatorId + ": Type cannot be empty");
        }
        
        Map<String, String> properties = config.getProperties();
        if (properties == null) {
            warnings.add("Generator " + generatorId + ": No properties defined");
        } else {
            // Validate specific properties based on generator type
            ValidationResult propResult = validateGeneratorProperties(generatorId, type, properties);
            errors.addAll(propResult.getErrors());
            warnings.addAll(propResult.getWarnings());
        }
        
        if (!errors.isEmpty()) {
            return ValidationResult.failure(errors);
        }
        
        return ValidationResult.success(warnings);
    }
    
    /**
     * Validates generator properties based on the generator type.
     * 
     * @param generatorId The generator ID
     * @param type The generator type
     * @param properties The properties to validate
     * @return ValidationResult for the properties
     */
    private ValidationResult validateGeneratorProperties(String generatorId, String type, Map<String, String> properties) {
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        
        switch (type.toUpperCase()) {
            case "SEQUENTIALNUMBERGENERATOR":
                validateSequentialNumberProperties(generatorId, properties, errors, warnings);
                break;
            case "SEQUENTIALASCIIGENERATOR":
                validateSequentialAsciiProperties(generatorId, properties, errors, warnings);
                break;
            case "CALCULATION":
                validateCalculationProperties(generatorId, properties, errors, warnings);
                break;
            case "EVALUATION":
                validateEvaluationProperties(generatorId, properties, errors, warnings);
                break;
            default:
                warnings.add("Generator " + generatorId + ": Unknown generator type '" + type + "'");
        }
        
        if (!errors.isEmpty()) {
            return ValidationResult.failure(errors);
        }
        
        return ValidationResult.success(warnings);
    }
    
    private void validateSequentialNumberProperties(String generatorId, Map<String, String> properties, 
                                                   List<String> errors, List<String> warnings) {
        // Validate required properties
        if (!properties.containsKey("length") || properties.get("length").trim().isEmpty()) {
            errors.add("Generator " + generatorId + ": 'length' property is required");
        } else {
            try {
                int length = Integer.parseInt(properties.get("length"));
                if (length <= 0) {
                    errors.add("Generator " + generatorId + ": 'length' must be positive");
                } else if (length > 20) {
                    warnings.add("Generator " + generatorId + ": Large length values may cause performance issues");
                }
            } catch (NumberFormatException e) {
                errors.add("Generator " + generatorId + ": 'length' must be a valid number");
            }
        }
        
        // Validate start value
        if (properties.containsKey("start") && !properties.get("start").trim().isEmpty()) {
            try {
                Long.parseLong(properties.get("start"));
            } catch (NumberFormatException e) {
                errors.add("Generator " + generatorId + ": 'start' must be a valid number");
            }
        }
        
        // Validate step
        if (properties.containsKey("step") && !properties.get("step").trim().isEmpty()) {
            try {
                int step = Integer.parseInt(properties.get("step"));
                if (step == 0) {
                    errors.add("Generator " + generatorId + ": 'step' cannot be zero");
                }
            } catch (NumberFormatException e) {
                errors.add("Generator " + generatorId + ": 'step' must be a valid number");
            }
        }
    }
    
    private void validateSequentialAsciiProperties(String generatorId, Map<String, String> properties, 
                                                  List<String> errors, List<String> warnings) {
        // Validate list property
        if (!properties.containsKey("list") || properties.get("list").trim().isEmpty()) {
            errors.add("Generator " + generatorId + ": 'list' property is required");
        } else {
            String list = properties.get("list");
            String[] items = list.split(",");
            if (items.length < 2) {
                warnings.add("Generator " + generatorId + ": List should contain at least 2 items for meaningful sequences");
            }
        }
        
        // Also validate common sequential properties
        validateSequentialNumberProperties(generatorId, properties, errors, warnings);
    }
    
    private void validateCalculationProperties(String generatorId, Map<String, String> properties, 
                                             List<String> errors, List<String> warnings) {
        // Validate formula
        if (!properties.containsKey("formula") || properties.get("formula").trim().isEmpty()) {
            errors.add("Generator " + generatorId + ": 'formula' property is required");
        }
        
        // Validate input reference
        if (!properties.containsKey("input") || properties.get("input").trim().isEmpty()) {
            errors.add("Generator " + generatorId + ": 'input' property is required");
        } else {
            try {
                Integer.parseInt(properties.get("input"));
            } catch (NumberFormatException e) {
                errors.add("Generator " + generatorId + ": 'input' must be a valid generator index");
            }
        }
    }
    
    private void validateEvaluationProperties(String generatorId, Map<String, String> properties, 
                                            List<String> errors, List<String> warnings) {
        // Similar to calculation validation
        validateCalculationProperties(generatorId, properties, errors, warnings);
    }
    
    /**
     * Validates dependencies between generators to detect circular references.
     * 
     * @param configuration The configuration to check
     * @return ValidationResult for dependency validation
     */
    private ValidationResult validateDependencies(GeneratorConfiguration configuration) {
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        
        // TODO: Implement circular dependency detection
        // This would involve building a dependency graph and checking for cycles
        
        return ValidationResult.success(warnings);
    }
    
    /**
     * Validates numeric input with range checking.
     * 
     * @param value The value to validate
     * @param fieldName The name of the field being validated
     * @param min Minimum allowed value (inclusive)
     * @param max Maximum allowed value (inclusive)
     * @return ValidationResult for the numeric input
     */
    public ValidationResult validateNumericInput(String value, String fieldName, int min, int max) {
        List<String> errors = new ArrayList<>();
        
        if (value == null || value.trim().isEmpty()) {
            errors.add(fieldName + " cannot be empty");
            return ValidationResult.failure(errors);
        }
        
        try {
            int numValue = Integer.parseInt(value.trim());
            if (numValue < min) {
                errors.add(fieldName + " must be at least " + min);
            } else if (numValue > max) {
                errors.add(fieldName + " must be at most " + max);
            }
        } catch (NumberFormatException e) {
            errors.add(fieldName + " must be a valid number");
        }
        
        if (!errors.isEmpty()) {
            return ValidationResult.failure(errors);
        }
        
        return ValidationResult.success();
    }
}