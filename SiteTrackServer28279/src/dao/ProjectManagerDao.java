package dao;

import java.util.Collections;
import java.util.List;
import java.time.LocalDate;
import model.*;
import org.hibernate.*;

public class ProjectManagerDao {

    public ProjectManager save(ProjectManager obj) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ss.save(obj);
            tr.commit();
            ss.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Save failed: " + e.getMessage(), e);
        }
    }

    public ProjectManager update(ProjectManager obj) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ss.update(obj);
            tr.commit();
            ss.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Update failed: " + e.getMessage(), e);
        }
    }

    public ProjectManager findById(String id) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            ProjectManager obj = (ProjectManager) ss.get(ProjectManager.class, id);
            ss.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ProjectManager delete(String id) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ProjectManager obj = (ProjectManager) ss.get(ProjectManager.class, id);
            if (obj != null) {
                obj.setStatus(EManagerStatus.REMOVED);
                ss.update(obj);
            }
            tr.commit();
            ss.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Delete failed: " + e.getMessage(), e);
        }
    }

    public List<ProjectManager> findAll() {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM ProjectManager ORDER BY assignedDate DESC");
            List<ProjectManager> list = q.list();
            ss.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }

    public List<ProjectManager> findByProject(String projectId) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM ProjectManager WHERE project.id = :projectId");
            q.setParameter("projectId", projectId);
            List<ProjectManager> list = q.list();
            ss.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }

    public List<ProjectManager> findByUser(String userId) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM ProjectManager WHERE user.id = :userId");
            q.setParameter("userId", userId);
            List<ProjectManager> list = q.list();
            ss.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }

    public List<ProjectManager> findActiveByProject(String projectId) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM ProjectManager WHERE project.id = :pid AND status = :st");
            q.setParameter("pid", projectId);
            q.setParameter("st", EManagerStatus.ACTIVE);
            List<ProjectManager> list = q.list();
            ss.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }

    public ProjectManager findByProjectAndUser(String projectId, String userId) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM ProjectManager WHERE project.id = :projectId AND user.id = :userId");
            q.setParameter("projectId", projectId);
            q.setParameter("userId", userId);
            ProjectManager obj = (ProjectManager) q.uniqueResult();
            ss.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isManagerAssigned(String projectId, String userId) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("SELECT COUNT(pm) FROM ProjectManager pm WHERE pm.project.id = :pid AND pm.user.id = :uid AND pm.status = :st");
            q.setParameter("pid", projectId);
            q.setParameter("uid", userId);
            q.setParameter("st", EManagerStatus.ACTIVE);
            Long c = (Long) q.uniqueResult();
            ss.close();
            return c != null && c > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
