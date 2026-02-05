package kg.nurtelecom.internlabs.customerservice.service;

import kg.nurtelecom.internlabs.customerservice.payload.request.auth.LoginRequest;
import kg.nurtelecom.internlabs.customerservice.payload.request.auth.RegisterCustomerRequest;
import kg.nurtelecom.internlabs.customerservice.payload.response.AuthResponse;
import kg.nurtelecom.internlabs.customerservice.security.jwt.JwtService;
import kg.nurtelecom.internlabs.customerservice.storage.StorageService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AuthService {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final StorageService storageService;


    public AuthService(JwtService jwtService, AuthenticationManager authenticationManager, StorageService storageService) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.storageService = storageService;
    }


    public AuthResponse verify(LoginRequest loginRequest) {
      Authentication authentication = authenticationManager.authenticate
              (new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

      if (authentication.isAuthenticated()) {
          return new AuthResponse(jwtService.generateToken(loginRequest.getUsername()));
      }

      throw new kg.nurtelecom.internlabs.customerservice.exception.UnauthorizedException("Invalid credentials");
    }

    public void register(RegisterCustomerRequest request, MultipartFile photo) {
        String imagePath = null;
        if (photo != null && !photo.isEmpty()) {
            imagePath = storageService.store(photo);
        }

          }
}
