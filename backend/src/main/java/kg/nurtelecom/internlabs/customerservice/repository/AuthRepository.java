package kg.nurtelecom.internlabs.customerservice.repository;

import kg.nurtelecom.internlabs.customerservice.enums.Role;
import kg.nurtelecom.internlabs.customerservice.exception.ConflictException;
import kg.nurtelecom.internlabs.customerservice.payload.request.auth.LoginRequest;
import kg.nurtelecom.internlabs.customerservice.payload.request.auth.RegisterCustomerRequest;
import kg.nurtelecom.internlabs.customerservice.payload.response.AuthResponse;
import kg.nurtelecom.internlabs.customerservice.repository.jdbc.JdbcConnectionFactory;
import kg.nurtelecom.internlabs.customerservice.security.UserPrinciple;
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
import java.sql.SQLException;
import java.util.UUID;

@Repository
public class AuthRepository implements AuthService {

  private final JdbcConnectionFactory connectionFactory;
  private final AuthRepositoryJdbc authJdbc;
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;
  private final StorageService storage;
  private final AuthenticationManager authenticationManager;

  public AuthRepository(
      JdbcConnectionFactory connectionFactory,
      AuthRepositoryJdbc authJdbc,
      JwtService jwtService,
      PasswordEncoder passwordEncoder,
      StorageService storage, AuthenticationManager authenticationManager
  ) {
    this.connectionFactory = connectionFactory;
    this.authJdbc = authJdbc;
    this.jwtService = jwtService;
    this.passwordEncoder = passwordEncoder;
    this.storage = storage;
    this.authenticationManager = authenticationManager;
  }

  @Override
  public AuthResponse register(RegisterCustomerRequest request,
                               MultipartFile photo) {
    String imagePath = null;
    if (photo != null && !photo.isEmpty()) {
      imagePath = storage.store(photo);
    }

    UUID customerId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    String encodedPassword = passwordEncoder.encode(request.getPassword());
    Role role = Role.USER;

    try (Connection connection = connectionFactory.getConnection()) {
      boolean old = connection.getAutoCommit();
      connection.setAutoCommit(false);

      try {
        authJdbc.insertCustomer(
            connection, customerId, request, imagePath);
        authJdbc.insertUser(
            connection, userId, request, encodedPassword, role, customerId);

        connection.commit();
        connection.setAutoCommit(old);

      } catch (SQLException e) {
        connection.rollback();
        connection.setAutoCommit(old);

        if (imagePath != null) {
          storage.delete(imagePath);
        }

        if ("23505".equals(e.getSQLState())) {
          throw new ConflictException("Phone or email already exists");
        }
        throw new RuntimeException("Transaction failed: "
            + e.getMessage(), e);
      }
    } catch (SQLException e) {
      if (imagePath != null) {
        storage.delete(imagePath);
      }
      throw new RuntimeException("Connection error", e);
    }

    String token = jwtService.generateToken(
        authJdbc.normalizeEmail(request.getEmail()), role);
    return new AuthResponse(token);
  }

  @Override
  public AuthResponse login(LoginRequest req) {
    Authentication auth = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            authJdbc.normalizeEmail(req.getEmail()),
            req.getPassword()
        )
    );

    UserPrinciple user = (UserPrinciple) auth.getPrincipal();
    String token = jwtService.generateToken(
        user.getUsername(), user.getRole());
    return new AuthResponse(token);
  }
}
