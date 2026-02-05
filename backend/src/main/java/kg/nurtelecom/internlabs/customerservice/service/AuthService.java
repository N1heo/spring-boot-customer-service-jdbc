package kg.nurtelecom.internlabs.customerservice.service;

import kg.nurtelecom.internlabs.customerservice.payload.request.auth.LoginRequest;
import kg.nurtelecom.internlabs.customerservice.payload.request.auth.RegisterCustomerRequest;
import kg.nurtelecom.internlabs.customerservice.payload.response.AuthResponse;
import org.springframework.web.multipart.MultipartFile;
import java.util.Optional;

public interface AuthService {
    AuthResponse verify(LoginRequest loginRequest);
    void register(RegisterCustomerRequest request, MultipartFile photo);
    Optional<String> findPasswordHashByEmail(String email);
}
