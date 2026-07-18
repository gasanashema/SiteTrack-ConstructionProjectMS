/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.SessionFactory;

/**
 * Hibernate Utility class with a convenient method to get Session Factory
 * object.
 *
 * @author GeekNest
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory;
    
    static {
        try {
            // Create the SessionFactory from standard (hibernate.cfg.xml) config file.
            org.hibernate.cfg.AnnotationConfiguration config = new org.hibernate.cfg.AnnotationConfiguration().configure();
            
            // Override with environment variables if present
            String dbUrl = System.getenv("DB_URL");
            if (dbUrl != null && !dbUrl.isEmpty()) {
                config.setProperty("hibernate.connection.url", dbUrl);
            }
            String dbUser = System.getenv("DB_USER");
            if (dbUser != null && !dbUser.isEmpty()) {
                config.setProperty("hibernate.connection.username", dbUser);
            }
            String dbPassword = System.getenv("DB_PASSWORD");
            if (dbPassword != null) {
                config.setProperty("hibernate.connection.password", dbPassword);
            }
            
            sessionFactory = config.buildSessionFactory();
        } catch (Throwable ex) {
            // Log the exception. 
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
