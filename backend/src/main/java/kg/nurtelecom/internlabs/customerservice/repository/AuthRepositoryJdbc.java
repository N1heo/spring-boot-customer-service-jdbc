package kg.nurtelecom.internlabs.customerservice.repository;

import kg.nurtelecom.internlabs.customerservice.enums.Role;
import kg.nurtelecom.internlabs.customerservice.payload.request.auth.RegisterCustomerRequest;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

@Repository
public class AuthRepositoryJdbc {

    public void insertCustomer(
        Connection c,
        UUID customerId,
        RegisterCustomerRequest request,
        String imagePath
    ) throws SQLException {
        String sql = """
            INSERT INTO customers (id, first_name, last_name, phone, image_path)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, customerId);
            ps.setString(2, request.getFirstname());
            ps.setString(3, request.getLastname());
            ps.setString(4, request.getPhone());
            ps.setString(5, imagePath);
            ps.executeUpdate();
        }
    }

    public void insertUser(
        Connection c,
        UUID userId,
        RegisterCustomerRequest request,
        String encodedPassword,
        Role role,
        UUID customerId
    ) throws SQLException {
        String sql = """
            INSERT INTO users (id, email, password_hash, role, customer_id)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, userId);
            ps.setString(2, normalizeEmail(request.getEmail()));
            ps.setString(3, encodedPassword);
            ps.setString(4, role.name());
            ps.setObject(5, customerId);
            ps.executeUpdate();
        }
    }

    public String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }
}