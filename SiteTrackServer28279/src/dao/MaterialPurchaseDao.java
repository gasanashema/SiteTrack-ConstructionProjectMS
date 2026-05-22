package dao;

import java.util.Collections;
import java.util.List;
import java.time.LocalDate;
import model.*;
import org.hibernate.*;

public class MaterialPurchaseDao {
    public MaterialPurchase save(MaterialPurchase obj) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); Transaction tr = ss.beginTransaction(); ss.save(obj); tr.commit(); return obj; } catch (Exception e) { e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return null;
    }
    public MaterialPurchase update(MaterialPurchase obj) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); Transaction tr = ss.beginTransaction(); ss.update(obj); tr.commit(); return obj; } catch (Exception e) { e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return null;
    }
    public MaterialPurchase findById(String id) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); MaterialPurchase obj = (MaterialPurchase) ss.get(MaterialPurchase.class, id); return obj; } catch (Exception e) { e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return null;
    }
    public MaterialPurchase delete(String id) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); Transaction tr = ss.beginTransaction(); MaterialPurchase obj = (MaterialPurchase) ss.get(MaterialPurchase.class, id); if(obj != null) { ss.delete(obj); } tr.commit(); return obj; } catch (Exception e) { e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return null;
    }
    public List<MaterialPurchase> findAll() {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM MaterialPurchase ORDER BY purchaseDate DESC"); List<MaterialPurchase> list = q.list(); return list; } catch (Exception e) { e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return Collections.EMPTY_LIST;
    }
    public List<MaterialPurchase> findByProject(String projectId) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM MaterialPurchase WHERE project.id = :projectId ORDER BY purchaseDate DESC"); q.setParameter("projectId", projectId); List<MaterialPurchase> list = q.list(); return list; } catch (Exception e) { e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return Collections.EMPTY_LIST;
    }
    public List<MaterialPurchase> findByMaterial(String materialId) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM MaterialPurchase WHERE material.id = :materialId ORDER BY purchaseDate DESC"); q.setParameter("materialId", materialId); List<MaterialPurchase> list = q.list(); return list; } catch (Exception e) { e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return Collections.EMPTY_LIST;
    }
    public List<MaterialPurchase> findByProjectAndMaterial(String projectId, String materialId) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM MaterialPurchase WHERE project.id = :projectId AND material.id = :materialId ORDER BY purchaseDate DESC"); q.setParameter("projectId", projectId); q.setParameter("materialId", materialId); List<MaterialPurchase> list = q.list(); return list; } catch (Exception e) { e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return Collections.EMPTY_LIST;
    }
    public List<MaterialPurchase> findByProjectAndDateRange(String projectId, LocalDate from, LocalDate to) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM MaterialPurchase WHERE project.id = :projectId AND purchaseDate BETWEEN :from AND :to ORDER BY purchaseDate DESC"); q.setParameter("projectId", projectId); q.setParameter("from", from); q.setParameter("to", to); List<MaterialPurchase> list = q.list(); return list; } catch (Exception e) { e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return Collections.EMPTY_LIST;
    }
    public List<MaterialPurchase> findByRecordedBy(String userId) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM MaterialPurchase WHERE recordedBy.id = :userId ORDER BY purchaseDate DESC"); q.setParameter("userId", userId); List<MaterialPurchase> list = q.list(); return list; } catch (Exception e) { e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return Collections.EMPTY_LIST;
    }
    public MaterialPurchase saveWithSession(MaterialPurchase obj, Session ss) {
        try { ss.save(obj); return obj; } catch (Exception e) { e.printStackTrace(); throw e; } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        }
    }
    public MaterialPurchase updateWithSession(MaterialPurchase obj, Session ss) {
        try { ss.update(obj); return obj; } catch (Exception e) { e.printStackTrace(); throw e; } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        }
    }
    public List<MaterialPurchase> findAvailablePurchases(String projectId, String materialId) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM MaterialPurchase WHERE project.id = :projectId AND material.id = :materialId AND stockStatus != 'DEPLETED' ORDER BY purchaseDate ASC, createdAt ASC"); q.setParameter("projectId", projectId); q.setParameter("materialId", materialId); List<MaterialPurchase> list = q.list(); return list; } catch (Exception e) { e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return Collections.EMPTY_LIST;
    }
}
