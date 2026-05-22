package dao;

import java.util.Collections;
import java.util.List;
import java.time.LocalDate;
import model.*;
import org.hibernate.*;

public class MaterialUsageDao {
    public MaterialUsage save(MaterialUsage obj) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); Transaction tr = ss.beginTransaction(); ss.save(obj); tr.commit(); return obj; } catch (Exception e) { e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return null;
    }
    public MaterialUsage update(MaterialUsage obj) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); Transaction tr = ss.beginTransaction(); ss.update(obj); tr.commit(); return obj; } catch (Exception e) { e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return null;
    }
    public MaterialUsage findById(String id) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); MaterialUsage obj = (MaterialUsage) ss.get(MaterialUsage.class, id); return obj; } catch (Exception e) { e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return null;
    }
    public MaterialUsage delete(String id) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); Transaction tr = ss.beginTransaction(); MaterialUsage obj = (MaterialUsage) ss.get(MaterialUsage.class, id); if(obj != null) { ss.delete(obj); } tr.commit(); return obj; } catch (Exception e) { e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return null;
    }
    public List<MaterialUsage> findAll() {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM MaterialUsage ORDER BY usageDate DESC"); List<MaterialUsage> list = q.list(); return list; } catch (Exception e) { e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return Collections.EMPTY_LIST;
    }
    public List<MaterialUsage> findByProject(String projectId) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM MaterialUsage WHERE project.id = :projectId ORDER BY usageDate DESC"); q.setParameter("projectId", projectId); List<MaterialUsage> list = q.list(); return list; } catch (Exception e) { e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return Collections.EMPTY_LIST;
    }
    public List<MaterialUsage> findByMaterial(String materialId) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM MaterialUsage WHERE material.id = :materialId ORDER BY usageDate DESC"); q.setParameter("materialId", materialId); List<MaterialUsage> list = q.list(); return list; } catch (Exception e) { e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return Collections.EMPTY_LIST;
    }
    public List<MaterialUsage> findByProjectAndMaterial(String projectId, String materialId) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM MaterialUsage WHERE project.id = :projectId AND material.id = :materialId ORDER BY usageDate DESC"); q.setParameter("projectId", projectId); q.setParameter("materialId", materialId); List<MaterialUsage> list = q.list(); return list; } catch (Exception e) { e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return Collections.EMPTY_LIST;
    }
    public List<MaterialUsage> findByProjectAndDateRange(String projectId, LocalDate from, LocalDate to) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM MaterialUsage WHERE project.id = :projectId AND usageDate BETWEEN :from AND :to ORDER BY usageDate DESC"); q.setParameter("projectId", projectId); q.setParameter("from", from); q.setParameter("to", to); List<MaterialUsage> list = q.list(); return list; } catch (Exception e) { e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return Collections.EMPTY_LIST;
    }
    public List<MaterialUsage> findByRecordedBy(String userId) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM MaterialUsage WHERE recordedBy.id = :userId ORDER BY usageDate DESC"); q.setParameter("userId", userId); List<MaterialUsage> list = q.list(); return list; } catch (Exception e) { e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return Collections.EMPTY_LIST;
    }
    public MaterialUsage saveWithSession(MaterialUsage obj, Session ss) {
        try { ss.save(obj); return obj; } catch (Exception e) { e.printStackTrace(); throw e; } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        }
    }
}
