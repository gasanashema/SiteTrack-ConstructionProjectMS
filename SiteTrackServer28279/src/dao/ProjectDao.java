package dao;

import java.util.Collections;
import java.util.List;
import java.time.LocalDate;
import model.*;
import org.hibernate.*;

public class ProjectDao {

    public Project save(Project obj) {
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

    public Project update(Project obj) {
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

    public Project findById(String id) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Project obj = (Project) ss.get(Project.class, id);
            ss.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Project delete(String id) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            Project obj = (Project) ss.get(Project.class, id);
            if (obj != null) {
                obj.setStatus(EProjectStatus.CANCELLED);
                ss.update(obj);
            }
            tr.commit();
            ss.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Project> findAll() {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM Project ORDER BY projectName ASC");
            List<Project> list = q.list();
            ss.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }

    public List<Project> findByStatus(EProjectStatus status) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM Project WHERE status = :status");
            q.setParameter("status", status);
            List<Project> list = q.list();
            ss.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }

    public List<Project> findByCreatedBy(String userId) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM Project WHERE createdBy.id = :userId");
            q.setParameter("userId", userId);
            List<Project> list = q.list();
            ss.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }

    public List<Project> findAssignedToManager(String userId) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("SELECT pm.project FROM ProjectManager pm WHERE pm.user.id = :uid AND pm.status = :st");
            q.setParameter("uid", userId);
            q.setParameter("st", EManagerStatus.ACTIVE);
            List<Project> list = q.list();
            ss.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }

    public boolean hasAssociatedRecords(String projectId) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Query q1 = ss.createQuery("SELECT COUNT(mp) FROM MaterialPurchase mp WHERE mp.project.id = :pid");
            q1.setParameter("pid", projectId);
            Query q2 = ss.createQuery("SELECT COUNT(mu) FROM MaterialUsage mu WHERE mu.project.id = :pid");
            q2.setParameter("pid", projectId);
            Query q3 = ss.createQuery("SELECT COUNT(wa) FROM WorkerAttendance wa WHERE wa.project.id = :pid");
            q3.setParameter("pid", projectId);
            Long c1 = (Long) q1.uniqueResult();
            Long c2 = (Long) q2.uniqueResult();
            Long c3 = (Long) q3.uniqueResult();
            ss.close();
            return (c1 != null && c1 > 0) || (c2 != null && c2 > 0) || (c3 != null && c3 > 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
