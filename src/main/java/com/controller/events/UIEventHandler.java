package com.controller.events;

import com.controller.business.GeneratorController;
import com.controller.business.ValidationController;
import com.controller.business.ConfigurationManager;
import com.controller.business.DataExportManager;
import com.controller.business.ExampleLoader;
import com.model.GeneratorConfiguration;
import com.model.Template;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Centralized event handling for the UI.
 * Coordinates between UI components and business logic controllers.
 * 
 * Responsibilities:
 * - Handle UI events and delegate to appropriate controllers
 * - Coordinate between different UI components
 * - Manage event listener registration
 * - Provide event-driven communication
 */
public class UIEventHandler {
    
    /**
     * Event types for the application.
     */
    public enum EventType {
        TEMPLATE_APPLIED,
        CONFIGURATION_CHANGED,
        GENERATION_STARTED,
        GENERATION_COMPLETED,
        GENERATION_FAILED,
        VALIDATION_ERROR,
        EXPORT_STARTED,
        EXPORT_COMPLETED,
        CONFIGURATION_SAVED,
        CONFIGURATION_LOADED,
        EXAMPLE_LOADED,
        PREVIEW_UPDATED,
        STATUS_CHANGED
    }
    
    /**
     * Event data container.
     */
    public static class EventData {
        private final EventType type;
        private final Object data;
        private final String message;
        private final long timestamp;
        
        public EventData(EventType type, Object data, String message) {
            this.type = type;
            this.data = data;
            this.message = message;
            this.timestamp = System.currentTimeMillis();
        }
        
        public EventType getType() { return type; }
        public Object getData() { return data; }
        public String getMessage() { return message; }
        public long getTimestamp() { return timestamp; }
    }
    
    /**
     * Event listener interface.
     */
    public interface EventListener {
        void onEvent(EventData eventData);
    }
    
    // Controllers
    private final GeneratorController generatorController;
    private final ValidationController validationController;
    private final ConfigurationManager configurationManager;
    private final DataExportManager exportManager;
    private final ExampleLoader exampleLoader;
    
    // Event listeners
    private final Map<EventType, List<EventListener>> eventListeners;
    
    // UI References (to be set by the UI)
    private JTextField templateFormatField;
    private JTextArea outputArea;
    private JProgressBar progressBar;
    private JLabel statusLabel;
    private JLabel previewLabel;
    private JPanel generatorPanelContainer;
    
    public UIEventHandler(GeneratorController generatorController,
                         ValidationController validationController,
                         ConfigurationManager configurationManager,
                         DataExportManager exportManager,
                         ExampleLoader exampleLoader) {
        this.generatorController = generatorController;
        this.validationController = validationController;
        this.configurationManager = configurationManager;
        this.exportManager = exportManager;
        this.exampleLoader = exampleLoader;
        this.eventListeners = new HashMap<>();
        
        // Initialize event listener lists
        for (EventType type : EventType.values()) {
            eventListeners.put(type, new java.util.ArrayList<>());
        }
    }
    
    /**
     * Sets UI component references.
     * 
     * @param templateFormatField Template format input field
     * @param outputArea Output display area
     * @param progressBar Progress indicator
     * @param statusLabel Status display label
     * @param previewLabel Preview display label
     * @param generatorPanelContainer Container for generator panels
     */
    public void setUIReferences(JTextField templateFormatField,
                               JTextArea outputArea,
                               JProgressBar progressBar,
                               JLabel statusLabel,
                               JLabel previewLabel,
                               JPanel generatorPanelContainer) {
        this.templateFormatField = templateFormatField;
        this.outputArea = outputArea;
        this.progressBar = progressBar;
        this.statusLabel = statusLabel;
        this.previewLabel = previewLabel;
        this.generatorPanelContainer = generatorPanelContainer;
    }
    
    /**
     * Registers an event listener for a specific event type.
     * 
     * @param eventType The event type to listen for
     * @param listener The listener to register
     */
    public void addEventListener(EventType eventType, EventListener listener) {
        eventListeners.get(eventType).add(listener);
    }
    
    /**
     * Removes an event listener.
     * 
     * @param eventType The event type
     * @param listener The listener to remove
     */
    public void removeEventListener(EventType eventType, EventListener listener) {
        eventListeners.get(eventType).remove(listener);
    }
    
