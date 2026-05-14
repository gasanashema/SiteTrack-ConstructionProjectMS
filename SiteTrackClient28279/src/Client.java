import com.formdev.flatlaf.FlatLightLaf;
import config.RMIConnection;
import view.MainFrame;
import view.auth.LoginPanel;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Font;
import java.awt.Color;

public class Client {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Client());
    }

    public Client() {
        // 1. Set FlatLaf theme
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            // Set Ubuntu font globally
            UIManager.put("defaultFont", new Font("Ubuntu", Font.PLAIN, 14));
            
            // Override FlatLaf defaults
            UIManager.put("Button.background", Color.decode("#FF5E14")); // Primary Orange
            UIManager.put("Button.foreground", Color.WHITE);
            UIManager.put("Button.arc", 8); // slightly rounded
            UIManager.put("TextComponent.arc", 8);
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

        // 2 & 3. Verify RMI connection
        try {
            RMIConnection.getInstance().verifyConnection();
        } catch (RuntimeException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Failed to connect to server:\n" + e.getMessage(),
                "Connection Error",
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // 4. Create MainFrame
        MainFrame mainFrame = new MainFrame();

        // 5. Display LoginPanel inside MainFrame
        LoginPanel loginPanel = new LoginPanel(mainFrame);
        mainFrame.addPanel("LoginPanel", loginPanel);
        mainFrame.switchPanel("LoginPanel");

        // 6 & 7. Set default close operation and pack frame
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }
}
