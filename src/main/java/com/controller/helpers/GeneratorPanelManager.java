package com.controller.helpers;

import com.model.GeneratorConfiguration;
import com.controller.business.ValidationController;
import com.view.GeneratorPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.function.Consumer;

/**
 * Manages the creation, layout, and interaction of generator panels.
 * Handles dynamic panel generation based on template requirements.
 * 
 * Responsibilities:
 * - Create and manage generator panels dynamically
 * - Handle panel layout and sizing
 * - Coordinate panel interactions
 * - Manage evaluator panels
 */
public class GeneratorPanelManager {
    
    /**
     * Panel configuration for a generator.
     */
    public static class PanelConfiguration {
        private final String generatorId;
        private final String generatorType;
        private final Map<String, String> defaultProperties;
        private final boolean isEvaluator;
        
        public PanelConfiguration(String generatorId, String generatorType, 
                                 Map<String, String> defaultProperties, boolean isEvaluator) {
            this.generatorId = generatorId;
            this.generatorType = generatorType;
            this.defaultProperties = new HashMap<>(defaultProperties != null ? defaultProperties : new HashMap<>());
            this.isEvaluator = isEvaluator;
        }
        
        // Getters
        public String getGeneratorId() { return generatorId; }
        public String getGeneratorType() { return generatorType; }
        public Map<String, String> getDefaultProperties() { return new HashMap<>(defaultProperties); }
        public boolean isEvaluator() { return isEvaluator; }
    }
    
    /**
     * Panel change listener interface.
     */
    public interface PanelChangeListener {
        void onPanelAdded(GeneratorPanel panel);
        void onPanelRemoved(GeneratorPanel panel);
        void onPanelConfigurationChanged(GeneratorPanel panel);
    }
    
    private final JPanel containerPanel;
    private final ValidationController validationController;
    private final DocumentationManager documentationManager;
    
    private final List<GeneratorPanel> generatorPanels;
    private final List<GeneratorPanel> evaluatorPanels;
    private final Map<String, GeneratorPanel> panelMap;
    
    private PanelChangeListener changeListener;
    private int nextEvaluatorIndex;
    
    // Layout settings
    private LayoutType layoutType;
    private int panelsPerRow;
    private Dimension preferredPanelSize;
    
    /**
     * Layout types for panel arrangement.
     */
    public enum LayoutType {
        VERTICAL,    // Stack panels vertically
        HORIZONTAL,  // Arrange panels horizontally
        GRID,        // Grid layout with specified columns
        FLOW         // Flow layout (wrapping)
    }
    
    public GeneratorPanelManager(JPanel containerPanel, 
                                ValidationController validationController,
                                DocumentationManager documentationManager) {
        this.containerPanel = containerPanel;
        this.validationController = validationController;
        this.documentationManager = documentationManager;
        
        this.generatorPanels = new ArrayList<>();
        this.evaluatorPanels = new ArrayList<>();
        this.panelMap = new HashMap<>();
        
        this.nextEvaluatorIndex = 0;
        this.layoutType = LayoutType.VERTICAL;
        this.panelsPerRow = 2;
        this.preferredPanelSize = new Dimension(400, 200);
        
        setupContainerPanel();
    }
    
    /**
     * Sets up the container panel layout.
     */
    private void setupContainerPanel() {
        if (containerPanel != null) {
            containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
            containerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        }
    }
    
    /**
     * Sets the panel change listener.
     * 
     * @param listener The listener to notify of panel changes
     */
    public void setPanelChangeListener(PanelChangeListener listener) {
        this.changeListener = listener;
    }
    
    /**
     * Sets the layout type for panels.
     * 
     * @param layoutType The layout type to use
     */
    public void setLayoutType(LayoutType layoutType) {
        this.layoutType = layoutType;
        refreshLayout();
    }
    
    /**
     * Sets the number of panels per row for grid layout.
     * 
     * @param panelsPerRow Number of panels per row
     */
    public void setPanelsPerRow(int panelsPerRow) {
        this.panelsPerRow = Math.max(1, panelsPerRow);
        if (layoutType == LayoutType.GRID) {
            refreshLayout();
        }
    }
    
