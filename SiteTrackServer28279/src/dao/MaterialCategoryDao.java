package dao;

import java.util.Collections;
import java.util.List;
import java.time.LocalDate;
import model.*;
import org.hibernate.*;

public class MaterialCategoryDao {

    public MaterialCategory save(MaterialCategory obj) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ss.save(obj);
            tr.commit();
            ss.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public MaterialCategory update(MaterialCategory obj) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ss.update(obj);
            tr.commit();
            ss.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public MaterialCategory findById(String id) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            MaterialCategory obj = (MaterialCategory) ss.get(MaterialCategory.class, id);
            ss.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public MaterialCategory delete(String id) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            MaterialCategory obj = (MaterialCategory) ss.get(MaterialCategory.class, id);
            if (obj != null) {
                ss.delete(obj);
            }
            tr.commit();
            ss.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<MaterialCategory> findAll() {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM MaterialCategory ORDER BY categoryName ASC");
            List<MaterialCategory> list = q.list();
            ss.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }

    public MaterialCategory findByName(String categoryName) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("FROM MaterialCategory WHERE categoryName = :categoryName");
            q.setParameter("categoryName", categoryName);
            MaterialCategory obj = (MaterialCategory) q.uniqueResult();
            ss.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean hasLinkedMaterials(String categoryId) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("SELECT COUNT(m) FROM Material m WHERE m.category.id = :categoryId");
            q.setParameter("categoryId", categoryId);
            Long count = (Long) q.uniqueResult();
            ss.close();
            return count != null && count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean existsByName(String categoryName) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("SELECT COUNT(m) FROM MaterialCategory m WHERE m.categoryName = :categoryName");
            q.setParameter("categoryName", categoryName);
            Long count = (Long) q.uniqueResult();
            ss.close();
            return count != null && count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
