package dao;

import model.AuditLog;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import java.util.List;

public class AuditLogDao {
    
    public AuditLog save(AuditLog log) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(log);
            tx.commit();
            return log;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }

    public List<AuditLog> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query query = session.createQuery("FROM AuditLog ORDER BY createdAt DESC");
            return query.list();
        } finally {
            session.close();
        }
    }
    
    public List<AuditLog> findRecent(int maxResults) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query query = session.createQuery("FROM AuditLog ORDER BY createdAt DESC");
            query.setMaxResults(maxResults);
            return query.list();
        } finally {
            session.close();
        }
    }

    public int deleteOlderThan(int months) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            // Calculate date in Java to avoid DB dialect issues
            java.time.LocalDateTime cutoff = java.time.LocalDateTime.now().minusMonths(months);
            Query query = session.createQuery("DELETE FROM AuditLog WHERE createdAt < :cutoff");
            query.setParameter("cutoff", cutoff);
            int deletedCount = query.executeUpdate();
            tx.commit();
            return deletedCount;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return 0;
        } finally {
            session.close();
        }
    }
}
