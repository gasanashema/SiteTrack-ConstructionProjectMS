package dao;

import java.util.Collections;
import java.util.List;
import java.time.LocalDate;
import model.*;
import org.hibernate.*;

public class ProjectActivityDao {

    public ProjectActivity save(ProjectActivity obj) {
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

    public ProjectActivity update(ProjectActivity obj) {
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

    public ProjectActivity findById(String id) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            ProjectActivity obj = (ProjectActivity) ss.get(ProjectActivity.class, id);
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

    public ProjectActivity delete(String id) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ProjectActivity obj = (ProjectActivity) ss.get(ProjectActivity.class, id);
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

    public List<ProjectActivity> findAll() {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM ProjectActivity ORDER BY activityDate DESC");
            List<ProjectActivity> list = q.list();
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

    public List<ProjectActivity> findByProject(String projectId) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM ProjectActivity WHERE project.id = :projectId ORDER BY activityDate DESC");
            q.setParameter("projectId", projectId);
            List<ProjectActivity> list = q.list();
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

    public List<ProjectActivity> findByProjectAndDateRange(String projectId, LocalDate from, LocalDate to) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM ProjectActivity WHERE project.id = :projectId AND activityDate BETWEEN :from AND :to ORDER BY activityDate DESC");
            q.setParameter("projectId", projectId);
            q.setParameter("from", from);
            q.setParameter("to", to);
            List<ProjectActivity> list = q.list();
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

    public List<ProjectActivity> findByRecordedBy(String userId) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM ProjectActivity WHERE recordedBy.id = :userId ORDER BY activityDate DESC");
            q.setParameter("userId", userId);
            List<ProjectActivity> list = q.list();
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

    public ProjectActivity findLatestByProject(String projectId) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM ProjectActivity WHERE project.id = :pid ORDER BY activityDate DESC");
            q.setParameter("pid", projectId);
            q.setMaxResults(1);
            ProjectActivity obj = (ProjectActivity) q.uniqueResult();
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
}
