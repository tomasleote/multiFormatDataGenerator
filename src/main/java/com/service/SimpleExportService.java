package com.service;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Simplified export service without external dependencies
 */
public class SimpleExportService {
    
    public boolean exportToCSV(Component parent, List<String> data, String templateFormat) {
        return exportToFile(parent, data, templateFormat, "csv", "CSV files", this::writeCSV);
    }
    
    public boolean exportToTXT(Component parent, List<String> data, String templateFormat) {
        return exportToFile(parent, data, templateFormat, "txt", "Text files", this::writeTXT);
    }
    
    public boolean exportToJSON(Component parent, List<String> data, String templateFormat) {
        return exportToFile(parent, data, templateFormat, "json", "JSON files", this::writeJSON);
    }
    
    private boolean exportToFile(Component parent, List<String> data, String templateFormat,
                                String extension, String description, DataWriter writer) {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Export as " + extension.toUpperCase());
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(description, extension));
            fileChooser.setSelectedFile(new File("generated-data." + extension));
            
            if (fileChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (!file.getName().toLowerCase().endsWith("." + extension)) {
                    file = new File(file.getAbsolutePath() + "." + extension);
                }
                
                writer.writeFile(file, data, templateFormat);
                
                JOptionPane.showMessageDialog(parent,
                    "Successfully exported " + data.size() + " records to " + file.getName(),
                    extension.toUpperCase() + " Export",
                    JOptionPane.INFORMATION_MESSAGE);
                
                return true;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent,
                "Failed to export " + extension.toUpperCase() + ": " + e.getMessage(),
                "Export Error",
                JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
    
    private void writeCSV(File file, List<String> data, String templateFormat) throws IOException {
        try (PrintWriter writer = new PrintWriter(new java.io.FileWriter(file))) {
            writer.println("Generated_Data");
            for (String value : data) {
                writer.println("\"" + value.replace("\"", "\"\"") + "\"");
            }
        }
    }
    
    private void writeTXT(File file, List<String> data, String templateFormat) throws IOException {
        try (PrintWriter writer = new PrintWriter(new java.io.FileWriter(file))) {
            writer.println("# Generated Data - " + getCurrentTimestamp());
            writer.println("# Template Format: " + templateFormat);
            writer.println("# Total Records: " + data.size());
            writer.println("# ================================");
            writer.println();
            
            for (String value : data) {
                writer.println(value);
            }
        }
    }
    
    private void writeJSON(File file, List<String> data, String templateFormat) throws IOException {
        try (PrintWriter writer = new PrintWriter(new java.io.FileWriter(file))) {
            writer.println("{");
            writer.println("  \"metadata\": {");
            writer.println("    \"exportDate\": \"" + getCurrentTimestamp() + "\",");
            writer.println("    \"templateFormat\": \"" + templateFormat + "\",");
            writer.println("    \"totalRecords\": " + data.size() + ",");
            writer.println("    \"version\": \"3.0\"");
            writer.println("  },");
            writer.println("  \"data\": [");
            
            for (int i = 0; i < data.size(); i++) {
                writer.print("    \"" + data.get(i).replace("\"", "\\\"") + "\"");
                if (i < data.size() - 1) {
                    writer.println(",");
                } else {
                    writer.println();
                }
            }
            
            writer.println("  ]");
            writer.println("}");
        }
    }
    
    private String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    
    @FunctionalInterface
    private interface DataWriter {
        void writeFile(File file, List<String> data, String templateFormat) throws IOException;
    }
}
