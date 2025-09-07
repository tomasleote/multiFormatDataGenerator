package com.view.ui.constants;

import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;

/**
 * UIConstants - Central repository for all UI constants
 * 
 * This class holds all UI-related constants including colors, fonts, 
 * dimensions, and magic numbers that were previously scattered throughout
 * the GeneratorUI class.
 * 
 * @since Phase 1 Refactoring
 * @version 1.0
 */
public final class UIConstants {
    
    // Private constructor to prevent instantiation
    private UIConstants() {
        throw new AssertionError("UIConstants is a utility class and should not be instantiated");
    }
    
    // ============== Window Constants ==============
    public static final String APPLICATION_TITLE = "Synthetic Data Generator";
    public static final int WINDOW_WIDTH = 1200;
    public static final int WINDOW_HEIGHT = 800;
    public static final int MIN_WINDOW_WIDTH = 800;
    public static final int MIN_WINDOW_HEIGHT = 600;
    
    // ============== Color Scheme ==============
    // Dark theme colors
    public static final Color BACKGROUND_COLOR = new Color(60, 63, 65);
    public static final Color DARK_BACKGROUND_COLOR = new Color(43, 43, 43);
    public static final Color PANEL_BACKGROUND_COLOR = new Color(50, 53, 55);
    public static final Color TEXT_COLOR = new Color(230, 230, 230);
    public static final Color ACCENT_COLOR = new Color(104, 93, 156);
    public static final Color SUCCESS_COLOR = new Color(176, 179, 50);
    public static final Color WARNING_COLOR = new Color(248, 187, 0);
    public static final Color ERROR_COLOR = new Color(169, 46, 34);
    public static final Color INFO_COLOR = new Color(66, 139, 221);
    public static final Color BORDER_COLOR = new Color(80, 83, 85);
    
    // Nimbus theme colors
    public static final Color NIMBUS_BASE = new Color(18, 30, 49);
    public static final Color NIMBUS_FOCUS = new Color(115, 164, 209);
    public static final Color NIMBUS_LIGHT_BACKGROUND = new Color(60, 63, 65);
    public static final Color NIMBUS_SELECTION_BACKGROUND = new Color(104, 93, 156);
    
    // ============== Fonts ==============
    public static final Font DEFAULT_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font MONOSPACE_FONT = new Font("Consolas", Font.PLAIN, 12);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 10);
    
    // ============== Component Dimensions ==============
    // Buttons
    public static final Dimension BUTTON_SIZE = new Dimension(120, 30);
    public static final Dimension SMALL_BUTTON_SIZE = new Dimension(80, 25);
    public static final Dimension LARGE_BUTTON_SIZE = new Dimension(150, 35);
    
    // Text fields
    public static final int TEXT_FIELD_COLUMNS = 25;
    public static final int SMALL_TEXT_FIELD_COLUMNS = 10;
    public static final int LARGE_TEXT_FIELD_COLUMNS = 40;
    
    // Panels
    public static final Dimension GENERATOR_PANEL_SIZE = new Dimension(800, 150);
    public static final Dimension SIDE_PANEL_SIZE = new Dimension(250, 600);
    
    // Text areas
    public static final int OUTPUT_AREA_ROWS = 20;
    public static final int OUTPUT_AREA_COLUMNS = 60;
    
    // ============== Spacing and Padding ==============
    public static final int DEFAULT_PADDING = 10;
    public static final int SMALL_PADDING = 5;
    public static final int LARGE_PADDING = 15;
    public static final int COMPONENT_SPACING = 8;
    public static final int SECTION_SPACING = 20;
    
    // ============== Border Constants ==============
    public static final int BORDER_THICKNESS = 1;
    public static final int ROUNDED_BORDER_RADIUS = 5;
    
    // ============== Animation and Timing ==============
    public static final int ANIMATION_DELAY = 50; // milliseconds
    public static final int TOOLTIP_DELAY = 500; // milliseconds
    public static final int STATUS_MESSAGE_DURATION = 3000; // milliseconds
    
    // ============== Progress Bar ==============
    public static final int PROGRESS_BAR_HEIGHT = 20;
    public static final int PROGRESS_BAR_WIDTH = 300;
    
    // ============== Spinner Constants ==============
    public static final int MIN_BATCH_SIZE = 1;
    public static final int MAX_BATCH_SIZE = 100000;
    public static final int DEFAULT_BATCH_SIZE = 1000;
    public static final int BATCH_SIZE_STEP = 100;
    
    // ============== ComboBox Constants ==============
    public static final int MAX_EVALUATORS = 5;
    public static final String[] EXPORT_FORMATS = {"CSV", "TXT", "JSON", "XML"};
    
    // ============== Icons and Images ==============
    public static final String ICON_PATH = "/icons/";
    public static final String LOGO_PATH = "/images/logo.png";
    
    // ============== Messages and Labels ==============
    public static final String GENERATE_BUTTON_TEXT = "Generate";
    public static final String CLEAR_BUTTON_TEXT = "Clear Output";
    public static final String APPLY_TEMPLATE_TEXT = "Apply Template";
    public static final String SAVE_CONFIG_TEXT = "Save Configuration";
    public static final String LOAD_CONFIG_TEXT = "Load Configuration";
    public static final String EXPORT_BUTTON_TEXT = "Export";
    public static final String RESET_ALL_TEXT = "Reset All";
    public static final String DOCUMENTATION_TEXT = "Help";
    
    // ============== Tooltips ==============
    public static final String TEMPLATE_TOOLTIP = "Enter template format like {0}, {0}-{1}, etc.";
    public static final String EVALUATOR_TOOLTIP = "Number of additional evaluator filters to add";
    public static final String BATCH_SIZE_TOOLTIP = "Number of values to generate in a single batch";
    public static final String EXPORT_FORMAT_TOOLTIP = "Select the format for exporting generated data";
    public static final String AUTO_SAVE_TOOLTIP = "Automatically save configuration on changes";
    
    // ============== Status Messages ==============
    public static final String STATUS_READY = "Ready";
    public static final String STATUS_GENERATING = "Generating...";
    public static final String STATUS_COMPLETE = "Generation Complete";
    public static final String STATUS_ERROR = "Error occurred";
    public static final String STATUS_SAVED = "Configuration Saved";
    public static final String STATUS_LOADED = "Configuration Loaded";
    
    // ============== Split Pane Constants ==============
    public static final double SPLIT_PANE_DIVIDER_LOCATION = 0.7;
    public static final int SPLIT_PANE_DIVIDER_SIZE = 10;
    
    // ============== Miscellaneous ==============
    public static final int MAX_OUTPUT_LINES = 10000;
    public static final int DEFAULT_INDENTATION = 4;
    public static final String LINE_SEPARATOR = System.lineSeparator();
}
