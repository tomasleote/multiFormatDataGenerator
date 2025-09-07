package com.controller.business;

import com.service.SimpleExportService;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Manages data export operations and format handling.
 * Provides various export formats and batch processing capabilities.
 * 
 * Responsibilities:
 * - Handle different export formats (CSV, TXT, JSON)
 * - Manage export settings and configurations
 * - Provide batch export functionality
 * - Track export history and statistics
 */
public class DataExportManager {
    
    /**
     * Supported export formats.
     */
    public enum ExportFormat {
        CSV("Comma-Separated Values", ".csv"),
        TXT("Plain Text", ".txt"),
        JSON("JavaScript Object Notation", ".json"),
        XML("Extensible Markup Language", ".xml");
        
        private final String description;
        private final String extension;
        
        ExportFormat(String description, String extension) {
            this.description = description;
            this.extension = extension;
        }
        
        public String getDescription() { return description; }
        public String getExtension() { return extension; }
    }
    
    /**
     * Export configuration settings.
     */
    public static class ExportSettings {
        private ExportFormat format;
        private String filePath;
        private boolean includeHeaders;
        private String delimiter;
        private int batchSize;
        private boolean appendMode;
        private String encoding;
        
        public ExportSettings() {
            this.format = ExportFormat.CSV;
            this.includeHeaders = true;
            this.delimiter = ",";
            this.batchSize = 1000;
            this.appendMode = false;
            this.encoding = "UTF-8";
        }
        
        // Getters and setters
        public ExportFormat getFormat() { return format; }
        public void setFormat(ExportFormat format) { this.format = format; }
        
        public String getFilePath() { return filePath; }
        public void setFilePath(String filePath) { this.filePath = filePath; }
        
        public boolean isIncludeHeaders() { return includeHeaders; }
        public void setIncludeHeaders(boolean includeHeaders) { this.includeHeaders = includeHeaders; }
        
        public String getDelimiter() { return delimiter; }
        public void setDelimiter(String delimiter) { this.delimiter = delimiter; }
        
        public int getBatchSize() { return batchSize; }
        public void setBatchSize(int batchSize) { this.batchSize = batchSize; }
        
        public boolean isAppendMode() { return appendMode; }
        public void setAppendMode(boolean appendMode) { this.appendMode = appendMode; }
        
        public String getEncoding() { return encoding; }
        public void setEncoding(String encoding) { this.encoding = encoding; }
    }
    
    /**
     * Export result information.
     */
    public static class ExportResult {
        private final boolean success;
        private final String filePath;
        private final int recordCount;
        private final long fileSize;
        private final String error;
        private final long exportTimeMs;
        
        private ExportResult(boolean success, String filePath, int recordCount, 
                           long fileSize, String error, long exportTimeMs) {
            this.success = success;
            this.filePath = filePath;
            this.recordCount = recordCount;
            this.fileSize = fileSize;
            this.error = error;
            this.exportTimeMs = exportTimeMs;
        }
        
        public static ExportResult success(String filePath, int recordCount, long fileSize, long exportTimeMs) {
            return new ExportResult(true, filePath, recordCount, fileSize, null, exportTimeMs);
        }
        
        public static ExportResult failure(String error) {
            return new ExportResult(false, null, 0, 0, error, 0);
        }
        
        // Getters
        public boolean isSuccess() { return success; }
        public String getFilePath() { return filePath; }
        public int getRecordCount() { return recordCount; }
        public long getFileSize() { return fileSize; }
        public String getError() { return error; }
        public long getExportTimeMs() { return exportTimeMs; }
    }
    
    private final SimpleExportService exportService;
    private final ExecutorService executor;
    private final Map<String, ExportResult> exportHistory;
    private volatile boolean isExporting;
    
    public DataExportManager() {
        this.exportService = new SimpleExportService();
        this.executor = Executors.newSingleThreadExecutor();
        this.exportHistory = new HashMap<>();
        this.isExporting = false;
    }
    
