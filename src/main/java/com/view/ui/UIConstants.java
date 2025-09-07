package com.view.ui;

import java.awt.*;

/**
 * UIConstants - Centralized UI constants and resources
 * 
 * Purpose: Store all UI-related constants to eliminate magic numbers
 * and provide consistent styling across the application.
 * 
 * @author Multi-Format Data Generator Team
 * @version 1.0
 */
public final class UIConstants {
    
    // Prevent instantiation
    private UIConstants() {
        throw new UnsupportedOperationException("Constants class cannot be instantiated");
    }
    
    // ===================
    // WINDOW DIMENSIONS
    // ===================
    public static final int MAIN_WINDOW_WIDTH = 1000;
    public static final int MAIN_WINDOW_HEIGHT = 700;
    public static final int DOC_WINDOW_WIDTH = 800;
    public static final int DOC_WINDOW_HEIGHT = 600;
    
    // ===================
    // COMPONENT SIZES
    // ===================
    public static final int TEMPLATE_FIELD_COLUMNS = 25;
    public static final int OUTPUT_AREA_ROWS = 15;
    public static final int OUTPUT_AREA_COLUMNS = 60;
    public static final int GENERATE_BUTTON_WIDTH = 120;
    public static final int GENERATE_BUTTON_HEIGHT = 35;
    
    // ===================
    // SPINNER LIMITS
    // ===================
    public static final int BATCH_SIZE_MIN = 1;
    public static final int BATCH_SIZE_MAX = 10000;
    public static final int BATCH_SIZE_DEFAULT = 100;
    public static final int BATCH_SIZE_STEP = 50;
    
    // ===================
    // EVALUATOR LIMITS
    // ===================
    public static final Integer[] EVALUATOR_COUNT_OPTIONS = {0, 1, 2, 3, 4, 5};
    public static final int MAX_EVALUATORS = 5;
    
    // ===================
    // EXPORT FORMATS
    // ===================
    public static final String[] EXPORT_FORMATS = {"CSV", "TXT", "JSON"};
    public static final String DEFAULT_EXPORT_FORMAT = "CSV";
    
    // ===================
    // COLORS - Dark Theme
    // ===================
    public static final Color DARK_CONTROL = new Color(60, 63, 65);
    public static final Color DARK_INFO = new Color(60, 63, 65);
    public static final Color DARK_BASE = new Color(18, 30, 49);
    public static final Color DARK_LIGHT_BACKGROUND = new Color(60, 63, 65);
    public static final Color DARK_TEXT = new Color(230, 230, 230);
    public static final Color DARK_SELECTED_TEXT = new Color(255, 255, 255);
    public static final Color DARK_SELECTION_BACKGROUND = new Color(104, 93, 156);
    public static final Color DARK_DISABLED_TEXT = new Color(128, 128, 128);
    
    // ===================
    // COLORS - UI Elements
    // ===================
    public static final Color PRIMARY_BUTTON_COLOR = new Color(70, 130, 180);
    public static final Color SUCCESS_COLOR = new Color(34, 139, 34);
    public static final Color WARNING_COLOR = new Color(248, 187, 0);
    public static final Color ERROR_COLOR = new Color(220, 20, 60);
    public static final Color INFO_COLOR = new Color(66, 139, 221);
    public static final Color EXAMPLE_BUTTON_COLOR = new Color(100, 149, 237);
    public static final Color HELP_BUTTON_COLOR = new Color(70, 130, 180);
    
    // Validation colors
    public static final Color VALIDATION_VALID = new Color(34, 139, 34);
    public static final Color VALIDATION_WARNING = new Color(255, 165, 0);
    public static final Color VALIDATION_ERROR = new Color(220, 20, 60);
    public static final Color VALIDATION_EMPTY = Color.GRAY;
    
