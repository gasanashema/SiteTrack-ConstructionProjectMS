package view.dashboard;

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {
    public DashboardPanel() {
        setLayout(new BorderLayout());
        
        JLabel title = new JLabel("Dashboard Overview", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        add(title, BorderLayout.CENTER);
    }
}
