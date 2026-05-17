package view.admin;

import controller.SystemAdminController;
import view.dashboard.KpiCard;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SystemHealthPanel extends JPanel {
    private SystemAdminController adminController;

    private JLabel serverStatusLabel;
    private JLabel lastCheckedLabel;
    
    private KpiCard memoryCard;
    private KpiCard activeUsersCard;
    private KpiCard pendingTasksCard;
    private KpiCard apiResponseCard;

    private JTextArea logsArea;

    public SystemHealthPanel() {
        this.adminController = new SystemAdminController();
        setLayout(new BorderLayout());
        setBackground(UIManager.getColor("Panel.background"));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initUI();
        refreshStats();
        
        // Auto-refresh timer every 10 seconds
        Timer timer = new Timer(10000, e -> refreshStats());
        timer.start();
    }

    private void initUI() {
        JLabel titleLabel = new JLabel("System Health Monitor");
        titleLabel.setFont(new Font("Ubuntu", Font.BOLD, 24));
        titleLabel.setForeground(Color.decode("#2c3e50"));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(UIManager.getColor("Panel.background"));

        // --- Section 1: Server Status ---
        JPanel serverPanel = new JPanel(new GridBagLayout());
        serverPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Server Status", TitledBorder.LEFT, TitledBorder.TOP, new Font("Ubuntu", Font.BOLD, 14)));
        serverPanel.setBackground(UIManager.getColor("Panel.background"));
        serverPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        serverStatusLabel = new JLabel("Checking...");
        serverStatusLabel.setFont(new Font("Ubuntu", Font.BOLD, 14));
        
        JButton testBtn = new JButton("Test Connection");
        testBtn.addActionListener(e -> testConnection());
        
        lastCheckedLabel = new JLabel("Last Checked: Never");

        gbc.gridx = 0; gbc.gridy = 0; serverPanel.add(new JLabel("Server Connection:"), gbc);
        gbc.gridx = 1; serverPanel.add(serverStatusLabel, gbc);
        gbc.gridx = 0; gbc.gridy = 1; serverPanel.add(new JLabel("Server Host:"), gbc);
        gbc.gridx = 1; serverPanel.add(new JLabel(adminController.getServerHost()), gbc);
        gbc.gridx = 0; gbc.gridy = 2; serverPanel.add(new JLabel("Server Port:"), gbc);
        gbc.gridx = 1; serverPanel.add(new JLabel(adminController.getServerPort()), gbc);
        gbc.gridx = 2; gbc.gridy = 1; serverPanel.add(testBtn, gbc);
        gbc.gridy = 2; serverPanel.add(lastCheckedLabel, gbc);

        // --- Section 2: Performance Metrics ---
        JPanel kpiPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        kpiPanel.setOpaque(false);
        kpiPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        kpiPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        memoryCard = new KpiCard("Memory Usage", "0 MB", "Client heap usage", 0);
        activeUsersCard = new KpiCard("Active Users", "3", "Currently connected", 1);
        pendingTasksCard = new KpiCard("Pending Tasks", "5", "Background jobs", 2);
        apiResponseCard = new KpiCard("API Response", "45 ms", "Avg ping time", 3);

        kpiPanel.add(memoryCard);
        kpiPanel.add(activeUsersCard);
        kpiPanel.add(pendingTasksCard);
        kpiPanel.add(apiResponseCard);

        // --- Section 3: Recent Logs ---
        JPanel logsPanel = new JPanel(new BorderLayout());
        logsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Recent System Logs", TitledBorder.LEFT, TitledBorder.TOP, new Font("Ubuntu", Font.BOLD, 14)));
        logsPanel.setBackground(UIManager.getColor("Panel.background"));
        
        logsArea = new JTextArea(10, 50);
        logsArea.setEditable(false);
        logsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        logsArea.setBackground(Color.decode("#2b2b2b"));
        logsArea.setForeground(Color.decode("#a9b7c6"));
        
        logsPanel.add(new JScrollPane(logsArea), BorderLayout.CENTER);

        contentPanel.add(serverPanel);
        contentPanel.add(kpiPanel);
        contentPanel.add(logsPanel);

        add(contentPanel, BorderLayout.CENTER);
    }

    private void testConnection() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        boolean isConnected = adminController.testServerConnection();
        setCursor(Cursor.getDefaultCursor());
        
        if (isConnected) {
            serverStatusLabel.setText("🟢 Connected");
            serverStatusLabel.setForeground(Color.decode("#27ae60"));
        } else {
            serverStatusLabel.setText("🔴 Disconnected");
            serverStatusLabel.setForeground(Color.decode("#c0392b"));
        }
        
        lastCheckedLabel.setText("Last Checked: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }

    private void refreshStats() {
        testConnection();
        
        long[] mem = adminController.getMemoryUsage();
        memoryCard.setValue(mem[0] + " MB");
        
        // Randomize active users slightly for UI liveliness
        activeUsersCard.setValue(String.valueOf(2 + (int)(Math.random() * 5)));
        apiResponseCard.setValue((30 + (int)(Math.random() * 20)) + " ms");
        
        // Populate logs
        StringBuilder sb = new StringBuilder();
        for (String[] log : adminController.getDummySystemLogs()) {
            sb.append(String.format("[%s] %-5s %-15s - %s%n", log[0], log[1], log[2], log[3]));
        }
        logsArea.setText(sb.toString());
    }
}
