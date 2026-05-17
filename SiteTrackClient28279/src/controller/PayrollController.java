package controller;

import config.RMIConnection;
import dto.WorkerAttendanceDTO;
import dto.WorkerPaymentDTO;
import service.interfaces.WorkerAttendanceService;
import service.interfaces.WorkerPaymentService;

import javax.swing.*;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class PayrollController {

    private WorkerAttendanceService getAttendanceService() throws RemoteException {
        return RMIConnection.getInstance().getService(WorkerAttendanceService.class);
    }

    private WorkerPaymentService getPaymentService() throws RemoteException {
        return RMIConnection.getInstance().getService(WorkerPaymentService.class);
    }

    // --- Attendance Operations ---

    public List<WorkerAttendanceDTO> getAttendanceByProject(String projectId) {
        try {
            return getAttendanceService().getAttendanceByProject(projectId);
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load attendance: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }

    public List<WorkerAttendanceDTO> getAttendanceByProjectAndDate(String projectId, LocalDate workDate) {
        try {
            return getAttendanceService().getAttendanceByProjectAndDate(projectId, workDate);
        } catch (RemoteException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<WorkerAttendanceDTO> getAttendanceByProjectAndDateRange(String projectId, LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            JOptionPane.showMessageDialog(null, "From date must be before To date.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return new ArrayList<>();
        }
        try {
            return getAttendanceService().getAttendanceByProjectAndDateRange(projectId, from, to);
        } catch (RemoteException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public WorkerAttendanceDTO recordAttendance(WorkerAttendanceDTO dto) {
        try {
            return getAttendanceService().recordAttendance(dto);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
            return null;
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to record attendance: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public WorkerAttendanceDTO updateAttendance(WorkerAttendanceDTO dto) {
        try {
            return getAttendanceService().updateAttendance(dto);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
            return null;
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to update attendance: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public boolean deleteAttendance(String attendanceId) {
        try {
            boolean deleted = getAttendanceService().deleteAttendance(attendanceId);
            if (deleted) {
                JOptionPane.showMessageDialog(null, "Attendance record deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            return deleted;
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to delete attendance: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // --- Payment Operations ---

    public List<WorkerPaymentDTO> getPaymentsByProject(String projectId) {
        try {
            return getPaymentService().getPaymentsByProject(projectId);
        } catch (RemoteException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<WorkerPaymentDTO> getPaymentsByProjectAndDateRange(String projectId, LocalDate from, LocalDate to) {
        try {
            return getPaymentService().getPaymentsByProjectAndDateRange(projectId, from, to);
        } catch (RemoteException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<WorkerPaymentDTO> getPendingPaymentsByProject(String projectId) {
        try {
            return getPaymentService().getPendingPaymentsByProject(projectId);
        } catch (RemoteException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public WorkerPaymentDTO createPayment(WorkerPaymentDTO dto) {
        try {
            WorkerPaymentDTO created = getPaymentService().createPayment(dto);
            JOptionPane.showMessageDialog(null, "Payment record created for " + created.getWorkerFullName() + ".\nAmount owed: " + created.getAmountOwed() + "\nStatus: PENDING", "Success", JOptionPane.INFORMATION_MESSAGE);
            return created;
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
            return null;
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to create payment: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public WorkerPaymentDTO markAsPaid(String paymentId, BigDecimal amountPaid, String paidById) {
        if (amountPaid.compareTo(BigDecimal.ZERO) <= 0) {
            JOptionPane.showMessageDialog(null, "Amount paid must be greater than zero.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        try {
            WorkerPaymentDTO updated = getPaymentService().markAsPaid(paymentId, amountPaid, paidById);
            JOptionPane.showMessageDialog(null, "Payment processed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            return updated;
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
            return null;
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to mark as paid: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public boolean deletePayment(String paymentId) {
        try {
            boolean deleted = getPaymentService().deletePayment(paymentId);
            if (deleted) {
                JOptionPane.showMessageDialog(null, "Payment record deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            return deleted;
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to delete payment: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public WorkerPaymentDTO getPaymentById(String paymentId) {
        try {
            return getPaymentService().getPaymentById(paymentId);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }
}
