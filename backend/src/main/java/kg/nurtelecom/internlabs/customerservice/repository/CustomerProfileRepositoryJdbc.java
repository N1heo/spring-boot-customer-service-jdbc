package kg.nurtelecom.internlabs.customerservice.repository;

import kg.nurtelecom.internlabs.customerservice.payload.request.customer.AdminCustomerUpdateRequest;
import kg.nurtelecom.internlabs.customerservice.payload.response.CustomerResponse;
import kg.nurtelecom.internlabs.customerservice.repository.jdbc.JdbcConnectionFactory;
import org.springframework.stereotype.Repository;
import kg.nurtelecom.internlabs.customerservice.repository.jdbc.JdbcTxRunner;
import kg.nurtelecom.internlabs.customerservice.storage.StorageService;
import org.springframework.web.multipart.MultipartFile;
import java.sql.*;
import java.util.UUID;

@Repository
public class CustomerProfileRepositoryJdbc {

    private final JdbcTxRunner tx;
    private final StorageService storage;
    private final JdbcConnectionFactory cf;

    public CustomerProfileRepositoryJdbc(JdbcTxRunner tx, StorageService storage , JdbcConnectionFactory cf) {
        this.tx = tx;
        this.storage = storage;
        this.cf = cf;
    }

    public CustomerResponse updateProfilePhoto(UUID customerId, MultipartFile photo) {
        String newPath = null;

        try {
            newPath = storage.store(photo);

            String finalNewPath = newPath;

            class Result {
                CustomerResponse updated;
                String oldPath;
            }

            Result r = tx.inTx(c -> {
                CustomerResponse existing = findById(c, customerId);
                if (existing == null) return null;

                Result res = new Result();
                res.oldPath = existing.imagePath();

                updateImagePath(c, customerId, finalNewPath);

                res.updated = findById(c, customerId);
                return res;
            });

            if (r == null || r.updated == null) {
                storage.delete(newPath);
                return null;
            }

            storage.delete(r.oldPath);

            return r.updated;

        } catch (Exception e) {
            if (newPath != null) storage.delete(newPath);
            throw new RuntimeException(e);
        }
    }

    public CustomerResponse findById(UUID id) {
        try (Connection c = cf.getConnection()) {
            return findById(c, id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateProfile(Connection c, UUID id, AdminCustomerUpdateRequest req) throws SQLException {
        String sql = """
        UPDATE customers
        SET first_name = COALESCE(?, first_name),
            last_name  = COALESCE(?, last_name),
            email      = COALESCE(?, email),
            phone      = COALESCE(?, phone)
        WHERE id = ?
    """;

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, req.getFirstName());
            ps.setString(2, req.getLastName());
            ps.setString(3, req.getEmail());
            ps.setString(4, req.getPhone());
            ps.setObject(5, id);
            ps.executeUpdate();
        }
    }


    public CustomerResponse findById(Connection c, UUID id) throws SQLException {
        String sql = "SELECT id, first_name, last_name, email, phone, image_path, role FROM customers WHERE id = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return map(rs);
            }
        }
    }

    public void updateImagePath(Connection c, UUID id, String imagePath) throws SQLException {
        String sql = "UPDATE customers SET image_path = ? WHERE id = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, imagePath);
            ps.setObject(2, id);
            ps.executeUpdate();
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
