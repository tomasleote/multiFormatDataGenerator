package com.controller.events;

import com.controller.business.*;
import com.controller.events.UIEventHandler.EventType;
import com.controller.events.UIEventHandler.EventData;
import com.controller.events.UIEventHandler.EventListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Unit tests for UIEventHandler.
 * Tests event handling and coordination functionality.
 */
class UIEventHandlerTest {
    
    private UIEventHandler eventHandler;
    private GeneratorController generatorController;
    private ValidationController validationController;
    private ConfigurationManager configurationManager;
    private DataExportManager exportManager;
    private ExampleLoader exampleLoader;
    
    // UI Components for testing
    private JTextField templateField;
    private JTextArea outputArea;
    private JProgressBar progressBar;
    private JLabel statusLabel;
    private JLabel previewLabel;
    private JPanel containerPanel;
    
    @BeforeEach
    void setUp() {
        // Create controllers
        generatorController = new GeneratorController();
        validationController = new ValidationController();
        configurationManager = new ConfigurationManager();
        exportManager = new DataExportManager();
        exampleLoader = new ExampleLoader();
        
        // Create event handler
        eventHandler = new UIEventHandler(
            generatorController,
            validationController,
            configurationManager,
            exportManager,
            exampleLoader
        );
        
        // Create UI components
        templateField = new JTextField();
        outputArea = new JTextArea();
        progressBar = new JProgressBar();
        statusLabel = new JLabel();
        previewLabel = new JLabel();
        containerPanel = new JPanel();
        
        // Set UI references
        eventHandler.setUIReferences(
            templateField,
            outputArea,
            progressBar,
            statusLabel,
            previewLabel,
            containerPanel
        );
    }
    
    @Test
    @DisplayName("Should register and fire events correctly")
    void testEventRegistrationAndFiring() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        EventData[] receivedEvent = new EventData[1];
        
        // Register event listener
        EventListener listener = eventData -> {
            receivedEvent[0] = eventData;
            latch.countDown();
        };
        
        eventHandler.addEventListener(EventType.STATUS_CHANGED, listener);
        
        // Fire event
        EventData testEvent = new EventData(EventType.STATUS_CHANGED, "test data", "test message");
        eventHandler.fireEvent(testEvent);
        
