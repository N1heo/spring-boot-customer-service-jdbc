package kg.nurtelecom.internlabs.customerservice.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kg.nurtelecom.internlabs.customerservice.payload.request.auth.LoginRequest;
import kg.nurtelecom.internlabs.customerservice.payload.response.AuthResponse;
import kg.nurtelecom.internlabs.customerservice.security.UserPrinciple;
import kg.nurtelecom.internlabs.customerservice.security.jwt.JwtService;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Deprecated
@Component
public class CustomAuthenticationFilter
    extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;
    private final JwtService jwtService;

    public CustomAuthenticationFilter(
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        super(authenticationManager);
        this.objectMapper = new ObjectMapper();
        this.jwtService = jwtService;
        setFilterProcessesUrl("/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
            HttpServletResponse response
    ) throws AuthenticationException {
        try {
            LoginRequest loginRequest = objectMapper.readValue(
                    request.getInputStream(),
                    LoginRequest.class
            );

            String normalizedEmail = loginRequest.getEmail() == null
                    ? null
                    : loginRequest.getEmail().trim().toLowerCase();

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            normalizedEmail,
                            loginRequest.getPassword()
                    );

            return getAuthenticationManager().authenticate(authToken);

        } catch (IOException e) {
            throw new RuntimeException("Failed to parse authentication request", e);
        }
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult
    ) throws IOException, ServletException {
        UserPrinciple userPrinciple = (UserPrinciple) authResult.getPrincipal();

        String token = jwtService.generateToken(
                userPrinciple.getUsername(),
                userPrinciple.getRole()
        );

        AuthResponse authResponse = new AuthResponse(token);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        objectMapper.writeValue(response.getWriter(), authResponse);
    }

    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed
    ) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        String errorJson = String.format(
                "{\"error\": \"Authentication failed\", \"message\": \"%s\"}",
                failed.getMessage()
        );

        response.getWriter().write(errorJson);
    }
}

