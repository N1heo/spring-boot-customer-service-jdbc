package kg.nurtelecom.internlabs.customerservice.controller;

import jakarta.validation.Valid;
import kg.nurtelecom.internlabs.customerservice.payload.request.customer.AdminCustomerUpdateRequest;
import kg.nurtelecom.internlabs.customerservice.payload.response.CustomerResponse;
import kg.nurtelecom.internlabs.customerservice.service.CustomerProfileService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/customer/profile")
public class CustomerProfileControllerAPI {

    private final CustomerProfileService service;

    public CustomerProfileControllerAPI(@Qualifier("customerProfileRepositoryJdbc") CustomerProfileService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<CustomerResponse> getMyProfile() {
        CustomerResponse customer = service.getCurrentCustomer();
        return customer != null ? ResponseEntity.ok(customer) : ResponseEntity.notFound().build();
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerResponse> updateMyProfile(
            @Valid @RequestBody AdminCustomerUpdateRequest request) {

        CustomerResponse updated = service.updateCurrentProfile(request);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @PutMapping(value = "/uploads", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CustomerResponse> updateMyPhoto(@RequestPart("photo") MultipartFile photo) {
        if (photo == null || photo.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        CustomerResponse updated = service.updateProfilePhoto(photo);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/uploads")
    public ResponseEntity<CustomerResponse> deleteMyPhoto() {
        CustomerResponse updated = service.deleteProfilePhoto();
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }
}
