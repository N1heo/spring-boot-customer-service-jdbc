package kg.nurtelecom.internlabs.customerservice.controller;

import kg.nurtelecom.internlabs.customerservice.payload.request.auth.LoginRequest;
import kg.nurtelecom.internlabs.customerservice.payload.request.auth.RegisterCustomerRequest;
import kg.nurtelecom.internlabs.customerservice.payload.response.AuthResponse;
import kg.nurtelecom.internlabs.customerservice.security.UserPrinciple;
import kg.nurtelecom.internlabs.customerservice.security.jwt.JwtService;
import kg.nurtelecom.internlabs.customerservice.service.AuthService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class AuthControllerAPI {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthControllerAPI(AuthService authService,
                             AuthenticationManager authenticationManager,
                             JwtService jwtService) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> register(
            @RequestPart("data") RegisterCustomerRequest request,
            @RequestPart(value = "photo", required = false) MultipartFile photo
    ) {
        authService.register(request, photo);
        return ResponseEntity.ok("Customer registered successfully");
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest req) {
        return authService.login(req);
    }
}
