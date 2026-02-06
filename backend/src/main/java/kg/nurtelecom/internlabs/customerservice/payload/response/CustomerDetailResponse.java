package kg.nurtelecom.internlabs.customerservice.payload.response;

public record CustomerDetailResponse(
        String email,
        String password,
        String role
) {

}
