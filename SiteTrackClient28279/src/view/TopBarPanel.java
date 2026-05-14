package view;

import session.SessionManager;
import javax.swing.*;
import java.awt.*;

public class TopBarPanel extends JPanel {
    private MainFrame mainFrame;
    private JLabel userInfoLabel;
    private JButton themeToggleBtn;
    private JButton logoutBtn;

    public TopBarPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, 60));
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#E0E0E0")));
        
        // Left side - Title/Logo area
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        
        JLabel appTitle = new JLabel("SiteTrack Construction Manager");
        appTitle.setFont(new Font("Ubuntu", Font.BOLD, 18));
        appTitle.setForeground(UIManager.getColor("Label.foreground"));
        leftPanel.add(appTitle);
        
        // Right side - User info and actions
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 12));
        
        userInfoLabel = new JLabel();
        userInfoLabel.setFont(new Font("Ubuntu", Font.PLAIN, 14));
        userInfoLabel.setForeground(Color.decode("#5F6368"));
        
        themeToggleBtn = new JButton("☀ Light");
        themeToggleBtn.setFocusPainted(false);
        themeToggleBtn.setFont(new Font("Ubuntu", Font.PLAIN, 13));
        themeToggleBtn.addActionListener(e -> toggleTheme());
        
        logoutBtn = new JButton("Logout");
        logoutBtn.setFocusPainted(false);
        logoutBtn.setFont(new Font("Ubuntu", Font.BOLD, 13));
        logoutBtn.setForeground(Color.decode("#D93025")); // Red text
        logoutBtn.setBorder(BorderFactory.createLineBorder(Color.decode("#D93025")));
        logoutBtn.setContentAreaFilled(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setPreferredSize(new Dimension(80, 32));
        logoutBtn.addActionListener(e -> mainFrame.handleLogout());
        
        rightPanel.add(userInfoLabel);
        rightPanel.add(themeToggleBtn);
        rightPanel.add(logoutBtn);
        
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
    }
    
    public void updateUserInfo() {
        if (SessionManager.getInstance().isLoggedIn()) {
            String name = SessionManager.getInstance().getCurrentUserFullName();
            String role = SessionManager.getInstance().getCurrentUserRole();
            userInfoLabel.setText("Welcome, " + name + " (" + role + ")");
        } else {
            userInfoLabel.setText("");
        }
    }
    
    private void toggleTheme() {
        boolean isDark = UIManager.getBoolean("laf.dark");
        if (isDark) {
            mainFrame.setTheme("light");
            themeToggleBtn.setText("☀ Light");
        } else {
            mainFrame.setTheme("dark");
            themeToggleBtn.setText("🌙 Dark");
        }
        
        // We need to trigger a repaint/revalidate to update custom components that check the theme manually
        SwingUtilities.invokeLater(() -> {
            mainFrame.revalidate();
            mainFrame.repaint();
        });
    }
}
