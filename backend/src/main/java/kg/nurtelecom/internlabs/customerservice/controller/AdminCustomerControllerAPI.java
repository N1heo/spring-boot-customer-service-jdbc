package kg.nurtelecom.internlabs.customerservice.controller;

import jakarta.validation.Valid;
import kg.nurtelecom.internlabs.customerservice.payload.request.customer.AdminCustomerCreateRequest;
import kg.nurtelecom.internlabs.customerservice.payload.request.customer.AdminCustomerUpdateRequest;
import kg.nurtelecom.internlabs.customerservice.payload.response.CustomerResponse;
import kg.nurtelecom.internlabs.customerservice.service.AdminCustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/customers")
public class AdminCustomerControllerAPI {

    private final AdminCustomerService service;

    public AdminCustomerControllerAPI(AdminCustomerService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        List<CustomerResponse> customers = service.findAll();
        if (customers == null || customers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(customers);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getById(@PathVariable("id") UUID id) {
        CustomerResponse customer = service.findById(id);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customer);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CustomerResponse> createCustomer(
            @RequestPart(value = "photo", required = false) MultipartFile photo,
            @Valid @RequestPart("data") AdminCustomerCreateRequest request
    ) {
        CustomerResponse created = service.create(photo, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @PathVariable("id") UUID id,
            @Valid @RequestBody AdminCustomerUpdateRequest request
    ) {
        CustomerResponse updated = service.update(id, request);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable("id") UUID id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
