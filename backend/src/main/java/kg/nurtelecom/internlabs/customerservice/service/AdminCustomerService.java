package kg.nurtelecom.internlabs.customerservice.service;

import kg.nurtelecom.internlabs.customerservice.payload.request.customer.AdminCustomerCreateRequest;
import kg.nurtelecom.internlabs.customerservice.payload.request.customer.AdminCustomerUpdateRequest;
import kg.nurtelecom.internlabs.customerservice.payload.response.CustomerResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface    AdminCustomerService {

    List<CustomerResponse> findAll();

    CustomerResponse findById(UUID id);

    CustomerResponse create(MultipartFile photo, AdminCustomerCreateRequest request);

    CustomerResponse update(UUID id, AdminCustomerUpdateRequest request);

    void deleteById(UUID id);
}
