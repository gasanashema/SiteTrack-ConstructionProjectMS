package dao;

import java.util.Collections;
import java.util.List;
import java.time.LocalDate;
import model.*;
import org.hibernate.*;

public class WorkerPaymentDao {

    public WorkerPayment save(WorkerPayment obj) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ss.save(obj);
            tr.commit();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        }
        return null;
    }

    public WorkerPayment update(WorkerPayment obj) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ss.update(obj);
            tr.commit();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        }
        return null;
    }

    public WorkerPayment findById(String id) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            WorkerPayment obj = (WorkerPayment) ss.get(WorkerPayment.class, id);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        }
        return null;
    }

    public WorkerPayment delete(String id) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            WorkerPayment obj = (WorkerPayment) ss.get(WorkerPayment.class, id);
            if (obj != null) {
                ss.delete(obj);
            }
            tr.commit();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        }
        return null;
    }

    public List<WorkerPayment> findAll() {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM WorkerPayment ORDER BY workDate DESC");
            List<WorkerPayment> list = q.list();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        }
        return Collections.EMPTY_LIST;
    }

    public List<WorkerPayment> findByProject(String projectId) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM WorkerPayment WHERE project.id = :projectId ORDER BY workDate DESC");
            q.setParameter("projectId", projectId);
            List<WorkerPayment> list = q.list();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        }
        return Collections.EMPTY_LIST;
    }

    public List<WorkerPayment> findByWorker(String workerId) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM WorkerPayment WHERE worker.id = :workerId ORDER BY workDate DESC");
            q.setParameter("workerId", workerId);
            List<WorkerPayment> list = q.list();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        }
        return Collections.EMPTY_LIST;
    }

    public List<WorkerPayment> findByProjectAndDate(String projectId, LocalDate workDate) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM WorkerPayment WHERE project.id = :projectId AND workDate = :workDate ORDER BY worker.fullName ASC");
            q.setParameter("projectId", projectId);
            q.setParameter("workDate", workDate);
            List<WorkerPayment> list = q.list();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        }
        return Collections.EMPTY_LIST;
    }

    public List<WorkerPayment> findByProjectAndDateRange(String projectId, LocalDate from, LocalDate to) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM WorkerPayment WHERE project.id = :projectId AND workDate BETWEEN :from AND :to ORDER BY workDate DESC");
            q.setParameter("projectId", projectId);
            q.setParameter("from", from);
            q.setParameter("to", to);
            List<WorkerPayment> list = q.list();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        }
        return Collections.EMPTY_LIST;
    }

    public List<WorkerPayment> findByPaymentStatus(String projectId, EPaymentStatus status) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM WorkerPayment WHERE project.id = :projectId AND paymentStatus = :status ORDER BY workDate DESC");
            q.setParameter("projectId", projectId);
            q.setParameter("status", status);
            List<WorkerPayment> list = q.list();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        }
        return Collections.EMPTY_LIST;
    }

    public WorkerPayment findByAttendance(String attendanceId) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM WorkerPayment WHERE attendance.id = :attendanceId");
            q.setParameter("attendanceId", attendanceId);
            WorkerPayment obj = (WorkerPayment) q.uniqueResult();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        }
        return null;
    }

    public boolean existsByAttendance(String attendanceId) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("SELECT COUNT(wp) FROM WorkerPayment wp WHERE wp.attendance.id = :attendanceId");
            q.setParameter("attendanceId", attendanceId);
            Long count = (Long) q.uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        }
        return false;
    }
}
