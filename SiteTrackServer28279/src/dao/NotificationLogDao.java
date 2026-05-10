package dao;

import java.util.Collections;
import java.util.List;
import java.time.LocalDate;
import model.*;
import org.hibernate.*;
import util.HibernateUtil;

public class NotificationLogDao {
    public NotificationLog save(NotificationLog obj) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Transaction tr = ss.beginTransaction(); ss.save(obj); tr.commit(); ss.close(); return obj; } catch (Exception e) { e.printStackTrace(); } return null;
    }
    public NotificationLog update(NotificationLog obj) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Transaction tr = ss.beginTransaction(); ss.update(obj); tr.commit(); ss.close(); return obj; } catch (Exception e) { e.printStackTrace(); } return null;
    }
    public NotificationLog findById(String id) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); NotificationLog obj = (NotificationLog) ss.get(NotificationLog.class, id); ss.close(); return obj; } catch (Exception e) { e.printStackTrace(); } return null;
    }
    public NotificationLog delete(String id) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Transaction tr = ss.beginTransaction(); NotificationLog obj = (NotificationLog) ss.get(NotificationLog.class, id); if(obj != null) { ss.delete(obj); } tr.commit(); ss.close(); return obj; } catch (Exception e) { e.printStackTrace(); } return null;
    }
    public List<NotificationLog> findAll() {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM NotificationLog ORDER BY createdAt DESC"); List<NotificationLog> list = q.list(); ss.close(); return list; } catch (Exception e) { e.printStackTrace(); } return Collections.EMPTY_LIST;
    }
    public List<NotificationLog> findByUser(String userId) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM NotificationLog WHERE user.id = :userId ORDER BY createdAt DESC"); q.setParameter("userId", userId); List<NotificationLog> list = q.list(); ss.close(); return list; } catch (Exception e) { e.printStackTrace(); } return Collections.EMPTY_LIST;
    }
    public List<NotificationLog> findByStatus(ENotifStatus status) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM NotificationLog WHERE status = :status ORDER BY createdAt DESC"); q.setParameter("status", status); List<NotificationLog> list = q.list(); ss.close(); return list; } catch (Exception e) { e.printStackTrace(); } return Collections.EMPTY_LIST;
    }
    public List<NotificationLog> findByChannel(ENotifChannel channel) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM NotificationLog WHERE channel = :channel ORDER BY createdAt DESC"); q.setParameter("channel", channel); List<NotificationLog> list = q.list(); ss.close(); return list; } catch (Exception e) { e.printStackTrace(); } return Collections.EMPTY_LIST;
    }
    public List<NotificationLog> findByEventType(String eventType) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM NotificationLog WHERE eventType = :eventType ORDER BY createdAt DESC"); q.setParameter("eventType", eventType); List<NotificationLog> list = q.list(); ss.close(); return list; } catch (Exception e) { e.printStackTrace(); } return Collections.EMPTY_LIST;
    }
    public List<NotificationLog> findFailedNotifications() {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM NotificationLog WHERE status = :st ORDER BY createdAt DESC"); q.setParameter("st", ENotifStatus.FAILED); List<NotificationLog> list = q.list(); ss.close(); return list; } catch (Exception e) { e.printStackTrace(); } return Collections.EMPTY_LIST;
    }
    public List<NotificationLog> findByUserAndEventType(String userId, String eventType) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM NotificationLog WHERE user.id = :userId AND eventType = :eventType ORDER BY createdAt DESC"); q.setParameter("userId", userId); q.setParameter("eventType", eventType); List<NotificationLog> list = q.list(); ss.close(); return list; } catch (Exception e) { e.printStackTrace(); } return Collections.EMPTY_LIST;
    }
}
