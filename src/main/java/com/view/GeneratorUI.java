package com.view;

import com.controller.business.*;
import com.controller.events.UIEventHandler;
import com.controller.helpers.*;
import com.view.ui.constants.UIConstants;
import com.view.ui.components.factory.UIComponentFactory;
import com.view.ui.theme.UIThemeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * REFACTORED Main UI class for the Multi-Format Data Generator.
 * 
 * This class has been dramatically simplified from 1500+ lines to ~200 lines
 * by delegating all business logic to specialized controllers and helpers.
 * 
 * Responsibilities (ONLY UI-related):
 * - Create and layout UI components
 * - Coordinate between UI helpers
 * - Handle application lifecycle
 * - Apply themes and styling
 */
public class GeneratorUI extends JFrame {
    
    // === CONTROLLERS (Business Logic - Phase 2) ===
    private final GeneratorController generatorController;
    private final ValidationController validationController;
    private final ConfigurationManager configurationManager;
    private final DataExportManager exportManager;
    private final ExampleLoader exampleLoader;
    
    // === UI HELPERS (Event Handling & Coordination - Phase 3) ===
    private final UIEventHandler eventHandler;
    private final PreviewGenerator previewGenerator;
    private final DocumentationManager documentationManager;
    private final GeneratorPanelManager panelManager;
    
    // === UI UTILITIES (Phase 1) ===
    private final UIThemeManager themeManager;
    private final UIComponentFactory componentFactory;
    
    // === UI COMPONENTS (Only UI state, no business logic) ===
    private JTextField templateFormatField;
    private JTextArea outputArea;
    private JProgressBar progressBar;
    private JLabel statusLabel;
    private JLabel previewLabel;
    private JPanel generatorPanelContainer;
    private JPanel previewPanel;
    private JSpinner batchSizeSpinner;
    private JComboBox<Integer> evaluatorCountComboBox;
    private JComboBox<DataExportManager.ExportFormat> exportFormatComboBox;
    
    public GeneratorUI() {
        // === STEP 1: Initialize All Controllers (Phase 2) ===
        this.generatorController = new GeneratorController();
        this.validationController = new ValidationController();
        this.configurationManager = new ConfigurationManager();
        this.exportManager = new DataExportManager();
        this.exampleLoader = new ExampleLoader();
        
        // === STEP 2: Initialize UI Helpers (Phase 3) ===
        this.eventHandler = new UIEventHandler(
            generatorController, validationController, configurationManager, 
            exportManager, exampleLoader);
        this.previewGenerator = new PreviewGenerator(generatorController, validationController);
        this.documentationManager = new DocumentationManager();
        
        // === STEP 3: Initialize UI Utilities (Phase 1) ===
        this.themeManager = new UIThemeManager();
        this.componentFactory = new UIComponentFactory();
        
        // === STEP 4: Build UI (Delegated to helpers) ===
        initializeFrame();
        createComponents();
        layoutComponents();
        
        // === STEP 5: Initialize Panel Manager (needs container first) ===
        this.panelManager = new GeneratorPanelManager(
            generatorPanelContainer, validationController, documentationManager);
        
        // === STEP 6: Connect Everything Together ===
        connectEventHandlers();
        setupPreviewSystem();
        setupExampleButtons();
        
        // === STEP 7: Final Setup ===
        applyTheme();
        setupEventListeners();
        
        // Show initial help
        showWelcomeMessage();
    }
    
