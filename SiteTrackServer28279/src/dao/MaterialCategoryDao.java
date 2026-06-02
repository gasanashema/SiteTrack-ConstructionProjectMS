package dao;

import java.util.Collections;
import java.util.List;
import java.time.LocalDate;
import model.*;
import org.hibernate.*;

public class MaterialCategoryDao {

    public MaterialCategory save(MaterialCategory obj) {
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

    public MaterialCategory update(MaterialCategory obj) {
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

    public MaterialCategory findById(String id) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            MaterialCategory obj = (MaterialCategory) ss.get(MaterialCategory.class, id);
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

    public MaterialCategory delete(String id) {
        Session ss = null;
        Transaction tr = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            tr = ss.beginTransaction();
            MaterialCategory obj = (MaterialCategory) ss.get(MaterialCategory.class, id);
            if (obj != null) {
                ss.delete(obj);
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

    public List<MaterialCategory> findAll() {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM MaterialCategory ORDER BY categoryName ASC");
            List<MaterialCategory> list = q.list();
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

    public MaterialCategory findByName(String categoryName) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM MaterialCategory WHERE categoryName = :categoryName");
            q.setParameter("categoryName", categoryName);
            MaterialCategory obj = (MaterialCategory) q.uniqueResult();
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

    public boolean hasLinkedMaterials(String categoryId) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("SELECT COUNT(m) FROM Material m WHERE m.category.id = :categoryId");
            q.setParameter("categoryId", categoryId);
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

    public boolean existsByName(String categoryName) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("SELECT COUNT(m) FROM MaterialCategory m WHERE m.categoryName = :categoryName");
            q.setParameter("categoryName", categoryName);
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
}
