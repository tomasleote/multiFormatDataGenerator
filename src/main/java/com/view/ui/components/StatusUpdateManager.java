package com.view.ui.components;

import com.view.ui.constants.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * StatusUpdateManager - Manages status bar updates and progress indication
 * 
 * This class handles all status bar updates, progress bar management,
 * and temporary status messages with automatic clearing.
 * 
 * @since Phase 1 Refactoring
 * @version 1.0
 */
public class StatusUpdateManager {
    
    private JLabel statusLabel;
    private JProgressBar progressBar;
    private JLabel configurationStatusLabel;
    private ScheduledExecutorService scheduler;
    private Timer statusTimer;
    
    /**
     * Constructor for StatusUpdateManager
     * @param statusLabel The main status label
     * @param progressBar The progress bar component
     * @param configurationStatusLabel The configuration status label
     */
    public StatusUpdateManager(JLabel statusLabel, JProgressBar progressBar, JLabel configurationStatusLabel) {
        this.statusLabel = statusLabel;
        this.progressBar = progressBar;
        this.configurationStatusLabel = configurationStatusLabel;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        initializeTimer();
    }
    
    /**
     * Initialize the status timer for automatic clearing
     */
    private void initializeTimer() {
        statusTimer = new Timer(UIConstants.STATUS_MESSAGE_DURATION, e -> {
            setStatus(UIConstants.STATUS_READY);
        });
        statusTimer.setRepeats(false);
    }
    
    /**
     * Set the status message
     * @param message The status message
     */
    public void setStatus(String message) {
        SwingUtilities.invokeLater(() -> {
            if (statusLabel != null) {
                statusLabel.setText(message);
            }
        });
    }
    
    /**
     * Set a temporary status message that auto-clears
     * @param message The status message
     */
    public void setTemporaryStatus(String message) {
        setStatus(message);
        statusTimer.restart();
    }
    
    /**
     * Set a temporary status message with custom duration
     * @param message The status message
     * @param duration Duration in milliseconds
     */
    public void setTemporaryStatus(String message, int duration) {
        setStatus(message);
        statusTimer.setDelay(duration);
        statusTimer.restart();
    }
    
    /**
     * Set status with color indication
     * @param message The status message
     * @param color The text color
     */
    public void setStatusWithColor(String message, Color color) {
        SwingUtilities.invokeLater(() -> {
            if (statusLabel != null) {
                statusLabel.setText(message);
                statusLabel.setForeground(color);
            }
        });
    }
    
    /**
     * Set success status
     * @param message The success message
     */
    public void setSuccessStatus(String message) {
        setStatusWithColor(message, UIConstants.SUCCESS_COLOR);
        statusTimer.restart();
    }
    
    /**
     * Set error status
     * @param message The error message
     */
    public void setErrorStatus(String message) {
        setStatusWithColor(message, UIConstants.ERROR_COLOR);
        statusTimer.setDelay(UIConstants.STATUS_MESSAGE_DURATION * 2); // Show errors longer
        statusTimer.restart();
    }
    
    /**
     * Set warning status
     * @param message The warning message
     */
    public void setWarningStatus(String message) {
        setStatusWithColor(message, UIConstants.WARNING_COLOR);
        statusTimer.restart();
    }
    
    /**
     * Set info status
     * @param message The info message
     */
    public void setInfoStatus(String message) {
        setStatusWithColor(message, UIConstants.INFO_COLOR);
        statusTimer.restart();
    }
    
    /**
     * Update configuration status
     * @param message The configuration status message
     */
    public void setConfigurationStatus(String message) {
        SwingUtilities.invokeLater(() -> {
            if (configurationStatusLabel != null) {
                configurationStatusLabel.setText(message);
            }
        });
    }
    
    /**
     * Start progress indication
     * @param message The progress message
     */
    public void startProgress(String message) {
        SwingUtilities.invokeLater(() -> {
            setStatus(message);
            if (progressBar != null) {
                progressBar.setIndeterminate(true);
                progressBar.setVisible(true);
            }
        });
    }
    
