package kg.nurtelecom.internlabs.customerservice.repository.jdbc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

public class JdbcConnectionFactory {

    private final DataSource dataSource;

    public JdbcConnectionFactory(DataSource dataSource) {
        this.dataSource = Objects.requireNonNull(dataSource, "dataSource must not be null");
    }

    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            String msg =
                    "Failed to obtain JDBC Connection from DataSource. " +
                            "Reason: " + e.getMessage() +
                            " (sqlState=" + e.getSQLState() + ", vendorCode=" + e.getErrorCode() + ")";
            throw new IllegalStateException(msg, e);
        }
    }
}
