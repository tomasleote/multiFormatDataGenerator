package com.controller.business;

import com.model.GeneratorConfiguration;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Loads and manages pre-built example configurations.
 * Provides ready-to-use configurations for common data generation scenarios.
 * 
 * Responsibilities:
 * - Load pre-built example configurations
 * - Provide configuration templates for common use cases
 * - Manage example metadata and descriptions
 * - Support custom example registration
 */
public class ExampleLoader {
    
    /**
     * Example configuration metadata.
     */
    public static class ExampleMetadata {
        private final String name;
        private final String description;
        private final String category;
        private final String templateFormat;
        private final List<String> tags;
        private final String difficulty;
        
        public ExampleMetadata(String name, String description, String category, 
                             String templateFormat, List<String> tags, String difficulty) {
            this.name = name;
            this.description = description;
            this.category = category;
            this.templateFormat = templateFormat;
            this.tags = new ArrayList<>(tags != null ? tags : Collections.emptyList());
            this.difficulty = difficulty;
        }
        
        // Getters
        public String getName() { return name; }
        public String getDescription() { return description; }
        public String getCategory() { return category; }
        public String getTemplateFormat() { return templateFormat; }
        public List<String> getTags() { return new ArrayList<>(tags); }
        public String getDifficulty() { return difficulty; }
    }
    
    /**
     * Complete example with configuration and metadata.
     */
    public static class Example {
        private final ExampleMetadata metadata;
        private final GeneratorConfiguration configuration;
        
        public Example(ExampleMetadata metadata, GeneratorConfiguration configuration) {
            this.metadata = metadata;
            this.configuration = configuration;
        }
        
        public ExampleMetadata getMetadata() { return metadata; }
        public GeneratorConfiguration getConfiguration() { return configuration; }
    }
    
    private final Map<String, Example> examples;
    private final Map<String, List<String>> categorizedExamples;
    
    public ExampleLoader() {
        this.examples = new HashMap<>();
        this.categorizedExamples = new HashMap<>();
        loadBuiltInExamples();
    }
    
    /**
     * Loads all built-in example configurations.
     */
    private void loadBuiltInExamples() {
        loadDutchBSNExample();
        loadSequentialExamples();
        loadBusinessIdExamples();
        loadLicensePlateExamples();
    }
    
    /**
     * Loads the Dutch BSN number example.
     */
    private void loadDutchBSNExample() {
        ExampleMetadata metadata = new ExampleMetadata(
            "Dutch BSN Numbers",
            "Generates valid Dutch Burgerservicenummer (BSN) using the 11-proof validation",
            "Government ID",
            "{0}",
            List.of("dutch", "bsn", "government", "validation", "netherlands"),
            "Advanced"
        );
        
        GeneratorConfiguration config = new GeneratorConfiguration();
        
        // Sequential number generator for BSN base
        GeneratorConfiguration.GeneratorConfig bsnGenerator = new GeneratorConfiguration.GeneratorConfig();
        bsnGenerator.setType("SEQUENTIALNUMBERGENERATOR");
        
        Map<String, String> bsnProperties = new HashMap<>();
        bsnProperties.put("length", "9");
        bsnProperties.put("start", "100000000");
        bsnProperties.put("step", "1");
        bsnProperties.put("padding-length", "0");
        bsnProperties.put("input", "0");
        
        bsnGenerator.setProperties(bsnProperties);
        
        // Evaluator for BSN validation
        GeneratorConfiguration.GeneratorConfig evaluator = new GeneratorConfiguration.GeneratorConfig();
        evaluator.setType("EVALUATION");
        
        Map<String, String> evalProperties = new HashMap<>();
        evalProperties.put("formula", "(9*A + 8*B + 7*C + 6*D + 5*E + 4*F + 3*G + 2*H - I) % 11 == 0");
        evalProperties.put("input", "0");
        
        evaluator.setProperties(evalProperties);
        
        Map<String, GeneratorConfiguration.GeneratorConfig> generators = new HashMap<>();
        generators.put("0", bsnGenerator);
        generators.put("evaluator_0", evaluator);
        
        config.setGeneratorConfigs(generators);
        
        Example example = new Example(metadata, config);
        addExample("dutch_bsn", example);
    }
    
