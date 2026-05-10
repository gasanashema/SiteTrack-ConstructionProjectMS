package dao;

import java.util.Collections;
import java.util.List;
import java.time.LocalDate;
import model.*;
import org.hibernate.*;
import util.HibernateUtil;

public class WorkerTypeDao {
    public WorkerType save(WorkerType obj) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Transaction tr = ss.beginTransaction(); ss.save(obj); tr.commit(); ss.close(); return obj; } catch (Exception e) { e.printStackTrace(); } return null;
    }
    public WorkerType update(WorkerType obj) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Transaction tr = ss.beginTransaction(); ss.update(obj); tr.commit(); ss.close(); return obj; } catch (Exception e) { e.printStackTrace(); } return null;
    }
    public WorkerType findById(String id) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); WorkerType obj = (WorkerType) ss.get(WorkerType.class, id); ss.close(); return obj; } catch (Exception e) { e.printStackTrace(); } return null;
    }
    public WorkerType delete(String id) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Transaction tr = ss.beginTransaction(); WorkerType obj = (WorkerType) ss.get(WorkerType.class, id); if(obj != null) { ss.delete(obj); } tr.commit(); ss.close(); return obj; } catch (Exception e) { e.printStackTrace(); } return null;
    }
    public List<WorkerType> findAll() {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM WorkerType ORDER BY typeName ASC"); List<WorkerType> list = q.list(); ss.close(); return list; } catch (Exception e) { e.printStackTrace(); } return Collections.EMPTY_LIST;
    }
    public WorkerType findByName(String typeName) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM WorkerType WHERE typeName = :typeName"); q.setParameter("typeName", typeName); WorkerType obj = (WorkerType) q.uniqueResult(); ss.close(); return obj; } catch (Exception e) { e.printStackTrace(); } return null;
    }
    public boolean existsByName(String typeName) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("SELECT COUNT(w) FROM WorkerType w WHERE w.typeName = :typeName"); q.setParameter("typeName", typeName); Long count = (Long) q.uniqueResult(); ss.close(); return count != null && count > 0; } catch (Exception e) { e.printStackTrace(); } return false;
    }
    public boolean hasLinkedWorkers(String workerTypeId) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("SELECT COUNT(w) FROM SiteWorker w WHERE w.workerType.id = :workerTypeId"); q.setParameter("workerTypeId", workerTypeId); Long count = (Long) q.uniqueResult(); ss.close(); return count != null && count > 0; } catch (Exception e) { e.printStackTrace(); } return false;
    }
}
