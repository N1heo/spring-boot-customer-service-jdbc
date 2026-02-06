package kg.nurtelecom.internlabs.customerservice.repository;

import kg.nurtelecom.internlabs.customerservice.payload.request.auth.LoginRequest;
import kg.nurtelecom.internlabs.customerservice.payload.request.auth.RegisterCustomerRequest;
import kg.nurtelecom.internlabs.customerservice.payload.response.AuthResponse;
import kg.nurtelecom.internlabs.customerservice.repository.jdbc.JdbcConnectionFactory;
import kg.nurtelecom.internlabs.customerservice.security.jwt.JwtService;
import kg.nurtelecom.internlabs.customerservice.service.AuthService;
import kg.nurtelecom.internlabs.customerservice.storage.StorageService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AuthRepositoryJdbc implements AuthService {

    private final JdbcConnectionFactory jdbcConnectionFactory;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final StorageService storageService;

    public AuthRepositoryJdbc(JdbcConnectionFactory jdbcConnectionFactory,
                              JwtService jwtService,
                              AuthenticationManager authenticationManager,
                              PasswordEncoder passwordEncoder,
                              StorageService storageService) {
        this.jdbcConnectionFactory = jdbcConnectionFactory;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.storageService = storageService;
    }


    @Override
    public Optional<String> findPasswordHashByEmail(String email) {
        String sql = "SELECT password_hash FROM users WHERE email = ?";
        try (Connection connection = jdbcConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(resultSet.getString("password_hash"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error during password lookup", e);
        }
        return Optional.empty();
    }

    @Override
    public void register(RegisterCustomerRequest request, MultipartFile photo) {

        String imagePath = null;
        if (photo != null && !photo.isEmpty()) {
            imagePath = storageService.store(photo);
        }

        UUID customerId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        String customerSql = """
                    INSERT INTO customers (id, first_name, last_name, phone, image_path)
                    VALUES (?, ?, ?, ?, ?)
                """;

        String userSql = """
                    INSERT INTO users (id, email, password_hash, role, customer_id)
                    VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = jdbcConnectionFactory.getConnection()) {

            connection.setAutoCommit(false);

            try {
                try (PreparedStatement custStmt = connection.prepareStatement(customerSql)) {
                    custStmt.setObject(1, customerId);
                    custStmt.setString(2, request.getFirstname());
                    custStmt.setString(3, request.getLastname());
                    custStmt.setString(4, request.getPhone());
                    custStmt.setString(5, imagePath);
                    custStmt.executeUpdate();
                }

                try (PreparedStatement userStmt = connection.prepareStatement(userSql)) {
                    userStmt.setObject(1, userId);
                    userStmt.setString(2, request.getEmail());
                    userStmt.setString(3, encodedPassword);
                    userStmt.setString(4, "USER");
                    userStmt.setObject(5, customerId);
                    userStmt.executeUpdate();
                }

                connection.commit();

            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Transaction failed: " + e.getMessage(), e);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Connection error", e);
        }
    }
}