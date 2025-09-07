package com.controller.business;

import com.model.GeneratorConfiguration;
import com.controller.business.ValidationController.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.HashMap;

/**
 * Unit tests for ValidationController.
 * Tests validation logic for templates, configurations, and inputs.
 */
class ValidationControllerTest {
    
    private ValidationController validationController;
    
    @BeforeEach
    void setUp() {
        validationController = new ValidationController();
    }
    
    @Test
    @DisplayName("Should validate template format correctly")
    void testValidateTemplateFormat() {
        // Valid template formats
        ValidationResult result = validationController.validateTemplateFormat("{0}");
        assertTrue(result.isValid());
        assertTrue(result.getErrors().isEmpty());
        
        result = validationController.validateTemplateFormat("{0}-{1}");
        assertTrue(result.isValid());
        
        result = validationController.validateTemplateFormat("PREFIX-{0}-{1}-SUFFIX");
        assertTrue(result.isValid());
        
        // Invalid template formats
        result = validationController.validateTemplateFormat(null);
        assertFalse(result.isValid());
        assertFalse(result.getErrors().isEmpty());
        
        result = validationController.validateTemplateFormat("");
        assertFalse(result.isValid());
        
        result = validationController.validateTemplateFormat("   ");
        assertFalse(result.isValid());
        
        result = validationController.validateTemplateFormat("no placeholders");
        assertFalse(result.isValid());
        
        // Test warnings for gaps in indices
        result = validationController.validateTemplateFormat("{0}-{2}"); // Missing {1}
        assertTrue(result.isValid()); // Still valid, but should have warnings
        assertFalse(result.getWarnings().isEmpty());
    }
    
    @Test
    @DisplayName("Should validate configuration correctly")
    void testValidateConfiguration() {
        // Valid configuration
        GeneratorConfiguration validConfig = createValidConfiguration();
        ValidationResult result = validationController.validateConfiguration(validConfig);
        assertTrue(result.isValid());
        
        // Null configuration
        result = validationController.validateConfiguration(null);
        assertFalse(result.isValid());
        assertFalse(result.getErrors().isEmpty());
        
        // Empty configuration
        GeneratorConfiguration emptyConfig = new GeneratorConfiguration();
        emptyConfig.setGeneratorConfigs(new HashMap<>());
        result = validationController.validateConfiguration(emptyConfig);
        assertFalse(result.isValid());
        
        // Configuration with null generators
        GeneratorConfiguration nullConfig = new GeneratorConfiguration();
        nullConfig.setGeneratorConfigs(null);
        result = validationController.validateConfiguration(nullConfig);
        assertFalse(result.isValid());
    }
    
    @Test
    @DisplayName("Should validate generator config correctly")
    void testValidateGeneratorConfig() {
        // Valid generator config
        GeneratorConfiguration.GeneratorConfig validGen = createValidGeneratorConfig();
        ValidationResult result = validationController.validateGeneratorConfig("test", validGen);
        assertTrue(result.isValid());
        
        // Null generator config
        result = validationController.validateGeneratorConfig("test", null);
        assertFalse(result.isValid());
        
        // Generator config with null type
        GeneratorConfiguration.GeneratorConfig nullTypeGen = new GeneratorConfiguration.GeneratorConfig();
        nullTypeGen.setType(null);
        result = validationController.validateGeneratorConfig("test", nullTypeGen);
        assertFalse(result.isValid());
        
        // Generator config with empty type
        GeneratorConfiguration.GeneratorConfig emptyTypeGen = new GeneratorConfiguration.GeneratorConfig();
        emptyTypeGen.setType("");
        result = validationController.validateGeneratorConfig("test", emptyTypeGen);
        assertFalse(result.isValid());
    }
    
    @Test
    @DisplayName("Should validate numeric input correctly")
    void testValidateNumericInput() {
        // Valid numeric inputs
        ValidationResult result = validationController.validateNumericInput("5", "test field", 1, 10);
        assertTrue(result.isValid());
        
        result = validationController.validateNumericInput("1", "test field", 1, 10);
        assertTrue(result.isValid());
        
        result = validationController.validateNumericInput("10", "test field", 1, 10);
        assertTrue(result.isValid());
        
        // Invalid numeric inputs
        result = validationController.validateNumericInput(null, "test field", 1, 10);
        assertFalse(result.isValid());
        
        result = validationController.validateNumericInput("", "test field", 1, 10);
        assertFalse(result.isValid());
        
        result = validationController.validateNumericInput("   ", "test field", 1, 10);
        assertFalse(result.isValid());
        
        result = validationController.validateNumericInput("abc", "test field", 1, 10);
        assertFalse(result.isValid());
        
        result = validationController.validateNumericInput("0", "test field", 1, 10);
        assertFalse(result.isValid()); // Below minimum
        
        result = validationController.validateNumericInput("11", "test field", 1, 10);
        assertFalse(result.isValid()); // Above maximum
    }
    
