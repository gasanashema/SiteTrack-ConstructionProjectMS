package dao;

import java.util.Collections;
import java.util.List;
import java.time.LocalDate;
import model.*;
import org.hibernate.*;

public class NotificationLogDao {

    public NotificationLog save(NotificationLog obj) {
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

    public NotificationLog update(NotificationLog obj) {
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

    public NotificationLog findById(String id) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            NotificationLog obj = (NotificationLog) ss.get(NotificationLog.class, id);
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

    public NotificationLog delete(String id) {
        Session ss = null;
        Transaction tr = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            tr = ss.beginTransaction();
            NotificationLog obj = (NotificationLog) ss.get(NotificationLog.class, id);
            if (obj != null) {
                ss.delete(obj);
            }
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

    public List<NotificationLog> findAll() {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM NotificationLog ORDER BY createdAt DESC");
            List<NotificationLog> list = q.list();
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

    public List<NotificationLog> findByUser(String userId) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM NotificationLog WHERE user.id = :userId ORDER BY createdAt DESC");
            q.setParameter("userId", userId);
            List<NotificationLog> list = q.list();
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

    public List<NotificationLog> findByStatus(ENotifStatus status) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM NotificationLog WHERE status = :status ORDER BY createdAt DESC");
            q.setParameter("status", status);
            List<NotificationLog> list = q.list();
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

    public List<NotificationLog> findByChannel(ENotifChannel channel) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM NotificationLog WHERE channel = :channel ORDER BY createdAt DESC");
            q.setParameter("channel", channel);
            List<NotificationLog> list = q.list();
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

    public List<NotificationLog> findByEventType(String eventType) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM NotificationLog WHERE eventType = :eventType ORDER BY createdAt DESC");
            q.setParameter("eventType", eventType);
            List<NotificationLog> list = q.list();
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

    public List<NotificationLog> findFailedNotifications() {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM NotificationLog WHERE status = :st ORDER BY createdAt DESC");
            q.setParameter("st", ENotifStatus.FAILED);
            List<NotificationLog> list = q.list();
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

    public List<NotificationLog> findByUserAndEventType(String userId, String eventType) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM NotificationLog WHERE user.id = :userId AND eventType = :eventType ORDER BY createdAt DESC");
            q.setParameter("userId", userId);
            q.setParameter("eventType", eventType);
            List<NotificationLog> list = q.list();
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
}
