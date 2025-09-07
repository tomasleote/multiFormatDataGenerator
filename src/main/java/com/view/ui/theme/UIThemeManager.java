package com.view.ui.theme;

import com.view.ui.constants.UIConstants;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.Enumeration;

/**
 * UIThemeManager - Manages UI themes and Look & Feel settings
 * 
 * This class is responsible for setting up and managing the application's
 * visual theme, including dark mode support and custom styling.
 * 
 * @since Phase 1 Refactoring
 * @version 1.0
 */
public class UIThemeManager {
    
    private static UIThemeManager instance;
    private ThemeType currentTheme = ThemeType.DARK;
    
    public enum ThemeType {
        DARK,
        LIGHT,
        SYSTEM_DEFAULT
    }
    
    /**
     * Private constructor for singleton pattern
     */
    private UIThemeManager() {
        // Initialize with default theme
    }
    
    /**
     * Get the singleton instance of UIThemeManager
     * @return The UIThemeManager instance
     */
    public static UIThemeManager getInstance() {
        if (instance == null) {
            instance = new UIThemeManager();
        }
        return instance;
    }
    
    /**
     * Set up the modern dark theme using Nimbus Look & Feel
     */
    public void setupModernDarkTheme() {
        try {
            // Set Nimbus Look & Feel
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            
            // Configure Nimbus colors for dark theme
            UIManager.put("control", UIConstants.BACKGROUND_COLOR);
            UIManager.put("info", UIConstants.BACKGROUND_COLOR);
            UIManager.put("nimbusBase", UIConstants.NIMBUS_BASE);
            UIManager.put("nimbusAlertYellow", UIConstants.WARNING_COLOR);
            UIManager.put("nimbusDisabledText", new Color(128, 128, 128));
            UIManager.put("nimbusFocus", UIConstants.NIMBUS_FOCUS);
            UIManager.put("nimbusGreen", UIConstants.SUCCESS_COLOR);
            UIManager.put("nimbusInfoBlue", UIConstants.INFO_COLOR);
            UIManager.put("nimbusLightBackground", UIConstants.NIMBUS_LIGHT_BACKGROUND);
            UIManager.put("nimbusOrange", new Color(191, 98, 4));
            UIManager.put("nimbusRed", UIConstants.ERROR_COLOR);
            UIManager.put("nimbusSelectedText", Color.WHITE);
            UIManager.put("nimbusSelectionBackground", UIConstants.NIMBUS_SELECTION_BACKGROUND);
            UIManager.put("text", UIConstants.TEXT_COLOR);
            
            // Additional component-specific customizations
            setupComponentDefaults();
            
            currentTheme = ThemeType.DARK;
            
        } catch (Exception e) {
            System.err.println("Failed to set dark theme: " + e.getMessage());
            // Fall back to system default
            setSystemDefaultTheme();
        }
    }
    
    /**
     * Set up light theme
     */
    public void setupLightTheme() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Configure light theme colors
            UIManager.put("control", Color.WHITE);
            UIManager.put("info", new Color(242, 242, 242));
            UIManager.put("nimbusBase", new Color(51, 98, 140));
            UIManager.put("nimbusAlertYellow", new Color(255, 220, 35));
            UIManager.put("nimbusDisabledText", Color.GRAY);
            UIManager.put("nimbusFocus", new Color(115, 164, 209));
            UIManager.put("nimbusGreen", new Color(176, 179, 50));
            UIManager.put("nimbusInfoBlue", new Color(47, 92, 180));
            UIManager.put("nimbusLightBackground", Color.WHITE);
            UIManager.put("nimbusOrange", new Color(191, 98, 4));
            UIManager.put("nimbusRed", new Color(169, 46, 34));
            UIManager.put("nimbusSelectedText", Color.WHITE);
            UIManager.put("nimbusSelectionBackground", new Color(57, 105, 138));
            UIManager.put("text", Color.BLACK);
            
