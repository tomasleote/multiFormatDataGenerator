package com.controller.business;

import com.controller.generators.MainGenerator;
import com.controller.InputProcessor;
import com.model.GeneratorConfiguration;
import com.model.Template;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Core business logic controller for data generation operations.
 * Handles the orchestration of data generation without UI dependencies.
 * 
 * Responsibilities:
 * - Coordinate data generation processes
 * - Apply templates and configurations
 * - Manage generation workflows
 * - Handle asynchronous generation tasks
 */
public class GeneratorController {
    
    private MainGenerator mainGenerator;
    private InputProcessor inputProcessor;
    private ExecutorService executor;
    private volatile boolean isGenerating;
    
    public GeneratorController() {
        this.mainGenerator = new MainGenerator();
        this.inputProcessor = new InputProcessor();
        this.executor = Executors.newSingleThreadExecutor();
        this.isGenerating = false;
    }
    
    /**
     * Generates data based on the provided template and configuration.
     * 
     * @param template The template defining the output format
     * @param configuration The generator configuration with settings
     * @param batchSize Number of items to generate
     * @return List of generated data strings
     */
    public List<String> generateNumbers(Template template, GeneratorConfiguration configuration, int batchSize) {
        if (template == null || configuration == null) {
            throw new IllegalArgumentException("Template and configuration cannot be null");
        }
        
        try {
            isGenerating = true;
            
            // Set up the generator with configuration
            mainGenerator.initialize(configuration);
            
            List<String> results = new ArrayList<>();
            
            // Generate the requested batch
            for (int i = 0; i < batchSize; i++) {
                Map<String, Object> generated = mainGenerator.generateNext();
                if (generated != null && !generated.isEmpty()) {
                    String formatted = applyTemplate(template, generated);
                    if (formatted != null && !formatted.trim().isEmpty()) {
                        results.add(formatted);
                    }
                }
            }
            
            return results;
        } finally {
            isGenerating = false;
        }
    }
    
    /**
     * Generates data asynchronously.
     * 
     * @param template The template defining the output format
     * @param configuration The generator configuration
     * @param batchSize Number of items to generate
     * @return CompletableFuture containing the generated data
     */
    public CompletableFuture<List<String>> generateNumbersAsync(Template template, 
                                                                 GeneratorConfiguration configuration, 
                                                                 int batchSize) {
        return CompletableFuture.supplyAsync(() -> 
            generateNumbers(template, configuration, batchSize), executor);
    }
    
    /**
     * Applies the template format to generated data.
     * 
     * @param template The template to apply
     * @param generatedData The data to format
     * @return Formatted string result
     */
    public String applyTemplate(Template template, Map<String, Object> generatedData) {
        if (template == null || generatedData == null) {
            return "";
        }
        
        try {
            return inputProcessor.formatOutput(template.getFormat(), generatedData);
        } catch (Exception e) {
            System.err.println("Error applying template: " + e.getMessage());
            return "";
        }
    }
    
    /**
     * Validates if a template format is valid.
     * 
     * @param templateFormat The format string to validate
     * @return true if valid, false otherwise
     */
    public boolean validateTemplateFormat(String templateFormat) {
        if (templateFormat == null || templateFormat.trim().isEmpty()) {
            return false;
        }
        
        try {
            // Basic validation - check for valid placeholder format
            return templateFormat.matches(".*\\{\\d+\\}.*") || 
                   templateFormat.trim().equals("{0}");
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Extracts the number of generators needed from a template format.
     * 
     * @param templateFormat The template format string
     * @return Number of generators required
     */
    public int extractGeneratorCount(String templateFormat) {
        if (templateFormat == null || templateFormat.trim().isEmpty()) {
            return 0;
        }
        
        int maxIndex = 0;
        String[] parts = templateFormat.split("\\{");
        
        for (String part : parts) {
            if (part.contains("}")) {
                try {
                    String indexStr = part.substring(0, part.indexOf("}"));
                    int index = Integer.parseInt(indexStr);
                    maxIndex = Math.max(maxIndex, index);
                } catch (NumberFormatException e) {
                    // Ignore invalid indices
                }
            }
        }
        
        return maxIndex + 1; // +1 because indices start at 0
    }
    
    /**
     * Creates a preview of what the generated data will look like.
     * 
     * @param template The template to preview
     * @param configuration The configuration to use
     * @return Preview string
     */
    public String generatePreview(Template template, GeneratorConfiguration configuration) {
        if (template == null || configuration == null) {
            return "Invalid template or configuration";
        }
        
        try {
            List<String> sample = generateNumbers(template, configuration, 1);
            if (!sample.isEmpty()) {
                return "Preview: " + sample.get(0);
            } else {
                return "Unable to generate preview";
            }
        } catch (Exception e) {
            return "Error generating preview: " + e.getMessage();
        }
    }
    
    /**
     * Checks if generation is currently in progress.
     * 
     * @return true if generating, false otherwise
     */
    public boolean isGenerating() {
        return isGenerating;
    }
    
    /**
     * Stops any ongoing generation process.
     */
    public void stopGeneration() {
        isGenerating = false;
        if (mainGenerator != null) {
            // Signal to stop if the generator supports it
            // This would need to be implemented in MainGenerator
        }
    }
    
    /**
     * Resets the generator state.
     */
    public void resetGenerator() {
        if (mainGenerator != null) {
            mainGenerator = new MainGenerator();
        }
        isGenerating = false;
    }
    
    /**
     * Validates a generator configuration.
     * 
     * @param configuration The configuration to validate
     * @return true if valid, false otherwise
     */
    public boolean validateConfiguration(GeneratorConfiguration configuration) {
        if (configuration == null) {
            return false;
        }
        
        try {
            // Basic validation - check if configuration has required data
            return configuration.getGeneratorConfigs() != null && 
                   !configuration.getGeneratorConfigs().isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Shuts down the controller and releases resources.
     */
    public void shutdown() {
        isGenerating = false;
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }
}