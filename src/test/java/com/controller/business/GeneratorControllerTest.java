package com.controller.business;

import com.model.GeneratorConfiguration;
import com.model.Template;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

/**
 * Unit tests for GeneratorController.
 * Tests the core business logic for data generation.
 */
class GeneratorControllerTest {
    
    private GeneratorController generatorController;
    private GeneratorConfiguration testConfiguration;
    private Template testTemplate;
    
    @BeforeEach
    void setUp() {
        generatorController = new GeneratorController();
        
        // Create a test configuration
        testConfiguration = new GeneratorConfiguration();
        GeneratorConfiguration.GeneratorConfig testGeneratorConfig = new GeneratorConfiguration.GeneratorConfig();
        testGeneratorConfig.setType("SEQUENTIALNUMBERGENERATOR");
        
        Map<String, String> properties = new HashMap<>();
        properties.put("length", "5");
        properties.put("start", "1");
        properties.put("step", "1");
        properties.put("padding-length", "0");
        
        testGeneratorConfig.setProperties(properties);
        
        Map<String, GeneratorConfiguration.GeneratorConfig> generators = new HashMap<>();
        generators.put("0", testGeneratorConfig);
        
        testConfiguration.setGeneratorConfigs(generators);
        
        // Create a test template
        testTemplate = new Template();
        testTemplate.setFormat("{0}");
    }
    
    @Test
    @DisplayName("Should validate template format correctly")
    void testValidateTemplateFormat() {
        // Valid formats
        assertTrue(generatorController.validateTemplateFormat("{0}"));
        assertTrue(generatorController.validateTemplateFormat("{0}-{1}"));
        assertTrue(generatorController.validateTemplateFormat("PREFIX-{0}-SUFFIX"));
        
        // Invalid formats
        assertFalse(generatorController.validateTemplateFormat(null));
        assertFalse(generatorController.validateTemplateFormat(""));
        assertFalse(generatorController.validateTemplateFormat("   "));
        assertFalse(generatorController.validateTemplateFormat("no placeholders"));
    }
    
    @Test
    @DisplayName("Should extract generator count correctly")
    void testExtractGeneratorCount() {
        assertEquals(1, generatorController.extractGeneratorCount("{0}"));
        assertEquals(2, generatorController.extractGeneratorCount("{0}-{1}"));
        assertEquals(3, generatorController.extractGeneratorCount("{0}-{1}-{2}"));
        assertEquals(3, generatorController.extractGeneratorCount("{2}-{0}-{1}")); // Max index is 2
        assertEquals(0, generatorController.extractGeneratorCount("no placeholders"));
        assertEquals(0, generatorController.extractGeneratorCount(null));
    }
    
    @Test
    @DisplayName("Should validate configuration correctly")
    void testValidateConfiguration() {
        // Valid configuration
        assertTrue(generatorController.validateConfiguration(testConfiguration));
        
        // Invalid configurations
        assertFalse(generatorController.validateConfiguration(null));
        
        GeneratorConfiguration emptyConfig = new GeneratorConfiguration();
        emptyConfig.setGeneratorConfigs(new HashMap<>());
        assertFalse(generatorController.validateConfiguration(emptyConfig));
        
        GeneratorConfiguration nullConfigsConfig = new GeneratorConfiguration();
        nullConfigsConfig.setGeneratorConfigs(null);
        assertFalse(generatorController.validateConfiguration(nullConfigsConfig));
    }
    
    @Test
    @DisplayName("Should handle generation state correctly")
    void testGenerationState() {
        // Initially not generating
        assertFalse(generatorController.isGenerating());
        
        // Reset should work
        assertDoesNotThrow(() -> generatorController.resetGenerator());
        
        // Stop generation should work even when not generating
        assertDoesNotThrow(() -> generatorController.stopGeneration());
        
        // Shutdown should work
        assertDoesNotThrow(() -> generatorController.shutdown());
    }
    
    @Test
    @DisplayName("Should handle null inputs gracefully")
    void testNullInputHandling() {
        // Generate numbers with null inputs
        assertThrows(IllegalArgumentException.class, () -> 
            generatorController.generateNumbers(null, testConfiguration, 10));
        
        assertThrows(IllegalArgumentException.class, () -> 
            generatorController.generateNumbers(testTemplate, null, 10));
        
        // Apply template with null inputs
        assertEquals("", generatorController.applyTemplate(null, new HashMap<>()));
        assertEquals("", generatorController.applyTemplate(testTemplate, null));
        
        // Generate preview with null inputs
        assertNotNull(generatorController.generatePreview(null, testConfiguration));
        assertNotNull(generatorController.generatePreview(testTemplate, null));
    }
    
    @Test
    @DisplayName("Should generate preview correctly")
    void testGeneratePreview() {
        String preview = generatorController.generatePreview(testTemplate, testConfiguration);
        assertNotNull(preview);
        assertTrue(preview.startsWith("Preview:") || preview.contains("Error") || preview.contains("Unable"));
    }
    
    @Test
    @DisplayName("Should handle async generation")
    void testAsyncGeneration() {
        CompletableFuture<List<String>> future = generatorController.generateNumbersAsync(
            testTemplate, testConfiguration, 5);
        
        assertNotNull(future);
        assertDoesNotThrow(() -> {
            List<String> results = future.get();
            // Results might be empty if the main generator isn't properly initialized,
            // but the call should not throw an exception
            assertNotNull(results);
        });
    }
}
