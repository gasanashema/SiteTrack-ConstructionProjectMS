package view.payroll;

import controller.PayrollController;
import view.MainFrame;

import javax.swing.*;
import java.awt.*;

public class PayrollPanel extends JPanel {
    private MainFrame mainFrame;
    private PayrollController payrollController;
    private JTabbedPane tabbedPane;

    public PayrollPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.payrollController = new PayrollController();
        
        setLayout(new BorderLayout());
        setBackground(UIManager.getColor("Panel.background"));

        JPanel topHeaderPanel = new JPanel(new BorderLayout());
        topHeaderPanel.setBackground(UIManager.getColor("Panel.background"));
        
        JLabel titleLabel = new JLabel("Payroll Management");
        titleLabel.setFont(new Font("Ubuntu", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        topHeaderPanel.add(titleLabel, BorderLayout.NORTH);
        add(topHeaderPanel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Ubuntu", Font.PLAIN, 16));

        PaymentListPanel paymentListPanel = new PaymentListPanel(mainFrame, payrollController);
        PaymentFormPanel paymentFormPanel = new PaymentFormPanel(payrollController);

        tabbedPane.addTab("Payment Records", paymentListPanel);
        tabbedPane.addTab("Create Payment", paymentFormPanel);
        
        // Refresh PaymentListPanel when returning to its tab
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedComponent() == paymentListPanel) {
                paymentListPanel.loadData();
            }
        });

        add(tabbedPane, BorderLayout.CENTER);
    }
}
