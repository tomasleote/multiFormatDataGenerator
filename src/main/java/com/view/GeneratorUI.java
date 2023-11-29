package com.view;

import com.controller.InputProcessor;
import com.controller.generators.MainGenerator;
import com.model.Template;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;

public class GeneratorUI extends JFrame {

    private JPanel generatorPanelContainer;
    private JTextArea outputArea;
    private JButton generateButton;
    private JTextField templateFormatField;
    private JButton applyTemplateButton;
    private JComboBox<Integer> evaluatorCountComboBox;
    private List<String> results;
    private JButton clearOutputButton;

    public GeneratorUI() {
        setTitle("Synthetic Data Generator");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initializeComponents();
        layoutComponents();
    }

    private void initializeComponents() {
        templateFormatField = new JTextField(20);
        applyTemplateButton = new JButton("Apply Template"); // Initialize the class-level button
        applyTemplateButton.addActionListener(e -> applyTemplate());

        evaluatorCountComboBox = new JComboBox<>(new Integer[]{0, 1, 2, 3, 4, 5});
        evaluatorCountComboBox.addActionListener(e -> addEvaluatorPanels());

        clearOutputButton = new JButton("Clear Output");
        clearOutputButton.addActionListener(e -> outputArea.setText(""));

        generatorPanelContainer = new JPanel(new CardLayout());
        outputArea = new JTextArea(10, 50);
        outputArea.setEditable(false);
        generateButton = new JButton("Generate");
        generateButton.addActionListener(e -> generateNumbers());
    }


    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel northPanel = new JPanel(new GridLayout(0, 1));
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS)); // Use BoxLayout for vertical stacking
        northPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add margins

        JPanel templatePanel = new JPanel();
        templatePanel.add(new JLabel("Template Format:"));
        templatePanel.add(templateFormatField);
        templatePanel.add(applyTemplateButton);
        northPanel.add(templatePanel);

        JPanel evaluatorPanel = new JPanel();
        evaluatorPanel.add(new JLabel("Number of Additional Evaluators:"));
        evaluatorPanel.add(evaluatorCountComboBox);
        northPanel.add(evaluatorPanel);

        northPanel.add(templatePanel);
        northPanel.add(evaluatorPanel);

        add(northPanel, BorderLayout.NORTH);

        generatorPanelContainer.setLayout(new WrapLayout(FlowLayout.LEADING, 10, 10));
        add(generatorPanelContainer, BorderLayout.CENTER);

        JPanel southPanel = new JPanel();
        southPanel.add(new JScrollPane(outputArea));
        southPanel.add(clearOutputButton);
        add(southPanel, BorderLayout.SOUTH);

        JPanel eastPanel = new JPanel();
        eastPanel.add(generateButton);
        add(eastPanel, BorderLayout.EAST);
    }

    private void applyTemplate() {
        String templateFormat = templateFormatField.getText();
        int numberOfGenerators = countGenerators(templateFormat);
        setupGeneratorsBasedOnTemplate(numberOfGenerators);
        addEvaluatorPanels(); // Call this here to add evaluator panels
    }

    private void addEvaluatorPanels() {
        int evaluatorCount = (int) evaluatorCountComboBox.getSelectedItem();
        for (int i = 0; i < evaluatorCount; i++) {
            GeneratorPanel evaluatorPanel = new GeneratorPanel(-1); // -1 indicates an additional evaluator
            generatorPanelContainer.add(evaluatorPanel);
        }

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

        generatorPanelContainer.revalidate();
        generatorPanelContainer.repaint();
    }

    private void generateNumbers() {
        try {
            InputProcessor processor = new InputProcessor(templateFormatField.getText());
            for (Component comp : generatorPanelContainer.getComponents()) {
                if (comp instanceof GeneratorPanel) {
                    GeneratorPanel panel = (GeneratorPanel) comp;
                    Map<String, String> properties = panel.getProperties();
                    String genType = properties.get("type");
                    processor.addGeneratorAndPattern(genType, properties);
                }
            }

            Template template = processor.initTemplate();
            MainGenerator mainGenerator = processor.initMainGenerator();

            results = mainGenerator.generate().limit(100).collect(Collectors.toList());
            for (String value : results) {
                outputArea.append(value + "\n");
            }
        } catch (Exception e) {
            outputArea.append("Error: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GeneratorUI frame = new GeneratorUI();
            frame.setVisible(true);
        });
    }
}
