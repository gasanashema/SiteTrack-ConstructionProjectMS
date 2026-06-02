package dao;

import java.util.Collections;
import java.util.List;
import java.time.LocalDate;
import model.*;
import org.hibernate.*;

public class ProjectManagerDao {

    public ProjectManager save(ProjectManager obj) {
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
            throw new RuntimeException("Save failed: " + e.getMessage(), e);
        } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        }
    }

    public ProjectManager update(ProjectManager obj) {
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
            throw new RuntimeException("Update failed: " + e.getMessage(), e);
        } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        }
    }

    public ProjectManager findById(String id) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            ProjectManager obj = (ProjectManager) ss.get(ProjectManager.class, id);
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

    public ProjectManager delete(String id) {
        Session ss = null;
        Transaction tr = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            tr = ss.beginTransaction();
            ProjectManager obj = (ProjectManager) ss.get(ProjectManager.class, id);
            if (obj != null) {
                obj.setStatus(EManagerStatus.REMOVED);
                ss.update(obj);
            }
            tr.commit();
            return obj;
        } catch (Exception e) { if (tr != null && tr.isActive()) { tr.rollback(); }
            e.printStackTrace();
            throw new RuntimeException("Delete failed: " + e.getMessage(), e);
        } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        }
    }

    public List<ProjectManager> findAll() {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM ProjectManager ORDER BY assignedDate DESC");
            List<ProjectManager> list = q.list();
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

    public List<ProjectManager> findByProject(String projectId) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM ProjectManager WHERE project.id = :projectId");
            q.setParameter("projectId", projectId);
            List<ProjectManager> list = q.list();
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

    public List<ProjectManager> findByUser(String userId) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM ProjectManager WHERE user.id = :userId");
            q.setParameter("userId", userId);
            List<ProjectManager> list = q.list();
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

    public List<ProjectManager> findActiveByProject(String projectId) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM ProjectManager WHERE project.id = :pid AND status = :st");
            q.setParameter("pid", projectId);
            q.setParameter("st", EManagerStatus.ACTIVE);
            List<ProjectManager> list = q.list();
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

    public ProjectManager findByProjectAndUser(String projectId, String userId) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM ProjectManager WHERE project.id = :projectId AND user.id = :userId");
            q.setParameter("projectId", projectId);
            q.setParameter("userId", userId);
            ProjectManager obj = (ProjectManager) q.uniqueResult();
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

    public boolean isManagerAssigned(String projectId, String userId) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("SELECT COUNT(pm) FROM ProjectManager pm WHERE pm.project.id = :pid AND pm.user.id = :uid AND pm.status = :st");
            q.setParameter("pid", projectId);
            q.setParameter("uid", userId);
            q.setParameter("st", EManagerStatus.ACTIVE);
            Long c = (Long) q.uniqueResult();
            return c != null && c > 0;
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
