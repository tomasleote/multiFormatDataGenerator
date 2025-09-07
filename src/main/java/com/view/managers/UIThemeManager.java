package com.view.managers;

import com.view.ui.UIConstants;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * UIThemeManager - Handles all theme and styling operations
 * 
 * Purpose: Centralize theme management and component styling to ensure
 * consistent appearance across the application.
 * 
 * @author Multi-Format Data Generator Team
 * @version 1.0
 */
public class UIThemeManager {
    
    private boolean darkThemeEnabled = true;
    private boolean modernStylingEnabled = true;
    
    /**
     * Constructor
     */
    public UIThemeManager() {
        // Default constructor
    }
    
    /**
     * Apply the complete theme setup (dark theme + modern styling)
     * 
     * @param rootComponent The root component to update after theme application
     */
    public void applyCompleteTheme(Component rootComponent) {
        if (darkThemeEnabled) {
            applyDarkTheme(rootComponent);
        }
        
        if (modernStylingEnabled) {
            setupModernStyling();
        }
    }
    
    /**
     * Apply dark theme using Nimbus Look and Feel
     * 
     * @param rootComponent The root component to update after theme application
     */
    public void applyDarkTheme(Component rootComponent) {
        try {
            // Set Nimbus Look and Feel
            UIManager.setLookAndFeel(UIConstants.NIMBUS_LAF);
            
            // Apply dark theme colors
            configureDarkThemeColors();
            
            // Update the component tree if provided
            if (rootComponent != null) {
                SwingUtilities.updateComponentTreeUI(rootComponent);
            }
            
            System.out.println("Dark theme applied successfully");
            
        } catch (Exception e) {
            System.err.println("Could not set dark theme: " + e.getMessage());
            // Fallback to default system look and feel
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
            } catch (Exception fallbackException) {
                System.err.println("Could not set fallback theme: " + fallbackException.getMessage());
            }
        }
    }
    
    /**
     * Configure dark theme colors using Nimbus properties
     */
    private void configureDarkThemeColors() {
        UIManager.put(UIConstants.NIMBUS_CONTROL, UIConstants.DARK_CONTROL);
        UIManager.put(UIConstants.NIMBUS_INFO, UIConstants.DARK_INFO);
        UIManager.put(UIConstants.NIMBUS_BASE, UIConstants.DARK_BASE);
        UIManager.put(UIConstants.NIMBUS_ALERT_YELLOW, UIConstants.WARNING_COLOR);
        UIManager.put(UIConstants.NIMBUS_DISABLED_TEXT, UIConstants.DARK_DISABLED_TEXT);
        UIManager.put(UIConstants.NIMBUS_FOCUS, new Color(115, 164, 209));
        UIManager.put(UIConstants.NIMBUS_GREEN, new Color(176, 179, 50));
        UIManager.put(UIConstants.NIMBUS_INFO_BLUE, UIConstants.INFO_COLOR);
        UIManager.put(UIConstants.NIMBUS_LIGHT_BACKGROUND, UIConstants.DARK_LIGHT_BACKGROUND);
        UIManager.put(UIConstants.NIMBUS_ORANGE, new Color(191, 98, 4));
        UIManager.put(UIConstants.NIMBUS_RED, new Color(169, 46, 34));
        UIManager.put(UIConstants.NIMBUS_SELECTED_TEXT, UIConstants.DARK_SELECTED_TEXT);
        UIManager.put(UIConstants.NIMBUS_SELECTION_BACKGROUND, UIConstants.DARK_SELECTION_BACKGROUND);
        UIManager.put(UIConstants.NIMBUS_TEXT, UIConstants.DARK_TEXT);
    }
    
    /**
     * Setup modern styling for components
     */
    public void setupModernStyling() {
        try {
            // Set modern button styling
            UIManager.put(UIConstants.BUTTON_BACKGROUND, UIConstants.PRIMARY_BUTTON_COLOR);
            UIManager.put(UIConstants.BUTTON_FOREGROUND, Color.WHITE);
            UIManager.put(UIConstants.BUTTON_FONT, UIConstants.BOLD_FONT);
            
            System.out.println("Modern styling applied successfully");
            
        } catch (Exception e) {
            System.err.println("Could not apply custom styling: " + e.getMessage());
        }
    }
    
    /**
     * Style a text field with modern border and padding
     * 
     * @param textField The text field to style
     * @return The styled text field
     */
    public JTextField styleTextField(JTextField textField) {
        Border border = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.EXAMPLE_BUTTON_COLOR, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        );
        textField.setBorder(border);
        textField.setFont(UIConstants.DEFAULT_FONT);
        return textField;
    }
    
    /**
     * Style a primary action button (like Generate)
     * 
     * @param button The button to style
     * @return The styled button
     */
    public JButton stylePrimaryButton(JButton button) {
        button.setBackground(UIConstants.SUCCESS_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(UIConstants.BOLD_FONT);
        return button;
    }
    
    /**
     * Style a secondary action button
     * 
     * @param button The button to style
     * @return The styled button
     */
    public JButton styleSecondaryButton(JButton button) {
        button.setBackground(UIConstants.PRIMARY_BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(UIConstants.DEFAULT_FONT);
        return button;
    }
    
    /**
     * Style a warning/danger button (like Reset All)
     * 
     * @param button The button to style
     * @return The styled button
     */
    public JButton styleWarningButton(JButton button) {
        button.setBackground(UIConstants.ERROR_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(UIConstants.BOLD_FONT);
        return button;
    }
    
    /**
     * Style an info/help button
     * 
     * @param button The button to style
     * @return The styled button
     */
    public JButton styleInfoButton(JButton button) {
        button.setBackground(UIConstants.HELP_BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(UIConstants.DEFAULT_FONT);
        return button;
    }
    
    /**
     * Style an example button
     * 
     * @param button The button to style
     * @return The styled button
     */
    public JButton styleExampleButton(JButton button) {
        button.setBackground(UIConstants.EXAMPLE_BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(UIConstants.DEFAULT_FONT);
        return button;
    }
    
    /**
     * Create a titled border with consistent styling
     * 
     * @param title The title for the border
     * @return The styled titled border
     */
    public Border createTitledBorder(String title) {
        return BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), 
            title,
            TitledBorder.DEFAULT_JUSTIFICATION, 
            TitledBorder.DEFAULT_POSITION,
            UIConstants.BOLD_FONT
        );
    }
    
    /**
     * Create a panel with standard padding
     * 
     * @return A panel with standard empty border padding
     */
    public Border createStandardPadding() {
        return BorderFactory.createEmptyBorder(
            UIConstants.BORDER_PADDING, 
            UIConstants.BORDER_PADDING, 
            UIConstants.BORDER_PADDING, 
            UIConstants.BORDER_PADDING
        );
    }
    
    /**
     * Style a label for status display
     * 
     * @param label The label to style
     * @param isError Whether this is an error status
     * @return The styled label
     */
    public JLabel styleStatusLabel(JLabel label, boolean isError) {
        if (isError) {
            label.setForeground(UIConstants.ERROR_COLOR);
        } else {
            label.setForeground(UIConstants.SUCCESS_COLOR);
        }
        label.setFont(UIConstants.DEFAULT_FONT);
        return label;
    }
    
    /**
     * Style a label for configuration status
     * 
     * @param label The label to style
     * @return The styled label
     */
    public JLabel styleConfigStatusLabel(JLabel label) {
        label.setForeground(Color.GRAY);
        label.setFont(UIConstants.SMALL_ITALIC_FONT);
        return label;
    }
    
    /**
     * Style a preview label
     * 
     * @param label The label to style
     * @return The styled label
     */
    public JLabel stylePreviewLabel(JLabel label) {
        label.setForeground(Color.GRAY);
        label.setFont(UIConstants.PREVIEW_FONT);
        return label;
    }
    
    /**
     * Update preview label with content and appropriate color
     * 
     * @param label The preview label to update
     * @param content The content to display
     * @param hasContent Whether the label has meaningful content
     */
    public void updatePreviewLabel(JLabel label, String content, boolean hasContent) {
        label.setText(content);
        if (hasContent) {
            label.setForeground(UIConstants.SUCCESS_COLOR);
        } else {
            label.setForeground(Color.GRAY);
        }
    }
    
    /**
     * Style a text area for output display
     * 
     * @param textArea The text area to style
     * @return The styled text area
     */
    public JTextArea styleOutputArea(JTextArea textArea) {
        textArea.setEditable(false);
        textArea.setFont(UIConstants.MONOSPACE_FONT);
        textArea.setTabSize(4);
        return textArea;
    }
    
    /**
     * Style a progress bar
     * 
     * @param progressBar The progress bar to style
     * @return The styled progress bar
     */
    public JProgressBar styleProgressBar(JProgressBar progressBar) {
        progressBar.setStringPainted(true);
        progressBar.setString(UIConstants.PROGRESS_READY);
        return progressBar;
    }
    
    /**
     * Get validation color based on validation state
     * 
     * @param isValid Whether the input is valid
     * @param isEmpty Whether the input is empty
     * @param hasWarning Whether the input has warnings
     * @return The appropriate validation color
     */
    public Color getValidationColor(boolean isValid, boolean isEmpty, boolean hasWarning) {
        if (isEmpty) {
            return UIConstants.VALIDATION_EMPTY;
        } else if (!isValid) {
            return UIConstants.VALIDATION_ERROR;
        } else if (hasWarning) {
            return UIConstants.VALIDATION_WARNING;
        } else {
            return UIConstants.VALIDATION_VALID;
        }
    }
    
    // ===================
    // GETTERS AND SETTERS
    // ===================
    
    /**
     * Check if dark theme is enabled
     * 
     * @return true if dark theme is enabled
     */
    public boolean isDarkThemeEnabled() {
        return darkThemeEnabled;
    }
    
    /**
     * Enable or disable dark theme
     * 
     * @param darkThemeEnabled true to enable dark theme
     */
    public void setDarkThemeEnabled(boolean darkThemeEnabled) {
        this.darkThemeEnabled = darkThemeEnabled;
    }
    
    /**
     * Check if modern styling is enabled
     * 
     * @return true if modern styling is enabled
     */
    public boolean isModernStylingEnabled() {
        return modernStylingEnabled;
    }
    
    /**
     * Enable or disable modern styling
     * 
     * @param modernStylingEnabled true to enable modern styling
     */
    public void setModernStylingEnabled(boolean modernStylingEnabled) {
        this.modernStylingEnabled = modernStylingEnabled;
    }
}
