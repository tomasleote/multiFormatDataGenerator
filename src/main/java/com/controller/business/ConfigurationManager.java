package com.controller.business;

import com.model.GeneratorConfiguration;
import com.service.SimpleConfigurationService;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Optional;
import java.io.File;

/**
 * Manages generator configurations including save/load operations.
 * Provides configuration persistence and management functionality.
 * 
 * Responsibilities:
 * - Save and load configurations
 * - Manage configuration history
 * - Provide configuration templates
 * - Handle auto-save functionality
 */
public class ConfigurationManager {
    
    private final SimpleConfigurationService configurationService;
    private final Map<String, GeneratorConfiguration> savedConfigurations;
    private final List<GeneratorConfiguration> configurationHistory;
    private GeneratorConfiguration currentConfiguration;
    private boolean autoSaveEnabled;
    private String lastSavedPath;
    private final int maxHistorySize;
    
    public ConfigurationManager() {
        this.configurationService = new SimpleConfigurationService();
        this.savedConfigurations = new ConcurrentHashMap<>();
        this.configurationHistory = new ArrayList<>();
        this.autoSaveEnabled = false;
        this.maxHistorySize = 20;
    }
    
    /**
     * Saves a configuration to file.
     * 
     * @param configuration The configuration to save
     * @param filePath The file path to save to
     * @return true if successful, false otherwise
     */
    public boolean saveConfiguration(GeneratorConfiguration configuration, String filePath) {
        if (configuration == null || filePath == null || filePath.trim().isEmpty()) {
            return false;
        }
        
        try {
            boolean success = configurationService.saveConfiguration(configuration, filePath);
            if (success) {
                lastSavedPath = filePath;
                addToHistory(configuration);
                
                // Cache the configuration
                String fileName = new File(filePath).getName();
                savedConfigurations.put(fileName, configuration);
            }
            return success;
        } catch (Exception e) {
            System.err.println("Error saving configuration: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Loads a configuration from file.
     * 
     * @param filePath The file path to load from
     * @return Optional containing the configuration if successful
     */
    public Optional<GeneratorConfiguration> loadConfiguration(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            return Optional.empty();
        }
        
        try {
            GeneratorConfiguration config = configurationService.loadConfiguration(filePath);
            if (config != null) {
                currentConfiguration = config;
                lastSavedPath = filePath;
                addToHistory(config);
                
                // Cache the configuration
                String fileName = new File(filePath).getName();
                savedConfigurations.put(fileName, config);
                
                return Optional.of(config);
            }
        } catch (Exception e) {
            System.err.println("Error loading configuration: " + e.getMessage());
        }
        
        return Optional.empty();
    }
    
    /**
     * Auto-saves the current configuration if enabled.
     * 
     * @param configuration The configuration to auto-save
     */
    public void autoSave(GeneratorConfiguration configuration) {
        if (!autoSaveEnabled || configuration == null) {
            return;
        }
        
        try {
            if (lastSavedPath != null && !lastSavedPath.trim().isEmpty()) {
                saveConfiguration(configuration, lastSavedPath);
            } else {
                // Create a default auto-save file
                String autoSavePath = "auto_save_config.json";
                saveConfiguration(configuration, autoSavePath);
            }
        } catch (Exception e) {
            System.err.println("Auto-save failed: " + e.getMessage());
        }
    }
    
    /**
     * Creates a new empty configuration with default settings.
     * 
     * @return A new GeneratorConfiguration instance
     */
    public GeneratorConfiguration createDefaultConfiguration() {
        GeneratorConfiguration config = new GeneratorConfiguration();
        
        // Add a default sequential number generator
        GeneratorConfiguration.GeneratorConfig defaultGenerator = 
            new GeneratorConfiguration.GeneratorConfig();
        defaultGenerator.setType("SEQUENTIALNUMBERGENERATOR");
        
        Map<String, String> defaultProperties = new HashMap<>();
        defaultProperties.put("length", "5");
        defaultProperties.put("start", "1");
        defaultProperties.put("step", "1");
        defaultProperties.put("padding-length", "0");
        
        defaultGenerator.setProperties(defaultProperties);
        
        Map<String, GeneratorConfiguration.GeneratorConfig> generators = new HashMap<>();
        generators.put("0", defaultGenerator);
        
        config.setGeneratorConfigs(generators);
        
        return config;
    }
    
    /**
     * Creates a configuration based on a template format.
     * 
     * @param templateFormat The template format string
     * @param generatorCount Number of generators needed
     * @return Generated configuration
     */
    public GeneratorConfiguration createConfigurationFromTemplate(String templateFormat, int generatorCount) {
        GeneratorConfiguration config = new GeneratorConfiguration();
        Map<String, GeneratorConfiguration.GeneratorConfig> generators = new HashMap<>();
        
        for (int i = 0; i < generatorCount; i++) {
            GeneratorConfiguration.GeneratorConfig generator = createDefaultGeneratorConfig(i);
            generators.put(String.valueOf(i), generator);
        }
        
        config.setGeneratorConfigs(generators);
        return config;
    }
    
    /**
     * Creates a default generator configuration for a given index.
     * 
     * @param index The generator index
     * @return Default generator configuration
     */
    private GeneratorConfiguration.GeneratorConfig createDefaultGeneratorConfig(int index) {
        GeneratorConfiguration.GeneratorConfig config = new GeneratorConfiguration.GeneratorConfig();
        config.setType("SEQUENTIALNUMBERGENERATOR");
        
        Map<String, String> properties = new HashMap<>();
        properties.put("length", "3");
        properties.put("start", String.valueOf(100 + index * 100));
        properties.put("step", "1");
        properties.put("padding-length", "0");
        
        config.setProperties(properties);
        return config;
    }
    
    /**
     * Gets the current configuration.
     * 
     * @return Current configuration or null if none set
     */
    public GeneratorConfiguration getCurrentConfiguration() {
        return currentConfiguration;
    }
    
    /**
     * Sets the current configuration.
     * 
     * @param configuration The configuration to set as current
     */
    public void setCurrentConfiguration(GeneratorConfiguration configuration) {
        this.currentConfiguration = configuration;
        addToHistory(configuration);
        
        if (autoSaveEnabled) {
            autoSave(configuration);
        }
    }
    
    /**
     * Gets the configuration history.
     * 
     * @return List of recent configurations
     */
    public List<GeneratorConfiguration> getConfigurationHistory() {
        return new ArrayList<>(configurationHistory);
    }
    
    /**
     * Adds a configuration to the history.
     * 
     * @param configuration The configuration to add
     */
    private void addToHistory(GeneratorConfiguration configuration) {
        if (configuration == null) {
            return;
        }
        
        // Remove if already exists to avoid duplicates
        configurationHistory.removeIf(config -> 
            config != null && configsEqual(config, configuration));
        
        // Add to the beginning
        configurationHistory.add(0, deepCopy(configuration));
        
        // Maintain max history size
        while (configurationHistory.size() > maxHistorySize) {
            configurationHistory.remove(configurationHistory.size() - 1);
        }
    }
    
    /**
     * Checks if two configurations are equal.
     * 
     * @param config1 First configuration
     * @param config2 Second configuration
     * @return true if equal, false otherwise
     */
    private boolean configsEqual(GeneratorConfiguration config1, GeneratorConfiguration config2) {
        if (config1 == config2) return true;
        if (config1 == null || config2 == null) return false;
        
        // Simple comparison - could be enhanced
        Map<String, GeneratorConfiguration.GeneratorConfig> configs1 = config1.getGeneratorConfigs();
        Map<String, GeneratorConfiguration.GeneratorConfig> configs2 = config2.getGeneratorConfigs();
        
        return configs1 != null && configs2 != null && configs1.size() == configs2.size();
    }
    
    /**
     * Creates a deep copy of a configuration.
     * 
     * @param original The original configuration
     * @return Deep copy of the configuration
     */
    private GeneratorConfiguration deepCopy(GeneratorConfiguration original) {
        // This is a simplified deep copy - in a real implementation,
        // you might want to use a proper deep copy library or serialization
        GeneratorConfiguration copy = new GeneratorConfiguration();
        
        if (original.getGeneratorConfigs() != null) {
            Map<String, GeneratorConfiguration.GeneratorConfig> copyConfigs = new HashMap<>();
            
            for (Map.Entry<String, GeneratorConfiguration.GeneratorConfig> entry : 
                 original.getGeneratorConfigs().entrySet()) {
                
                GeneratorConfiguration.GeneratorConfig originalConfig = entry.getValue();
                GeneratorConfiguration.GeneratorConfig copyConfig = new GeneratorConfiguration.GeneratorConfig();
                
                copyConfig.setType(originalConfig.getType());
                
                if (originalConfig.getProperties() != null) {
                    copyConfig.setProperties(new HashMap<>(originalConfig.getProperties()));
                }
                
                copyConfigs.put(entry.getKey(), copyConfig);
            }
            
            copy.setGeneratorConfigs(copyConfigs);
        }
        
        return copy;
    }
    
    /**
     * Clears the configuration history.
     */
    public void clearHistory() {
        configurationHistory.clear();
    }
    
    /**
     * Gets all saved configuration names.
     * 
     * @return List of saved configuration file names
     */
    public List<String> getSavedConfigurationNames() {
        return new ArrayList<>(savedConfigurations.keySet());
    }
    
    /**
     * Gets a saved configuration by name.
     * 
     * @param name The configuration file name
     * @return Optional containing the configuration if found
     */
    public Optional<GeneratorConfiguration> getSavedConfiguration(String name) {
        return Optional.ofNullable(savedConfigurations.get(name));
    }
    
    /**
     * Enables or disables auto-save functionality.
     * 
     * @param enabled true to enable auto-save, false to disable
     */
    public void setAutoSaveEnabled(boolean enabled) {
        this.autoSaveEnabled = enabled;
    }
    
    /**
     * Checks if auto-save is enabled.
     * 
     * @return true if auto-save is enabled, false otherwise
     */
    public boolean isAutoSaveEnabled() {
        return autoSaveEnabled;
    }
    
    /**
     * Gets the last saved file path.
     * 
     * @return The last saved file path, or null if none
     */
    public String getLastSavedPath() {
        return lastSavedPath;
    }
    
    /**
     * Sets the last saved file path.
     * 
     * @param path The file path to set
     */
    public void setLastSavedPath(String path) {
        this.lastSavedPath = path;
    }
    
    /**
     * Resets all configurations and history.
     */
    public void reset() {
        currentConfiguration = null;
        lastSavedPath = null;
        configurationHistory.clear();
        savedConfigurations.clear();
        autoSaveEnabled = false;
    }
    
    /**
     * Validates that a configuration can be saved.
     * 
     * @param configuration The configuration to validate
     * @return true if the configuration can be saved, false otherwise
     */
    public boolean canSaveConfiguration(GeneratorConfiguration configuration) {
        return configuration != null && 
               configuration.getGeneratorConfigs() != null && 
               !configuration.getGeneratorConfigs().isEmpty();
    }
    
    /**
     * Creates a configuration for common use cases (Dutch BSN, License plates, etc.).
     * 
     * @param templateType The type of template to create
     * @return Pre-configured GeneratorConfiguration
     */
    public GeneratorConfiguration createTemplateConfiguration(String templateType) {
        switch (templateType.toUpperCase()) {
            case "DUTCH_BSN":
                return createDutchBSNConfiguration();
            case "LICENSE_PLATE":
                return createLicensePlateConfiguration();
            case "CREDIT_CARD":
                return createCreditCardConfiguration();
            default:
                return createDefaultConfiguration();
        }
    }
    
    private GeneratorConfiguration createDutchBSNConfiguration() {
        GeneratorConfiguration config = new GeneratorConfiguration();
        
        // Sequential number generator for BSN base
        GeneratorConfiguration.GeneratorConfig bsnGenerator = new GeneratorConfiguration.GeneratorConfig();
        bsnGenerator.setType("SEQUENTIALNUMBERGENERATOR");
        
        Map<String, String> bsnProperties = new HashMap<>();
        bsnProperties.put("length", "9");
        bsnProperties.put("start", "100000000");
        bsnProperties.put("step", "1");
        bsnProperties.put("padding-length", "0");
        
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
        return config;
    }
    
    private GeneratorConfiguration createLicensePlateConfiguration() {
        GeneratorConfiguration config = new GeneratorConfiguration();
        
        // Letters part
        GeneratorConfiguration.GeneratorConfig letters = new GeneratorConfiguration.GeneratorConfig();
        letters.setType("SEQUENTIALASCIIGENERATOR");
        
        Map<String, String> letterProps = new HashMap<>();
        letterProps.put("list", "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z");
        letterProps.put("length", "2");
        letterProps.put("start", "AA");
        
        letters.setProperties(letterProps);
        
        // Numbers part
        GeneratorConfiguration.GeneratorConfig numbers = new GeneratorConfiguration.GeneratorConfig();
        numbers.setType("SEQUENTIALNUMBERGENERATOR");
        
        Map<String, String> numberProps = new HashMap<>();
        numberProps.put("length", "3");
        numberProps.put("start", "100");
        numberProps.put("step", "1");
        numberProps.put("padding-length", "3");
        
        numbers.setProperties(numberProps);
        
        Map<String, GeneratorConfiguration.GeneratorConfig> generators = new HashMap<>();
        generators.put("0", letters);
        generators.put("1", numbers);
        
        config.setGeneratorConfigs(generators);
        return config;
    }
    
    private GeneratorConfiguration createCreditCardConfiguration() {
        GeneratorConfiguration config = new GeneratorConfiguration();
        
        // Credit card number generator
        GeneratorConfiguration.GeneratorConfig cardNumber = new GeneratorConfiguration.GeneratorConfig();
        cardNumber.setType("SEQUENTIALNUMBERGENERATOR");
        
        Map<String, String> cardProps = new HashMap<>();
        cardProps.put("length", "16");
        cardProps.put("start", "4000000000000000");
        cardProps.put("step", "1");
        cardProps.put("padding-length", "16");
        
        cardNumber.setProperties(cardProps);
        
        Map<String, GeneratorConfiguration.GeneratorConfig> generators = new HashMap<>();
        generators.put("0", cardNumber);
        
        config.setGeneratorConfigs(generators);
        return config;
    }
}