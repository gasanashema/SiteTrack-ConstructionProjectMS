package dao;

import java.util.Collections;
import java.util.List;
import java.time.LocalDate;
import model.*;
import org.hibernate.*;
import util.HibernateUtil;

public class WorkerPaymentDao {
    public WorkerPayment save(WorkerPayment obj) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Transaction tr = ss.beginTransaction(); ss.save(obj); tr.commit(); ss.close(); return obj; } catch (Exception e) { e.printStackTrace(); } return null;
    }
    public WorkerPayment update(WorkerPayment obj) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Transaction tr = ss.beginTransaction(); ss.update(obj); tr.commit(); ss.close(); return obj; } catch (Exception e) { e.printStackTrace(); } return null;
    }
    public WorkerPayment findById(String id) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); WorkerPayment obj = (WorkerPayment) ss.get(WorkerPayment.class, id); ss.close(); return obj; } catch (Exception e) { e.printStackTrace(); } return null;
    }
    public WorkerPayment delete(String id) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Transaction tr = ss.beginTransaction(); WorkerPayment obj = (WorkerPayment) ss.get(WorkerPayment.class, id); if(obj != null) { ss.delete(obj); } tr.commit(); ss.close(); return obj; } catch (Exception e) { e.printStackTrace(); } return null;
    }
    public List<WorkerPayment> findAll() {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM WorkerPayment ORDER BY workDate DESC"); List<WorkerPayment> list = q.list(); ss.close(); return list; } catch (Exception e) { e.printStackTrace(); } return Collections.EMPTY_LIST;
    }
    public List<WorkerPayment> findByProject(String projectId) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM WorkerPayment WHERE project.id = :projectId ORDER BY workDate DESC"); q.setParameter("projectId", projectId); List<WorkerPayment> list = q.list(); ss.close(); return list; } catch (Exception e) { e.printStackTrace(); } return Collections.EMPTY_LIST;
    }
    public List<WorkerPayment> findByWorker(String workerId) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM WorkerPayment WHERE worker.id = :workerId ORDER BY workDate DESC"); q.setParameter("workerId", workerId); List<WorkerPayment> list = q.list(); ss.close(); return list; } catch (Exception e) { e.printStackTrace(); } return Collections.EMPTY_LIST;
    }
    public List<WorkerPayment> findByProjectAndDate(String projectId, LocalDate workDate) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM WorkerPayment WHERE project.id = :projectId AND workDate = :workDate ORDER BY worker.fullName ASC"); q.setParameter("projectId", projectId); q.setParameter("workDate", workDate); List<WorkerPayment> list = q.list(); ss.close(); return list; } catch (Exception e) { e.printStackTrace(); } return Collections.EMPTY_LIST;
    }
    public List<WorkerPayment> findByProjectAndDateRange(String projectId, LocalDate from, LocalDate to) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM WorkerPayment WHERE project.id = :projectId AND workDate BETWEEN :from AND :to ORDER BY workDate DESC"); q.setParameter("projectId", projectId); q.setParameter("from", from); q.setParameter("to", to); List<WorkerPayment> list = q.list(); ss.close(); return list; } catch (Exception e) { e.printStackTrace(); } return Collections.EMPTY_LIST;
    }
    public List<WorkerPayment> findByPaymentStatus(String projectId, EPaymentStatus status) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM WorkerPayment WHERE project.id = :projectId AND paymentStatus = :status ORDER BY workDate DESC"); q.setParameter("projectId", projectId); q.setParameter("status", status); List<WorkerPayment> list = q.list(); ss.close(); return list; } catch (Exception e) { e.printStackTrace(); } return Collections.EMPTY_LIST;
    }
    public WorkerPayment findByAttendance(String attendanceId) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM WorkerPayment WHERE attendance.id = :attendanceId"); q.setParameter("attendanceId", attendanceId); WorkerPayment obj = (WorkerPayment) q.uniqueResult(); ss.close(); return obj; } catch (Exception e) { e.printStackTrace(); } return null;
    }
    public boolean existsByAttendance(String attendanceId) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("SELECT COUNT(wp) FROM WorkerPayment wp WHERE wp.attendance.id = :attendanceId"); q.setParameter("attendanceId", attendanceId); Long count = (Long) q.uniqueResult(); ss.close(); return count != null && count > 0; } catch (Exception e) { e.printStackTrace(); } return false;
    }
}