    // ===================
    // FONTS
    // ===================
    public static final Font DEFAULT_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
    public static final Font BOLD_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 12);
    public static final Font LARGE_BOLD_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 14);
    public static final Font MONOSPACE_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 12);
    public static final Font SMALL_ITALIC_FONT = new Font(Font.SANS_SERIF, Font.ITALIC, 10);
    public static final Font SMALL_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 10);
    public static final Font PREVIEW_FONT = new Font(Font.MONOSPACED, Font.ITALIC, 11);
    
    // ===================
    // SPACING & PADDING
    // ===================
    public static final int BORDER_PADDING = 15;
    public static final int COMPONENT_SPACING = 10;
    public static final int PANEL_SPACING = 5;
    public static final int GENERATOR_PANEL_SPACING_H = 15;
    public static final int GENERATOR_PANEL_SPACING_V = 15;
    public static final int GRID_BAG_INSET = 5;
    
    // ===================
    // LAYOUT DIMENSIONS
    // ===================
    public static final int SPLIT_PANE_DIVIDER_LOCATION = 450;
    public static final double SPLIT_PANE_RESIZE_WEIGHT = 0.7;
    public static final int SPLIT_PANE_DIVIDER_SIZE = 12;
    public static final int GENERATOR_CONTAINER_WIDTH = 1200;
    public static final int GENERATOR_CONTAINER_HEIGHT_MIN = 280;
    public static final int PANELS_PER_ROW_ESTIMATE = 3;
    public static final int PANEL_HEIGHT_ESTIMATE = 280;
    
    // ===================
    // GENERATION SETTINGS
    // ===================
    public static final int PREVIEW_SAMPLE_SIZE = 5;
    public static final String PREVIEW_SEPARATOR = "=== PREVIEW (5 samples) ===";
    public static final String PREVIEW_END = "=== END PREVIEW ===";
    public static final String GENERATION_SEPARATOR = "=" .repeat(60);
    
    // ===================
    // UI STRINGS
    // ===================
    public static final String WINDOW_TITLE = "Synthetic Data Generator - Phase 3 Complete";
    public static final String TEMPLATE_TOOLTIP = "Enter template format like {0}, {0}-{1}, etc.";
    public static final String APPLY_TEMPLATE_TOOLTIP = "Apply the template format and create generator panels";
    public static final String EVALUATOR_TOOLTIP = "Number of additional evaluator filters to add";
    public static final String CLEAR_OUTPUT_TOOLTIP = "Clear the output display area";
    public static final String GENERATE_TOOLTIP = "Generate synthetic data based on current configuration";
    public static final String BATCH_SIZE_TOOLTIP = "Number of records to generate";
    public static final String EXPORT_TOOLTIP = "Export generated data to file";
    public static final String EXPORT_FORMAT_TOOLTIP = "Select export format";
    public static final String AUTO_SAVE_TOOLTIP = "Automatically save configuration changes";
    public static final String RESET_TOOLTIP = "Clear all inputs and reset to default state";
    public static final String HELP_TOOLTIP = "Open documentation and examples";
    public static final String SAVE_CONFIG_TOOLTIP = "Save current configuration to JSON file";
    public static final String LOAD_CONFIG_TOOLTIP = "Load configuration from JSON file";
    public static final String PREVIEW_BUTTON_TOOLTIP = "Generate a small preview sample";
    
    // Panel titles
    public static final String TEMPLATE_CONFIG_TITLE = "Template Configuration";
    public static final String GENERATOR_CONFIG_TITLE = "Generator Configuration";
    public static final String GENERATED_OUTPUT_TITLE = "Generated Output";
    public static final String GENERATION_CONTROL_TITLE = "Generation Control";
    public static final String EXPORT_OPTIONS_TITLE = "Export Options";
    public static final String LOAD_EXAMPLE_TITLE = "Load Example";
    
    // Button texts
    public static final String APPLY_TEMPLATE_TEXT = "Apply Template";
    public static final String GENERATE_DATA_TEXT = "Generate Data";
    public static final String CLEAR_OUTPUT_TEXT = "Clear Output";
    public static final String SAVE_CONFIG_TEXT = "Save Config";
    public static final String LOAD_CONFIG_TEXT = "Load Config";
    public static final String EXPORT_DATA_TEXT = "Export Data";
    public static final String RESET_ALL_TEXT = "Reset All";
    public static final String HELP_DOCS_TEXT = "Help & Docs";
    public static final String PREVIEW_SAMPLE_TEXT = "Preview Sample";
    public static final String AUTO_SAVE_TEXT = "Auto-save config";
    
    // Example button texts
    public static final String BSN_EXAMPLE_TEXT = "Dutch BSN";
    public static final String LICENSE_EXAMPLE_TEXT = "License Plate";
    public static final String COMPLEX_EXAMPLE_TEXT = "Complex Multi-Gen";
    
    // Status messages
    public static final String STATUS_READY = "Ready to generate data";
    public static final String STATUS_GENERATING = "Generating data...";
    public static final String STATUS_COMPLETE = "Generation complete";
    public static final String STATUS_OUTPUT_CLEARED = "Output cleared";
    public static final String STATUS_PREVIEW_GENERATING = "Generating preview...";
    public static final String STATUS_PREVIEW_COMPLETE = "Preview complete";
    public static final String STATUS_CONFIG_SAVED = "Configuration saved";
    public static final String STATUS_CONFIG_LOADED = "Configuration loaded successfully";
    public static final String STATUS_RESET = "All inputs reset to default state";
    
    // Progress bar states
    public static final String PROGRESS_READY = "Ready";
    public static final String PROGRESS_GENERATING = "Generating...";
    public static final String PROGRESS_PREVIEW = "Preview...";
    
    // Default labels
    public static final String PREVIEW_DEFAULT = "Template preview will appear here";
    public static final String CONFIG_STATUS_DEFAULT = "No configuration loaded";
    
    // ===================
    // VALIDATION MESSAGES
    // ===================
    public static final String ERROR_EMPTY_TEMPLATE = "Please enter a template format (e.g., {0} or {0}-{1})";
    public static final String ERROR_INVALID_TEMPLATE = "Template format must contain placeholders like {0}, {1}, etc.";
    public static final String ERROR_NO_GENERATORS = "Template format must contain at least one generator placeholder (e.g., {0})";
    public static final String ERROR_NO_CONFIG = "No generators configured. Please apply a template first.";
    public static final String ERROR_GENERATOR_TYPE = "Generator type not selected for panel";
    
    // Dialog titles
    public static final String DIALOG_INVALID_TEMPLATE = "Invalid Template";
    public static final String DIALOG_INVALID_FORMAT = "Invalid Template Format";
    public static final String DIALOG_NO_GENERATORS = "No Generators Found";
    public static final String DIALOG_TEMPLATE_APPLIED = "Template Applied";
    public static final String DIALOG_RESET_CONFIRM = "Reset All";
    public static final String DIALOG_EXPORT_ERROR = "Export Error";
    
    // Dialog messages
    public static final String MSG_TEMPLATE_APPLIED = "Template applied successfully!";
    public static final String MSG_RESET_CONFIRM = "Are you sure you want to reset all inputs?\nThis will clear the template, generators, and output.";
    public static final String MSG_UNSUPPORTED_FORMAT = "Unsupported export format: ";
    public static final String MSG_EXPORT_FAILED = "Failed to export data: ";
    public static final String MSG_EXPORT_SUCCESS = "Data exported successfully as ";
    
    // ===================
    // DOCUMENTATION CONTENT
    // ===================
    public static final String DOC_WINDOW_TITLE = "Multi-Format Data Generator - Documentation";
    public static final String DOC_TAB_OVERVIEW = "Overview";
    public static final String DOC_TAB_GENERATORS = "Generator Types";
    public static final String DOC_TAB_EXAMPLES = "Examples";
    public static final String DOC_TAB_VALIDATION = "Field Validation";
    
    // ===================
    // NIMBUS THEME KEYS
    // ===================
    public static final String NIMBUS_CONTROL = "control";
    public static final String NIMBUS_INFO = "info";
    public static final String NIMBUS_BASE = "nimbusBase";
    public static final String NIMBUS_ALERT_YELLOW = "nimbusAlertYellow";
    public static final String NIMBUS_DISABLED_TEXT = "nimbusDisabledText";
    public static final String NIMBUS_FOCUS = "nimbusFocus";
    public static final String NIMBUS_GREEN = "nimbusGreen";
    public static final String NIMBUS_INFO_BLUE = "nimbusInfoBlue";
    public static final String NIMBUS_LIGHT_BACKGROUND = "nimbusLightBackground";
    public static final String NIMBUS_ORANGE = "nimbusOrange";
    public static final String NIMBUS_RED = "nimbusRed";
    public static final String NIMBUS_SELECTED_TEXT = "nimbusSelectedText";
    public static final String NIMBUS_SELECTION_BACKGROUND = "nimbusSelectionBackground";
    public static final String NIMBUS_TEXT = "text";
    public static final String NIMBUS_LAF = "javax.swing.plaf.nimbus.NimbusLookAndFeel";
    
    // Button styling keys
    public static final String BUTTON_BACKGROUND = "Button.background";
    public static final String BUTTON_FOREGROUND = "Button.foreground";
    public static final String BUTTON_FONT = "Button.font";
}
