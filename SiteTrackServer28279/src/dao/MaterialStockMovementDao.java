package dao;

import java.util.Collections;
import java.util.List;
import java.time.LocalDate;
import model.*;
import org.hibernate.*;

public class MaterialStockMovementDao {
    public MaterialStockMovement save(MaterialStockMovement obj) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); Transaction tr = ss.beginTransaction(); ss.save(obj); tr.commit(); return obj; } catch (Exception e) { e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return null;
    }
    public MaterialStockMovement update(MaterialStockMovement obj) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); Transaction tr = ss.beginTransaction(); ss.update(obj); tr.commit(); return obj; } catch (Exception e) { e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return null;
    }
    public MaterialStockMovement findById(String id) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); MaterialStockMovement obj = (MaterialStockMovement) ss.get(MaterialStockMovement.class, id); return obj; } catch (Exception e) { e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return null;
    }
    public MaterialStockMovement delete(String id) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); Transaction tr = ss.beginTransaction(); MaterialStockMovement obj = (MaterialStockMovement) ss.get(MaterialStockMovement.class, id); if(obj != null) { ss.delete(obj); } tr.commit(); return obj; } catch (Exception e) { e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return null;
    }
    public List<MaterialStockMovement> findAll() {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM MaterialStockMovement ORDER BY movementDate DESC, createdAt DESC"); List<MaterialStockMovement> list = q.list(); return list; } catch (Exception e) { e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return Collections.EMPTY_LIST;
    }
    public List<MaterialStockMovement> findByProject(String projectId) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM MaterialStockMovement WHERE project.id = :projectId ORDER BY movementDate DESC, createdAt DESC"); q.setParameter("projectId", projectId); List<MaterialStockMovement> list = q.list(); return list; } catch (Exception e) { e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return Collections.EMPTY_LIST;
    }
    public List<MaterialStockMovement> findByProjectAndMaterial(String projectId, String materialId) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM MaterialStockMovement WHERE project.id = :projectId AND material.id = :materialId ORDER BY movementDate DESC, createdAt DESC"); q.setParameter("projectId", projectId); q.setParameter("materialId", materialId); List<MaterialStockMovement> list = q.list(); return list; } catch (Exception e) { e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return Collections.EMPTY_LIST;
    }
    public List<MaterialStockMovement> findByMovementType(String projectId, EMovementType type) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM MaterialStockMovement WHERE project.id = :projectId AND movementType = :type ORDER BY movementDate DESC, createdAt DESC"); q.setParameter("projectId", projectId); q.setParameter("type", type); List<MaterialStockMovement> list = q.list(); return list; } catch (Exception e) { e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return Collections.EMPTY_LIST;
    }
    public List<MaterialStockMovement> findByProjectAndDateRange(String projectId, LocalDate from, LocalDate to) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM MaterialStockMovement WHERE project.id = :projectId AND movementDate BETWEEN :from AND :to ORDER BY movementDate DESC, createdAt DESC"); q.setParameter("projectId", projectId); q.setParameter("from", from); q.setParameter("to", to); List<MaterialStockMovement> list = q.list(); return list; } catch (Exception e) { e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return Collections.EMPTY_LIST;
    }
    public List<MaterialStockMovement> findByReference(String referenceType, String referenceId) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM MaterialStockMovement WHERE referenceType = :referenceType AND referenceId = :referenceId"); q.setParameter("referenceType", referenceType); q.setParameter("referenceId", referenceId); List<MaterialStockMovement> list = q.list(); return list; } catch (Exception e) { e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return Collections.EMPTY_LIST;
    }
    public MaterialStockMovement saveWithSession(MaterialStockMovement obj, Session ss) {
        try { ss.save(obj); return obj; } catch (Exception e) { e.printStackTrace(); throw e; } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        }
    }
}
