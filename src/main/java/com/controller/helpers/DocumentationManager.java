package com.controller.helpers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Manages documentation and help content for the application.
 * Provides context-sensitive help and comprehensive documentation.
 * 
 * Responsibilities:
 * - Display help content and documentation
 * - Provide context-sensitive help
 * - Manage help topics and navigation
 * - Show tooltips and user guidance
 */
public class DocumentationManager {
    
    /**
     * Help topic categories.
     */
    public enum HelpCategory {
        GETTING_STARTED("Getting Started"),
        GENERATORS("Generators"),
        TEMPLATES("Templates"),
        EXAMPLES("Examples"),
        TROUBLESHOOTING("Troubleshooting"),
        ADVANCED("Advanced Topics");
        
        private final String displayName;
        
        HelpCategory(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * Help topic container.
     */
    public static class HelpTopic {
        private final String id;
        private final String title;
        private final HelpCategory category;
        private final String content;
        private final List<String> keywords;
        private final List<String> relatedTopics;
        
        public HelpTopic(String id, String title, HelpCategory category, String content, 
                        List<String> keywords, List<String> relatedTopics) {
            this.id = id;
            this.title = title;
            this.category = category;
            this.content = content;
            this.keywords = new ArrayList<>(keywords != null ? keywords : new ArrayList<>());
            this.relatedTopics = new ArrayList<>(relatedTopics != null ? relatedTopics : new ArrayList<>());
        }
        
        // Getters
        public String getId() { return id; }
        public String getTitle() { return title; }
        public HelpCategory getCategory() { return category; }
        public String getContent() { return content; }
        public List<String> getKeywords() { return new ArrayList<>(keywords); }
        public List<String> getRelatedTopics() { return new ArrayList<>(relatedTopics); }
    }
    
    private final Map<String, HelpTopic> helpTopics;
    private final Map<HelpCategory, List<String>> categorizedTopics;
    private JFrame helpFrame;
    private JTree helpTree;
    private JEditorPane contentPane;
    private JTextField searchField;
    
    public DocumentationManager() {
        this.helpTopics = new HashMap<>();
        this.categorizedTopics = new HashMap<>();
        
        // Initialize categories
        for (HelpCategory category : HelpCategory.values()) {
            categorizedTopics.put(category, new ArrayList<>());
        }
        
        loadHelpContent();
    }
    
    /**
     * Loads all help content.
     */
    private void loadHelpContent() {
        loadGettingStartedTopics();
        loadGeneratorTopics();
        loadTemplateTopics();
        loadExampleTopics();
        loadTroubleshootingTopics();
        loadAdvancedTopics();
    }
    
    /**
     * Loads getting started help topics.
     */
    private void loadGettingStartedTopics() {
        addHelpTopic(new HelpTopic(
            "overview",
            "Application Overview",
            HelpCategory.GETTING_STARTED,
            "<html><h2>Multi-Format Data Generator</h2>" +
            "<p>This application helps you generate synthetic data with various formats and validation rules.</p>" +
            "<h3>Key Features:</h3>" +
            "<ul>" +
            "<li><b>Template-based generation:</b> Define output format using placeholders like {0}, {1}</li>" +
            "<li><b>Multiple generator types:</b> Sequential numbers, ASCII sequences, calculations</li>" +
            "<li><b>Validation support:</b> Built-in validation for common formats (BSN, SSN, etc.)</li>" +
            "<li><b>Export options:</b> Save data in CSV, TXT, JSON, or XML formats</li>" +
            "<li><b>Pre-built examples:</b> Ready-to-use configurations for common scenarios</li>" +
            "</ul>" +
            "<p>To get started, try loading an example or create a simple template like <code>{0}</code></p></html>",
            List.of("overview", "introduction", "features", "getting started"),
            List.of("quickstart", "templates_basic")
        ));
        
        addHelpTopic(new HelpTopic(
            "quickstart",
            "Quick Start Guide",
            HelpCategory.GETTING_STARTED,
            "<html><h2>Quick Start Guide</h2>" +
            "<ol>" +
            "<li><b>Enter a template format:</b> Type something like <code>{0}</code> in the template field</li>" +
            "<li><b>Apply the template:</b> Click 'Apply Template' to create generator panels</li>" +
            "<li><b>Configure generators:</b> Set properties like length, start value, step</li>" +
            "<li><b>Generate data:</b> Click 'Generate' to create sample data</li>" +
            "<li><b>Export results:</b> Use the export button to save your data</li>" +
            "</ol>" +
            "<h3>Example:</h3>" +
            "<p>Template: <code>{0}-{1}</code><br>" +
            "This creates two generators that will produce output like: <code>100-AA</code>, <code>101-AB</code></p>" +
            "<p><b>Tip:</b> Try loading a pre-built example to see how complex configurations work!</p></html>",
            List.of("quick", "start", "tutorial", "basic"),
            List.of("overview", "templates_basic", "examples_basic")
        ));
    }
    
    /**
     * Loads generator help topics.
     */
    private void loadGeneratorTopics() {
        addHelpTopic(new HelpTopic(
            "generators_overview",
            "Generator Types Overview",
            HelpCategory.GENERATORS,
            "<html><h2>Generator Types</h2>" +
            "<p>The application supports several types of data generators:</p>" +
            "<h3>Sequential Number Generator</h3>" +
            "<p>Generates sequential numbers with customizable properties:</p>" +
            "<ul>" +
            "<li><b>Length:</b> Number of digits in output</li>" +
            "<li><b>Start:</b> Starting number</li>" +
            "<li><b>Step:</b> Increment between numbers</li>" +
            "<li><b>Padding:</b> Zero-pad to fixed length</li>" +
            "</ul>" +
            "<h3>Sequential ASCII Generator</h3>" +
            "<p>Generates sequences from a list of characters or strings:</p>" +
            "<ul>" +
            "<li><b>List:</b> Comma-separated values to cycle through</li>" +
            "<li><b>Length:</b> Length of each generated item</li>" +
            "</ul>" +
            "<h3>Calculation Generator</h3>" +
            "<p>Performs calculations on other generator outputs:</p>" +
            "<ul>" +
            "<li><b>Formula:</b> Mathematical expression using A, B, C variables</li>" +
            "<li><b>Input:</b> References to other generators</li>" +
            "</ul>" +
            "<h3>Evaluation Generator</h3>" +
            "<p>Filters results based on logical conditions.</p></html>",
            List.of("generators", "types", "sequential", "calculation", "evaluation"),
            List.of("generators_sequential", "generators_calculation")
        ));
    }
    
    /**
     * Loads template help topics.
     */
    private void loadTemplateTopics() {
        addHelpTopic(new HelpTopic(
            "templates_basic",
            "Template Format Basics",
            HelpCategory.TEMPLATES,
            "<html><h2>Template Format Basics</h2>" +
            "<p>Templates define the output format using placeholders and static text.</p>" +
            "<h3>Placeholder Syntax:</h3>" +
            "<p>Use <code>{0}</code>, <code>{1}</code>, <code>{2}</code>, etc. to reference generators.</p>" +
            "<h3>Examples:</h3>" +
            "<table border='1' cellpadding='5'>" +
            "<tr><th>Template</th><th>Output Example</th><th>Description</th></tr>" +
            "<tr><td><code>{0}</code></td><td>12345</td><td>Single generator output</td></tr>" +
            "<tr><td><code>{0}-{1}</code></td><td>123-ABC</td><td>Two generators with hyphen</td></tr>" +
            "<tr><td><code>ID{0}</code></td><td>ID12345</td><td>Static prefix</td></tr>" +
            "<tr><td><code>{0}_{1}_{2}</code></td><td>123_ABC_XYZ</td><td>Three generators with underscores</td></tr>" +
            "</table>" +
            "<h3>Best Practices:</h3>" +
            "<ul>" +
            "<li>Start with simple templates like <code>{0}</code></li>" +
            "<li>Use meaningful separators (-, _, :)</li>" +
            "<li>Placeholders must be sequential (no gaps)</li>" +
            "<li>Static text can be placed anywhere</li>" +
            "</ul></html>",
            List.of("templates", "format", "placeholders", "syntax"),
            List.of("templates_advanced", "quickstart")
        ));
    }
    
    /**
     * Loads example help topics.
     */
    private void loadExampleTopics() {
        addHelpTopic(new HelpTopic(
            "examples_basic",
            "Using Pre-built Examples",
            HelpCategory.EXAMPLES,
            "<html><h2>Using Pre-built Examples</h2>" +
            "<p>The application includes several ready-to-use examples for common scenarios.</p></html>",
            List.of("examples", "prebuilt", "templates"),
            List.of("quickstart")
        ));
    }
    
    /**
     * Loads troubleshooting help topics.
     */
    private void loadTroubleshootingTopics() {
        addHelpTopic(new HelpTopic(
            "troubleshooting_common",
            "Common Issues",
            HelpCategory.TROUBLESHOOTING,
            "<html><h2>Common Issues and Solutions</h2>" +
            "<h3>Template Validation Errors</h3>" +
            "<p><b>Problem:</b> \"Template format cannot be empty\" or \"Must contain at least one placeholder\"</p>" +
            "<p><b>Solution:</b> Make sure your template includes placeholders like <code>{0}</code></p>" +
            "<h3>No Data Generated</h3>" +
            "<p><b>Problem:</b> Generation completes but no output appears</p>" +
            "<p><b>Solutions:</b></p>" +
            "<ul>" +
            "<li>Check that all generator configurations are valid</li>" +
            "<li>If using evaluation generators, they may be filtering out all results</li>" +
            "<li>Try a simpler configuration first</li>" +
            "</ul>" +
            "<h3>Performance Issues</h3>" +
            "<p><b>Problem:</b> Generation is very slow or appears to hang</p>" +
            "<p><b>Solutions:</b></p>" +
            "<ul>" +
            "<li>Reduce batch size for large datasets</li>" +
            "<li>Check evaluation formulas for infinite loops</li>" +
            "<li>Simplify complex generator configurations</li>" +
            "</ul></html>",
            List.of("troubleshooting", "problems", "errors", "performance"),
            List.of("templates_basic", "generators_overview")
        ));
    }
    
    /**
     * Loads advanced help topics.
     */
    private void loadAdvancedTopics() {
        addHelpTopic(new HelpTopic(
            "advanced_formulas",
            "Advanced Formulas",
            HelpCategory.ADVANCED,
            "<html><h2>Advanced Formula Usage</h2>" +
            "<p>Formula expressions can use mathematical operations and references to other generators.</p>" +
            "<h3>Available Operations:</h3>" +
            "<ul>" +
            "<li><b>Arithmetic:</b> +, -, *, /, %</li>" +
            "<li><b>Comparison:</b> ==, !=, <, >, <=, >=</li>" +
            "<li><b>Logical:</b> &&, ||, !</li>" +
            "</ul>" +
            "<h3>Variable References:</h3>" +
            "<p>Use A, B, C, etc. to reference digits from input generators.</p>" +
            "<p>Example: For input \"12345\", A=1, B=2, C=3, D=4, E=5</p></html>",
            List.of("formulas", "expressions", "calculations", "advanced"),
            List.of("generators_calculation")
        ));
    }
    
    /**
     * Adds a help topic to the collection.
     */
    private void addHelpTopic(HelpTopic topic) {
        helpTopics.put(topic.getId(), topic);
        categorizedTopics.get(topic.getCategory()).add(topic.getId());
    }
    
    /**
     * Shows the main help window.
     */
    public void showHelpWindow() {
        if (helpFrame == null) {
            createHelpWindow();
        }
        
        helpFrame.setVisible(true);
        helpFrame.toFront();
    }
    
    /**
     * Shows help for a specific topic.
     */
    public void showHelpTopic(String topicId) {
        showHelpWindow();
        
        HelpTopic topic = helpTopics.get(topicId);
        if (topic != null) {
            displayHelpContent(topic);
        }
    }
    
    /**
     * Creates the help window.
     */
    private void createHelpWindow() {
        helpFrame = new JFrame("Help & Documentation");
        helpFrame.setSize(800, 600);
        helpFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        helpFrame.setLocationRelativeTo(null);
        
        // Create layout
        helpFrame.setLayout(new BorderLayout());
        
        // Create search panel
        JPanel searchPanel = createSearchPanel();
        helpFrame.add(searchPanel, BorderLayout.NORTH);
        
        // Create split pane for navigation and content
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(createNavigationPanel());
        splitPane.setRightComponent(createContentPanel());
        splitPane.setDividerLocation(250);
        
        helpFrame.add(splitPane, BorderLayout.CENTER);
        
        // Show overview by default
        showHelpTopic("overview");
    }
    
    /**
     * Creates the search panel.
     */
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        searchField = new JTextField();
        searchField.addActionListener(e -> performSearch());
        
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> performSearch());
        
