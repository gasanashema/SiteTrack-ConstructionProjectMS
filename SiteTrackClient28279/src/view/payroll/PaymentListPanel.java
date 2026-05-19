package view.payroll;

import controller.PayrollController;
import controller.ProjectController;
import dto.ProjectDTO;
import dto.WorkerPaymentDTO;
import session.SessionManager;
import view.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;

public class PaymentListPanel extends JPanel {
    private MainFrame mainFrame;
    private PayrollController payrollController;
    private ProjectController projectController;
    
    private JComboBox<String> projectCombo;
    private JComboBox<String> statusFilter;
    private com.toedter.calendar.JDateChooser fromDateField;
    private com.toedter.calendar.JDateChooser toDateField;
    
    private JTable table;
    private DefaultTableModel tableModel;
    private List<WorkerPaymentDTO> currentPayments;

    public PaymentListPanel(MainFrame mainFrame, PayrollController payrollController) {
        this.mainFrame = mainFrame;
        this.payrollController = payrollController;
        this.projectController = new ProjectController();
        
        setLayout(new BorderLayout());
        setBackground(UIManager.getColor("Panel.background"));
        
        initUI();
    }

    private void initUI() {
        // --- Filter Bar ---
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filterPanel.setBackground(UIManager.getColor("Panel.background"));

        projectCombo = new JComboBox<>();
        List<ProjectDTO> projects = projectController.getAllProjects();
        for (ProjectDTO p : projects) {
            projectCombo.addItem(p.getId() + " - " + p.getProjectName());
        }

        statusFilter = new JComboBox<>(new String[]{"All", "PENDING", "PAID"});

        fromDateField = new com.toedter.calendar.JDateChooser();
        toDateField = new com.toedter.calendar.JDateChooser();

        JButton filterBtn = new JButton("Apply Filters");
        JButton clearBtn = new JButton("Clear Filters");

        filterPanel.add(new JLabel("Project:"));
        filterPanel.add(projectCombo);
        filterPanel.add(new JLabel("Status:"));
        filterPanel.add(statusFilter);
        filterPanel.add(new JLabel("From:"));
        filterPanel.add(fromDateField);
        filterPanel.add(new JLabel("To:"));
        filterPanel.add(toDateField);
        filterPanel.add(filterBtn);
        filterPanel.add(clearBtn);

        add(filterPanel, BorderLayout.NORTH);

        // --- Table ---
        String[] columns = {"ID", "Date", "Worker Name", "Daily Rate", "Amount Owed", "Amount Paid", "Status", "Paid By"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFont(new Font("Ubuntu", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Ubuntu", Font.BOLD, 14));
        
        // Hide ID column
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);

        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "RW"));
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if ((column == 3 || column == 4 || column == 5) && value != null) {
                    try {
                        BigDecimal amt = new BigDecimal(value.toString());
                        setText(currencyFormat.format(amt));
                        setHorizontalAlignment(JLabel.RIGHT);
                    } catch (Exception e) {}
                } else {
                    setHorizontalAlignment(JLabel.LEFT);
                }
                
                if (column == 6) { // Status
                    setHorizontalAlignment(JLabel.CENTER);
                    if ("PAID".equals(value)) {
                        c.setBackground(new Color(204, 255, 204));
                        c.setForeground(Color.BLACK);
                    } else if ("PENDING".equals(value)) {
                        c.setBackground(new Color(255, 255, 204));
                        c.setForeground(Color.BLACK);
                    }
                } else if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 250));
                    c.setForeground(table.getForeground());
                }
                
                if (isSelected) {
                    c.setBackground(table.getSelectionBackground());
                    c.setForeground(table.getSelectionForeground());
                }
                return c;
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);

        // --- Action Buttons (South) ---
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        actionPanel.setBackground(UIManager.getColor("Panel.background"));

        JButton viewBtn = new JButton("👁️ View Details");
        viewBtn.setEnabled(false);
        
        JButton processBtn = new JButton("💳 Mark as Paid");
        processBtn.setBackground(Color.decode("#27ae60"));
        processBtn.setForeground(Color.WHITE);
        processBtn.setEnabled(false);
        
        JButton deleteBtn = new JButton("🗑️ Delete");
        deleteBtn.setEnabled(false);
        
        JButton refreshBtn = new JButton("🔄 Refresh");

        boolean isAdmin = SessionManager.getInstance().isAdmin();

        actionPanel.add(viewBtn);
        actionPanel.add(processBtn);
        if (isAdmin) actionPanel.add(deleteBtn);
        actionPanel.add(refreshBtn);

        add(actionPanel, BorderLayout.SOUTH);

        // --- Listeners ---
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                viewBtn.setEnabled(true);
                if (isAdmin) deleteBtn.setEnabled(true);
                
                String status = (String) table.getValueAt(row, 6);
                processBtn.setEnabled("PENDING".equals(status));
            } else {
                viewBtn.setEnabled(false);
                processBtn.setEnabled(false);
                deleteBtn.setEnabled(false);
            }
        });

        filterBtn.addActionListener(e -> loadData());
        clearBtn.addActionListener(e -> {
            statusFilter.setSelectedIndex(0);
            fromDateField.setDate(null);
            toDateField.setDate(null);
            loadData();
        });
        refreshBtn.addActionListener(e -> loadData());

        viewBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                WorkerPaymentDTO payment = currentPayments.get(row);
                PaymentDetailDialog dialog = new PaymentDetailDialog(mainFrame, payrollController, payment, false);
                dialog.setVisible(true);
            }
        });

        processBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                WorkerPaymentDTO payment = currentPayments.get(row);
                PaymentDetailDialog dialog = new PaymentDetailDialog(mainFrame, payrollController, payment, true);
                dialog.setVisible(true);
                if (dialog.isSaved()) loadData();
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                WorkerPaymentDTO payment = currentPayments.get(row);
                int confirm = JOptionPane.showConfirmDialog(this, "Delete payment record for " + payment.getWorkerFullName() + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (payrollController.deletePayment(payment.getId())) loadData();
                }
            }
        });
    }

    private String getSelectedProjectId() {
        if (projectCombo.getSelectedItem() != null) {
            String selected = (String) projectCombo.getSelectedItem();
            return selected.split(" - ")[0];
        }
        return null;
    }

    public void loadData() {
        String projId = getSelectedProjectId();
        if (projId == null) return;

        LocalDate from = fromDateField.getDate() != null ? fromDateField.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
        LocalDate to = toDateField.getDate() != null ? toDateField.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;

        if (from != null && to != null) {
            currentPayments = payrollController.getPaymentsByProjectAndDateRange(projId, from, to);
        } else {
            currentPayments = payrollController.getPaymentsByProject(projId);
        }

        String sFilter = (String) statusFilter.getSelectedItem();

        tableModel.setRowCount(0);
        for (WorkerPaymentDTO p : currentPayments) {
            if ("All".equals(sFilter) || p.getPaymentStatus().equals(sFilter)) {
                tableModel.addRow(new Object[]{
                    p.getId(),
                    p.getWorkDate(),
                    p.getWorkerFullName(),
                    p.getDailyRate(),
                    p.getAmountOwed(),
                    p.getAmountPaid(),
                    p.getPaymentStatus(),
                    p.getPaidByName() != null ? p.getPaidByName() : ""
                });
            }
        }
    }
}
