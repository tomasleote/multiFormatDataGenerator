package com.service;

import com.model.GeneratorConfiguration;
import com.view.GeneratorPanel;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Simplified configuration service without Jackson dependency
 */
public class SimpleConfigurationService {
    
    private static final String CONFIG_EXTENSION = ".txt";
    private static final String DEFAULT_CONFIG_NAME = "generator-config";
    
    private File lastSaveLocation;
    
    /**
     * Save current UI configuration to a simple text file
     */
    public boolean saveConfiguration(Component parent, String templateFormat, int batchSize, 
                                   int evaluatorCount, List<Component> generatorComponents) {
        try {
            // Show save dialog
            JFileChooser fileChooser = createFileChooser("Save Configuration", true);
            if (lastSaveLocation != null) {
                fileChooser.setCurrentDirectory(lastSaveLocation.getParentFile());
            }
            
            int result = fileChooser.showSaveDialog(parent);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                
                // Add extension if not present
                if (!file.getName().toLowerCase().endsWith(CONFIG_EXTENSION)) {
                    file = new File(file.getAbsolutePath() + CONFIG_EXTENSION);
                }
                
                // Save to file
                saveConfigurationToFile(file, templateFormat, batchSize, evaluatorCount, generatorComponents);
                lastSaveLocation = file;
                
                return true;
            }
        } catch (Exception e) {
            showErrorDialog(parent, "Save Error", 
                "Failed to save configuration: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Load configuration from a simple text file
     */
    public GeneratorConfiguration loadConfiguration(Component parent) {
        try {
            // Show open dialog
            JFileChooser fileChooser = createFileChooser("Load Configuration", false);
            if (lastSaveLocation != null) {
                fileChooser.setCurrentDirectory(lastSaveLocation.getParentFile());
            }
            
            int result = fileChooser.showOpenDialog(parent);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                
                // Load from file
                GeneratorConfiguration config = loadConfigurationFromFile(file);
                lastSaveLocation = file;
                
                return config;
            }
        } catch (Exception e) {
            showErrorDialog(parent, "Load Error", 
                "Failed to load configuration: " + e.getMessage());
        }
        return null;
    }
    
    private void saveConfigurationToFile(File file, String templateFormat, int batchSize,
                                       int evaluatorCount, List<Component> generatorComponents) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println("# Multi-Format Data Generator Configuration");
            writer.println("# Generated: " + getCurrentTimestamp());
            writer.println("# Version: 3.0");
            writer.println();
            
            writer.println("template_format=" + templateFormat);
            writer.println("batch_size=" + batchSize);
            writer.println("evaluator_count=" + evaluatorCount);
            writer.println();
            
            writer.println("# Generator Configurations");
            int index = 0;
            for (Component comp : generatorComponents) {
                if (comp instanceof GeneratorPanel) {
                    GeneratorPanel panel = (GeneratorPanel) comp;
                    Map<String, String> properties = panel.getProperties();
                    
                    writer.println("generator." + index + ".type=" + properties.get("type"));
                    writer.println("generator." + index + ".isEvaluator=" + (panel.getGeneratorIndex() < 0));
                    
                    for (Map.Entry<String, String> prop : properties.entrySet()) {
                        if (!prop.getKey().equals("type")) {
                            writer.println("generator." + index + "." + prop.getKey() + "=" + prop.getValue());
                        }
                    }
                    writer.println();
                    index++;
                }
            }
        }
    }
    
    private GeneratorConfiguration loadConfigurationFromFile(File file) throws IOException {
        GeneratorConfiguration config = new GeneratorConfiguration();
        GeneratorConfiguration.ConfigurationMetadata metadata = new GeneratorConfiguration.ConfigurationMetadata();
        List<GeneratorConfiguration.GeneratorConfig> generators = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("#") || line.isEmpty()) {
                    continue;
                }
                
                if (line.startsWith("template_format=")) {
                    config.setTemplateFormat(line.substring("template_format=".length()));
                } else if (line.startsWith("batch_size=")) {
                    config.setBatchSize(Integer.parseInt(line.substring("batch_size=".length())));
                } else if (line.startsWith("evaluator_count=")) {
                    config.setEvaluatorCount(Integer.parseInt(line.substring("evaluator_count=".length())));
                }
                // Note: For simplicity, we're not parsing generator configurations in this version
            }
        }
        
        metadata.setName(getFileNameWithoutExtension(file));
        metadata.setLastModified(getCurrentTimestamp());
        config.setMetadata(metadata);
        config.setGenerators(generators);
        
        return config;
    }
    
    private JFileChooser createFileChooser(String title, boolean forSave) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(title);
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Configuration files", "txt"));
        
        if (forSave) {
            fileChooser.setSelectedFile(new File(DEFAULT_CONFIG_NAME + CONFIG_EXTENSION));
        }
        
        return fileChooser;
    }
    
    private String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    
    private String getFileNameWithoutExtension(File file) {
        String name = file.getName();
        int lastDot = name.lastIndexOf('.');
        return lastDot > 0 ? name.substring(0, lastDot) : name;
    }
    
    private void showErrorDialog(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
    }
}
