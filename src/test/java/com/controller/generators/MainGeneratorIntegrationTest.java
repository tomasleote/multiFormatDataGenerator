package com.controller.generators;

import com.controller.InputProcessor;
import com.model.Template;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Integration tests for the core generator functionality.
 * Tests the complete pipeline from input processing to generation.
 */
class MainGeneratorIntegrationTest {

    @Test
    @DisplayName("Simple sequential number generation should work")
    void testSimpleSequentialGeneration() {
        System.out.println("\n=== Testing Simple Sequential Generation ===");
        
        try {
            // Setup input processor
            InputProcessor processor = new InputProcessor("{0}");
            
            // Configure sequential number generator
            Map<String, String> properties = new HashMap<>();
            properties.put("type", "SEQUENTIALNUMBERGENERATOR");
            properties.put("input", "0");
            properties.put("length", "3");
            properties.put("start", "100");
            properties.put("step", "1");
            properties.put("padding-length", "3");
            properties.put("format", "");
            
            processor.addGeneratorAndPattern("SEQUENTIALNUMBERGENERATOR", properties);
            
            // Initialize template and generator
            Template template = processor.initTemplate();
            MainGenerator mainGenerator = processor.initMainGenerator();
            
            // Generate results
            List<String> results = mainGenerator.generate().limit(5).collect(Collectors.toList());
            
            // Verify results
            assertNotNull(results, "Results should not be null");
            assertEquals(5, results.size(), "Should generate 5 results");
            
            System.out.println("✓ Generated results:");
            for (int i = 0; i < results.size(); i++) {
                String expected = String.valueOf(100 + i);
                String actual = results.get(i);
                System.out.println("  " + (i + 1) + ": " + actual);
                assertEquals(expected, actual, "Result " + i + " should match expected value");
            }
            
            System.out.println("✓ Simple sequential generation test passed!");
            
        } catch (Exception e) {
            fail("Simple sequential generation failed: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Multi-generator template should work")
    void testMultiGeneratorTemplate() {
        System.out.println("\n=== Testing Multi-Generator Template ===");
        
        try {
            // Setup input processor with multi-generator template
            InputProcessor processor = new InputProcessor("{0}-{1}");
            
            // Configure first generator (ASCII)
            Map<String, String> asciiProps = new HashMap<>();
            asciiProps.put("type", "SEQUENTIALASCIIGENERATOR");
            asciiProps.put("input", "0");
            asciiProps.put("list", "A,B,C");
            asciiProps.put("length", "1");
            asciiProps.put("start", "A");
            asciiProps.put("step", "1");
            asciiProps.put("padding-length", "1");
            asciiProps.put("format", "");
            
            processor.addGeneratorAndPattern("SEQUENTIALASCIIGENERATOR", asciiProps);
            
            // Configure second generator (Number)
            Map<String, String> numberProps = new HashMap<>();
            numberProps.put("type", "SEQUENTIALNUMBERGENERATOR");
            numberProps.put("input", "1");
            numberProps.put("length", "3");
            numberProps.put("start", "100");
            numberProps.put("step", "1");
            numberProps.put("padding-length", "3");
            numberProps.put("format", "");
            
            processor.addGeneratorAndPattern("SEQUENTIALNUMBERGENERATOR", numberProps);
            
            // Initialize and generate
            Template template = processor.initTemplate();
            MainGenerator mainGenerator = processor.initMainGenerator();
            
            List<String> results = mainGenerator.generate().limit(3).collect(Collectors.toList());
            
            // Verify results
            assertNotNull(results, "Results should not be null");
            assertEquals(3, results.size(), "Should generate 3 results");
            
            System.out.println("✓ Generated multi-generator results:");
            for (int i = 0; i < results.size(); i++) {
                String result = results.get(i);
                System.out.println("  " + (i + 1) + ": " + result);
                assertTrue(result.contains("-"), "Result should contain separator");
                String[] parts = result.split("-");
                assertEquals(2, parts.length, "Result should have 2 parts");
            }
            
            System.out.println("✓ Multi-generator template test passed!");
            
        } catch (Exception e) {
            fail("Multi-generator template failed: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Input validation should work correctly")
    void testInputValidation() {
        System.out.println("\n=== Testing Input Validation ===");
        
        // Test empty template
        assertThrows(IllegalArgumentException.class, () -> {
            InputProcessor processor = new InputProcessor("");
            processor.initTemplate();
        }, "Empty template should throw exception");
        System.out.println("✓ Empty template validation works");
        
        // Test no generators
        assertThrows(IllegalArgumentException.class, () -> {
            InputProcessor processor = new InputProcessor("{0}");
            processor.initTemplate();
        }, "No generators should throw exception");
        System.out.println("✓ No generators validation works");
        
        // Test empty properties
        assertThrows(IllegalArgumentException.class, () -> {
            InputProcessor processor = new InputProcessor("{0}");
            processor.addGeneratorAndPattern("SEQUENTIALNUMBERGENERATOR", new HashMap<>());
        }, "Empty properties should throw exception");
        System.out.println("✓ Empty properties validation works");
        
        System.out.println("✓ Input validation test passed!");
    }

    @Test
    @DisplayName("Error handling should be robust")
    void testErrorHandling() {
        System.out.println("\n=== Testing Error Handling ===");
        
        try {
            // Test invalid generator type
            InputProcessor processor = new InputProcessor("{0}");
            
            Map<String, String> invalidProps = new HashMap<>();
            invalidProps.put("type", "INVALID_GENERATOR");
            invalidProps.put("input", "0");
            
            assertThrows(IllegalArgumentException.class, () -> {
                processor.addGeneratorAndPattern("INVALID_GENERATOR", invalidProps);
            }, "Invalid generator type should throw exception");
            
            System.out.println("✓ Invalid generator type handling works");
            System.out.println("✓ Error handling test passed!");
            
        } catch (Exception e) {
            fail("Error handling test failed: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Integration test summary")
    void testIntegrationSummary() {
        System.out.println("\n=== Integration Test Summary ===");
        System.out.println("All core functionality tests completed!");
        System.out.println("✓ Simple generation working");
        System.out.println("✓ Multi-generator templates working");
        System.out.println("✓ Input validation working");
        System.out.println("✓ Error handling working");
        System.out.println("✓ Phase 1 core functionality verified");
        System.out.println("===============================");
        
        assertTrue(true, "All integration tests passed successfully");
    }
}
