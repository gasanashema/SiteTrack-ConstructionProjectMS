package dao;

import java.util.Collections;
import java.util.List;
import java.time.LocalDate;
import model.*;
import org.hibernate.*;

public class MaterialDao {

    public Material save(Material obj) {
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

    public Material update(Material obj) {
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

    public Material findById(String id) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Material obj = (Material) ss.get(Material.class, id);
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

    public Material delete(String id) {
        Session ss = null;
        Transaction tr = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            tr = ss.beginTransaction();
            Material obj = (Material) ss.get(Material.class, id);
            if (obj != null) {
                obj.setStatus(EMaterialStatus.INACTIVE);
                ss.update(obj);
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

    public List<Material> findAll() {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM Material ORDER BY materialName ASC");
            List<Material> list = q.list();
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

    public List<Material> findAllActive() {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM Material WHERE status = :st ORDER BY materialName ASC");
            q.setParameter("st", EMaterialStatus.ACTIVE);
            List<Material> list = q.list();
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

    public List<Material> findByCategory(String categoryId) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM Material WHERE category.id = :categoryId ORDER BY materialName ASC");
            q.setParameter("categoryId", categoryId);
            List<Material> list = q.list();
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

    public List<Material> findActiveByCategory(String categoryId) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM Material WHERE category.id = :cid AND status = :st ORDER BY materialName ASC");
            q.setParameter("cid", categoryId);
            q.setParameter("st", EMaterialStatus.ACTIVE);
            List<Material> list = q.list();
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

    public Material findByName(String materialName) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM Material WHERE materialName = :materialName");
            q.setParameter("materialName", materialName);
            Material obj = (Material) q.uniqueResult();
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

    public boolean existsByName(String materialName) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("SELECT COUNT(m) FROM Material m WHERE m.materialName = :materialName");
            q.setParameter("materialName", materialName);
            Long count = (Long) q.uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        }
        return false;
    }

    public boolean hasAssociatedRecords(String materialId) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q1 = ss.createQuery("SELECT COUNT(mp) FROM MaterialPurchase mp WHERE mp.material.id = :mid");
            q1.setParameter("mid", materialId);
            Query q2 = ss.createQuery("SELECT COUNT(mu) FROM MaterialUsage mu WHERE mu.material.id = :mid");
            q2.setParameter("mid", materialId);
            Long c1 = (Long) q1.uniqueResult();
            Long c2 = (Long) q2.uniqueResult();
            return (c1 != null && c1 > 0) || (c2 != null && c2 > 0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        }
        return false;
    }
}
