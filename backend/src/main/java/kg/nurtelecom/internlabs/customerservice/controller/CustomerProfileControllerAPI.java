package kg.nurtelecom.internlabs.customerservice.controller;

import jakarta.validation.Valid;
import kg.nurtelecom.internlabs.customerservice.payload.request.customer.AdminCustomerUpdateRequest; // Используем тот же DTO или создаем свой ProfileUpdateRequest
import kg.nurtelecom.internlabs.customerservice.payload.response.CustomerResponse;
import kg.nurtelecom.internlabs.customerservice.service.CustomerService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/customer/avatar")
public class CustomerProfileControllerAPI {

    private final CustomerService service;

    public CustomerProfileControllerAPI(CustomerService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<CustomerResponse> getMyProfile() {
        try {
            CustomerResponse customer = service.getCurrentCustomer();

            if (customer != null) {
                return new ResponseEntity<>(customer, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>((HttpHeaders) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<CustomerResponse> updateMyProfile(
            @Valid @RequestBody AdminCustomerUpdateRequest request) {

        try {
            CustomerResponse updatedCustomer = service.updateCurrentProfile(request);
            return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>((HttpHeaders) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/uploads", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CustomerResponse> updateMyPhoto(
            @RequestPart("photo") MultipartFile photo) {
        try {
            if (photo == null || photo.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            CustomerResponse updatedCustomer = service.updateProfilePhoto(photo);
            return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>((HttpHeaders) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/uploads")
    public ResponseEntity<CustomerResponse> deleteMyPhoto() {
        try {
            // Метод сервиса удалит файл и вернет обновленный профиль (где imagePath = default)
            CustomerResponse updatedCustomer = service.deleteProfilePhoto();
            return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>((HttpHeaders) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}