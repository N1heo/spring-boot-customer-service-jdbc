package kg.nurtelecom.internlabs.customerservice.security;

import kg.nurtelecom.internlabs.customerservice.enums.Role;
import kg.nurtelecom.internlabs.customerservice.repository.jdbc.JdbcConnectionFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class CustomerDetailService implements UserDetailsService {

    private final JdbcConnectionFactory cf;

    public CustomerDetailService(JdbcConnectionFactory cf) {
        this.cf = cf;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        String normalized = email == null ? null : email.trim().toLowerCase();
        if (normalized == null || normalized.isBlank()) {
            throw new UsernameNotFoundException("User not found");
        }

        String sql = """
            SELECT email, password_hash, role
            FROM users
            WHERE lower(email) = ?
        """;

        try (Connection c = cf.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, normalized);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new UsernameNotFoundException("User not found: " + normalized);
                }

                return new UserPrinciple(
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        Role.valueOf(rs.getString("role"))
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("DB error while loading user", e);
        }
    }
}
