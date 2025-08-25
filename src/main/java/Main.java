
/**
 * Main entry point for the Multi-Format Data Generator application.
 * 
 * @version 2.0 - Enhanced User Experience
 * @since 1.0
 */
public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            // Set modern FlatLaf theme
            try {
                // Try to set FlatLaf Dark theme for modern appearance
                com.formdev.flatlaf.FlatDarkLaf.setup();
                
                // Configure FlatLaf properties for better appearance
                javax.swing.UIManager.put("Button.arc", 8);
                javax.swing.UIManager.put("Component.arc", 8);
                javax.swing.UIManager.put("ProgressBar.arc", 8);
                javax.swing.UIManager.put("TextComponent.arc", 8);
                javax.swing.UIManager.put("ScrollBar.trackArc", 8);
                javax.swing.UIManager.put("ScrollBar.thumbArc", 8);
                
                System.out.println("✓ FlatLaf Dark theme applied successfully");
                
            } catch (Exception e) {
                System.err.println("Could not set FlatLaf theme, using default: " + e.getMessage());
                System.out.println("Note: The application will still work but with default styling");
            }
            
            // Create and display the main UI
            com.view.GeneratorUI frame = new com.view.GeneratorUI();
            frame.setVisible(true);
        });
    }
}
