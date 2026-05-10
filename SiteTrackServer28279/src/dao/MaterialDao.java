package dao;

import java.util.Collections;
import java.util.List;
import java.time.LocalDate;
import model.*;
import org.hibernate.*;
import util.HibernateUtil;

public class MaterialDao {
    public Material save(Material obj) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Transaction tr = ss.beginTransaction(); ss.save(obj); tr.commit(); ss.close(); return obj; } catch (Exception e) { e.printStackTrace(); } return null;
    }
    public Material update(Material obj) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Transaction tr = ss.beginTransaction(); ss.update(obj); tr.commit(); ss.close(); return obj; } catch (Exception e) { e.printStackTrace(); } return null;
    }
    public Material findById(String id) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Material obj = (Material) ss.get(Material.class, id); ss.close(); return obj; } catch (Exception e) { e.printStackTrace(); } return null;
    }
    public Material delete(String id) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Transaction tr = ss.beginTransaction(); Material obj = (Material) ss.get(Material.class, id); if(obj != null) { obj.setStatus(EMaterialStatus.INACTIVE); ss.update(obj); } tr.commit(); ss.close(); return obj; } catch (Exception e) { e.printStackTrace(); } return null;
    }
    public List<Material> findAll() {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM Material ORDER BY materialName ASC"); List<Material> list = q.list(); ss.close(); return list; } catch (Exception e) { e.printStackTrace(); } return Collections.EMPTY_LIST;
    }
    public List<Material> findAllActive() {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM Material WHERE status = :st ORDER BY materialName ASC"); q.setParameter("st", EMaterialStatus.ACTIVE); List<Material> list = q.list(); ss.close(); return list; } catch (Exception e) { e.printStackTrace(); } return Collections.EMPTY_LIST;
    }
    public List<Material> findByCategory(String categoryId) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM Material WHERE category.id = :categoryId ORDER BY materialName ASC"); q.setParameter("categoryId", categoryId); List<Material> list = q.list(); ss.close(); return list; } catch (Exception e) { e.printStackTrace(); } return Collections.EMPTY_LIST;
    }
    public List<Material> findActiveByCategory(String categoryId) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM Material WHERE category.id = :cid AND status = :st ORDER BY materialName ASC"); q.setParameter("cid", categoryId); q.setParameter("st", EMaterialStatus.ACTIVE); List<Material> list = q.list(); ss.close(); return list; } catch (Exception e) { e.printStackTrace(); } return Collections.EMPTY_LIST;
    }
    public Material findByName(String materialName) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM Material WHERE materialName = :materialName"); q.setParameter("materialName", materialName); Material obj = (Material) q.uniqueResult(); ss.close(); return obj; } catch (Exception e) { e.printStackTrace(); } return null;
    }
    public boolean existsByName(String materialName) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("SELECT COUNT(m) FROM Material m WHERE m.materialName = :materialName"); q.setParameter("materialName", materialName); Long count = (Long) q.uniqueResult(); ss.close(); return count != null && count > 0; } catch (Exception e) { e.printStackTrace(); } return false;
    }
    public boolean hasAssociatedRecords(String materialId) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); 
              Query q1 = ss.createQuery("SELECT COUNT(mp) FROM MaterialPurchase mp WHERE mp.material.id = :mid"); q1.setParameter("mid", materialId); 
              Query q2 = ss.createQuery("SELECT COUNT(mu) FROM MaterialUsage mu WHERE mu.material.id = :mid"); q2.setParameter("mid", materialId); 
              Long c1 = (Long) q1.uniqueResult(); Long c2 = (Long) q2.uniqueResult(); 
              ss.close(); return (c1 != null && c1 > 0) || (c2 != null && c2 > 0); 
        } catch (Exception e) { e.printStackTrace(); } return false;
    }
}
