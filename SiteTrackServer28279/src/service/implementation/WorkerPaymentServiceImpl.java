package service.implementation;

import dao.WorkerPaymentDao;
import dao.WorkerAttendanceDao;
import dao.ProjectDao;
import dao.SiteWorkerDao;
import dao.UserDao;
import dto.WorkerPaymentDTO;
import model.WorkerPayment;
import model.WorkerAttendance;
import model.Project;
import model.SiteWorker;
import model.User;
import model.EPaymentStatus;
import service.interfaces.WorkerPaymentService;
import util.NotificationProducer;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class WorkerPaymentServiceImpl extends UnicastRemoteObject implements WorkerPaymentService {
    private final WorkerPaymentDao dao;
    private final WorkerAttendanceDao attendanceDao;
    private final ProjectDao projectDao;
    private final SiteWorkerDao siteWorkerDao;
    private final UserDao userDao;

    public WorkerPaymentServiceImpl() throws RemoteException {
        super();
        this.dao = new WorkerPaymentDao();
        this.attendanceDao = new WorkerAttendanceDao();
        this.projectDao = new ProjectDao();
        this.siteWorkerDao = new SiteWorkerDao();
        this.userDao = new UserDao();
    }

    private WorkerPaymentDTO toDTO(WorkerPayment entity) {
        if (entity == null) return null;
        return new WorkerPaymentDTO(
            entity.getId(),
            entity.getProject() != null ? entity.getProject().getId() : null,
            entity.getProject() != null ? entity.getProject().getProjectName() : null,
            entity.getWorker() != null ? entity.getWorker().getId() : null,
            entity.getWorker() != null ? entity.getWorker().getFullName() : null,
            entity.getAttendance() != null ? entity.getAttendance().getId() : null,
            entity.getWorkDate(),
            entity.getDailyRate(),
            entity.getAmountOwed(),
            entity.getAmountPaid(),
            entity.getPaymentStatus().name(),
            entity.getPaidBy() != null ? entity.getPaidBy().getFullName() : null,
            entity.getNotes()
        );
    }

    @Override
    public WorkerPaymentDTO createPayment(WorkerPaymentDTO dto) throws RemoteException {
        try {
            WorkerPayment entity = new WorkerPayment();
            entity.setWorkDate(dto.getWorkDate());
            entity.setDailyRate(dto.getDailyRate());
            entity.setAmountOwed(dto.getAmountOwed());
            entity.setAmountPaid(dto.getAmountPaid());
            entity.setPaymentStatus(EPaymentStatus.PENDING); // defaults to PENDING
            entity.setNotes(dto.getNotes());
            
            Project p = projectDao.findById(dto.getProjectId());
            if (p != null) entity.setProject(p);
            
            SiteWorker w = siteWorkerDao.findById(dto.getWorkerId());
            if (w != null) entity.setWorker(w);
            
            if (dto.getAttendanceId() != null) {
                WorkerAttendance a = attendanceDao.findById(dto.getAttendanceId());
                if (a != null) entity.setAttendance(a);
            }
            
            entity = dao.save(entity);
            return toDTO(entity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create worker payment");
        }
    }

    @Override
    public WorkerPaymentDTO markAsPaid(String paymentId, BigDecimal amountPaid, String paidById) throws RemoteException {
        try {
            WorkerPayment entity = dao.findById(paymentId);
            if (entity == null) throw new IllegalArgumentException("Payment not found");
            
            entity.setAmountPaid(amountPaid);
            entity.setPaymentStatus(EPaymentStatus.PAID);
            
            User u = userDao.findById(paidById);
            if (u != null) entity.setPaidBy(u);
            
            entity = dao.update(entity);
            NotificationProducer.sendNotification("ADMIN", "PAYMENT_PROCESSED", "Payment processed for worker " + entity.getWorker().getFullName() + " amount: " + amountPaid, "SYSTEM");
            return toDTO(entity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to process payment");
        }
    }

    @Override
    public boolean deletePayment(String paymentId) throws RemoteException {
        try {
            return dao.delete(paymentId) != null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete worker payment");
        }
    }

    @Override
    public WorkerPaymentDTO getPaymentById(String paymentId) throws RemoteException {
        try {
            return toDTO(dao.findById(paymentId));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch worker payment");
        }
    }

    @Override
    public List<WorkerPaymentDTO> getPaymentsByProject(String projectId) throws RemoteException {
        try {
            List<WorkerPaymentDTO> list = new ArrayList<>();
            for (WorkerPayment p : dao.findByProject(projectId)) {
                list.add(toDTO(p));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch payments");
        }
    }

    @Override
    public List<WorkerPaymentDTO> getPaymentsByProjectAndDateRange(String projectId, LocalDate from, LocalDate to) throws RemoteException {
        try {
            List<WorkerPaymentDTO> list = new ArrayList<>();
            for (WorkerPayment p : dao.findByProjectAndDateRange(projectId, from, to)) {
                list.add(toDTO(p));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch payments by date range");
        }
    }

    @Override
    public List<WorkerPaymentDTO> getPendingPaymentsByProject(String projectId) throws RemoteException {
        try {
            List<WorkerPaymentDTO> list = new ArrayList<>();
            for (WorkerPayment p : dao.findByPaymentStatus(projectId, EPaymentStatus.PENDING)) {
                list.add(toDTO(p));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch pending payments");
        }
    }

    @Override
    public List<WorkerPaymentDTO> getPaymentsByWorker(String workerId) throws RemoteException {
        try {
            List<WorkerPaymentDTO> list = new ArrayList<>();
            for (WorkerPayment p : dao.findByWorker(workerId)) {
                list.add(toDTO(p));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch payments by worker");
        }
    }
}
