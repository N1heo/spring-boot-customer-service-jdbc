package kg.nurtelecom.internlabs.customerservice.service;

import kg.nurtelecom.internlabs.customerservice.payload.request.auth.LoginRequest;
import kg.nurtelecom.internlabs.customerservice.payload.request.auth.RegisterCustomerRequest;

import java.util.Optional;

public interface AuthService {
  String verify(LoginRequest loginRequest);
  Optional<String> findPasswordHashByEmail(String email);
  String register(RegisterCustomerRequest request);
}