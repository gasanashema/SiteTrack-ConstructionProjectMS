package view;

import session.SessionManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TopBarPanel extends JPanel {
    private MainFrame mainFrame;
    private JLabel appName;
    private JLabel userName;
    private JButton themeToggle;
    private JButton logoutButton;
    private boolean isLightMode = true;

    public TopBarPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, 60));
        
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#E0E0E0")));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        leftPanel.setOpaque(false);
        appName = new JLabel("SiteTrack Construction Manager");
        appName.setFont(new Font("Ubuntu", Font.BOLD, 18));
        appName.setForeground(Color.decode("#1f242e"));
        leftPanel.add(appName);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        rightPanel.setOpaque(false);
        
        userName = new JLabel("");
        userName.setFont(new Font("Ubuntu", Font.PLAIN, 14));
        userName.setForeground(Color.decode("#1f242e"));
        
        themeToggle = new JButton("🌙 Dark");
        themeToggle.setBackground(Color.WHITE);
        themeToggle.setForeground(Color.decode("#1f242e"));
        themeToggle.setFocusPainted(false);
        themeToggle.setCursor(new Cursor(Cursor.HAND_CURSOR));
        themeToggle.addActionListener((ActionEvent e) -> {
            if (isLightMode) {
                mainFrame.setTheme("dark");
                themeToggle.setText("☀️ Light");
            } else {
                mainFrame.setTheme("light");
                themeToggle.setText("🌙 Dark");
            }
            isLightMode = !isLightMode;
        });
        
        logoutButton = new JButton("Logout");
        logoutButton.setBackground(Color.WHITE);
        logoutButton.setForeground(Color.RED);
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(e -> mainFrame.handleLogout());

        rightPanel.add(userName);
        rightPanel.add(themeToggle);
        rightPanel.add(logoutButton);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
    }
    
    public void updateUserInfo() {
        if (SessionManager.getInstance().isLoggedIn()) {
            userName.setText("Welcome, " + SessionManager.getInstance().getCurrentUserName() + 
                " (" + SessionManager.getInstance().getCurrentUserRole() + ")");
        } else {
            userName.setText("");
        }
    }
}
