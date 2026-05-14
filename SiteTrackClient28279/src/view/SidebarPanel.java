package view;

import session.SessionManager;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SidebarPanel extends JPanel {
    private MainFrame mainFrame;
    private List<JButton> menuButtons;

    public SidebarPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.menuButtons = new ArrayList<>();
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(240, 0));
        setBackground(Color.decode("#1f242e")); // Dark slate background
        setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
    }

    public void updateMenu() {
        removeAll();
        menuButtons.clear();
        
        JLabel menuLabel = new JLabel("MAIN MENU");
        menuLabel.setFont(new Font("Ubuntu", Font.BOLD, 12));
        menuLabel.setForeground(Color.decode("#8A94A6")); // muted gray
        menuLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(menuLabel);
        add(Box.createRigidArea(new Dimension(0, 15)));
        
        if (SessionManager.getInstance().isAdmin()) {
            addMenuButton("📊 Dashboard", "DashboardPanel");
            addMenuButton("👥 Users", "UserPanel");
            addMenuButton("📁 Projects", "ProjectPanel");
            addMenuButton("📦 Materials", "MaterialPanel");
            addMenuButton("📈 Stock & Usage", "StockPanel");
            addMenuButton("🏗️ Workers", "WorkerPanel");
            addMenuButton("💰 Payroll", "PayrollPanel");
            addMenuButton("📋 Reports", "ReportPanel");
        } else if (SessionManager.getInstance().isSiteManager()) {
            addMenuButton("📊 Dashboard", "DashboardPanel");
            addMenuButton("📁 My Projects", "ProjectPanel");
            addMenuButton("📦 Materials", "MaterialPanel");
            addMenuButton("📈 Stock & Usage", "StockPanel");
            addMenuButton("🏗️ Workers", "WorkerPanel");
            addMenuButton("✓ Attendance", "AttendancePanel");
            addMenuButton("💰 Payroll", "PayrollPanel");
        }
        
        revalidate();
        repaint();
    }

    private void addMenuButton(String title, String panelName) {
        JButton btn = new JButton("  " + title);
        btn.setMaximumSize(new Dimension(220, 45));
        btn.setPreferredSize(new Dimension(220, 45));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(new Font("Ubuntu", Font.PLAIN, 15));
        btn.setFocusPainted(false);
        btn.setBackground(Color.decode("#1f242e")); 
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addActionListener(e -> {
            mainFrame.switchPanel(panelName);
            highlightButton(btn);
        });
        
        menuButtons.add(btn);
        add(btn);
        add(Box.createRigidArea(new Dimension(0, 5)));
    }

    private void highlightButton(JButton activeBtn) {
        for (JButton btn : menuButtons) {
            if (btn == activeBtn) {
                btn.setFont(new Font("Ubuntu", Font.BOLD, 15));
                btn.setBackground(Color.decode("#FF5E14")); 
                btn.setForeground(Color.WHITE);
            } else {
                btn.setFont(new Font("Ubuntu", Font.PLAIN, 15));
                btn.setBackground(Color.decode("#1f242e"));
                btn.setForeground(Color.WHITE);
            }
        }
    }
}
