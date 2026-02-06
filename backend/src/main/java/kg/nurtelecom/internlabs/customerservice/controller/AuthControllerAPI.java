package kg.nurtelecom.internlabs.customerservice.controller;

import kg.nurtelecom.internlabs.customerservice.payload.request.auth.LoginRequest;
import kg.nurtelecom.internlabs.customerservice.payload.request.auth.RegisterCustomerRequest;
import kg.nurtelecom.internlabs.customerservice.payload.response.AuthResponse;
import kg.nurtelecom.internlabs.customerservice.service.AuthService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class AuthControllerAPI {
    private final AuthService authService;

    public AuthControllerAPI(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> register(
            @RequestPart("data") RegisterCustomerRequest request,
            @RequestPart(value = "photo", required = false) MultipartFile photo) {
        authService.register(request, photo);
        return ResponseEntity.ok("Customer registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {

        System.out.println("HIT /login controller");

        return ResponseEntity.ok(authService.verify(loginRequest));
    }
}
