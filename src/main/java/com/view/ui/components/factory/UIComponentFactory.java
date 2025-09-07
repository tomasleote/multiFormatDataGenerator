package com.view.ui.components.factory;

import com.view.ui.constants.UIConstants;
import com.view.ui.theme.UIThemeManager;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * UIComponentFactory - Factory for creating styled UI components
 * 
 * This class provides factory methods for creating pre-styled UI components
 * with consistent appearance and behavior across the application.
 * 
 * @since Phase 1 Refactoring
 * @version 1.0
 */
public class UIComponentFactory {
    
    private static final UIThemeManager themeManager = UIThemeManager.getInstance();
    
    /**
     * Private constructor to prevent instantiation
     */
    private UIComponentFactory() {
        throw new AssertionError("UIComponentFactory is a utility class and should not be instantiated");
    }
    
    /**
     * Create a styled button with default styling
     * @param text The button text
     * @return A styled JButton
     */
    public static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(UIConstants.BUTTON_FONT);
        button.setPreferredSize(UIConstants.BUTTON_SIZE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        themeManager.applyThemeToComponent(button);
        return button;
    }
    
    /**
     * Create a styled button with tooltip
     * @param text The button text
     * @param tooltip The tooltip text
     * @return A styled JButton with tooltip
     */
    public static JButton createButton(String text, String tooltip) {
        JButton button = createButton(text);
        button.setToolTipText(tooltip);
        return button;
    }
    
    /**
     * Create a small button
     * @param text The button text
     * @return A small styled JButton
     */
    public static JButton createSmallButton(String text) {
        JButton button = createButton(text);
        button.setPreferredSize(UIConstants.SMALL_BUTTON_SIZE);
        return button;
    }
    
    /**
     * Create a large button
     * @param text The button text
     * @return A large styled JButton
     */
    public static JButton createLargeButton(String text) {
        JButton button = createButton(text);
        button.setPreferredSize(UIConstants.LARGE_BUTTON_SIZE);
        return button;
    }
    
    /**
     * Create a primary button (emphasized)
     * @param text The button text
     * @return A styled primary JButton
     */
    public static JButton createPrimaryButton(String text) {
        JButton button = createButton(text);
        button.setBackground(UIConstants.ACCENT_COLOR);
        button.setForeground(Color.WHITE);
        button.setFont(UIConstants.BUTTON_FONT.deriveFont(Font.BOLD));
        return button;
    }
    
    /**
     * Create a secondary button (normal)
     * @param text The button text
     * @return A styled secondary JButton
     */
    public static JButton createSecondaryButton(String text) {
        JButton button = createButton(text);
        return button;
    }
    
