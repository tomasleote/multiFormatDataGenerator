package com.view;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.awt.GridBagConstraints;
import java.awt.Insets;

@Getter
@Setter
public class GeneratorPanel extends JPanel {

    private Map<String, JComponent> propertyFields;
    private String generatorType;
    private Map<String, String> properties;

    private int generatorIndex;

    public GeneratorPanel(int generatorIndex) {
        this.generatorIndex = generatorIndex;
        propertyFields = new HashMap<>();

        setLayout(new GridBagLayout()); // Set GridBagLayout for more control
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER; // End row after the combo box
        gbc.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally
        gbc.weightx = 1; // Fill space
        gbc.insets = new Insets(2, 2, 2, 2); // Padding

        properties = new HashMap<>();

        JComboBox<String> generatorTypeComboBox = new JComboBox<>(new String[]{"SEQUENTIALNUMBERGENERATOR", "CALCULATION", "EVALUATION", "SEQUENTIALASCIIGENERATOR"});
        generatorTypeComboBox.addActionListener(e -> configureFieldsBasedOnType((String) generatorTypeComboBox.getSelectedItem()));
        add(generatorTypeComboBox);
    }

    public void addPropertyField(String propertyName) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 2, 2, 2); // Padding
        gbc.weightx = 0.1; // Give less space to labels

        JLabel label = new JLabel(propertyName);
        add(label, gbc);

        gbc.weightx = 1; // Give more space to text fields
        gbc.gridwidth = GridBagConstraints.REMAINDER; // End row

        JTextField textField = new JTextField(10);
        propertyFields.put(propertyName, textField);
        add(textField, gbc);
    }

    public Map<String, String> getProperties() {
        propertyFields.forEach((key, field) -> properties.put(key, ((JTextField) field).getText()));
        return properties;
    }


    private void configureFieldsBasedOnType(String type) {
        removeAll();
        setGeneratorType(type);

        JComboBox<String> generatorTypeComboBox = new JComboBox<>(new String[]{"SEQUENTIALNUMBERGENERATOR", "CALCULATION", "EVALUATION", "SEQUENTIALASCIIGENERATOR"});
        generatorTypeComboBox.setSelectedItem(type);
        generatorTypeComboBox.addActionListener(e -> configureFieldsBasedOnType((String) generatorTypeComboBox.getSelectedItem()));
        add(generatorTypeComboBox);
        properties.put("type", type);


        // Specific fields based on generator type
        switch (type) {
            case "SEQUENTIALNUMBERGENERATOR":
                addPropertyField("input");
                addPropertyField("length");
                addPropertyField("start");
                addPropertyField("step");
                addPropertyField("padding-length");
                addPropertyField("format");
                break;
            case "SEQUENTIALASCIIGENERATOR":
                addPropertyField("input");
                addPropertyField("list");
                addPropertyField("length");
                addPropertyField("start");
                addPropertyField("step");
                addPropertyField("padding-length");
                addPropertyField("format");
                break;
            case "EVALUATION":
                addPropertyField("input");
                addPropertyField("formula");
                break;
            case "CALCULATION":
                addPropertyField("input");
                addPropertyField("formula");
                addPropertyField("length");
                addPropertyField("format");
                break;
        }

        revalidate();
        repaint();
    }

}
