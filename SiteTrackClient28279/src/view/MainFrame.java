package view;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import controller.AuthController;
import javax.swing.*;
import java.awt.*;
import session.SessionManager;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private SidebarPanel sidebar;
    private TopBarPanel topBar;
    private String currentPanelName;
    private AuthController authController;

    public MainFrame() {
        super("SiteTrack Construction Manager");
        this.authController = new AuthController();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1400, 900));
        setLayout(new BorderLayout());

        // Set Application Icon
        try {
            java.net.URL iconURL = getClass().getResource("/resources/logo.png");
            if (iconURL != null) {
                setIconImage(new ImageIcon(iconURL).getImage());
            }
        } catch (Exception e) {
            // Ignore if icon fails to load
        }

        // Initialize components
        topBar = new TopBarPanel(this);
        sidebar = new SidebarPanel(this);
        
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);


        add(topBar, BorderLayout.NORTH);
        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
        
        topBar.setVisible(false);
        sidebar.setVisible(false);
    }

    public void switchPanel(String panelName) {
        cardLayout.show(contentPanel, panelName);
        currentPanelName = panelName;
        
        // Show/hide sidebar and topbar based on whether we are logged in
        if (SessionManager.getInstance().isLoggedIn()) {
            topBar.setVisible(true);
            sidebar.setVisible(true);
            topBar.updateUserInfo();
            sidebar.updateMenu();
        } else {
            topBar.setVisible(false);
            sidebar.setVisible(false);
        }
    }

    public void addPanel(String name, JPanel panel) {
        contentPanel.add(panel, name);
    }

    public void setTheme(String themeName) {
        try {
            if ("light".equalsIgnoreCase(themeName)) {
                UIManager.setLookAndFeel(new FlatLightLaf());
            } else if ("dark".equalsIgnoreCase(themeName)) {
                UIManager.setLookAndFeel(new FlatDarkLaf());
            }
            SwingUtilities.updateComponentTreeUI(this);
            repaint();
        } catch (Exception ex) {
            System.err.println("Failed to switch theme");
        }
    }

    public void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to logout?", "Confirm Logout", 
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            authController.logout(this);
            // Re-add or reset panels if needed to clear sensitive data
        }
    }
}
