/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.Collections;
import java.util.List;
import model.Barcode;
import org.hibernate.*;

/**
 *
 * @author GeekNest
 */
public class BarcodeDao {

    public Barcode registerBarcode(Barcode barcodeObj) {
        try {

            Session ss = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ss.save(barcodeObj);
            tr.commit();
            ss.close();
            return barcodeObj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Barcode updateBarcode(Barcode barcodeObj) {
        try {

            Session ss = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ss.update(barcodeObj);
            tr.commit();
            ss.close();
            return barcodeObj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Barcode deleteBarcode(Barcode barcodeObj) {
        try {

            Session ss = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ss.delete(barcodeObj);
            tr.commit();
            ss.close();
            return barcodeObj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Barcode findBarcodeById(Barcode barcodeObj){
        try {
          Session ss = HibernateUtil.getSessionFactory().openSession();
          Barcode found = (Barcode)ss.get(Barcode.class, barcodeObj.getId());  
          ss.close();
          return found;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Barcode> findAllBarcodes(){
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            List<Barcode> barcodes = ss.createQuery("SELECT barcode From Barcode barcode").list();
            ss.close();
            return barcodes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }
}
