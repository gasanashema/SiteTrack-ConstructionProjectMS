package dao;

import java.util.Collections;
import java.util.List;
import java.time.LocalDate;
import model.*;
import org.hibernate.*;
import util.HibernateUtil;

public class OtpVerificationDao {
    public OtpVerification save(OtpVerification obj) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Transaction tr = ss.beginTransaction(); ss.save(obj); tr.commit(); ss.close(); return obj; } catch (Exception e) { e.printStackTrace(); } return null;
    }
    public OtpVerification update(OtpVerification obj) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Transaction tr = ss.beginTransaction(); ss.update(obj); tr.commit(); ss.close(); return obj; } catch (Exception e) { e.printStackTrace(); } return null;
    }
    public OtpVerification findById(String id) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); OtpVerification obj = (OtpVerification) ss.get(OtpVerification.class, id); ss.close(); return obj; } catch (Exception e) { e.printStackTrace(); } return null;
    }
    public OtpVerification delete(String id) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Transaction tr = ss.beginTransaction(); OtpVerification obj = (OtpVerification) ss.get(OtpVerification.class, id); if(obj != null) { ss.delete(obj); } tr.commit(); ss.close(); return obj; } catch (Exception e) { e.printStackTrace(); } return null;
    }
    public List<OtpVerification> findAll() {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM OtpVerification ORDER BY createdAt DESC"); List<OtpVerification> list = q.list(); ss.close(); return list; } catch (Exception e) { e.printStackTrace(); } return Collections.EMPTY_LIST;
    }
    public List<OtpVerification> findByUser(String userId) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM OtpVerification WHERE user.id = :userId ORDER BY createdAt DESC"); q.setParameter("userId", userId); List<OtpVerification> list = q.list(); ss.close(); return list; } catch (Exception e) { e.printStackTrace(); } return Collections.EMPTY_LIST;
    }
    public OtpVerification findLatestByUser(String userId) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM OtpVerification WHERE user.id = :uid ORDER BY createdAt DESC"); q.setParameter("uid", userId); q.setMaxResults(1); OtpVerification obj = (OtpVerification) q.uniqueResult(); ss.close(); return obj; } catch (Exception e) { e.printStackTrace(); } return null;
    }
    public OtpVerification findByUserAndCode(String userId, String otpCode) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM OtpVerification WHERE user.id = :userId AND otpCode = :otpCode"); q.setParameter("userId", userId); q.setParameter("otpCode", otpCode); OtpVerification obj = (OtpVerification) q.uniqueResult(); ss.close(); return obj; } catch (Exception e) { e.printStackTrace(); } return null;
    }
    public List<OtpVerification> findUnusedByUser(String userId) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM OtpVerification WHERE user.id = :userId AND isUsed = false ORDER BY createdAt DESC"); q.setParameter("userId", userId); List<OtpVerification> list = q.list(); ss.close(); return list; } catch (Exception e) { e.printStackTrace(); } return Collections.EMPTY_LIST;
    }
    public void markAllUsedForUser(String userId) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Transaction tr = ss.beginTransaction(); Query q = ss.createQuery("UPDATE OtpVerification SET isUsed = true WHERE user.id = :uid AND isUsed = false"); q.setParameter("uid", userId); q.executeUpdate(); tr.commit(); ss.close(); } catch (Exception e) { e.printStackTrace(); }
    }
}
