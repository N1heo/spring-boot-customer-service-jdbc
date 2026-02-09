package kg.nurtelecom.internlabs.customerservice.repository;

import kg.nurtelecom.internlabs.customerservice.exception.ConflictException;
import kg.nurtelecom.internlabs.customerservice.payload.request.customer.AdminCustomerCreateRequest;
import kg.nurtelecom.internlabs.customerservice.payload.request.customer.AdminCustomerUpdateRequest;
import kg.nurtelecom.internlabs.customerservice.payload.response.CustomerDetailResponse;
import kg.nurtelecom.internlabs.customerservice.payload.response.CustomerResponse;
import kg.nurtelecom.internlabs.customerservice.repository.jdbc.JdbcConnectionFactory;
import kg.nurtelecom.internlabs.customerservice.service.AdminCustomerService;
import kg.nurtelecom.internlabs.customerservice.storage.StorageService;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Repository
public class AdminCustomerRepository implements AdminCustomerService {

    private final JdbcConnectionFactory cf;
    private final AdminCustomerRepositoryJdbc jdbc;
    private final StorageService storage;

    public AdminCustomerRepository(JdbcConnectionFactory cf, AdminCustomerRepositoryJdbc jdbc, StorageService storage) {
        this.cf = cf;
        this.jdbc = jdbc;
        this.storage = storage;
    }

    @Override
    public List<CustomerResponse> findAll() {
        return jdbc.findAll();
    }

    @Override
    public CustomerResponse findById(UUID id) {
        try (Connection c = cf.getConnection()) {
            return jdbc.findById(c, id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public CustomerDetailResponse findByEmail(String email) {
        try (Connection c = cf.getConnection()) {
            return jdbc.findByEmail(c, email);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CustomerResponse create(MultipartFile photo, AdminCustomerCreateRequest request) {
        String path = null;

        if (photo != null && !photo.isEmpty()) {
            path = storage.store(photo);
        }

        try (Connection c = cf.getConnection()) {
            boolean old = c.getAutoCommit();
            c.setAutoCommit(false);

            try {
                UUID id = UUID.randomUUID();
                CustomerResponse created = jdbc.insert(c, id, request, path);
                c.commit();
                c.setAutoCommit(old);
                return created;

            } catch (SQLException e) {
                c.rollback();
                c.setAutoCommit(old);
                if (path != null) storage.delete(path);
                if ("23505".equals(e.getSQLState())) {
                    throw new ConflictException(conflictMessage(e));
                }
                throw new RuntimeException(e);
            } catch (Exception e) {
                c.rollback();
                c.setAutoCommit(old);
                if (path != null) storage.delete(path);
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            if (path != null) storage.delete(path);
            throw new RuntimeException(e);
        }
    }

    @Override
    public CustomerResponse update(UUID id, AdminCustomerUpdateRequest request) {
        try (Connection c = cf.getConnection()) {
            boolean old = c.getAutoCommit();
            c.setAutoCommit(false);

            try {
                CustomerResponse existing = jdbc.findById(c, id);
                if (existing == null) {
                    c.rollback();
                    c.setAutoCommit(old);
                    return null;
                }

                CustomerResponse updated = jdbc.update(c, id, request);
                c.commit();
                c.setAutoCommit(old);
                return updated;

            } catch (SQLException e) {
                c.rollback();
                c.setAutoCommit(old);
                if ("23505".equals(e.getSQLState())) {
                    throw new ConflictException(conflictMessage(e));
                }
                throw new RuntimeException(e);
            } catch (Exception e) {
                c.rollback();
                c.setAutoCommit(old);
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(UUID id) {
        String oldPath = null;

        try (Connection c = cf.getConnection()) {
            boolean old = c.getAutoCommit();
            c.setAutoCommit(false);

            try {
                CustomerResponse existing = jdbc.findById(c, id);
                if (existing == null) {
                    c.rollback();
                    c.setAutoCommit(old);
                    return;
                }

                oldPath = existing.imagePath();
                jdbc.deleteById(c, id);

                c.commit();
                c.setAutoCommit(old);

            } catch (Exception e) {
                c.rollback();
                c.setAutoCommit(old);
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (oldPath != null && !oldPath.isBlank()) {
            storage.delete(oldPath);
        }
    }

    private String conflictMessage(SQLException e) {
        String m = e.getMessage();
        if (m == null) return "Conflict";
        String s = m.toLowerCase();
        if (s.contains("users_email") || s.contains("email")) return "Email already exists";
        if (s.contains("customers_phone") || s.contains("phone")) return "Phone already exists";
        return "Conflict";
    }
}
