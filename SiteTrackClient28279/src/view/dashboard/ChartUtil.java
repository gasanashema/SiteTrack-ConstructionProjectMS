package view.dashboard;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
import java.util.Locale;
import java.util.Collections;

public class ChartUtil {

    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "RW"));
    
    // Sparkline
    public static JPanel createSparkline(List<Number> values, Color lineColor, int width, int height) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (values == null || values.size() < 2) return;
                
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                double min = Double.MAX_VALUE;
                double max = Double.MIN_VALUE;
                for (Number n : values) {
                    double v = n.doubleValue();
                    if (v < min) min = v;
                    if (v > max) max = v;
                }
                if (max == min) {
                    max += 1;
                    min -= 1;
                }
                
                double xStep = (double) getWidth() / (values.size() - 1);
                double yRange = max - min;
                
                Path2D path = new Path2D.Double();
                for (int i = 0; i < values.size(); i++) {
                    double x = i * xStep;
                    double y = getHeight() - ((values.get(i).doubleValue() - min) / yRange * getHeight());
                    
                    if (i == 0) {
                        path.moveTo(x, y);
                    } else {
                        path.lineTo(x, y);
                    }
                }
                
                g2.setColor(lineColor);
                g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.draw(path);
                
                // Draw endpoint marker
                double lastX = (values.size() - 1) * xStep;
                double lastY = getHeight() - ((values.get(values.size() - 1).doubleValue() - min) / yRange * getHeight());
                g2.fillOval((int)lastX - 3, (int)lastY - 3, 6, 6);
            }
        };
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(width, height));
        return panel;
    }

    // Pie Chart
    public static JPanel createPieChart2D(String title, Map<String, Number> data, Map<String, Color> colorMap) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth();
                int h = getHeight();
                
                // Draw Title
                g2.setFont(TITLE_FONT);
                g2.setColor(UIManager.getColor("Label.foreground"));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(title, (w - fm.stringWidth(title)) / 2, 25);
                
                if (data == null || data.isEmpty()) {
                    g2.setFont(LABEL_FONT);
                    g2.setColor(Color.GRAY);
                    g2.drawString("No data available", w/2 - 40, h/2);
                    g2.dispose();
                    return;
                }
                
                double total = data.values().stream().mapToDouble(Number::doubleValue).sum();
                if (total <= 0) total = 1;
                
                int pieSize = Math.min(w - 150, h - 60);
                int pieX = 20;
                int pieY = 40;
                
                double currentAngle = 90; // Start at 12 o'clock
                
                int legendX = pieX + pieSize + 30;
                int legendY = pieY + 20;
                
                for (Map.Entry<String, Number> entry : data.entrySet()) {
                    String label = entry.getKey();
                    double value = entry.getValue().doubleValue();
                    double angle = (value / total) * 360.0;
                    
                    Color color = colorMap.getOrDefault(label, Color.GRAY);
                    g2.setColor(color);
                    
                    // Draw slice
                    g2.fill(new Arc2D.Double(pieX, pieY, pieSize, pieSize, currentAngle, -angle, Arc2D.PIE));
                    currentAngle -= angle;
                    
                    // Draw Legend
                    g2.fillRect(legendX, legendY, 12, 12);
                    g2.setColor(UIManager.getColor("Label.foreground"));
                    g2.setFont(LABEL_FONT);
                    int percent = (int) Math.round((value / total) * 100);
                    g2.drawString(label + " (" + percent + "%)", legendX + 20, legendY + 11);
                    legendY += 25;
                }
                
                g2.dispose();
            }
        };
    }

    // Horizontal Bar Chart
    public static JPanel createHorizontalBarChart2D(String title, Map<String, Number> data, Color barColor, boolean isCurrency) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth();
                int h = getHeight();
                
                g2.setFont(TITLE_FONT);
                g2.setColor(UIManager.getColor("Label.foreground"));
                g2.drawString(title, 20, 25);
                
                if (data == null || data.isEmpty()) {
                    g2.setFont(LABEL_FONT);
                    g2.setColor(Color.GRAY);
                    g2.drawString("No data available", 20, 60);
                    g2.dispose();
                    return;
                }
                
                double max = data.values().stream().mapToDouble(Number::doubleValue).max().orElse(1.0);
                if (max <= 0) max = 1;
                
                int startY = 50;
                int labelWidth = 120;
                int maxBarWidth = w - labelWidth - 80;
                int barHeight = 25;
                int gap = 15;
                
                g2.setFont(LABEL_FONT);
                
                for (Map.Entry<String, Number> entry : data.entrySet()) {
                    String label = entry.getKey();
                    double value = entry.getValue().doubleValue();
                    
                    // Label
                    g2.setColor(UIManager.getColor("Label.foreground"));
                    FontMetrics fm = g2.getFontMetrics();
                    String trimmed = label.length() > 15 ? label.substring(0, 15) + "..." : label;
                    g2.drawString(trimmed, 20, startY + barHeight - 7);
                    
                    // Bar
                    int barW = (int) ((value / max) * maxBarWidth);
                    g2.setColor(barColor);
                    g2.fillRoundRect(20 + labelWidth, startY, barW, barHeight, 5, 5);
                    
                    // Value
                    g2.setColor(UIManager.getColor("Label.disabledForeground"));
                    String valStr = isCurrency ? currencyFormat.format(value) : String.valueOf((int)value);
                    g2.drawString(valStr, 20 + labelWidth + barW + 10, startY + barHeight - 7);
                    
                    startY += barHeight + gap;
                }
                g2.dispose();
            }
        };
    }

    // Gauge Chart
    public static JPanel createGaugeChart2D(String title, double percentage, String footerLabel) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth();
                int h = getHeight();
                
                g2.setFont(TITLE_FONT);
                g2.setColor(UIManager.getColor("Label.foreground"));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(title, (w - fm.stringWidth(title)) / 2, 25);
                
                int gaugeSize = Math.min(w - 40, h - 80);
                int x = (w - gaugeSize) / 2;
                int y = 40;
                
                // Draw background arc
                g2.setStroke(new BasicStroke(20.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.setColor(UIManager.getColor("Component.borderColor"));
                g2.draw(new Arc2D.Double(x, y, gaugeSize, gaugeSize, 180, -180, Arc2D.OPEN));
                
                // Draw filled arc
                Color fill;
                if (percentage < 70) fill = Color.decode("#4CAF50");
                else if (percentage < 90) fill = Color.decode("#FFC107");
                else fill = Color.decode("#F44336");
                
                g2.setColor(fill);
                double angle = (Math.min(percentage, 100.0) / 100.0) * 180.0;
                g2.draw(new Arc2D.Double(x, y, gaugeSize, gaugeSize, 180, -angle, Arc2D.OPEN));
                
                // Percentage text
                String pctStr = String.format("%.1f%%", percentage);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 32));
                g2.setColor(UIManager.getColor("Label.foreground"));
                fm = g2.getFontMetrics();
                g2.drawString(pctStr, (w - fm.stringWidth(pctStr)) / 2, y + gaugeSize / 2 + 20);
                
                // Footer
                g2.setFont(LABEL_FONT);
                g2.setColor(UIManager.getColor("Label.disabledForeground"));
                fm = g2.getFontMetrics();
                g2.drawString(footerLabel, (w - fm.stringWidth(footerLabel)) / 2, h - 15);
                
                g2.dispose();
            }
        };
    }

    // Line Chart (Trend)
    public static JPanel createLineChart2D(String title, Map<String, List<Number>> dataSeries, Map<String, Color> colorMap, List<String> xLabels) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth();
                int h = getHeight();
                
                g2.setFont(TITLE_FONT);
                g2.setColor(UIManager.getColor("Label.foreground"));
                g2.drawString(title, 20, 25);
                
                if (dataSeries == null || dataSeries.isEmpty()) {
                    g2.setFont(LABEL_FONT);
                    g2.drawString("No data available", 20, 60);
                    g2.dispose();
                    return;
                }
                
                int paddingX = 60;
                int paddingY = 60;
                int plotW = w - paddingX * 2;
                int plotH = h - paddingY * 2;
                int startX = paddingX;
                int startY = paddingY;
                
                double max = 0;
                for (List<Number> series : dataSeries.values()) {
                    for (Number n : series) {
                        if (n.doubleValue() > max) max = n.doubleValue();
                    }
                }
                if (max == 0) max = 1;
                
                // Grid lines and Y axis labels
                g2.setFont(LABEL_FONT);
                g2.setColor(UIManager.getColor("Component.borderColor"));
                int lines = 5;
                for (int i = 0; i <= lines; i++) {
                    int y = startY + plotH - (i * plotH / lines);
                    g2.drawLine(startX, y, startX + plotW, y);
                    String label = String.valueOf((int)(max * i / lines));
                    g2.drawString(label, startX - g2.getFontMetrics().stringWidth(label) - 10, y + 5);
                }
                
                // Draw series
                int seriesCount = 0;
                for (Map.Entry<String, List<Number>> entry : dataSeries.entrySet()) {
                    String name = entry.getKey();
                    List<Number> values = entry.getValue();
                    Color c = colorMap.getOrDefault(name, Color.BLUE);
                    g2.setColor(c);
                    
                    if (values.size() < 2) continue;
                    double xStep = (double) plotW / (values.size() - 1);
                    
                    Path2D path = new Path2D.Double();
                    for (int i = 0; i < values.size(); i++) {
                        double x = startX + i * xStep;
                        double y = startY + plotH - ((values.get(i).doubleValue() / max) * plotH);
                        if (i == 0) path.moveTo(x, y);
                        else path.lineTo(x, y);
                        
                        // Draw point marker
                        g2.fillOval((int)x - 3, (int)y - 3, 6, 6);
                        
                        // X axis label for first series only
                        if (seriesCount == 0 && xLabels != null && i < xLabels.size()) {
                            g2.setColor(UIManager.getColor("Label.disabledForeground"));
                            String xl = xLabels.get(i);
                            int strW = g2.getFontMetrics().stringWidth(xl);
                            // Only draw a few labels to prevent overlap
                            if (values.size() <= 10 || i % (values.size()/5) == 0) {
                                g2.drawString(xl, (int)x - strW/2, startY + plotH + 20);
                            }
                            g2.setColor(c); // restore
                        }
                    }
                    g2.setStroke(new BasicStroke(2.0f));
                    g2.draw(path);
                    
                    // Legend
                    int legX = startX + (seriesCount * 120);
                    int legY = startY + plotH + 40;
                    g2.fillRect(legX, legY, 12, 12);
                    g2.setColor(UIManager.getColor("Label.foreground"));
                    g2.drawString(name, legX + 20, legY + 11);
                    seriesCount++;
                }
                
                g2.dispose();
            }
        };
    }
}
