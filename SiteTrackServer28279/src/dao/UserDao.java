package dao;

import java.util.Collections;
import java.util.List;
import java.time.LocalDate;
import model.*;
import org.hibernate.*;
import util.HibernateUtil;

public class UserDao {
    public User save(User obj) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Transaction tr = ss.beginTransaction(); ss.save(obj); tr.commit(); ss.close(); return obj; } catch (Exception e) { e.printStackTrace(); } return null;
    }
    public User update(User obj) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Transaction tr = ss.beginTransaction(); ss.update(obj); tr.commit(); ss.close(); return obj; } catch (Exception e) { e.printStackTrace(); } return null;
    }
    public User findById(String id) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); User obj = (User) ss.get(User.class, id); ss.close(); return obj; } catch (Exception e) { e.printStackTrace(); } return null;
    }
    public User delete(String id) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Transaction tr = ss.beginTransaction(); User obj = (User) ss.get(User.class, id); if(obj != null) { obj.setStatus(EUserStatus.INACTIVE); ss.update(obj); } tr.commit(); ss.close(); return obj; } catch (Exception e) { e.printStackTrace(); } return null;
    }
    public List<User> findAll() {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM User ORDER BY fullName ASC"); List<User> list = q.list(); ss.close(); return list; } catch (Exception e) { e.printStackTrace(); } return Collections.EMPTY_LIST;
    }
    public List<User> findAllActive() {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM User WHERE status = :st ORDER BY fullName ASC"); q.setParameter("st", EUserStatus.ACTIVE); List<User> list = q.list(); ss.close(); return list; } catch (Exception e) { e.printStackTrace(); } return Collections.EMPTY_LIST;
    }
    public User findByUsername(String username) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM User WHERE username = :username"); q.setParameter("username", username); User obj = (User) q.uniqueResult(); ss.close(); return obj; } catch (Exception e) { e.printStackTrace(); } return null;
    }
    public User findByEmail(String email) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM User WHERE email = :email"); q.setParameter("email", email); User obj = (User) q.uniqueResult(); ss.close(); return obj; } catch (Exception e) { e.printStackTrace(); } return null;
    }
    public List<User> findByRole(ERole role) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("FROM User WHERE role = :role ORDER BY fullName ASC"); q.setParameter("role", role); List<User> list = q.list(); ss.close(); return list; } catch (Exception e) { e.printStackTrace(); } return Collections.EMPTY_LIST;
    }
    public boolean existsByUsername(String username) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("SELECT COUNT(u) FROM User u WHERE u.username = :username"); q.setParameter("username", username); Long count = (Long) q.uniqueResult(); ss.close(); return count != null && count > 0; } catch (Exception e) { e.printStackTrace(); } return false;
    }
    public boolean existsByEmail(String email) {
        try { Session ss = HibernateUtil.getSessionFactory().openSession(); Query q = ss.createQuery("SELECT COUNT(u) FROM User u WHERE u.email = :email"); q.setParameter("email", email); Long count = (Long) q.uniqueResult(); ss.close(); return count != null && count > 0; } catch (Exception e) { e.printStackTrace(); } return false;
    }
}
