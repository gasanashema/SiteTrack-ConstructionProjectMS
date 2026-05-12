package dto;

import java.io.Serializable;

public class MaterialUsageReportDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String projectName;
    private java.time.LocalDate fromDate;
    private java.time.LocalDate toDate;
    private java.math.BigDecimal totalQuantityUsed;
    private java.math.BigDecimal totalCost;
    private java.util.List<MaterialUsageDTO> usageRecords;

    public MaterialUsageReportDTO() {}

    public MaterialUsageReportDTO(String projectName, java.time.LocalDate fromDate, java.time.LocalDate toDate, java.math.BigDecimal totalQuantityUsed, java.math.BigDecimal totalCost, java.util.List<MaterialUsageDTO> usageRecords) {
        this.projectName = projectName;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.totalQuantityUsed = totalQuantityUsed;
        this.totalCost = totalCost;
        this.usageRecords = usageRecords;
    }

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
    public java.time.LocalDate getFromDate() { return fromDate; }
    public void setFromDate(java.time.LocalDate fromDate) { this.fromDate = fromDate; }
    public java.time.LocalDate getToDate() { return toDate; }
    public void setToDate(java.time.LocalDate toDate) { this.toDate = toDate; }
    public java.math.BigDecimal getTotalQuantityUsed() { return totalQuantityUsed; }
    public void setTotalQuantityUsed(java.math.BigDecimal totalQuantityUsed) { this.totalQuantityUsed = totalQuantityUsed; }
    public java.math.BigDecimal getTotalCost() { return totalCost; }
    public void setTotalCost(java.math.BigDecimal totalCost) { this.totalCost = totalCost; }
    public java.util.List<MaterialUsageDTO> getUsageRecords() { return usageRecords; }
    public void setUsageRecords(java.util.List<MaterialUsageDTO> usageRecords) { this.usageRecords = usageRecords; }
}
