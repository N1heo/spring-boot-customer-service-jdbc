package kg.nurtelecom.internlabs.customerservice.repository;

import kg.nurtelecom.internlabs.customerservice.payload.request.customer.AdminCustomerCreateRequest;
import kg.nurtelecom.internlabs.customerservice.payload.request.customer.AdminCustomerUpdateRequest;
import kg.nurtelecom.internlabs.customerservice.payload.response.CustomerDetailResponse;
import kg.nurtelecom.internlabs.customerservice.payload.response.CustomerResponse;
import kg.nurtelecom.internlabs.customerservice.repository.jdbc.JdbcConnectionFactory;
import kg.nurtelecom.internlabs.customerservice.service.AdminCustomerService;
import kg.nurtelecom.internlabs.customerservice.storage.StorageService;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.nio.channels.ScatteringByteChannel;
import java.sql.Connection;
import java.util.List;
import java.util.UUID;

@Repository
public class AdminCustomerRepository implements AdminCustomerService {

    private final JdbcConnectionFactory cf;
    private final AdminCustomerRepositoryJdbc jdbc;
    private final StorageService storage;

    public AdminCustomerRepository(
            JdbcConnectionFactory cf,
            AdminCustomerRepositoryJdbc jdbc,
            StorageService storage
    ) {
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public CustomerDetailResponse findByEmail(String email) {
        try (Connection c = cf.getConnection()) {
            return jdbc.findByEmail(c, email);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CustomerResponse create(MultipartFile photo, AdminCustomerCreateRequest request) {
        String newPath = null;

        try {
            if (photo != null && !photo.isEmpty()) {
                newPath = storage.store(photo);
            }

            String finalPath = newPath;

            try (Connection c = cf.getConnection()) {
                boolean old = c.getAutoCommit();
                c.setAutoCommit(false);

                try {
                    UUID id = UUID.randomUUID();
                    CustomerResponse created = jdbc.insert(c, id, request, finalPath);

                    c.commit();
                    c.setAutoCommit(old);
                    return created;

                } catch (Exception e) {
                    c.rollback();
                    c.setAutoCommit(old);
                    if (newPath != null) storage.delete(newPath);

                    throw new RuntimeException(e);
                }
            }

        } catch (Exception e) {
            if (newPath != null) storage.delete(newPath);
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

            } catch (Exception e) {
                c.rollback();
                c.setAutoCommit(old);
                throw new RuntimeException(e);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(UUID id) {
        String oldPath;

        try (Connection c = cf.getConnection()) {
            boolean oldAutoCommit = c.getAutoCommit();
            c.setAutoCommit(false);

            try {
                CustomerResponse existing = jdbc.findById(c, id);
                if (existing == null) {
                    c.rollback();
                    return;
                }

                oldPath = existing.imagePath();

                jdbc.deleteById(c, id);

                c.commit();
            } catch (Exception e) {
                c.rollback();
                throw new RuntimeException(e);
            } finally {
                c.setAutoCommit(oldAutoCommit);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (oldPath != null && !oldPath.isBlank()) {
            try {
                storage.delete(oldPath);
            } catch (Exception e) {
            }
        }
    }

}
