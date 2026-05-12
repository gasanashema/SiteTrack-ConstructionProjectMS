package util;

import java.io.Serializable;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.type.Type;

public class CustomIdGenerator implements IdentifierGenerator, Configurable {
    private String prefix;
    private String sequenceName;

    @Override
    public void configure(Type type, Properties params, Dialect dialect) throws MappingException {
        prefix = params.getProperty("prefix");
        sequenceName = params.getProperty("sequence_name");
    }

    @Override
    public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
        String sql = String.format("SELECT nextval('%s')", sequenceName);
        
        // Cast to Session to use standard query methods
        org.hibernate.Session hSession = (org.hibernate.Session) session;
        Number nextValue = (Number) hSession.createSQLQuery(sql).uniqueResult();
        
        // Format the ID as PREFIX-00X
        return String.format("%s-%03d", prefix, nextValue.longValue());
    }
}
