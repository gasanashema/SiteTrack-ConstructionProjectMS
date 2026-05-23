package view.dashboard;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class KpiCard extends JPanel {
    private String title;
    private String value;
    private String subtitle;
    private int styleIndex;
    private String icon;
    private String trend; // "UP", "DOWN", "STABLE"
    private List<Number> sparklineData;
    
    private JLabel valueLabel;
    private JLabel trendLabel;
    private JPanel sparklinePanel;

    public KpiCard(String title, String value, String subtitle) {
        this(title, value, subtitle, 0); // Default style
    }

    public KpiCard(String title, String value, String subtitle, int styleIndex) {
        this(title, value, subtitle, styleIndex, null, null);
    }
    
    public KpiCard(String title, String value, String subtitle, int styleIndex, String icon, List<Number> sparklineData) {
        this.title = title;
        this.value = value;
        this.subtitle = subtitle;
        this.styleIndex = styleIndex;
        this.icon = icon;
        this.sparklineData = sparklineData;
        initUI();
    }

    private void initUI() {
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(220, 140));
        setMinimumSize(new Dimension(220, 140));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1.0;

        // Row 0: Icon + Title
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        topPanel.setOpaque(false);
        if (icon != null && !icon.isEmpty()) {
            JLabel iconLabel = new JLabel(icon);
            iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
            topPanel.add(iconLabel);
        }
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(UIManager.getColor("Label.disabledForeground"));
        topPanel.add(titleLabel);
        
        gbc.gridx = 0; gbc.gridy = 0;
        add(topPanel, gbc);

        // Row 1: Value + Sparkline
        JPanel middlePanel = new JPanel(new BorderLayout());
        middlePanel.setOpaque(false);
        
        valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(UIManager.getColor("Label.foreground"));
        middlePanel.add(valueLabel, BorderLayout.WEST);
        
        if (sparklineData != null && !sparklineData.isEmpty()) {
            sparklinePanel = ChartUtil.createSparkline(sparklineData, getTrendColor(), 80, 30);
            middlePanel.add(sparklinePanel, BorderLayout.EAST);
        }
        
        gbc.gridy = 1; gbc.insets = new Insets(10, 0, 5, 0);
        add(middlePanel, gbc);

        // Row 2: Trend + Subtitle
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        bottomPanel.setOpaque(false);
        
        trendLabel = new JLabel("");
        trendLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        bottomPanel.add(trendLabel);
        
        if (subtitle != null && !subtitle.trim().isEmpty()) {
            JLabel subtitleLabel = new JLabel(subtitle);
            subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            subtitleLabel.setForeground(UIManager.getColor("Label.disabledForeground"));
            bottomPanel.add(subtitleLabel);
        }
        
        gbc.gridy = 2; gbc.insets = new Insets(0, 0, 0, 0);
        add(bottomPanel, gbc);
        
        updateTrendUI();
    }

    public void setValue(String newValue) {
        this.value = newValue;
        if (valueLabel != null) {
            valueLabel.setText(newValue);
        }
    }

    public void setTrend(String trend) {
        this.trend = trend;
        updateTrendUI();
    }
    
    private void updateTrendUI() {
        if (trendLabel == null) return;
        if ("UP".equalsIgnoreCase(trend)) {
            trendLabel.setText("↑");
            trendLabel.setForeground(Color.decode("#4CAF50")); // Green
        } else if ("DOWN".equalsIgnoreCase(trend)) {
            trendLabel.setText("↓");
            trendLabel.setForeground(Color.decode("#F44336")); // Red
        } else if ("STABLE".equalsIgnoreCase(trend)) {
            trendLabel.setText("→");
            trendLabel.setForeground(Color.GRAY);
        } else {
            trendLabel.setText("");
        }
    }
    
    public void setMiniChart(List<Number> data) {
        this.sparklineData = data;
        // Logic to replace or update sparklinePanel would go here
    }

    private Color getTrendColor() {
        if ("DOWN".equalsIgnoreCase(trend)) return Color.decode("#F44336");
        if ("STABLE".equalsIgnoreCase(trend)) return Color.GRAY;
        return Color.decode("#4CAF50"); // Default green
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        boolean isDark = UIManager.getBoolean("laf.dark");
        Color bgColor;
        Color borderColor;

        if (isDark) {
            borderColor = Color.decode("#3C3F41");
            bgColor = Color.decode("#2B2D30"); // Solid dark color for dark mode
        } else {
            borderColor = UIManager.getColor("Component.borderColor");
            bgColor = Color.WHITE; // Full white
        }

        int radius = 25; // More rounded border

        // Draw Background
        g2.setColor(bgColor);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        
        // Draw Border
        g2.setStroke(new BasicStroke(1.5f));
        g2.setColor(borderColor);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        
        g2.dispose();
    }
}