    /**
     * Loads license plate examples.
     */
    private void loadLicensePlateExamples() {
        ExampleMetadata euroMetadata = new ExampleMetadata(
            "European License Plates",
            "Generates European-style license plates with letters and numbers",
            "Automotive",
            "{0}-{1}-{2}",
            List.of("license", "plate", "automotive", "european", "vehicle"),
            "Intermediate"
        );
        
        GeneratorConfiguration euroConfig = new GeneratorConfiguration();
        
        // Letters part 1
        GeneratorConfiguration.GeneratorConfig letters1 = new GeneratorConfiguration.GeneratorConfig();
        letters1.setType("SEQUENTIALASCIIGENERATOR");
        
        Map<String, String> letters1Props = new HashMap<>();
        letters1Props.put("list", "A,B,C,D,E,F,G,H,J,K,L,M,N,P,R,S,T,U,V,W,X,Y,Z");
        letters1Props.put("length", "2");
        letters1Props.put("start", "AA");
        letters1Props.put("padding-length", "2");
        
        letters1.setProperties(letters1Props);
        
        // Numbers part
        GeneratorConfiguration.GeneratorConfig numbers = new GeneratorConfiguration.GeneratorConfig();
        numbers.setType("SEQUENTIALNUMBERGENERATOR");
        
        Map<String, String> numberProps = new HashMap<>();
        numberProps.put("length", "3");
        numberProps.put("start", "100");
        numberProps.put("step", "1");
        numberProps.put("padding-length", "3");
        
        numbers.setProperties(numberProps);
        
        // Letters part 2
        GeneratorConfiguration.GeneratorConfig letters2 = new GeneratorConfiguration.GeneratorConfig();
        letters2.setType("SEQUENTIALASCIIGENERATOR");
        
        Map<String, String> letters2Props = new HashMap<>();
        letters2Props.put("list", "A,B,C,D,E,F,G,H,J,K,L,M,N,P,R,S,T,U,V,W,X,Y,Z");
        letters2Props.put("length", "2");
        letters2Props.put("start", "AA");
        letters2Props.put("padding-length", "2");
        
        letters2.setProperties(letters2Props);
        
        Map<String, GeneratorConfiguration.GeneratorConfig> euroGenerators = new HashMap<>();
        euroGenerators.put("0", letters1);
        euroGenerators.put("1", numbers);
        euroGenerators.put("2", letters2);
        
        euroConfig.setGeneratorConfigs(euroGenerators);
        
        Example euroExample = new Example(euroMetadata, euroConfig);
        addExample("euro_license_plate", euroExample);
    }
    
    /**
     * Loads sequential examples.
     */
    private void loadSequentialExamples() {
        // Simple sequential numbers
        ExampleMetadata simpleMetadata = new ExampleMetadata(
            "Simple Sequential Numbers",
            "Basic sequential number generation starting from 1",
            "Basic",
            "{0}",
            List.of("sequential", "numbers", "basic", "simple"),
            "Beginner"
        );
        
        GeneratorConfiguration simpleConfig = new GeneratorConfiguration();
        
        GeneratorConfiguration.GeneratorConfig simpleGen = new GeneratorConfiguration.GeneratorConfig();
        simpleGen.setType("SEQUENTIALNUMBERGENERATOR");
        
        Map<String, String> simpleProperties = new HashMap<>();
        simpleProperties.put("length", "5");
        simpleProperties.put("start", "1");
        simpleProperties.put("step", "1");
        simpleProperties.put("padding-length", "0");
        
        simpleGen.setProperties(simpleProperties);
        
        Map<String, GeneratorConfiguration.GeneratorConfig> simpleGenerators = new HashMap<>();
        simpleGenerators.put("0", simpleGen);
        
        simpleConfig.setGeneratorConfigs(simpleGenerators);
        
        Example simpleExample = new Example(simpleMetadata, simpleConfig);
        addExample("simple_sequential", simpleExample);
    }
    
