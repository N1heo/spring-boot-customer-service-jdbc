package kg.nurtelecom.internlabs.customerservice.repository;

import kg.nurtelecom.internlabs.customerservice.payload.request.customer.AdminCustomerCreateRequest;
import kg.nurtelecom.internlabs.customerservice.payload.request.customer.AdminCustomerUpdateRequest;
import kg.nurtelecom.internlabs.customerservice.payload.response.CustomerResponse;
import kg.nurtelecom.internlabs.customerservice.repository.jdbc.JdbcConnectionFactory;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;

@Repository
public class AdminCustomerRepositoryJdbc {

    private final JdbcConnectionFactory cf;

    public AdminCustomerRepositoryJdbc(JdbcConnectionFactory cf) {
        this.cf = cf;
    }

    public List<CustomerResponse> findAll() {
        try (Connection c = cf.getConnection()) {
            return findAll(c);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<CustomerResponse> findAll(Connection c) throws SQLException {
        String sql = "SELECT id, first_name, last_name, email, phone, image_path, role FROM customers ORDER BY first_name";
        try (PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<CustomerResponse> list = new ArrayList<>();
            while (rs.next()) list.add(map(rs));
            return list;
        }
    }

    public CustomerResponse insert(Connection c, UUID id, AdminCustomerCreateRequest req, String imagePath) throws SQLException {
        String sql = """
            INSERT INTO customers(id, first_name, last_name, email, image_path)
            VALUES(?,?,?,?,?)
        """;
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, id);
            ps.setString(2, req.getFirstName());
            ps.setString(3, req.getLastName());
            ps.setString(4, req.getEmail());
            ps.setString(5, imagePath);
            ps.executeUpdate();
        }
        return findById(c, id);
    }

    public CustomerResponse update(Connection c, UUID id, AdminCustomerUpdateRequest req) throws SQLException {
        String sql = """
            UPDATE customers
            SET first_name = ?, last_name = ?, email = ?
            WHERE id = ?
        """;
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, req.getFirstName());
            ps.setString(2, req.getLastName());
            ps.setString(3, req.getEmail());
            ps.setObject(4, id);
            ps.executeUpdate();
        }
        return findById(c, id);
    }

    public void deleteById(Connection c, UUID id) throws SQLException {
        String sql = "DELETE FROM customers WHERE id = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, id);
            ps.executeUpdate();
        }
    }

    public CustomerResponse findById(Connection c, UUID id) throws SQLException {
        String sql = "SELECT id, first_name, last_name, email, phone, image_path  FROM customers WHERE id = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return map(rs);
            }
        }
    }

    private CustomerResponse map(ResultSet rs) throws SQLException {
        return new CustomerResponse(
                (UUID) rs.getObject("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("image_path"),
                rs.getString("role")
        );
    }

}
