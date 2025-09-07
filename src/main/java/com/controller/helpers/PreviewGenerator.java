package com.controller.helpers;

import com.controller.business.GeneratorController;
import com.controller.business.ValidationController;
import com.model.GeneratorConfiguration;
import com.model.Template;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Handles real-time preview generation and display.
 * Provides live preview updates as users modify configurations.
 * 
 * Responsibilities:
 * - Generate real-time previews of data output
 * - Update preview displays automatically
 * - Manage preview refresh scheduling
 * - Handle preview error states
 */
public class PreviewGenerator {
    
    /**
     * Preview update modes.
     */
    public enum UpdateMode {
        IMMEDIATE,    // Update immediately on change
        DEBOUNCED,    // Update after a delay (good for typing)
        MANUAL        // Update only when requested
    }
    
    /**
     * Preview result container.
     */
    public static class PreviewResult {
        private final boolean success;
        private final String preview;
        private final String error;
        private final long generationTimeMs;
        
        private PreviewResult(boolean success, String preview, String error, long generationTimeMs) {
            this.success = success;
            this.preview = preview;
            this.error = error;
            this.generationTimeMs = generationTimeMs;
        }
        
        public static PreviewResult success(String preview, long generationTimeMs) {
            return new PreviewResult(true, preview, null, generationTimeMs);
        }
        
        public static PreviewResult error(String error) {
            return new PreviewResult(false, null, error, 0);
        }
        
        public boolean isSuccess() { return success; }
        public String getPreview() { return preview; }
        public String getError() { return error; }
        public long getGenerationTimeMs() { return generationTimeMs; }
    }
    
    /**
     * Preview update listener interface.
     */
    public interface PreviewUpdateListener {
        void onPreviewUpdated(PreviewResult result);
    }
    
    private final GeneratorController generatorController;
    private final ValidationController validationController;
    private final ScheduledExecutorService scheduler;
    
    private UpdateMode updateMode;
    private int debounceDelayMs;
    private PreviewUpdateListener updateListener;
    
    // UI Components
    private JLabel previewLabel;
    private JPanel previewPanel;
    private JTextArea previewArea;
    
    // State
    private volatile boolean previewEnabled;
    private volatile boolean updateScheduled;
    private Template currentTemplate;
    private GeneratorConfiguration currentConfiguration;
    
    public PreviewGenerator(GeneratorController generatorController, 
                           ValidationController validationController) {
        this.generatorController = generatorController;
        this.validationController = validationController;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        
        this.updateMode = UpdateMode.DEBOUNCED;
        this.debounceDelayMs = 500; // 500ms delay
        this.previewEnabled = true;
        this.updateScheduled = false;
    }
    
    /**
     * Sets the preview display components.
     * 
     * @param previewLabel Simple text label for preview
     * @param previewPanel Panel container for preview
     * @param previewArea Text area for detailed preview
     */
    public void setPreviewComponents(JLabel previewLabel, JPanel previewPanel, JTextArea previewArea) {
        this.previewLabel = previewLabel;
        this.previewPanel = previewPanel;
        this.previewArea = previewArea;
        
        setupPreviewPanel();
    }
    
