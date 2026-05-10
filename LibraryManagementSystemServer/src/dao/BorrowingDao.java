/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.Collections;
import java.util.List;
import model.Borrowing;
import org.hibernate.*;

/**
 *
 * @author GeekNest
 */
public class BorrowingDao {

    public Borrowing registerBorrowing(Borrowing borrowingObj) {
        try {

            Session ss = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ss.save(borrowingObj);
            tr.commit();
            ss.close();
            return borrowingObj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Borrowing updateBorrowing(Borrowing borrowingObj) {
        try {

            Session ss = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ss.update(borrowingObj);
            tr.commit();
            ss.close();
            return borrowingObj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Borrowing deleteBorrowing(Borrowing borrowingObj) {
        try {

            Session ss = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ss.delete(borrowingObj);
            tr.commit();
            ss.close();
            return borrowingObj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Borrowing findBorrowingById(Borrowing borrowingObj){
        try {
          Session ss = HibernateUtil.getSessionFactory().openSession();
          Borrowing found = (Borrowing)ss.get(Borrowing.class, borrowingObj.getId());  
          ss.close();
          return found;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Borrowing> findAllBorrowings(){
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            List<Borrowing> borrowings = ss.createQuery("SELECT borrowing From Borrowing borrowing").list();
            ss.close();
            return borrowings;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }
}
