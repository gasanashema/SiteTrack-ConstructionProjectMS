package dao;

import java.util.Collections;
import java.util.List;
import java.time.LocalDate;
import model.*;
import org.hibernate.*;
import util.HibernateUtil;

public class ProjectActivityDao {
    public ProjectActivity save(ProjectActivity obj) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Transaction tr = ss.beginTransaction(); ss.save(obj); tr.commit(); ss.close(); return obj; } catch (Exception e) { e.printStackTrace(); } return null;
    }
    public ProjectActivity update(ProjectActivity obj) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Transaction tr = ss.beginTransaction(); ss.update(obj); tr.commit(); ss.close(); return obj; } catch (Exception e) { e.printStackTrace(); } return null;
    }
    public ProjectActivity findById(String id) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); ProjectActivity obj = (ProjectActivity) ss.get(ProjectActivity.class, id); ss.close(); return obj; } catch (Exception e) { e.printStackTrace(); } return null;
    }
    public ProjectActivity delete(String id) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Transaction tr = ss.beginTransaction(); ProjectActivity obj = (ProjectActivity) ss.get(ProjectActivity.class, id); if(obj != null) { ss.delete(obj); } tr.commit(); ss.close(); return obj; } catch (Exception e) { e.printStackTrace(); } return null;
    }
    public List<ProjectActivity> findAll() {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM ProjectActivity ORDER BY activityDate DESC"); List<ProjectActivity> list = q.list(); ss.close(); return list; } catch (Exception e) { e.printStackTrace(); } return Collections.EMPTY_LIST;
    }
    public List<ProjectActivity> findByProject(String projectId) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM ProjectActivity WHERE project.id = :projectId ORDER BY activityDate DESC"); q.setParameter("projectId", projectId); List<ProjectActivity> list = q.list(); ss.close(); return list; } catch (Exception e) { e.printStackTrace(); } return Collections.EMPTY_LIST;
    }
    public List<ProjectActivity> findByProjectAndDateRange(String projectId, LocalDate from, LocalDate to) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM ProjectActivity WHERE project.id = :projectId AND activityDate BETWEEN :from AND :to ORDER BY activityDate DESC"); q.setParameter("projectId", projectId); q.setParameter("from", from); q.setParameter("to", to); List<ProjectActivity> list = q.list(); ss.close(); return list; } catch (Exception e) { e.printStackTrace(); } return Collections.EMPTY_LIST;
    }
    public List<ProjectActivity> findByRecordedBy(String userId) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM ProjectActivity WHERE recordedBy.id = :userId ORDER BY activityDate DESC"); q.setParameter("userId", userId); List<ProjectActivity> list = q.list(); ss.close(); return list; } catch (Exception e) { e.printStackTrace(); } return Collections.EMPTY_LIST;
    }
    public ProjectActivity findLatestByProject(String projectId) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM ProjectActivity WHERE project.id = :pid ORDER BY activityDate DESC"); q.setParameter("pid", projectId); q.setMaxResults(1); ProjectActivity obj = (ProjectActivity) q.uniqueResult(); ss.close(); return obj; } catch (Exception e) { e.printStackTrace(); } return null;
    }
}
