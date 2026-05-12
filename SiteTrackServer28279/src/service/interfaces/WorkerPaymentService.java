package service.interfaces;

import dto.WorkerPaymentDTO;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;

public interface WorkerPaymentService extends Remote {
    WorkerPaymentDTO createPayment(WorkerPaymentDTO dto) throws RemoteException;
    WorkerPaymentDTO markAsPaid(String paymentId, BigDecimal amountPaid, String paidById) throws RemoteException;
    boolean deletePayment(String paymentId) throws RemoteException;
    WorkerPaymentDTO getPaymentById(String paymentId) throws RemoteException;
    List<WorkerPaymentDTO> getPaymentsByProject(String projectId) throws RemoteException;
    List<WorkerPaymentDTO> getPaymentsByProjectAndDateRange(String projectId, LocalDate from, LocalDate to) throws RemoteException;
    List<WorkerPaymentDTO> getPendingPaymentsByProject(String projectId) throws RemoteException;
    List<WorkerPaymentDTO> getPaymentsByWorker(String workerId) throws RemoteException;
}
