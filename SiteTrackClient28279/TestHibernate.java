import org.hibernate.Session;
import util.HibernateUtil;
public class TestHibernate {
    public static void main(String[] args) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            System.out.println("Hibernate Initialized!");
            ss.close();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