        panel.add(new JLabel("Search: "), BorderLayout.WEST);
        panel.add(searchField, BorderLayout.CENTER);
        panel.add(searchButton, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Creates the navigation panel with topic tree.
     */
    private JPanel createNavigationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Topics"));
        
        // Create tree model
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Help Topics");
        
        for (HelpCategory category : HelpCategory.values()) {
            DefaultMutableTreeNode categoryNode = new DefaultMutableTreeNode(category.getDisplayName());
            root.add(categoryNode);
            
            for (String topicId : categorizedTopics.get(category)) {
                HelpTopic topic = helpTopics.get(topicId);
                if (topic != null) {
                    DefaultMutableTreeNode topicNode = new DefaultMutableTreeNode(topic.getTitle());
                    topicNode.setUserObject(topicId);
                    categoryNode.add(topicNode);
                }
            }
        }
        
        helpTree = new JTree(root);
        helpTree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) helpTree.getLastSelectedPathComponent();
            if (node != null && node.isLeaf() && node.getUserObject() instanceof String) {
                String topicId = (String) node.getUserObject();
                HelpTopic topic = helpTopics.get(topicId);
                if (topic != null) {
                    displayHelpContent(topic);
                }
            }
        });
        
        // Expand all categories by default
        for (int i = 0; i < helpTree.getRowCount(); i++) {
            helpTree.expandRow(i);
        }
        
        JScrollPane scrollPane = new JScrollPane(helpTree);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Creates the content display panel.
     */
    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Content"));
        
        contentPane = new JEditorPane();
        contentPane.setContentType("text/html");
        contentPane.setEditable(false);
        
        JScrollPane scrollPane = new JScrollPane(contentPane);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Displays help content for a topic.
     */
    private void displayHelpContent(HelpTopic topic) {
        if (contentPane != null) {
            StringBuilder content = new StringBuilder();
            content.append(topic.getContent());
            
            // Add related topics
            if (!topic.getRelatedTopics().isEmpty()) {
                content.append("<h3>Related Topics:</h3><ul>");
                for (String relatedId : topic.getRelatedTopics()) {
                    HelpTopic related = helpTopics.get(relatedId);
                    if (related != null) {
                        content.append("<li>").append(related.getTitle()).append("</li>");
                    }
                }
                content.append("</ul>");
            }
            
            contentPane.setText(content.toString());
            contentPane.setCaretPosition(0); // Scroll to top
        }
    }
    
    /**
     * Performs a search through help topics.
     */
    private void performSearch() {
        String searchText = searchField.getText().toLowerCase().trim();
        if (searchText.isEmpty()) {
            return;
        }
        
        // Simple search through keywords and content
        for (HelpTopic topic : helpTopics.values()) {
            if (topic.getKeywords().stream().anyMatch(keyword -> keyword.toLowerCase().contains(searchText)) ||
                topic.getTitle().toLowerCase().contains(searchText) ||
                topic.getContent().toLowerCase().contains(searchText)) {
                
                showHelpTopic(topic.getId());
                break;
            }
        }
    }
    
    /**
     * Creates a tooltip for a UI component.
     */
    public static String createTooltip(String title, String description) {
        return "<html><b>" + title + "</b><br>" + description + "</html>";
    }
    
    /**
     * Creates a tooltip for generator properties.
     */
    public static String createGeneratorTooltip(String generatorType, String property) {
        Map<String, Map<String, String>> tooltips = new HashMap<>();
        
        // Sequential Number Generator tooltips
        Map<String, String> seqTooltips = new HashMap<>();
        seqTooltips.put("length", "Maximum number of digits in the generated number");
        seqTooltips.put("start", "The first number to generate (starting point)");
        seqTooltips.put("step", "How much to increment each number (1 for consecutive)");
        seqTooltips.put("padding-length", "Pad with zeros to this many digits (0 for no padding)");
        tooltips.put("SEQUENTIALNUMBERGENERATOR", seqTooltips);
        
        // ASCII Generator tooltips
        Map<String, String> asciiTooltips = new HashMap<>();
        asciiTooltips.put("list", "Comma-separated list of values to cycle through");
        asciiTooltips.put("length", "Length of each generated sequence");
        asciiTooltips.put("start", "Starting sequence value");
        tooltips.put("SEQUENTIALASCIIGENERATOR", asciiTooltips);
        
        // Calculation tooltips
        Map<String, String> calcTooltips = new HashMap<>();
        calcTooltips.put("formula", "Mathematical expression using A, B, C variables from input");
        calcTooltips.put("input", "Index of the generator to use as input (0, 1, 2, etc.)");
        tooltips.put("CALCULATION", calcTooltips);
        
        Map<String, String> generatorTooltips = tooltips.get(generatorType.toUpperCase());
        if (generatorTooltips != null) {
            String tooltip = generatorTooltips.get(property.toLowerCase());
            if (tooltip != null) {
                return createTooltip(property, tooltip);
            }
        }
        
        return null;
    }
    
    /**
     * Gets a help topic by ID.
     */
    public HelpTopic getHelpTopic(String topicId) {
        return helpTopics.get(topicId);
    }
    
    /**
     * Gets all help topics in a category.
     */
    public List<HelpTopic> getTopicsInCategory(HelpCategory category) {
        List<HelpTopic> topics = new ArrayList<>();
        for (String topicId : categorizedTopics.get(category)) {
            HelpTopic topic = helpTopics.get(topicId);
            if (topic != null) {
                topics.add(topic);
            }
        }
        return topics;
    }
    
    /**
     * Hides the help window.
     */
    public void hideHelpWindow() {
        if (helpFrame != null) {
            helpFrame.setVisible(false);
        }
    }
    
    /**
     * Creates an ActionListener for showing help.
     */
    public ActionListener createShowHelpListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showHelpWindow();
            }
        };
    }
    
    /**
     * Creates an ActionListener for showing a specific help topic.
     */
    public ActionListener createShowTopicListener(String topicId) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showHelpTopic(topicId);
            }
        };
    }
}