    /**
     * Fires an event to all registered listeners.
     * 
     * @param eventData The event data
     */
    public void fireEvent(EventData eventData) {
        List<EventListener> listeners = eventListeners.get(eventData.getType());
        if (listeners != null) {
            for (EventListener listener : listeners) {
                try {
                    listener.onEvent(eventData);
                } catch (Exception e) {
                    System.err.println("Error in event listener: " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Creates an ActionListener for applying templates.
     * 
     * @return ActionListener for template application
     */
    public ActionListener createApplyTemplateListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleApplyTemplate();
            }
        };
    }
    
    /**
     * Creates an ActionListener for generating data.
     * 
     * @param batchSizeSupplier Supplier for batch size
     * @return ActionListener for data generation
     */
    public ActionListener createGenerateListener(java.util.function.Supplier<Integer> batchSizeSupplier) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleGenerate(batchSizeSupplier.get());
            }
        };
    }
    
    /**
     * Creates an ActionListener for clearing output.
     * 
     * @return ActionListener for clearing output
     */
    public ActionListener createClearOutputListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleClearOutput();
            }
        };
    }
    
    /**
     * Creates an ActionListener for saving configurations.
     * 
     * @return ActionListener for saving configurations
     */
    public ActionListener createSaveConfigListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSaveConfiguration();
            }
        };
    }
    
    /**
     * Creates an ActionListener for loading configurations.
     * 
     * @return ActionListener for loading configurations
     */
    public ActionListener createLoadConfigListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLoadConfiguration();
            }
        };
    }
    
    /**
     * Creates an ActionListener for exporting data.
     * 
     * @param formatSupplier Supplier for export format
     * @return ActionListener for data export
     */
    public ActionListener createExportListener(java.util.function.Supplier<DataExportManager.ExportFormat> formatSupplier) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleExport(formatSupplier.get());
            }
        };
    }
    
    /**
     * Creates an ActionListener for loading examples.
     * 
     * @param exampleKey The example key to load
     * @return ActionListener for loading examples
     */
    public ActionListener createLoadExampleListener(String exampleKey) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLoadExample(exampleKey);
            }
        };
    }
    
    /**
     * Handles template application.
     */
    private void handleApplyTemplate() {
        if (templateFormatField == null) return;
        
        String templateFormat = templateFormatField.getText();
        
        // Validate template format
        ValidationController.ValidationResult result = validationController.validateTemplateFormat(templateFormat);
        
        if (!result.isValid()) {
            updateStatus("Template validation failed: " + String.join(", ", result.getErrors()));
            fireEvent(new EventData(EventType.VALIDATION_ERROR, result, "Template validation failed"));
            return;
        }
        
        // Create template
        Template template = new Template();
        template.setFormat(templateFormat);
        
        // Extract generator count
        int generatorCount = generatorController.extractGeneratorCount(templateFormat);
        
        // Create default configuration
        GeneratorConfiguration config = configurationManager.createConfigurationFromTemplate(templateFormat, generatorCount);
        configurationManager.setCurrentConfiguration(config);
        
        updateStatus("Template applied successfully. " + generatorCount + " generators required.");
        
        // Show warnings if any
        if (!result.getWarnings().isEmpty()) {
            updateStatus("Warnings: " + String.join(", ", result.getWarnings()));
        }
        
        fireEvent(new EventData(EventType.TEMPLATE_APPLIED, template, "Template applied successfully"));
        
        // Update preview
        updatePreview(template, config);
    }
    
    /**
     * Handles data generation.
     * 
     * @param batchSize Number of items to generate
     */
    private void handleGenerate(int batchSize) {
        GeneratorConfiguration config = configurationManager.getCurrentConfiguration();
        if (config == null) {
            updateStatus("No configuration available. Please apply a template first.");
            return;
        }
        
        String templateFormat = templateFormatField != null ? templateFormatField.getText() : "{0}";
        Template template = new Template();
        template.setFormat(templateFormat);
        
        updateStatus("Generating " + batchSize + " items...");
        setProgress(0);
        
        fireEvent(new EventData(EventType.GENERATION_STARTED, batchSize, "Generation started"));
        
        // Generate asynchronously
        CompletableFuture<List<String>> future = generatorController.generateNumbersAsync(template, config, batchSize);
        
        future.whenComplete((results, throwable) -> {
            SwingUtilities.invokeLater(() -> {
                if (throwable != null) {
                    updateStatus("Generation failed: " + throwable.getMessage());
                    setProgress(0);
                    fireEvent(new EventData(EventType.GENERATION_FAILED, throwable, "Generation failed"));
                } else {
                    displayResults(results);
                    updateStatus("Generated " + results.size() + " items successfully.");
                    setProgress(100);
                    fireEvent(new EventData(EventType.GENERATION_COMPLETED, results, "Generation completed"));
                }
            });
        });
    }
    
    /**
     * Handles clearing output.
     */
    private void handleClearOutput() {
        if (outputArea != null) {
            outputArea.setText("");
        }
        updateStatus("Output cleared.");
    }
    
    /**
     * Handles saving configuration.
     */
    private void handleSaveConfiguration() {
        GeneratorConfiguration config = configurationManager.getCurrentConfiguration();
        if (config == null) {
            updateStatus("No configuration to save.");
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Configuration");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("JSON files", "json"));
        
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filePath.endsWith(".json")) {
                filePath += ".json";
            }
            
            boolean success = configurationManager.saveConfiguration(config, filePath);
            if (success) {
                updateStatus("Configuration saved to: " + filePath);
                fireEvent(new EventData(EventType.CONFIGURATION_SAVED, filePath, "Configuration saved"));
            } else {
                updateStatus("Failed to save configuration.");
            }
        }
    }
    
    /**
     * Handles loading configuration.
     */
    private void handleLoadConfiguration() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load Configuration");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("JSON files", "json"));
        
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            
            configurationManager.loadConfiguration(filePath).ifPresentOrElse(
                config -> {
                    updateStatus("Configuration loaded from: " + filePath);
                    fireEvent(new EventData(EventType.CONFIGURATION_LOADED, config, "Configuration loaded"));
                },
                () -> updateStatus("Failed to load configuration.")
            );
        }
    }
    
    /**
     * Handles data export.
     * 
     * @param format Export format
     */
    private void handleExport(DataExportManager.ExportFormat format) {
        if (outputArea == null || outputArea.getText().trim().isEmpty()) {
            updateStatus("No data to export.");
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Data");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            format.getDescription(), format.getExtension().substring(1)));
        
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            
            // Get data from output area
            String[] lines = outputArea.getText().split("\n");
            List<String> data = List.of(lines);
            
            DataExportManager.ExportSettings settings = exportManager.createDefaultSettings(format, filePath);
            
            updateStatus("Exporting data...");
            fireEvent(new EventData(EventType.EXPORT_STARTED, format, "Export started"));
            
            CompletableFuture<DataExportManager.ExportResult> future = exportManager.exportDataAsync(data, settings);
            
            future.whenComplete((result, throwable) -> {
                SwingUtilities.invokeLater(() -> {
                    if (throwable != null || !result.isSuccess()) {
                        String error = throwable != null ? throwable.getMessage() : result.getError();
                        updateStatus("Export failed: " + error);
                    } else {
                        updateStatus("Data exported to: " + result.getFilePath());
                        fireEvent(new EventData(EventType.EXPORT_COMPLETED, result, "Export completed"));
                    }
                });
            });
        }
    }
    
    /**
     * Handles loading example configurations.
     * 
     * @param exampleKey The example key to load
     */
    private void handleLoadExample(String exampleKey) {
        ExampleLoader.Example example = exampleLoader.getExample(exampleKey);
        if (example == null) {
            updateStatus("Example not found: " + exampleKey);
            return;
        }
        
        // Set template format
        if (templateFormatField != null) {
            templateFormatField.setText(example.getMetadata().getTemplateFormat());
        }
        
        // Set configuration
        configurationManager.setCurrentConfiguration(example.getConfiguration());
        
        updateStatus("Loaded example: " + example.getMetadata().getName());
        fireEvent(new EventData(EventType.EXAMPLE_LOADED, example, "Example loaded"));
        
        // Apply template automatically
        handleApplyTemplate();
    }
    
    /**
     * Updates the preview display.
     * 
     * @param template The template to preview
     * @param config The configuration to use
     */
    private void updatePreview(Template template, GeneratorConfiguration config) {
        if (previewLabel == null) return;
        
        try {
            String preview = generatorController.generatePreview(template, config);
            previewLabel.setText(preview);
            fireEvent(new EventData(EventType.PREVIEW_UPDATED, preview, "Preview updated"));
        } catch (Exception e) {
            previewLabel.setText("Preview unavailable");
        }
    }
    
    /**
     * Updates the status display.
     * 
     * @param message Status message
     */
    private void updateStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
        fireEvent(new EventData(EventType.STATUS_CHANGED, message, message));
    }
    
    /**
     * Sets the progress bar value.
     * 
     * @param progress Progress percentage (0-100)
     */
    private void setProgress(int progress) {
        if (progressBar != null) {
            progressBar.setValue(progress);
            progressBar.setStringPainted(progress > 0);
            progressBar.setString(progress > 0 ? progress + "%" : "");
        }
    }
    
    /**
     * Displays results in the output area.
     * 
     * @param results The results to display
     */
    private void displayResults(List<String> results) {
        if (outputArea == null) return;
        
        StringBuilder sb = new StringBuilder();
        for (String result : results) {
            sb.append(result).append("\n");
        }
        outputArea.setText(sb.toString());
    }
    
    /**
     * Gets the current configuration.
     * 
     * @return Current configuration
     */
    public GeneratorConfiguration getCurrentConfiguration() {
        return configurationManager.getCurrentConfiguration();
    }
    
    /**
     * Shuts down the event handler and releases resources.
     */
    public void shutdown() {
        eventListeners.clear();
        generatorController.shutdown();
        exportManager.shutdown();
    }
}