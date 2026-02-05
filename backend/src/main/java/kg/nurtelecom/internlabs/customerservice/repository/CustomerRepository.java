package kg.nurtelecom.internlabs.customerservice.repository;

import kg.nurtelecom.internlabs.customerservice.payload.request.customer.AdminCustomerCreateRequest;
import kg.nurtelecom.internlabs.customerservice.payload.request.customer.AdminCustomerUpdateRequest;
import kg.nurtelecom.internlabs.customerservice.payload.response.CustomerResponse;
import kg.nurtelecom.internlabs.customerservice.service.CustomerService;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Repository
public class CustomerRepository implements CustomerService {
    @Override
    public List<CustomerResponse> findAll() {
        return List.of();
    }

    @Override
    public CustomerResponse findById(UUID id) {
        return null;
    }

    @Override
    public CustomerResponse create(MultipartFile photo, AdminCustomerCreateRequest request) {
        return null;
    }

    @Override
    public CustomerResponse update(UUID id, AdminCustomerUpdateRequest request) {
        return null;
    }

    @Override
    public void deleteById(UUID id) {

    }

    @Override
    public CustomerResponse getCurrentCustomer() {
        return null;
    }

    @Override
    public CustomerResponse updateProfilePhoto(MultipartFile photo) {
        return null;
    }

    @Override
    public CustomerResponse updateCurrentProfile(AdminCustomerUpdateRequest request) {
        return null;
    }

    @Override
    public CustomerResponse deleteProfilePhoto() {
        return null;
    }
}
