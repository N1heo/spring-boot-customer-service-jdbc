package kg.nurtelecom.internlabs.customerservice.config;

import kg.nurtelecom.internlabs.customerservice.repository.jdbc.JdbcConnectionFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

@Component
public class AdminBootstrapRunner implements CommandLineRunner {

    private final JdbcConnectionFactory connectionFactory;
    private final PasswordEncoder passwordEncoder;
    private final AdminBootstrapProperties props;

    public AdminBootstrapRunner(JdbcConnectionFactory connectionFactory, PasswordEncoder passwordEncoder, AdminBootstrapProperties props) {
        this.connectionFactory = connectionFactory;
        this.passwordEncoder = passwordEncoder;
        this.props = props;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!props.isEnabled()) return;

        String email = normalize(props.getEmail());
        String password = props.getPassword();

        if (email == null || email.isBlank()) return;
        if (password == null || password.isBlank()) return;

        try (Connection c = connectionFactory.getConnection()) {
            if (exists(c, email)) return;

            UUID id = UUID.randomUUID();
            String hash = passwordEncoder.encode(password);

            try (PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO users (id, email, password_hash, role, customer_id) VALUES (?, ?, ?, ?, NULL)"
            )) {
                ps.setObject(1, id);
                ps.setString(2, email);
                ps.setString(3, hash);
                ps.setString(4, "ADMIN");
                ps.executeUpdate();
            }
        }
    }

    private boolean exists(Connection c, String email) throws Exception {
        try (PreparedStatement ps = c.prepareStatement("SELECT 1 FROM users WHERE email = ? LIMIT 1")) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private String normalize(String s) {
        return s == null ? null : s.trim().toLowerCase();
    }
}
