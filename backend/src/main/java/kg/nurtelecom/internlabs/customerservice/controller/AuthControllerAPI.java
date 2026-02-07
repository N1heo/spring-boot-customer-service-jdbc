package kg.nurtelecom.internlabs.customerservice.controller;

import jakarta.validation.Valid;
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

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponse> registerJson(@RequestBody @Valid RegisterCustomerRequest request) {
        return ResponseEntity.ok(authService.register(request, null));
    }

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AuthResponse> register(
            @RequestPart("data") @Valid RegisterCustomerRequest request,
            @RequestPart(value = "photo", required = false) MultipartFile photo
    ) {
        AuthResponse resp = authService.register(request, photo);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest req) {
        AuthResponse resp = authService.login(req);
        return ResponseEntity.ok(resp);
    }
}
