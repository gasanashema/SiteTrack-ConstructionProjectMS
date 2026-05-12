package dao;

import java.util.Collections;
import java.util.List;
import java.time.LocalDate;
import model.*;
import org.hibernate.*;

public class WorkerAttendanceDao {

    public WorkerAttendance save(WorkerAttendance obj) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ss.save(obj);
            tr.commit();
            ss.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public WorkerAttendance update(WorkerAttendance obj) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ss.update(obj);
            tr.commit();
            ss.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public WorkerAttendance findById(String id) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            WorkerAttendance obj = (WorkerAttendance) ss.get(WorkerAttendance.class, id);
            ss.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public WorkerAttendance delete(String id) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            WorkerAttendance obj = (WorkerAttendance) ss.get(WorkerAttendance.class, id);
            if (obj != null) {
                ss.delete(obj);
            }
            tr.commit();
            ss.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<WorkerAttendance> findAll() {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM WorkerAttendance ORDER BY workDate DESC");
            List<WorkerAttendance> list = q.list();
            ss.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }

    public List<WorkerAttendance> findByProject(String projectId) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM WorkerAttendance WHERE project.id = :projectId ORDER BY workDate DESC");
            q.setParameter("projectId", projectId);
            List<WorkerAttendance> list = q.list();
            ss.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }

    public List<WorkerAttendance> findByWorker(String workerId) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM WorkerAttendance WHERE worker.id = :workerId ORDER BY workDate DESC");
            q.setParameter("workerId", workerId);
            List<WorkerAttendance> list = q.list();
            ss.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }

    public List<WorkerAttendance> findByProjectAndDate(String projectId, LocalDate workDate) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM WorkerAttendance WHERE project.id = :projectId AND workDate = :workDate ORDER BY worker.fullName ASC");
            q.setParameter("projectId", projectId);
            q.setParameter("workDate", workDate);
            List<WorkerAttendance> list = q.list();
            ss.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }

    public WorkerAttendance findByProjectAndWorkerAndDate(String projectId, String workerId, LocalDate workDate) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM WorkerAttendance WHERE project.id = :projectId AND worker.id = :workerId AND workDate = :workDate");
            q.setParameter("projectId", projectId);
            q.setParameter("workerId", workerId);
            q.setParameter("workDate", workDate);
            WorkerAttendance obj = (WorkerAttendance) q.uniqueResult();
            ss.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<WorkerAttendance> findByProjectAndDateRange(String projectId, LocalDate from, LocalDate to) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM WorkerAttendance WHERE project.id = :projectId AND workDate BETWEEN :from AND :to ORDER BY workDate DESC");
            q.setParameter("projectId", projectId);
            q.setParameter("from", from);
            q.setParameter("to", to);
            List<WorkerAttendance> list = q.list();
            ss.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }

    public List<WorkerAttendance> findPresentByProjectAndDate(String projectId, LocalDate workDate) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM WorkerAttendance WHERE project.id = :pid AND workDate = :wd AND attendanceStatus = :st ORDER BY worker.fullName ASC");
            q.setParameter("pid", projectId);
            q.setParameter("wd", workDate);
            q.setParameter("st", EAttendanceStatus.PRESENT);
            List<WorkerAttendance> list = q.list();
            ss.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }

    public boolean existsByProjectAndWorkerAndDate(String projectId, String workerId, LocalDate workDate) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("SELECT COUNT(wa) FROM WorkerAttendance wa WHERE wa.project.id = :projectId AND wa.worker.id = :workerId AND wa.workDate = :workDate");
            q.setParameter("projectId", projectId);
            q.setParameter("workerId", workerId);
            q.setParameter("workDate", workDate);
            Long count = (Long) q.uniqueResult();
            ss.close();
            return count != null && count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
