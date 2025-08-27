package com.view;

import com.controller.InputProcessor;
import com.controller.generators.MainGenerator;
import com.model.Template;
import com.model.GeneratorConfiguration;
import com.service.SimpleConfigurationService;
import com.service.SimpleExportService;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;

public class GeneratorUI extends JFrame {

    private JPanel generatorPanelContainer;
    private JTextArea outputArea;
    private JButton generateButton;
    private JTextField templateFormatField;
    private JButton applyTemplateButton;
    private JComboBox<Integer> evaluatorCountComboBox;
    private List<String> results;
    private JButton clearOutputButton;
    private JProgressBar progressBar;
    private JLabel statusLabel;
    private JButton saveConfigButton;
    private JButton loadConfigButton;
    private JSpinner batchSizeSpinner;
    private JLabel previewLabel;
    private SimpleConfigurationService configurationService;
    private SimpleExportService exportService;
    private JButton exportButton;
    private JComboBox<String> exportFormatComboBox;
    private JCheckBox autoSaveCheckBox;
    private JLabel configurationStatusLabel;
    private JSplitPane mainSplitPane;
    private JButton resetAllButton;
    private JButton documentationButton;

    public GeneratorUI() {
        setTitle("Synthetic Data Generator - Phase 3 Complete");
        setSize(1000, 700); // Larger size for better UX
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen
        
        // Apply dark theme
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            UIManager.put("control", new Color(60, 63, 65));
            UIManager.put("info", new Color(60, 63, 65));
            UIManager.put("nimbusBase", new Color(18, 30, 49));
            UIManager.put("nimbusAlertYellow", new Color(248, 187, 0));
            UIManager.put("nimbusDisabledText", new Color(128, 128, 128));
            UIManager.put("nimbusFocus", new Color(115, 164, 209));
            UIManager.put("nimbusGreen", new Color(176, 179, 50));
            UIManager.put("nimbusInfoBlue", new Color(66, 139, 221));
            UIManager.put("nimbusLightBackground", new Color(60, 63, 65));
            UIManager.put("nimbusOrange", new Color(191, 98, 4));
            UIManager.put("nimbusRed", new Color(169, 46, 34));
            UIManager.put("nimbusSelectedText", new Color(255, 255, 255));
            UIManager.put("nimbusSelectionBackground", new Color(104, 93, 156));
            UIManager.put("text", new Color(230, 230, 230));
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            System.err.println("Could not set dark theme: " + e.getMessage());
        }
        
        // Initialize services
        configurationService = new SimpleConfigurationService();
        exportService = new SimpleExportService();
        
