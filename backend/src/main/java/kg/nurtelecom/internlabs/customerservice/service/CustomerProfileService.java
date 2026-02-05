package kg.nurtelecom.internlabs.customerservice.service;

import kg.nurtelecom.internlabs.customerservice.payload.request.customer.AdminCustomerUpdateRequest;
import kg.nurtelecom.internlabs.customerservice.payload.response.CustomerResponse;
import org.springframework.web.multipart.MultipartFile;

public interface CustomerProfileService {

    CustomerResponse getCurrentCustomer();

    CustomerResponse updateCurrentProfile(AdminCustomerUpdateRequest request);

    CustomerResponse updateProfilePhoto(MultipartFile photo);

    CustomerResponse deleteProfilePhoto();
}
