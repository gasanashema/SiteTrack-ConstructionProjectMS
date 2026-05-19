package view.dashboard;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

public class ModernPieChartPanel extends JPanel {

    private BigDecimal materialCost;
    private BigDecimal laborCost;
    private String title;

    public ModernPieChartPanel(String title, BigDecimal materialCost, BigDecimal laborCost) {
        this.title = title;
        this.materialCost = materialCost != null ? materialCost : BigDecimal.ZERO;
        this.laborCost = laborCost != null ? laborCost : BigDecimal.ZERO;
        
        setBackground(UIManager.getColor("Panel.background"));
        setPreferredSize(new Dimension(300, 300));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // Draw Title
        g2d.setColor(UIManager.getColor("Label.foreground"));
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 16));
        FontMetrics fm = g2d.getFontMetrics();
        int titleWidth = fm.stringWidth(title);
        g2d.drawString(title, (width - titleWidth) / 2, 25);

        BigDecimal total = materialCost.add(laborCost);
        if (total.compareTo(BigDecimal.ZERO) <= 0) {
            g2d.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            String noData = "No cost data available";
            int noDataWidth = g2d.getFontMetrics().stringWidth(noData);
            g2d.drawString(noData, (width - noDataWidth) / 2, height / 2);
            g2d.dispose();
            return;
        }

        double materialAngle = (materialCost.doubleValue() / total.doubleValue()) * 360.0;
        double laborAngle = 360.0 - materialAngle;

        int diameter = Math.min(width, height) - 100;
        int x = (width - diameter) / 2;
        int y = (height - diameter) / 2 + 10;

        // Shadow
        g2d.setColor(new Color(0, 0, 0, 30));
        g2d.fill(new Arc2D.Double(x + 3, y + 3, diameter, diameter, 0, 360, Arc2D.PIE));

        // Material Slice
        Color materialColor = Color.decode("#3498db");
        g2d.setColor(materialColor);
        g2d.fill(new Arc2D.Double(x, y, diameter, diameter, 0, materialAngle, Arc2D.PIE));

        // Labor Slice
        Color laborColor = Color.decode("#e67e22");
        g2d.setColor(laborColor);
        g2d.fill(new Arc2D.Double(x, y, diameter, diameter, materialAngle, laborAngle, Arc2D.PIE));

        // White border between slices
        g2d.setColor(UIManager.getColor("Panel.background"));
        g2d.setStroke(new BasicStroke(3f));
        g2d.draw(new Arc2D.Double(x, y, diameter, diameter, 0, materialAngle, Arc2D.PIE));
        g2d.draw(new Arc2D.Double(x, y, diameter, diameter, materialAngle, laborAngle, Arc2D.PIE));

        // Draw Legend
        int legendY = height - 30;
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        NumberFormat percentFormat = NumberFormat.getPercentInstance();
        percentFormat.setMaximumFractionDigits(1);
        String matPct = percentFormat.format(materialCost.doubleValue() / total.doubleValue());
        String labPct = percentFormat.format(laborCost.doubleValue() / total.doubleValue());

        String matLegend = "Materials (" + matPct + ")";
        String labLegend = "Labor (" + labPct + ")";

        int matLegendWidth = g2d.getFontMetrics().stringWidth(matLegend) + 20;
        int labLegendWidth = g2d.getFontMetrics().stringWidth(labLegend) + 20;
        int totalLegendWidth = matLegendWidth + labLegendWidth + 20;
        int legendX = (width - totalLegendWidth) / 2;

        // Material Legend
        g2d.setColor(materialColor);
        g2d.fillRect(legendX, legendY - 10, 12, 12);
        g2d.setColor(UIManager.getColor("Label.foreground"));
        g2d.drawString(matLegend, legendX + 18, legendY);

        // Labor Legend
        legendX += matLegendWidth + 20;
        g2d.setColor(laborColor);
        g2d.fillRect(legendX, legendY - 10, 12, 12);
        g2d.setColor(UIManager.getColor("Label.foreground"));
        g2d.drawString(labLegend, legendX + 18, legendY);

        g2d.dispose();
    }
}
