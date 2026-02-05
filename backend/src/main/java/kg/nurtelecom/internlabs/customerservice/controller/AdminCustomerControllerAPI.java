package kg.nurtelecom.internlabs.customerservice.controller;

import jakarta.validation.Valid;
import kg.nurtelecom.internlabs.customerservice.payload.request.customer.AdminCustomerCreateRequest;
import kg.nurtelecom.internlabs.customerservice.payload.request.customer.AdminCustomerUpdateRequest;
import kg.nurtelecom.internlabs.customerservice.payload.response.CustomerResponse;
import kg.nurtelecom.internlabs.customerservice.service.CustomerService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/customers")
public class AdminCustomerControllerAPI {

    private final CustomerService service;

    public AdminCustomerControllerAPI(CustomerService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        try {
            List<CustomerResponse> customers = new ArrayList<>();
            service.findAll().forEach(customers::add);

            if (customers.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(customers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>((HttpHeaders) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CustomerResponse> createCustomer(
            @RequestPart(value = "photo", required = false) MultipartFile photo,
            @Valid @RequestPart("data") AdminCustomerCreateRequest request
    ) {
        try {
            CustomerResponse _customer = service.create(photo, request);
            return new ResponseEntity<>(_customer, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>((HttpHeaders) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @PathVariable("id") UUID id,
            @Valid @RequestBody AdminCustomerUpdateRequest request) {

        try {
            // Проверяем существование (согласно вашему стилю)
            CustomerResponse existing = service.findById(id);

            if (existing != null) {
                CustomerResponse updatedCustomer = service.update(id, request);
                return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>((HttpHeaders) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCustomer(@PathVariable("id") UUID id) {
        try {
            service.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}