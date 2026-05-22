package dao;

import model.EAssignmentStatus;
import model.WorkerAssignment;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.Query;
import util.HibernateUtil;
import java.util.List;

public class WorkerAssignmentDao {

    public void save(WorkerAssignment assignment) {
        Transaction tx = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.save(assignment);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    public void update(WorkerAssignment assignment) {
        Transaction tx = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.update(assignment);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    public List<WorkerAssignment> findByProject(String projectId, EAssignmentStatus status) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            String hql = "FROM WorkerAssignment WHERE project.id = :projectId";
            if (status != null) {
                hql += " AND status = :status";
            }
            Query query = session.createQuery(hql);
            query.setParameter("projectId", projectId);
            if (status != null) {
                query.setParameter("status", status);
            }
            return query.list();
        } finally {
            if (session != null) session.close();
        }
    }

    public WorkerAssignment findActiveAssignmentByWorker(String workerId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            String hql = "FROM WorkerAssignment WHERE worker.id = :workerId AND status = :status";
            Query query = session.createQuery(hql);
            query.setParameter("workerId", workerId);
            query.setParameter("status", EAssignmentStatus.ACTIVE);
            query.setMaxResults(1);
            return (WorkerAssignment) query.uniqueResult();
        } finally {
            if (session != null) session.close();
        }
    }
}