    /**
     * Loads business ID examples.
     */
    private void loadBusinessIdExamples() {
        ExampleMetadata metadata = new ExampleMetadata(
            "Custom Business IDs",
            "Generates custom business identifiers with prefix and sequential numbers",
            "Business",
            "BIZ-{0}-{1}",
            List.of("business", "id", "custom", "prefix", "enterprise"),
            "Intermediate"
        );
        
        GeneratorConfiguration config = new GeneratorConfiguration();
        
        // Department code
        GeneratorConfiguration.GeneratorConfig deptGen = new GeneratorConfiguration.GeneratorConfig();
        deptGen.setType("SEQUENTIALASCIIGENERATOR");
        
        Map<String, String> deptProperties = new HashMap<>();
        deptProperties.put("list", "IT,HR,FN,MK,SL,OP");
        deptProperties.put("length", "2");
        deptProperties.put("start", "IT");
        deptProperties.put("padding-length", "2");
        
        deptGen.setProperties(deptProperties);
        
        // Sequential ID
        GeneratorConfiguration.GeneratorConfig idGen = new GeneratorConfiguration.GeneratorConfig();
        idGen.setType("SEQUENTIALNUMBERGENERATOR");
        
        Map<String, String> idProperties = new HashMap<>();
        idProperties.put("length", "6");
        idProperties.put("start", "100000");
        idProperties.put("step", "1");
        idProperties.put("padding-length", "6");
        
        idGen.setProperties(idProperties);
        
        Map<String, GeneratorConfiguration.GeneratorConfig> bizGenerators = new HashMap<>();
        bizGenerators.put("0", deptGen);
        bizGenerators.put("1", idGen);
        
        config.setGeneratorConfigs(bizGenerators);
        
        Example bizExample = new Example(metadata, config);
        addExample("business_ids", bizExample);
    }
    
    /**
     * Adds an example to the collection.
     * 
     * @param key The unique key for the example
     * @param example The example to add
     */
    private void addExample(String key, Example example) {
        examples.put(key, example);
        
        // Add to category
        String category = example.getMetadata().getCategory();
        categorizedExamples.computeIfAbsent(category, k -> new ArrayList<>()).add(key);
    }
    
    /**
     * Gets an example by key.
     * 
     * @param key The example key
     * @return The example, or null if not found
     */
    public Example getExample(String key) {
        return examples.get(key);
    }
    
    /**
     * Gets all available example keys.
     * 
     * @return List of all example keys
     */
    public List<String> getAllExampleKeys() {
        return new ArrayList<>(examples.keySet());
    }
    
    /**
     * Gets all examples.
     * 
     * @return Map of all examples
     */
    public Map<String, Example> getAllExamples() {
        return new HashMap<>(examples);
    }
    
    /**
     * Gets examples by category.
     * 
     * @param category The category to filter by
     * @return List of example keys in the category
     */
    public List<String> getExamplesByCategory(String category) {
        return new ArrayList<>(categorizedExamples.getOrDefault(category, Collections.emptyList()));
    }
    
    /**
     * Gets all available categories.
     * 
     * @return List of all categories
     */
    public List<String> getAllCategories() {
        return new ArrayList<>(categorizedExamples.keySet());
    }
    
    /**
     * Searches examples by tags.
     * 
     * @param tags The tags to search for
     * @return List of matching example keys
     */
    public List<String> searchByTags(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return getAllExampleKeys();
        }
        
        List<String> results = new ArrayList<>();
        
        for (Map.Entry<String, Example> entry : examples.entrySet()) {
            List<String> exampleTags = entry.getValue().getMetadata().getTags();
            
            // Check if any of the search tags match
            for (String tag : tags) {
                if (exampleTags.stream().anyMatch(exampleTag -> 
                    exampleTag.toLowerCase().contains(tag.toLowerCase()))) {
                    results.add(entry.getKey());
                    break;
                }
            }
        }
        
        return results;
    }
    
    /**
     * Gets example metadata without the full configuration.
     * 
     * @param key The example key
     * @return The example metadata, or null if not found
     */
    public ExampleMetadata getExampleMetadata(String key) {
        Example example = examples.get(key);
        return example != null ? example.getMetadata() : null;
    }
    
    /**
     * Loads a specific example configuration.
     * 
     * @param key The example key
     * @return The generator configuration, or null if not found
     */
    public GeneratorConfiguration loadBSNExample() {
        Example example = getExample("dutch_bsn");
        return example != null ? example.getConfiguration() : null;
    }
    
    /**
     * Loads the license plate example configuration.
     * 
     * @return The generator configuration, or null if not found
     */
    public GeneratorConfiguration loadLicensePlateExample() {
        Example example = getExample("euro_license_plate");
        return example != null ? example.getConfiguration() : null;
    }
    
    /**
     * Gets the total number of examples.
     * 
     * @return Number of available examples
     */
    public int getExampleCount() {
        return examples.size();
    }
}