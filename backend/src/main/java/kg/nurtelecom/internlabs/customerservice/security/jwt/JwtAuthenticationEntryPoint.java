//package kg.nurtelecom.internlabs.customerservice.security;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import kg.nurtelecom.internlabs.customerservice.payload.response.ErrorResponse;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
//
//    private final ObjectMapper mapper = new ObjectMapper();
//
//    @Override
//    public void commence(HttpServletRequest request,
//                         HttpServletResponse response,
//                         AuthenticationException authException) throws IOException {
//
//        response.setContentType("application/json");
//        response.setStatus(HttpStatus.UNAUTHORIZED.value());
//
//        ErrorResponse error = new ErrorResponse(
//                HttpStatus.UNAUTHORIZED.value(),
//                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
//                "Unauthorized",
//                request.getRequestURI()
//        );
//
//        mapper.writeValue(response.getOutputStream(), error);
//    }
//}
