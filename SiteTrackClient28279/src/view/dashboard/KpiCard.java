package view.dashboard;

import javax.swing.*;
import java.awt.*;

public class KpiCard extends JPanel {
    private String title;
    private String value;
    private String subtitle;
    private int styleIndex;

    public KpiCard(String title, String value, String subtitle) {
        this(title, value, subtitle, 0); // Default style
    }

    public KpiCard(String title, String value, String subtitle, int styleIndex) {
        this.title = title;
        this.value = value;
        this.subtitle = subtitle;
        this.styleIndex = styleIndex;
        initUI();
    }

    private void initUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(180, 120));
        setMinimumSize(new Dimension(180, 120));
        setOpaque(false); 
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(UIManager.getColor("Label.disabledForeground"));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(UIManager.getColor("Label.foreground"));
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(titleLabel);
        add(Box.createVerticalStrut(10));
        add(valueLabel);

        if (subtitle != null && !subtitle.trim().isEmpty()) {
            JLabel subtitleLabel = new JLabel(subtitle);
            subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            subtitleLabel.setForeground(UIManager.getColor("Label.disabledForeground"));
            subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            add(Box.createVerticalStrut(5));
            add(subtitleLabel);
        }
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
            switch (styleIndex) {
                case 1: bgColor = Color.decode("#4A3030"); break; // Dark red/pink
                case 2: bgColor = Color.decode("#2E4233"); break; // Dark green
                case 3: bgColor = Color.decode("#4A4228"); break; // Dark yellow/orange
                default: bgColor = Color.decode("#2B3340"); break; // Dark blue
            }
        } else {
            borderColor = Color.decode("#DADCE0");
            switch (styleIndex) {
                case 1: bgColor = Color.decode("#FCE8E6"); break; // Light red/pink
                case 2: bgColor = Color.decode("#E6F4EA"); break; // Light green
                case 3: bgColor = Color.decode("#FEF7E0"); break; // Light yellow
                default: bgColor = Color.decode("#E8F0FE"); break; // Light blue
            }
        }

        g2.setColor(bgColor);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
        
        g2.setColor(borderColor);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
        
        g2.dispose();
    }
}
