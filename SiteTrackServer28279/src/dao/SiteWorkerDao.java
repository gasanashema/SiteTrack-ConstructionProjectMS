package dao;

import java.util.Collections;
import java.util.List;
import java.time.LocalDate;
import model.*;
import org.hibernate.*;

public class SiteWorkerDao {

    public SiteWorker save(SiteWorker obj) {
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

    public SiteWorker update(SiteWorker obj) {
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

    public SiteWorker findById(String id) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            SiteWorker obj = (SiteWorker) ss.get(SiteWorker.class, id);
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

    public SiteWorker delete(String id) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            SiteWorker obj = (SiteWorker) ss.get(SiteWorker.class, id);
            if (obj != null) {
                obj.setStatus(EWorkerStatus.INACTIVE);
                ss.update(obj);
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

    public List<SiteWorker> findAll() {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM SiteWorker ORDER BY fullName ASC");
            List<SiteWorker> list = q.list();
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

    public List<SiteWorker> findAllActive() {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM SiteWorker WHERE status = :st ORDER BY fullName ASC");
            q.setParameter("st", EWorkerStatus.ACTIVE);
            List<SiteWorker> list = q.list();
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

    public List<SiteWorker> findByWorkerType(String workerTypeId) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM SiteWorker WHERE workerType.id = :workerTypeId ORDER BY fullName ASC");
            q.setParameter("workerTypeId", workerTypeId);
            List<SiteWorker> list = q.list();
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

    public List<SiteWorker> findActiveByWorkerType(String workerTypeId) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM SiteWorker WHERE workerType.id = :wid AND status = :st ORDER BY fullName ASC");
            q.setParameter("wid", workerTypeId);
            q.setParameter("st", EWorkerStatus.ACTIVE);
            List<SiteWorker> list = q.list();
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

    public List<SiteWorker> findByProject(String projectId) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("SELECT DISTINCT wa.worker FROM WorkerAttendance wa WHERE wa.project.id = :projectId");
            q.setParameter("projectId", projectId);
            List<SiteWorker> list = q.list();
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

    public SiteWorker findByName(String fullName) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM SiteWorker WHERE fullName = :fullName");
            q.setParameter("fullName", fullName);
            SiteWorker obj = (SiteWorker) q.uniqueResult();
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
