package view.payroll;

import controller.PayrollController;
import dto.WorkerPaymentDTO;
import session.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class PaymentDetailDialog extends JDialog {
    private PayrollController payrollController;
    private WorkerPaymentDTO payment;
    private boolean isProcessMode;
    private boolean isSaved = false;

    private JTextField amountPaidField;
    private JTextArea notesArea;

    public PaymentDetailDialog(JFrame parent, PayrollController payrollController, WorkerPaymentDTO payment, boolean isProcessMode) {
        super(parent, isProcessMode ? "Process Payment" : "Payment Details", true);
        this.payrollController = payrollController;
        this.payment = payment;
        this.isProcessMode = isProcessMode;
        
        setSize(500, 400);
        setLocationRelativeTo(parent);
        
        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "RW"));
        
        String rateFormatted = payment.getDailyRate() != null ? currencyFormat.format(payment.getDailyRate()) : "RWF 0.00";
        String owedFormatted = payment.getAmountOwed() != null ? currencyFormat.format(payment.getAmountOwed()) : "RWF 0.00";

        int row = 0;
        addReadOnlyField(formPanel, "Worker:", payment.getWorkerFullName(), gbc, row++);
        addReadOnlyField(formPanel, "Project:", payment.getProjectName(), gbc, row++);
        addReadOnlyField(formPanel, "Work Date:", payment.getWorkDate().toString(), gbc, row++);
        addReadOnlyField(formPanel, "Daily Rate:", rateFormatted, gbc, row++);
        
        JLabel owedLabel = new JLabel(owedFormatted);
        owedLabel.setFont(new Font("Ubuntu", Font.BOLD, 16));
        owedLabel.setForeground(Color.decode("#2980b9"));
        gbc.gridy = row++;
        gbc.gridx = 0; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Amount Owed:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        formPanel.add(owedLabel, gbc);

        if (isProcessMode) {
            amountPaidField = new JTextField();
            amountPaidField.setPreferredSize(new Dimension(0, 35));
            if (payment.getAmountOwed() != null) {
                amountPaidField.setText(payment.getAmountOwed().toString());
            }

            notesArea = new JTextArea(3, 20);
            notesArea.setLineWrap(true);
            notesArea.setWrapStyleWord(true);
            
            gbc.gridy = row++;
            gbc.gridx = 0; gbc.weightx = 0.3;
            formPanel.add(new JLabel("Amount to Pay *"), gbc);
            gbc.gridx = 1; gbc.weightx = 0.7;
            formPanel.add(amountPaidField, gbc);
            
            gbc.gridy = row++;
            gbc.gridx = 0; gbc.weightx = 0.3;
            formPanel.add(new JLabel("Notes"), gbc);
            gbc.gridx = 1; gbc.weightx = 0.7;
            formPanel.add(new JScrollPane(notesArea), gbc);
        } else {
            String paidFormatted = payment.getAmountPaid() != null ? currencyFormat.format(payment.getAmountPaid()) : "RWF 0.00";
            addReadOnlyField(formPanel, "Amount Paid:", paidFormatted, gbc, row++);
            addReadOnlyField(formPanel, "Status:", payment.getPaymentStatus(), gbc, row++);
            if ("PAID".equals(payment.getPaymentStatus())) {
                addReadOnlyField(formPanel, "Paid By:", payment.getPaidByName(), gbc, row++);
            }
        }

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // --- Buttons ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dispose());
        
        if (isProcessMode) {
            JButton saveBtn = new JButton("Save Payment");
            saveBtn.setBackground(Color.decode("#27ae60"));
            saveBtn.setForeground(Color.WHITE);
            saveBtn.addActionListener(e -> savePayment());
            buttonPanel.add(closeBtn);
            buttonPanel.add(saveBtn);
        } else {
            buttonPanel.add(closeBtn);
        }

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        setContentPane(mainPanel);
    }
    
    private void addReadOnlyField(JPanel panel, String label, String value, GridBagConstraints gbc, int row) {
        gbc.gridy = row;
        gbc.gridx = 0; gbc.weightx = 0.3;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Ubuntu", Font.BOLD, 12));
        panel.add(lbl, gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.7;
        JLabel valLbl = new JLabel(value != null ? value : "N/A");
        panel.add(valLbl, gbc);
    }

    private void savePayment() {
        String paidStr = amountPaidField.getText().trim();
        if (paidStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the amount paid.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        BigDecimal amountPaid;
        try {
            amountPaid = new BigDecimal(paidStr);
            if (amountPaid.compareTo(BigDecimal.ZERO) <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valid positive number required for Amount Paid.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (amountPaid.compareTo(payment.getAmountOwed()) > 0) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Amount paid exceeds the amount owed.\nAre you sure you want to proceed?", 
                    "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm != JOptionPane.YES_OPTION) return;
        }

        String currentUserId = SessionManager.getInstance().getCurrentUserId();
        
        if (payrollController.markAsPaid(payment.getId(), amountPaid, currentUserId) != null) {
            isSaved = true;
            dispose();
        }
    }

    public boolean isSaved() {
        return isSaved;
    }
}
