package view.reports;

import javax.swing.*;
import java.awt.*;

public class ReportsPanel extends JPanel {
    private JTabbedPane tabbedPane;

    public ReportsPanel() {
        setLayout(new BorderLayout());
        setBackground(UIManager.getColor("Panel.background"));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Reports & Analytics");
        titleLabel.setFont(new Font("Ubuntu", Font.BOLD, 24));
        titleLabel.setForeground(UIManager.getColor("Label.foreground"));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        add(titleLabel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Ubuntu", Font.BOLD, 14));

        tabbedPane.addTab("Material Usage", new javax.swing.ImageIcon(), new MaterialUsageReportPanel(), "View material consumption over time");
        tabbedPane.addTab("Stock Movement", new javax.swing.ImageIcon(), new StockMovementReportPanel(), "View IN, OUT, and ADJUSTMENT logs");
        tabbedPane.addTab("Labor Cost", new javax.swing.ImageIcon(), new LaborCostReportPanel(), "View worker payments and wages");
        tabbedPane.addTab("Attendance", new javax.swing.ImageIcon(), new AttendanceReportPanel(), "View worker attendance records");
        tabbedPane.addTab("Project Summary", new javax.swing.ImageIcon(), new ProjectSummaryReportPanel(), "Comprehensive project dashboard");

        add(tabbedPane, BorderLayout.CENTER);
    }
}
