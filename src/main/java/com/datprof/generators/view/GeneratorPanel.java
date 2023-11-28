package com.datprof.generators.view;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class GeneratorPanel extends JPanel {

    private Map<String, JComponent> propertyFields;
    private String generatorType;

    private int generatorIndex;

    public GeneratorPanel(int generatorIndex) {
        this.generatorIndex = generatorIndex;
        propertyFields = new HashMap<>();
        setLayout(new GridLayout(0, 2));

        JComboBox<String> generatorTypeComboBox = new JComboBox<>(new String[]{"SEQUENTIALNUMBERGENERATOR", "CALCULATION", "EVALUATION", "SEQUENTIALASCIIGENERATOR"});
        generatorTypeComboBox.addActionListener(e -> configureFieldsBasedOnType((String) generatorTypeComboBox.getSelectedItem()));
        add(generatorTypeComboBox);
    }

    public void addPropertyField(String propertyName) {
        JLabel label = new JLabel(propertyName);
        JTextField textField = new JTextField(10);
        propertyFields.put(propertyName, textField);
        add(label);
        add(textField);
    }

    public Map<String, String> getProperties() {
        Map<String, String> properties = new HashMap<>();
        propertyFields.forEach((key, field) -> properties.put(key, ((JTextField) field).getText()));
        return properties;
    }

    public void setupForGeneratorType(String generatorType) {
        removeAll();

        JComboBox<String> generatorTypeComboBox = new JComboBox<>(new String[]{"SEQUENTIALNUMBERGENERATOR", "CALCULATION", "EVALUATION", "SEQUENTIALASCIIGENERATOR"});
        generatorTypeComboBox.addActionListener(e -> configureFieldsBasedOnType((String) generatorTypeComboBox.getSelectedItem()));
        add(generatorTypeComboBox);

        setGeneratorType(generatorType);

        // Add common fields
        addPropertyField("length");
        addPropertyField("start");

        // Add specific fields based on generator type
        switch (generatorType) {
            case "SEQUENTIALNUMBERGENERATOR":
                addPropertyField("step");
                addPropertyField("padding-length");
                break;
            case "SEQUENTIALASCIIGENERATOR":
                addPropertyField("list");
                break;
            case "EVALUATION":
            case "CALCULATION":
                addPropertyField("formula");
                break;
        }

        revalidate();
        repaint();
    }

    private void configureFieldsBasedOnType(String type) {
        removeAll();
        setGeneratorType(type);

        JComboBox<String> generatorTypeComboBox = new JComboBox<>(new String[]{"SEQUENTIALNUMBERGENERATOR", "CALCULATION", "EVALUATION", "SEQUENTIALASCIIGENERATOR"});
        generatorTypeComboBox.setSelectedItem(type);
        generatorTypeComboBox.addActionListener(e -> configureFieldsBasedOnType((String) generatorTypeComboBox.getSelectedItem()));
        add(generatorTypeComboBox);

        // Common fields
        addPropertyField("input");

        // Specific fields based on generator type
        switch (type) {
            case "SEQUENTIALNUMBERGENERATOR":
                addPropertyField("length");
                addPropertyField("start");
                addPropertyField("step");
                addPropertyField("padding-length");
                break;
            case "SEQUENTIALASCIIGENERATOR":
                addPropertyField("list");
                break;
            case "EVALUATION":
            case "CALCULATION":
                addPropertyField("formula");
                break;
        }

        revalidate();
        repaint();
    }

}
