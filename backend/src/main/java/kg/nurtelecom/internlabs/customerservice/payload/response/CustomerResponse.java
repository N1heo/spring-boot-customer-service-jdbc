package kg.nurtelecom.internlabs.customerservice.payload.response;

import java.util.UUID;

public record CustomerResponse(
        UUID idCustomer,
        String firstName,
        String lastName,
        String email,
        String phone,
        String imagePath,
        String role) {
}
