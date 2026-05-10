/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.Collections;
import java.util.List;
import model.Book;
import org.hibernate.*;

/**
 *
 * @author GeekNest
 */
public class BookDao {

    public Book registerBook(Book bookObj) {
        try {

            Session ss = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ss.save(bookObj);
            tr.commit();
            ss.close();
            return bookObj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Book updateBook(Book bookObj) {
        try {

            Session ss = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ss.update(bookObj);
            tr.commit();
            ss.close();
            return bookObj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Book deleteBook(Book bookObj) {
        try {

            Session ss = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ss.delete(bookObj);
            tr.commit();
            ss.close();
            return bookObj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Book findBookById(Book bookObj){
        try {
          Session ss = HibernateUtil.getSessionFactory().openSession();
          Book found = (Book)ss.get(Book.class, bookObj.getId());  
          ss.close();
          return found;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Book> findAllBooks(){
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            List<Book> books = ss.createQuery("SELECT book From Book book").list();
            ss.close();
            return books;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }
}
