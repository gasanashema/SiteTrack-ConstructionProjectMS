/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.Collections;
import java.util.List;
import model.LibraryCard;
import org.hibernate.*;

/**
 *
 * @author GeekNest
 */
public class LibraryCardDao {

    public LibraryCard registerLibraryCard(LibraryCard libraryCardObj) {
        try {

            Session ss = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ss.save(libraryCardObj);
            tr.commit();
            ss.close();
            return libraryCardObj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public LibraryCard updateLibraryCard(LibraryCard libraryCardObj) {
        try {

            Session ss = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ss.update(libraryCardObj);
            tr.commit();
            ss.close();
            return libraryCardObj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public LibraryCard deleteLibraryCard(LibraryCard libraryCardObj) {
        try {

            Session ss = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ss.delete(libraryCardObj);
            tr.commit();
            ss.close();
            return libraryCardObj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public LibraryCard findLibraryCardById(LibraryCard libraryCardObj){
        try {
          Session ss = HibernateUtil.getSessionFactory().openSession();
          LibraryCard found = (LibraryCard)ss.get(LibraryCard.class, libraryCardObj.getId());  
          ss.close();
          return found;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<LibraryCard> findAllLibraryCards(){
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            List<LibraryCard> libraryCards = ss.createQuery("SELECT libraryCard From LibraryCard libraryCard").list();
            ss.close();
            return libraryCards;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }
}
