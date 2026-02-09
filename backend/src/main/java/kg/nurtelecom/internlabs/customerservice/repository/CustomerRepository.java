package kg.nurtelecom.internlabs.customerservice.repository;

import kg.nurtelecom.internlabs.customerservice.payload.request.customer.AdminCustomerUpdateRequest;
import kg.nurtelecom.internlabs.customerservice.payload.response.CustomerResponse;
import kg.nurtelecom.internlabs.customerservice.repository.jdbc.JdbcConnectionFactory;
import kg.nurtelecom.internlabs.customerservice.service.CustomerProfileService;
import kg.nurtelecom.internlabs.customerservice.storage.StorageService;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Connection;
import java.util.UUID;

@Deprecated
@Repository
public class CustomerRepository implements CustomerProfileService  {

    private final JdbcConnectionFactory cf;
    private final CustomerProfileRepositoryJdbc profileJdbc;
    private final StorageService storage;

    public CustomerRepository(
            JdbcConnectionFactory cf,
            CustomerProfileRepositoryJdbc profileJdbc,
            StorageService storage
    ) {
        this.cf = cf;
        this.profileJdbc = profileJdbc;
        this.storage = storage;
    }

    @Override
    public CustomerResponse getCurrentCustomer() {
        UUID id = getCurrentCustomerId();
        if (id == null) return null;

        return profileJdbc.findById(id);
    }

    @Override
    public CustomerResponse updateCurrentProfile(AdminCustomerUpdateRequest request) {
        UUID id = getCurrentCustomerId();
        if (id == null) return null;

        try (Connection c = cf.getConnection()) {
            boolean old = c.getAutoCommit();
            c.setAutoCommit(false);
            try {
                CustomerResponse existing = profileJdbc.findById(c, id);
                if (existing == null) {
                    c.rollback();
                    c.setAutoCommit(old);
                    return null;
                }

                profileJdbc.updateProfile(c, id, request);

                CustomerResponse updated = profileJdbc.findById(c, id);

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
    public CustomerResponse updateProfilePhoto(MultipartFile photo) {
        UUID id = getCurrentCustomerId();
        if (id == null) return null;

        String newPath = null;

        try {
            newPath = storage.store(photo);
            String oldPath;
            CustomerResponse updated;
            try (Connection c = cf.getConnection()) {
                boolean old = c.getAutoCommit();
                c.setAutoCommit(false);

                try {
                    CustomerResponse existing = profileJdbc.findById(c, id);
                    if (existing == null) {
                        c.rollback();
                        c.setAutoCommit(old);

                        storage.delete(newPath);
                        return null;
                    }

                    oldPath = existing.imagePath();

                    profileJdbc.updateImagePath(c, id, newPath);
                    updated = profileJdbc.findById(c, id);

                    c.commit();
                    c.setAutoCommit(old);

                } catch (Exception e) {
                    c.rollback();
                    c.setAutoCommit(old);
                    storage.delete(newPath);
                    throw new RuntimeException(e);
                }
            }
            storage.delete(oldPath);

            return updated;

        } catch (Exception e) {
            if (newPath != null) storage.delete(newPath);
            throw new RuntimeException(e);
        }
    }

    @Override
    public CustomerResponse deleteProfilePhoto() {
        UUID id = getCurrentCustomerId();
        if (id == null) return null;

        String oldPath;

        try (Connection c = cf.getConnection()) {
            boolean old = c.getAutoCommit();
            c.setAutoCommit(false);

            try {
                CustomerResponse existing = profileJdbc.findById(c, id);
                if (existing == null) {
                    c.rollback();
                    c.setAutoCommit(old);
                    return null;
                }

                oldPath = existing.imagePath();

                profileJdbc.updateImagePath(c, id, null);

                CustomerResponse updated = profileJdbc.findById(c, id);

                c.commit();
                c.setAutoCommit(old);
                storage.delete(oldPath);

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

    private UUID getCurrentCustomerId() {
        var auth = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication();

        if (auth == null || auth.getName() == null) return null;
        return UUID.fromString(auth.getName());
    }
}