    @Test
    @DisplayName("Should handle sequential number generator properties")
    void testSequentialNumberGeneratorValidation() {
        GeneratorConfiguration config = new GeneratorConfiguration();
        
        // Valid sequential number generator
        GeneratorConfiguration.GeneratorConfig seqGen = new GeneratorConfiguration.GeneratorConfig();
        seqGen.setType("SEQUENTIALNUMBERGENERATOR");
        
        Map<String, String> validProperties = new HashMap<>();
        validProperties.put("length", "5");
        validProperties.put("start", "1");
        validProperties.put("step", "1");
        
        seqGen.setProperties(validProperties);
        
        Map<String, GeneratorConfiguration.GeneratorConfig> generators = new HashMap<>();
        generators.put("0", seqGen);
        config.setGeneratorConfigs(generators);
        
        ValidationResult result = validationController.validateConfiguration(config);
        assertTrue(result.isValid());
        
        // Invalid properties
        Map<String, String> invalidProperties = new HashMap<>();
        invalidProperties.put("length", "abc"); // Invalid number
        invalidProperties.put("start", "xyz"); // Invalid number
        invalidProperties.put("step", "0"); // Zero step
        
        seqGen.setProperties(invalidProperties);
        result = validationController.validateConfiguration(config);
        assertFalse(result.isValid());
    }
    
    @Test
    @DisplayName("Should handle calculation generator properties")
    void testCalculationGeneratorValidation() {
        GeneratorConfiguration config = new GeneratorConfiguration();
        
        // Valid calculation generator
        GeneratorConfiguration.GeneratorConfig calcGen = new GeneratorConfiguration.GeneratorConfig();
        calcGen.setType("CALCULATION");
        
        Map<String, String> validProperties = new HashMap<>();
        validProperties.put("formula", "A+B+C");
        validProperties.put("input", "0");
        
        calcGen.setProperties(validProperties);
        
        Map<String, GeneratorConfiguration.GeneratorConfig> generators = new HashMap<>();
        generators.put("0", calcGen);
        config.setGeneratorConfigs(generators);
        
        ValidationResult result = validationController.validateConfiguration(config);
        assertTrue(result.isValid());
        
        // Missing required properties
        Map<String, String> incompleteProperties = new HashMap<>();
        incompleteProperties.put("formula", "A+B"); // Missing input
        
        calcGen.setProperties(incompleteProperties);
        result = validationController.validateConfiguration(config);
        assertFalse(result.isValid());
    }
    
    @Test
    @DisplayName("ValidationResult should work correctly")
    void testValidationResult() {
        // Test success
        ValidationResult success = ValidationResult.success();
        assertTrue(success.isValid());
        assertTrue(success.getErrors().isEmpty());
        assertTrue(success.getWarnings().isEmpty());
        
        // Test failure
        ValidationResult failure = ValidationResult.failure("Test error");
        assertFalse(failure.isValid());
        assertFalse(failure.getErrors().isEmpty());
        assertEquals("Test error", failure.getErrors().get(0));
    }
    
    private GeneratorConfiguration createValidConfiguration() {
        GeneratorConfiguration config = new GeneratorConfiguration();
        
        GeneratorConfiguration.GeneratorConfig generator = createValidGeneratorConfig();
        
        Map<String, GeneratorConfiguration.GeneratorConfig> generators = new HashMap<>();
        generators.put("0", generator);
        
        config.setGeneratorConfigs(generators);
        return config;
    }
    
    private GeneratorConfiguration.GeneratorConfig createValidGeneratorConfig() {
        GeneratorConfiguration.GeneratorConfig generator = new GeneratorConfiguration.GeneratorConfig();
        generator.setType("SEQUENTIALNUMBERGENERATOR");
        
        Map<String, String> properties = new HashMap<>();
        properties.put("length", "5");
        properties.put("start", "1");
        properties.put("step", "1");
        
        generator.setProperties(properties);
        return generator;
    }
}