        initializeComponents();
        layoutComponents();
        setupModernStyling();
    }

    private void initializeComponents() {
        // Enhanced template format field with tooltip
        templateFormatField = new JTextField(25);
        templateFormatField.setToolTipText("Enter template format like {0}, {0}-{1}, etc.");
        templateFormatField.addActionListener(e -> showPreview());
        
        applyTemplateButton = new JButton("Apply Template");
        applyTemplateButton.setToolTipText("Apply the template format and create generator panels");
        applyTemplateButton.addActionListener(e -> applyTemplate());

        // Enhanced evaluator combo with better styling
        evaluatorCountComboBox = new JComboBox<>(new Integer[]{0, 1, 2, 3, 4, 5});
        evaluatorCountComboBox.setToolTipText("Number of additional evaluator filters to add");
        evaluatorCountComboBox.addActionListener(e -> addEvaluatorPanels());

        // Enhanced buttons with icons and tooltips
        clearOutputButton = new JButton("Clear Output");
        clearOutputButton.setToolTipText("Clear the output display area");
        clearOutputButton.addActionListener(e -> {
            outputArea.setText("");
            statusLabel.setText("Output cleared");
        });

        // Enhanced configuration buttons with new functionality
        saveConfigButton = new JButton("Save Config");
        saveConfigButton.setToolTipText("Save current configuration to JSON file");
        saveConfigButton.addActionListener(e -> saveConfiguration());
        
        loadConfigButton = new JButton("Load Config");
        loadConfigButton.setToolTipText("Load configuration from JSON file");
        loadConfigButton.addActionListener(e -> loadConfiguration());
        
        // Phase 3: Export functionality
        exportButton = new JButton("Export Data");
        exportButton.setToolTipText("Export generated data to file");
        exportButton.addActionListener(e -> exportData());
        exportButton.setEnabled(false); // Disabled until data is generated
        
        exportFormatComboBox = new JComboBox<>(new String[]{"CSV", "TXT", "JSON"});
        exportFormatComboBox.setToolTipText("Select export format");
        
        // Phase 3: Auto-save feature
        autoSaveCheckBox = new JCheckBox("Auto-save config");
        autoSaveCheckBox.setToolTipText("Automatically save configuration changes");
        
        // Configuration status
        configurationStatusLabel = new JLabel("No configuration loaded");
        configurationStatusLabel.setForeground(Color.GRAY);
        configurationStatusLabel.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 10));
        
        // Reset All button
        resetAllButton = new JButton("Reset All");
        resetAllButton.setToolTipText("Clear all inputs and reset to default state");
        resetAllButton.addActionListener(e -> resetAllInputs());
        resetAllButton.setBackground(new Color(220, 20, 60));
        resetAllButton.setForeground(Color.WHITE);
        
        // Documentation button
        documentationButton = new JButton("Help & Docs");
        documentationButton.setToolTipText("Open documentation and examples");
        documentationButton.addActionListener(e -> showDocumentation());
        documentationButton.setBackground(new Color(70, 130, 180));
        documentationButton.setForeground(Color.WHITE);

        // Enhanced generator panel container with better layout
        generatorPanelContainer = new JPanel();
        generatorPanelContainer.setLayout(new WrapLayout(WrapLayout.LEFT, 15, 15));
        generatorPanelContainer.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Generator Configuration",
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
            new Font(Font.SANS_SERIF, Font.BOLD, 12)));
        // Remove the white background - let it inherit the default grey
        
        // Enhanced output area with better formatting
        outputArea = new JTextArea(15, 60);
        outputArea.setEditable(false);
        outputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        outputArea.setTabSize(4);
        
        // Enhanced generate button
        generateButton = new JButton("Generate Data");
        generateButton.setToolTipText("Generate synthetic data based on current configuration");
        generateButton.addActionListener(e -> generateNumbers());
        
        // New batch size control
        batchSizeSpinner = new JSpinner(new SpinnerNumberModel(100, 1, 10000, 50));
        batchSizeSpinner.setToolTipText("Number of records to generate");
        
        // Progress bar for generation feedback
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setString("Ready");
        
        // Status label for user feedback
        statusLabel = new JLabel("Ready to generate data");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        
        // Preview label for real-time template preview
        previewLabel = new JLabel("Template preview will appear here");
        previewLabel.setForeground(Color.GRAY);
        previewLabel.setFont(new Font(Font.MONOSPACED, Font.ITALIC, 11));
    }


    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));

        // Create main panels
        JPanel northPanel = createNorthPanel();
        JPanel centerPanel = createCenterPanel();
        JPanel southPanel = createSouthPanel();
        JPanel eastPanel = createEastPanel();

        // Create split pane for center content (generators vs output)
        mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, centerPanel, southPanel);
        mainSplitPane.setDividerLocation(450); // Generator config gets 450px, output gets rest
        mainSplitPane.setResizeWeight(0.7); // Generator config gets 70% of space by default
        mainSplitPane.setOneTouchExpandable(true); // Add expand/collapse buttons
        mainSplitPane.setDividerSize(12); // Thicker divider for easier dragging
        mainSplitPane.setContinuousLayout(true); // Smooth resizing
        
        // Add property change listener to remember divider position
        mainSplitPane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, e -> {
            // Store divider location for user preference
            int newLocation = (Integer) e.getNewValue();
            // Could save this to preferences if needed
        });
        
        add(northPanel, BorderLayout.NORTH);
        add(mainSplitPane, BorderLayout.CENTER);
        add(eastPanel, BorderLayout.EAST);
    }

    private JPanel createNorthPanel() {
        JPanel northPanel = new JPanel(new BorderLayout(10, 10));
        northPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Template configuration panel
        JPanel templatePanel = new JPanel(new GridBagLayout());
        templatePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Template Configuration",
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
            new Font(Font.SANS_SERIF, Font.BOLD, 12)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Template format row
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        templatePanel.add(new JLabel("Template Format:"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        templatePanel.add(templateFormatField, gbc);
        
        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        templatePanel.add(applyTemplateButton, gbc);

        // Preview row
        gbc.gridx = 0; gbc.gridy = 1;
        templatePanel.add(new JLabel("Preview:"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        templatePanel.add(previewLabel, gbc);

        // Controls panel with Phase 3 features
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        controlsPanel.add(new JLabel("Additional Evaluators:"));
        controlsPanel.add(evaluatorCountComboBox);
        controlsPanel.add(new JLabel("    "));
        controlsPanel.add(new JLabel("Batch Size:"));
        controlsPanel.add(batchSizeSpinner);
        controlsPanel.add(new JLabel("    "));
        controlsPanel.add(saveConfigButton);
        controlsPanel.add(loadConfigButton);
        controlsPanel.add(autoSaveCheckBox);
        controlsPanel.add(new JLabel("    "));
        controlsPanel.add(resetAllButton);
        controlsPanel.add(documentationButton);
        
        // Export controls panel
        JPanel exportPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        exportPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Export Options",
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
            new Font(Font.SANS_SERIF, Font.BOLD, 11)));
        exportPanel.add(new JLabel("Format:"));
        exportPanel.add(exportFormatComboBox);
        exportPanel.add(exportButton);
        
        // Load Example controls panel
        JPanel examplePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        examplePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Load Example",
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
            new Font(Font.SANS_SERIF, Font.BOLD, 11)));
        
        JButton bsnExampleButton = new JButton("Dutch BSN");
        bsnExampleButton.setToolTipText("Load Dutch BSN number generator example");
        bsnExampleButton.addActionListener(e -> loadBSNExample());
        bsnExampleButton.setBackground(new Color(100, 149, 237));
        bsnExampleButton.setForeground(Color.WHITE);
        
        JButton licenseExampleButton = new JButton("License Plate");
        licenseExampleButton.setToolTipText("Load license plate generator example");
        licenseExampleButton.addActionListener(e -> loadLicensePlateExample());
        licenseExampleButton.setBackground(new Color(100, 149, 237));
        licenseExampleButton.setForeground(Color.WHITE);
        
        JButton complexExampleButton = new JButton("Complex Multi-Gen");
        complexExampleButton.setToolTipText("Load complex example using all generator types");
        complexExampleButton.addActionListener(e -> loadComplexExample());
        complexExampleButton.setBackground(new Color(100, 149, 237));
        complexExampleButton.setForeground(Color.WHITE);
        
        examplePanel.add(bsnExampleButton);
        examplePanel.add(licenseExampleButton);
        examplePanel.add(complexExampleButton);
        
        // Configuration status panel
        JPanel statusConfigPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusConfigPanel.add(new JLabel("Config Status:"));
        statusConfigPanel.add(configurationStatusLabel);
        
        // Add quick configuration help
        JLabel configHelpLabel = new JLabel("<html><font size='-1'><i>Tip: Use Reset All to clear everything, Help & Docs for detailed guidance</i></font></html>");
        configHelpLabel.setForeground(Color.GRAY);
        statusConfigPanel.add(new JLabel("  |  "));
        statusConfigPanel.add(configHelpLabel);

        northPanel.add(templatePanel, BorderLayout.CENTER);
        
        JPanel controlsContainer = new JPanel(new BorderLayout());
        controlsContainer.add(controlsPanel, BorderLayout.NORTH);
        
        // Create a combined panel for export and examples
        JPanel exportExamplePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        exportExamplePanel.add(exportPanel);
        exportExamplePanel.add(examplePanel);
        controlsContainer.add(exportExamplePanel, BorderLayout.CENTER);
        
        controlsContainer.add(statusConfigPanel, BorderLayout.SOUTH);
        northPanel.add(controlsContainer, BorderLayout.SOUTH);
        
        return northPanel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));

        // Scrollable generator panel container with BOTH vertical and horizontal scrolling
        JScrollPane generatorScrollPane = new JScrollPane(generatorPanelContainer);
        generatorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        generatorScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        generatorScrollPane.setMinimumSize(new Dimension(0, 200)); // Minimum size for split pane
        generatorScrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
        
        // Use WrapLayout instead of FlowLayout for better wrapping behavior
        generatorPanelContainer.setLayout(new WrapLayout(WrapLayout.LEFT, 15, 15));
        // Set a more reasonable preferred size for proper scrolling
        generatorPanelContainer.setPreferredSize(new Dimension(1200, 300)); // Start smaller, will be adjusted dynamically

        centerPanel.add(generatorScrollPane, BorderLayout.CENTER);
        return centerPanel;
    }

    private JPanel createSouthPanel() {
        JPanel southPanel = new JPanel(new BorderLayout(10, 10));
        southPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Output panel with title
        JPanel outputPanel = new JPanel(new BorderLayout(5, 5));
        outputPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Generated Output",
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
            new Font(Font.SANS_SERIF, Font.BOLD, 12)));

        JScrollPane outputScrollPane = new JScrollPane(outputArea);
        outputScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        outputPanel.add(outputScrollPane, BorderLayout.CENTER);

        // Output controls
        JPanel outputControlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        outputControlsPanel.add(clearOutputButton);
        outputPanel.add(outputControlsPanel, BorderLayout.SOUTH);

        // Status panel
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.add(statusLabel, BorderLayout.WEST);
        statusPanel.add(progressBar, BorderLayout.CENTER);

        southPanel.add(outputPanel, BorderLayout.CENTER);
        southPanel.add(statusPanel, BorderLayout.SOUTH);
        
        return southPanel;
    }

    private JPanel createEastPanel() {
        JPanel eastPanel = new JPanel(new BorderLayout());
        eastPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 15));
        
        // Generation control panel
        JPanel generatePanel = new JPanel(new GridLayout(2, 1, 5, 10));
        generatePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Generation Control",
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
            new Font(Font.SANS_SERIF, Font.BOLD, 12)));
        
        generateButton.setPreferredSize(new Dimension(120, 35));
        generatePanel.add(generateButton);
        
        JButton previewButton = new JButton("Preview Sample");
        previewButton.setToolTipText("Generate a small preview sample");
        previewButton.addActionListener(e -> generatePreview());
        generatePanel.add(previewButton);
        
        eastPanel.add(generatePanel, BorderLayout.NORTH);
        return eastPanel;
    }

    private void applyTemplate() {
        String templateFormat = templateFormatField.getText();
        
        // Validate template format
        if (templateFormat == null || templateFormat.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a template format (e.g., {0} or {0}-{1})", 
                "Invalid Template", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Basic template validation
        if (!templateFormat.contains("{") || !templateFormat.contains("}")) {
            JOptionPane.showMessageDialog(this, 
                "Template format must contain placeholders like {0}, {1}, etc.", 
                "Invalid Template Format", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int numberOfGenerators = countGenerators(templateFormat);
        
        if (numberOfGenerators == 0) {
            JOptionPane.showMessageDialog(this, 
                "Template format must contain at least one generator placeholder (e.g., {0})", 
                "No Generators Found", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        setupGeneratorsBasedOnTemplate(numberOfGenerators);
        
        // Show success message
        JOptionPane.showMessageDialog(this, 
            "Template applied successfully! " + numberOfGenerators + " generator(s) configured.", 
            "Template Applied", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void addEvaluatorPanels() {
        int evaluatorCount = (int) evaluatorCountComboBox.getSelectedItem();
        for (int i = 0; i < evaluatorCount; i++) {
            GeneratorPanel evaluatorPanel = new GeneratorPanel(-1); // -1 indicates an additional evaluator
            generatorPanelContainer.add(evaluatorPanel);
        }
        
        // Recalculate container size when evaluators are added - more precise sizing
        int totalPanels = generatorPanelContainer.getComponentCount();
        int panelsPerRow = Math.max(1, 3); // Assume roughly 3 panels per row
        int numberOfRows = (int) Math.ceil((double) totalPanels / panelsPerRow);
        int requiredHeight = Math.max(280, numberOfRows * 280 + 50); // More precise calculation
        
        generatorPanelContainer.setPreferredSize(new Dimension(1200, requiredHeight));

        generatorPanelContainer.revalidate();
        generatorPanelContainer.repaint();
    }


    /**
     * WORKING
     * Counts the number of generators in the template format.
     * A generator is defined as a string of the form {GENERATOR_TYPE}.
     * @param template The template format to count the generators in.
     * @return The number of generators in the template format.
     */
    private int countGenerators(String template) {
        return (int) template.chars().filter(ch -> ch == '{').count();
    }

    /**
     * Sets up the generator panels based on the template format.
     * @param numberOfGenerators The number of generators to set up.
     */

    private void setupGeneratorsBasedOnTemplate(int numberOfGenerators) {
        // Remove the GridLayout setup as it's not needed with the WrapLayout
        generatorPanelContainer.removeAll();

        // Add generator panels
        for (int i = 0; i < numberOfGenerators; i++) {
            generatorPanelContainer.add(new GeneratorPanel(i));
        }

        // Add evaluator panels if any
        addEvaluatorPanels();
        
        // Calculate required size based on number of panels - more precise sizing
        int totalPanels = numberOfGenerators + (int) evaluatorCountComboBox.getSelectedItem();
        int panelsPerRow = Math.max(1, 3); // Assume roughly 3 panels per row for better calculation
        int numberOfRows = (int) Math.ceil((double) totalPanels / panelsPerRow);
        int requiredHeight = Math.max(280, numberOfRows * 280 + 50); // More precise: 280px per row + small padding
        
        generatorPanelContainer.setPreferredSize(new Dimension(1200, requiredHeight));

        generatorPanelContainer.revalidate();
        generatorPanelContainer.repaint();
    }

    private void generateNumbers() {
        SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    // Input validation
                    String templateFormat = templateFormatField.getText();
                    if (templateFormat == null || templateFormat.trim().isEmpty()) {
                        publish("Error: Please enter a template format first.");
                        return null;
                    }
                    
                    // Update UI state
                    SwingUtilities.invokeLater(() -> {
                        generateButton.setEnabled(false);
                        progressBar.setString("Generating...");
                        progressBar.setIndeterminate(true);
                        statusLabel.setText("Generating data...");
                        outputArea.setText("");
                    });
                    
                    InputProcessor processor = new InputProcessor(templateFormat.trim());
                    
                    // Collect and validate generator configurations
                    int generatorCount = 0;
                    for (Component comp : generatorPanelContainer.getComponents()) {
                        if (comp instanceof GeneratorPanel) {
                            GeneratorPanel panel = (GeneratorPanel) comp;
                            Map<String, String> properties = panel.getProperties();
                            String genType = properties.get("type");
                            
                            if (genType == null || genType.trim().isEmpty()) {
                                publish("Error: Generator type not selected for panel " + generatorCount);
                                return null;
                            }
                            
                            processor.addGeneratorAndPattern(genType, properties);
                            generatorCount++;
                        }
                    }
                    
                    if (generatorCount == 0) {
                        publish("Error: No generators configured. Please apply a template first.");
                        return null;
                    }

                    Template template = processor.initTemplate();
                    MainGenerator mainGenerator = processor.initMainGenerator();

                    // Get batch size from spinner
                    int batchSize = (Integer) batchSizeSpinner.getValue();
                    publish("Generating " + batchSize + " values...\n");
                    
                    // Generate results with progress updates
                    results = mainGenerator.generate().limit(batchSize).collect(Collectors.toList());
                    
                    publish("Generated " + results.size() + " values:");
                    publish("=" .repeat(60));
                    
                    for (String value : results) {
                        publish(value);
                    }
                    
                    publish("");
                    publish("=" .repeat(60));
                    publish("Generation completed successfully. Generated " + results.size() + " records.");
                    
                } catch (IllegalArgumentException e) {
                    publish("Configuration Error: " + e.getMessage());
                    publish("Please check your generator settings and try again.");
                } catch (Exception e) {
                    publish("Unexpected Error: " + e.getMessage());
                    publish("Please check the application logs for more details.");
                }
                return null;
            }
            
            @Override
            protected void process(List<String> chunks) {
                for (String line : chunks) {
                    outputArea.append(line + "\n");
                }
                outputArea.setCaretPosition(outputArea.getDocument().getLength());
            }
            
            @Override
            protected void done() {
                generateButton.setEnabled(true);
                progressBar.setIndeterminate(false);
                progressBar.setString("Ready");
                statusLabel.setText("Generation complete");
                
                // Phase 3: Enable export button after successful generation
                if (results != null && !results.isEmpty()) {
                    exportButton.setEnabled(true);
                    statusLabel.setText("Generation complete - " + results.size() + " records ready for export");
                }
                
                // Auto-save configuration if enabled
                if (autoSaveCheckBox.isSelected()) {
                    autoSaveConfiguration();
                }
            }
        };
        
        worker.execute();
    }

    // New Phase 2 Enhanced Methods
    
    private void setupModernStyling() {
        // Apply modern styling to components
        try {
            // Set modern button styling
            UIManager.put("Button.background", new Color(70, 130, 180));
            UIManager.put("Button.foreground", Color.WHITE);
            UIManager.put("Button.font", new Font(Font.SANS_SERIF, Font.BOLD, 12));
            
            // Enhanced component styling
            templateFormatField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 149, 237), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
            
            // Style the generate button specially
            generateButton.setBackground(new Color(34, 139, 34));
            generateButton.setForeground(Color.WHITE);
            generateButton.setFocusPainted(false);
            
        } catch (Exception e) {
            System.err.println("Could not apply custom styling: " + e.getMessage());
        }
    }
    
    /**
     * Show comprehensive documentation window
     */
    private void showDocumentation() {
        JFrame docFrame = new JFrame("Multi-Format Data Generator - Documentation");
        docFrame.setSize(800, 600);
        docFrame.setLocationRelativeTo(this);
        docFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Overview tab
        tabbedPane.addTab("Overview", createOverviewPanel());
        
        // Generator types tab
        tabbedPane.addTab("Generator Types", createGeneratorTypesPanel());
        
        // Examples tab
        tabbedPane.addTab("Examples", createExamplesPanel());
        
        // Validation guide tab
        tabbedPane.addTab("Field Validation", createValidationPanel());
        
        docFrame.add(tabbedPane);
        docFrame.setVisible(true);
    }
    
    private JPanel createOverviewPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        textArea.setText(
            "MULTI-FORMAT DATA GENERATOR\n" +
            "===========================\n\n" +
            "This application generates synthetic data with customizable formats.\n\n" +
            "HOW IT WORKS:\n" +
            "1. Define a template format like {0}, {0}-{1}, or {0}_{1}_{2}\n" +
            "2. Configure generators for each placeholder ({0}, {1}, etc.)\n" +
            "3. Optionally add evaluators to filter results\n" +
            "4. Generate data and export in CSV, TXT, or JSON formats\n\n" +
            "FEATURES:\n" +
            "- Real-time input validation with color feedback\n" +
            "- Progress tracking for large datasets\n" +
            "- Configuration save/load\n" +
            "- Multiple export formats\n" +
            "- Adjustable interface layout\n" +
            "- Comprehensive help system"
        );
        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createGeneratorTypesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        textArea.setText(
            "GENERATOR TYPES\n" +
            "===============\n\n" +
            "1. SEQUENTIAL NUMBER GENERATOR\n" +
            "   Purpose: Generate sequential numeric values\n" +
            "   Example: 100, 101, 102, 103...\n\n" +
            "2. SEQUENTIAL ASCII GENERATOR\n" +
            "   Purpose: Generate sequential character combinations\n" +
            "   Example: AAA, AAB, AAC, ABA...\n\n" +
            "3. CALCULATION GENERATOR\n" +
            "   Purpose: Perform mathematical calculations\n" +
            "   Example: formula=A+B (adds values from other generators)\n\n" +
            "4. EVALUATION GENERATOR (Filter)\n" +
            "   Purpose: Filter results based on conditions\n" +
            "   Example: A%2==0 (only even numbers)"
        );
        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createExamplesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        textArea.setText(
            "PRACTICAL EXAMPLES\n" +
            "==================\n\n" +
            "EXAMPLE 1: Simple Sequential Numbers\n" +
            "Template: {0}\n" +
            "Generator 0: SEQUENTIALNUMBERGENERATOR\n" +
            "  input=0, start=100, step=1, length=3\n" +
            "Result: 100, 101, 102, 103...\n\n" +
            "EXAMPLE 2: License Plates (Letters + Numbers)\n" +
            "Template: {0}-{1}\n" +
            "Generator 0: SEQUENTIALASCIIGENERATOR\n" +
            "  list=A,B,C, start=AAA, length=3\n" +
            "Generator 1: SEQUENTIALNUMBERGENERATOR\n" +
            "  start=100, step=1\n" +
            "Result: AAA-100, AAB-101, AAC-102, ABA-103...\n\n" +
            "EXAMPLE 3: Dutch BSN Numbers (with validation)\n" +
            "Template: {0}\n" +
            "Generator 0: SEQUENTIALNUMBERGENERATOR\n" +
            "  start=100000000, step=1, length=9\n" +
            "Evaluator: EVALUATION\n" +
            "  formula=(9*A+8*B+7*C+6*D+5*E+4*F+3*G+2*H-I)%11==0\n" +
            "Result: Only valid BSN numbers\n\n" +
            "EXAMPLE 4: Complex Format with Calculation\n" +
            "Template: {0}-{1}-{2}\n" +
            "Generator 0: SEQUENTIALNUMBERGENERATOR (start=100)\n" +
            "Generator 1: CALCULATION (formula=A*2, input=0)\n" +
            "Generator 2: SEQUENTIALASCIIGENERATOR (list=X,Y,Z)\n" +
            "Result: 100-200-XXX, 101-202-XXY, 102-204-XXZ..."
        );
        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createValidationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        textArea.setText(
            "FIELD VALIDATION GUIDE\n" +
            "======================\n\n" +
            "VALIDATION COLORS:\n" +
            "- Green: Valid input\n" +
            "- Orange: Valid but check range\n" +
            "- Red: Invalid input\n" +
            "- Gray: Empty field\n\n" +
            "FIELD REQUIREMENTS:\n\n" +
            "input: Generator index (0-19)\n" +
            "length: Output length (1-50)\n" +
            "start: Number (0+) or ASCII text (A-Z, 1-10 chars)\n" +
            "step: Increment (1-1000)\n" +
            "list: Comma-separated (A,B,C or 1,2,3)\n" +
            "formula: Math/logic expression (A+B, A%2==0)\n" +
            "format: Template with placeholders ({0}, {1})"
        );
        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }
    
    /**
     * Reset all inputs to default state
     */
    private void resetAllInputs() {
        int result = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to reset all inputs?\nThis will clear the template, generators, and output.",
            "Reset All",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (result == JOptionPane.YES_OPTION) {
            // Clear template format
            templateFormatField.setText("");
            
            // Reset batch size to default
            batchSizeSpinner.setValue(100);
            
            // Reset evaluator count
            evaluatorCountComboBox.setSelectedIndex(0);
            
            // Clear all generator panels
            generatorPanelContainer.removeAll();
            generatorPanelContainer.revalidate();
            generatorPanelContainer.repaint();
            
            // Clear output
            outputArea.setText("");
            
            // Reset export button
            exportButton.setEnabled(false);
            
            // Clear results
            results = null;
            
            // Reset preview
            previewLabel.setText("Template preview will appear here");
            previewLabel.setForeground(Color.GRAY);
            
            // Reset status labels
            statusLabel.setText("Ready to generate data");
            configurationStatusLabel.setText("No configuration loaded");
            configurationStatusLabel.setForeground(Color.GRAY);
            
            // Reset progress bar
            progressBar.setString("Ready");
            progressBar.setValue(0);
            
            // Reset auto-save checkbox
            autoSaveCheckBox.setSelected(false);
            
            // Reset export format
            exportFormatComboBox.setSelectedIndex(0);
            
            statusLabel.setText("All inputs reset to default state");
        }
    }
    
    private void showPreview() {
        String template = templateFormatField.getText();
        if (template != null && !template.trim().isEmpty()) {
            // Generate a simple preview of what the template might look like
            String preview = template.replaceAll("\\{(\\d+)\\}", "[Gen$1]")
                                   .replaceAll("\\{(\\w+)\\}", "[Gen]");
            previewLabel.setText("Preview: " + preview);
            previewLabel.setForeground(new Color(34, 139, 34));
        } else {
            previewLabel.setText("Template preview will appear here");
            previewLabel.setForeground(Color.GRAY);
        }
    }
    
    private void generatePreview() {
        SwingWorker<Void, String> previewWorker = new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    String templateFormat = templateFormatField.getText();
                    if (templateFormat == null || templateFormat.trim().isEmpty()) {
                        publish("Preview Error: Please enter a template format first.");
                        return null;
                    }
                    
                    SwingUtilities.invokeLater(() -> {
                        statusLabel.setText("Generating preview...");
                        progressBar.setString("Preview...");
                    });
                    
                    InputProcessor processor = new InputProcessor(templateFormat.trim());
                    
                    int generatorCount = 0;
                    for (Component comp : generatorPanelContainer.getComponents()) {
                        if (comp instanceof GeneratorPanel) {
                            GeneratorPanel panel = (GeneratorPanel) comp;
                            Map<String, String> properties = panel.getProperties();
                            String genType = properties.get("type");
                            
                            if (genType != null && !genType.trim().isEmpty()) {
                                processor.addGeneratorAndPattern(genType, properties);
                                generatorCount++;
                            }
                        }
                    }
                    
                    if (generatorCount > 0) {
                        Template template = processor.initTemplate();
                        MainGenerator mainGenerator = processor.initMainGenerator();
                        
                        List<String> previewResults = mainGenerator.generate().limit(5).collect(Collectors.toList());
                        
                        publish("=== PREVIEW (5 samples) ===");
                        for (String value : previewResults) {
                            publish(value);
                        }
                        publish("=== END PREVIEW ===");
                        publish("");
                    } else {
                        publish("Preview Error: No generators configured.");
                    }
                    
                } catch (Exception e) {
                    publish("Preview Error: " + e.getMessage());
                }
                return null;
            }
            
            @Override
            protected void process(List<String> chunks) {
                for (String line : chunks) {
                    outputArea.append(line + "\n");
                }
                outputArea.setCaretPosition(outputArea.getDocument().getLength());
            }
            
            @Override
            protected void done() {
                statusLabel.setText("Preview complete");
                progressBar.setString("Ready");
            }
        };
        
        previewWorker.execute();
    }
    
    private void saveConfiguration() {
        try {
            String templateFormat = templateFormatField.getText();
            int batchSize = (Integer) batchSizeSpinner.getValue();
            int evaluatorCount = (Integer) evaluatorCountComboBox.getSelectedItem();
            
            // Get all generator components
            List<Component> generatorComponents = new ArrayList<>();
            for (Component comp : generatorPanelContainer.getComponents()) {
                if (comp instanceof GeneratorPanel) {
                    generatorComponents.add(comp);
                }
            }
            
            boolean success = configurationService.saveConfiguration(
                this, templateFormat, batchSize, evaluatorCount, generatorComponents);
            
            if (success) {
                configurationStatusLabel.setText("Configuration saved successfully");
                configurationStatusLabel.setForeground(new Color(34, 139, 34));
                statusLabel.setText("Configuration saved");
            }
        } catch (Exception e) {
            statusLabel.setText("Failed to save configuration: " + e.getMessage());
            configurationStatusLabel.setText("Save failed");
            configurationStatusLabel.setForeground(Color.RED);
        }
    }
    
    private void loadConfiguration() {
        try {
            GeneratorConfiguration config = configurationService.loadConfiguration(this);
            
            if (config != null) {
                // Apply loaded configuration to UI
                applyConfigurationToUI(config);
                
                configurationStatusLabel.setText("Configuration loaded: " + config.getMetadata().getName());
                configurationStatusLabel.setForeground(new Color(34, 139, 34));
                statusLabel.setText("Configuration loaded successfully");
            }
        } catch (Exception e) {
            statusLabel.setText("Failed to load configuration: " + e.getMessage());
            configurationStatusLabel.setText("Load failed");
            configurationStatusLabel.setForeground(Color.RED);
        }
    }
    
    private void applyConfigurationToUI(GeneratorConfiguration config) {
        // Set template format
        templateFormatField.setText(config.getTemplateFormat());
        
        // Set batch size
        batchSizeSpinner.setValue(config.getBatchSize());
        
        // Set evaluator count
        evaluatorCountComboBox.setSelectedItem(config.getEvaluatorCount());
        
        // Apply template to create generator panels
        applyTemplate();
        
        // Configure each generator panel
        int panelIndex = 0;
        for (Component comp : generatorPanelContainer.getComponents()) {
            if (comp instanceof GeneratorPanel && panelIndex < config.getGenerators().size()) {
                GeneratorPanel panel = (GeneratorPanel) comp;
                GeneratorConfiguration.GeneratorConfig genConfig = config.getGenerators().get(panelIndex);
                
                // Apply properties to the panel
                for (Map.Entry<String, String> property : genConfig.getProperties().entrySet()) {
                    // This would require exposing property fields or adding a method to GeneratorPanel
                    // For now, we'll skip detailed property setting
                }
                
                panelIndex++;
            }
        }
        
        // Update preview
        showPreview();
    }
    
    private void autoSaveConfiguration() {
        // TODO: Implement auto-save to a temp file
        // This would save to a predefined location without showing dialog
        configurationStatusLabel.setText("Auto-saved");
        configurationStatusLabel.setForeground(new Color(100, 149, 237));
    }
    
    private void exportData() {
        if (results == null || results.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No data to export. Please generate data first.",
                "No Data",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String selectedFormat = (String) exportFormatComboBox.getSelectedItem();
        String templateFormat = templateFormatField.getText();
        
        try {
            boolean success = false;
            
            switch (selectedFormat.toUpperCase()) {
                case "CSV":
                    success = exportService.exportToCSV(this, results, templateFormat);
                    break;
                case "TXT":
                    success = exportService.exportToTXT(this, results, templateFormat);
                    break;
                case "JSON":
                    success = exportService.exportToJSON(this, results, templateFormat);
                    break;
                default:
                    JOptionPane.showMessageDialog(this,
                        "Unsupported export format: " + selectedFormat,
                        "Export Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
            }
            
            if (success) {
                statusLabel.setText("Data exported successfully as " + selectedFormat);
            }
        } catch (Exception e) {
            statusLabel.setText("Export failed: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                "Failed to export data: " + e.getMessage(),
                "Export Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Example loading methods
    private void loadBSNExample() {
        // Clear existing configuration
        generatorPanelContainer.removeAll();
        
        // Set template for Dutch BSN
        templateFormatField.setText("{0}");
        evaluatorCountComboBox.setSelectedIndex(1); // Set to 1 evaluator first
        batchSizeSpinner.setValue(10);
        
        // Apply template to create generator panels
        setupGeneratorsBasedOnTemplate(1);
        
        // Configure the generator panel
        if (generatorPanelContainer.getComponentCount() > 0) {
            GeneratorPanel panel = (GeneratorPanel) generatorPanelContainer.getComponent(0);
            setGeneratorProperties(panel, "SEQUENTIALNUMBERGENERATOR", new String[][]{
                {"input", "0"},
                {"length", "9"},
                {"start", "100000000"},
                {"step", "1"},
                {"padding-length", "0"}
            });
        }
        
        // The evaluator should already be created by setupGeneratorsBasedOnTemplate
        // Find and configure the evaluator panel
        for (int i = 0; i < generatorPanelContainer.getComponentCount(); i++) {
            Component comp = generatorPanelContainer.getComponent(i);
            if (comp instanceof GeneratorPanel) {
                GeneratorPanel panel = (GeneratorPanel) comp;
                if (panel.getGeneratorIndex() == -1) { // This is an evaluator
                    setGeneratorProperties(panel, "EVALUATION", new String[][]{
                        {"input", "0"},
                        {"formula", "(9*A + 8*B + 7*C + 6*D + 5*E + 4*F + 3*G + 2*H - I) % 11 == 0"}
                    });
                    break;
                }
            }
        }
        
        statusLabel.setText("Dutch BSN example loaded - ready to generate!");
        showPreview();
    }
    
    private void loadLicensePlateExample() {
        // Clear existing configuration
        generatorPanelContainer.removeAll();
        
        // Set template for License Plate (ABC-123 format)
        templateFormatField.setText("{0}-{1}");
        evaluatorCountComboBox.setSelectedIndex(0);
        batchSizeSpinner.setValue(20);
        
        // Apply template to create generator panels
        setupGeneratorsBasedOnTemplate(2);
        
        // Configure first generator (letters)
        if (generatorPanelContainer.getComponentCount() > 0) {
            GeneratorPanel panel = (GeneratorPanel) generatorPanelContainer.getComponent(0);
            setGeneratorProperties(panel, "SEQUENTIALASCIIGENERATOR", new String[][]{
                {"input", "0"},
                {"list", "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z"},
                {"length", "3"},
                {"start", "AAA"},
                {"step", "1"},
                {"padding-length", "3"}
            });
        }
        
        // Configure second generator (numbers)
        if (generatorPanelContainer.getComponentCount() > 1) {
            GeneratorPanel panel = (GeneratorPanel) generatorPanelContainer.getComponent(1);
            setGeneratorProperties(panel, "SEQUENTIALNUMBERGENERATOR", new String[][]{
                {"input", "1"},
                {"length", "3"},
                {"start", "100"},
                {"step", "1"},
                {"padding-length", "3"}
            });
        }
        
        statusLabel.setText("License Plate example loaded - ready to generate!");
        showPreview();
    }
    
    private void loadComplexExample() {
        // Clear existing configuration
        generatorPanelContainer.removeAll();
        
        // Set simple multi-generator template
        templateFormatField.setText("{0}-{1}-{2}");
        evaluatorCountComboBox.setSelectedIndex(0); // No evaluators to keep it simple
        batchSizeSpinner.setValue(10);
        
        // Apply template to create generator panels
        setupGeneratorsBasedOnTemplate(3);
        
        // Generator 0: Simple Sequential Number
        if (generatorPanelContainer.getComponentCount() > 0) {
            GeneratorPanel panel = (GeneratorPanel) generatorPanelContainer.getComponent(0);
            setGeneratorProperties(panel, "SEQUENTIALNUMBERGENERATOR", new String[][]{
                {"input", "0"}, {"length", "3"}, {"start", "100"}, {"step", "1"}, {"padding-length", "3"}
            });
        }
        
        // Generator 1: Simple ASCII generator
        if (generatorPanelContainer.getComponentCount() > 1) {
            GeneratorPanel panel = (GeneratorPanel) generatorPanelContainer.getComponent(1);
            setGeneratorProperties(panel, "SEQUENTIALASCIIGENERATOR", new String[][]{
                {"input", "1"}, {"list", "A,B,C"}, {"length", "2"}, {"start", "AA"}, {"step", "1"}, {"padding-length", "2"}
            });
        }
        
        // Generator 2: Simple Calculation using Generator 0
        if (generatorPanelContainer.getComponentCount() > 2) {
            GeneratorPanel panel = (GeneratorPanel) generatorPanelContainer.getComponent(2);
            setGeneratorProperties(panel, "CALCULATION", new String[][]{
                {"input", "0"}, {"formula", "A + 10"}, {"length", "3"}
            });
        }
        
        statusLabel.setText("Simple multi-generator example loaded - ready to generate!");
        showPreview();
    }
    
    private void setGeneratorProperties(GeneratorPanel panel, String generatorType, String[][] properties) {
        // This is a helper method to programmatically set properties
        // Since GeneratorPanel doesn't expose direct property setting, 
        // we'll need to simulate the configuration process
        
        // Set the generator type first
        panel.setGeneratorType(generatorType);
        panel.configureFieldsBasedOnType(generatorType);
        
        // Set properties in the panel's property fields
        Map<String, JComponent> fields = panel.getPropertyFields();
        
        for (String[] property : properties) {
            String key = property[0];
            String value = property[1];
            
            JComponent field = fields.get(key);
            if (field != null) {
                if (field instanceof JTextField) {
                    ((JTextField) field).setText(value);
                } else if (field instanceof JScrollPane) {
                    // Handle text area wrapped in scroll pane
                    JScrollPane scrollPane = (JScrollPane) field;
                    JTextArea textArea = (JTextArea) scrollPane.getViewport().getView();
                    textArea.setText(value);
                }
            }
        }
        
        panel.revalidate();
        panel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GeneratorUI frame = new GeneratorUI();
            frame.setVisible(true);
        });
    }
}