    /**
     * Sets the preferred size for panels.
     * 
     * @param preferredSize Preferred panel size
     */
    public void setPreferredPanelSize(Dimension preferredSize) {
        this.preferredPanelSize = new Dimension(preferredSize);
        
        // Apply to existing panels
        for (GeneratorPanel panel : generatorPanels) {
            panel.setPreferredSize(preferredSize);
        }
        for (GeneratorPanel panel : evaluatorPanels) {
            panel.setPreferredSize(preferredSize);
        }
        
        refreshLayout();
    }
    
    /**
     * Creates generator panels based on template requirements.
     * 
     * @param generatorCount Number of generators needed
     * @param evaluatorCount Number of evaluators to add
     */
    public void setupGeneratorsBasedOnTemplate(int generatorCount, int evaluatorCount) {
        clearAllPanels();
        
        // Create main generator panels
        for (int i = 0; i < generatorCount; i++) {
            PanelConfiguration config = new PanelConfiguration(
                String.valueOf(i),
                "SEQUENTIALNUMBERGENERATOR",
                createDefaultProperties(i),
                false
            );
            addGeneratorPanel(config);
        }
        
        // Add evaluator panels
        for (int i = 0; i < evaluatorCount; i++) {
            addEvaluatorPanel();
        }
        
        refreshLayout();
    }
    
    /**
     * Adds a generator panel with the specified configuration.
     * 
     * @param config Panel configuration
     * @return The created GeneratorPanel
     */
    public GeneratorPanel addGeneratorPanel(PanelConfiguration config) {
        GeneratorPanel panel = createGeneratorPanel(config);
        
        if (config.isEvaluator()) {
            evaluatorPanels.add(panel);
        } else {
            generatorPanels.add(panel);
        }
        
        panelMap.put(config.getGeneratorId(), panel);
        
        // Add to container
        if (containerPanel != null) {
            containerPanel.add(panel);
        }
        
        // Notify listener
        if (changeListener != null) {
            changeListener.onPanelAdded(panel);
        }
        
        refreshLayout();
        return panel;
    }
    
    /**
     * Adds an evaluator panel.
     * 
     * @return The created evaluator panel
     */
    public GeneratorPanel addEvaluatorPanel() {
        String evaluatorId = "evaluator_" + nextEvaluatorIndex++;
        
        Map<String, String> defaultProps = new HashMap<>();
        defaultProps.put("formula", "A%2==0");
        defaultProps.put("input", "0");
        
        PanelConfiguration config = new PanelConfiguration(
            evaluatorId,
            "EVALUATION",
            defaultProps,
            true
        );
        
        return addGeneratorPanel(config);
    }
    
    /**
     * Removes a generator panel.
     * 
     * @param panel The panel to remove
     */
    public void removeGeneratorPanel(GeneratorPanel panel) {
        if (panel == null) return;
        
        // Remove from lists
        generatorPanels.remove(panel);
        evaluatorPanels.remove(panel);
        
        // Remove from map
        String panelId = null;
        for (Map.Entry<String, GeneratorPanel> entry : panelMap.entrySet()) {
            if (entry.getValue() == panel) {
                panelId = entry.getKey();
                break;
            }
        }
        if (panelId != null) {
            panelMap.remove(panelId);
        }
        
        // Remove from container
        if (containerPanel != null) {
            containerPanel.remove(panel);
        }
        
        // Notify listener
        if (changeListener != null) {
            changeListener.onPanelRemoved(panel);
        }
        
        refreshLayout();
    }
    
    /**
     * Clears all panels.
     */
    public void clearAllPanels() {
        generatorPanels.clear();
        evaluatorPanels.clear();
        panelMap.clear();
        nextEvaluatorIndex = 0;
        
        if (containerPanel != null) {
            containerPanel.removeAll();
        }
        
        refreshLayout();
    }
    
