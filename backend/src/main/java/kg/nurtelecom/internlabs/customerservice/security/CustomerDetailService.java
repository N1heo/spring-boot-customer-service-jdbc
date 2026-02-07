package kg.nurtelecom.internlabs.customerservice.security;

import kg.nurtelecom.internlabs.customerservice.enums.Role;
import kg.nurtelecom.internlabs.customerservice.repository.jdbc.JdbcConnectionFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.UUID;

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
            SELECT email, password_hash, role, customer_id
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

                UUID customerId = (UUID) rs.getObject("customer_id");
                String roleRaw = rs.getString("role");
                Role role;
                try {
                    role = Role.valueOf(roleRaw.trim().toUpperCase());
                } catch (Exception ex) {
                    throw new UsernameNotFoundException("Invalid role: " + roleRaw);
                }
                return new UserPrinciple(
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        role,
                        customerId
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("DB error while loading user", e);
        }
    }
}
