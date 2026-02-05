package kg.nurtelecom.internlabs.customerservice.controller;

import kg.nurtelecom.internlabs.customerservice.payload.request.auth.LoginRequest;
import kg.nurtelecom.internlabs.customerservice.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthControllerAPI {
    private final AuthService authService;

    public AuthControllerAPI(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.verify(loginRequest);
        return ResponseEntity.ok(token);
    }
}
