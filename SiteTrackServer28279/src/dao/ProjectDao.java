package dao;

import java.util.Collections;
import java.util.List;
import java.time.LocalDate;
import model.*;
import org.hibernate.*;

public class ProjectDao {

    public Project save(Project obj) {
        Session ss = null;
        Transaction tr = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            tr = ss.beginTransaction();
            ss.save(obj);
            tr.commit();
            return obj;
        } catch (Exception e) { if (tr != null && tr.isActive()) { tr.rollback(); }
            e.printStackTrace();
        } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        }
        return null;
    }

    public Project update(Project obj) {
        Session ss = null;
        Transaction tr = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            tr = ss.beginTransaction();
            ss.update(obj);
            tr.commit();
            return obj;
        } catch (Exception e) { if (tr != null && tr.isActive()) { tr.rollback(); }
            e.printStackTrace();
        } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        }
        return null;
    }

    public Project findById(String id) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Project obj = (Project) ss.get(Project.class, id);
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

    public Project delete(String id) {
        Session ss = null;
        Transaction tr = null;
        try {
            if (hasAssociatedRecords(id)) {
                throw new RuntimeException("Cannot delete project with existing purchases, usage, or attendance records.");
            }
            ss = HibernateUtil.getSessionFactory().openSession();
            tr = ss.beginTransaction();
            Project obj = (Project) ss.get(Project.class, id);
            if (obj != null) {
                Query q = ss.createQuery("DELETE FROM ProjectManager WHERE project.id = :pid");
                q.setParameter("pid", id);
                q.executeUpdate();
                
                ss.delete(obj);
            }
            tr.commit();
            return obj;
        } catch (Exception e) { if (tr != null && tr.isActive()) { tr.rollback(); }
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        }
    }

    public List<Project> findAll() {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM Project ORDER BY projectName ASC");
            List<Project> list = q.list();
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

    public List<Project> findByStatus(EProjectStatus status) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM Project WHERE status = :status");
            q.setParameter("status", status);
            List<Project> list = q.list();
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

    public List<Project> findByCreatedBy(String userId) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM Project WHERE createdBy.id = :userId");
            q.setParameter("userId", userId);
            List<Project> list = q.list();
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

    public List<Project> findAssignedToManager(String userId) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("SELECT pm.project FROM ProjectManager pm WHERE pm.user.id = :uid AND pm.status = :st");
            q.setParameter("uid", userId);
            q.setParameter("st", EManagerStatus.ACTIVE);
            List<Project> list = q.list();
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

    public boolean hasAssociatedRecords(String projectId) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q1 = ss.createQuery("SELECT COUNT(mp) FROM MaterialPurchase mp WHERE mp.project.id = :pid");
            q1.setParameter("pid", projectId);
            Query q2 = ss.createQuery("SELECT COUNT(mu) FROM MaterialUsage mu WHERE mu.project.id = :pid");
            q2.setParameter("pid", projectId);
            Query q3 = ss.createQuery("SELECT COUNT(wa) FROM WorkerAttendance wa WHERE wa.project.id = :pid");
            q3.setParameter("pid", projectId);
            Long c1 = (Long) q1.uniqueResult();
            Long c2 = (Long) q2.uniqueResult();
            Long c3 = (Long) q3.uniqueResult();
            return (c1 != null && c1 > 0) || (c2 != null && c2 > 0) || (c3 != null && c3 > 0);
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
