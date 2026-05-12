package dto;

import java.io.Serializable;

public class LaborCostReportDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String projectName;
    private java.time.LocalDate fromDate;
    private java.time.LocalDate toDate;
    private java.math.BigDecimal totalAmountOwed;
    private java.math.BigDecimal totalAmountPaid;
    private java.math.BigDecimal totalPending;
    private java.util.List<WorkerPaymentDTO> paymentRecords;

    public LaborCostReportDTO() {}

    public LaborCostReportDTO(String projectName, java.time.LocalDate fromDate, java.time.LocalDate toDate, java.math.BigDecimal totalAmountOwed, java.math.BigDecimal totalAmountPaid, java.math.BigDecimal totalPending, java.util.List<WorkerPaymentDTO> paymentRecords) {
        this.projectName = projectName;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.totalAmountOwed = totalAmountOwed;
        this.totalAmountPaid = totalAmountPaid;
        this.totalPending = totalPending;
        this.paymentRecords = paymentRecords;
    }

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
    public java.time.LocalDate getFromDate() { return fromDate; }
    public void setFromDate(java.time.LocalDate fromDate) { this.fromDate = fromDate; }
    public java.time.LocalDate getToDate() { return toDate; }
    public void setToDate(java.time.LocalDate toDate) { this.toDate = toDate; }
    public java.math.BigDecimal getTotalAmountOwed() { return totalAmountOwed; }
    public void setTotalAmountOwed(java.math.BigDecimal totalAmountOwed) { this.totalAmountOwed = totalAmountOwed; }
    public java.math.BigDecimal getTotalAmountPaid() { return totalAmountPaid; }
    public void setTotalAmountPaid(java.math.BigDecimal totalAmountPaid) { this.totalAmountPaid = totalAmountPaid; }
    public java.math.BigDecimal getTotalPending() { return totalPending; }
    public void setTotalPending(java.math.BigDecimal totalPending) { this.totalPending = totalPending; }
    public java.util.List<WorkerPaymentDTO> getPaymentRecords() { return paymentRecords; }
    public void setPaymentRecords(java.util.List<WorkerPaymentDTO> paymentRecords) { this.paymentRecords = paymentRecords; }
}
