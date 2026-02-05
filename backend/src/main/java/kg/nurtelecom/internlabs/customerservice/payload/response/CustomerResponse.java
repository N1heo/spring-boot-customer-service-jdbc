package kg.nurtelecom.internlabs.customerservice.payload.response;

import java.util.UUID;

public record CustomerResponse(
        UUID id,
        String name,
        String email,
        String phone,
        String imagePath,
        String role) {
}
