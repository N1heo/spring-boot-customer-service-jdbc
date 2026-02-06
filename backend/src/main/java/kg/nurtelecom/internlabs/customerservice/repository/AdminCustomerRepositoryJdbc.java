package kg.nurtelecom.internlabs.customerservice.repository;

import kg.nurtelecom.internlabs.customerservice.payload.request.customer.AdminCustomerCreateRequest;
import kg.nurtelecom.internlabs.customerservice.payload.request.customer.AdminCustomerUpdateRequest;
import kg.nurtelecom.internlabs.customerservice.payload.response.CustomerDetailResponse;
import kg.nurtelecom.internlabs.customerservice.payload.response.CustomerResponse;
import kg.nurtelecom.internlabs.customerservice.repository.jdbc.JdbcConnectionFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;

@Repository
public class AdminCustomerRepositoryJdbc {

    private final JdbcConnectionFactory cf;
    private final PasswordEncoder passwordEncoder;

    public AdminCustomerRepositoryJdbc(JdbcConnectionFactory cf, PasswordEncoder passwordEncoder) {
        this.cf = cf;
        this.passwordEncoder = passwordEncoder;
    }

    public List<CustomerResponse> findAll() {
        try (Connection c = cf.getConnection()) {
            return findAll(c);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<CustomerResponse> findAll(Connection c) throws SQLException {

        String sql = """
            SELECT
                c.id,
                c.first_name,
                c.last_name,
                u.email,
                c.phone,
                c.image_path,
                u.role
            FROM customers c
            LEFT JOIN users u ON u.customer_id = c.id
            ORDER BY c.first_name
        """;

        try (PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<CustomerResponse> list = new ArrayList<>();
            while (rs.next()) {
                list.add(map(rs));
            }
            return list;
        }
    }

    public CustomerResponse insert(Connection c, UUID customerId, AdminCustomerCreateRequest req, String imagePath) throws SQLException {

        String customerSql = """
            INSERT INTO customers(id, first_name, last_name, phone, image_path)
            VALUES(?,?,?,?,?)
        """;

        try (PreparedStatement ps = c.prepareStatement(customerSql)) {
            ps.setObject(1, customerId);
            ps.setString(2, req.getFirstName());
            ps.setString(3, req.getLastName());
            ps.setString(4, req.getPhone());
            ps.setString(5, imagePath);
            ps.executeUpdate();
        }

        UUID userId = UUID.randomUUID();
        String passwordHash = passwordEncoder.encode(req.getPassword());

        String userSql = """
            INSERT INTO users(id, email, password_hash, role, customer_id)
            VALUES(?,?,?,?,?)
        """;

        try (PreparedStatement ps = c.prepareStatement(userSql)) {
            ps.setObject(1, userId);
            ps.setString(2, req.getEmail());
            ps.setString(3, passwordHash);

            String role = (req.getRole() == null) ? "USER" : req.getRole().name();
            ps.setString(4, role);

            ps.setObject(5, customerId);
            ps.executeUpdate();
        }

        return findById(c, customerId);
    }

    public CustomerResponse update(Connection c, UUID customerId, AdminCustomerUpdateRequest req) throws SQLException {

        if (req.getFirstName() != null || req.getLastName() != null || req.getPhone() != null) {
            String customerSql = """
                UPDATE customers
                SET
                    first_name = COALESCE(?, first_name),
                    last_name  = COALESCE(?, last_name),
                    phone      = COALESCE(?, phone)
                WHERE id = ?
            """;

            try (PreparedStatement ps = c.prepareStatement(customerSql)) {
                ps.setString(1, req.getFirstName());
                ps.setString(2, req.getLastName());
                ps.setString(3, req.getPhone());
                ps.setObject(4, customerId);
                ps.executeUpdate();
            }
        }

        if (req.getEmail() != null) {
            String userSql = """
                UPDATE users
                SET email = ?
                WHERE customer_id = ?
            """;

            try (PreparedStatement ps = c.prepareStatement(userSql)) {
                ps.setString(1, req.getEmail());
                ps.setObject(2, customerId);
                ps.executeUpdate();
            }
        }

        return findById(c, customerId);
    }

    public void deleteById(Connection c, UUID customerId) throws SQLException {

        String deleteUsersSql = "DELETE FROM users WHERE customer_id = ?";
        try (PreparedStatement ps = c.prepareStatement(deleteUsersSql)) {
            ps.setObject(1, customerId);
            ps.executeUpdate();
        }

        String deleteCustomerSql = "DELETE FROM customers WHERE id = ?";
        try (PreparedStatement ps = c.prepareStatement(deleteCustomerSql)) {
            ps.setObject(1, customerId);
            ps.executeUpdate();
        }
    }

    public CustomerDetailResponse findByEmail(Connection c, String email) throws SQLException {
        String sql = "SELECT  email, password_hash, role  FROM users WHERE email = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()){ return null;}
                CustomerDetailResponse response =
                        new CustomerDetailResponse(rs.getString("email"),
                                                   rs.getString("password_hash"),
                                                   rs.getString("role"));
                return response;
            }
        }
    }


//    public CustomerResponse findById(Connection c, UUID id) throws SQLException {
//        String sql = "SELECT id, first_name, last_name, email, phone, image_path  FROM customers WHERE id = ?";
    public CustomerResponse findById(Connection c, UUID customerId) throws SQLException {

        String sql = """
            SELECT
                c.id,
                c.first_name,
                c.last_name,
                u.email,
                c.phone,
                c.image_path,
                u.role
            FROM customers c
            LEFT JOIN users u ON u.customer_id = c.id
            WHERE c.id = ?
        """;

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, customerId);

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
