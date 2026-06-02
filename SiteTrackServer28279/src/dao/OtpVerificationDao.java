package dao;

import java.util.Collections;
import java.util.List;
import model.*;
import org.hibernate.*;

public class OtpVerificationDao {
    public OtpVerification save(OtpVerification obj) {
        Session ss = null;
        Transaction tr = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); tr = ss.beginTransaction(); ss.save(obj); tr.commit(); return obj; } catch (Exception e) { if (tr != null && tr.isActive()) { tr.rollback(); } e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return null;
    }
    public OtpVerification update(OtpVerification obj) {
        Session ss = null;
        Transaction tr = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); tr = ss.beginTransaction(); ss.update(obj); tr.commit(); return obj; } catch (Exception e) { if (tr != null && tr.isActive()) { tr.rollback(); } e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return null;
    }
    public OtpVerification findById(String id) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); OtpVerification obj = (OtpVerification) ss.get(OtpVerification.class, id); return obj; } catch (Exception e) { e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return null;
    }
    public OtpVerification delete(String id) {
        Session ss = null;
        Transaction tr = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); tr = ss.beginTransaction(); OtpVerification obj = (OtpVerification) ss.get(OtpVerification.class, id); if(obj != null) { ss.delete(obj); } tr.commit(); return obj; } catch (Exception e) { if (tr != null && tr.isActive()) { tr.rollback(); } e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return null;
    }
    public List<OtpVerification> findAll() {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM OtpVerification ORDER BY createdAt DESC"); List<OtpVerification> list = q.list(); return list; } catch (Exception e) { e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return Collections.EMPTY_LIST;
    }
    public List<OtpVerification> findByUser(String userId) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM OtpVerification WHERE user.id = :userId ORDER BY createdAt DESC"); q.setParameter("userId", userId); List<OtpVerification> list = q.list(); return list; } catch (Exception e) { e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return Collections.EMPTY_LIST;
    }
    public OtpVerification findLatestByUser(String userId) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM OtpVerification WHERE user.id = :uid ORDER BY createdAt DESC"); q.setParameter("uid", userId); q.setMaxResults(1); OtpVerification obj = (OtpVerification) q.uniqueResult(); return obj; } catch (Exception e) { e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return null;
    }
    public OtpVerification findByUserAndCode(String userId, String otpCode) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM OtpVerification WHERE user.id = :userId AND otpCode = :otpCode"); q.setParameter("userId", userId); q.setParameter("otpCode", otpCode); OtpVerification obj = (OtpVerification) q.uniqueResult(); return obj; } catch (Exception e) { e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return null;
    }
    public List<OtpVerification> findUnusedByUser(String userId) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM OtpVerification WHERE user.id = :userId AND isUsed = false ORDER BY createdAt DESC"); q.setParameter("userId", userId); List<OtpVerification> list = q.list(); return list; } catch (Exception e) { e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return Collections.EMPTY_LIST;
    }
    public void markAllUsedForUser(String userId) {
        Session ss = null;
        Transaction tr = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); tr = ss.beginTransaction(); Query q = ss.createQuery("UPDATE OtpVerification SET isUsed = true WHERE user.id = :uid AND isUsed = false"); q.setParameter("uid", userId); q.executeUpdate(); tr.commit(); } catch (Exception e) { if (tr != null && tr.isActive()) { tr.rollback(); } e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        }
    }
    public OtpVerification saveWithSession(OtpVerification obj, Session ss) {
        try { ss.save(obj); return obj; } catch (Exception e) { e.printStackTrace(); throw e; } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        }
    }
}
