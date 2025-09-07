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
     * Create UI components (using static factory methods).
     */
    private void createComponents() {
        // Template section
        templateFormatField = UIComponentFactory.createTextField(30);
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
     * Layout components with side-by-side design.
     * Template at top, generators and output side by side with resizable divider, controls at bottom centered.
     */
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        // Top panel - Compact template configuration
        JPanel topPanel = createCompactTemplatePanel();
        add(topPanel, BorderLayout.NORTH);
        
        // Center - Split pane with generators and output side by side
        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        
        // Create and configure left panel (Generator Configuration)
        JPanel leftPanel = createGeneratorConfigurationPanel();
        leftPanel.setMinimumSize(new Dimension(400, 0));
        
        // Create and configure right panel (Output Display) - Default visible and opened
        JPanel rightPanel = createOutputDisplayPanel();
        rightPanel.setMinimumSize(new Dimension(400, 0));
        
        mainSplit.setLeftComponent(leftPanel);
        mainSplit.setRightComponent(rightPanel);
        
        // Configure split pane for optimal user experience
        mainSplit.setDividerLocation(600); // Initial position - adjustable
        mainSplit.setResizeWeight(0.5); // Equal resize distribution
        mainSplit.setContinuousLayout(true); // Smooth resizing
        mainSplit.setOneTouchExpandable(true); // Allow quick expand/collapse
        mainSplit.setDividerSize(8); // Make divider more prominent
        
        add(mainSplit, BorderLayout.CENTER);
        
        // Bottom - Centered generation controls
        JPanel bottomPanel = createGenerationControlsPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Create compact template configuration panel for the top.
     */
    private JPanel createCompactTemplatePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Template Configuration"));
        
        panel.add(new JLabel("Template:"));
        panel.add(templateFormatField);
        panel.add(UIComponentFactory.createPrimaryButton("Apply Template"));
        
        panel.add(Box.createHorizontalStrut(20));
        
        panel.add(new JLabel("Evaluators:"));
        panel.add(evaluatorCountComboBox);
        
        return panel;
    }
    
    /**
     * Create the left-side generator configuration panel.
     */
    private JPanel createGeneratorConfigurationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Generator Configuration"));
        panel.setPreferredSize(new Dimension(600, 400));
        
        // Generator panels container with scroll
        JScrollPane generatorScroll = new JScrollPane(generatorPanelContainer);
        generatorScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        generatorScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(generatorScroll, BorderLayout.CENTER);
        
        // Preview panel at the bottom of configuration
        previewPanel.setPreferredSize(new Dimension(0, 120));
        panel.add(previewPanel, BorderLayout.SOUTH);
        
        // Example buttons at the top
        JPanel examplePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        examplePanel.setBorder(BorderFactory.createTitledBorder("Quick Examples"));
        panel.add(examplePanel, BorderLayout.NORTH);
        
        return panel;
    }
    
    /**
     * Create the right-side output display panel.
     * This panel is prominently displayed and always visible by default.
     */
    private JPanel createOutputDisplayPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Generated Output"));
        panel.setPreferredSize(new Dimension(600, 400));
        
        // Output area - prominently displayed and always visible
        outputArea.setBackground(Color.WHITE);
        outputArea.setMargin(new Insets(10, 10, 10, 10));
        outputArea.setText("Generated data will appear here...\n\nTo get started:\n1. Enter a template format (e.g., {0}, {0}-{1})\n2. Configure generators\n3. Click 'Generate Data'");
        outputArea.setForeground(Color.GRAY);
        
        JScrollPane outputScroll = new JScrollPane(outputArea);
        outputScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        outputScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        outputScroll.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(5, 5, 5, 5),
            BorderFactory.createLoweredBevelBorder()
        ));
        outputScroll.setPreferredSize(new Dimension(0, 300)); // Ensure minimum height
        panel.add(outputScroll, BorderLayout.CENTER);
        
        // Output controls at the top
        JPanel outputControlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        outputControlPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
        outputControlPanel.add(new JLabel("Actions:"));
        outputControlPanel.add(UIComponentFactory.createSecondaryButton("Clear Output"));
        outputControlPanel.add(Box.createHorizontalStrut(10));
        outputControlPanel.add(new JLabel("Export:"));
        outputControlPanel.add(exportFormatComboBox);
        outputControlPanel.add(UIComponentFactory.createSecondaryButton("Export"));
        panel.add(outputControlPanel, BorderLayout.NORTH);
        
        // Status information at the bottom - more prominent
        JPanel statusInfoPanel = new JPanel(new BorderLayout());
        statusInfoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(5, 5, 5, 5),
            BorderFactory.createRaisedBevelBorder()
        ));
        statusInfoPanel.add(statusLabel, BorderLayout.WEST);
        
        // Progress bar with better styling
        progressBar.setStringPainted(true);
        progressBar.setString("Ready");
        statusInfoPanel.add(progressBar, BorderLayout.CENTER);
        
        panel.add(statusInfoPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Create the bottom generation controls panel - centered and prominent.
     */
    private JPanel createGenerationControlsPanel() {
        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Generation Controls"),
            BorderFactory.createEmptyBorder(5, 10, 10, 10)
        ));
        
        // Main controls - centered
        JPanel mainControlsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        
        // Batch size control
        JPanel batchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        batchPanel.add(new JLabel("Batch Size:"));
        batchSizeSpinner.setPreferredSize(new Dimension(80, 25));
        batchPanel.add(batchSizeSpinner);
        mainControlsPanel.add(batchPanel);
        
        // Prominent generate button - larger and centered
        JButton generateButton = UIComponentFactory.createPrimaryButton("🚀 Generate Data");
        generateButton.setPreferredSize(new Dimension(180, 40));
        generateButton.setFont(generateButton.getFont().deriveFont(Font.BOLD, 14f));
        mainControlsPanel.add(generateButton);
        
        outerPanel.add(mainControlsPanel, BorderLayout.CENTER);
        
        // Secondary controls - right side
        JPanel secondaryControlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        secondaryControlsPanel.add(UIComponentFactory.createSecondaryButton("Save Config"));
        secondaryControlsPanel.add(UIComponentFactory.createSecondaryButton("Load Config"));
        secondaryControlsPanel.add(UIComponentFactory.createSecondaryButton("Reset All"));
        secondaryControlsPanel.add(UIComponentFactory.createSecondaryButton("Help"));
        
        outerPanel.add(secondaryControlsPanel, BorderLayout.EAST);
        
        return outerPanel;
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
        findButton("🚀 Generate Data").addActionListener(eventHandler.createGenerateListener(() -> (Integer) batchSizeSpinner.getValue()));
        findButton("Clear Output").addActionListener(e -> resetOutputArea());
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
        JPanel examplePanel = findPanel("Quick Examples");
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
            eventData -> {
                progressBar.setIndeterminate(true);
                progressBar.setString("Generating...");
            });
        eventHandler.addEventListener(UIEventHandler.EventType.GENERATION_COMPLETED, 
            eventData -> {
                progressBar.setIndeterminate(false);
                progressBar.setValue(100);
                progressBar.setString("Complete");
                // Clear placeholder text when real data is generated
                if (outputArea.getForeground().equals(Color.GRAY)) {
                    outputArea.setForeground(Color.BLACK);
                }
            });
        eventHandler.addEventListener(UIEventHandler.EventType.GENERATION_FAILED, 
            eventData -> {
                progressBar.setIndeterminate(false);
                progressBar.setValue(0);
                progressBar.setString("Failed");
            });
            
        // Clear placeholder behavior for output area
        outputArea.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (outputArea.getForeground().equals(Color.GRAY)) {
                    outputArea.setText("");
                    outputArea.setForeground(Color.BLACK);
                }
            }
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
        resetOutputArea();
        progressBar.setValue(0);
        progressBar.setString("Ready");
        previewLabel.setText("Preview: No template applied");
        panelManager.clearAllPanels();
        statusLabel.setText("All components reset. Ready to start fresh.");
    }
    
    /**
     * Reset output area to initial placeholder state.
     */
    private void resetOutputArea() {
        outputArea.setText("Generated data will appear here...\n\nTo get started:\n1. Enter a template format (e.g., {0}, {0}-{1})\n2. Configure generators\n3. Click 'Generate Data'");
        outputArea.setForeground(Color.GRAY);
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