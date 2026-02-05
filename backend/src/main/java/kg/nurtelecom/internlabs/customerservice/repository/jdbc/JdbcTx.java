package kg.nurtelecom.internlabs.customerservice.repository.jdbc;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

@Component
public class JdbcTx {

    private final JdbcConnectionFactory cf;

    public JdbcTx(JdbcConnectionFactory cf) {
        this.cf = cf;
    }

    public interface TxCallback<T> {
        T doInTx(Connection c) throws Exception;
    }

    public <T> T inTx(TxCallback<T> cb) {
        try (Connection c = cf.getConnection()) {
            boolean oldAutoCommit = c.getAutoCommit();
            c.setAutoCommit(false);
            try {
                T result = cb.doInTx(c);
                c.commit();
                return result;
            } catch (Exception e) {
                c.rollback();
                throw new RuntimeException(e);
            } finally {
                c.setAutoCommit(oldAutoCommit);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
