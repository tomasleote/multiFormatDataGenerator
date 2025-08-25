
/**
 * Fallback Main class without FlatLaf dependency
 * This version will work even if FlatLaf fails to load
 */
public class MainFallback {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            System.out.println("Starting Multi-Format Data Generator - Phase 2 Enhanced UI");
            System.out.println("Using system default theme (FlatLaf disabled for compatibility)");
            
            // Create and display the main UI
            com.view.GeneratorUI frame = new com.view.GeneratorUI();
            frame.setVisible(true);
        });
    }
}
