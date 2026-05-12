package dto;

import java.io.Serializable;

public class StockMovementReportDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String projectName;
    private java.time.LocalDate fromDate;
    private java.time.LocalDate toDate;
    private java.util.List<MaterialStockMovementDTO> movements;

    public StockMovementReportDTO() {}

    public StockMovementReportDTO(String projectName, java.time.LocalDate fromDate, java.time.LocalDate toDate, java.util.List<MaterialStockMovementDTO> movements) {
        this.projectName = projectName;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.movements = movements;
    }

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
    public java.time.LocalDate getFromDate() { return fromDate; }
    public void setFromDate(java.time.LocalDate fromDate) { this.fromDate = fromDate; }
    public java.time.LocalDate getToDate() { return toDate; }
    public void setToDate(java.time.LocalDate toDate) { this.toDate = toDate; }
    public java.util.List<MaterialStockMovementDTO> getMovements() { return movements; }
    public void setMovements(java.util.List<MaterialStockMovementDTO> movements) { this.movements = movements; }
}
