package view.payroll;

import controller.PayrollController;
import controller.ProjectController;
import controller.WorkerController;
import dto.ProjectDTO;
import dto.SiteWorkerDTO;
import dto.WorkerAttendanceDTO;
import dto.WorkerPaymentDTO;
import session.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PaymentFormPanel extends JPanel {
    private PayrollController payrollController;
    private ProjectController projectController;
    private WorkerController workerController;
    
    private JComboBox<String> projectCombo;
    private com.toedter.calendar.JDateChooser workDateField;
    private JButton loadButton;
    
    private JComboBox<String> attendeeCombo;
    private JLabel selectedWorkerLabel;
    private JLabel dailyRateLabel;
    private JLabel amountOwedLabel;
    
    private List<WorkerAttendanceDTO> presentAttendees;
    private WorkerAttendanceDTO selectedAttendance;
    private SiteWorkerDTO selectedWorker;

    public PaymentFormPanel(PayrollController payrollController) {
        this.payrollController = payrollController;
        this.projectController = new ProjectController();
        this.workerController = new WorkerController();
        
        setLayout(new BorderLayout(10, 10));
        setBackground(UIManager.getColor("Panel.background"));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        initUI();
    }

    private void initUI() {
        // --- Top Selection ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topPanel.setBackground(UIManager.getColor("Panel.background"));
        
        projectCombo = new JComboBox<>();
        List<ProjectDTO> projects = projectController.getAllProjects();
        for (ProjectDTO p : projects) {
            projectCombo.addItem(p.getId() + " - " + p.getProjectName());
        }

        workDateField = new com.toedter.calendar.JDateChooser();
        workDateField.setDate(new java.util.Date());
        workDateField.setPreferredSize(new Dimension(150, 30));

        loadButton = new JButton("Load Attendees");
        loadButton.setBackground(Color.decode("#3498db"));
        loadButton.setForeground(Color.WHITE);

        topPanel.add(new JLabel("Project:"));
        topPanel.add(projectCombo);
        topPanel.add(new JLabel("Work Date:"));
        topPanel.add(workDateField);
        topPanel.add(loadButton);

        add(topPanel, BorderLayout.NORTH);

        // --- Center Form ---
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(UIManager.getColor("Panel.background"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 15, 15, 15);

        attendeeCombo = new JComboBox<>();
        attendeeCombo.setPreferredSize(new Dimension(300, 35));
        attendeeCombo.setEnabled(false);

        selectedWorkerLabel = new JLabel("-");
        dailyRateLabel = new JLabel("RWF 0.00");
        amountOwedLabel = new JLabel("RWF 0.00");
        
        Font boldFont = new Font("Ubuntu", Font.BOLD, 14);
        selectedWorkerLabel.setFont(boldFont);
        dailyRateLabel.setFont(boldFont);
        amountOwedLabel.setFont(new Font("Ubuntu", Font.BOLD, 18));
        amountOwedLabel.setForeground(Color.decode("#2980b9"));
        
        JLabel statusLabel = new JLabel("PENDING");
        statusLabel.setFont(boldFont);
        statusLabel.setForeground(Color.decode("#f39c12")); // Orange-ish

        int row = 0;
        gbc.gridy = row++;
        gbc.gridx = 0; gbc.weightx = 0.3;
        centerPanel.add(new JLabel("Select Attendee:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        centerPanel.add(attendeeCombo, gbc);

        addReadonlyRow(centerPanel, "Worker:", selectedWorkerLabel, gbc, row++);
        addReadonlyRow(centerPanel, "Daily Rate:", dailyRateLabel, gbc, row++);
        addReadonlyRow(centerPanel, "Amount Owed:", amountOwedLabel, gbc, row++);
        addReadonlyRow(centerPanel, "Status:", statusLabel, gbc, row++);

        // Push everything up
        gbc.gridy = row++;
        gbc.weighty = 1.0;
        centerPanel.add(Box.createVerticalGlue(), gbc);

        add(centerPanel, BorderLayout.CENTER);

        // --- Bottom Buttons ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        bottomPanel.setBackground(UIManager.getColor("Panel.background"));

        JButton clearBtn = new JButton("Clear Form");
        
        JButton createBtn = new JButton("Create Payment");
        createBtn.setBackground(Color.decode("#27ae60"));
        createBtn.setForeground(Color.WHITE);
        createBtn.setFont(new Font("Ubuntu", Font.BOLD, 14));
        createBtn.setPreferredSize(new Dimension(180, 40));
        createBtn.setEnabled(false);

        bottomPanel.add(clearBtn);
        bottomPanel.add(createBtn);

        add(bottomPanel, BorderLayout.SOUTH);

        // --- Listeners ---
        loadButton.addActionListener(e -> loadAttendees());

        attendeeCombo.addItemListener(e -> {
            if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                int idx = attendeeCombo.getSelectedIndex() - 1; // 0 is placeholder
                if (idx >= 0) {
                    selectedAttendance = presentAttendees.get(idx);
                    fetchWorkerDetails();
                    createBtn.setEnabled(true);
                } else {
                    selectedAttendance = null;
                    selectedWorker = null;
                    resetFormFields();
                    createBtn.setEnabled(false);
                }
            }
        });

        clearBtn.addActionListener(e -> {
            attendeeCombo.removeAllItems();
            attendeeCombo.setEnabled(false);
            createBtn.setEnabled(false);
            resetFormFields();
            workDateField.setDate(new java.util.Date());
        });

        createBtn.addActionListener(e -> {
            if (selectedAttendance == null || selectedWorker == null) return;
            
            WorkerPaymentDTO dto = new WorkerPaymentDTO();
            dto.setProjectId(selectedAttendance.getProjectId());
            dto.setWorkerId(selectedAttendance.getWorkerId());
            dto.setAttendanceId(selectedAttendance.getId());
            dto.setWorkDate(selectedAttendance.getWorkDate());
            dto.setDailyRate(selectedWorker.getDailyRate());
            dto.setAmountOwed(selectedWorker.getDailyRate());
            
            if (payrollController.createPayment(dto) != null) {
                clearBtn.doClick();
            }
        });
    }

    private void addReadonlyRow(JPanel panel, String label, JLabel valLabel, GridBagConstraints gbc, int row) {
        gbc.gridy = row;
        gbc.gridx = 0; gbc.weightx = 0.3;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Ubuntu", Font.PLAIN, 14));
        panel.add(lbl, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        panel.add(valLabel, gbc);
    }

    private void resetFormFields() {
        selectedWorkerLabel.setText("-");
        dailyRateLabel.setText("RWF 0.00");
        amountOwedLabel.setText("RWF 0.00");
    }

    private void loadAttendees() {
        String projId = null;
        if (projectCombo.getSelectedItem() != null) {
            projId = ((String) projectCombo.getSelectedItem()).split(" - ")[0];
        }
        
        LocalDate workDate = null;
        if (workDateField.getDate() != null) {
            workDate = workDateField.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }

        if (projId == null || workDate == null) {
            JOptionPane.showMessageDialog(this, "Please select a Project and Work Date.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<WorkerAttendanceDTO> allAtt = payrollController.getAttendanceByProjectAndDate(projId, workDate);
        presentAttendees = new ArrayList<>();
        
        for (WorkerAttendanceDTO a : allAtt) {
            if ("PRESENT".equals(a.getAttendanceStatus())) {
                presentAttendees.add(a);
            }
        }

        attendeeCombo.removeAllItems();
        if (presentAttendees.isEmpty()) {
            attendeeCombo.addItem("No PRESENT attendees found");
            attendeeCombo.setEnabled(false);
            resetFormFields();
        } else {
            attendeeCombo.addItem("-- Select Attendee --");
            for (WorkerAttendanceDTO a : presentAttendees) {
                attendeeCombo.addItem(a.getWorkerFullName() + " (" + a.getWorkerTypeName() + ")");
            }
            attendeeCombo.setEnabled(true);
        }
    }

    private void fetchWorkerDetails() {
        selectedWorker = null;
        // Fetch worker details to get accurate daily rate
        List<SiteWorkerDTO> activeWorkers = workerController.getActiveWorkers();
        for (SiteWorkerDTO w : activeWorkers) {
            if (w.getId().equals(selectedAttendance.getWorkerId())) {
                selectedWorker = w;
                break;
            }
        }

        if (selectedWorker != null) {
            selectedWorkerLabel.setText(selectedWorker.getFullName());
            
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "RW"));
            String rateFormatted = selectedWorker.getDailyRate() != null ? currencyFormat.format(selectedWorker.getDailyRate()) : "RWF 0.00";
            
            dailyRateLabel.setText(rateFormatted);
            amountOwedLabel.setText(rateFormatted);
        } else {
            resetFormFields();
        }
    }
}