    /**
     * Sets up the preview panel with enhanced display.
     */
    private void setupPreviewPanel() {
        if (previewPanel == null) return;
        
        previewPanel.setLayout(new BorderLayout());
        previewPanel.setBorder(BorderFactory.createTitledBorder("Preview"));
        
        if (previewArea == null) {
            previewArea = new JTextArea(3, 40);
            previewArea.setEditable(false);
            previewArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            previewArea.setBackground(previewPanel.getBackground());
        }
        
        JScrollPane scrollPane = new JScrollPane(previewArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        previewPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add status label at bottom
        if (previewLabel == null) {
            previewLabel = new JLabel("Ready for preview");
            previewLabel.setFont(previewLabel.getFont().deriveFont(Font.ITALIC));
        }
        previewPanel.add(previewLabel, BorderLayout.SOUTH);
    }
    
    /**
     * Sets the preview update listener.
     * 
     * @param listener The listener to notify on updates
     */
    public void setUpdateListener(PreviewUpdateListener listener) {
        this.updateListener = listener;
    }
    
    /**
     * Sets the update mode for previews.
     * 
     * @param mode The update mode
     */
    public void setUpdateMode(UpdateMode mode) {
        this.updateMode = mode;
    }
    
    /**
     * Sets the debounce delay for updates.
     * 
     * @param delayMs Delay in milliseconds
     */
    public void setDebounceDelay(int delayMs) {
        this.debounceDelayMs = Math.max(100, delayMs); // Minimum 100ms
    }
    
    /**
     * Enables or disables preview generation.
     * 
     * @param enabled true to enable, false to disable
     */
    public void setPreviewEnabled(boolean enabled) {
        this.previewEnabled = enabled;
        
        if (!enabled) {
            clearPreview();
        } else if (currentTemplate != null && currentConfiguration != null) {
            requestPreviewUpdate();
        }
    }
    
    /**
     * Updates the current template and triggers preview update.
     * 
     * @param template The new template
     */
    public void updateTemplate(Template template) {
        this.currentTemplate = template;
        requestPreviewUpdate();
    }
    
    /**
     * Updates the current configuration and triggers preview update.
     * 
     * @param configuration The new configuration
     */
    public void updateConfiguration(GeneratorConfiguration configuration) {
        this.currentConfiguration = configuration;
        requestPreviewUpdate();
    }
    
    /**
     * Updates both template and configuration.
     * 
     * @param template The new template
     * @param configuration The new configuration
     */
    public void updatePreviewData(Template template, GeneratorConfiguration configuration) {
        this.currentTemplate = template;
        this.currentConfiguration = configuration;
        requestPreviewUpdate();
    }
    
    /**
     * Requests a preview update based on the current update mode.
     */
    public void requestPreviewUpdate() {
        if (!previewEnabled || currentTemplate == null || currentConfiguration == null) {
            return;
        }
        
        switch (updateMode) {
            case IMMEDIATE:
                generatePreviewNow();
                break;
            case DEBOUNCED:
                schedulePreviewUpdate();
                break;
            case MANUAL:
                // Do nothing - wait for manual trigger
                break;
        }
    }
    
    /**
     * Manually triggers a preview update regardless of mode.
     */
    public void forcePreviewUpdate() {
        if (currentTemplate != null && currentConfiguration != null) {
            generatePreviewNow();
        }
    }
    
    /**
     * Schedules a debounced preview update.
     */
    private void schedulePreviewUpdate() {
        if (updateScheduled) {
            return; // Already scheduled
        }
        
        updateScheduled = true;
        
        scheduler.schedule(() -> {
            updateScheduled = false;
            if (previewEnabled) {
                generatePreviewNow();
            }
        }, debounceDelayMs, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Generates preview immediately.
     */
    private void generatePreviewNow() {
        if (!previewEnabled || currentTemplate == null || currentConfiguration == null) {
            return;
        }
        
        // Validate first
        ValidationController.ValidationResult templateValidation = 
            validationController.validateTemplateFormat(currentTemplate.getFormat());
        
        if (!templateValidation.isValid()) {
            PreviewResult result = PreviewResult.error("Template validation failed: " + 
                String.join(", ", templateValidation.getErrors()));
            updatePreviewDisplay(result);
            return;
        }
        
        ValidationController.ValidationResult configValidation = 
            validationController.validateConfiguration(currentConfiguration);
        
        if (!configValidation.isValid()) {
            PreviewResult result = PreviewResult.error("Configuration validation failed: " + 
                String.join(", ", configValidation.getErrors()));
            updatePreviewDisplay(result);
            return;
        }
        
        // Generate preview asynchronously
        CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();
            try {
                // Generate multiple samples for better preview
                List<String> samples = generatorController.generateNumbers(currentTemplate, currentConfiguration, 3);
                long endTime = System.currentTimeMillis();
                
                if (samples.isEmpty()) {
                    return PreviewResult.error("No data generated");
                }
                
                StringBuilder preview = new StringBuilder();
                preview.append("Sample output:\n");
                for (int i = 0; i < Math.min(samples.size(), 3); i++) {
                    preview.append("  ").append(i + 1).append(". ").append(samples.get(i)).append("\n");
                }
                
                if (samples.size() > 3) {
                    preview.append("  ... (and more)");
                }
                
                return PreviewResult.success(preview.toString(), endTime - startTime);
                
            } catch (Exception e) {
                return PreviewResult.error("Generation error: " + e.getMessage());
            }
        }).whenComplete((result, throwable) -> {
            SwingUtilities.invokeLater(() -> {
                if (throwable != null) {
                    updatePreviewDisplay(PreviewResult.error("Preview generation failed: " + throwable.getMessage()));
                } else {
                    updatePreviewDisplay(result);
                }
            });
        });
    }
    
    /**
     * Updates the preview display with the result.
     * 
     * @param result The preview result
     */
    private void updatePreviewDisplay(PreviewResult result) {
        if (result.isSuccess()) {
            // Update preview area
            if (previewArea != null) {
                previewArea.setText(result.getPreview());
                previewArea.setForeground(Color.BLACK);
            }
            
            // Update status label
            if (previewLabel != null) {
                String statusText = String.format("Preview generated in %dms", result.getGenerationTimeMs());
                previewLabel.setText(statusText);
                previewLabel.setForeground(Color.DARK_GRAY);
            }
        } else {
            // Show error
            if (previewArea != null) {
                previewArea.setText("Preview Error:\n" + result.getError());
                previewArea.setForeground(Color.RED);
            }
            
            if (previewLabel != null) {
                previewLabel.setText("Preview failed");
                previewLabel.setForeground(Color.RED);
            }
        }
        
        // Notify listener
        if (updateListener != null) {
            updateListener.onPreviewUpdated(result);
        }
    }
    
    /**
     * Clears the preview display.
     */
    public void clearPreview() {
        if (previewArea != null) {
            previewArea.setText("Preview disabled");
            previewArea.setForeground(Color.GRAY);
        }
        
        if (previewLabel != null) {
            previewLabel.setText("Preview disabled");
            previewLabel.setForeground(Color.GRAY);
        }
    }
    
    /**
     * Shows a loading state in the preview.
     */
    public void showPreviewLoading() {
        if (previewArea != null) {
            previewArea.setText("Generating preview...");
            previewArea.setForeground(Color.BLUE);
        }
        
        if (previewLabel != null) {
            previewLabel.setText("Generating...");
            previewLabel.setForeground(Color.BLUE);
        }
    }
    
    /**
     * Gets the current preview text.
     * 
     * @return Current preview text, or null if not available
     */
    public String getCurrentPreview() {
        return previewArea != null ? previewArea.getText() : null;
    }
    
    /**
     * Checks if preview is currently enabled.
     * 
     * @return true if preview is enabled
     */
    public boolean isPreviewEnabled() {
        return previewEnabled;
    }
    
    /**
     * Gets the current update mode.
     * 
     * @return Current update mode
     */
    public UpdateMode getUpdateMode() {
        return updateMode;
    }
    
    /**
     * Gets the current debounce delay.
     * 
     * @return Debounce delay in milliseconds
     */
    public int getDebounceDelay() {
        return debounceDelayMs;
    }
    
    /**
     * Shuts down the preview generator and releases resources.
     */
    public void shutdown() {
        previewEnabled = false;
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(1, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}