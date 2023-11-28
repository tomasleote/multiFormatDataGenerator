package com.datprof.generators.view;

import com.datprof.generators.controller.InputProcessor;
import com.datprof.generators.controller.generators.MainGenerator;
import com.datprof.generators.model.Template;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class GeneratorUI extends JFrame {

    private JPanel generatorPanelContainer;
    private JTextArea outputArea;
    private JButton generateButton;
    private JTextField templateFormatField;
    private JButton applyTemplateButton;

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

        generatorPanelContainer = new JPanel(new CardLayout());
        outputArea = new JTextArea(10, 50);
        outputArea.setEditable(false);
        generateButton = new JButton("Generate");
        generateButton.addActionListener(e -> generateNumbers());
    }


    private void layoutComponents() {
        setLayout(new BorderLayout());

        JPanel templatePanel = new JPanel();
        templatePanel.add(new JLabel("Template Format:"));
        templatePanel.add(templateFormatField);
        templatePanel.add(applyTemplateButton);

        add(templatePanel, BorderLayout.NORTH);
        add(generatorPanelContainer, BorderLayout.CENTER);
        add(new JScrollPane(outputArea), BorderLayout.SOUTH);
        add(generateButton, BorderLayout.EAST);
    }

    private void applyTemplate() {
        String templateFormat = templateFormatField.getText();
        int numberOfGenerators = countGenerators(templateFormat);
        setupGeneratorsBasedOnTemplate(numberOfGenerators);
    }

    private int countGenerators(String template) {
        return (int) template.chars().filter(ch -> ch == '{').count();
    }

    private void setupGeneratorsBasedOnTemplate(int numberOfGenerators) {
        generatorPanelContainer.setLayout(new GridLayout(numberOfGenerators, 1));
        generatorPanelContainer.removeAll();

        for (int i = 0; i < numberOfGenerators; i++) {
            GeneratorPanel panel = new GeneratorPanel(i);
            generatorPanelContainer.add(panel);
        }

        generatorPanelContainer.revalidate();
        generatorPanelContainer.repaint();
    }

    private void generateNumbers() {
        InputProcessor processor = new InputProcessor(templateFormatField.getText());
        for (Component comp : generatorPanelContainer.getComponents()) {
            if (comp instanceof GeneratorPanel) {
                GeneratorPanel panel = (GeneratorPanel) comp;
                Map<String, String> properties = panel.getProperties();
                String genType = properties.get("type"); // Extract and remove the hidden type field
                processor.addGeneratorAndPattern(genType, properties);
            }
        }

        Template template = processor.initTemplate();
        MainGenerator mainGenerator = processor.initMainGenerator();
        mainGenerator.generate().limit(100)
                .forEach(value -> outputArea.append(value + "\n"));
    }


    public void setGeneratorPanel(JPanel panel) {
        generatorPanelContainer.removeAll();
        generatorPanelContainer.add(panel);
        generatorPanelContainer.revalidate();
        generatorPanelContainer.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GeneratorUI frame = new GeneratorUI();
            frame.setVisible(true);
        });
    }
}