            setupComponentDefaults();
            currentTheme = ThemeType.LIGHT;
            
        } catch (Exception e) {
            System.err.println("Failed to set light theme: " + e.getMessage());
            setSystemDefaultTheme();
        }
    }
    
    /**
     * Set system default theme
     */
    public void setSystemDefaultTheme() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            currentTheme = ThemeType.SYSTEM_DEFAULT;
        } catch (Exception e) {
            System.err.println("Failed to set system default theme: " + e.getMessage());
        }
    }
    
    /**
     * Setup component-specific defaults
     */
    private void setupComponentDefaults() {
        // Button defaults
        UIManager.put("Button.font", new FontUIResource(UIConstants.BUTTON_FONT));
        UIManager.put("Button.background", UIConstants.PANEL_BACKGROUND_COLOR);
        UIManager.put("Button.foreground", UIConstants.TEXT_COLOR);
        UIManager.put("Button.focus", UIConstants.ACCENT_COLOR);
        
        // TextField defaults
        UIManager.put("TextField.font", new FontUIResource(UIConstants.DEFAULT_FONT));
        UIManager.put("TextField.background", UIConstants.DARK_BACKGROUND_COLOR);
        UIManager.put("TextField.foreground", UIConstants.TEXT_COLOR);
        UIManager.put("TextField.caretForeground", UIConstants.TEXT_COLOR);
        
        // TextArea defaults
        UIManager.put("TextArea.font", new FontUIResource(UIConstants.MONOSPACE_FONT));
        UIManager.put("TextArea.background", UIConstants.DARK_BACKGROUND_COLOR);
        UIManager.put("TextArea.foreground", UIConstants.TEXT_COLOR);
        UIManager.put("TextArea.caretForeground", UIConstants.TEXT_COLOR);
        
        // ComboBox defaults
        UIManager.put("ComboBox.font", new FontUIResource(UIConstants.DEFAULT_FONT));
        UIManager.put("ComboBox.background", UIConstants.PANEL_BACKGROUND_COLOR);
        UIManager.put("ComboBox.foreground", UIConstants.TEXT_COLOR);
        UIManager.put("ComboBox.selectionBackground", UIConstants.ACCENT_COLOR);
        UIManager.put("ComboBox.selectionForeground", Color.WHITE);
        
        // Label defaults
        UIManager.put("Label.font", new FontUIResource(UIConstants.DEFAULT_FONT));
        UIManager.put("Label.foreground", UIConstants.TEXT_COLOR);
        
        // Panel defaults
        UIManager.put("Panel.background", UIConstants.BACKGROUND_COLOR);
        
        // ScrollPane defaults
        UIManager.put("ScrollPane.background", UIConstants.BACKGROUND_COLOR);
        UIManager.put("ScrollBar.background", UIConstants.PANEL_BACKGROUND_COLOR);
        
        // TitledBorder defaults
        UIManager.put("TitledBorder.font", new FontUIResource(UIConstants.HEADER_FONT));
        UIManager.put("TitledBorder.titleColor", UIConstants.TEXT_COLOR);
        
        // ProgressBar defaults
        UIManager.put("ProgressBar.font", new FontUIResource(UIConstants.SMALL_FONT));
        UIManager.put("ProgressBar.foreground", UIConstants.ACCENT_COLOR);
        UIManager.put("ProgressBar.background", UIConstants.PANEL_BACKGROUND_COLOR);
        
        // Spinner defaults
        UIManager.put("Spinner.font", new FontUIResource(UIConstants.DEFAULT_FONT));
        UIManager.put("Spinner.background", UIConstants.PANEL_BACKGROUND_COLOR);
        UIManager.put("Spinner.foreground", UIConstants.TEXT_COLOR);
        
        // CheckBox defaults
        UIManager.put("CheckBox.font", new FontUIResource(UIConstants.DEFAULT_FONT));
        UIManager.put("CheckBox.foreground", UIConstants.TEXT_COLOR);
        
        // ToolTip defaults
        UIManager.put("ToolTip.font", new FontUIResource(UIConstants.SMALL_FONT));
        UIManager.put("ToolTip.background", UIConstants.PANEL_BACKGROUND_COLOR);
        UIManager.put("ToolTip.foreground", UIConstants.TEXT_COLOR);
        
        // SplitPane defaults
        UIManager.put("SplitPane.background", UIConstants.BACKGROUND_COLOR);
        UIManager.put("SplitPane.dividerSize", UIConstants.SPLIT_PANE_DIVIDER_SIZE);
        
        // Menu defaults
        UIManager.put("Menu.font", new FontUIResource(UIConstants.DEFAULT_FONT));
        UIManager.put("MenuItem.font", new FontUIResource(UIConstants.DEFAULT_FONT));
        UIManager.put("MenuBar.background", UIConstants.PANEL_BACKGROUND_COLOR);
        UIManager.put("Menu.foreground", UIConstants.TEXT_COLOR);
        UIManager.put("MenuItem.background", UIConstants.PANEL_BACKGROUND_COLOR);
        UIManager.put("MenuItem.foreground", UIConstants.TEXT_COLOR);
    }
    
    /**
     * Apply theme to a specific component and its children
     * @param component The component to apply the theme to
     */
    public void applyThemeToComponent(Component component) {
        if (component == null) return;
        
        // Apply theme based on component type
        if (component instanceof JButton) {
            styleButton((JButton) component);
        } else if (component instanceof JTextField) {
            styleTextField((JTextField) component);
        } else if (component instanceof JTextArea) {
            styleTextArea((JTextArea) component);
        } else if (component instanceof JLabel) {
            styleLabel((JLabel) component);
        } else if (component instanceof JPanel) {
            stylePanel((JPanel) component);
        }
        
        // Recursively apply to children if it's a container
        if (component instanceof Container) {
            Container container = (Container) component;
            for (Component child : container.getComponents()) {
                applyThemeToComponent(child);
            }
        }
    }
    
    /**
     * Style a button component
     */
    private void styleButton(JButton button) {
        button.setBackground(UIConstants.PANEL_BACKGROUND_COLOR);
        button.setForeground(UIConstants.TEXT_COLOR);
        button.setFont(UIConstants.BUTTON_FONT);
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    /**
     * Style a text field component
     */
    private void styleTextField(JTextField textField) {
        textField.setBackground(UIConstants.DARK_BACKGROUND_COLOR);
        textField.setForeground(UIConstants.TEXT_COLOR);
        textField.setCaretColor(UIConstants.TEXT_COLOR);
        textField.setFont(UIConstants.DEFAULT_FONT);
    }
    
    /**
     * Style a text area component
     */
    private void styleTextArea(JTextArea textArea) {
        textArea.setBackground(UIConstants.DARK_BACKGROUND_COLOR);
        textArea.setForeground(UIConstants.TEXT_COLOR);
        textArea.setCaretColor(UIConstants.TEXT_COLOR);
        textArea.setFont(UIConstants.MONOSPACE_FONT);
    }
    
    /**
     * Style a label component
     */
    private void styleLabel(JLabel label) {
        label.setForeground(UIConstants.TEXT_COLOR);
        label.setFont(UIConstants.DEFAULT_FONT);
    }
    
    /**
     * Style a panel component
     */
    private void stylePanel(JPanel panel) {
        panel.setBackground(UIConstants.BACKGROUND_COLOR);
    }
    
    /**
     * Switch between themes
     * @param themeType The theme to switch to
     */
    public void switchTheme(ThemeType themeType) {
        switch (themeType) {
            case DARK:
                setupModernDarkTheme();
                break;
            case LIGHT:
                setupLightTheme();
                break;
            case SYSTEM_DEFAULT:
                setSystemDefaultTheme();
                break;
        }
    }
    
    /**
     * Get the current theme type
     * @return The current theme
     */
    public ThemeType getCurrentTheme() {
        return currentTheme;
    }
    
    /**
     * Update all windows with the current theme
     */
    public void updateAllWindows() {
        for (Window window : Window.getWindows()) {
            SwingUtilities.updateComponentTreeUI(window);
            applyThemeToComponent(window);
        }
    }
}