    /**
     * Create an icon button
     * @param icon The button icon
     * @param tooltip The tooltip text
     * @return A styled icon button
     */
    public static JButton createIconButton(Icon icon, String tooltip) {
        JButton button = new JButton(icon);
        button.setToolTipText(tooltip);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(30, 30));
        themeManager.applyThemeToComponent(button);
        return button;
    }
    
    /**
     * Create a styled text field
     * @param columns The number of columns
     * @return A styled JTextField
     */
    public static JTextField createTextField(int columns) {
        JTextField textField = new JTextField(columns);
        textField.setFont(UIConstants.DEFAULT_FONT);
        themeManager.applyThemeToComponent(textField);
        return textField;
    }
    
    /**
     * Create a styled text field with tooltip
     * @param columns The number of columns
     * @param tooltip The tooltip text
     * @return A styled JTextField with tooltip
     */
    public static JTextField createTextField(int columns, String tooltip) {
        JTextField textField = createTextField(columns);
        textField.setToolTipText(tooltip);
        return textField;
    }
    
    /**
     * Create a styled text area
     * @param rows The number of rows
     * @param columns The number of columns
     * @return A styled JTextArea
     */
    public static JTextArea createTextArea(int rows, int columns) {
        JTextArea textArea = new JTextArea(rows, columns);
        textArea.setFont(UIConstants.MONOSPACE_FONT);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        themeManager.applyThemeToComponent(textArea);
        return textArea;
    }
    
    /**
     * Create a scrollable text area
     * @param rows The number of rows
     * @param columns The number of columns
     * @return A JScrollPane containing a styled JTextArea
     */
    public static JScrollPane createScrollableTextArea(int rows, int columns) {
        JTextArea textArea = createTextArea(rows, columns);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        return scrollPane;
    }
    
    /**
     * Create a styled label
     * @param text The label text
     * @return A styled JLabel
     */
    public static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UIConstants.DEFAULT_FONT);
        themeManager.applyThemeToComponent(label);
        return label;
    }
    
    /**
     * Create a header label
     * @param text The label text
     * @return A styled header JLabel
     */
    public static JLabel createHeaderLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UIConstants.HEADER_FONT);
        themeManager.applyThemeToComponent(label);
        return label;
    }
    
    /**
     * Create a title label
     * @param text The label text
     * @return A styled title JLabel
     */
    public static JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UIConstants.TITLE_FONT);
        themeManager.applyThemeToComponent(label);
        return label;
    }
    
    /**
     * Create a styled combo box
     * @param items The items for the combo box
     * @param <T> The type of the items
     * @return A styled JComboBox
     */
    public static <T> JComboBox<T> createComboBox(T[] items) {
        JComboBox<T> comboBox = new JComboBox<>(items);
        comboBox.setFont(UIConstants.DEFAULT_FONT);
        themeManager.applyThemeToComponent(comboBox);
        return comboBox;
    }
    
    /**
     * Create a styled combo box with tooltip
     * @param items The items for the combo box
     * @param tooltip The tooltip text
     * @param <T> The type of the items
     * @return A styled JComboBox with tooltip
     */
    public static <T> JComboBox<T> createComboBox(T[] items, String tooltip) {
        JComboBox<T> comboBox = createComboBox(items);
        comboBox.setToolTipText(tooltip);
        return comboBox;
    }
    
    /**
     * Create a styled checkbox
     * @param text The checkbox text
     * @return A styled JCheckBox
     */
    public static JCheckBox createCheckBox(String text) {
        JCheckBox checkBox = new JCheckBox(text);
        checkBox.setFont(UIConstants.DEFAULT_FONT);
        checkBox.setFocusPainted(false);
        themeManager.applyThemeToComponent(checkBox);
        return checkBox;
    }
    
    /**
     * Create a styled checkbox with tooltip
     * @param text The checkbox text
     * @param tooltip The tooltip text
     * @return A styled JCheckBox with tooltip
     */
    public static JCheckBox createCheckBox(String text, String tooltip) {
        JCheckBox checkBox = createCheckBox(text);
        checkBox.setToolTipText(tooltip);
        return checkBox;
    }
    
    /**
     * Create a styled spinner
     * @param model The spinner model
     * @return A styled JSpinner
     */
    public static JSpinner createSpinner(SpinnerModel model) {
        JSpinner spinner = new JSpinner(model);
        spinner.setFont(UIConstants.DEFAULT_FONT);
        themeManager.applyThemeToComponent(spinner);
        return spinner;
    }
    
    /**
     * Create a batch size spinner
     * @return A styled JSpinner for batch size
     */
    public static JSpinner createBatchSizeSpinner() {
        SpinnerNumberModel model = new SpinnerNumberModel(
            UIConstants.DEFAULT_BATCH_SIZE, 
            UIConstants.MIN_BATCH_SIZE, 
            UIConstants.MAX_BATCH_SIZE, 
            UIConstants.BATCH_SIZE_STEP
        );
        JSpinner spinner = createSpinner(model);
        spinner.setToolTipText(UIConstants.BATCH_SIZE_TOOLTIP);
        return spinner;
    }
    
    /**
     * Create a styled progress bar
     * @return A styled JProgressBar
     */
    public static JProgressBar createProgressBar() {
        JProgressBar progressBar = new JProgressBar();
        progressBar.setPreferredSize(new Dimension(UIConstants.PROGRESS_BAR_WIDTH, UIConstants.PROGRESS_BAR_HEIGHT));
        progressBar.setStringPainted(true);
        themeManager.applyThemeToComponent(progressBar);
        return progressBar;
    }
    
    /**
     * Create a styled panel
     * @return A styled JPanel
     */
    public static JPanel createPanel() {
        JPanel panel = new JPanel();
        themeManager.applyThemeToComponent(panel);
        return panel;
    }
    
    /**
     * Create a panel with layout
     * @param layout The layout manager
     * @return A styled JPanel with the specified layout
     */
    public static JPanel createPanel(LayoutManager layout) {
        JPanel panel = createPanel();
        panel.setLayout(layout);
        return panel;
    }
    
    /**
     * Create a titled panel
     * @param title The panel title
     * @return A styled JPanel with titled border
     */
    public static JPanel createTitledPanel(String title) {
        JPanel panel = createPanel();
        panel.setBorder(createTitledBorder(title));
        return panel;
    }
    
    /**
     * Create a titled panel with layout
     * @param title The panel title
     * @param layout The layout manager
     * @return A styled JPanel with titled border and layout
     */
    public static JPanel createTitledPanel(String title, LayoutManager layout) {
        JPanel panel = createTitledPanel(title);
        panel.setLayout(layout);
        return panel;
    }
    
    /**
     * Create a styled titled border
     * @param title The border title
     * @return A styled TitledBorder
     */
    public static TitledBorder createTitledBorder(String title) {
        TitledBorder border = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER_COLOR, UIConstants.BORDER_THICKNESS),
            title
        );
        border.setTitleFont(UIConstants.HEADER_FONT);
        border.setTitleColor(UIConstants.TEXT_COLOR);
        return border;
    }
    
    /**
     * Create a styled separator
     * @return A styled JSeparator
     */
    public static JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.setForeground(UIConstants.BORDER_COLOR);
        return separator;
    }
    
    /**
     * Create a vertical separator
     * @return A vertical styled JSeparator
     */
    public static JSeparator createVerticalSeparator() {
        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
        separator.setForeground(UIConstants.BORDER_COLOR);
        return separator;
    }
    
    /**
     * Create a styled split pane
     * @param orientation The split orientation (JSplitPane.HORIZONTAL_SPLIT or JSplitPane.VERTICAL_SPLIT)
     * @param leftComponent The left/top component
     * @param rightComponent The right/bottom component
     * @return A styled JSplitPane
     */
    public static JSplitPane createSplitPane(int orientation, Component leftComponent, Component rightComponent) {
        JSplitPane splitPane = new JSplitPane(orientation, leftComponent, rightComponent);
        splitPane.setDividerSize(UIConstants.SPLIT_PANE_DIVIDER_SIZE);
        splitPane.setDividerLocation(UIConstants.SPLIT_PANE_DIVIDER_LOCATION);
        splitPane.setContinuousLayout(true);
        themeManager.applyThemeToComponent(splitPane);
        return splitPane;
    }
    
    /**
     * Create a styled scroll pane
     * @param component The component to make scrollable
     * @return A styled JScrollPane
     */
    public static JScrollPane createScrollPane(Component component) {
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setBackground(UIConstants.BACKGROUND_COLOR);
        return scrollPane;
    }
    
    /**
     * Create a status label
     * @return A styled status JLabel
     */
    public static JLabel createStatusLabel() {
        JLabel label = createLabel(UIConstants.STATUS_READY);
        label.setFont(UIConstants.SMALL_FONT);
        label.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        return label;
    }
    
    /**
     * Create a menu bar
     * @return A styled JMenuBar
     */
    public static JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        themeManager.applyThemeToComponent(menuBar);
        return menuBar;
    }
    
    /**
     * Create a menu
     * @param text The menu text
     * @return A styled JMenu
     */
    public static JMenu createMenu(String text) {
        JMenu menu = new JMenu(text);
        menu.setFont(UIConstants.DEFAULT_FONT);
        themeManager.applyThemeToComponent(menu);
        return menu;
    }
    
    /**
     * Create a menu item
     * @param text The menu item text
     * @return A styled JMenuItem
     */
    public static JMenuItem createMenuItem(String text) {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.setFont(UIConstants.DEFAULT_FONT);
        themeManager.applyThemeToComponent(menuItem);
        return menuItem;
    }
    
    /**
     * Create a toolbar
     * @return A styled JToolBar
     */
    public static JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBorderPainted(false);
        themeManager.applyThemeToComponent(toolBar);
        return toolBar;
    }
    
    /**
     * Apply standard padding to a component
     * @param component The component to add padding to
     * @return The component with padding
     */
    public static JComponent applyPadding(JComponent component) {
        component.setBorder(BorderFactory.createEmptyBorder(
            UIConstants.DEFAULT_PADDING,
            UIConstants.DEFAULT_PADDING,
            UIConstants.DEFAULT_PADDING,
            UIConstants.DEFAULT_PADDING
        ));
        return component;
    }
    
    /**
     * Apply custom padding to a component
     * @param component The component to add padding to
     * @param top Top padding
     * @param left Left padding
     * @param bottom Bottom padding
     * @param right Right padding
     * @return The component with padding
     */
    public static JComponent applyPadding(JComponent component, int top, int left, int bottom, int right) {
        component.setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
        return component;
    }
}
