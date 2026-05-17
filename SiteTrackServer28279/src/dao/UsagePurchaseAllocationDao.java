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
        try { ss.save(obj); return obj; } catch (Exception e) { e.printStackTrace(); throw e; }
    }
    
    public BigDecimal getSumOfAllocationsForPurchase(String purchaseId) {
        try { 
            Session ss = HibernateUtil.getSessionFactory().openSession(); 
            Query q = ss.createQuery("SELECT SUM(allocatedQuantity) FROM UsagePurchaseAllocation WHERE purchase.id = :purchaseId"); 
            q.setParameter("purchaseId", purchaseId); 
            BigDecimal sum = (BigDecimal) q.uniqueResult(); 
            ss.close(); 
            return sum != null ? sum : BigDecimal.ZERO; 
        } catch (Exception e) { 
            e.printStackTrace(); 
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
        } 
    }
}
