package kg.nurtelecom.internlabs.customerservice.repository.jdbc;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Logger;

public class SimpleDataSource implements DataSource {

    private final String url;
    private final String user;
    private final String password;

    private volatile int loginTimeoutSeconds = 0;
    private volatile PrintWriter logWriter;

    private final Properties props;

    public SimpleDataSource(String url, String user, String password) {
        this(url, user, password, null);
    }

    public SimpleDataSource(String url, String user, String password, Properties props) {
        this.url = Objects.requireNonNull(url, "JDBC url must not be null");
        this.user = user;
        this.password = password;
        this.props = (props != null) ? (Properties) props.clone() : new Properties();

        try {
            DriverManager.getDriver(this.url);
        } catch (SQLException e) {
            SQLException wrapped = new SQLException(
                    "No JDBC driver found for url: " + safeUrl(this.url) +
                            ". Check that the JDBC driver dependency is on the classpath " +
                            "(e.g., org.postgresql:postgresql for PostgreSQL).",
                    e.getSQLState(),
                    e.getErrorCode(),
                    e
            );
            throw new IllegalStateException(wrapped.getMessage(), wrapped);
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return doGetConnection(this.user, this.password);
    }

    @Override
    public Connection getConnection(String username, String pwd) throws SQLException {
        return doGetConnection(username, pwd);
    }

    private Connection doGetConnection(String username, String pwd) throws SQLException {
        try {
            if (loginTimeoutSeconds > 0) {
                DriverManager.setLoginTimeout(loginTimeoutSeconds);
            }

            Properties p = (Properties) this.props.clone();
            if (username != null) p.setProperty("user", username);
            if (pwd != null) p.setProperty("password", pwd);

            return DriverManager.getConnection(this.url, p);
        } catch (SQLException e) {
            throw new SQLException(
                    "DB connection failed. url=" + safeUrl(this.url) +
                            ", user=" + (username == null ? "<null>" : username) +
                            ", sqlState=" + e.getSQLState() +
                            ", vendorCode=" + e.getErrorCode() +
                            ", message=" + e.getMessage(),
                    e.getSQLState(),
                    e.getErrorCode(),
                    e
            );
        }
    }

    private static String safeUrl(String url) {
        int q = url.indexOf('?');
        return (q >= 0) ? url.substring(0, q) + "?…" : url;
    }

    @Override
    public PrintWriter getLogWriter() {
        return logWriter;
    }

    @Override
    public void setLogWriter(PrintWriter out) {
        this.logWriter = out;
        DriverManager.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) {
        this.loginTimeoutSeconds = seconds;
        DriverManager.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() {
        return loginTimeoutSeconds;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException("getParentLogger is not supported by SimpleDataSource");
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new SQLException("SimpleDataSource is not a wrapper");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) {
        return false;
    }
}