    /**
     * Exports data synchronously.
     * 
     * @param data The data to export
     * @param settings Export settings
     * @return ExportResult with success/failure information
     */
    public ExportResult exportData(List<String> data, ExportSettings settings) {
        if (data == null || data.isEmpty()) {
            return ExportResult.failure("No data to export");
        }
        
        if (settings == null || settings.getFilePath() == null || settings.getFilePath().trim().isEmpty()) {
            return ExportResult.failure("Invalid export settings or file path");
        }
        
        long startTime = System.currentTimeMillis();
        isExporting = true;
        
        try {
            // Ensure file path has correct extension
            String filePath = ensureCorrectExtension(settings.getFilePath(), settings.getFormat());
            settings.setFilePath(filePath);
            
            // Perform the export based on format
            boolean success = false;
            switch (settings.getFormat()) {
                case CSV:
                    success = exportService.exportToCSV(data, filePath, settings.getDelimiter());
                    break;
                case TXT:
                    success = exportService.exportToTXT(data, filePath);
                    break;
                case JSON:
                    success = exportService.exportToJSON(data, filePath);
                    break;
                case XML:
                    success = exportToXML(data, filePath);
                    break;
                default:
                    return ExportResult.failure("Unsupported export format: " + settings.getFormat());
            }
            
            if (success) {
                long endTime = System.currentTimeMillis();
                long exportTime = endTime - startTime;
                
                // Get file size
                File file = new File(filePath);
                long fileSize = file.exists() ? file.length() : 0;
                
                ExportResult result = ExportResult.success(filePath, data.size(), fileSize, exportTime);
                
                // Store in history
                String historyKey = generateHistoryKey(filePath);
                exportHistory.put(historyKey, result);
                
                return result;
            } else {
                return ExportResult.failure("Export operation failed");
            }
            
        } catch (Exception e) {
            return ExportResult.failure("Export error: " + e.getMessage());
        } finally {
            isExporting = false;
        }
    }
    
    /**
     * Exports data asynchronously.
     * 
     * @param data The data to export
     * @param settings Export settings
     * @return CompletableFuture containing the ExportResult
     */
    public CompletableFuture<ExportResult> exportDataAsync(List<String> data, ExportSettings settings) {
        return CompletableFuture.supplyAsync(() -> exportData(data, settings), executor);
    }
    
    /**
     * Progress callback interface for batch exports.
     */
    public interface ProgressCallback {
        void onProgress(int progressPercent, int exportedCount, int totalCount);
    }
    