    /**
     * Initialize frame properties (UI only, no business logic).
     */
    private void initializeFrame() {
        setTitle("Multi-Format Data Generator - Refactored Architecture");
        setSize(1200, 800);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleApplicationShutdown();
            }
        });
    }
    
    /**
     * Create UI components (delegated to component factory).
     */
    private void createComponents() {
        // Template section
        templateFormatField = componentFactory.createTextField(30);
        templateFormatField.setToolTipText(
            documentationManager.createTooltip("Template Format", 
            "Enter format like {0}, {0}-{1}, etc."));
        
        // Controls
        evaluatorCountComboBox = new JComboBox<>(new Integer[]{0, 1, 2, 3, 4, 5});
        batchSizeSpinner = new JSpinner(new SpinnerNumberModel(100, 1, 10000, 10));
        exportFormatComboBox = new JComboBox<>(exportManager.getAvailableFormats());
        
        // Output area
        outputArea = new JTextArea(15, 50);
        outputArea.setEditable(false);
        outputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        
        // Status and preview
        statusLabel = new JLabel("Ready");
        previewLabel = new JLabel("Preview: No template applied");
        progressBar = new JProgressBar(0, 100);
        
        // Containers
        generatorPanelContainer = new JPanel();
        previewPanel = new JPanel(new BorderLayout());
        previewPanel.setBorder(BorderFactory.createTitledBorder("Preview"));
    }
    
    /**
     * Layout components (UI structure only).
     */
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        // Top panel - Template and controls
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);
        
        // Center - Split pane with generators and output
        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplit.setLeftComponent(createGeneratorPanel());
        mainSplit.setRightComponent(createOutputPanel());
        mainSplit.setDividerLocation(500);
        add(mainSplit, BorderLayout.CENTER);
        
        // Bottom - Status bar
        JPanel statusPanel = createStatusPanel();
        add(statusPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Template Configuration"));
        
        panel.add(new JLabel("Template:"));
        panel.add(templateFormatField);
        panel.add(componentFactory.createPrimaryButton("Apply Template"));
        panel.add(Box.createHorizontalStrut(20));
        
        panel.add(new JLabel("Evaluators:"));
        panel.add(evaluatorCountComboBox);
        panel.add(Box.createHorizontalStrut(20));
        
        panel.add(new JLabel("Batch Size:"));
        panel.add(batchSizeSpinner);
        panel.add(componentFactory.createPrimaryButton("Generate"));
        
        return panel;
    }
    
    private JPanel createGeneratorPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Generators"));
        
        JScrollPane scrollPane = new JScrollPane(generatorPanelContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Example buttons
        JPanel examplePanel = new JPanel(new FlowLayout());
        examplePanel.setBorder(BorderFactory.createTitledBorder("Examples"));
        panel.add(examplePanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createOutputPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Output area
        JScrollPane outputScroll = new JScrollPane(outputArea);
        outputScroll.setBorder(BorderFactory.createTitledBorder("Generated Data"));
        
        // Preview area
        previewPanel.add(previewLabel, BorderLayout.CENTER);
        previewPanel.setPreferredSize(new Dimension(0, 100));
        
        // Controls
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.add(componentFactory.createSecondaryButton("Clear Output"));
        controlPanel.add(new JLabel("Export:"));
        controlPanel.add(exportFormatComboBox);
        controlPanel.add(componentFactory.createSecondaryButton("Export"));
        controlPanel.add(componentFactory.createSecondaryButton("Save Config"));
        controlPanel.add(componentFactory.createSecondaryButton("Load Config"));
        
        panel.add(previewPanel, BorderLayout.NORTH);
        panel.add(outputScroll, BorderLayout.CENTER);
        panel.add(controlPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        panel.add(statusLabel, BorderLayout.WEST);
        panel.add(progressBar, BorderLayout.CENTER);
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.add(componentFactory.createSecondaryButton("Help"));
        rightPanel.add(componentFactory.createSecondaryButton("Reset All"));
        panel.add(rightPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Connect event handlers (ALL business logic delegated to eventHandler).
     */
    private void connectEventHandlers() {
        // Set UI references in event handler
        eventHandler.setUIReferences(
            templateFormatField, outputArea, progressBar, 
            statusLabel, previewLabel, generatorPanelContainer);
        
        // Connect button actions to event handler (NO business logic here!)
        findButton("Apply Template").addActionListener(eventHandler.createApplyTemplateListener());
        findButton("Generate").addActionListener(eventHandler.createGenerateListener(() -> (Integer) batchSizeSpinner.getValue()));
        findButton("Clear Output").addActionListener(eventHandler.createClearOutputListener());
        findButton("Save Config").addActionListener(eventHandler.createSaveConfigListener());
        findButton("Load Config").addActionListener(eventHandler.createLoadConfigListener());
        findButton("Export").addActionListener(eventHandler.createExportListener(() -> 
            (DataExportManager.ExportFormat) exportFormatComboBox.getSelectedItem()));
        findButton("Help").addActionListener(documentationManager.createShowHelpListener());
        findButton("Reset All").addActionListener(e -> resetAllComponents());
        
        // Evaluator combo
        evaluatorCountComboBox.addActionListener(e -> 
            panelManager.setupGeneratorsBasedOnTemplate(
                generatorController.extractGeneratorCount(templateFormatField.getText()),
                (Integer) evaluatorCountComboBox.getSelectedItem()));
    }
    
    /**
     * Setup preview system (delegated to PreviewGenerator).
     */
    private void setupPreviewSystem() {
        previewGenerator.setPreviewComponents(previewLabel, previewPanel, null);
        previewGenerator.setUpdateMode(PreviewGenerator.UpdateMode.DEBOUNCED);
        previewGenerator.setDebounceDelay(500);
        
        // Listen for configuration changes
        eventHandler.addEventListener(UIEventHandler.EventType.TEMPLATE_APPLIED, 
            eventData -> previewGenerator.forcePreviewUpdate());
        eventHandler.addEventListener(UIEventHandler.EventType.CONFIGURATION_CHANGED, 
            eventData -> previewGenerator.forcePreviewUpdate());
    }
    
    /**
     * Setup example buttons (delegated to ExampleLoader).
     */
    private void setupExampleButtons() {
        JPanel examplePanel = findPanel("Examples");
        if (examplePanel != null) {
            for (String exampleKey : exampleLoader.getAllExampleKeys()) {
                ExampleLoader.ExampleMetadata metadata = exampleLoader.getExampleMetadata(exampleKey);
                if (metadata != null) {
                    JButton exampleButton = new JButton(metadata.getName());
                    exampleButton.setToolTipText(metadata.getDescription());
                    exampleButton.addActionListener(eventHandler.createLoadExampleListener(exampleKey));
                    examplePanel.add(exampleButton);
                }
            }
        }
    }
    
    /**
     * Apply theme (delegated to ThemeManager).
     */
    private void applyTheme() {
        themeManager.setupModernStyling();
        SwingUtilities.updateComponentTreeUI(this);
    }
    
    /**
     * Setup additional event listeners.
     */
    private void setupEventListeners() {
        // Status updates
        eventHandler.addEventListener(UIEventHandler.EventType.STATUS_CHANGED, 
            eventData -> statusLabel.setText(eventData.getMessage()));
        
        // Progress updates
        eventHandler.addEventListener(UIEventHandler.EventType.GENERATION_STARTED, 
            eventData -> progressBar.setIndeterminate(true));
        eventHandler.addEventListener(UIEventHandler.EventType.GENERATION_COMPLETED, 
            eventData -> {
                progressBar.setIndeterminate(false);
                progressBar.setValue(100);
            });
        eventHandler.addEventListener(UIEventHandler.EventType.GENERATION_FAILED, 
            eventData -> {
                progressBar.setIndeterminate(false);
                progressBar.setValue(0);
            });
    }
    
    /**
     * Show welcome message with help.
     */
    private void showWelcomeMessage() {
        statusLabel.setText("Welcome! Apply a template to get started, or click Help for guidance.");
        
        // Show quick start tip
        Timer timer = new Timer(2000, e -> {
            if (statusLabel.getText().contains("Welcome")) {
                statusLabel.setText("Try loading an example or enter a template like {0}");
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    /**
     * Reset all components to initial state.
     */
    private void resetAllComponents() {
        templateFormatField.setText("");
        outputArea.setText("");
        progressBar.setValue(0);
        previewLabel.setText("Preview: No template applied");
        panelManager.clearAllPanels();
        statusLabel.setText("All components reset. Ready to start fresh.");
    }
    
    /**
     * Handle application shutdown with proper cleanup.
     */
    private void handleApplicationShutdown() {
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Do you want to save your current configuration before exiting?",
            "Save Before Exit?",
            JOptionPane.YES_NO_CANCEL_OPTION
        );
        
        if (choice == JOptionPane.YES_OPTION) {
            // Trigger save through event handler
            eventHandler.createSaveConfigListener().actionPerformed(null);
        } else if (choice == JOptionPane.NO_OPTION) {
            performShutdown();
        }
        // CANCEL_OPTION does nothing (stays open)
    }
    
    /**
     * Perform actual shutdown with cleanup.
     */
    private void performShutdown() {
        // Cleanup all controllers and helpers
        eventHandler.shutdown();
        previewGenerator.shutdown();
        exportManager.shutdown();
        generatorController.shutdown();
        
        dispose();
        System.exit(0);
    }
    
    // === UTILITY METHODS FOR COMPONENT FINDING ===
    
    private JButton findButton(String text) {
        return findComponent(this, JButton.class, text);
    }
    
    private JPanel findPanel(String title) {
        return findComponentByTitle(this, JPanel.class, title);
    }
    
    @SuppressWarnings("unchecked")
    private <T extends JComponent> T findComponent(Container parent, Class<T> type, String text) {
        for (Component comp : parent.getComponents()) {
            if (type.isInstance(comp)) {
                if (comp instanceof JButton && ((JButton) comp).getText().equals(text)) {
                    return (T) comp;
                }
            }
            if (comp instanceof Container) {
                T found = findComponent((Container) comp, type, text);
                if (found != null) return found;
            }
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    private <T extends JComponent> T findComponentByTitle(Container parent, Class<T> type, String title) {
        for (Component comp : parent.getComponents()) {
            if (type.isInstance(comp) && comp instanceof JPanel) {
                if (((JPanel) comp).getBorder() instanceof javax.swing.border.TitledBorder) {
                    javax.swing.border.TitledBorder border = 
                        (javax.swing.border.TitledBorder) ((JPanel) comp).getBorder();
                    if (title.equals(border.getTitle())) {
                        return (T) comp;
                    }
                }
            }
            if (comp instanceof Container) {
                T found = findComponentByTitle((Container) comp, type, title);
                if (found != null) return found;
            }
        }
        return null;
    }
    
    // === MAIN METHOD ===
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new GeneratorUI().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, 
                    "Failed to start application: " + e.getMessage(),
                    "Startup Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}