    /**
     * Creates a generator panel with the specified configuration.
     * 
     * @param config Panel configuration
     * @return Created GeneratorPanel
     */
    private GeneratorPanel createGeneratorPanel(PanelConfiguration config) {
        // Create a simplified GeneratorPanel for now
        GeneratorPanel panel = new GeneratorPanel();
        
        panel.setPreferredSize(preferredPanelSize);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, preferredPanelSize.height));
        
        // Set border with title
        String title = config.isEvaluator() ? 
            "Evaluator: " + config.getGeneratorId() : 
            "Generator " + config.getGeneratorId() + " (" + config.getGeneratorType() + ")";
        panel.setBorder(BorderFactory.createTitledBorder(title));
        
        // Add change listener to panel
        panel.addPropertyChangeListener(evt -> {
            if (changeListener != null) {
                changeListener.onPanelConfigurationChanged(panel);
            }
        });
        
        // Add tooltips if documentation manager is available
        if (documentationManager != null) {
            addTooltipsToPanel(panel, config);
        }
        
        return panel;
    }
    
    /**
     * Adds tooltips to panel components.
     * 
     * @param panel The panel to add tooltips to
     * @param config Panel configuration
     */
    private void addTooltipsToPanel(GeneratorPanel panel, PanelConfiguration config) {
        // This would add context-specific tooltips based on the generator type
        // For now, we'll add a general tooltip
        panel.setToolTipText(DocumentationManager.createTooltip(
            config.getGeneratorType(),
            "Configure properties for this " + (config.isEvaluator() ? "evaluator" : "generator")
        ));
    }
    
    /**
     * Creates default properties for a generator.
     * 
     * @param index Generator index
     * @return Default properties map
     */
    private Map<String, String> createDefaultProperties(int index) {
        Map<String, String> properties = new HashMap<>();
        properties.put("length", "3");
        properties.put("start", String.valueOf(100 + index * 100));
        properties.put("step", "1");
        properties.put("padding-length", "0");
        return properties;
    }
    
    /**
     * Refreshes the panel layout.
     */
    private void refreshLayout() {
        if (containerPanel == null) return;
        
        // Apply layout based on type
        switch (layoutType) {
            case VERTICAL:
                containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
                break;
            case HORIZONTAL:
                containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.X_AXIS));
                break;
            case GRID:
                int totalPanels = generatorPanels.size() + evaluatorPanels.size();
                int rows = (int) Math.ceil((double) totalPanels / panelsPerRow);
                containerPanel.setLayout(new GridLayout(rows, panelsPerRow, 10, 10));
                break;
            case FLOW:
                containerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
                break;
        }
        
        containerPanel.revalidate();
        containerPanel.repaint();
    }
    
    /**
     * Gets all generator panels.
     * 
     * @return List of generator panels
     */
    public List<GeneratorPanel> getGeneratorPanels() {
        return new ArrayList<>(generatorPanels);
    }
    
    /**
     * Gets all evaluator panels.
     * 
     * @return List of evaluator panels
     */
    public List<GeneratorPanel> getEvaluatorPanels() {
        return new ArrayList<>(evaluatorPanels);
    }
    
    /**
     * Gets all panels (generators and evaluators).
     * 
     * @return List of all panels
     */
    public List<GeneratorPanel> getAllPanels() {
        List<GeneratorPanel> allPanels = new ArrayList<>();
        allPanels.addAll(generatorPanels);
        allPanels.addAll(evaluatorPanels);
        return allPanels;
    }
    
    /**
     * Gets a panel by its ID.
     * 
     * @param panelId The panel ID
     * @return The panel, or null if not found
     */
    public GeneratorPanel getPanel(String panelId) {
        return panelMap.get(panelId);
    }
    
    /**
     * Builds a GeneratorConfiguration from all panels.
     * 
     * @return Complete generator configuration
     */
    public GeneratorConfiguration buildConfiguration() {
        GeneratorConfiguration config = new GeneratorConfiguration();
        Map<String, GeneratorConfiguration.GeneratorConfig> generatorConfigs = new HashMap<>();
        
        // Add generator panels
        for (GeneratorPanel panel : generatorPanels) {
            GeneratorConfiguration.GeneratorConfig genConfig = extractConfigurationFromPanel(panel);
            if (genConfig != null) {
                // Find the panel ID
                String panelId = findPanelId(panel);
                if (panelId != null) {
                    generatorConfigs.put(panelId, genConfig);
                }
            }
        }
        
        // Add evaluator panels
        for (GeneratorPanel panel : evaluatorPanels) {
            GeneratorConfiguration.GeneratorConfig evalConfig = extractConfigurationFromPanel(panel);
            if (evalConfig != null) {
                // Find the panel ID
                String panelId = findPanelId(panel);
                if (panelId != null) {
                    generatorConfigs.put(panelId, evalConfig);
                }
            }
        }
        
        config.setGeneratorConfigs(generatorConfigs);
        return config;
    }
    
    /**
     * Extracts configuration from a panel.
     * 
     * @param panel The panel to extract from
     * @return Generator configuration
     */
    private GeneratorConfiguration.GeneratorConfig extractConfigurationFromPanel(GeneratorPanel panel) {
        // This would extract the actual configuration from the panel
        // For now, return a basic configuration
        GeneratorConfiguration.GeneratorConfig config = new GeneratorConfiguration.GeneratorConfig();
        config.setType("SEQUENTIALNUMBERGENERATOR");
        
        Map<String, String> properties = new HashMap<>();
        properties.put("length", "3");
        properties.put("start", "100");
        properties.put("step", "1");
        config.setProperties(properties);
        
        return config;
    }
    
    /**
     * Finds the ID of a panel.
     * 
     * @param panel The panel to find
     * @return Panel ID, or null if not found
     */
    private String findPanelId(GeneratorPanel panel) {
        for (Map.Entry<String, GeneratorPanel> entry : panelMap.entrySet()) {
            if (entry.getValue() == panel) {
                return entry.getKey();
            }
        }
        return null;
    }
    
    /**
     * Loads configuration into panels.
     * 
     * @param configuration The configuration to load
     */
    public void loadConfiguration(GeneratorConfiguration configuration) {
        if (configuration == null || configuration.getGeneratorConfigs() == null) {
            return;
        }
        
        clearAllPanels();
        
        Map<String, GeneratorConfiguration.GeneratorConfig> configs = configuration.getGeneratorConfigs();
        
        // Create panels for each configuration
        for (Map.Entry<String, GeneratorConfiguration.GeneratorConfig> entry : configs.entrySet()) {
            String generatorId = entry.getKey();
            GeneratorConfiguration.GeneratorConfig genConfig = entry.getValue();
            
            boolean isEvaluator = generatorId.startsWith("evaluator_") || 
                                 "EVALUATION".equals(genConfig.getType());
            
            PanelConfiguration panelConfig = new PanelConfiguration(
                generatorId,
                genConfig.getType(),
                genConfig.getProperties(),
                isEvaluator
            );
            
            addGeneratorPanel(panelConfig);
        }
        
        refreshLayout();
    }
    
    /**
     * Validates all panel configurations.
     * 
     * @return Validation result
     */
    public ValidationController.ValidationResult validateAllPanels() {
        GeneratorConfiguration config = buildConfiguration();
        return validationController.validateConfiguration(config);
    }
    
    /**
     * Gets the total number of panels.
     * 
     * @return Total panel count
     */
    public int getTotalPanelCount() {
        return generatorPanels.size() + evaluatorPanels.size();
    }
    
    /**
     * Gets the number of generator panels.
     * 
     * @return Generator panel count
     */
    public int getGeneratorPanelCount() {
        return generatorPanels.size();
    }
    
    /**
     * Gets the number of evaluator panels.
     * 
     * @return Evaluator panel count
     */
    public int getEvaluatorPanelCount() {
        return evaluatorPanels.size();
    }
    
    /**
     * Creates an ActionListener for adding evaluator panels.
     * 
     * @return ActionListener for adding evaluators
     */
    public ActionListener createAddEvaluatorListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEvaluatorPanel();
            }
        };
    }
    
    /**
     * Creates an ActionListener for removing a specific panel.
     * 
     * @param panel The panel to remove
     * @return ActionListener for removing the panel
     */
    public ActionListener createRemovePanelListener(GeneratorPanel panel) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeGeneratorPanel(panel);
            }
        };
    }
    
    /**
     * Resets all panels to default configurations.
     */
    public void resetAllPanels() {
        int generatorCount = generatorPanels.size();
        int evaluatorCount = evaluatorPanels.size();
        
        setupGeneratorsBasedOnTemplate(generatorCount, evaluatorCount);
    }
    
    /**
     * Gets the current layout type.
     * 
     * @return Current layout type
     */
    public LayoutType getLayoutType() {
        return layoutType;
    }
    
    /**
     * Gets the panels per row setting.
     * 
     * @return Panels per row
     */
    public int getPanelsPerRow() {
        return panelsPerRow;
    }
    
    /**
     * Gets the preferred panel size.
     * 
     * @return Preferred panel size
     */
    public Dimension getPreferredPanelSize() {
        return new Dimension(preferredPanelSize);
    }
}