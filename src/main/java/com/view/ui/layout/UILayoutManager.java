package com.view.ui.layout;

import com.view.ui.constants.UIConstants;
import com.view.WrapLayout;

import javax.swing.*;
import java.awt.*;

/**
 * UILayoutManager - Manages layout configurations and panel arrangements
 * 
 * This class provides utility methods for creating and configuring
 * various layout managers and arranging components within panels.
 * 
 * @since Phase 1 Refactoring
 * @version 1.0
 */
public class UILayoutManager {
    
    /**
     * Private constructor to prevent instantiation
     */
    private UILayoutManager() {
        throw new AssertionError("UILayoutManager is a utility class and should not be instantiated");
    }
    
    /**
     * Create a north panel layout for the main window
     * @return A JPanel configured for the north region
     */
    public static JPanel createNorthPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(
            UIConstants.DEFAULT_PADDING,
            UIConstants.DEFAULT_PADDING,
            UIConstants.SMALL_PADDING,
            UIConstants.DEFAULT_PADDING
        ));
        return panel;
    }
    
    /**
     * Create a center panel layout for the main window
     * @return A JPanel configured for the center region
     */
    public static JPanel createCenterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(UIConstants.COMPONENT_SPACING, UIConstants.COMPONENT_SPACING));
        panel.setBorder(BorderFactory.createEmptyBorder(
            UIConstants.SMALL_PADDING,
            UIConstants.DEFAULT_PADDING,
            UIConstants.SMALL_PADDING,
            UIConstants.DEFAULT_PADDING
        ));
        return panel;
    }
    
    /**
     * Create a south panel layout for the main window
     * @return A JPanel configured for the south region
     */
    public static JPanel createSouthPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(UIConstants.COMPONENT_SPACING, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(
            UIConstants.SMALL_PADDING,
            UIConstants.DEFAULT_PADDING,
            UIConstants.DEFAULT_PADDING,
            UIConstants.DEFAULT_PADDING
        ));
        return panel;
    }
    
    /**
     * Create an east panel layout for the main window
     * @return A JPanel configured for the east region
     */
    public static JPanel createEastPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(UIConstants.SIDE_PANEL_SIZE);
        panel.setBorder(BorderFactory.createEmptyBorder(
            UIConstants.DEFAULT_PADDING,
            UIConstants.SMALL_PADDING,
            UIConstants.DEFAULT_PADDING,
            UIConstants.DEFAULT_PADDING
        ));
        return panel;
    }
    
    /**
     * Create a flow layout with default alignment
     * @return A FlowLayout with default settings
     */
    public static FlowLayout createFlowLayout() {
        return new FlowLayout(FlowLayout.LEFT, UIConstants.COMPONENT_SPACING, UIConstants.COMPONENT_SPACING);
    }
    
    /**
     * Create a flow layout with specified alignment
     * @param alignment The alignment (FlowLayout.LEFT, CENTER, or RIGHT)
     * @return A FlowLayout with specified alignment
     */
    public static FlowLayout createFlowLayout(int alignment) {
        return new FlowLayout(alignment, UIConstants.COMPONENT_SPACING, UIConstants.COMPONENT_SPACING);
    }
    
    /**
     * Create a grid layout
     * @param rows Number of rows
     * @param cols Number of columns
     * @return A GridLayout with spacing
     */
    public static GridLayout createGridLayout(int rows, int cols) {
        return new GridLayout(rows, cols, UIConstants.COMPONENT_SPACING, UIConstants.COMPONENT_SPACING);
    }
    
    /**
     * Create a grid bag layout with constraints
     * @return A new GridBagLayout
     */
    public static GridBagLayout createGridBagLayout() {
        return new GridBagLayout();
    }
    
    /**
     * Create default GridBagConstraints
     * @return GridBagConstraints with default settings
     */
    public static GridBagConstraints createGridBagConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(
            UIConstants.SMALL_PADDING,
            UIConstants.SMALL_PADDING,
            UIConstants.SMALL_PADDING,
            UIConstants.SMALL_PADDING
        );
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        return gbc;
    }
    
    /**
     * Create GridBagConstraints for a specific position
     * @param x Grid x position
     * @param y Grid y position
     * @return GridBagConstraints for the specified position
     */
    public static GridBagConstraints createGridBagConstraints(int x, int y) {
        GridBagConstraints gbc = createGridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        return gbc;
    }
    
    /**
     * Create GridBagConstraints with full parameters
     * @param x Grid x position
     * @param y Grid y position
     * @param width Grid width
     * @param height Grid height
     * @param weightx Horizontal weight
     * @param weighty Vertical weight
     * @param fill Fill constraint
     * @return Configured GridBagConstraints
     */
    public static GridBagConstraints createGridBagConstraints(
            int x, int y, int width, int height,
            double weightx, double weighty, int fill) {
        GridBagConstraints gbc = createGridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = width;
        gbc.gridheight = height;
        gbc.weightx = weightx;
        gbc.weighty = weighty;
        gbc.fill = fill;
        return gbc;
    }
    
    /**
     * Create a wrap layout for flowing components
     * @return A WrapLayout instance
     */
    public static WrapLayout createWrapLayout() {
        return new WrapLayout(FlowLayout.LEFT, UIConstants.COMPONENT_SPACING, UIConstants.COMPONENT_SPACING);
    }
    
    /**
     * Create a vertical box layout panel
     * @return JPanel with vertical BoxLayout
     */
    public static JPanel createVerticalBoxPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        return panel;
    }
    
    /**
     * Create a horizontal box layout panel
     * @return JPanel with horizontal BoxLayout
     */
    public static JPanel createHorizontalBoxPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        return panel;
    }
    
    /**
     * Add a component with glue for spacing
     * @param panel The panel to add to
     * @param component The component to add
     */
    public static void addWithGlue(JPanel panel, JComponent component) {
        panel.add(component);
        panel.add(Box.createHorizontalGlue());
    }
    
    /**
     * Add vertical spacing to a panel
     * @param panel The panel to add spacing to
     * @param height The height of the spacing
     */
    public static void addVerticalSpacing(JPanel panel, int height) {
        panel.add(Box.createRigidArea(new Dimension(0, height)));
    }
    
    /**
     * Add horizontal spacing to a panel
     * @param panel The panel to add spacing to
     * @param width The width of the spacing
     */
    public static void addHorizontalSpacing(JPanel panel, int width) {
        panel.add(Box.createRigidArea(new Dimension(width, 0)));
    }
    
    /**
     * Create a button panel with standard spacing
     * @param buttons The buttons to add
     * @return A JPanel containing the buttons
     */
    public static JPanel createButtonPanel(JButton... buttons) {
        JPanel panel = new JPanel(createFlowLayout(FlowLayout.RIGHT));
        for (JButton button : buttons) {
            panel.add(button);
        }
        return panel;
    }
    
    /**
     * Create a form panel with labels and fields
     * @return A JPanel configured for form layout
     */
    public static JPanel createFormPanel() {
        JPanel panel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);
        panel.setBorder(BorderFactory.createEmptyBorder(
            UIConstants.DEFAULT_PADDING,
            UIConstants.DEFAULT_PADDING,
            UIConstants.DEFAULT_PADDING,
            UIConstants.DEFAULT_PADDING
        ));
        return panel;
    }
    
    /**
     * Add a labeled field to a form panel
     * @param panel The form panel
     * @param label The label text
     * @param field The field component
     * @param row The row number
     */
    public static void addFormField(JPanel panel, String label, JComponent field, int row) {
        GridBagConstraints labelGbc = createGridBagConstraints(0, row);
        labelGbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel(label + ":"), labelGbc);
        
        GridBagConstraints fieldGbc = createGridBagConstraints(1, row);
        fieldGbc.weightx = 1.0;
        fieldGbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(field, fieldGbc);
    }
    
    /**
     * Create a tabbed pane
     * @return A styled JTabbedPane
     */
    public static JTabbedPane createTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setTabPlacement(JTabbedPane.TOP);
        return tabbedPane;
    }
    
    /**
     * Create a card layout panel
     * @return A JPanel with CardLayout
     */
    public static JPanel createCardLayoutPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new CardLayout());
        return panel;
    }
    
    /**
     * Center a window on the screen
     * @param window The window to center
     */
    public static void centerWindow(Window window) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = window.getSize();
        int x = (screenSize.width - windowSize.width) / 2;
        int y = (screenSize.height - windowSize.height) / 2;
        window.setLocation(x, y);
    }
    
    /**
     * Set minimum size for a component
     * @param component The component
     * @param width Minimum width
     * @param height Minimum height
     */
    public static void setMinimumSize(JComponent component, int width, int height) {
        component.setMinimumSize(new Dimension(width, height));
    }
    
    /**
     * Set preferred size for a component
     * @param component The component
     * @param width Preferred width
     * @param height Preferred height
     */
    public static void setPreferredSize(JComponent component, int width, int height) {
        component.setPreferredSize(new Dimension(width, height));
    }
    
    /**
     * Set maximum size for a component
     * @param component The component
     * @param width Maximum width
     * @param height Maximum height
     */
    public static void setMaximumSize(JComponent component, int width, int height) {
        component.setMaximumSize(new Dimension(width, height));
    }
    
    /**
     * Make all components in a container the same size
     * @param container The container with components
     */
    public static void equalizeComponentSizes(Container container) {
        Component[] components = container.getComponents();
        if (components.length == 0) return;
        
        // Find the maximum preferred size
        Dimension maxSize = new Dimension(0, 0);
        for (Component comp : components) {
            Dimension prefSize = comp.getPreferredSize();
            maxSize.width = Math.max(maxSize.width, prefSize.width);
            maxSize.height = Math.max(maxSize.height, prefSize.height);
        }
        
        // Apply the maximum size to all components
        for (Component comp : components) {
            comp.setPreferredSize(maxSize);
            if (comp instanceof JComponent) {
                ((JComponent) comp).setMinimumSize(maxSize);
                ((JComponent) comp).setMaximumSize(maxSize);
            }
        }
    }
}
