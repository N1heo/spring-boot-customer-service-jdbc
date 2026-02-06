package kg.nurtelecom.internlabs.customerservice.security;

import kg.nurtelecom.internlabs.customerservice.enums.Role;
import kg.nurtelecom.internlabs.customerservice.model.Customer;
import kg.nurtelecom.internlabs.customerservice.payload.response.CustomerDetailResponse;
import kg.nurtelecom.internlabs.customerservice.payload.response.CustomerResponse;
import kg.nurtelecom.internlabs.customerservice.repository.AdminCustomerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerDetailService implements UserDetailsService {
    private final AdminCustomerRepository customerRepository;

    public CustomerDetailService(AdminCustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }




    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        CustomerDetailResponse customer = customerRepository.findByEmail(email);
        if (customer == null) {
            throw new UsernameNotFoundException("User with email " + email + " not found");
        }
        Role role = Role.valueOf(customer.role());
        return new UserPrinciple(customer.email(), customer.password(), role );
    }
}
