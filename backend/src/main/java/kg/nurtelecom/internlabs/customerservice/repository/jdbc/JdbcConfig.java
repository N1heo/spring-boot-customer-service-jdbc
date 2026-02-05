package kg.nurtelecom.internlabs.customerservice.repository.jdbc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class JdbcConfig {

    @Bean
    public DataSource dataSource(
            @Value("${app.datasource.url}") String url,
            @Value("${app.datasource.username}") String username,
            @Value("${app.datasource.password}") String password
    ) {
        return new SimpleDataSource(url, username, password);
    }

    @Bean
    public JdbcConnectionFactory jdbcConnectionFactory(DataSource dataSource) {
        return new JdbcConnectionFactory(dataSource);
    }
}
