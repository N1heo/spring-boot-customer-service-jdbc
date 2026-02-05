package kg.nurtelecom.internlabs.customerservice.repository.jdbc;

import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;


class JdbcSmokeIT {

    @Test
    void shouldConnectToDbAndRunSelect1_usingMainApplicationProperties() throws Exception {
        Properties properties = loadMainApplicationProperties();

        String url = require(properties, "app.datasource.url");
        String username = require(properties, "app.datasource.username");
        String password = require(properties, "app.datasource.password");

        DataSource dataSource = new SimpleDataSource(url, username, password);
        JdbcConnectionFactory jdbcConnectionFactory = new JdbcConnectionFactory(dataSource);

        try (Connection connection = jdbcConnectionFactory.getConnection()) {
            assertNotNull(connection);
            assertFalse(connection.isClosed());

            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT 1");
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                assertTrue(resultSet.next());
                assertEquals(1, resultSet.getInt(1));
            }
        }
    }

    private static Properties loadMainApplicationProperties() throws Exception {
        Properties properties = new Properties();
        try (InputStream in = JdbcSmokeIT.class.getClassLoader().getResourceAsStream("application.properties")) {
            assertNotNull(in, "application.properties not found on classpath");
            properties.load(in);
        }
        return properties;
    }

    private static String require(Properties properties, String key) {
        String singleProperty = properties.getProperty(key);
        assertNotNull(singleProperty, "Missing property: " + key);
        assertFalse(singleProperty.isBlank(), "Blank property: " + key);
        return singleProperty.trim();
    }
}