    /**
     * Start progress with maximum value
     * @param message The progress message
     * @param maximum The maximum value
     */
    public void startProgress(String message, int maximum) {
        SwingUtilities.invokeLater(() -> {
            setStatus(message);
            if (progressBar != null) {
                progressBar.setIndeterminate(false);
                progressBar.setMinimum(0);
                progressBar.setMaximum(maximum);
                progressBar.setValue(0);
                progressBar.setVisible(true);
            }
        });
    }
    
    /**
     * Update progress value
     * @param value The current progress value
     */
    public void updateProgress(int value) {
        SwingUtilities.invokeLater(() -> {
            if (progressBar != null && !progressBar.isIndeterminate()) {
                progressBar.setValue(value);
            }
        });
    }
    
    /**
     * Update progress with message
     * @param value The current progress value
     * @param message The progress message
     */
    public void updateProgress(int value, String message) {
        SwingUtilities.invokeLater(() -> {
            setStatus(message);
            if (progressBar != null && !progressBar.isIndeterminate()) {
                progressBar.setValue(value);
            }
        });
    }
    
    /**
     * Update progress percentage
     * @param percentage The progress percentage (0-100)
     */
    public void updateProgressPercentage(int percentage) {
        SwingUtilities.invokeLater(() -> {
            if (progressBar != null) {
                progressBar.setValue(percentage);
                progressBar.setString(percentage + "%");
            }
        });
    }
    
    /**
     * Stop progress indication
     */
    public void stopProgress() {
        SwingUtilities.invokeLater(() -> {
            if (progressBar != null) {
                progressBar.setIndeterminate(false);
                progressBar.setValue(0);
                progressBar.setVisible(false);
            }
            setStatus(UIConstants.STATUS_READY);
        });
    }
    
    /**
     * Stop progress with completion message
     * @param message The completion message
     */
    public void stopProgress(String message) {
        SwingUtilities.invokeLater(() -> {
            if (progressBar != null) {
                progressBar.setIndeterminate(false);
                progressBar.setValue(progressBar.getMaximum());
                progressBar.setVisible(false);
            }
            setSuccessStatus(message);
        });
    }
    
    /**
     * Show progress bar
     */
    public void showProgressBar() {
        SwingUtilities.invokeLater(() -> {
            if (progressBar != null) {
                progressBar.setVisible(true);
            }
        });
    }
    
    /**
     * Hide progress bar
     */
    public void hideProgressBar() {
        SwingUtilities.invokeLater(() -> {
            if (progressBar != null) {
                progressBar.setVisible(false);
            }
        });
    }
    
    /**
     * Reset all status indicators
     */
    public void reset() {
        SwingUtilities.invokeLater(() -> {
            setStatus(UIConstants.STATUS_READY);
            if (statusLabel != null) {
                statusLabel.setForeground(UIConstants.TEXT_COLOR);
            }
            if (progressBar != null) {
                progressBar.setValue(0);
                progressBar.setVisible(false);
            }
            if (configurationStatusLabel != null) {
                configurationStatusLabel.setText("");
            }
        });
    }
    
    /**
     * Schedule a delayed status update
     * @param message The status message
     * @param delay The delay in milliseconds
     */
    public void scheduleStatusUpdate(String message, long delay) {
        scheduler.schedule(() -> {
            setStatus(message);
        }, delay, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Display a status with automatic progress simulation
     * @param message The status message
     * @param duration The duration for the simulated progress
     */
    public void simulateProgress(String message, int duration) {
        startProgress(message, 100);
        
        int steps = 20;
        int stepDelay = duration / steps;
        
        for (int i = 0; i <= steps; i++) {
            final int progress = (i * 100) / steps;
            scheduler.schedule(() -> {
                updateProgress(progress);
            }, i * stepDelay, TimeUnit.MILLISECONDS);
        }
        
        scheduler.schedule(() -> {
            stopProgress(UIConstants.STATUS_COMPLETE);
        }, duration, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Clean up resources
     */
    public void cleanup() {
        if (statusTimer != null) {
            statusTimer.stop();
        }
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(1, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
    
    /**
     * Get the current status text
     * @return The current status message
     */
    public String getCurrentStatus() {
        return statusLabel != null ? statusLabel.getText() : "";
    }
    
    /**
     * Check if progress is active
     * @return true if progress bar is visible
     */
    public boolean isProgressActive() {
        return progressBar != null && progressBar.isVisible();
    }
}
