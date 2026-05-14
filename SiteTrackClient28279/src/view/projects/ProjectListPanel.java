package view.projects;

import controller.ProjectController;
import dto.ProjectDTO;
import session.SessionManager;
import view.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ProjectListPanel extends JPanel {
    private MainFrame mainFrame;
    private ProjectController controller;
    private List<ProjectDTO> projectList;
    
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;
    
    private JComboBox<String> statusFilter;
    private JTextField searchField;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JLabel statusLabel;

    public ProjectListPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.controller = new ProjectController();
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        
        // 1. Action Bar (North)
        JPanel actionBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        actionBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UIManager.getColor("Component.borderColor")));
        
        statusFilter = new JComboBox<>(new String[]{"All", "PLANNING", "ONGOING", "COMPLETED", "CANCELLED"});
        statusFilter.setPreferredSize(new Dimension(150, 32));
        statusFilter.addActionListener(e -> applyFilters());
        
        searchField = new JTextField(20);
        searchField.setPreferredSize(new Dimension(250, 32));
        // FlatLaf placeholder
        searchField.putClientProperty("JTextField.placeholderText", "Search name or location...");
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) { applyFilters(); }
        });
        
        addButton = new JButton("+ New Project");
        editButton = new JButton("✎ Edit");
        deleteButton = new JButton("🗑 Delete");
        JButton refreshButton = new JButton("↻ Refresh");
        
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
        
        addButton.addActionListener(e -> openProjectForm(null));
        editButton.addActionListener(e -> openSelectedProject());
        deleteButton.addActionListener(e -> deleteSelectedProject());
        refreshButton.addActionListener(e -> loadData());
        
        // Only admins can create or delete projects directly here
        if (!SessionManager.getInstance().isAdmin()) {
            addButton.setVisible(false);
            deleteButton.setVisible(false);
        }

        actionBar.add(new JLabel("Status:"));
        actionBar.add(statusFilter);
        actionBar.add(searchField);
        actionBar.add(Box.createHorizontalStrut(20));
        actionBar.add(addButton);
        actionBar.add(editButton);
        actionBar.add(deleteButton);
        actionBar.add(refreshButton);
        
        add(actionBar, BorderLayout.NORTH);

        // 2. Table (Center)
        String[] columns = {"ID", "Project Name", "Location", "Status", "Progress", "Created By"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Hide ID column
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);
        
        // Custom renderer for Progress column
        table.getColumnModel().getColumn(4).setCellRenderer(new ProgressCellRenderer());
        
        rowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(rowSorter);
        
        table.getSelectionModel().addListSelectionListener(e -> {
            boolean hasSelection = table.getSelectedRow() != -1;
            editButton.setEnabled(hasSelection);
            if (SessionManager.getInstance().isAdmin()) {
                deleteButton.setEnabled(hasSelection);
            }
            updateStatusBar();
        });
        
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    openSelectedProject();
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        // 3. Status Bar (South)
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        statusLabel = new JLabel("Total: 0 | Selected: None");
        statusBar.add(statusLabel);
        add(statusBar, BorderLayout.SOUTH);
    }
    
    public void loadData() {
        projectList = controller.getAllProjects();
        tableModel.setRowCount(0);
        
        if (projectList != null) {
            for (ProjectDTO p : projectList) {
                int progress = calculateProgress(p.getStartDate(), p.getExpectedEndDate());
                tableModel.addRow(new Object[]{
                    p.getId(),
                    p.getProjectName(),
                    p.getLocation(),
                    p.getStatus(),
                    progress,
                    p.getCreatedByName()
                });
            }
        }
        applyFilters();
        updateStatusBar();
    }
    
    private void applyFilters() {
        String text = searchField.getText().trim().toLowerCase();
        String status = (String) statusFilter.getSelectedItem();
        
        RowFilter<DefaultTableModel, Object> rf = new RowFilter<DefaultTableModel, Object>() {
            public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                String rowName = entry.getStringValue(1).toLowerCase();
                String rowLocation = entry.getStringValue(2).toLowerCase();
                String rowStatus = entry.getStringValue(3);
                
                boolean matchesSearch = text.isEmpty() || rowName.contains(text) || rowLocation.contains(text);
                boolean matchesStatus = "All".equals(status) || rowStatus.equals(status);
                
                return matchesSearch && matchesStatus;
            }
        };
        rowSorter.setRowFilter(rf);
        updateStatusBar();
    }
    
    private void updateStatusBar() {
        int total = table.getRowCount();
        String selected = "None";
        int viewRow = table.getSelectedRow();
        if (viewRow != -1) {
            selected = (String) table.getValueAt(viewRow, 1);
        }
        statusLabel.setText("Total visible: " + total + " | Selected: " + selected);
    }
    
    private int calculateProgress(LocalDate start, LocalDate end) {
        if (start == null || end == null) return 0;
        LocalDate now = LocalDate.now();
        if (now.isBefore(start)) return 0;
        if (now.isAfter(end)) return 100;
        long totalDays = ChronoUnit.DAYS.between(start, end);
        if (totalDays == 0) return 100;
        long elapsedDays = ChronoUnit.DAYS.between(start, now);
        return (int) ((elapsedDays * 100) / totalDays);
    }
    
    private void openProjectForm(ProjectDTO project) {
        // We will implement ProjectFormPanel next
        ProjectFormPanel dialog = new ProjectFormPanel(mainFrame, project, controller);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadData();
        }
    }
    
    private void openSelectedProject() {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) return;
        int modelRow = table.convertRowIndexToModel(viewRow);
        String id = (String) tableModel.getValueAt(modelRow, 0);
        
        ProjectDTO selectedProject = null;
        for (ProjectDTO p : projectList) {
            if (p.getId().equals(id)) {
                selectedProject = p;
                break;
            }
        }
        
        if (selectedProject != null) {
            openProjectForm(selectedProject);
        }
    }
    
    private void deleteSelectedProject() {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) return;
        int modelRow = table.convertRowIndexToModel(viewRow);
        String id = (String) tableModel.getValueAt(modelRow, 0);
        
        if (controller.deleteProject(id)) {
            loadData();
        }
    }
    
    // Custom renderer for progress bar
    private class ProgressCellRenderer extends JProgressBar implements javax.swing.table.TableCellRenderer {
        public ProgressCellRenderer() {
            super(0, 100);
            setStringPainted(true);
            setOpaque(true);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            int progress = (Integer) value;
            setValue(progress);
            setString(progress + "%");
            
            if (progress < 30) {
                setForeground(Color.decode("#D93025"));
            } else if (progress < 70) {
                setForeground(Color.decode("#F29900"));
            } else {
                setForeground(Color.decode("#188038"));
            }
            
            if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                setBackground(table.getBackground());
            }
            return this;
        }
    }
}
