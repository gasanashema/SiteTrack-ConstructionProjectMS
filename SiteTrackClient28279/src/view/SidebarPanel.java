package view;

import session.SessionManager;
import javax.swing.*;
import java.awt.*;
import java.net.URL;
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
            addMenuButton("📊", "Dashboard", "DashboardPanel");
            addMenuButton("📁", "Projects", "ProjectPanel");
            addMenuButton("📦", "Materials", "MaterialPanel");
            addMenuButton("📈", "Stock & Purchases", "StockPanel");
            addMenuButton("📋", "Daily Usage", "DailyUsageDialog");
            addMenuButton("🕒", "Usage History", "UsageHistoryPanel");
            addMenuButton("🏗", "Workers", "WorkerPanel");
            addMenuButton("📋", "Assignments", "WorkerAssignmentPanel");
            addMenuButton("✓", "Attendance", "AttendancePanel");
            addMenuButton("💰", "Payroll", "PayrollPanel");
            addMenuButton("📈", "Reports", "ReportsPanel");

            add(Box.createRigidArea(new Dimension(0, 15)));
            JLabel adminMenuLabel = new JLabel("ADMIN TOOLS");
            adminMenuLabel.setFont(new Font("Ubuntu", Font.BOLD, 12));
            adminMenuLabel.setForeground(Color.decode("#8A94A6"));
            adminMenuLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(adminMenuLabel);
            add(Box.createRigidArea(new Dimension(0, 5)));

            addMenuButton("👥", "User Management", "UserManagementPanel");
            addMenuButton("📜", "Audit Log", "AuditLogPanel");
            addMenuButton("📃", "Logs", "LogViewerPanel");
        } else if (SessionManager.getInstance().isSiteManager()) {
            addMenuButton("📊", "Dashboard", "DashboardPanel");
            addMenuButton("📁", "My Projects", "ProjectPanel");
            addMenuButton("📦", "Materials", "MaterialPanel");
            addMenuButton("📈", "Stock & Purchases", "StockPanel");
            addMenuButton("📋", "Daily Usage", "DailyUsageDialog");
            addMenuButton("🕒", "Usage History", "UsageHistoryPanel");
            addMenuButton("🏗", "Workers", "WorkerPanel");
            addMenuButton("✓", "Attendance", "AttendancePanel");
            addMenuButton("💰", "Payroll", "PayrollPanel");
            addMenuButton("📈", "Reports", "ReportsPanel");
        }
        
        revalidate();
        repaint();
    }

    private void addMenuButton(String emoji, String title, String panelName) {
        JButton btn = new JButton(" " + title);
        btn.setIcon(new EmojiIcon(emoji, 16));
        btn.setMaximumSize(new Dimension(220, 45));
        btn.setPreferredSize(new Dimension(220, 45));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        // Use Segoe UI for the text
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btn.setFocusPainted(false);
        btn.setBackground(Color.decode("#1f242e")); 
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addActionListener(e -> {
            if ("DailyUsageDialog".equals(panelName)) {
                new view.materials.UsageFormDialog(mainFrame, new controller.MaterialController()).setVisible(true);
            } else {
                mainFrame.switchPanel(panelName);
                highlightButton(btn);
            }
        });
        
        menuButtons.add(btn);
        add(btn);
        add(Box.createRigidArea(new Dimension(0, 5)));
    }

    private void highlightButton(JButton activeBtn) {
        for (JButton btn : menuButtons) {
            if (btn == activeBtn) {
                btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
                btn.setBackground(Color.decode("#FF5E14")); 
                btn.setForeground(Color.WHITE);
            } else {
                btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
                btn.setBackground(Color.decode("#1f242e"));
                btn.setForeground(Color.WHITE);
            }
        }
    }

    // Custom Icon to safely draw emojis using Segoe UI Emoji font
    private static class EmojiIcon implements Icon {
        private String emoji;
        private int size;
        private Font font;

        public EmojiIcon(String emoji, int size) {
            this.emoji = emoji;
            this.size = size;
            this.font = new Font("Segoe UI Emoji", Font.PLAIN, size);
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setFont(font);
            // On Windows, Segoe UI Emoji automatically handles its own colors for standard emojis
            // But we set the foreground just in case for monochromatic symbols
            g2.setColor(c.getForeground());
            FontMetrics fm = g2.getFontMetrics();
            // Draw vertically centered
            int textY = y + ((size - fm.getHeight()) / 2) + fm.getAscent();
            g2.drawString(emoji, x, textY);
            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return size + 4; // Add a little padding to the right
        }

        @Override
        public int getIconHeight() {
            return size;
        }
    }
}
