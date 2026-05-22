package dao;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import model.UsagePurchaseAllocation;
import org.hibernate.Query;
import org.hibernate.Session;
import util.HibernateUtil;

public class UsagePurchaseAllocationDao {
    public UsagePurchaseAllocation saveWithSession(UsagePurchaseAllocation obj, Session ss) {
        try { ss.save(obj); return obj; } catch (Exception e) { e.printStackTrace(); throw e; } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        }
    }
    
    public BigDecimal getSumOfAllocationsForPurchase(String purchaseId) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession(); 
            Query q = ss.createQuery("SELECT SUM(allocatedQuantity) FROM UsagePurchaseAllocation WHERE purchase.id = :purchaseId"); 
            q.setParameter("purchaseId", purchaseId); 
            BigDecimal sum = (BigDecimal) q.uniqueResult(); 
            return sum != null ? sum : BigDecimal.ZERO; 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } 
        return BigDecimal.ZERO;
    }
    
    public BigDecimal getSumOfAllocationsForPurchaseWithSession(String purchaseId, Session ss) {
        try { 
            Query q = ss.createQuery("SELECT SUM(allocatedQuantity) FROM UsagePurchaseAllocation WHERE purchase.id = :purchaseId"); 
            q.setParameter("purchaseId", purchaseId); 
            BigDecimal sum = (BigDecimal) q.uniqueResult(); 
            return sum != null ? sum : BigDecimal.ZERO; 
        } catch (Exception e) { 
            e.printStackTrace(); 
            throw e;
        } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        } 
    }
}
