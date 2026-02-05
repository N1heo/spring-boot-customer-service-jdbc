//package kg.nurtelecom.internlabs.customerservice.exception;
//
//import kg.nurtelecom.internlabs.customerservice.payload.response.ErrorResponse;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import javax.servlet.http.HttpServletRequest;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler({BadRequestException.class})
//    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex, HttpServletRequest request) {
//        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
//        String message = "Validation failed";
//        if (ex.getBindingResult() != null && ex.getBindingResult().getFieldError() != null) {
//            message = ex.getBindingResult().getFieldError().getField() + ": " +
//                    ex.getBindingResult().getFieldError().getDefaultMessage();
//        }
//        return buildError(HttpStatus.BAD_REQUEST, message, request);
//    }
//
//    @ExceptionHandler(NotFoundException.class)
//    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex, HttpServletRequest request) {
//        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request);
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleAll(Exception ex, HttpServletRequest request) {
//        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", request);
//    }
//
//    private ResponseEntity<ErrorResponse> buildError(HttpStatus status, String message, HttpServletRequest request) {
//        ErrorResponse body = new ErrorResponse(
//                status.value(),
//                status.getReasonPhrase(),
//                message,
//                request.getRequestURI()
//        );
//        return ResponseEntity.status(status).body(body);
//    }
//}
//
