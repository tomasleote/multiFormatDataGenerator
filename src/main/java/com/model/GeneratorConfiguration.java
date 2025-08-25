package com.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Configuration model for saving and loading generator configurations.
 * This class represents the complete state of a data generation setup.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneratorConfiguration {
    
    private String version = "3.0";
    private String templateFormat;
    private int batchSize = 100;
    private int evaluatorCount = 0;
    private List<GeneratorConfig> generators;
    private ConfigurationMetadata metadata;
    
    /**
     * Individual generator configuration
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GeneratorConfig {
        private int index;
        private String type;
        private Map<String, String> properties;
        private boolean isEvaluator = false;
    }
    
    /**
     * Configuration metadata
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConfigurationMetadata {
        private String name;
        private String description;
        private String createdDate;
        private String lastModified;
        private String author = "Data Generator User";
        private List<String> tags;
    }
}