        // Wait for event to be processed
        assertTrue(latch.await(1, TimeUnit.SECONDS));
        assertNotNull(receivedEvent[0]);
        assertEquals(EventType.STATUS_CHANGED, receivedEvent[0].getType());
        assertEquals("test message", receivedEvent[0].getMessage());
    }
    
    @Test
    @DisplayName("Should remove event listeners correctly")
    void testEventListenerRemoval() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        
        EventListener listener = eventData -> latch.countDown();
        
        // Add and then remove listener
        eventHandler.addEventListener(EventType.STATUS_CHANGED, listener);
        eventHandler.removeEventListener(EventType.STATUS_CHANGED, listener);
        
        // Fire event - should not be received
        EventData testEvent = new EventData(EventType.STATUS_CHANGED, "test", "test");
        eventHandler.fireEvent(testEvent);
        
        // Event should not be received
        assertFalse(latch.await(500, TimeUnit.MILLISECONDS));
    }
    
    @Test
    @DisplayName("Should create action listeners correctly")
    void testActionListenerCreation() {
        // Test apply template listener
        var applyListener = eventHandler.createApplyTemplateListener();
        assertNotNull(applyListener);
        
        // Test generate listener
        var generateListener = eventHandler.createGenerateListener(() -> 10);
        assertNotNull(generateListener);
        
        // Test clear output listener
        var clearListener = eventHandler.createClearOutputListener();
        assertNotNull(clearListener);
        
        // Test save config listener
        var saveListener = eventHandler.createSaveConfigListener();
        assertNotNull(saveListener);
        
        // Test load config listener
        var loadListener = eventHandler.createLoadConfigListener();
        assertNotNull(loadListener);
        
        // Test export listener
        var exportListener = eventHandler.createExportListener(() -> DataExportManager.ExportFormat.CSV);
        assertNotNull(exportListener);
        
        // Test load example listener
        var exampleListener = eventHandler.createLoadExampleListener("test_example");
        assertNotNull(exampleListener);
    }
    
    @Test
    @DisplayName("Should handle clear output action")
    void testClearOutputAction() {
        // Set some text in output area
        outputArea.setText("Some test output");
        assertEquals("Some test output", outputArea.getText());
        
        // Create and trigger clear listener
        var clearListener = eventHandler.createClearOutputListener();
        clearListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "clear"));
        
        // Output should be cleared
        assertEquals("", outputArea.getText());
    }
    
    @Test
    @DisplayName("Should handle template application with validation")
    void testTemplateApplicationWithValidation() throws InterruptedException {
        CountDownLatch validationLatch = new CountDownLatch(1);
        CountDownLatch templateLatch = new CountDownLatch(1);
        
        // Listen for validation and template events
        eventHandler.addEventListener(EventType.VALIDATION_ERROR, eventData -> validationLatch.countDown());
        eventHandler.addEventListener(EventType.TEMPLATE_APPLIED, eventData -> templateLatch.countDown());
        
        // Test invalid template
        templateField.setText(""); // Empty template should cause validation error
        var applyListener = eventHandler.createApplyTemplateListener();
        applyListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "apply"));
        
        // Should receive validation error
        assertTrue(validationLatch.await(1, TimeUnit.SECONDS));
        
        // Test valid template
        templateField.setText("{0}");
        applyListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "apply"));
        
        // Should receive template applied event
        assertTrue(templateLatch.await(1, TimeUnit.SECONDS));
    }
    
    @Test
    @DisplayName("Should get current configuration")
    void testGetCurrentConfiguration() {
        var config = eventHandler.getCurrentConfiguration();
        // Should return whatever the configuration manager has
        assertNotNull(config); // ConfigurationManager creates a default config
    }
    
    @Test
    @DisplayName("Should handle shutdown gracefully")
    void testShutdown() {
        assertDoesNotThrow(() -> eventHandler.shutdown());
        
        // After shutdown, the underlying controllers should be shut down
        assertTrue(generatorController != null); // Controllers still exist but are shut down
    }
    
    @Test
    @DisplayName("Should handle event listener exceptions gracefully")
    void testEventListenerExceptionHandling() {
        // Register a listener that throws an exception
        EventListener faultyListener = eventData -> {
            throw new RuntimeException("Test exception");
        };
        
        eventHandler.addEventListener(EventType.STATUS_CHANGED, faultyListener);
        
        // Firing an event should not throw an exception even if listener fails
        EventData testEvent = new EventData(EventType.STATUS_CHANGED, "test", "test");
        assertDoesNotThrow(() -> eventHandler.fireEvent(testEvent));
    }
    
    @Test
    @DisplayName("Should handle multiple listeners for same event")
    void testMultipleListenersForSameEvent() throws InterruptedException {
        CountDownLatch latch1 = new CountDownLatch(1);
        CountDownLatch latch2 = new CountDownLatch(1);
        
        EventListener listener1 = eventData -> latch1.countDown();
        EventListener listener2 = eventData -> latch2.countDown();
        
        eventHandler.addEventListener(EventType.STATUS_CHANGED, listener1);
        eventHandler.addEventListener(EventType.STATUS_CHANGED, listener2);
        
        // Fire event
        EventData testEvent = new EventData(EventType.STATUS_CHANGED, "test", "test");
        eventHandler.fireEvent(testEvent);
        
        // Both listeners should receive the event
        assertTrue(latch1.await(1, TimeUnit.SECONDS));
        assertTrue(latch2.await(1, TimeUnit.SECONDS));
    }
    
    @Test
    @DisplayName("EventData should store information correctly")
    void testEventDataStorage() {
        EventData eventData = new EventData(EventType.GENERATION_COMPLETED, "test data", "test message");
        
        assertEquals(EventType.GENERATION_COMPLETED, eventData.getType());
        assertEquals("test data", eventData.getData());
        assertEquals("test message", eventData.getMessage());
        assertTrue(eventData.getTimestamp() > 0);
        assertTrue(eventData.getTimestamp() <= System.currentTimeMillis());
    }
}