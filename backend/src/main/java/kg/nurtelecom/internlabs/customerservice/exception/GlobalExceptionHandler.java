package kg.nurtelecom.internlabs.customerservice.exception;

public class GlobalExceptionHandler extends RuntimeException {

    public GlobalExceptionHandler(String message) {
        super(message);
    }
}