    /**
     * Exports data to XML format.
     * 
     * @param data The data to export
     * @param filePath The file path to export to
     * @return true if successful, false otherwise
     */
    private boolean exportToXML(List<String> data, String filePath) {
        try {
            StringBuilder xml = new StringBuilder();
            xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            xml.append("<data>\n");
            
            for (int i = 0; i < data.size(); i++) {
                xml.append("  <record id=\"").append(i + 1).append("\">")
                   .append(escapeXml(data.get(i)))
                   .append("</record>\n");
            }
            
            xml.append("</data>");
            
            // Use the export service to write the XML
            return exportService.exportToTXT(List.of(xml.toString()), filePath);
            
        } catch (Exception e) {
            System.err.println("Error exporting to XML: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Escapes XML special characters.
     * 
     * @param text The text to escape
     * @return Escaped text
     */
    private String escapeXml(String text) {
        if (text == null) return "";
        
        return text.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace(">", "&gt;")
                  .replace("\"", "&quot;")
                  .replace("'", "&apos;");
    }
    
    /**
     * Ensures the file path has the correct extension for the format.
     * 
     * @param filePath The original file path
     * @param format The export format
     * @return File path with correct extension
     */
    private String ensureCorrectExtension(String filePath, ExportFormat format) {
        if (filePath == null || format == null) {
            return filePath;
        }
        
        String extension = format.getExtension();
        if (!filePath.toLowerCase().endsWith(extension)) {
            // Remove any existing extension and add the correct one
            int lastDot = filePath.lastIndexOf('.');
            if (lastDot > 0 && lastDot > filePath.lastIndexOf(File.separator)) {
                filePath = filePath.substring(0, lastDot);
            }
            filePath += extension;
        }
        
        return filePath;
    }
    
    /**
     * Generates a unique key for export history.
     * 
     * @param filePath The file path
     * @return History key
     */
    private String generateHistoryKey(String filePath) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = new File(filePath).getName();
        return timestamp + "_" + fileName;
    }
    
    /**
     * Gets the export history.
     * 
     * @return Map of export history entries
     */
    public Map<String, ExportResult> getExportHistory() {
        return new HashMap<>(exportHistory);
    }
    
    /**
     * Clears the export history.
     */
    public void clearExportHistory() {
        exportHistory.clear();
    }
    
    /**
     * Checks if an export operation is currently in progress.
     * 
     * @return true if exporting, false otherwise
     */
    public boolean isExporting() {
        return isExporting;
    }
    
    /**
     * Cancels any ongoing export operation.
     */
    public void cancelExport() {
        isExporting = false;
        // Additional cancellation logic could be added here
    }
    
    /**
     * Creates default export settings for a given format.
     * 
     * @param format The export format
     * @param filePath The file path
     * @return Default export settings
     */
    public ExportSettings createDefaultSettings(ExportFormat format, String filePath) {
        ExportSettings settings = new ExportSettings();
        settings.setFormat(format);
        settings.setFilePath(filePath);
        
        // Format-specific defaults
        switch (format) {
            case CSV:
                settings.setDelimiter(",");
                settings.setIncludeHeaders(true);
                break;
            case TXT:
                settings.setDelimiter("\n");
                settings.setIncludeHeaders(false);
                break;
            case JSON:
            case XML:
                settings.setIncludeHeaders(false);
                break;
        }
        
        return settings;
    }
    
    /**
     * Validates export settings.
     * 
     * @param settings The settings to validate
     * @return true if valid, false otherwise
     */
    public boolean validateExportSettings(ExportSettings settings) {
        if (settings == null) {
            return false;
        }
        
        if (settings.getFilePath() == null || settings.getFilePath().trim().isEmpty()) {
            return false;
        }
        
        if (settings.getFormat() == null) {
            return false;
        }
        
        if (settings.getBatchSize() <= 0) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Gets available export formats.
     * 
     * @return Array of available export formats
     */
    public ExportFormat[] getAvailableFormats() {
        return ExportFormat.values();
    }
    
    /**
     * Gets the recommended file extension for a format.
     * 
     * @param format The export format
     * @return Recommended file extension
     */
    public String getRecommendedExtension(ExportFormat format) {
        return format != null ? format.getExtension() : ".txt";
    }
    
    /**
     * Estimates the file size for the given data and format.
     * 
     * @param data The data to export
     * @param format The export format
     * @return Estimated file size in bytes
     */
    public long estimateFileSize(List<String> data, ExportFormat format) {
        if (data == null || data.isEmpty()) {
            return 0;
        }
        
        long totalChars = data.stream().mapToLong(String::length).sum();
        
        // Add overhead based on format
        switch (format) {
            case CSV:
                // Add commas and newlines
                return totalChars + (data.size() * 2); // rough estimate
            case JSON:
                // Add JSON structure overhead
                return totalChars + (data.size() * 20); // rough estimate for JSON structure
            case XML:
                // Add XML tag overhead
                return totalChars + (data.size() * 50); // rough estimate for XML tags
            case TXT:
            default:
                // Add newlines
                return totalChars + data.size();
        }
    }
    
    /**
     * Shuts down the export manager and releases resources.
     */
    public void shutdown() {
        isExporting = false;
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }
}