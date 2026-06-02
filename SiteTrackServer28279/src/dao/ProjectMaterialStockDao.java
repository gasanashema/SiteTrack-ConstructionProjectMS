package dao;

import java.util.Collections;
import java.util.List;
import java.time.LocalDate;
import model.*;
import org.hibernate.*;
public class ProjectMaterialStockDao {
    public ProjectMaterialStock save(ProjectMaterialStock obj) {
        Session ss = null;
        Transaction tr = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); tr = ss.beginTransaction(); ss.save(obj); tr.commit(); return obj; } catch (Exception e) { if (tr != null && tr.isActive()) { tr.rollback(); } e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return null;
    }
    public ProjectMaterialStock update(ProjectMaterialStock obj) {
        Session ss = null;
        Transaction tr = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); tr = ss.beginTransaction(); ss.update(obj); tr.commit(); return obj; } catch (Exception e) { if (tr != null && tr.isActive()) { tr.rollback(); } e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return null;
    }
    public ProjectMaterialStock findById(String id) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); ProjectMaterialStock obj = (ProjectMaterialStock) ss.get(ProjectMaterialStock.class, id); return obj; } catch (Exception e) { e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return null;
    }
    public ProjectMaterialStock delete(String id) {
        Session ss = null;
        Transaction tr = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); tr = ss.beginTransaction(); ProjectMaterialStock obj = (ProjectMaterialStock) ss.get(ProjectMaterialStock.class, id); if(obj != null) { ss.delete(obj); } tr.commit(); return obj; } catch (Exception e) { if (tr != null && tr.isActive()) { tr.rollback(); } e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return null;
    }
    public List<ProjectMaterialStock> findAll() {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM ProjectMaterialStock"); List<ProjectMaterialStock> list = q.list(); return list; } catch (Exception e) { e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return Collections.EMPTY_LIST;
    }
    public List<ProjectMaterialStock> findByProject(String projectId) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM ProjectMaterialStock WHERE project.id = :projectId"); q.setParameter("projectId", projectId); List<ProjectMaterialStock> list = q.list(); return list; } catch (Exception e) { e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return Collections.EMPTY_LIST;
    }
    public ProjectMaterialStock findByProjectAndMaterial(String projectId, String materialId) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM ProjectMaterialStock WHERE project.id = :projectId AND material.id = :materialId"); q.setParameter("projectId", projectId); q.setParameter("materialId", materialId); ProjectMaterialStock obj = (ProjectMaterialStock) q.uniqueResult(); return obj; } catch (Exception e) { e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return null;
    }
    public List<ProjectMaterialStock> findBelowMinimum(String projectId) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM ProjectMaterialStock WHERE project.id = :projectId AND quantityAvailable < minimumQuantity"); q.setParameter("projectId", projectId); List<ProjectMaterialStock> list = q.list(); return list; } catch (Exception e) { e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return Collections.EMPTY_LIST;
    }
    public boolean existsByProjectAndMaterial(String projectId, String materialId) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("SELECT COUNT(s) FROM ProjectMaterialStock s WHERE s.project.id = :projectId AND s.material.id = :materialId"); q.setParameter("projectId", projectId); q.setParameter("materialId", materialId); Long count = (Long) q.uniqueResult(); return count != null && count > 0; } catch (Exception e) { e.printStackTrace(); } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } return false;
    }
    public ProjectMaterialStock saveWithSession(ProjectMaterialStock obj, Session ss) {
        try { ss.save(obj); return obj; } catch (Exception e) { e.printStackTrace(); throw e; } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        }
    }
    public ProjectMaterialStock updateWithSession(ProjectMaterialStock obj, Session ss) {
        try { ss.update(obj); return obj; } catch (Exception e) { e.printStackTrace(); throw e; } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        }
    }
}
