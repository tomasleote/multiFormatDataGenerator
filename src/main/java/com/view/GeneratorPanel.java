package com.view;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.awt.GridBagConstraints;
import java.awt.Insets;

@Getter
@Setter
public class GeneratorPanel extends JPanel {

    private Map<String, JComponent> propertyFields;
    private String generatorType;
    private Map<String, String> properties;
    private int generatorIndex;
    
    // Height adjustment components
    private JButton resizeButton;
    private boolean isExpanded = false;
    private Dimension defaultSize = new Dimension(320, 240);
    private Dimension expandedSize = new Dimension(320, 380);
    private JPanel fieldsPanel;

    public GeneratorPanel(int generatorIndex) {
        this.generatorIndex = generatorIndex;
        propertyFields = new HashMap<>();
        properties = new HashMap<>();

        setupModernPanelDesign();
        initializePanel();
    }
    
    private void setupModernPanelDesign() {
        setLayout(new BorderLayout());
        
        // Enhanced border with color coding
        Color borderColor = generatorIndex >= 0 ? new Color(70, 130, 180) : new Color(255, 140, 0);
        String title = generatorIndex >= 0 ? "Generator " + generatorIndex : "Evaluator";
        
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(borderColor, 2),
            BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), title,
                    TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                    new Font(Font.SANS_SERIF, Font.BOLD, 12), borderColor),
                BorderFactory.createEmptyBorder(5, 8, 5, 8))));
        
        // Start with default size but make it resizable
        setPreferredSize(defaultSize);
        setMinimumSize(new Dimension(300, 220));
        setMaximumSize(new Dimension(400, 500));
    }
    
    private void initializePanel() {
        // Create header panel with controls
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 5, 2));
        
        // Generator type selector
        JComboBox<String> generatorTypeComboBox = new JComboBox<>(new String[]{
            "SEQUENTIALNUMBERGENERATOR", "CALCULATION", "EVALUATION", "SEQUENTIALASCIIGENERATOR"});
        generatorTypeComboBox.setToolTipText("Select the type of generator");
        generatorTypeComboBox.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        generatorTypeComboBox.addActionListener(e -> {
            String selectedType = (String) generatorTypeComboBox.getSelectedItem();
            configureFieldsBasedOnType(selectedType);
            showGeneratorHelp(selectedType);
        });
        
        // Create resize button with better visibility
        resizeButton = new JButton(isExpanded ? "▲" : "▼");
        resizeButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        resizeButton.setPreferredSize(new Dimension(30, 25));
        resizeButton.setToolTipText("Click to expand/collapse panel height");
        resizeButton.addActionListener(e -> togglePanelHeight());
        resizeButton.setBackground(new Color(160, 160, 160)); // Darker grey to blend better
        resizeButton.setBorderPainted(true);
        resizeButton.setFocusPainted(false);
        resizeButton.setBorder(BorderFactory.createRaisedBevelBorder());
        
        // Add status indicator for validation
        JLabel statusLabel = new JLabel("●");
        statusLabel.setForeground(Color.GRAY);
        statusLabel.setToolTipText("Panel status: Gray=Empty, Green=Valid, Orange=Check, Red=Invalid");
        
        // Header layout
        JPanel headerControls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 0));
        headerControls.add(statusLabel);
        headerControls.add(resizeButton);
        
        headerPanel.add(generatorTypeComboBox, BorderLayout.CENTER);
        headerPanel.add(headerControls, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Initialize the fields panel
        fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        JScrollPane scrollPane = new JScrollPane(fieldsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        // Remove white background - let it inherit the default grey background
        
        add(scrollPane, BorderLayout.CENTER);
        
        // Default to first type
        configureFieldsBasedOnType("SEQUENTIALNUMBERGENERATOR");
    }

    private void togglePanelHeight() {
        isExpanded = !isExpanded;
        resizeButton.setText(isExpanded ? "▲" : "▼");
        resizeButton.setToolTipText(isExpanded ? "Click to collapse panel" : "Click to expand panel");
        
        Dimension newSize = isExpanded ? expandedSize : defaultSize;
        setPreferredSize(newSize);
        setMinimumSize(isExpanded ? new Dimension(300, 360) : new Dimension(300, 220));
        
        // Force immediate layout update with animation effect
        SwingUtilities.invokeLater(() -> {
            Container parent = getParent();
            if (parent != null) {
                parent.revalidate();
                parent.repaint();
                
                // Also update the main container size if needed
                Container grandParent = parent.getParent();
                if (grandParent != null) {
                    grandParent.revalidate();
                    grandParent.repaint();
                }
            }
        });
    }

    public void addPropertyField(String propertyName) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.gridy = fieldsPanel.getComponentCount() / 2; // Calculate row based on existing components
        
        // Enhanced label with proper wrapping
        gbc.gridx = 0;
        gbc.weightx = 0.35;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        
        JLabel label = new JLabel("<html>" + propertyName + ":</html>");
        label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        label.setVerticalAlignment(SwingConstants.TOP);
        setPropertyTooltip(label, propertyName);
        fieldsPanel.add(label, gbc);

        // Enhanced input field with better layout
        gbc.gridx = 1;
        gbc.weightx = 0.65;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        // Choose appropriate field type based on property
        JComponent inputField;
        if ("formula".equals(propertyName)) {
            // Use text area for formulas to handle longer expressions
            JTextArea textArea = new JTextArea(2, 15);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
            textArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(3, 6, 3, 6)));
            
            JScrollPane textAreaScroll = new JScrollPane(textArea);
            textAreaScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            textAreaScroll.setPreferredSize(new Dimension(180, 50));
            
            inputField = textAreaScroll;
            
            // Add validation to the text area
            textArea.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyReleased(java.awt.event.KeyEvent e) {
                    validateTextAreaField(textArea, propertyName);
                }
            });
            
        } else {
            // Regular text field for other properties
            JTextField textField = new JTextField(15);
            textField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
            textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(3, 6, 3, 6)));
            
            inputField = textField;
            
            // Add real-time validation feedback
            textField.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyReleased(java.awt.event.KeyEvent e) {
                    validateField(textField, propertyName);
                }
            });
            
            textField.addFocusListener(new java.awt.event.FocusAdapter() {
                @Override
                public void focusLost(java.awt.event.FocusEvent e) {
                    validateField(textField, propertyName);
                }
            });
        }
        
        inputField.setToolTipText(getPropertyTooltip(propertyName));
        propertyFields.put(propertyName, inputField);
        fieldsPanel.add(inputField, gbc);
        
        // Update the fields panel
        fieldsPanel.revalidate();
        fieldsPanel.repaint();
    }
    
    private void validateTextAreaField(JTextArea field, String propertyName) {
        String value = field.getText().trim();
        Color borderColor = Color.LIGHT_GRAY;
        
        switch (propertyName) {
            case "formula":
                // Formula validation - check for basic mathematical/logical operators
                if (!value.isEmpty()) {
                    // Basic validation for formulas
                    boolean hasValidOperators = value.matches(".*[A-Z+\\-*/()%=<>!&|]+.*");
                    boolean hasVariables = value.matches(".*[A-Z].*");
                    
                    if (hasValidOperators && hasVariables && value.length() >= 3 && value.length() <= 200) {
                        borderColor = new Color(34, 139, 34); // Green for seemingly valid formula
                    } else if (value.length() < 200) {
                        borderColor = new Color(255, 140, 0); // Orange for questionable formula
                    } else {
                        borderColor = new Color(220, 20, 60); // Red for too long or clearly invalid
                    }
                }
                break;
                
            default:
                if (!value.isEmpty()) {
                    borderColor = new Color(34, 139, 34); // Green for any content
                } else {
                    borderColor = Color.LIGHT_GRAY; // Gray for empty
                }
        }
        
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(borderColor, 1),
            BorderFactory.createEmptyBorder(3, 6, 3, 6)));
    }

    private void validateField(JTextField field, String propertyName) {
        String value = field.getText().trim();
        Color borderColor = Color.LIGHT_GRAY;
        
        // Enhanced validation logic based on actual generator requirements
        switch (propertyName) {
            case "length":
                // Length must be positive integer, reasonable range
                try {
                    if (!value.isEmpty()) {
                        int length = Integer.parseInt(value);
                        if (length > 0 && length <= 50) {
                            borderColor = new Color(34, 139, 34); // Green for valid
                        } else {
                            borderColor = new Color(255, 140, 0); // Orange for out of range
                        }
                    }
                } catch (NumberFormatException e) {
                    borderColor = new Color(220, 20, 60); // Red for invalid
                }
                break;
                
            case "step":
                // Step must be positive integer, reasonable range
                try {
                    if (!value.isEmpty()) {
                        int step = Integer.parseInt(value);
                        if (step > 0 && step <= 1000) {
                            borderColor = new Color(34, 139, 34); // Green for valid
                        } else {
                            borderColor = new Color(255, 140, 0); // Orange for out of range
                        }
                    }
                } catch (NumberFormatException e) {
                    borderColor = new Color(220, 20, 60); // Red for invalid
                }
                break;
                
            case "padding-length":
                // Padding length must be non-negative integer, reasonable range
                try {
                    if (!value.isEmpty()) {
                        int padding = Integer.parseInt(value);
                        if (padding >= 0 && padding <= 20) {
                            borderColor = new Color(34, 139, 34); // Green for valid
                        } else {
                            borderColor = new Color(255, 140, 0); // Orange for out of range
                        }
                    }
                } catch (NumberFormatException e) {
                    borderColor = new Color(220, 20, 60); // Red for invalid
                }
                break;
                
            case "start":
                // For start field, accept numbers OR ASCII text depending on generator type
                if (!value.isEmpty()) {
                    // Try parsing as number first
                    try {
                        long startNum = Long.parseLong(value);
                        if (startNum >= 0) {
                            borderColor = new Color(34, 139, 34); // Green for valid number
                        } else {
                            borderColor = new Color(255, 140, 0); // Orange for negative
                        }
                    } catch (NumberFormatException e) {
                        // Check if it's valid ASCII text (letters only, reasonable length)
                        if (value.matches("[A-Za-z]{1,10}")) {
                            borderColor = new Color(34, 139, 34); // Green for valid ASCII
                        } else {
                            borderColor = new Color(220, 20, 60); // Red for invalid
                        }
                    }
                }
                break;
                
            case "input":
                // Input must be non-negative integer (generator index)
                try {
                    if (!value.isEmpty()) {
                        int inputValue = Integer.parseInt(value);
                        if (inputValue >= 0 && inputValue < 20) { // Reasonable generator count
                            borderColor = new Color(34, 139, 34); // Green for valid
                        } else {
                            borderColor = new Color(255, 140, 0); // Orange for out of range
                        }
                    }
                } catch (NumberFormatException e) {
                    borderColor = new Color(220, 20, 60); // Red for invalid
                }
                break;
                
            case "list":
                // For list field (comma-separated values), validate format and content
                if (!value.isEmpty()) {
                    if (value.contains(",")) {
                        // Comma-separated list validation
                        String[] items = value.split(",");
                        boolean allValid = true;
                        for (String item : items) {
                            String trimmed = item.trim();
                            // Allow single characters (letters or numbers)
                            if (trimmed.isEmpty() || (trimmed.length() != 1 || !trimmed.matches("[A-Za-z0-9]"))) {
                                allValid = false;
                                break;
                            }
                        }
                        if (allValid && items.length >= 2 && items.length <= 50) {
                            borderColor = new Color(34, 139, 34); // Green for valid list
                        } else {
                            borderColor = new Color(220, 20, 60); // Red for invalid list
                        }
                    } else {
                        // Single item validation - should be comma-separated for lists
                        if (value.matches("[A-Za-z0-9]") && value.length() == 1) {
                            borderColor = new Color(255, 140, 0); // Orange - single item (suggest comma-separated)
                        } else {
                            borderColor = new Color(220, 20, 60); // Red for invalid
                        }
                    }
                }
                break;
                
            case "formula":
                // Formula validation - check for basic mathematical/logical operators
                if (!value.isEmpty()) {
                    // Basic validation for formulas
                    boolean hasValidOperators = value.matches(".*[A-Z+\\-*/()%=<>!&|]+.*");
                    boolean hasVariables = value.matches(".*[A-Z].*");
                    
                    if (hasValidOperators && hasVariables && value.length() >= 3 && value.length() <= 200) {
                        borderColor = new Color(34, 139, 34); // Green for seemingly valid formula
                    } else if (value.length() < 200) {
                        borderColor = new Color(255, 140, 0); // Orange for questionable formula
                    } else {
                        borderColor = new Color(220, 20, 60); // Red for too long or clearly invalid
                    }
                }
                break;
                
            case "format":
                // Format string validation - check for placeholders
                if (!value.isEmpty()) {
                    if (value.contains("{") && value.contains("}")) {
                        borderColor = new Color(34, 139, 34); // Green for format with placeholders
                    } else if (value.length() <= 50) {
                        borderColor = new Color(255, 140, 0); // Orange for simple format
                    } else {
                        borderColor = new Color(220, 20, 60); // Red for too long
                    }
                }
                break;
                
            default:
                // Default validation - just check if not empty
                if (!value.isEmpty()) {
                    borderColor = new Color(34, 139, 34); // Green for any content
                } else {
                    borderColor = Color.LIGHT_GRAY; // Gray for empty
                }
        }
        
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(borderColor, 1),
            BorderFactory.createEmptyBorder(3, 6, 3, 6)));
    }
    
    private void setPropertyTooltip(JLabel label, String propertyName) {
        label.setToolTipText(getPropertyTooltip(propertyName));
    }
    
    private String getPropertyTooltip(String propertyName) {
        switch (propertyName) {
            case "input": return "Index of another generator to use as input (0-based)";
            case "length": return "Expected length of generated values";
            case "start": return "Starting value for the sequence";
            case "step": return "Increment step between values";
            case "padding-length": return "Minimum length with zero-padding";
            case "format": return "Custom format string (optional)";
            case "formula": return "Mathematical or logical expression";
            case "list": return "Comma-separated list of values";
            default: return "Enter value for " + propertyName;
        }
    }

    public Map<String, String> getProperties() {
        // Clear existing properties first
        properties.clear();
        
        // Always add the generator type
        properties.put("type", generatorType);
        
        // Add all field properties with validation
        propertyFields.forEach((key, field) -> {
            String value = "";
            if (field instanceof JTextField) {
                value = ((JTextField) field).getText();
            } else if (field instanceof JScrollPane) {
                // Handle text area wrapped in scroll pane
                JScrollPane scrollPane = (JScrollPane) field;
                JTextArea textArea = (JTextArea) scrollPane.getViewport().getView();
                value = textArea.getText();
            }
            properties.put(key, value != null ? value.trim() : "");
        });
        
        return new HashMap<>(properties); // Return a copy to prevent external modification
    }


    public void configureFieldsBasedOnType(String type) {
        // Clear existing field components and properties
        fieldsPanel.removeAll();
        propertyFields.clear();
        setGeneratorType(type);
        
        properties.put("type", type);

        // Specific fields based on generator type
        switch (type) {
            case "SEQUENTIALNUMBERGENERATOR":
                addPropertyField("input");
                addPropertyField("length");
                addPropertyField("start");
                addPropertyField("step");
                addPropertyField("padding-length");
                addPropertyField("format");
                break;
            case "SEQUENTIALASCIIGENERATOR":
                addPropertyField("input");
                addPropertyField("list");
                addPropertyField("length");
                addPropertyField("start");
                addPropertyField("step");
                addPropertyField("padding-length");
                addPropertyField("format");
                break;
            case "EVALUATION":
                addPropertyField("input");
                addPropertyField("formula");
                break;
            case "CALCULATION":
                addPropertyField("input");
                addPropertyField("formula");
                addPropertyField("length");
                addPropertyField("format");
                break;
        }

        fieldsPanel.revalidate();
        fieldsPanel.repaint();
    }
    
    private void showGeneratorHelp(String type) {
        // Add a small help text area or tooltip for the selected generator type
        String helpText = getGeneratorHelpText(type);
        setToolTipText(helpText);
    }
    
    private String getGeneratorHelpText(String type) {
        switch (type) {
            case "SEQUENTIALNUMBERGENERATOR":
                return "<html><b>Sequential Number Generator</b><br/>" +
                       "Generates sequential numeric values.<br/>" +
                       "Example: 100, 101, 102, 103...</html>";
            case "SEQUENTIALASCIIGENERATOR":
                return "<html><b>Sequential ASCII Generator</b><br/>" +
                       "Generates sequential character combinations.<br/>" +
                       "Example: AAA, AAB, AAC, AAD...</html>";
            case "CALCULATION":
                return "<html><b>Calculation Generator</b><br/>" +
                       "Performs mathematical calculations on input.<br/>" +
                       "Example: A+B+C, A*2, etc.</html>";
            case "EVALUATION":
                return "<html><b>Evaluation Filter</b><br/>" +
                       "Filters values based on logical conditions.<br/>" +
                       "Example: A%2==0 (only even numbers)</html>";
            default:
                return "Select a generator type to see help information.";
        }
    }

}
