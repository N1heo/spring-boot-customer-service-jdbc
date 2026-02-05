package kg.nurtelecom.internlabs.customerservice.repository.jdbc;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

@Component
public class JdbcTxRunner {

    private final JdbcConnectionFactory cf;

    public JdbcTxRunner(JdbcConnectionFactory cf) {
        this.cf = cf;
    }

    public interface Callback<T> {
        T run(Connection c) throws Exception;
    }

    public <T> T inTx(Callback<T> cb) {
        try (Connection c = cf.getConnection()) {
            boolean old = c.getAutoCommit();
            c.setAutoCommit(false);
            try {
                T res = cb.run(c);
                c.commit();
                return res;
            } catch (Exception e) {
                c.rollback();
                throw new RuntimeException(e);
            } finally {
                c.setAutoCommit(old);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
