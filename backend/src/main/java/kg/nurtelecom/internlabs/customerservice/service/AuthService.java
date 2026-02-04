package kg.nurtelecom.internlabs.customerservice.service;

import kg.nurtelecom.internlabs.customerservice.payload.request.auth.LoginRequest;
import kg.nurtelecom.internlabs.customerservice.security.jwt.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthService(JwtService jwtService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }


    public String verify(LoginRequest loginRequest) {
      Authentication authentication = authenticationManager.authenticate
              (new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

      if (authentication.isAuthenticated()) {
          return jwtService.generateToken(loginRequest.getUsername());
      }

      return "fail";
    }
}
